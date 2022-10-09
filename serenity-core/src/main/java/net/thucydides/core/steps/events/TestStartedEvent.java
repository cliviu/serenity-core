package net.thucydides.core.steps.events;

import net.thucydides.core.steps.StepEventBus;

public class TestStartedEvent extends StepEventBusEventBase {


	private String testName;

	private String id;

	public TestStartedEvent(StepEventBus eventBus, final String testName, final String id) {
		super(eventBus);
		this.testName =  testName;
		this.id = id;
	}


	@Override
	public void play() {
		getStepEventBus().testStarted(testName,id);
	}

	public String toString() {
		return("EventBusEvent TEST_STARTED_EVENT " + testName + " " + id);
	}
}
