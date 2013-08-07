/*	
 * $Id$
 * created by    : hsshin
 * creation-date : 2011. 11. 16.
 * =========================================================
 * Copyright (c) 2011 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.authority.manager;

import net.smartworks.server.engine.authority.exception.SwaException;
import net.smartworks.server.engine.authority.model.SwaDepartment;
import net.smartworks.server.engine.authority.model.SwaDepartmentCond;
import net.smartworks.server.engine.authority.model.SwaGroup;
import net.smartworks.server.engine.authority.model.SwaGroupCond;
import net.smartworks.server.engine.authority.model.SwaResource;
import net.smartworks.server.engine.authority.model.SwaResourceCond;
import net.smartworks.server.engine.authority.model.SwaUser;
import net.smartworks.server.engine.authority.model.SwaUserCond;
import net.smartworks.server.engine.authority.model.SwaAuthProxy;
import net.smartworks.server.engine.authority.model.SwaAuthProxyCond;
import net.smartworks.server.engine.common.manager.IManager;
import net.smartworks.server.engine.infowork.domain.exception.SwdException;
import net.smartworks.server.engine.organization.exception.SwoException;
import net.smartworks.server.engine.process.process.exception.PrcException;

public interface ISwaManager extends IManager {

	public SwaResource getResource(String user, String objId, String level) throws SwaException;
	public SwaResource getResource(String user, SwaResourceCond cond, String level) throws SwaException;
	public void setResource(String user, SwaResource obj, String level) throws SwaException;
	public void removeResource(String user, String objId) throws SwaException;
	public void removeResource(String user, SwaResourceCond cond) throws SwaException;
	public long getResourceSize(String user, SwaResourceCond cond) throws SwaException;
	public SwaResource[] getResources(String user, SwaResourceCond cond, String level) throws SwaException;

	public SwaUser getUser(String user, String objId, String level) throws SwaException;
	public SwaUser getUser(String user, SwaUserCond cond, String level) throws SwaException;
	public void setUser(String user, SwaUser obj, String level) throws SwaException;
	public void removeUser(String user, String objId) throws SwaException;
	public void removeUser(String user, SwaUserCond cond) throws SwaException;
	public long getUserSize(String user, SwaUserCond cond) throws SwaException;
	public SwaUser[] getUsers(String user, SwaUserCond cond, String level) throws SwaException;

	public String getUserMode(String user, String rscId, int type, String instId, String companyId) throws SwaException, SwoException, SwdException, PrcException;

	public SwaDepartment getAuthDepartment(String user, String objId, String level) throws SwaException;
	public SwaDepartment getAuthDepartment(String user, SwaDepartmentCond cond, String level) throws SwaException;
	public void setAuthDepartment(String user, SwaDepartment obj, String level) throws SwaException;
	public void removeAuthDepartment(String user, String objId) throws SwaException;
	public void removeAuthDepartment(String user, SwaDepartmentCond cond) throws SwaException;
	public long getAuthDepartmentSize(String user, SwaDepartmentCond cond) throws SwaException;
	public SwaDepartment[] getAuthDepartments(String user, SwaDepartmentCond cond, String level) throws SwaException;

	public SwaGroup getAuthGroup(String user, String objId, String level) throws SwaException;
	public SwaGroup getAuthGroup(String user, SwaGroupCond cond, String level) throws SwaException;
	public void setAuthGroup(String user, SwaGroup obj, String level) throws SwaException;
	public void removeAuthGroup(String user, String objId) throws SwaException;
	public void removeAuthGroup(String user, SwaGroupCond cond) throws SwaException;
	public long getAuthGroupSize(String user, SwaGroupCond cond) throws SwaException;
	public SwaGroup[] getAuthGroups(String user, SwaGroupCond cond, String level) throws SwaException;
	
	public SwaAuthProxy getAuthProxy(String user, String objId, String level) throws SwaException;
	public SwaAuthProxy getAuthProxy(String user, SwaAuthProxyCond cond, String level) throws SwaException;
	public void setAuthProxy(String user, SwaAuthProxy obj, String level) throws SwaException;
	public void removeAuthProxy(String user, String objId) throws SwaException;
	public void removeAuthProxy(String user, SwaAuthProxyCond cond) throws SwaException;
	public long getAuthProxySize(String user, SwaAuthProxyCond cond) throws SwaException;
	public SwaAuthProxy[] getAuthProxys(String user, SwaAuthProxyCond cond, String level) throws SwaException;
	public boolean compareAccessPolicyWithAuthProxy(String user, String resourceId, String accessLevel, String accessValue) throws Exception;	
	
	public void setAuthProxy(String userId, SwaUser[] objs) throws Exception;
	public void removeAllAuthProxyByResourceId(String userId, String resourceId) throws Exception;
}