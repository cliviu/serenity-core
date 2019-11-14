package net.serenitybdd.testng;

import org.testng.*;
import org.testng.internal.IConfiguration;
import org.testng.internal.annotations.IAnnotationFinder;
import org.testng.xml.XmlTest;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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
