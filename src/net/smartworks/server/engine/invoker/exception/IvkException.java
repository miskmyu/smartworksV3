/*	
 * $Id$
 * created by    : hsshin
 * creation-date : 2012. 3. 7.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.invoker.exception;

import net.smartworks.server.engine.common.exception.SwException;

public class IvkException extends SwException {
	private static final long serialVersionUID = 1L;
	public IvkException() {
		super();
	}
	public IvkException(String message) {
		super(message);
	}
	public IvkException(Throwable t) {
		super(t);
	}
	public IvkException(String message, Throwable t) {
		super(message, t);
	}

}