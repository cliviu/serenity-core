package net.thucydides.core.steps.session;

import net.thucydides.core.steps.events.StepEventBusEvent;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class TestSessionContext {

	private String sessionId;

	private AtomicBoolean sessionStarted = new AtomicBoolean(false);

	private List<StepEventBusEvent> stepEventBusEvents = Collections.synchronizedList(new LinkedList<>());

	public AtomicBoolean getSessionStarted() {
		return sessionStarted;
	}

	public List<StepEventBusEvent> getStepEventBusEvents() {
		return stepEventBusEvents;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getSessionId() {
		return sessionId;
	}
}
