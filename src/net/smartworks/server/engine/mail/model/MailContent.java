/*	
 * $Id$
 * created by    : hsshin
 * creation-date : 2012. 6. 12.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.mail.model;

import java.util.Date;

import net.smartworks.server.engine.common.model.MisObject;
import net.smartworks.server.engine.common.util.CommonUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MailContent extends MisObject {

	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(MailContent.class);

	protected static final String PREFIX = "Mail";
	private static final String NAME = CommonUtil.toName(MailContent.class, PREFIX);

	public static final String A_ID = "id";
	public static final String A_USERNAME = "username";
	public static final String A_FOLDERID = "folderId";
	public static final String A_UNIQUEID = "uniqueId";
	public static final String A_SENDER = "sender";
	public static final String A_RECEIVER = "receiver";
	public static final String A_CC = "cc";
	public static final String A_BCC = "bcc";
	public static final String A_REPLYTO = "replyTo";
	public static final String A_SUBJECT = "subject";
	public static final String A_MULTIPART = "multipart";
	public static final String A_PRIORITY = "priority";
	public static final String A_SENTDATE = "sentDate";
	public static final String A_UNREAD = "unread";
	public static final String A_MSGSIZE = "msgSize";
	public static final String A_EMAIL = "email";

	private long id;
	private String username;
	private long folderId;
	private String uniqueId;
	private String sender;
	private String receiver;
	private String cc;
	private String bcc;
	private String replyTo;
	private String subject;
	private int multipart;
	private int priority;
	private Date sentDate;
	private int unread;
	private long msgSize;
	private byte[] email;

	public MailContent() {
		super();
	}

	public String toString(String name, String tab) {
		if (name == null || name.trim().length() == 0)
			name = NAME;
		return super.toString(name, tab);
	}
	public String toLiteString(String name, String tab) {
		if (name == null || name.trim().length() == 0)
			name = NAME;
		return super.toLiteString(name, tab);
	}
	public Object clone() throws CloneNotSupportedException {
		try {
			return toObject(this.toString());
		} catch (Exception e) {
			logger.warn(e, e);
			return null;
		}
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public long getFolderId() {
		return folderId;
	}
	public void setFolderId(long folderId) {
		this.folderId = folderId;
	}
	public String getUniqueId() {
		return uniqueId;
	}
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getCc() {
		return cc;
	}
	public void setCc(String cc) {
		this.cc = cc;
	}
	public String getBcc() {
		return bcc;
	}
	public void setBcc(String bcc) {
		this.bcc = bcc;
	}
	public String getReplyTo() {
		return replyTo;
	}
	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public Date getSentDate() {
		return sentDate;
	}
	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}
	public long getMsgSize() {
		return msgSize;
	}
	public void setMsgSize(long msgSize) {
		this.msgSize = msgSize;
	}
	public byte[] getEmail() {
		return email;
	}
	public void setEmail(byte[] email) {
		this.email = email;
	}
	public int getMultipart() {
		return multipart;
	}
	public void setMultipart(int multipart) {
		this.multipart = multipart;
	}
	public int getUnread() {
		return unread;
	}
	public void setUnread(int unread) {
		this.unread = unread;
	}

}