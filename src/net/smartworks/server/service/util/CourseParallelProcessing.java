package net.smartworks.server.service.util;

import net.smartworks.model.sera.CourseAdList;
import net.smartworks.model.sera.CourseList;
import net.smartworks.model.sera.info.CourseInfo;
import net.smartworks.util.Semaphore;

public class CourseParallelProcessing extends ParallelProcessing {
	private Class model;
	private int type;
	private String userId;
	private int maxList;

	public CourseParallelProcessing(Semaphore semaphore, Thread currentThread, Class model, int type, String userId, int maxList){
		super(semaphore, currentThread);
		this.model = model;
		this.type = type;
		this.userId = userId;
		this.maxList = maxList;
	}

	@Override
	public void doRun() throws Exception {

		if(model == null) return;
		
		CourseInfo[] courses = null;
		if(model.equals(CourseAdList.class)){
			switch(this.type){
			case CourseAdList.TYPE_FAVORITE:
				courses = seraService.getFavoriteCourses(null, maxList);
				break;
			case CourseAdList.TYPE_RECOMMENDED:
				courses = seraService.getRecommendedCourses(null, maxList);
				break;
			}
		}else if(model.equals(CourseList.class)){
			switch(this.type){
			case CourseList.TYPE_MY_RUNNING_COURSE:
				break;
			case CourseList.TYPE_MY_ATTENDING_COURSE:
				break;
			}
		}
		setArrayResult(courses);
	}
	
}
