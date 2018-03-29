package net.thucydides.junit5samples;

import net.serenitybdd.junit.extensions.*;
import net.thucydides.core.annotations.*;
import net.thucydides.junit.runners.*;
import net.thucydides.samples.*;
import org.junit.jupiter.api.*;

@Serenity
public class SamplePassingNonWebScenario {
    
    @Steps
    public SampleNonWebSteps steps;

    @Test
    public void happy_day_scenario() throws Throwable {
        steps.stepThatSucceeds();
        steps.stepThatIsIgnored();
        steps.stepThatIsPending();
        steps.anotherStepThatSucceeds();
    }

    @Test
    public void edge_case_1() {
        steps.stepThatSucceeds();
        steps.anotherStepThatSucceeds();
        steps.stepThatIsPending();
    }

    @Test
    @DisplayName("My Custom Display Name For Edge Case 2")
    public void edge_case_2() {
        steps.stepThatSucceeds();
        steps.anotherStepThatSucceeds();
    }
}
