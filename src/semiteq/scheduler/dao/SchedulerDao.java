/*	
 * $Id: SchedulerDao.java,v 1.1 2012/01/18 02:13:56 hsshin Exp $
 * created by    : SHIN HYUN SEONG
 * creation-date : 2011. 12. 23.
 * =========================================================
 * Copyright (c) 2011 ManinSoft, Inc. All rights reserved.
 */

package semiteq.scheduler.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import semiteq.scheduler.model.PurchaseModel;
import semiteq.scheduler.model.RequestModel;

public interface SchedulerDao {

	public List getPurchaseNewData() throws DataAccessException;

	public List getRequestNewData() throws DataAccessException;

	public void updatePurchaseSelectStatus(PurchaseModel purchaseModel) throws DataAccessException;

	public void updateRequestSelectStatus(RequestModel requestModel) throws DataAccessException;

}