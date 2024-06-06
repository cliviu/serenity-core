package net.serenitybdd.testng.sampletests;

import net.serenitybdd.annotations.Steps;
import net.serenitybdd.testng.SerenityTestNG;
import net.thucydides.samples.SampleNonWebSteps;
import org.testng.annotations.Test;

@SerenityTestNG
public class SampleTestScenarioWithIgnoredAndPendingSteps {
	@Steps
        public SampleNonWebSteps steps;

        @Test
        public void scenario_with_failing_step() {
            steps.stepThatSucceeds();
            steps.stepThatIsPending();
            steps.stepThatIsIgnored();
        }
}
