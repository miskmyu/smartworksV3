package net.smartworks.server.engine.process.task.manager.impl;

import net.smartworks.server.engine.common.manager.IManager;
import net.smartworks.server.engine.common.model.Order;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.common.util.DateUtil;
import net.smartworks.server.engine.common.util.MisUtil;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.engine.process.process.model.PrcProcessInst;
import net.smartworks.server.engine.process.task.manager.AbstractTskManagerAdvisor;
import net.smartworks.server.engine.process.task.model.TskTask;
import net.smartworks.server.engine.process.task.model.TskTaskCond;

import org.springframework.util.StringUtils;

import pro.ucity.manager.ucityWorkList.manager.IUcityWorkListManager;
import pro.ucity.manager.ucityWorkList.model.UcityWorkList;
import pro.ucity.manager.ucityWorkList.model.UcityWorkListCond;

public class TskManageUcityAdvisorImpl extends AbstractTskManagerAdvisor {

	public void preExecuteTask(String user, TskTask obj, String action) throws Exception {
	}
	public void postExecuteTask(String user, TskTask obj, String action) throws Exception {
		
		System.out.println("########## UCITY ADVISOR START #############");
		if (obj.getProcessInstId() != null) {
			
			String prcInstId = obj.getProcessInstId();
			String title = obj.getTitle();
			String taskDef = obj.getDef();
			if (CommonUtil.isEmpty(taskDef))
				throw new Exception("TaskDefinition is Empty!!!");
			String[] taskDefInfo = StringUtils.tokenizeToStringArray(taskDef, "|");
			String packageId = taskDefInfo[0];
			if (packageId.indexOf("md_") != -1)
				return;
			String status = CommonUtil.toDefault((String)MisUtil.processInstStatusMap().get("started"), "started");
			
			TskTaskCond taskCond = new TskTaskCond();
			taskCond.setStatus(TskTask.TASKSTATUS_ASSIGN);
			taskCond.setProcessInstId(prcInstId);
			taskCond.setOrders(new Order[]{new Order(TskTask.A_CREATIONDATE,false)});
			TskTask[] runningTasks = SwManagerFactory.getInstance().getTskManager().getTasks("UcityAdvisorImpl", taskCond, IManager.LEVEL_LITE);

			String runningTaskId = null;
			String runningTaskName = null;
			if (runningTasks != null && runningTasks.length != 0) {
				runningTaskId = runningTasks[0].getObjId();
				runningTaskName = runningTasks[0].getName();
			}
			

			//InstanceServiceImpl 의 setUcityExtendedProperty 메소드에서 값을 넘긴다
			String serviceName = obj.getExtendedAttributeValue("ucity_serviceName");
			String eventId = obj.getExtendedAttributeValue("ucity_eventId");
			String eventTime = obj.getExtendedAttributeValue("ucity_eventTime");
			String eventName = obj.getExtendedAttributeValue("ucity_eventName");
			String type = obj.getExtendedAttributeValue("ucity_type");
			String externalDisplay = obj.getExtendedAttributeValue("ucity_externalDisplay");
			String isSms = obj.getExtendedAttributeValue("ucity_isSms");
			String eventPlace = obj.getExtendedAttributeValue("ucity_eventPlace");
			
			UcityWorkListCond ucityCond = new UcityWorkListCond();
			ucityCond.setPrcInstId(prcInstId);
			ucityCond.setPackageId(packageId);
			
			IUcityWorkListManager ucityMgr = SwManagerFactory.getInstance().getUcityWorkListManager();
			UcityWorkList ucityWorkList = ucityMgr.getUcityWorkList(user, ucityCond, null);
			
			if (ucityWorkList == null) {
				//인스턴스 신규 생성
				ucityWorkList = new UcityWorkList();
			
				ucityWorkList.setPrcInstId(prcInstId);
				ucityWorkList.setPackageId(packageId);
				ucityWorkList.setStatus(status);
				ucityWorkList.setTitle(title);
				ucityWorkList.setRunningTaskId(runningTaskId);
				ucityWorkList.setRunningTaskName(runningTaskName);
				
				ucityWorkList.setServiceName(serviceName);
				ucityWorkList.setEventId(eventId);
				ucityWorkList.setEventTime(DateUtil.toDate(eventTime, "yyyyMMdd"));
				ucityWorkList.setEventName(eventName);
				ucityWorkList.setType(type);
				ucityWorkList.setExternalDisplay(externalDisplay);
				ucityWorkList.setSms(CommonUtil.toBoolean(isSms));
				ucityWorkList.setEventPlace(eventPlace);
				
				ucityWorkList.setCreationUser(user);
				
				System.out.println("INSERT UCITY WORKLIST TABLE");
				ucityMgr.setUcityWorkList(user, ucityWorkList, null);
			
			} else {
				//이미생성된 인스턴스라면 업데이트
				PrcProcessInst prcInst = getPrcManager().getProcessInst(user, prcInstId, IManager.LEVEL_LITE);
				ucityWorkList.setStatus(prcInst.getStatus());
				ucityWorkList.setRunningTaskId(runningTaskId);
				ucityWorkList.setRunningTaskName(runningTaskName);

				if (!CommonUtil.isEmpty(serviceName))
					ucityWorkList.setServiceName(serviceName);
				if (!CommonUtil.isEmpty(eventId))
					ucityWorkList.setEventId(eventId);
				if (!CommonUtil.isEmpty(eventTime))
					ucityWorkList.setEventTime(DateUtil.toDate(eventTime, "yyyyMMdd"));
				if (!CommonUtil.isEmpty(eventName))
					ucityWorkList.setEventName(eventName);
				if (!CommonUtil.isEmpty(type))
					ucityWorkList.setType(type);
				if (!CommonUtil.isEmpty(externalDisplay))
					ucityWorkList.setExternalDisplay(externalDisplay);
				if (!CommonUtil.isEmpty(isSms))
					ucityWorkList.setSms(CommonUtil.toBoolean(isSms));
				if (!CommonUtil.isEmpty(eventPlace))
					ucityWorkList.setEventPlace(eventPlace);

				System.out.println("UPDATE UCITY WORKLIST TABLE");
				ucityMgr.setUcityWorkList(user, ucityWorkList, null);
			}
		}
		System.out.println("########## UCITY ADVISOR END #############");
	}
	public void preSetTask(String user, TskTask obj, String level) throws Exception {
	}
	public void postSetTask(String user, TskTask obj, String level) throws Exception {
	}
}
