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

import net.smartworks.model.instance.Instance;
import net.smartworks.model.instance.info.InstanceInfo;
import net.smartworks.model.sera.Team;
import net.smartworks.model.sera.info.MissionInstanceInfo;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.service.ISmartWorks;
import net.smartworks.util.LocalDate;
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
public class SeraController extends ExceptionInterceptor {
	
	ISmartWorks smartworks;

	@Autowired
	public void setSmartworks(ISmartWorks smartworks) {
		this.smartworks = smartworks;
	}

	@RequestMapping("/seraCampus")
	public ModelAndView seraCampus(HttpServletRequest request, HttpServletResponse response) {

		request.getSession().setAttribute("currentMenu", "searCampus");
		return SmartUtil.returnMnvSera(request, "sera/jsp/layouts.jsp", "seraCampus.tiles");
	}

	@RequestMapping("/logins")
	public ModelAndView logins(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mnv = new ModelAndView();
		String type = CommonUtil.toNotNull(request.getParameter("type"));
		mnv.addObject("type", type);
		mnv.setViewName("sera/jsp/login.jsp");
		return mnv;
	}

	@RequestMapping("/logouts")
	public ModelAndView logouts(HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.logout(request, response);
		ModelAndView mnv = new ModelAndView();
		mnv.addObject("href", "logout");
		mnv.setViewName("jsp/movePage.jsp");
		return mnv;
	}

