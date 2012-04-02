package net.smartworks.server.service.impl;

import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.instance.info.InstanceInfo;
import net.smartworks.model.sera.Course;
import net.smartworks.model.sera.CourseList;
import net.smartworks.model.sera.FriendList;
import net.smartworks.model.sera.Mentor;
import net.smartworks.model.sera.info.CourseInfo;
import net.smartworks.server.service.ISeraService;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SeraTest;
import org.springframework.stereotype.Service;

@Service
public class SeraServiceImpl implements ISeraService {

	@Override
	public CourseList getCoursesById(String userId, int maxList) throws Exception {

		try{
			CourseList courses = SeraTest.getCoursesById(userId, maxList);
			return courses;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}		
	}

	@Override
	public CourseInfo[] getCoursesById(String userId, int courseType, LocalDate FromDate, int maxList) throws Exception {

		try{
			CourseInfo[] courses = SeraTest.getCoursesById(userId, courseType, FromDate, maxList);
			return courses;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}		
	}

	@Override
	public Course getCourseById(String courseId) throws Exception {

		try{
			Course course = SeraTest.getCourseById(courseId);
			return course;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}		
	}

	@Override
	public Mentor getMentorById(String mentorId) throws Exception {

		try{
			Mentor mentor = SeraTest.getMentorById(mentorId);
			return mentor;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}		
	}

	@Override
	public FriendList getFriendsById(String userId, int maxList) throws Exception{
		try{
			FriendList friendList = SeraTest.getFriendsById(userId, maxList);
			return friendList;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}		
	}
	
	@Override
	public UserInfo[] getFriendsById(String userId, String lastId, int maxList) throws Exception{
		try{
			UserInfo[] friends = SeraTest.getFriendsById(userId, lastId, maxList);
			return friends;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}		
		
	}
	
	@Override
	public InstanceInfo[] getCourseNotices(String courseId, LocalDate fromDate, int maxList) throws Exception{
		try{
			InstanceInfo[] notices = SeraTest.getCourseNotices(courseId, fromDate, maxList);
			return notices;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}		
		
	}
	
	@Override
	public InstanceInfo[] getSeraInstancesByUser(String userId, LocalDate fromDate, int maxList) throws Exception{
		try{
			InstanceInfo[] instances = SeraTest.getSeraInstancesByUser(userId, fromDate, maxList);
			return instances;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}		
		
	}
}
