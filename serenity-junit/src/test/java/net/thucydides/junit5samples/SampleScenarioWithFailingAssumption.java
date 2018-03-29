package net.thucydides.junit5samples;

import net.serenitybdd.junit.extensions.*;
import net.thucydides.core.annotations.*;
import net.thucydides.samples.*;
import org.junit.jupiter.api.*;


@Serenity
public class SampleScenarioWithFailingAssumption {
    
    @Steps
    public SampleNonWebSteps steps;

    @Test
    public void happy_day_scenario() throws Throwable {
        steps.stepWithFailingAssumption();
        steps.stepThatSucceeds();
        steps.anotherStepThatSucceeds();
    }

    @Test
    public void edge_case_1() {
        steps.stepThatSucceeds();
        steps.anotherStepThatSucceeds();
        steps.stepThatIsPending();
    }

    @Test
    public void edge_case_2() {
        steps.stepThatSucceeds();
        steps.anotherStepThatSucceeds();
    }
}
