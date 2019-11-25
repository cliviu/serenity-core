package net.serenitybdd.testng;

import com.google.inject.Injector;
import net.serenitybdd.core.environment.WebDriverConfiguredEnvironment;
import net.serenitybdd.junit.runners.*;
import net.thucydides.core.annotations.ManagedWebDriverAnnotatedField;
import net.thucydides.core.annotations.TestCaseAnnotations;
import net.thucydides.core.batches.BatchManager;
import net.thucydides.core.batches.BatchManagerProvider;
import net.thucydides.core.guice.Injectors;
import net.thucydides.core.guice.webdriver.WebDriverModule;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.pages.Pages;
import net.thucydides.core.reports.AcceptanceTestReporter;
import net.thucydides.core.reports.ReportService;
import net.thucydides.core.steps.*;
import net.thucydides.core.tags.TagScanner;
import net.thucydides.core.webdriver.*;
import net.thucydides.junit.listeners.JUnitStepListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.*;
import org.testng.internal.IConfiguration;
import org.testng.internal.annotations.IAnnotationFinder;
import org.testng.xml.XmlTest;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static net.thucydides.core.ThucydidesSystemProperty.TEST_RETRY_COUNT;
import static net.thucydides.core.webdriver.ThucydidesWebDriverSupport.getPages;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class SerenityTestNGRunner extends TestRunner {

    private StepFactory stepFactory;
    private Pages pages;
    private DriverConfiguration configuration;

    protected SerenityTestNGRunner(IConfiguration configuration, ISuite suite, XmlTest test, String outputDirectory, IAnnotationFinder finder, boolean skipFailedInvocationCounts, Collection<IInvokedMethodListener> invokedMethodListeners, List<IClassListener> classListeners, Comparator<ITestNGMethod> comparator, Map<Class<? extends IDataProviderListener>, IDataProviderListener> dataProviderListeners) {
        super(configuration, suite, test, outputDirectory, finder, skipFailedInvocationCounts, invokedMethodListeners, classListeners, comparator, dataProviderListeners);
        initializeSerenityEnvironment();
    }

    public SerenityTestNGRunner(IConfiguration configuration, ISuite suite, XmlTest test, boolean skipFailedInvocationCounts, Collection<IInvokedMethodListener> invokedMethodListeners, List<IClassListener> classListeners, Comparator<ITestNGMethod> comparator) {
        super(configuration, suite, test, skipFailedInvocationCounts, invokedMethodListeners, classListeners, comparator);
        initializeSerenityEnvironment();
    }

    public SerenityTestNGRunner(IConfiguration configuration, ISuite suite, XmlTest test, boolean skipFailedInvocationCounts, Collection<IInvokedMethodListener> invokedMethodListeners, List<IClassListener> classListeners) {
        super(configuration, suite, test, skipFailedInvocationCounts, invokedMethodListeners, classListeners);
        initializeSerenityEnvironment();
    }

    private void initializeSerenityEnvironment() {
        Injector injector = Injectors.getInjector(new WebDriverModule());
        WebdriverManager webdriverManager = ThucydidesWebDriverSupport.getWebdriverManager();
        configuration = injector.getInstance(DriverConfiguration.class);
        try {
            setOutputDirectory(configuration.getOutputDirectory().getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        StepEventBus.getEventBus().registerListener(buildBaseStepListener(configuration.getOutputDirectory()));
        //.and().withPageFactory(pageFactory)

    }

    private void buildAndConfigureListeners() {
        initStepEventBus();
        /*if (webtestsAreSupported()) {
            ThucydidesWebDriverSupport.initialize(requestedDriver);
            WebDriver driver = ThucydidesWebDriverSupport.getWebdriverManager().getWebdriver();
            initPagesObjectUsing(driver);
            setStepListener(initListenersUsing(getPages()));
            initStepFactoryUsing(getPages());
        } else {*/
            initListeners();
            initStepFactory();
        //}
    }

    private BaseStepListener buildBaseStepListener(File outputDirectory) {
        /*if (pageFactory != null) {
            return Listeners.getBaseStepListener()
                    .withPages(pageFactory)
                    .and().withOutputDirectory(outputDirectory);
        } else {*/
            return Listeners.getBaseStepListener()
                    .withOutputDirectory(outputDirectory);
        //}
    }

    protected void initStepEventBus() {
        StepEventBus.getEventBus().clear();
    }

    protected JUnitStepListener initListeners() {
        return JUnitStepListener.withOutputDirectory(configuration.getOutputDirectory())
                //.and().withTestClass(getTestClass().getJavaClass())
                .and().build();
    }

    /*private boolean webtestsAreSupported() {
        return TestCaseAnnotations.supportsWebTests(this.getTestClass().getJavaClass());
    } */

    private void initStepFactoryUsing(final Pages pagesObject) {
        stepFactory = StepFactory.getFactory().usingPages(pagesObject);
    }

    private void initStepFactory() {
        stepFactory = StepFactory.getFactory();
    }


}
