package io.cucumber.core.plugin.events;

import net.thucydides.core.steps.StepEventBus;

public class AddDescriptionToCurrentTestEvent extends StepEventBusEventBase {

	private String description;


	public AddDescriptionToCurrentTestEvent(StepEventBus eventBus, String description) {
		super(eventBus);
		this.description =  description;
	}


	@Override
	public void play() {
		getStepEventBus().addDescriptionToCurrentTest(description);
	}
}
