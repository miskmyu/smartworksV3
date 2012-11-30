package net.smartworks.model.instance.info;

import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.instance.Instance;
import net.smartworks.model.instance.WorkInstance;
import net.smartworks.model.work.info.WorkInfo;
import net.smartworks.util.LocalDate;

public class PWInstanceInfo extends WorkInstanceInfo {
	
	private TaskInstanceInfo[] runningTasks;
	
	public TaskInstanceInfo[] getRunningTasks() {
		return runningTasks;
	}
	public void setRunningTasks(TaskInstanceInfo[] runningTasks) {
		this.runningTasks = runningTasks;
	}
	public PWInstanceInfo() {
		super();
		super.setType(WorkInstance.TYPE_PROCESS);
	}
//	public PWInstanceInfo(String id, String subject, WorkInfo work, UserInfo owner, UserInfo lastModifier, LocalDate lastModifiedDate, TaskInstanceInfo lastTas) {
//	super(id, subject, owner, lastModifier, lastModifiedDate);
//	super.setWork(work);
//	super.setType(WorkInstance.TYPE_PROCESS);
//}
	public PWInstanceInfo(String id, String subject, String workId, String workName, int workType, UserInfo owner, UserInfo lastModifier, LocalDate lastModifiedDate, TaskInstanceInfo lastTas) {
		super(id, subject, owner, lastModifier, lastModifiedDate);
		super.setWorkId(workId);
		super.setWorkName(workName);
		super.setWorkType(workType);
		super.setType(WorkInstance.TYPE_PROCESS);
	}
	public PWInstanceInfo(String id, String subject, WorkInfo work, UserInfo owner, UserInfo lastModifier, LocalDate lastModifiedDate, TaskInstanceInfo lastTas) {
		super(id, subject, owner, lastModifier, lastModifiedDate);
		super.setWorkInfo(work);
		super.setType(WorkInstance.TYPE_PROCESS);
	}
	
}
