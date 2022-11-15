package net.thucydides.core.steps.events;

import net.thucydides.core.steps.StepEventBus;

public class SetTestIgnoredEvent
    extends StepEventBusEventBase {

	public SetTestIgnoredEvent(StepEventBus eventBus) {
		super(eventBus);
	}

	@Override
	public void play() {
		getStepEventBus().testPending();
	}
}
