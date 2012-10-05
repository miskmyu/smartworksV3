/*
 * $Id: LoginDaoImpl.java,v 1.1 2009/12/16 05:43:00 kmyu Exp $
 * created by    : jiwoongLee
 * creation-date : Apr 5, 2008
 * =========================================================
 * Copyright (c) 2008 ManInSoft, Inc. All rights reserved.
 */
package net.smartworks.server.engine.security.manager.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.sql.DataSource;

import net.smartworks.model.community.Community;
import net.smartworks.model.community.User;
import net.smartworks.model.community.info.DepartmentInfo;
import net.smartworks.server.engine.common.manager.IManager;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.engine.organization.model.SwoDepartment;
import net.smartworks.server.engine.organization.model.SwoDepartmentCond;
import net.smartworks.server.engine.security.manager.LoginDao;
import net.smartworks.server.engine.security.model.Login;
import net.smartworks.util.LocalDate;
import net.smartworks.util.LocaleInfo;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.util.StringUtils;

/**
 * @spring.bean id= "loginDao"
 * @spring.property name="dataSource" ref="dataSource"
 */
public class LoginDaoImpl extends JdbcDaoSupport implements LoginDao {

	private static String RETRIVE_USER = " select userInfo.*, mailAccount.userId as mailUserId, mailAccount.mailServerId, mailAccount.mailServerName, mailAccount.mailId, mailAccount.mailPassword " +
										 " from (" +
										 "	select 	orguser.id, orguser.name, orguser.nickName, orguser.companyId, orgcompany.name as companyName, orguser.deptId, orguser.adjunctDeptIds, orgdept.name as deptName, 		" +
										 "		   	orguser.empNo, orguser.mobileNo, orguser.internalNo, orguser.locale, orguser.timeZone,										" +
										 "          orguser.type, orguser.lang, orguser.pos, orguser.stdtime, orguser.authId,													" +
										 "	        orguser.email, orguser.useMail, useSign, sign, orguser.passwd, orguser.picture, orguser.roleId								" +
										 "    from 	sworguser orguser, sworgdept orgdept, sworgcompany orgcompany																" +
										 "	 where 	orguser.deptid = orgdept.id																									" +
										 "	   and 	orguser.companyid = orgcompany.id																							" +
										 "	   and 	orguser.id = ?" +
										 " ) userInfo " +
										 " left outer join " +
										 " swmailaccount mailAccount " +
										 " on userInfo.id = mailAccount.userId ";
	protected SelectQuery00 selectQuery00;
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.dao.support.DaoSupport#initDao()
	 */
	protected void initDao() throws Exception {
		super.initDao();
		this.selectQuery00 = new SelectQuery00(getDataSource());
	}

