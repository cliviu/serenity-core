package net.serenitybdd.testng;

import net.thucydides.core.model.Story;
import net.thucydides.core.steps.BaseStepListener;
import net.thucydides.core.steps.Listeners;
import net.thucydides.core.steps.StepEventBus;
import org.testng.*;

public class SerenityTestNGExecutionListener extends TestListenerAdapter implements IExecutionListener,ISuiteListener,ITestListener {


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
        StepEventBus.getEventBus().testStarted(result.getTestName(),result.getTestClass().getRealClass());
    }

    @Override
    public void onTestSuccess(ITestResult result) {

    }

    @Override
    public void onTestFailure(ITestResult result) {

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
}