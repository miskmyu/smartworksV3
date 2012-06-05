package net.smartworks.server.engine.message.model;

import java.util.Date;

import net.smartworks.server.engine.common.model.BaseObject;
import net.smartworks.server.engine.common.model.MisObject;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.common.util.DateUtil;
import net.smartworks.server.engine.common.util.XmlUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class Message extends MisObject {

	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(Message.class);

	protected static final String PREFIX = "";
	private static final String NAME = CommonUtil.toName(Message.class, PREFIX);
	
	public static final String A_CONTENT = "content";
	public static final String A_SENDUSER = "sendUser";
	public static final String A_TARGETUSER = "targetUser";
	public static final String A_ISCHECKED = "isChecked";			
	public static final String A_CHECKEDTIME = "checkedTime";
	public static final String A_CHATID = "chatId";
	public static final String A_CHATTERSID = "chattersId";
	public static final String A_DELETEUSER = "deleteUser";
	
	private String content;
	private String sendUser;
	private String targetUser;
	private boolean isChecked = false;
	private Date checkedTime;
	private String chatId;
	private String chattersId;
	private String deleteUser;

	public Message(){
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
		appendAttributeString(A_SENDUSER, sendUser, buf);
		appendAttributeString(A_TARGETUSER, targetUser, buf);
		appendAttributeString(A_ISCHECKED, isChecked, buf);
		appendAttributeString(A_CHECKEDTIME, checkedTime, buf);
		appendAttributeString(A_CHATID, chatId, buf);
		appendAttributeString(A_CHATTERSID, chattersId, buf);
		appendAttributeString(A_DELETEUSER, deleteUser, buf);
			
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

		Message obj = null;
		if (baseObj == null || !(baseObj instanceof Message))
			obj = new Message();
		else
			obj = (Message)baseObj;
		MisObject.toObject(node, obj);

		NamedNodeMap attrMap = node.getAttributes();
		if (attrMap != null) {
			
			Node content = attrMap.getNamedItem(A_CONTENT);
			Node targetUser = attrMap.getNamedItem(A_TARGETUSER);
			Node sendUser = attrMap.getNamedItem(A_SENDUSER);
			Node isChecked = attrMap.getNamedItem(A_ISCHECKED);
			Node checkedTime = attrMap.getNamedItem(A_CHECKEDTIME);
			Node chatId = attrMap.getNamedItem(A_CHATID);
			Node chattersId = attrMap.getNamedItem(A_CHATTERSID);
			Node deleteUser = attrMap.getNamedItem(A_DELETEUSER);

			if(content != null)
				obj.setContent(content.getNodeValue());
			if(targetUser != null)
				obj.setTargetUser(targetUser.getNodeValue());
			if(sendUser != null)
				obj.setSendUser(sendUser.getNodeValue());
			if (isChecked != null)
				obj.setChecked(CommonUtil.toBoolean(isChecked.getNodeValue()));
			if (checkedTime != null)
				obj.setCheckedTime(DateUtil.toDate(checkedTime.getNodeValue()));
			if(chatId != null)
				obj.setChatId(chatId.getNodeValue());
			if(chattersId != null)
				obj.setChattersId(chattersId.getNodeValue());
			if(deleteUser != null)
				obj.setDeleteUser(deleteUser.getNodeValue());
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
	
	public static Message[] add(Message[] objs, Message obj) {
		if (obj == null)
			return objs;
		int size = 0;
		if (objs != null)
			size = objs.length;
		Message[] newObjs = new Message[size+1];
		int i;
		for (i=0; i<size; i++)
			newObjs[i] = objs[i];
		return newObjs;
	}
	
	public static Message[] remove(Message[] objs, Message obj) {
		if (obj == null)
			return objs;
		int size = 0;
		if (objs != null)
			size = objs.length;
		if (size == 0)
			return objs;
		Message[] newObjs = new Message[size-1];
		int i;
		int j = 0;
		for (i=0; i<size; i++) {
			if (objs[i].equals(obj))
				continue;
			newObjs[j++] = objs[i];
		}
		return newObjs;
	}
	
	public static Message[] left(Message[] objs, Message obj) {
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
		Message[] newObjs = new Message[objs.length];
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
	
	public static Message[] right(Message[] objs, Message obj) {
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
		Message[] newObjs = new Message[objs.length];
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
	public String getSendUser() {
		return sendUser;
	}
	public void setSendUser(String sendUser) {
		this.sendUser = sendUser;
	}
	public boolean isChecked() {
		return isChecked;
	}
	public boolean getIsChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	public void setIsChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	public Date getCheckedTime() {
		return checkedTime;
	}
	public void setCheckedTime(Date checkedTime) {
		this.checkedTime = checkedTime;
	}
	public String getChatId() {
		return chatId;
	}
	public void setChatId(String chatId) {
		this.chatId = chatId;
	}
	public String getChattersId() {
		return chattersId;
	}
	public void setChattersId(String chattersId) {
		this.chattersId = chattersId;
	}
	public String getDeleteUser() {
		return deleteUser;
	}
	public void setDeleteUser(String deleteUser) {
		this.deleteUser = deleteUser;
	}

}