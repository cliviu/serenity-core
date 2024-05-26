package net.serenitybdd.testng;

import net.thucydides.model.configuration.SystemPropertiesConfiguration;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import org.testng.*;
import org.testng.internal.IConfiguration;
import org.testng.internal.annotations.IAnnotationFinder;
import org.testng.xml.XmlTest;

import java.io.File;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class SerenityTestNGRunnerFactory implements ITestRunnerFactory {

    public SerenityTestNGRunnerFactory(){
        System.out.println("SerenityTestNGRunnerFactory created" );
    }

    private static File getOutputDirectory() {
        SystemPropertiesConfiguration systemPropertiesConfiguration = new SystemPropertiesConfiguration(new SystemEnvironmentVariables());
        return systemPropertiesConfiguration.getOutputDirectory();
    }

    @Override
    public TestRunner newTestRunner(ISuite suite, XmlTest test,
                                    Collection<IInvokedMethodListener> listeners,
                                    List<IClassListener> classListeners) {
        System.out.println("Calling new TestRunner " + suite + " " + test.getName() );
        //return new SerenityTestNGRunner(new SerenityTestNGConfiguration(), suite, test, true, listeners, classListeners);
        //SerenityTestNGRunner(IConfiguration configuration, ISuite suite, XmlTest test, String outputDirectory,
                                // IAnnotationFinder finder, boolean skipFailedInvocationCounts,
                                // Collection<IInvokedMethodListener> invokedMethodListeners, List<IClassListener> classListeners,
                                // Comparator<ITestNGMethod> comparator, DataProviderHolder otherHolder, ISuiteRunnerListener suiteRunner) {
        return new SerenityTestNGRunner(new SerenityTestNGConfiguration(), suite, test, getOutputDirectory().getAbsolutePath(),null,
                                    true, listeners, classListeners,null,null,null);
    }

}

