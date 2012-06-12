package net.smartworks.model.approval;

import net.smartworks.model.community.User;
import net.smartworks.util.SmartMessage;

public class Approval{

	public static final int APPROVER_CHOOSE_ON_RUNNING = 1;
	public static final int APPROVER_MY_BOSS = 2;
	public static final int APPROVER_CHOOSE_USER = 3;
	
	public static final Approval DEFAULT_APPROVAL_APPROVER = new Approval(SmartMessage.getString("approval.title.approver"), Approval.APPROVER_CHOOSE_ON_RUNNING, null, 0, 1, 0);
	public static final Approval DEFAULT_APPROVAL_REVIEWER = new Approval(SmartMessage.getString("approval.title.reviewer"), Approval.APPROVER_CHOOSE_ON_RUNNING, null, 0, 1, 0);
	public static final Approval DEFAULT_APPROVAL_FINAL_APPROVER = new Approval(SmartMessage.getString("approval.title.final_reviewer"), Approval.APPROVER_CHOOSE_ON_RUNNING, null, 0, 1, 0);

	private String name;
	private int approverType;
	private User approver;
	private int meanTimeDays=0;
	private int meanTimeHours=0;
	private int meanTimeMinutes=30;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getApproverType() {
		return approverType;
	}
	public void setApproverType(int approverType) {
		this.approverType = approverType;
	}
	public User getApprover() {
		return approver;
	}
	public void setApprover(User approver) {
		this.approver = approver;
	}	
	public int getMeanTimeDays() {
		return meanTimeDays;
	}
	public void setMeanTimeDays(int meanTimeDays) {
		this.meanTimeDays = meanTimeDays;
	}
	public int getMeanTimeHours() {
		return meanTimeHours;
	}
	public void setMeanTimeHours(int meanTimeHours) {
		this.meanTimeHours = meanTimeHours;
	}
	public int getMeanTimeMinutes() {
		return meanTimeMinutes;
	}
	public void setMeanTimeMinutes(int meanTimeMinutes) {
		this.meanTimeMinutes = meanTimeMinutes;
	}
	
	public Approval(){
	}	

	public Approval(String name, int approverType, User approver, int meanTimeDays, int meanTimeHours, int meanTimeMinutes){
		super();
		this.name = name;
		this.approverType = approverType;
		this.approver = approver;
		this.meanTimeDays = meanTimeDays;
		this.meanTimeHours = meanTimeHours;
		this.meanTimeMinutes = meanTimeMinutes;
	}
}
