package net.serenitybdd.testng;

import org.junit.Assume;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.is;

@SerenityTestNG
public final class HasAFailingAssumptionInATest {
    @Test
    public void test_with_failing_assumption() {
        Assume.assumeThat(true, is(false));
    }

    @Test
    public void following_test() {
    }
}