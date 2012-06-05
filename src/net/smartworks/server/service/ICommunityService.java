package net.smartworks.server.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.smartworks.model.community.Department;
import net.smartworks.model.community.Group;
import net.smartworks.model.community.User;
import net.smartworks.model.community.WorkSpace;
import net.smartworks.model.community.info.CommunityInfo;
import net.smartworks.model.community.info.DepartmentInfo;
import net.smartworks.model.community.info.GroupInfo;
import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.community.info.WorkSpaceInfo;

public interface ICommunityService {

	public abstract UserInfo[] searchCommunityMember(String communityId, String key) throws Exception;

	public abstract WorkSpaceInfo[] searchCommunity(String key, HttpServletRequest request) throws Exception;

	public abstract User getUserById(String userId) throws Exception;

	public abstract Group getGroupById(String groupId) throws Exception;

	public abstract GroupInfo[] getMyGroups() throws Exception;

	public abstract String setGroup(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract Department getDepartmentById(String departmentId) throws Exception;

	public abstract DepartmentInfo[] getMyDepartments() throws Exception;

	public abstract DepartmentInfo[] getMyChildDepartments() throws Exception;

	public abstract WorkSpace getWorkSpaceById(String workSpaceId) throws Exception;

	public abstract WorkSpaceInfo getWorkSpaceInfoById(String workSpaceId) throws Exception;

	public abstract UserInfo[] getAvailableChatter(HttpServletRequest request) throws Exception;

	public abstract UserInfo[] searchAvailableChatter(String key) throws Exception;

	public abstract UserInfo[] searchUser(String key) throws Exception;

	public abstract CommunityInfo[] getMyCommunities() throws Exception;

	public abstract String setMyProfile(HttpServletRequest request) throws Exception;

	public abstract void joinGroupRequest(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void inviteGroupMembers(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void approvalJoinGroup(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void pushoutGroupMember(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void leaveGroup(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract UserInfo[] searchCompanyUser(String key) throws Exception;

	public abstract UserInfo[] searchContact(User currentUser, String key) throws Exception;

	public abstract UserInfo[] searchEmailAddress(String key) throws Exception;

}