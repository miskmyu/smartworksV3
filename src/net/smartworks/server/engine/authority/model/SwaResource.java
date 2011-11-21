/*	
 * $Id$
 * created by    : hsshin
 * creation-date : 2011. 11. 16.
 * =========================================================
 * Copyright (c) 2011 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.authority.model;

import net.smartworks.server.engine.common.model.BaseObject;
import net.smartworks.server.engine.common.model.ClassObject;
import net.smartworks.server.engine.common.model.MisObject;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.common.util.XmlUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class SwaResource extends MisObject {

	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(SwaResource.class);
	
	protected static final String PREFIX = "Swa";
	private static final String NAME = CommonUtil.toName(SwaResource.class, PREFIX);
	
	public static final String A_RESOURCEID = "resourceId";
	public static final String A_TYPE = "type";
	public static final String A_MODE = "mode";
	public static final String A_PERMISSION = "permission";
	
	public static final int TYPE_PROCESS = 1;
	public static final int TYPE_SINGLE = 4;
	
	public static final String PERMISSION_ALL = "PUB_ALL";
	public static final String PERMISSION_SELECT = "PUB_SELECT";
	public static final String PERMISSION_NO = "PUB_NO";
	
	public static final String MODE_READONLY = "RO";
	public static final String MODE_READ = "R";
	public static final String MODE_WRITE = "W";
	public static final String MODE_MODIFY = "M";
	public static final String MODE_DELETE = "D";
	public static final String MODE_WRITEREAD = "WR";

	private String resourceId;
	private int type;
	private String mode;
	private String permission;
	
	public SwaResource() {
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
		appendAttributeString(A_RESOURCEID, resourceId, buf);
		appendAttributeString(A_TYPE, type, buf);
		appendAttributeString(A_MODE, mode, buf);
		appendAttributeString(A_PERMISSION, permission, buf);
		return buf.toString();
	}
	public static BaseObject toObject(Node node, BaseObject baseObj) throws Exception {
		if (node == null)
			return null;
		
		SwaResource obj = null;
		if (baseObj == null || !(baseObj instanceof SwaResource))
			obj = new SwaResource();
		else
			obj = (SwaResource)baseObj;
		
		// 부모 attributes, elements 값 설정
		ClassObject.toObject(node, obj);
		
		NamedNodeMap attrMap = node.getAttributes();
		if (attrMap != null) {
			Node resourceId = attrMap.getNamedItem(A_RESOURCEID);
			Node type = attrMap.getNamedItem(A_TYPE);
			Node mode = attrMap.getNamedItem(A_MODE);
			Node permission = attrMap.getNamedItem(A_PERMISSION);
			if (resourceId != null)
				obj.setResourceId(resourceId.getNodeValue());
			if (type != null)
				obj.setType(CommonUtil.toInt(type.getNodeValue()));
			if (mode != null)
				obj.setMode(mode.getNodeValue());
			if (permission != null)
				obj.setPermission(permission.getNodeValue());
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
	public static SwaResource[] add(SwaResource[] objs, SwaResource obj) {
		if (obj == null)
			return objs;
		int size = 0;
		if (objs != null)
			size = objs.length;
		SwaResource[] newObjs = new SwaResource[size+1];
		int i;
		for (i=0; i<size; i++)
			newObjs[i] = objs[i];
		newObjs[i] = obj;
		return newObjs;
	}
	public static SwaResource[] remove(SwaResource[] objs, SwaResource obj) {
		if (obj == null)
			return objs;
		int size = 0;
		if (objs != null)
			size = objs.length;
		if (size == 0)
			return objs;
		SwaResource[] newObjs = new SwaResource[size-1];
		int i;
		int j = 0;
		for (i=0; i<size; i++) {
			if (objs[i].equals(obj))
				continue;
			newObjs[j++] = objs[i];
		}
		return newObjs;
	}
	public static SwaResource[] left(SwaResource[] objs, SwaResource obj) {
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
		SwaResource[] newObjs = new SwaResource[objs.length];
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
	public static SwaResource[] right(SwaResource[] objs, SwaResource obj) {
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
		SwaResource[] newObjs = new SwaResource[objs.length];
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
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

}