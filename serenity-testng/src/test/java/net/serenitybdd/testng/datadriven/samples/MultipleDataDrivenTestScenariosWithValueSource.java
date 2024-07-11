package net.serenitybdd.testng.datadriven.samples;

import net.serenitybdd.annotations.Steps;
import net.serenitybdd.testng.SerenityTestNG;
import net.thucydides.samples.SampleScenarioSteps;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

@SerenityTestNG
public class MultipleDataDrivenTestScenariosWithValueSource {

    @Steps
    public SampleScenarioSteps steps;

    @Test(dataProvider="stringDataProvider")
    void withValueSource(String word) {
        steps.stepThatSucceeds();
        steps.anotherStepThatSucceeds();
        assertNotNull(word);
    }

    @DataProvider
	public Object[][] stringDataProvider() {
	 return new Object[][] {
	   { "Hello" },
	   { "JUnit"},
	 };
	}


	@Test(dataProvider="intDataProvider")
    void withValueSourceIntegers(int number) {
        steps.stepThatSucceeds();
    }

    @DataProvider
	public Object[][] intDataProvider() {
	 return new Object[][] {
	   { 1 },
	   { 2 },
	   { 3 },
	 };
	}


}
