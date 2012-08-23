/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 8. 23.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.common.menuitem.model;

import net.smartworks.server.engine.common.model.MisObject;

public class FormChange extends MisObject {

	public static final String A_OLDFORMID = "oldFormId";
	public static final String A_NEWFORMID = "newFormId";
	
	private String oldFormId;
	private String newFormId;
	
	public String getOldFormId() {
		return oldFormId;
	}
	public void setOldFormId(String oldFormId) {
		this.oldFormId = oldFormId;
	}
	public String getNewFormId() {
		return newFormId;
	}
	public void setNewFormId(String newFormId) {
		this.newFormId = newFormId;
	}
}
