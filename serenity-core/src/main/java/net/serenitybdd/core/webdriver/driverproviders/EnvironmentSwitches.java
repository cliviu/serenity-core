package net.serenitybdd.core.webdriver.driverproviders;

import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.thucydides.core.ThucydidesSystemProperty;
import net.thucydides.core.util.EnvironmentVariables;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumOptions;

import java.util.List;
import java.util.Optional;

import static net.thucydides.core.ThucydidesSystemProperty.HEADLESS_MODE;

public class EnvironmentSwitches {
    private final EnvironmentVariables environmentVariables;

    EnvironmentSwitches(EnvironmentVariables environmentVariables) {
        this.environmentVariables = environmentVariables;
    }

    public static EnvironmentSwitches from(EnvironmentVariables environmentVariables) {
        return new EnvironmentSwitches(environmentVariables);
    }

    public void addEnvironmentSwitchesTo(ChromiumOptions<?> options) {

        List<String> arguments = DriverArgs.fromProperty(ThucydidesSystemProperty.CHROME_SWITCHES).configuredIn(environmentVariables);

        if (!arguments.isEmpty()) {
            options.addArguments(arguments);
        }

        Optional<String> headless = EnvironmentSpecificConfiguration.from(environmentVariables).getOptionalProperty(HEADLESS_MODE);
        if (headless.isPresent() && Boolean.parseBoolean(headless.get())) {
            options.addArguments("--headless");
        }
    }
}
