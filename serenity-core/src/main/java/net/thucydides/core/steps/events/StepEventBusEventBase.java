package net.thucydides.core.steps.events;

import net.thucydides.core.steps.StepEventBus;

public abstract class StepEventBusEventBase implements StepEventBusEvent {

	private StepEventBus stepEventBus;

	private String scenarioId;

	public StepEventBusEventBase(StepEventBus stepEventBus) {
		this(stepEventBus,"")
;	}

	public StepEventBusEventBase(StepEventBus stepEventBus,String scenarioId) {
		this.stepEventBus = stepEventBus;
		this.scenarioId = scenarioId;
	}

	public StepEventBus getStepEventBus() {
		return stepEventBus;
	}

	public void setStepEventBus(StepEventBus stepEventBus) {
		this.stepEventBus = stepEventBus;
	}

	public String getScenarioId (){
		return scenarioId;
	}

}
