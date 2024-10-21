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

    private final List<BaseStepListener> allBaseStepListeners =  Collections.synchronizedList(new ArrayList<>());

    public SerenityTestNGExecutionListener() {

    }


    /**
     * This method is invoked before the SuiteRunner starts. (ISuiteListener)
     * Params:
     * suite – The suite
     * @param suite The suite
     */
    @Override
    public void onStart(ISuite suite) {
        logger.info("Starting Suite " + suite.getName());
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
            currentExample = 0;
            dataTable = null;
        }
        logger.info("Finishing Suite " + suite.getName());
        //TODO
        //eventBusFor(suite.).testSuiteFinished();
        generateReports(suite);
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
        if (!isSerenityTestNGClass(result)) {
            return;
        }
        injectSteps(result.getInstance());
        startTestSuiteForFirstTest(result);
        //stepEventBus().clear();
        Method testMethod =  result.getMethod().getConstructorOrMethod().getMethod();
        eventBusFor(result).getBaseStepListener().addTagsToCurrentStory(TestNGTags.forMethod(testMethod));
        eventBusFor(result).setTestSource(TEST_SOURCE_TESTNG.getValue());

        String testName = result.getName();
        if (TestNGTestMethodAnnotations.forTest(testMethod).getDisplayName().isPresent()) {
            testName = TestNGTestMethodAnnotations.forTest(testMethod).getDisplayName().get();
        }
        logger.info("On test start " + result + " " + testName + " testName " + result.getTestName());
        eventBusFor(result).testStarted(testName,result.getTestClass().getRealClass());
        startTest();
        if (dataTable != null) {
            eventBusFor(result).useExamplesFrom(dataTable);
            eventBusFor(result).exampleStarted(dataTable.row(currentExample).toStringMap());
            logger.info("Example started " + currentExample  + " " + dataTable.row(currentExample).toStringMap());
            currentExample++;
        }
    }

    List<Class<?>> testSuites = new ArrayList<>();

    private void startTestSuiteForFirstTest(ITestResult result) {
        Class<?> testCase = result.getTestClass().getRealClass();
        //if (!testSuites.contains(testCase)) {
            testSuites.add(testCase);
            logger.info("-->TestSuiteStarted " + testCase);
            String testSuiteName = TEST_CASE_DISPLAY_NAMES.getOrDefault(testCase, testCase.getSimpleName());
            eventBusFor(result).testSuiteStarted(testCase, testSuiteName);
        //} else {
        //    logger.info("-->TestSuiteAlreadyStarted " + testCase);
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
        eventBusFor(result).testFinished();
        if (dataTable != null) {
            eventBusFor(result).exampleFinished();
        }
        eventBusFor(result).setTestSource(null);
        endTest();
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
        //TODO startTestIfNotYetStarted(failure.getDescription());
        eventBusFor(result).testFailed(result.getThrowable());
        //TODO updateFailureList(failure);
        endTest();

    }

    @Override
    public void onTestSkipped(ITestResult result) {
        if (!isSerenityTestNGClass(result)) {
            return;
        }
        super.onTestSkipped(result);

    }

    @Override
    public void onConfigurationSkip(ITestResult result) {
        if (!isSerenityTestNGClass(result)) {
            return;
        }
        super.onConfigurationSkip(result);
    }


    private boolean isIgnored(Method child) {
        return child.getAnnotation(Ignore.class) != null;
    }


    private void startTestAtEventBus(String testMethodName,Class<?> testClass) {
        eventBusFor(testClass).setTestSource(TEST_SOURCE_TESTNG.getValue());
        String displayName = testMethodName;
        String className = testClass.getName();
        try {
            eventBusFor(testClass).testStarted(Optional.ofNullable(displayName).orElse("Initialisation"), Class.forName(className));
        } catch (ClassNotFoundException exception) {
            logger.error("Exception when starting test at event bus ", exception);
        }

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
        Collection<ITestNGMethod> excludedITestMethods = context.getExcludedMethods();
        List<String> excludedMethods = excludedITestMethods.stream()
                .map(each -> each.getConstructorOrMethod().getMethod().getName())
                .collect(Collectors.toList());
        for (ITestNGMethod excludedITestMethod : excludedITestMethods) {
               Class testClass = excludedITestMethod.getRealClass();
               startTestAtEventBus(excludedITestMethod.getMethodName(),testClass);
               eventBusFor(testClass).testIgnored();
               eventBusFor(testClass).testFinished();
        }
        System.out.println("On start excluded methods " +  excludedMethods);
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

    private synchronized StepEventBus eventBusFor(ITestResult testResult) {
        return eventBusFor(testResult.getTestClass().getRealClass());
    }

    private synchronized StepEventBus eventBusFor(Class<?> testclass) {

        StepEventBus currentEventBus = StepEventBus.eventBusFor(testclass.getName());
        if (!currentEventBus.isBaseStepListenerRegistered()) {
            File outputDirectory = getOutputDirectory();
            BaseStepListener baseStepListener = Listeners.getBaseStepListener().withOutputDirectory(outputDirectory);
            allBaseStepListeners.add(baseStepListener);
            currentEventBus.registerListener(baseStepListener);
//            currentEventBus.registerListener(new ConsoleLoggingListener(currentEventBus.getEnvironmentVariables()));
            currentEventBus.registerListener(SerenityInfrastructure.getLoggingListener());
            logger.trace("  -> ADDED BASE LISTENER " + baseStepListener);
            StepListener loggingListener = Listeners.getLoggingListener();
            currentEventBus.registerListener(loggingListener);
            logger.trace("  -> ADDED LOGGING LISTENER " + loggingListener);
        }
        logger.trace("SETTING EVENT BUS FOR THREAD " + Thread.currentThread() + " TO " + currentEventBus);
        StepEventBus.setCurrentBusToEventBusFor(testclass.getName());
        return currentEventBus;
    }



    private void startTest() {
        testStarted = true;
    }
    private void endTest() {
        testStarted = false;
    }

    private void generateReports(ISuite suite) {
        logger.debug("GENERATE REPORTS for suite " + suite.getName());
        generateReportsFor(getNonDataDrivenTestOutcomes());
        generateReportsForParameterizedTests();
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
        ParameterizedTestsOutcomeAggregator parameterizedTestsOutcomeAggregator
                = new ParameterizedTestsOutcomeAggregator(getDataDrivenTestOutcomes()/*allTestOutcomes*/);
        generateReportsFor(parameterizedTestsOutcomeAggregator.aggregateTestOutcomesByTestMethods());
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
    public List<TestOutcome> getTestOutcomes() {

        List<TestOutcome> allTestOutcomes = new ArrayList<>();
        for(BaseStepListener stepListener :  allBaseStepListeners) {
            allTestOutcomes.addAll(stepListener.getTestOutcomes());
        }
        /*testOutcomes.forEach(
                outcome -> {
                    if (testIdentifier.getParentId().isPresent() && DATA_DRIVEN_TEST_NAMES.get(testIdentifier.getParentId().get()) != null) {
                        outcome.setTestOutlineName(DATA_DRIVEN_TEST_NAMES.get(testIdentifier.getParentId().get()));
                    }
                }
        );*/
        return allTestOutcomes;
    }

    /**
     * Find the current set of test outcomes produced by the test execution.
     * @return the current list of test outcomes
     */
    public List<TestOutcome> getDataDrivenTestOutcomes() {
        return getTestOutcomes().stream().filter(TestOutcome::isDataDriven).collect(Collectors.toList());
    }

    public List<TestOutcome> getNonDataDrivenTestOutcomes() {
        return getTestOutcomes().stream().filter(to->!to.isDataDriven()).collect(Collectors.toList());
    }


    private void updateResultsUsingTestAnnotations(ITestResult result) {
        Method method =  result.getMethod().getConstructorOrMethod().getMethod();
        if (TestMethodConfiguration.forMethod(method).isManual()) {
            setToManual(result,method);
        }
    }


    private void setToManual(ITestResult iTestResult,Method methodSource) {
        eventBusFor(iTestResult).testIsManual();
        TestResult result = TestMethodConfiguration.forMethod(methodSource).getManualResult();
        eventBusFor(iTestResult).getBaseStepListener().recordManualTestResult(result);
    }

    @Override
    public void beforeDataProviderExecution(
      IDataProviderMethod dataProviderMethod, ITestNGMethod method, ITestContext iTestContext) {
        logger.info("beforeDataProviderExecution " + dataProviderMethod.getName() + " " + dataProviderMethod.getIndices() +   " methodName: " + method.getMethodName()
                    + " context: " + iTestContext.getName());
        Method testDataMethod =  method.getConstructorOrMethod().getMethod();
        String dataTableName = testDataMethod.getDeclaringClass().getCanonicalName() + "." + testDataMethod.getName();
        dataTable  = dataTables.get(dataTableName);
        if (dataTable == null) {
            NamedDataTable namedDataTable = new TestNGDataDrivenAnnotations().generateDataTableForMethod(dataProviderMethod, method);
            dataTable = namedDataTable.getDataTable();
            dataTables.put(dataTableName, dataTable);
        }
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
      //dataTable = null;
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

    @Override
    public void transform (
        ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        // manipulate annotations if needed
    }

    public static Map<String, DataTable> getDataTables() {
        return dataTables;
    }


}