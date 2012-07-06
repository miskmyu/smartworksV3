package net.smartworks.model.instance;

import net.smartworks.model.community.User;
import net.smartworks.model.instance.info.TaskInstanceInfo;
import net.smartworks.model.work.SmartWork;
import net.smartworks.model.work.SocialWork;
import net.smartworks.model.work.Work;
import net.smartworks.model.work.WorkCategory;
import net.smartworks.service.ISmartWorks;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartMessage;
import net.smartworks.util.SmartUtil;

public class WorkInstance extends Instance {

	public static final int TYPE_PROCESS = 31;
	public static final int TYPE_INFORMATION = 32;
	public static final int TYPE_SCHEDULE = 33;

	public static final String CONTROLLER_IWORK_SPACE = "iwork_space.sw";
	public static final String CONTROLLER_PWORK_SPACE = "pwork_space.sw";
	public static final String CONTROLLER_SWORK_SPACE = "swork_space.sw";
	public static final String CONTROLLER_FILE_SPACE = "file_space.sw";

	public static final int DEFAULT_SUB_INSTANCE_FETCH_COUNT = 3; 
	public static final int FETCH_ALL_SUB_INSTANCE = -1; 
	
	private TaskInstanceInfo[] tasks;
	private int numberOfSubInstances;
	private String[] likers;// Array of User Id.
	
	public String[] getLikers() {
		return likers;
	}
	public void setLikers(String[] likers) {
		this.likers = likers;
	}
	public TaskInstanceInfo[] getTasks() {
		return tasks;
	}
	public void setTasks(TaskInstanceInfo[] tasks) {
		this.tasks = tasks;
	}
	public int getNumberOfSubInstances() {
		return numberOfSubInstances;
	}
	public void setNumberOfSubInstances(int numberOfSubInstances) {
		this.numberOfSubInstances = numberOfSubInstances;
	}
	public String getController(){
		if(getWork()==null) return "";
		switch(getWork().getType()){
		case SmartWork.TYPE_INFORMATION:
		case SocialWork.TYPE_BOARD:
		case SocialWork.TYPE_EVENT:
		case SocialWork.TYPE_FILE:
		case SocialWork.TYPE_IMAGE:
		case SocialWork.TYPE_MEMO:
		case SocialWork.TYPE_YTVIDEO:
			if(getWork().getId().equals(SmartWork.ID_FILE_MANAGEMENT))
				return WorkInstance.CONTROLLER_IWORK_SPACE;
			else
				return WorkInstance.CONTROLLER_IWORK_SPACE;
		case SmartWork.TYPE_PROCESS:
			return WorkInstance.CONTROLLER_PWORK_SPACE;
		case SmartWork.TYPE_SCHEDULE:
			return WorkInstance.CONTROLLER_SWORK_SPACE;
		case WorkCategory.TYPE_CATEGORY:
			return "";
		}
		return "";
	}
	
	public String getContextId(){
		if(getWork()==null) return "";
		switch(getWork().getType()){
		case SmartWork.TYPE_INFORMATION:
		case SocialWork.TYPE_BOARD:
		case SocialWork.TYPE_EVENT:
		case SocialWork.TYPE_FILE:
		case SocialWork.TYPE_IMAGE:
		case SocialWork.TYPE_MEMO:
		case SocialWork.TYPE_YTVIDEO:
			if(SmartWork.ID_FILE_MANAGEMENT.equals(getWork().getId()))
				return ISmartWorks.CONTEXT_PREFIX_IWORK_SPACE + getId();
			else if(SmartWork.ID_DEPARTMENT_MANAGEMENT.equals(getWork().getId()))
				return ISmartWorks.CONTEXT_PREFIX_DEPARTMENT_SPACE + getId();
			else if(SmartWork.ID_GROUP_MANAGEMENT.equals(getWork().getId()))
				return ISmartWorks.CONTEXT_PREFIX_GROUP_SPACE + getId();
			else
				return ISmartWorks.CONTEXT_PREFIX_IWORK_SPACE + getId();
		case SmartWork.TYPE_PROCESS:
			return ISmartWorks.CONTEXT_PREFIX_PWORK_SPACE + getId();
		case SmartWork.TYPE_SCHEDULE:
			return ISmartWorks.CONTEXT_PREFIX_SWORK_SPACE + getId();
		case WorkCategory.TYPE_CATEGORY:
			return "";
		}
		return "";
	}
	
	public WorkInstance() {
		super();
		super.setType(Instance.TYPE_WORK);
	}

	public WorkInstance(String id, String subject, Work work, User owner, User lastModifier, LocalDate lastModifiedDate) {
		super(id, subject, Instance.TYPE_WORK, owner, lastModifier, lastModifiedDate);
		super.setWork(work);
	}
	
	public boolean doesCurrentUserLike(){
		if(SmartUtil.isBlankObject(this.likers)) return false;
		String cUserId = SmartUtil.getCurrentUser().getId();
		for(int i=0; i<likers.length; i++)
			if(cUserId.equals(likers[i]))
				return true;
		return false;
	}
	
	public TaskInstanceInfo getMyRunningApprovalTask(){
		if(SmartUtil.isBlankObject(tasks)) return null;
		for(TaskInstanceInfo task : tasks){
			if(task.getTaskType() == TaskInstance.TYPE_APPROVAL_TASK_ASSIGNED 
				&& !SmartUtil.isBlankObject(task.getAssignee()) 
				&& task.getAssignee().getId().equals(SmartUtil.getCurrentUser().getId())
				&& task.getStatus() == TaskInstance.STATUS_RUNNING){
					return task;
				}
		}
		return null;
	}
}
