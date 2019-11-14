package net.serenitybdd.testng;


import net.thucydides.core.configuration.WebDriverConfiguration;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.model.TestResult;
import net.thucydides.core.util.MockEnvironmentVariables;
import net.thucydides.core.webdriver.DriverConfiguration;
import net.thucydides.core.webdriver.WebDriverFactory;
import org.testng.annotations.BeforeTest;

import java.util.List;

public abstract class AbstractTestStepRunnerTest {

    protected MockEnvironmentVariables environmentVariables;

    public AbstractTestStepRunnerTest() {
        super();
    }

    @BeforeTest
    public void initEnvironment() {
        environmentVariables = new MockEnvironmentVariables();
    }

    protected SerenityTestNGRunner getTestRunnerUsing(Class<?> testClass)  {
        DriverConfiguration configuration = new WebDriverConfiguration(environmentVariables);
        WebDriverFactory factory = new WebDriverFactory(environmentVariables);
        return new SerenityTestNGRunner(testClass, factory, configuration);
    }

    public TestOutcomeChecker inTheTesOutcomes(List<TestOutcome> testOutcomes) {
        return new TestOutcomeChecker(testOutcomes);
    }

    public class TestOutcomeChecker {
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
}
