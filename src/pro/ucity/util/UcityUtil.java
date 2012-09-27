/*	
 * $Id$
 * created by    : SHIN HYUN SEONG
 * creation-date : 2011. 7. 15.
 * =========================================================
 * Copyright (c) 2011 ManinSoft, Inc. All rights reserved.
 */

package pro.ucity.util;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import pro.ucity.model.Adapter;
import pro.ucity.model.CMHistory;
import pro.ucity.model.ICSituation;
import pro.ucity.model.OPDisplay;
import pro.ucity.model.OPSituation;
import pro.ucity.model.System;

import net.smartworks.model.instance.ProcessWorkInstance;
import net.smartworks.model.instance.TaskInstance;
import net.smartworks.model.instance.info.InstanceInfoList;
import net.smartworks.model.instance.info.PWInstanceInfo;
import net.smartworks.model.security.AccessPolicy;
import net.smartworks.model.work.FormField;
import net.smartworks.model.work.ProcessWork;
import net.smartworks.model.work.SmartForm;
import net.smartworks.model.work.SmartWork;
import net.smartworks.model.work.info.SmartTaskInfo;
import net.smartworks.server.engine.infowork.domain.model.SwdDataField;
import net.smartworks.server.engine.infowork.domain.model.SwdRecord;
import net.smartworks.server.service.IInstanceService;
import net.smartworks.server.service.IWorkService;
import net.smartworks.server.service.factory.SwServiceFactory;
import net.smartworks.service.ISmartWorks;
import net.smartworks.util.SmartUtil;

public class UcityUtil {
	
	public UcityUtil() {
		super();
	}
	
	public static void startUService(ResultSet resultSet) throws Exception{
		if(SmartUtil.isBlankObject(resultSet)){
			throw new Exception("Invalid parameters exception !!!");
		}
		Adapter adapter = new Adapter(resultSet);
		if(!adapter.isValid() || adapter.getEventType() != Adapter.EVENT_TYPE_OCCURRENCE){
			throw new Exception("Invalid result set exception !!!");
		}
		adapter.startProcess();				
	}

	public static void startPortalService(ResultSet resultSet, ResultSet joinResultSet) throws Exception{
		if(SmartUtil.isBlankObject(resultSet) || SmartUtil.isBlankObject(joinResultSet)){
			throw new Exception("Invalid parameters exception !!!");
		}
		OPSituation opSituation = new OPSituation(resultSet, joinResultSet);
		if(!opSituation.isValid() || !opSituation.getStatus().equals(OPSituation.STATUS_SITUATION_OCCURRED)){
			throw new Exception("Invalid result set exception !!!");					
		}
		if(!opSituation.getStatus().equals(OPSituation.STATUS_SITUATION_OCCURRED)){
			throw new Exception("Invalid situation status exception !!!");					
		}				
		opSituation.startProcess();
	}

	public static void startUServiceProcess(String processId, Map<String, Object> data) throws Exception{
		if(processId == null || data == null){
			throw new Exception("Invalid parameters exception !!!");
		}
		
		IWorkService workService = SwServiceFactory.getInstance().getWorkService();
		
		Map<String, Object> requestBody = new HashMap<String, Object>();
		
		
		ProcessWork work = (ProcessWork)workService.getWorkById(processId);
		SmartForm form = workService.getFormById(work.getDiagram().getStartTask().getForm().getId(), processId);
		if(SmartUtil.isBlankObject(form) || SmartUtil.isBlankObject(form.getFields())){
			throw new Exception("Invalid Form information exception !!!");
		}
		
		requestBody.put("workId", processId);
		requestBody.put("formId", form.getId());
		requestBody.put("formName", form.getName());
		requestBody.put("serviceName", data.get("serviceName"));
		requestBody.put("eventName", data.get("eventName"));
		requestBody.put("eventPlace", data.get("eventPlace"));
		
		Map<String, Object> fieldData = new HashMap<String, Object>();
		for(int i=0; i<form.getFields().length; i++){
			FormField field = form.getFields()[i];
			if(data.containsKey(field.getName())){
				fieldData.put(field.getId(), data.get(field.getName()));
			}
		}
		requestBody.put("frmSmartForm", fieldData);
		
		Map<String, Object> accessData = new HashMap<String, Object>();	
		accessData.put("selWorkSpace", SmartUtil.getSystemUser().getId());
		accessData.put("selWorkSpaceType", ISmartWorks.SPACE_TYPE_USER);
		accessData.put("selAccessLevel", AccessPolicy.LEVEL_PUBLIC);
		requestBody.put("frmAccessSpace", accessData);
		
		String instanceId = SwServiceFactory.getInstance().getInstanceService().startProcessWorkInstance(requestBody, null);
		UcityUtil.startPollingForRunningTasks(processId, instanceId);
	}
	
