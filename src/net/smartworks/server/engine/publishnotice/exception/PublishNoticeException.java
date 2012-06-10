package net.smartworks.server.engine.publishnotice.exception;

import net.smartworks.server.engine.common.exception.SwException;

public class PublishNoticeException extends SwException {
	private static final long serialVersionUID = 1L;
	public PublishNoticeException() {
	}
	public PublishNoticeException(String message) {
		super(message);
	}
	public PublishNoticeException(Throwable t) {
		super(t);
	}
	public PublishNoticeException(String message, Throwable t) {
		super(message, t);
	}
}
