package net.smartworks.server.engine.sera.exception;

import net.smartworks.server.engine.common.exception.SwException;

public class SeraException extends SwException {
	private static final long serialVersionUID = 1L;
	public SeraException() {
		super();
	}
	public SeraException(String message) {
		super(message);
	}
	public SeraException(String errorCode, String message) {
		super(errorCode, message);
	}
	public SeraException(Throwable t) {
		super(t);
	}
	public SeraException(String message, Throwable t) {
		super(message, t);
	}
	public SeraException(String errorCode, String message, Throwable t) {
		super(errorCode, message, t);
	}
}
