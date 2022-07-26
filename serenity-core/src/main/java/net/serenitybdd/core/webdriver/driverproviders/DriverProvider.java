package net.serenitybdd.core.webdriver.driverproviders;

import net.thucydides.core.ThucydidesSystemProperty;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.util.Properties;

public interface DriverProvider {

    default WebDriver newInstance(String options) throws MalformedURLException {
        return newInstance(options, SystemEnvironmentVariables.currentEnvironmentVariables());
    }
    WebDriver newInstance(String options, EnvironmentVariables environmentVariables);

    default Properties capabilitiesToProperties(Capabilities capabilities) {
        return CapabilitiesConverter.capabilitiesToProperties(capabilities);
    }

    default boolean isDriverAutomaticallyDownloaded(EnvironmentVariables environmentVariables) {
        return ThucydidesSystemProperty.WEBDRIVER_AUTODOWNLOAD.booleanFrom(environmentVariables, true);
    }
}
