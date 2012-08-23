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
import net.smartworks.server.engine.common.loginuser.model.LoginUserHistory;

public interface ILoginUserManager {

	public LoginUser getLoginUser(String user, String objId, String level) throws LoginUserException;
	public LoginUser getLoginUser(String user, LoginUserCond cond, String level) throws LoginUserException;
	public void createLoginUser(String user, LoginUser obj) throws LoginUserException;
	public void setLoginUser(String user, LoginUser obj) throws LoginUserException;
	public void removeLoginUser(String user, String objId) throws LoginUserException;
	public void removeLoginUser(String user, LoginUserCond cond) throws LoginUserException;
	public long getLoginUserSize(String user, LoginUserCond cond) throws LoginUserException;
	public LoginUser[] getLoginUsers(String user, LoginUserCond cond, String level) throws LoginUserException;
	public void deleteAllLoginUser(String user) throws LoginUserException;
	
	public void setLoginUserHistory(String user, LoginUserHistory obj) throws LoginUserException;

	
	
	//삭제예정
	public void copyAllCategory(String targetCtgId, String parentCtgId) throws Exception;
	
	
	
	
}