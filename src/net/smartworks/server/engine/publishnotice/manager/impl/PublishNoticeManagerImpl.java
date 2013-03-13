/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 6. 10.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.publishnotice.manager.impl;

import java.util.Date;
import java.util.List;

import net.smartworks.server.engine.common.manager.AbstractManager;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.publishnotice.exception.PublishNoticeException;
import net.smartworks.server.engine.publishnotice.manager.IPublishNoticeManager;
import net.smartworks.server.engine.publishnotice.model.AlarmNotice;
import net.smartworks.server.engine.publishnotice.model.AlarmNoticeCond;
import net.smartworks.server.engine.publishnotice.model.PublishNotice;
import net.smartworks.server.engine.publishnotice.model.PublishNoticeCond;
import net.smartworks.server.engine.publishnotice.model.SpaceNotice;
import net.smartworks.server.engine.publishnotice.model.SpaceNoticeCond;

import org.hibernate.Query;

public class PublishNoticeManagerImpl extends AbstractManager implements IPublishNoticeManager {

	public PublishNoticeManagerImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public PublishNotice getPublishNotice(String userId, String id, String level) throws PublishNoticeException {
		try {
			if (level == null)
				level = LEVEL_ALL;
			if (level.equals(LEVEL_ALL)) {
				PublishNotice obj = (PublishNotice)this.get(PublishNotice.class, id);
				return obj;
			} else {
				PublishNoticeCond cond = new PublishNoticeCond();
				cond.setObjId(id);
				return getPublishNotice(userId, cond, level);
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw new PublishNoticeException(e);
		}
	}

	@Override
	public PublishNotice getPublishNotice(String userId, PublishNoticeCond cond, String level) throws PublishNoticeException {
		if (cond == null)
			return null;
		if (level == null)
			level = LEVEL_ALL;
		cond.setPageSize(2);
		PublishNotice[] objs = getPublishNotices(userId, cond, level);
		if (CommonUtil.isEmpty(objs))
			return null;
		if (objs.length > 1)
			throw new PublishNoticeException("More than 1 Process Instance.");
		return objs[0];
	}

	@Override
	public void setPublishNotice(String userId, PublishNotice obj, String level) throws PublishNoticeException {
		try {
			fill(userId, obj);
			set(obj);
		} catch (Exception e) {
			throw new PublishNoticeException(e);
		}
	}

	@Override
	public void removePublishNotice(String userId, String id) throws PublishNoticeException {
		try {
			remove(PublishNotice.class, id);
		} catch (Exception e) {
			throw new PublishNoticeException(e);
		}
	}

	@Override
	public void removePublishNotice(String userId, PublishNoticeCond cond) throws PublishNoticeException {
		PublishNotice obj = getPublishNotice(userId, cond, null);
		if (obj == null)
			return;
		removePublishNotice(userId, obj.getObjId());
	}
	private Query appendQuery(StringBuffer buf, PublishNoticeCond cond) throws Exception {
		String objId = null;
		int type = 0;
		String refType = null;
		String refId = null;
		String assignee = null;
		
		String creationUser = null;
		Date creationDate = null;
		Date creationDateFrom = null;
		Date creationDateTo = null;

		if (cond != null) {
			objId = cond.getObjId();
			type = cond.getType();
			refType = cond.getRefType();
			refId = cond.getRefId();
			assignee = cond.getAssignee();
			
			creationUser = cond.getCreationUser();
			creationDate = cond.getCreationDate();
			creationDateFrom = cond.getCreationDateFrom();
			creationDateTo = cond.getCreationDateTo();
		}
		buf.append(" from PublishNotice obj");
		buf.append(" where obj.objId is not null");
		if (cond != null) {
			if (objId != null) 
				buf.append(" and obj.objId = :objId");
			if (type != 0)
				buf.append(" and obj.type = :type");
			if (refType != null) 
				buf.append(" and obj.refType = :refType");
			if (refId != null) 
				buf.append(" and obj.refId = :refId");
			if (assignee != null) 
				buf.append(" and obj.assignee = :assignee");
			if (creationUser != null)
				buf.append(" and obj.creationUser = :creationUser");
			if (creationDate != null)
				buf.append(" and obj.creationDate = :creationDate");
			if (creationDateFrom != null)
				buf.append(" and obj.creationDate > :creationDateFrom");
			if (creationDateTo != null)
				buf.append(" and obj.creationDate < :creationDateTo");
		}
		this.appendOrderQuery(buf, "obj", cond);
		
		Query query = this.createQuery(buf.toString(), cond);
		if (cond != null) {
			if (objId != null)
				query.setString("objId", objId);
			if (type != 0)
				query.setInteger("type", type);
			if (refType != null)
				query.setString("refType", refType);
			if (refId != null)
				query.setString("refId", refId);
			if (assignee != null)
				query.setString("assignee", assignee);
			if (creationUser != null)
				query.setString("creationUser", creationUser);
			if (creationDate != null)
				query.setTimestamp("creationDate", creationDate);
			if (creationDateFrom != null)
				query.setTimestamp("creationDateFrom", creationDateFrom);
			if (creationDateTo != null)
				query.setTimestamp("creationDateTo", creationDateTo);
		}

		return query;

	}
	@Override
	public long getPublishNoticeSize(String userId, PublishNoticeCond cond) throws PublishNoticeException {
		try {
			StringBuffer buf = new StringBuffer();
			buf.append("select count(obj)");
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			long count = ((Long)list.get(0)).longValue();
			return count;
		} catch (Exception e) {
			logger.error(e, e);
			throw new PublishNoticeException(e);
		}
	}

	@Override
	public PublishNotice[] getPublishNotices(String userId, PublishNoticeCond cond, String level) throws PublishNoticeException {
		try {
			StringBuffer buf = new StringBuffer();
			buf.append("select obj ");
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			if (list == null || list.isEmpty())
				return null;
			PublishNotice[] objs = new PublishNotice[list.size()];
			list.toArray(objs);
			return objs;
		} catch (Exception e) {
			logger.error(e, e);
			throw new PublishNoticeException(e);
		}
	}

	@Override
	public SpaceNotice getSpaceNotice(String userId, String id, String level) throws PublishNoticeException {
		try {
			if (level == null)
				level = LEVEL_ALL;
			if (level.equals(LEVEL_ALL)) {
				SpaceNotice obj = (SpaceNotice)this.get(SpaceNotice.class, id);
				return obj;
			} else {
				SpaceNoticeCond cond = new SpaceNoticeCond();
				cond.setObjId(id);
				return getSpaceNotice(userId, cond, level);
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw new PublishNoticeException(e);
		}
	}
	@Override
	public SpaceNotice getSpaceNotice(String userId, SpaceNoticeCond cond, String level) throws PublishNoticeException {
		if (cond == null)
			return null;
		if (level == null)
			level = LEVEL_ALL;
		cond.setPageSize(2);
		SpaceNotice[] objs = getSpaceNotices(userId, cond, level);
		if (CommonUtil.isEmpty(objs))
			return null;
		if (objs.length > 1)
			throw new PublishNoticeException("More than 1 Process Instance.");
		return objs[0];
	}

	@Override
	public void setSpaceNotice(String userId, SpaceNotice obj, String level) throws PublishNoticeException {
		try {
			fill(userId, obj);
			set(obj);
		} catch (Exception e) {
			throw new PublishNoticeException(e);
		}
	}

	@Override
	public void removeSpaceNotice(String userId, String id) throws PublishNoticeException {
		try {
			remove(SpaceNotice.class, id);
		} catch (Exception e) {
			throw new PublishNoticeException(e);
		}
	}

	@Override
	public void removeSpaceNotice(String userId, SpaceNoticeCond cond) throws PublishNoticeException {
		SpaceNotice[] objs = getSpaceNotices(userId, cond, null);
		if (objs == null || objs.length == 0)
			return;
		for (int i = 0; i < objs.length; i++) {
			SpaceNotice obj = objs[i];
			removeSpaceNotice(userId, obj.getObjId());
		}
	}

	private Query appendQuery(StringBuffer buf, SpaceNoticeCond cond) throws Exception {
		String objId = null;
		String refType = null;
		String refId = null;
		String taskId = null;
		String assignee = null;
		String workId = null;
		String workSpaceType = null;
		String workSpaceId = null;
		
		String creationUser = null;
		Date creationDate = null;
		Date creationDateFrom = null;
		Date creationDateTo = null;

		if (cond != null) {
			objId = cond.getObjId();
			refType = cond.getRefType();
			refId = cond.getRefId();
			taskId = cond.getTaskId();
			assignee = cond.getAssignee();
			workId = cond.getWorkId();
			workSpaceType = cond.getWorkSpaceType();
			workSpaceId = cond.getWorkSpaceId();
			
			creationUser = cond.getCreationUser();
			creationDate = cond.getCreationDate();
			creationDateFrom = cond.getCreationDateFrom();
			creationDateTo = cond.getCreationDateTo();
		}
		buf.append(" from SpaceNotice obj");
		buf.append(" where obj.objId is not null");
		if (cond != null) {
			if (objId != null) 
				buf.append(" and obj.objId = :objId");
			if (workId != null)
				buf.append(" and obj.workId = :workId");
			if (workSpaceType != null)
				buf.append(" and obj.workSpaceType = :workSpaceType");
			if (workSpaceId != null)
				buf.append(" and obj.workSpaceId = :workSpaceId");
			if (refType != null) 
				buf.append(" and obj.refType = :refType");
			if (refId != null) 
				buf.append(" and obj.refId = :refId");
			if (taskId != null) 
				buf.append(" and obj.taskId = :taskId");
			if (assignee != null) 
				buf.append(" and obj.assignee = :assignee");
			if (creationUser != null)
				buf.append(" and obj.creationUser = :creationUser");
			if (creationDate != null)
				buf.append(" and obj.creationDate = :creationDate");
			if (creationDateFrom != null)
				buf.append(" and obj.creationDate > :creationDateFrom");
			if (creationDateTo != null)
				buf.append(" and obj.creationDate < :creationDateTo");
		}
		this.appendOrderQuery(buf, "obj", cond);
		
		Query query = this.createQuery(buf.toString(), cond);
		if (cond != null) {
			if (objId != null)
				query.setString("objId", objId);
			if (workId != null)
				query.setString("workId", workId);
			if (workSpaceType != null)
				query.setString("workSpaceType", workSpaceType);
			if (workSpaceId != null)
				query.setString("workSpaceId", workSpaceId);
			if (refType != null)
				query.setString("refType", refType);
			if (refId != null)
				query.setString("refId", refId);
			if (taskId != null)
				query.setString("taskId", taskId);
			if (assignee != null)
				query.setString("assignee", assignee);
			if (creationUser != null)
				query.setString("creationUser", creationUser);
			if (creationDate != null)
				query.setTimestamp("creationDate", creationDate);
			if (creationDateFrom != null)
				query.setTimestamp("creationDateFrom", creationDateFrom);
			if (creationDateTo != null)
				query.setTimestamp("creationDateTo", creationDateTo);
		}

		return query;

	}
	@Override
	public long getSpaceNoticeSize(String userId, SpaceNoticeCond cond) throws PublishNoticeException {
		try {
			StringBuffer buf = new StringBuffer();
			buf.append("select count(obj)");
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			long count = ((Long)list.get(0)).longValue();
			return count;
		} catch (Exception e) {
			logger.error(e, e);
			throw new PublishNoticeException(e);
		}
	}

	@Override
	public SpaceNotice[] getSpaceNotices(String userId, SpaceNoticeCond cond, String level) throws PublishNoticeException {
		try {
			StringBuffer buf = new StringBuffer();
			buf.append("select obj ");
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			if (list == null || list.isEmpty())
				return null;
			SpaceNotice[] objs = new SpaceNotice[list.size()];
			list.toArray(objs);
			return objs;
		} catch (Exception e) {
			logger.error(e, e);
			throw new PublishNoticeException(e);
		}
	}

	@Override
	public AlarmNotice getAlarmNotice(String userId, String id, String level) throws PublishNoticeException {
		try {
			if (level == null)
				level = LEVEL_ALL;
			if (level.equals(LEVEL_ALL)) {
				AlarmNotice obj = (AlarmNotice)this.get(AlarmNotice.class, id);
				return obj;
			} else {
				AlarmNoticeCond cond = new AlarmNoticeCond();
				cond.setObjId(id);
				return getAlarmNotice(userId, cond, level);
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw new PublishNoticeException(e);
		}
	}

	@Override
	public AlarmNotice getAlarmNotice(String userId, AlarmNoticeCond cond, String level) throws PublishNoticeException {
		if (cond == null)
			return null;
		if (level == null)
			level = LEVEL_ALL;
		cond.setPageSize(2);
		AlarmNotice[] objs = getAlarmNotices(userId, cond, level);
		if (CommonUtil.isEmpty(objs))
			return null;
		if (objs.length > 1)
			throw new PublishNoticeException("More than 1 Process Instance.");
		return objs[0];
	}

	@Override
	public void setAlarmNotice(String userId, AlarmNotice obj, String level) throws PublishNoticeException {
		try {
			fill(userId, obj);
			set(obj);
		} catch (Exception e) {
			throw new PublishNoticeException(e);
		}
	}

	@Override
	public void removeAlarmNotice(String userId, String id) throws PublishNoticeException {
		try {
			remove(AlarmNotice.class, id);
		} catch (Exception e) {
			throw new PublishNoticeException(e);
		}
	}

	@Override
	public void removeAlarmNotice(String userId, AlarmNoticeCond cond) throws PublishNoticeException {
		AlarmNotice[] objs = getAlarmNotices(userId, cond, null);
		if (objs == null || objs.length == 0)
			return;
		for (int i = 0; i < objs.length; i++) {
			AlarmNotice obj = objs[i];
			removeAlarmNotice(userId, obj.getObjId());
		}
	}
	private Query appendQuery(StringBuffer buf, AlarmNoticeCond cond) throws Exception {
		String objId = null;
		Date noticeTime = null;
		String workId = null;
		String recordId = null;
		String targetUser = null;
		String companyId = null;
		
		String creationUser = null;
		Date creationDate = null;
		Date creationDateFrom = null;
		Date creationDateTo = null;

		if (cond != null) {
			objId = cond.getObjId();
			noticeTime = cond.getNoticeTime();
			workId = cond.getWorkId();
			recordId = cond.getRecordId();
			targetUser = cond.getTargetUser();
			companyId = cond.getCompanyId();
			
			creationUser = cond.getCreationUser();
			creationDate = cond.getCreationDate();
			creationDateFrom = cond.getCreationDateFrom();
			creationDateTo = cond.getCreationDateTo();
		}
		buf.append(" from AlarmNotice obj");
		buf.append(" where obj.objId is not null");
		if (cond != null) {
			if (objId != null) 
				buf.append(" and obj.objId = :objId");
			if (noticeTime != null)
				buf.append(" and obj.noticeTime = :noticeTime");
			if (workId != null)
				buf.append(" and obj.workId = :workId");
			if (recordId != null)
				buf.append(" and obj.recordId = :recordId");
			if (targetUser != null)
				buf.append(" and obj.targetUser = :targetUser");
			if (companyId != null) 
				buf.append(" and obj.companyId = :companyId");
			if (creationUser != null)
				buf.append(" and obj.creationUser = :creationUser");
			if (creationDate != null)
				buf.append(" and obj.creationDate = :creationDate");
			if (creationDateFrom != null)
				buf.append(" and obj.creationDate > :creationDateFrom");
			if (creationDateTo != null)
				buf.append(" and obj.creationDate < :creationDateTo");
		}
		this.appendOrderQuery(buf, "obj", cond);
		
		Query query = this.createQuery(buf.toString(), cond);
		if (cond != null) {
			if (objId != null)
				query.setString("objId", objId);
			if (noticeTime != null)
				query.setTimestamp("noticeTime", noticeTime);
			if (workId != null)
				query.setString("workId", workId);
			if (recordId != null)
				query.setString("recordId", recordId);
			if (targetUser != null)
				query.setString("targetUser", targetUser);
			if (companyId != null)
				query.setString("companyId", companyId);
			if (creationUser != null)
				query.setString("creationUser", creationUser);
			if (creationDate != null)
				query.setTimestamp("creationDate", creationDate);
			if (creationDateFrom != null)
				query.setTimestamp("creationDateFrom", creationDateFrom);
			if (creationDateTo != null)
				query.setTimestamp("creationDateTo", creationDateTo);
		}
		return query;
	}
	@Override
	public long getAlarmNoticeSize(String userId, AlarmNoticeCond cond) throws PublishNoticeException {
		try {
			StringBuffer buf = new StringBuffer();
			buf.append("select count(obj)");
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			long count = ((Long)list.get(0)).longValue();
			return count;
		} catch (Exception e) {
			logger.error(e, e);
			throw new PublishNoticeException(e);
		}
	}
	@Override
	public AlarmNotice[] getAlarmNotices(String userId, AlarmNoticeCond cond, String level) throws PublishNoticeException {
		try {
			StringBuffer buf = new StringBuffer();
			buf.append("select obj ");
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			if (list == null || list.isEmpty())
				return null;
			AlarmNotice[] objs = new AlarmNotice[list.size()];
			list.toArray(objs);
			return objs;
		} catch (Exception e) {
			logger.error(e, e);
			throw new PublishNoticeException(e);
		}
	}
}
