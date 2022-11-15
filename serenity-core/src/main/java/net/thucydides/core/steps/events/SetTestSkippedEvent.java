package net.thucydides.core.steps.events;

import net.thucydides.core.steps.StepEventBus;

public class SetTestSkippedEvent
    extends StepEventBusEventBase {

	public SetTestSkippedEvent(StepEventBus eventBus) {
		super(eventBus);
	}

	@Override
	public void play() {
		getStepEventBus().testSkipped();
	}
}
