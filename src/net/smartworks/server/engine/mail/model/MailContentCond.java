/*	
 * $Id$
 * created by    : hsshin
 * creation-date : 2012. 6. 12.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.mail.model;

import java.util.Date;

import net.smartworks.server.engine.common.model.BaseObject;
import net.smartworks.server.engine.common.model.MisObjectCond;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.common.util.DateUtil;
import net.smartworks.server.engine.common.util.XmlUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class MailContentCond extends MisObjectCond {

	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(MailContentCond.class);

	protected static final String PREFIX = "Mail";
	private static final String NAME = CommonUtil.toName(MailContentCond.class, PREFIX);

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
	public static final String A_SEARCHKEY = "searchKey";

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
	private String searchKey;

	public MailContentCond() {
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

	public String toAttributesString() {
		StringBuffer buf = new StringBuffer();
		buf.append(super.toAttributesString());

		appendAttributeString(A_ID, id, buf);
		appendAttributeString(A_USERNAME, username, buf);
		appendAttributeString(A_FOLDERID, folderId, buf);
		appendAttributeString(A_UNIQUEID, uniqueId, buf);
		appendAttributeString(A_SENDER, sender, buf);
		appendAttributeString(A_RECEIVER, receiver, buf);
		appendAttributeString(A_CC, cc, buf);
		appendAttributeString(A_BCC, bcc, buf);
		appendAttributeString(A_REPLYTO, replyTo, buf);
		appendAttributeString(A_SUBJECT, subject, buf);
		appendAttributeString(A_MULTIPART, multipart, buf);
		appendAttributeString(A_PRIORITY, priority, buf);
		appendAttributeString(A_SENTDATE, sentDate, buf);
		appendAttributeString(A_UNREAD, unread, buf);
		appendAttributeString(A_MSGSIZE, msgSize, buf);
		appendAttributeString(A_SEARCHKEY, searchKey, buf);
		return buf.toString();
	}

	public String toElementsString(String tab, boolean lite) {
		StringBuffer buf = new StringBuffer();
		buf.append(super.toElementsString(tab, lite));
		return buf.toString();
	}

	public static BaseObject toObject(Node node, BaseObject baseObj) throws Exception {
		if (node == null)
			return null;

		MailContentCond obj = null;
		if (baseObj == null || !(baseObj instanceof MailContentCond))
			obj = new MailContentCond();
		else
			obj = (MailContentCond)baseObj;
		MisObjectCond.toObject(node, obj);

		NamedNodeMap attrMap = node.getAttributes();
		if (attrMap != null) {

			Node id = attrMap.getNamedItem(A_ID);
			Node username = attrMap.getNamedItem(A_USERNAME);
			Node folderId = attrMap.getNamedItem(A_FOLDERID);
			Node uniqueId = attrMap.getNamedItem(A_UNIQUEID);
			Node sender = attrMap.getNamedItem(A_SENDER);
			Node receiver = attrMap.getNamedItem(A_RECEIVER);
			Node cc = attrMap.getNamedItem(A_CC);
			Node bcc = attrMap.getNamedItem(A_BCC);
			Node replyTo = attrMap.getNamedItem(A_REPLYTO);
			Node subject = attrMap.getNamedItem(A_SUBJECT);
			Node multipart = attrMap.getNamedItem(A_MULTIPART);
			Node priority = attrMap.getNamedItem(A_PRIORITY);
			Node sentDate = attrMap.getNamedItem(A_SENTDATE);
			Node unread = attrMap.getNamedItem(A_UNREAD);
			Node msgSize = attrMap.getNamedItem(A_MSGSIZE);
			Node searchKey = attrMap.getNamedItem(A_SEARCHKEY);

			if(id != null)
				obj.setId(CommonUtil.toLong(id.getNodeValue()));
			if(username != null)
				obj.setUsername(username.getNodeValue());
			if(folderId != null)
				obj.setFolderId(CommonUtil.toLong(folderId.getNodeValue()));
			if(uniqueId != null)
				obj.setUniqueId(uniqueId.getNodeValue());
			if(sender != null)
				obj.setSender(sender.getNodeValue());
			if(receiver != null)
				obj.setReceiver(receiver.getNodeValue());
			if(cc != null)
				obj.setCc(cc.getNodeValue());
			if(bcc != null)
				obj.setBcc(bcc.getNodeValue());
			if(replyTo != null)
				obj.setReplyTo(replyTo.getNodeValue());
			if(subject != null)
				obj.setSubject(subject.getNodeValue());
			if(multipart != null)
				obj.setMultipart(CommonUtil.toInt(multipart.getNodeValue()));
			if(priority != null)
				obj.setPriority(CommonUtil.toInt(priority.getNodeValue()));
			if(sentDate != null)
				obj.setSentDate(DateUtil.toDate(sentDate.getNodeValue()));
			if(unread != null)
				obj.setUnread(CommonUtil.toInt(unread.getNodeValue()));
			if(msgSize != null)
				obj.setMsgSize(CommonUtil.toLong(msgSize.getNodeValue()));
			if(searchKey != null)
				obj.setSearchKey(searchKey.getNodeValue());
		}

		return  obj;
	}

	public static BaseObject toObject(String str) throws Exception {
		if (str == null)
			return null;
		Document doc = XmlUtil.toDocument(str);
		if (doc == null)
			return null;
		return toObject(doc.getDocumentElement(), null);
	}

	public static MailContentCond[] add(MailContentCond[] objs, MailContentCond obj) {
		if (obj == null)
			return objs;
		int size = 0;
		if (objs != null)
			size = objs.length;
		MailContentCond[] newObjs = new MailContentCond[size+1];
		int i;
		for (i=0; i<size; i++)
			newObjs[i] = objs[i];
		return newObjs;
	}

	public static MailContentCond[] remove(MailContentCond[] objs, MailContentCond obj) {
		if (obj == null)
			return objs;
		int size = 0;
		if (objs != null)
			size = objs.length;
		if (size == 0)
			return objs;
		MailContentCond[] newObjs = new MailContentCond[size-1];
		int i;
		int j = 0;
		for (i=0; i<size; i++) {
			if (objs[i].equals(obj))
				continue;
			newObjs[j++] = objs[i];
		}
		return newObjs;
	}

	public static MailContentCond[] left(MailContentCond[] objs, MailContentCond obj) {
		if (objs == null || objs.length == 0 || obj == null)
			return objs;
		int idx = -1;
		for (int i=0; i<objs.length; i++) {
			if (!objs[i].equals(obj))
				continue;
			idx = i;
			break;
		}
		if (idx < 1)
			return objs;
		MailContentCond[] newObjs = new MailContentCond[objs.length];
		for (int i=0; i<objs.length; i++) {
			if (i == idx) {
				newObjs[i] = objs[idx-1];
				continue;
			} else if (i == idx-1) {
				newObjs[i] = objs[idx];
				continue;
			}
			newObjs[i] = objs[i];
		}
		return newObjs;
	}

	public static MailContentCond[] right(MailContentCond[] objs, MailContentCond obj) {
		if (objs == null || objs.length == 0 || obj == null)
			return objs;
		int idx = -1;
		for (int i=0; i<objs.length; i++) {
			if (!objs[i].equals(obj))
				continue;
			idx = i;
			break;
		}
		if (idx == -1 || idx+1 == objs.length)
			return objs;
		MailContentCond[] newObjs = new MailContentCond[objs.length];
		for (int i=0; i<objs.length; i++) {
			if (i == idx) {
				newObjs[i] = objs[idx+1];
				continue;
			} else if (i == idx+1) {
				newObjs[i] = objs[idx];
				continue;
			}
			newObjs[i] = objs[i];
		}
		return newObjs;
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
	public String getSearchKey() {
		return searchKey;
	}
	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

}