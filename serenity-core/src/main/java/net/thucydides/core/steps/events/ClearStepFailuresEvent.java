package net.thucydides.core.steps.events;

import net.thucydides.core.steps.StepEventBus;

public class ClearStepFailuresEvent extends StepEventBusEventBase {

	public ClearStepFailuresEvent(StepEventBus eventBus) {
		super(eventBus);
	}

	@Override
	public void play() {
		getStepEventBus().clearStepFailures();
	}
}
