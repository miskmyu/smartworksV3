/*	
 * $Id: PurchaseModel.java,v 1.1 2012/01/18 02:13:56 hsshin Exp $
 * created by    : SHIN HYUN SEONG
 * creation-date : 2011. 12. 23.
 * =========================================================
 * Copyright (c) 2011 ManinSoft, Inc. All rights reserved.
 */

package semiteq.scheduler.model;

import java.math.BigDecimal;
import java.util.Date;

public class PurchaseModel {

	public String poNo;				// 발주번호
	public String reqPrsn;			// 요청자
	public String reqPrsnId;		// 요청자 아이디
	public Date poDt;				// 발주일
	public String bpNm;				// 공급처
	public Date dlvyDt;				// 납기일 
	public BigDecimal poDocAmt;		// 발주 금액
	public BigDecimal vatLocAmt;	// 부가세 금액
	public BigDecimal totLocAmt;	// 총 금액
	public String payMeth;			// 결제방법
	public String conFlag;			// 승인 여부
	public String quitFlag;			// 종결여부
	public String reqDocPath;		// 파일 경로
	public String groupId;			// 스마트웍스닷넷에서의 groupId
	public String insertUserId;		// 등록자
	public Date insertDt;			// 등록일
	public String uputUserId;		// 변경자
	public Date uputDT;				// 변경일

	public String getReqPrsnId() {
		return reqPrsnId;
	}
	public void setReqPrsnId(String reqPrsnId) {
		this.reqPrsnId = reqPrsnId;
	}
	public String getPoNo() {
		return poNo;
	}
	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}
	public String getReqPrsn() {
		return reqPrsn;
	}
	public void setReqPrsn(String reqPrsn) {
		this.reqPrsn = reqPrsn;
	}
	public Date getPoDt() {
		return poDt;
	}
	public void setPoDt(Date poDt) {
		this.poDt = poDt;
	}
	public String getBpNm() {
		return bpNm;
	}
	public void setBpNm(String bpNm) {
		this.bpNm = bpNm;
	}
	public Date getDlvyDt() {
		return dlvyDt;
	}
	public void setDlvyDt(Date dlvyDt) {
		this.dlvyDt = dlvyDt;
	}
	public BigDecimal getPoDocAmt() {
		return poDocAmt;
	}
	public void setPoDocAmt(BigDecimal poDocAmt) {
		this.poDocAmt = poDocAmt;
	}
	public BigDecimal getVatLocAmt() {
		return vatLocAmt;
	}
	public void setVatLocAmt(BigDecimal vatLocAmt) {
		this.vatLocAmt = vatLocAmt;
	}
	public BigDecimal getTotLocAmt() {
		return totLocAmt;
	}
	public void setTotLocAmt(BigDecimal totLocAmt) {
		this.totLocAmt = totLocAmt;
	}
	public String getPayMeth() {
		return payMeth;
	}
	public void setPayMeth(String payMeth) {
		this.payMeth = payMeth;
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