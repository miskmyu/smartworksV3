package net.smartworks.model.sera;

import net.smartworks.model.sera.info.CourseInfo;

public class CourseList{

	public static final int MAX_BRIEF_COURSE_LIST = 5;
	public static final int MAX_COURSE_LIST = 6;
	
	private int myRunnings = 0;
	private int myAttendings = 0;
	private CourseInfo[] myRunningCourses;
	private CourseInfo[] myAttendingCourses;
	
	public int getMyRunnings() {
		return myRunnings;
	}
	public void setMyRunnings(int myRunnings) {
		this.myRunnings = myRunnings;
	}
	public int getMyAttendings() {
		return myAttendings;
	}
	public void setMyAttendings(int myAttendings) {
		this.myAttendings = myAttendings;
	}
	public CourseInfo[] getMyRunningCourses() {
		return myRunningCourses;
	}
	public void setMyRunningCourses(CourseInfo[] myRunningCourses) {
		this.myRunningCourses = myRunningCourses;
	}
	public CourseInfo[] getMyAttendingCourses() {
		return myAttendingCourses;
	}
	public void setMyAttendingCourses(CourseInfo[] myAttendingCourses) {
		this.myAttendingCourses = myAttendingCourses;
	}

	public CourseList(){
		super();
	}
}