	@RequestMapping("/joinUs")
	public ModelAndView joinUs(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "", "joinUs.tiles");
	}

	@RequestMapping("/myPAGE")
	public ModelAndView myPAGE(HttpServletRequest request, HttpServletResponse response) {

		request.getSession().setAttribute("currentMenu", "myPAGE");
		return SmartUtil.returnMnvSera(request, "sera/jsp/content/my_page.jsp", "myPAGE.tiles");
	}

	@RequestMapping("/myProfile")
	public ModelAndView myProfile(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "sera/jsp/content/my_profile.jsp", "");
	}

	@RequestMapping("/Course")
	public ModelAndView Course(HttpServletRequest request, HttpServletResponse response) {

		request.getSession().setAttribute("currentMenu", "Course");
		return SmartUtil.returnMnvSera(request, "", "Course.tiles");
	}

	@RequestMapping("/Header")
	public ModelAndView Header(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "", "Header.tiles");
	}

	@RequestMapping("/createCourse")
	public ModelAndView createCourse(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "sera/jsp/content/course/create.jsp", "createCourse.tiles");
	}

	@RequestMapping("/myCourses")
	public ModelAndView myCourses(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "sera/jsp/content/course/my_courses.jsp", "myCourses.tiles");
	}

	@RequestMapping("/courseHome")
	public ModelAndView courseHome(HttpServletRequest request, HttpServletResponse response) {

		if(SmartUtil.getCurrentUser().isAnonymusUser()){
	 		ISmartWorks smartworks = (ISmartWorks)SmartUtil.getBean("smartWorks", request);
			return new ModelAndView("courseHomeNouser.tiles", "smartWorks", smartworks);
		}
		return SmartUtil.returnMnvSera(request, "sera/jsp/content/course/home.jsp", "courseHome.tiles");
	}

	@RequestMapping("/courseInstanceList")
	public ModelAndView courseInstanceList(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "sera/jsp/content/course/detail/instance_list.jsp", "");
	}

	@RequestMapping("/courseGeneral")
	public ModelAndView courseGeneral(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "sera/jsp/content/course/detail/general.jsp", "courseGeneral.tiles");
	}

	@RequestMapping("/courseBoard")
	public ModelAndView courseBoard(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "sera/jsp/content/course/detail/board.jsp", "");
	}

	@RequestMapping("/courseSetting")
	public ModelAndView courseSetting(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "sera/jsp/content/course/detail/course_setting.jsp", "");
	}

	@RequestMapping("/courseSettingProfile")
	public ModelAndView courseSettingProfile(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "sera/jsp/content/course/setting/profile.jsp", "");
	}

	@RequestMapping("/courseSettingMentee")
	public ModelAndView courseSettingMentee(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "sera/jsp/content/course/setting/mentee.jsp", "");
	}

	@RequestMapping("/courseSettingMentor")
	public ModelAndView courseSettingMentor(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "sera/jsp/content/course/setting/mentor.jsp", "");
	}

	@RequestMapping("/courseTeamManagement")
	public ModelAndView courseTeamManagement(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "sera/jsp/content/course/team/management.jsp", "");
	}

	@RequestMapping("/courseTeamModify")
	public ModelAndView courseTeamModify(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "sera/jsp/content/course/team/modify.jsp", "");
	}

	@RequestMapping("/courseTeamCreate")
	public ModelAndView courseTeamCreate(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "sera/jsp/content/course/team/create.jsp", "");
	}

	@RequestMapping("/courseTeamMembers")
	public ModelAndView courseTeamMembers(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "sera/jsp/content/course/team/members.jsp", "");
	}

	@RequestMapping("/courseTeamJoinRequests")
	public ModelAndView courseTeamJoinRequests(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "sera/jsp/content/course/team/join_requests.jsp", "");
	}

	@RequestMapping("/courseTeamActivity")
	public ModelAndView courseTeamActivity(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "sera/jsp/content/course/team/activity.jsp", "");
	}

	@RequestMapping("/courseTeamHome")
	public ModelAndView courseTeamHome(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "sera/jsp/content/course/team/home.jsp", "");
	}

	@RequestMapping("/courseMissionHome")
	public ModelAndView courseMissionHome(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "sera/jsp/content/course/mission/home.jsp", "");
	}

	@RequestMapping("/courseMissionCalendar")
	public ModelAndView courseMissionCalendar(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "sera/jsp/content/course/mission/calendar.jsp", "");
	}

	@RequestMapping("/courseMissionList")
	public ModelAndView courseMissionList(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "sera/jsp/content/course/mission/list.jsp", "");
	}

	@RequestMapping("/courseMissionCreate")
	public ModelAndView courseMissionCreate(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "sera/jsp/content/course/mission/create.jsp", "");
	}

	@RequestMapping("/courseMissionModify")
	public ModelAndView courseMissionModify(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "sera/jsp/content/course/mission/modify.jsp", "");
	}

	@RequestMapping("/courseMissionPerform")
	public ModelAndView courseMissionPerform(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "sera/jsp/content/course/mission/perform.jsp", "");
	}

	@RequestMapping("/socialNote")
	public ModelAndView socialNote(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "", "socialNote.tiles");
	}

	@RequestMapping("/socialBadge")
	public ModelAndView socialBadge(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "", "socialBadge.tiles");
	}

	@RequestMapping("/socialBoard")
	public ModelAndView socialBoard(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "", "socialBoard.tiles");
	}

	@RequestMapping("/socialCalendar")
	public ModelAndView socialCalendar(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "sera/jsp/content/social/calendar.jsp", "socialCalendar.tiles");
	}

	@RequestMapping("/socialEvent")
	public ModelAndView socialEvent(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "sera/jsp/content/social/event.jsp", "socialEvent.tiles");
	}

	@RequestMapping("/socialFriend")
	public ModelAndView socialFriend(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "sera/jsp/content/social/friend.jsp", "socialFriend.tiles");
	}

	@RequestMapping("/othersPAGE")
	public ModelAndView othersPage(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "", "othersPAGE.tiles");
	}

	@RequestMapping("/aboutSera")
	public ModelAndView aboutSera(HttpServletRequest request, HttpServletResponse response) {

		request.getSession().setAttribute("currentMenu", "aboutSera");
		return SmartUtil.returnMnvSera(request, "", "aboutSera.tiles");
	}

	@RequestMapping("/seraNews")
	public ModelAndView seraNews(HttpServletRequest request, HttpServletResponse response) {

		request.getSession().setAttribute("currentMenu", "seraNews");
		return SmartUtil.returnMnvSera(request, "", "seraNews.tiles");
	}

	@RequestMapping("/seraTrend")
	public ModelAndView seraTrend(HttpServletRequest request, HttpServletResponse response) {
		
		request.getSession().setAttribute("currentMenu", "seraTrend");
		return SmartUtil.returnMnvSera(request, "", "seraTrend.tiles");
	}

	@RequestMapping("/seraBoardItem")
	public ModelAndView seraBoardItem(HttpServletRequest request, HttpServletResponse response) {
		if(SmartUtil.getCurrentUser().isAnonymusUser()){
			String getHeader = request.getHeader("X-Requested-With");
	 		ISmartWorks smartworks = (ISmartWorks)SmartUtil.getBean("smartWorks", request);
			if (getHeader != null){
				return new ModelAndView("sera/jsp/content/board/sera_board_item.jsp", "smartWorks", smartworks);
			}else{
				return new ModelAndView("seraBoardItem.tiles", "smartWorks", smartworks);
			}
		}
		return SmartUtil.returnMnvSera(request, "sera/jsp/content/board/sera_board_item.jsp", "seraBoardItem.tiles");
	}

	@RequestMapping("/helpCenter")
	public ModelAndView helpCenter(HttpServletRequest request, HttpServletResponse response) {

		request.getSession().setAttribute("currentMenu", "helpCenter");
		return SmartUtil.returnMnvSera(request, "", "helpCenter.tiles");
	}

	@RequestMapping("/helpFAQ")
	public ModelAndView helpFAQ(HttpServletRequest request, HttpServletResponse response) {

		request.getSession().setAttribute("currentMenu", "helpFAQ");
		return SmartUtil.returnMnvSera(request, "", "helpFAQ.tiles");
	}

	@RequestMapping("/othersCourses")
	public ModelAndView othersCourses(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "sera/jsp/content/course/others_courses.jsp", "");
	}

	@RequestMapping("/othersFriend")
	public ModelAndView othersFriend(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "sera/jsp/content/social/others_friend.jsp", "");
	}

	@RequestMapping("/othersBadge")
	public ModelAndView othersBadge(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "sera/jsp/content/social/others_badge.jsp", "");
	}

	@RequestMapping("/seraInstances")
	public ModelAndView userInstances(HttpServletRequest request, HttpServletResponse response) {
		return SmartUtil.returnMnvSera(request, "sera/jsp/content/sera_instances.jsp", "");
	}

	@RequestMapping("/moreCourses")
	public ModelAndView moreCourses(HttpServletRequest request, HttpServletResponse response) {
		if(SmartUtil.getCurrentUser().isAnonymusUser()){
	 		ISmartWorks smartworks = (ISmartWorks)SmartUtil.getBean("smartWorks", request);
			return new ModelAndView("sera/jsp/content/more_courses.jsp", "smartWorks", smartworks);
		}
		return SmartUtil.returnMnvSera(request, "sera/jsp/content/more_courses.jsp", "");
	}

	@RequestMapping("/moreFriendInforms")
	public ModelAndView moreFriendInforms(HttpServletRequest request, HttpServletResponse response) {
		return SmartUtil.returnMnvSera(request, "sera/jsp/content/social/more_friend_informs.jsp", "");
	}

	@RequestMapping("/moreMenteeInforms")
	public ModelAndView moreMenteeInforms(HttpServletRequest request, HttpServletResponse response) {
		return SmartUtil.returnMnvSera(request, "sera/jsp/content/course/setting/more_mentee_informs.jsp", "");
	}

	@RequestMapping("/moreCourseReviews")
	public ModelAndView moreCourseReviews(HttpServletRequest request, HttpServletResponse response) {
		return SmartUtil.returnMnvSera(request, "sera/jsp/content/course/detail/more_course_reviews.jsp", "");
	}

	@RequestMapping("/moreMemberInforms")
	public ModelAndView moreMemberInforms(HttpServletRequest request, HttpServletResponse response) {
		return SmartUtil.returnMnvSera(request, "sera/jsp/content/course/team/more_member_informs.jsp", "");
	}

	@RequestMapping("/inviteCourseMembers")
	public ModelAndView inviteCourseMembers(HttpServletRequest request, HttpServletResponse response) {
		return SmartUtil.returnMnvSera(request, "sera/jsp/content/course/invite_course_members.jsp", "");
	}

	@RequestMapping("/course_member")
	public ModelAndView courseMember(HttpServletRequest request, HttpServletResponse response) throws Exception {

		return SmartUtil.returnMnvSera(request, "sera/jsp/search/course_member.jsp", "");
	}

	@RequestMapping("/sera_user")
	public ModelAndView seraUser(HttpServletRequest request, HttpServletResponse response) throws Exception {

		return SmartUtil.returnMnvSera(request, "sera/jsp/search/sera_user.jsp", "");
	}

	@RequestMapping("/pop_notification_list")
	public ModelAndView popNotificationList(HttpServletRequest request, HttpServletResponse response) throws Exception {

		return SmartUtil.returnMnvSera(request, "sera/jsp/popup/pop_notification_list.jsp", "");
	}

	@RequestMapping("/globalSearch")
	public ModelAndView globalSearch(HttpServletRequest request, HttpServletResponse response) {

 		ISmartWorks smartworks = (ISmartWorks)SmartUtil.getBean("smartWorks", request);
		return new ModelAndView("sera/jsp/search/global_search.jsp", "smartWorks", smartworks);
	}

	@RequestMapping("/moreSearchCourses")
	public ModelAndView moreSearchCourses(HttpServletRequest request, HttpServletResponse response) {

 		ISmartWorks smartworks = (ISmartWorks)SmartUtil.getBean("smartWorks", request);
		return new ModelAndView("sera/jsp/search/more_courses.jsp", "smartWorks", smartworks);
	}

	@RequestMapping("/moreSearchSeraUsers")
	public ModelAndView moreSearchSeraUsers(HttpServletRequest request, HttpServletResponse response) {

 		ISmartWorks smartworks = (ISmartWorks)SmartUtil.getBean("smartWorks", request);
		return new ModelAndView("sera/jsp/search/more_sera_users.jsp", "smartWorks", smartworks);
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

	@RequestMapping(value = "/set_course_profile", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody Map<String, Object> setCourseProfile(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String courseId = smartworks.setCourseProfile(requestBody, request);//smartworks.setCourse(requestBody, request);
		// TO DO : Exception handler
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("href", "courseHome.sw?courseId=" + courseId);
		return map;
	}

	@RequestMapping(value = "/set_mentor_profile", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody void setMentorProfile(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String mentorId = smartworks.setMentorProfile(requestBody, request);//smartworks.setCourse(requestBody, request);
	}

	@RequestMapping(value = "/remove_course", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody Map<String, Object> removeCourse(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.removeCourse(requestBody, request);//smartworks.setCourse(requestBody, request);
		// TO DO : Exception handler
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("href", "myPAGE.sw");
		return map;
	}

	@RequestMapping(value = "/create_new_mission", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody Map<String, Object> createNewMission(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String missionId = smartworks.createNewMission(requestBody, request);
		String courseId = (String)requestBody.get("courseId");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("href", "courseMissionHome.sw?courseId=" + courseId);
		return map;
	}

	@RequestMapping(value = "/modify_mission", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody Map<String, Object> modifyMission(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String missionId = smartworks.modifyMission(requestBody, request);
		String courseId = (String)requestBody.get("courseId");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("href", "courseMissionHome.sw?courseId=" + courseId);
		return map;
	}

	@RequestMapping(value = "/remove_mission", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Map<String, Object> removeMission(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.removeMission(requestBody, request);
		String courseId = (String)requestBody.get("courseId");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("href", "courseHome.sw?courseId=" + courseId);
		return map;
	}

	@RequestMapping(value = "/perform_mission_report", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody Map<String, Object> performMissionReport(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.performMissionReport(requestBody, request);
		// TO DO : Exception handler
		String courseId = (String)requestBody.get("courseId");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("href", "courseMissionHome.sw?courseId=" + courseId);
		return map;
	}

	@RequestMapping(value = "/create_sera_note", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody Map<String, Object> createSeraNote(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.setSeraNote(requestBody, request);
		// TO DO : Exception handler
		Map<String, Object> map = new HashMap<String, Object>();
		int spaceType = Integer.parseInt((String)requestBody.get("spaceType"));
		String spaceId = (String)requestBody.get("spaceId");
		String teamId = (String)requestBody.get("teamId");
		String href = "";
		switch(spaceType){
		case ISmartWorks.SPACE_TYPE_USER:
			href = (SmartUtil.getCurrentUser().getId().equals(spaceId)) ? "myPAGE.sw" : "othersPAGE.sw?userId=" + spaceId;
			break;
		case ISmartWorks.SPACE_TYPE_GROUP:
			if(!SmartUtil.isBlankObject(teamId))
				href = (SmartUtil.getCurrentUser().getId().equals(spaceId)) ? "myPAGE.sw" : "";
			else
				href = (SmartUtil.getCurrentUser().getId().equals(spaceId)) ? "myPAGE.sw" : "courseHome.sw?courseId=" + spaceId;
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
		Team team = smartworks.createNewTeam(requestBody, request);
		// TO DO : Exception handler
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("teamId", team.getId());
		map.put("teamName", team.getName());
		return map;
	}

	@RequestMapping(value = "/modify_course_team", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void modifyCourseTeam(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.modifyCourseTeam(requestBody, request);
	}

	@RequestMapping(value = "/remove_course_team", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void removeCourseTeam(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.removeCourseTeam(requestBody, request);
	}

	@RequestMapping(value = "/update_sera_profile", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody Map<String, Object> updateSeraProfile(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.updateSeraProfile(requestBody, request);
		// TO DO : Exception handler
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("href", "myPAGE.sw");
		return map;
	}

	@RequestMapping(value = "/get_mission_list", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Map<String, Object> getMissionList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String courseId = request.getParameter("courseId");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		MissionInstanceInfo[] missions = smartworks.getMissionInstanceList(courseId, LocalDate.convertLocalDateStringToLocalDate(fromDate), LocalDate.convertLocalDateStringToLocalDate(toDate));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("courseId", courseId);
		map.put("missions", missions);
		return map;
	}	

	@RequestMapping(value = "/create_sera_user", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody Map<String, Object> createSeraUser(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String userId = smartworks.createSeraUser(requestBody, request);//smartworks.setCourse(requestBody, request);
		// TO DO : Exception handler
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("href", "myPAGE.sw");
		return map;
	}

	@RequestMapping(value = "/leave_sera_user", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Map<String, Object> leaveSeraUser(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String userId = smartworks.leaveSeraUser(requestBody, request);//smartworks.setCourse(requestBody, request);
		// TO DO : Exception handler
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("href", "logins.sw");
		return map;
	}

	@RequestMapping(value = "/friend_request", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void friendRequest(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.friendRequest(requestBody, request);
	}

	@RequestMapping(value = "/reply_friend_request", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void replyFriendRequest(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.replyFriendRequest(requestBody, request);
	}

	@RequestMapping(value = "/destroy_friendship", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void destroyFriendship(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.destroyFriendship(requestBody, request);
	}

	@RequestMapping(value = "/team_join_request", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void teamJoinRequest(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.teamJoinRequest(requestBody, request);
	}

	@RequestMapping(value = "/reply_team_join_request", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void replyTeamJoinRequest(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.replyTeamJoinRequest(requestBody, request);
	}

	@RequestMapping(value = "/leave_team", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void leaveTeam(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.leaveTeam(requestBody, request);
	}

	@RequestMapping(value = "/destroy_membership", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void destroyMembership(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.destroyMembership(requestBody, request);
	}

	@RequestMapping("/comments_in_instance")
	public ModelAndView commentsInInstance(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().setAttribute("subComments", null);
		return SmartUtil.returnMnvSera(request, "sera/jsp/content/comments_in_instance.jsp", "");
	}

	@RequestMapping("/course_by_type")
	public ModelAndView courseByType(HttpServletRequest request, HttpServletResponse response) {
		if(SmartUtil.getCurrentUser().isAnonymusUser()){
	 		ISmartWorks smartworks = (ISmartWorks)SmartUtil.getBean("smartWorks", request);
			return new ModelAndView("sera/jsp/content/course_by_type.jsp", "smartWorks", smartworks);
		}
		return SmartUtil.returnMnvSera(request, "sera/jsp/content/course_by_type.jsp", "");
	}

	@RequestMapping(value = "/remove_sera_instance", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void removeSeraInstance(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.removeSeraInstane(requestBody, request);
	}

	@RequestMapping("/pop_select_course_member")
	public ModelAndView popSelectCourseMember(HttpServletRequest request, HttpServletResponse response) throws Exception {

		return SmartUtil.returnMnvSera(request, "sera/jsp/popup/pop_select_course_member.jsp", "");
	}
	
	@RequestMapping("/pop_select_friend")
	public ModelAndView popSelectFriend(HttpServletRequest request, HttpServletResponse response) throws Exception {

		return SmartUtil.returnMnvSera(request, "sera/jsp/popup/pop_select_friend.jsp", "");
	}
	
	@RequestMapping(value = "/add_review_on_course", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void addReviewOnCourse(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.addReviewOnCourse(requestBody, request);
	}


	@RequestMapping(value = "/defective_course_report", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void defectiveCourseReport(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
	}

	@RequestMapping("/search_sera_user_by_type")
	public ModelAndView search_sera_userByType(HttpServletRequest request, HttpServletResponse response) {
		return SmartUtil.returnMnvSera(request, "sera/jsp/search/sera_user_by_type.jsp", "");
	}

	@RequestMapping("/search_course_member_by_type")
	public ModelAndView searchCourseMemberByType(HttpServletRequest request, HttpServletResponse response) {
		return SmartUtil.returnMnvSera(request, "sera/jsp/search/course_member_by_type.jsp", "");
	}

	@RequestMapping("/search_course_by_type")
	public ModelAndView searchCourseByType(HttpServletRequest request, HttpServletResponse response) {
		if(SmartUtil.getCurrentUser().isAnonymusUser()){
	 		ISmartWorks smartworks = (ISmartWorks)SmartUtil.getBean("smartWorks", request);
			return new ModelAndView("sera/jsp/search/course_by_type.jsp", "smartWorks", smartworks);
		}
		return SmartUtil.returnMnvSera(request, "sera/jsp/search/course_by_type.jsp", "");
	}

	@RequestMapping("/moreCoursesByUser")
	public ModelAndView moreCoursesByUser(HttpServletRequest request, HttpServletResponse response) {
		return SmartUtil.returnMnvSera(request, "sera/jsp/content/course/more_courses_by_user.jsp", "");
	}

	@RequestMapping("/search_team_member_by_type")
	public ModelAndView searchTeamMemberByType(HttpServletRequest request, HttpServletResponse response) {
		return SmartUtil.returnMnvSera(request, "sera/jsp/search/team_member_by_type.jsp", "");
	}
	
	@RequestMapping(value = "/get_course_notices", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Map<String, Object> getCourseNotices(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String courseId = request.getParameter("courseId");
		String fromDate = request.getParameter("fromDate");
		String maxList = request.getParameter("maxList");
		InstanceInfo[] notices = smartworks.getCourseNotices(courseId, LocalDate.convertLocalDateStringToLocalDate(fromDate), Integer.parseInt(maxList));
		String data = "";
		if(!SmartUtil.isBlankObject(notices)){
			for(int i=0; i<notices.length; i++){
				InstanceInfo notice = notices[i];
				if(notice.getType()==Instance.TYPE_BOARD){
					data = data + "<dd>[알림] " + notice.getSubject() + "</dd>";
				}else if(notice.getType()==Instance.TYPE_EVENT){
					data = data + "<dd>[이벤트] " + notice.getSubject() + "</dd>";
				}
			}
		}

		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", data);
		return map;
	}	

}
