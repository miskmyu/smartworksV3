/*	
 * $Id$
 * created by    : SHIN HYUN SEONG
 * creation-date : 2011. 7. 15.
 * =========================================================
 * Copyright (c) 2011 ManinSoft, Inc. All rights reserved.
 */

package pro.ucity.util;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.smartworks.model.instance.Instance;
import net.smartworks.model.instance.ProcessWorkInstance;
import net.smartworks.model.instance.TaskInstance;
import net.smartworks.model.instance.info.InstanceInfoList;
import net.smartworks.model.instance.info.PWInstanceInfo;
import net.smartworks.model.instance.info.RequestParams;
import net.smartworks.model.instance.info.TaskInstanceInfo;
import net.smartworks.model.security.AccessPolicy;
import net.smartworks.model.work.FormField;
import net.smartworks.model.work.ProcessWork;
import net.smartworks.model.work.SmartForm;
import net.smartworks.model.work.SmartWork;
import net.smartworks.model.work.info.SmartFormInfo;
import net.smartworks.model.work.info.SmartTaskInfo;
import net.smartworks.server.engine.common.model.Property;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.engine.infowork.domain.model.SwdDataField;
import net.smartworks.server.engine.infowork.domain.model.SwdRecord;
import net.smartworks.server.service.IInstanceService;
import net.smartworks.server.service.IWorkService;
import net.smartworks.server.service.factory.SwServiceFactory;
import net.smartworks.service.ISmartWorks;
import net.smartworks.util.SmartUtil;

import org.apache.log4j.Logger;

import pro.ucity.manager.ucityWorkList.model.UcityWorkList;
import pro.ucity.manager.ucityWorkList.model.UcityWorkListCond;
import pro.ucity.model.Adapter;
import pro.ucity.model.CMHistory;
import pro.ucity.model.DMHistory;
import pro.ucity.model.Event;
import pro.ucity.model.ICSituation;
import pro.ucity.model.OPDisplay;
import pro.ucity.model.OPSituation;
import pro.ucity.model.OPSms;
import pro.ucity.model.Service;
import pro.ucity.model.System;
import pro.ucity.model.UcityConstant;

public class UcityUtil {
	
	private static Logger logger = Logger.getLogger(UcityUtil.class);

	public UcityUtil() {
		super();
	}

	//Excel Download 시 service,eventname,periodName,categoryName 변경
	public static String getServiceName(String serviceName) throws Exception{
		
		String[] allServices = Service.getAllServiceNames();
		if(serviceName.isEmpty()) return null;
		if(serviceName.equalsIgnoreCase(System.REPORT_OPTION_ALL_SERVICES)){
			serviceName = "모든 서비스";
		}else if(serviceName.equalsIgnoreCase(allServices[0])){
			serviceName = "환경";
		}else if(serviceName.equalsIgnoreCase(allServices[1])){
			serviceName = "교통";
		}else if(serviceName.equalsIgnoreCase(allServices[2])){
			serviceName = "방범/방재";
		}else if(serviceName.equalsIgnoreCase(allServices[3])){
			serviceName = "시설물관리";
		}else if(serviceName.equalsIgnoreCase(allServices[4])){
			serviceName = "플랫폼";
		}
		return serviceName;
	}
		
	public static String getEventName(String eventName, String serviceName) throws Exception{
		
		String[] allServices = Service.getAllServiceNames();
		String[] eventNames = Event.getAllEventNames(serviceName);
		
		if(eventName.isEmpty()) return null;
		if(serviceName.equalsIgnoreCase(allServices[0])){
			if(eventName.equalsIgnoreCase(eventNames[0])){
				eventName = "대기오염";
			}else if(eventName.equalsIgnoreCase(eventNames[1])){
				eventName = "환경경보";
			}else if(eventName.equalsIgnoreCase(eventNames[2])){
				eventName = "수질";
			}else if(eventName.equalsIgnoreCase(System.REPORT_OPTION_ALL_EVENTS)){
				eventName = "모든 이벤트";
			}		
		}else if(serviceName.equalsIgnoreCase(allServices[1])){
			if(eventName.equalsIgnoreCase(eventNames[0])){
				eventName = "교통혼잡";
			}else if(eventName.equalsIgnoreCase(eventNames[1])){
				eventName = "차량고장";
			}else if(eventName.equalsIgnoreCase(eventNames[2])){
				eventName = "뺑소니";
			}else if(eventName.equalsIgnoreCase(eventNames[3])){
				eventName = "교통사고";
			}else if(eventName.equalsIgnoreCase(System.REPORT_OPTION_ALL_EVENTS)){
				eventName = "모든 이벤트";
			}		
		}else if(serviceName.equalsIgnoreCase(allServices[2])){
			if(eventName.equalsIgnoreCase(eventNames[0])){
				eventName = "호우";
			}else if(eventName.equalsIgnoreCase(eventNames[1])){
				eventName = "지하차도침수";
			}else if(eventName.equalsIgnoreCase(eventNames[2])){
				eventName = "태풍";
			}else if(eventName.equalsIgnoreCase(eventNames[3])){
				eventName = "화재";
			}else if(eventName.equalsIgnoreCase(eventNames[4])){
				eventName = "수위경보";
			}else if(eventName.equalsIgnoreCase(eventNames[5])){
				eventName = "비상벨요청";
			}else if(eventName.equalsIgnoreCase(eventNames[6])){
				eventName = "용의차량추적";
			}else if(eventName.equalsIgnoreCase(eventNames[7])){
				eventName = "응급";
			}else if(eventName.equalsIgnoreCase(eventNames[8])){
				eventName = "미아";
			}else if(eventName.equalsIgnoreCase(eventNames[9])){
				eventName = "강도";
			}else if(eventName.equalsIgnoreCase(System.REPORT_OPTION_ALL_EVENTS)){
				eventName = "모든 이벤트";
			}	
		}else if(serviceName.equalsIgnoreCase(allServices[3])){
			if(eventName.equalsIgnoreCase(eventNames[0])){
				eventName = "상수도누수발생가능";
			}else if(eventName.equalsIgnoreCase(eventNames[1])){
				eventName = "시설물고장";
			}else if(eventName.equalsIgnoreCase(System.REPORT_OPTION_ALL_EVENTS)){
				eventName = "모든 이벤트";
			}	
		}else if(serviceName.equalsIgnoreCase(allServices[4])){
			if(eventName.equalsIgnoreCase(eventNames[0])){
				eventName = "도로통제";
			}else if(eventName.equalsIgnoreCase(System.REPORT_OPTION_ALL_EVENTS)){
				eventName = "모든 이벤트";
			}	
		}else{
			if(eventName.equalsIgnoreCase(System.REPORT_OPTION_ALL_EVENTS))
				eventName = "모든 이벤트";
		}
		return eventName;
	}

