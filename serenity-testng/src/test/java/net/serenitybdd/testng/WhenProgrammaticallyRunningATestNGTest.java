package net.serenitybdd.testng;

import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.annotations.Test;

public class WhenProgrammaticallyRunningATestNGTest {

    @Test
    public void testRunning(){
        TestListenerAdapter tla = new TestListenerAdapter();
        TestNG testng = new TestNG();
        testng.setTestClasses(new Class[] { HomeTest.class });
        testng.addListener(tla);
        testng.run();
    }
}
