package net.serenitybdd.testng.datadriven.samples;

import net.serenitybdd.annotations.Steps;
import net.serenitybdd.testng.SerenityTestNG;
import net.thucydides.samples.SampleScenarioSteps;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

@SerenityTestNG
public class SampleDataDrivenIgnoredScenario
{

    @Steps
    public SampleScenarioSteps steps;
        

    @Ignore
    @Test(dataProvider = "getParametersForIgnoredScenario")
    public void ignored_scenario(String option1,int option2) {
        steps.stepWithParameters(option1,option2);
    }

    @DataProvider
    public Object[][] getParametersForIgnoredScenario() {
        return new Object[][] {
            { "a", 1 },
            { "B", 2 },
            { "C", 3 },
            { "D", 4 },
            { "e", 5 },
            };
    }

    @DataProvider
    public Object[][] getParametersForHappyDayScenario() {
        return new Object[][] {
            { "B", 2 },
            { "c", 3 },
            { "D", 4 },
            { "e", 5 },
            };
    }

}
