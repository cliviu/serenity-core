package net.thucydides.model.adapters.testng;


import net.thucydides.model.adapters.JUnitStrategy;
import net.thucydides.model.domain.TestTag;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.stream;

public class TestNGStrategy
    implements JUnitStrategy
{

    @Override
    public boolean isTestClass(final Class<?> testClass) {
        if (hasTestMethods(testClass)) {
            return true;
        }
        if (!hasNestedTestClasses(testClass)) {
            return false;
        }
        return stream(testClass.getDeclaredClasses()).anyMatch(this::isTestClass);

        //JUnit5 nested tests
        //        for (Class innerClass : testClass.getDeclaredClasses()) {
        //            if (isTestClass(innerClass) && isNestedTestClass(innerClass)) {
        //                return true;
        //            }
        //        }
        //        return false;
    }

    private boolean hasNestedTestClasses(final Class<?> testClass) {
        return stream(testClass.getDeclaredClasses()).anyMatch(this::isNestedTestClass);
    }

    private boolean hasTestMethods(final Class<?> testClass) {
        return stream(testClass.getDeclaredMethods()).anyMatch(this::isTestMethod);
    }

    private boolean isNestedTestClass(Class testClass) {
        return false;
        /*return (testClass.getAnnotation(org.junit.jupiter.api.Nested.class) != null);*/
    }

    @Override
    public boolean isTestMethod(final Method method) {
        return (method.getAnnotation(Test.class) != null);
    }

    @Override
    public boolean isTestSetupMethod(final Method method) {
        return (method.getAnnotation(BeforeTest.class) != null)
               || (method.getAnnotation(BeforeClass.class) != null);
    }

    @Override
    public boolean isSerenityTestCase(Class<?> testClass) {
        return stream(testClass.getDeclaredMethods()).anyMatch(this::isTestMethod);
    }

    @Override
    public boolean isIgnored(final Method method) {
        boolean ignored = stream(method.getAnnotations()).anyMatch(annotation -> annotation.annotationType().getName().contains("Ignore"));
        return ignored;
    }

    @Override
    public boolean isAssumptionViolatedException(final Throwable throwable) {
        return (throwable instanceof SkipException);
    }

    @Override
    public boolean isATaggableClass(final Class<?> testClass) {
        // serenity tagging mechanism currently not supported for JUnit 5, since JUnit 5 has its own tagging
        // feature.
        return false;
    }

    @Override
    public Optional<String> getTitleAnnotation(Method testMethod) {
        //Right now, in TestNG there is no way to provide a custom name.
        return Optional.empty();
    }

    @Override
    public List<TestTag> getTagsFor(Method testMethod) {

        List<TestTag> tags = new ArrayList<>();
        //TODO -use groups
        /*for (Annotation currentAnnotation : testMethod.getDeclaredAnnotations()) {
            if (currentAnnotation instanceof WithTag) {
                String name = ((WithTag) currentAnnotation).name();
                String type = ((WithTag) currentAnnotation).type();
                String value = ((WithTag) currentAnnotation).value();
                if (name != null) {
                    tags.add(TestTag.withName(name).andType(type));
                } else {
                    tags.add(TestTag.withValue(value));
                }
            }
            if (currentAnnotation instanceof WithTags) {
                List<TestTag> testTags = stream(((WithTags) currentAnnotation).value()).map(
                    tag -> {
                        String name = tag.name();
                        String type = tag.type();
                        String value = tag.value();
                        if (name != null) {
                            return TestTag.withName(name).andType(type);
                        } else {
                            return TestTag.withValue(value);
                        }
                    }
                ).collect(Collectors.toList());
                tags.addAll(testTags);
            }
            if (currentAnnotation instanceof Tag) {
                tags.add(TestTag.withValue(((Tag) currentAnnotation).value()));
            }
            if (currentAnnotation instanceof Tags) {
                Tag[] allTags = ((Tags) currentAnnotation).value();
                Arrays.stream(allTags).forEach(tag -> tags.add(TestTag.withValue(tag.value())));
            }
        }*/
        return tags;
    }
}