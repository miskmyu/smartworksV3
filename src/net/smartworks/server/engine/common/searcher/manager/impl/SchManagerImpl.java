/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2011. 12. 6.
 * =========================================================
 * Copyright (c) 2011 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.common.searcher.manager.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.smartworks.model.community.User;
import net.smartworks.server.engine.common.manager.AbstractManager;
import net.smartworks.server.engine.common.searcher.exception.SchException;
import net.smartworks.server.engine.common.searcher.manager.ISchManager;
import net.smartworks.server.engine.common.searcher.model.SchUser;
import net.smartworks.server.engine.common.searcher.model.SchWorkspace;
import net.smartworks.server.engine.common.util.CommonUtil;

import org.hibernate.Query;

public class SchManagerImpl extends AbstractManager implements ISchManager {

	public SchWorkspace[] getSchWorkspace(String companyId, String userid, String key) throws SchException {

		if (CommonUtil.isEmpty(key)) 
			return null;
		
		StringBuffer queryBuffer = new StringBuffer();
		
		queryBuffer.append(" select *  ");
		queryBuffer.append(" from  ");
		queryBuffer.append(" ( ");
		queryBuffer.append(" 	select 'user' as type ");
		queryBuffer.append(" 		, usr.id, usr.name ");
		queryBuffer.append(" 		, '' as description ");
		queryBuffer.append(" 		, usr.nickName as userNickName ");
		queryBuffer.append(" 		, usr.pos as userPosition ");
		queryBuffer.append(" 		, usr.picture as userPicture ");
		queryBuffer.append(" 		, dept.id as userDeptId ");
		queryBuffer.append(" 		, dept.name as userDeptName ");
		queryBuffer.append(" 		, dept.description as userDeptDesc ");
		queryBuffer.append(" 	From sworguser usr, sworgdept dept ");
		queryBuffer.append(" 	where usr.deptId = dept.id ");
		queryBuffer.append(" 	union ");
		queryBuffer.append(" 	select 'department' as type ");
		queryBuffer.append(" 		, dept.id, dept.name ");
		queryBuffer.append(" 		, dept.description as description ");
		queryBuffer.append(" 		, '' as userNickName ");
		queryBuffer.append(" 		, '' as userPosition ");
		queryBuffer.append(" 		, '' as userPicture ");
		queryBuffer.append(" 		, '' as userDeptId ");
		queryBuffer.append(" 		, '' as userDeptName ");
		queryBuffer.append(" 		, '' as userDeptDesc  ");
		queryBuffer.append(" 	from sworgdept dept ");
		queryBuffer.append(" 	union ");
		queryBuffer.append(" 	select 'group' as type ");
		queryBuffer.append(" 		, grp.id, grp.name ");
		queryBuffer.append(" 		, grp.description as description ");
		queryBuffer.append(" 		, '' as userNickName ");
		queryBuffer.append(" 		, '' as userPosition ");
		queryBuffer.append(" 		, '' as userPicture ");
		queryBuffer.append(" 		, '' as userDeptId ");
		queryBuffer.append(" 		, '' as userDeptName ");
		queryBuffer.append(" 		, '' as userDeptDesc  ");
		queryBuffer.append(" 	from sworggroup grp ");
		queryBuffer.append(" 	where grp.groupType != 'C' ");
		queryBuffer.append(" ) workspaceinfo ");
		queryBuffer.append(" where workspaceinfo.name like :key ");
		
		Query query = this.getSession().createSQLQuery(queryBuffer.toString());

		query.setString("key", CommonUtil.toLikeString(key));
		
		List list = query.list();
		if (list == null || list.isEmpty())
			return null;
		List objList = new ArrayList();
		for (Iterator itr = list.iterator(); itr.hasNext();) {
			Object[] fields = (Object[]) itr.next();
			SchWorkspace obj = new SchWorkspace();
			int j = 0;
	
			obj.setType((String)fields[j++]);    
			obj.setId((String)fields[j++]);    
			obj.setName((String)fields[j++]);     
			obj.setDescription((String)fields[j++]);
			obj.setUserNickName((String)fields[j++]);   
			obj.setUserPosition((String)fields[j++]);   
			obj.setUserPicture((String)fields[j++]); 
			obj.setUserDeptId((String)fields[j++]);
			obj.setUserDeptName((String)fields[j++]);
			obj.setUserDeptDesc((String)fields[j++]);
			
			objList.add(obj);
		}
		list = objList;
		SchWorkspace[] objs = new SchWorkspace[list.size()];
		list.toArray(objs);
		return objs;
	}

