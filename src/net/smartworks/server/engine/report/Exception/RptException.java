package net.smartworks.server.engine.report.Exception;

import net.smartworks.server.engine.common.exception.SwException;

public class RptException extends SwException {
	private static final long serialVersionUID = 1L;
	public RptException() {
		super();
	}
	public RptException(String message) {
		super(message);
	}
	public RptException(String errorCode, String message) {
		super(errorCode, message);
	}
	public RptException(Throwable t) {
		super(t);
	}
	public RptException(String message, Throwable t) {
		super(message, t);
	}
	public RptException(String errorCode, String message, Throwable t) {
		super(errorCode, message, t);
	}
}
