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
import net.thucydides.core.steps.PageObjectDependencyInjector;
import net.thucydides.core.steps.StepEventBus;
import net.thucydides.core.steps.StepFactory;
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
import java.util.*;

import static net.thucydides.core.ThucydidesSystemProperty.TEST_RETRY_COUNT;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class SerenityTestNGRunner extends TestRunner {

    protected SerenityTestNGRunner(IConfiguration configuration, ISuite suite, XmlTest test, String outputDirectory, IAnnotationFinder finder, boolean skipFailedInvocationCounts, Collection<IInvokedMethodListener> invokedMethodListeners, List<IClassListener> classListeners, Comparator<ITestNGMethod> comparator, Map<Class<? extends IDataProviderListener>, IDataProviderListener> dataProviderListeners) {
        super(configuration, suite, test, outputDirectory, finder, skipFailedInvocationCounts, invokedMethodListeners, classListeners, comparator, dataProviderListeners);
    }

    public SerenityTestNGRunner(IConfiguration configuration, ISuite suite, XmlTest test, boolean skipFailedInvocationCounts, Collection<IInvokedMethodListener> invokedMethodListeners, List<IClassListener> classListeners, Comparator<ITestNGMethod> comparator) {
        super(configuration, suite, test, skipFailedInvocationCounts, invokedMethodListeners, classListeners, comparator);
    }

    public SerenityTestNGRunner(IConfiguration configuration, ISuite suite, XmlTest test, boolean skipFailedInvocationCounts, Collection<IInvokedMethodListener> invokedMethodListeners, List<IClassListener> classListeners) {
        super(configuration, suite, test, skipFailedInvocationCounts, invokedMethodListeners, classListeners);
    }


}
