/*	
 * $Id$
 * created by    : hsshin
 * creation-date : 2012. 6. 1.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.common.loginuser.manager.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.smartworks.server.engine.common.loginuser.exception.LoginUserException;
import net.smartworks.server.engine.common.loginuser.manager.ILoginUserManager;
import net.smartworks.server.engine.common.loginuser.model.LoginUser;
import net.smartworks.server.engine.common.loginuser.model.LoginUserCond;
import net.smartworks.server.engine.common.manager.AbstractManager;
import net.smartworks.server.engine.common.util.CommonUtil;

import org.hibernate.Query;

public class LoginUserManagerImpl extends AbstractManager implements ILoginUserManager {

	public LoginUserManagerImpl() {
		super();
		if (logger.isInfoEnabled())
			logger.info(this.getClass().getName() + " created");
	}

	@Override
	public LoginUser getLoginUser(String user, String objId, String level) throws LoginUserException {
		if (level == null)
			level = LEVEL_ALL;
		if (level.equals(LEVEL_ALL)) {
			try {
				LoginUser obj = (LoginUser)get(LoginUser.class, objId);
				return obj;
			} catch (Exception e) {
				throw new LoginUserException(e);
			}
		} else {
			LoginUserCond cond = new LoginUserCond();
			cond.setObjId(objId);
			LoginUser[] objs = this.getLoginUsers(user, cond, level);
			if (CommonUtil.isEmpty(objs))
				return null;
			return objs[0];
		}
	}

	@Override
	public LoginUser getLoginUser(String user, LoginUserCond cond, String level) throws LoginUserException {
		if (cond == null)
			return null;
		if (level == null)
			level = LEVEL_ALL;
		cond.setPageSize(2);
		LoginUser[] loginUsers = getLoginUsers(user, cond, level);
		if (CommonUtil.isEmpty(loginUsers))
			return null;
		if (loginUsers.length > 1)
			throw new LoginUserException("More than 1 loginUser. ");
		return loginUsers[0];
	}

	@Override
	public void createLoginUser(String user, LoginUser obj) throws LoginUserException {

		StringBuffer buff = new StringBuffer();

		String userId = obj.getUserId();
		Date loginTime = obj.getLoginTime();

		buff.append(" insert into SwLoginUser ");
		buff.append(" (userId, loginTime) ");
		buff.append(" values (:userId, :loginTime) ");

		Query query = this.getSession().createSQLQuery(buff.toString());
		query.setString(LoginUser.A_USERID, userId);
		query.setTimestamp(LoginUser.A_LOGINTIME, loginTime);

		query.executeUpdate();

	}

	@Override
	public void setLoginUser(String user, LoginUser obj) throws LoginUserException {

		StringBuffer buff = new StringBuffer();

		String userId = obj.getUserId();
		Date loginTime = obj.getLoginTime();

		buff.append(" update SwLoginUser set ");
		buff.append(" loginTime = :loginTime ");
		buff.append(" where userId = :userId ");

		Query query = this.getSession().createSQLQuery(buff.toString());
		query.setString(LoginUser.A_USERID, userId);
		query.setTimestamp(LoginUser.A_LOGINTIME, loginTime);

		query.executeUpdate();

	}

	@Override
	public void removeLoginUser(String user, String objId) throws LoginUserException {
		try {
			remove(LoginUser.class, objId);
		} catch (Exception e) {
			throw new LoginUserException(e);
		}
	}

	@Override
	public void removeLoginUser(String user, LoginUserCond cond) throws LoginUserException {
		LoginUser obj = getLoginUser(user, cond, null);
		if (obj == null)
			return;
		removeLoginUser(user, obj.getUserId());
	}

	private Query appendQuery(StringBuffer buf, LoginUserCond cond) throws Exception {

		String userId = null;
		Date loginTime = null;

		if (cond != null) {
			userId = cond.getUserId();
			loginTime = cond.getLoginTime();
		}
		buf.append(" from LoginUser obj");
		buf.append(" where obj.userId is not null");
		if (cond != null) {
			if (userId != null)
				buf.append(" and obj.userId = :userId");
			if (loginTime != null)
				buf.append(" and obj.loginTime = :loginTime ");
		}
		this.appendOrderQuery(buf, "obj", cond);

		Query query = this.createQuery(buf.toString(), cond);
		if (cond != null) {
			if (userId != null)
				query.setString("userId", userId);
			if (loginTime != null)
				query.setTimestamp("loginTime", loginTime);
		}

		return query;

	}

	@Override
	public long getLoginUserSize(String user, LoginUserCond cond) throws LoginUserException {
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
			throw new LoginUserException(e);
		}
	}

	@Override
	public LoginUser[] getLoginUsers(String user, LoginUserCond cond, String level) throws LoginUserException {
		try {
			if (level == null)
				level = LEVEL_LITE;
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			if (level.equals(LEVEL_ALL)) {
				buf.append(" obj");
			} else {
				buf.append(" obj.userId, obj.loginTime");
			}
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			if (list == null || list.isEmpty())
				return null;
			if (!level.equals(LEVEL_ALL)) {
				List objList = new ArrayList();
				for (Iterator itr = list.iterator(); itr.hasNext();) {
					Object[] fields = (Object[]) itr.next();
					LoginUser obj = new LoginUser();
					int j = 0;
					obj.setUserId((String)fields[j++]);
					obj.setLoginTime(((Timestamp)fields[j++]));
					objList.add(obj);
				}
				list = objList;
			}
			LoginUser[] objs = new LoginUser[list.size()];
			list.toArray(objs);
			return objs;
		} catch (Exception e) {
			logger.error(e, e);
			throw new LoginUserException(e);
		}
	}

	@Override
	public void deleteAllLoginUser(String user) throws LoginUserException {
		StringBuffer buff = new StringBuffer();

		buff.append(" delete from SwLoginUser ");

		Query query = this.getSession().createSQLQuery(buff.toString());

		query.executeUpdate();

	}

}