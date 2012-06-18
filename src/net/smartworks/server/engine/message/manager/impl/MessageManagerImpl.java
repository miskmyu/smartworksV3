package net.smartworks.server.engine.message.manager.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.smartworks.server.engine.common.manager.AbstractManager;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.message.exception.MessageException;
import net.smartworks.server.engine.message.manager.IMessageManager;
import net.smartworks.server.engine.message.model.Message;
import net.smartworks.server.engine.message.model.MessageCond;

import org.hibernate.Query;

public class MessageManagerImpl extends AbstractManager implements IMessageManager {

	@Override
	public Message getMessage(String user, String objId, String level)  throws MessageException {
		if (level == null)
			level = LEVEL_ALL;
		if (level.equals(LEVEL_ALL)) {
			try {
				Message obj = (Message)get(Message.class, objId);
				return obj;
			} catch (Exception e) {
				throw new MessageException(e);
			}
		} else {
			MessageCond cond = new MessageCond();
			cond.setObjId(objId);
			Message[] objs = this.getMessages(user, cond, level);
			if (CommonUtil.isEmpty(objs))
				return null;
			return objs[0];
		}
	}

	@Override
	public Message getMessage(String user, MessageCond cond, String level) throws MessageException {
		if (cond == null)
			return null;
		if (level == null)
			level = LEVEL_ALL;
		cond.setPageSize(2);
		Message[] objs = getMessages(user, cond, level);
		if (CommonUtil.isEmpty(objs))
			return null;
		if (objs.length > 1)
			throw new MessageException("More than 1 Message. ");
		return objs[0];
	}

