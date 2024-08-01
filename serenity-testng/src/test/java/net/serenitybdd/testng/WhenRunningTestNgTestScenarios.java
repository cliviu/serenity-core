package net.serenitybdd.testng;

import net.serenitybdd.testng.sampletests.*;
import net.thucydides.core.steps.StepEventBus;
import net.thucydides.core.webdriver.WebDriverFactory;
import net.thucydides.model.domain.TestOutcome;
import net.thucydides.model.domain.TestResult;
import net.thucydides.model.domain.TestStep;
import net.thucydides.model.domain.TestTag;
import net.thucydides.model.environment.MockEnvironmentVariables;
import net.thucydides.samples.SamplePassingNonWebScenarioWithManualTests;
import org.hamcrest.Matchers;
import org.mockito.MockitoAnnotations;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class WhenRunningTestNgTestScenarios extends AbstractTestNgStepRunnerTest {

    MockEnvironmentVariables environmentVariables;

    WebDriverFactory webDriverFactory;

    @BeforeMethod
    public void createATestableDriverFactory() {
        MockitoAnnotations.initMocks(this);
        environmentVariables = new MockEnvironmentVariables();
        webDriverFactory = new WebDriverFactory(environmentVariables);
        StepEventBus.getParallelEventBus().clear();
        StepEventBus.setNoCleanupForStickyBuses(true);
    }


    @Test
    public void should_run_top_level_tests() {

        runTestForClass(HappyDayScenarios.class);

        TestOutcome happyDayScenario = getTestOutcomeFor("some_happy_day_scenario");
        TestOutcome edgeCase1Scenario = getTestOutcomeFor("some_edge_case_1");
        TestOutcome edgeCase2Scenario = getTestOutcomeFor("some_edge_case_2");

        assertThat(happyDayScenario.getTitle(), is("Some happy day scenario"));
        assertThat(happyDayScenario.getResult(), is(TestResult.SUCCESS));

        assertThat(edgeCase1Scenario.getTitle(), is("Some edge case 1"));
        //TODO
        //assertThat(edgeCase1Scenario.getResult(), is(TestResult.PENDING));

        assertThat(edgeCase2Scenario.getTitle(), is("Some edge case 2"));
        assertThat(edgeCase2Scenario.getResult(), is(TestResult.SUCCESS));
    }

    @Test
    public void should_record_each_step_of_a_scenario() {

        runTestForClass(HappyDayScenarios.class);

        TestOutcome happyDayScenario = getTestOutcomeFor("some_happy_day_scenario");

        assertThat(happyDayScenario.getTitle(), is("Some happy day scenario"));
        assertThat(happyDayScenario.getTestSteps(), hasSize(2));
        assertThat(happyDayScenario.getTestSteps().get(0).getDescription(), is("Step that succeeds"));
        assertThat(happyDayScenario.getTestSteps().get(1).getDescription(), is("Another step that succeeds"));

    }

    @Test
    public void should_not_store_screenshots_for_non_web_tests() {

        runTestForClass(HappyDayScenarios.class);

        TestOutcome happyDayScenario = getTestOutcomeFor("some_happy_day_scenario");

        assertThat(happyDayScenario.getScreenshots(), hasSize(0));
    }


    @Test
    //@Ignore("clarify if junit assumption has to be supported in JUnit test")
    public void tests_with_failing_assumptions_should_be_aborted() {

        runTestForClass(HasAFailingAssumptionInATest.class);

        TestOutcome testWithFailingAssumption = getTestOutcomeFor("test_with_failing_assumption");

        assertThat(testWithFailingAssumption.getResult(), is(TestResult.ABORTED));
        assertThat(testWithFailingAssumption.getTestFailureCause().asException(), Matchers.instanceOf(SkipException.class));
    }

    @Test
    public void tests_marked_as_manual_should_be_flagged_as_manual_tests() {

        runTestForClass(SamplePassingNonWebScenarioWithManualTests.class);

        TestOutcome manualTestResult = getTestOutcomeFor("a_manual_test");

        assertThat(manualTestResult.getResult(), is(TestResult.PENDING));
        assertThat(manualTestResult.isManual(), equalTo(true));
        assertThat(manualTestResult.getTags(), hasItem(TestTag.withName("manual").andType("tag")));
    }

    @Test
    public void tests_marked_as_manual_should_be_given_the_requested_result_if_specified() {

        runTestForClass(SamplePassingNonWebScenarioWithManualTests.class);

        TestOutcome failingManualTestResult = getTestOutcomeFor("a_failing_manual_test");

        assertThat(failingManualTestResult.isManual(), equalTo(true));
        assertThat(failingManualTestResult.getResult(), is(TestResult.FAILURE));
    }



    @Test
    public void tests_with_no_steps_should_be_marked_as_successful() {

        runTestForClass(ATestWithNoSteps.class);

        TestOutcome testOutcome = getTestOutcomeFor("test_with_no_steps");

        assertThat(testOutcome.getResult(), is(TestResult.SUCCESS));
    }


    //TODO ignored tests are completely ignored
    /*@Test
    public void disabled_tests_should_be_skipped() {
        runTestForClass(ADisabledTest.class);
        TestOutcome testOutcome = getTestOutcomeFor("a_disabled_test");
        assertThat(testOutcome.getResult(), is(TestResult.IGNORED));
    }*/


    //@ExtendWith(SerenityJUnit5Extension.class)


    @Test
    public void pending_tests_should_be_flagged_as_pending() {

        runTestForClass(APendingTest.class);
        TestOutcome previousTest = getTestOutcomeFor("previous_test");
        TestOutcome pendingTest = getTestOutcomeFor("a_pending_test");
        TestOutcome followingTest = getTestOutcomeFor("following_test");

        assertThat(previousTest.getResult(), is(TestResult.SUCCESS));
        assertThat(pendingTest.getResult(), is(TestResult.PENDING));
        assertThat(followingTest.getResult(), is(TestResult.SUCCESS));
    }



    @Test
    @Ignore("phase-2")
    public void tests_should_be_run_after_an_assertion_error() {
        SerenityTestNGStarter.runTestClass(AScenarioWithAnAssertionError.class);
        TestOutcome failingTest = getTestOutcomeFor("a_scenario_with_an_assertion_error");
        TestOutcome nextTest = getTestOutcomeFor("a_test_after_the_assertion_error");

        assertThat(failingTest.getResult(), is(TestResult.FAILURE));
        assertThat(nextTest.getResult(), is(TestResult.SUCCESS));
    }

    @Test
    @Ignore("phase-2")
    public void failing_assertions_with_no_steps_should_still_record_the_error() {

        SerenityTestNGStarter.runTestClass(AScenarioWithAnAssertionError.class);
        TestOutcome outcome = getTestOutcomeFor("scenario_with_an_assertion_error");

        assertThat(outcome.getResult(), is(TestResult.FAILURE));
        assertThat(outcome.getTestFailureMessage(), is("Oh bother!"));
    }

    /*@ParameterizedTest
    @ValueSource(strings = {
            "a_scenario_with_an_assertion_error_in_a_step",
            "a_scenario_with_a_junit_4_assertion_error_in_a_step",
            "a_scenario_with_a_junit_5_assertion_error_in_a_step"
    })
    public void failing_assertions_in_a_step_should_record_the_error(String failingMethod) {

        TestLauncher.runTestForClass(AScenarioWithAnAssertionError.class);
        TestOutcome outcome = getTestOutcomeFor(failingMethod);

        assertThat(outcome.getResult(), is(TestResult.FAILURE));
        assertThat(outcome.getTestFailureMessage(), not(isEmptyString()));
    }*/



    @Test
    public void the_test_runner_skips_any_tests_after_a_failure() {

        SerenityTestNGStarter.runTestClass(SampleTestScenario.class);
        TestOutcome outcome = getTestOutcomeFor("a_scenario_with_a_failing_step");

        List<TestStep> steps = outcome.getTestSteps();
        System.out.println("Outcome " + outcome.getTitle());
        System.out.println("Steps " + steps);
        assertThat(steps.size(), is(4));
        assertThat(steps.get(0).isSuccessful(), is(true));
        assertThat(steps.get(1).isFailure(), is(true));
        assertThat(steps.get(2).isSkipped(), is(true));
        assertThat(steps.get(3).isSkipped(), is(true));
    }


    @Test
    public void the_test_runner_distinguishes_between_ignored_skipped_and_pending_steps() {
        SerenityTestNGStarter.runTestClass(SampleTestScenarioWithIgnoredAndPendingSteps.class);
        TestOutcome testOutcome = getTestOutcomeFor("scenario_with_failing_step");

        TestStep succeeds = testOutcome.getTestSteps().get(0);
        TestStep pending = testOutcome.getTestSteps().get(1);
        TestStep ignored = testOutcome.getTestSteps().get(2);

        assertThat(succeeds.getResult(), is(TestResult.SUCCESS));
        assertThat(ignored.getResult(), is(TestResult.IGNORED));
        assertThat(pending.getResult(), is(TestResult.PENDING));
    }


    @Test
    public void the_test_runner_records_each_step_with_a_nice_name_when_steps_have_parameters() {

        SerenityTestNGStarter.runTestClass(NonWebTestScenarioWithParameterizedSteps.class);
        TestOutcome testOutcome = getTestOutcomeFor("scenario_with_parameterized_steps");

        TestStep firstStep = testOutcome.getTestSteps().get(0);

        assertThat(firstStep.getDescription(), containsString("Step with a parameter:"));
        assertThat(firstStep.getDescription(), containsString("proportionOf"));
    }

    @Test
    public void should_report_nested_class_parameters_correctly() {

        runTestForClass(NonWebTestScenarioWithParameterizedSteps.class);
        TestOutcome testOutcome = getTestOutcomeFor("should_handle_nested_object_parameters");

        TestStep firstStep = testOutcome.getTestSteps().get(0);
        assertThat(firstStep.getDescription(), is("a step with an object parameter called $100.00"));
    }

    @Test
    public void should_report_correctly_customized_title_for_parameter_with_comma() {

        runTestForClass(NonWebTestScenarioWithParameterizedSteps.class);
        TestOutcome testOutcome = getTestOutcomeFor("should_be_correct_customized_title_for_parameter_with_comma");
        TestStep firstStep = testOutcome.getTestSteps().get(0);
        assertThat(firstStep.getDescription(), is("a step with two object parameters called 'Joe, Smith' and '20'"));
    }

    @Test
    public void the_test_runner_records_each_step_with_a_nice_name_when_steps_have_multiple_parameters() {

        runTestForClass(NonWebTestScenarioWithParameterizedSteps.class);

        TestOutcome testOutcome = getTestOutcomeFor("scenario_with_parameterized_steps");
        TestStep secondStep = testOutcome.getTestSteps().get(1);

        assertThat(secondStep.getDescription(), containsString("Step with two parameters"));
        assertThat(secondStep.getDescription(), containsString("proportionOf, 2"));
    }

    /*@ExtendWith(SerenityJUnit5Extension.class)
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    static final class JUnit5GeneratedExample {

        @Test
        void sample_test() {
        }

        @Nested
        class Nested_Test_Cases {

            @Test
            void sample_nested_test() {
            }
        }
    }*/

    /*@Test
    public void the_test_runner_records_the_name_of_the_test_scenario() {

        SerenityTestNGStarter.runTestClass(JUnit5GeneratedExample.class);

        TestOutcome topLevelTestOutcome = getTestOutcomeFor("sample_test");
        assertThat(topLevelTestOutcome.getName(), is("sample test"));

        TestOutcome nestedTestOutcome = getTestOutcomeFor("sample_nested_test");
        assertThat(nestedTestOutcome.getName(), is("sample nested test"));
    }*/
}
