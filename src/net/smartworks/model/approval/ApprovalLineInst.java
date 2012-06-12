package net.smartworks.model.approval;

import net.smartworks.model.BaseObject;

public class ApprovalLineInst extends BaseObject{
	

	private Approval[] approvals;
	
	public Approval[] getApprovals() {
		return approvals;
	}
	public void setApprovals(Approval[] approvals) {
		this.approvals = approvals;
	}
	public ApprovalLineInst(){
		super();
	}
	public ApprovalLineInst(String id, String name){
		super(id, name);
	}	
}
