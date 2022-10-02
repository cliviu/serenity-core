package io.cucumber.core.plugin.events;

import net.thucydides.core.steps.StepEventBus;

public class TestFinishedEvent extends StepEventBusEventBase {

	private boolean inDataTest;

	public TestFinishedEvent(StepEventBus eventBus,boolean inDataDrivenTest) {
		super(eventBus);
		this.inDataTest = inDataDrivenTest;
	}


	@Override
	public void play() {
		getStepEventBus().testFinished(inDataTest);
	}

	public String toString() {
		return("EventBusEvent TEST_FINISHED_EVENT "  + " " + inDataTest);
	}
}
