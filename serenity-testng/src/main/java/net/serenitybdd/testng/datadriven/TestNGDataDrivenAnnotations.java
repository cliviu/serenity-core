package net.serenitybdd.testng.datadriven;

import com.google.common.base.Splitter;
//import net.serenitybdd.junit5.datadriven.JUnit5CSVTestDataSource;
import net.thucydides.model.domain.DataTable;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;
import org.apache.commons.lang3.StringUtils;
//import org.junit.jupiter.api.TestInstance;
//import org.junit.jupiter.params.ParameterizedTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IDataProviderMethod;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;

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

    private  Map<String, DataTable> parameterTables = new HashMap<>();

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
        this.parameterTables = generateParameterTables();
    }

    public NamedDataTable generateDataTableForMethod(IDataProviderMethod dataProviderMethod, ITestNGMethod method, ITestContext iTestContext) {
        List<List<Object>> parametersAsListsOfObjects = parametersFromDataProvider(dataProviderMethod);
        Method testDataMethod =  method.getConstructorOrMethod().getMethod();
        String columnNamesString = createColumnNamesFromParameterNames(testDataMethod);
        System.out.println("ColumnNames string  " + columnNamesString);
        String dataTableName = testDataMethod.getDeclaringClass().getCanonicalName() + "." + testDataMethod.getName();
        System.out.println("DataTableName  " + dataTableName);
        DataTable parametersTableFrom = createParametersTableFrom(columnNamesString, parametersAsListsOfObjects);
        return new NamedDataTable(dataTableName, parametersTableFrom);
    }

    List<List<Object>> parametersFromDataProvider(IDataProviderMethod dataProviderMethod) {
		try {
		    System.out.println("XXX Result before " + dataProviderMethod.getMethod());
            Object[][] result = (Object[][])dataProviderMethod.getMethod().invoke(dataProviderMethod.getInstance());
            System.out.println("XXX Result " + result);
            List<List<Object>>  parametersList = Arrays.asList(result).stream().map(Arrays::asList).collect(Collectors.toList());
            System.out.println("XXX Result1 " + parametersList);
            return parametersList;
        }   catch (IllegalAccessException e) {
              logger.error("Cannot list the objects from Data Provider ", e);
		}   catch (InvocationTargetException e) {
            logger.error("Cannot list the objects from Data Provider ", e);
		}
		return null;
        /*MethodSource methodSourceAnnotation = testDataMethod.getAnnotation(MethodSource.class);
        String[] value = methodSourceAnnotation.value();
        String methodName;
        boolean staticMethodUsed = isStaticMethodUsed(testDataMethod);
        if (value != null && (value.length > 0) && (!value[0].isEmpty())) {
            List<String> methodNames = Arrays.asList(value);
            methodName = methodNames.get(0);
            if (methodName.indexOf("#") > 0) { //external class source
                List<List<Object>> result = getListOfObjectsFromExternalClassSource(methodName);
                if (result != null) return result;
            }
        } else { //no factory method name
            methodName = testDataMethod.getName();
        }

        try {
            Method factoryMethod = testDataMethod.getDeclaringClass().getDeclaredMethod(methodName);
            factoryMethod.setAccessible(true);
            try {
                Stream<?> result = null;
                if (staticMethodUsed) {
                    result = (Stream<?>) factoryMethod.invoke(null);
                } else {
                    result = (Stream<?>) factoryMethod.invoke(testDataMethod.getDeclaringClass().getConstructor().newInstance());
                }
                return result.map(argument -> convertToListOfParameters(argument)).collect(Collectors.toList());
                //return result.map(argument->Arrays.asList(argument.get())).collect(Collectors.toList());
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                logger.error("Cannot get list of objects from method source ", e);
            }
        } catch (NoSuchMethodException ex) {
            logger.error("No static method with the name " + methodName + " found ", ex);
        }*/
    }



    public Map<String, DataTable> getParameterTables() {
        return parameterTables;
    }

    private Map<String, DataTable> generateParameterTables() {
        List<Method> allMethods = findTestDataMethods();
        Map<String, DataTable> dataTables = new HashMap<>();
        for (Method testDataMethod : allMethods) {
            //TODO
            /*if (isAValueSourceAnnotatedMethod(testDataMethod)) {
                fillDataTablesFromValueSource(dataTables, testDataMethod);
            }  if (isAMethodSourceAnnotatedMethod(testDataMethod)) {
                fillDataTablesFromMethodSource(dataTables, testDataMethod);
            }*/
        }
        return dataTables;
    }

    private void fillDataTablesFromValueSource(Map<String, DataTable> dataTables, Method testDataMethod) {
        String columnNamesString = createColumnNamesFromParameterNames(testDataMethod);
        String dataTableName = testDataMethod.getDeclaringClass().getCanonicalName() + "." + testDataMethod.getName();
        //TODO
        /*List<List<Object>> parametersAsListsOfObjects = listOfObjectsFromValueSource(testDataMethod);
        logger.debug("GetParameterTables: Put parameter dataTableName " + dataTableName + " -- " + parametersAsListsOfObjects);
        dataTables.put(dataTableName, createParametersTableFrom(columnNamesString, parametersAsListsOfObjects));*/
    }


    private void fillDataTablesFromMethodSource(Map<String, DataTable> dataTables, Method testDataMethod) {
        String columnNamesString = createColumnNamesFromParameterNames(testDataMethod);
        String dataTableName = testDataMethod.getDeclaringClass().getCanonicalName() + "." + testDataMethod.getName();
        //TODO
        /*List<List<Object>> parametersAsListsOfObjects = listOfObjectsFromMethodSource(testDataMethod);
        logger.info("GetParameterTablesFromMethodSource: Put parameter dataTableName " + dataTableName + " " + parametersAsListsOfObjects);
        dataTables.put(dataTableName, createParametersTableFrom(columnNamesString, parametersAsListsOfObjects));*/
    }

    List<Method> findTestDataMethods() {
        return findTestDataMethods(testClass);
    }

    List<Method> findTestDataMethods(Class<?> testClass) {

        List<Method> dataDrivenMethods = new ArrayList<>();

        // Add all the data driven methods in this class
        List<Method> allMethods = Arrays.asList(testClass.getDeclaredMethods());
        //TODO
        //allMethods.stream().filter(this::findParameterizedTests).forEach(dataDrivenMethods::add);

        // Add all the data driven methods in any nested classes
        List<Class<?>> nestedClasses = Arrays.asList(testClass.getDeclaredClasses());
        nestedClasses.forEach(
                nestedClass -> dataDrivenMethods.addAll(findTestDataMethods(nestedClass))
        );
        return dataDrivenMethods;
    }

    String createColumnNamesFromParameterNames(Method method) {
        return Arrays.asList(method.getParameters()).stream().map(Parameter::getName).collect(Collectors.joining(","));
    }

