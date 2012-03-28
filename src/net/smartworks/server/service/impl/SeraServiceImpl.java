package net.smartworks.server.service.impl;

import net.smartworks.model.sera.Course;
import net.smartworks.model.sera.CourseList;
import net.smartworks.model.sera.Mentor;
import net.smartworks.model.sera.info.CourseInfo;
import net.smartworks.server.service.ISeraService;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SeraTest;
import org.springframework.stereotype.Service;

@Service
public class SeraServiceImpl implements ISeraService {

	@Override
	public CourseList getMyCourses(int maxList) throws Exception {

		try{
			CourseList courses = SeraTest.getMyCourses(maxList);
			return courses;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}		
	}

	@Override
	public CourseInfo[] getMyCoursesByType(int courseType, LocalDate FromDate, int maxList) throws Exception {

		try{
			CourseInfo[] courses = SeraTest.getMyCoursesByType(courseType, FromDate, maxList);
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

}
