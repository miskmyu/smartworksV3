/*	
 * $Id$
 * created by    : hsshin
 * creation-date : 2012. 6. 9.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package org.claros.intouch.webmail.models;

import java.util.Date;

public class HeaderDbObject {

	private Long id;
	private Long folderId;
	private String uniqueId;
	private String sender;
	private String receiver;
	private String cc;
	private String bcc;
	private String replyTo;
	private String subject;
	private boolean multipart;
	private int priority;
	private Date date;
	private Boolean unread;
	private Long msgSize;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the folderId
	 */
	public Long getFolderId() {
		return folderId;
	}
	/**
	 * @param folderId the folderId to set
	 */
	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}
	/**
	 * @return the uniqueId
	 */
	public String getUniqueId() {
		return uniqueId;
	}
	/**
	 * @param uniqueId the uniqueId to set
	 */
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	/**
	 * @return the sender
	 */
	public String getSender() {
		return sender;
	}
	/**
	 * @param sender the sender to set
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}
	/**
	 * @return the receiver
	 */
	public String getReceiver() {
		return receiver;
	}
	/**
	 * @param receiver the receiver to set
	 */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	/**
	 * @return the cc
	 */
	public String getCc() {
		return cc;
	}
	/**
	 * @param cc the cc to set
	 */
	public void setCc(String cc) {
		this.cc = cc;
	}
	/**
	 * @return the bcc
	 */
	public String getBcc() {
		return bcc;
	}
	/**
	 * @param bcc the bcc to set
	 */
	public void setBcc(String bcc) {
		this.bcc = bcc;
	}
	/**
	 * @return the replyTo
	 */
	public String getReplyTo() {
		return replyTo;
	}
	/**
	 * @param replyTo the replyTo to set
	 */
	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return the multipart
	 */
	public boolean isMultipart() {
		return multipart;
	}
	/**
	 * @param multipart the multipart to set
	 */
	public void setMultipart(boolean multipart) {
		this.multipart = multipart;
	}
	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}
	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	/**
	 * @return the unread
	 */
	public Boolean getUnread() {
		return unread;
	}
	/**
	 * @param unread the unread to set
	 */
	public void setUnread(Boolean unread) {
		this.unread = unread;
	}
	/**
	 * @return the msgSize
	 */
	public Long getMsgSize() {
		return msgSize;
	}
	/**
	 * @param msgSize the msgSize to set
	 */
	public void setMsgSize(Long msgSize) {
		this.msgSize = msgSize;
	}
}