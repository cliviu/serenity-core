package net.serenitybdd.testng.datadriven;


import net.serenitybdd.testng.ParameterizedTestsOutcomeAggregator;
import net.serenitybdd.testng.SerenityTestNGStarter;
import net.serenitybdd.testng.datadriven.samples.MultipleDataDrivenTestScenariosWithValueSource;
import net.serenitybdd.testng.sampletests.APendingTest;
import net.thucydides.core.steps.StepEventBus;
import net.thucydides.core.webdriver.WebDriverFactory;
import net.thucydides.model.domain.TestOutcome;
import net.thucydides.model.environment.MockEnvironmentVariables;
import net.thucydides.model.webdriver.Configuration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import spock.lang.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class WhenRunningADataDrivenTestNGScenario {

    MockEnvironmentVariables environmentVariables;

    WebDriverFactory webDriverFactory;

    @Before
    public void createATestableDriverFactory() {
        MockitoAnnotations.initMocks(this);
        environmentVariables = new MockEnvironmentVariables();
        webDriverFactory = new WebDriverFactory(environmentVariables);
    }

    @TempDir
    Path anotherTempDir;

    Configuration configuration;


    @Test
    public void the_test_runner_records_the_steps_as_they_are_executed() {


        SerenityTestNGStarter.runTestClass(MultipleDataDrivenTestScenariosWithValueSource.class);

        for(int i = 1; i<= 2; i++) {
            String eventBusName = String.format("[engine:junit-jupiter]/[class:net.serenitybdd.junit5.datadriven.samples.MultipleDataDrivenTestScenariosWithValueSource]/[test-template:withValueSource(java.lang.String)]/[test-template-invocation:#%s]",i);
            //StepEventBus stepEventBus = StepEventBus.eventBusFor(eventBusName);
            StepEventBus stepEventBus = StepEventBus.getEventBus();
            List<TestOutcome> currentOutcomes = new ParameterizedTestsOutcomeAggregator(stepEventBus.getBaseStepListener()).getTestOutcomesForAllParameterSets();
            assertThat(currentOutcomes.size(), is(1));
            assertThat(currentOutcomes.get(0).getTestSteps().size(), is(2));
            StepEventBus.forceClearEventBusFor(eventBusName);
        }

        for(int i = 1; i<= 3; i++) {
            String eventBusName = String.format("[engine:junit-jupiter]/[class:net.serenitybdd.junit5.datadriven.samples.MultipleDataDrivenTestScenariosWithValueSource]/[test-template:withValueSourceIntegers(int)]/[test-template-invocation:#%s]",i);
            //StepEventBus stepEventBus = StepEventBus.eventBusFor(eventBusName);
            StepEventBus stepEventBus = StepEventBus.getEventBus();
            List<TestOutcome> currentOutcomes = new ParameterizedTestsOutcomeAggregator(stepEventBus.getBaseStepListener()).getTestOutcomesForAllParameterSets();
            assertThat(currentOutcomes.size(), is(1));
            assertThat(currentOutcomes.get(0).getTestSteps().size(), is(2));
            StepEventBus.forceClearEventBusFor(eventBusName);
        }

        /*ConcurrentMap<Object, StepEventBus> stickyEventBuses = StepEventBus.getStickyEventBuses();
        System.out.println("Sticky buses size " + stickyEventBuses.size());
        stickyEventBuses.forEach((k,v)->System.out.println(k + "--" + v) );

        assertTrue(StepEventBus.getStickyEventBuses().size()==0);*/
    }

    @Test
    public void a_data_driven_test_driver_should_run_one_test_per_row_of_data() {
        SerenityTestNGStarter.runTestClass(MultipleDataDrivenTestScenariosWithValueSource.class);
        for(int i = 1; i <= 2; i++) {
            String eventBusName = String.format("[engine:junit-jupiter]/[class:net.serenitybdd.junit5.datadriven.samples.MultipleDataDrivenTestScenariosWithValueSource]/[test-template:withValueSource(java.lang.String)]/[test-template-invocation:#%s]",i);
            //StepEventBus stepEventBus = StepEventBus.eventBusFor(eventBusName);
            StepEventBus stepEventBus = StepEventBus.getEventBus();
            List<TestOutcome> currentOutcomes = new ParameterizedTestsOutcomeAggregator(stepEventBus.getBaseStepListener()).getTestOutcomesForAllParameterSets();
            assertThat(currentOutcomes.size(), is(1));
            StepEventBus.forceClearEventBusFor(eventBusName);
        }
        for(int i = 1; i<= 3; i++) {
            String eventBusName = String.format("[engine:junit-jupiter]/[class:net.serenitybdd.junit5.datadriven.samples.MultipleDataDrivenTestScenariosWithValueSource]/[test-template:withValueSourceIntegers(int)]/[test-template-invocation:#%s]",i);
            //StepEventBus stepEventBus = StepEventBus.eventBusFor(eventBusName);
            StepEventBus stepEventBus = StepEventBus.getEventBus();
            List<TestOutcome> currentOutcomes = new ParameterizedTestsOutcomeAggregator(stepEventBus.getBaseStepListener()).getTestOutcomesForAllParameterSets();
            assertThat(currentOutcomes.size(), is(1));
            StepEventBus.forceClearEventBusFor(eventBusName);
        }
    }

}
