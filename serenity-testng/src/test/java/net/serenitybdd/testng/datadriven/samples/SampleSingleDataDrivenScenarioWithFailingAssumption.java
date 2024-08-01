package net.serenitybdd.testng.datadriven.samples;


import net.serenitybdd.annotations.Steps;
import net.serenitybdd.testng.SerenityTestNG;
import net.thucydides.samples.SampleScenarioSteps;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@SerenityTestNG
public class SampleSingleDataDrivenScenarioWithFailingAssumption
{

    @Steps
    public SampleScenarioSteps steps;

    @Test(dataProvider = "getParametersForHappyDayScenario")
    public void happy_day_scenario(String option1,int option2) {
        steps.stepWithFailedAssumption();
        steps.stepWithParameters(option1, option2);
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
