/**
*-------------------------------------------------------------------------------------------------------------------------
* File Name : SessionVO.java
* Description :
* Special Logics :
* @author : jihhong
* @version : $Revision$, $Date$
*-------------------------------------------------------------------------------------------------------------------------
* Copyright (c) 2009-2009 by LG CNS, Inc.
* All rights reserved.
*-------------------------------------------------------------------------------------------------------------------------
* DATE                  AUTHOR               DESCRIPTION
*-------------------------------------------------------------------------------------------------------------------------
* $Date$            jihhong
*-------------------------------------------------------------------------------------------------------------------------
*/
package ifez.framework.session;

import ifez.framework.session.service.UserSessionVO;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 세션 VO 클래스
 *
 * @author : jihhong
 * @version $Revision$  $Date$
 * @see
 */
public class SessionVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserSessionVO userInfo;
	private ArrayList authProgramList;
	
	public UserSessionVO getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(UserSessionVO userInfo) {
		this.userInfo = userInfo;
	}
	public ArrayList getAuthProgramList() {
		return authProgramList;
	}
	public void setAuthProgramList(ArrayList authProgramList) {
		this.authProgramList = authProgramList;
	}


}