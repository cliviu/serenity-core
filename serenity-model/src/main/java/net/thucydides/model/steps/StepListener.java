package net.thucydides.model.steps;


import net.thucydides.model.domain.DataTable;
import net.thucydides.model.domain.Story;
import net.thucydides.model.domain.TestOutcome;
import net.thucydides.model.domain.TestResult;
import net.thucydides.model.screenshots.ScreenshotAndHtmlSource;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

/**
 * Represents a class interested in knowing about test execution flow and results.
 */
public interface StepListener {

    /**
     * Start a test run using a test case or a user story.
     * For JUnit tests, the test case should be provided. The test case should be annotated with the
     * Story annotation to indicate what user story it tests. Otherwise, the test case itself will
     * be treated as a user story.
     * For easyb stories, the story class can be provided directly.
     */
    void testSuiteStarted(final Class<?> storyClass);

    /**
     * Start a test run using a specific story, without a corresponding Java class.
     */
    void testSuiteStarted(final Story story);

    /**
     * Start a test run using JUnit 5 with a specific name
     */
    default void testSuiteStarted(Class<?> testClass, String testCaseName) {
        testSuiteStarted(Story.called(testCaseName));
    }

    /**
     * End of a test case or story.
     */
    void testSuiteFinished();

    /**
     * A test with a given name has started.
     */
    void testStarted(final String description);

    default void testStarted(final String description, ZonedDateTime startTime) {
        testStarted(description);
    }

    void testStarted(final String description, final String id);

    default void testStarted(final String testName, String testMethod, final String id, String scenarioId) {
        testStarted(testName, id);
    }

    void testStarted(final String description, final String id, ZonedDateTime startTime);

    /**
     * Called when a test finishes.
     */
    void testFinished(final TestOutcome result);

    default void testFinished(final TestOutcome result, boolean isInDataDrivenTest) {
        testFinished(result);
    }

    void testFinished(final TestOutcome result, boolean isInDataDrivenTest, ZonedDateTime finishTime);

    /**
     * The last test run is about to be restarted
     */
    void testRetried();

    /**
     * Called when a test step is about to be started.
     *
     * @param description the description of the test that is about to be run
     *                    (generally a class and method name)
     */
    void stepStarted(final ExecutedStepDescription description);

    default void stepStarted(final ExecutedStepDescription description, ZonedDateTime startTime) {
        stepStarted(description);
    }

    /**
     * Called when a test step is about to be started, but this step is scheduled to be skipped.
     *
     * @param description the description of the test that is about to be run
     *                    (generally a class and method name)
     */
    void skippedStepStarted(final ExecutedStepDescription description);

    /**
     * Called when a test step fails.
     *
     * @param failure describes the test that failed and the exception that was thrown
     */
    void stepFailed(final StepFailure failure);

    /**
     * Called when a test step fails.
     *
     * @param failure            describes the test that failed and the exception that was thrown
     * @param screenshotList     list of screenshots
     * @param isInDataDrivenTest if the step failed was called from a data driven test
     */
    default void stepFailed(final StepFailure failure,
                    List<ScreenshotAndHtmlSource> screenshotList,
                    boolean isInDataDrivenTest) {
        stepFailed(failure, screenshotList, isInDataDrivenTest, ZonedDateTime.now());
    }

    void stepFailed(final StepFailure failure,
                            List<ScreenshotAndHtmlSource> screenshotList,
                            boolean isInDataDrivenTest,
                            ZonedDateTime timestamp);

    /**
     * Declare that a step has failed after it has finished.
     */
    void lastStepFailed(StepFailure failure);

    /**
     * Called when a step will not be run, generally because a test method is annotated
     * with <code>org.junit.Ignore<code/>.
     */
    void stepIgnored();

    /**
     * The step is marked as pending.
     */
    void stepPending();

    /**
     * The step is marked as pending with a descriptive message.
     *
     * @param message
     */
    void stepPending(String message);

    /**
     * Called when an test step has finished successfully
     */
    void stepFinished();

    default void stepFinished(List<ScreenshotAndHtmlSource> screenshotList) {
        stepFinished(screenshotList, ZonedDateTime.now());
    }

    void stepFinished(List<ScreenshotAndHtmlSource> screenshotList, ZonedDateTime time);

    /**
     * The test failed, but not while executing a step.
     *
     * @param testOutcome The test outcome structure for the failing test
     * @param cause       The exception that triggered the failure
     */
    void testFailed(TestOutcome testOutcome, final Throwable cause);

    /**
     * The test as a whole was ignored.
     */
    void testIgnored();

    /**
     * The test as a whole was skipped.
     */
    void testSkipped();

    /**
     * The test as a whole was aborted.
     */
    default void testAborted() {
    }

    /**
     * The test as a whole should be marked as 'pending'.
     */
    void testPending();

    void testIsManual();

    void notifyScreenChange();

    /**
     * The current scenario is a data-driven scenario using test data from the specified table.
     */
    void useExamplesFrom(DataTable table);

    /**
     * If multiple tables are used, this method will add any new rows to the test data
     */
    void addNewExamplesFrom(DataTable table);

    /**
     * A new example has just started.
     */
    void exampleStarted(Map<String, String> data);

    default void exampleStarted(Map<String, String> data, ZonedDateTime time) {
        exampleStarted(data, ZonedDateTime.now());
    }

    default void exampleStarted(Map<String, String> data, String exampleName) {
        exampleStarted(data);
    }

    default void exampleStarted(Map<String, String> data, String exampleName, ZonedDateTime time) {
        exampleStarted(data);
    }

    /**
     * An example has finished.
     */
    void exampleFinished();

    void assumptionViolated(String message);

    void testRunFinished();

    void takeScreenshots(List<ScreenshotAndHtmlSource> screenshots);

    void takeScreenshots(TestResult testResult, List<ScreenshotAndHtmlSource> screenshots);

    /**
     * Records an arbitrary screenshot stored as a byte[]
     * @param screenshotName - screenshot destination file name. It should be a valid file name.
     * @param screenshot - screenshot represented as byte[.
     */
    void recordScreenshot(String screenshotName, byte[] screenshot);
}
