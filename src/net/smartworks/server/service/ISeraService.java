package net.smartworks.server.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.smartworks.model.community.User;
import net.smartworks.model.instance.info.BoardInstanceInfo;
import net.smartworks.model.instance.info.CommentInstanceInfo;
import net.smartworks.model.instance.info.EventInstanceInfo;
import net.smartworks.model.instance.info.InstanceInfo;
import net.smartworks.model.notice.Notice;
import net.smartworks.model.sera.Course;
import net.smartworks.model.sera.CourseAdList;
import net.smartworks.model.sera.CourseList;
import net.smartworks.model.sera.FriendInformList;
import net.smartworks.model.sera.FriendList;
import net.smartworks.model.sera.GlobalSearchList;
import net.smartworks.model.sera.MemberInformList;
import net.smartworks.model.sera.MenteeInformList;
import net.smartworks.model.sera.Mentor;
import net.smartworks.model.sera.MissionInstance;
import net.smartworks.model.sera.SeraBoardList;
import net.smartworks.model.sera.SeraUser;
import net.smartworks.model.sera.Team;
import net.smartworks.model.sera.info.CourseInfo;
import net.smartworks.model.sera.info.MissionInstanceInfo;
import net.smartworks.model.sera.info.MissionReportInstanceInfo;
import net.smartworks.model.sera.info.NoteInstanceInfo;
import net.smartworks.model.sera.info.ReviewInstanceInfo;
import net.smartworks.model.sera.info.SeraUserInfo;
import net.smartworks.model.sera.info.TeamInfo;
import net.smartworks.util.LocalDate;

public interface ISeraService {

	public abstract String createNewMission(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract String createNewCourse(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract String setCourseProfile(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract String removeCourse(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void scoreCoursePointByType(String courseId, int type, int count, boolean isAdd) throws Exception;

	public abstract String performMissionReport(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract String modifyMission(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract String removeMission(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract String setSeraNote(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract Team createNewTeam(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public void modifyCourseTeam(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public void removeCourseTeam(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract String updateSeraProfile(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract CourseList getCoursesById(String userId, int maxList) throws Exception;

	public abstract CourseAdList getCourseAds(int maxList) throws Exception;

	public abstract CourseInfo[] getCoursesById(String userId, int courseType, LocalDate fromDate, int maxList) throws Exception;

	public abstract CourseInfo[] getCoursesByType(int courseType, String lastId, int maxList) throws Exception;

	public abstract CourseInfo[] getCoursesByCategory(String categoryName, String lastId, int maxList) throws Exception;

	public abstract Course getCourseById(String courseId) throws Exception;

	public abstract Mentor getMentorById(String mentorId) throws Exception;

	public abstract FriendList getFriendsById(String userId, int maxList) throws Exception;
	
	public abstract SeraUserInfo[] getFriendsById(String userId, String lastId, int maxList, String key) throws Exception;

	public abstract InstanceInfo[] getCourseNotices(String courseId, LocalDate fromDate, int maxList) throws Exception;

	public abstract InstanceInfo[] getSeraInstances(int type, String userId, String courseId, String missionId, String teamId, LocalDate fromDate, int maxList) throws Exception;

	public abstract void removeSeraInstane(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void addReviewOnCourse(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract ReviewInstanceInfo[] getReviewInstancesByCourse(String courseId, LocalDate fromDate, int maxList) throws Exception;

	public abstract MissionInstanceInfo[] getMissionInstanceList(String courseId, LocalDate fromDate, LocalDate toDate) throws Exception;

	public abstract MissionInstance getMissionById(String missionId) throws Exception;

	public abstract SeraUser getSeraUserById(String userId) throws Exception;

	public abstract SeraUserInfo[] getFriendRequestsForMe(String lastId, int maxList) throws Exception;

	public abstract void replyFriendRequest(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void friendRequest(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void destroyFriendship(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract CommentInstanceInfo[] getSubInstancesByRefId(String refId, int maxSize) throws Exception;

	public abstract String createSeraUser(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract String leaveSeraUser(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract MenteeInformList getCourseMenteeInformations(String courseId, int maxList) throws Exception;

	public abstract SeraUserInfo[] getCourseMenteeInformsByType(int type, String courseId, String lastId, int maxList) throws Exception;
	
	public abstract FriendInformList getMyFriendInformations(int maxList) throws Exception;

	public abstract SeraUserInfo[] getFriendInformsByType(int type, String userId, String lastId, int maxList) throws Exception;
	
	public abstract Notice[] getSeraNoticesForMe() throws Exception;

	public abstract SeraUserInfo[] searchSeraUserByType(int type, String userId, String key) throws Exception;

	public abstract SeraUserInfo[] searchCourseMemberByType(int type, String courseId, String key) throws Exception;

	public abstract CourseInfo[] searchCourseByType(int type, String key) throws Exception;

	public abstract CourseInfo[] searchCourseByCategory(String categoryName, String key) throws Exception;

	public abstract CourseInfo[] getCoursesByUser(String userId, int courseType, String lastId, int maxList) throws Exception;

	public abstract TeamInfo[] getTeamsByCourse(String courseId) throws Exception;
	
	public abstract Team getMyTeamByCourse(String courseId) throws Exception;
	
	public abstract Team getTeamById(String teamId) throws Exception;

	public abstract MemberInformList getTeamMemberInformations(String teamId, int maxList) throws Exception;

	public abstract SeraUserInfo[] getTeamMemberInformsByType(int type, String teamId, String lastId, int maxList) throws Exception;

	public abstract SeraUserInfo[] searchTeamMemberByType(int type, String courseId, String teamId, String key) throws Exception;
	
	public abstract void replyTeamJoinRequest(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void teamJoinRequest(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void leaveTeam(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void destroyMembership(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract SeraBoardList getSeraBoards(int maxList) throws Exception;
	
	public abstract String setMentorProfile(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract Team getJoinRequestTeamByCourseId(String courseId) throws Exception;
	
	public abstract BoardInstanceInfo[] getSeraTrends(int maxList) throws Exception; 

	public abstract BoardInstanceInfo[] getBoardInstancesByCourseId(User currentUser, String userId, String courseId, String missionId, String teamId, String workSpaceId, LocalDate fromDate, int maxList) throws Exception;

	public abstract EventInstanceInfo[] getEventInstanceInfosByWorkSpaceId(User currentUser, String userId, String courseId, String missionId, String teamId, LocalDate fromDate, int maxList) throws Exception;

	public abstract NoteInstanceInfo[] getSeraNoteByMissionId(User currentUser, String userId, String courseId, String missionId, String teamId, LocalDate fromDate, int maxList) throws Exception;

	public abstract MissionReportInstanceInfo[] getSeraReportByMissionId(User currentUser, String userId, String courseId, String missionId, String teamId, LocalDate fromDate, int maxList) throws Exception;

	public abstract CourseInfo[] getFavoriteCourses(String fromCourseId, int maxList) throws Exception;

	public abstract CourseInfo[] getRecommendedCourses(String fromCourseId, int maxList) throws Exception;
	
	public abstract GlobalSearchList searchGlobal(String key, int maxCourseList, int maxUserList) throws Exception;

	public abstract CourseInfo[] searchCourses(GlobalSearchList searchResult, String key, String lastId, int maxList) throws Exception;

	public abstract SeraUserInfo[] searchSeraUsers(GlobalSearchList searchResult, String key, String lastId, int maxList) throws Exception;

}
