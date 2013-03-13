package net.smartworks.model.instance;

import net.smartworks.model.approval.ApprovalLine;
import net.smartworks.model.community.User;
import net.smartworks.model.instance.info.TaskInstanceInfo;
import net.smartworks.model.work.Work;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartUtil;

public class InformationWorkInstance extends WorkInstance {

	private int views =0;
	private int numberOfRelatedWorks = 0;
	private int numberOfUpdateHistories = 0;
	private int numberOfForwardHistories = 0;
	private int numberOfDownloadHistories = 0;
	private boolean isApprovalWork;
	private ApprovalLine approvalLine;
	private String repeatEventId;
	
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
	public int getNumberOfUpdateHistories() {
		return numberOfUpdateHistories;
	}
	public void setNumberOfUpdateHistories(int numberOfUpdateHistories) {
		this.numberOfUpdateHistories = numberOfUpdateHistories;
	}
	public int getNumberOfForwardHistories() {
		return numberOfForwardHistories;
	}
	public void setNumberOfForwardHistories(int numberOfForwardHistories) {
		this.numberOfForwardHistories = numberOfForwardHistories;
	}
	public int getNumberOfDownloadHistories() {
		return numberOfDownloadHistories;
	}
	public void setNumberOfDownloadHistories(int numberOfDownloadHistories) {
		this.numberOfDownloadHistories = numberOfDownloadHistories;
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
	public String getRepeatEventId() {
		return repeatEventId;
	}
	public void setRepeatEventId(String repeatEventId) {
		this.repeatEventId = repeatEventId;
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
}
