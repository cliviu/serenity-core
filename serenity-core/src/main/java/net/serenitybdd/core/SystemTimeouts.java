package net.serenitybdd.core;

import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.core.pages.DefaultTimeouts;
import net.thucydides.core.ThucydidesSystemProperty;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;

import java.util.Optional;

public class SystemTimeouts {
    private EnvironmentVariables environmentVariables;

    public SystemTimeouts(EnvironmentVariables environmentVariables) {
        this.environmentVariables = environmentVariables;
    }

    public static SystemTimeouts forTheCurrentTest() {
        return new SystemTimeouts(SystemEnvironmentVariables.currentEnvironmentVariables());
    }

    public long getImplicitTimeout() {
        Optional<Long> configuredTimeout = webdriverCapabilitiesImplicitTimeoutFrom(environmentVariables);
        return configuredTimeout.orElse(DefaultTimeouts.DEFAULT_IMPLICIT_WAIT_TIMEOUT.toMillis());
    }

    public long getWaitForTimeout() {
        return ThucydidesSystemProperty.WEBDRIVER_WAIT_FOR_TIMEOUT.longFrom(environmentVariables, DefaultTimeouts.DEFAULT_WAIT_FOR_TIMEOUT.toMillis());
    }

    public static Optional<Long> webdriverCapabilitiesImplicitTimeoutFrom(EnvironmentVariables environmentVariables) {
        return EnvironmentSpecificConfiguration
                .from(environmentVariables)
                .getOptionalProperty("webdriver.capabilities.timeouts.implicit","webdriver.timeouts.implicitlywait")
                .map(Long::parseLong);
    }
}
