/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 8. 2.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.model.community;

import net.smartworks.server.engine.common.exception.SwException;

public class OverflowGroupMemberException extends SwException {

	private static final long serialVersionUID = 1L;
	public OverflowGroupMemberException() {
		super();
	}
	public OverflowGroupMemberException(String message) {
		super(message);
	}
	public OverflowGroupMemberException(Throwable t) {
		super(t);
	}
	public OverflowGroupMemberException(String message, Throwable t) {
		super(message, t);
	}

}