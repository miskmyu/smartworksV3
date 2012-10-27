/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 10. 22.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package pro.ucity.sso.manager.impl;

import ifez.framework.session.UserProgramAuthInfoVO;
import ifez.framework.session.service.UserSessionVO;

import java.math.BigInteger;
import java.sql.Clob;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import net.smartworks.server.engine.common.manager.AbstractManager;
import net.smartworks.server.engine.process.process.exception.PrcException;
import net.smartworks.server.engine.worklist.model.TaskWork;

import pro.ucity.sso.manager.ISsoManager;

public class SsoManagerImpl extends AbstractManager implements ISsoManager {

	private String getSelectUserQuery(String userId) {	
		StringBuffer query = new StringBuffer();
		query.append(" SELECT USER_ID, GROUP_ID, PASSWORD, USER_NAME_KO, USER_NAME_EN, ");
        query.append("        LOCALE, EMP_NO, POSITION_CD, POSITION_NAME, OFFICE_PHONE,  ");
        query.append("        MOBL_PHONE, MAIL, STATUS ");
        query.append(" FROM CMDB.TM_CM_USER ");
        query.append(" WHERE USER_ID =  '").append(userId).append("'");
        return query.toString();
	}
	private String getAuthInfoQuery(String userId, String groupId) {
		StringBuffer query = new StringBuffer();
		query.append(" SELECT A.PROGRAM_ID , A.PROGRAM_URL ");
		query.append(" FROM USITUATION.TN_PO_PROGRAM_INFO A, USITUATION.TN_PO_PROGRAM_USER B ");
		query.append(" WHERE A.PROGRAM_ID = B.PROGRAM_ID ");
		query.append(" AND B.USER_ID = '").append(userId).append("'");
        query.append(" UNION ");
        query.append(" SELECT A.PROGRAM_ID , A.PROGRAM_URL ");
		query.append(" FROM USITUATION.TN_PO_PROGRAM_INFO A, USITUATION.TN_PO_PROGRAM_GROUP B ");
		query.append(" WHERE A.PROGRAM_ID = B.PROGRAM_ID ");
		query.append(" AND B.GROUP_ID = '").append(groupId).append("'");
        query.append(" UNION ");
        query.append(" SELECT A.PROGRAM_ID , A.PROGRAM_URL ");
        query.append(" FROM USITUATION.TN_PO_PROGRAM_INFO A, USITUATION.TN_PO_PROGRAM_ROLE B ");
		query.append(" WHERE A.PROGRAM_ID = B.PROGRAM_ID ");
		query.append(" AND B.ROLE_ID IN (SELECT ROLE_ID FROM CMDB.TN_CM_USER_ROLE WHERE USER_ID = '").append(userId).append("') ");	
		return query.toString();
	}
	
	@Override
	public UserSessionVO selectUser(UserSessionVO userSessionVO) throws Exception {
		try {
			String userId = userSessionVO.getUserId();
			Query query = this.getSession().createSQLQuery(getSelectUserQuery(userId));
		
			List list = query.list();
			if (list == null || list.isEmpty())
				return null;
			List objList = new ArrayList();
			for (Iterator itr = list.iterator(); itr.hasNext();) {
				Object[] fields = (Object[]) itr.next();
				UserSessionVO obj = new UserSessionVO();
				int j = 0;
		
				obj.setUserId((String)fields[j++]);
				obj.setGroupId((String)fields[j++]);
				obj.setPasswd((String)fields[j++]);
				obj.setUserNameKo((String)fields[j++]);
				obj.setUserNameEn((String)fields[j++]);
				obj.setLocale((String)fields[j++]);
				obj.setEmpNo((String)fields[j++]);
				obj.setGradeCd((String)fields[j++]);
				obj.setGradeName((String)fields[j++]);
				obj.setOfficePhone((String)fields[j++]);
				obj.setCellularPhone((String)fields[j++]);
				obj.setMail((String)fields[j++]);
				obj.setStatus((String)fields[j++]);
				
				objList.add(obj);
			}
			list = objList;
			UserSessionVO[] objs = new UserSessionVO[list.size()];
			list.toArray(objs);
			return objs[0];
				
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public ArrayList<UserProgramAuthInfoVO> retriveUserProgramAuthInfo(UserSessionVO userSessionVO) throws Exception {
		try {
			String userId = userSessionVO.getUserId();
			String groupId = userSessionVO.getGroupId();
			Query query = this.getSession().createSQLQuery(getAuthInfoQuery(userId, groupId));
		
			List list = query.list();
			if (list == null || list.isEmpty())
				return null;
			ArrayList objList = new ArrayList();
			for (Iterator itr = list.iterator(); itr.hasNext();) {
				Object[] fields = (Object[]) itr.next();
				UserProgramAuthInfoVO obj = new UserProgramAuthInfoVO();
				int j = 0;
				obj.setProgramId((String)fields[j++]);
				obj.setProgramUrl((String)fields[j++]);
				objList.add(obj);
			}
			return objList;
				
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

}
