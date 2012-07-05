package net.smartworks.server.engine.process.approval.model;

import net.smartworks.server.engine.common.model.BaseObject;
import net.smartworks.server.engine.common.model.MisObject;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.common.util.XmlUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class AprApproval extends MisObject {
	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(AprApproval.class);

	protected static final String PREFIX = "Apr";
	private static final String NAME = CommonUtil.toName(AprApproval.class, PREFIX);

	public static final String A_TYPE = "type";
	public static final String A_APPROVER = "approver";
	public static final String A_DUEDATE = "dueDate";
	public static final String A_ISMANDATORY = "isMandatory";
	public static final String A_ISMODIFIABLE = "isModifiable";
	
	public static final String APPROVAL_STATUS_COMPLETE = "21";
	public static final String APPROVAL_STATUS_REJECT = "23";
	public static final String APPROVAL_STATUS_RUNNING = "11";

	private String type;
	private String approver;
	private String dueDate;
	private boolean isMandatory = true;
	private boolean isModifiable = true;
	private AprApprovalLine approvalLine;

	public AprApproval() {
		super();
	}
	public String toString(String name, String tab){
		if(name == null || name.trim().length() == 0)
			name = NAME;
		return super.toString(name, tab);
	}
	public String toAttributesString() {
		StringBuffer buf = new StringBuffer();
		buf.append(super.toAttributesString());
		appendAttributeString(A_TYPE, type, buf);
		appendAttributeString(A_APPROVER, approver, buf);
		appendAttributeString(A_DUEDATE, dueDate, buf);
		appendAttributeString(A_ISMANDATORY, isMandatory, buf);
		appendAttributeString(A_ISMODIFIABLE, isModifiable, buf);
		return buf.toString();
	}
	public static BaseObject toObject(Node node, BaseObject baseObj) throws Exception {
		if (node == null)
			return null;
		
		AprApproval obj = null;
		if (baseObj == null || !(baseObj instanceof AprApproval))
			obj = new AprApproval();
		else
			obj = (AprApproval)baseObj;
		
		MisObject.toObject(node, obj);
		
		NamedNodeMap attrMap = node.getAttributes();
		if (attrMap != null) {
			Node type = attrMap.getNamedItem(A_TYPE);
			Node approver = attrMap.getNamedItem(A_APPROVER);
			Node dueDate = attrMap.getNamedItem(A_DUEDATE);
			Node isMandatory = attrMap.getNamedItem(A_ISMANDATORY);
			Node isModifiable = attrMap.getNamedItem(A_ISMODIFIABLE);
			if (type != null)
				obj.setType(type.getNodeValue());
			if (approver != null)
				obj.setApprover(approver.getNodeValue());
			if (dueDate != null)
				obj.setDueDate(dueDate.getNodeValue());
			if (isMandatory != null)
				obj.setMandatory(CommonUtil.toBoolean(isMandatory.getNodeValue()));
			if (isModifiable != null)
				obj.setModifiable(CommonUtil.toBoolean(isModifiable.getNodeValue()));
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
	public static AprApproval[] add(AprApproval[] objs, AprApproval obj) {
		if (obj == null)
			return objs;
		int size = 0;
		if (objs != null)
			size = objs.length;
		AprApproval[] newObjs = new AprApproval[size+1];
		int i;
		for (i=0; i<size; i++)
			newObjs[i] = objs[i];
		newObjs[i] = obj;
		return newObjs;
	}
	public static AprApproval[] remove(AprApproval[] objs, AprApproval obj) {
		if (obj == null)
			return objs;
		int size = 0;
		if (objs != null)
			size = objs.length;
		if (size == 0)
			return objs;
		AprApproval[] newObjs = new AprApproval[size-1];
		int i;
		int j = 0;
		for (i=0; i<size; i++) {
			if (objs[i].equals(obj))
				continue;
			newObjs[j++] = objs[i];
		}
		return newObjs;
	}
	public static AprApproval[] left(AprApproval[] objs, AprApproval obj) {
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
		AprApproval[] newObjs = new AprApproval[objs.length];
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
	public static AprApproval[] right(AprApproval[] objs, AprApproval obj) {
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
		AprApproval[] newObjs = new AprApproval[objs.length];
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getApprover() {
		return approver;
	}
	public void setApprover(String approver) {
		this.approver = approver;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public boolean isModifiable() {
		return isModifiable;
	}
	public void setModifiable(boolean isModifiable) {
		this.isModifiable = isModifiable;
	}
	public boolean isMandatory() {
		return isMandatory;
	}
	public void setMandatory(boolean isMandatory) {
		this.isMandatory = isMandatory;
	}
	public AprApprovalLine getApprovalLine() {
		return approvalLine;
	}
	public void setApprovalLine(AprApprovalLine approvalLine) {
		this.approvalLine = approvalLine;
	}
}
