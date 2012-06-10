/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 6. 10.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.publishnotice.model;

import net.smartworks.model.notice.Notice;
import net.smartworks.server.engine.common.model.MisObject;
import net.smartworks.server.engine.common.util.CommonUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PublishNotice extends MisObject {

	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(PublishNotice.class);

	protected static final String PREFIX = "";
	private static final String NAME = CommonUtil.toName(PublishNotice.class, PREFIX);

	public static final int TYPE_MESSAGE = Notice.TYPE_MESSAGE;
	public static final int TYPE_NOTIFICATION = Notice.TYPE_NOTIFICATION;
	public static final int TYPE_COMMENT = Notice.TYPE_COMMENT;
	public static final int TYPE_ASSIGNED = Notice.TYPE_ASSIGNED;
	public static final int TYPE_MAILBOX = Notice.TYPE_MAILBOX;
	public static final int TYPE_SAVEDBOX = Notice.TYPE_SAVEDBOX;

	public static final String REFTYPE_ASSIGNED_TASK = "assignedTask";
	public static final String REFTYPE_DELAYED_TASK = "delayedTask";
	public static final String REFTYPE_GROUPJOINREQUEST = "groupJoinReq"; 
	public static final String REFTYPE_MESSAGE = "message";
	public static final String REFTYPE_COMMENT_PROCESSINST = "commentProcessInst";
	public static final String REFTYPE_COMMENT_INFORWORK = "commentInforwork";
	public static final String REFTYPE_COMMENT_PROCESSMANUAL = "commentPrcManual";
	public static final String REFTYPE_COMMENT_INFOWORKMANUAL = "commentInfoworkManual";
	public static final String REFTYPE_MAIL = "mail";
	
	public static final String A_ASSIGNEE = "assignee";
	public static final String A_TYPE = "type";
	public static final String A_REFTYPE = "refType";
	public static final String A_REFID = "refId";
	
	private String assignee;
	private int type;
	private String refType;
	private String refId;

	public PublishNotice(){
	}
	public PublishNotice(String assignee, int type, String refType, String refId) {
		this.assignee = assignee;
		this.type = type;
		this.refType = refType;
		this.refId = refId;
	}
	
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getRefType() {
		return refType;
	}
	public void setRefType(String refType) {
		this.refType = refType;
	}
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
}
