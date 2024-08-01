package net.serenitybdd.testng;



import nl.javadude.assumeng.Assumes;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.is;

@SerenityTestNG
public final class HasAFailingAssumptionInATest {
    @Test
    public void test_with_failing_assumption() {
        Assumes.assumeThat(true, is(false));
    }

    @Test
    public void following_test() {
    }
}