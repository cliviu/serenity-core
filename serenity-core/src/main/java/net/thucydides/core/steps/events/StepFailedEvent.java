package net.thucydides.core.steps.events;

import net.thucydides.core.steps.StepEventBus;
import net.thucydides.core.steps.StepFailure;

public class StepFailedEvent
    extends StepEventBusEventBase {

	private final StepFailure stepFailure;

	public StepFailedEvent(StepEventBus eventBus, StepFailure stepFailure) {
		super(eventBus);
		this.stepFailure = stepFailure;
	}

	@Override
	public void play() {
		getStepEventBus().stepFailed(stepFailure);
	}

	public String toString() {
		return("EventBusEvent STEP_FAILED_EVENT " + stepFailure);
	}
}
