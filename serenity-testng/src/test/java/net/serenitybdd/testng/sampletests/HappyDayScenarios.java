package net.serenitybdd.testng.sampletests;

import net.serenitybdd.annotations.Steps;
import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.testng.SerenityTestNG;
import net.thucydides.samples.SampleNonWebSteps;
import org.testng.annotations.Test;

@SerenityTestNG
public class HappyDayScenarios {

	@Steps
	public SampleNonWebSteps steps;

	public HappyDayScenarios() {
		this.steps = Instrumented.instanceOf(SampleNonWebSteps.class).newInstance();
	}

	@Test(testName = "My happy day scenario")
	public void some_happy_day_scenario() throws Throwable {
		steps.stepThatSucceeds();
		steps.anotherStepThatSucceeds();
	}

	@Test
	public void some_edge_case_1() {
		steps.stepThatSucceeds();
		steps.anotherStepThatSucceeds();
		steps.stepThatIsPending();
	}

	@Test
	public void some_edge_case_2() {
		steps.stepThatSucceeds();
		steps.anotherStepThatSucceeds();
	}
}
