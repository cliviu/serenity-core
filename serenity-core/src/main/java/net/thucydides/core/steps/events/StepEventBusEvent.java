package net.thucydides.core.steps.events;

public interface StepEventBusEvent {
	public String getScenarioId();
	public void play();
}
