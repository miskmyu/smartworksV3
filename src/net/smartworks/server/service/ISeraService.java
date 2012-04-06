package net.smartworks.server.service;

import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.instance.info.InstanceInfo;
import net.smartworks.model.sera.Course;
import net.smartworks.model.sera.CourseList;
import net.smartworks.model.sera.FriendList;
import net.smartworks.model.sera.Mentor;
import net.smartworks.model.sera.info.CourseInfo;
import net.smartworks.model.sera.info.MissionInstanceInfo;
import net.smartworks.model.sera.info.ReviewInstanceInfo;
import net.smartworks.util.LocalDate;

public interface ISeraService {

	public abstract CourseList getCoursesById(String userId, int maxList) throws Exception;

	public abstract CourseInfo[] getCoursesById(String userId, int courseType, LocalDate fromDate, int maxList) throws Exception;

	public abstract Course getCourseById(String courseId) throws Exception;

	public abstract Mentor getMentorById(String mentorId) throws Exception;

	public abstract FriendList getFriendsById(String userId, int maxList) throws Exception;
	
	public abstract UserInfo[] getFriendsById(String userId, String lastId, int maxList) throws Exception;

	public abstract InstanceInfo[] getCourseNotices(String courseId, LocalDate fromDate, int maxList) throws Exception;

	public abstract InstanceInfo[] getSeraInstances(String userId, String courseId, LocalDate fromDate, int maxList) throws Exception;

	public abstract ReviewInstanceInfo[] getReviewInstancesByCourse(String courseId, LocalDate fromDate, int maxList) throws Exception;

	public abstract MissionInstanceInfo[] getMissionInstanceList(String courseId, LocalDate fromDate, LocalDate toDate) throws Exception;

}