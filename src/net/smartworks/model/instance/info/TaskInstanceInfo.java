package net.smartworks.model.instance.info;

import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.instance.Instance;
import net.smartworks.model.instance.TaskInstance;
import net.smartworks.model.instance.WorkInstance;
import net.smartworks.model.work.SmartWork;
import net.smartworks.model.work.WorkCategory;
import net.smartworks.service.ISmartWorks;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartUtil;

public class TaskInstanceInfo extends InstanceInfo {
	
	private String name;
	private int taskType=-1;
	private WorkInstanceInfo  workInstance;
	private UserInfo assigner;
	private UserInfo assignee;
	private UserInfo performer;
	private String formId;
	private String content;
	private String comments;
	private String approvalId="";
	private String approvalTaskId="";
	private String forwardId="";
	private boolean isApprovalWork;
	private String approvalLineId="";
	
	public String getName() {
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
	public WorkInstanceInfo getWorkInstance() {
		return workInstance;
	}
	public void setWorkInstance(WorkInstanceInfo workInstance) {
		this.workInstance = workInstance;
	}
	public UserInfo getAssigner() {
		return assigner;
	}
	public void setAssigner(UserInfo assigner) {
		this.assigner = assigner;
	}
	public UserInfo getAssignee() {
		return assignee;
	}
	public void setAssignee(UserInfo assignee) {
		this.assignee = assignee;
	}
	public UserInfo getPerformer() {
		return performer;
	}
	public void setPerformer(UserInfo performer) {
		this.performer = performer;
	}
	public boolean isApprovalWork() {
		return isApprovalWork;
	}
	public void setApprovalWork(boolean isApprovalWork) {
		this.isApprovalWork = isApprovalWork;
	}
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	public String getApprovalLineId() {
		return approvalLineId;
	}
	public void setApprovalLineId(String approvalLineId) {
		this.approvalLineId = approvalLineId;
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
	
	public TaskInstanceInfo(){
		super();
		super.setType(Instance.TYPE_TASK);
	}
	
	public TaskInstanceInfo(String id, String name, int taskType, UserInfo owner,
			UserInfo performer, LocalDate lastModifiedDate) {
		super(id, name, Instance.TYPE_TASK, owner, performer, lastModifiedDate);
		this.name = name;
		this.taskType = taskType;
	}
	
	public boolean isForwardedTask(String taskInstId){
		if(	taskInstId.equals(this.getId()) 
			&& (this.getTaskType() == TaskInstance.TYPE_INFORMATION_TASK_FORWARDED || this.getType() == TaskInstance.TYPE_APPROVAL_TASK_FORWARDED) 
			&& !SmartUtil.isBlankObject(this.getAssignee())){ 
			return true;
		}
		return false;
		
	}
	
	public boolean isRunningForwardedForMe(String userId, String taskInstId){
		if(	taskInstId.equals(this.getId()) 
			&& (this.getTaskType() == TaskInstance.TYPE_INFORMATION_TASK_FORWARDED || this.getType() == TaskInstance.TYPE_APPROVAL_TASK_FORWARDED) 
			&& !SmartUtil.isBlankObject(this.getAssignee()) 
			&& this.getAssignee().getId().equals(userId)
			&& this.getStatus() == TaskInstance.STATUS_RUNNING){
			return true;
		}
		return false;
		
	}
	
	public boolean isRunningApprovalForMe(String userId, String taskInstId, String processTaskInstId){
		if(SmartUtil.isBlankObject(taskInstId)) taskInstId = this.getId();
		if(	taskInstId.equals(this.getId())
			&& (SmartUtil.isBlankObject(processTaskInstId) || (!SmartUtil.isBlankObject(approvalTaskId) && approvalTaskId.equals(processTaskInstId)))
			&& this.getTaskType() == TaskInstance.TYPE_APPROVAL_TASK_ASSIGNED 
			&& !SmartUtil.isBlankObject(this.getAssignee()) 
			&& this.getAssignee().getId().equals(userId)
			&& this.getStatus() == TaskInstance.STATUS_RUNNING){
			return true;
		}
		return false;
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
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
	public String getApprovalTaskId() {
		return approvalTaskId;
	}
	public void setApprovalTaskId(String approvalTaskId) {
		this.approvalTaskId = approvalTaskId;
	}
}
