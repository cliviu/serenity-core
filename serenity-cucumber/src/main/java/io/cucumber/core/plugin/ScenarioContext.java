package io.cucumber.core.plugin;


import net.thucydides.core.steps.events.StepEventBusEvent;
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
import net.thucydides.core.steps.session.TestSession;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.toList;



class ScenarioContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScenarioContext.class);

    private Map<UUID,List<StepEventBusEvent>> stepEventBusEvents = Collections.synchronizedMap(new HashMap<>());
    private final Map<UUID,Queue<Step>> stepQueue = Collections.synchronizedMap(new HashMap<>());
    private final Map<UUID,Queue<TestStep>> testStepQueue = Collections.synchronizedMap(new HashMap<>());

    //private boolean examplesRunning;
    private Map<String,Boolean> examplesRunningMap = Collections.synchronizedMap(new HashMap<>());
    //private boolean addingScenarioOutlineSteps = false;
    private Map<String,Boolean> addingScenarioOutlineStepsMap = Collections.synchronizedMap(new HashMap<>());
    private Map<String,DataTable> tableMap = Collections.synchronizedMap(new HashMap<>());;

    //keys are line numbers, entries are example rows (key=header, value=rowValue )
    //private Map<Long, Map<String, String>> exampleRows;
    private Map<String,Map<Long, Map<String, String>>> exampleRowsMap = Collections.synchronizedMap(new HashMap<>());

    //keys are line numbers
    private Map<Long, List<Tag>> exampleTags;

    //private AtomicInteger exampleCount = new AtomicInteger(0);
    private Map<String,AtomicInteger> exampleCountMap = Collections.synchronizedMap(new HashMap<>());

    private boolean waitingToProcessBackgroundSteps = false;

    //private String currentScenarioId;
    //private Map<UUID,String> currentScenarioIdMap = Collections.synchronizedMap(new HashMap<>());
    private List<String> currentScenarioIdList = Collections.synchronizedList(new ArrayList<>());

    //private Scenario currentScenarioDefinition;
    private Map<String,Scenario> currentScenarioDefinitionMap = Collections.synchronizedMap(new HashMap<>());

    //private String currentScenario;
    private Map<String,String> currentScenarioMap =  Collections.synchronizedMap(new HashMap<>());;

    private List<Tag> featureTags = new ArrayList<>();

    //private URI currentFeaturePath;

    private FeaturePathFormatter featurePathFormatter = new FeaturePathFormatter();


    private final List<BaseStepListener> baseStepListeners;

    private final List<BaseStepListener> parameterizedBaseStepListeners;

    private URI featureURI;

    //ThreadLocal<List<StepEventBusEvent>> stepEventBusEvents = ThreadLocal.withInitial(()->Collections.synchronizedList(new LinkedList<>()));


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

    public synchronized Scenario currentScenarioOutline(String scenarioId) {
        return currentScenarioDefinitionMap.get(scenarioId);
    }

    /*public synchronized URI currentFeaturePath() {
        return currentFeaturePath;
    }*/

    public synchronized Queue<Step> getStepQueue(TestCase testCase) {
        stepQueue.computeIfAbsent(testCase.getId(), k -> new LinkedList<>());
        return stepQueue.get(testCase.getId());
        //return simpleStepQueue;
    }

    public synchronized Queue<TestStep> getTestStepQueue(TestCase testCase) {
        testStepQueue.computeIfAbsent(testCase.getId(), k -> new LinkedList<>());
        return testStepQueue.get(testCase.getId());
        //return simpleStepTestQueue;
    }

    public synchronized boolean examplesAreRunning(String scenarioId) {
        //LOGGER.info("ZZZEx1 ExamplesAreRunning " + Thread.currentThread() + " " + scenarioId + " " + examplesRunning);
        //return examplesRunning;
        if(!examplesRunningMap.containsKey(scenarioId)) {
            LOGGER.info("ZZZEx1 ExamplesAreRunning " + Thread.currentThread() + " " + scenarioId + " " + false);
            return false;
        }
        LOGGER.info("ZZZEx1 ExamplesAreRunning " + Thread.currentThread() + " " + scenarioId + " " + examplesRunningMap.get(scenarioId));
        return examplesRunningMap.get(scenarioId);
    }

    public synchronized Map<Long, Map<String, String>> getExampleRows(String scenarioId) {
        return exampleRowsMap.get(scenarioId);
    }

    public synchronized void setExampleRows(String scenarioId,Map<Long, Map<String, String>> exampleRows) {
        this.exampleRowsMap.put(scenarioId,exampleRows);
    }

    public synchronized Map<Long, List<Tag>> getExampleTags() {
        return exampleTags;
    }

    public synchronized void setExampleTags(Map<Long, List<Tag>> exampleTags) {
        this.exampleTags =  exampleTags;
    }

    public synchronized int getExampleCount(String scenarioId)  {
        LOGGER.info("ZZZEx getExampleCount " + scenarioId + " " +   Thread.currentThread() + " " + exampleCountMap.get(scenarioId));
        if(exampleCountMap.containsKey(scenarioId))
            return exampleCountMap.get(scenarioId).get();
        else
            return 0;
    }

    public synchronized int decrementExampleCount(String scenarioId) {
        LOGGER.info("ZZZEx decrement ExampleCount " +scenarioId + " " +   Thread.currentThread() + " " + exampleCountMap.get(scenarioId));
        if(exampleCountMap.get(scenarioId) != null) {
            return exampleCountMap.get(scenarioId).decrementAndGet();
        }
        else //single example
        return 0;
    }

    public synchronized DataTable getTable(String scenarioId) {
        return tableMap.get(scenarioId);
    }

    public synchronized boolean isWaitingToProcessBackgroundSteps() {
        return waitingToProcessBackgroundSteps;
    }



    public synchronized void addCurrentScenarioId(String scenarioId) {

        LOGGER.info("ZZZEx SetCurrentScenario called " + Thread.currentThread() + " " + scenarioId);
        if(scenarioId != null)
            currentScenarioIdList.add(scenarioId);
        else
           currentScenarioIdList.clear();
    }

    public synchronized Scenario getCurrentScenarioDefinition(String scenarioId) {
        return currentScenarioDefinitionMap.get(scenarioId);
    }

    public synchronized String getCurrentScenario(String scenarioId) {
        LOGGER.info("ZZZGetCurrentScenario called " + Thread.currentThread() + " " + currentScenarioMap.get(scenarioId));
        return currentScenarioMap.get(scenarioId);
    }

    public synchronized void setCurrentScenario(String scenarioId,String currentScenario) {
        LOGGER.info("SetCurrentScenario called " + Thread.currentThread() + " " + currentScenario);
        this.currentScenarioMap.put(scenarioId,currentScenario);
    }

    public synchronized List<Tag> getFeatureTags() {
        return featureTags;
    }

    public synchronized boolean isAddingScenarioOutlineSteps(String scenarioId) {
        return addingScenarioOutlineStepsMap.get(scenarioId) != null ? addingScenarioOutlineStepsMap.get(scenarioId) : false;
    }

    public synchronized void doneAddingScenarioOutlineSteps(String scenarioId) {
        this.addingScenarioOutlineStepsMap.put(scenarioId,false);
    }

    public synchronized void setFeatureTags(List<Tag> tags) {
        this.featureTags = new ArrayList<>(tags);
    }

    public synchronized void setCurrentScenarioDefinitionFrom(String scenarioId,TestSourcesModel.AstNode astNode) {
        this.currentScenarioDefinitionMap.put(scenarioId, TestSourcesModel.getScenarioDefinition(astNode));
    }

    public synchronized boolean isAScenarioOutline(String scenarioId) {
        return currentScenarioDefinitionMap.get(scenarioId) != null  &&
                currentScenarioDefinitionMap.get(scenarioId).getExamples().size() > 0;
    }

    public synchronized void startNewExample(URI featurePath, String scenarioId) {

        //examplesRunning = true;
        LOGGER.info("ZZZEx1 startNewExample " + Thread.currentThread() + " " + scenarioId);
        examplesRunningMap.put(scenarioId,true);
        addingScenarioOutlineStepsMap.put(scenarioId,true);
    }

    public synchronized void setExamplesRunning(String scenarioId,boolean examplesRunning) {
        examplesRunningMap.put(scenarioId, examplesRunning);
        LOGGER.info("ZZZEx1 setExamplesAreRunning " + examplesRunning + " " +  Thread.currentThread() + " " + scenarioId);
        //this.examplesRunning = examplesRunning;
    }

    /*public synchronized List<Tag> getScenarioTags() {
        return currentScenarioDefinition.getTags();
    }

    public synchronized String getScenarioName() {
        return currentScenarioDefinition.getName();
    }

    public synchronized List<Examples> getScenarioExamples() {
        return currentScenarioDefinition.getExamples();
    }*/

    public synchronized void clearStepQueue(TestCase testCase) {
        getStepQueue(testCase).clear();
    }

    public synchronized void clearStepQueue() {
        //TODO check
        stepQueue.clear();
        //simpleStepQueue.clear();
    }

    public synchronized void clearTestStepQueue() {
        testStepQueue.clear();
        //simpleStepTestQueue.clear();
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
        boolean hasScenario =  (currentScenarioIdList.contains(scenarioId));
        LOGGER.info("ZZZEx HasCurrentScenarioId called " + Thread.currentThread() + " " + scenarioId + " has scenario " + hasScenario + " " + currentScenarioIdList);
        return hasScenario;
    }

    public synchronized void setTable(String scenarioId,DataTable table) {
        this.tableMap.put(scenarioId,table);
        LOGGER.info("ZZZEx setTable " + table + " " + scenarioId + " " + Thread.currentThread() + " " + table.getSize());
        exampleCountMap.put(scenarioId,new AtomicInteger(table.getSize()));
        //exampleCount.set(table.getSize());
    }

    public synchronized void addTableRows(String scenarioId,List<String> headers,
                             List<Map<String, String>> rows,
                             String name,
                             String description,
                             Map<Integer, Long> lineNumbersOfEachRow) {
        DataTable table = tableMap.get(scenarioId);
        table.startNewDataSet(name, description);

        AtomicInteger rowNumber = new AtomicInteger();
        rows.forEach(
                row -> table.appendRow(newRow(headers, lineNumbersOfEachRow, rowNumber.getAndIncrement(), row))
        );
        table.updateLineNumbers(lineNumbersOfEachRow);
        LOGGER.info("ZZZEx addTableRows " + table + " " + scenarioId + Thread.currentThread() + " " + table.getSize());
        exampleCountMap.put(scenarioId,new AtomicInteger(table.getSize()));
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

    public synchronized void addTableTags(String scenarioId,List<TestTag> tags) {
        DataTable table = tableMap.get(scenarioId);
        table.addTagsToLatestDataSet(tags);
    }

    public synchronized void clearTable(TestCase testCase) {
        tableMap.clear();
        //tableMap.put(testCase.getId(),null);
        //DataTable table = tableMap.get(testCase.getId());
        //table = null;
    }

    public synchronized StepEventBus stepEventBus(URI featurePath) {
        URI prefixedPath = featurePathFormatter.featurePathWithPrefixIfNecessary(featurePath);
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
        List<StepEventBusEvent> eventList = stepEventBusEvents.computeIfAbsent(testCase.getId(),k->Collections.synchronizedList(new LinkedList<>()));
        eventList.add(event);
        if(TestSession.isSessionStarted()) {

            TestSession.addEvent(event);
        } else {
            LOGGER.info("ZZZ ignored event " + event + " " +  Thread.currentThread() + " because session not opened for test case id " + testCase.getId());
        }
        //stepEventBusEvents.get().add(event);
    }

    public void playAllStepEventBusEvents(TestCase testCase){
        /*List<StepEventBusEvent> stepEventBusEvents = this.stepEventBusEvents.get(testCase.getId());
        for(StepEventBusEvent currentStepBusEvent : stepEventBusEvents) {
            LOGGER.info("ZZZ PLAY event  " + currentStepBusEvent + " " +  Thread.currentThread());
            currentStepBusEvent.play();
        }*/
        if(TestSession.isSessionStarted()) {
             TestSession.closeSession();
             List<StepEventBusEvent> stepEventBusEvents = TestSession.getSessionEvents();
             for(StepEventBusEvent currentStepBusEvent : stepEventBusEvents) {
                LOGGER.info("ZZZ PLAY session event  " + currentStepBusEvent + " " +  Thread.currentThread());
                currentStepBusEvent.play();
            }
            TestSession.cleanupSession();
            stepEventBusEvents.clear();
        }
    }
}