	/*
	 * (non-Javadoc)
	 * @see com.maninsoft.smart.portal.webapp.common.dao.LoginDao#isExistUserId(java.lang.String)
	 */
	public boolean isExistUserId(String userId) throws DataAccessException {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.maninsoft.smart.portal.webapp.common.dao.LoginDao#retrieve(java.lang.String)
	 */
	public Login retrieve(String userName) throws DataAccessException {
		return this.retrieveUserId(userName);
	}

	/*
	 * (non-Javadoc)
	 * @see com.maninsoft.smart.portal.webapp.common.dao.LoginDao#retrieveUserId(java.lang.String)
	 */
	public Login retrieveUserId(String userId) throws DataAccessException, ObjectRetrievalFailureException {
		
		Login login = (Login) selectQuery00.findObject(userId);
		
		String adjunctDeptIds = login.getAdjunctDeptIds();
		if (!CommonUtil.isEmpty(adjunctDeptIds)) {
			
			String[] ajtDeptInfo = StringUtils.tokenizeToStringArray(adjunctDeptIds, ";");
			String[] idIns = new String[ajtDeptInfo.length];
			for (int i = 0; i < ajtDeptInfo.length; i++) {
				String[] ajtDeptIdInfo = StringUtils.tokenizeToStringArray(ajtDeptInfo[i], "|");
				String deptId = ajtDeptIdInfo[0];
				idIns[i]= deptId;
			}
			try {
				SwoDepartmentCond cond = new SwoDepartmentCond();
				cond.setIdIns(idIns);
				SwoDepartment[] swoDepts = SwManagerFactory.getInstance().getSwoManager().getDepartments(userId, cond, IManager.LEVEL_LITE);
				DepartmentInfo[] depts = null;
				if (swoDepts != null && swoDepts.length != 0) {
					depts = new DepartmentInfo[swoDepts.length];
					for (int i = 0; i < swoDepts.length; i++) {
						depts[i] = new DepartmentInfo(swoDepts[i].getId(), swoDepts[i].getName(), swoDepts[i].getDescription()); 
					}
				}
				login.setDepartments(depts);
				
			} catch (Exception e) {
				throw new ObjectRetrievalFailureException("SwoException", e);
			}
		}
		if(login == null){
			throw new ObjectRetrievalFailureException("tried Login Id: "+userId+" ", login);
		}
		return login; 
	}

	protected class SelectQuery00 extends MappingSqlQuery {
		protected SelectQuery00(DataSource ds) {
			super(ds, RETRIVE_USER);
			declareParameter(new SqlParameter(Types.VARCHAR));
			compile();
		}
		protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

			Login login = new Login();
			login.setId(rs.getString("id"));
			login.setName(rs.getString("name"));
			login.setNickName(rs.getString("nickName"));
			login.setCompanyId(rs.getString("companyId"));
			login.setCompany(rs.getString("companyName"));
			login.setDepartmentId(rs.getString("deptId"));
			login.setAdjunctDeptIds(rs.getString("adjunctDeptIds"));
			login.setDepartment(rs.getString("deptName"));
			login.setEmpNo(rs.getString("empNo"));
			login.setCellPhoneNo(rs.getString("mobileNo"));
			login.setPhoneNo(rs.getString("internalNo"));
			login.setType(rs.getString("type"));
			login.setPosition(rs.getString("pos"));
			login.setAuthId(rs.getString("authId"));
			login.setEmail(rs.getString("email"));
			login.setUseMail(rs.getBoolean("useMail"));
			login.setUseSign(rs.getBoolean("useSign"));
			login.setPassword(rs.getString("passwd"));
			login.setLocale(rs.getString("locale"));
			login.setMailUserId(rs.getString("mailUserId"));
			login.setMailServerId(rs.getString("mailServerId"));
			login.setMailServerName(rs.getString("mailServerName"));
			login.setMailId(rs.getString("mailId"));
			login.setMailPassword(rs.getString("mailPassword"));

			String locale = CommonUtil.toNotNull(login.getLocale());
			if(locale.equals(""))
				locale = LocaleInfo.LOCALE_DEFAULT;
			login.setLocale(locale);

			String timeZone = CommonUtil.toNotNull(rs.getString("timeZone"));
			if(timeZone.equals(""))
				timeZone = LocalDate.TIMEZONE_SEOUL;
			login.setTimeZone(timeZone);
			String picture = CommonUtil.toNotNull(rs.getString("picture"));
			if(!picture.equals("")) {
				String extension = picture.lastIndexOf(".") > 1 ? picture.substring(picture.lastIndexOf(".") + 1) : null;
				String pictureId = picture.substring(0, (picture.length() - extension.length())-1);
				login.setBigPictureName(pictureId + Community.IMAGE_TYPE_THUMB + "." + extension);
				login.setSmallPictureName(pictureId + Community.IMAGE_TYPE_THUMB + "." + extension);
			} else {
				login.setBigPictureName(null);
				login.setSmallPictureName(null);
			}

			String sign = CommonUtil.toNotNull(rs.getString("sign"));

			if(!sign.equals("")) {
				String extension = sign.lastIndexOf(".") > 0 ? sign.substring(sign.lastIndexOf(".") + 1) : null;
				String signId = sign.substring(0, (sign.length() - extension.length())-1);
				login.setSignPictureName(signId + Community.IMAGE_TYPE_THUMB + "." + extension);
			} else {
				login.setSignPictureName(null);
			}

			login.setRole(rs.getString("roleId").equals("DEPT LEADER") ? User.USER_ROLE_LEADER : User.USER_ROLE_MEMBER);
			login.setUserLevel(login.getAuthId().equals("ADMINISTRATOR") ? User.USER_LEVEL_AMINISTRATOR : User.USER_LEVEL_DEFAULT);

			return login;
		}
	}

}
