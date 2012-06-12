/*	
 * $Id$
 * created by    : hsshin
 * creation-date : 2012. 6. 12.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.mail.exception;

import net.smartworks.server.engine.common.exception.SwException;

public class MailException extends SwException {

	private static final long serialVersionUID = 1L;
	public MailException() {
		super();
	}
	public MailException(String message) {
		super(message);
	}
	public MailException(Throwable t) {
		super(t);
	}
	public MailException(String message, Throwable t) {
		super(message, t);
	}

}