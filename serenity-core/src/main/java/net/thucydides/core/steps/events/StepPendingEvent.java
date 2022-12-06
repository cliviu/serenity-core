package net.thucydides.core.steps.events;

public class StepPendingEvent
    extends StepEventBusEventBase {


	@Override
	public void play() {
		getStepEventBus().stepPending();
	}

	public String toString() {
		return("EventBusEvent STEP_PENDING_EVENT ");
	}
}
