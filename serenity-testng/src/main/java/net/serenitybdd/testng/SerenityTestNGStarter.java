package net.serenitybdd.testng;

import org.testng.CommandLineArgs;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;

import java.util.HashMap;
import java.util.Map;

public class SerenityTestNGStarter {
    public static void runTestClass(Class... testClasses) {
        TestListenerAdapter tla = new TestListenerAdapter();
        TestNG testng = new TestNG();
        Map configMap = new HashMap();
        //configMap.put(CommandLineArgs.TEST_RUNNER_FACTORY,"net.serenitybdd.testng.SerenityTestNGRunnerFactory");
        CommandLineArgs commandLineArgs = new CommandLineArgs();
        testng.configure(configMap);
        testng.setTestClasses(testClasses);
        //testng.addListener(tla);
        testng.run();
    }
}
