/*	
 * $Id: RequestModel.java,v 1.1 2012/01/18 02:13:56 hsshin Exp $
 * created by    : SHIN HYUN SEONG
 * creation-date : 2011. 12. 23.
 * =========================================================
 * Copyright (c) 2011 ManinSoft, Inc. All rights reserved.
 */

package semiteq.scheduler.model;

import java.util.Date;

public class RequestModel {

	public String prNo;			// 구매요청번호
	public String reqPrsn;		// 요청자
	public String reqPrsnId;	// 요청자 아이디
	public String reqDept;		// 요청부서
	public String reqDeptName;	// 요청부서명
	public String bpCd;			// 공급처
	public String bpPrsnNm;		// 공급처 담당자
	public String telNo1;		// 공급처 전화번호
	public String faxNo;		// Fax 번호
	public Date reqDt;			// 요청일
	public String reqDocPath;	// 파일 경로
	public String groupId;		// 스마트웍스닷넷에서의 groupId
	public Date dlvyDt;			// 납기일
	public String conFlag;		// 승인 여부
	public String quitFlag;		// 종결여부
	public String insertUserId;	// 등록자
	public Date insertDt;		// 등록일
	public String uputUserId;	// 변경자
	public Date uputDT;			// 변경일

	public String getReqPrsnId() {
		return reqPrsnId;
	}
	public void setReqPrsnId(String reqPrsnId) {
		this.reqPrsnId = reqPrsnId;
	}
	public String getPrNo() {
		return prNo;
	}
	public void setPrNo(String prNo) {
		this.prNo = prNo;
	}
	public String getReqPrsn() {
		return reqPrsn;
	}
	public void setReqPrsn(String reqPrsn) {
		this.reqPrsn = reqPrsn;
	}
	public String getReqDept() {
		return reqDept;
	}
	public void setReqDept(String reqDept) {
		this.reqDept = reqDept;
	}
	public String getReqDeptName() {
		return reqDeptName;
	}
	public void setReqDeptName(String reqDeptName) {
		this.reqDeptName = reqDeptName;
	}
	public String getBpCd() {
		return bpCd;
	}
	public void setBpCd(String bpCd) {
		this.bpCd = bpCd;
	}
	public String getBpPrsnNm() {
		return bpPrsnNm;
	}
	public void setBpPrsnNm(String bpPrsnNm) {
		this.bpPrsnNm = bpPrsnNm;
	}
	public String getTelNo1() {
		return telNo1;
	}
	public void setTelNo1(String telNo1) {
		this.telNo1 = telNo1;
	}
	public String getFaxNo() {
		return faxNo;
	}
	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}
	public Date getReqDt() {
		return reqDt;
	}
	public void setReqDt(Date reqDt) {
		this.reqDt = reqDt;
	}
	public String getReqDocPath() {
		return reqDocPath;
	}
	public void setReqDocPath(String reqDocPath) {
		this.reqDocPath = reqDocPath;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public Date getDlvyDt() {
		return dlvyDt;
	}
	public void setDlvyDt(Date dlvyDt) {
		this.dlvyDt = dlvyDt;
	}
	public String getConFlag() {
		return conFlag;
	}
	public void setConFlag(String conFlag) {
		this.conFlag = conFlag;
	}
	public String getQuitFlag() {
		return quitFlag;
	}
	public void setQuitFlag(String quitFlag) {
		this.quitFlag = quitFlag;
	}
	public String getInsertUserId() {
		return insertUserId;
	}
	public void setInsertUserId(String insertUserId) {
		this.insertUserId = insertUserId;
	}
	public Date getInsertDt() {
		return insertDt;
	}
	public void setInsertDt(Date insertDt) {
		this.insertDt = insertDt;
	}
	public String getUputUserId() {
		return uputUserId;
	}
	public void setUputUserId(String uputUserId) {
		this.uputUserId = uputUserId;
	}
	public Date getUputDT() {
		return uputDT;
	}
	public void setUputDT(Date uputDT) {
		this.uputDT = uputDT;
	}

}