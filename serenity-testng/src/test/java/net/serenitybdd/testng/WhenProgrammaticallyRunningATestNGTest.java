package net.serenitybdd.testng;

import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class WhenProgrammaticallyRunningATestNGTest {

    @Test
    public void testRunning(){
        TestListenerAdapter tla = new TestListenerAdapter();
        TestNG testng = new TestNG();
        Map configMap = new HashMap();
        configMap.put("-testrunfactory","net.serenitybdd.testng.SerenityTestNGRunnerFactory");
        testng.configure(configMap);
        testng.setTestClasses(new Class[] { HomeTest.class });
        testng.addListener(tla);
        testng.run();
    }
}
