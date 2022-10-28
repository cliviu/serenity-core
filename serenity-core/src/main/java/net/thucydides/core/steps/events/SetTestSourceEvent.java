package net.thucydides.core.steps.events;

import net.thucydides.core.steps.StepEventBus;

public class SetTestSourceEvent extends StepEventBusEventBase {

	public SetTestSourceEvent(StepEventBus eventBus,String testSource) {
		super(eventBus);
		this.testSource =  testSource;
	}

	private String testSource;


	@Override
	public void play() {
		getStepEventBus().setTestSource(testSource);
		//TODO - to be fixed - TestOutcome.get
		// Base StepListener : newTestOutcome.setTestSource(StepEventBus.getEventBus().getTestSource());
		StepEventBus.getEventBus().setTestSource(testSource);

	}
}
