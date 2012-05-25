package net.smartworks.server.service.util;

import net.smartworks.model.instance.Instance;
import net.smartworks.model.instance.info.InstanceInfo;
import net.smartworks.server.service.ISeraService;
import net.smartworks.util.LocalDate;
import net.smartworks.util.Semaphore;

import org.springframework.beans.factory.annotation.Autowired;

public class InstanceParallelProcessing extends ParallelProcessing {
	private int instanceType;
	private String userId;
	private String courseId;
	private String missionId;
	private String teamId;
	private LocalDate fromDate;
	private int maxList;
	private ISeraService seraService;

	@Autowired(required=true)
	public void setSeraService(ISeraService seraService) {
		this.seraService = seraService;
	}
	public int getInstanceType() {
		return instanceType;
	}
	public void setInstanceType(int instanceType) {
		this.instanceType = instanceType;
	}

	public InstanceParallelProcessing(Semaphore semaphore, Thread currentThread, int instanceType, String userId, String courseId, String missionId, String teamId, LocalDate fromDate, int maxList){
		super(semaphore, currentThread);
		this.instanceType = instanceType;
		this.userId = userId;
		this.courseId = courseId;
		this.missionId = missionId;
		this.teamId = teamId;
		this.fromDate = fromDate;
		this.maxList = maxList;
	}

	@Override
	public void doRun() throws Exception {
		seraService.getSeraUserById("hsshin@maninsoft.co.kr");
		InstanceInfo[] instances = null;
		switch(this.instanceType){
		case Instance.TYPE_BOARD:
			instances = seraService.getBoardInstancesByCourseId(userId, courseId, missionId, teamId, null, fromDate, maxList);
			break;
		case Instance.TYPE_EVENT:			
			instances = seraService.getEventInstanceInfosByWorkSpaceId(userId, courseId, missionId, teamId, fromDate, maxList);
			break;
		case Instance.TYPE_SERA_NOTE:
			instances = seraService.getSeraNoteByMissionId(userId, courseId, missionId, teamId, fromDate, maxList);
			break;
		case Instance.TYPE_SERA_MISSION_REPORT:
			instances = seraService.getSeraReportByMissionId(userId, courseId, missionId, teamId, fromDate, maxList);
			break;
		}		
		setArrayResult(instances);
	}
	
}