	@Override
	public SchUser[] getSchUser(String companyId, String userId, String key) throws SchException {
		if (CommonUtil.isEmpty(key)) 
			return null;
		
		StringBuffer queryBuffer = new StringBuffer();
		
		queryBuffer.append(" select usr.id, usr.name ");
		queryBuffer.append(" 		, usr.pos as position ");
		queryBuffer.append(" 		, usr.roleId as roleId ");
		queryBuffer.append(" 		, usr.picture ");
		queryBuffer.append(" 		, dept.id as deptId ");
		queryBuffer.append(" 		, dept.name as deptName ");
		queryBuffer.append(" 		, dept.description as deptDesc ");
		queryBuffer.append("  from sworguser usr, sworgdept dept ");
		queryBuffer.append(" where usr.deptId = dept.id ");
		queryBuffer.append("   and usr.name like :key ");

		Query query = this.getSession().createSQLQuery(queryBuffer.toString());

		query.setString("key", CommonUtil.toLikeString(key));

		List list = query.list();
		if (list == null || list.isEmpty())
			return null;
		List objList = new ArrayList();
		for (Iterator itr = list.iterator(); itr.hasNext();) {
			Object[] fields = (Object[]) itr.next();
			SchUser obj = new SchUser();
			int j = 0;
			obj.setId((String)fields[j++]);
			obj.setName((String)fields[j++]);
			obj.setUserPosition((String)fields[j++]);
			obj.setUserRole(((String)fields[j++]).equals("DEPT LEADER") ? User.USER_ROLE_LEADER : User.USER_ROLE_MEMBER);
			obj.setUserPicture((String)fields[j++]);
			obj.setUserDeptId((String)fields[j++]);
			obj.setUserDeptName((String)fields[j++]);
			obj.setUserDeptDesc((String)fields[j++]);

			objList.add(obj);
		}
		list = objList;
		SchUser[] objs = new SchUser[list.size()];
		list.toArray(objs);
		return objs;

	}

