package io.cucumber.core.plugin.events;

import net.thucydides.core.steps.StepEventBus;

import java.util.List;

public class AddIssuesToCurrentTestEvent extends StepEventBusEventBase {

	private List<String> issues;


	public AddIssuesToCurrentTestEvent(StepEventBus eventBus, List<String> issues) {
		super(eventBus);
		this.issues = issues;
	}


	@Override
	public void play() {
		getStepEventBus().addIssuesToCurrentStory(issues);
	}
}
