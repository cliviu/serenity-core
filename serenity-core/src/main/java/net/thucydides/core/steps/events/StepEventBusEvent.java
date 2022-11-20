package net.thucydides.core.steps.events;

public interface StepEventBusEvent {
	String getScenarioId();
	void play();
}
