package net.serenitybdd.testng;

import net.thucydides.core.steps.StepEventBus;
import net.thucydides.model.domain.TestOutcome;
import net.thucydides.model.domain.TestResult;
import net.thucydides.model.environment.MockEnvironmentVariables;
import org.junit.Before;
/*import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;*/

import java.util.List;
import java.util.stream.Collectors;

//import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

public abstract class AbstractTestNgStepRunnerTest {

    protected MockEnvironmentVariables environmentVariables;

    public AbstractTestNgStepRunnerTest() {
        super();
    }

    @Before
    public void initEnvironment() {
        environmentVariables = new MockEnvironmentVariables();
    }

    public TestOutcomeChecker inTheTestOutcomes(List<TestOutcome> testOutcomes) {
        return new TestOutcomeChecker(testOutcomes);
    }

    public static class TestOutcomeChecker {
        private final List<TestOutcome> testOutcomes;

        public TestOutcomeChecker(List<TestOutcome> testOutcomes) {
            this.testOutcomes = testOutcomes;
        }


        public TestOutcome theOutcomeFor(String methodName) {
            return matchingTestOutcomeCalled(methodName);
        }

        public TestResult theResultFor(String methodName) {
            return matchingTestOutcomeCalled(methodName).getResult();
        }

        private TestOutcome matchingTestOutcomeCalled(String methodName) {
            for (TestOutcome testOutcome : testOutcomes) {
                if (testOutcome.getName().equals(methodName)) {
                    return testOutcome;
                }
            }
            throw new AssertionError("No matching test method called " + methodName);
        }
    }

    public void runTestForClass(Class testClass){
        SerenityTestNGStarter.runTestClass(testClass);
    }


    public static TestOutcome getTestOutcomeFor(String testName) {
        return StepEventBus.getEventBus().getBaseStepListener().getTestOutcomes().stream().filter(ta->ta.getName().equals(testName)).collect(Collectors.toList()).get(0);
    }
}
