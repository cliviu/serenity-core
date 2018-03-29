package net.serenitybdd.junit5.samples.integration;

import com.google.inject.*;
import net.serenitybdd.junit.extensions.*;
import net.serenitybdd.junit.runners.*;
import net.serenitybdd.junit5.*;
import net.thucydides.core.annotations.*;
import net.thucydides.core.guice.*;
import net.thucydides.core.model.*;
import net.thucydides.core.steps.*;
import net.thucydides.core.webdriver.*;
import net.thucydides.junit.rules.*;
import net.thucydides.junit5samples.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

import org.mockito.*;

import java.io.*;
import java.util.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class WhenRunningANonWebTestScenario extends AbstractTestStepRunnerTest {

    /*@Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Rule
    public DisableThucydidesHistoryRule disableThucydidesHistoryRule = new DisableThucydidesHistoryRule();*/

    Injector injector;

    @BeforeEach
    public void createATestableDriverFactory() throws Exception {
        MockitoAnnotations.initMocks(this);
        injector = Guice.createInjector(new ThucydidesModule());
        StepEventBus.getEventBus().clear();
    }

    @Test
    @ExtendWith(TemporaryFolderExtension.class)
    public void the_test_runner_records_the_steps_as_they_are_executed() {

        TestLauncher.runTestForClass(SamplePassingNonWebScenario.class);
        List<TestOutcome> executedSteps = StepEventBus.getEventBus().getBaseStepListener().getTestOutcomes();
        assertThat(executedSteps.size(), is(3));


        assertThat(inTheTesOutcomes(executedSteps).theOutcomeFor("happy_day_scenario").getTitle(), is("Happy day scenario"));
        assertThat(inTheTesOutcomes(executedSteps).theOutcomeFor("edge_case_1").getTitle(), is("Edge case 1"));
        assertThat(inTheTesOutcomes(executedSteps).theOutcomeFor("My Custom Display Name For Edge Case 2").getTitle(), is("My Custom Display Name For Edge Case 2"));
    }

    @Test
    public void tests_marked_as_pending_should_be_pending() {

        TestLauncher.runTestForClass(SamplePassingNonWebScenarioWithPendingTests.class);

        List<TestOutcome> executedSteps =  StepEventBus.getEventBus().getBaseStepListener().getTestOutcomes();
        assertThat(executedSteps.size(), is(3));

        assertThat(inTheTesOutcomes(executedSteps).theResultFor("happy_day_scenario"), is(TestResult.SUCCESS));
        assertThat(inTheTesOutcomes(executedSteps).theResultFor("edge_case_1"), is(TestResult.PENDING));
        assertThat(inTheTesOutcomes(executedSteps).theResultFor("edge_case_2"), is(TestResult.PENDING));
    }

    @Test
    public void tests_with_failing_assumptions_should_be_ignored()  {

        TestLauncher.runTestForClass(SampleScenarioWithFailingAssumption.class);

        List<TestOutcome> executedSteps = StepEventBus.getEventBus().getBaseStepListener().getTestOutcomes();
        assertThat(executedSteps.size(), is(3));

        assertThat(inTheTesOutcomes(executedSteps).theResultFor("happy_day_scenario"), is(TestResult.IGNORED));
    }


}
