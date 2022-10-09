package net.thucydides.core.steps.events;

import net.thucydides.core.steps.StepEventBus;

public class StepIgnoredEvent extends StepEventBusEventBase {



	public StepIgnoredEvent(StepEventBus eventBus) {
		super(eventBus);
	}


	@Override
	public void play() {
		getStepEventBus().stepFinished();
	}

	public String toString() {
		return("EventBusEvent STEP_IGNORED_EVENT ");
	}
}