	@Override
	public SchUser[] getSchCommunityMember(String companyId, String userId, String communityId, String key) throws SchException {
		if (CommonUtil.isEmpty(key)) 
			return null;

		StringBuffer queryBuffer = new StringBuffer();
		queryBuffer.append(" select userId, userName, userPos, userRoleId, deptId, deptName, deptDesc ");
		queryBuffer.append("   from ");
		queryBuffer.append("      (");
		queryBuffer.append("		select dept.id as id, usr.id as userId, usr.name as userName, ");
		queryBuffer.append("	           usr.pos as userPos, usr.roleId as userRoleId, ");
		queryBuffer.append("	   	       dept.id as deptId, dept.name as deptName, dept.description as deptDesc ");
		queryBuffer.append("		  from ");
		queryBuffer.append("	           sworguser usr, sworgdept dept ");
		queryBuffer.append("		 where usr.deptId = dept.id");
		queryBuffer.append("	     union ");
		queryBuffer.append("		select grpmember.groupId as id, usr.id as userId, usr.name as userName,");
		queryBuffer.append("	   		   usr.pos as userPos, usr.roleId as userRoleId,");
		queryBuffer.append("	   	       dept.id as deptId, dept.name as deptName, dept.description as deptDesc");
		queryBuffer.append("		  from ");
		queryBuffer.append("	           sworguser usr, sworgdept dept, sworggroupmember grpmember");
		queryBuffer.append("	     where usr.id = grpmember.userId");
		queryBuffer.append("  	       and usr.deptId = dept.id");
		queryBuffer.append("	   ) community");
		queryBuffer.append("   where community.id = :communityId ");
		queryBuffer.append("     and community.userName like :key ");
		queryBuffer.append("order by community.userRoleId asc, community.userName asc");

		Query query = this.getSession().createSQLQuery(queryBuffer.toString());

		query.setString("communityId", communityId);
		query.setString("key", CommonUtil.toLikeString(key));

		List list = query.list();
		if (list == null || list.isEmpty())
			return null;
		List objList = new ArrayList();
		for (Iterator itr = list.iterator(); itr.hasNext();) {
			Object[] fields = (Object[]) itr.next();
			SchUser obj = new SchUser();
			int j = 0;
			obj.setId((String)fields[j++]);
			obj.setName((String)fields[j++]);
			obj.setUserPosition((String)fields[j++]);
			obj.setUserRole(((String)fields[j++]).equals("DEPT LEADER") ? User.USER_ROLE_LEADER : User.USER_ROLE_MEMBER);
			obj.setUserDeptId((String)fields[j++]);
			obj.setUserDeptName((String)fields[j++]);
			obj.setUserDeptDesc((String)fields[j++]);

			objList.add(obj);
		}
		list = objList;
		SchUser[] objs = new SchUser[list.size()];
		list.toArray(objs);

		return objs;

	}
	@Override
	public SchUser[] getSchCommunityNonMember(String companyId, String userId, String communityId, String key) throws SchException {
		if (CommonUtil.isEmpty(key)) 
			return null;

		StringBuffer queryBuffer = new StringBuffer();
		queryBuffer.append("select userId, userName, userPos, userRoleId, deptId, deptName, deptDesc ");
		queryBuffer.append("from ");
		queryBuffer.append("(");
		queryBuffer.append("	select dept.id as id, usr.id as userId, usr.name as userName, ");
		queryBuffer.append("	usr.pos as userPos, usr.roleId as userRoleId, ");
		queryBuffer.append("	dept.id as deptId, dept.name as deptName, dept.description as deptDesc ");
		queryBuffer.append("	from sworguser usr, sworgdept dept ");
		queryBuffer.append("	where usr.deptId = dept.id ");
		queryBuffer.append("	and usr.id not in ( ");
		queryBuffer.append("		select userId ");
		queryBuffer.append("		from sworggroupmember grpmember ");
		queryBuffer.append("		where groupId =:communityId ");
		queryBuffer.append("	) ");
		queryBuffer.append(") community ");
		queryBuffer.append("where community.userName like :key ");
		queryBuffer.append("order by community.userRoleId asc, community.userName asc ");

		Query query = this.getSession().createSQLQuery(queryBuffer.toString());

		query.setString("communityId", communityId);
		query.setString("key", CommonUtil.toLikeString(key));

		List list = query.list();
		if (list == null || list.isEmpty())
			return null;
		List objList = new ArrayList();
		for (Iterator itr = list.iterator(); itr.hasNext();) {
			Object[] fields = (Object[]) itr.next();
			SchUser obj = new SchUser();
			int j = 0;
			obj.setId((String)fields[j++]);
			obj.setName((String)fields[j++]);
			obj.setUserPosition((String)fields[j++]);
			obj.setUserRole(((String)fields[j++]).equals("DEPT LEADER") ? User.USER_ROLE_LEADER : User.USER_ROLE_MEMBER);
			obj.setUserDeptId((String)fields[j++]);
			obj.setUserDeptName((String)fields[j++]);
			obj.setUserDeptDesc((String)fields[j++]);

			objList.add(obj);
		}
		list = objList;
		SchUser[] objs = new SchUser[list.size()];
		list.toArray(objs);

		return objs;

	}

}