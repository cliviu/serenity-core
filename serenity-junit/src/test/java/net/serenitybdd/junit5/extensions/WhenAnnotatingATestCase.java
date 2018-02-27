package net.serenitybdd.junit5.extensions;

import net.thucydides.core.annotations.*;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class WhenAnnotatingATestCase {

    static class WithManagedAnnotation {
        @Managed
        WebDriver webDriver;
    }

    @Test
    public void a_test_case_with_the_Managed_annotation_should_support_web_tests() {
        assertThat(TestCaseAnnotations.supportsWebTests(WithManagedAnnotation.class), is(true));
    }

    static class WithoutManagedAnnotation {
        WebDriver webDriver;
    }

    @Test
    public void a_test_case_without_the_Managed_annotation_should_not_support_web_tests() {
        assertThat(TestCaseAnnotations.supportsWebTests(WithoutManagedAnnotation.class), is(false));
    }
}
