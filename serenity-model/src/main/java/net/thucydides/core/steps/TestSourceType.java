package net.thucydides.core.steps;

public enum TestSourceType {

    TEST_SOURCE_JUNIT("JUnit"),TEST_SOURCE_JBEHAVE("JBehave"),TEST_SOURCE_CUCUMBER("Cucumber"),TEST_SOURCE_TESTNG("TestNG");

    private final String value;

    TestSourceType(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
