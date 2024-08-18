package net.serenitybdd.testng;

import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.di.SerenityInfrastructure;
import net.serenitybdd.testng.datadriven.NamedDataTable;
import net.serenitybdd.testng.datadriven.TestNGDataDrivenAnnotations;
import net.thucydides.core.steps.BaseStepListener;
import net.thucydides.core.steps.Listeners;
import net.thucydides.core.steps.StepEventBus;
import net.thucydides.model.configuration.SystemPropertiesConfiguration;
import net.thucydides.model.domain.DataTable;
import net.thucydides.model.domain.TestOutcome;
import net.thucydides.model.domain.TestResult;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.reports.ReportService;
import net.thucydides.model.steps.StepListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.*;
import org.testng.annotations.ITestAnnotation;
import org.testng.annotations.Ignore;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static net.thucydides.model.reports.ReportService.getDefaultReporters;
import static net.thucydides.model.steps.TestSourceType.TEST_SOURCE_TESTNG;


public class SerenityTestNGExecutionListener extends TestListenerAdapter implements ITestNGListener, IExecutionListener,
		IDataProviderListener, ISuiteListener, IAnnotationTransformer {

    private static final Logger logger = LoggerFactory.getLogger(SerenityTestNGExecutionListener.class);


    private static final Map<Class<?>, String> TEST_CASE_DISPLAY_NAMES = new ConcurrentHashMap<>();

    private static final Map<String, String> DATA_DRIVEN_TEST_NAMES =  new ConcurrentHashMap<>();

    //key-> "ClassName.MethodName"
    //entries-> DataTable associated with method
    private final static Map<String, DataTable> dataTables = Collections.synchronizedMap(new HashMap<>());

    private ReportService reportService;

    private static File getOutputDirectory() {
        SystemPropertiesConfiguration systemPropertiesConfiguration = new SystemPropertiesConfiguration(new SystemEnvironmentVariables());
        return systemPropertiesConfiguration.getOutputDirectory();
    }

    private boolean testStarted;

    private DataTable dataTable;

    public SerenityTestNGExecutionListener() {
       BaseStepListener baseStepListener = Listeners.getBaseStepListener().withOutputDirectory(getOutputDirectory());
    }


    /**
     * This method is invoked before the SuiteRunner starts. (ISuiteListener)
     * Params:
     * suite – The suite
     * @param suite The suite
     */
    @Override
    public void onStart(ISuite suite) {
        System.out.println("Starting Suite " + suite.getName());


        //eventBusFor(suite.).testSuiteStarted(suite.getXmlSuite().getClass(),"" /*suite.getXmlSuite().getTest().*/);
        //StepEventBus.getEventBus().testSuiteStarted(suite.getXmlSuite().getClass(),suite.getName());
    }

    private void configureParameterizedTestData(Class javaClass) {
        Map<String, DataTable> parameterTablesForClass = TestNGDataDrivenAnnotations.forClass(javaClass).getParameterTables();
        if (!parameterTablesForClass.isEmpty()) {
            dataTables.putAll(parameterTablesForClass);
        }
    }


    /**
     * This method is invoked after the SuiteRunner has run all the tests in the suite. (ISuiteListener)
     * Params:
     * suite – The suite
     * @param suite The suite
     */
    @Override
    public void onFinish(ISuite suite) {
        if (dataTable != null) {
            eventBusFor().exampleFinished();
            System.out.println("Example finished " + currentExample  + " " + dataTable.row(currentExample).toStringMap());
            currentExample = 0;
            dataTable = null;
        }
        System.out.println("Finishing Suite " + suite.getName());
        StepEventBus.getEventBus().testSuiteFinished();
        generateReports();
    }


    /**
     * Invoked before the TestNG run starts. (IExecutionListener)
     */
    @Override
    public void onExecutionStart() {

    }

    /**
     * Invoked once all the suites have been run. (IExecutionListener)
     */
    @Override
    public void onExecutionFinish() {
        System.out.println("On Execution finish");
        StepEventBus.getEventBus().testSuiteFinished();
    }

    private void injectSteps(Object testInstance){
        Serenity.injectDriverInto(testInstance);
        Serenity.injectAnnotatedPagesObjectInto(testInstance);
        Serenity.injectScenarioStepsInto(testInstance);
        Serenity.injectDependenciesInto(testInstance);
        SystemEnvironmentVariables.currentEnvironment().reset();
    }


    boolean exampleStarted = false;
    int currentExample = 0;

    /**
     * (ITestListener)
     * Invoked each time before a test will be invoked. The ITestResult is only partially filled with the references
     * to class, method, start millis and status.
     * Params:
     * result – the partially filled ITestResult
     * See Also:
     * ITestResult. STARTED
     * @param result the partially filled <code>ITestResult</code>
     */
    @Override
    public void onTestStart(ITestResult result) {
        //status = STARTED

        if (!isSerenityTestNGClass(result)) {
            return;
        }

        injectSteps(result.getInstance());

        startTestSuiteForFirstTest(result);

        logger.info("On test start " + result + " " + result.getName() + " " + result.getInstance() + "datatable" + dataTable);
        stepEventBus().clear();
        //stepEventBus().setTestSource(TEST_SOURCE_TESTNG.getValue());
        stepEventBus().testStarted(result.getName(),result.getTestClass().getRealClass());
        startTest();
        if (dataTable != null) {
            if (!exampleStarted) {
                //eventBusFor().exampleFinished();
                eventBusFor().useExamplesFrom(dataTable);
                exampleStarted = true;
                currentExample = 0;
            }
            logger.info("useDataTable " + dataTable);

            eventBusFor().exampleStarted(dataTable.row(currentExample).toStringMap());
            System.out.println("Example started " + currentExample  + " " + dataTable.row(currentExample).toStringMap());
            currentExample++;
        }
    }

    private void startTestSuiteForFirstTest(ITestResult result) {
        //if (isMethodSource(testIdentifier)) {
            Class<?> testCase = result.getTestClass().getRealClass();
            logger.info("-->TestSuiteStarted " + testCase);
            String testSuiteName = TEST_CASE_DISPLAY_NAMES.getOrDefault(testCase, testCase.getSimpleName());
            eventBusFor().testSuiteStarted(testCase, testSuiteName);
        //}
    }

    private boolean isSerenityTestNGClass(ITestResult testResult) {
        return testResult.getTestClass().getRealClass().isAnnotationPresent(SerenityTestNG.class);
    }

    /**
     * Invoked each time a test succeeds.
     * Params:
     * result – ITestResult containing information about the run test
     * See Also:
     * ITestResult. SUCCESS
     * @param result <code>ITestResult</code> containing information about the run test
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        if (!isSerenityTestNGClass(result)) {
            return;
        }
        super.onTestSuccess(result);
        updateResultsUsingTestAnnotations(result);
        System.out.println("On test success " + result);
        if (testingThisTest(result)) {
            // TODO updateResultsUsingTestAnnotations(description);
            stepEventBus().testFinished();
            if (dataTable != null) {
                stepEventBus().exampleFinished();
            }
            stepEventBus().setTestSource(null);
            endTest();
        }
    }

    /**
     * Invoked each time a test fails.
     * Params:
     * result – ITestResult containing information about the run test
     * See Also:
     * ITestResult. FAILURE
     * @param result <code>ITestResult</code> containing information about the run test
     */
    @Override
    public void onTestFailure(ITestResult result) {
        if (!isSerenityTestNGClass(result)) {
            return;
        }
        super.onTestFailure(result);
        if (testingThisTest(result)) {
            //TODO startTestIfNotYetStarted(failure.getDescription());
            stepEventBus().testFailed(result.getThrowable());
            //TODO updateFailureList(failure);
            endTest();
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        if (!isSerenityTestNGClass(result)) {
            return;
        }
        super.onTestSkipped(result);
       // processTestMethodAnnotationsFor(result);
    }

    @Override
    public void onConfigurationSkip(ITestResult result) {
        if (!isSerenityTestNGClass(result)) {
            return;
        }
        super.onTestSkipped(result);
        processTestMethodAnnotationsFor(result);
    }

    private void processTestMethodAnnotationsFor(ITestResult testResult) {
        //String className = methodTestSource.getClassName();
        //String methodName = methodTestSource.getMethodName();
        ITestNGMethod method = testResult.getMethod();
        //method parameter types are class names as strings comma separated : java.langString,java.lang.Integer
        /*String methodParameterTypes = methodTestSource.getMethodParameterTypes();
        List<Class> methodParameterClasses = null;

        if (methodParameterTypes != null && !methodParameterTypes.isEmpty()) {
            methodParameterClasses = Arrays.asList(methodParameterTypes.split(",")).stream().map(parameterClassName -> {
                try {
                    //ClassUtils handles also simple data type like int, char..
                    return ClassUtil.forName(parameterClassName.trim(), this.getClass().getClassLoader());
                } catch (ClassNotFoundException e) {
                    logger.error("Problem when getting parameter classes ", e);
                    return null;
                }
            }).collect(Collectors.toList());
        }*/

        if (isIgnored(method.getConstructorOrMethod().getMethod())) {
            startTestAtEventBus(testResult);
            eventBusFor().testIgnored();
            eventBusFor().testFinished();
        }


    }

    private boolean isIgnored(Method child) {
        return child.getAnnotation(Ignore.class) != null;
    }


    private void startTestAtEventBus(ITestResult testResult) {
        eventBusFor().setTestSource(TEST_SOURCE_TESTNG.getValue());
        //String displayName = removeEndBracketsFromDisplayName(testIdentifier.getDisplayName());
        String displayName = testResult.getTestName();
        //if (isMethodSource(testIdentifier)) {
            //String className = ((MethodSource) testIdentifier.getSource().get()).getClassName();
            String className = testResult.getTestClass().getRealClass().getName();
            try {
                eventBusFor().testStarted(Optional.ofNullable(displayName).orElse("Initialisation"), Class.forName(className));
            } catch (ClassNotFoundException exception) {
                logger.error("Exception when starting test at event bus ", exception);
            }
        //}
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        if (!isSerenityTestNGClass(result)) {
            return;
        }
        super.onTestFailedButWithinSuccessPercentage(result);
    }

    /**
     * (ITestListener)
     * Invoked before running all the test methods belonging to the classes inside the <test> tag and
     * calling all their Configuration methods.
     * Params:
     * context – The test context
     * @param context The test context
     */
    @Override
    public void onStart(ITestContext context) {
        super.onStart(context);
        System.out.println("On start " + context.getCurrentXmlTest().getName());
    }

    /**
     * (ITestListener)
     * Invoked after all the test methods belonging to the classes inside the <test> tag have run and all their Configuration methods have been called.
     * Params:
     * context – The test context
     * @param context The test context
     */
    @Override
    public void onFinish(ITestContext context) {
        System.out.println("On finish " + context.getCurrentXmlTest().getName());
    }

    StepEventBus stepEventBus(/*ITestContext testContext */) {
        return  eventBusFor(/*testContext*/);
    }

    private synchronized StepEventBus eventBusFor(/*ITestContext testContext*/) {
        //String uniqueTestId = testIdentifier.getUniqueId();

        //StepEventBus currentEventBus = StepEventBus.eventBusFor(/*testContext.getName()*/);
        StepEventBus currentEventBus = StepEventBus.getEventBus();
        if (!currentEventBus.isBaseStepListenerRegistered()) {
            File outputDirectory = getOutputDirectory();
            BaseStepListener baseStepListener = Listeners.getBaseStepListener().withOutputDirectory(outputDirectory);
            currentEventBus.registerListener(baseStepListener);
//            currentEventBus.registerListener(new ConsoleLoggingListener(currentEventBus.getEnvironmentVariables()));
            currentEventBus.registerListener(SerenityInfrastructure.getLoggingListener());
            logger.trace("  -> ADDED BASE LISTENER " + baseStepListener);
            StepListener loggingListener = Listeners.getLoggingListener();
            currentEventBus.registerListener(loggingListener);
            logger.trace("  -> ADDED LOGGING LISTENER " + loggingListener);
        }
        logger.trace("SETTING EVENT BUS FOR THREAD " + Thread.currentThread() + " TO " + currentEventBus);
        StepEventBus.setCurrentBusToEventBusFor("TestNg");
        return currentEventBus;
    }



    private void startTest() {
        testStarted = true;
    }
    private void endTest() {
        testStarted = false;
    }

    private boolean testingThisTest(ITestResult testResult) {
        //return (testResult.getTestClass() != null) && (testResult.getTestClass().equals(testClass));
        return true;
    }

    private void generateReports(/*ITestContext testContext*/) {
        //logger.trace("GENERATE REPORTS FOR TEST " + testContext.getName());
        generateReportsFor(getNonDataDrivenTestOutcomes());
        generateReportsForParameterizedTests();
        //StepEventBus.clearEventBusFor(testContext.getName());
    }

        /**
     * A test runner can generate reports via Reporter instances that subscribe
     * to the test runner. The test runner tells the reporter what directory to
     * place the reports in. Then, at the end of the test, the test runner
     * notifies these reporters of the test outcomes. The reporter's job is to
     * process each test run outcome and do whatever is appropriate.
     *
     * @param testOutcomeResults the test results from the previous test run.
     */
    private void generateReportsFor(final List<TestOutcome> testOutcomeResults) {

        getReportService().generateReportsFor(testOutcomeResults);
        getReportService().generateConfigurationsReport();
    }

     private void generateReportsForParameterizedTests(/*List<TestIdentifier> testIdentifiers*/) {
        //logger.trace("GENERATE REPORTS FOR PARAMETERIZED TESTS " + testIdentifiers);
        /*List<TestOutcome> allTestOutcomes = testIdentifiers
                .stream()
                .map(this::getTestOutcomes)
                .flatMap(List::stream)
                .collect(Collectors.toList());*/
        ParameterizedTestsOutcomeAggregator parameterizedTestsOutcomeAggregator
                = new ParameterizedTestsOutcomeAggregator(getDataDrivenTestOutcomes()/*allTestOutcomes*/);

        generateReportsFor(parameterizedTestsOutcomeAggregator.aggregateTestOutcomesByTestMethods());

        //testIdentifiers.stream().map(TestIdentifier::getUniqueId).forEach(StepEventBus::clearEventBusFor);
    }

    private ReportService getReportService() {
        if (reportService == null) {
            reportService = new ReportService(getOutputDirectory(), getDefaultReporters());
        }
        return reportService;
    }

        /**
     * Find the current set of test outcomes produced by the test execution.
     *
     *
     * @return the current list of test outcomes
     */
    public List<TestOutcome> getTestOutcomes(/*ITestContext testContext*/) {
        //logger.trace("GET TEST OUTCOMES FOR " + testContext);
        //logger.trace(" - BASE STEP LISTENER: " + eventBusFor(testIdentifier).getBaseStepListener());
        //List<TestOutcome> testOutcomes = eventBusFor(testIdentifier).getBaseStepListener().getTestOutcomes();
        List<TestOutcome> testOutcomes = eventBusFor().getBaseStepListener().getTestOutcomes();
        /*testOutcomes.forEach(
                outcome -> {
                    if (testIdentifier.getParentId().isPresent() && DATA_DRIVEN_TEST_NAMES.get(testIdentifier.getParentId().get()) != null) {
                        outcome.setTestOutlineName(DATA_DRIVEN_TEST_NAMES.get(testIdentifier.getParentId().get()));
                    }
                }
        );*/
        System.out.println("TestOutcomes " + testOutcomes);
        return testOutcomes;
    }

        /**
     * Find the current set of test outcomes produced by the test execution.
     *
     *
     * @return the current list of test outcomes
     */
    public List<TestOutcome> getDataDrivenTestOutcomes(/*ITestContext testContext*/) {
        //logger.trace("GET TEST OUTCOMES FOR " + testContext);
        //logger.trace(" - BASE STEP LISTENER: " + eventBusFor(testIdentifier).getBaseStepListener());
        //List<TestOutcome> testOutcomes = eventBusFor(testIdentifier).getBaseStepListener().getTestOutcomes();
        List<TestOutcome> testOutcomes = eventBusFor().getBaseStepListener().getTestOutcomes().stream().filter(TestOutcome::isDataDriven).collect(Collectors.toList());
        /*testOutcomes.forEach(
                outcome -> {
                    if (testIdentifier.getParentId().isPresent() && DATA_DRIVEN_TEST_NAMES.get(testIdentifier.getParentId().get()) != null) {
                        outcome.setTestOutlineName(DATA_DRIVEN_TEST_NAMES.get(testIdentifier.getParentId().get()));
                    }
                }
        );*/
        System.out.println("TestOutcomes " + testOutcomes);
        return testOutcomes;
    }

    public List<TestOutcome> getNonDataDrivenTestOutcomes(/*ITestContext testContext*/) {
        //logger.trace("GET TEST OUTCOMES FOR " + testContext);
        //logger.trace(" - BASE STEP LISTENER: " + eventBusFor(testIdentifier).getBaseStepListener());
        //List<TestOutcome> testOutcomes = eventBusFor(testIdentifier).getBaseStepListener().getTestOutcomes();
        List<TestOutcome> testOutcomes = eventBusFor().getBaseStepListener().getTestOutcomes().stream().filter(to->!to.isDataDriven()).collect(Collectors.toList());
        /*testOutcomes.forEach(
                outcome -> {
                    if (testIdentifier.getParentId().isPresent() && DATA_DRIVEN_TEST_NAMES.get(testIdentifier.getParentId().get()) != null) {
                        outcome.setTestOutlineName(DATA_DRIVEN_TEST_NAMES.get(testIdentifier.getParentId().get()));
                    }
                }
        );*/
        System.out.println("TestOutcomes " + testOutcomes);
        return testOutcomes;
    }







    private void updateResultsUsingTestAnnotations(ITestResult result) {

        Method method =  result.getMethod().getConstructorOrMethod().getMethod();
        if (TestMethodConfiguration.forMethod(method).isManual()) {
            setToManual(method);
        }
        //expectedExceptions.forEach(ex -> updateResultsForExpectedException(testIdentifier, ex));
    }


    private void setToManual(Method methodSource) {
        eventBusFor().testIsManual();
        TestResult result = TestMethodConfiguration.forMethod(methodSource).getManualResult();
        eventBusFor().getBaseStepListener().recordManualTestResult(result);
    }

    @Override
    public void beforeDataProviderExecution(
      IDataProviderMethod dataProviderMethod, ITestNGMethod method, ITestContext iTestContext) {
        logger.info("beforeDataProviderExecution " + dataProviderMethod.getName() + " " + dataProviderMethod.getIndices() +   " methodName: " + method.getMethodName()
                    + " context: " + iTestContext.getName());
        System.out.println("XXXbeforeDataProviderExecution " + dataProviderMethod.getName() + " " + dataProviderMethod.getIndices() +   " methodName: " + method.getMethodName()
                + " context: " + iTestContext.getName());
        Method testDataMethod =  method.getConstructorOrMethod().getMethod();
        String dataTableName = testDataMethod.getDeclaringClass().getCanonicalName() + "." + testDataMethod.getName();
        dataTable  = dataTables.get(dataTableName);
        if (dataTable == null) {
            NamedDataTable namedDataTable = new TestNGDataDrivenAnnotations().generateDataTableForMethod(dataProviderMethod, method, iTestContext);
            dataTable = namedDataTable.getDataTable();
            dataTables.put(dataTableName, dataTable);
        }
        exampleStarted = false;
    }



  /**
   * This method gets invoked just after a data provider is invoked.
   *
   * @param dataProviderMethod - A {@link IDataProviderMethod} object that contains details about
   *     the data provider that got executed.
   * @param method - The {@link ITestNGMethod} method that received the data
   * @param iTestContext - The current test context
   */
  public void afterDataProviderExecution(
      IDataProviderMethod dataProviderMethod, ITestNGMethod method, ITestContext iTestContext) {
      logger.info("afterDataProviderExecution " + dataProviderMethod + "name: " + method.getMethodName()
                + " testContext: " + iTestContext + " " + iTestContext.getPassedTests());
  }

  /**
   * This method gets invoked when the data provider encounters an exception
   *
   * @param method - The {@link ITestNGMethod} method that received the data. A reference to the
   *     corresponding data provider can be obtained via {@link
   *     ITestNGMethod#getDataProviderMethod()}
   * @param ctx - The current test context
   * @param t - The {@link RuntimeException} that embeds the actual exception. Use {@link
   *     RuntimeException#getCause()} to get to the actual exception.
   */
    public void onDataProviderFailure(ITestNGMethod method, ITestContext ctx, RuntimeException t) {
        logger.info("onDataProviderFailure " + method  + " " + ctx  + " " + t);
  }

    public void transform(
        ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
   		System.out.println("XXXTransform " + testMethod);
    }

    public static Map<String, DataTable> getDataTables() {
        return dataTables;
    }


}