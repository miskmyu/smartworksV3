package net.smartworks.model.instance;

import net.smartworks.model.approval.ApprovalLine;
import net.smartworks.model.community.User;
import net.smartworks.model.instance.info.TaskInstanceInfo;
import net.smartworks.model.work.Work;
import net.smartworks.util.LocalDate;

public class InformationWorkInstance extends WorkInstance {

	private int views =0;
	private int numberOfRelatedWorks = 0;
	private boolean isApprovalWork;
	private ApprovalLine approvalLine;
	
	public int getViews() {
		return views;
	}
	public void setViews(int views) {
		this.views = views;
	}
	public int getNumberOfRelatedWorks() {
		return numberOfRelatedWorks;
	}
	public void setNumberOfRelatedWorks(int numberOfRelatedWorks) {
		this.numberOfRelatedWorks = numberOfRelatedWorks;
	}
	public boolean isApprovalWork() {
		return isApprovalWork;
	}
	public void setApprovalWork(boolean isApprovalWork) {
		this.isApprovalWork = isApprovalWork;
	}
	public ApprovalLine getApprovalLine() {
		return approvalLine;
	}
	public void setApprovalLine(ApprovalLine approvalLine) {
		this.approvalLine = approvalLine;
	}

	public InformationWorkInstance() {
		super();
		super.setType(WorkInstance.TYPE_INFORMATION);
	}

	public InformationWorkInstance(String id, String subject, Work work, User owner, User lastModifier,
			LocalDate lastModifiedDate) {
		super(id, subject, work, owner, lastModifier, lastModifiedDate);
		super.setType(WorkInstance.TYPE_INFORMATION);
	}
	
	public int getNumberOfHistories(){
		int numberOfHistories = 0;
		if (this.getTasks() == null)
			return numberOfHistories;
		for(TaskInstanceInfo task : (TaskInstanceInfo[])this.getTasks()){
			if(task.getType() == TaskInstance.TYPE_INFORMATION_TASK_ASSIGNED || task.getType() == TaskInstance.TYPE_INFORMATION_TASK_CREATED
				|| task.getType() == TaskInstance.TYPE_INFORMATION_TASK_UDATED){
				numberOfHistories++;
			}
		}
		return numberOfHistories;
		
	}
}
