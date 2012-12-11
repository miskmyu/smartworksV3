/*	
 * $Id: SchedulerManagerImpl.java,v 1.1 2012/01/18 02:13:55 hsshin Exp $
 * created by    : SHIN HYUN SEONG
 * creation-date : 2011. 12. 23.
 * =========================================================
 * Copyright (c) 2011 ManinSoft, Inc. All rights reserved.
 */

package semiteq.scheduler.manager.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.resource.manager.IDocumentManager;
import net.smartworks.server.engine.resource.manager.SmartServerManager;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import semiteq.scheduler.dao.SchedulerDao;
import semiteq.scheduler.model.PurchaseModel;
import semiteq.scheduler.model.RequestModel;
import semiteq.util.LinkageUtil;

/**
 * @spring.bean id="schedulerManager" 
 * @spring.property name="schedulerDao" ref="schedulerDao"
 */
public class SchedulerManagerImpl extends QuartzJobBean {

	private SchedulerDao schedulerDao;

	public SchedulerDao getSchedulerDao() {
		return schedulerDao;
	}

	public void setSchedulerDao(SchedulerDao schedulerDao) {
		this.schedulerDao = schedulerDao;
	}

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {

		List purchaseModelList = schedulerDao.getPurchaseNewData();

		System.out.println("발주등록 스케쥴러 동작 시간 : " + new Date());
		System.out.println("가져온 데이터 사이즈 : " + purchaseModelList.size());
		LinkageUtil lisLinkageUtil = new LinkageUtil();
		IDocumentManager docMgr = SmartServerManager.getInstance().getDocumentManager();
		String groupId = "";
		if(purchaseModelList.size() > 0) {
			Iterator iter = purchaseModelList.iterator();
			while(iter.hasNext()) {
				PurchaseModel purchaseModel = (PurchaseModel)iter.next();
//				if(!CommonUtil.isEmpty(purchaseModel.getReqDocPath())) {
//					groupId = docMgr.createFileFromErp(purchaseModel.getReqDocPath());
//					purchaseModel.setGroupId(groupId);
//				}
				try {
					lisLinkageUtil.allocationFirstTask(purchaseModel);
					schedulerDao.updatePurchaseSelectStatus(purchaseModel);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		List requestModelList = schedulerDao.getRequestNewData();

		System.out.println("구매요청 스케쥴러 동작 시간 : " + new Date());
		System.out.println("가져온 데이터 사이즈 : " + requestModelList.size());

		if(requestModelList.size() > 0) {
			Iterator iter = requestModelList.iterator();
			while(iter.hasNext()) {
				RequestModel requestModel = (RequestModel)iter.next();
//				if(!CommonUtil.isEmpty(requestModel.getReqDocPath())) {
//					groupId = docMgr.createFileFromErp(requestModel.getReqDocPath());
//					requestModel.setGroupId(groupId);
//				}
				try {
					lisLinkageUtil.allocationFirstTask(requestModel);
					schedulerDao.updateRequestSelectStatus(requestModel);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

}