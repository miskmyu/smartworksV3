package net.smartworks.model.sera;

import net.smartworks.model.sera.info.CourseInfo;

public class CourseList{

	public static final int MAX_BRIEF_COURSE_LIST = 3;
	public static final int MAX_COURSE_LIST = 1;
	
	private int runnings = 0;
	private int attendings = 0;
	private CourseInfo[] runningCourses;
	private CourseInfo[] attendingCourses;
	
	public int getRunnings() {
		return runnings;
	}
	public void setRunnings(int runnings) {
		this.runnings = runnings;
	}
	public int getAttendings() {
		return attendings;
	}
	public void setAttendings(int attendings) {
		this.attendings = attendings;
	}
	public CourseInfo[] getRunningCourses() {
		return runningCourses;
	}
	public void setRunningCourses(CourseInfo[] runningCourses) {
		this.runningCourses = runningCourses;
	}
	public CourseInfo[] getAttendingCourses() {
		return attendingCourses;
	}
	public void setAttendingCourses(CourseInfo[] attendingCourses) {
		this.attendingCourses = attendingCourses;
	}

	public CourseList(){
		super();
	}
}