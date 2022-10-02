package io.cucumber.core.plugin.events;

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
	}
}
