package net.serenitybdd.testng;

import net.thucydides.core.model.Story;
import net.thucydides.core.steps.BaseStepListener;
import net.thucydides.core.steps.Listeners;
import net.thucydides.core.steps.StepEventBus;
import org.junit.runner.Description;
import org.testng.*;

import java.util.Optional;

import static net.thucydides.core.steps.TestSourceType.TEST_SOURCE_JUNIT;
import static net.thucydides.core.steps.TestSourceType.TEST_SOURCE_TESTNG;

public class SerenityTestNGExecutionListener extends TestListenerAdapter implements IExecutionListener,ISuiteListener,ITestListener {


    private boolean testStarted;

    public SerenityTestNGExecutionListener() {
       // BaseStepListener baseStepListener = Listeners.getBaseStepListener().withOutputDirectory(outputDirectory);
        
    }


    @Override
    public void onStart(ISuite suite) {
        System.out.println("Starting Suite");
        StepEventBus.getEventBus().testSuiteStarted(Story.called(suite.getName()));
    }


    @Override
    public void onFinish(ISuite suite) {
        System.out.println("Finishing Suite");
        StepEventBus.getEventBus().testSuiteFinished();
    }


    @Override
    public void onExecutionStart() {

    }

    @Override
    public void onExecutionFinish() {

    }

    @Override
    public void onTestStart(ITestResult result) {
        stepEventBus().clear();
        stepEventBus().setTestSource(TEST_SOURCE_TESTNG.getValue());
        stepEventBus().testStarted(result.getTestName(),result.getTestClass().getRealClass());
        startTest();
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        if (testingThisTest(result)) {
            // TODO updateResultsUsingTestAnnotations(description);
            stepEventBus().testFinished();
            stepEventBus().setTestSource(null);
            endTest();
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        if (testingThisTest(result)) {
            //TODO startTestIfNotYetStarted(failure.getDescription());
            stepEventBus().testFailed(result.getThrowable());
            //TODO updateFailureList(failure);
            endTest();
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }

    @Override
    public void onStart(ITestContext context) {

    }

    @Override
    public void onFinish(ITestContext context) {

    }

    StepEventBus stepEventBus() {
        return  StepEventBus.getEventBus();
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
}