package net.thucydides.core.steps.events;

public class TestStartedEvent extends StepEventBusEventBase {


	private String testName;

	private String id;

	public TestStartedEvent(String scenarioId, final String testName, final String id) {
		super(scenarioId);
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
