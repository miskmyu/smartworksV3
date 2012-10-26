/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 10. 22.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.scheduling.manager.impl;

import java.util.Date;
import java.util.List;

import net.smartworks.server.engine.common.manager.AbstractManager;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.scheduling.exception.ScheduleException;
import net.smartworks.server.engine.scheduling.manager.IScheduleManager;
import net.smartworks.server.engine.scheduling.model.ScheduleDef;
import net.smartworks.server.engine.scheduling.model.ScheduleDefCond;

import org.hibernate.Query;
import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.impl.StdScheduler;

public class ScheduleManagerImpl extends AbstractManager implements IScheduleManager {
	
	private StdScheduler fb;
	public StdScheduler getFb() {
		return fb;
	}
	public void setFb(StdScheduler fb) {
		this.fb = fb;
	}
	
	@Override
	public ScheduleDef getScheduleDef(String userId, String id, String level) throws ScheduleException {
		try {
			if (level == null)
				level = LEVEL_ALL;
			if (level.equals(LEVEL_ALL)) {
				ScheduleDef obj = (ScheduleDef)this.get(ScheduleDef.class, id);
				return obj;
			} else {
				ScheduleDefCond cond = new ScheduleDefCond();
				cond.setObjId(id);
				return getScheduleDef(userId, cond, level);
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw new ScheduleException(e);
		}
	}
	@Override
	public ScheduleDef getScheduleDef(String userId, ScheduleDefCond cond, String level) throws ScheduleException {
		if (cond == null)
			return null;
		if (level == null)
			level = LEVEL_ALL;
		cond.setPageSize(2);
		ScheduleDef[] objs = getScheduleDefs(userId, cond, level);
		if (CommonUtil.isEmpty(objs))
			return null;
		if (objs.length > 1)
			throw new ScheduleException("More than 1 Instance.");
		return objs[0];
	}
	@Override
	public void setScheduleDef(String userId, ScheduleDef obj, String level) throws ScheduleException {
		try {
			fill(userId, obj);
			set(obj);
		} catch (Exception e) {
			throw new ScheduleException(e);
		}
	}
	@Override
	public void removeScheduleDef(String userId, String id) throws ScheduleException {
		try {
			remove(ScheduleDef.class, id);
		} catch (Exception e) {
			throw new ScheduleException(e);
		}
	}
	@Override
	public void removeScheduleDef(String userId, ScheduleDefCond cond) throws ScheduleException {
		ScheduleDef obj = getScheduleDef(userId, cond, null);
		if (obj == null)
			return;
		removeScheduleDef(userId, obj.getObjId());
	}
	private Query appendQuery(StringBuffer buf, ScheduleDefCond cond) throws Exception {
		String objId = null;

		String name = null;
		String status = null;
		String targetPackageType = null;
		String targetPackageId = null;
		String targetFormId = null;
		String targetFieldId = null;
		String groupName = null;
		String jobName = null;
		String triggerName = null;
		String targetClass = null;
		String cronExpression = null;
		String isAutoStart = null;
		
		String creationUser = null;
		Date creationDate = null;
		Date creationDateFrom = null;
		Date creationDateTo = null;

		if (cond != null) {
			objId = cond.getObjId();
			name = cond.getName();
			status = cond.getStatus();
			targetPackageType = cond.getTargetPackageType();
			targetPackageId = cond.getTargetPackageId();
			targetFormId = cond.getTargetFormId();
			targetFieldId = cond.getTargetFieldId();
			groupName = cond.getGroupName();
			jobName = cond.getJobName();
			triggerName = cond.getTriggerName();
			targetClass = cond.getTargetClass();
			cronExpression = cond.getCronExpression();
			isAutoStart = cond.getIsAutoStart();			
			
			creationUser = cond.getCreationUser();
			creationDate = cond.getCreationDate();
			creationDateFrom = cond.getCreationDateFrom();
			creationDateTo = cond.getCreationDateTo();
		}
		buf.append(" from ScheduleDef obj");
		buf.append(" where obj.objId is not null");
		if (cond != null) {
			if (objId != null) 
				buf.append(" and obj.objId = :objId");
			if (name != null)
				buf.append(" and obj.name = :name");
			if (status != null) 
				buf.append(" and obj.status = :status");
			if (targetPackageType != null) 
				buf.append(" and obj.targetPackageType = :targetPackageType");
			if (targetPackageId != null) 
				buf.append(" and obj.targetPackageId = :targetPackageId");
			if (targetFormId != null) 
				buf.append(" and obj.targetFormId = :targetFormId");
			if (targetFieldId != null) 
				buf.append(" and obj.targetFieldId = :targetFieldId");
			if (groupName != null) 
				buf.append(" and obj.groupName = :groupName");
			if (jobName != null) 
				buf.append(" and obj.jobName = :jobName");
			if (triggerName != null) 
				buf.append(" and obj.triggerName = :triggerName");
			if (targetClass != null) 
				buf.append(" and obj.targetClass = :targetClass");
			if (cronExpression != null) 
				buf.append(" and obj.cronExpression = :cronExpression");
			if (isAutoStart != null) 
				buf.append(" and obj.isAutoStart = :isAutoStart");
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
			if (name != null)
				query.setString("name", name);
			if (status != null)
				query.setString("status", status);
			if (targetPackageType != null)
				query.setString("targetPackageType", targetPackageType);
			if (targetPackageId != null)
				query.setString("targetPackageId", targetPackageId);
			if (targetFormId != null)
				query.setString("targetFormId", targetFormId);
			if (targetFieldId != null)
				query.setString("targetFieldId", targetFieldId);
			if (groupName != null)
				query.setString("groupName", groupName);
			if (jobName != null)
				query.setString("jobName", jobName);
			if (triggerName != null)
				query.setString("triggerName", triggerName);
			if (targetClass != null)
				query.setString("targetClass", targetClass);
			if (cronExpression != null)
				query.setString("cronExpression", cronExpression);
			if (isAutoStart != null)
				query.setString("isAutoStart", isAutoStart);
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
	public long getScheduleDefSize(String userId, ScheduleDefCond cond) throws ScheduleException {
		try {
			StringBuffer buf = new StringBuffer();
			buf.append("select count(obj)");
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			long count = ((Long)list.get(0)).longValue();
			return count;
		} catch (Exception e) {
			logger.error(e, e);
			throw new ScheduleException(e);
		}
	}
	@Override
	public ScheduleDef[] getScheduleDefs(String userId, ScheduleDefCond cond, String level) throws ScheduleException {
		try {
			StringBuffer buf = new StringBuffer();
			buf.append("select obj ");
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			if (list == null || list.isEmpty())
				return null;
			ScheduleDef[] objs = new ScheduleDef[list.size()];
			list.toArray(objs);
			return objs;
		} catch (Exception e) {
			logger.error(e, e);
			throw new ScheduleException(e);
		}
	}
	@Override
	public void fireScheduleDef(String userId, ScheduleDef obj, StdScheduler scheduleFactory) throws ScheduleException {
		if (scheduleFactory == null) {
			if (this.fb == null) {
				return;
			} else {
				scheduleFactory = this.fb;
			}
		}
		String className = obj.getTargetClass();
		String groupName = obj.getGroupName();
		String triggerName = obj.getTriggerName();
		String jobName = obj.getJobName();
		String cronStr = obj.getCronExpression();
		
		String name = obj.getName();

		if (CommonUtil.isEmpty(className))
			return;
		if (CommonUtil.isEmpty(jobName))
			return;
		if (CommonUtil.isEmpty(triggerName))
			return;
		if (CommonUtil.isEmpty(cronStr))
			return;
		
		try {
			Class targetClass = Class.forName(className);
			JobDetail job = new JobDetail(jobName, targetClass);
			if (!CommonUtil.isEmpty(name)) {
				JobDataMap jobMap = new JobDataMap();
				jobMap.put("name", name);
				job.setJobDataMap(jobMap);
			}
			CronTrigger cron = new CronTrigger(triggerName, CommonUtil.isEmpty(groupName) ? StdScheduler.DEFAULT_GROUP : groupName);
			CronExpression cronExp = new CronExpression(cronStr);
			cron.setCronExpression(cronExp);
			scheduleFactory.scheduleJob(job, cron);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ScheduleException(e);
		}
	}
	@Override
	public void stopScheduleDef(String userId, ScheduleDef obj, StdScheduler scheduleFactory) throws ScheduleException {
		if (scheduleFactory == null) {
			if (this.fb == null) {
				return;
			} else {
				scheduleFactory = this.fb;
			}
		}
		if (obj == null)
			return;
		if (CommonUtil.isEmpty(obj.getJobName()))
			return;
		try {
			scheduleFactory.deleteJob(obj.getJobName(), CommonUtil.isEmpty(obj.getGroupName())? StdScheduler.DEFAULT_GROUP : obj.getGroupName()); 
		} catch (Exception e) {
			e.printStackTrace();
			throw new ScheduleException(e);
		}
	}
}
