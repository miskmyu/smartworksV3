/*	
 * $Id$
 * created by    : hsshin
 * creation-date : 2012. 3. 23.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.like.exception;

import net.smartworks.server.engine.common.exception.SwException;

public class LikeException extends SwException {

	private static final long serialVersionUID = 1L;
	public LikeException() {
		super();
	}
	public LikeException(String message) {
		super(message);
	}
	public LikeException(Throwable t) {
		super(t);
	}
	public LikeException(String message, Throwable t) {
		super(message, t);
	}

}