package net.serenitybdd.junit5.samples;

import net.serenitybdd.junit.extensions.*;
import net.serenitybdd.junit.runners.*;
import net.thucydides.core.annotations.*;
import net.thucydides.samples.*;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;

@Serenity
@WithTag("module:M1")
public class SamplePassingScenario {
    
    @Managed(driver = "firefox")
    public WebDriver webdriver;

    @Steps
    public SampleScenarioSteps steps;

    @Test
    @WithTagValuesOf({"story:simple scenario", "iteration:I1"})
    public void happy_day_scenario() throws Throwable {
        steps.stepThatSucceeds();
        steps.stepThatIsIgnored();
        steps.stepThatIsPending();
        steps.anotherStepThatSucceeds();
    }

    @Test
    @WithTagValuesOf({"story:simple scenario", "iteration:I1"})
    public void edge_case_1() {
        steps.stepThatSucceeds();
        steps.anotherStepThatSucceeds();
        steps.stepThatIsPending();
    }

    @Test
    @WithTagValuesOf({"story:simple scenario", "iteration:I2"})
    public void edge_case_2() {
        steps.stepThatSucceeds();
        steps.anotherStepThatSucceeds();
    }
}