	public static void startUServiceProcess(String processId, String startTaskName, Map<String, Object> data) throws Exception{
		if(processId == null || startTaskName == null || data == null){
			throw new Exception("Invalid parameters exception !!!");
		}
		
		IWorkService workService = SwServiceFactory.getInstance().getWorkService();
		
		Map<String, Object> requestBody = new HashMap<String, Object>();
		
		
		ProcessWork work = (ProcessWork)workService.getWorkById(processId);
		
		SmartTaskInfo startTask = null;
		for(int i=0; i<work.getDiagram().getTasks().length; i++){
			SmartTaskInfo task = work.getDiagram().getTasks()[i];
			if(task.getName().equals(startTaskName)){
				startTask = task;
				break;
			}
		}		
		if(SmartUtil.isBlankObject(startTask)){
			throw new Exception("Invalid start task name exception !!!");			
		}
		SmartForm form = workService.getFormById(startTask.getForm().getId(), processId);
		if(SmartUtil.isBlankObject(form) || SmartUtil.isBlankObject(form.getFields())){
			throw new Exception("Invalid Form information exception !!!");
		}
		
		requestBody.put("workId", processId);
		requestBody.put("formId", form.getId());
		requestBody.put("formName", form.getName());
		
		Map<String, Object> fieldData = new HashMap<String, Object>();
		for(int i=0; i<form.getFields().length; i++){
			FormField field = form.getFields()[i];
			if(data.containsKey(field.getName())){
				fieldData.put(field.getId(), data.get(field.getName()));
			}
		}
		requestBody.put("frmSmartForm", fieldData);
		
		Map<String, Object> accessData = new HashMap<String, Object>();	
		accessData.put("selWorkSpace", SmartUtil.getSystemUser().getId());
		accessData.put("selWorkSpaceType", ISmartWorks.SPACE_TYPE_USER);
		accessData.put("selAccessLevel", AccessPolicy.LEVEL_PUBLIC);
		requestBody.put("frmAccessSpace", accessData);
		
		String instanceId = SwServiceFactory.getInstance().getInstanceService().startProcessWorkInstance(requestBody, null);
		UcityUtil.startPollingForRunningTasks(processId, instanceId);
	}
	
	public static void performUServiceTask(TaskInstance taskInstance, Map<String, Object> data) throws Exception{
		if(taskInstance == null || data == null){
			throw new Exception("Invalid parameters exception !!!");
		}

		Map<String, Object> requestBody = new HashMap<String, Object>();
		SmartForm form = taskInstance.getSmartForm();
		if(SmartUtil.isBlankObject(form) || SmartUtil.isBlankObject(form.getFields())){
			throw new Exception("Invalid Form information exception !!!");
		}
		
		requestBody.put("workId", taskInstance.getWork().getId());
		requestBody.put("instanceId", taskInstance.getWorkInstance().getId());
		requestBody.put("taskInstId", taskInstance.getId());
		requestBody.put("formId", form.getId());
		requestBody.put("formName", form.getName());
		
		Map<String, Object> fieldData = new HashMap<String, Object>();
		for(int i=0; i<form.getFields().length; i++){
			FormField field = form.getFields()[i];
			if(data.containsKey(field.getName())){
				fieldData.put(field.getId(), data.get(field.getName()));
			}
		}
		requestBody.put("frmSmartForm", fieldData);
		
		Map<String, Object> accessData = new HashMap<String, Object>();	
		accessData.put("selWorkSpace", SmartUtil.getSystemUser().getId());
		accessData.put("selWorkSpaceType", ISmartWorks.SPACE_TYPE_USER);
		accessData.put("selAccessLevel", AccessPolicy.LEVEL_PUBLIC);
		requestBody.put("frmAccessSpace", accessData);

		SwServiceFactory.getInstance().getInstanceService().performTaskInstance(requestBody, null);
		UcityUtil.startPollingForRunningTasks(taskInstance.getWork().getId(), taskInstance.getWorkInstance().getId());
	}
	
