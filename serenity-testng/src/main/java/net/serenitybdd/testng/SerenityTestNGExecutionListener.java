package net.serenitybdd.testng;

import org.testng.*;

public class SerenityTestNGExecutionListener implements IExecutionListener,ISuiteListener,ITestListener {

    @Override
    public void onStart(ISuite suite) {
        System.out.println("Starting");
    }


    @Override
    public void onFinish(ISuite suite) {
        System.out.println("Finishing");
    }


    @Override
    public void onExecutionStart() {

    }

    @Override
    public void onExecutionFinish() {

    }

    @Override
    public void onTestStart(ITestResult result) {

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