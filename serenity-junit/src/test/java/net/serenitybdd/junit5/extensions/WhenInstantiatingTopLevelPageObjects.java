package net.serenitybdd.junit5.extensions;

import net.serenitybdd.junit.extensions.*;
import net.thucydides.core.annotations.*;
import net.thucydides.core.pages.*;
import net.thucydides.junit.runners.*;
import net.thucydides.samples.*;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;

import static org.assertj.core.api.Assertions.*;


@Serenity
public class WhenInstantiatingTopLevelPageObjects {
    
    @Managed(driver = "htmlunit")
    public WebDriver webdriver;

    @ManagedPages(defaultUrl = "classpath:static-site/index.html")
    public Pages pages;
    
    @Steps
    public SampleScenarioSteps steps;

    IndexPage page;

    @Test
    public void shouldInstantiateDeclaredPageObjects() throws Throwable {
        assertThat(page).isNotNull();
    }

    @Test
    public void shouldInstantiateNestedPageObjects() throws Throwable {
        assertThat(page).isNotNull();
        assertThat(page.nestedIndexPage).isNotNull();
    }

}
