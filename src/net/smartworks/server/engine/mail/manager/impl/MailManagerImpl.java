/*	
 * $Id$
 * created by    : hsshin
 * creation-date : 2012. 6. 12.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.mail.manager.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.smartworks.server.engine.common.manager.AbstractManager;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.mail.exception.MailException;
import net.smartworks.server.engine.mail.manager.IMailManager;
import net.smartworks.server.engine.mail.model.MailContent;
import net.smartworks.server.engine.mail.model.MailContentCond;

import org.hibernate.Query;

public class MailManagerImpl extends AbstractManager implements IMailManager {

	public MailManagerImpl() {
		super();
		if (logger.isInfoEnabled())
			logger.info(this.getClass().getName() + " created");
	}

	@Override
	public MailContent getMailContent(String user, String objId, String level) throws MailException {
		if (level == null)
			level = LEVEL_ALL;
		if (level.equals(LEVEL_ALL)) {
			try {
				MailContent obj = (MailContent)get(MailContent.class, objId);
				return obj;
			} catch (Exception e) {
				throw new MailException(e);
			}
		} else {
			MailContentCond cond = new MailContentCond();
			cond.setId(Long.parseLong(objId));
			MailContent[] objs = this.getMailContents(user, cond, level);
			if (CommonUtil.isEmpty(objs))
				return null;
			return objs[0];
		}
	}

	@Override
	public MailContent getMailContent(String user, MailContentCond cond, String level) throws MailException {
		if (cond == null)
			return null;
		if (level == null)
			level = LEVEL_ALL;
		cond.setPageSize(2);
		MailContent[] mailContents = getMailContents(user, cond, level);
		if (CommonUtil.isEmpty(mailContents))
			return null;
		if (mailContents.length > 1)
			throw new MailException("More than 1 mail. ");
		return mailContents[0];
	}

	@Override
	public void setMailContent(String user, MailContent obj, String level) throws MailException {
		if (level == null)
			level = LEVEL_ALL;
		try {
			if (level.equals(LEVEL_ALL)) {
				fill(user, obj);
				set(obj);
			} else {
				StringBuffer buf = new StringBuffer();
				buf.append("update MailContent set ");
				buf.append(" folderId=:folderId ");
				buf.append(" where id=:id");
				Query query = this.getSession().createQuery(buf.toString());
				query.setLong(MailContent.A_FOLDERID, obj.getFolderId());
				query.setLong(MailContent.A_ID, obj.getId());
				query.executeUpdate();
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw new MailException(e);
		}
	}

	@Override
	public void createMailContent(String user, MailContent obj) throws MailException {
		try {
			fill(user, obj);
			create(obj);
		} catch (Exception e) {
			logger.error(e, e);
			throw new MailException(e);
		}
	}

	@Override
	public void removeMailContent(String user, String objId) throws MailException {
		try {
			remove(MailContent.class, objId);
		} catch (Exception e) {
			throw new MailException(e);
		}
	}

	@Override
	public void removeMailContent(String user, MailContentCond cond) throws MailException {
		MailContent obj = getMailContent(user, cond, null);
		if (obj == null)
			return;
		removeMailContent(user, obj.getObjId());
	}

	private Query appendQuery(StringBuffer buf, MailContentCond cond) throws Exception {
		String username = null; 
		long folderId = -1;
		String searchKey = null;
		int unread = -1;

		if (cond != null) {
			username = cond.getUsername();
			folderId = cond.getFolderId();
			searchKey = cond.getSearchKey();
			unread = cond.getUnread();
		}
		buf.append(" from MailContent obj");
		buf.append(" where obj.id is not null");
		//TODO 시간 검색에 대한 확인 필요
		if(cond != null) {
			if(username != null) 
				buf.append(" and obj.username = :username");
			if(folderId != -1) 
				buf.append(" and obj.folderId = :folderId");
			if(searchKey != null)
				buf.append(" and (obj.sender like :searchKey or obj.subject like :searchKey)");
			if(unread != -1) 
				buf.append(" and obj.unread = :unread");
		}
		this.appendOrderQuery(buf, "obj", cond);

		Query query = this.createQuery(buf.toString(), cond);

		if(cond != null) {
			if(username != null)
				query.setString("username", username);
			if(folderId != -1)
				query.setLong("folderId", folderId);
			if(searchKey != null)
				query.setString("searchKey", searchKey);
			if(unread != -1)
				query.setInteger("unread", unread);
		}
		return query;

	}

	@Override
	public long getMailContentSize(String user, MailContentCond cond) throws MailException {
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
			throw new MailException(e);
		}
	}

	@Override
	public MailContent[] getMailContents(String user, MailContentCond cond, String level) throws MailException {
		try {
			if (level == null)
				level = LEVEL_LITE;
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			if (level.equals(LEVEL_ALL)) {
				buf.append(" obj");
			} else {
				buf.append(" obj.id, obj.username, obj.folderId, obj.uniqueId,");
				buf.append(" obj.sender, obj.receiver, obj.cc, obj.bcc,");
				buf.append(" obj.replyTo, obj.subject, obj.multipart, obj.priority,");
				buf.append(" obj.sentDate, obj.unread, obj.msgSize ");
			}
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			if (list == null || list.isEmpty())
				return null;
			if (!level.equals(LEVEL_ALL)) {
				List objList = new ArrayList();
				for (Iterator itr = list.iterator(); itr.hasNext();) {
					Object[] fields = (Object[]) itr.next();
					MailContent obj = new MailContent();
					int j = 0;
					obj.setId((Long)fields[j++]);
					obj.setUsername((String)fields[j++]);
					obj.setFolderId((Long)fields[j++]);
					obj.setUniqueId((String)fields[j++]);
					obj.setSender((String)fields[j++]);
					obj.setReceiver((String)fields[j++]);
					obj.setCc((String)fields[j++]);
					obj.setBcc((String)fields[j++]);
					obj.setReplyTo((String)fields[j++]);
					obj.setSubject((String)fields[j++]);
					obj.setMultipart((Integer)fields[j++]);
					obj.setPriority((Integer)fields[j++]);
					obj.setSentDate((Timestamp)fields[j++]);
					obj.setUnread((Integer)fields[j++]);
					obj.setMsgSize((Long)fields[j++]);
					objList.add(obj);
				}
				list = objList;
			}
			MailContent[] objs = new MailContent[list.size()];
			list.toArray(objs);
			return objs;
		} catch (Exception e) {
			logger.error(e, e);
			throw new MailException(e);
		}
	}

}