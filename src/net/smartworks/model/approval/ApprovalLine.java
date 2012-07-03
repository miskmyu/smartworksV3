package net.smartworks.model.approval;

import net.smartworks.model.BaseObject;
import net.smartworks.util.SmartMessage;

public class ApprovalLine extends BaseObject{
	
	public static final String ID_DEFAULT_APPROVAL_LINE_2_LEVEL = "system.approvalLine.default.2level";
	public static final String ID_DEFAULT_APPROVAL_LINE_3_LEVEL = "system.approvalLine.default.3level";
	public static final ApprovalLine DEFAULT_APPROVAL_LINE_2_LEVEL = new ApprovalLine(
			ApprovalLine.ID_DEFAULT_APPROVAL_LINE_2_LEVEL, 
			SmartMessage.getString("approval.name.defaul_2_level"), 
			SmartMessage.getString("approval.desc.defaul_2_level"), 
			2, new Approval[]{Approval.DEFAULT_APPROVAL_REVIEWER, Approval.DEFAULT_APPROVAL_APPROVER});
	public static final ApprovalLine DEFAULT_APPROVAL_LINE_3_LEVEL = new ApprovalLine(
			ApprovalLine.ID_DEFAULT_APPROVAL_LINE_3_LEVEL, 
			SmartMessage.getString("approval.name.defaul_3_level"), 
			SmartMessage.getString("approval.desc.defaul_3_level"), 
			3, new Approval[]{Approval.DEFAULT_APPROVAL_REVIEWER, Approval.DEFAULT_APPROVAL_APPROVER, Approval.DEFAULT_APPROVAL_FINAL_APPROVER});
	
	private String desc;
	private int approvalLevel;
	private Approval[] approvals;
	
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public int getApprovalLevel() {
		return approvalLevel;
	}
	public void setApprovalLevel(int approvalLevel) {
		this.approvalLevel = approvalLevel;
	}
	public Approval[] getApprovals() {
		return approvals;
	}
	public void setApprovals(Approval[] approvals) {
		this.approvals = approvals;
	}
	public ApprovalLine(){
		super();
	}
	public ApprovalLine(String id, String name){
		super(id, name);
	}	
	public ApprovalLine(String id, String name, String desc, int approvalLevel, Approval[] approvals){
		super(id, name);
		this.desc = desc;
		this.approvalLevel = approvalLevel;
		this.approvals = approvals;
	}	
}
