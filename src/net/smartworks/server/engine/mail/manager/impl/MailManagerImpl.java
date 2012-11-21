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
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.smartworks.server.engine.common.manager.AbstractManager;
import net.smartworks.server.engine.common.model.Order;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.mail.exception.MailException;
import net.smartworks.server.engine.mail.manager.IMailManager;
import net.smartworks.server.engine.mail.model.MailAccount;
import net.smartworks.server.engine.mail.model.MailAccountCond;
import net.smartworks.server.engine.mail.model.MailContent;
import net.smartworks.server.engine.mail.model.MailContentCond;
import net.smartworks.server.engine.mail.model.MailServer;
import net.smartworks.server.engine.mail.model.MailServerCond;

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
				buf.append(" and (obj.sender like :searchKey or obj.subject like :searchKey or obj.receiver like :searchKey)");
			if(unread != -1) 
				buf.append(" and obj.unread = :unread");
		}
		Order[] orders = cond.getOrders();
		if (orders != null && orders.length != 0) {
			for (int i = 0; i < orders.length; i++) {
				Order order = orders[i];
				String fieldId = order.getField();
				if (fieldId.equalsIgnoreCase("modifiedTime")) {
					order.setField("sentDate");
					orders[i] = order;
				}
			}
		}
		
		this.appendOrderQuery(buf, "obj", cond);

		Query query = this.createQuery(buf.toString(), cond);

		if(cond != null) {
			if(username != null)
				query.setString("username", username);
			if(folderId != -1)
				query.setLong("folderId", folderId);
			if(searchKey != null)
				query.setString("searchKey", CommonUtil.toLikeString(searchKey));
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

	@Override
	public long getPrevMailId(String user, MailContentCond cond) throws MailException {
		try {
			long id = -1;
			long folderId = -1;
			if(cond != null) {
				id = cond.getId();
				folderId = cond.getFolderId();
			}
			StringBuffer buf = new StringBuffer();
			buf.append("select max(b.id) as prevMsgId from MailContent a, MailContent b where a.id > b.id and a.id = :id and b.folderId = :folderId");

			Query query = this.getSession().createQuery(buf.toString());

			if(id != -1)
				query.setLong("id", id);
			if(folderId != -1)
				query.setLong("folderId", folderId);

			long result = 0;
			Object object = query.uniqueResult();
			if(!CommonUtil.isEmpty(object))
				result = Long.parseLong(String.valueOf(object));

			return result;
		} catch (Exception e) {
			logger.error(e, e);
			throw new MailException(e);
		}
	}

	@Override
	public long getNextMailId(String user, MailContentCond cond) throws MailException {
		try {
			long id = -1;
			long folderId = -1;
			if(cond != null) {
				id = cond.getId();
				folderId = cond.getFolderId();
			}
			StringBuffer buf = new StringBuffer();
			buf.append("select min(b.id) as nextMsgId from MailContent a, MailContent b where a.id < b.id and a.id = :id and b.folderId = :folderId");

			Query query = this.getSession().createQuery(buf.toString());

			if(id != -1)
				query.setLong("id", id);
			if(folderId != -1)
				query.setLong("folderId", folderId);

			long result = 0;
			Object object = query.uniqueResult();
			if(!CommonUtil.isEmpty(object))
				result = Long.parseLong(String.valueOf(object));

			return result;
		} catch (Exception e) {
			logger.error(e, e);
			throw new MailException(e);
		}
	}

	@Override
	public MailServer getMailServer(String user, String objId, String level) throws MailException {
		if (level == null)
			level = LEVEL_ALL;
		if (level.equals(LEVEL_ALL)) {
			try {
				MailServer obj = (MailServer)get(MailServer.class, objId);
				return obj;
			} catch (Exception e) {
				throw new MailException(e);
			}
		} else {
			MailServerCond cond = new MailServerCond();
			cond.setObjId(objId);
			MailServer[] objs = this.getMailServers(user, cond, level);
			if (CommonUtil.isEmpty(objs))
				return null;
			return objs[0];
		}
	}

	@Override
	public MailServer getMailServer(String user, MailServerCond cond, String level) throws MailException {
		if (cond == null)
			return null;
		if (level == null)
			level = LEVEL_ALL;
		cond.setPageSize(2);
		MailServer[] objs = getMailServers(user, cond, level);
		if (CommonUtil.isEmpty(objs))
			return null;
		if (objs.length > 1)
			throw new MailException("More than 1 MailServer. ");
		return objs[0];
	}

	@Override
	public void setMailServer(String user, MailServer obj, String level) throws MailException {
		if (level == null)
			level = LEVEL_ALL;
		try {
			if (level.equals(LEVEL_ALL)) {
				fill(user, obj);
				set(obj);
			} else {
				StringBuffer buf = new StringBuffer();
				buf.append("update MailServer set ");
				buf.append(" name=:name, fetchServer=:fetchServer, fetchServerPort=:fetchServerPort, fetchProtocol=:fetchProtocol, fetchSsl=:fetchSsl,");
				buf.append(" smtpServer=:smtpServer, smtpServerPort=:smtpServerPort, smtpAuthenticated=:smtpAuthenticated, smtpSsl=:smtpSsl");
				buf.append(" where objId=:objId");
				Query query = this.getSession().createQuery(buf.toString());
				query.setString(MailServer.A_NAME, obj.getName());
				query.setString(MailServer.A_FETCHSERVER, obj.getFetchServer());
				query.setInteger(MailServer.A_FETCHSERVERPORT, obj.getFetchServerPort());
				query.setString(MailServer.A_FETCHPROTOCOL, obj.getFetchProtocol());
				query.setBoolean(MailServer.A_FETCHSSL, obj.isFetchSsl());
				query.setString(MailServer.A_SMTPSERVER, obj.getSmtpServer());
				query.setInteger(MailServer.A_SMTPSERVERPORT, obj.getSmtpServerPort());
				query.setBoolean(MailServer.A_SMTPAUTHENTICATED, obj.isSmtpAuthenticated());
				query.setBoolean(MailServer.A_SMTPSSL, obj.isSmtpSsl());
				query.setString(MailServer.A_OBJID, obj.getObjId());
				query.executeUpdate();
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw new MailException(e);
		}
	}

	@Override
	public void createMailServer(String user, MailServer obj) throws MailException {
		try{
			fill(user, obj);
			create(obj);
		}catch(Exception e){
			logger.error(e, e);
			throw new MailException(e);
		}
	}

	@Override
	public void removeMailServer(String user, String objId) throws MailException {
		try {
			remove(MailServer.class, objId);
		} catch (Exception e) {
			throw new MailException(e);
		}
	}

	@Override
	public void removeMailServer(String user, MailServerCond cond) throws MailException {
		MailServer obj = getMailServer(user, cond, null);
		if (obj == null)
			return;
		removeMailServer(user, obj.getObjId());
	}

	private Query appendQuery(StringBuffer buf, MailServerCond cond) throws Exception {

		String objId = null;
		String creationUser = null;
		Date creationDate = null;
		String modificationUser = null;
		Date modificationDate = null;
		Date creationDateFrom = null;
		Date creationDateTo = null;

		if (cond != null) {
			objId = cond.getObjId();
			creationUser = cond.getCreationUser();
			creationDate = cond.getCreationDate();
			creationDateFrom = cond.getCreationDateFrom();
			creationDateTo = cond.getCreationDateTo();
			modificationUser = cond.getModificationUser();
			modificationDate = cond.getModificationDate();
		}
		buf.append(" from MailServer obj");
		buf.append(" where obj.objId is not null");

		if (cond != null) {
			if (objId != null) 
				buf.append(" and obj.objId = :objId");
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
	public long getMailServerSize(String user, MailServerCond cond) throws MailException {
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
	public MailServer[] getMailServers(String user, MailServerCond cond, String level) throws MailException {
		try {
			if (level == null)
				level = LEVEL_LITE;
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			if (level.equals(LEVEL_ALL)) {
				buf.append(" obj");
			} else {
				buf.append(" obj.objId,");
				buf.append(" obj.name, obj.fetchServer, obj.fetchServerPort, obj.fetchProtocol, obj.fetchSsl, obj.smtpServer, obj.smtpServerPort,");
				buf.append(" obj.smtpAuthenticated, obj.smtpSsl, obj.creationUser, obj.creationDate, obj.modificationUser, obj.modificationDate");
			}
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			if (list == null || list.isEmpty())
				return null;
			if (!level.equals(LEVEL_ALL)) {
				List objList = new ArrayList();
				for (Iterator itr = list.iterator(); itr.hasNext();) {
					Object[] fields = (Object[])itr.next();
					MailServer obj = new MailServer();
					int j = 0;
					obj.setObjId((String)fields[j++]);
					obj.setName((String)fields[j++]);
					obj.setFetchServer((String)fields[j++]);
					obj.setFetchServerPort(CommonUtil.toInt(fields[j++]));
					obj.setFetchProtocol((String)fields[j++]);
					obj.setFetchSsl(CommonUtil.toBoolean(fields[j++]));
					obj.setSmtpServer((String)fields[j++]);
					obj.setSmtpServerPort(CommonUtil.toInt(fields[j++]));
					obj.setSmtpAuthenticated(CommonUtil.toBoolean(fields[j++]));
					obj.setSmtpSsl(CommonUtil.toBoolean(fields[j++]));
					obj.setCreationUser((String)fields[j++]);
					obj.setCreationDate((Timestamp)fields[j++]);
					obj.setModificationUser((String)fields[j++]);
					obj.setModificationDate((Timestamp)fields[j++]);
					objList.add(obj);
				}
				list = objList;
			}
			MailServer[] objs = new MailServer[list.size()];
			list.toArray(objs);
			return objs;
		} catch (Exception e) {
			logger.error(e, e);
			throw new MailException(e);
		}
	}

	@Override
	public MailAccount getMailAccount(String user, String objId, String level) throws MailException {
		if (level == null)
			level = LEVEL_ALL;
		if (level.equals(LEVEL_ALL)) {
			try {
				MailAccount obj = (MailAccount)get(MailAccount.class, objId);
				return obj;
			} catch (Exception e) {
				throw new MailException(e);
			}
		} else {
			MailAccountCond cond = new MailAccountCond();
			cond.setObjId(objId);
			MailAccount[] objs = this.getMailAccounts(user, cond, level);
			if (CommonUtil.isEmpty(objs))
				return null;
			return objs[0];
		}
	}

	@Override
	public MailAccount getMailAccount(String user, MailAccountCond cond, String level) throws MailException {
		if (cond == null)
			return null;
		if (level == null)
			level = LEVEL_ALL;
		cond.setPageSize(2);
		MailAccount[] objs = getMailAccounts(user, cond, level);
		if (CommonUtil.isEmpty(objs))
			return null;
		if (objs.length > 1)
			throw new MailException("More than 1 MailAccount. ");
		return objs[0];
	}

	@Override
	public void setMailAccount(String user, MailAccount obj, String level) throws MailException {
		if (level == null)
			level = LEVEL_ALL;
		try {
			if (level.equals(LEVEL_ALL)) {
				fill(user, obj);
				set(obj);
			} else {
				StringBuffer buf = new StringBuffer();
				buf.append("update MailAccount set ");
				buf.append(" userId=:userId, mailServerId=:mailServerId, mailServerName=:mailServerName, mailId=:mailId,mailUserName=:mailUserName,mailDeleteFetched=:mailDeleteFetched, mailPassword=:mailPassword, mailSignature=:mailSignature, useMailSign=:useMailSign, senderUserTitle=:senderUserTitle ");
				buf.append(" where objId=:objId");
				Query query = this.getSession().createQuery(buf.toString());
				query.setString(MailAccount.A_USERID, obj.getUserId());
				query.setString(MailAccount.A_MAILSERVERID, obj.getMailServerId());
				query.setString(MailAccount.A_MAILSERVERNAME, obj.getMailServerName());
				query.setString(MailAccount.A_MAILID, obj.getMailId());
				query.setString(MailAccount.A_MAILUSERNAME, obj.getMailUserName());
				query.setString(MailAccount.A_MAILDELETEFETCHED, obj.getMailDeleteFetched());
				query.setString(MailAccount.A_MAILPASSWORD, obj.getMailPassword());
				query.setString(MailAccount.A_MAILSIGNATURE, obj.getMailSignature());
				query.setBoolean(MailAccount.A_USEMAILSIGN, obj.isUseMailSign());
				query.setString(MailAccount.A_SENDERUSERTITLE, obj.getSenderUserTitle());
				query.setString(MailAccount.A_OBJID, obj.getObjId());
				query.executeUpdate();
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw new MailException(e);
		}
	}

	@Override
	public void createMailAccount(String user, MailAccount obj) throws MailException {
		try{
			fill(user, obj);
			create(obj);
		}catch(Exception e){
			logger.error(e, e);
			throw new MailException(e);
		}
	}

	@Override
	public void removeMailAccount(String user, String objId) throws MailException {
		try {
			remove(MailAccount.class, objId);
		} catch (Exception e) {
			throw new MailException(e);
		}
	}

	@Override
	public void removeMailAccount(String user, MailAccountCond cond) throws MailException {
		MailAccount obj = getMailAccount(user, cond, null);
		if (obj == null)
			return;
		removeMailAccount(user, obj.getObjId());
	}

	private Query appendQuery(StringBuffer buf, MailAccountCond cond) throws Exception {

		String objId = null;
		String userId = null;
		String creationUser = null;
		Date creationDate = null;
		String modificationUser = null;
		Date modificationDate = null;
		Date creationDateFrom = null;
		Date creationDateTo = null;

		if (cond != null) {
			objId = cond.getObjId();
			userId = cond.getUserId();
			creationUser = cond.getCreationUser();
			creationDate = cond.getCreationDate();
			creationDateFrom = cond.getCreationDateFrom();
			creationDateTo = cond.getCreationDateTo();
			modificationUser = cond.getModificationUser();
			modificationDate = cond.getModificationDate();
		}
		buf.append(" from MailAccount obj");
		buf.append(" where obj.objId is not null");

		if (cond != null) {
			if (objId != null) 
				buf.append(" and obj.objId = :objId");
			if (userId != null) 
				buf.append(" and obj.userId = :userId");
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
			if (userId != null)
				query.setString("userId", userId);
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
	public long getMailAccountSize(String user, MailAccountCond cond) throws MailException {
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
	public MailAccount[] getMailAccounts(String user, MailAccountCond cond, String level) throws MailException {
		try {
			if (level == null)
				level = LEVEL_LITE;
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			if (level.equals(LEVEL_ALL)) {
				buf.append(" obj");
			} else {
				buf.append(" obj.objId,");
				buf.append(" obj.userId, obj.mailServerId, obj.mailServerName, obj.mailId,obj.mailUserName,obj.mailDeleteFetched, obj.mailPassword, obj.mailSignature, obj.useMailSign, obj.senderUserTitle,");
				buf.append(" obj.creationUser, obj.creationDate, obj.modificationUser, obj.modificationDate");
			}
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			if (list == null || list.isEmpty())
				return null;
			if (!level.equals(LEVEL_ALL)) {
				List objList = new ArrayList();
				for (Iterator itr = list.iterator(); itr.hasNext();) {
					Object[] fields = (Object[])itr.next();
					MailAccount obj = new MailAccount();
					int j = 0;
					obj.setObjId((String)fields[j++]);
					obj.setUserId((String)fields[j++]);
					obj.setMailServerId((String)fields[j++]);
					obj.setMailServerName((String)fields[j++]);
					obj.setMailId((String)fields[j++]);
					obj.setMailUserName((String)fields[j++]);
					obj.setMailDeleteFetched((String)fields[j++]);
					obj.setMailPassword((String)fields[j++]);
					obj.setMailSignature((String)fields[j++]);
					obj.setUseMailSign((Boolean)fields[j++]);
					obj.setSenderUserTitle((String)fields[j++]);
					obj.setCreationUser((String)fields[j++]);
					obj.setCreationDate((Timestamp)fields[j++]);
					obj.setModificationUser((String)fields[j++]);
					obj.setModificationDate((Timestamp)fields[j++]);
					objList.add(obj);
				}
				list = objList;
			}
			MailAccount[] objs = new MailAccount[list.size()];
			list.toArray(objs);
			return objs;
		} catch (Exception e) {
			logger.error(e, e);
			throw new MailException(e);
		}
	}

}