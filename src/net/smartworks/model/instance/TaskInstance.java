package net.smartworks.model.instance;

import net.smartworks.model.community.User;
import net.smartworks.model.work.SmartForm;
import net.smartworks.model.work.SmartWork;
import net.smartworks.model.work.Work;
import net.smartworks.model.work.WorkCategory;
import net.smartworks.service.ISmartWorks;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartMessage;

public class TaskInstance extends Instance {

	public final static int TYPE_INFORMATION_TASK_ASSIGNED = 11;
	public final static int TYPE_INFORMATION_TASK_FORWARDED = 12;
	public final static int TYPE_INFORMATION_TASK_CREATED = 13;
	public final static int TYPE_INFORMATION_TASK_UPDATED = 14;
	public final static int TYPE_INFORMATION_TASK_DELETED = 15;

	public final static int TYPE_PROCESS_TASK_ASSIGNED = 21;
	public final static int TYPE_PROCESS_TASK_FORWARDED = 22;

	public final static int TYPE_SCHEDULE_TASK_ASSIGNED = 31;
	public final static int TYPE_SCHEDULE_TASK_FORWARDED = 32;

	public final static int TYPE_APPROVAL_TASK_ASSIGNED = 41;
	public final static int TYPE_APPROVAL_TASK_FORWARDED = 42;
	public final static int TYPE_APPROVAL_TASK_DRAFTED = 43;

	public final static int TYPE_REMAINING_TASKS_SIZE = 99;

	private String name;
	private int taskType=-1;
	private WorkInstance workInstance;
	private boolean isStartTask=false;
	private User assigner;
	private User assignee;
	private User performer;
	private SmartForm smartForm;
	private String approvalId;
	private String forwardId;
	private boolean isSubTask=false;
	private String subWorkId;
	private String subWorkInstanceId;

	public String getName() {
		if(this.getTaskType() == TaskInstance.TYPE_APPROVAL_TASK_FORWARDED){
			return SmartMessage.getString("common.title.forwarded");
		}
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getTaskType() {
		return taskType;
	}
	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}
	public User getAssigner() {
		return assigner;
	}
	public void setAssigner(User assigner) {
		this.assigner = assigner;
	}
	public User getAssignee() {
		return assignee;
	}
	public void setAssignee(User assignee) {
		this.assignee = assignee;
	}
	public WorkInstance getWorkInstance() {
		return workInstance;
	}
	public void setWorkInstance(WorkInstance workInstance) {
		this.workInstance = workInstance;
	}
	public boolean isStartTask() {
		return isStartTask;
	}
	public void setStartTask(boolean isStartTask) {
		this.isStartTask = isStartTask;
	}
	public User getPerformer() {
		return performer;
	}
	public void setPerformer(User performer) {
		super.setLastModifier(performer);
		this.performer = performer;
	}
	public SmartForm getSmartForm() {
		return smartForm;
	}
	public void setSmartForm(SmartForm smartForm) {
		this.smartForm = smartForm;
	}
	public String getApprovalId() {
		return approvalId;
	}
	public void setApprovalId(String approvalId) {
		this.approvalId = approvalId;
	}
	public String getForwardId() {
		return forwardId;
	}
	public void setForwardId(String forwardId) {
		this.forwardId = forwardId;
	}
	public boolean isSubTask() {
		return isSubTask;
	}
	public void setSubTask(boolean isSubTask) {
		this.isSubTask = isSubTask;
	}
	public String getSubWorkId() {
		return subWorkId;
	}
	public void setSubWorkId(String subWorkId) {
		this.subWorkId = subWorkId;
	}
	public String getSubWorkInstanceId() {
		return subWorkInstanceId;
	}
	public void setSubWorkInstanceId(String subWorkInstanceId) {
		this.subWorkInstanceId = subWorkInstanceId;
	}
	public String getController(){
		if(getWork()==null) return "";
		switch(getWork().getType()){
		case SmartWork.TYPE_INFORMATION:
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
		if(getWork()==null || getWorkInstance()==null) return "";
		switch(getWork().getType()){
		case SmartWork.TYPE_INFORMATION:
			return ISmartWorks.CONTEXT_PREFIX_IWORK_SPACE + getWorkInstance().getId();
		case SmartWork.TYPE_PROCESS:
			return ISmartWorks.CONTEXT_PREFIX_PWORK_SPACE + getWorkInstance().getId();
		case SmartWork.TYPE_SCHEDULE:
			return ISmartWorks.CONTEXT_PREFIX_SWORK_SPACE + getWorkInstance().getId();
		case WorkCategory.TYPE_CATEGORY:
			return "";
		}
		return "";
	}
	
	public TaskInstance() {
		super();
		super.setType(Instance.TYPE_TASK);
	}

	public TaskInstance(String id, String name, int taskType, User owner,
			User performer, LocalDate lastModifiedDate) {
		super(id, name, Instance.TYPE_TASK, owner, performer, lastModifiedDate);
		this.name = name;
		this.taskType = taskType;
	}

	public boolean isRunning(){
		if(this.getStatus()==TaskInstance.STATUS_RUNNING || this.getStatus()==TaskInstance.STATUS_APPROVAL_RUNNING || this.getStatus()==TaskInstance.STATUS_DELAYED_RUNNING)
			return true;
		return false;
	}
	
}
