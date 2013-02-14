/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 9. 27.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package pro.ucity.manager.ucityWorkList.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.smartworks.server.engine.common.manager.IManager;
import pro.ucity.manager.ucityWorkList.exception.UcityWorkListException;
import pro.ucity.manager.ucityWorkList.model.UcityWorkList;
import pro.ucity.manager.ucityWorkList.model.UcityWorkListCond;

public interface IUcityWorkListManager extends IManager {

	public UcityWorkList getUcityWorkList(String userId, String id, String level) throws UcityWorkListException;
	public UcityWorkList getUcityWorkList(String userId, UcityWorkListCond cond, String level) throws UcityWorkListException;
	public void setUcityWorkList(String userId, UcityWorkList obj, String level) throws UcityWorkListException;
	public void removeUcityWorkList(String userId, String id) throws UcityWorkListException;
	public void removeUcityWorkList(String userId, UcityWorkListCond cond) throws UcityWorkListException;
	public long getUcityWorkListSize(String userId, UcityWorkListCond cond) throws UcityWorkListException;
	public UcityWorkList[] getUcityWorkLists(String userId, UcityWorkListCond cond, String level) throws UcityWorkListException;
	public String getUcityChartXml(String categoryName, String periodName, String serviceName, String eventName) throws Exception;
	public void getUcityChartExcel(String categoryName, String periodName, String serviceName, String eventName, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
