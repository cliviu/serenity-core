package net.thucydides.core.steps.events;

import net.thucydides.core.steps.StepEventBus;

public class SetBackgroundTitleEvent extends StepEventBusEventBase {

	private String backgroundTitle;

	public SetBackgroundTitleEvent(StepEventBus eventBus, String backgroundTitle) {
		super(eventBus);
		this.backgroundTitle =  backgroundTitle;
	}

	@Override
	public void play() {
		getStepEventBus().setBackgroundTitle(backgroundTitle);
	}
}
