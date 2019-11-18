package net.serenitybdd.testng;

import com.google.inject.Key;
import net.serenitybdd.junit.runners.ParameterizedJUnitStepListener;
import net.thucydides.core.model.DataTable;
import net.thucydides.core.pages.Pages;
import net.thucydides.core.steps.BaseStepListener;
import net.thucydides.core.steps.Listeners;
import net.thucydides.core.steps.StepListener;
/*import net.thucydides.junit.guice.JUnitInjectors;
import net.thucydides.junit.listeners.JUnitStepListener;
import net.thucydides.junit.listeners.JUnitStepListenerBuilder;
import net.thucydides.junit.listeners.TestCounter;*/

import java.io.File;

public class TestNGStepListenerBuilder {
    private final File outputDirectory;
    private final Pages pageFactory;
    private final int parameterSetNumber;
    private final DataTable parametersTable;
    private final Class<?> testClass;

    public TestNGStepListenerBuilder(File outputDirectory) {
        this(outputDirectory, null, -1, null);
    }

    public TestNGStepListenerBuilder(File outputDirectory,
                                     Pages pageFactory) {
        this(outputDirectory, pageFactory, -1, null);
    }

    public TestNGStepListenerBuilder(File outputDirectory,
                                     Pages pageFactory,
                                     int parameterSetNumber) {
        this(outputDirectory, pageFactory, parameterSetNumber, null);
    }

    public TestNGStepListenerBuilder(File outputDirectory,
                                     Pages pageFactory,
                                     int parameterSetNumber,
                                     DataTable parametersTable) {
        this(outputDirectory, pageFactory, parameterSetNumber, parametersTable, null);
    }

    public TestNGStepListenerBuilder(File outputDirectory,
                                     Pages pageFactory,
                                     int parameterSetNumber,
                                     DataTable parametersTable,
                                     Class<?> testClass) {
        this.outputDirectory = outputDirectory;
        this.pageFactory = pageFactory;
        this.parameterSetNumber = parameterSetNumber;
        this.parametersTable = parametersTable;
        this.testClass = testClass;
    }

    public TestNGStepListenerBuilder and() {
        return this;
    }

    public TestNGStepListenerBuilder withPageFactory(Pages pageFactory) {
        return new TestNGStepListenerBuilder(outputDirectory, pageFactory);
    }

    public TestNGStepListenerBuilder withParameterSetNumber(int parameterSetNumber) {
        return new TestNGStepListenerBuilder(outputDirectory, pageFactory, parameterSetNumber);
    }

    public TestNGStepListenerBuilder withParametersTable(DataTable parametersTable) {
        return new TestNGStepListenerBuilder(outputDirectory, pageFactory, parameterSetNumber, parametersTable);
    }

    public TestNGStepListenerBuilder withTestClass(Class<?> testClass) {
        return new TestNGStepListenerBuilder(outputDirectory, pageFactory, parameterSetNumber, parametersTable, testClass);
    }

    /*public TestNGStepListenerBuilder build() {
        if (parameterSetNumber >= 0) {
            return newParameterizedJUnitStepListener();
        } else {
            return newStandardJunitStepListener();
        }
    } */

    private BaseStepListener buildBaseStepListener() {
        if (pageFactory != null) {
            return Listeners.getBaseStepListener()
                    .withPages(pageFactory)
                    .and().withOutputDirectory(outputDirectory);
        } else {
            return Listeners.getBaseStepListener()
                    .withOutputDirectory(outputDirectory);
        }
    }

    /*private TestNGStepListenerBuilder newParameterizedJUnitStepListener() {
        return new ParameterizedJUnitStepListener(parameterSetNumber,
                parametersTable,
                testClass,
                buildBaseStepListener(),
                Listeners.getLoggingListener(),
                newTestCountListener());
//                Listeners.getStatisticsListener());
    } */

   /* private StepListener newTestCountListener() {
        return JUnitInjectors.getInjector().getInstance(Key.get(StepListener.class, TestCounter.class));
    }*/

    /*private TestNGStepListenerBuilder newStandardJunitStepListener() {
        return new TestNGStepListenerBuilder(testClass,
                buildBaseStepListener(),
                Listeners.getLoggingListener(),
                newTestCountListener());
//                Listeners.getStatisticsListener());
    } */

}
