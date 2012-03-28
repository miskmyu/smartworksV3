package net.smartworks.server.service;

import net.smartworks.model.sera.Course;
import net.smartworks.model.sera.CourseList;
import net.smartworks.model.sera.Mentor;
import net.smartworks.model.sera.info.CourseInfo;
import net.smartworks.util.LocalDate;

public interface ISeraService {

	public abstract CourseList getMyCourses(int maxList) throws Exception;

	public abstract CourseInfo[] getMyCoursesByType(int courseType, LocalDate fromDate, int maxList) throws Exception;

	public abstract Course getCourseById(String courseId) throws Exception;

	public abstract Mentor getMentorById(String mentorId) throws Exception;

}