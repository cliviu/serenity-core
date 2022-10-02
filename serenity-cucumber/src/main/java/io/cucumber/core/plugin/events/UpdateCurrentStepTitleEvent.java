package io.cucumber.core.plugin.events;

import net.thucydides.core.steps.StepEventBus;

public class UpdateCurrentStepTitleEvent extends StepEventBusEventBase {

	private String stepTitle;

	public UpdateCurrentStepTitleEvent(StepEventBus eventBus,String stepTitle) {
		super(eventBus);
		this.stepTitle = stepTitle;
	}


	@Override
	public void play() {
		getStepEventBus().updateCurrentStepTitle(stepTitle);
	}
}
