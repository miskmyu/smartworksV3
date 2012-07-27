/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 7. 20.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.authority.model;

import net.smartworks.server.engine.common.model.MisObjectCond;

public class SwaGroupCond extends MisObjectCond {

	public static final String GROUP_AUTHTYPE_EVENT_WRITE = "event.write";
	public static final String GROUP_AUTHTYPE_EVENT_EDIT = "event.edit";
	public static final String GROUP_AUTHTYPE_BOARD_WRITE = "board.write";
	public static final String GROUP_AUTHTYPE_BOARD_EDIT = "board.edit";
	public static final String GROUP_AUTHTYPE_MEMBER_INVITE = "member.invite";
	
	public static final String GROUP_ROLEKYE_OWNER = "owner";
	public static final String GROUP_ROLEKYE_ADMIN = "admin";
	public static final String GROUP_ROLEKYE_LEADER = "leader";
	public static final String GROUP_ROLEKYE_MEMBER = "member";
	public static final String GROUP_ROLEKYE_CUSTOM = "custom";

	String groupId = null;
	String groupAuthType = null;
	String groupAuthTypeLike = null;
	String roleKey = null;
	String roleKeyLike = null;
	String customUser = null;
	String customUserLike = null;
	String adminOrCustomUserLike = null;
	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getGroupAuthType() {
		return groupAuthType;
	}
	public void setGroupAuthType(String groupAuthType) {
		this.groupAuthType = groupAuthType;
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
	public String getGroupAuthTypeLike() {
		return groupAuthTypeLike;
	}
	public void setGroupAuthTypeLike(String groupAuthTypeLike) {
		this.groupAuthTypeLike = groupAuthTypeLike;
	}
	public String getRoleKeyLike() {
		return roleKeyLike;
	}
	public void setRoleKeyLike(String roleKeyLike) {
		this.roleKeyLike = roleKeyLike;
	}
	public String getCustomUserLike() {
		return customUserLike;
	}
	public void setCustomUserLike(String customUserLike) {
		this.customUserLike = customUserLike;
	}
	public String getAdminOrCustomUserLike() {
		return adminOrCustomUserLike;
	}
	public void setAdminOrCustomUserLike(String adminOrCustomUserLike) {
		this.adminOrCustomUserLike = adminOrCustomUserLike;
	}

	
}
