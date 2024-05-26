package net.serenitybdd.testng;

import org.testng.*;
import org.testng.internal.IConfiguration;
import org.testng.internal.annotations.IAnnotationFinder;
import org.testng.xml.XmlTest;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class SerenityTestNGRunner extends TestRunner {


    protected SerenityTestNGRunner(IConfiguration configuration, ISuite suite, XmlTest test, String outputDirectory, IAnnotationFinder finder, boolean skipFailedInvocationCounts, Collection<IInvokedMethodListener> invokedMethodListeners, List<IClassListener> classListeners, Comparator<ITestNGMethod> comparator, DataProviderHolder otherHolder, ISuiteRunnerListener suiteRunner) {
        super(configuration, suite, test, outputDirectory, finder, skipFailedInvocationCounts, invokedMethodListeners, classListeners, comparator, otherHolder, suiteRunner);
    }
}
