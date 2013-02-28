package org.claros.intouch.webmail.models;

import java.util.Date;

/**
 * @author Umut Gokbayrak
 */
public class MsgDbObject {
	private Long id;
	private String username;
	private Long folderId;
	private String uniqueId;
	private String uid;
	private String messageId;
	private String sentMessageId;
	private String sender;
	private String receiver;
	private String cc;
	private String bcc;
	private String replyTo;
	private String reader;
	private String subject;
	private Boolean multipart;
	private Integer priority;
	private Date sentDate;
	private Boolean unread;
	private Long msgSize;
	private byte[] email;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Long getFolderId() {
		return folderId;
	}
	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}
	public String getUniqueId() {
		return uniqueId;
	}
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getSentMessageId() {
		return sentMessageId;
	}
	public void setSentMessageId(String sentMessageId) {
		this.sentMessageId = sentMessageId;
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
	public String getReader() {
		return reader;
	}
	public void setReader(String reader) {
		this.reader = reader;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public Boolean getMultipart() {
		return multipart;
	}
	public void setMultipart(Boolean multipart) {
		this.multipart = multipart;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public Date getSentDate() {
		return sentDate;
	}
	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}
	public Boolean getUnread() {
		return unread;
	}
	public void setUnread(Boolean unread) {
		this.unread = unread;
	}
	public Long getMsgSize() {
		return msgSize;
	}
	public void setMsgSize(Long msgSize) {
		this.msgSize = msgSize;
	}
	public byte[] getEmail() {
		return email;
	}
	public void setEmail(byte[] email) {
		this.email = email;
	}
}