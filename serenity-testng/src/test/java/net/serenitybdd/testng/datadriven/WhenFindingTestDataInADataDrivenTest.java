package net.serenitybdd.testng.datadriven;


import net.serenitybdd.testng.datadriven.samples.MultipleDataDrivenTestScenariosWithValueSource;
import net.thucydides.model.domain.DataTable;
import net.thucydides.model.domain.DataTableRow;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

public class WhenFindingTestDataInADataDrivenTest
{


    @Test
    public void the_parameterized_data_method_returns_the_set_of_test_data_valueSource() throws Throwable {

        Map<String,DataTable> testDataTable = TestNGDataDrivenAnnotations.forClass(MultipleDataDrivenTestScenariosWithValueSource.class).getParameterTables();
        assertThat(testDataTable.keySet().size(), is(2));

        DataTable dataTableStrings = testDataTable.get("net.serenitybdd.junit5.datadriven.samples.MultipleDataDrivenTestScenariosWithValueSource.withValueSource");
        assertThat(dataTableStrings.getRows().size(), is(2));
        assertThat(dataTableStrings.getHeaders(),contains("arg0"));
        List<DataTableRow> rows = dataTableStrings.getRows();
        assertThat(rows.get(0).getStringValues().get(0), is("Hello"));
        assertThat(rows.get(1).getStringValues().get(0), is("JUnit"));

        DataTable dataTableIntegers = testDataTable.get("net.serenitybdd.junit5.datadriven.samples.MultipleDataDrivenTestScenariosWithValueSource.withValueSourceIntegers");
        assertThat(dataTableIntegers.getRows().size(), is(3));
        assertThat(dataTableIntegers.getHeaders(),contains("arg0"));
        List<DataTableRow> integersRows = dataTableIntegers.getRows();
        assertThat(integersRows.get(0).getStringValues().get(0), is("1"));
        assertThat(integersRows.get(1).getStringValues().get(0), is("2"));
        assertThat(integersRows.get(2).getStringValues().get(0), is("3"));
    }
}