	public static void abendUServiceTask(TaskInstance taskInstance) throws Exception{
		if(taskInstance == null){
			throw new Exception("Invalid parameters exception !!!");
		}

		Map<String, Object> requestBody = new HashMap<String, Object>();
		
		requestBody.put("workId", taskInstance.getWork().getId());
		requestBody.put("instanceId", taskInstance.getWorkInstance().getId());
		requestBody.put("taskInstId", taskInstance.getId());
				
		Map<String, Object> accessData = new HashMap<String, Object>();	
		accessData.put("selWorkSpace", SmartUtil.getSystemUser().getId());
		accessData.put("selWorkSpaceType", ISmartWorks.SPACE_TYPE_USER);
		accessData.put("selAccessLevel", AccessPolicy.LEVEL_PUBLIC);
		requestBody.put("frmAccessSpace", accessData);

		SwServiceFactory.getInstance().getInstanceService().abendTaskInstance(requestBody, null);
	}
	
	public static ResultSet readTaskTable(String tableName, String eventId, String status) throws Exception{
		
		ResultSet resultSet = null;
		int tableId = System.getTableId(tableName);
		switch(tableId){
		case System.TABLE_ID_ADAPTER_HISTORY:
			resultSet = Adapter.readHistoryTable(eventId);
			break;
//		case System.TABLE_ID_COMMID_TRACE:
//			resultSet = CMHistory.readHistoryTable(eventId);
//			break;
//		case System.TABLE_ID_OPPORTAL_SITUATION:
//			resultSet = OPSituation.readHistoryTable(eventId, status);
//			break;
//		case System.TABLE_ID_OPPORTAL_DISPLAY:
//			resultSet = OPDisplay.readHistoryTable(eventId);
//			break;
//		case System.TABLE_ID_INTCON_SITUATION:
//			resultSet = ICSituation.readHistoryTable(eventId);
//			break;
		case System.TABLE_ID_DEVMID_DEVICE_STATUS:
			break;
		case System.TABLE_ID_DEVMID_SEND_STATUS:
			break;
		}
		return resultSet;
	}
	
