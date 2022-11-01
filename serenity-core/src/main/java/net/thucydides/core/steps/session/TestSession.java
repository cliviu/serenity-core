package net.thucydides.core.steps.session;

import net.thucydides.core.steps.StepEventBus;
import net.thucydides.core.steps.events.StepEventBusEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class TestSession {

  	private static final Logger LOGGER = LoggerFactory.getLogger(TestSession.class);

	private static ThreadLocal<TestSessionContext> sessionContext = ThreadLocal.withInitial(()->new TestSessionContext());

	public static void startSession(String sessionId, StepEventBus stepEventBus) {
		sessionContext.get().getSessionStarted().set(true);
		sessionContext.get().setSessionId(sessionId);
		sessionContext.get().setStepEventBus(stepEventBus);
		LOGGER.info("ZZZSessionStart: id " + sessionId);
	}

	public static void closeSession() {
		sessionContext.get().getSessionStarted().set(false);
		LOGGER.info("ZZZSessionEnd: id " + sessionContext.get().getSessionId());
	}

	public static TestSessionContext getTestSessionContext() {
		return sessionContext.get();
	}

	public static void cleanupSession() {
		sessionContext.get().getStepEventBusEvents().clear();
	}

	public static boolean isSessionStarted(){
		return sessionContext.get().getSessionStarted().get();
	}

	public static void addEvent(StepEventBusEvent event) {
		LOGGER.info("ZZZSessionAddEvent: id " + sessionContext.get().getSessionId() + " " + event);
		sessionContext.get().getStepEventBusEvents().add(event);
	}

	public static List<StepEventBusEvent> getSessionEvents() {
		return sessionContext.get().getStepEventBusEvents();
	}
}
