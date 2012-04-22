package net.smartworks.server.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.smartworks.model.instance.info.CommentInstanceInfo;
import net.smartworks.model.instance.info.InstanceInfo;
import net.smartworks.model.sera.Course;
import net.smartworks.model.sera.CourseList;
import net.smartworks.model.sera.FriendList;
import net.smartworks.model.sera.MenteeInformList;
import net.smartworks.model.sera.Mentor;
import net.smartworks.model.sera.MissionInstance;
import net.smartworks.model.sera.SeraUser;
import net.smartworks.model.sera.info.CourseInfo;
import net.smartworks.model.sera.info.MissionInstanceInfo;
import net.smartworks.model.sera.info.ReviewInstanceInfo;
import net.smartworks.model.sera.info.SeraUserInfo;
import net.smartworks.util.LocalDate;

public interface ISeraService {

	public abstract String createNewMission(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract String createNewCourse(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract String performMissionReport(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract String setSeraNote(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract String createNewTeam(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public void modifyCourseTeam(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public void removeCourseTeam(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract String updateSeraProfile(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract CourseList getCoursesById(String userId, int maxList) throws Exception;

	public abstract CourseInfo[] getCoursesById(String userId, int courseType, LocalDate fromDate, int maxList) throws Exception;

	public abstract CourseInfo[] getFavoriteCourses(int maxList) throws Exception;

	public abstract CourseInfo[] getRecommendedCourses(int maxList) throws Exception;

	public abstract CourseInfo[] getCoursesByType(int courseType, String lastId, int maxList) throws Exception;

	public abstract CourseInfo[] getCoursesByCategory(String categoryName, String lastId, int maxList) throws Exception;

	public abstract Course getCourseById(String courseId) throws Exception;

	public abstract Mentor getMentorById(String mentorId) throws Exception;

	public abstract FriendList getFriendsById(String userId, int maxList) throws Exception;
	
	public abstract SeraUserInfo[] getFriendsById(String userId, String lastId, int maxList) throws Exception;

	public abstract InstanceInfo[] getCourseNotices(String courseId, LocalDate fromDate, int maxList) throws Exception;

	public abstract InstanceInfo[] getSeraInstances(int type, String userId, String courseId, String missionId, LocalDate fromDate, int maxList) throws Exception;

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
	
}