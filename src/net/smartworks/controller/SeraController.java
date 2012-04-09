/*	
 * $Id: ContentController.java,v 1.1 2011/10/29 00:34:23 ysjung Exp $
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

import net.smartworks.server.engine.common.util.CommonUtil;
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
public class SeraController {
	
	ISmartWorks smartworks;

	@Autowired
	public void setSmartworks(ISmartWorks smartworks) {
		this.smartworks = smartworks;
	}

	@RequestMapping("/seraCampus")
	public ModelAndView seraCampus(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "sera/jsp/layouts.jsp", "seraCampus.tiles");
	}

	@RequestMapping("/logins")
	public ModelAndView logins(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mnv = new ModelAndView();
		String type = CommonUtil.toNotNull(request.getParameter("type"));
		mnv.addObject("type", type);
		mnv.setViewName("sera/jsp/login.jsp");
		return mnv;
	}

	@RequestMapping("/joinUs")
	public ModelAndView joinUs(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "", "sera/jsp/joinus.jsp");
	}

	@RequestMapping("/myPAGE")
	public ModelAndView myPAGE(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "sera/jsp/content/my_page.jsp", "myPAGE.tiles");
	}

	@RequestMapping("/myProfile")
	public ModelAndView myProfile(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "sera/jsp/content/my_profile.jsp", "");
	}

	@RequestMapping("/Course")
	public ModelAndView Course(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "sera/jsp/content/course.jsp", "Course.tiles");
	}

	@RequestMapping("/createCourse")
	public ModelAndView createCourse(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "sera/jsp/content/course/create.jsp", "createCourse.tiles");
	}

	@RequestMapping("/myCourses")
	public ModelAndView myCourses(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "sera/jsp/content/course/my_courses.jsp", "myCourses.tiles");
	}

	@RequestMapping("/courseHome")
	public ModelAndView courseHome(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "sera/jsp/content/course/home.jsp", "courseHome.tiles");
	}

	@RequestMapping("/courseInstanceList")
	public ModelAndView courseInstanceList(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "sera/jsp/content/course/detail/instance_list.jsp", "");
	}

	@RequestMapping("/courseGeneral")
	public ModelAndView courseGeneral(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "sera/jsp/content/course/detail/general.jsp", "");
	}

	@RequestMapping("/courseBoard")
	public ModelAndView courseBoard(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "sera/jsp/content/course/detail/board.jsp", "");
	}

	@RequestMapping("/courseSetting")
	public ModelAndView courseSetting(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "sera/jsp/content/course/detail/course_setting.jsp", "");
	}

	@RequestMapping("/courseSettingProfile")
	public ModelAndView courseSettingProfile(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "sera/jsp/content/course/setting/profile.jsp", "");
	}

	@RequestMapping("/courseSettingMentee")
	public ModelAndView courseSettingMentee(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "sera/jsp/content/course/setting/mentee.jsp", "");
	}

	@RequestMapping("/courseSettingTeam")
	public ModelAndView courseSettingTeam(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "sera/jsp/content/course/setting/team.jsp", "");
	}

	@RequestMapping("/courseMissionList")
	public ModelAndView courseMissionList(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "sera/jsp/content/course/mission/list.jsp", "");
	}

	@RequestMapping("/courseMissionCreate")
	public ModelAndView courseMissionCreate(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "sera/jsp/content/course/mission/create.jsp", "");
	}

	@RequestMapping("/courseMissionModify")
	public ModelAndView courseMissionModify(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "sera/jsp/content/course/mission/modify.jsp", "");
	}

	@RequestMapping("/courseMissionPerform")
	public ModelAndView courseMissionPerform(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "sera/jsp/content/course/mission/perform.jsp", "");
	}

	@RequestMapping("/courseTeamCreate")
	public ModelAndView courseTeamCreate(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "sera/jsp/content/course/detail/create_team.jsp", "");
	}

	@RequestMapping("/socialNote")
	public ModelAndView socialNote(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "sera/jsp/content/social/note.jsp", "");
	}

	@RequestMapping("/socialBadge")
	public ModelAndView socialBadge(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "sera/jsp/content/social/badge.jsp", "");
	}

	@RequestMapping("/socialBoard")
	public ModelAndView socialBoard(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "sera/jsp/content/social/board.jsp", "");
	}

	@RequestMapping("/socialCalendar")
	public ModelAndView socialCalendar(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "sera/jsp/content/social/calendar.jsp", "");
	}

	@RequestMapping("/socialEvent")
	public ModelAndView socialEvent(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "sera/jsp/content/social/event.jsp", "");
	}

	@RequestMapping("/socialFriend")
	public ModelAndView socialFriend(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "sera/jsp/content/social/friend.jsp", "");
	}

	@RequestMapping("/othersPAGE")
	public ModelAndView othersPage(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "", "othersPAGE.tiles");
	}

	@RequestMapping("/othersCourses")
	public ModelAndView othersCourses(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "sera/jsp/content/course/others_courses.jsp", "");
	}

	@RequestMapping("/othersFriend")
	public ModelAndView othersFriend(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "sera/jsp/content/social/others_friend.jsp", "");
	}

	@RequestMapping("/othersBadge")
	public ModelAndView othersBadge(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "sera/jsp/content/social/others_badge.jsp", "");
	}

	@RequestMapping("/seraInstances")
	public ModelAndView userInstances(HttpServletRequest request, HttpServletResponse response) {

		String userId = request.getParameter("userId");
		String courseId = request.getParameter("courseId");
		return SmartUtil.returnMnv(request, "sera/jsp/content/sera_instances.jsp?userId=" + userId + "&courseId=" + courseId, "");
	}

	@RequestMapping(value = "/create_new_course", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody Map<String, Object> createNewCourse(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String courseId = smartworks.createNewCourse(requestBody, request);//smartworks.setCourse(requestBody, request);
		// TO DO : Exception handler
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("href", "courseHome.sw?courseId=" + courseId);
		return map;
	}

	@RequestMapping(value = "/create_new_mission", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody Map<String, Object> createNewMission(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//smartworks.setMission(requestBody, request);
		// TO DO : Exception handler
		//String courseId = (String)requestBody.get("courseId");
		String courseId = smartworks.createNewMission(requestBody, request);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("href", "courseHome.sw?courseId=" + courseId);
		return map;
	}

	@RequestMapping(value = "/perform_mission_report", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody Map<String, Object> performMissionReport(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//smartworks.performMissionReport(requestBody, request);
		// TO DO : Exception handler
		String courseId = (String)requestBody.get("courseId");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("href", "courseMissionList.sw?courseId=" + courseId);
		return map;
	}

	@RequestMapping(value = "/create_sera_note", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody Map<String, Object> createSeraNote(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//smartworks.setSeraNote(requestBody, request);
		// TO DO : Exception handler
		Map<String, Object> map = new HashMap<String, Object>();
		int spaceType = Integer.parseInt((String)requestBody.get("spaceType"));
		String spaceId = (String)requestBody.get("spaceId");
		String href = "";
		switch(spaceType){
		case ISmartWorks.SPACE_TYPE_USER:
			href = (SmartUtil.getCurrentUser().getId().equals(spaceId)) ? "myPAGE.sw" : "othersPAGE.sw?userId=" + spaceId;
			break;
		case ISmartWorks.SPACE_TYPE_GROUP:
			href = (SmartUtil.getCurrentUser().getId().equals(spaceId)) ? "myPAGE.sw" : "othersPAGE.sw?userId=" + spaceId;
			break;
		case ISmartWorks.SPACE_TYPE_WORK_INSTANCE:
			href = (SmartUtil.getCurrentUser().getId().equals(spaceId)) ? "myPAGE.sw" : "othersPAGE.sw?userId=" + spaceId;
			break;
		}
		map.put("href", href);
		return map;
	}

	@RequestMapping(value = "/create_new_team", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody Map<String, Object> createNewTeam(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//smartworks.setCourse(requestBody, request);
		// TO DO : Exception handler
		String courseId = (String)requestBody.get("courseId");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("href", "courseHome.sw?courseId=" + courseId);
		return map;
	}

	@RequestMapping(value = "/update_sera_profile", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody Map<String, Object> updateSeraProfile(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//smartworks.setMission(requestBody, request);
		// TO DO : Exception handler
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("href", "myPAGE.sw");
		return map;
	}

}
