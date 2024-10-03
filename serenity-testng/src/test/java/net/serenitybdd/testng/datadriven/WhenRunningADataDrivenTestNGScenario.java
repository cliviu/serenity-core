package net.serenitybdd.testng.datadriven;


import net.serenitybdd.testng.ParameterizedTestsOutcomeAggregator;
import net.serenitybdd.testng.SerenityTestNGStarter;
import net.serenitybdd.testng.datadriven.samples.MultipleDataDrivenTestScenariosWithValueSource;
import net.serenitybdd.testng.datadriven.samples.SampleDataDrivenIgnoredScenario;
import net.serenitybdd.testng.datadriven.samples.SampleSingleDataDrivenScenarioWithFailingAssumption;
import net.serenitybdd.testng.datadriven.samples.SimpleSuccessfulParameterizedTestSample;
import net.thucydides.core.steps.StepEventBus;
import net.thucydides.core.webdriver.WebDriverFactory;
import net.thucydides.model.domain.TestOutcome;
import net.thucydides.model.domain.TestResult;
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
        StepEventBus stepEventBus = StepEventBus.getEventBus();
        List<TestOutcome> currentOutcomes = new ParameterizedTestsOutcomeAggregator(stepEventBus.getBaseStepListener()).getTestOutcomesForAllParameterSets();
        assertThat(currentOutcomes.size(), is(5));
        assertThat(currentOutcomes.get(0).getTestSteps().size(), is(2));
        assertThat(currentOutcomes.get(1).getTestSteps().size(), is(2));
        assertThat(currentOutcomes.get(2).getTestSteps().size(), is(1));
        assertThat(currentOutcomes.get(3).getTestSteps().size(), is(1));
        assertThat(currentOutcomes.get(4).getTestSteps().size(), is(1));
        stepEventBus.getBaseStepListener().clearTestOutcomes();
        stepEventBus.clear();
    }

   /* @Test
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
    }*/

   /* @Test
    public void the_parameterized_data_method_returns_the_set_of_test_data_valueSource() throws Throwable {

        SerenityTestNGStarter.runTestClass(MultipleDataDrivenTestScenariosWithValueSource.class);
        Map<String, DataTable> dataTables = SerenityTestNGExecutionListener.getDataTables();
        assertThat(dataTables.keySet().size(), is(2));

        System.out.println("DataTables "+ dataTables.keySet());

        DataTable dataTableStrings = dataTables.get("net.serenitybdd.testng.datadriven.samples.MultipleDataDrivenTestScenariosWithValueSource.withValueSource");
        assertThat(dataTableStrings.getRows().size(), is(2));
        assertThat(dataTableStrings.getHeaders(),contains("arg0"));
        List<DataTableRow> rows = dataTableStrings.getRows();
        assertThat(rows.get(0).getStringValues().get(0), is("Hello"));
        assertThat(rows.get(1).getStringValues().get(0), is("JUnit"));

        DataTable dataTableIntegers = dataTables.get("net.serenitybdd.testng.datadriven.samples.MultipleDataDrivenTestScenariosWithValueSource.withValueSourceIntegers");
        assertThat(dataTableIntegers.getRows().size(), is(3));
        assertThat(dataTableIntegers.getHeaders(),contains("arg0"));
        List<DataTableRow> integersRows = dataTableIntegers.getRows();
        assertThat(integersRows.get(0).getStringValues().get(0), is("1"));
        assertThat(integersRows.get(1).getStringValues().get(0), is("2"));
        assertThat(integersRows.get(2).getStringValues().get(0), is("3"));
    }*/

    @Test
    public void a_data_driven_test_with_a_failing_assumption_should_be_ignored()  {
        SerenityTestNGStarter.runTestClass(SampleSingleDataDrivenScenarioWithFailingAssumption.class);
        StepEventBus stepEventBus = StepEventBus.getEventBus();
        List<TestOutcome> currentOutcomes = new ParameterizedTestsOutcomeAggregator(stepEventBus.getBaseStepListener()).getTestOutcomesForAllParameterSets();
        assertThat(currentOutcomes.size(), is(4));
        TestOutcome testOutcome1 = currentOutcomes.get(0);
        assertThat(testOutcome1.getResult(), is(TestResult.IGNORED));
    }

    @Test
    public void data_driven_tests_should_pass_even_if_no_steps_are_called() {
        SerenityTestNGStarter.runTestClass(SimpleSuccessfulParameterizedTestSample.class);
        StepEventBus stepEventBus = StepEventBus.getEventBus();
        List<TestOutcome> currentOutcomes = new ParameterizedTestsOutcomeAggregator(stepEventBus.getBaseStepListener()).getTestOutcomesForAllParameterSets();
        assertThat(currentOutcomes.size(), is(7));
        assertThat(currentOutcomes.get(0).getResult(), is(TestResult.SUCCESS));
        assertThat(currentOutcomes.get(1).getResult(), is(TestResult.SUCCESS));
        stepEventBus.getBaseStepListener().clearTestOutcomes();
        stepEventBus.clear();
    }

    @Test
    public void an_ignored_data_driven_test_should_have_result_status_as_ignored() {

        SerenityTestNGStarter.runTestClass(SampleDataDrivenIgnoredScenario.class);
        StepEventBus stepEventBus = StepEventBus.getEventBus();
        List<TestOutcome> currentOutcomes
            = new ParameterizedTestsOutcomeAggregator(stepEventBus.getBaseStepListener()).getTestOutcomesForAllParameterSets();
        assertThat(currentOutcomes.size(), is(1));
        assertThat(currentOutcomes.get(0).getResult(), is(TestResult.IGNORED));
        stepEventBus.getBaseStepListener().clearTestOutcomes();
        stepEventBus.clear();

    }
}
