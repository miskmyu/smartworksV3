/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 9. 27.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package pro.ucity.manager.ucityWorkList.manager.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.smartworks.server.engine.common.manager.AbstractManager;
import net.smartworks.server.engine.common.util.CommonUtil;

import org.hibernate.Query;

import pro.ucity.manager.ucityWorkList.exception.UcityWorkListException;
import pro.ucity.manager.ucityWorkList.manager.IUcityWorkListManager;
import pro.ucity.manager.ucityWorkList.model.UcityWorkList;
import pro.ucity.manager.ucityWorkList.model.UcityWorkListCond;

public class UcityWorkListManagerImpl extends AbstractManager implements IUcityWorkListManager {

	@Override
	public UcityWorkList getUcityWorkList(String userId, String id, String level) throws UcityWorkListException {
		try {
			if (level == null)
				level = LEVEL_ALL;
			if (level.equals(LEVEL_ALL)) {
				UcityWorkList obj = (UcityWorkList)this.get(UcityWorkList.class, id);
				return obj;
			} else {
				UcityWorkListCond cond = new UcityWorkListCond();
				cond.setObjId(id);
				return getUcityWorkList(userId, cond, level);
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw new UcityWorkListException(e);
		}
	}

	@Override
	public UcityWorkList getUcityWorkList(String userId, UcityWorkListCond cond, String level) throws UcityWorkListException {
		if (level == null)
			level = LEVEL_ALL;
		cond.setPageSize(2);
		UcityWorkList[] objs = getUcityWorkLists(userId, cond, level);
		if (CommonUtil.isEmpty(objs))
			return null;
		try {
			if (objs.length != 1)
				throw new UcityWorkListException("More than 1 Object");
		} catch (UcityWorkListException e) {
			logger.error(e, e);
			throw e;
		}
		return objs[0];
	}

	@Override
	public void setUcityWorkList(String userId, UcityWorkList obj, String level) throws UcityWorkListException {
		try {
			fill(userId, obj);
			set(obj);
		} catch (UcityWorkListException e) {
			throw e;
		} catch (Exception e) {
			throw new UcityWorkListException(e);
		}
	}

	@Override
	public void removeUcityWorkList(String userId, String id) throws UcityWorkListException {
		try {
			remove(UcityWorkList.class, id);
		} catch (Exception e) {
			logger.error(e, e);
			throw new UcityWorkListException(e);
		}
	}

	@Override
	public void removeUcityWorkList(String userId, UcityWorkListCond cond) throws UcityWorkListException {
		UcityWorkList obj = getUcityWorkList(userId, cond, null);
		if (obj == null)
			return;
		removeUcityWorkList(userId, obj.getObjId());
	}
	private Query appendQuery(StringBuffer buf, UcityWorkListCond cond) throws Exception {
		String objId = null;
		
		String prcInstId = null;
		String packageId = null;
		String status = null;
		String title = null;
		String runningTaskId = null;
		String runningTaskName = null;

		String serviceName = null;
		String eventName = null;
		String type = null;
		String externalDisplay = null;
		String isSms = null;
		String eventPlace = null;
		
		String searchKey = null;
		
		String creationUser = null;
		Date creationDate = null;
		String modificationUser = null;
		Date modificationDate = null;
		
		if (cond != null) {
			objId = cond.getObjId();
			
			prcInstId = cond.getPrcInstId();
			packageId = cond.getPackageId();
			status = cond.getStatus();
			title = cond.getTitle();
			runningTaskId = cond.getRunningTaskId();
			runningTaskName = cond.getRunningTaskName();
			serviceName = cond.getServiceName();
			eventName = cond.getEventName();
			type = cond.getType();
			externalDisplay = cond.getExternalDisplay();
			isSms = cond.getIsSms();
			eventPlace = cond.getEventPlace();
			
			searchKey = cond.getSearchKey();
			
			creationUser = cond.getCreationUser();
			creationDate = cond.getCreationDate();
			modificationUser = cond.getModificationUser();
			modificationDate = cond.getModificationDate();
		}
		buf.append(" from UcityWorkList obj");
		buf.append(" where obj.objId is not null");
		Map filterMap = new HashMap();
		//TODO 시간 검색에 대한 확인 필요
		if (cond != null) {
			if (objId != null)
				buf.append(" and obj.objId = :objId");
			if (prcInstId != null)
				buf.append(" and obj.prcInstId = :prcInstId");
			if (packageId != null)
				buf.append(" and obj.packageId = :packageId");
			if (status != null)
				buf.append(" and obj.status = :status");
			if (title != null)
				buf.append(" and obj.title = :title");
			if (runningTaskId != null)
				buf.append(" and obj.runningTaskId = :runningTaskId");
			if (runningTaskName != null)
				buf.append(" and obj.runningTaskName = :runningTaskName");
			if (serviceName != null)
				buf.append(" and obj.serviceName = :serviceName");
			if (eventName != null)
				buf.append(" and obj.eventName = :eventName");
			if (type != null)
				buf.append(" and obj.type = :type");
			if (externalDisplay != null)
				buf.append(" and obj.externalDisplay = :externalDisplay");
			if (isSms != null)
				buf.append(" and obj.isSms = :isSms");
			if (eventPlace != null)
				buf.append(" and obj.eventPlace = :eventPlace");
			if (creationUser != null)
				buf.append(" and obj.creationUser = :creationUser");
			if (creationDate != null)
				buf.append(" and obj.creationDate = :creationDate");
			if (modificationUser != null)
				buf.append(" and obj.modificationUser = :modificationUser");
			if (modificationDate != null)
				buf.append(" and obj.modificationDate = :modificationDate");
			if (searchKey != null) {
				buf.append("and (obj.status like :searchKey or obj.title like :searchKey or obj.runningTaskName like :searchKey )");
			}
		}
		this.appendOrderQuery(buf, "obj", cond);
		
		Query query = this.createQuery(buf.toString(), cond);
		if (cond != null) {
			if (objId != null)
				query.setString("objId", objId);
			

			prcInstId = cond.getPrcInstId();
			packageId = cond.getPackageId();
			status = cond.getStatus();
			title = cond.getTitle();
			runningTaskId = cond.getRunningTaskId();
			runningTaskName = cond.getRunningTaskName();
			serviceName = cond.getServiceName();
			eventName = cond.getEventName();
			type = cond.getType();
			externalDisplay = cond.getExternalDisplay();
			isSms = cond.getIsSms();
			eventPlace = cond.getEventPlace();
			
			if (prcInstId != null)
				query.setString("prcInstId", prcInstId);
			if (packageId != null)
				query.setString("packageId", packageId);
			if (status != null)
				query.setString("status", status);
			if (title != null)
				query.setString("title", title);
			if (runningTaskId != null)
				query.setString("runningTaskId", runningTaskId);
			if (runningTaskName != null)
				query.setString("runningTaskName", runningTaskName);
			if (serviceName != null)
				query.setString("serviceName", serviceName);
			if (eventName != null)
				query.setString("eventName", eventName);
			if (type != null)
				query.setString("type", type);
			if (externalDisplay != null)
				query.setString("externalDisplay", externalDisplay);
			if (isSms != null)
				query.setString("isSms", isSms);
			if (eventPlace != null)
				query.setString("eventPlace", eventPlace);
			if (searchKey != null)
				query.setString("searchKey", CommonUtil.toLikeString(searchKey));
			if (creationUser != null)
				query.setString("creationUser", creationUser);
			if (creationDate != null)
				query.setTimestamp("creationDate", creationDate);
			if (modificationUser != null)
				query.setString("modificationUser", modificationUser);
			if (modificationDate != null)
				query.setTimestamp("modificationDate", modificationDate);
		}
		return query;
	}
	@Override
	public long getUcityWorkListSize(String userId, UcityWorkListCond cond) throws UcityWorkListException {
		try {
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			buf.append(" count(obj)");
			Query query = this.appendQuery(buf,cond);
			List list = query.list();
			long count = ((Long)list.get(0)).longValue();
			return count;
		} catch (Exception e) {
			logger.error(e, e);
			throw new UcityWorkListException(e);
		}
	}

	@Override
	public UcityWorkList[] getUcityWorkLists(String userId, UcityWorkListCond cond, String level) throws UcityWorkListException {
		try {
			if (level == null)
				level = LEVEL_ALL;
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			buf.append(" obj");
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			if (list == null || list.isEmpty())
				return null;
			UcityWorkList[] objs = new UcityWorkList[list.size()];
			list.toArray(objs);
			return objs;
		} catch (Exception e) {
			logger.error(e, e);
			throw new UcityWorkListException(e);
		}
	}
}
