package net.smartworks.model.sera;

import net.smartworks.model.sera.info.CourseInfo;

public class CourseAdList{

	public static final int MAX_BRIEF_COURSE_LIST = 3;
	public static final int TYPE_FAVORITE = 1;
	public static final int TYPE_RECOMMENDED = 2;
	
	private CourseInfo[] favoriteCourses;
	private CourseInfo[] recommendedCourses;
	
	public CourseInfo[] getFavoriteCourses() {
		return favoriteCourses;
	}
	public void setFavoriteCourses(CourseInfo[] favoriteCourses) {
		this.favoriteCourses = favoriteCourses;
	}
	public CourseInfo[] getRecommendedCourses() {
		return recommendedCourses;
	}
	public void setRecommendedCourses(CourseInfo[] recommendedCourses) {
		this.recommendedCourses = recommendedCourses;
	}

	public CourseAdList(){
		super();
	}
}