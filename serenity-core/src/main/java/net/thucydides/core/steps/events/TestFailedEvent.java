package net.thucydides.core.steps.events;

import net.thucydides.core.steps.StepEventBus;

public class TestFailedEvent extends StepEventBusEventBase {



	public TestFailedEvent(StepEventBus eventBus) {
		super(eventBus);
	}


	@Override
	public void play() {
		getStepEventBus().exampleFinished();
	}
}
