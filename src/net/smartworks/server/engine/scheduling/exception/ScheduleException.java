package net.smartworks.server.engine.scheduling.exception;

import net.smartworks.server.engine.common.exception.SwException;

public class ScheduleException extends SwException {
	private static final long serialVersionUID = 1L;
	public ScheduleException() {
	}
	public ScheduleException(String message) {
		super(message);
	}
	public ScheduleException(Throwable t) {
		super(t);
	}
	public ScheduleException(String message, Throwable t) {
		super(message, t);
	}
}
