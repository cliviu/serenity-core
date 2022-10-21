package net.thucydides.core.steps.events;

import net.thucydides.core.model.DataTable;
import net.thucydides.core.steps.StepEventBus;

public class AddNewExamplesFromEvent extends StepEventBusEventBase {

	private DataTable dataTable;;

	public AddNewExamplesFromEvent(StepEventBus eventBus, DataTable dataTable) {
		super(eventBus);
		this.dataTable = dataTable;
	}

	@Override
	public void play() {
		getStepEventBus().addNewExamplesFrom(dataTable);
	}
}
