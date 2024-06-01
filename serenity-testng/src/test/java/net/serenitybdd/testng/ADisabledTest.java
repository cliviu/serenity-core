package net.serenitybdd.testng;

import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

@SerenityTestNG
public final class ADisabledTest {
    @Test
    public void previous_test() {
    }

    //@Ignore
    @Test(enabled = false)
    //https://github.com/junit-team/testng-engine/issues/3
    public void a_disabled_test() {
    }

    @Test
    public void following_test() {
    }
}
