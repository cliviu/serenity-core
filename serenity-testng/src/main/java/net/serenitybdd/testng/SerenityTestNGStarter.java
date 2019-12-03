package net.serenitybdd.testng;

import org.testng.TestListenerAdapter;
import org.testng.TestNG;

import java.util.HashMap;
import java.util.Map;

public class SerenityTestNGStarter {
    public static void runTestClass(Class... testClasses) {
        TestListenerAdapter tla = new TestListenerAdapter();
        TestNG testng = new TestNG();
        Map configMap = new HashMap();
        configMap.put("-testrunfactory","net.serenitybdd.testng.SerenityTestNGRunnerFactory");
        testng.configure(configMap);
        testng.setTestClasses(testClasses);
        testng.addListener(tla);
        testng.run();
    }
}
