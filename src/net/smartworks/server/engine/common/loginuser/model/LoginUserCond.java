/*	
 * $Id$
 * created by    : hsshin
 * creation-date : 2012. 6. 1.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.common.loginuser.model;

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

public class LoginUserCond extends MisObjectCond {

	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(LoginUserCond.class);

	protected static final String PREFIX = "Lnu";
	private static final String NAME = CommonUtil.toName(LoginUserCond.class, PREFIX);

	public static final String A_USERID = "userId";
	public static final String A_LOGINTIME = "loginTime";

	private String userId;
	private Date loginTime;

	public LoginUserCond() {
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
		appendAttributeString(A_LOGINTIME, loginTime, buf);
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

		LoginUserCond obj = null;
		if (baseObj == null || !(baseObj instanceof LoginUserCond))
			obj = new LoginUserCond();
		else
			obj = (LoginUserCond)baseObj;
		//부모 attributes, elements값 설정
		MisObjectCond.toObject(node, obj);

		NamedNodeMap attrMap = node.getAttributes();
		if (attrMap != null) {
			Node userId = attrMap.getNamedItem(A_USERID);
			Node loginTime = attrMap.getNamedItem(A_LOGINTIME);

			if(userId != null)
				obj.setUserId(userId.getNodeValue());
			if (loginTime != null)
				obj.setLoginTime(DateUtil.toDate(loginTime.getNodeValue()));
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
	public Date getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

}