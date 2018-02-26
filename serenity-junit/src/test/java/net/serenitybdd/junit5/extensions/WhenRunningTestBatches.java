package net.serenitybdd.junit5.extensions;

import net.serenitybdd.junit.runners.*;
import net.thucydides.core.model.*;
import net.thucydides.core.steps.*;
import net.thucydides.core.util.*;
import net.thucydides.core.webdriver.*;
import net.thucydides.junit.rules.*;
import net.thucydides.junit.runners.AbstractTestStepRunnerTest;
import net.thucydides.samples.*;
import org.junit.*;
import org.junit.platform.launcher.*;
import org.junit.platform.launcher.core.*;
import org.junit.rules.*;
import org.junit.runner.notification.*;
import org.junit.runners.model.*;
import org.mockito.*;
import org.openqa.selenium.firefox.*;

import java.util.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectMethod;

public class WhenRunningTestBatches extends AbstractTestStepRunnerTest {

    @Mock
    FirefoxDriver firefoxDriver;

    MockEnvironmentVariables environmentVariables;


    WebDriverFactory webDriverFactory;

    @Before
    public void createATestableDriverFactory() throws Exception {
        MockitoAnnotations.initMocks(this);
        environmentVariables = new MockEnvironmentVariables();
        webDriverFactory = new WebDriverFactory(environmentVariables);
        StepEventBus.getEventBus().clear();
    }

    @Test
    public void the_test_runner_records_the_steps_as_they_are_executed() throws InitializationError {

        runTestForClass(net.serenitybdd.junit5.samples.SamplePassingScenario.class);

        List<TestOutcome> executedSteps = StepEventBus.getEventBus().getBaseStepListener().getTestOutcomes();
        assertThat(executedSteps.size(), is(3));

        assertThat(inTheTesOutcomes(executedSteps).theOutcomeFor("happy_day_scenario()").getTestSteps().size(), is(4));
        assertThat(inTheTesOutcomes(executedSteps).theOutcomeFor("edge_case_1()").getTestSteps().size(), is(3));
        assertThat(inTheTesOutcomes(executedSteps).theOutcomeFor("edge_case_2()").getTestSteps().size(), is(2));
    }

    private void runTestForClass(Class testClass){
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectClass(testClass))
                .build();
        LauncherFactory.create().execute(request);
    }

}