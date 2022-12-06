package net.thucydides.core.steps.events;

import net.thucydides.core.model.DataTable;

public class AddNewExamplesFromEvent extends StepEventBusEventBase {

	private DataTable dataTable;

	public AddNewExamplesFromEvent(DataTable dataTable) {
		this.dataTable = dataTable;
	}

	@Override
	public void play() {
		getStepEventBus().addNewExamplesFrom(dataTable);
	}
}
