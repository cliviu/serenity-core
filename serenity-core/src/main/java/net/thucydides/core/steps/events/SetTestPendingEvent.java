package net.thucydides.core.steps.events;

import net.thucydides.core.steps.StepEventBus;

public class SetTestPendingEvent
    extends StepEventBusEventBase {

	public SetTestPendingEvent(StepEventBus eventBus) {
		super(eventBus);
	}

	@Override
	public void play() {
		getStepEventBus().testPending();
	}
}
