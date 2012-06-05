/*	
 * $Id$
 * created by    : hsshin
 * creation-date : 2012. 6. 1.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.common.loginuser.exception;

import net.smartworks.server.engine.common.exception.SwException;

public class LoginUserException extends SwException {

	private static final long serialVersionUID = 1L;
	public LoginUserException() {
		super();
	}
	public LoginUserException(String message) {
		super(message);
	}
	public LoginUserException(Throwable t) {
		super(t);
	}
	public LoginUserException(String message, Throwable t) {
		super(message, t);
	}

}