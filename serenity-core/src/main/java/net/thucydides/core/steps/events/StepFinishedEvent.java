package net.thucydides.core.steps.events;

import net.thucydides.core.steps.StepEventBus;

public class StepFinishedEvent extends StepEventBusEventBase {



	public StepFinishedEvent(StepEventBus eventBus) {
		super(eventBus);
	}


	@Override
	public void play() {
		getStepEventBus().stepFinished();
	}

	public String toString() {
		return("EventBusEvent STEP_FINISHED_EVENT ");
	}
}
