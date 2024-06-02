package net.serenitybdd.testng.sampletests;

import net.serenitybdd.annotations.Pending;
import net.serenitybdd.testng.SerenityTestNG;
import org.testng.annotations.Test;

@SerenityTestNG
public final class APendingTest {
    @Test
    public void previous_test() {
    }

    @Pending
    @Test
    public void a_pending_test() {
    }

    @Test
    public void following_test() {
    }
}