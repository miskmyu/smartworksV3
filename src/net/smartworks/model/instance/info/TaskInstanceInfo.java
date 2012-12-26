package net.smartworks.model.instance.info;

import net.smartworks.model.community.WorkSpace;
import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.instance.Instance;
import net.smartworks.model.instance.TaskInstance;
import net.smartworks.model.instance.WorkInstance;
import net.smartworks.model.work.SmartWork;
import net.smartworks.model.work.Work;
import net.smartworks.model.work.WorkCategory;
import net.smartworks.service.ISmartWorks;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartMessage;
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
	private boolean isSubTask=false;
	private String subWorkId;
	private String subWorkFullpathName;
	private String subWorkInstanceId;
	private int numberOfDownloadHistories=0;
	
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
//	public String getController(){
//		if(getWork()==null) return "";
//		switch(getWork().getType()){
//		case SmartWork.TYPE_INFORMATION:
//			return WorkInstance.CONTROLLER_IWORK_SPACE;
//		case SmartWork.TYPE_PROCESS:
//			return WorkInstance.CONTROLLER_PWORK_SPACE;
//		case SmartWork.TYPE_SCHEDULE:
//			return WorkInstance.CONTROLLER_SWORK_SPACE;
//		case WorkCategory.TYPE_CATEGORY:
//			return "";
//		}
//		return "";
//	}
	public String getController(){
		if(SmartUtil.isBlankObject(getWorkId())) return "";
		return TaskInstanceInfo.getController(getWorkId(), getWorkType());
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
	public String getSubWorkFullpathName() {
		return subWorkFullpathName;
	}
	public void setSubWorkFullpathName(String subWorkFullpathName) {
		this.subWorkFullpathName = subWorkFullpathName;
	}
	public int getNumberOfDownloadHistories() {
		return numberOfDownloadHistories;
	}
	public void setNumberOfDownloadHistories(int numberOfDownloadHistories) {
		this.numberOfDownloadHistories = numberOfDownloadHistories;
	}
	public static String getController(String workId, int workType){
		if(SmartUtil.isBlankObject(workId)) return "";
		switch(workType){
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
//	public String getContextId(){
//		if(getWork()==null || getWorkInstance()==null) return "";
//		switch(getWork().getType()){
//		case SmartWork.TYPE_INFORMATION:
//			return ISmartWorks.CONTEXT_PREFIX_IWORK_SPACE + getWorkInstance().getId();
//		case SmartWork.TYPE_PROCESS:
//			return ISmartWorks.CONTEXT_PREFIX_PWORK_SPACE + getWorkInstance().getId();
//		case SmartWork.TYPE_SCHEDULE:
//			return ISmartWorks.CONTEXT_PREFIX_SWORK_SPACE + getWorkInstance().getId();
//		case WorkCategory.TYPE_CATEGORY:
//			return "";
//		}
//		return "";
//	}
	public String getContextId(){
		if(SmartUtil.isBlankObject(getWorkId()) || SmartUtil.isBlankObject(getWorkInstance())) return "";
		return TaskInstanceInfo.getContextId(getWorkId(), getWorkType(), getWorkInstance().getId());
	}
	
	public static String getContextId(String workId, int workType, String workInstanceId){
		if(SmartUtil.isBlankObject(workId) || SmartUtil.isBlankObject(workInstanceId)) return "";
		switch(workType){
		case SmartWork.TYPE_INFORMATION:
			return ISmartWorks.CONTEXT_PREFIX_IWORK_SPACE + workInstanceId;
		case SmartWork.TYPE_PROCESS:
			return ISmartWorks.CONTEXT_PREFIX_PWORK_SPACE + workInstanceId;
		case SmartWork.TYPE_SCHEDULE:
			return ISmartWorks.CONTEXT_PREFIX_SWORK_SPACE + workInstanceId;
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
			&& (this.getTaskType() == TaskInstance.TYPE_PROCESS_TASK_FORWARDED || this.getTaskType() == TaskInstance.TYPE_INFORMATION_TASK_FORWARDED) 
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
			&& (this.getTaskType() == TaskInstance.TYPE_APPROVAL_TASK_ASSIGNED || this.getTaskType() == TaskInstance.TYPE_APPROVAL_TASK_FORWARDED)
			&& !SmartUtil.isBlankObject(this.getAssignee()) 
			&& this.getAssignee().getId().equals(userId)
			&& this.getStatus() == TaskInstance.STATUS_RUNNING){
			return true;
		}
		return false;
	}
	
	public boolean isTaskForMe(String userId){
		if(	!SmartUtil.isBlankObject(this.getAssignee()) && this.getAssignee().getId().equals(userId)){
			return true;
		}
		return false;
	}
	
	public boolean isRunning(){
		if(this.getStatus()==TaskInstance.STATUS_RUNNING || this.getStatus()==TaskInstance.STATUS_APPROVAL_RUNNING || this.getStatus()==TaskInstance.STATUS_DELAYED_RUNNING)
			return true;
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
	
//	public TaskInstance getTaskInstance(){
//		TaskInstance taskInstance = new TaskInstance();
//		taskInstance.setId(this.getId());
//		taskInstance.setName(this.getName());
//		taskInstance.setAssignee((this.getAssignee()!=null) ? this.getAssignee().getUser():null);
//		taskInstance.setAssigner((this.getAssigner()!=null) ? this.getAssigner().getUser():null);
//		taskInstance.setCreatedDate(this.getCreatedDate());
//		taskInstance.setForwardId(this.getForwardId());
//		taskInstance.setLastModifiedDate(this.getLastModifiedDate());
//		taskInstance.setLastModifier((this.getLastModifier()!=null) ? this.getLastModifier().getUser():null);
//		taskInstance.setOwner((this.getOwner()!=null) ? this.getOwner().getUser():null);
//		taskInstance.setPerformer((this.getPerformer()!=null) ? this.getPerformer().getUser():null);
//		taskInstance.setStatus(this.getStatus());
//		taskInstance.setSubject(this.getSubject());
//		taskInstance.setTaskType(this.getTaskType());
//		taskInstance.setType(this.getType());
//		taskInstance.setWork((this.getWork()!=null) ? this.getWork().getWork():null);
//		taskInstance.setWorkInstance((this.getWorkInstance()!=null) ? this.getWorkInstance().getInstance():null);
//		taskInstance.setWorkSpace((this.getWorkSpace()!=null) ? this.getWorkSpace().getWorkSpace():null);
//		return taskInstance;
//	}
	public TaskInstance getTaskInstance(){
		TaskInstance taskInstance = new TaskInstance();
		taskInstance.setId(this.getId());
		taskInstance.setName(this.getName());
		taskInstance.setAssignee((this.getAssignee()!=null) ? this.getAssignee().getUser():null);
		taskInstance.setAssigner((this.getAssigner()!=null) ? this.getAssigner().getUser():null);
		taskInstance.setCreatedDate(this.getCreatedDate());
		taskInstance.setForwardId(this.getForwardId());
		taskInstance.setLastModifiedDate(this.getLastModifiedDate());
		taskInstance.setLastModifier((this.getLastModifier()!=null) ? this.getLastModifier().getUser():null);
		taskInstance.setOwner((this.getOwner()!=null) ? this.getOwner().getUser():null);
		taskInstance.setPerformer((this.getPerformer()!=null) ? this.getPerformer().getUser():null);
		taskInstance.setStatus(this.getStatus());
		taskInstance.setSubject(this.getSubject());
		taskInstance.setTaskType(this.getTaskType());
		taskInstance.setType(this.getType());
		Work work = SmartUtil.isBlankObject(getWorkId()) ? null : new Work(getWorkId(), getWorkName(), getWorkType(), "");
		taskInstance.setWork(work);
		taskInstance.setWorkInstance((this.getWorkInstance()!=null) ? this.getWorkInstance().getInstance():null);
		WorkSpace workSpace = SmartUtil.isBlankObject(getWorkSpaceId()) ? null : new WorkSpace(getWorkSpaceId(), getWorkSpaceName());
		taskInstance.setWorkSpace(workSpace);
		return taskInstance;
	}
}
