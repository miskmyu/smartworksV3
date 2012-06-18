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
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.security.manager.LoginDao;
import net.smartworks.server.engine.security.model.Login;
import net.smartworks.util.LocalDate;
import net.smartworks.util.LocaleInfo;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.orm.ObjectRetrievalFailureException;

/**
 * @spring.bean id= "loginDao"
 * @spring.property name="dataSource" ref="dataSource"
 */
public class LoginDaoImpl extends JdbcDaoSupport implements LoginDao {

	private static String RETRIVE_USER = "	select 	orguser.id, orguser.name, orguser.nickName, orguser.companyId, orgcompany.name as companyName, orguser.deptId, orgdept.name as deptName, 		" +
										 "		   	orguser.empNo, orguser.mobileNo, orguser.internalNo, orguser.locale, orguser.timeZone,										" +
										 "          orguser.type, orguser.lang, orguser.pos, orguser.stdtime, orguser.authId,													" +
										 "	        orguser.email, orguser.useMail, orguser.passwd, orguser.picture, orguser.roleId																" +
										 "    from 	sworguser orguser, sworgdept orgdept, sworgcompany orgcompany																" +
										 "	 where 	orguser.deptid = orgdept.id																									" +
										 "	   and 	orguser.companyid = orgcompany.id																							" +
										 "	   and 	orguser.id = ?																												";
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
			login.setDepartment(rs.getString("deptName"));
			login.setEmpNo(rs.getString("empNo"));
			login.setCellPhoneNo(rs.getString("mobileNo"));
			login.setPhoneNo(rs.getString("internalNo"));
			login.setType(rs.getString("type"));
			login.setPosition(rs.getString("pos"));
			login.setAuthId(rs.getString("authId"));
			login.setEmail(rs.getString("email"));
			login.setUseMail(rs.getBoolean("useMail"));
			login.setPassword(rs.getString("passwd"));
			login.setLocale(rs.getString("locale"));

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
			login.setRole(rs.getString("roleId").equals("DEPT LEADER") ? User.USER_ROLE_LEADER : User.USER_ROLE_MEMBER);
			login.setUserLevel(login.getAuthId().equals("ADMINISTRATOR") ? User.USER_LEVEL_AMINISTRATOR : User.USER_LEVEL_DEFAULT);

			return login;
		}
	}

}
