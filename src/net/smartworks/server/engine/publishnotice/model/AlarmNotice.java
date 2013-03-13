/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 6. 10.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.publishnotice.model;

import java.util.Date;

import net.smartworks.server.engine.common.model.MisObject;
import net.smartworks.server.engine.common.util.CommonUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AlarmNotice extends MisObject {

	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(AlarmNotice.class);

	protected static final String PREFIX = "";
	private static final String NAME = CommonUtil.toName(AlarmNotice.class, PREFIX);

	public static final String A_NOTICETIME = "noticeTime";
	public static final String A_WORKID = "workId";
	public static final String A_RECORDID = "recordId";
	public static final String A_TARGETUSER = "targetUser";
	public static final String A_COMPANYID = "companyId";
	
	private Date noticeTime;
	private String workId;
	private String recordId;
	private String targetUser;
	private String companyId;
	
	public AlarmNotice(){
		super();
	}
	
	public Date getNoticeTime() {
		return noticeTime;
	}
	public void setNoticeTime(Date noticeTime) {
		this.noticeTime = noticeTime;
	}
	public String getWorkId() {
		return workId;
	}
	public void setWorkId(String workId) {
		this.workId = workId;
	}
	public String getRecordId() {
		return recordId;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	public String getTargetUser() {
		return targetUser;
	}
	public void setTargetUser(String targetUser) {
		this.targetUser = targetUser;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
	
}
