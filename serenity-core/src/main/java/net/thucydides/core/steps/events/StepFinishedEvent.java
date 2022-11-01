package net.thucydides.core.steps.events;

import net.thucydides.core.screenshots.ScreenshotAndHtmlSource;
import net.thucydides.core.steps.StepEventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class StepFinishedEvent extends StepEventBusEventBase {


	private static final Logger LOGGER = LoggerFactory.getLogger(StepFinishedEvent.class);

	private List<ScreenshotAndHtmlSource> screenshotList;

	public StepFinishedEvent(StepEventBus eventBus) {
		super(eventBus);
	}

	public StepFinishedEvent(StepEventBus eventBus, List<ScreenshotAndHtmlSource> screenshotList) {
		super(eventBus);
		this.screenshotList =  screenshotList;
	}


	@Override
	public void play() {
	 	LOGGER.info("ZZZPlayStepFinishedEvent with screenshot size "
	 					+ ((screenshotList != null) ?  screenshotList.size() : 0));
		getStepEventBus().stepFinished(screenshotList);
	}

	public String toString() {
		return("EventBusEvent STEP_FINISHED_EVENT ");
	}
}
