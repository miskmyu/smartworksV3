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

import net.smartworks.server.engine.category.model.CtgCategory;
import net.smartworks.server.engine.category.model.CtgCategoryCond;
import net.smartworks.server.engine.common.loginuser.exception.LoginUserException;
import net.smartworks.server.engine.common.loginuser.manager.ILoginUserManager;
import net.smartworks.server.engine.common.loginuser.model.LoginUser;
import net.smartworks.server.engine.common.loginuser.model.LoginUserCond;
import net.smartworks.server.engine.common.loginuser.model.LoginUserHistory;
import net.smartworks.server.engine.common.manager.AbstractManager;
import net.smartworks.server.engine.common.menuitem.model.CategoryChange;
import net.smartworks.server.engine.common.menuitem.model.FormChange;
import net.smartworks.server.engine.common.menuitem.model.FormChangeCond;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.engine.pkg.model.PkgPackage;
import net.smartworks.server.engine.pkg.model.PkgPackageCond;
import net.smartworks.server.engine.resource.exception.SmartServerRuntimeException;
import net.smartworks.server.engine.resource.model.IPackageModel;
import net.smartworks.server.engine.resource.util.XmlUtil;

import org.hibernate.Query;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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

	@Override
	public void setLoginUserHistory(String user, LoginUserHistory obj) throws LoginUserException {
		try {
			fill(user, obj);
			set(obj);
		} catch (LoginUserException e) {
			throw e;
		} catch (Exception e) {
			throw new LoginUserException(e);
		}
		
	}

	public void copyAllCategory(String targetCtgId, String parentCtgId) throws Exception {
		
		
		//카테고리를 복사를 한다
		CtgCategory ctg = SwManagerFactory.getInstance().getCtgManager().getCategory("jybae@maninsoft.co.kr", targetCtgId, null);
		
		if (ctg == null)
			return;
		String oldCtgId = ctg.getObjId();
		
		CtgCategory newCtg = (CtgCategory)ctg.clone();
		
		String newCtgId = "newCtg_"+CommonUtil.newId();
		
		newCtg.setParentId(parentCtgId);
		newCtg.setObjId(newCtgId);
		newCtg.setName("복사본_" + newCtg.getName());
		
		SwManagerFactory.getInstance().getCtgManager().setCategory("jybae@maninsoft.co.kr", newCtg, null);
		CategoryChange cc = new CategoryChange();
		cc.setOldCategoryId(oldCtgId);
		cc.setNewCategoryId(newCtgId);
		SwManagerFactory.getInstance().getItmManager().setCategoryChange("jybae@maninsoft.co.kr", cc, null);
		System.out.println(newCtg.getName() + " 카테고리 생성 (old : " + oldCtgId + " , new : " + newCtgId + ")" );
		
		this.copyAllPackage(oldCtgId, newCtgId);
		
		CtgCategoryCond subCtgCond = new CtgCategoryCond();
		subCtgCond.setParentId(oldCtgId);
		CtgCategory[] subCtgs = SwManagerFactory.getInstance().getCtgManager().getCategorys("", subCtgCond, null);
		
		if (subCtgs == null || subCtgs.length == 0)
			return;
		for (int i = 0; i < subCtgs.length; i++) {
			CtgCategory subCtg = subCtgs[i];
			copyAllCategory(subCtg.getObjId(), newCtgId);
		}
	}
	
	
	public void copyAllPackage(String categoryId, String targetCategoryId) throws Exception {
		
		PkgPackageCond pkgCond = new PkgPackageCond();
		pkgCond.setCategoryId(categoryId);
		PkgPackage[] pkgs = SwManagerFactory.getInstance().getPkgManager().getPackages("jybae@maninsoft.co.kr", pkgCond, null);
		if (pkgs == null || pkgs.length == 0)
			return;
		for (int i = 0; i < pkgs.length; i++) {
			PkgPackage pkg = pkgs[i];
			IPackageModel newPkg = SwManagerFactory.getInstance().getDesigntimeManager().clonePackage("jybae@maninsoft.co.kr", targetCategoryId, "복사본_" + pkg.getName() , "설명", pkg.getPackageId(), 1);
			System.out.println(newPkg.getName() + " 패키지 생성 (old : " + pkg.getPackageId() + " , new : " + newPkg.getPackageId() + ")" );
			System.out.println(" 1 초간 딜레이! ");
			Thread.sleep(1000);
			populateNewMappingFormIdToCopyPackage(newPkg);
			
		}
	}
	
	public void populateNewMappingFormIdToCopyPackage(IPackageModel pkg) throws Exception {
		
		List formList = pkg.getFormContentList();
		
		if (formList != null && !formList.isEmpty()) {
			for (Iterator<String> formItr = formList.iterator(); formItr.hasNext();) {
				String formXml = formItr.next();

				if (CommonUtil.isEmpty(formXml))
					return;
				
				Document doc = XmlUtil.parse(formXml, false, "UTF-8");
				Element root = doc.getDocumentElement();
				
				String formId = root.getAttribute("id");
				String version = root.getAttribute("version");
				String formName = root.getAttribute("name");
				String formTitle = root.getAttribute("title");
				String formSystemName = root.getAttribute("systemName");
				
				Node childrenNode = XmlUtil.getXpathNode(root, "./mappingForms");
				if (childrenNode == null)
					return;

				NodeList entityNodeList = XmlUtil.getXpathNodeList(childrenNode, "./mappingForm");
				if (CommonUtil.isEmpty(entityNodeList))
					return;
				
				for(int i = 0 ; i < entityNodeList.getLength() ; i++) {
					Element entity = (Element)entityNodeList.item(i);
					String targetFormId = entity.getAttribute("targetFormId");
					String newTargetFormId = null;

					FormChangeCond formChangeCond = new FormChangeCond();
					formChangeCond.setOldFormId(targetFormId);
					FormChange formChange = null;//SwManagerFactory.getInstance().getItmManager().getFormChanges("", formChangeCond, null);
					
					if (CommonUtil.isEmpty(newTargetFormId)) {
						System.out.println("######################## FAIL FORM UPDATE : FORMID - " + formId + "#############################");
					} else {
						newTargetFormId = formChange.getNewFormId();
					}
					entity.setAttribute("targetFormId", newTargetFormId);
				}
				updateFormContent(formId, 1, net.smartworks.server.engine.common.util.XmlUtil.toXmlString(doc));
				System.out.println(formId + " 폼 mapping Info 업데이트 완료!! ");
				System.out.println(" 1 초간 딜레이! ");
				Thread.sleep(1000);
			}
		}
	}

	private void updateFormContent(String formId, int version, String content) throws SmartServerRuntimeException {
		String hql = "update HbFormContent set content = :content where formId = :formId and version = :version";
		Query query = this.getSession().createQuery(hql);
		query.setString("content", content);
		query.setString("formId", formId);
		query.setInteger("version", version);
		query.executeUpdate();
	}
}