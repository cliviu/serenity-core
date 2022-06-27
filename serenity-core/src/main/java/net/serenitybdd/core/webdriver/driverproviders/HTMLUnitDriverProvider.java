//package net.serenitybdd.core.webdriver.driverproviders;
//
//import net.serenitybdd.core.buildinfo.DriverCapabilityRecord;
//import net.serenitybdd.core.di.WebDriverInjectors;
//import net.serenitybdd.core.webdriver.driverproviders.webdrivermanager.WebDriverManagerSetup;
//import net.thucydides.core.fixtureservices.FixtureProviderService;
//import net.thucydides.core.steps.StepEventBus;
//import net.thucydides.core.util.EnvironmentVariables;
//import net.thucydides.core.webdriver.CapabilityEnhancer;
//import net.thucydides.core.webdriver.SupportedWebDriver;
//import net.thucydides.core.webdriver.stubs.WebDriverStub;
//import org.openqa.selenium.Capabilities;
//import org.openqa.selenium.MutableCapabilities;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.htmlunit.HtmlUnitDriver;
//import org.openqa.selenium.remote.DesiredCapabilities;
//import org.openqa.selenium.safari.SafariDriver;
//import org.openqa.selenium.safari.SafariOptions;
//
///**
// * HTMLUnit provider.
// * Experimental: used mostly for testing.
// */
//public class HTMLUnitDriverProvider implements DriverProvider {
//
//    private final DriverCapabilityRecord driverProperties;
//
//    private final FixtureProviderService fixtureProviderService;
//
//    public HTMLUnitDriverProvider(FixtureProviderService fixtureProviderService) {
//        this.fixtureProviderService = fixtureProviderService;
//        this.driverProperties = WebDriverInjectors.getInjector().getInstance(DriverCapabilityRecord.class);
//    }
//
//    @Override
//    public WebDriver newInstance(String options, EnvironmentVariables environmentVariables) {
//        if (StepEventBus.getEventBus().webdriverCallsAreSuspended()) {
//            return new WebDriverStub();
//        }
//
//        CapabilityEnhancer enhancer = new CapabilityEnhancer(environmentVariables, fixtureProviderService);
//        MutableCapabilities capabilities = new DesiredCapabilities();
//        capabilities.setCapability("browserName","htmlunit");
//        capabilities = enhancer.enhanced(capabilities, SupportedWebDriver.HTMLUNIT);
//
//        WebDriver driver = new HtmlUnitDriver(capabilities);
//        driverProperties.registerCapabilities("htmlunit", capabilitiesToProperties(capabilities));
//        return driver;
//    }
//}
