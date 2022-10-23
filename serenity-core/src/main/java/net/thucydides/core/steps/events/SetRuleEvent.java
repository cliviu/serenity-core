package net.thucydides.core.steps.events;

import net.thucydides.core.model.Rule;
import net.thucydides.core.steps.StepEventBus;

public class SetRuleEvent extends StepEventBusEventBase {


	private Rule rule;

	public SetRuleEvent(StepEventBus eventBus, final Rule rule) {
		super(eventBus);
		this.rule = rule;
	}


	@Override
	public void play() {
		getStepEventBus().setRule(rule);
	}

	public String toString() {
		return("EventBusEvent SET_RULE " + rule);
	}
}
