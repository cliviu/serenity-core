package net.serenitybdd.testng.datadriven;

import com.google.common.base.Splitter;
import net.thucydides.model.domain.DataTable;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IDataProviderMethod;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TestNGDataDrivenAnnotations {


    private final static Logger logger = LoggerFactory.getLogger(TestNGDataDrivenAnnotations.class);

    private EnvironmentVariables environmentVariables;

    private Class testClass;


    public static TestNGDataDrivenAnnotations forClass(final Class testClass) {
        return new TestNGDataDrivenAnnotations(testClass);
    }

    public TestNGDataDrivenAnnotations() {

    }

    TestNGDataDrivenAnnotations(final Class testClass) {
        this(testClass, SystemEnvironmentVariables.currentEnvironmentVariables());
    }

    TestNGDataDrivenAnnotations(final Class testClass, EnvironmentVariables environmentVariables) {
        this.testClass = testClass;
        this.environmentVariables = environmentVariables;
    }

    public NamedDataTable generateDataTableForMethod(IDataProviderMethod dataProviderMethod, ITestNGMethod method) {
        List<List<Object>> parametersAsListsOfObjects = parametersFromDataProvider(dataProviderMethod);
        Method testDataMethod =  method.getConstructorOrMethod().getMethod();
        String columnNamesString = createColumnNamesFromParameterNames(testDataMethod);
        String dataTableName = testDataMethod.getDeclaringClass().getCanonicalName() + "." + testDataMethod.getName();
        DataTable parametersTableFrom = createParametersTableFrom(columnNamesString, parametersAsListsOfObjects);
        return new NamedDataTable(dataTableName, parametersTableFrom);
    }

    List<List<Object>> parametersFromDataProvider(IDataProviderMethod dataProviderMethod) {
		try {
            Object[][] result = (Object[][])dataProviderMethod.getMethod().invoke(dataProviderMethod.getInstance());
            List<List<Object>>  parametersList = Arrays.asList(result).stream().map(Arrays::asList).collect(Collectors.toList());
            return parametersList;
        }   catch (IllegalAccessException | InvocationTargetException e) {
              logger.error("Cannot list the objects from Data Provider ", e);
		}
		return null;
    }


    String createColumnNamesFromParameterNames(Method method) {
        return Arrays.asList(method.getParameters()).stream().map(Parameter::getName).collect(Collectors.joining(","));
    }


    private DataTable createParametersTableFrom(String columnNamesString, List<List<Object>> parametersList) {
        int numberOfColumns = parametersList.isEmpty() ? 0 : parametersList.get(0).size();
        List<String> columnNames = split(columnNamesString, numberOfColumns);
        return DataTable.withHeaders(columnNames)
                .andRows(parametersList)
                .build();
    }

    private List<String> split(String columnNamesString, int numberOfColumns) {
        if (StringUtils.isEmpty(columnNamesString)) {
            return numberedColumnHeadings(numberOfColumns);
        }
        return Splitter.on(",").trimResults().omitEmptyStrings().splitToList(columnNamesString);
    }

    private List<String> numberedColumnHeadings(int numberOfColumns) {
        List<String> columnNames = new ArrayList<>();
        for (int i = 0; i < numberOfColumns; i++) {
            columnNames.add("Parameter " + (i + 1));
        }
        return columnNames;
    }
}
