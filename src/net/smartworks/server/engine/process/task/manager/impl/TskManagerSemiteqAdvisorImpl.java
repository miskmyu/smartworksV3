package net.smartworks.server.engine.process.task.manager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.smartworks.server.engine.common.manager.IManager;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.engine.infowork.domain.model.SwdDataField;
import net.smartworks.server.engine.infowork.domain.model.SwdRecord;
import net.smartworks.server.engine.organization.model.SwoUser;
import net.smartworks.server.engine.organization.model.SwoUserCond;
import net.smartworks.server.engine.process.task.manager.AbstractTskManagerAdvisor;
import net.smartworks.server.engine.process.task.model.TskTask;
import net.smartworks.server.engine.process.task.model.TskTaskCond;

import org.springframework.util.StringUtils;

public class TskManagerSemiteqAdvisorImpl extends AbstractTskManagerAdvisor {

	public void preExecuteTask(String user, TskTask obj, String action) throws Exception {
		String type = obj.getType();
		if (!type.equalsIgnoreCase(TskTask.TASKTYPE_COMMON))
			return;
		
		//semiteq
		//String targetDefId = "pkg_f7b2074fbad042d19684951b23b17fc5|prc_8a4747ac45204658bd7f0889a14e3c58|2";
		//String targetFieldId = "3";
		
		//local
		String targetDefId = "pkg_17d5f9a3ea144811809b2eedc62bff23|prc_380b87b5db724e149a5c33fd7c2bbbaf|2";
		String targetFieldId = "1";

		String taskDefId = obj.getDef();
		String prcInstId = obj.getProcessInstId();
		if (CommonUtil.isEmpty(taskDefId))
			return;
		if (!taskDefId.equalsIgnoreCase(targetDefId))
			return;
		if (!CommonUtil.isEmpty(prcInstId)) {
			TskTaskCond taskCond = new TskTaskCond();
			taskCond.setProcessInstId(prcInstId);
			taskCond.setDef(taskDefId);
			long size = SwManagerFactory.getInstance().getTskManager().getTaskSize("", taskCond);
			if (size > 0)
				return;
		}
		String document = obj.getDocument();
		SwdRecord record = (SwdRecord)SwdRecord.toObject(document);
		if (CommonUtil.isEmpty(record)) {
			logger.warn("MultiInstanceStart ERROR!! : SEMITEQ ADVISOR - taskDefId : " + taskDefId + " (Empty Record)" );
			return;
		}
		SwdDataField dataField = record.getDataField(targetFieldId);
		if (CommonUtil.isEmpty(dataField)) {
			logger.warn("MultiInstanceStart ERROR!! : SEMITEQ ADVISOR - taskDefId : " + taskDefId + " (Empty dataField) , fieldId : " + targetFieldId );
			return;
		}
		String refRecordId = dataField.getRefRecordId();
		String value = dataField.getValue();
		if (CommonUtil.isEmpty(refRecordId)) {
			logger.warn("MultiInstanceStart ERROR!! : SEMITEQ ADVISOR - taskDefId : " + taskDefId + " (Empty dataFieldValue and refRecordIds) , fieldId : " + targetFieldId );
			return;
		}
		String[] targetAssignersArray = StringUtils.tokenizeToStringArray(refRecordId, ";");
		String[] targetAssignersNameArray = StringUtils.tokenizeToStringArray(value, ";");
		
		
		if (CommonUtil.isEmpty(targetAssignersArray)){
			logger.warn("MultiInstanceStart ERROR!! : SEMITEQ ADVISOR - taskDefId : " + taskDefId + " (Empty Target Assigners)");
			return;
		}
		
		Map assigneeMap = new HashMap();
		for (int i = 0; i < targetAssignersArray.length; i++) {
			assigneeMap.put(targetAssignersArray[i], targetAssignersNameArray[i]);
		}
		
		//부서코드가 들어왔을경우 부서사람들을 취합하여 다시 array를 만든다
		assigneeMap = applyDeptUserToArray(assigneeMap);
		
		if (assigneeMap.size() == 1)
			return;
		
		logger.info("MultiInstanceStart Start : " + obj.getTitle());
		//TskTask clonTask = (TskTask)obj.clone();
		
		//다수의 수행자중 첫번째 수행자만 빼고 나머지 수행자는 제거한다

		String tempAssigner = null;
		String tempValue = null;
		Iterator itr = assigneeMap.keySet().iterator();
		while (itr.hasNext()) {
			tempAssigner = (String)itr.next();
			tempValue = (String)assigneeMap.get(tempAssigner);
			assigneeMap.remove(tempAssigner);
			break;
		}
		dataField.setRefRecordId(tempAssigner);
		dataField.setValue(tempValue);
		record.setDataField(targetFieldId, dataField);
		obj.setDocument(record.toString());

//		refRecordId = StringUtils.replace(refRecordId, tempAssigner + ";", "");
//		value = StringUtils.replace(value, tempValue + ";", "");
//		targetAssignersArray = StringUtils.tokenizeToStringArray(refRecordId, ";");
//		targetAssignersNameArray = StringUtils.tokenizeToStringArray(value, ";");
		
		createClonProcessInstByAssigners(user, obj, assigneeMap, targetFieldId);
		
		logger.info("MultiInstanceStart End : taskDefId " + taskDefId);
	}
	private Map applyDeptUserToArray(Map assigneeMap) throws Exception {
		
		List deptIdList = new ArrayList();
		
		Iterator itr = assigneeMap.keySet().iterator();
		while (itr.hasNext()) {
			String userId = (String)itr.next();
			String assignerName = (String)assigneeMap.get(userId);
			
			if (userId.indexOf("dept_") != -1 || userId.indexOf("@") == -1) {
				deptIdList.add(userId);
			}
		}
		
		if (deptIdList.size() == 0) 
			return assigneeMap;
		
		for (int i = 0; i < deptIdList.size(); i++) {
			String deptId = (String)deptIdList.get(i);
			
			assigneeMap.remove(deptId);
			
			SwoUserCond userCond = new SwoUserCond();
			userCond.setDeptId(deptId);
			SwoUser[] users = SwManagerFactory.getInstance().getSwoManager().getUsers("", userCond, IManager.LEVEL_LITE);
			if (users == null || users.length == 0)
				continue;
			for (int j = 0; j < users.length; j++) {
				String id = users[j].getId();
				String name = users[j].getName();
				assigneeMap.put(id, name);
			}
		}
		return assigneeMap;
	}
	private void createClonProcessInstByAssigners(String userId, TskTask obj, Map assigneeMap, String targetFieldId) throws Exception {

		Iterator itr = assigneeMap.keySet().iterator();
		while (itr.hasNext()) {
			String assigner = (String)itr.next();
			String assignerName = (String)assigneeMap.get(assigner);
			
			TskTask clonTask = (TskTask)obj.clone();
			clonTask.setObjId(null);
			String document = clonTask.getDocument();
			SwdRecord record = (SwdRecord)SwdRecord.toObject(document);
			SwdDataField dataField = record.getDataField(targetFieldId);
			dataField.setRefRecordId(assigner);
			dataField.setValue(assignerName);
			record.setDataField(targetFieldId, dataField);
			clonTask.setDocument(record.toString());
			SwManagerFactory.getInstance().getTskManager().executeTask(userId, clonTask, "execute");
			logger.info("MultiInstance Assign To : " + assignerName + "(" + assigner +")");
		}
	}
	public void postExecuteTask(String user, TskTask obj, String action) throws Exception {
	}
	public void preSetTask(String user, TskTask obj, String level) throws Exception {
	}
	public void postSetTask(String user, TskTask obj, String level) throws Exception {
	}
}
