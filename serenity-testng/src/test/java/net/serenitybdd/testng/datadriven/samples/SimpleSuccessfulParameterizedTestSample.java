package net.serenitybdd.testng.datadriven.samples;

import net.serenitybdd.annotations.Steps;
import net.serenitybdd.testng.SerenityTestNG;
import net.thucydides.samples.SampleScenarioSteps;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@SerenityTestNG
public class SimpleSuccessfulParameterizedTestSample
{


    @Steps
    public SampleScenarioSteps steps;


    @Test(dataProvider = "getParametersFortest1")
    public void test1(String option1) {

    }

    @DataProvider
    public Object[][] getParametersFortest1() {
        return new Object[][] { {"A"} ,{ "B"} , {"C"} };
    }

    @Test(dataProvider = "getParametersFortest2")
    public void test2(String option1) {

    }

    @DataProvider
    public Object[][] getParametersFortest2() {
        return new Object[][] { {"D"}, {"E"} ,{"F"},{"H"} };
    }

}
