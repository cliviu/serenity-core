package net.serenitybdd.cucumber.outcomes.parallel

import io.cucumber.junit.CucumberJUnit5ParallelRunner
import net.serenitybdd.cucumber.integration.FailingScenario
import net.thucydides.core.guice.Injectors
import net.thucydides.core.model.TestOutcome
import net.thucydides.core.model.TestResult
import net.thucydides.core.model.TestStep
import net.thucydides.core.reports.OutcomeFormat
import net.thucydides.core.reports.TestOutcomeLoader
import net.thucydides.core.webdriver.Configuration
import org.assertj.core.util.Files
import spock.lang.Specification

import static io.cucumber.junit.CucumberRunner.serenityRunnerForCucumberTestRunner

class WhenCreatingSerenityTestOutcomesInParallel extends Specification {

    static File outputDirectory

    def setup() {
        outputDirectory = Files.newTemporaryFolder()
    }

    def cleanup() {
        outputDirectory.deleteDir()
    }

    def "should record failures for a failing scenario"() {
        given:
            Configuration configuration = Injectors.getInjector().getProvider(Configuration.class).get();
            configuration.setOutputDirectory(outputDirectory);
        when:
            CucumberJUnit5ParallelRunner.runFileFromClasspathInParallel("samples/failing_scenario.feature","net.serenitybdd.cucumber.integration.steps");
            List<TestOutcome> recordedTestOutcomes = new TestOutcomeLoader().forFormat(OutcomeFormat.JSON).loadFrom(outputDirectory).sort { it.name };
            TestOutcome testOutcome = recordedTestOutcomes[0]
            List<TestStep> stepResults = testOutcome.testSteps.collect { step -> step.result }
            System.out.println("XXXStepResults " + stepResults);
        then:
            testOutcome.result == TestResult.FAILURE
        and:
            stepResults == [TestResult.SUCCESS, TestResult.SUCCESS, TestResult.SUCCESS, TestResult.FAILURE, TestResult.SKIPPED]
    }


    def "should generate a well-structured Serenity test outcome for each executed Cucumber scenario"() {
        given:
        Configuration configuration = Injectors.getInjector().getProvider(Configuration.class).get();
        configuration.setOutputDirectory(outputDirectory);

        when:
        CucumberJUnit5ParallelRunner.runFileFromClasspathInParallel("samples/simple_scenario.feature","net.serenitybdd.cucumber.integration.steps");

        def recordedTestOutcomes = new TestOutcomeLoader().forFormat(OutcomeFormat.JSON).loadFrom(outputDirectory).sort { it.name };
        def testOutcome = recordedTestOutcomes[0]
        def steps = testOutcome.testSteps.collect { step -> step.description }

        then:
        testOutcome.title == "A simple scenario"
    }

    def "should record results for each step"() {
        given:
              Configuration configuration = Injectors.getInjector().getProvider(Configuration.class).get();
        configuration.setOutputDirectory(outputDirectory);

        when:
        CucumberJUnit5ParallelRunner.runFileFromClasspathInParallel("samples/simple_scenario.feature","net.serenitybdd.cucumber.integration.steps");
        def recordedTestOutcomes = new TestOutcomeLoader().forFormat(OutcomeFormat.JSON).loadFrom(outputDirectory).sort { it.name };
        def testOutcome = recordedTestOutcomes[0]

        then:
        testOutcome.result == TestResult.SUCCESS

        and:
        testOutcome.testSteps.collect { step -> step.result } == [TestResult.SUCCESS, TestResult.SUCCESS, TestResult.SUCCESS, TestResult.SUCCESS]
    }


}