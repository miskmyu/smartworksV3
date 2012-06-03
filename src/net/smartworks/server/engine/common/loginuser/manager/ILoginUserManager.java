/*	
 * $Id$
 * created by    : hsshin
 * creation-date : 2012. 6. 1.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.common.loginuser.manager;

import net.smartworks.server.engine.common.loginuser.exception.LoginUserException;
import net.smartworks.server.engine.common.loginuser.model.LoginUser;
import net.smartworks.server.engine.common.loginuser.model.LoginUserCond;

public interface ILoginUserManager {

	public LoginUser getLoginUser(String user, String objId, String level) throws LoginUserException;
	public LoginUser getLoginUser(String user, LoginUserCond cond, String level) throws LoginUserException;
	public void createLoginUser(String user, LoginUser obj) throws LoginUserException;
	public void setLoginUser(String user, LoginUser obj) throws LoginUserException;
	public void removeLoginUser(String user, String objId) throws LoginUserException;
	public void removeLoginUser(String user, LoginUserCond cond) throws LoginUserException;
	public long getLoginUserSize(String user, LoginUserCond cond) throws LoginUserException;
	public LoginUser[] getLoginUsers(String user, LoginUserCond cond, String level) throws LoginUserException;

}