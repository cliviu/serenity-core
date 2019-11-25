package net.serenitybdd.testng;

import org.testng.*;
import org.testng.internal.IConfiguration;
import org.testng.internal.annotations.IAnnotationFinder;
import org.testng.xml.XmlTest;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class SerenityTestNGRunnerFactory implements ITestRunnerFactory {

    @Override
    public TestRunner newTestRunner(ISuite suite, XmlTest test, Collection<IInvokedMethodListener> listeners, List<IClassListener> classListeners) {
        return new SerenityTestNGRunner(new SerenityTestNGConfiguration(), suite, test, true, listeners, classListeners);
    }

}

