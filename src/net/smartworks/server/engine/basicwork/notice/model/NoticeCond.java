/*	
 * $Id$
 * created by    : hsshin
 * creation-date : 2011. 11. 16.
 * =========================================================
 * Copyright (c) 2011 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.basicwork.notice.model;

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

public class NoticeCond extends MisObjectCond {

	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(NoticeCond.class);

	protected static final String PREFIX = "Noti";
	private static final String NAME = CommonUtil.toName(NoticeCond.class, PREFIX);

	public static final String A_TITLE = "title";
	public static final String A_CONTENT = "content";
	public static final String A_FILEGROUPID = "fileGroupId";
	public static final String A_STARTDATE = "startDate";
	public static final String A_ENDDATE = "endDate";

	private String title;
	private String content;
	private String fileGroupId;
	private Date startDate;
	private Date endDate;

	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getFileGroupId() {
		return fileGroupId;
	}
	public void setFileGroupId(String fileGroupId) {
		this.fileGroupId = fileGroupId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public NoticeCond() {
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
		appendAttributeString(A_TITLE, title, buf);
		appendAttributeString(A_CONTENT, content, buf);
		appendAttributeString(A_FILEGROUPID, fileGroupId, buf);
		appendAttributeString(A_STARTDATE, startDate, buf);
		appendAttributeString(A_ENDDATE, endDate, buf);
		
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

		NoticeCond obj = null;
		if (baseObj == null || !(baseObj instanceof NoticeCond))
			obj = new NoticeCond();
		else
			obj = (NoticeCond)baseObj;
		//부모 attributes, elements값 설정
		MisObjectCond.toObject(node, obj);
		
		NamedNodeMap attrMap = node.getAttributes();
		if (attrMap != null) {
			Node title = attrMap.getNamedItem(A_TITLE);
			Node content = attrMap.getNamedItem(A_CONTENT);
			Node fileGroupId = attrMap.getNamedItem(A_FILEGROUPID);
			Node startDate = attrMap.getNamedItem(A_STARTDATE);
			Node endDate = attrMap.getNamedItem(A_ENDDATE);
			
			if (content != null)
				obj.setTitle(title.getNodeValue());
				obj.setContent(content.getNodeValue());
				obj.setFileGroupId(fileGroupId.getNodeValue());
				obj.setStartDate(DateUtil.toDate(startDate.getNodeValue()));
				obj.setEndDate(DateUtil.toDate(endDate.getNodeValue()));
		}
		//element값 설정
		
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
	public static NoticeCond[] add(NoticeCond[] objs, NoticeCond obj) {
		if (obj == null)
			return objs;
		int size = 0;
		if (objs != null)
			size = objs.length;
		NoticeCond[] newObjs = new NoticeCond[size+1];
		int i;
		for (i=0; i<size; i++)
			newObjs[i] = objs[i];
		return newObjs;
	}
	public static NoticeCond[] remove(NoticeCond[] objs, NoticeCond obj) {
		if (obj == null)
			return objs;
		int size = 0;
		if (objs != null)
			size = objs.length;
		if (size == 0)
			return objs;
		NoticeCond[] newObjs = new NoticeCond[size-1];
		int i;
		int j = 0;
		for (i=0; i<size; i++) {
			if (objs[i].equals(obj))
				continue;
			newObjs[j++] = objs[i];
		}
		return newObjs;
	}
	public static NoticeCond[] left(NoticeCond[] objs, NoticeCond obj) {
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
		NoticeCond[] newObjs = new NoticeCond[objs.length];
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
	public static NoticeCond[] right(NoticeCond[] objs, NoticeCond obj) {
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
		NoticeCond[] newObjs = new NoticeCond[objs.length];
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