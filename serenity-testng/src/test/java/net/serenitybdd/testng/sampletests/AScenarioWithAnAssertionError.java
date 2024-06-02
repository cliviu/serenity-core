package net.serenitybdd.testng.sampletests;

import net.serenitybdd.annotations.Steps;
import net.serenitybdd.testng.SerenityTestNG;
import net.thucydides.samples.SampleNonWebSteps;
import org.testng.annotations.Test;


@SerenityTestNG
public class AScenarioWithAnAssertionError {

        @Steps
        public SampleNonWebSteps steps;

        @Test
        public void a_scenario_with_an_assertion_error() {
            steps.stepThatSucceeds();
            steps.stepThatIsIgnored();
            steps.anotherStepThatSucceeds();
            throw new AssertionError("Oh bother!");
        }

        @Test
        public void a_scenario_with_an_assertion_error_in_a_step() {
            steps.stepThatFails();
        }

        /*@Test
        public void a_scenario_with_a_junit_4_assertion_error_in_a_step() {
            steps.stepWithAFailingJUnit4Assertion();
        }

        @Test
        public void a_scenario_with_a_junit_5_assertion_error_in_a_step() {
            steps.stepWithAFailingJUnit5Assertion();
        }*/

        @Test
        public void a_test_after_the_assertion_error() {
        }
}
