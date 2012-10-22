/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 10. 22.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package pro.ucity.sso.manager;

import java.util.ArrayList;

import ifez.framework.session.UserProgramAuthInfoVO;
import ifez.framework.session.service.UserSessionVO;

public interface ISsoManager {

	public UserSessionVO selectUser(UserSessionVO userSessionVO) throws Exception;
	public ArrayList<UserProgramAuthInfoVO> retriveUserProgramAuthInfo(UserSessionVO userSessionVO) throws Exception;
	
}
