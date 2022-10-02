package io.cucumber.core.plugin.events;

import net.thucydides.core.model.TestTag;
import net.thucydides.core.steps.StepEventBus;

import java.util.List;

public class AddTagsToCurrentTestEvent extends StepEventBusEventBase {

	private List<TestTag> tags;


	public AddTagsToCurrentTestEvent(StepEventBus eventBus, List<TestTag> tags) {
		super(eventBus);
		this.tags = tags;
	}


	@Override
	public void play() {
		getStepEventBus().addTagsToCurrentTest(tags);
	}
}
