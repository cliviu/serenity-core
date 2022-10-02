package io.cucumber.core.plugin.events;

import net.thucydides.core.steps.ExecutedStepDescription;
import net.thucydides.core.steps.StepEventBus;

public class StepStartedEvent extends StepEventBusEventBase {

	ExecutedStepDescription stepDescription;

	public StepStartedEvent(StepEventBus eventBus, ExecutedStepDescription stepDescription) {
		super(eventBus);
		this.stepDescription = stepDescription;
	}

	@Override
	public void play() {
		getStepEventBus().stepStarted(stepDescription);
	}

	public String toString() {
		return("EventBusEvent STEP_STARTED_EVENT " + stepDescription);
	}
}