/*    List<List<Object>> listOfObjectsFromMethodSource(Method testDataMethod) {
        MethodSource methodSourceAnnotation = testDataMethod.getAnnotation(MethodSource.class);
        String[] value = methodSourceAnnotation.value();
        String methodName;
        boolean staticMethodUsed = isStaticMethodUsed(testDataMethod);
        if (value != null && (value.length > 0) && (!value[0].isEmpty())) {
            List<String> methodNames = Arrays.asList(value);
            methodName = methodNames.get(0);
            if (methodName.indexOf("#") > 0) { //external class source
                List<List<Object>> result = getListOfObjectsFromExternalClassSource(methodName);
                if (result != null) return result;
            }
        } else { //no factory method name
            methodName = testDataMethod.getName();
        }

        try {
            Method factoryMethod = testDataMethod.getDeclaringClass().getDeclaredMethod(methodName);
            factoryMethod.setAccessible(true);
            try {
                Stream<?> result = null;
                if (staticMethodUsed) {
                    result = (Stream<?>) factoryMethod.invoke(null);
                } else {
                    result = (Stream<?>) factoryMethod.invoke(testDataMethod.getDeclaringClass().getConstructor().newInstance());
                }
                return result.map(argument -> convertToListOfParameters(argument)).collect(Collectors.toList());
                //return result.map(argument->Arrays.asList(argument.get())).collect(Collectors.toList());
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                logger.error("Cannot get list of objects from method source ", e);
            }
        } catch (NoSuchMethodException ex) {
            logger.error("No static method with the name " + methodName + " found ", ex);
        }
        return null;
    }*/

    /*private List<Object> convertToListOfParameters(Object argument) {
        if (argument instanceof Arguments) {
            return Arrays.asList(((Arguments) argument).get());
        } else {
            return Collections.singletonList(argument);
        }
    }

    private boolean isStaticMethodUsed(Method testDataMethod) {
        List<Annotation> annotations = Arrays.asList(testDataMethod.getDeclaringClass().getDeclaredAnnotations());
        List<Annotation> allTestInstanceAnnotations = annotations.stream().filter(annotation -> annotation.annotationType().equals(TestInstance.class)).collect(Collectors.toList());
        Optional<Annotation> perClassAnnotation = allTestInstanceAnnotations.stream().filter(currentAnnotation -> ((TestInstance) currentAnnotation).value().equals(TestInstance.Lifecycle.PER_CLASS)).findAny();
        return !perClassAnnotation.isPresent();
    }

    private List<List<Object>> getListOfObjectsFromExternalClassSource(String methodName) {
        Method factoryMethod;
        String externalParameterFactoryClassName = methodName.substring(0, methodName.indexOf("#"));
        String externalParameterFactoryMethodName = methodName.substring(methodName.indexOf("#") + 1);
        try {
            Class externalClassFactory = Class.forName(externalParameterFactoryClassName);
            factoryMethod = externalClassFactory.getDeclaredMethod(externalParameterFactoryMethodName);
            factoryMethod.setAccessible(true);
            Stream<Arguments> result = (Stream<Arguments>) factoryMethod.invoke(null);
            return result.map(argument -> Arrays.asList(argument.get())).collect(Collectors.toList());
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException e) {
            logger.error("Cannot found external parameter factory class method", e);
        }
        return null;
    }

    List<List<Object>> listOfObjectsFromValueSource(Method testDataMethod) {
        ValueSource annotation = testDataMethod.getAnnotation(ValueSource.class);
        if (ArrayUtils.isNotEmpty(annotation.strings()))
            return listOfObjectsFrom(annotation.strings());
        else if (ArrayUtils.isNotEmpty(annotation.bytes()))
            return listOfObjectsFrom(ArrayUtils.toObject(annotation.bytes()));
        else if (ArrayUtils.isNotEmpty(annotation.chars()))
            return listOfObjectsFrom(ArrayUtils.toObject(annotation.chars()));
        else if (ArrayUtils.isNotEmpty(annotation.doubles()))
            return listOfObjectsFrom(ArrayUtils.toObject(annotation.doubles()));
        else if (ArrayUtils.isNotEmpty(annotation.floats()))
            return listOfObjectsFrom(ArrayUtils.toObject(annotation.floats()));
        else if (ArrayUtils.isNotEmpty(annotation.ints()))
            return listOfObjectsFrom(ArrayUtils.toObject(annotation.ints()));
        else if (ArrayUtils.isNotEmpty(annotation.shorts()))
            return listOfObjectsFrom(ArrayUtils.toObject(annotation.shorts()));
        else if (ArrayUtils.isNotEmpty(annotation.classes()))
            return listOfObjectsFrom(annotation.classes());
        return null;
    }*/


    private List<List<Object>> listOfCsvObjectsFrom(Object[] parameters, String delimiter) {
        List<List<Object>> ret = new ArrayList<>();
        for (Object parameter : parameters) {
            String[] split = ((String) parameter).split(Pattern.quote(delimiter));
            ret.add(Arrays.asList(split));
        }
        return ret;
    }


    private List<List<Object>> listOfObjectsFrom(Object[] parameters) {
        return Arrays.stream(parameters).map(Arrays::asList).collect(Collectors.toList());
    }


    private DataTable createParametersTableFrom(String columnNamesString, List<List<Object>> parametersList) {
        int numberOfColumns = parametersList.isEmpty() ? 0 : parametersList.get(0).size();
        List<String> columnNames = split(columnNamesString, numberOfColumns);
        return DataTable.withHeaders(columnNames)
                .andRows(parametersList)
                .build();
    }

    private DataTable createParametersTableFrom(List<String> columnNames, List<List<Object>> parametersList) {
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

    /*private boolean findParameterizedTests(Method method) {
        return method.getAnnotation(ParameterizedTest.class) != null &&
                (isAValueSourceAnnotatedMethod(method)
                        || isACsvFileSourceAnnotatedMethod(method)
                        || isACsvSourceAnnotatedMethod(method)
                        || isAEnumSourceAnnotatedMethod(method)
                        || isAMethodSourceAnnotatedMethod(method)
                        || isAnArgumentsSourceAnnotatedMethod(method));
    }*/

   /* private boolean isAValueSourceAnnotatedMethod(Method method) {
        return method.getAnnotation(ValueSource.class) != null;
    }

    private boolean isAEnumSourceAnnotatedMethod(Method method) {
        return method.getAnnotation(EnumSource.class) != null;
    }

    private boolean isACsvSourceAnnotatedMethod(Method method) {
        return method.getAnnotation(CsvSource.class) != null;
    }

    private boolean isACsvFileSourceAnnotatedMethod(Method method) {
        return method.getAnnotation(CsvFileSource.class) != null;
    }

    private boolean isAMethodSourceAnnotatedMethod(Method method) {
        return method.getAnnotation(MethodSource.class) != null;
    }

    private boolean isAnArgumentsSourceAnnotatedMethod(Method method) {
        return method.getAnnotation(ArgumentsSource.class) != null;
    }*/
}