	public static String getCategoryName(String categoryName) throws Exception{
		
		if(categoryName.isEmpty()) return null;
		if(categoryName.equalsIgnoreCase(System.REPORT_OPTION_CATEGORY_BY_TIME)){
			categoryName = "시간대 별";
		}else if(categoryName.equalsIgnoreCase(System.REPORT_OPTION_CATEGORY_BY_AMPM)){
			categoryName = "오전/오후";
		}else if(categoryName.equalsIgnoreCase(System.REPORT_OPTION_CATEGORY_BY_DAY)){
			categoryName = "요일별";
		}else if(categoryName.equalsIgnoreCase(System.REPORT_OPTION_CATEGORY_BY_MONTH)){
			categoryName = "월별";
		}else if(categoryName.equalsIgnoreCase(System.REPORT_OPTION_CATEGORY_BY_SEASON)){
			categoryName = "계절별";
		}else if(categoryName.equalsIgnoreCase(System.REPORT_OPTION_CATEGORY_BY_QUARTER)){
			categoryName = "분기별";
		}else if(categoryName.equalsIgnoreCase(System.REPORT_OPTION_CATEGORY_BY_HALFYEAR)){
			categoryName = "반기별";
		}else{
			logger.error("CategoryName is Null");
		}
		return categoryName;
	}

	public static String getPeriodName(String periodName) throws Exception{
		
		if(periodName.isEmpty()) return null;
		if(periodName.equalsIgnoreCase(System.REPORT_OPTION_ALL_HISTORY)){
			periodName = "전체";
		}else if(periodName.equalsIgnoreCase(System.REPORT_OPTION_THIS_YEAR)){
			periodName = "올해";
		}else if(periodName.equalsIgnoreCase(System.REPORT_OPTION_RECENT_A_YEAR)){
			periodName = "최근 1년";
		}else if(periodName.equalsIgnoreCase(System.REPORT_OPTION_RECENT_THREE_YEARS)){
			periodName = "최근 3년";
		}else if(periodName.equalsIgnoreCase(System.REPORT_OPTION_RECENT_FIVE_YEARS)){
			periodName = "최근 5년";
		}else{
			logger.error("period null");
		}
		return periodName;
	}

	public static boolean isAbendable(ProcessWorkInstance instance){
		if(SmartUtil.isBlankObject(instance) || instance.getStatus() == Instance.STATUS_COMPLETED || instance.getStatus() == Instance.STATUS_ABORTED || SmartUtil.isBlankObject(instance.getTasks())) return false;
		TaskInstanceInfo[] tasks = instance.getTasks();
		
		int tasksLen = tasks.length;
		for(int i=0; i < tasksLen ; i++){
			TaskInstanceInfo task = tasks[i];
			// && task.getName().equals(System.TASK_NAME_USERVICE_END) 뺏음. 일단은 모두 표시
			if(task.getStatus() == Instance.STATUS_COMPLETED)
				return true;
		}
		return false;
	}
	
	public static String getServiceTypeName(String taskName){
		if(SmartUtil.isBlankObject(taskName)) return "";
		for(int i=0; i<System.getReleaseTaskNames().length; i++){
			if(taskName.equals(System.getReleaseTaskNames()[i]))
				return "종료";
		}
		return "발생";
	}
	
	public static String getDateString(String value){
		if(SmartUtil.isBlankObject(value) || value.length()<12) return "";
		if(value.length()>=14)
			return value.substring(0, 4) + "-" + value.substring(4, 6) + "-" + value.substring(6, 8) + " " +  value.substring(8, 10) + ":" +  value.substring(10, 12) + ":" +  value.substring(12, 14) + ".0";
		return value.substring(0, 4) + "-" + value.substring(4, 6) + "-" + value.substring(6, 8) + " " +  value.substring(8, 10) + ":" +  value.substring(10, 12) + ":" + "00.0";
	}
	
	public static FormField[] getSituationListFields() throws Exception{
		return new FormField[] { 
			new FormField(FormField.ID_STATUS, "상태", FormField.TYPE_COMBO_STATUS), 
			new FormField("serviceName", "U-서비스명", FIELD_TYPE_COMBO_U_SERVICE),
			new FormField("eventName", "이벤트명", FIELD_TYPE_COMBO_U_EVENT),
//			new FormField("ucity.type", "구분", FIELD_TYPE_COMBO_U_TYPE),
			new FormField(FormField.ID_LAST_TASK, "진행단계", FormField.TYPE_TEXT),
			new FormField("externalDisplay", "외부표출", FormField.TYPE_TEXT),
			new FormField("isSms", "SMS발송", FIELD_TYPE_COMBO_U_ISSMS),
			new FormField("eventTime", "발생일시", FormField.TYPE_DATE),
			new FormField("eventPlace", "발생장소", FormField.TYPE_TEXT)
		};
	}

	public static final String FIELD_TYPE_COMBO_U_SERVICE = "comboUService";
	public static final String FIELD_TYPE_COMBO_U_EVENT = "comboUEvent";
	public static final String FIELD_TYPE_COMBO_U_TYPE = "comboUType";
	public static final String FIELD_TYPE_COMBO_U_ISSMS = "comboUIssms";
	public static String getPageNameByField(FormField field) throws Exception{
		if(SmartUtil.isBlankObject(field)) return "";
		if(field.getType().equals(FormField.TYPE_COMBO_STATUS))
			return "combo_u_status_field";
		else if(field.getType().equals(FIELD_TYPE_COMBO_U_SERVICE))
			return "combo_u_service_field";
		else if(field.getType().equals(FIELD_TYPE_COMBO_U_EVENT))
			return "combo_u_event_field";
		else if(field.getType().equals(FIELD_TYPE_COMBO_U_TYPE))
			return "combo_u_type_field";
		else if(field.getType().equals(FIELD_TYPE_COMBO_U_ISSMS))
			return "combo_u_issms_field";
		return field.getPageName();
	}
	
