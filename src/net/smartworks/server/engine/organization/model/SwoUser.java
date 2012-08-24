/*	
 * $Id$
 * created by    : maninsoft
 * creation-date : 2011. 11. 2.
 * =========================================================
 * Copyright (c) 2011 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.organization.model;

import net.smartworks.server.engine.common.model.BaseObject;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.common.util.XmlUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class SwoUser extends SwoObject {

	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(SwoUser.class);
	
	private static final String NAME = CommonUtil.toName(SwoUser.class, PREFIX);

	public static final String USER_ROLE_DEPT_LEADER = "DEPT LEADER";
	public static final String USER_ROLE_DEPT_MEMBER = "DEPT MEMBER";

	public static final String USER_AUTH_ADMINISTRATOR = "ADMINISTRATOR";
	public static final String USER_AUTH_OPERATOR = "OPERATOR";
	public static final String USER_AUTH_USER = "USER";
	public static final String USER_AUTH_EXTERNALUSER = "EXTERNALUSER";

	public static final String A_NICKNAME = "nickName";
	public static final String A_PASSWORD = "password";
	public static final String A_COMPANYID = "companyId";
	public static final String A_DEPTID = "deptId";
	public static final String A_ADJUNCTDEPTIDS = "adjunctDeptIds";
	public static final String A_ROLEID = "roleId";
	public static final String A_AUTHID = "authId";
	public static final String A_EMPNO = "empNo";
	public static final String A_TYPE = "type";
	public static final String A_POSITION = "position";
	public static final String A_EMAIL = "email";
	public static final String A_USEMAIL = "useMail";
	public static final String A_LANG = "lang";
	public static final String A_LOCALE = "locale";
	public static final String A_TIMEZONE = "timeZone";
	public static final String A_STDTIME = "stdTime";
	public static final String A_PICTURE = "picture";
	public static final String A_RETIREE = "retiree";
	public static final String A_MOBILENO = "mobileNo";
	public static final String A_EXTENSIONNO = "extensionNo";
	public static final String A_SIGN = "sign";
	public static final String A_USESIGN = "useSign";

	private String nickName;
	private String password;
	private String companyId;
	private String deptId;
	private String adjunctDeptIds;
	private String roleId;
	private String authId;
	private String empNo;
	private String type;
	private String position;
	private String email;
	private boolean useMail;
	private boolean useSign;
	private String lang;
	private String locale;
	private String timeZone;
	private String stdTime;
	private String picture;
	private String retiree;
	private String mobileNo;
	private String extensionNo;
	private String sign;

	public SwoUser() {
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
		appendAttributeString(A_NICKNAME, nickName, true, buf);
		appendAttributeString(A_PASSWORD, password, true, buf);
		appendAttributeString(A_COMPANYID, companyId, buf);
		appendAttributeString(A_DEPTID, deptId, buf);
		appendAttributeString(A_ADJUNCTDEPTIDS, adjunctDeptIds, buf);
		appendAttributeString(A_ROLEID, roleId, buf);
		appendAttributeString(A_AUTHID, authId, buf);
		appendAttributeString(A_EMPNO, empNo, buf);
		appendAttributeString(A_TYPE, type, buf);
		appendAttributeString(A_POSITION, position, buf);
		appendAttributeString(A_EMAIL, email, buf);
		appendAttributeString(A_USEMAIL, useMail, buf);
		appendAttributeString(A_LANG, lang, buf);
		appendAttributeString(A_LOCALE, locale, buf);
		appendAttributeString(A_TIMEZONE, timeZone, buf);
		appendAttributeString(A_STDTIME, stdTime, buf);
		appendAttributeString(A_PICTURE, picture, true, buf);
		appendAttributeString(A_RETIREE, retiree, true, buf);
		appendAttributeString(A_MOBILENO, mobileNo, true, buf);
		appendAttributeString(A_EXTENSIONNO, extensionNo, true, buf);
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
		
		SwoUser obj = null;
		if (baseObj == null || !(baseObj instanceof SwoUser))
			obj = new SwoUser();
		else
			obj = (SwoUser)baseObj;
		
		// 부모 attributes, elements 값 설정
		SwoObject.toObject(node, obj);
		
		NamedNodeMap attrMap = node.getAttributes();
		if (attrMap != null) {
			Node nickName = attrMap.getNamedItem(A_NICKNAME);
			Node password = attrMap.getNamedItem(A_PASSWORD);
			Node companyId = attrMap.getNamedItem(A_COMPANYID);
			Node deptId = attrMap.getNamedItem(A_DEPTID);
			Node adjunctDeptIds = attrMap.getNamedItem(A_ADJUNCTDEPTIDS);
			Node roleId = attrMap.getNamedItem(A_ROLEID);
			Node authId = attrMap.getNamedItem(A_AUTHID);
			Node empNo = attrMap.getNamedItem(A_EMPNO);
			Node type = attrMap.getNamedItem(A_TYPE);
			Node position = attrMap.getNamedItem(A_POSITION);
			Node email = attrMap.getNamedItem(A_EMAIL);
			Node useMail = attrMap.getNamedItem(A_USEMAIL);
			Node lang = attrMap.getNamedItem(A_LANG);
			Node locale = attrMap.getNamedItem(A_LOCALE);
			Node timeZone = attrMap.getNamedItem(A_TIMEZONE);
			Node stdTime = attrMap.getNamedItem(A_STDTIME);
			Node picture = attrMap.getNamedItem(A_PICTURE);
			Node retiree = attrMap.getNamedItem(A_RETIREE);
			Node mobileNo = attrMap.getNamedItem("A_MOBILENO");
			Node extensionNo = attrMap.getNamedItem("A_EXTENSIONNO");
			
			if (nickName != null)
				obj.setNickName(nickName.getNodeValue());
			if (password != null)
				obj.setPassword(password.getNodeValue());
			if (companyId != null)
				obj.setCompanyId(companyId.getNodeValue());
			if (deptId != null)
				obj.setDeptId(deptId.getNodeValue());
			if (adjunctDeptIds != null)
				obj.setAdjunctDeptIds(adjunctDeptIds.getNodeValue());
			if (roleId != null)
				obj.setRoleId(roleId.getNodeValue());
			if (authId != null)
				obj.setAuthId(authId.getNodeValue());
			if (empNo != null)
				obj.setEmpNo(empNo.getNodeValue());
			if (type != null)
				obj.setType(type.getNodeValue());
			if (position != null)
				obj.setPosition(position.getNodeValue());
			if (email != null)
				obj.setEmail(email.getNodeValue());
			if (useMail != null)
				obj.setUseMail(CommonUtil.toBoolean(useMail.getNodeValue()));
			if (lang != null)
				obj.setLang(lang.getNodeValue());
			if (locale != null)
				obj.setLocale(locale.getNodeValue());
			if (timeZone != null)
				obj.setTimeZone(timeZone.getNodeValue());
			if (stdTime != null)
				obj.setStdTime(stdTime.getNodeValue());
			if (picture != null)
				obj.setPicture(picture.getNodeValue());
			if (retiree != null)
				obj.setRetiree(retiree.getNodeValue());
			if (mobileNo != null)
				obj.setMobileNo(mobileNo.getNodeValue());
			if (extensionNo != null)
				obj.setExtensionNo(extensionNo.getNodeValue());
		}

		// elements 값 설정
		
		return obj;
	}
	public static BaseObject toObject(String str) throws Exception {
		if (str == null)
			return null;
		Document doc = XmlUtil.toDocument(str);
		if (doc == null)
			return null;
		return toObject(doc.getDocumentElement(), null);
	}
	public static SwoUser[] add(SwoUser[] objs, SwoUser obj) {
		if (obj == null)
			return objs;
		int size = 0;
		if (objs != null)
			size = objs.length;
		SwoUser[] newObjs = new SwoUser[size+1];
		int i;
		for (i=0; i<size; i++)
			newObjs[i] = objs[i];
		newObjs[i] = obj;
		return newObjs;
	}
	public static SwoUser[] remove(SwoUser[] objs, SwoUser obj) {
		if (obj == null)
			return objs;
		int size = 0;
		if (objs != null)
			size = objs.length;
		if (size == 0)
			return objs;
		SwoUser[] newObjs = new SwoUser[size-1];
		int i;
		int j = 0;
		for (i=0; i<size; i++) {
			if (objs[i].equals(obj))
				continue;
			newObjs[j++] = objs[i];
		}
		return newObjs;
	}
	public static SwoUser[] left(SwoUser[] objs, SwoUser obj) {
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
		SwoUser[] newObjs = new SwoUser[objs.length];
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
	public static SwoUser[] right(SwoUser[] objs, SwoUser obj) {
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
		SwoUser[] newObjs = new SwoUser[objs.length];
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

	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getEmpNo() {
		return empNo;
	}
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getAdjunctDeptIds() {
		return adjunctDeptIds;
	}
	public void setAdjunctDeptIds(String adjunctDeptIds) {
		this.adjunctDeptIds = adjunctDeptIds;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getAuthId() {
		return authId;
	}
	public void setAuthId(String authId) {
		this.authId = authId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String passwd) {
		this.password = passwd;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isUseMail() {
		return useMail;
	}
	public void setUseMail(boolean useMail) {
		this.useMail = useMail;
	}
	public String getLang() {
		return this.lang;
	}
	public String getStdTime() {
		return this.stdTime;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public void setStdTime(String stdTime) {
		this.stdTime = stdTime;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getRetiree() {
		return retiree;
	}
	public void setRetiree(String retiree) {
		this.retiree = retiree;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getExtensionNo() {
		return extensionNo;
	}
	public void setExtensionNo(String extensionNo) {
		this.extensionNo = extensionNo;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public boolean isUseSign() {
		return useSign;
	}
	public void setUseSign(boolean useSign) {
		this.useSign = useSign;
	}
}
