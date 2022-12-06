package net.thucydides.core.steps.events;

public class TestFinishedEvent extends StepEventBusEventBase {

	private boolean inDataTest;


	public TestFinishedEvent(String scenarioId,boolean inDataDrivenTest) {
		super(scenarioId);
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
