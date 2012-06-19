/*	
 * $Id$
 * created by    : hsshin
 * creation-date : 2012. 6. 18.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.mail.model;

import net.smartworks.server.engine.common.model.BaseObject;
import net.smartworks.server.engine.common.model.MisObjectCond;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.common.util.XmlUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class MailAccountCond extends MisObjectCond {

	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(MailAccountCond.class);

	protected static final String PREFIX = "Mail";
	private static final String NAME = CommonUtil.toName(MailAccountCond.class, PREFIX);

	public static final String A_USERID = "userId";
	public static final String A_MAILSERVERID = "mailServerId";
	public static final String A_MAILSERVERNAME = "mailServerName";
	public static final String A_MAILID = "mailId";
	public static final String A_MAILPASSWORD = "mailPassword";

	private String userId;
	private String mailServerId;
	private String mailServerName;
	private String mailId;
	private String mailPassword;

	public MailAccountCond() {
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

		appendAttributeString(A_USERID, userId, buf);
		appendAttributeString(A_MAILSERVERID, mailServerId, buf);
		appendAttributeString(A_MAILSERVERNAME, mailServerName, buf);
		appendAttributeString(A_MAILID, mailId, buf);
		appendAttributeString(A_MAILPASSWORD, mailPassword, buf);
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

		MailAccountCond obj = null;
		if (baseObj == null || !(baseObj instanceof MailAccountCond))
			obj = new MailAccountCond();
		else
			obj = (MailAccountCond)baseObj;
		MisObjectCond.toObject(node, obj);

		NamedNodeMap attrMap = node.getAttributes();
		if (attrMap != null) {
			
			Node userId = attrMap.getNamedItem(A_USERID);
			Node mailServerId = attrMap.getNamedItem(A_MAILSERVERID);
			Node mailServerName = attrMap.getNamedItem(A_MAILSERVERNAME);
			Node mailId = attrMap.getNamedItem(A_MAILID);
			Node mailPassword = attrMap.getNamedItem(A_MAILPASSWORD);

			if(userId != null)
				obj.setUserId(userId.getNodeValue());
			if(mailServerId != null)
				obj.setMailServerId(mailServerId.getNodeValue());
			if(mailServerName != null)
				obj.setMailServerName(mailServerName.getNodeValue());
			if(mailId != null)
				obj.setMailId(mailId.getNodeValue());
			if(mailPassword != null)
				obj.setMailPassword(mailPassword.getNodeValue());
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

	public static MailAccountCond[] add(MailAccountCond[] objs, MailAccountCond obj) {
		if (obj == null)
			return objs;
		int size = 0;
		if (objs != null)
			size = objs.length;
		MailAccountCond[] newObjs = new MailAccountCond[size+1];
		int i;
		for (i=0; i<size; i++)
			newObjs[i] = objs[i];
		return newObjs;
	}

	public static MailAccountCond[] remove(MailAccountCond[] objs, MailAccountCond obj) {
		if (obj == null)
			return objs;
		int size = 0;
		if (objs != null)
			size = objs.length;
		if (size == 0)
			return objs;
		MailAccountCond[] newObjs = new MailAccountCond[size-1];
		int i;
		int j = 0;
		for (i=0; i<size; i++) {
			if (objs[i].equals(obj))
				continue;
			newObjs[j++] = objs[i];
		}
		return newObjs;
	}

	public static MailAccountCond[] left(MailAccountCond[] objs, MailAccountCond obj) {
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
		MailAccountCond[] newObjs = new MailAccountCond[objs.length];
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

	public static MailAccountCond[] right(MailAccountCond[] objs, MailAccountCond obj) {
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
		MailAccountCond[] newObjs = new MailAccountCond[objs.length];
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

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getMailServerId() {
		return mailServerId;
	}
	public void setMailServerId(String mailServerId) {
		this.mailServerId = mailServerId;
	}
	public String getMailServerName() {
		return mailServerName;
	}
	public void setMailServerName(String mailServerName) {
		this.mailServerName = mailServerName;
	}
	public String getMailId() {
		return mailId;
	}
	public void setMailId(String mailId) {
		this.mailId = mailId;
	}
	public String getMailPassword() {
		return mailPassword;
	}
	public void setMailPassword(String mailPassword) {
		this.mailPassword = mailPassword;
	}

}