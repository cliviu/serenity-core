package com.github.tomakehurst.wiremock.junit5;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Method;

/**
 * Lightweight test adapter that provides a junit5-style WireMockExtension backed by the
 * WireMockServer available on the classpath. This avoids requiring an external junit5-specific
 * WireMock artifact while giving tests the same lifecycle helpers.
 */
public class WireMockExtension implements BeforeAllCallback, AfterAllCallback {

    private final WireMockConfiguration options;
    private final boolean proxyMode;
    private WireMockServer server;
    private final String filesRoot;

    private WireMockExtension(WireMockConfiguration options, boolean proxyMode) {
        this(options, proxyMode, null);
    }

    private WireMockExtension(WireMockConfiguration options, boolean proxyMode, String filesRoot) {
        this.options = options == null ? WireMockConfiguration.options() : options;
        this.proxyMode = proxyMode;
        this.filesRoot = filesRoot;
    }

    public static Builder newInstance() {
        return new Builder();
    }

    public static class Builder {
        private WireMockConfiguration options;
        private boolean proxyMode = false;
        private String filesRoot;

        public Builder options(WireMockConfiguration options) {
            this.options = options;
            return this;
        }

        public Builder proxyMode(boolean proxyMode) {
            this.proxyMode = proxyMode;
            return this;
        }

        public Builder filesRoot(String filesRoot) {
            this.filesRoot = filesRoot;
            return this;
        }

        public WireMockExtension build() {
            return new WireMockExtension(options, proxyMode, filesRoot);
        }
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        start();
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        stop();
    }

    public synchronized void start() {
        if (server == null) {
            // Ensure proxy mode / files root are enabled when requested.
            try {
                if (proxyMode) {
                    try {
                        Method m = options.getClass().getMethod("enableBrowserProxying", boolean.class);
                        m.invoke(options, true);
                    } catch (NoSuchMethodException ignored) {
                        // older/newer WireMock versions may not expose this method; ignore if absent
                    }
                }
            } catch (Exception ignored) {
            }

            // Determine the effective files root: system property overrides the builder's filesRoot.
            // Leave the options untouched (and rely on whatever the caller already configured, e.g.
            // usingFilesUnderClasspath(...) or a relative withRootDirectory(...)) unless a files root
            // was explicitly requested here — an unconditional default would silently override a
            // caller's portable configuration and break on machines/CI where that path doesn't exist.
            String effectiveFilesRoot = System.getProperty("wiremock.files.root");
            if (effectiveFilesRoot == null || effectiveFilesRoot.isEmpty()) {
                effectiveFilesRoot = this.filesRoot;
            }

            if (effectiveFilesRoot != null && !effectiveFilesRoot.isEmpty()) {
                try {
                    Method withRoot = options.getClass().getMethod("withRootDirectory", String.class);
                    withRoot.invoke(options, effectiveFilesRoot);
                } catch (Exception ignored) {
                    // If the API isn't available, proceed — WireMock will fall back to classpath-based loading
                }
            }

            server = new WireMockServer(options);
            server.start();
            // Debugging output to help tests and CI verify mappings were loaded and the server is running
            try {
                System.out.println("[WireMockExtension] baseUrl: " + server.baseUrl());
                System.out.println("[WireMockExtension] isRunning: " + server.isRunning());
                try {
                    Object mappings = server.getStubMappings();
                    if (mappings != null) {
                        try {
                            // mappings is typically a List<StubMapping>
                            java.lang.reflect.Method sizeMethod = mappings.getClass().getMethod("size");
                            Object size = sizeMethod.invoke(mappings);
                            System.out.println("[WireMockExtension] mappings loaded: " + size);
                        } catch (NoSuchMethodException nsme) {
                            // fallback: just print the toString()
                            System.out.println("[WireMockExtension] mappings: " + mappings.toString());
                        }
                    }
                } catch (Throwable t) {
                    // ignore any introspection errors
                }
            } catch (Throwable t) {
                // never fail startup because of debug printing
            }
        } else if (!server.isRunning()) {
            server.start();
        }
    }

    public synchronized void stop() {
        if (server != null && server.isRunning()) {
            server.stop();
        }
    }

    public int getPort() {
        return server == null ? -1 : server.port();
    }

    public String baseUrl() {
        return server == null ? null : server.baseUrl();
    }

    public RuntimeInfo getRuntimeInfo() {
        return new RuntimeInfo(baseUrl());
    }

    public void startRecording(Object recordSpecOrBuilder) {
        if (server == null) {
            start();
        }
        if (recordSpecOrBuilder == null) {
            try {
                Method m = server.getClass().getMethod("startRecording");
                m.invoke(server);
                return;
            } catch (Exception ignored) {
            }
            return;
        }
        try {
            // If it's a builder with a build() method, call build() first
            Method build = null;
            try {
                build = recordSpecOrBuilder.getClass().getMethod("build");
            } catch (NoSuchMethodException ignored) {
            }
            Object recordSpec = recordSpecOrBuilder;
            if (build != null) {
                recordSpec = build.invoke(recordSpecOrBuilder);
            }
            // Invoke server.startRecording(recordSpec) via reflection to avoid compile-time type binding
            Method startRecording = null;
            for (Method m : server.getClass().getMethods()) {
                if (m.getName().equals("startRecording") && m.getParameterCount() == 1) {
                    startRecording = m;
                    break;
                }
            }
            if (startRecording != null) {
                startRecording.invoke(server, recordSpec);
            }
        } catch (Exception e) {
            // best-effort: swallow exceptions so tests can still run; failures will surface in test assertions
        }
    }

    public void stopRecording() {
        if (server == null) return;
        try {
            Method m = server.getClass().getMethod("stopRecording");
            m.invoke(server);
        } catch (Exception ignored) {
        }
    }

    // Minimal runtime info wrapper to satisfy callers of getRuntimeInfo().getHttpBaseUrl()
    public static class RuntimeInfo {
        private final String httpBaseUrl;

        public RuntimeInfo(String httpBaseUrl) {
            this.httpBaseUrl = httpBaseUrl;
        }

        public String getHttpBaseUrl() {
            return httpBaseUrl;
        }
    }
}