	synchronized public static void resumePollingForRunningTasks(String processId) throws Exception{
		
		IInstanceService instanceService = SwServiceFactory.getInstance().getInstanceService();
		IWorkService workService = SwServiceFactory.getInstance().getWorkService();
		
		InstanceInfoList instanceList = instanceService.getAllPWorkInstanceList(true, null);
		if(SmartUtil.isBlankObject(instanceList) || SmartUtil.isBlankObject(instanceList.getInstanceDatas())){
			return;
		}
		PWInstanceInfo[] instances = (PWInstanceInfo[])instanceList.getInstanceDatas();
		for(int i=0; i<instances.length; i++){			
			if(!SmartUtil.isBlankObject(processId) && !instances[i].getWork().getId().equals(processId)) continue;
			ProcessWorkInstance processInstance = (ProcessWorkInstance)instanceService.getWorkInstanceById(SmartWork.TYPE_PROCESS, processId, instances[i].getId());
			if(SmartUtil.isBlankObject(processInstance) || SmartUtil.isBlankObject(processInstance.getTasks())) continue;
			for(int j=0; j<processInstance.getTasks().length; j++){
				if(!processInstance.getTasks()[j].isRunning()) continue;				
				TaskInstance taskInstance = (TaskInstance)instanceService.getTaskInstanceById(processInstance.getWork().getId(), processInstance.getTasks()[j].getId());
				if(SmartUtil.isBlankObject(taskInstance)) continue;
				SwdRecord record = workService.getRecord(processId, null, taskInstance.getId());
				record = instanceService.refreshDataFields(record);
				if(SmartUtil.isBlankObject(record.getDataFields())) continue;
				String eventId = null;
				String tableName = null;
				String status = null;
				String timeout = null;
				for(int k=0; k<record.getDataFields().length; k++){
					SwdDataField dataField = record.getDataFields()[k];
					if(dataField.getName().equals(System.DATA_FIELD_NAME_EVENT_ID)){
						eventId = dataField.getValue();
					}else if(dataField.getName().equals(System.DATA_FIELD_NAME_TABLE_ID)){
						tableName = System.getTableName(dataField.getValue());
					}else if(dataField.getName().equals(System.DATA_FIELD_NAME_STATUS)){
						status = dataField.getValue();
					}else if(dataField.getName().equals(System.DATA_FIELD_NAME_TIMEOUT)){
						timeout = dataField.getValue();
					}
				}
				if(!SmartUtil.isBlankObject(eventId) && !SmartUtil.isBlankObject(tableName)){
					UcityUtil.invokePollingForRunningTask(eventId, tableName, status, timeout, taskInstance);
				}
			}
			
		}	
	}
	
	synchronized public static void startPollingForRunningTasks(String processId, String instanceId) throws Exception{
		
		IInstanceService instanceService = SwServiceFactory.getInstance().getInstanceService();
		IWorkService workService = SwServiceFactory.getInstance().getWorkService();
		ProcessWorkInstance processInstance = (ProcessWorkInstance)instanceService.getWorkInstanceById(SmartWork.TYPE_PROCESS, processId, instanceId);
		if(SmartUtil.isBlankObject(processInstance) || SmartUtil.isBlankObject(processInstance.getTasks())){
			throw new Exception("Invalid Process Instance or No Running Tasks Error !!!");
		}
		for(int j=0; j<processInstance.getTasks().length; j++){
			if(!processInstance.getTasks()[j].isRunning()) continue;				
			TaskInstance taskInstance = (TaskInstance)instanceService.getTaskInstanceById(processInstance.getWork().getId(), processInstance.getTasks()[j].getId());
			if(SmartUtil.isBlankObject(taskInstance)) continue;
			SwdRecord record = workService.getRecord(processId, null, taskInstance.getId());
			record = instanceService.refreshDataFields(record);
			if(SmartUtil.isBlankObject(record.getDataFields())) continue;
			String eventId = null;
			String tableName = null;
			String status = null;
			String timeout = null;
			for(int k=0; k<record.getDataFields().length; k++){
				SwdDataField dataField = record.getDataFields()[k];
				if(dataField.getName().equals(System.DATA_FIELD_NAME_EVENT_ID)){
					eventId = dataField.getValue();
				}else if(dataField.getName().equals(System.DATA_FIELD_NAME_TABLE_ID)){
					tableName = System.getTableName(dataField.getValue());
				}else if(dataField.getName().equals(System.DATA_FIELD_NAME_STATUS)){
					status = dataField.getValue();
				}else if(dataField.getName().equals(System.DATA_FIELD_NAME_TIMEOUT)){
					timeout = dataField.getValue();
				}
			}
			if(!SmartUtil.isBlankObject(eventId) && !SmartUtil.isBlankObject(tableName)){
				try{
					UcityUtil.invokePollingForRunningTask(eventId, tableName, status, timeout, taskInstance);
				}catch (Exception e){
					throw e;
				}
			}
		}
	}
	
