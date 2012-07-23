/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2011. 12. 4.
 * =========================================================
 * Copyright (c) 2011 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.worklist.model;

import java.util.Date;

import net.smartworks.server.engine.common.model.Cond;
import net.smartworks.util.LocalDate;

public class TaskWorkCond extends Cond {

	private static final long serialVersionUID = 1L;

	private String tskObjId;    
	private String[] tskObjIdIns;
	private String tskTitle;    
	private String tskType; 
	private String tskRefType;
	private String tskStatus;
	private String tskAssignee; 
	private String tskAssigneeIdIns;
	private String tskAssigneeOrSpaceId;
	private String tskStartOrAssigned;
	private String tskStartOnly;
	private Date taskLastModifyDate;
	private String tskCreateUser;
	private Date tskCreateDate;
	private Date tskExecuteDate;
	private Date tskExecuteDateFrom;
	private Date tskExecuteDateTo;
	private Date tskExecuteDateBefore;
	private Date tskModifyDateFrom;
	private String tskName;     
	private String tskPrcInstId;
	private String tskForm;    
	private String isStartActivity;
	private String tskWorkSpaceId;
	private String tskWorkSpaceType; 
	private String tskDef;
	private String tskApprovalId;
	private String tskForwardId;
	private String packageId;   
	private String childCtgId;  
	private String childCtgName;
	private String parentCtgId; 
	private String parentCtgName;
	private String prcObjId;                           
	private String prcTitle;                           
	private String prcType;                            
	private String prcStatus;  
	private String[] prcStatusIns;
	private String prcCreateUser;                      
	private String prcDid;                             
	private String prcPrcId;
	private String packageName;
	private String packageStatus;
	private String lastTskObjId;                       
	private String lastTskName;                        
	private String lastTskCreateUser;                  
	private Date lastTskCreateDate;                  
	private String lastTskStatus;                      
	private String lastTskType;                        
	private String lastTskTitle;                       
	private String lastTskAssignee;                    
	private Date lastTskExecuteDate;                 
	private Date lastTskDueDate; 
	private String lastTskForm;                        
	private String lastTskCount;
	private String lastTskDef;
	private LocalDate lastInstanceDate; 
	private Date expectEndDateFrom;
	private Date expectEndDateTo;
	private String[] workSpaceIdNotIns;
	private String[] likeAccessValues;
	
	private String searchKey;

