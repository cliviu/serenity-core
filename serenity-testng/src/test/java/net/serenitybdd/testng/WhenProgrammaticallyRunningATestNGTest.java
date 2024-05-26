package net.serenitybdd.testng;

import org.junit.Test;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
//import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class WhenProgrammaticallyRunningATestNGTest {


    //public  WhenProgrammaticallyRunningATestNGTest(String name){}

    @Test
    public void testRunning() {
        SerenityTestNGStarter.runTestClass(HomeTest.class);
    }


}
