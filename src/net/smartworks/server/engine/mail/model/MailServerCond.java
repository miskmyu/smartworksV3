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

public class MailServerCond extends MisObjectCond {

	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(MailServerCond.class);

	protected static final String PREFIX = "Mail";
	private static final String NAME = CommonUtil.toName(MailServerCond.class, PREFIX);

	public static final String A_FETCHSERVER = "fetchServer";
	public static final String A_FETCHSERVERPORT = "fetchServerPort";
	public static final String A_FETCHPROTOCOL = "fetchProtocol";
	public static final String A_FETCHSSL = "fetchSsl";
	public static final String A_SMTPSERVER = "smtpServer";
	public static final String A_SMTPSERVERPORT = "smtpServerPort";
	public static final String A_SMTPAUTHENTICATED = "smtpAuthenticated";
	public static final String A_SMTPSSL = "smtpSsl";

	private String fetchServer;
	private int fetchServerPort;
	private String fetchProtocol;
	private boolean fetchSsl;
	private String smtpServer;
	private int smtpServerPort;
	private boolean smtpAuthenticated;
	private boolean smtpSsl;

	public MailServerCond() {
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

		appendAttributeString(A_FETCHSERVER, fetchServer, buf);
		appendAttributeString(A_FETCHSERVERPORT, fetchServerPort, buf);
		appendAttributeString(A_FETCHPROTOCOL, fetchProtocol, buf);
		appendAttributeString(A_FETCHSSL, fetchSsl, buf);
		appendAttributeString(A_SMTPSERVER, smtpServer, buf);
		appendAttributeString(A_SMTPSERVERPORT, smtpServerPort, buf);
		appendAttributeString(A_SMTPAUTHENTICATED, smtpAuthenticated, buf);
		appendAttributeString(A_SMTPSSL, smtpSsl, buf);
			
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

		MailServerCond obj = null;
		if (baseObj == null || !(baseObj instanceof MailServerCond))
			obj = new MailServerCond();
		else
			obj = (MailServerCond)baseObj;
		MisObjectCond.toObject(node, obj);

		NamedNodeMap attrMap = node.getAttributes();
		if (attrMap != null) {
			
			Node fetchServer = attrMap.getNamedItem(A_FETCHSERVER);
			Node fetchServerPort = attrMap.getNamedItem(A_FETCHSERVERPORT);
			Node fetchProtocol = attrMap.getNamedItem(A_FETCHPROTOCOL);
			Node fetchSsl = attrMap.getNamedItem(A_FETCHSSL);
			Node smtpServer = attrMap.getNamedItem(A_SMTPSERVER);
			Node smtpServerPort = attrMap.getNamedItem(A_SMTPSERVERPORT);
			Node smtpAuthenticated = attrMap.getNamedItem(A_SMTPAUTHENTICATED);
			Node smtpSsl = attrMap.getNamedItem(A_SMTPSSL);

			if(fetchServer != null)
				obj.setFetchServer(fetchServer.getNodeValue());
			if(fetchServerPort != null)
				obj.setFetchServerPort(CommonUtil.toInt(fetchServerPort.getNodeValue()));
			if(fetchProtocol != null)
				obj.setFetchProtocol(fetchProtocol.getNodeValue());
			if (fetchSsl != null)
				obj.setFetchSsl(CommonUtil.toBoolean(fetchSsl.getNodeValue()));
			if(smtpServer != null)
				obj.setSmtpServer(smtpServer.getNodeValue());
			if(smtpServerPort != null)
				obj.setSmtpServerPort(CommonUtil.toInt(smtpServerPort.getNodeValue()));
			if (smtpAuthenticated != null)
				obj.setSmtpAuthenticated(CommonUtil.toBoolean(smtpAuthenticated.getNodeValue()));
			if (smtpSsl != null)
				obj.setSmtpSsl(CommonUtil.toBoolean(smtpSsl.getNodeValue()));
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

	public static MailServerCond[] add(MailServerCond[] objs, MailServerCond obj) {
		if (obj == null)
			return objs;
		int size = 0;
		if (objs != null)
			size = objs.length;
		MailServerCond[] newObjs = new MailServerCond[size+1];
		int i;
		for (i=0; i<size; i++)
			newObjs[i] = objs[i];
		return newObjs;
	}

	public static MailServerCond[] remove(MailServerCond[] objs, MailServerCond obj) {
		if (obj == null)
			return objs;
		int size = 0;
		if (objs != null)
			size = objs.length;
		if (size == 0)
			return objs;
		MailServerCond[] newObjs = new MailServerCond[size-1];
		int i;
		int j = 0;
		for (i=0; i<size; i++) {
			if (objs[i].equals(obj))
				continue;
			newObjs[j++] = objs[i];
		}
		return newObjs;
	}

	public static MailServerCond[] left(MailServerCond[] objs, MailServerCond obj) {
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
		MailServerCond[] newObjs = new MailServerCond[objs.length];
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

	public static MailServerCond[] right(MailServerCond[] objs, MailServerCond obj) {
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
		MailServerCond[] newObjs = new MailServerCond[objs.length];
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

	public String getFetchServer() {
		return fetchServer;
	}
	public void setFetchServer(String fetchServer) {
		this.fetchServer = fetchServer;
	}
	public int getFetchServerPort() {
		return fetchServerPort;
	}
	public void setFetchServerPort(int fetchServerPort) {
		this.fetchServerPort = fetchServerPort;
	}
	public String getFetchProtocol() {
		return fetchProtocol;
	}
	public void setFetchProtocol(String fetchProtocol) {
		this.fetchProtocol = fetchProtocol;
	}
	public boolean isFetchSsl() {
		return fetchSsl;
	}
	public void setFetchSsl(boolean fetchSsl) {
		this.fetchSsl = fetchSsl;
	}
	public String getSmtpServer() {
		return smtpServer;
	}
	public void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
	}
	public int getSmtpServerPort() {
		return smtpServerPort;
	}
	public void setSmtpServerPort(int smtpServerPort) {
		this.smtpServerPort = smtpServerPort;
	}
	public boolean isSmtpAuthenticated() {
		return smtpAuthenticated;
	}
	public void setSmtpAuthenticated(boolean smtpAuthenticated) {
		this.smtpAuthenticated = smtpAuthenticated;
	}
	public boolean isSmtpSsl() {
		return smtpSsl;
	}
	public void setSmtpSsl(boolean smtpSsl) {
		this.smtpSsl = smtpSsl;
	}

}