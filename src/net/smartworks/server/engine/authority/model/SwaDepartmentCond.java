/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 7. 20.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.authority.model;

import net.smartworks.server.engine.common.model.MisObjectCond;

public class SwaDepartmentCond extends MisObjectCond {

	String deptId = null;
	String deptAuthType = null;
	String deptAuthTypeLike = null;
	String roleKey = null;
	String roleKeyLike = null;
	String customUser = null;
	String customUserLikek = null;

	public String getDeptAuthTypeLike() {
		return deptAuthTypeLike;
	}
	public void setDeptAuthTypeLike(String deptAuthTypeLike) {
		this.deptAuthTypeLike = deptAuthTypeLike;
	}
	public String getRoleKeyLike() {
		return roleKeyLike;
	}
	public void setRoleKeyLike(String roleKeyLike) {
		this.roleKeyLike = roleKeyLike;
	}
	public String getCustomUserLikek() {
		return customUserLikek;
	}
	public void setCustomUserLikek(String customUserLikek) {
		this.customUserLikek = customUserLikek;
	}
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
