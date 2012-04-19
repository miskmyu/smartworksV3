/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 4. 12.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.sera.model;

import java.util.Date;

public class SeraFriend {

	public final static int ACCEPT_STATUS_YET = 0;
	public final static int ACCEPT_STATUS_ACCEPT = 1;
	public final static int ACCEPT_STATUS_LATER  = 2;
	public final static int ACCEPT_STATUS_REJECT  = 3;
	public final static int ACCEPT_STATUS_DESTROY = 4;

	private String objId;
	private String requestId;
	private String requestName;
	private String receiveId;
	private String receiveName;
	private int acceptStatus;
	private Date requestDate;
	private Date replyDate;

	public String getObjId() {
		return objId;
	}
	public void setObjId(String objId) {
		this.objId = objId;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getRequestName() {
		return requestName;
	}
	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}
	public String getReceiveId() {
		return receiveId;
	}
	public void setReceiveId(String receiveId) {
		this.receiveId = receiveId;
	}
	public String getReceiveName() {
		return receiveName;
	}
	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}
	public int getAcceptStatus() {
		return acceptStatus;
	}
	public void setAcceptStatus(int acceptStatus) {
		this.acceptStatus = acceptStatus;
	}
	public Date getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}
	public Date getReplyDate() {
		return replyDate;
	}
	public void setReplyDate(Date replyDate) {
		this.replyDate = replyDate;
	}
	
}