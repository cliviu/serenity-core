package net.thucydides.core.steps.events;

import net.thucydides.core.steps.StepEventBus;

public class SetTestManualEvent extends StepEventBusEventBase {

	public SetTestManualEvent(StepEventBus eventBus) {
		super(eventBus);
	}

	@Override
	public void play() {
		getStepEventBus().testIsManual();
	}
}