package net.thucydides.core.steps.events;

import net.thucydides.core.steps.StepEventBus;

public class AssumptionViolatedEvent
    extends StepEventBusEventBase {

	private final String message;

	public AssumptionViolatedEvent(StepEventBus eventBus, String message) {
		super(eventBus);
		this.message = message;
	}


	@Override
	public void play() {
		getStepEventBus().assumptionViolated(message);
	}
}
