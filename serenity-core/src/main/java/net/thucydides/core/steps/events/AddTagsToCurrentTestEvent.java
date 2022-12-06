package net.thucydides.core.steps.events;

import net.thucydides.core.model.TestTag;

import java.util.List;

public class AddTagsToCurrentTestEvent extends StepEventBusEventBase {

	private List<TestTag> tags;


	public AddTagsToCurrentTestEvent( List<TestTag> tags) {
		this.tags = tags;
	}


	@Override
	public void play() {
		getStepEventBus().addTagsToCurrentTest(tags);
	}
}
