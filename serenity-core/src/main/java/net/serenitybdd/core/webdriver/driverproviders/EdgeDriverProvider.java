package net.serenitybdd.core.webdriver.driverproviders;

import net.serenitybdd.core.buildinfo.DriverCapabilityRecord;
import net.serenitybdd.core.di.WebDriverInjectors;
import net.serenitybdd.core.webdriver.servicepools.ChromeServicePool;
import net.serenitybdd.core.webdriver.servicepools.DriverServicePool;
import net.thucydides.core.fixtureservices.FixtureProviderService;
import net.thucydides.core.steps.StepEventBus;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.webdriver.capabilities.W3CCapabilities;
import net.thucydides.core.webdriver.stubs.WebDriverStub;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.edge.EdgeOptions;

/**
 * Create a new instance of a ChromeDriver driver, using the configuration options defined in serenity.conf
 * and/or in the custom fixture classes.
 */
public class EdgeDriverProvider extends DownloadableDriverProvider implements DriverProvider {

    private final DriverCapabilityRecord driverProperties;

    private final DriverServicePool<ChromeDriverService> driverServicePool = new ChromeServicePool();

    private final FixtureProviderService fixtureProviderService;

    public EdgeDriverProvider(FixtureProviderService fixtureProviderService) {
        this.fixtureProviderService = fixtureProviderService;
        this.driverProperties = WebDriverInjectors.getInjector().getInstance(DriverCapabilityRecord.class);
    }

    @Override
    public WebDriver newInstance(String options, EnvironmentVariables environmentVariables) {
        // If webdriver calls are suspended no need to create a new driver
        if (StepEventBus.getEventBus().webdriverCallsAreSuspended()) {
            return new WebDriverStub();
        }
        // Download the driver using WebDriverManager if required
        downloadDriverIfRequired("edge", environmentVariables);
        //
        // Update the binary path if necessary
        //
        UpdateDriverEnvironmentProperty.forDriverProperty(EdgeDriverService.EDGE_DRIVER_EXE_PROPERTY);
        //
        // Load the EdgeDriver capabilities from the serenity.conf file
        //
        EdgeOptions edgeOptions = W3CCapabilities.definedIn(environmentVariables).withPrefix("webdriver.capabilities").edgeOptions();
        //
        // Add any arguments passed from the test itself
        //
        edgeOptions.addArguments(argumentsIn(options));
        //
        // Check for extended classes to add extra ChromeOptions configuration
        //
        EdgeOptions enhancedOptions = ConfigureChromiumOptions.from(environmentVariables).into(edgeOptions);
        EnhanceCapabilitiesWithFixtures.using(fixtureProviderService).into(edgeOptions);
        //
        // Record the driver capabilities for reporting
        //
        driverProperties.registerCapabilities("edge", capabilitiesToProperties(enhancedOptions));

        return new EdgeDriver(enhancedOptions);
    }

}
