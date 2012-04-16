package net.smartworks.server.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.instance.info.InstanceInfo;
import net.smartworks.model.sera.Course;
import net.smartworks.model.sera.CourseList;
import net.smartworks.model.sera.FriendList;
import net.smartworks.model.sera.Mentor;
import net.smartworks.model.sera.MissionInstance;
import net.smartworks.model.sera.SeraUser;
import net.smartworks.model.sera.info.CourseInfo;
import net.smartworks.model.sera.info.MissionInstanceInfo;
import net.smartworks.model.sera.info.ReviewInstanceInfo;
import net.smartworks.util.LocalDate;

public interface ISeraService {

	public abstract String createNewMission(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract String createNewCourse(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract String performMissionReport(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract String setSeraNote(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract String createNewTeam(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract String updateSeraProfile(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract CourseList getCoursesById(String userId, int maxList) throws Exception;

	public abstract CourseInfo[] getCoursesById(String userId, int courseType, LocalDate fromDate, int maxList) throws Exception;

	public abstract CourseInfo[] getFavoriteCourses(int maxList) throws Exception;

	public abstract CourseInfo[] getRecommendedCourses(int maxList) throws Exception;

	public abstract Course getCourseById(String courseId) throws Exception;

	public abstract Mentor getMentorById(String mentorId) throws Exception;

	public abstract FriendList getFriendsById(String userId, int maxList) throws Exception;
	
	public abstract UserInfo[] getFriendsById(String userId, String lastId, int maxList) throws Exception;

	public abstract InstanceInfo[] getCourseNotices(String courseId, LocalDate fromDate, int maxList) throws Exception;

	public abstract InstanceInfo[] getSeraInstances(String userId, String courseId, String missionId, LocalDate fromDate, int maxList) throws Exception;

	public abstract ReviewInstanceInfo[] getReviewInstancesByCourse(String courseId, LocalDate fromDate, int maxList) throws Exception;

	public abstract MissionInstanceInfo[] getMissionInstanceList(String courseId, LocalDate fromDate, LocalDate toDate) throws Exception;

	public abstract MissionInstance getMissionById(String missionId) throws Exception;

	public abstract SeraUser getSeraUserById(String userId) throws Exception;
}
