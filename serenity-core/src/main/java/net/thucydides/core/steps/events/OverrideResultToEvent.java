package net.thucydides.core.steps.events;

import net.thucydides.core.model.Rule;
import net.thucydides.core.model.TestResult;
import net.thucydides.core.steps.StepEventBus;

public class OverrideResultToEvent
    extends StepEventBusEventBase {


	private TestResult testResult;

	public OverrideResultToEvent(StepEventBus eventBus, final TestResult testResult) {
		super(eventBus);
		this.testResult = testResult;
	}


	@Override
	public void play() {
		getStepEventBus().getBaseStepListener().overrideResultTo(testResult);
	}

	public String toString() {
		return("EventBusEvent OVERRIDE_RESULT_TO " + testResult);
	}
}
