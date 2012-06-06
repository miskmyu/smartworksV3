package net.smartworks.server.service.util;

import net.smartworks.model.community.User;
import net.smartworks.model.instance.Instance;
import net.smartworks.model.instance.info.InstanceInfo;
import net.smartworks.util.LocalDate;
import net.smartworks.util.Semaphore;

public class InstanceParallelProcessing extends ParallelProcessing {
	private User currentUser;
	private int instanceType;
	private String userId;
	private String courseId;
	private String missionId;
	private String teamId;
	private String workSpaceId;
	private LocalDate fromDate;
	private int maxList;

	public int getInstanceType() {
		return instanceType;
	}

	public void setInstanceType(int instanceType) {
		this.instanceType = instanceType;
	}

	public InstanceParallelProcessing(Semaphore semaphore, Thread currentThread, User currentUser, int instanceType, String userId, String courseId, String missionId, String teamId, String workSpaceId, LocalDate fromDate, int maxList){
		super(semaphore, currentThread);
		this.currentUser = currentUser; 
		this.instanceType = instanceType;
		this.userId = userId;
		this.courseId = courseId;
		this.missionId = missionId;
		this.teamId = teamId;
		this.workSpaceId = workSpaceId;
		this.fromDate = fromDate;
		this.maxList = maxList;
	}

	@Override
	public void doRun() throws Exception {

		InstanceInfo[] instances = null;
		switch(this.instanceType){
		case Instance.TYPE_BOARD:
			instances = seraService.getBoardInstancesByCourseId(currentUser, userId, courseId, missionId, teamId, workSpaceId, fromDate, maxList);
			break;
		case Instance.TYPE_EVENT:			
			instances = seraService.getEventInstanceInfosByWorkSpaceId(currentUser, userId, courseId, missionId, teamId, fromDate, maxList);
			break;
		case Instance.TYPE_SERA_NOTE:
			instances = seraService.getSeraNoteByMissionId(currentUser, userId, courseId, missionId, teamId, fromDate, maxList);
			break;
		case Instance.TYPE_SERA_MISSION_REPORT:
			instances = seraService.getSeraReportByMissionId(currentUser, userId, courseId, missionId, teamId, fromDate, maxList);
			break;
		}		
		setArrayResult(instances);
	}
	
}