	public LocalDate getLastInstanceDate() {
		return lastInstanceDate;
	}
	public void setLastInstanceDate(LocalDate lastInstanceDate) {
		this.lastInstanceDate = lastInstanceDate;
	}
	public String getTskObjId() {
		return tskObjId;
	}
	public void setTskObjId(String tskObjId) {
		this.tskObjId = tskObjId;
	}
	public String getTskTitle() {
		return tskTitle;
	}
	public void setTskTitle(String tskTitle) {
		this.tskTitle = tskTitle;
	}
	public String getTskType() {
		return tskType;
	}
	public void setTskType(String tskType) {
		this.tskType = tskType;
	}
	public String getTskRefType() {
		return tskRefType;
	}
	public void setTskRefType(String tskRefType) {
		this.tskRefType = tskRefType;
	}
	public String getTskStatus() {
		return tskStatus;
	}
	public void setTskStatus(String tskStatus) {
		this.tskStatus = tskStatus;
	}
	public String getTskAssignee() {
		return tskAssignee;
	}
	public void setTskAssignee(String tskAssignee) {
		this.tskAssignee = tskAssignee;
	}
	public String getTskAssigneeIdIns() {
		return tskAssigneeIdIns;
	}
	public void setTskAssigneeIdIns(String tskAssigneeIdIns) {
		this.tskAssigneeIdIns = tskAssigneeIdIns;
	}
	public String getTskAssigneeOrSpaceId() {
		return tskAssigneeOrSpaceId;
	}
	public void setTskAssigneeOrSpaceId(String tskAssigneeOrSpaceId) {
		this.tskAssigneeOrSpaceId = tskAssigneeOrSpaceId;
	}
	public Date getTaskLastModifyDate() {
		return taskLastModifyDate;
	}
	public void setTaskLastModifyDate(Date taskLastModifyDate) {
		this.taskLastModifyDate = taskLastModifyDate;
	}
	public String getTskCreateUser() {
		return tskCreateUser;
	}
	public void setTskCreateUser(String tskCreateUser) {
		this.tskCreateUser = tskCreateUser;
	}
	public Date getTskCreateDate() {
		return tskCreateDate;
	}
	public void setTskCreateDate(Date tskCreateDate) {
		this.tskCreateDate = tskCreateDate;
	}
	public Date getTskExecuteDate() {
		return tskExecuteDate;
	}
	public void setTskExecuteDate(Date tskExecuteDate) {
		this.tskExecuteDate = tskExecuteDate;
	}
	public Date getTskExecuteDateFrom() {
		return tskExecuteDateFrom;
	}
	public void setTskExecuteDateFrom(Date tskExecuteDateFrom) {
		this.tskExecuteDateFrom = tskExecuteDateFrom;
	}
	public Date getTskExecuteDateTo() {
		return tskExecuteDateTo;
	}
	public void setTskExecuteDateTo(Date tskExecuteDateTo) {
		this.tskExecuteDateTo = tskExecuteDateTo;
	}
	public Date getTskModifyDateFrom() {
		return tskModifyDateFrom;
	}
	public void setTskModifyDateFrom(Date tskModifyDateFrom) {
		this.tskModifyDateFrom = tskModifyDateFrom;
	}
	public Date getTskExecuteDateBefore() {
		return tskExecuteDateBefore;
	}
	public void setTskExecuteDateBefore(Date tskExecuteDateBefore) {
		this.tskExecuteDateBefore = tskExecuteDateBefore;
	}
	public String getTskName() {
		return tskName;
	}
	public void setTskName(String tskName) {
		this.tskName = tskName;
	}
	public String getTskPrcInstId() {
		return tskPrcInstId;
	}
	public void setTskPrcInstId(String tskPrcInstId) {
		this.tskPrcInstId = tskPrcInstId;
	}
	public String getTskForm() {
		return tskForm;
	}
	public void setTskForm(String tskForm) {
		this.tskForm = tskForm;
	}
	public String getIsStartActivity() {
		return isStartActivity;
	}
	public void setIsStartActivity(String isStartActivity) {
		this.isStartActivity = isStartActivity;
	}
	public String getTskWorkSpaceId() {
		return tskWorkSpaceId;
	}
	public void setTskWorkSpaceId(String tskWorkSpaceId) {
		this.tskWorkSpaceId = tskWorkSpaceId;
	}
	public String getTskWorkSpaceType() {
		return tskWorkSpaceType;
	}
	public void setTskWorkSpaceType(String tskWorkSpaceType) {
		this.tskWorkSpaceType = tskWorkSpaceType;
	}
	public String getPackageId() {
		return packageId;
	}
	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getPackageStatus() {
		return packageStatus;
	}
	public void setPackageStatus(String packageStatus) {
		this.packageStatus = packageStatus;
	}
	public String getChildCtgId() {
		return childCtgId;
	}
	public void setChildCtgId(String childCtgId) {
		this.childCtgId = childCtgId;
	}
	public String getChildCtgName() {
		return childCtgName;
	}
	public void setChildCtgName(String childCtgName) {
		this.childCtgName = childCtgName;
	}
	public String getParentCtgId() {
		return parentCtgId;
	}
	public void setParentCtgId(String parentCtgId) {
		this.parentCtgId = parentCtgId;
	}
	public String getParentCtgName() {
		return parentCtgName;
	}
	public void setParentCtgName(String parentCtgName) {
		this.parentCtgName = parentCtgName;
	}
	public String getPrcObjId() {
		return prcObjId;
	}
	public void setPrcObjId(String prcObjId) {
		this.prcObjId = prcObjId;
	}
	public String[] getTskObjIdIns() {
		return tskObjIdIns;
	}
	public void setTskObjIdIns(String[] tskObjIdIns) {
		this.tskObjIdIns = tskObjIdIns;
	}
	public String getPrcTitle() {
		return prcTitle;
	}
	public void setPrcTitle(String prcTitle) {
		this.prcTitle = prcTitle;
	}
	public String getPrcType() {
		return prcType;
	}
	public void setPrcType(String prcType) {
		this.prcType = prcType;
	}
	public String getPrcStatus() {
		return prcStatus;
	}
	public void setPrcStatus(String prcStatus) {
		this.prcStatus = prcStatus;
	}
	public String getPrcCreateUser() {
		return prcCreateUser;
	}
	public void setPrcCreateUser(String prcCreateUser) {
		this.prcCreateUser = prcCreateUser;
	}
	public String getPrcDid() {
		return prcDid;
	}
	public void setPrcDid(String prcDid) {
		this.prcDid = prcDid;
	}
	public String getPrcPrcId() {
		return prcPrcId;
	}
	public void setPrcPrcId(String prcPrcId) {
		this.prcPrcId = prcPrcId;
	}
	public String getLastTskObjId() {
		return lastTskObjId;
	}
	public void setLastTskObjId(String lastTskObjId) {
		this.lastTskObjId = lastTskObjId;
	}
	public String getLastTskName() {
		return lastTskName;
	}
	public void setLastTskName(String lastTskName) {
		this.lastTskName = lastTskName;
	}
	public String getLastTskCreateUser() {
		return lastTskCreateUser;
	}
	public void setLastTskCreateUser(String lastTskCreateUser) {
		this.lastTskCreateUser = lastTskCreateUser;
	}
	public Date getLastTskCreateDate() {
		return lastTskCreateDate;
	}
	public void setLastTskCreateDate(Date lastTskCreateDate) {
		this.lastTskCreateDate = lastTskCreateDate;
	}
	public String getLastTskStatus() {
		return lastTskStatus;
	}
	public void setLastTskStatus(String lastTskStatus) {
		this.lastTskStatus = lastTskStatus;
	}
	public String getLastTskType() {
		return lastTskType;
	}
	public void setLastTskType(String lastTskType) {
		this.lastTskType = lastTskType;
	}
	public String getLastTskTitle() {
		return lastTskTitle;
	}
	public void setLastTskTitle(String lastTskTitle) {
		this.lastTskTitle = lastTskTitle;
	}
	public String getLastTskAssignee() {
		return lastTskAssignee;
	}
	public void setLastTskAssignee(String lastTskAssignee) {
		this.lastTskAssignee = lastTskAssignee;
	}
	public Date getLastTskExecuteDate() {
		return lastTskExecuteDate;
	}
	public void setLastTskExecuteDate(Date lastTskExecuteDate) {
		this.lastTskExecuteDate = lastTskExecuteDate;
	}
	public Date getLastTskDueDate() {
		return lastTskDueDate;
	}
	public void setLastTskDueDate(Date lastTskDueDate) {
		this.lastTskDueDate = lastTskDueDate;
	}
	public String getLastTskForm() {
		return lastTskForm;
	}
	public void setLastTskForm(String lastTskForm) {
		this.lastTskForm = lastTskForm;
	}
	public String getLastTskCount() {
		return lastTskCount;
	}
	public void setLastTskCount(String lastTskCount) {
		this.lastTskCount = lastTskCount;
	}
	public String getTskDef() {
		return tskDef;
	}
	public void setTskDef(String tskDef) {
		this.tskDef = tskDef;
	}
	public String getLastTskDef() {
		return lastTskDef;
	}
	public void setLastTskDef(String lastTskDef) {
		this.lastTskDef = lastTskDef;
	}
	public Date getExpectEndDateFrom() {
		return expectEndDateFrom;
	}
	public void setExpectEndDateFrom(Date expectEndDateFrom) {
		this.expectEndDateFrom = expectEndDateFrom;
	}
	public Date getExpectEndDateTo() {
		return expectEndDateTo;
	}
	public void setExpectEndDateTo(Date expectEndDateTo) {
		this.expectEndDateTo = expectEndDateTo;
	}
	public String getTskStartOrAssigned() {
		return tskStartOrAssigned;
	}
	public void setTskStartOrAssigned(String tskStartOrAssigned) {
		this.tskStartOrAssigned = tskStartOrAssigned;
	}
	public String getSearchKey() {
		return searchKey;
	}
	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}
	public String[] getPrcStatusIns() {
		return prcStatusIns;
	}
	public void setPrcStatusIns(String[] prcStatusIns) {
		this.prcStatusIns = prcStatusIns;
	}
	public String getTskStartOnly() {
		return tskStartOnly;
	}
	public void setTskStartOnly(String tskStartOnly) {
		this.tskStartOnly = tskStartOnly;
	}
	public String getTskApprovalId() {
		return tskApprovalId;
	}
	public void setTskApprovalId(String tskApprovalId) {
		this.tskApprovalId = tskApprovalId;
	}
	public String getTskForwardId() {
		return tskForwardId;
	}
	public void setTskForwardId(String tskForwardId) {
		this.tskForwardId = tskForwardId;
	}
	public String[] getWorkSpaceIdNotIns() {
		return workSpaceIdNotIns;
	}
	public void setWorkSpaceIdNotIns(String[] workSpaceIdNotIns) {
		this.workSpaceIdNotIns = workSpaceIdNotIns;
	}
	public String[] getLikeAccessValues() {
		return likeAccessValues;
	}
	public void setLikeAccessValues(String[] likeAccessValues) {
		this.likeAccessValues = likeAccessValues;
	}

}