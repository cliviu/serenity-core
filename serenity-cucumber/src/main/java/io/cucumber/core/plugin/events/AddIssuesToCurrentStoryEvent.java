package io.cucumber.core.plugin.events;

import net.thucydides.core.model.TestTag;
import net.thucydides.core.steps.StepEventBus;

import java.util.List;

public class AddIssuesToCurrentStoryEvent extends StepEventBusEventBase {

	private List<String> issues;


	public AddIssuesToCurrentStoryEvent(StepEventBus eventBus, List<String> issues) {
		super(eventBus);
		this.issues = issues;
	}


	@Override
	public void play() {
		getStepEventBus().addIssuesToCurrentStory(issues);
	}
}
