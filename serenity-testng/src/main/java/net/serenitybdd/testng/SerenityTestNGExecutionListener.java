package net.serenitybdd.testng;

import org.testng.IExecutionListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;

public class SerenityTestNGExecutionListener implements IExecutionListener,ISuiteListener {

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
}