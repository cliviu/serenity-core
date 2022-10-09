package net.thucydides.core.steps.events;

import net.thucydides.core.steps.StepEventBus;

public abstract class StepEventBusEventBase implements StepEventBusEvent {

	private StepEventBus stepEventBus;

	public StepEventBusEventBase(StepEventBus stepEventBus) {
		this.stepEventBus = stepEventBus;
	}

	public StepEventBus getStepEventBus() {
		return stepEventBus;
	}


}
