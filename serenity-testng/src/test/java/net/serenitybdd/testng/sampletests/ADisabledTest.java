package net.serenitybdd.testng.sampletests;

import net.serenitybdd.testng.SerenityTestNG;
import org.testng.annotations.Test;

@SerenityTestNG
public final class ADisabledTest {
    @Test
    public void previous_test() {
    }

    @Test(enabled = false)
    public void a_disabled_test() {
    }

    @Test
    public void following_test() {
    }
}
