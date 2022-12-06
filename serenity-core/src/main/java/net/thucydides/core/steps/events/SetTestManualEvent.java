package net.thucydides.core.steps.events;


public class SetTestManualEvent extends StepEventBusEventBase {
	@Override
	public void play() {
		getStepEventBus().testIsManual();
	}
}
