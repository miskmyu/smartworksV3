package net.smartworks.model.approval;

import net.smartworks.model.BaseObject;
import net.smartworks.model.community.User;
import net.smartworks.model.instance.Instance;
import net.smartworks.util.SmartUtil;

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
	
	public Approval getDraft(){
		if(SmartUtil.isBlankObject(approvals)) return null;
		for(Approval approval : approvals){
			if(approval.getStatus() == Instance.STATUS_DRAFTED)
				return approval;
		}
		return null;
	}
}