	public static void endUService(ResultSet resultSet) throws Exception{
		if(SmartUtil.isBlankObject(resultSet)){
			throw new Exception("Invalid parameters exception !!!");
		}
		Adapter adapter = new Adapter(resultSet);
		if(!adapter.isValid() || adapter.getEventType() != Adapter.EVENT_TYPE_RELEASE){
			throw new Exception("Invalid result set exception !!!");
		}
		adapter.endProcess();
	}

	public static void startPortalProcess(String processId, String eventId, String eventTime, Map<String, Object> data) throws Exception{
		if(processId == null || data == null){
			throw new Exception("Invalid parameters exception !!!");
		}
		
		IWorkService workService = SwServiceFactory.getInstance().getWorkService();
		
		Map<String, Object> requestBody = new HashMap<String, Object>();
		
		
		ProcessWork work = (ProcessWork)workService.getWorkById(processId);
		if(SmartUtil.isBlankObject(work.getDiagram()) || SmartUtil.isBlankObject(work.getDiagram().getTasks())){
			throw new Exception("Invalid Process Diagram or Tasks exception !!!");			
		}
		
		SmartForm form = null;
		for(int i=0; i<work.getDiagram().getTasks().length; i++){
			SmartTaskInfo task = work.getDiagram().getTasks()[i];
			if(task.getName().equals(OPSituation.TASK_NAME_SITUATION_OCCURRENCE)){
				form = workService.getFormById(task.getForm().getId(), processId);
			}
		}
		if(SmartUtil.isBlankObject(form) || SmartUtil.isBlankObject(form.getFields())){
			throw new Exception("Invalid Form information exception !!!");
		}
		
		requestBody.put("workId", processId);
		requestBody.put("formId", form.getId());
		requestBody.put("formName", form.getName());
		requestBody.put("serviceName", data.get("serviceName"));
		requestBody.put("eventName", data.get("eventName"));
		if(!SmartUtil.isBlankObject(data.get("eventPlace")))
			requestBody.put("eventPlace", data.get("eventPlace"));
		requestBody.put("eventId", eventId);
		requestBody.put("eventTime", eventTime);
		requestBody.put("isSms", "false");
		
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
	
	public static void startUServiceProcess(String processId, String eventId, String eventTime, Map<String, Object> data) throws Exception{
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
		requestBody.put("eventId", eventId);
		requestBody.put("eventTime", eventTime);
		requestBody.put("isSms", "false");
		
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
	public static void startUServiceProcess(String processId, String eventId, String eventTime, Map<String, Object> data, String facilityId) throws Exception{
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
		requestBody.put("eventId", eventId);
		requestBody.put("eventTime", eventTime);
		requestBody.put("isSms", "false");
		requestBody.put("facilityId", facilityId);
		
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
	public static boolean ucityWorklistSearch(String processId,String eventId) throws Exception{
		
		IInstanceService instanceService = SwServiceFactory.getInstance().getInstanceService();
		
		InstanceInfoList instanceList = instanceService.getAllPWorkInstanceList(true, new RequestParams());
		if(SmartUtil.isBlankObject(instanceList))
			return true;
		
		PWInstanceInfo[] instances = (PWInstanceInfo[])instanceList.getInstanceDatas();
		for(int i=0; i<instances.length; i++){			
			ProcessWorkInstance processInstance = (ProcessWorkInstance)instanceService.getWorkInstanceById(SmartWork.TYPE_PROCESS, processId, instances[i].getId());
			if(SmartUtil.isBlankObject(processInstance) || SmartUtil.isBlankObject(processInstance.getTasks())) continue;
			UcityWorkListCond cond = new UcityWorkListCond();
			cond.setPrcInstId(processInstance.getId());
			UcityWorkList workList = SwManagerFactory.getInstance().getUcityWorkListManager().getUcityWorkList("", cond, null);
			if(workList != null){
				if(eventId.equals(workList.getEventId())){
					return false;
				}
			}
		}
		return true;
	}
	
	public static void endUServiceProcess(String processId, String eventId, Map<String, Object> data) throws Exception{
		if(processId == null || data == null){
			throw new Exception("Invalid parameters exception !!!");
		}
		
		IWorkService workService = SwServiceFactory.getInstance().getWorkService();
		IInstanceService instanceService = SwServiceFactory.getInstance().getInstanceService();
		
		ProcessWork work = (ProcessWork)workService.getWorkById(processId);
		if(SmartUtil.isBlankObject(work) || SmartUtil.isBlankObject(work.getDiagram()) || SmartUtil.isBlankObject(work.getDiagram().getTasks())){
			throw new Exception("Invalid Process Work exception !!!");
		}
		SmartForm endForm = null;
		for(int i=0; i<work.getDiagram().getTasks().length; i++){
			SmartFormInfo form = work.getDiagram().getTasks()[i].getForm();
			if(SmartUtil.isBlankObject(form)) continue;
			if(form.getName().equals(System.TASK_FORM_NAME_USERVICE_END)){
				endForm = workService.getFormById(form.getId(), processId);
				break;
			}
		}
		if(SmartUtil.isBlankObject(endForm)){
			throw new Exception("Invalid U Serive End Form exception !!!");			
		}
		
		InstanceInfoList instanceList = instanceService.getAllPWorkInstanceList(true, new RequestParams());
		if(SmartUtil.isBlankObject(instanceList) || SmartUtil.isBlankObject(instanceList.getInstanceDatas())){
			return;
		}
		
		ProcessWorkInstance endProcessInstance = null;
		//종료안된 process 중에 data 유무 파악
		boolean processRunningData = true;
		
		PWInstanceInfo[] instances = (PWInstanceInfo[])instanceList.getInstanceDatas();
		for(int i=0; i<instances.length; i++){			
			if(!SmartUtil.isBlankObject(processId) && !instances[i].getWorkId().equals(processId)) continue;
			ProcessWorkInstance processInstance = (ProcessWorkInstance)instanceService.getWorkInstanceById(SmartWork.TYPE_PROCESS, processId, instances[i].getId());
			if(SmartUtil.isBlankObject(processInstance) || SmartUtil.isBlankObject(processInstance.getTasks())) continue;
			UcityWorkListCond cond = new UcityWorkListCond();
			cond.setPrcInstId(processInstance.getId());
			UcityWorkList workList = SwManagerFactory.getInstance().getUcityWorkListManager().getUcityWorkList("", cond, null);
			if(eventId.equals(workList.getEventId())){
				endProcessInstance = processInstance;
				break;
			}
			logger.info("eventId : " + eventId);
			logger.info("workListEventId : " + workList.getEventId());
			processRunningData = false;
		}
		if(processRunningData == false){
			InstanceInfoList nInstanceList = instanceService.getAllPWorkInstanceList(false, new RequestParams());
			if(SmartUtil.isBlankObject(nInstanceList) || SmartUtil.isBlankObject(nInstanceList.getInstanceDatas())){
				return;
			}
			
			PWInstanceInfo[] nInstances = (PWInstanceInfo[])nInstanceList.getInstanceDatas();
			for(int i=0; i<nInstances.length; i++){			
				if(!SmartUtil.isBlankObject(processId) && !nInstances[i].getWorkId().equals(processId)) continue;
				ProcessWorkInstance processInstance = (ProcessWorkInstance)instanceService.getWorkInstanceById(SmartWork.TYPE_PROCESS, processId, nInstances[i].getId());
				if(SmartUtil.isBlankObject(processInstance) || SmartUtil.isBlankObject(processInstance.getTasks())) continue;
				UcityWorkListCond cond = new UcityWorkListCond();
				logger.info("processId : " + processInstance.getId());
				cond.setPrcInstId(processInstance.getId());
				UcityWorkList workList = SwManagerFactory.getInstance().getUcityWorkListManager().getUcityWorkList("", cond, null);
				if(eventId.equals(workList.getEventId())){
					endProcessInstance = processInstance;
					break;
				}
				logger.info("eventId : " + eventId);
				logger.info("workListEventId : " + workList.getEventId());
			}
		}
		
		if(SmartUtil.isBlankObject(endProcessInstance)){
			throw new Exception("Invalid Process Instance exception !!!");						
		}

		Map<String, Object> requestBody = new HashMap<String, Object>();		
		requestBody.put("workId", processId);
		requestBody.put("instanceId", endProcessInstance.getId());
		requestBody.put("formId", endForm.getId());
		requestBody.put("formName", endForm.getName());
		
		Map<String, Object> fieldData = new HashMap<String, Object>();
		for(int i=0; i<endForm.getFields().length; i++){
			FormField field = endForm.getFields()[i];
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
		
		String instanceId = SwServiceFactory.getInstance().getInstanceService().createTaskInstance(requestBody, null);
		UcityUtil.startPollingForRunningTasks(processId, instanceId);
	}
	public static void endUServiceProcessFacility(String processId, String facilityId, Map<String, Object> data) throws Exception{
		if(processId == null || data == null){
			throw new Exception("Invalid parameters exception !!!");
		}
		
		IWorkService workService = SwServiceFactory.getInstance().getWorkService();
		IInstanceService instanceService = SwServiceFactory.getInstance().getInstanceService();
		
		ProcessWork work = (ProcessWork)workService.getWorkById(processId);
		if(SmartUtil.isBlankObject(work) || SmartUtil.isBlankObject(work.getDiagram()) || SmartUtil.isBlankObject(work.getDiagram().getTasks())){
			throw new Exception("Invalid Process Work exception !!!");
		}
		SmartForm endForm = null;
		for(int i=0; i<work.getDiagram().getTasks().length; i++){
			SmartFormInfo form = work.getDiagram().getTasks()[i].getForm();
			if(SmartUtil.isBlankObject(form)) continue;
			if(form.getName().equals(System.TASK_FORM_NAME_USERVICE_END)){
				endForm = workService.getFormById(form.getId(), processId);
				break;
			}
		}
		if(SmartUtil.isBlankObject(endForm)){
			throw new Exception("Invalid U Serive End Form exception !!!");			
		}
		
		InstanceInfoList instanceList = instanceService.getAllPWorkInstanceList(true, new RequestParams());
		if(SmartUtil.isBlankObject(instanceList) || SmartUtil.isBlankObject(instanceList.getInstanceDatas())){
			return;
		}
		
		ProcessWorkInstance endProcessInstance = null;
		//종료안된 process 중에 data 유무 파악
		boolean processRunningData = false;
		
		PWInstanceInfo[] instances = (PWInstanceInfo[])instanceList.getInstanceDatas();
		for(int i=0; i<instances.length; i++){			
			if(!SmartUtil.isBlankObject(processId) && !instances[i].getWorkId().equals(processId)) continue;
			ProcessWorkInstance processInstance = (ProcessWorkInstance)instanceService.getWorkInstanceById(SmartWork.TYPE_PROCESS, processId, instances[i].getId());
			if(SmartUtil.isBlankObject(processInstance) || SmartUtil.isBlankObject(processInstance.getTasks())) continue;
			UcityWorkListCond cond = new UcityWorkListCond();
			cond.setPrcInstId(processInstance.getId());
			UcityWorkList workList = SwManagerFactory.getInstance().getUcityWorkListManager().getUcityWorkList("", cond, null);

			if(facilityId.equals(workList.getFacilityId())){
				endProcessInstance = processInstance;
				break;
			}
			logger.info("facilityId : " + facilityId);
			logger.info("workListFacilityId : " + workList.getFacilityId());

			processRunningData = true;
		}
		
		if(processRunningData == false){
			InstanceInfoList nInstanceList = instanceService.getAllPWorkInstanceList(false, new RequestParams());
			if(SmartUtil.isBlankObject(nInstanceList) || SmartUtil.isBlankObject(nInstanceList.getInstanceDatas())){
				return;
			}
			
			PWInstanceInfo[] nInstances = (PWInstanceInfo[])nInstanceList.getInstanceDatas();
			for(int i=0; i<nInstances.length; i++){			
				if(!SmartUtil.isBlankObject(processId) && !nInstances[i].getWorkId().equals(processId)) continue;
				ProcessWorkInstance processInstance = (ProcessWorkInstance)instanceService.getWorkInstanceById(SmartWork.TYPE_PROCESS, processId, nInstances[i].getId());
				if(SmartUtil.isBlankObject(processInstance) || SmartUtil.isBlankObject(processInstance.getTasks())) continue;
				UcityWorkListCond cond = new UcityWorkListCond();
				cond.setPrcInstId(processInstance.getId());
				UcityWorkList workList = SwManagerFactory.getInstance().getUcityWorkListManager().getUcityWorkList("", cond, null);
				if(facilityId.equals(workList.getFacilityId())){
					endProcessInstance = processInstance;
					break;
				}
			}						
		}
		if(SmartUtil.isBlankObject(endProcessInstance)){
			throw new Exception("Invalid Process Instance exception !!!");						
		}

		Map<String, Object> requestBody = new HashMap<String, Object>();		
		requestBody.put("workId", processId);
		requestBody.put("instanceId", endProcessInstance.getId());
		requestBody.put("formId", endForm.getId());
		requestBody.put("formName", endForm.getName());
		
		Map<String, Object> fieldData = new HashMap<String, Object>();
		for(int i=0; i<endForm.getFields().length; i++){
			FormField field = endForm.getFields()[i];
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
		
		String instanceId = SwServiceFactory.getInstance().getInstanceService().createTaskInstance(requestBody, null);
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
		if(!SmartUtil.isBlankObject(data.get("externalDisplay"))){
			Property[] extendedProperties = SwServiceFactory.getInstance().getInstanceService().getUcityExtendedPropertyByTaskInstId(taskInstance.getId());

			String externalDisplay = "";
			if(!SmartUtil.isBlankObject(extendedProperties)){
				for(int i=0; i<extendedProperties.length; i++){
					Property extendedProperty = extendedProperties[i];
					if(extendedProperty.getName().equals("externalDisplay")){
						externalDisplay = CommonUtil.toNotNull(extendedProperty.getValue());
						externalDisplay = SmartUtil.combineStrings(externalDisplay, (String)data.get("externalDisplay"));
					}
				}
			}
			
			requestBody.put("externalDisplay", externalDisplay);
		}
		if(!SmartUtil.isBlankObject(data.get("isSms")) && data.get("isSms") == "true"){
			requestBody.put("isSms", data.get("isSms"));
		}
		
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
	
	public static Map<String,Object> readTaskTable(String tableName, String eventId, String status, String deviceId) throws Exception{
		
		Map<String,Object> dataRecord = null;
		int tableId = System.getTableId(tableName);
		switch(tableId){
		case System.TABLE_ID_ADAPTER_HISTORY:
			dataRecord = Adapter.readHistoryTable(eventId, deviceId, status);
			break;
		case System.TABLE_ID_COMMID_TRACE:
			dataRecord = CMHistory.readHistoryTable(eventId, status);
			break;
		case System.TABLE_ID_OPPORTAL_SITUATION:
			dataRecord = OPSituation.readHistoryTable(eventId, status);
			break;
		case System.TABLE_ID_OPPORTAL_DISPLAY:
			dataRecord = OPDisplay.readHistoryTable(eventId, status);
			break;
		case System.TABLE_ID_OPPORTAL_SMS:
			dataRecord = OPSms.readHistoryTable(eventId, deviceId);
			break;
		case System.TABLE_ID_INTCON_SITUATION:
			dataRecord = ICSituation.readHistoryTable(eventId, status);
			break;
		case System.TABLE_ID_DEVMID_SEND_STATUS:
			dataRecord = DMHistory.readHistoryTable(eventId, status, deviceId);
			break;
		}
		return dataRecord;
	}
	synchronized public static void resumePollingForRunningTasks(String processId) throws Exception{
		
		IInstanceService instanceService = SwServiceFactory.getInstance().getInstanceService();
		IWorkService workService = SwServiceFactory.getInstance().getWorkService();
		//처음 was 올릴때 페이지를 20개가 아닌 200개를 읽음.
		RequestParams params = new RequestParams();
		params.setPageSize(500);
		
		InstanceInfoList instanceList = instanceService.getAllUcityPWorkInstanceList(true, params, -1);
		if(SmartUtil.isBlankObject(instanceList) || SmartUtil.isBlankObject(instanceList.getInstanceDatas())){
			return;
		}
		PWInstanceInfo[] instances = (PWInstanceInfo[])instanceList.getInstanceDatas();
		for(int i=0; i<instances.length; i++){			
//			if(!SmartUtil.isBlankObject(processId) && !instances[i].getWork().getId().equals(processId)) continue;
			if(!SmartUtil.isBlankObject(processId) && !instances[i].getWorkId().equals(processId)) continue;
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
				String displayId = null;
				String deviceId = null;
				String smsId = null;
				String timeout = null;
				for(int k=0; k<record.getDataFields().length; k++){
					SwdDataField dataField = record.getDataFields()[k];
					if(dataField.getName().equals(System.DATA_FIELD_NAME_EVENT_ID)){
						eventId = dataField.getValue();
					}else if(dataField.getName().equals(System.DATA_FIELD_NAME_TABLE_ID)){
						tableName = System.getTableName(dataField.getValue());
					}else if(dataField.getName().equals(System.DATA_FIELD_NAME_STATUS)){
						status = dataField.getValue();
					}else if(dataField.getName().equals(System.DATA_FIELD_NAME_DISPLAY_ID)){
						displayId = dataField.getValue();
					}else if(dataField.getName().equals(System.DATA_FIELD_NAME_DEVICE_ID)){
						deviceId = dataField.getValue();
					}else if(dataField.getName().equals(System.DATA_FIELD_NAME_SMS_ID)){
						smsId = dataField.getValue();
					}else if(dataField.getName().equals(System.DATA_FIELD_NAME_TIMEOUT)){
						timeout = dataField.getValue();
					}
				}
				if(!SmartUtil.isBlankObject(eventId) && !SmartUtil.isBlankObject(tableName)){
					try{
						UcityUtil.invokePollingForRunningTask(eventId, tableName, status, displayId, deviceId, smsId, timeout, taskInstance);
					}catch (Exception e){
						logger.error("UcityUtil : resumePollingForRunningTasks.617");
					}
				}else if(!SmartUtil.isBlankObject(eventId)){
					try{
						Map<String,Object> dataRecord = new HashMap<String,Object>();
						dataRecord.put(System.DATA_FIELD_NAME_EVENT_ID, eventId);
						UcityUtil.performUServiceTask(taskInstance, dataRecord);
					}catch (Exception e){
						logger.error("UcityUtil : resumePollingForRunningTasks.625");
					}
				}else{
					try{
						Map<String,Object> dataRecord = new HashMap<String,Object>();
						UcityUtil.performUServiceTask(taskInstance, dataRecord);
					}catch (Exception e){
						logger.error("UcityUtil : resumePollingForRunningTasks.632");
					}
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
			if(record != null){
				record = instanceService.refreshDataFields(record);
				if(SmartUtil.isBlankObject(record.getDataFields())) continue;
				String eventId = null;
				String tableName = null;
				String status = null;
				String displayId = null;
				String deviceId = null;
				String smsId = null;
				String timeout = null;
				for(int k=0; k<record.getDataFields().length; k++){
					SwdDataField dataField = record.getDataFields()[k];
					if(dataField.getName().equals(System.DATA_FIELD_NAME_EVENT_ID)){
						eventId = dataField.getValue();
					}else if(dataField.getName().equals(System.DATA_FIELD_NAME_TABLE_ID)){
						tableName = System.getTableName(dataField.getValue());
					}else if(dataField.getName().equals(System.DATA_FIELD_NAME_STATUS)){
						status = dataField.getValue();
					}else if(dataField.getName().equals(System.DATA_FIELD_NAME_DISPLAY_ID)){
						displayId = dataField.getValue();
					}else if(dataField.getName().equals(System.DATA_FIELD_NAME_DEVICE_ID)){
						deviceId = dataField.getValue();
					}else if(dataField.getName().equals(System.DATA_FIELD_NAME_SMS_ID)){
						smsId = dataField.getValue();
					}else if(dataField.getName().equals(System.DATA_FIELD_NAME_TIMEOUT)){
						timeout = dataField.getValue();
					}
				}
				if(!SmartUtil.isBlankObject(eventId) && !SmartUtil.isBlankObject(tableName)){
					try{
						UcityUtil.invokePollingForRunningTask(eventId, tableName, status, displayId, deviceId, smsId, timeout, taskInstance);
					}catch (Exception e){
						logger.error("UcityUtil : startPollingForRunningTasks.685");
					}
				}else if(!SmartUtil.isBlankObject(eventId)){
					try{
						Map<String,Object> dataRecord = new HashMap<String,Object>();
						dataRecord.put(System.DATA_FIELD_NAME_EVENT_ID, eventId);
						UcityUtil.performUServiceTask(taskInstance, dataRecord);
					}catch (Exception e){
						logger.error("UcityUtil : startPollingForRunningTasks.693");
					}
				}else{
					try{
						Map<String,Object> dataRecord = new HashMap<String,Object>();
						UcityUtil.performUServiceTask(taskInstance, dataRecord);
					}catch (Exception e){
						logger.error("UcityUtil : startPollingForRunningTasks.700");
					}
				}
			}
			Thread.sleep(100);
		}
	}

	static List<PollingModel> pollingQueue = new LinkedList<PollingModel>();
	synchronized static int addPolling(String eventId, String tableName, String status, String displayId, String deviceId, String smsId, long timeout, TaskInstance taskInstance){
		if(SmartUtil.isBlankObject(eventId) || SmartUtil.isBlankObject(taskInstance)){
			logger.info("EventId or TaskInstance does not exist Error!!!!, EventId=" + eventId);
			return -1;
		}
		if(SmartUtil.isBlankObject(pollingQueue)){
			pollingQueue.add(new PollingModel(eventId, tableName, status, displayId, deviceId, smsId, timeout, taskInstance));
			return 0;
		}
		
		for(int index=0; index<pollingQueue.size(); index++){
			PollingModel pollingModel = pollingQueue.get(index);
			if(pollingModel.getEventId().equals(eventId) && pollingModel.getTaskInstance().getId().equals(taskInstance.getId())){
				logger.info("PollingTask is already Running !!!!, Model Event Id=" + pollingModel.getEventId() + ", Task Name=" + taskInstance.getName() + ", Size=" + pollingQueue.size() +  ", Index=" + index);
				return -1;
			}
		}
		
		pollingQueue.add(new PollingModel(eventId, tableName, status, displayId, deviceId, smsId, timeout, taskInstance));
		return pollingQueue.size() -1;
	}
	synchronized static void addThreadToPolling(int index, Thread thread){
		if( index<0 || thread==null || !(index < pollingQueue.size())) return;
		
		PollingModel pollingModel = pollingQueue.get(index);
		pollingModel.setThread(thread);
		pollingQueue.set(index, pollingModel);
	}
	
	synchronized static PollingModel getPolling(Thread thread){
		if(thread==null || SmartUtil.isBlankObject(pollingQueue))
			return null;
		
		for(int index=0; index<pollingQueue.size(); index++){
			PollingModel pollingModel = pollingQueue.get(index);
			if(pollingModel.getThread() == thread){
				pollingQueue.remove(index);
				return pollingModel;
			}
		}
		return null;

	}
	
	static PollingModel getModel(Thread thread){
		if(thread==null || SmartUtil.isBlankObject(pollingQueue))
			return null;
		
		for(int index=0; index<pollingQueue.size(); index++){
			PollingModel pollingModel = pollingQueue.get(index);
			if(pollingModel.getThread() == thread)
				return pollingModel;
		}
		return null;

	}
	
	static boolean isPollingInterrupted(Thread thread){
		if(thread==null || SmartUtil.isBlankObject(pollingQueue))
			return false;
		
		for(int index=0; index<pollingQueue.size(); index++){
			PollingModel pollingModel = pollingQueue.get(index);
			if(pollingModel.getThread() == thread)
				return pollingModel.isInterrupted();
		}
		return false;
	}
	
	static void stopAllPollingsForEvent(String eventId) throws Exception{
		if(SmartUtil.isBlankObject(eventId) || SmartUtil.isBlankObject(UcityUtil.pollingQueue)) return;
		for(int i=0; i<pollingQueue.size(); i++){
			PollingModel pollingModel = pollingQueue.get(i);
			if(pollingModel.getEventId().equals(eventId)){
				pollingModel.setInterrupted(true);
			}
		}
		
	}
	
	public static void sendAbendMessage(String instanceId){
		if(SmartUtil.isBlankObject(instanceId)) return;
		
		InetAddress addr;
		String hostAddr = null;
		String serverIp = null;
		
		try {
			//자신의 host를 받아오며, ip주소의 마지막 2~3글자를 가져옴.
			addr = InetAddress.getLocalHost();
			hostAddr = addr.getHostAddress();
			String[] serverIps = hostAddr.split("\\.");
		    serverIp = serverIps[3];
		} catch (UnknownHostException e) {
			logger.info("Localhost Not Found");
		}		
		try{
			DatagramSocket ds = new DatagramSocket();
			byte[] buffer = instanceId.getBytes();
			String hostIpList = UcityConstant.getHostIpByKey("BPM.SERVERLIST"); // applicataionContext-ucityConstants.xml 에서 목록 받아옴.
			
			if( hostIpList != null && hostIpList.trim().length() != 0 ){
				String[] hostIpListArray = hostIpList.split(",");            // hostIpList 에서 , 로 구분 하여, array 변환.
				int hostIpLen = hostIpListArray.length;                      // array 갯수
				for( int i = 0 ; i < hostIpLen ; i++  ){                    
					String hostIp = hostIpListArray[i];
					String[] hostIps = hostIp.split("\\.");
					String ipEnd = hostIps[3]; 
					if(!serverIp.equalsIgnoreCase(ipEnd)){               // 자기 hostIp의 마지막 2~3글자와 비교하여, 틀릴 시만 send.
					    DatagramPacket dp = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(hostIp), UcityUtilServer.UDP_ABEND_PORT);
					    ds.send(dp);
					    ds.close();				
					}				
				}				
			}else{
				logger.info("hostIpList Not Found");
			}
		}catch (Exception e){
			logger.info("DatagramSocket Error",e);
		}
	}
	
	public static void stopAllPollingsForInstance(String instanceId) throws Exception{
		//소켓통신으로 자기 Host주소와 비교하여,다른 곳에다 쏨.
		sendAbendMessage(instanceId);
		if(SmartUtil.isBlankObject(instanceId) || UcityUtil.pollingQueue.isEmpty()){
//			UcityUtilSocket.UcitySocketIn(instanceId);
			return;
		}
		for(int i=0; i<pollingQueue.size(); i++){
			PollingModel pollingModel = pollingQueue.get(i);
			if(!SmartUtil.isBlankObject(pollingModel.getTaskInstance()) 
					&& !SmartUtil.isBlankObject(pollingModel.getTaskInstance().getWorkInstance()) 
					&& !SmartUtil.isBlankObject(pollingModel.getTaskInstance().getWorkInstance().getId()) 
					&& pollingModel.getTaskInstance().getWorkInstance().getId().equals(instanceId)){
				pollingModel.setInterrupted(true);
				logger.info("정상적으로 이상종료 처리 되었습니다.");
			}
		}
		
	}
	public static void stopAllPollingsForInstanceSocket(String instanceId) throws Exception{
		if(SmartUtil.isBlankObject(instanceId) || SmartUtil.isBlankObject(UcityUtil.pollingQueue)) return;
		for(int i=0; i<pollingQueue.size(); i++){
			PollingModel pollingModel = pollingQueue.get(i);
			if(!SmartUtil.isBlankObject(pollingModel.getTaskInstance()) 
					&& !SmartUtil.isBlankObject(pollingModel.getTaskInstance().getWorkInstance()) 
					&& !SmartUtil.isBlankObject(pollingModel.getTaskInstance().getWorkInstance().getId()) 
					&& pollingModel.getTaskInstance().getWorkInstance().getId().equals(instanceId)){
				pollingModel.setInterrupted(true);
				logger.info("정상적으로 이상종료 처리 되었습니다.");
			}
		}
		
	}

	
	public static void stopAllThread() throws Exception{
		if(SmartUtil.isBlankObject(UcityUtil.pollingQueue)) 
			return;
		for(int i=0; i<pollingQueue.size(); i++){
			PollingModel pollingModel = pollingQueue.get(i);
			pollingModel.getThread().stop();			
		}
		logger.info("Thread Exit");
	}
	synchronized public static void invokePollingForRunningTask(String eventId, String tableName, String status, String displayId, String deviceId, String smsId, String timeout, TaskInstance taskInstance) throws Exception{
		if(SmartUtil.isBlankObject(eventId) || SmartUtil.isBlankObject(taskInstance)) return;
		String tbName = UcityConstant.getQueryByKey("System.TABLE_NAME_COMMID_TRACE");
		long timeoutInMilliseconds =  System.DEFAULT_TASK_TIMEOUT;
		try{
			if(tableName.equalsIgnoreCase(tbName)){
				timeoutInMilliseconds = Integer.parseInt(timeout) * 6*1;
			}else{
				timeoutInMilliseconds = Integer.parseInt(timeout) * 60*1000;
			}
			
		}catch (Exception e){
			logger.error("UcityUtil : invokePollingForRunningTask.806");
		}
		
		int index = -1;
		if((index = addPolling(eventId, tableName, status, displayId, deviceId, smsId, timeoutInMilliseconds, taskInstance)) == -1){
			return;		
		}

		Thread pollingForRunningTask = new Thread(new Runnable() {
			public void run() {
				PollingModel thisModel = getModel(Thread.currentThread());
				String eventId = thisModel.getEventId();
				String tableName = thisModel.getTableName();
				String status = thisModel.getStatus();
				String displayId = thisModel.getDisplayId();
				String deviceId = thisModel.getDeviceId();
				String smsId = thisModel.getSmsId();
				TaskInstance taskInstance = thisModel.getTaskInstance();
				long timeout = thisModel.getTimeout();
				Map<String, Object> dataRecord = null;

				IInstanceService instanceService = SwServiceFactory.getInstance().getInstanceService();
				
					while(timeout > 0 && SmartUtil.isBlankObject(dataRecord) && !isPollingInterrupted(Thread.currentThread()) && !Thread.currentThread().isInterrupted()){
						try{
							logger.info("############ START checking Table=" + tableName + ", Event Id=" + eventId + ", Timeout=" + timeout + ", Task Name=" + taskInstance.getName() + " To Perform  ################");
							try {
								if(System.getTableId(tableName) == System.TABLE_ID_OPPORTAL_DISPLAY)
									dataRecord = UcityUtil.readTaskTable(tableName, eventId, displayId, deviceId);
								else if(System.getTableId(tableName) == System.TABLE_ID_OPPORTAL_SMS){
									dataRecord = UcityUtil.readTaskTable(tableName, eventId, displayId, smsId);
								}
								else
									dataRecord = UcityUtil.readTaskTable(tableName, eventId, status, deviceId);
							} catch(Exception e) {
								if(timeout<System.DEFAULT_POLLING_INTERVAL){
									timeout=0;
									break;
								}
								timeout = timeout - System.DEFAULT_POLLING_INTERVAL;
								dataRecord = null;
								try{
									logger.info("############ END checking Table=" + tableName + ", Event Id=" + eventId + ", Task Name=" + taskInstance.getName() + " To Perform  ################");
									Thread.sleep(System.DEFAULT_POLLING_INTERVAL);
									continue;
								}catch (Exception ex){
									logger.error("UcityUtil : invokePollingForRunningTask.Thread.852");
									timeout=0;
									break;
								}
							}
							if(SmartUtil.isBlankObject(dataRecord)){
								if(timeout<System.DEFAULT_POLLING_INTERVAL){
									timeout=0;
									break;
								}
								timeout = timeout - System.DEFAULT_POLLING_INTERVAL;
								try{
									logger.info("############ END checking Table=" + tableName + ", Event Id=" + eventId + ", Task Name=" + taskInstance.getName() + " To Perform  ################");
									Thread.sleep(System.DEFAULT_POLLING_INTERVAL);
									continue;
								}catch (Exception ex){
									logger.error("UcityUtil : invokePollingForRunningTask.Thread.868");
									timeout=0;
									break;
								}
							}else{
								taskInstance = (TaskInstance)instanceService.getTaskInstanceById(taskInstance.getWork().getId(), taskInstance.getId());
								if(!taskInstance.isRunning()) break;
								
								if(System.getTableId(tableName)==System.TABLE_ID_OPPORTAL_SITUATION && OPSituation.isDisplayableStatus(status)){
									if(OPDisplay.checkIfDisplay(eventId, false))
										dataRecord = OPDisplay.checkForDisplay(eventId, false, dataRecord);
									else if(OPDisplay.checkIfDisplay(eventId, true))
										dataRecord = OPDisplay.checkForDisplay(eventId, true, dataRecord);	
									if(OPSms.checkIfDisplay(eventId))
										dataRecord = OPSms.checkForDisplay(eventId, dataRecord);
								}
								try{
								logger.info("############ START Perform Event Id=" + eventId + ", Task Name=" + taskInstance.getName() + " ################");
								UcityUtil.performUServiceTask(taskInstance, dataRecord);
								logger.info("############ END Perform Event Id=" + eventId + ", Task Name=" + taskInstance.getName() + " ################");
								break;
								}catch (Exception e){
									logger.info("############ END(ERROR) Perform Event Id=" + eventId + ", Task Name=" + taskInstance.getName() + " ################");
									logger.error("UcityUtil : invokePollingForRunningTask.Thread.891");
									if(timeout<System.DEFAULT_POLLING_INTERVAL){
										timeout=0;
										break;
									}
									timeout = timeout - System.DEFAULT_POLLING_INTERVAL;
									dataRecord = null;
									try{
										logger.info("############ END checking Table=" + tableName + ", Event Id=" + eventId + ", Task Name=" + taskInstance.getName() + " To Perform  ################");
										Thread.sleep(System.DEFAULT_POLLING_INTERVAL);
										continue;
									}catch (Exception ex){
										logger.error("UcityUtil : invokePollingForRunningTask.Thread.903");
										timeout=0;
										break;
									}
								}
							}	
						}catch(InterruptedException e){
							break;
						}catch(Exception e){
							
						}
					}
					
					if(timeout==0 && SmartUtil.isBlankObject(dataRecord)){
						logger.info("############ END(TIMEOUT) checking Table=" + tableName + ", Event Id=" + eventId + ", Task Name=" + taskInstance.getName() + " To Perform  ################");
						try{
							UcityUtil.abendUServiceTask(taskInstance);
							UcityUtil.stopAllPollingsForEvent(eventId);
						}catch (Exception e){
							logger.error("UcityUtil : invokePollingForRunningTask.Thread.920");
						}
					}else if(isPollingInterrupted(Thread.currentThread())){
						logger.info("############ END(INTERRUPTED) checking Table=" + tableName + ", Event Id=" + eventId + ", Task Name=" + taskInstance.getName() + " To Perform  ################");
					}else{
						logger.info("############ END checking Table=" + tableName + ", Event Id=" + eventId + ", Task Name=" + taskInstance.getName() + " To Perform  ################");
					}			
					PollingModel pollingTask = getPolling(Thread.currentThread());
					logger.info("pollingTask = " + pollingTask);
			}
		});
		addThreadToPolling(index, pollingForRunningTask);
		pollingForRunningTask.start();
		Thread.sleep(1000);
	}
}

class PollingModel {
	PollingModel(String eventId, String tableName, String status, String displayId, String deviceId, String smsId, long timeout, TaskInstance taskInstance) {
		this.eventId = eventId;
		this.tableName = tableName;
		this.status = status;
		this.displayId = displayId;
		this.deviceId = deviceId;
		this.smsId = smsId;
		this.taskInstance = taskInstance;
		this.timeout = timeout;
	}
	
	protected boolean interrupted=false;
	protected Thread thread=null;
	protected String eventId = null;
	protected String tableName = null;
	protected String status = null;
	protected String displayId = null;
	protected String deviceId = null;
	protected String smsId = null;
	protected long timeout = System.DEFAULT_TASK_TIMEOUT;
	protected TaskInstance taskInstance = null;
	
	public boolean isInterrupted() {
		return interrupted;
	}
	public void setInterrupted(boolean interrupted) {
		this.interrupted = interrupted;
	}
	public Thread getThread() {
		return thread;
	}
	public void setThread(Thread thread) {
		this.thread = thread;
	}
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDisplayId() {
		return displayId;
	}
	public void setDisplayId(String displayId) {
		this.displayId = displayId;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getSmsId() {
		return smsId;
	}
	public void setSmsId(String smsId) {
		this.smsId = smsId;
	}
	public long getTimeout() {
		return timeout;
	}
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
	public TaskInstance getTaskInstance() {
		return taskInstance;
	}
	public void setTaskInstance(TaskInstance taskInstance) {
		this.taskInstance = taskInstance;
	}	
}

