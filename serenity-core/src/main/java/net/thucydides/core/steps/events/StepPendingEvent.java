package net.thucydides.core.steps.events;

import net.thucydides.core.steps.StepEventBus;

public class StepPendingEvent
    extends StepEventBusEventBase {



	public StepPendingEvent(StepEventBus eventBus) {
		super(eventBus);
	}


	@Override
	public void play() {
		getStepEventBus().stepIgnored();
	}

	public String toString() {
		return("EventBusEvent STEP_IGNORED_EVENT ");
	}
}
