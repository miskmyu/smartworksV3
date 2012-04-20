/*	
 * $Id$
 * created by    : hsshin
 * creation-date : 2012. 3. 23.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.like.manager.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.smartworks.server.engine.common.manager.AbstractManager;
import net.smartworks.server.engine.like.exception.LikeException;
import net.smartworks.server.engine.like.manager.ILikeManager;
import net.smartworks.server.engine.like.model.Like;
import net.smartworks.server.engine.like.model.LikeCond;

import org.hibernate.Query;

public class LikeManagerImpl extends AbstractManager implements ILikeManager {

	public LikeManagerImpl() {
		super();
		if (logger.isInfoEnabled())
			logger.info(this.getClass().getName() + " created");
	}

	@Override
	public void createLike(String user, Like obj) throws LikeException {
		try {
			fill(user, obj);
			create(obj);
		} catch (Exception e) {
			logger.error(e, e);
			throw new LikeException(e);
		}
	}

	@Override
	public void removeLike(String user, String objId) throws LikeException {
		try {
			remove(Like.class, objId);
		} catch (Exception e) {
			throw new LikeException(e);
		}
	}

	private Query appendQuery(StringBuffer buf, LikeCond cond) throws Exception {
		String objId = null;
		int refType = 0;
		String refId = null;
		String creationUser = null;
		Date creationDate = null;

		if (cond != null) {
			objId = cond.getObjId();
			refType = cond.getRefType();
			refId = cond.getRefId();
			creationUser = cond.getCreationUser();
			creationDate = cond.getCreationDate();
		}
		buf.append(" from Like obj");
		buf.append(" where obj.objId is not null");
		if (cond != null) {
			if (objId != null) 
				buf.append(" and obj.objId = :objId");
			if (refType != 0)
				buf.append(" and obj.refType = :refType");
			if (refId != null) 
				buf.append(" and obj.refId = :refId");
			if (creationUser != null)
				buf.append(" and obj.creationUser = :creationUser");
			if (creationDate != null)
				buf.append(" and obj.creationDate = :creationDate");
		}
		this.appendOrderQuery(buf, "obj", cond);
		
		Query query = this.createQuery(buf.toString(), cond);
		if (cond != null) {
			if (objId != null)
				query.setString("objId", objId);
			if (refType != 0)
				query.setInteger("refType", refType);
			if (refId != null)
				query.setString("refId", refId);
			if (creationUser != null)
				query.setString("creationUser", creationUser);
			if (creationDate != null)
				query.setTimestamp("creationDate", creationDate);
		}

		return query;

	}

	@Override
	public long getLikeSize(String user, LikeCond cond) throws LikeException {
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
			throw new LikeException(e);
		}
	}

	@Override
	public Like[] getLikes(String user, LikeCond cond, String level) throws LikeException {
		try {
			if (level == null)
				level = LEVEL_LITE;
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			if (level.equals(LEVEL_ALL)) {
				buf.append(" obj");
			} else {
				buf.append(" obj.objId, obj.refType, obj.refId,");
				buf.append(" obj.creationUser, obj.creationDate");
			}
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			if (list == null || list.isEmpty())
				return null;
			if (!level.equals(LEVEL_ALL)) {
				List objList = new ArrayList();
				for (Iterator itr = list.iterator(); itr.hasNext();) {
					Object[] fields = (Object[]) itr.next();
					Like obj = new Like();
					int j = 0;
					obj.setObjId((String)fields[j++]);
					obj.setRefType((Integer)fields[j++]);
					obj.setRefId((String)fields[j++]);
					obj.setCreationUser(((String)fields[j++]));
					obj.setCreationDate(((Timestamp)fields[j++]));
					objList.add(obj);
				}
				list = objList;
			}
			Like[] objs = new Like[list.size()];
			list.toArray(objs);
			return objs;
		} catch (Exception e) {
			logger.error(e, e);
			throw new LikeException(e);
		}
	}

}