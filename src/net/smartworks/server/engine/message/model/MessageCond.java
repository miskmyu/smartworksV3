package net.smartworks.server.engine.message.model;

import java.util.Date;

import net.smartworks.server.engine.common.model.BaseObject;
import net.smartworks.server.engine.common.model.MisObject;
import net.smartworks.server.engine.common.model.MisObjectCond;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.common.util.DateUtil;
import net.smartworks.server.engine.common.util.XmlUtil;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class MessageCond extends MisObjectCond {

	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(MessageCond.class);

	protected static final String PREFIX = "";
	private static final String NAME = CommonUtil.toName(MessageCond.class, PREFIX);
	
	public static final String A_CONTENT = "content";
	public static final String A_TARGETUSER = "targetUser";
	public static final String A_SENDER = "sender";
	public static final String A_ISCHECKED = "isChecked";	
	public static final String A_CHECKEDTIME = "checkedTime";	
	
	private String content;
	private String targetUser;
	private String sender;
	private boolean isChecked = false;
	private Date checkedTime;
	
	
	public MessageCond(){
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
		
		appendAttributeString(A_CONTENT, content, buf);
		appendAttributeString(A_TARGETUSER, targetUser, buf);
		appendAttributeString(A_SENDER, sender, buf);
		appendAttributeString(A_ISCHECKED, isChecked, buf);
		appendAttributeString(A_CHECKEDTIME, checkedTime, buf);
				
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

		MessageCond obj = null;
		if (baseObj == null || !(baseObj instanceof MessageCond))
			obj = new MessageCond();
		else
			obj = (MessageCond)baseObj;
		MisObject.toObject(node, obj);

		NamedNodeMap attrMap = node.getAttributes();
		if (attrMap != null) {
			
			Node content = attrMap.getNamedItem(A_CONTENT);
			Node targetUser = attrMap.getNamedItem(A_TARGETUSER);
			Node sender = attrMap.getNamedItem(A_SENDER);
			Node isChecked = attrMap.getNamedItem(A_ISCHECKED);
			Node checkedTime = attrMap.getNamedItem(A_CHECKEDTIME);

			if(content != null)
				obj.setContent(content.getNodeValue());
			if(targetUser != null)
				obj.setTargetUser(targetUser.getNodeValue());
			if(sender != null)
				obj.setSender(sender.getNodeValue());
			if (isChecked != null)
				obj.setChecked(CommonUtil.toBoolean(isChecked.getNodeValue()));
			if (checkedTime != null)
				obj.setCheckedTime(DateUtil.toDate(checkedTime.getNodeValue()));
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
	
	public static MessageCond[] add(MessageCond[] objs, MessageCond obj) {
		if (obj == null)
			return objs;
		int size = 0;
		if (objs != null)
			size = objs.length;
		MessageCond[] newObjs = new MessageCond[size+1];
		int i;
		for (i=0; i<size; i++)
			newObjs[i] = objs[i];
		return newObjs;
	}
	
	public static MessageCond[] remove(MessageCond[] objs, MessageCond obj) {
		if (obj == null)
			return objs;
		int size = 0;
		if (objs != null)
			size = objs.length;
		if (size == 0)
			return objs;
		MessageCond[] newObjs = new MessageCond[size-1];
		int i;
		int j = 0;
		for (i=0; i<size; i++) {
			if (objs[i].equals(obj))
				continue;
			newObjs[j++] = objs[i];
		}
		return newObjs;
	}
	
	public static MessageCond[] left(MessageCond[] objs, MessageCond obj) {
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
		MessageCond[] newObjs = new MessageCond[objs.length];
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
	
	public static MessageCond[] right(MessageCond[] objs, MessageCond obj) {
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
		MessageCond[] newObjs = new MessageCond[objs.length];
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
		
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTargetUser() {
		return targetUser;
	}

	public void setTargetUser(String targetUser) {
		this.targetUser = targetUser;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public Date getCheckedTime() {
		return checkedTime;
	}

	public void setCheckedTime(Date checkedTime) {
		this.checkedTime = checkedTime;
	}

}
