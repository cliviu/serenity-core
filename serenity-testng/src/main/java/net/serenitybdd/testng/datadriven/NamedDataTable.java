package net.serenitybdd.testng.datadriven;

import net.thucydides.model.domain.DataTable;

public class NamedDataTable {

	private final String dataTableName;
	private final DataTable dataTable;

	public NamedDataTable(String dataTableName, DataTable dataTable) {
		this.dataTableName = dataTableName;
		this.dataTable = dataTable;
	}

	public String getDataTableName() {
		return dataTableName;
	}

	public DataTable getDataTable() {
		return dataTable;
	}
}
