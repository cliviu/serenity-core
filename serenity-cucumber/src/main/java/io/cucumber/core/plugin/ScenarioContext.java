package io.cucumber.core.plugin;


import io.cucumber.core.plugin.events.StepEventBusEvent;
import io.cucumber.messages.types.Examples;
import io.cucumber.messages.types.Scenario;
import io.cucumber.messages.types.Step;
import io.cucumber.messages.types.Tag;
import io.cucumber.plugin.event.TestCase;
import io.cucumber.plugin.event.TestStep;
import net.thucydides.core.model.DataTable;
import net.thucydides.core.model.DataTableRow;
import net.thucydides.core.model.TestTag;
import net.thucydides.core.steps.BaseStepListener;
import net.thucydides.core.steps.StepEventBus;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.toList;



class ScenarioContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScenarioContext.class);
    //private final Queue<Step> stepQueue = new LinkedList<>();

    private Map<UUID,LinkedList<StepEventBusEvent>> stepEventBusEvents = Collections.synchronizedMap(new HashMap<>());

    private final Map<UUID,Queue<Step>> stepQueue = Collections.synchronizedMap(new HashMap<>());
    private final Map<UUID,Queue<TestStep>> testStepQueue = Collections.synchronizedMap(new HashMap<>());

    private boolean examplesRunning;
    private boolean addingScenarioOutlineSteps = false;
    private DataTable table;

    //keys are line numbers, entries are example rows (key=header, value=rowValue )
    private Map<Long, Map<String, String>> exampleRows;

    //keys are line numbers
    private Map<Long, List<Tag>> exampleTags;

    private AtomicInteger exampleCount = new AtomicInteger(0);

    private boolean waitingToProcessBackgroundSteps = false;

    private String currentScenarioId;

    private Scenario currentScenarioDefinition;

    private String currentScenario;

    private List<Tag> featureTags = new ArrayList<>();

    //private URI currentFeaturePath;

    private FeaturePathFormatter featurePathFormatter = new FeaturePathFormatter();


    private final List<BaseStepListener> baseStepListeners;

    private final List<BaseStepListener> parameterizedBaseStepListeners;

    private URI featureURI;


    public ScenarioContext(){
        this.baseStepListeners = Collections.synchronizedList(new ArrayList<>());
        this.parameterizedBaseStepListeners = Collections.synchronizedList(new ArrayList<>());
    }

    public synchronized void setFeatureURI(URI featureURI){
        this.featureURI = featureURI;
    }

    /*public synchronized void currentFeaturePathIs(URI featurePath) {
        currentFeaturePath = featurePath;
    }*/

    public synchronized Scenario currentScenarioOutline() {
        return  currentScenarioDefinition;
    }

    /*public synchronized URI currentFeaturePath() {
        return currentFeaturePath;
    }*/

    public synchronized Queue<Step> getStepQueue(TestCase testCase) {
        stepQueue.computeIfAbsent(testCase.getId(), k -> new LinkedList<>());
        return stepQueue.get(testCase.getId());
    }

    public synchronized Queue<TestStep> getTestStepQueue(TestCase testCase) {
        testStepQueue.computeIfAbsent(testCase.getId(), k -> new LinkedList<>());
        return testStepQueue.get(testCase.getId());
    }

    public synchronized boolean examplesAreRunning() {
        return examplesRunning;
    }

    public synchronized Map<Long, Map<String, String>> getExampleRows() {
        return exampleRows;
    }

    public synchronized void setExampleRows(Map<Long, Map<String, String>> exampleRows) {
        this.exampleRows = exampleRows;
    }

    public synchronized Map<Long, List<Tag>> getExampleTags() {
        return exampleTags;
    }

    public synchronized void setExampleTags(Map<Long, List<Tag>> exampleTags) {
        this.exampleTags =  exampleTags;
    }

    public synchronized int getExampleCount() {
        return exampleCount.get();
    }

    public synchronized int decrementExampleCount() {
        return exampleCount.decrementAndGet();
    }

    public synchronized DataTable getTable() {
        return table;
    }

    public synchronized boolean isWaitingToProcessBackgroundSteps() {
        return waitingToProcessBackgroundSteps;
    }



    public synchronized void setCurrentScenarioId(String scenarioId) {
        currentScenarioId = scenarioId;
    }

    public synchronized Scenario getCurrentScenarioDefinition() {
        return currentScenarioDefinition;
    }

    public synchronized String getCurrentScenario() {
        return currentScenario;
    }

    public synchronized void setCurrentScenario(String currentScenario) {
        this.currentScenario = currentScenario;
    }

    public synchronized List<Tag> getFeatureTags() {
        return featureTags;
    }

    public synchronized boolean isAddingScenarioOutlineSteps() {
        return addingScenarioOutlineSteps;
    }

    public synchronized void doneAddingScenarioOutlineSteps() {
        this.addingScenarioOutlineSteps = false;
    }

    public synchronized void setFeatureTags(List<Tag> tags) {
        this.featureTags = new ArrayList<>(tags);
    }

    public synchronized void setCurrentScenarioDefinitionFrom(TestSourcesModel.AstNode astNode) {
        this.currentScenarioDefinition = TestSourcesModel.getScenarioDefinition(astNode);
    }

    public synchronized boolean isAScenarioOutline() {
        return currentScenarioDefinition.getExamples().size() > 0;
    }

    public synchronized void startNewExample(URI featurePath, TestCase testCase) {

        examplesRunning = true;
        addingScenarioOutlineSteps = true;
    }

    public synchronized void setExamplesRunning(boolean examplesRunning) {
        this.examplesRunning = examplesRunning;
    }

    public synchronized List<Tag> getScenarioTags() {
        return currentScenarioDefinition.getTags();
    }

    public synchronized String getScenarioName() {
        return currentScenarioDefinition.getName();
    }

    public synchronized List<Examples> getScenarioExamples() {
        return currentScenarioDefinition.getExamples();
    }

    public synchronized void clearStepQueue(TestCase testCase) {
        getStepQueue(testCase).clear();
    }

    public synchronized void clearStepQueue() {
        //TODO check
        stepQueue.clear();
    }

    public synchronized void clearTestStepQueue() {
        testStepQueue.clear();
    }

    public synchronized void queueStep(TestCase testCase,Step step) {
        getStepQueue(testCase).add(step);
    }

    public synchronized void queueTestStep(TestCase testCase,TestStep testStep) {
        getTestStepQueue(testCase).add(testStep);
    }

    public synchronized Step getCurrentStep(TestCase testCase) {
        return getStepQueue(testCase).peek();
    }

    public synchronized Step nextStep(TestCase testCase) {
        return getStepQueue(testCase).poll();
    }

    public synchronized TestStep nextTestStep(TestCase testCase) {
        return getTestStepQueue(testCase).poll();
    }

    public synchronized boolean noStepsAreQueued(TestCase testCase) {
        return getStepQueue(testCase).isEmpty();
    }

    public synchronized boolean hasScenarioId(String scenarioId) {
        return (currentScenarioId != null) && (currentScenarioId.equals(scenarioId));
    }

    public synchronized void setTable(DataTable table) {
        this.table = table;
        exampleCount.set(table.getSize());
    }

    public synchronized void addTableRows(List<String> headers,
                             List<Map<String, String>> rows,
                             String name,
                             String description,
                             Map<Integer, Long> lineNumbersOfEachRow) {
        table.startNewDataSet(name, description);

        AtomicInteger rowNumber = new AtomicInteger();
        rows.forEach(
                row -> table.appendRow(newRow(headers, lineNumbersOfEachRow, rowNumber.getAndIncrement(), row))
        );
        table.updateLineNumbers(lineNumbersOfEachRow);
        exampleCount.set(table.getSize());
    }

    @NotNull
    private DataTableRow newRow(List<String> headers,
                                Map<Integer, Long> lineNumbersOfEachRow,
                                int rowNumber,
                                Map<String, String> row) {
        return new DataTableRow(
                rowValuesFrom(headers, row),
                lineNumbersOfEachRow.getOrDefault(rowNumber, 0L));
    }

    private List<String> rowValuesFrom(List<String> headers, Map<String, String> row) {
        return headers.stream().map(row::get).collect(toList());
    }

    public synchronized void addTableTags(List<TestTag> tags) {
        table.addTagsToLatestDataSet(tags);
    }

    public synchronized void clearTable() {
        table = null;
    }

    public synchronized StepEventBus stepEventBus(URI featurePath) {
        URI prefixedPath = featurePathFormatter.featurePathWithPrefixIfNecessary(featurePath);
        System.out.println("XXXPrefixedPath " + prefixedPath);
        return StepEventBus.eventBusFor(prefixedPath);
    }

    public synchronized StepEventBus stepEventBus(TestCase testCase) {

        URI prefixedPath = featurePathFormatter.featurePathWithPrefixIfNecessary(testCase.getUri());
        return StepEventBus.eventBusFor(prefixedPath);
        //return StepEventBus.eventBusFor(testCase);
    }

    /*public synchronized List<BaseStepListener> getBaseStepListeners()
    {
        return baseStepListeners;
    }*/

    public void addBaseStepListener(BaseStepListener baseStepListener,TestCase testCase){
        System.out.println("WWWAddBaseStepListener " + baseStepListener);
        baseStepListeners.add(baseStepListener);
        stepEventBus(testCase).registerListener(baseStepListener);
    }


    public void addBaseStepListener(BaseStepListener baseStepListener,URI featurePath){
        System.out.println("WWWAddBaseStepListener " + baseStepListener);
        baseStepListeners.add(baseStepListener);
        stepEventBus(featurePath).registerListener(baseStepListener);
    }




    public synchronized void collectAllBaseStepListeners(List<BaseStepListener>  allBaseStepListeners){
        allBaseStepListeners.addAll(baseStepListeners);
    }


    public void setWaitingToProcessBackgroundSteps(boolean waitingToProcessBackgroundSteps) {
        this.waitingToProcessBackgroundSteps = waitingToProcessBackgroundSteps;
    }

    public void addParameterizedStepListener(BaseStepListener baseStepListener) {
        System.out.println("WWWAddParameterizedBaseStepListener " + baseStepListener);
        parameterizedBaseStepListeners.add(baseStepListener);
    }

    public void addStepEventBusEvent(TestCase testCase, StepEventBusEvent event) {
        LinkedList<StepEventBusEvent> eventList = stepEventBusEvents.computeIfAbsent(testCase.getId(),k->new LinkedList<>());
        eventList.add(event);
    }

    public void playAllStepEventBusEvents(TestCase testCase){
        LinkedList<StepEventBusEvent> stepEventBusEvents = this.stepEventBusEvents.get(testCase.getId());
        for(StepEventBusEvent currentStepBusEvent : stepEventBusEvents) {
            LOGGER.info("ZZZ PLAY event  " + currentStepBusEvent + " " +  Thread.currentThread());
            currentStepBusEvent.play();
        }
    }
}