	public static String currentEventId = null;
	public static String currentTableName = null;
	public static String currentStatus = null;
	public static TaskInstance currentTaskInstance = null;
	public static long currentTimeout = System.DEFAULT_TASK_TIMEOUT;
	synchronized public static void invokePollingForRunningTask(String eventId, String tableName, String status, String timeout, TaskInstance taskInstance) throws Exception{
		if(SmartUtil.isBlankObject(eventId) || SmartUtil.isBlankObject(taskInstance)) return;
		
		long timeoutInMilliseconds =  System.DEFAULT_TASK_TIMEOUT;
		try{
			timeoutInMilliseconds = Integer.parseInt(timeout) * 60*1000;
		}catch (Exception e){}
		
		currentEventId = eventId;
		currentTableName = tableName;
		currentStatus = status;
		currentTaskInstance = taskInstance;
		currentTimeout = timeoutInMilliseconds;
		
		Thread pollingForRunningTask = new Thread(new Runnable() {
			public void run() {
				String eventId = currentEventId;
				String tableName = currentTableName;
				String status = currentStatus;
				TaskInstance taskInstance = currentTaskInstance;
				long timeout = currentTimeout;
				ResultSet result = null;
				while(timeout > 0 && result==null) {
					IInstanceService instanceService = SwServiceFactory.getInstance().getInstanceService();					
					try {
						taskInstance = (TaskInstance)instanceService.getTaskInstanceById(taskInstance.getWork().getId(), taskInstance.getId());
						if(!taskInstance.isRunning()) break;
						result = UcityUtil.readTaskTable(tableName, eventId, status);
					} catch(Exception e) {
						if(timeout<System.DEFAULT_POLLING_INTERVAL){
							timeout=0;
							break;
						}
						timeout = timeout - System.DEFAULT_POLLING_INTERVAL;
						try{
							Thread.sleep(System.DEFAULT_POLLING_INTERVAL);
						}catch (Exception ex){
							ex.printStackTrace();
							timeout=0;
							break;
						}
					}
					int tableId = System.getTableId(tableName);
					Map<String, Object> dataRecord = null;
					switch(tableId){
					case System.TABLE_ID_OPPORTAL_SITUATION:
						OPSituation opSituation = new OPSituation(result);
						dataRecord = opSituation.getDataRecord();
						break;
					case System.TABLE_ID_OPPORTAL_DISPLAY:
						OPDisplay opDisplay = new OPDisplay(result);
						dataRecord = opDisplay.getDataRecord();
						break;
					case System.TABLE_ID_INTCON_SITUATION:
						ICSituation icSituation = new ICSituation(result);
						dataRecord = icSituation.getDataRecord();
						break;
					case System.TABLE_ID_COMMID_TRACE:
						CMHistory cmHistory = new CMHistory(result);
						dataRecord = cmHistory.getDataRecord();
						break;
					case System.TABLE_ID_DEVMID_SEND_STATUS:
						break;
					case System.TABLE_ID_DEVMID_DEVICE_STATUS:
						break;
					case System.TABLE_ID_ADAPTER_HISTORY:
						Adapter adapter = new Adapter(result);
						dataRecord = adapter.getDataRecord();
						break;
					}
					if(SmartUtil.isBlankObject(dataRecord)){
						result = null;
						if(timeout<System.DEFAULT_POLLING_INTERVAL){
							timeout=0;
							break;
						}
						timeout = timeout - System.DEFAULT_POLLING_INTERVAL;
						try{
							Thread.sleep(System.DEFAULT_POLLING_INTERVAL);
						}catch (Exception ex){
							ex.printStackTrace();
							timeout=0;
							break;
						}
					}
					try{
						UcityUtil.performUServiceTask(taskInstance, dataRecord);
					}catch (Exception e){
						e.printStackTrace();
						if(timeout<System.DEFAULT_POLLING_INTERVAL){
							timeout=0;
							break;
						}
						timeout = timeout - System.DEFAULT_POLLING_INTERVAL;
						try{
							Thread.sleep(System.DEFAULT_POLLING_INTERVAL);
						}catch (Exception ex){
							ex.printStackTrace();
							timeout=0;
							break;
						}
					}
				}
				
				if(timeout==0 && SmartUtil.isBlankObject(result)){
					try{
						UcityUtil.abendUServiceTask(taskInstance);
					}catch (Exception e){
						e.printStackTrace();
					}
				}
			}
		});
		pollingForRunningTask.start();
		Thread.sleep(1000);
	}
}

