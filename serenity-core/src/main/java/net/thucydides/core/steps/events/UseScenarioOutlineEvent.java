package net.thucydides.core.steps.events;

import net.thucydides.core.steps.StepEventBus;

public class UseScenarioOutlineEvent extends StepEventBusEventBase {

	private String scenarioOutline;

	public UseScenarioOutlineEvent(StepEventBus eventBus, String scenarioOutline) {
		super(eventBus);
		this.scenarioOutline = scenarioOutline;
	}

	@Override
	public void play() {
		getStepEventBus().useScenarioOutline(scenarioOutline);
	}
}
