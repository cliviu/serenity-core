package net.thucydides.core.steps.events;

import net.thucydides.core.steps.StepEventBus;

public class TestFailedEvent extends StepEventBusEventBase {

	private final Throwable cause;

	public TestFailedEvent(StepEventBus eventBus,Throwable cause) {
		super(eventBus);
		this.cause =  cause;
	}


	@Override
	public void play() {
		getStepEventBus().testFailed(cause);
	}
}
