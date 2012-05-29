package net.smartworks.model.sera;

import net.smartworks.model.sera.info.CourseInfo;
import net.smartworks.model.sera.info.SeraUserInfo;

public class GlobalSearchList{

	public static final int MAX_COURSE_LIST = 20;
	public static final int MAX_SERA_USER_LIST = 20;
	public static final int TYPE_COURSE = 1;
	public static final int TYPE_SERA_USER = 2;
	
	private int totalCourses = 0;
	private int totalSeraUsers = 0;
	private CourseInfo[] courses;
	private SeraUserInfo[] seraUsers;
	
	public int getTotalCourses() {
		return totalCourses;
	}
	public void setTotalCourses(int totalCourses) {
		this.totalCourses = totalCourses;
	}
	public int getTotalSeraUsers() {
		return totalSeraUsers;
	}
	public void setTotalSeraUsers(int totalSeraUsers) {
		this.totalSeraUsers = totalSeraUsers;
	}
	public CourseInfo[] getCourses() {
		return courses;
	}
	public void setCourses(CourseInfo[] courses) {
		this.courses = courses;
	}
	public SeraUserInfo[] getSeraUsers() {
		return seraUsers;
	}
	public void setSeraUsers(SeraUserInfo[] seraUsers) {
		this.seraUsers = seraUsers;
	}
	
	public GlobalSearchList(){
		super();
	}
}