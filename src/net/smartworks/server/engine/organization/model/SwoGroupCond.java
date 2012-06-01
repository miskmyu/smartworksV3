/*	
 * $Id$
 * created by    : hsshin
 * creation-date : 2012. 1. 10.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.organization.model;

import java.util.Date;

import net.smartworks.server.engine.common.model.BaseObject;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.common.util.XmlUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class SwoGroupCond extends SwoObjectCond {

	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(SwoGroupCond.class);
	
	private static final String NAME = CommonUtil.toName(SwoGroupCond.class, PREFIX);

	public static final String A_GROUPLEADER = "groupLeader";
	public static final String A_GROUPTYPE = "groupType";
	public static final String A_STATUS = "status";
	public static final String A_DESCRIPTION = "description";
	public static final String A_PICTUTRE = "picture";
	public static final String A_GROUP_MEMBER = "swoGroupMember";
	public static final String A_GROUP_MEMBERS = "swoGroupMembers";

	private String[] groupIdIns;
	private String groupLeader;
	private String notGroupLeader;
	private String groupType;
	private String status;
	private String description;
	private String nameLike;
	private String picture;
	private Date createDateTo;
	private Date lastCreateDateTo;
	private String lastName;
	private SwoGroupMember[] swoGroupMembers;

	public SwoGroupMember[] getSwoGroupMembers() {
		return swoGroupMembers;
	}
	public void setSwoGroupMembers(SwoGroupMember[] swoGroupMembers) {
		this.swoGroupMembers = swoGroupMembers;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getGroupLeader() {
		return groupLeader;
	}
	public void setGroupLeader(String groupLeader) {
		this.groupLeader = groupLeader;
	}
	public String getNotGroupLeader() {
		return notGroupLeader;
	}
	public void setNotGroupLeader(String notGroupLeader) {
		this.notGroupLeader = notGroupLeader;
	}
	public String getGroupType() {
		return groupType;
	}
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getNameLike() {
		return nameLike;
	}
	public void setNameLike(String nameLike) {
		this.nameLike = nameLike;
	}
	public String[] getGroupIdIns() {
		return groupIdIns;
	}
	public void setGroupIdIns(String[] groupIdIns) {
		this.groupIdIns = groupIdIns;
	}
	public Date getCreateDateTo() {
		return createDateTo;
	}
	public void setCreateDateTo(Date createDateTo) {
		this.createDateTo = createDateTo;
	}
	public Date getLastCreateDateTo() {
		return lastCreateDateTo;
	}
	public void setLastCreateDateTo(Date lastCreateDateTo) {
		this.lastCreateDateTo = lastCreateDateTo;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public SwoGroupCond() {
		super();
	}
	public String toString(String name, String tab) {
		if (name == null || name.trim().length() == 0)
			name = NAME;
		return super.toString(name, tab);
	}
	public String toAttributesString() {
		StringBuffer buf = new StringBuffer();
		buf.append(super.toAttributesString());
		appendAttributeString(A_GROUPLEADER, groupLeader, buf);
		appendAttributeString(A_GROUPTYPE, groupType, buf);
		appendAttributeString(A_STATUS, status, buf);
		appendAttributeString(A_DESCRIPTION, description, buf);
		appendAttributeString(A_PICTUTRE, picture, buf);

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
		
		SwoGroupCond obj = null;
		if (baseObj == null || !(baseObj instanceof SwoGroupCond))
			obj = new SwoGroupCond();
		else
			obj = (SwoGroupCond)baseObj;
		SwoObjectCond.toObject(node, obj);
		
		NamedNodeMap attrMap = node.getAttributes();
		if (attrMap != null) {
			Node groupLeader = attrMap.getNamedItem(A_GROUPLEADER);
			Node groupType = attrMap.getNamedItem(A_GROUPTYPE);
			Node status = attrMap.getNamedItem(A_STATUS);
			Node description = attrMap.getNamedItem(A_DESCRIPTION);
			Node picture = attrMap.getNamedItem(A_PICTUTRE);

			if (groupLeader != null)
				obj.setGroupLeader(groupLeader.getNodeValue());
			if (groupType != null)
				obj.setGroupType(groupType.getNodeValue());
			if (status != null)
				obj.setStatus(status.getNodeValue());
			if (description != null)
				obj.setDescription(description.getNodeValue());
			if (picture != null)
				obj.setPicture(picture.getNodeValue());
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
	public static SwoGroupCond[] add(SwoGroupCond[] objs, SwoGroupCond obj) {
		if (obj == null)
			return objs;
		int size = 0;
		if (objs != null)
			size = objs.length;
		SwoGroupCond[] newObjs = new SwoGroupCond[size+1];
		int i;
		for (i=0; i<size; i++)
			newObjs[i] = objs[i];
		newObjs[i] = obj;
		return newObjs;
	}
	public static SwoGroupCond[] remove(SwoGroupCond[] objs, SwoGroupCond obj) {
		if (obj == null)
			return objs;
		int size = 0;
		if (objs != null)
			size = objs.length;
		if (size == 0)
			return objs;
		SwoGroupCond[] newObjs = new SwoGroupCond[size-1];
		int i;
		int j = 0;
		for (i=0; i<size; i++) {
			if (objs[i].equals(obj))
				continue;
			newObjs[j++] = objs[i];
		}
		return newObjs;
	}
	public static SwoGroupCond[] left(SwoGroupCond[] objs, SwoGroupCond obj) {
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
		SwoGroupCond[] newObjs = new SwoGroupCond[objs.length];
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
	public static SwoGroupCond[] right(SwoGroupCond[] objs, SwoGroupCond obj) {
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
		SwoGroupCond[] newObjs = new SwoGroupCond[objs.length];
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

}