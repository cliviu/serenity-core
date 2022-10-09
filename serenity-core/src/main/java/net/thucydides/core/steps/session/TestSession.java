package net.thucydides.core.steps.session;

import net.thucydides.core.steps.events.StepEventBusEvent;

import java.util.List;


public class TestSession {

	private static ThreadLocal<TestSessionContext> sessionContext = ThreadLocal.withInitial(()->new TestSessionContext());

	public static void startSession() {
		sessionContext.get().getSessionStarted().set(true);
	}

	public static void closeSession() {
		sessionContext.get().getSessionStarted().set(false);
	}

	public static void cleanupSession() {
		sessionContext.get().getStepEventBusEvents().clear();
	}

	public static boolean isSessionStarted(){
		return sessionContext.get().getSessionStarted().get();
	}

	public static void addEvent(StepEventBusEvent event) {
		sessionContext.get().getStepEventBusEvents().add(event);
	}

	public static List<StepEventBusEvent> getSessionEvents() {
		return sessionContext.get().getStepEventBusEvents();
	}
}
