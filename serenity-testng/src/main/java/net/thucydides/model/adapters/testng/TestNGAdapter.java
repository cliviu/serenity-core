package net.thucydides.model.adapters.testng;

import net.thucydides.model.adapters.TestStrategyAdapter;
import net.thucydides.model.domain.TestTag;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

public class TestNGAdapter implements TestStrategyAdapter
{

    private final TestNGStrategy testNGStrategy;

    public TestNGAdapter() {
        testNGStrategy = new TestNGStrategy();
    }

    public boolean isTestClass(final Class<?> testClass) {
        if (testClass == null) {
            return false;
        }
        return testNGStrategy.isTestClass(testClass);
    }

    public boolean isTestMethod(final Method method) {
        if (method == null) {
            return false;
        }
        return testNGStrategy.isTestMethod(method);
    }

    public boolean isTestSetupMethod(final Method method) {
        if (method == null) {
            return false;
        }
        return testNGStrategy.isTestSetupMethod(method);
    }

    public boolean isSerenityTestCase(final Class<?> testClass) {
        if (testClass == null) {
            return false;
        }
        return testNGStrategy.isSerenityTestCase(testClass);
    }

    public boolean isAssumptionViolatedException(final Throwable throwable) {
        return testNGStrategy.isAssumptionViolatedException(throwable);
    }

    public boolean isATaggableClass(final Class<?> testClass) {
        if (testClass == null) {
            return false;
        }
        return testNGStrategy.isATaggableClass(testClass);
    }

    public boolean isIgnored(final Method method) {
        if (method == null) {
            return false;
        }
        return testNGStrategy.isIgnored(method);
    }

    public Optional<String> getTitleAnnotation(Method testMethod) {
        return testNGStrategy.getTitleAnnotation(testMethod);
    }

    public List<TestTag> getTagsFor(Method testMethod) {
        return testNGStrategy.getTagsFor(testMethod);
    }

    @Override
    public Double priority() {
        return 5.7;
    }
}



