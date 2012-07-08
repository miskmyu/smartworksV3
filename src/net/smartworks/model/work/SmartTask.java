package net.smartworks.model.work;

import net.smartworks.model.BaseObject;
import net.smartworks.model.community.User;

public class SmartTask extends BaseObject{

	private SmartForm form;
	private boolean startTask;
	private User assignedUser;
	private String assigningName;
	private String approvalLineId;

	public boolean isStartTask() {
		return startTask;
	}
	public void setStartTask(boolean startTask) {
		this.startTask = startTask;
	}
	public SmartForm getForm() {
		return form;
	}
	public User getAssignedUser() {
		return assignedUser;
	}
	public void setAssignedUser(User assignedUser) {
		this.assignedUser = assignedUser;
	}
	public String getAssigningName() {
		return assigningName;
	}
	public void setAssigningName(String assigningName) {
		this.assigningName = assigningName;
	}
	public void setForm(SmartForm form) {
		this.form = form;
	}
	public String getApprovalLineId() {
		return approvalLineId;
	}
	public void setApprovalLineId(String approvalLineId) {
		this.approvalLineId = approvalLineId;
	}
	
	public SmartTask(){
		super();
	}
	public SmartTask(String id, String name){
		super(id, name);
	}
}
