package net.thucydides.samples;

import net.serenitybdd.annotations.Manual;

import net.serenitybdd.testng.SerenityTestNG;
import net.thucydides.model.domain.TestResult;
import org.testng.annotations.Test;




@SerenityTestNG
public class SamplePassingNonWebScenarioWithManualTests {
    
    @Test
    @Manual
    public void a_manual_test() {}

    @Test
    @Manual(result = TestResult.FAILURE)
    public void a_failing_manual_test() {}
}
