/*	
 * $Id: CommunityController.java,v 1.1 2011/10/29 00:34:23 ysjung Exp $
 * created by    : SHIN HYUN SEONG
 * creation-date : 2011. 10. 15.
 * =========================================================
 * Copyright (c) 2011 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.smartworks.service.ISmartWorks;
import net.smartworks.service.impl.SmartWorks;
import net.smartworks.util.SmartUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CommunityController {

	ISmartWorks smartworks;

	@Autowired
	public void setSmartworks(ISmartWorks smartworks) {
		this.smartworks = smartworks;
	}

	@RequestMapping("/department_space")
	public ModelAndView departmentSpace(HttpServletRequest request, HttpServletResponse response) throws Exception {

		return SmartUtil.returnMnv(request, "jsp/content/community/space/department_space.jsp", "department_space.tiles");
	}

	@RequestMapping("/group_space")
	public ModelAndView groupSpace(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/content/community/space/group_space.jsp", "group_space.tiles");
	}

	@RequestMapping("/user_space")
	public ModelAndView userSpace(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/content/community/space/user_space.jsp", "user_space.tiles");
	}

	@RequestMapping("/space_tab_dayly")
	public ModelAndView spaceTabDayly(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/content/community/space/space_tab_dayly.jsp", "space_tab_dayly.tiles");
	}

	@RequestMapping("/space_tab_timeline")
	public ModelAndView spaceTabTimeline(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/content/community/space/space_tab_timeline.jsp", "space_tab_timeline.tiles");
	}

	@RequestMapping("/space_tab_weekly")
	public ModelAndView spaceTabWeekly(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/content/community/space/space_tab_weekly.jsp", "space_tab_weekly.tiles");
	}

	@RequestMapping("/space_tab_monthly")
	public ModelAndView spaceTabMonthly(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/content/community/space/space_tab_monthly.jsp", "space_tab_monthly.tiles");
	}

	@RequestMapping("/chatting_box")
	public ModelAndView chattingBox(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/chatting/chatting_box.jsp", "");
	}

	@RequestMapping("/available_chatter_list")
	public ModelAndView availableChatterList(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/chatting/available_chatter_list.jsp", "");
	}

	@RequestMapping("/update_group_space")
	public ModelAndView updateGroupSpace(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/content/community/space/update_group_space.jsp", "");
	}

	@RequestMapping("/update_department_space")
	public ModelAndView updateDepartmentSpace(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/content/community/space/update_department_space.jsp", "");
	}

	@RequestMapping(value = "/create_new_group", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody Map<String, Object> createNewGroup(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String instanceId = smartworks.setGroup(requestBody, request);
		// TO DO : Exception handler
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("href", "group_space.sw?cid=" + SmartWorks.CONTEXT_PREFIX_GROUP_SPACE + instanceId + "&wid=" + instanceId ); // wid 추가( 그룹 생성 후 멤버가 보이지 않는 현상 수정)
		return map;
	}

	@RequestMapping(value = "/join_group_request", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void joinGroupRequest(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.joinGroupRequest(requestBody, request);
		// TO DO : Exception handler
	}

	@RequestMapping(value = "/invite_group_members", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void inviteGroupMembers(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.inviteGroupMembers(requestBody, request);
	}

	@RequestMapping(value = "/approval_join_group", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void approvalJoinGroup(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.approvalJoinGroup(requestBody, request);
	}

	@RequestMapping(value = "/pushout_group_member", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void pushout_group_member(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.pushoutGroupMember(requestBody, request);
	}

	@RequestMapping(value = "/leave_group", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void leaveGroup(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.leaveGroup(requestBody, request);
	}

	@RequestMapping(value = "/update_group_setting", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void updateGroupSetting(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.updateGroupSetting(requestBody, request);
	}

}