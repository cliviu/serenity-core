package net.serenitybdd.testng.sampletests;

import net.serenitybdd.annotations.Steps;
import net.serenitybdd.testng.SerenityTestNG;
import net.thucydides.samples.SampleNonWebSteps;
import org.testng.annotations.Test;

@SerenityTestNG
public class SampleTestScenario {
	@Steps
	public SampleNonWebSteps steps;

	@Test
	public void a_scenario_with_a_failing_step() {
		steps.stepThatSucceeds();
		steps.stepThatFails();
		steps.anotherStepThatSucceeds();
		steps.stepThatSucceeds();
	}
}
