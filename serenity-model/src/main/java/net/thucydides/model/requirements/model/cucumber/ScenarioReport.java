package net.thucydides.model.requirements.model.cucumber;

import io.cucumber.messages.types.Feature;
import net.thucydides.model.domain.ReportNamer;
import net.thucydides.model.domain.ReportType;

public class ScenarioReport {
    private final String scenarioName;

    private ScenarioReport(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    public static ScenarioReport forScenario(String scenarioName) {
        return new ScenarioReport(scenarioName);
    }

    public String inFeature(Feature feature) {
        return ReportNamer.forReportType(ReportType.HTML)
                          .getNormalizedTestReportNameFor(convertToId(feature.getName()) + "_" + convertToId(scenarioName));
    }

    static String convertToId(String name) {
        return name.replaceAll("[\\s'!,]", "-").toLowerCase();
    }
}
