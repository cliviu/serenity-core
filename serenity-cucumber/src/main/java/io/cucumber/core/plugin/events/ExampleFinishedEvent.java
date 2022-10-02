package io.cucumber.core.plugin.events;

import net.thucydides.core.steps.StepEventBus;

import java.util.Map;

public class ExampleFinishedEvent extends StepEventBusEventBase {



	public ExampleFinishedEvent(StepEventBus eventBus) {
		super(eventBus);
	}


	@Override
	public void play() {
		getStepEventBus().exampleFinished();
	}

	public String toString() {
		return("EventBusEvent EXAMPLE_FINISHED_EVENT ");
	}
}
