package net.serenitybdd.testng;

import net.serenitybdd.annotations.SingleBrowser;
import net.thucydides.model.domain.TestTag;
import org.testng.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class TestNGTags {
    private static final List<Class<? extends Annotation>> SERENITY_SPECIFIC_ANNOTATIONS = List.of(
            SingleBrowser.class
    );

    private static final Collection<? extends TestTag> NO_TAGS = new ArrayList<>();

    public static List<TestTag> forMethod(Method method) {
        List<TestTag> tags = new ArrayList<>();
        tags.addAll(multipleTagAnnotationsIn(method));
        tags.addAll(customAnnotationsAsTagsIn(method));
        tags.addAll(customAnnotationsAsClassLevelTagsIn(method.getDeclaringClass()));
        return tags;
    }

    private static Collection<? extends TestTag> customAnnotationsAsClassLevelTagsIn(Class<?> declaringClass) {
        List<TestTag> tags = new ArrayList<>();
        while(declaringClass != null) {
            for (Class<? extends Annotation> serenitySpecificAnnotation : SERENITY_SPECIFIC_ANNOTATIONS) {
                if (declaringClass.getAnnotation(serenitySpecificAnnotation) != null) {
                    tags.add(testTagFromAnnotation(serenitySpecificAnnotation));
                }
            }
            declaringClass = declaringClass.getEnclosingClass();
        }
        return tags;
    }

    private static TestTag testTagFromAnnotation(Class<? extends Annotation> serenitySpecificAnnotation) {
        return TestTag.withValue(serenitySpecificAnnotation.getSimpleName().toLowerCase());
    }

    private static Collection<? extends TestTag> customAnnotationsAsTagsIn(Method method) {
        List<TestTag> tags = new ArrayList<>();
        for(Class<? extends Annotation> serenitySpecificAnnotation : SERENITY_SPECIFIC_ANNOTATIONS) {
            if (method.getAnnotation(serenitySpecificAnnotation) != null) {
                tags.add(testTagFromAnnotation(serenitySpecificAnnotation));
            }
        }
        return tags;
    }

    private static Collection<? extends TestTag> multipleTagAnnotationsIn(Method method) {
        Test testAnnotation = method.getAnnotation(Test.class);
        if (testAnnotation != null && testAnnotation.groups() != null && testAnnotation.groups().length >0) {
            String[] groups = testAnnotation.groups();
            return testNgTagsAsSerenityTags(groups);
        }
        return NO_TAGS;
    }


    private static List<TestTag> testNgTagsAsSerenityTags(String[] tags) {
        return Arrays.stream(tags)
                .map(TestTag::withValue)
                .collect(Collectors.toList());
    }
}