	@Override
	public void setMessage(String user, Message obj, String level) throws MessageException {
		if (level == null)
			level = LEVEL_ALL;
		try {
			if (level.equals(LEVEL_ALL)) {
				fill(user, obj);
				set(obj);
			} else {
				StringBuffer buf = new StringBuffer();
				buf.append("update Message set ");
				buf.append(" content=:content, sendUser=:sendUser, targetUser=:targetUser, isChecked=:isChecked, checkedTime=:checkedTime, chatId=:chatId, chattersId=:chattersId, deleteUser=:deleteUser");
				buf.append(" where objId=:objId");
				Query query = this.getSession().createQuery(buf.toString());
				query.setString(Message.A_CONTENT, obj.getContent());
				query.setString(Message.A_SENDUSER, obj.getSendUser());
				query.setString(Message.A_TARGETUSER, obj.getTargetUser());
				query.setBoolean(Message.A_ISCHECKED, obj.isChecked());
				query.setTimestamp(Message.A_CHECKEDTIME, obj.getCheckedTime());
				query.setString(Message.A_CHATID, obj.getChatId());
				query.setString(Message.A_CHATTERSID, obj.getChattersId());
				query.setString(Message.A_DELETEUSER, obj.getDeleteUser());
				query.setString(Message.A_OBJID, obj.getObjId());
				query.executeUpdate();
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw new MessageException(e);
		}
	}

	@Override
	public void createMessage(String user, Message obj) throws MessageException {
		try{
			fill(user, obj);
			create(obj);
		}catch(Exception e){
			logger.error(e, e);
			throw new MessageException(e);
		}
	}

	@Override
	public void removeMessage(String user, String objId) throws MessageException {
		try {
			remove(Message.class, objId);
		} catch (Exception e) {
			throw new MessageException(e);
		}
	}
	
	
	@Override
	public void removeMessage(String user, MessageCond cond) throws MessageException {
		Message obj = getMessage(user, cond, null);
		if (obj == null)
			return;
		removeMessage(user, obj.getObjId());
		
	}
	private Query appendQuery(StringBuffer buf, MessageCond cond) throws Exception {
		
		String objId = null;
		String[] objIdIns = null;
		String content = null;
		String sendUser = null;
		String targetUser = null;
		String deleteUser = null;
		boolean isChecked = false;
		int readStatus = -1;
		String chatId = null;
		String chattersId = null;
		Date checkedTime = null;
		String creationUser = null;
		Date creationDate = null;
		String modificationUser = null;
		Date modificationDate = null;
		Date creationDateFrom = null;
		Date creationDateTo = null;

		if (cond != null) {
			objId = cond.getObjId();
			objIdIns = cond.getObjIdIns();
			content = cond.getContent();
			sendUser = cond.getSendUser();
			targetUser = cond.getTargetUser();
			deleteUser = cond.getDeleteUser();
			readStatus = cond.getReadStatus();
			chatId = cond.getChatId();
			chattersId = cond.getChattersId();
			checkedTime = cond.getCheckedTime();
			creationUser = cond.getCreationUser();
			creationDate = cond.getCreationDate();
			creationDateFrom = cond.getCreationDateFrom();
			creationDateTo = cond.getCreationDateTo();
			modificationUser = cond.getModificationUser();
			modificationDate = cond.getModificationDate();
		}
		buf.append(" from Message obj");
		buf.append(" where obj.objId is not null");

		if (cond != null) {
			if (objId != null) 
				buf.append(" and obj.objId = :objId");
			if (objIdIns != null && objIdIns.length != 0) {
				buf.append(" and obj.objId in (");
				for (int i=0; i<objIdIns.length; i++) {
					if (i != 0)
						buf.append(", ");
					buf.append(":objIdIn").append(i);
				}
				buf.append(")");
			}
			if (content != null)
				buf.append(" and obj.content = :content");
			if (sendUser != null) 
				buf.append(" and obj.sendUser = :sendUser");
			if (targetUser != null) 
				buf.append(" and obj.targetUser = :targetUser");
			if (deleteUser != null) 
				buf.append(" and obj.deleteUser = :deleteUser");
			if (readStatus != -1)
				buf.append(" and obj.isChecked = :isChecked");
			if (chatId != null)
				buf.append(" and obj.chatId = :chatId");
			if (chattersId != null)
				buf.append(" and obj.chattersId = :chattersId");
			if (checkedTime != null)
				buf.append(" and obj.checkedTime = :checkedTime");
			if (creationUser != null) 
				buf.append(" and obj.creationUser = :creationUser");
			if (creationDate != null) 
				buf.append(" and obj.creationDate = :creationDate");
			if (modificationUser != null)
				buf.append(" and obj.modificationUser = :modificationUser");
			if (modificationDate != null)
				buf.append(" and obj.modificationDate = :modificationDate");
			if (creationDateFrom != null)
				buf.append(" and obj.creationDate > :creationDateFrom ");
			if (creationDateTo != null)
				buf.append(" and obj.creationDate < :creationDateTo ");
		}
		this.appendOrderQuery(buf, "obj", cond);
		
		Query query = this.createQuery(buf.toString(), cond);
		if (cond != null) {
			if (objId != null)
				query.setString("objId", objId);
			if (objIdIns != null && objIdIns.length != 0) {
				for (int i=0; i<objIdIns.length; i++) {
					query.setString("objIdIn"+i, objIdIns[i]);
				}
			}
			if (content != null)
				query.setString("content", content);
			if (sendUser != null)
				query.setString("sendUser", sendUser);
			if (targetUser != null)
				query.setString("targetUser", targetUser);
			if (deleteUser != null)
				query.setString("deleteUser", deleteUser);
			if (readStatus != -1) {
				if(readStatus == 1) isChecked = true;
				query.setBoolean("isChecked", isChecked);
			}
			if (chatId != null)
				query.setString("chatId", chatId);
			if (chattersId != null)
				query.setString("chattersId", chattersId);
			if (checkedTime != null)
				query.setTimestamp("checkedTime", checkedTime);
			if (creationDate != null)
				query.setTimestamp("creationDate", creationDate);
			if (creationUser != null)
				query.setString("creationUser", creationUser);
			if (modificationUser != null)
				query.setString("modificationUser", modificationUser);			
			if (modificationDate != null)
				query.setDate("modificationDate", modificationDate);
			if (creationDateFrom != null)
				query.setTimestamp("creationDateFrom", creationDateFrom);
			if (creationDateTo != null)
				query.setTimestamp("creationDateTo", creationDateTo);
		}
		return query;
	}
	
	@Override
	public long getMessageSize(String user, MessageCond cond) throws MessageException{
		try {
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			buf.append(" count(obj)");
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			long count = ((Long)list.get(0)).longValue();
			return count;
		} catch (Exception e) {
			logger.error(e, e);
			throw new MessageException(e);
		}
	}

	@Override
	public Message[] getMessages(String user, MessageCond cond, String level) throws MessageException {
		
		try {
			if (level == null)
				level = LEVEL_LITE;
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			if (level.equals(LEVEL_ALL)) {
				buf.append(" obj");
			} else {
				buf.append(" obj.objId,");
				buf.append(" obj.content, obj.sendUser, obj.targetUser, obj.isChecked, obj.chatId, obj.chattersId, obj.checkedTime,");
				buf.append(" obj.creationUser, obj.creationDate, obj.modificationUser, obj.modificationDate, obj.deleteUser");
			}
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			if (list == null || list.isEmpty())
				return null;
			if (!level.equals(LEVEL_ALL)) {
				List objList = new ArrayList();
				for (Iterator itr = list.iterator(); itr.hasNext();) {
					Object[] fields = (Object[])itr.next();
					Message obj = new Message();
					int j = 0;
					obj.setObjId((String)fields[j++]);
					obj.setContent((String)fields[j++]);
					obj.setSendUser((String)fields[j++]);
					obj.setTargetUser((String)fields[j++]);
					obj.setChecked(CommonUtil.toBoolean(fields[j++]));
					obj.setChatId((String)fields[j++]);
					obj.setChattersId((String)fields[j++]);
					obj.setCheckedTime((Timestamp)fields[j++]);
					obj.setCreationUser((String)fields[j++]);
					obj.setCreationDate((Timestamp)fields[j++]);
					obj.setModificationUser((String)fields[j++]);
					obj.setModificationDate((Timestamp)fields[j++]);
					obj.setDeleteUser((String)fields[j++]);
					objList.add(obj);
				}
				list = objList;
			}
			Message[] objs = new Message[list.size()];
			list.toArray(objs);
			return objs;
		} catch (Exception e) {
			logger.error(e, e);
			throw new MessageException(e);
		}
	}

}