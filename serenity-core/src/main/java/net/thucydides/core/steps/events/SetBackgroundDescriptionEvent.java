package net.thucydides.core.steps.events;

import net.thucydides.core.steps.StepEventBus;

public class SetBackgroundDescriptionEvent extends StepEventBusEventBase {

	private String backgroundDescription;

	public SetBackgroundDescriptionEvent(StepEventBus eventBus, String backgroundDescription) {
		super(eventBus);
		this.backgroundDescription =  backgroundDescription;
	}

	@Override
	public void play() {
		getStepEventBus().setBackgroundDescription(backgroundDescription);
	}
}
