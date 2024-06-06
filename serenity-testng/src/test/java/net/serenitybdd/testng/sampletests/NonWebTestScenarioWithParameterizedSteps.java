package net.serenitybdd.testng.sampletests;

import net.serenitybdd.annotations.Steps;
import net.serenitybdd.testng.SerenityTestNG;
import net.thucydides.samples.SampleNonWebSteps;
import org.testng.annotations.Test;

@SerenityTestNG
public class NonWebTestScenarioWithParameterizedSteps {
        @Steps
        public SampleNonWebSteps steps;

        @Test
        public void scenario_with_parameterized_steps() {
            steps.stepWithAParameter("proportionOf");
            steps.stepWithTwoParameters("proportionOf", 2);
            steps.stepThatSucceeds();
            steps.stepThatIsIgnored();
            steps.stepThatIsPending();
            steps.anotherStepThatSucceeds();
        }

        @Test
        public void should_handle_nested_object_parameters() {
            steps.a_customized_step_with_object_parameters(new SampleNonWebSteps.CurrencyIn$(100));
        }

        @Test
        public void should_be_correct_customized_title_for_parameter_with_comma() {
            steps.a_customized_step_with_two_parameters("Joe, Smith", "20");
        }
}
