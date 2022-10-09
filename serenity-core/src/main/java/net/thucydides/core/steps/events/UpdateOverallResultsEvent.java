package net.thucydides.core.steps.events;

import net.thucydides.core.steps.StepEventBus;

public class UpdateOverallResultsEvent extends StepEventBusEventBase {



	public UpdateOverallResultsEvent(StepEventBus eventBus) {
		super(eventBus);

	}


	@Override
	public void play() {
		getStepEventBus().updateOverallResults();
	}

	public String toString() {
		return("EventBusEvent UPDATE_OVERALL_EVENTS " );
	}
}
