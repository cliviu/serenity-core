package net.thucydides.junit5samples;

import net.serenitybdd.junit.extensions.*;
import net.thucydides.core.annotations.*;
import net.thucydides.junit.runners.*;
import net.thucydides.samples.*;

import org.junit.jupiter.api.*;
import org.slf4j.*;

@Serenity
public class SamplePassingNonWebScenarioWithPendingTests {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SamplePassingNonWebScenarioWithPendingTests.class);

    @Steps
    public SampleNonWebSteps steps;

    @Test
    public void happy_day_scenario() throws Throwable {
        steps.stepThatSucceeds();
        steps.stepThatIsIgnored();
        steps.anotherStepThatSucceeds();
    }

    @Pending @Test
    public void edge_case_1() {
        steps.stepThatSucceeds();
        steps.anotherStepThatSucceeds();
    }

    @Pending @Test
    public void edge_case_2() {
        steps.stepThatSucceeds();
        steps.anotherStepThatSucceeds();
    }
}
