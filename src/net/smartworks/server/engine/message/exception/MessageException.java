package net.smartworks.server.engine.message.exception;

import net.smartworks.server.engine.common.exception.SwException;


public class MessageException extends SwException {

	private static final long serialVersionUID = 1L;
	public MessageException() {
		super();
	}
	public MessageException(String message) {
		super(message);
	}
	public MessageException(Throwable t) {
		super(t);
	}
	public MessageException(String message, Throwable t) {
		super(message, t);
	}

}