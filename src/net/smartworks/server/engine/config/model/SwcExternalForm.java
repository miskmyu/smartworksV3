/*	
 * $Id$
 * created by    : hsshin
 * creation-date : 2011. 11. 9.
 * =========================================================
 * Copyright (c) 2011 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.config.model;

import net.smartworks.server.engine.common.model.BaseObject;
import net.smartworks.server.engine.common.model.MisObject;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.common.util.XmlUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class SwcExternalForm extends MisObject {

	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(SwcExternalForm.class);
	
	protected static final String PREFIX = "Swc";
	private static final String NAME = CommonUtil.toName(SwcExternalForm.class, PREFIX);
	
	public static final String A_EXTERNALFORMNAME = "webAppServiceName";
	public static final String A_EXTERNALFORMURL = "webAppServiceUrl";
	public static final String A_MODIFYMETHOD = "modifyMethod";
	public static final String A_VIEWMETHOD = "viewMethod";
	public static final String A_DESCRIPTION = "description";
	public static final String A_EXTERNALFORMPARAMETERS = "webAppServiceParameters";
	public static final String A_EXTERNALFORMPARAMETER = "webAppServiceParameter";

	private String webAppServiceName;
	private String webAppServiceUrl;
	private String modifyMethod;
	private String viewMethod;
	private String description;

	private SwcExternalFormParameter[] swcExternalFormParameters;

	public SwcExternalForm() {
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
		appendAttributeString(A_EXTERNALFORMNAME, webAppServiceName, buf);
		appendAttributeString(A_EXTERNALFORMURL, webAppServiceUrl, buf);
		appendAttributeString(A_MODIFYMETHOD, modifyMethod, buf);
		appendAttributeString(A_VIEWMETHOD, viewMethod, buf);
		return buf.toString();
	}
	public String toElementsString(String tab, boolean lite) {
		StringBuffer buf = new StringBuffer();
		buf.append(super.toElementsString(tab, lite));
		appendElementString(A_DESCRIPTION, description, tab, true, buf);
		appendElementsString(A_EXTERNALFORMPARAMETERS, A_EXTERNALFORMPARAMETER, getSwcExternalFormParameters(), tab, lite, buf);
		return buf.toString();
	}
	public static BaseObject toObject(Node node, BaseObject baseObj) throws Exception {
		if (node == null)
			return null;
		
		SwcExternalForm obj = null;
		if (baseObj == null || !(baseObj instanceof SwcExternalForm))
			obj = new SwcExternalForm();
		else
			obj = (SwcExternalForm)baseObj;
		
		//부모 attributes, elements 값 설정
		MisObject.toObject(node, obj);
		
		NamedNodeMap attrMap = node.getAttributes();
		if (attrMap != null) {
			Node webAppServiceName = attrMap.getNamedItem(A_EXTERNALFORMNAME);
			Node webAppServiceUrl = attrMap.getNamedItem(A_EXTERNALFORMURL);
			Node modifyMethod = attrMap.getNamedItem(A_MODIFYMETHOD);
			Node viewMethod = attrMap.getNamedItem(A_VIEWMETHOD);
			Node description = attrMap.getNamedItem(A_DESCRIPTION);

			if (webAppServiceName != null)
				obj.setWebAppServiceName(webAppServiceName.getNodeValue());
			if (webAppServiceUrl != null)
				obj.setWebAppServiceUrl(webAppServiceUrl.getNodeValue());
			if (modifyMethod != null)
				obj.setModifyMethod(modifyMethod.getNodeValue());
			if (viewMethod != null)
				obj.setViewMethod(viewMethod.getNodeValue());
			if (description != null)
				obj.setDescription(description.getNodeValue());
		}
		//elements 값 설정
		NodeList childNodeList = node.getChildNodes();
		if (childNodeList == null || childNodeList.getLength() == 0)
			return obj;
		for (int i=0; i<childNodeList.getLength(); i++) {
			Node childNode  = childNodeList.item(i);
			if (childNode.getNodeType() != Node.ELEMENT_NODE || childNode.getNodeName() == null)
				continue;
			if (childNode.getNodeName().equals("A_EXTERNALFORMPARAMETER")) {
				obj.addExfExternalFormParameter((SwcExternalFormParameter)SwcExternalFormParameter.toObject(childNode, null));
			}
		}
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
	public static SwcExternalForm[] add(SwcExternalForm[] objs, SwcExternalForm obj) {
		if (obj == null)
			return objs;
		int size = 0;
		if (objs != null)
			size = objs.length;
		SwcExternalForm[] newObjs = new SwcExternalForm[size+1];
		int i;
		for (i=0; i<size; i++)
			newObjs[i] = objs[i];
		newObjs[i] = obj;
		return newObjs;
	}
	public static SwcExternalForm[] remove(SwcExternalForm[] objs, SwcExternalForm obj) {
		if (obj == null)
			return objs;
		int size = 0;
		if (objs != null)
			size = objs.length;
		if (size == 0)
			return objs;
		SwcExternalForm[] newObjs = new SwcExternalForm[size-1];
		int i;
		int j = 0;
		for (i=0; i<size; i++) {
			if (objs[i].equals(obj))
				continue;
			newObjs[j++] = objs[i];
		}
		return newObjs;
	}
	public static SwcExternalForm[] left(SwcExternalForm[] objs, SwcExternalForm obj) {
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
		SwcExternalForm[] newObjs = new SwcExternalForm[objs.length];
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
	public static SwcExternalForm[] right(SwcExternalForm[] objs, SwcExternalForm obj) {
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
		SwcExternalForm[] newObjs = new SwcExternalForm[objs.length];
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
	public void addExfExternalFormParameter(SwcExternalFormParameter swcWebAppServiceParameter) {
		if (swcWebAppServiceParameter == null)
			return;
		this.setSwcExternalFormParameters(SwcExternalFormParameter.add(this.getSwcExternalFormParameters(), swcWebAppServiceParameter));
	}

	public String getWebAppServiceName() {
		return webAppServiceName;
	}
	public void setWebAppServiceName(String webAppServiceName) {
		this.webAppServiceName = webAppServiceName;
	}
	public String getWebAppServiceUrl() {
		return webAppServiceUrl;
	}
	public void setWebAppServiceUrl(String webAppServiceUrl) {
		this.webAppServiceUrl = webAppServiceUrl;
	}
	public String getModifyMethod() {
		return modifyMethod;
	}
	public void setModifyMethod(String modifyMethod) {
		this.modifyMethod = modifyMethod;
	}
	public String getViewMethod() {
		return viewMethod;
	}
	public void setViewMethod(String viewMethod) {
		this.viewMethod = viewMethod;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public SwcExternalFormParameter[] getSwcExternalFormParameters() {
		return swcExternalFormParameters;
	}
	public void setSwcExternalFormParameters(SwcExternalFormParameter[] swcExternalFormParameters) {
		this.swcExternalFormParameters = swcExternalFormParameters;
	}

}