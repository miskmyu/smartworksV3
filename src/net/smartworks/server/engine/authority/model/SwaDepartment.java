/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 7. 20.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.authority.model;

import net.smartworks.server.engine.common.model.MisObject;

public class SwaDepartment extends MisObject {

	public static final String DEPT_AUTHTYPE_EVENT_WRITE = "event.write";
	public static final String DEPT_AUTHTYPE_EVENT_EDIT = "event.edit";
	public static final String DEPT_AUTHTYPE_BOARD_WRITE = "board.write";
	public static final String DEPT_AUTHTYPE_BOARD_EDIT = "board.edit";
	
	public static final String DEPT_ROLEKYE_OWNER = "owner";
	public static final String DEPT_ROLEKYE_ADMIN = "admin";
	public static final String DEPT_ROLEKYE_LEADER = "leader";
	public static final String DEPT_ROLEKYE_MEMBER = "member";
	public static final String DEPT_ROLEKYE_CUSTOM = "custom";

	String deptId = null;
	String deptAuthType = null;
	String roleKey = null;
	String customUser = null;

	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getDeptAuthType() {
		return deptAuthType;
	}
	public void setDeptAuthType(String deptAuthType) {
		this.deptAuthType = deptAuthType;
	}
	public String getRoleKey() {
		return roleKey;
	}
	public void setRoleKey(String roleKey) {
		this.roleKey = roleKey;
	}
	public String getCustomUser() {
		return customUser;
	}
	public void setCustomUser(String customUser) {
		this.customUser = customUser;
	}
	
}
