package net.thucydides.core.steps.events;

import net.thucydides.core.steps.StepEventBus;

public class UpdateExampleLineNumberEvent extends StepEventBusEventBase {

	private int exampleLine;

	public UpdateExampleLineNumberEvent(StepEventBus eventBus,int exampleLine) {
		super(eventBus);
		this.exampleLine = exampleLine;
	}


	@Override
	public void play() {
		getStepEventBus().updateExampleLineNumber(exampleLine);
	}

	public String toString() {
		return("EventBusEvent UPDATE_EXAMPLE_LINE_NUMBER " + exampleLine);
	}
}
