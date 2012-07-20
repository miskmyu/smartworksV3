package net.smartworks.server.engine.autoindex.exception;

import net.smartworks.server.engine.common.exception.SwException;

public class AutoIndexException extends SwException {
	private static final long serialVersionUID = 1L;
	public AutoIndexException() {
		super();
	}
	public AutoIndexException(String message) {
		super(message);
	}
	public AutoIndexException(Throwable t) {
		super(t);
	}
	public AutoIndexException(String message, Throwable t) {
		super(message, t);
	}
}
