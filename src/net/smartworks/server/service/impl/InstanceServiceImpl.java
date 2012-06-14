package net.smartworks.server.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.smartworks.model.approval.Approval;
import net.smartworks.model.approval.ApprovalLine;
import net.smartworks.model.approval.ApprovalLineInst;
import net.smartworks.model.community.User;
import net.smartworks.model.community.info.CommunityInfo;
import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.community.info.WorkSpaceInfo;
import net.smartworks.model.filter.Condition;
import net.smartworks.model.filter.SearchFilter;
import net.smartworks.model.instance.AsyncMessageInstance;
import net.smartworks.model.instance.CommentInstance;
import net.smartworks.model.instance.FieldData;
import net.smartworks.model.instance.InformationWorkInstance;
import net.smartworks.model.instance.Instance;
import net.smartworks.model.instance.ProcessWorkInstance;
import net.smartworks.model.instance.RunningCounts;
import net.smartworks.model.instance.SortingField;
import net.smartworks.model.instance.WorkInstance;
import net.smartworks.model.instance.info.AsyncMessageInstanceInfo;
import net.smartworks.model.instance.info.AsyncMessageList;
import net.smartworks.model.instance.info.BoardInstanceInfo;
import net.smartworks.model.instance.info.ChatInstanceInfo;
import net.smartworks.model.instance.info.CommentInstanceInfo;
import net.smartworks.model.instance.info.EventInstanceInfo;
import net.smartworks.model.instance.info.IWInstanceInfo;
import net.smartworks.model.instance.info.ImageInstanceInfo;
import net.smartworks.model.instance.info.InstanceInfo;
import net.smartworks.model.instance.info.InstanceInfoList;
import net.smartworks.model.instance.info.MemoInstanceInfo;
import net.smartworks.model.instance.info.PWInstanceInfo;
import net.smartworks.model.instance.info.RequestParams;
import net.smartworks.model.instance.info.TaskInstanceInfo;
import net.smartworks.model.instance.info.WorkInstanceInfo;
import net.smartworks.model.notice.Notice;
import net.smartworks.model.work.FileCategory;
import net.smartworks.model.work.FormField;
import net.smartworks.model.work.SmartForm;
import net.smartworks.model.work.SmartWork;
import net.smartworks.model.work.SocialWork;
import net.smartworks.model.work.Work;
import net.smartworks.model.work.info.SmartWorkInfo;
import net.smartworks.model.work.info.WorkCategoryInfo;
import net.smartworks.model.work.info.WorkInfo;
import net.smartworks.server.engine.common.manager.IManager;
import net.smartworks.server.engine.common.model.Filter;
import net.smartworks.server.engine.common.model.Filters;
import net.smartworks.server.engine.common.model.MappingService;
import net.smartworks.server.engine.common.model.MisObjectCond;
import net.smartworks.server.engine.common.model.Order;
import net.smartworks.server.engine.common.model.Property;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.common.util.DateUtil;
import net.smartworks.server.engine.common.util.StringUtil;
import net.smartworks.server.engine.common.util.WebServiceUtil;
import net.smartworks.server.engine.config.manager.ISwcManager;
import net.smartworks.server.engine.config.model.SwcWebService;
import net.smartworks.server.engine.config.model.SwcWebServiceParameter;
import net.smartworks.server.engine.config.model.SwcWorkHour;
import net.smartworks.server.engine.config.model.SwcWorkHourCond;
import net.smartworks.server.engine.docfile.exception.DocFileException;
import net.smartworks.server.engine.docfile.manager.IDocFileManager;
import net.smartworks.server.engine.docfile.model.FileWork;
import net.smartworks.server.engine.docfile.model.FileWorkCond;
import net.smartworks.server.engine.docfile.model.IFileModel;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.engine.infowork.domain.manager.ISwdManager;
import net.smartworks.server.engine.infowork.domain.model.SwdDataField;
import net.smartworks.server.engine.infowork.domain.model.SwdDomain;
import net.smartworks.server.engine.infowork.domain.model.SwdDomainCond;
import net.smartworks.server.engine.infowork.domain.model.SwdField;
import net.smartworks.server.engine.infowork.domain.model.SwdFieldCond;
import net.smartworks.server.engine.infowork.domain.model.SwdRecord;
import net.smartworks.server.engine.infowork.domain.model.SwdRecordCond;
import net.smartworks.server.engine.infowork.domain.model.SwdRecordExtend;
import net.smartworks.server.engine.infowork.form.manager.ISwfManager;
import net.smartworks.server.engine.infowork.form.model.SwfCondition;
import net.smartworks.server.engine.infowork.form.model.SwfConditions;
import net.smartworks.server.engine.infowork.form.model.SwfField;
import net.smartworks.server.engine.infowork.form.model.SwfFieldMapping;
import net.smartworks.server.engine.infowork.form.model.SwfFieldRef;
import net.smartworks.server.engine.infowork.form.model.SwfForm;
import net.smartworks.server.engine.infowork.form.model.SwfFormCond;
import net.smartworks.server.engine.infowork.form.model.SwfFormLink;
import net.smartworks.server.engine.infowork.form.model.SwfFormModel;
import net.smartworks.server.engine.infowork.form.model.SwfFormRef;
import net.smartworks.server.engine.infowork.form.model.SwfFormat;
import net.smartworks.server.engine.infowork.form.model.SwfMapping;
import net.smartworks.server.engine.infowork.form.model.SwfMappings;
import net.smartworks.server.engine.infowork.form.model.SwfOperand;
import net.smartworks.server.engine.like.manager.ILikeManager;
import net.smartworks.server.engine.like.model.Like;
import net.smartworks.server.engine.like.model.LikeCond;
import net.smartworks.server.engine.message.manager.IMessageManager;
import net.smartworks.server.engine.message.model.Message;
import net.smartworks.server.engine.message.model.MessageCond;
import net.smartworks.server.engine.opinion.manager.IOpinionManager;
import net.smartworks.server.engine.opinion.model.Opinion;
import net.smartworks.server.engine.opinion.model.OpinionCond;
import net.smartworks.server.engine.organization.manager.ISwoManager;
import net.smartworks.server.engine.organization.model.SwoDepartment;
import net.smartworks.server.engine.organization.model.SwoDepartmentCond;
import net.smartworks.server.engine.organization.model.SwoUser;
import net.smartworks.server.engine.organization.model.SwoUserCond;
import net.smartworks.server.engine.pkg.manager.IPkgManager;
import net.smartworks.server.engine.pkg.model.PkgPackage;
import net.smartworks.server.engine.pkg.model.PkgPackageCond;
import net.smartworks.server.engine.process.approval.manager.IAprManager;
import net.smartworks.server.engine.process.approval.model.AprApproval;
import net.smartworks.server.engine.process.approval.model.AprApprovalDef;
import net.smartworks.server.engine.process.approval.model.AprApprovalLine;
import net.smartworks.server.engine.process.approval.model.AprApprovalLineDef;
import net.smartworks.server.engine.process.deploy.model.AcpActualParameter;
import net.smartworks.server.engine.process.process.exception.PrcException;
import net.smartworks.server.engine.process.process.manager.IPrcManager;
import net.smartworks.server.engine.process.process.model.PrcProcess;
import net.smartworks.server.engine.process.process.model.PrcProcessCond;
import net.smartworks.server.engine.process.process.model.PrcProcessInst;
import net.smartworks.server.engine.process.process.model.PrcProcessInstCond;
import net.smartworks.server.engine.process.process.model.PrcProcessInstExtend;
import net.smartworks.server.engine.process.task.manager.ITskManager;
import net.smartworks.server.engine.process.task.model.TskTask;
import net.smartworks.server.engine.process.task.model.TskTaskCond;
import net.smartworks.server.engine.process.task.model.TskTaskDef;
import net.smartworks.server.engine.process.task.model.TskTaskDefCond;
import net.smartworks.server.engine.publishnotice.manager.IPublishNoticeManager;
import net.smartworks.server.engine.publishnotice.model.PublishNotice;
import net.smartworks.server.engine.publishnotice.model.PublishNoticeCond;
import net.smartworks.server.engine.worklist.manager.IWorkListManager;
import net.smartworks.server.engine.worklist.model.TaskWork;
import net.smartworks.server.engine.worklist.model.TaskWorkCond;
import net.smartworks.server.service.ICommunityService;
import net.smartworks.server.service.IInstanceService;
import net.smartworks.server.service.ISeraService;
import net.smartworks.server.service.util.ModelConverter;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartMessage;
import net.smartworks.util.SmartTest;
import net.smartworks.util.SmartUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class InstanceServiceImpl implements IInstanceService {
	protected final Log logger = LogFactory.getLog(getClass());

	private static ITskManager getTskManager() {
		return SwManagerFactory.getInstance().getTskManager();
	}
	private static IPrcManager getPrcManager() {
		return SwManagerFactory.getInstance().getPrcManager();
	}
	private static IPkgManager getPkgManager() {
		return SwManagerFactory.getInstance().getPkgManager();
	}
	private static ISwdManager getSwdManager() {
		return SwManagerFactory.getInstance().getSwdManager();
	}
	private static ISwfManager getSwfManager() {
		return SwManagerFactory.getInstance().getSwfManager();
	}
	private static IDocFileManager getDocManager() {
		return SwManagerFactory.getInstance().getDocManager();
	}
	private static ISwoManager getSwoManager() {
		return SwManagerFactory.getInstance().getSwoManager();
	}
	private static ISwcManager getSwcManager() {
		return SwManagerFactory.getInstance().getSwcManager();
	}
	private static IOpinionManager getOpinionManager() {
		return SwManagerFactory.getInstance().getOpinionManager();
	}
	private static IWorkListManager getWorkListManager() {
		return SwManagerFactory.getInstance().getWorkListManager();
	}
	private static IMessageManager getMessageManager() {
		return SwManagerFactory.getInstance().getMessageManager();
	}
	private static IPublishNoticeManager getPublishNoticeManager() {
		return SwManagerFactory.getInstance().getPublishNoticeManager();
	}
	private static ILikeManager getLikeManager() {
		return SwManagerFactory.getInstance().getLikeManager();
	}
	private static IAprManager getAprManager() {
		return SwManagerFactory.getInstance().getAprManager();
	}

	@Autowired
	private ICommunityService communityService;
	@Autowired
	private ISeraService seraService;

	public BoardInstanceInfo[] getBoardInstancesByWorkSpaceId(String spaceId) throws Exception {

		try {
			String workId = SmartWork.ID_BOARD_MANAGEMENT;

			User user = SmartUtil.getCurrentUser();
			String userId = user.getId();
			String companyId = user.getCompanyId();

			SwdRecordCond swdRecordCond = new SwdRecordCond();
			swdRecordCond.setCompanyId(companyId);
			String domainId = "frm_notice_SYSTEM";
			swdRecordCond.setDomainId(domainId);
			String[] workSpaceIdIns = null;
			if(spaceId == null)
				workSpaceIdIns = ModelConverter.getWorkSpaceIdIns(user);
			swdRecordCond.setWorkSpaceId(spaceId);
			swdRecordCond.setWorkSpaceIdIns(workSpaceIdIns);

			swdRecordCond.setPageNo(0);
			swdRecordCond.setPageSize(5);
			swdRecordCond.setOrders(new Order[]{new Order(FormField.ID_CREATED_DATE, false)});

			SwdRecord[] swdRecords = getSwdManager().getRecords(userId, swdRecordCond, IManager.LEVEL_LITE);

			List<BoardInstanceInfo> boardInstanceInfoList = new ArrayList<BoardInstanceInfo>();
			BoardInstanceInfo[] boardInstanceInfos = null;

			if(!CommonUtil.isEmpty(swdRecords)) {
				int swdRecordLength = swdRecords.length;
				for(int i=0; i < swdRecordLength; i++) {
					SwdRecord swdRecord = swdRecords[i];
					BoardInstanceInfo boardInstanceInfo = new BoardInstanceInfo();
					boardInstanceInfo.setId(swdRecord.getRecordId());
					boardInstanceInfo.setOwner(ModelConverter.getUserInfoByUserId(swdRecord.getCreationUser()));
					boardInstanceInfo.setCreatedDate(new LocalDate((swdRecord.getCreationDate()).getTime()));
					int type = WorkInstance.TYPE_INFORMATION;
					boardInstanceInfo.setType(type);
					boardInstanceInfo.setStatus(WorkInstance.STATUS_COMPLETED);
					String workSpaceId = swdRecord.getWorkSpaceId();
					if(workSpaceId == null)
						workSpaceId = user.getId();

					WorkInfo workInfo = new WorkInfo(workId, null, SocialWork.TYPE_BOARD);

					boardInstanceInfo.setWork(workInfo);
					boardInstanceInfo.setLastModifier(ModelConverter.getUserInfoByUserId(swdRecord.getModificationUser()));
					boardInstanceInfo.setLastModifiedDate(new LocalDate((swdRecord.getModificationDate()).getTime()));

					SwdDataField[] swdDataFields = swdRecord.getDataFields();
					if(!CommonUtil.isEmpty(swdDataFields)) {
						int swdDataFieldsLength = swdDataFields.length;
						for(int j=0; j<swdDataFieldsLength; j++) {
							SwdDataField swdDataField = swdDataFields[j];
							String value = swdDataField.getValue();
							if(swdDataField.getId().equals("0")) {
								boardInstanceInfo.setSubject(StringUtil.subString(value, 0, 24, "..."));
							} else if(swdDataField.getId().equals("1")) {
								boardInstanceInfo.setBriefContent(StringUtil.subString(value, 0, 40, "..."));
							}
						}
					}
					boardInstanceInfoList.add(boardInstanceInfo);
				}
			}
			if(boardInstanceInfoList.size() > 0) {
				boardInstanceInfos = new BoardInstanceInfo[boardInstanceInfoList.size()];
				boardInstanceInfoList.toArray(boardInstanceInfos);
			}
			return boardInstanceInfos;
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public BoardInstanceInfo[] getMyRecentBoardInstances() throws Exception {
		return getBoardInstancesByWorkSpaceId(null);
	}

	@Override
	public BoardInstanceInfo[] getCommunityRecentBoardInstances(String spaceId) throws Exception {
		return getBoardInstancesByWorkSpaceId(spaceId);
	}

	@Override
	public InstanceInfo[] getMyRecentInstances() throws Exception {
		try{
			User cuser = SmartUtil.getCurrentUser();
			String userId = null;
			if (cuser != null)
				userId = cuser.getId();
			
			TaskWorkCond cond = new TaskWorkCond();
			cond.setTskAssignee(userId);
			cond.setTskStatus(TskTask.TASKSTATUS_COMPLETE);
			cond.setOrders(new Order[]{new Order("taskLastModifyDate", false)});
			cond.setPageNo(0);
			cond.setPageSize(10);
			TaskWork[] tasks = getWorkListManager().getTaskWorkList(userId, cond);
			if (tasks == null || tasks.length == 0)
				return null;
			
			List<InstanceInfo> instanceInfoList = new ArrayList<InstanceInfo>();
			List<String> prcInstIdList = new ArrayList<String>();
			int taskLength = tasks.length;
			for (int i = 0; i < taskLength; i++) {
				TaskWork task = tasks[i];
				/*if (instanceInfoList.size() == 10)
					break;*/
				if (prcInstIdList.contains(task.getTskPrcInstId()))
					continue;
				prcInstIdList.add(task.getTskPrcInstId());
				instanceInfoList.add(ModelConverter.getWorkInstanceInfoByTaskWork(task));
			}
			InstanceInfo[] resultTasks = new InstanceInfo[instanceInfoList.size()];
			instanceInfoList.toArray(resultTasks);
			
			return resultTasks;
			
//			User user = SmartUtil.getCurrentUser();
//			if (CommonUtil.isEmpty(user.getCompanyId()) || CommonUtil.isEmpty(user.getId()))
//				return null;
//	
//			TskTaskCond taskCond = new TskTaskCond();
//			taskCond.setAssignee(user.getId());
//			taskCond.setStatus(TskTask.TASKSTATUS_COMPLETE);
//			taskCond.setTypeNotIns(TskTask.NOTUSERTASKTYPES);
//			taskCond.setOrders(new Order[]{new Order("executionDate" , false)});
//			taskCond.setPageNo(0);
//			taskCond.setPageSize(50);
//			
//			TskTask[] tasks = getTskManager().getTasks(user.getId(), taskCond, IManager.LEVEL_LITE);
//			if (CommonUtil.isEmpty(tasks))
//				return null;
//		
//			List<String> prcInstIdList = new ArrayList<String>();
//			for (int i = 0; i < tasks.length; i++) {
//				TskTask task = tasks[i];
//				if (prcInstIdList.size() == 10)
//					break;
//				if (prcInstIdList.contains(task.getProcessInstId()))
//					continue;
//				prcInstIdList.add(task.getProcessInstId());
//			}
//			
//			String[] prcInstIdArray = new String[prcInstIdList.size()];
//			
//			prcInstIdList.toArray(prcInstIdArray);
//			
//			PrcProcessInstCond prcInstCond = new PrcProcessInstCond();
//			
//			prcInstCond.setCompanyId(user.getCompanyId());
//			prcInstCond.setObjIdIns(prcInstIdArray);
//			prcInstCond.setOrders(new Order[]{new Order("creationDate" , false)});
//			
//			PrcProcessInst[] prcInsts = getPrcManager().getProcessInsts(user.getId(), prcInstCond, IManager.LEVEL_LITE);
//			
//			InstanceInfo[] instInfo = ModelConverter.getInstanceInfoArrayByPrcInstArray(prcInsts);
//			
//			return instInfo;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required
		}
	}

	@Override
	public Instance getInstanceById(String instanceId) throws Exception {
		try{
			return SmartTest.getInstanceById(instanceId);
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required
		}
	}

	public InstanceInfo[] getMyRunningInstances(LocalDate lastInstanceDate, int requestSize, boolean assignedOnly, RequestParams params) throws Exception {
		
		try{
			//정보관리업무에서 파생된 업무는 IWInstanceInfo
			//프로세스 태스크및 프로세스에서 파생된 업무는 PWInstanceInfo
			
			User user = SmartUtil.getCurrentUser();
			if (CommonUtil.isEmpty(user.getCompanyId()) || CommonUtil.isEmpty(user.getId()))
				return null;
	
			TaskWorkCond taskCond = new TaskWorkCond();
			if (assignedOnly) {
				taskCond.setTskStatus(TskTask.TASKSTATUS_ASSIGN);
				taskCond.setTskAssignee(user.getId());
			} else {
				taskCond.setTskStartOrAssigned(user.getId());
			}
			if (lastInstanceDate != null) {
				taskCond.setLastInstanceDate(lastInstanceDate);
			} else {
				taskCond.setLastInstanceDate(new LocalDate());
			}
			if (params != null) {
				if (!CommonUtil.isEmpty(params.getSearchKey())) {
					taskCond.setSearchKey(params.getSearchKey());
				}
			}
			taskCond.setPageNo(0);
			taskCond.setPageSize(requestSize);
			taskCond.setPrcStatus(PrcProcessInst.PROCESSINSTSTATUS_RUNNING);
			
			taskCond.setOrders(new Order[]{new Order("tskCreatedate", false)});
			
			TaskWork[] tasks = getWorkListManager().getTaskWorkList(user.getId(), taskCond);
			
			if(tasks != null) return ModelConverter.getInstanceInfoArrayByTaskWorkArray(user.getId(), tasks);
			return null;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
		
	}
	/*
	 * 
	 * 현재사용자의 진행중인 업무, 즉 현재사용자에게 할당된 태스크들과 현재사용자가 시작한 업무중 진행중인 업무를 가져다 주는 서비스로,
	 * 인스턴스들중에서 lastModifiedDate가 lastInstacneDate보다 이전것들을 requestSize만큼 sorting 순서를 적용하여 가져다 준다
	 * 
	 * lastInstanceDate : input
	 * 		가져올 인스턴스들의 기준 시점. lastInstanceDate가 null 이면 현재시간을 기준으로 가져다 준다.
	 * 
	 * requestSize : input
	 * 		가져올 인스턴스 갯수
	 * assignedOnly : input
	 * 		가져올 인스턴스 종류.
	 * 		true : 현재사용자의 진행중인 업무중에서 할당된 업무들만 가져온다.
	 * 		false : 현재사용자의 모든 진행중인 업무들을 가져온다..
	 * 
	 * InstanceInfo[] : return
	 * 	
	 */
	public InstanceInfo[] getMyRunningInstances(LocalDate lastInstanceDate, int requestSize, boolean assignedOnly) throws Exception {

		return getMyRunningInstances(lastInstanceDate, requestSize, assignedOnly, null);
	}

	/*
	 * 
	 * 현재사용자의 진행중인 업무들의 갯수를 가져도준다. 진행중인 업무 전체갯수와 할당된업무갯수만을 가져다 준다.
	 * 
	 * RunningCounts : return
	 * 	
	 */
	public RunningCounts getMyRunningInstancesCounts() throws Exception {
		
		try{
			User user = SmartUtil.getCurrentUser();
			if (CommonUtil.isEmpty(user.getCompanyId()) || CommonUtil.isEmpty(user.getId()))
				return null;
			
			TaskWorkCond totalTaskCond = new TaskWorkCond();
			totalTaskCond.setTskStartOrAssigned(user.getId());
			totalTaskCond.setLastInstanceDate(new LocalDate());
			totalTaskCond.setPrcStatus(PrcProcessInst.PROCESSINSTSTATUS_RUNNING);
			
			long totalTaskSize = getWorkListManager().getTaskWorkListSize(user.getId(), totalTaskCond);
			
			TaskWorkCond assignedTaskCond = new TaskWorkCond();

			assignedTaskCond.setTskAssignee(user.getId());
			assignedTaskCond.setTskStatus(TskTask.TASKSTATUS_ASSIGN);
			
			long assignedTaskSize = getWorkListManager().getTaskWorkListSize(user.getId(), assignedTaskCond);
			
			RunningCounts runningCounts = new RunningCounts();
			runningCounts.setTotal((int)totalTaskSize);
			runningCounts.setAssignedOnly((int)assignedTaskSize);
			return runningCounts;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required
		}
	}

	@Override
	public InstanceInfo[] searchMyRunningInstance(String key) throws Exception {
		try{
			return SmartTest.getRunningInstances();
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}

	@Override
	public String  setMemoInstance(HttpServletRequest request) throws Exception {		
		try{
			return "testId";
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}

	private SwdRecord setDataFieldsInfo(SwdRecord swdRecord, SwfForm swfForm) throws Exception {
		if (swdRecord == null || swfForm == null)
			return null;
		
		SwfField[] swfFields = swfForm.getFields();
		SwdDataField[] swdDataFields = swdRecord.getDataFields();
		for(SwdDataField swdDataField : swdDataFields) {
			for(SwfField swfField : swfFields) {
				if(swdDataField.getId().equals(swfField.getId())) {
					String formatType = swfField.getFormat().getType();
					String value = swdDataField.getValue();
					String refRecordId = swdDataField.getRefRecordId();
					List<Map<String, String>> resultUsers = null;
					if(formatType.equals(FormField.TYPE_USER)) {
						if(value != null && refRecordId != null) {
							String[] values = value.split(";");
							String[] refRecordIds = refRecordId.split(";");
							resultUsers = new ArrayList<Map<String,String>>();
							if(values.length > 0 && refRecordIds.length > 0) {
								for(int j=0; j<values.length; j++) {
									Map<String, String> map = new LinkedHashMap<String, String>();
									map.put("userId", refRecordIds[j]);
									map.put("longName", values[j]);
									resultUsers.add(map);
								}
							} else {
								Map<String, String> map = new LinkedHashMap<String, String>();
								map.put("userId", refRecordId);
								map.put("longName", value);
								resultUsers.add(map);
							}
						}
						swdDataField.setUsers(resultUsers);
					} else if(formatType.equals(FormField.TYPE_DATE)) {
						if(value != null) {
							try {
								LocalDate localDate = LocalDate.convertGMTStringToLocalDate(value);
								if(localDate != null)
									value = localDate.toLocalDateSimpleString();
							} catch (Exception e) {
							}
						}
					} else if(formatType.equals(FormField.TYPE_TIME)) {
						if(value != null) {
							try {
								LocalDate localDate = LocalDate.convertGMTTimeStringToLocalDate(value);
								if(localDate != null)
									value = localDate.toLocalTimeShortString();
							} catch (Exception e) {
							}
						}
					} else if(formatType.equals(FormField.TYPE_DATETIME)) {
						if(value != null) {
							try {
								LocalDate localDate = LocalDate.convertGMTStringToLocalDate(value);
								if(localDate != null)
									value = localDate.toLocalDateTimeSimpleString();
							} catch (Exception e) {
							}
						}
					}
					swdDataField.setValue(value);
				}
			}
		}
		return swdRecord;
		
	}
	public SwdRecord refreshDataFields(SwdRecord record) throws Exception {
		try{
			User cuser = SmartUtil.getCurrentUser();
			String userId = null;
			if (cuser != null)
				userId = cuser.getId();
			
			String formId = record.getFormId();
			if (CommonUtil.isEmpty(formId))
				return null;
			SwfForm form = getSwfManager().getForm(null, formId);
			if (form == null)
				return null;
			SwfField[] fields = form.getFields();
			if (CommonUtil.isEmpty(fields))
				return null;
			
			boolean isFirstSetMode = false; //초기 데이터 입력인지 수정인지를 판단한다
			
			//새로 값이 셋팅되어 변경될 레코드 클론
			SwdRecord oldRecord = (SwdRecord)record.clone();
			SwdRecord newRecord = (SwdRecord)record.clone();
			
			Map<String, SwdDataField> resultMap = new HashMap<String, SwdDataField>();
			
			// 각 필드들 마다 가져오기 맵핑을 확인하여 값을 셋팅한다
			for (SwfField field : fields) {
				setResultFieldMapByFields(userId, form, resultMap, field, newRecord, oldRecord, isFirstSetMode);
			}

			setDataFieldsInfo(newRecord, form);
			
			if (logger.isInfoEnabled()) {
				StringBuffer infoBuff = new StringBuffer();
				infoBuff.append("Refresh Data Field \r\n[\r\n Original Record : \r\n").append(oldRecord.toString());
				infoBuff.append(", \r\n New Record : \r\n").append(newRecord.toString()).append(" \r\n]\r\n");
				logger.info(infoBuff.toString());
			}
			
			return newRecord; 
			
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}
	@Override
	public SwdRecord refreshDataFields(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		
		try{
			User cuser = SmartUtil.getCurrentUser();
			String userId = null;
			if (cuser != null)
				userId = cuser.getId();
			
			String formId = (String)requestBody.get("formId");
			boolean retry = CommonUtil.toBoolean(requestBody.get("retry"));
			
			boolean isFirstSetMode = !retry; //초기 데이터 입력인지 수정인지를 판단한다
			
			//레코드 폼정보를 가져온다
			if (CommonUtil.isEmpty(formId))
				return null;
			SwfForm form = getSwfManager().getForm(null, formId);
			if (form == null)
				return null;
			SwfField[] fields = form.getFields();
			if (CommonUtil.isEmpty(fields))
				return null;

			SwfField[] formFields = form.getFields();
			List domainFieldList = new ArrayList();
			for (SwfField field: formFields) {
				SwdField domainField = new SwdField();
				domainField.setFormFieldId(field.getId());
				domainField.setFormFieldName(field.getName());
				domainField.setFormFieldType(field.getSystemType());
				domainField.setArray(field.isArray());
				domainField.setSystemField(field.isSystem());
				domainFieldList.add(domainField);
			}
			SwdField[] domainFields = new SwdField[domainFieldList.size()];
			domainFieldList.toArray(domainFields);
			
			SwdRecord record = getSwdRecordByRequestBody(userId, domainFields, requestBody, request);
			
			if (record.getCreationDate() == null)
				record.setCreationDate(new LocalDate());
			
			//새로 값이 셋팅되어 변경될 레코드 클론
			SwdRecord oldRecord = (SwdRecord)record.clone();
			SwdRecord newRecord = (SwdRecord)record.clone();
			
			Map<String, SwdDataField> resultMap = new HashMap<String, SwdDataField>();
			
			// 각 필드들 마다 가져오기 맵핑을 확인하여 값을 셋팅한다
			for (SwfField field : fields) {
				setResultFieldMapByFields(userId, form, resultMap, field, newRecord, oldRecord, isFirstSetMode);
			}

			setDataFieldsInfo(newRecord, form);
			
			if (logger.isInfoEnabled()) {
				StringBuffer infoBuff = new StringBuffer();
				infoBuff.append("Refresh Data Field \r\n[\r\n Original Record : \r\n").append(oldRecord.toString());
				infoBuff.append(", \r\n New Record : \r\n").append(newRecord.toString()).append(" \r\n]\r\n");
				logger.info(infoBuff.toString());
			}
			
			return newRecord; 
			
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			logger.error(e);
			return null;			
			// Exception Handling Required			
		}
	}
	private void setResultFieldMapByFields(String userId, SwfForm form, Map<String, SwdDataField> resultMap, SwfField field, SwdRecord newRecord, SwdRecord oldRecord, boolean isFirst) throws Exception {
		
		try{
			if (resultMap == null)
				resultMap = new HashMap<String, SwdDataField>();
			
			String fieldId = field.getId();
			String fieldType = field.getSystemType();
			
			SwfMappings mappings = field.getMappings();
			if (mappings == null) {
				resultMap.put(fieldId, oldRecord.getDataField(fieldId));
				newRecord.setDataField(fieldId, oldRecord.getDataField(fieldId));
				return;
			}	
			SwfMapping[] preMappings = mappings.getPreMappings();
			if (CommonUtil.isEmpty(preMappings)){
				resultMap.put(fieldId, oldRecord.getDataField(fieldId));
				newRecord.setDataField(fieldId, oldRecord.getDataField(fieldId));
				return;
			}	
			// 가져오기 매핑정의가 있는지 확인 끝

			// 연결 업무 목록
			SwfFormLink[] formLinks = form.getMappingForms();
			Map<String, SwfFormLink> formLinkMap = new HashMap<String, SwfFormLink>();
			if (!CommonUtil.isEmpty(formLinks)) {
				for (SwfFormLink formLink:formLinks)
					formLinkMap.put(formLink.getId(), formLink);
			}
			
			//현재폼, 외부폼, 프로세스폼의 우선 적용순위를 정하기위한 트리맵(소팅맵)
			Map<Long, SwdDataField> resultTreeMap = new HashMap<Long, SwdDataField>();
			//함수, 웹서비스, 계산식의 순서중 마지막 값을 취하기위한 스텍
			Stack<SwdDataField> resultStack = new Stack<SwdDataField>();
			
			//가져오기 셋팅이 여러개 일수 있다
			for (SwfMapping preMapping : preMappings) {
				//초기 데이터 가져오기 호출이 아니고 매번호출이 아니라면 스킵
				//초기 데이터 가져오기내용 검토 필요(초기인지 아닌지 알수 있나?)
				if (!preMapping.isEachTime() && !isFirst){
					//resultMap.put(fieldId, oldRecord.getDataField(fieldId));
					//newRecord.setDataField(fieldId, oldRecord.getDataField(fieldId));
					continue;
				}	
	
				String mappingType = preMapping.getType();
	//			mappingType = TYPE_SIMPLE = "mapping_form"; 단순 맵핑
	//			mappingTYpe = TYPE_EXPRESSION = "expression"; 계산식
				
				if (SwfMapping.TYPE_SIMPLE.equalsIgnoreCase(mappingType)) {
					//단순 맵핑 (현재업무화면, 다른업무화면, 프로세스업무화면, 시스템함수, 웹서비스)
					String mappingFormType = preMapping.getMappingFormType();
					// 현재업무항목
					if (SwfMapping.MAPPINGTYPE_SELFFORM.equalsIgnoreCase(mappingFormType)) {
						//현재 업무 항목이라면 재귀 함수호출로 호출되는쪽의 데이터 맵핑이 있는지를 다시 살핀다
						String mappingFieldId = CommonUtil.toNull(preMapping.getFieldId());
						if (CommonUtil.isEmpty(mappingFieldId))
							continue;
						SwfField[] fields = form.getFields();
						SwfField targetField = null;
						for (SwfField tempField : fields) {
							if (tempField.getId().equalsIgnoreCase(mappingFieldId)) {
								targetField = tempField;
								break;
							}
						}
						if (targetField == null) {
							logger.warn("TargetMapping Field Is Null!!! Check Change Field Id!!");
							continue;
						}
						//재귀 호출
						if (!resultMap.containsKey(targetField.getId()))
							setResultFieldMapByFields(userId, form, resultMap, targetField, newRecord, oldRecord, isFirst);
						
						SwdDataField dataField = (SwdDataField)resultMap.get(mappingFieldId);
						
						SwdDataField myDataField = new SwdDataField();
						myDataField.setId(fieldId);
						myDataField.setType(fieldType);
						if (dataField != null)
							myDataField.setValue(dataField.getValue());
						//resultMap.put(fieldId, new SwdDataField());
						
						long timeKey = oldRecord.getCreationDate().getTime();
						if (!resultTreeMap.containsKey(timeKey)) {
							resultTreeMap.put(timeKey, myDataField);
						}
					} else if (SwfMapping.MAPPINGTYPE_OTHERFORM.equalsIgnoreCase(mappingFormType)) {
					//외부업무 화면
						String formLinkId = preMapping.getMappingFormId();
						String mappingFieldId = preMapping.getFieldId();
						SwfFieldMapping[] fieldMappings = preMapping.getFieldMappings();
						
						if (CommonUtil.isEmpty(formLinkId) || !formLinkMap.containsKey(formLinkId) || 
							(CommonUtil.isEmpty(mappingFieldId) && CommonUtil.isEmpty(fieldMappings))) {
							continue;
						}
						
						//외부폼에서 가져오기를 하기전에 외부폼과 연결되어 있는 자신의 폼의 필드들의 값을 먼저 채운다
						SwfFormLink formLink = formLinkMap.get(formLinkId);
						SwfConditions condsObj = formLink.getConds();
						SwfCondition[] conds = condsObj.getCond();
						for (int i = 0; i < conds.length; i++) {
							SwfCondition swfCondition = conds[i];
							
							SwfOperand first = swfCondition.getFirst();
							SwfOperand second = swfCondition.getSecond();
							
							String selfFieldId = null;
							if (first != null && first.getType() != null && first.getType().equalsIgnoreCase("self")) {
								selfFieldId = first.getFieldId();
							}
							if (second != null && second.getType() != null && second.getType().equalsIgnoreCase("self")) {
								selfFieldId = second.getFieldId();
							}	
							if (selfFieldId == null)
								continue;
							
							SwfField[] fields = form.getFields();
							SwfField targetField = null;
							for (SwfField tempField : fields) {
								if (tempField.getId().equalsIgnoreCase(selfFieldId)) {
									targetField = tempField;
									break;
								}
							}
							if (targetField == null) {
								logger.warn("TargetMapping Field Is Null!!! Check Change Field Id!!");
								continue;
							}
							//재귀 호출
							if (!resultMap.containsKey(targetField.getId()))
								setResultFieldMapByFields(userId, form, resultMap, targetField, newRecord, oldRecord, isFirst);
							
						}
						
						String valueFunc = preMapping.getValueFunc();
						if (valueFunc == null || valueFunc.equalsIgnoreCase("value")) {
							SwdRecord[] mappingRecords = null;
							if (!CommonUtil.isEmpty(mappingFieldId)) {
								//SwdRecord mappingRecord = getSwdManager().getRecordByMappingForm(userId, newRecord,  formLinkMap.get(formLinkId));
								mappingRecords = getSwdManager().getRecordsByMappingForm(userId, newRecord,  formLinkMap.get(formLinkId));
								//if (mappingRecord != null)
								//	mappingRecords = new SwdRecord[] {mappingRecord};
							} else {
								mappingRecords = getSwdManager().getRecordsByMappingForm(userId, newRecord,  formLinkMap.get(formLinkId));
							}
							
							if (CommonUtil.isEmpty(mappingRecords)) 
								continue;
							
							if (!CommonUtil.isEmpty(mappingFieldId)) {
								
								//다수의 검색 결과가 온경우 기본적으로 처음 결과값으로 dataField를 생성하고 그 객체 안의 하위 dataField[]에 
								//전체 결과값을 담아서 리턴한다.
								//폼런타임에서는 그필드가 콤보박스 필드라면 해당 하위 dataField[]를 검색하고 존재한다면 콤보 형식으로 데이터를 표현한다
								
								SwdDataField[] subDataFields = null;
								if (mappingRecords.length > 1) {
									
									subDataFields = new SwdDataField[mappingRecords.length];
									for (int i = 0; i < mappingRecords.length; i++) {
										SwdRecord subMappingRecord = mappingRecords[i];
										SwdDataField subMappingDataField = subMappingRecord.getDataField(mappingFieldId);
										SwdDataField subDataField = new SwdDataField();
										
										if (subMappingDataField == null) {
											subDataField.setValue(null);
											subDataField.setRefRecordId(null);
										} else {
											subDataField.setValue(subMappingDataField.getValue());
											subDataField.setRefRecordId(subMappingDataField.getRefRecordId());
										}
										subDataFields[i] = subDataField;
									}
								}
								
								
								SwdRecord mappingRecord = mappingRecords[0];
								SwdDataField mappingDataField = mappingRecord.getDataField(mappingFieldId);
								
								SwdDataField dataField = oldRecord.getDataField(fieldId);
								
								if (subDataFields != null)
									dataField.setDataFields(subDataFields);
								
								if (dataField == null) {
									dataField = new SwdDataField();
									dataField.setId(fieldId);
									dataField.setType(fieldType);
								}
								if (mappingDataField == null) {
									dataField.setValue(null);
									dataField.setRefRecordId(null);
								} else {
									dataField.setValue(mappingDataField.getValue());
									dataField.setRefRecordId(mappingDataField.getRefRecordId());
								}
								//setResultTreeMap
								long timeKey = mappingRecord.getCreationDate().getTime();
								if (!resultTreeMap.containsKey(timeKey)) 
									resultTreeMap.put(timeKey, dataField);
								
								SwfFormat format = field.getFormat();
								if (format == null)
									continue;
								String formatType = format.getType();
								if (!"refFormField".equalsIgnoreCase(formatType))
									continue;
								SwfFormRef formRef = format.getRefForm();
								if (formRef == null)
									continue;
								dataField.setRefForm(formRef.getId());
								SwfFieldRef fieldRef = formRef.getField();
								if (fieldRef == null)
									continue;
								dataField.setRefFormField(fieldRef.getId());
								if (CommonUtil.toNull(dataField.getRefRecordId()) == null &&
										CommonUtil.toNull(dataField.getRefForm()) != null && 
										CommonUtil.toNull(dataField.getRefFormField()) != null && 
										CommonUtil.toNull(dataField.getValue()) != null) {
									dataField.setRefRecordId(mappingRecord.getRecordId());
								}
								
								
								
							} else {
								//Sw 2.0 에서 구현 되어 있지만 3.0에서는 미구현! mappingFieldId 가 비어 있는 경우가 없어 보인다! 만약에 아래 오류를 발견한다면
								//2.0의 executionService.jsp 소스에서 refreshData 부분을 참고하여 코드를 작성해야 한다
								throw new Exception("InstanceServiceImpl Exception : mappingFieldId is Empty (InstanceServiceImpl.java)");
							}
						} else {
							double value = getSwdManager().getRecordValueByMappingForm(userId, oldRecord, formLinkMap.get(formLinkId), mappingFieldId, valueFunc);
							SwdDataField dataField = new SwdDataField();
							dataField.setId(fieldId);
							dataField.setType(fieldType);
							dataField.setRefRecordId(null);
							dataField.setRefForm(null);
							dataField.setRefFormField(null);
							dataField.setValue(value + "");
							resultStack.push(dataField);
						}
					} else if (SwfMapping.MAPPINGTYPE_PROCESSFORM.equalsIgnoreCase(mappingFormType)) {
					//프로세스 업무
						String mappingFormId = preMapping.getMappingFormId();
						if (!mappingFormId.equalsIgnoreCase("processParam"))
							continue;
					} else if (SwfMapping.MAPPINGTYPE_SYSTEM.equalsIgnoreCase(mappingFormType)) {
					//시스템 함수
						SwoUser func = getSwoManager().getUser(userId, userId, "all");
						String functionId = CommonUtil.toNull(preMapping.getFieldId());
						String funcDeptId = "";
						String funcDeptName = "";
						String funcTeamLeader = "";
						if (CommonUtil.isEmpty(functionId))
							continue;
						if (functionId.equals("mis:generateId")) {
							SwdDataField dataField = new SwdDataField();
							dataField.setId(fieldId);
							dataField.setType(fieldType);
							dataField.setRefRecordId(null);
							dataField.setRefForm(null);
							dataField.setRefFormField(null);
							dataField.setValue(CommonUtil.newId());
							resultStack.push(dataField);
							
						} else if (functionId.equals("mis:getCurrentDate")) {
							SwdDataField dataField = new SwdDataField();
							dataField.setId(fieldId);
							dataField.setType(fieldType);
							dataField.setRefRecordId(null);
							dataField.setRefForm(null);
							dataField.setRefFormField(null);
							dataField.setValue(DateUtil.toXsdDotDateString(new Date()));
							resultStack.push(dataField);
							
						} else if (functionId.equals("mis:getCurrentUser")) {
							SwdDataField dataField = toDataField(userId, field, userId);
							dataField.setId(fieldId);
							resultStack.push(dataField);
							
						} else if (functionId.equals("mis:getDeptId")){		
							if(func != null){
							funcDeptId = func.getDeptId();
							SwoDepartment funcdept = getSwoManager().getDepartment(userId, funcDeptId, "all");
								if(funcdept != null){
									funcDeptName = funcdept.getName();
								}
							}
							SwdDataField dataField = toDataField(userId, field, funcDeptName);
							dataField.setId(fieldId);
							resultStack.push(dataField);
							
						} else if (functionId.equals("mis:getTeamLeaderId")){
							if(func != null){
								funcDeptId = func.getDeptId();
								SwoUserCond cond = new SwoUserCond();
								cond.setDeptId(funcDeptId);
								cond.setRoleId("DEPT LEADER");
								
								SwoUser[] funcs = getSwoManager().getUsers(userId, cond, "all");
								if(funcs != null){
									funcTeamLeader = funcs[0].getId();
								}
							}
							SwdDataField dataField = toDataField(userId, field, funcTeamLeader);
							dataField.setId(fieldId);
							resultStack.push(dataField);
							
						} else if (functionId.equals("mis:getEmpNo")){
							String funcEmpNo = func.getEmpNo();
							SwdDataField dataField = toDataField(userId, field, funcEmpNo);
							dataField.setId(fieldId);
							resultStack.push(dataField);
							
						} else if (functionId.equals("mis:getMobileNo")){
							String funcMobileNo = func.getMobileNo();
							SwdDataField dataField = toDataField(userId, field, funcMobileNo);
							dataField.setId(fieldId);
							resultStack.push(dataField);
							
						} else if (functionId.equals("mis:getInternalNo")){
							String funcExtensionNo = func.getExtensionNo();
							SwdDataField dataField = toDataField(userId, field, funcExtensionNo);
							dataField.setId(fieldId);
							resultStack.push(dataField);
							
						}
					} else if ("service_form".equalsIgnoreCase(mappingFormType)) {
					//웹서비스
						MappingService[] mappingServices = form.getMappingService();
						if (mappingServices == null || mappingServices.length == 0)
							continue;
						
						for (int i = 0; i < mappingServices.length; i++) {
							MappingService mappingService = mappingServices[i];
							
							String serviceId = mappingService.getId();
							String targetServiceId = mappingService.getTargetServiceId();
							String execution = mappingService.getExecution();
							
							SwcWebService webServiceInfo = getSwcManager().getWebService(userId, targetServiceId, IManager.LEVEL_ALL);
							if (webServiceInfo == null)
								continue;

							String mappingServiceId = preMapping.getMappingServiceId();
							if (mappingServiceId == null || !mappingServiceId.equalsIgnoreCase(serviceId))
								continue;
							
							String endPoint = CommonUtil.toNotNull(webServiceInfo.getWebServiceAddress());
							String wsdlUrl = CommonUtil.toNotNull(webServiceInfo.getWsdlAddress());
							if(wsdlUrl.indexOf(".jws")>-1){
								if(!(endPoint.indexOf(".jws")>-1)){
									if(!endPoint.equals(""))
										endPoint = endPoint+".jws";
								}
							}
							String operation = webServiceInfo.getOperationName();
							AcpActualParameter[] actualParameters = mappingService.getActualParameters();
							SwcWebServiceParameter[] params = webServiceInfo.getSwcWebServiceParameters();
							
							Queue<SwdDataField> inputDataFieldQueue = new LinkedList<SwdDataField>();
							String outPutType = null;
							
							for(int j = 0 ; j < params.length; j++){
								SwcWebServiceParameter param = params[j];
								if(param.getType().equalsIgnoreCase("I")){
									if(actualParameters != null){
										for(AcpActualParameter actualParameter : actualParameters){
											if(!actualParameter.getId().equals(param.getParameterName()))
												continue;
												
											String targetFieldId = actualParameter.getFieldId();
											
											//연결된 폼필드의 값부터 채운후 그값을 가지고 웹서비스를 호출한다
											SwfField[] fields = form.getFields();
											SwfField targetField = null;
											for (SwfField tempField : fields) {
												if (tempField.getId().equalsIgnoreCase(targetFieldId)) {
													targetField = tempField;
													break;
												}
											}
											if (targetField == null) {
												logger.warn("TargetMapping Field Is Null!!! Check Change Field Id!!");
												continue;
											}
											//재귀 호출
											if (!resultMap.containsKey(targetField.getId()))
												setResultFieldMapByFields(userId, form, resultMap, targetField, newRecord, oldRecord, isFirst);
											inputDataFieldQueue.offer(newRecord.getDataField(targetFieldId));
										}
									}
								}else if(param.getType().equalsIgnoreCase("O")){
									outPutType = param.getParameterType();
								}
							}
							if(CommonUtil.isEmpty(execution) || execution.equalsIgnoreCase("before")){
								int size = inputDataFieldQueue.size();
								String[] inputParams = new String[size];
								for (int j = 0; j < size; j++) {
									SwdDataField dataField = inputDataFieldQueue.poll();
									inputParams[j] = dataField.getValue();
								}
								String[] returnWebService = WebServiceUtil.invokeWebService(endPoint,operation, inputParams, outPutType);
								if(returnWebService !=null){
									
									if (outPutType.equalsIgnoreCase("ArrayOf_xsd_string")) {
										//SwdDataField dataField = toDataField(userId, field, oldRecord.getDataFieldValue(fieldId));
										SwfFormat fieldFormat = field.getFormat();
										SwdDataField dataField = new SwdDataField();
										if (fieldFormat == null || !"userField".equals(fieldFormat.getType())) {
											dataField.setId(field.getId());
											//dataField.setType(field.getSystemType());
											dataField.setType("ArrayOf_xsd_string");
											dataField.setValue(oldRecord.getDataFieldValue(fieldId));
										} else {
											dataField = toUserDataField(userId, oldRecord.getDataFieldValue(fieldId));
										}
										
										SwdDataField[] subDataFields = new SwdDataField[returnWebService.length];
										for (int j = 0; j < returnWebService.length; j++) {
											
											SwdDataField subDataField = new SwdDataField();
											if (fieldFormat == null || !"userField".equals(fieldFormat.getType())) {
												subDataField.setId(field.getId());
												subDataField.setType(field.getSystemType());
												subDataField.setValue(returnWebService[j]);
											} else {
												subDataField = toUserDataField(userId, returnWebService[j]);
											}
											subDataFields[j] = subDataField;
										}
										dataField.setDataFields(subDataFields);
										resultStack.push(dataField);
									} else {
										SwfFormat fieldFormat = field.getFormat();
										SwdDataField dataField = new SwdDataField();
										if (fieldFormat == null || !"userField".equals(fieldFormat.getType())) {
											dataField.setId(field.getId());
											dataField.setType(field.getSystemType());
											dataField.setValue(returnWebService[0]);
										} else {
											dataField = toUserDataField(userId, returnWebService[0]);
										}
										resultStack.push(dataField);
									}
								} else {
									SwdDataField dataField = toDataField(userId, field, "Empty");
									resultStack.push(dataField);
								}
							} else {
								//TODO after invoke
							}
						}
					} else if (SwfMapping.TYPE_EXPRESSION.equalsIgnoreCase(mappingFormType)) {
					//직접입력
						String value = preMapping.getFieldName();
						value = StringUtils.replace(value, "'", "");
						SwdDataField dataField = toDataField(userId, field, value);
						dataField.setId(fieldId);
						resultStack.push(dataField);
					}
					
				} else if (SwfMapping.TYPE_EXPRESSION.equalsIgnoreCase(mappingType)) {
					//계산식
					String expr = preMapping.getExpression();
					String value = "";
					if (!CommonUtil.isEmpty(expr)) {
						value = getSwdManager().executeExpression(userId, preMapping.getExpression(), newRecord, formLinks);
					}
					SwdDataField dataField = toDataField(userId, field, value);
					resultStack.push(dataField);
				}
			}

			if (resultTreeMap != null && resultTreeMap.size() != 0) {
				TreeMap<Long, SwdDataField> sortMap = new TreeMap<Long, SwdDataField>(resultTreeMap);
				resultMap.put(fieldId, sortMap.get(sortMap.lastKey()) );
				newRecord.setDataField(fieldId, sortMap.get(sortMap.lastKey()));
			} else if (resultStack != null && resultStack.size() != 0) {
				resultMap.put(fieldId, resultStack.peek());
				newRecord.setDataField(fieldId, resultStack.peek());
			} else {
				resultMap.put(fieldId, oldRecord.getDataField(fieldId));
				newRecord.setDataField(fieldId, oldRecord.getDataField(fieldId));
			}
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			logger.error(e);
			// Exception Handling Required			
		}
	}
	private SwdDataField toDataField(String user, SwfField field, String id) throws Exception {
		if (CommonUtil.isEmpty(id))
			return null;
		SwfFormat fieldFormat = field.getFormat();
		SwdDataField obj = null;
		if (fieldFormat == null || !"userField".equals(fieldFormat.getType())) {
			obj = new SwdDataField();
			obj.setId(field.getId());
			obj.setType(field.getSystemType());
			obj.setValue(id);
		} else {
			obj = toUserDataField(user, id);
		}
		return obj;
	}
	private SwdDataField toUserDataField(String user, String id) throws Exception {
		if (CommonUtil.isEmpty(id))
			return null;
		SwoUser userModel = getSwoManager().getUser(user, id, IManager.LEVEL_LITE);
		if (userModel == null)
			return null;
		SwdDataField dataField = new SwdDataField();
		dataField.setRefForm("frm_user_SYSTEM");
		dataField.setRefFormField("4");
		dataField.setRefRecordId(id);
		dataField.setValue(userModel.getPosition() + " " + userModel.getName());
		return dataField;
	}
	public String setInformationWorkInstance_old(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		
		try{
			/*
			Key Set : frmSmartForm
			Key Set : frmScheduleWork
			Key Set : frmAccessSpace
			key Set : formId
			key Set : formName
			*/
			Map<String, Object> smartFormInfoMap = (Map<String, Object>)requestBody.get("frmSmartForm");
	
			String domainId = null; // domainId 가 없어도 내부 서버에서 폼아이디로 검색하여 저장
			String formId = (String)requestBody.get("formId");
			String formName = (String)requestBody.get("formName");
			String instanceId = (String)requestBody.get("instanceId");
			int formVersion = 1;
			User cuser = SmartUtil.getCurrentUser();
			String userId = null;
			if (cuser != null)
				userId = cuser.getId();
	
			SwdDomainCond swdDomainCond = new SwdDomainCond();
			swdDomainCond.setFormId(formId);
			SwdDomain swdDomain = getSwdManager().getDomain(userId, swdDomainCond, IManager.LEVEL_LITE);
	
			domainId = swdDomain.getObjId();
	
			SwdFieldCond swdFieldCond = new SwdFieldCond();
			swdFieldCond.setDomainObjId(domainId);
			SwdField[] fields = getSwdManager().getFields(userId, swdFieldCond, IManager.LEVEL_LITE);
			if (CommonUtil.isEmpty(fields))
				return null;//TODO return null? throw new Exception??
	
			Map<String, SwdField> fieldInfoMap = new HashMap<String, SwdField>();
			for (SwdField field : fields) {
				fieldInfoMap.put(field.getFormFieldId(), field);
			}
			
			Set<String> keySet = smartFormInfoMap.keySet();
			Iterator<String> itr = keySet.iterator();
			
	//		SwdField[] fieldDatas = new SwdField[keySet.size()];
			List fieldDataList = new ArrayList();
			List<Map<String, String>> files = null;
			List<Map<String, String>> users = null;
			String groupId = null;
			while (itr.hasNext()) {
				String fieldId = (String)itr.next();
				String value = null;
				String refForm = null;
				String refFormField = null;
				String refRecordId = null;
				Object fieldValue = smartFormInfoMap.get(fieldId);
				if (fieldValue instanceof LinkedHashMap) {
					Map<String, Object> valueMap = (Map<String, Object>)fieldValue;
					groupId = (String)valueMap.get("groupId");
					refForm = (String)valueMap.get("refForm");
					users = (ArrayList<Map<String,String>>)valueMap.get("users");
	
					if(!CommonUtil.isEmpty(groupId)) {
						files = (ArrayList<Map<String,String>>)valueMap.get("files");
						if(files != null && files.size() > 0)
							value = groupId;
					} else if(!CommonUtil.isEmpty(refForm)) {
						refFormField = (String)valueMap.get("refFormField");
						refRecordId = (String)valueMap.get("refRecordId");
						SwoDepartmentCond swoDepartmentCond = new SwoDepartmentCond();
						swoDepartmentCond.setId(refRecordId);
						String deptName = getSwoManager().getDepartment(userId, swoDepartmentCond, IManager.LEVEL_LITE).getName();
						value = deptName;
					} else if(!CommonUtil.isEmpty(users)) {
						refFormField = "frm_user_SYSTEM"; 
						String resultRefRecordId = "";
						String resultValue = "";
						String symbol = ";";
						if(users.size() == 1) {
							resultRefRecordId = users.get(0).get("id");
							resultValue = users.get(0).get("name");
						} else {
							for(int i=0; i < users.subList(0, users.size()).size(); i++) {
								Map<String, String> user = users.get(i);
								resultRefRecordId += user.get("id") + symbol;
								resultValue += user.get("name") + symbol;
							}
						}
						refRecordId = resultRefRecordId;
						value = resultValue;
					}
				} else if(fieldValue instanceof String) {
					value = (String)smartFormInfoMap.get(fieldId);
					if(formId.equals(SmartForm.ID_MEMO_MANAGEMENT)) {
						if(fieldId.equals("12"))
							value = StringUtil.subString(value, 0, 20, "...");
					} else if(formId.equals(SmartForm.ID_EVENT_MANAGEMENT)) {
						if(fieldId.equals("1") || fieldId.equals("2")) {
							if(!value.isEmpty())
								value = LocalDate.convertStringToLocalDate(value).toGMTDateString();
						}
					}
				}
				if (CommonUtil.isEmpty(value))
					continue;
				SwdDataField fieldData = new SwdDataField();
				fieldData.setId(fieldId);
				fieldData.setName(fieldInfoMap.get(fieldId).getFormFieldName());
				fieldData.setRefForm(refForm);
				fieldData.setRefFormField(refFormField);
				fieldData.setRefRecordId(refRecordId);
				fieldData.setValue(value);
	
				fieldDataList.add(fieldData);
				
			}
			String workType = "";
			String servletPath = request.getServletPath();
			if(servletPath.equals("/upload_new_picture.sw"))
				workType = "Pictures";
			else
				workType = "Files";
	
			SwdDataField[] fieldDatas = new SwdDataField[fieldDataList.size()];
			fieldDataList.toArray(fieldDatas);
			SwdRecord obj = new SwdRecord();
			obj.setDomainId(domainId);
			obj.setFormId(formId);
			obj.setFormName(formName);
			obj.setFormVersion(formVersion);
			obj.setDataFields(fieldDatas);
			obj.setRecordId(instanceId);
	
			String returnInstanceId = getSwdManager().setRecord(userId, obj, IManager.LEVEL_ALL);
	
			if(files != null && files.size() > 0) {
				try {
					for(int i=0; i < files.subList(0, files.size()).size(); i++) {
						Map<String, String> file = files.get(i);
						String fileId = file.get("fileId");
						String fileName = file.get("fileName");
						String fileSize = file.get("fileSize");
						getDocManager().insertFiles(workType, "", groupId, fileId, fileName, fileSize);
					}
				} catch (Exception e) {
					throw new DocFileException("file upload fail...");
				}
			}
	
			return returnInstanceId;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}
	@Override
	public String setInformationWorkInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		try{
			/*
			Key Set : frmSmartForm
			Key Set : frmScheduleWork
			Key Set : frmAccessSpace
			key Set : formId
			key Set : formName
			key Set : frmTaskForward
			key Set : frmApprovalLine
			key Set : frmTaskApproval
			*/
			Map<String, Object> frmSmartFormMap = (Map<String, Object>)requestBody.get("frmSmartForm");
			Map<String, Object> frmAccessSpaceMap = (Map<String, Object>)requestBody.get("frmAccessSpace");
			Map<String, Object> frmTaskForwardMap = (Map<String, Object>)requestBody.get("frmTaskForward");
	
			String domainId = null; // domainId 가 없어도 내부 서버에서 폼아이디로 검색하여 저장
			String formId = (String)requestBody.get("formId");
			String formName = (String)requestBody.get("formName");
			String instanceId = (String)requestBody.get("instanceId");
			if(CommonUtil.isEmpty(instanceId))
				instanceId = "dr_" + CommonUtil.newId();
			int formVersion = 1;
			User cuser = SmartUtil.getCurrentUser();
			String userId = null;
			if (cuser != null)
				userId = cuser.getId();
	
			SwdDomainCond swdDomainCond = new SwdDomainCond();
			swdDomainCond.setFormId(formId);
			SwdDomain swdDomain = getSwdManager().getDomain(userId, swdDomainCond, IManager.LEVEL_LITE);
	
			domainId = swdDomain.getObjId();
	
			SwdFieldCond swdFieldCond = new SwdFieldCond();
			swdFieldCond.setDomainObjId(domainId);
			SwdField[] fields = getSwdManager().getFields(userId, swdFieldCond, IManager.LEVEL_LITE);
			if (CommonUtil.isEmpty(fields))
				return null;//TODO return null? throw new Exception??
	
			Map<String, SwdField> fieldInfoMap = new HashMap<String, SwdField>();
			for (SwdField field : fields) {
				fieldInfoMap.put(field.getFormFieldId(), field);
			}
			
			//업무화면에서 폼데이터를 넘길때 모든 필드가 다 넘어오지 않는다. 예를 들면 checkbox 의 체크가 되어 있지 않으면 해당 필드 정보자체가
			//자체가 넘어 오지 않기 때문에 그값을 셋팅할수가 없다 따라서 화면에서 넘어오는 필드를 기준으로 하는것이 아니라 데이터베이스에서 조회한
			//필드 기준으로 Iterator 한다
			//그리고 만약 해당 필드가 boolean(checkbox) 일경우 값이 없다면 기본적으로 false를 입력하도록 한다
			
			//Set<String> keySet = frmSmartFormMap.keySet();
			//Iterator<String> itr = keySet.iterator();
			
			Set<String> keySet = fieldInfoMap.keySet();
			Iterator<String> itr = keySet.iterator();
			
	//		SwdField[] fieldDatas = new SwdField[keySet.size()];
			List fieldDataList = new ArrayList();
			List<Map<String, String>> files = null;
			List<Map<String, String>> users = null;
			String groupId = null;
			Map<String, List<Map<String, String>>> fileGroupMap = new HashMap<String, List<Map<String, String>>>();
			while (itr.hasNext()) {
				String fieldId = (String)itr.next();
				String value = null;
				String type = null;
				String refForm = null;
				String refFormField = null;
				String refRecordId = null;
				Object fieldValue = frmSmartFormMap.get(fieldId);
				if (fieldValue instanceof LinkedHashMap) {
					Map<String, Object> valueMap = (Map<String, Object>)fieldValue;
					groupId = (String)valueMap.get("groupId");
					refForm = (String)valueMap.get("refForm");
					users = (ArrayList<Map<String,String>>)valueMap.get("users");

					if(!CommonUtil.isEmpty(groupId)) {
						files = (ArrayList<Map<String,String>>)valueMap.get("files");
						if(!CommonUtil.isEmpty(files)) {
							fileGroupMap.put(groupId, files);
							value = groupId;
						}
					} else if(!CommonUtil.isEmpty(refForm)) {
						refFormField = (String)valueMap.get("refFormField");
						refRecordId = (String)valueMap.get("refRecordId");
						
						SwdRecordCond cond = new SwdRecordCond();
						cond.setFormId(refForm);
						//cond.setReferencedFormId(refFormField);
						cond.setRecordId(refRecordId);
						
						SwdRecord refRecord = getSwdManager().getRecord(userId, cond, IManager.LEVEL_LITE);
						
						if (refRecord != null) {
							value = refRecord.getDataFieldValue(refFormField);
						}
						
//						SwoDepartmentCond swoDepartmentCond = new SwoDepartmentCond();
//						swoDepartmentCond.setId(refRecordId);
//						String deptName = getSwoManager().getDepartment(userId, swoDepartmentCond, IManager.LEVEL_LITE).getName();
//						value = deptName;
					
					} else if(!CommonUtil.isEmpty(users)) {
						refForm = "frm_user_SYSTEM";
						refFormField = "4";
						String resultRefRecordId = "";
						String resultValue = "";
						String symbol = ";";
						if(users.size() == 1) {
							resultRefRecordId = users.get(0).get("id");
							resultValue = users.get(0).get("name");
						} else {
							for(int i=0; i < users.subList(0, users.size()).size(); i++) {
								Map<String, String> user = users.get(i);
								resultRefRecordId += user.get("id") + symbol;
								resultValue += user.get("name") + symbol;
							}
						}
						refRecordId = resultRefRecordId;
						value = resultValue;
					}
				} else if(fieldValue instanceof String) {
					value = (String)frmSmartFormMap.get(fieldId);
					type = fieldInfoMap.get(fieldId).getFormFieldType();
					if(!value.equals("")) {
						if(formId.equals(SmartForm.ID_MEMO_MANAGEMENT)) {
							if(fieldId.equals("12"))
								value = StringUtil.subString(value, 0, 20, "...");
						} else if(formId.equals(SmartForm.ID_EVENT_MANAGEMENT)) {
							if(fieldId.equals("1") || fieldId.equals("2")) {
								if(!value.isEmpty())
									value = LocalDate.convertStringToLocalDate(value).toGMTDateString();
							}
						}
						if(type.equals("datetime")) {
							if(value.length() == FieldData.SIZE_DATETIME)
								value = LocalDate.convertLocalDateTimeStringToLocalDate(value).toGMTDateString();
							else if(value.length() == FieldData.SIZE_DATE)
								value = LocalDate.convertLocalDateStringToLocalDate(value).toGMTDateString();
						} else if(type.equals("time")) {
							value = LocalDate.convertLocalTimeStringToLocalDate(value).toGMTTimeString2();
						}
					}
				} else if(fieldValue instanceof Integer) {
					value = (Integer)frmSmartFormMap.get(fieldId) + "";
				}
				if (CommonUtil.isEmpty(value)) {
					if (fieldInfoMap.get(fieldId).getFormFieldType().equalsIgnoreCase("boolean")) {
						value = "false";
					} else {
						continue;
					}
				}
				SwdDataField fieldData = new SwdDataField();
				fieldData.setId(fieldId);
				fieldData.setName(fieldInfoMap.get(fieldId).getFormFieldName());
				fieldData.setRefForm(refForm);
				fieldData.setRefFormField(refFormField);
				fieldData.setRefRecordId(refRecordId);
				fieldData.setValue(value);
	
				fieldDataList.add(fieldData);

			}
	
			SwdDataField[] fieldDatas = new SwdDataField[fieldDataList.size()];
			fieldDataList.toArray(fieldDatas);
			SwdRecord obj = new SwdRecord();
			obj.setDomainId(domainId);
			obj.setFormId(formId);
			obj.setFormName(formName);
			obj.setFormVersion(formVersion);
			obj.setDataFields(fieldDatas);
			obj.setRecordId(instanceId);

			if(frmAccessSpaceMap != null) {
				keySet = frmAccessSpaceMap.keySet();
				itr = keySet.iterator();
	
				String workSpaceId = null;
				String workSpaceType = null;
				String accessLevel = null;
				String accessValue = null;
	
				while (itr.hasNext()) {
					String fieldId = (String)itr.next();
					Object fieldValue = frmAccessSpaceMap.get(fieldId);
					if (fieldValue instanceof LinkedHashMap) {
						Map<String, Object> valueMap = (Map<String, Object>)fieldValue;
						users = (ArrayList<Map<String,String>>)valueMap.get("users");
						if(!CommonUtil.isEmpty(users)) {
							String symbol = ";";
							if(users.size() == 1) {
								accessValue = users.get(0).get("id");
							} else {
								accessValue = "";
								for(int i=0; i < users.subList(0, users.size()).size(); i++) {
									Map<String, String> user = users.get(i);
									accessValue += user.get("id") + symbol;
								}
							}
						}
					} else if(fieldValue instanceof String) {
						if(fieldId.equals("selWorkSpace")) {
							workSpaceId = (String)fieldValue;
						} else if(fieldId.equals("selWorkSpaceType")) {
							workSpaceType = (String)fieldValue;
						} else if(fieldId.equals("selAccessLevel")) {
							accessLevel = (String)fieldValue;
						}
					}
				}

				obj.setWorkSpaceId(workSpaceId);
				obj.setWorkSpaceType(workSpaceType);
				obj.setAccessLevel(accessLevel);
				obj.setAccessValue(accessValue);
			}
			
			//참조 업무 데이터 생성
			
			if (frmTaskForwardMap != null) {
				
				keySet = frmTaskForwardMap.keySet();
				itr = keySet.iterator();
	
				String txtForwardSubject = null;
				String txtForwardComments = null;
				String txtForwardForwardee = null;
	
				while (itr.hasNext()) {
					String fieldId = (String)itr.next();
					Object fieldValue = frmTaskForwardMap.get(fieldId);
					if (fieldValue instanceof LinkedHashMap) {
						Map<String, Object> valueMap = (Map<String, Object>)fieldValue;
						users = (ArrayList<Map<String,String>>)valueMap.get("users");
						if(!CommonUtil.isEmpty(users)) {
							String symbol = ";";
							if(users.size() == 1) {
								txtForwardForwardee = users.get(0).get("id");
							} else {
								txtForwardForwardee = "";
								for(int i=0; i < users.subList(0, users.size()).size(); i++) {
									Map<String, String> user = users.get(i);
									txtForwardForwardee += user.get("id") + symbol;
								}
							}
						}
					} else if(fieldValue instanceof String) {
						if(fieldId.equals("txtForwardSubject")) {
							txtForwardSubject = (String)fieldValue;
						} else if(fieldId.equals("txtForwardComments")) {
							txtForwardComments = (String)fieldValue;
						} 
					}
				}
				obj.setExtendedAttributeValue("txtForwardSubject", txtForwardSubject);
				obj.setExtendedAttributeValue("txtForwardForwardee", txtForwardForwardee);
				obj.setExtendedAttributeValue("txtForwardComments", txtForwardComments);
			}
			
			// 전자결재 업무 생성
			
			//전자 결재 정보를 바탕으로 approvalLine 을 생성한다
			
			String recId = obj.getRecordId();
			if (CommonUtil.isEmpty(recId)) {
				recId = CommonUtil.newId();
				obj.setRecordId(recId);
			}
			
			Map<String, Object> frmApprovalLine = (Map<String, Object>)requestBody.get("frmApprovalLine");
		
			if (frmApprovalLine != null) {
				Iterator appLineItr = frmApprovalLine.keySet().iterator();
				
				String hdnApprovalLineId = null;
				Map<String, Map<String, String>> appLineSortingMap = new HashMap<String, Map<String, String>>();
				while (appLineItr.hasNext()) {
					String key = (String)appLineItr.next();
					if (key.equalsIgnoreCase("hdnApprovalLineId")) {
						hdnApprovalLineId = (String)frmApprovalLine.get(key);
						continue;
					}
					//key - usrLevelApprover1, usrLevelApprover2, usrLevelApprover3 ......
					String keyIndex = StringUtils.replace(key, "usrLevelApprover", "");
					Object value = frmApprovalLine.get(key);
					
					if (value instanceof String) {
						Map userMap = new HashMap();
						userMap.put("id", (String)value);
						appLineSortingMap.put(keyIndex, userMap);
					} else if (value instanceof LinkedHashMap) {
						Map<String, Object> valueMap = (Map<String, Object>)value;
						ArrayList<Map<String,String>> userArray = (ArrayList<Map<String,String>>)valueMap.get("users");
						if(!CommonUtil.isEmpty(userArray)) {
							
							Map userMap = new HashMap();
							userMap.put("id", (String)userArray.get(0).get("id"));
							userMap.put("name", (String)userArray.get(0).get("name"));
							appLineSortingMap.put(keyIndex, userMap);
						}
					}
				}
				
				Map<String, Object> frmTaskApproval = (Map<String, Object>)requestBody.get("frmTaskApproval");
				String txtApprovalSubject = (String)frmTaskApproval.get("txtApprovalSubject");
				String txtApprovalComments = (String)frmTaskApproval.get("txtApprovalComments");
				
				//TODO 전자결재 참조업무 생성
//				Map<String, Object> txtApprovalForwardee = (Map<String, Object>)requestBody.get("txtApprovalForwardee");
//				ArrayList<Map<String, String>> forwardee = (ArrayList<Map<String,String>>)txtApprovalForwardee.get("users");
				
				if (appLineSortingMap != null && appLineSortingMap.size() != 0) {

					AprApprovalLine apprLine = new AprApprovalLine();
					
					AprApprovalDef[] aprAprDefs  = null;
					if (hdnApprovalLineId.equalsIgnoreCase("system.approvalLine.default.3level")) {
						ApprovalLine aprline = ApprovalLine.DEFAULT_APPROVAL_LINE_3_LEVEL;
						Approval[] aprs = aprline.getApprovals();
						aprAprDefs = new AprApprovalDef[aprs.length];
						for (int i = 0; i < aprs.length; i++) {
							aprAprDefs[i] = new AprApprovalDef();
							aprAprDefs[i].setName(aprs[i].getName());
							aprAprDefs[i].setType(aprs[i].getApproverType() + "");
						}
						apprLine.setName(aprline.getName());
					} else if (hdnApprovalLineId.equalsIgnoreCase("system.approvalLine.default.2level")) {
						ApprovalLine aprline = ApprovalLine.DEFAULT_APPROVAL_LINE_2_LEVEL;
						Approval[] aprs = aprline.getApprovals();
						aprAprDefs = new AprApprovalDef[aprs.length];
						for (int i = 0; i < aprs.length; i++) {
							aprAprDefs[i] = new AprApprovalDef();
							aprAprDefs[i].setName(aprs[i].getName());
							aprAprDefs[i].setType(aprs[i].getApproverType() + "");
						};
						apprLine.setName(aprline.getName());
					} else {
						AprApprovalLineDef aprAprLineDef = getAprManager().getApprovalLineDef(userId, hdnApprovalLineId, IManager.LEVEL_ALL);
						aprAprDefs = aprAprLineDef.getApprovalDefs();
						apprLine.setName(aprAprLineDef.getName());
					}
					
					apprLine.setStatus("created");

					AprApproval[] approvals = new AprApproval[appLineSortingMap.size()];
					
					for (int i = 1; i <= appLineSortingMap.size(); i++) {
						
						Map userMap = appLineSortingMap.get(i+"");
						String id = (String)userMap.get("id");
						String name = (String)userMap.get("name");

						AprApproval apr = new AprApproval();
						apr.setName(aprAprDefs[i-1].getName());
						apr.setType(aprAprDefs[i-1].getType());
						apr.setApprover(id);
						apr.setMandatory(true);
						apr.setModifiable(true);
						
						approvals[i-1] = apr;
					}
					apprLine.setApprovals(approvals);
					apprLine.setExtendedPropertyValue("recordId", recId);
					apprLine.setExtendedPropertyValue("txtApprovalComments", txtApprovalComments);
					apprLine.setExtendedPropertyValue("txtApprovalSubject", txtApprovalSubject);
					
					//참조한 approvallindef 가 있다면 그아이디를 입력한다
					apprLine.setRefAppLineDefId(hdnApprovalLineId);					

					obj.setExtendedAttributeValue("txtApprovalSubject", txtApprovalSubject);
					obj.setExtendedAttributeValue("txtApprovalComments", txtApprovalComments);
					obj.setExtendedAttributeValue("refAppLineDefId", hdnApprovalLineId);
					
					getAprManager().setApprovalLine(userId, apprLine, IManager.LEVEL_ALL);
					obj.setExtendedAttributeValue("approvalLine", apprLine.getObjId());
				}
			}
			// 전자결재 업무 끝

			//TODO 좋은방법이 멀까?
			String servletPath = request.getServletPath();
			if(servletPath.equals("/upload_new_picture.sw")) {
				obj.setExtendedAttributeValue("tskRefType", TskTask.TASKREFTYPE_IMAGE);
			} else if (servletPath.equals("/upload_new_file.sw")) {
				obj.setExtendedAttributeValue("tskRefType", TskTask.TASKREFTYPE_FILE);
			} else if (servletPath.equals("/create_new_event.sw")) {
				obj.setExtendedAttributeValue("tskRefType", TskTask.TASKREFTYPE_EVENT);
			} else if (servletPath.equals("/create_new_memo.sw")) {
				obj.setExtendedAttributeValue("tskRefType", TskTask.TASKREFTYPE_MEMO);
			} else if (servletPath.equals("/create_new_board.sw")) {
				obj.setExtendedAttributeValue("tskRefType", TskTask.TASKREFTYPE_BOARD);
			}

			instanceId = getSwdManager().setRecord(userId, obj, IManager.LEVEL_ALL);

			TskTaskCond tskCond = new TskTaskCond();
			tskCond.setExtendedProperties(new Property[] {new Property("recordId", instanceId)});
			tskCond.setModificationUser(userId);
			tskCond.setOrders(new Order[]{new Order(TskTaskCond.A_CREATIONDATE, false)});
			TskTask[] tskTasks = getTskManager().getTasks(userId, tskCond, IManager.LEVEL_LITE);
			String taskInstId = tskTasks[0].getObjId();

			String workType = "";
			if(servletPath.equals("/upload_new_picture.sw")) workType = "Pictures";
			else workType = "Files";

			if(fileGroupMap.size() > 0) {
				for(Map.Entry<String, List<Map<String, String>>> entry : fileGroupMap.entrySet()) {
					String fileGroupId = entry.getKey();
					List<Map<String, String>> fileGroups = entry.getValue();

					try {
						for(int i=0; i < fileGroups.subList(0, fileGroups.size()).size(); i++) {
							Map<String, String> file = fileGroups.get(i);
							String fileId = file.get("fileId");
							String fileName = file.get("fileName");
							String fileSize = file.get("fileSize");
							getDocManager().insertFiles(workType, taskInstId, fileGroupId, fileId, fileName, fileSize);
						}
					} catch (Exception e) {
						throw new DocFileException("file upload fail...");
					}
				}
			}
			if(groupId != null) {
				List<IFileModel> iFileModelList = getDocManager().findFileGroup(groupId);
				if(iFileModelList.size() > 0) {
					for(int i=0; i<iFileModelList.size(); i++) {
						IFileModel fileModel = iFileModelList.get(i);
						String fileId = fileModel.getId();
						String filePath = fileModel.getFilePath();
						if(fileModel.isDeleteAction()) {
							getDocManager().deleteFile(fileId);
							File f = new File(filePath);
							if(f.exists())
								f.delete();
						}
					}
				}
			}

			return instanceId;

		}catch (Exception e){
			e.printStackTrace();
			return null;			
		}
	}

	@Override
	public void removeInformationWorkInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		try{
			String workId = (String)requestBody.get("workId");
			String instanceId = (String)requestBody.get("instanceId");

			User user = SmartUtil.getCurrentUser();
			SwfFormCond swfFormCond = new SwfFormCond();
			swfFormCond.setCompanyId(user.getCompanyId());
			swfFormCond.setPackageId(workId);
	
			SwfForm[] swfForms = getSwfManager().getForms(user.getId(), swfFormCond, IManager.LEVEL_LITE);
	
			SwdRecordCond swdRecordCond = new SwdRecordCond();
			swdRecordCond.setPackageId(workId);
			swdRecordCond.setFormId(swfForms[0].getId());
			swdRecordCond.setRecordId(instanceId);
	
			//getSwdManager().removeRecord(user.getId(), swdRecordCond);
			
			// 삭제할 레코드 조회
			SwdRecord record = getSwdManager().getRecord(user.getId(), swdRecordCond, IManager.LEVEL_LITE);
			if (record == null)
				return;
			
			// 삭제할 도메인 아이디 조회
			String domainId = record.getDomainId();
			if (domainId == null) {
				SwdDomainCond domainCond = new SwdDomainCond();
				domainCond.setFormId(record.getFormId());
				SwdDomain domain = getSwdManager().getDomain(user.getId(), domainCond, IManager.LEVEL_LITE);
				domainId = domain.getObjId();
			}

			getSwdManager().removeRecord(user.getId(), record.getDomainId(), record.getRecordId());
			
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			// Exception Handling Required			
		}
	}

	@Override
	public String addCommentOnWork(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		try{
			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();
			if(CommonUtil.isEmpty(userId))
				return null;

			String workId = (String)requestBody.get("workId");
			String comment = (String)requestBody.get("comment");
			int refType = 6;

			Opinion opinion = new Opinion();
			opinion.setRefType(refType);
			opinion.setRefId(workId);
			opinion.setOpinion(comment);

			getOpinionManager().setOpinion(userId, opinion, IManager.LEVEL_ALL);

			return opinion.getObjId();

		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			// Exception Handling Required			
		}
		return null;
	}

	@Override
	public void updateCommentOnWork(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		try {
			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();
			if(CommonUtil.isEmpty(userId))
				return;

			String commentId = (String)requestBody.get("commentId");
			String comment = (String)requestBody.get("comment");

			Opinion opinion = new Opinion();
			opinion.setObjId(commentId);
			opinion.setOpinion(comment);

			getOpinionManager().setOpinion(userId, opinion, IManager.LEVEL_ALL);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void removeCommentFromWork(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		try {
			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();
			if(CommonUtil.isEmpty(userId))
				return;

			String commentId = (String)requestBody.get("commentId");

			Opinion opinion = new Opinion();
			opinion.setObjId(commentId);

			getOpinionManager().setOpinion(userId, opinion, IManager.LEVEL_ALL);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String addCommentOnInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		try{
			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();
			if(CommonUtil.isEmpty(userId))
				return null;

			int workType = (Integer)requestBody.get("workType");
			String workInstanceId = (String)requestBody.get("workInstanceId");
			String comment = (String)requestBody.get("comment");
			int refType = 0;
			String refDomainId = null; 
			String refFormId = null;
			TskTaskCond tskCond = new TskTaskCond();
			TskTask[] tskTasks = null;
			TskTask tskTask = null;
			if(!CommonUtil.isEmpty(workInstanceId)) {
				if(workType == SmartWork.TYPE_INFORMATION || workType == SocialWork.TYPE_MEMO || workType == SocialWork.TYPE_EVENT || workType == SocialWork.TYPE_BOARD
						 || workType == SocialWork.TYPE_FILE || workType == SocialWork.TYPE_IMAGE || workType == SocialWork.TYPE_YTVIDEO) {
					tskCond.setExtendedProperties(new Property[] {new Property("recordId", workInstanceId)});
					tskCond.setOrders(new Order[]{new Order(TskTaskCond.A_MODIFICATIONDATE, false)});
					tskTasks = getTskManager().getTasks(userId, tskCond, IManager.LEVEL_LITE);
					tskTask = tskTasks[0];
					String def = tskTask.getDef();
					if (!CommonUtil.isEmpty(def)) {
						String[] defArray = StringUtils.tokenizeToStringArray(def, "|");	
						refDomainId = defArray[0];
					}
					refFormId = tskTask.getForm();
					refType = 4;
				} else if(workType == SmartWork.TYPE_PROCESS) {
					refType = 2;
					
				}
			}

			Opinion opinion = new Opinion();
			opinion.setRefType(refType);
			opinion.setRefId(workInstanceId);
			opinion.setRefDomainId(refDomainId);
			opinion.setRefFormId(refFormId);
			opinion.setOpinion(comment);

			getOpinionManager().setOpinion(userId, opinion, IManager.LEVEL_ALL);
			
			if (workType == SmartWork.TYPE_INFORMATION || workType == SocialWork.TYPE_MEMO || workType == SocialWork.TYPE_EVENT || workType == SocialWork.TYPE_BOARD
					 || workType == SocialWork.TYPE_FILE || workType == SocialWork.TYPE_IMAGE || workType == SocialWork.TYPE_YTVIDEO) {
				if (tskTask != null) {
					PublishNotice pubNoticeObj = new PublishNotice(tskTask.getAssignee(), PublishNotice.TYPE_COMMENT, PublishNotice.REFTYPE_COMMENT_INFORWORK, opinion.getObjId());
					getPublishNoticeManager().setPublishNotice("linkadvisor", pubNoticeObj, IManager.LEVEL_ALL);
					SmartUtil.increaseNoticeCountByNoticeType(tskTask.getAssignee(), Notice.TYPE_COMMENT);
				}
			} else if (workType == SmartWork.TYPE_PROCESS) {
				TskTaskCond cond = new TskTaskCond();
				cond.setProcessInstId(workInstanceId);
				cond.setType(TskTask.TASKTYPE_COMMON);
				TskTask[] tasks = getTskManager().getTasks(userId, cond, IManager.LEVEL_LITE);
				if (tasks != null) {
					List assigneeIdList = new ArrayList();
					for (int i = 0; i < tasks.length; i++) {
						if (!assigneeIdList.contains(tasks[i].getAssignee())) {
							PublishNotice pubNoticeObj = new PublishNotice(tasks[i].getAssignee(), PublishNotice.TYPE_COMMENT, PublishNotice.REFTYPE_COMMENT_INFORWORK, opinion.getObjId());
							getPublishNoticeManager().setPublishNotice("linkadvisor", pubNoticeObj, IManager.LEVEL_ALL);
							SmartUtil.increaseNoticeCountByNoticeType(tasks[i].getAssignee(), Notice.TYPE_COMMENT);
							assigneeIdList.add(tasks[i].getAssignee());
						}
					}
				}
			}
			return opinion.getObjId();
		} catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			// Exception Handling Required			
		}
		return null;
	}

	@Override
	public void updateCommentOnInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		try {
			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();
			if(CommonUtil.isEmpty(userId))
				return;
	
			String commentId = (String)requestBody.get("commentId");
			String comment = (String)requestBody.get("comment");

			Opinion opinion = new Opinion();
			opinion.setObjId(commentId);
			opinion.setOpinion(comment);

			getOpinionManager().setOpinion(userId, opinion, IManager.LEVEL_ALL);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void removeCommentFromInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		try {
			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();
			if(CommonUtil.isEmpty(userId))
				return;

			String commentId = (String)requestBody.get("commentId");

			Opinion opinion = new Opinion();
			opinion.setObjId(commentId);

			getOpinionManager().setOpinion(userId, opinion, IManager.LEVEL_ALL);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private SwdRecord getSwdRecordByRequestBody_old(String userId, SwdField[] swdFields, Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		
		try{
			if (CommonUtil.isEmpty(swdFields))
				return null;//TODO return null? throw new Exception??
	
			Map<String, Object> smartFormInfoMap = (Map<String, Object>)requestBody.get("frmSmartForm");
	
			String domainId = null; // domainId 가 없어도 내부 서버에서 폼아이디로 검색하여 저장
			String formId = (String)requestBody.get("formId");
			String formName = (String)requestBody.get("formName");
			String instanceId = (String)requestBody.get("instanceId");
			int formVersion = 1;
			
			Map<String, SwdField> fieldInfoMap = new HashMap<String, SwdField>();
			for (SwdField field : swdFields) {
				fieldInfoMap.put(field.getFormFieldId(), field);
			}
			
			Set<String> keySet = smartFormInfoMap.keySet();
			Iterator<String> itr = keySet.iterator();
			
	//		SwdField[] fieldDatas = new SwdField[keySet.size()];
			List fieldDataList = new ArrayList();
			List<Map<String, String>> files = null;
			List<Map<String, String>> users = null;
			String groupId = null;
			while (itr.hasNext()) {
				String fieldId = (String)itr.next();
				String value = null;
				String refForm = null;
				String refFormField = null;
				String refRecordId = null;
				Object fieldValue = smartFormInfoMap.get(fieldId);
				if (fieldValue instanceof LinkedHashMap) {
					Map<String, Object> valueMap = (Map<String, Object>)fieldValue;
					groupId = (String)valueMap.get("groupId");
					refForm = (String)valueMap.get("refForm");
					users = (ArrayList<Map<String,String>>)valueMap.get("users");
	
					if(!CommonUtil.isEmpty(groupId)) {
						files = (ArrayList<Map<String,String>>)valueMap.get("files");
						if(files != null && files.size() > 0)
							value = groupId;
					} else if(!CommonUtil.isEmpty(refForm)) {
						refFormField = (String)valueMap.get("refFormField");
						refRecordId = (String)valueMap.get("refRecordId");
						SwoDepartmentCond swoDepartmentCond = new SwoDepartmentCond();
						swoDepartmentCond.setId(refRecordId);
						String deptName = getSwoManager().getDepartment(userId, swoDepartmentCond, IManager.LEVEL_LITE).getName();
						value = deptName;
					} else if(!CommonUtil.isEmpty(users)) {
						refFormField = "frm_user_SYSTEM"; 
						String resultRefRecordId = "";
						String resultValue = "";
						String symbol = ";";
						if(users.size() == 1) {
							resultRefRecordId = users.get(0).get("id");
							resultValue = users.get(0).get("name");
						} else {
							for(int i=0; i < users.subList(0, users.size()).size(); i++) {
								Map<String, String> user = users.get(i);
								resultRefRecordId += user.get("id") + symbol;
								resultValue += user.get("name") + symbol;
							}
						}
						refRecordId = resultRefRecordId;
						value = resultValue;
					}
				} else if(fieldValue instanceof String) {
					value = (String)smartFormInfoMap.get(fieldId);
					if(formId.equals(SmartForm.ID_MEMO_MANAGEMENT)) {
						if(fieldId.equals("12"))
							value = StringUtil.subString(value, 0, 20, "...");
					} else if(formId.equals(SmartForm.ID_EVENT_MANAGEMENT)) {
						if(fieldId.equals("1") || fieldId.equals("2")) {
							if(!value.isEmpty())
								value = LocalDate.convertStringToLocalDate(value).toGMTDateString();
						}
					}
				}
				if (CommonUtil.isEmpty(value))
					continue;
				SwdDataField fieldData = new SwdDataField();
				fieldData.setId(fieldId);
				fieldData.setName(fieldInfoMap.get(fieldId).getFormFieldName());
				fieldData.setRefForm(refForm);
				fieldData.setRefFormField(refFormField);
				fieldData.setRefRecordId(refRecordId);
				fieldData.setValue(value);
	
				fieldDataList.add(fieldData);
				
			}
			String workType = "";
			String servletPath = request.getServletPath();
			if(servletPath.equals("/upload_new_picture.sw"))
				workType = "Pictures";
			else
				workType = "Files";
	
			SwdDataField[] fieldDatas = new SwdDataField[fieldDataList.size()];
			fieldDataList.toArray(fieldDatas);
			SwdRecord obj = new SwdRecord();
			obj.setDomainId(domainId);
			obj.setFormId(formId);
			obj.setFormName(formName);
			obj.setFormVersion(formVersion);
			obj.setDataFields(fieldDatas);
			obj.setRecordId(instanceId);
	
			if(files != null && files.size() > 0) {
				try {
					for(int i=0; i < files.subList(0, files.size()).size(); i++) {
						Map<String, String> file = files.get(i);
						String fileId = file.get("fileId");
						String fileName = file.get("fileName");
						String fileSize = file.get("fileSize");
						getDocManager().insertFiles(workType, "", groupId, fileId, fileName, fileSize);
					}
				} catch (Exception e) {
					throw new DocFileException("file upload fail...");
				}
			}
			
			return obj;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}
		
	private SwdRecord getSwdRecordByRequestBody(String userId, SwdField[] swdFields, Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		try{
			if (CommonUtil.isEmpty(swdFields))
				return null;//TODO return null? throw new Exception??
	
			Map<String, Object> smartFormInfoMap = (Map<String, Object>)requestBody.get("frmSmartForm");
	
			String domainId = null; // domainId 가 없어도 내부 서버에서 폼아이디로 검색하여 저장
			String formId = (String)requestBody.get("formId");
			String formName = (String)requestBody.get("formName");
			String instanceId = (String)requestBody.get("instanceId");
			int formVersion = 1;

			Map<String, SwdField> fieldInfoMap = new HashMap<String, SwdField>();
			for (SwdField field : swdFields) {
				fieldInfoMap.put(field.getFormFieldId(), field);
			}
			
			Set<String> keySet = fieldInfoMap.keySet();
			Iterator<String> itr = keySet.iterator();
			
	//		SwdField[] fieldDatas = new SwdField[keySet.size()];
			List fieldDataList = new ArrayList();
			List<Map<String, String>> files = null;
			List<Map<String, String>> users = null;
			Map<String, List<Map<String, String>>> fileGroupMap = new HashMap<String, List<Map<String,String>>>();
			String groupId = null;
			while (itr.hasNext()) {
				String fieldId = (String)itr.next();
				String value = null;
				String refForm = null;
				String refFormField = null;
				String refRecordId = null;
				SwdField fieldTemp = fieldInfoMap.get(fieldId);
				if (fieldTemp.getFormFieldType().equalsIgnoreCase("boolean")) {
					value = "false";
				}
				
				Object fieldValue = smartFormInfoMap.get(fieldId);
				if (fieldValue instanceof LinkedHashMap) {
					Map<String, Object> valueMap = (Map<String, Object>)fieldValue;
					groupId = (String)valueMap.get("groupId");
					refForm = (String)valueMap.get("refForm");
					users = (ArrayList<Map<String,String>>)valueMap.get("users");
	
					if(!CommonUtil.isEmpty(groupId)) {
						files = (ArrayList<Map<String,String>>)valueMap.get("files");
						value = groupId;
						if(!CommonUtil.isEmpty(files)) {
							fileGroupMap.put(groupId, files);
						}
					} else if(!CommonUtil.isEmpty(refForm)) {
						refFormField = (String)valueMap.get("refFormField");
						refRecordId = (String)valueMap.get("refRecordId");
						SwdRecordCond cond = new SwdRecordCond();
						cond.setFormId(refForm);
						cond.setRecordId(refRecordId);
						SwdRecord refRecord = getSwdManager().getRecord(userId, cond, IManager.LEVEL_LITE);
						
						if (refRecord != null) {
							value = refRecord.getDataFieldValue(refFormField);
						}
//						SwoDepartmentCond swoDepartmentCond = new SwoDepartmentCond();
//						swoDepartmentCond.setId(refRecordId);
//						String deptName = "";
//						SwoDepartment swoDepartment = getSwoManager().getDepartment(userId, swoDepartmentCond, IManager.LEVEL_LITE);
//						if(swoDepartment != null)
//							deptName = swoDepartment.getName();
//						value = deptName;
					} else if(!CommonUtil.isEmpty(users)) {
						refFormField = "frm_user_SYSTEM"; 
						String resultRefRecordId = "";
						String resultValue = "";
						String symbol = ";";
						if(users.size() == 1) {
							resultRefRecordId = users.get(0).get("id");
							resultValue = users.get(0).get("name");
						} else {
							for(int i=0; i < users.subList(0, users.size()).size(); i++) {
								Map<String, String> user = users.get(i);
								resultRefRecordId += user.get("id") + symbol;
								resultValue += user.get("name") + symbol;
							}
						}
						refRecordId = resultRefRecordId;
						value = resultValue;
					}
				} else if(fieldValue instanceof String) {
					value = (String)smartFormInfoMap.get(fieldId);
					if(formId.equals(SmartForm.ID_MEMO_MANAGEMENT)) {
						if(fieldId.equals("12"))
							value = StringUtil.subString(value, 0, 20, "...");
					} else if(formId.equals(SmartForm.ID_EVENT_MANAGEMENT)) {
						if(fieldId.equals("1") || fieldId.equals("2")) {
							if(!value.isEmpty())
								value = LocalDate.convertStringToLocalDate(value).toGMTDateString();
						}
					}
				} else if (fieldValue instanceof Integer) {
					Integer intValue = (Integer)smartFormInfoMap.get(fieldId);
					value = intValue + "";
				}
	//			if (CommonUtil.isEmpty(value))
	//				continue;
				SwdDataField fieldData = new SwdDataField();
				fieldData.setId(fieldId);
				fieldData.setName(fieldInfoMap.get(fieldId).getFormFieldName());
				fieldData.setRefForm(refForm);
				fieldData.setRefFormField(refFormField);
				fieldData.setRefRecordId(refRecordId);
				fieldData.setValue(value);

				fieldDataList.add(fieldData);

			}

			SwdDataField[] fieldDatas = new SwdDataField[fieldDataList.size()];
			fieldDataList.toArray(fieldDatas);
			SwdRecord obj = new SwdRecord();
			obj.setDomainId(domainId);
			obj.setFormId(formId);
			obj.setFormName(formName);
			obj.setFormVersion(formVersion);
			obj.setDataFields(fieldDatas);
			obj.setRecordId(instanceId);
			obj.setFileGroupId(groupId);
			obj.setFileGroupMap(fileGroupMap);

			return obj;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}

	@Override
	public String startProcessWorkInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		/*{
			"workId":"pkg_cf3b0087995f4f99a41c93e2fe95b22d",
			"formId":"frm_c19b1fe4bceb4732acbb8a4cd2a57474",
			"formName":"기안품의",
			"frmSmartForm":
				{
					"4":
						{
							"users":
								[
									{
										"id":"kmyu@maninsoft.co.kr",
										"name":"\n\t\t\t\t\t\t\t\t연구소장 유광민\n\t\t\t\t\t\t\t"
									}
								]
						},
					"16":
						{
							"users":
								[
									{
										"id":"kmyu@maninsoft.co.kr",
										"name":"\n\t\t\t\t\t\t\t\t연구소장 유광민\n\t\t\t\t\t\t\t"
									}
								]
						},
					"92":"1",
					"535":"1"
				},
			"frmScheduleWork":
				{
				},
			"frmAccessSpace":
				{
					"selWorkSpace":"kmyu@maninsoft.co.kr",
					"selWorkSpaceType": 4(ISmartWorks.java 에 정의 되어 있음), 
					"selAccessLevel":"3",
					"txtAccessableUsers":
						{
							"users":
								[
								]
						}
				}
			}*/

		try{
			User cuser = SmartUtil.getCurrentUser();
			String userId = null;
			if (cuser != null)
				userId = cuser.getId();
			
			//패키지 정보로 프로세스 정보를 얻는다.
			String packageId = (String)requestBody.get("workId");
			String formId = (String)requestBody.get("formId");
			PrcProcessCond cond = new PrcProcessCond();
			cond.setDiagramId(packageId);
			PrcProcess[] prcs = getPrcManager().getProcesses(userId, cond, IManager.LEVEL_LITE);
			if (prcs == null || prcs.length != 1)
				throw new PrcException("Start Process Is Null Or More then 1");
			PrcProcess prc = prcs[0];
			String processId = prc.getProcessId();
			
			//패키지 정보로 프로세스 첫번째 taskdef를 찾는다
			Property[] extProps = new Property[] {new Property("processId", processId), new Property("startActivity", "true")};
			TskTaskDefCond taskCond = new TskTaskDefCond();
			taskCond.setExtendedProperties(extProps);
			TskTaskDef[] taskDefs = getTskManager().getTaskDefs(userId, taskCond, IManager.LEVEL_LITE);
			if (CommonUtil.isEmpty(taskDefs))
				throw new Exception(new StringBuffer("No start activity. -> processId:").append(processId).toString());
			TskTaskDef taskDef = taskDefs[0];
			String taskDefId = taskDef.getObjId();
			
			//넘어온 frmSamrtForm 정보로 레코드를 생성한다
			SwfForm form = getSwfManager().getForm(userId, formId);
			SwfField[] formFields = form.getFields();
			List domainFieldList = new ArrayList();
			
			//제목으로 사용할 필드 (필수>단문>첫번째)
			List requiredFieldIdList = new ArrayList();
			List textInputFieldIdList = new ArrayList();
			for (SwfField field: formFields) {
				//제목으로 사용할 필드 (필수>단문>첫번째)
				if (field.isRequired() && field.getFormat().getType().equals("textInput"))
					requiredFieldIdList.add(field.getId());
				//제목으로 사용할 필드 (필수>단문>첫번째)
				if (field.getFormat().getType().equals("textInput"))
					textInputFieldIdList.add(field.getId());
				SwdField domainField = new SwdField();
				domainField.setFormFieldId(field.getId());
				domainField.setFormFieldName(field.getName());
				domainField.setFormFieldType(field.getSystemType());
				domainField.setArray(field.isArray());
				domainField.setSystemField(field.isSystem());
				domainFieldList.add(domainField);
			}
			SwdField[] domainFields = new SwdField[domainFieldList.size()];
			domainFieldList.toArray(domainFields);
			
			SwdRecord recordObj = getSwdRecordByRequestBody(userId, domainFields, requestBody, request);
			String taskDocument = null;
			Map<String, List<Map<String, String>>> fileGroupMap = null;
			if (recordObj != null) {
				taskDocument = recordObj.toString();
				fileGroupMap = recordObj.getFileGroupMap();
			}

			//TODO 참조자, 전자결재, 연결업무 정보를 셋팅한다
			
			String title = null;
			if (requiredFieldIdList.size() != 0) {
				for (int i = 0; i < requiredFieldIdList.size(); i++) {
					String temp = recordObj.getDataFieldValue((String)requiredFieldIdList.get(i));
					if (!CommonUtil.isEmpty(temp)) {
						title = temp;
						break;
					}
				}
			} else {
				for (int i = 0; i < textInputFieldIdList.size(); i++) {
					String temp = recordObj.getDataFieldValue((String)textInputFieldIdList.get(i));
					if (!CommonUtil.isEmpty(temp)) {
						title = temp;
						break;
					}
					
				}
			}
			
			//태스크를 생성하여 실행한다
			TskTask task = new TskTask();
			task.setType(taskDef.getType());
			task.setName(taskDef.getName());
			task.setTitle(CommonUtil.toDefault(title, taskDef.getName() + "(No Title) - " + new LocalDate()));
			task.setAssignee(userId);
			task.setAssigner(userId);
			task.setForm(taskDef.getForm());
			task.setDef(taskDef.getObjId());
			task.setIsStartActivity("true");

			Map<String, Object> frmAccessSpaceMap = (Map<String, Object>)requestBody.get("frmAccessSpace");
			if(!CommonUtil.isEmpty(frmAccessSpaceMap)) {
				Set<String> keySet = frmAccessSpaceMap.keySet();
				Iterator<String> itr = keySet.iterator();
				List<Map<String, String>> users = null;
				String workSpaceId = null;
				String workSpaceType = null;
				String accessLevel = null;
				String accessValue = null;

				while (itr.hasNext()) {
					String fieldId = (String)itr.next();
					Object fieldValue = frmAccessSpaceMap.get(fieldId);
					if (fieldValue instanceof LinkedHashMap) {
						Map<String, Object> valueMap = (Map<String, Object>)fieldValue;
						users = (ArrayList<Map<String,String>>)valueMap.get("users");
						if(!CommonUtil.isEmpty(users)) {
							String symbol = ";";
							if(users.size() == 1) {
								accessValue = users.get(0).get("id");
							} else {
								accessValue = "";
								for(int i=0; i < users.subList(0, users.size()).size(); i++) {
									Map<String, String> user = users.get(i);
									accessValue += user.get("id") + symbol;
								}
							}
						}
					} else if(fieldValue instanceof String) {
						if(fieldId.equals("selWorkSpace")) {
							workSpaceId = (String)fieldValue;
						} else if(fieldId.equals("selWorkSpaceType")) {
							workSpaceType = (String)fieldValue;
						} else if(fieldId.equals("selAccessLevel")) {
							accessLevel = (String)fieldValue;
						}
					}
				}

				task.setWorkSpaceId(workSpaceId);
				task.setWorkSpaceType(workSpaceType);
				task.setAccessLevel(accessLevel);
				task.setAccessValue(accessValue);
			}
			task.setDocument(taskDocument);

			//date to localdate - Date now = new Date();
			LocalDate now = new LocalDate();
			task.setExpectStartDate(new LocalDate(now.getTime()));
			task.setRealStartDate(new LocalDate(now.getTime()));
			//date to localdate - Date expectEndDate = new Date();
			LocalDate expectEndDate = new LocalDate();
			if (taskDef != null &&  !CommonUtil.isEmpty(taskDef.getDueDate())) {
				//dueDate 는 분단위로 설정이 되어 있다
				expectEndDate.setTime(new LocalDate(now.getTime() + ((Long.parseLong(taskDef.getDueDate())) * 60 * 1000)).getTime());
			} else {
				expectEndDate.setTime(new LocalDate(now.getTime() + 1800000).getTime());
			}
			task.setExpectEndDate(expectEndDate);
			task = getTskManager().executeTask(userId, task, "execute");

			String taskInstId = task.getObjId();

			if(fileGroupMap.size() > 0) {
				for(Map.Entry<String, List<Map<String, String>>> entry : fileGroupMap.entrySet()) {
					String fileGroupId = entry.getKey();
					List<Map<String, String>> fileGroups = entry.getValue();

					try {
						for(int i=0; i < fileGroups.subList(0, fileGroups.size()).size(); i++) {
							Map<String, String> file = fileGroups.get(i);
							String fileId = file.get("fileId");
							String fileName = file.get("fileName");
							String fileSize = file.get("fileSize");
							getDocManager().insertFiles("Files", taskInstId, fileGroupId, fileId, fileName, fileSize);
						}
					} catch (Exception e) {
						throw new DocFileException("file upload fail...");
					}
				}
			}

			return task.getProcessInstId();
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}

	@Override
	public String setFileInstance(HttpServletRequest request) throws Exception {
		try{
			return "testId";
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}

	@Override
	public String setEventInstance(HttpServletRequest request) throws Exception {
		try{
			return "testId";		
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}

	@Override
	public String setBoardInstance(HttpServletRequest request) throws Exception {
		try{
			return "testId";		
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}

	@Override
	public CommentInstanceInfo[] getRecentCommentsInWorkManual(String workId, int length) throws Exception {
		try{
			User cuser = SmartUtil.getCurrentUser();
			String userId = null;
			if (cuser != null)
				userId = cuser.getId();

			CommentInstanceInfo[] commentInstanceInfos = null;
			List<CommentInstanceInfo> commentInstanceInfosList = new ArrayList<CommentInstanceInfo>();
			OpinionCond opinionCond = new OpinionCond();
			opinionCond.setRefId(workId);
			opinionCond.setPageSize(length);
			opinionCond.setOrders(new Order[]{new Order(MisObjectCond.A_CREATIONDATE, false)});
			Opinion[] opinions = getOpinionManager().getOpinions(userId, opinionCond, IManager.LEVEL_ALL);
			if(!CommonUtil.isEmpty(opinions)) {
				int opinionLength = opinions.length;
				for(int i=0; i<opinionLength; i++) {
					Opinion opinion = opinions[i];
					CommentInstanceInfo commentInstanceInfo = new CommentInstanceInfo();
					String modificationUser = opinion.getModificationUser() == null ? opinion.getCreationUser() : opinion.getModificationUser();
					Date modificationDate = opinion.getModificationDate() == null ? opinion.getCreationDate() : opinion.getModificationDate();
					commentInstanceInfo.setId(opinion.getObjId());
					commentInstanceInfo.setCommentType(CommentInstance.COMMENT_TYPE_ON_WORK_MANUAL);
					commentInstanceInfo.setComment(opinion.getOpinion());
					commentInstanceInfo.setCommentor(ModelConverter.getUserInfoByUserId(opinion.getCreationUser()));
					commentInstanceInfo.setLastModifiedDate(new LocalDate(modificationDate.getTime()));
					commentInstanceInfo.setType(Instance.TYPE_COMMENT);
					commentInstanceInfo.setOwner(ModelConverter.getUserInfoByUserId(opinion.getCreationUser()));
					commentInstanceInfo.setCreatedDate(new LocalDate(opinion.getCreationDate().getTime()));
					commentInstanceInfo.setLastModifier(ModelConverter.getUserInfoByUserId(modificationUser));
					commentInstanceInfosList.add(commentInstanceInfo);
				}
			}
			if(commentInstanceInfosList.size() > 0) {
				commentInstanceInfos = new CommentInstanceInfo[commentInstanceInfosList.size()];
				commentInstanceInfosList.toArray(commentInstanceInfos);
			}
			return commentInstanceInfos;

		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required
		}
	}

	@Override
	public InstanceInfo[] getRecentSubInstancesInInstance(String instanceId, int length) throws Exception {
		try{
			User cuser = SmartUtil.getCurrentUser();
			String userId = null;
			if (cuser != null)
				userId = cuser.getId();
			
			TaskWorkCond cond = new TaskWorkCond();
			cond.setTskWorkSpaceId(instanceId);
			cond.setTskStatus(TskTask.TASKSTATUS_COMPLETE);
			
			long tasksSize = getWorkListManager().getTaskWorkListSize(userId, cond);
			
			InstanceInfo[] subInstancesInInstances = null;
			List<InstanceInfo> instanceInfoList = new ArrayList<InstanceInfo>();
			
			if (tasksSize != 0) {
				
				cond.setOrders(new Order[]{new Order("taskLastModifyDate", true)});
				if(length == WorkInstance.DEFAULT_SUB_INSTANCE_FETCH_COUNT)
					cond.setPageSize(length);
				
				TaskWork[] tasks = getWorkListManager().getTaskWorkList(userId, cond);
				List<String> prcInstIdList = new ArrayList<String>();
				if(!CommonUtil.isEmpty(tasks)) {
					for (int i = 0; i < tasks.length; i++) {
						TaskWork task = tasks[i];
						if (instanceInfoList.size() == 10)
							break;
						if (prcInstIdList.contains(task.getTskPrcInstId()))
							continue;
						prcInstIdList.add(task.getTskPrcInstId());
						instanceInfoList.add(ModelConverter.getWorkInstanceInfoByTaskWork(task));
					}
				}
			}

			OpinionCond opinionCond = new OpinionCond();
			opinionCond.setRefId(instanceId);

			long opinionsSize = getOpinionManager().getOpinionSize(userId, opinionCond);
			
			if (opinionsSize != 0) {

				if(length == WorkInstance.DEFAULT_SUB_INSTANCE_FETCH_COUNT)
					opinionCond.setPageSize(length);
				
				Opinion[] opinions = getOpinionManager().getOpinions(userId, opinionCond, IManager.LEVEL_ALL);
				if(!CommonUtil.isEmpty(opinions)) {
					int opinionLength = opinions.length;
					for(int i=0; i<opinionLength; i++) {
						Opinion opinion = opinions[i];
						CommentInstanceInfo commentInstanceInfo = new CommentInstanceInfo();
						String modificationUser = opinion.getModificationUser() == null ? opinion.getCreationUser() : opinion.getModificationUser();
						Date modificationDate = opinion.getModificationDate() == null ? opinion.getCreationDate() : opinion.getModificationDate();
						commentInstanceInfo.setId(opinion.getObjId());
						commentInstanceInfo.setCommentType(CommentInstance.COMMENT_TYPE_ON_WORK_MANUAL);
						commentInstanceInfo.setComment(opinion.getOpinion());
						commentInstanceInfo.setCommentor(ModelConverter.getUserInfoByUserId(opinion.getCreationUser()));
						commentInstanceInfo.setLastModifiedDate(new LocalDate(modificationDate.getTime()));
						commentInstanceInfo.setType(Instance.TYPE_COMMENT);
						commentInstanceInfo.setOwner(ModelConverter.getUserInfoByUserId(opinion.getCreationUser()));
						commentInstanceInfo.setCreatedDate(new LocalDate(opinion.getCreationDate().getTime()));
						commentInstanceInfo.setLastModifier(ModelConverter.getUserInfoByUserId(modificationUser));;
						instanceInfoList.add(commentInstanceInfo);
					}
				}
			}
			
			if(instanceInfoList.size() > 0) {
				Collections.sort(instanceInfoList);
				subInstancesInInstances = new InstanceInfo[instanceInfoList.size()];
				instanceInfoList.toArray(subInstancesInInstances);
			}

			if(length == WorkInstance.DEFAULT_SUB_INSTANCE_FETCH_COUNT) {
				if(!CommonUtil.isEmpty(subInstancesInInstances)) {
					if(subInstancesInInstances.length > length) {
						List<InstanceInfo> resultInstanceInfoList = new ArrayList<InstanceInfo>();
						for(int i=0; i<length; i++) {
							InstanceInfo instanceInfo = subInstancesInInstances[i];
							resultInstanceInfoList.add(instanceInfo);
						}
						subInstancesInInstances = new InstanceInfo[resultInstanceInfoList.size()];
						resultInstanceInfoList.toArray(subInstancesInInstances);
					}
				}
			}

			return subInstancesInInstances;

		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}

	int previousPageSize = 0;
	@Override
	public InstanceInfoList getIWorkInstanceList(String workId, RequestParams params) throws Exception {

		try {
			User user = SmartUtil.getCurrentUser();
			if(user == null)
				return null;
			String userId = user.getId();

			SwdDomainCond swdDomainCond = new SwdDomainCond();
			swdDomainCond.setCompanyId(user.getCompanyId());

			SwfFormCond swfFormCond = new SwfFormCond();
			swfFormCond.setPackageId(workId);

			swdDomainCond.setFormId(getSwfManager().getForms(userId, swfFormCond, IManager.LEVEL_LITE)[0].getId());

			SwdDomain swdDomain = getSwdManager().getDomain(userId, swdDomainCond, IManager.LEVEL_LITE);

			if(swdDomain == null)
				return null;

			SwdRecordCond swdRecordCond = new SwdRecordCond();
			swdRecordCond.setCompanyId(user.getCompanyId());
			swdRecordCond.setFormId(swdDomain.getFormId());
			swdRecordCond.setDomainId(swdDomain.getObjId());

			SearchFilter searchFilter = params.getSearchFilter();
			List<Filter> filterList = new ArrayList<Filter>();
			if(searchFilter != null) {
				Condition[] conditions = searchFilter.getConditions();
				for(Condition condition : conditions) {
					Filter filter = new Filter();
	
					FormField leftOperand = condition.getLeftOperand();
					String formFieldId = leftOperand.getId();
					String tableColName = formFieldId;
					if(!formFieldId.equals(FormField.ID_OWNER) && !formFieldId.equals(FormField.ID_CREATED_DATE) && !formFieldId.equals(FormField.ID_LAST_MODIFIER) && !formFieldId.equals(FormField.ID_LAST_MODIFIED_DATE))
						tableColName = getSwdManager().getTableColName(swdDomain.getObjId(), formFieldId);

					String formFieldType = leftOperand.getType();
					String operator = condition.getOperator();
					String rightOperand = (String)condition.getRightOperand();

					filter.setLeftOperandType(formFieldType);
					filter.setLeftOperandValue(tableColName);
					filter.setOperator(operator);
					filter.setRightOperandType(formFieldType);
					filter.setRightOperandValue(rightOperand);
					filterList.add(filter);
				}

				Filter[] filters = new Filter[filterList.size()];
				filterList.toArray(filters);

				swdRecordCond.setFilter(filters);
			}

			String filterId = params.getFilterId();

			LocalDate priviousDate = new LocalDate(new LocalDate().getTime() - LocalDate.ONE_DAY*7);

			if(filterId != null) {
				if(filterId.equals(SearchFilter.FILTER_ALL_INSTANCES)) {
				} else if(filterId.equals(SearchFilter.FILTER_MY_INSTANCES)) {
					swdRecordCond.addFilter(new Filter("=", FormField.ID_LAST_MODIFIER, Filter.OPERANDTYPE_STRING, userId));
				} else if(filterId.equals(SearchFilter.FILTER_RECENT_INSTANCES)) {
					swdRecordCond.addFilter(new Filter(">=", FormField.ID_LAST_MODIFIED_DATE, Filter.OPERANDTYPE_DATE, priviousDate.toGMTSimpleDateString()));
				} else if(filterId.equals(SearchFilter.FILTER_MY_RECENT_INSTANCES)) {
					swdRecordCond.addFilter(new Filter("=", FormField.ID_LAST_MODIFIER, Filter.OPERANDTYPE_STRING, userId));
					swdRecordCond.addFilter(new Filter(">=", FormField.ID_LAST_MODIFIED_DATE, Filter.OPERANDTYPE_DATE, priviousDate.toGMTSimpleDateString()));
				} else {
					searchFilter = ModelConverter.getSearchFilterByFilterId(SwfFormModel.TYPE_SINGLE, workId, filterId);
					if (searchFilter != null) {
						Condition[] conditions = searchFilter.getConditions();
						Filters filters = new Filters();
						filterList = new ArrayList<Filter>();
						for(Condition condition : conditions) {
							Filter filter = new Filter();
							FormField leftOperand = condition.getLeftOperand();
							String lefOperandType = leftOperand.getType();
							String operator = condition.getOperator();
							Object rightOperand = condition.getRightOperand();
							String rightOperandValue = "";
							if(rightOperand instanceof User) {
								rightOperandValue = ((User)rightOperand).getId();
							} else if(rightOperand instanceof Work) {
								rightOperandValue = ((Work)rightOperand).getId();
							} else {
								if(lefOperandType.equals(FormField.TYPE_DATETIME)) rightOperandValue = ((LocalDate)rightOperand).toGMTDateString();
								else if(lefOperandType.equals(FormField.TYPE_DATE)) rightOperandValue = ((LocalDate)rightOperand).toGMTSimpleDateString2();
								else if(lefOperandType.equals(FormField.TYPE_TIME)) rightOperandValue = ((LocalDate)rightOperand).toGMTTimeString2();
								else rightOperandValue = (String)rightOperand;
							}
							filter.setLeftOperandType(lefOperandType);
							filter.setLeftOperandValue(leftOperand.getId());
							filter.setOperator(operator);
							filter.setRightOperandType(lefOperandType);
							filter.setRightOperandValue(rightOperandValue);
							filterList.add(filter);
						}
						Filter[] searchfilters = null;
						if(filterList.size() != 0) {
							searchfilters = new Filter[filterList.size()];
							filterList.toArray(searchfilters);
							filters.setFilter(searchfilters);
						}
						swdRecordCond.addFilters(filters);
					}
				}
			}

			String searchKey = params.getSearchKey();
			if(!CommonUtil.isEmpty(searchKey))
				swdRecordCond.setSearchKey(searchKey);

			String formId = swdDomain.getFormId();
			String formName = swdDomain.getFormName();
			String titleFieldId = swdDomain.getTitleFieldId();

			if(!ModelConverter.isAccessibleAllInstance(formId, userId))
				swdRecordCond.setCreationUser(userId);

			String[] workSpaceIdIns = ModelConverter.getWorkSpaceIdIns(user);
			swdRecordCond.setWorkSpaceIdIns(workSpaceIdIns);

			long totalCount = getSwdManager().getRecordSize(userId, swdRecordCond);

			//long totalCount = getSwdManager().getRecordSize(userId, swdRecordCond);
/*			SwdRecord[] totalSwdRecords = getSwdManager().getRecords(userId, swdRecordCond, IManager.LEVEL_LITE);
			List<SwdRecord> swdRecordList = new ArrayList<SwdRecord>();
			SwdRecord[] finalSwdRecords = null;
			int viewCount = 0;
			if(!CommonUtil.isEmpty(totalSwdRecords)) {
				for(SwdRecord totalSwdRecord : totalSwdRecords) {
					boolean isAccessForMe = ModelConverter.isAccessableInstance(totalSwdRecord);
					if(isAccessForMe) {
						viewCount = viewCount + 1;
						swdRecordList.add(totalSwdRecord);
					}
				}
				if(swdRecordList.size() > 0) {
					finalSwdRecords = new SwdRecord[swdRecordList.size()];
					swdRecordList.toArray(finalSwdRecords);
				}
			}*/

			int pageSize = params.getPageSize();
			if(pageSize == 0) pageSize = 20;

			int currentPage = params.getCurrentPage();
			if(currentPage == 0) currentPage = 1;

			int totalPages = (int)totalCount % pageSize;

			if(totalPages == 0)
				totalPages = (int)totalCount / pageSize;
			else
				totalPages = (int)totalCount / pageSize + 1;

			int result = 0;

			if(params.getPagingAction() != 0) {
				if(params.getPagingAction() == RequestParams.PAGING_ACTION_NEXT10) {
					result = (((currentPage - 1) / 10) * 10) + 11;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_NEXTEND) {
					result = totalPages;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_PREV10) {
					result = ((currentPage - 1) / 10) * 10;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_PREVEND) {
					result = 1;
				}
				currentPage = result;
			}

			if(previousPageSize != pageSize)
				currentPage = 1;

			previousPageSize = pageSize;

			if((long)((pageSize * (currentPage - 1)) + 1) > (int)totalCount)
				currentPage = 1;

			if (currentPage > 0)
				swdRecordCond.setPageNo(currentPage-1);

			swdRecordCond.setPageSize(pageSize);

			SortingField sf = params.getSortingField();
			String columnName = "";
			boolean isAsc;

			if (sf != null) {
				columnName  = CommonUtil.toDefault(sf.getFieldId(), FormField.ID_LAST_MODIFIED_DATE);
				isAsc = sf.isAscending();
			} else {
				columnName = FormField.ID_LAST_MODIFIED_DATE;
				isAsc = false;
			}
			SortingField sortingField = new SortingField();
			sortingField.setFieldId(columnName);
			sortingField.setAscending(isAsc);

			swdRecordCond.setOrders(new Order[]{new Order(columnName, isAsc)});

			SwdRecord[] swdRecords = getSwdManager().getRecords(userId, swdRecordCond, IManager.LEVEL_LITE);

			if(CommonUtil.isEmpty(swdRecords))
				return null;

			SwdRecordExtend[] swdRecordExtends = getSwdManager().getCtgPkg(workId);

			//SwdField[] swdFields = getSwdManager().getViewFieldList(workId, swdDomain.getFormId());

			SwfForm[] swfForms = getSwfManager().getForms(userId, swfFormCond, IManager.LEVEL_ALL);
			SwfField[] swfFields = swfForms[0].getFields();

			InstanceInfoList instanceInfoList = new InstanceInfoList();

			List<IWInstanceInfo> iWInstanceInfoList = new ArrayList<IWInstanceInfo>();
			IWInstanceInfo[] iWInstanceInfos = null;

			int recordSize = swdRecords.length;

			for(int i=0; i<recordSize; i++) {
				IWInstanceInfo iWInstanceInfo = new IWInstanceInfo();
				SwdRecord swdRecord = swdRecords[i];
				String creationUser = swdRecord.getCreationUser();
				Date creationDate = swdRecord.getCreationDate();
				String modificationUser = swdRecord.getModificationUser();
				Date modificationDate = swdRecord.getModificationDate();
				if(creationUser == null)
					creationUser = User.USER_ID_NONE_EXISTING;
				if(creationDate == null)
					creationDate = new Date();
				UserInfo owner = ModelConverter.getUserInfoByUserId(creationUser);
				LocalDate createdDate = new LocalDate(creationDate.getTime());
				UserInfo lastModifier = modificationUser != null ? ModelConverter.getUserInfoByUserId(modificationUser) : owner;
				LocalDate lastModifiedDate = modificationDate != null ? new LocalDate(modificationDate.getTime()) : createdDate;

				iWInstanceInfo.setId(swdRecord.getRecordId());
				iWInstanceInfo.setOwner(owner);
				iWInstanceInfo.setCreatedDate(createdDate);
				iWInstanceInfo.setLastModifier(lastModifier);
				iWInstanceInfo.setLastModifiedDate(lastModifiedDate);
				int type = WorkInstance.TYPE_INFORMATION;
				iWInstanceInfo.setType(type);
				iWInstanceInfo.setStatus(WorkInstance.STATUS_COMPLETED);
				String workSpaceId = swdRecord.getWorkSpaceId();
				if(CommonUtil.isEmpty(workSpaceId))
					workSpaceId = userId;

				WorkSpaceInfo workSpaceInfo = communityService.getWorkSpaceInfoById(workSpaceId);

				iWInstanceInfo.setWorkSpace(workSpaceInfo);

				WorkCategoryInfo groupInfo = null;
				if (!CommonUtil.isEmpty(swdRecordExtends[0].getSubCtgId()))
					groupInfo = new WorkCategoryInfo(swdRecordExtends[0].getSubCtgId(), swdRecordExtends[0].getSubCtg());
	
				WorkCategoryInfo categoryInfo = new WorkCategoryInfo(swdRecordExtends[0].getParentCtgId(), swdRecordExtends[0].getParentCtg());
	
				WorkInfo workInfo = new SmartWorkInfo(formId, formName, SmartWork.TYPE_INFORMATION, groupInfo, categoryInfo);

				iWInstanceInfo.setWork(workInfo);
				iWInstanceInfo.setViews(swdRecord.getHits());
				SwdDataField[] swdDataFields = swdRecord.getDataFields();
				List<FieldData> fieldDataList = new ArrayList<FieldData>();

				if(!CommonUtil.isEmpty(swdDataFields)) {
					int swdDataFieldsLength = swdDataFields.length;
					for(int j=0; j<swdDataFieldsLength; j++) {
						SwdDataField swdDataField = swdDataFields[j];
						if(swdDataField.getId().equals(titleFieldId))
							iWInstanceInfo.setSubject(swdDataField.getValue());
						if(!CommonUtil.isEmpty(swfFields)) {
							int swfFieldsLength = swfFields.length;
							for(int k=0; k<swfFieldsLength; k++) {
								SwfField swfField = swfFields[k];
								String formatType = swfField.getFormat().getType();
								if(swdDataField.getDisplayOrder() > -1 && !formatType.equals("richEditor") && !formatType.equals("imageBox") && !formatType.equals("dataGrid")) {
									if(swdDataField.getId().equals(swfField.getId())) {
										FieldData fieldData = new FieldData();
										fieldData.setFieldId(swdDataField.getId());
										fieldData.setFieldType(formatType);
										String value = swdDataField.getValue();
										if(formatType.equals(FormField.TYPE_USER)) {
											if(value != null) {
												String[] users = value.split(";");
												String resultUser = "";
												if(!CommonUtil.isEmpty(users) && users.length > 0) {
													if(users.length < 4) {
														for(int l=0; l<users.length; l++) {
															resultUser += users[l] + ", ";
														}
														resultUser = resultUser.substring(0, resultUser.length()-2);
													} else if(users.length > 3) {
														for(int l=0; l<3; l++) {
															resultUser += users[l] + ", ";
														}
														resultUser = resultUser.substring(0, resultUser.length()-2);
														resultUser = resultUser + " " + SmartMessage.getString("content.sentence.with_other_users", (new Object[]{(users.length - 3)}));
													}
												}
												value = resultUser;
											}
										} else if(formatType.equals(FormField.TYPE_CURRENCY)) {
											String symbol = swfField.getFormat().getCurrency();
											fieldData.setSymbol(symbol);
										} else if(formatType.equals(FormField.TYPE_PERCENT)) {
											// TO-DO
										} else if(formatType.equals(FormField.TYPE_DATE)) {
											LocalDate localDateValue = null;
											if(value != null) {
												localDateValue = LocalDate.convertGMTStringToLocalDate(value);
												if(localDateValue != null)
													value = LocalDate.convertGMTStringToLocalDate(value).toLocalDateSimpleString();
											}
										} else if(formatType.equals(FormField.TYPE_TIME)) {
											LocalDate localDateValue = null;
											if(value != null) {
												localDateValue = LocalDate.convertGMTStringToLocalDate(value);
												if(localDateValue != null)
													value = LocalDate.convertGMTStringToLocalDate(value).toLocalTimeSimpleString();
											}
										} else if(formatType.equals(FormField.TYPE_DATETIME)) {
											LocalDate localDateValue = null;
											if(value != null) {
												localDateValue = LocalDate.convertGMTStringToLocalDate(value);
												if(localDateValue != null)
													value = localDateValue.toLocalDateTimeSimpleString();
											}
										} else if(formatType.equals(FormField.TYPE_FILE)) { 
											List<IFileModel> fileModelList = getDocManager().findFileGroup(value);
											List<Map<String, String>> fileList = new ArrayList<Map<String,String>>();
											int fileModelListLength = fileModelList.size();
											for(int l=0; l<fileModelListLength; l++) {
												Map<String, String> fileMap = new LinkedHashMap<String, String>();
												IFileModel fileModel = fileModelList.get(l);
												String fileId = fileModel.getId();
												String fileName = fileModel.getFileName();
												String fileType = fileModel.getType();
												String fileSize = fileModel.getFileSize() + "";
												fileMap.put("fileId", fileId);
												fileMap.put("fileName", fileName);
												fileMap.put("fileType", fileType);
												fileMap.put("fileSize", fileSize);
												fileList.add(fileMap);
											}
											if(fileList.size() > 0)
												fieldData.setFiles(fileList);
										} else if(formatType.equals(FormField.TYPE_TEXT)) {
											value = StringUtil.subString(value, 0, 30, "...");
										}
										fieldData.setValue(value);
										fieldDataList.add(fieldData);
									}
								}
							}
						}
					}
				}
				FieldData[] fieldDatas = new FieldData[fieldDataList.size()];
				fieldDataList.toArray(fieldDatas);
				iWInstanceInfo.setDisplayDatas(fieldDatas);

				iWInstanceInfoList.add(iWInstanceInfo);
			}
			if(!CommonUtil.isEmpty(iWInstanceInfoList)) {
				iWInstanceInfos = new IWInstanceInfo[iWInstanceInfoList.size()];
				iWInstanceInfoList.toArray(iWInstanceInfos);
			}
			instanceInfoList.setInstanceDatas(iWInstanceInfos);

			/*SwdRecord[] swdRecords = getSwdManager().getRecords(userId, swdRecordCond, IManager.LEVEL_LITE);

			SwdRecordExtend[] swdRecordExtends = getSwdManager().getCtgPkg(workId);

			//SwdField[] swdFields = getSwdManager().getViewFieldList(workId, swdDomain.getFormId());

			SwfForm[] swfForms = getSwfManager().getForms(userId, swfFormCond, IManager.LEVEL_ALL);
			SwfField[] swfFields = swfForms[0].getFields();

			InstanceInfoList instanceInfoList = new InstanceInfoList();

			List<IWInstanceInfo> iWInstanceInfoList = new ArrayList<IWInstanceInfo>();
			IWInstanceInfo[] iWInstanceInfos = null;*/
			/*if(!CommonUtil.isEmpty(swdRecords)) {
				int swdRecordsLength = swdRecords.length;
				for(int i = 0; i < swdRecordsLength; i++) {
					IWInstanceInfo iWInstanceInfo = new IWInstanceInfo();
					SwdRecord swdRecord = swdRecords[i];
					boolean isAccessForMe = ModelConverter.isAccessableForMe(swdRecord);
					if(isAccessForMe) {
						String creationUser = swdRecord.getCreationUser();
						Date creationDate = swdRecord.getCreationDate();
						String modificationUser = swdRecord.getModificationUser();
						Date modificationDate = swdRecord.getModificationDate();
						if(creationUser == null)
							creationUser = User.USER_ID_NONE_EXISTING;
						if(creationDate == null)
							creationDate = new Date();
						UserInfo owner = ModelConverter.getUserInfoByUserId(creationUser);
						LocalDate createdDate = new LocalDate(creationDate.getTime());
						UserInfo lastModifier = modificationUser != null ? ModelConverter.getUserInfoByUserId(modificationUser) : owner;
						LocalDate lastModifiedDate = modificationDate != null ? new LocalDate(modificationDate.getTime()) : createdDate;
	
						iWInstanceInfo.setId(swdRecord.getRecordId());
						iWInstanceInfo.setOwner(owner);
						iWInstanceInfo.setCreatedDate(createdDate);
						iWInstanceInfo.setLastModifier(lastModifier);
						iWInstanceInfo.setLastModifiedDate(lastModifiedDate);
						int type = WorkInstance.TYPE_INFORMATION;
						iWInstanceInfo.setType(type);
						iWInstanceInfo.setStatus(WorkInstance.STATUS_COMPLETED);
						String workSpaceId = swdRecord.getWorkSpaceId();
						if(CommonUtil.isEmpty(workSpaceId))
							workSpaceId = userId;
	
						WorkSpaceInfo workSpaceInfo = communityService.getWorkSpaceInfoById(workSpaceId);
	
						iWInstanceInfo.setWorkSpace(workSpaceInfo);
	
						WorkCategoryInfo groupInfo = null;
						if (!CommonUtil.isEmpty(swdRecordExtends[0].getSubCtgId()))
							groupInfo = new WorkCategoryInfo(swdRecordExtends[0].getSubCtgId(), swdRecordExtends[0].getSubCtg());
			
						WorkCategoryInfo categoryInfo = new WorkCategoryInfo(swdRecordExtends[0].getParentCtgId(), swdRecordExtends[0].getParentCtg());
			
						WorkInfo workInfo = new SmartWorkInfo(formId, formName, SmartWork.TYPE_INFORMATION, groupInfo, categoryInfo);
		
						iWInstanceInfo.setWork(workInfo);
						iWInstanceInfo.setViews(swdRecord.getHits());
						SwdDataField[] swdDataFields = swdRecord.getDataFields();
						List<FieldData> fieldDataList = new ArrayList<FieldData>();
	
						if(!CommonUtil.isEmpty(swdDataFields)) {
							int swdDataFieldsLength = swdDataFields.length;
							for(int j=0; j<swdDataFieldsLength; j++) {
								SwdDataField swdDataField = swdDataFields[j];
								if(swdDataField.getId().equals(titleFieldId))
									iWInstanceInfo.setSubject(swdDataField.getValue());
								if(!CommonUtil.isEmpty(swfFields)) {
									int swfFieldsLength = swfFields.length;
									for(int k=0; k<swfFieldsLength; k++) {
										SwfField swfField = swfFields[k];
										String formatType = swfField.getFormat().getType();
										if(swdDataField.getDisplayOrder() > -1 && !formatType.equals("richEditor") && !formatType.equals("imageBox") && !formatType.equals("dataGrid")) {
											if(swdDataField.getId().equals(swfField.getId())) {
												FieldData fieldData = new FieldData();
												fieldData.setFieldId(swdDataField.getId());
												fieldData.setFieldType(formatType);
												String value = swdDataField.getValue();
												if(formatType.equals(FormField.TYPE_USER)) {
													if(value != null) {
														String[] users = value.split(";");
														String resultUser = "";
														if(!CommonUtil.isEmpty(users) && users.length > 0) {
															if(users.length < 4) {
																for(int l=0; l<users.length; l++) {
																	resultUser += users[l] + ", ";
																}
																resultUser = resultUser.substring(0, resultUser.length()-2);
															} else if(users.length > 3) {
																for(int l=0; l<3; l++) {
																	resultUser += users[l] + ", ";
																}
																resultUser = resultUser.substring(0, resultUser.length()-2);
																resultUser = resultUser + " " + SmartMessage.getString("content.sentence.with_other_users", (new Object[]{(users.length - 3)}));
															}
														}
														value = resultUser;
													}
												} else if(formatType.equals(FormField.TYPE_CURRENCY)) {
													String symbol = swfField.getFormat().getCurrency();
													fieldData.setSymbol(symbol);
												} else if(formatType.equals(FormField.TYPE_PERCENT)) {
													// TO-DO
												} else if(formatType.equals(FormField.TYPE_DATE)) {
													LocalDate localDateValue = null;
													if(value != null) {
														localDateValue = LocalDate.convertGMTStringToLocalDate(value);
														if(localDateValue != null)
															value = LocalDate.convertGMTStringToLocalDate(value).toLocalDateSimpleString();
													}
												} else if(formatType.equals(FormField.TYPE_TIME)) {
													LocalDate localDateValue = null;
													if(value != null) {
														localDateValue = LocalDate.convertGMTStringToLocalDate(value);
														if(localDateValue != null)
															value = LocalDate.convertGMTStringToLocalDate(value).toLocalTimeSimpleString();
													}
												} else if(formatType.equals(FormField.TYPE_DATETIME)) {
													LocalDate localDateValue = null;
													if(value != null) {
														localDateValue = LocalDate.convertGMTStringToLocalDate(value);
														if(localDateValue != null)
															value = localDateValue.toLocalDateTimeSimpleString();
													}
												} else if(formatType.equals(FormField.TYPE_FILE)) { 
													List<IFileModel> fileModelList = getDocManager().findFileGroup(value);
													List<Map<String, String>> fileList = new ArrayList<Map<String,String>>();
													int fileModelListLength = fileModelList.size();
													for(int l=0; l<fileModelListLength; l++) {
														Map<String, String> fileMap = new LinkedHashMap<String, String>();
														IFileModel fileModel = fileModelList.get(l);
														String fileId = fileModel.getId();
														String fileName = fileModel.getFileName();
														String fileType = fileModel.getType();
														String fileSize = fileModel.getFileSize() + "";
														fileMap.put("fileId", fileId);
														fileMap.put("fileName", fileName);
														fileMap.put("fileType", fileType);
														fileMap.put("fileSize", fileSize);
														fileList.add(fileMap);
													}
													if(fileList.size() > 0)
														fieldData.setFiles(fileList);
												} else if(formatType.equals(FormField.TYPE_TEXT)) {
													value = StringUtil.subString(value, 0, 30, "...");
												}
												fieldData.setValue(value);
												fieldDataList.add(fieldData);
											}
										}
									}
								}
							}
						}
						FieldData[] fieldDatas = new FieldData[fieldDataList.size()];
						fieldDataList.toArray(fieldDatas);
						iWInstanceInfo.setDisplayDatas(fieldDatas);

						iWInstanceInfoList.add(iWInstanceInfo);
					}
				}
				if(!CommonUtil.isEmpty(iWInstanceInfoList)) {
					iWInstanceInfos = new IWInstanceInfo[iWInstanceInfoList.size()];
					iWInstanceInfoList.toArray(iWInstanceInfos);
				}
				instanceInfoList.setInstanceDatas(iWInstanceInfos);
			}*/

			instanceInfoList.setTotalSize((int)totalCount);
			instanceInfoList.setSortedField(sortingField);
			instanceInfoList.setType(InstanceInfoList.TYPE_INFORMATION_INSTANCE_LIST);
			instanceInfoList.setPageSize(pageSize);
			instanceInfoList.setTotalPages(totalPages);
			instanceInfoList.setCurrentPage(currentPage);

			return instanceInfoList;
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public InstanceInfoList getIWorkInstanceListByFormId(String formId, RequestParams params) throws Exception {

		try{
			User user = SmartUtil.getCurrentUser();
	
			SwfFormCond swfCond = new SwfFormCond();
			swfCond.setCompanyId(user.getCompanyId());
			swfCond.setId(formId);
	
			SwfForm swfForm = getSwfManager().getForms(user.getId(), swfCond, IManager.LEVEL_LITE)[0];
	
			String workId = swfForm.getPackageId();
	
			InstanceInfoList instanceInfoList = getIWorkInstanceList(workId, params);
	
			return instanceInfoList;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}

	private String getProcessTableColName(String formFieldId) throws Exception {
		String tableColName = null;
		if(CommonUtil.isEmpty(formFieldId))
			return null;
		if(formFieldId.equalsIgnoreCase("status"))
			tableColName = "prcStatus";
		else if(formFieldId.equalsIgnoreCase("subject"))
			tableColName = "prcTitle";
		/*else if(formFieldId.equalsIgnoreCase("taskName"))
			tableColName = "taskName";*/
		else if(formFieldId.equalsIgnoreCase("lastTask"))
			tableColName = "lastTask_tskname";
		/*else if(formFieldId.equalsIgnoreCase("processTime"))
			tableColName = "processTime";
		else if(formFieldId.equalsIgnoreCase("processType"))
			tableColName = "processType";*/
		else if(formFieldId.equalsIgnoreCase("creator"))
			tableColName = "prcCreateUser";
		else if(formFieldId.equalsIgnoreCase("createdTime"))
			tableColName = "prcCreateDate";
		else if(formFieldId.equalsIgnoreCase("modifier"))
			tableColName = "lastTask_tskassignee";
		else if(formFieldId.equalsIgnoreCase("modifiedTime"))
			tableColName = "lastTask_tskexecuteDate";
		else
			tableColName = formFieldId;

		return tableColName;
	}
	public InstanceInfoList getPWorkInstanceList(String workId, RequestParams params) throws Exception {
		
		try{
			User user = SmartUtil.getCurrentUser();
			String userId = user.getId();
			//TODO workId = category 프로세스 인스턴스정보에는 패키지 컬럼이 없고 다이어 그램 컬럼에 정보가 들어가 있다
			//임시로 프로세스 다이어그램아이디 필드를 이용하고 프로세스인스턴스가 생성되는 시점(업무 시작, 처리 개발 완료)에 패키지 아이디 컬럼을 추가해 그곳에서 조회하는걸로 변경한다
			PrcProcessInstCond prcInstCond = new PrcProcessInstCond();
			prcInstCond.setPackageId(workId);
			String filterId = params.getFilterId();

			LocalDate priviousDate = new LocalDate(new LocalDate().getTime() - LocalDate.ONE_DAY*7);

			SearchFilter searchFilter = params.getSearchFilter();
			List<Filter> filterList = new ArrayList<Filter>();
			if(searchFilter != null) {
				Condition[] conditions = searchFilter.getConditions();
				for(Condition condition : conditions) {
					Filter filter = new Filter();

					FormField leftOperand = condition.getLeftOperand();
					String formFieldId = leftOperand.getId();
					String tableColName = getProcessTableColName(formFieldId);

					String formFieldType = leftOperand.getType();
					String operator = condition.getOperator();
					String rightOperand = (String)condition.getRightOperand();

					if(formFieldId.equalsIgnoreCase("status")) {
						int rightOperandInt = Integer.parseInt(rightOperand);
						if(rightOperandInt == Instance.STATUS_RUNNING)
							rightOperand = PrcProcessInst.PROCESSINSTSTATUS_RUNNING;
						else if(rightOperandInt == Instance.STATUS_DELAYED_RUNNING)
							rightOperand = PrcProcessInst.PROCESSINSTSTATUS_RUNNING;
						else if(rightOperandInt == Instance.STATUS_RETURNED)
							rightOperand = PrcProcessInst.PROCESSINSTSTATUS_RUNNING;
						else if(rightOperandInt == Instance.STATUS_COMPLETED)
							rightOperand = PrcProcessInst.PROCESSINSTSTATUS_COMPLETE;
					}

					filter.setLeftOperandType(formFieldType);
					filter.setLeftOperandValue(tableColName);
					filter.setOperator(operator);
					filter.setRightOperandType(formFieldType);
					filter.setRightOperandValue(rightOperand);
					filterList.add(filter);
				}

				Filter[] filters = new Filter[filterList.size()];
				filterList.toArray(filters);

				prcInstCond.setFilter(filters);
			}

			if(filterId != null) {
				if(filterId.equals(SearchFilter.FILTER_ALL_INSTANCES)) {
				} else if(filterId.equals(SearchFilter.FILTER_MY_INSTANCES)) {
					prcInstCond.addFilter(new Filter("=", getProcessTableColName(FormField.ID_LAST_MODIFIER), Filter.OPERANDTYPE_STRING, user.getId()));
				} else if(filterId.equals(SearchFilter.FILTER_RECENT_INSTANCES)) {
					prcInstCond.addFilter(new Filter(">=", getProcessTableColName(FormField.ID_LAST_MODIFIED_DATE), Filter.OPERANDTYPE_DATE, priviousDate.toGMTSimpleDateString()));
				} else if(filterId.equals(SearchFilter.FILTER_MY_RECENT_INSTANCES)) {
					prcInstCond.addFilter(new Filter("=", getProcessTableColName(FormField.ID_LAST_MODIFIER), Filter.OPERANDTYPE_STRING, user.getId()));
					prcInstCond.addFilter(new Filter(">=", getProcessTableColName(FormField.ID_LAST_MODIFIED_DATE), Filter.OPERANDTYPE_DATE, priviousDate.toGMTSimpleDateString()));
				} else if(filterId.equals(SearchFilter.FILTER_MY_RUNNING_INSTANCES)) {
					prcInstCond.addFilter(new Filter("=", getProcessTableColName(FormField.ID_OWNER), Filter.OPERANDTYPE_STRING, user.getId()));
					prcInstCond.addFilter(new Filter("=", getProcessTableColName("status"), Filter.OPERANDTYPE_STRING, PrcProcessInst.PROCESSINSTSTATUS_RUNNING));
				} else {
					searchFilter = ModelConverter.getSearchFilterByFilterId(SwfFormModel.TYPE_PROCESS, workId, filterId);
					if (searchFilter != null) {
						Condition[] conditions = searchFilter.getConditions();
						filterList = new ArrayList<Filter>();
						for(Condition condition : conditions) {
							Filter filter = new Filter();
							FormField leftOperand = condition.getLeftOperand();
							String formFieldId = leftOperand.getId();
							String tableColName = getProcessTableColName(formFieldId);
							String lefOperandType = leftOperand.getType();
							String operator = condition.getOperator();
							Object rightOperand = condition.getRightOperand();
							String rightOperandValue = "";
							if(rightOperand instanceof User) {
								rightOperandValue = ((User)rightOperand).getId();
							} else if(rightOperand instanceof Work) {
								rightOperandValue = ((Work)rightOperand).getId();
							} else {
								if(lefOperandType.equals(FormField.TYPE_DATETIME)) rightOperandValue = ((LocalDate)rightOperand).toGMTDateString();
								else if(lefOperandType.equals(FormField.TYPE_DATE)) rightOperandValue = ((LocalDate)rightOperand).toGMTSimpleDateString2();
								else if(lefOperandType.equals(FormField.TYPE_TIME)) rightOperandValue = ((LocalDate)rightOperand).toGMTTimeString2();
								else rightOperandValue = (String)rightOperand;
							}
							if(formFieldId.equalsIgnoreCase("status")) {
								int rightOperandInt = Integer.parseInt((String)rightOperand);
								if(rightOperandInt == Instance.STATUS_RUNNING)
									rightOperandValue = PrcProcessInst.PROCESSINSTSTATUS_RUNNING;
								else if(rightOperandInt == Instance.STATUS_DELAYED_RUNNING)
									rightOperandValue = PrcProcessInst.PROCESSINSTSTATUS_RUNNING;
								else if(rightOperandInt == Instance.STATUS_RETURNED)
									rightOperandValue = PrcProcessInst.PROCESSINSTSTATUS_RUNNING;
								else if(rightOperandInt == Instance.STATUS_COMPLETED)
									rightOperandValue = PrcProcessInst.PROCESSINSTSTATUS_COMPLETE;
							}
							filter.setLeftOperandType(lefOperandType);
							filter.setLeftOperandValue(tableColName);
							filter.setOperator(operator);
							filter.setRightOperandType(lefOperandType);
							filter.setRightOperandValue(rightOperandValue);
							filterList.add(filter);
						}
						Filter[] filters = new Filter[filterList.size()];
						filterList.toArray(filters);

						prcInstCond.setFilter(filters);
					}
				}
			}

			PkgPackageCond pkgPackageCond = new PkgPackageCond();
			pkgPackageCond.setPackageId(workId);
			PkgPackage pkgPackage = getPkgManager().getPackage(userId, pkgPackageCond, IManager.LEVEL_LITE);

			if(!ModelConverter.isAccessibleAllInstance(ModelConverter.getResourceIdByPkgPackage(pkgPackage), userId))
				prcInstCond.setCreationUser(userId);

			String[] workSpaceIdIns = ModelConverter.getWorkSpaceIdIns(user);
			prcInstCond.setWorkSpaceIdIns(workSpaceIdIns);

			long totalCount = getPrcManager().getProcessInstExtendsSize(user.getId(), prcInstCond);

			int pageSize = params.getPageSize();
			if(pageSize == 0) pageSize = 20;

			int currentPage = params.getCurrentPage();
			if(currentPage == 0) currentPage = 1;

			int totalPages = (int)totalCount % pageSize;

			if(totalPages == 0)
				totalPages = (int)totalCount / pageSize;
			else
				totalPages = (int)totalCount / pageSize + 1;

			int result = 0;

			if(params.getPagingAction() != 0) {
				if(params.getPagingAction() == RequestParams.PAGING_ACTION_NEXT10) {
					result = (((currentPage - 1) / 10) * 10) + 11;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_NEXTEND) {
					result = totalPages;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_PREV10) {
					result = ((currentPage - 1) / 10) * 10;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_PREVEND) {
					result = 1;
				}
				currentPage = result;
			}

			if(previousPageSize != pageSize)
				currentPage = 1;

			previousPageSize = pageSize;

			if((long)((pageSize * (currentPage - 1)) + 1) > (int)totalCount)
				currentPage = 1;

			if (currentPage > 0)
				prcInstCond.setPageNo(currentPage-1);

			prcInstCond.setPageSize(pageSize);

			SortingField sf = params.getSortingField();

			//화면에서 사용하고 있는 컬럼의 상수값과 실제 프로세스 인스턴스 데이터 베이스의 컬럼 이름이 맞지 않아 컨버팅 작업
			//한군데에서 관리 하도록 상수로 변경 필요
			if (sf == null) {
				sf = new SortingField();
				sf.setFieldId(FormField.ID_CREATED_DATE);
				sf.setAscending(false);
			}
			String sfColumnNameTemp = sf.getFieldId();
			
			if (sfColumnNameTemp.equalsIgnoreCase("status")) {
				sfColumnNameTemp = "prcStatus"; 
			} else if (sfColumnNameTemp.equalsIgnoreCase("subject")) {
				sfColumnNameTemp = "prcTitle";
			} else if (sfColumnNameTemp.equalsIgnoreCase("lastTask")) {
				sfColumnNameTemp = "lastTask_tskname"; 
			} else if (sfColumnNameTemp.equalsIgnoreCase("creator")) {
				sfColumnNameTemp = "prcCreateUser"; 
			} else if (sfColumnNameTemp.equalsIgnoreCase("createdTime")) {
				sfColumnNameTemp = "prcCreateDate"; 
			} else if (sfColumnNameTemp.equalsIgnoreCase("modifier")) {
				sfColumnNameTemp = "lastTask_tskassignee"; 
			} else if (sfColumnNameTemp.equalsIgnoreCase("modifiedTime")) {
				sfColumnNameTemp = "lastTask_tskexecuteDate"; 
			} else {
				sfColumnNameTemp = "prcCreateDate";
			}

			prcInstCond.setOrders(new Order[]{new Order(sfColumnNameTemp, sf.isAscending())});

			PrcProcessInstExtend[] processInstExtends = getPrcManager().getProcessInstExtends(userId, prcInstCond);

			InstanceInfoList instanceInfoList = new InstanceInfoList();

			List<PWInstanceInfo> pwInstanceInfoList = new ArrayList<PWInstanceInfo>();
			PWInstanceInfo[] pWInstanceInfos = null;

			if(!CommonUtil.isEmpty(processInstExtends)) {
				int length = processInstExtends.length;
				for(int i=0; i<length; i++) {
					PWInstanceInfo pwInstInfo = new PWInstanceInfo();
					PrcProcessInstExtend prcInst = processInstExtends[i];
					pwInstInfo.setId(prcInst.getPrcObjId());
					pwInstInfo.setOwner(ModelConverter.getUserInfoByUserId(prcInst.getPrcCreateUser()));
					pwInstInfo.setCreatedDate(new LocalDate(prcInst.getPrcCreateDate().getTime()));
					int status = -1;
					if (prcInst.getPrcStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_RUNNING)) {
						status = Instance.STATUS_RUNNING;
					} else if (prcInst.getPrcStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_COMPLETE)) {
						status = Instance.STATUS_COMPLETED;
					}
					pwInstInfo.setStatus(status);
					pwInstInfo.setSubject(prcInst.getPrcTitle());
					int type = WorkInstance.TYPE_PROCESS;
					pwInstInfo.setType(type);

					WorkCategoryInfo groupInfo = null;
					if (!CommonUtil.isEmpty(prcInst.getSubCtgId()))
						groupInfo = new WorkCategoryInfo(prcInst.getSubCtgId(), prcInst.getSubCtg());
						
					WorkCategoryInfo categoryInfo = new WorkCategoryInfo(prcInst.getParentCtgId(), prcInst.getParentCtg());
					
					WorkInfo workInfo = new SmartWorkInfo(prcInst.getPrcDid(), prcInst.getPrcName(), SmartWork.TYPE_PROCESS, groupInfo, categoryInfo);
					pwInstInfo.setWork(workInfo);
		
					TaskInstanceInfo lastTaskInfo = null;
					
					if (!CommonUtil.isEmpty(prcInst.getLastTask_tskObjId())) {
						lastTaskInfo = new TaskInstanceInfo();
						
						if (prcInst.getLastTask_tskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ASSIGN) || prcInst.getLastTask_tskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_CREATE)) {
							pwInstInfo.setLastModifiedDate(new LocalDate(prcInst.getLastTask_tskCreateDate().getTime()));
						} else {
							pwInstInfo.setLastModifiedDate(new LocalDate(prcInst.getLastTask_tskExecuteDate().getTime()));//마지막태스크 수행일
						}
						pwInstInfo.setLastModifier(ModelConverter.getUserInfoByUserId(prcInst.getLastTask_tskAssignee()));//마지막태스크 수행자
						
						String id = prcInst.getLastTask_tskObjId();
						String subject = prcInst.getLastTask_tskTitle();
						int tskType = WorkInstance.TYPE_TASK;
						String name = prcInst.getLastTask_tskName();
						String assignee = prcInst.getLastTask_tskAssignee();
						String performer = prcInst.getLastTask_tskAssignee();
						
						int tskStatus = -1;
						if (prcInst.getLastTask_tskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ASSIGN)) {
							tskStatus = Instance.STATUS_COMPLETED;
						} else if (prcInst.getLastTask_tskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_COMPLETE)) {
							tskStatus = Instance.STATUS_COMPLETED;
						}
						UserInfo owner = ModelConverter.getUserInfoByUserId(prcInst.getLastTask_tskAssignee());
						UserInfo lastModifier = ModelConverter.getUserInfoByUserId(prcInst.getLastTask_tskAssignee()); 
						LocalDate lastModifiedDate = new LocalDate((prcInst.getLastTask_tskCreateDate().getTime()));
						
						lastTaskInfo.setId(id);
						lastTaskInfo.setLastModifiedDate(lastModifiedDate);
						lastTaskInfo.setLastModifier(lastModifier);
						lastTaskInfo.setOwner(owner);
						lastTaskInfo.setStatus(tskStatus);
						lastTaskInfo.setSubject(subject);
						lastTaskInfo.setType(tskType);
						lastTaskInfo.setWork(workInfo);
						lastTaskInfo.setWorkInstance(pwInstInfo);
						lastTaskInfo.setWorkSpace(ModelConverter.getWorkSpaceInfo(prcInst.getLastTask_tskWorkSpaceType(), prcInst.getLastTask_tskWorkSpaceId()));
						lastTaskInfo.setName(name);
						lastTaskInfo.setTaskType(tskType);
						lastTaskInfo.setAssignee(ModelConverter.getUserInfoByUserId(assignee));
						lastTaskInfo.setPerformer(ModelConverter.getUserInfoByUserId(performer));
						//WorkInstanceInfo workInstanceInfo = paretProcessInstObj;
						pwInstInfo.setLastTask(lastTaskInfo);//마지막 태스크
					}
					pwInstInfo.setLastTaskCount(prcInst.getLastTask_tskCount());
					pwInstInfo.setWorkSpace(ModelConverter.getWorkSpaceInfo(prcInst.getPrcWorkSpaceType(), prcInst.getPrcWorkSpaceId()));
					pwInstanceInfoList.add(pwInstInfo);
				}
			}

			//prcInstCond.setPageNo(currentPage);
			//prcInstCond.setPageSize(pageCount);
			//prcInstCond.setOrders(new Order[]{new Order(sfColumnNameTemp, sf.isAscending())});
/*			PrcProcessInstExtend[] prcInsts = getPrcManager().getProcessInstExtends(userId, prcInstCond);

			if (prcInsts == null)
				return null;

			InstanceInfoList instanceInfoList = new InstanceInfoList();

			List<PWInstanceInfo> pwInstanceInfoList = new ArrayList<PWInstanceInfo>();
			PWInstanceInfo[] pWInstanceInfos = null;

			for(int i = 0; i < prcInsts.length; i++) {
				PWInstanceInfo pwInstInfo = new PWInstanceInfo();
				PrcProcessInstExtend prcInst = prcInsts[i];
				boolean isAccessForMe = ModelConverter.isAccessableForMe(prcInst);
				if(isAccessForMe) { 
					pwInstInfo.setId(prcInst.getPrcObjId());
					pwInstInfo.setOwner(ModelConverter.getUserInfoByUserId(prcInst.getPrcCreateUser()));
					int status = -1;
					if (prcInst.getPrcStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_RUNNING)) {
						status = Instance.STATUS_RUNNING;
					} else if (prcInst.getPrcStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_COMPLETE)) {
						status = Instance.STATUS_COMPLETED;
					}
					pwInstInfo.setStatus(status);
					pwInstInfo.setSubject(prcInst.getPrcTitle());
					int type = WorkInstance.TYPE_PROCESS;
					pwInstInfo.setType(type);
	
					WorkCategoryInfo groupInfo = null;
					if (!CommonUtil.isEmpty(prcInst.getSubCtgId()))
						groupInfo = new WorkCategoryInfo(prcInst.getSubCtgId(), prcInst.getSubCtg());
						
					WorkCategoryInfo categoryInfo = new WorkCategoryInfo(prcInst.getParentCtgId(), prcInst.getParentCtg());
					
					WorkInfo workInfo = new SmartWorkInfo(prcInst.getPrcDid(), prcInst.getPrcName(), SmartWork.TYPE_PROCESS, groupInfo, categoryInfo);
					pwInstInfo.setWork(workInfo);
		
					TaskInstanceInfo lastTaskInfo = null;
					
					if (!CommonUtil.isEmpty(prcInst.getLastTask_tskObjId())) {
						lastTaskInfo = new TaskInstanceInfo();
						
						if (prcInst.getLastTask_tskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ASSIGN) || prcInst.getLastTask_tskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_CREATE)) {
							pwInstInfo.setLastModifiedDate(new LocalDate(prcInst.getLastTask_tskCreateDate().getTime()));
						} else {
							pwInstInfo.setLastModifiedDate(new LocalDate(prcInst.getLastTask_tskExecuteDate().getTime()));//마지막태스크 수행일
						}
						pwInstInfo.setLastModifier(ModelConverter.getUserInfoByUserId(prcInst.getLastTask_tskAssignee()));//마지막태스크 수행자
						
						String id = prcInst.getLastTask_tskObjId();
						String subject = prcInst.getLastTask_tskTitle();
						int tskType = WorkInstance.TYPE_TASK;
						String name = prcInst.getLastTask_tskName();
						String assignee = prcInst.getLastTask_tskAssignee();
						String performer = prcInst.getLastTask_tskAssignee();
						
						int tskStatus = -1;
						if (prcInst.getLastTask_tskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ASSIGN)) {
							tskStatus = Instance.STATUS_COMPLETED;
						} else if (prcInst.getLastTask_tskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_COMPLETE)) {
							tskStatus = Instance.STATUS_COMPLETED;
						}
						UserInfo owner = ModelConverter.getUserInfoByUserId(prcInst.getLastTask_tskAssignee());
						UserInfo lastModifier = ModelConverter.getUserInfoByUserId(prcInst.getLastTask_tskAssignee()); 
						LocalDate lastModifiedDate = new LocalDate((prcInst.getLastTask_tskCreateDate().getTime()));
						
						lastTaskInfo.setId(id);
						lastTaskInfo.setLastModifiedDate(lastModifiedDate);
						lastTaskInfo.setLastModifier(lastModifier);
						lastTaskInfo.setOwner(owner);
						lastTaskInfo.setStatus(tskStatus);
						lastTaskInfo.setSubject(subject);
						lastTaskInfo.setType(tskType);
						lastTaskInfo.setWork(workInfo);
						lastTaskInfo.setWorkInstance(pwInstInfo);
						lastTaskInfo.setWorkSpace(ModelConverter.getWorkSpaceInfo(prcInst.getLastTask_tskWorkSpaceType(), prcInst.getLastTask_tskWorkSpaceId()));
						lastTaskInfo.setName(name);
						lastTaskInfo.setTaskType(tskType);
						lastTaskInfo.setAssignee(ModelConverter.getUserInfoByUserId(assignee));
						lastTaskInfo.setPerformer(ModelConverter.getUserInfoByUserId(performer));
						//WorkInstanceInfo workInstanceInfo = paretProcessInstObj;
						pwInstInfo.setLastTask(lastTaskInfo);//마지막 태스크
					}
					pwInstInfo.setLastTaskCount(prcInst.getLastTask_tskCount());
					pwInstInfo.setWorkSpace(ModelConverter.getWorkSpaceInfo(prcInst.getPrcWorkSpaceType(), prcInst.getPrcWorkSpaceId()));
					pwInstanceInfoList.add(pwInstInfo);
				}
			}*/
			if(pwInstanceInfoList.size() > 0) {
				pWInstanceInfos = new PWInstanceInfo[pwInstanceInfoList.size()];
				pwInstanceInfoList.toArray(pWInstanceInfos);
			}
	//		instanceInfoList.setInstanceDatas(ModelConverter.getPWInstanceInfoArrayByPrcProcessInstArray(prcInsts));
			instanceInfoList.setInstanceDatas(pWInstanceInfos);
			instanceInfoList.setPageSize(pageSize);
			instanceInfoList.setTotalSize((int)totalCount);
			instanceInfoList.setSortedField(sf);
			instanceInfoList.setTotalPages(totalPages);
			instanceInfoList.setCurrentPage(currentPage);
			instanceInfoList.setType(InstanceInfoList.TYPE_PROCESS_INSTANCE_LIST);
			return instanceInfoList;
		} catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}

	public InstanceInfoList getInstanceInfoListByWorkId(String workSpaceId, RequestParams params, String workId) throws Exception {

		try {

			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();
			String companyId = cUser.getCompanyId();

			InstanceInfoList instanceInfoList = new InstanceInfoList();
			
			SwdDomainCond swdDomainCond = new SwdDomainCond();
			swdDomainCond.setCompanyId(companyId);

			SwfFormCond swfFormCond = new SwfFormCond();
			swfFormCond.setCompanyId(companyId);
			swfFormCond.setPackageId(workId);
			SwfForm[] swfForms = getSwfManager().getForms(userId, swfFormCond, IManager.LEVEL_LITE);

			if(CommonUtil.isEmpty(swfForms))
				return null;

			swdDomainCond.setFormId(swfForms[0].getId());

			SwdDomain swdDomain = getSwdManager().getDomain(userId, swdDomainCond, IManager.LEVEL_LITE);

			if(swdDomain == null)
				return  null;

			SwdRecordCond swdRecordCond = new SwdRecordCond();
			swdRecordCond.setCompanyId(companyId);
			swdRecordCond.setFormId(swdDomain.getFormId());
			swdRecordCond.setDomainId(swdDomain.getObjId());

			if(userId.equals(workSpaceId))
				swdRecordCond.setCreatorOrSpaceId(workSpaceId);
			else
				swdRecordCond.setWorkSpaceId(workSpaceId);

			String searchKey = params.getSearchKey();
			if(!CommonUtil.isEmpty(searchKey))
				swdRecordCond.setSearchKey(searchKey);

			long totalCount = getSwdManager().getRecordSize(userId, swdRecordCond);

			int pageSize = params.getPageSize();
			if(pageSize == 0) pageSize = 20;

			int currentPage = params.getCurrentPage();
			if(currentPage == 0) currentPage = 1;

			int totalPages = (int)totalCount % pageSize;

			if(totalPages == 0)
				totalPages = (int)totalCount / pageSize;
			else
				totalPages = (int)totalCount / pageSize + 1;

			int result = 0;

			if(params.getPagingAction() != 0) {
				if(params.getPagingAction() == RequestParams.PAGING_ACTION_NEXT10) {
					result = (((currentPage - 1) / 10) * 10) + 11;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_NEXTEND) {
					result = totalPages;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_PREV10) {
					result = ((currentPage - 1) / 10) * 10;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_PREVEND) {
					result = 1;
				}
				currentPage = result;
			}

			if(previousPageSize != pageSize)
				currentPage = 1;

			previousPageSize = pageSize;

			if((long)((pageSize * (currentPage - 1)) + 1) > totalCount)
				currentPage = 1;

			if (currentPage > 0)
				swdRecordCond.setPageNo(currentPage-1);

			swdRecordCond.setPageSize(pageSize);

			SortingField sf = params.getSortingField();
			String fieldId = "";
			boolean isAsc;

			SortingField sortingField = new SortingField();
			if (sf != null) {
				fieldId = sf.getFieldId();
				if(fieldId.equals(FormField.ID_SUBJECT)) {
					if(workId.equals(SmartWork.ID_EVENT_MANAGEMENT))
						fieldId = "name";
					else
						fieldId = "title";
				}
				isAsc = sf.isAscending();

				sortingField.setFieldId(sf.getFieldId());
				sortingField.setAscending(isAsc);

			} else {
				fieldId = FormField.ID_LAST_MODIFIED_DATE;
				isAsc = false;
			}

			swdRecordCond.setOrders(new Order[]{new Order(fieldId, isAsc)});

			SwdRecord[] swdRecords = getSwdManager().getRecords(userId, swdRecordCond, IManager.LEVEL_LITE);

			SwdRecordExtend[] swdRecordExtends = getSwdManager().getCtgPkg(workId);

			List<WorkInstanceInfo> workInstanceInfoList = new ArrayList<WorkInstanceInfo>();
			WorkInstanceInfo[] workInstanceInfos = null;

			String subCtgId = swdRecordExtends[0].getSubCtgId();
			String subCtgName = swdRecordExtends[0].getSubCtg();
			String parentCtgId = swdRecordExtends[0].getParentCtgId();
			String parentCtgName = swdRecordExtends[0].getParentCtg();
			String formId = swdDomain.getFormId();
			String formName = swdDomain.getFormName();

			if(!CommonUtil.isEmpty(swdRecords)) {
				int swdRecordsLength = swdRecords.length;
				for(int i=0; i < swdRecordsLength; i++) {
					SwdRecord swdRecord = swdRecords[i];
					String recordId = swdRecord.getRecordId();
					SwdDataField[] swdDataFields = swdRecord.getDataFields();
					WorkInstanceInfo workInstanceInfo = null;
					if(workId.equals(SmartWork.ID_BOARD_MANAGEMENT)) {
						BoardInstanceInfo tempWorkInstanceInfo = new BoardInstanceInfo();
						tempWorkInstanceInfo.setType(Instance.TYPE_BOARD);
						tempWorkInstanceInfo.setViews(swdRecord.getHits());
						tempWorkInstanceInfo.setLikers(ModelConverter.getLikersUserIdArray(userId, Instance.TYPE_BOARD, recordId));
						if(!CommonUtil.isEmpty(swdDataFields)) {
							int swdDataFieldsLength = swdDataFields.length;
							for(int j=0; j<swdDataFieldsLength; j++) {
								SwdDataField swdDataField = swdDataFields[j];
								String value = swdDataField.getValue();
								if(swdDataField.getId().equals("0")) {
									tempWorkInstanceInfo.setSubject(StringUtil.subString(value, 0, 32, "..."));
								} else if(swdDataField.getId().equals("1")) {
									tempWorkInstanceInfo.setContent(value);
									tempWorkInstanceInfo.setBriefContent(StringUtil.subString(value, 0, 120, "..."));
								} else if(swdDataField.getId().equals("2")) {
									if(!CommonUtil.isEmpty(value)) {
										tempWorkInstanceInfo.setFileGroupId(value);
										List<IFileModel> fileModelList = getDocManager().findFileGroup(value);
										List<Map<String, String>> fileList = new ArrayList<Map<String,String>>();
										int fileModelListSize = fileModelList.size();
										if(fileList != null && fileModelListSize > 0) {
											for(int k=0; k<fileModelListSize; k++) {
												Map<String, String> fileMap = new LinkedHashMap<String, String>();
												IFileModel fileModel = fileModelList.get(k);
												String fileId = fileModel.getId();
												String fileName = fileModel.getFileName();
												String fileType = fileModel.getType();
												String fileSize = fileModel.getFileSize() + "";
												fileMap.put("fileId", fileId);
												fileMap.put("fileName", fileName);
												fileMap.put("fileType", fileType);
												fileMap.put("fileSize", fileSize);
												fileList.add(fileMap);
											}
											if(fileList.size() > 0)
												tempWorkInstanceInfo.setFiles(fileList);
										}
									}
								}
							}
						}
						workInstanceInfo = tempWorkInstanceInfo;
					} else if(workId.equals(SmartWork.ID_MEMO_MANAGEMENT)) {
						MemoInstanceInfo tempWorkInstanceInfo = new MemoInstanceInfo();
						tempWorkInstanceInfo.setType(Instance.TYPE_MEMO);
						tempWorkInstanceInfo.setViews(swdRecord.getHits());
						tempWorkInstanceInfo.setLikers(ModelConverter.getLikersUserIdArray(userId, Instance.TYPE_MEMO, recordId));
						if(!CommonUtil.isEmpty(swdDataFields)) {
							int swdDataFieldsLength = swdDataFields.length;
							for(int j=0; j<swdDataFieldsLength; j++) {
								SwdDataField swdDataField = swdDataFields[j];
								String value = swdDataField.getValue();
								if(swdDataField.getId().equals("4")) {
									tempWorkInstanceInfo.setContent(value);
									tempWorkInstanceInfo.setBriefContent(StringUtil.subString(value, 0, 120, "..."));
								}
							}
						}
						workInstanceInfo = tempWorkInstanceInfo;
					} else if(workId.equals(SmartWork.ID_EVENT_MANAGEMENT)) {
						EventInstanceInfo tempWorkInstanceInfo = new EventInstanceInfo();
						tempWorkInstanceInfo.setType(Instance.TYPE_EVENT);
						tempWorkInstanceInfo.setViews(swdRecord.getHits());
						tempWorkInstanceInfo.setLikers(ModelConverter.getLikersUserIdArray(userId, Instance.TYPE_EVENT, recordId));
						if(!CommonUtil.isEmpty(swdDataFields)) {
							int swdDataFieldsLength = swdDataFields.length;
							for(int j=0; j<swdDataFieldsLength; j++) {
								SwdDataField swdDataField = swdDataFields[j];
								String value = swdDataField.getValue();
								LocalDate startLocalDate = null;
								LocalDate endLocalDate = null;
								UserInfo[] relatedUsers = null;
								if(swdDataField.getId().equals("6")) {
									tempWorkInstanceInfo.setContent(value);
									tempWorkInstanceInfo.setBriefContent(StringUtil.subString(value, 0, 120, "..."));
								} else if(swdDataField.getId().equals("1")) {
									if (!CommonUtil.isEmpty(value)) {
										Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss").parse(value);
										startLocalDate = new LocalDate(startDate.getTime());
									}
									tempWorkInstanceInfo.setStart(startLocalDate);
								} else if(swdDataField.getId().equals("2")) {
									if (!CommonUtil.isEmpty(value)) {
										Date endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss").parse(value);
										endLocalDate = new LocalDate(endDate.getTime());
									}
									tempWorkInstanceInfo.setEnd(endLocalDate);
								} else if(swdDataField.getId().equals("5")) {
									if (value != null) {
										String[] userIdArray = value.split(";");
										relatedUsers = new UserInfo[userIdArray.length];
										for(int k= 0; k<userIdArray.length; k++) {
											relatedUsers[k] = ModelConverter.getUserInfoByUserId(userIdArray[k]);
										}
									}
									tempWorkInstanceInfo.setRelatedUsers(relatedUsers);
								}
							}
						}
						workInstanceInfo = tempWorkInstanceInfo;
					}

					workInstanceInfo.setId(recordId);
					workInstanceInfo.setOwner(ModelConverter.getUserInfoByUserId(swdRecord.getCreationUser()));
					workInstanceInfo.setCreatedDate(new LocalDate((swdRecord.getCreationDate()).getTime()));

					WorkSpaceInfo workSpaceInfo = communityService.getWorkSpaceInfoById(workSpaceId);

					workInstanceInfo.setWorkSpace(workSpaceInfo);

					WorkCategoryInfo groupInfo = null;
					if (!CommonUtil.isEmpty(subCtgId))
						groupInfo = new WorkCategoryInfo(subCtgId, subCtgName);

					WorkCategoryInfo categoryInfo = new WorkCategoryInfo(parentCtgId, parentCtgName);

					WorkInfo workInfo = new SmartWorkInfo(formId, formName, SocialWork.TYPE_BOARD, groupInfo, categoryInfo);

					workInstanceInfo.setWork(workInfo);
					workInstanceInfo.setLastModifier(ModelConverter.getUserInfoByUserId(swdRecord.getModificationUser()));
					workInstanceInfo.setLastModifiedDate(new LocalDate((swdRecord.getModificationDate()).getTime()));

					CommentInstanceInfo[] subInstanceInfos = seraService.getSubInstancesByRefId(recordId, WorkInstance.FETCH_ALL_SUB_INSTANCE);
					int subInstanceCount = 0;
					if(!CommonUtil.isEmpty(subInstanceInfos)) {
						subInstanceCount = subInstanceInfos.length;
						if(subInstanceInfos.length > WorkInstance.DEFAULT_SUB_INSTANCE_FETCH_COUNT) {
							CommentInstanceInfo[] defaultInstanceInfos = new CommentInstanceInfo[WorkInstance.DEFAULT_SUB_INSTANCE_FETCH_COUNT];
							for(int j=0; j<WorkInstance.DEFAULT_SUB_INSTANCE_FETCH_COUNT; j++) {
								CommentInstanceInfo subInstanceInfo = subInstanceInfos[j];
								defaultInstanceInfos[j] = subInstanceInfo;
							}
							workInstanceInfo.setSubInstances(defaultInstanceInfos);
						} else {
							workInstanceInfo.setSubInstances(subInstanceInfos);
						}
					}
					workInstanceInfo.setSubInstanceCount(subInstanceCount);

					workInstanceInfoList.add(workInstanceInfo);
				}
				if(workInstanceInfoList.size() > 0) {
					workInstanceInfos = new WorkInstanceInfo[workInstanceInfoList.size()];
					workInstanceInfoList.toArray(workInstanceInfos);
				}
			}

			instanceInfoList.setInstanceDatas(workInstanceInfos);
			instanceInfoList.setTotalSize((int)totalCount);
			instanceInfoList.setSortedField(sortingField);
			instanceInfoList.setPageSize(pageSize);
			instanceInfoList.setTotalPages(totalPages);
			instanceInfoList.setCurrentPage(currentPage);

			return instanceInfoList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public InstanceInfoList getInstanceInfoListByRefType(String spaceId, RequestParams params, String refType, int displayBy, String parentId) throws Exception {

		try {
			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();

			InstanceInfoList instanceInfoList = new InstanceInfoList();

			TaskWorkCond taskWorkCond = new TaskWorkCond();
			taskWorkCond.setTskAssigneeOrSpaceId(spaceId);
			taskWorkCond.setTskRefType(refType);
			taskWorkCond.setSearchKey(params.getSearchKey());

			long totalCount = getWorkListManager().getTaskWorkListSize(userId, taskWorkCond);

			int pageSize = params.getPageSize();
			if(pageSize == 0) pageSize = 20;

			int currentPage = params.getCurrentPage();
			if(currentPage == 0) currentPage = 1;

			int totalPages = (int)totalCount % pageSize;

			if(totalPages == 0)
				totalPages = (int)totalCount / pageSize;
			else
				totalPages = (int)totalCount / pageSize + 1;

			int result = 0;

			if(params.getPagingAction() != 0) {
				if(params.getPagingAction() == RequestParams.PAGING_ACTION_NEXT10) {
					result = (((currentPage - 1) / 10) * 10) + 11;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_NEXTEND) {
					result = totalPages;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_PREV10) {
					result = ((currentPage - 1) / 10) * 10;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_PREVEND) {
					result = 1;
				}
				currentPage = result;
			}

			if(previousPageSize != pageSize)
				currentPage = 1;

			previousPageSize = pageSize;

			if((long)((pageSize * (currentPage - 1)) + 1) > totalCount)
				currentPage = 1;

			if (currentPage > 0)
				taskWorkCond.setPageNo(currentPage-1);

			taskWorkCond.setPageSize(pageSize);

			SortingField sf = params.getSortingField();
			String fieldId = "";
			boolean isAsc;

			SortingField sortingField = new SortingField();
			if (sf != null) {
				fieldId = sf.getFieldId();
				if(fieldId.equals(FormField.ID_SUBJECT))
					fieldId = "tskTitle";
				else if(fieldId.equals(FormField.ID_LAST_MODIFIER))
					fieldId = "tskassignee";
				else if(fieldId.equals(FormField.ID_LAST_MODIFIED_DATE))
					fieldId = "taskLastModifyDate";
				else if(fieldId.equals(FormField.ID_STATUS))
					fieldId = "tskstatus";
				else if(fieldId.equals(FormField.ID_TASK_NAME))
					fieldId = "tskname";
				else if(fieldId.equals(FormField.ID_LAST_TASK))
					fieldId = "lastTask_tskname";
				else if(fieldId.equals(FormField.ID_PROCESS_TIME))
					fieldId = "tskExecuteDate";
				else if(fieldId.equals(FormField.ID_PROCESS_TYPE))
					fieldId = "prcType";
				else if(fieldId.equals(FormField.ID_WORK_INSTANCE))
					fieldId = "packageName";
				else if(fieldId.equals(FormField.ID_WORK_SPACE))
					fieldId = "tskWorkSpaceId";
				else if(fieldId.equals(FormField.ID_OWNER))
					fieldId = "tskassignee";
				else if(fieldId.equals(FormField.ID_CREATED_DATE))
					fieldId = "tskcreatedate";
				isAsc = sf.isAscending();

				sortingField.setFieldId(sf.getFieldId());
				sortingField.setAscending(isAsc);

			} else {
				fieldId = "taskLastModifyDate";
				isAsc = false;
			}

			taskWorkCond.setOrders(new Order[]{new Order(fieldId, isAsc)});

			TaskWork[] taskWorks = getWorkListManager().getTaskWorkList(userId, taskWorkCond);

			WorkInstanceInfo[] workInstanceInfos = ModelConverter.getWorkInstanceInfosByTaskWorks(taskWorks);

			instanceInfoList.setTotalSize((int)totalCount);
			instanceInfoList.setInstanceDatas(workInstanceInfos);
			instanceInfoList.setType(InstanceInfoList.TYPE_INFORMATION_INSTANCE_LIST);
			instanceInfoList.setPageSize(pageSize);
			instanceInfoList.setTotalPages(totalPages);
			instanceInfoList.setCurrentPage(currentPage);
			instanceInfoList.setSortedField(sortingField);
			return instanceInfoList;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public InstanceInfoList getWorkInstanceList(String workSpaceId, RequestParams params) throws Exception {
		return getInstanceInfoListByRefType(workSpaceId, params, TskTask.TASKREFTYPE_NOTHING, -1, "");
	}

	
	public InstanceInfoList getImageInstanceList(String workSpaceId, RequestParams params) throws Exception {
		return getInstanceInfoListByRefType(workSpaceId, params, TskTask.TASKREFTYPE_IMAGE, -1, "");
	}

	public InstanceInfoList getInstanceInfoListByImageList(String workSpaceId, RequestParams params, int displayBy, String parentId) {
		try {
			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();

			InstanceInfoList instanceInfoList = new InstanceInfoList();

			FileWorkCond fileWorkCond = new FileWorkCond();
			fileWorkCond.setTskAssigneeOrSpaceId(workSpaceId);
			fileWorkCond.setTskRefType(TskTask.TASKREFTYPE_IMAGE);

			switch (displayBy) {
			case FileCategory.DISPLAY_BY_CATEGORY:
				fileWorkCond.setFolderId(parentId);
				break;
			case FileCategory.DISPLAY_BY_YEAR:
				fileWorkCond.setWrittenTimeMonthString(parentId);
				break;
			case FileCategory.DISPLAY_BY_OWNER:
				fileWorkCond.setTskAssignee(parentId);
				break;
			}

			FileWork[] totalFileWorks = getDocManager().getFileWorkList(userId, fileWorkCond);
			int viewCount = 0;
			if(!CommonUtil.isEmpty(totalFileWorks)) {
				for(FileWork totalFileWork : totalFileWorks) {
					boolean isAccessForMe = ModelConverter.isAccessableInstance(totalFileWork);
					if(isAccessForMe) {
						viewCount = viewCount + 1;
					}
				}
			}

			//long totalCount = getDocManager().getFileWorkListSize(userId, fileWorkCond);

			SortingField sf = params.getSortingField();
			String columnName = "";
			boolean isAsc;

			if (sf != null) {
				String defaultSortingField = sf.getFieldId();
				if(defaultSortingField.equals(FormField.ID_LAST_MODIFIED_DATE))
					defaultSortingField = "taskLastModifyDate";
				columnName  = CommonUtil.toDefault(defaultSortingField, FormField.ID_LAST_MODIFIED_DATE);
				isAsc = sf.isAscending();
			} else {
				columnName = "taskLastModifyDate";
				isAsc = false;
			}
			SortingField sortingField = new SortingField();
			sortingField.setFieldId(columnName);
			sortingField.setAscending(isAsc);

			fileWorkCond.setOrders(new Order[]{new Order(columnName, isAsc)});

			int pageSize = params.getPageSize();
			if(pageSize == 0) pageSize = 20;

			int currentPage = params.getCurrentPage();
			if(currentPage == 0) currentPage = 1;

			int totalPages = viewCount % pageSize;

			if(totalPages == 0)
				totalPages = viewCount / pageSize;
			else
				totalPages = viewCount / pageSize + 1;

			int result = 0;

			if(params.getPagingAction() != 0) {
				if(params.getPagingAction() == RequestParams.PAGING_ACTION_NEXT10) {
					result = (((currentPage - 1) / 10) * 10) + 11;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_NEXTEND) {
					result = totalPages;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_PREV10) {
					result = ((currentPage - 1) / 10) * 10;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_PREVEND) {
					result = 1;
				}
				currentPage = result;
			}

			if(previousPageSize != pageSize)
				currentPage = 1;

			previousPageSize = pageSize;

			if((long)((pageSize * (currentPage - 1)) + 1) > viewCount)
				currentPage = 1;

			if (currentPage > 0)
				fileWorkCond.setPageNo(currentPage-1);

			fileWorkCond.setPageSize(pageSize);
			//fileWorkCond.setOrders(new Order[]{new Order("tskCreatedate", false)});

			List<FileWork> fileWorkList = new ArrayList<FileWork>();
			FileWork[] finalFileWorks = null;

			FileWork[] fileWorks = getDocManager().getFileWorkList(userId, fileWorkCond);

			WorkInstanceInfo[] workInstanceInfos = null;
			if(!CommonUtil.isEmpty(fileWorks)) {
				for(FileWork fileWork : fileWorks) {
					boolean isAccessForMe = ModelConverter.isAccessableInstance(fileWork);
					if(isAccessForMe) {
						fileWorkList.add(fileWork);
					}
				}
			}
			if(fileWorkList.size() > 0) {
				finalFileWorks = new FileWork[fileWorkList.size()];
				fileWorkList.toArray(finalFileWorks);
			}
			if(!CommonUtil.isEmpty(finalFileWorks))
				workInstanceInfos = ModelConverter.getWorkInstanceInfosByFileWorks(finalFileWorks, TskTask.TASKREFTYPE_IMAGE, displayBy);

/*			List<WorkInstanceInfo> newWorkInstanceInfoList = new ArrayList<WorkInstanceInfo>();
			for(WorkInstanceInfo workInstanceInfo : workInstanceInfos) {
				ImageInstanceInfo imageInstanceInfo = (ImageInstanceInfo)workInstanceInfo;
				if(imageInstanceInfo.getFileCategory().getId().equals(parentId)) {
					newWorkInstanceInfoList.add(workInstanceInfo);
				}
			}
			if(newWorkInstanceInfoList.size() > 0) {
				workInstanceInfos = new WorkInstanceInfo[newWorkInstanceInfoList.size()];
				newWorkInstanceInfoList.toArray(workInstanceInfos);
			} else {
				workInstanceInfos = null;
			}*/

			instanceInfoList.setTotalSize(viewCount);
			instanceInfoList.setInstanceDatas(workInstanceInfos);
			instanceInfoList.setType(InstanceInfoList.TYPE_INFORMATION_INSTANCE_LIST);
			instanceInfoList.setPageSize(pageSize);
			instanceInfoList.setTotalPages(totalPages);
			instanceInfoList.setCurrentPage(currentPage);

			return instanceInfoList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ImageInstanceInfo[] getImageInstancesByDate(int displayBy, String wid, String parentId, LocalDate lastDate, int maxCount) throws Exception{

		ImageInstanceInfo[] imageInstanceInfos = null;
		RequestParams params = new RequestParams();
		params.setPageSize(maxCount);
		InstanceInfoList instanceInfoList = getInstanceInfoListByImageList(wid, params, displayBy, parentId);
		if(instanceInfoList != null) {
			WorkInstanceInfo[] workInstanceInfos = (WorkInstanceInfo[])instanceInfoList.getInstanceDatas();
			if(!CommonUtil.isEmpty(workInstanceInfos)) {
				int workInstanceInfosLength = workInstanceInfos.length;
				imageInstanceInfos = new ImageInstanceInfo[workInstanceInfosLength];
				for(int i=0; i<workInstanceInfosLength; i++) {
					ImageInstanceInfo imageInstanceInfo = (ImageInstanceInfo)workInstanceInfos[i];
					imageInstanceInfos[i] = imageInstanceInfo;
				}
				return imageInstanceInfos;
			}
		}
		return null;

	}

	public InstanceInfoList getInstanceInfoListByFileList(String workSpaceId, RequestParams params, int displayBy) {
		try {
			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();

			InstanceInfoList instanceInfoList = new InstanceInfoList();

			FileWorkCond fileWorkCond = new FileWorkCond();
			if (!CommonUtil.isEmpty(params)) {
				fileWorkCond.setSearchKey(CommonUtil.toNull(params.getSearchKey()));
			}
			fileWorkCond.setTskAssigneeOrSpaceId(workSpaceId);

			SearchFilter searchFilter = params.getSearchFilter();
			if(searchFilter != null) {
				Condition[] conditions = searchFilter.getConditions();
				if(!CommonUtil.isEmpty(conditions) && conditions.length == 1) {
					Condition condition = conditions[0];
					String rightOperand = String.valueOf(condition.getRightOperand());
					if(!rightOperand.equals(FileCategory.ID_ALL_FILES)) {
						switch (displayBy) {
						case FileCategory.DISPLAY_BY_CATEGORY:
							fileWorkCond.setFolderId(rightOperand);
							break;
						case FileCategory.DISPLAY_BY_WORK:
							fileWorkCond.setPackageId(rightOperand);
							break;
						case FileCategory.DISPLAY_BY_YEAR:
							fileWorkCond.setWrittenTimeMonthString(rightOperand);
							break;
						case FileCategory.DISPLAY_BY_OWNER:
							fileWorkCond.setTskAssignee(rightOperand);
							break;
						case FileCategory.DISPLAY_BY_FILE_TYPE:
							fileWorkCond.setFileType(rightOperand);
							break;
						}
					}
				}
			}

			/*SearchFilter searchFilter = params.getSearchFilter();
			if(searchFilter != null) {
				Condition[] conditions = searchFilter.getConditions();
				Filters filters = new Filters();
				List<Filter> filterList = new ArrayList<Filter>();
				for(Condition condition : conditions) {
					Filter filter = new Filter();
					FormField leftOperand = condition.getLeftOperand();
					String leftOperandValue = leftOperand.getId();
					String operator = condition.getOperator();
					Object rightOperand = condition.getRightOperand();
					String rightOperandValue = String.valueOf(rightOperand);
					if(!rightOperandValue.equals(FileCategory.ID_ALL_FILES)) {
						if(leftOperandValue.equals(FormField.ID_FILE_CATEGORY)) leftOperandValue = "folderId";
						else if(leftOperandValue.equals(FormField.ID_WORK)) leftOperandValue = "form.packageId";
						else if(leftOperandValue.equals(FormField.ID_CREATED_DATE)) leftOperandValue = "writtenTime";
						else if(leftOperandValue.equals(FormField.ID_OWNER)) leftOperandValue = "tskassignee";
						else if(leftOperandValue.equals(FormField.ID_FILE_TYPE)) leftOperandValue = "docfile.type";
						filter.setLeftOperandValue(leftOperandValue);
						filter.setOperator(operator);
						filter.setRightOperandValue(rightOperandValue);
						filterList.add(filter);
					}
				}

				Filter[] searchfilters = null;
				if(filterList.size() != 0) {
					searchfilters = new Filter[filterList.size()];
					filterList.toArray(searchfilters);
					filters.setFilter(searchfilters);
				}
				fileWorkCond.addFilters(filters);
			}*/

			FileWork[] totalFileWorks = getDocManager().getFileWorkList(userId, fileWorkCond);
			int viewCount = 0;
			if(!CommonUtil.isEmpty(totalFileWorks)) {
				for(FileWork totalFileWork : totalFileWorks) {
					boolean isAccessForMe = ModelConverter.isAccessableInstance(totalFileWork);
					if(isAccessForMe) {
						viewCount = viewCount + 1;
					}
				}
			}

			//long totalCount = getDocManager().getFileWorkListSize(userId, fileWorkCond);

			SortingField sf = params.getSortingField();
			String columnName = "";
			boolean isAsc;

			if (sf != null) {
				String defaultSortingField = sf.getFieldId();
				if(defaultSortingField.equals(FormField.ID_LAST_MODIFIED_DATE))
					defaultSortingField = "taskLastModifyDate";
				columnName  = CommonUtil.toDefault(defaultSortingField, FormField.ID_LAST_MODIFIED_DATE);
				isAsc = sf.isAscending();
			} else {
				columnName = "taskLastModifyDate";
				isAsc = false;
			}
			SortingField sortingField = new SortingField();
			sortingField.setFieldId(columnName);
			sortingField.setAscending(isAsc);

			fileWorkCond.setOrders(new Order[]{new Order(columnName, isAsc)});

			int pageSize = params.getPageSize();
			if(pageSize == 0) pageSize = 20;

			int currentPage = params.getCurrentPage();
			if(currentPage == 0) currentPage = 1;

			int totalPages = viewCount % pageSize;

			if(totalPages == 0)
				totalPages = viewCount / pageSize;
			else
				totalPages = viewCount / pageSize + 1;

			int result = 0;

			if(params.getPagingAction() != 0) {
				if(params.getPagingAction() == RequestParams.PAGING_ACTION_NEXT10) {
					result = (((currentPage - 1) / 10) * 10) + 11;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_NEXTEND) {
					result = totalPages;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_PREV10) {
					result = ((currentPage - 1) / 10) * 10;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_PREVEND) {
					result = 1;
				}
				currentPage = result;
			}

			if(previousPageSize != pageSize)
				currentPage = 1;

			previousPageSize = pageSize;

			if((long)((pageSize * (currentPage - 1)) + 1) > viewCount)
				currentPage = 1;

			if (currentPage > 0)
				fileWorkCond.setPageNo(currentPage-1);

			fileWorkCond.setPageSize(pageSize);
			//fileWorkCond.setOrders(new Order[]{new Order("tskCreatedate", false)});

			List<FileWork> fileWorkList = new ArrayList<FileWork>();
			FileWork[] finalFileWorks = null;

			FileWork[] fileWorks = getDocManager().getFileWorkList(userId, fileWorkCond);

			WorkInstanceInfo[] workInstanceInfos = null;
			if(!CommonUtil.isEmpty(fileWorks)) {
				for(FileWork fileWork : fileWorks) {
					boolean isAccessForMe = ModelConverter.isAccessableInstance(fileWork);
					if(isAccessForMe) {
						fileWorkList.add(fileWork);
					}
				}
			}
			if(fileWorkList.size() > 0) {
				finalFileWorks = new FileWork[fileWorkList.size()];
				fileWorkList.toArray(finalFileWorks);
			}
			if(!CommonUtil.isEmpty(finalFileWorks))
				workInstanceInfos = ModelConverter.getWorkInstanceInfosByFileWorks(finalFileWorks, TskTask.TASKREFTYPE_FILE, 0);

			instanceInfoList.setTotalSize(viewCount);
			instanceInfoList.setSortedField(sortingField);
			instanceInfoList.setInstanceDatas(workInstanceInfos);
			instanceInfoList.setType(InstanceInfoList.TYPE_INFORMATION_INSTANCE_LIST);
			instanceInfoList.setPageSize(pageSize);
			instanceInfoList.setTotalPages(totalPages);
			instanceInfoList.setCurrentPage(currentPage);

			return instanceInfoList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public InstanceInfoList getFileInstanceList(String workSpaceId, RequestParams params) throws Exception {
		SearchFilter searchFilter = params.getSearchFilter();
		int displayBy = 0;
		if(searchFilter != null) {
			 String searchFilterId = searchFilter.getId();
			 if(searchFilterId.equals(SearchFilter.FILTER_BY_FILE_CATEGORY_ID)) displayBy = FileCategory.DISPLAY_BY_CATEGORY;
			 if(searchFilterId.equals(SearchFilter.FILTER_BY_WORK_ID)) displayBy = FileCategory.DISPLAY_BY_WORK;
			 if(searchFilterId.equals(SearchFilter.FILTER_BY_CREATED_DATE)) displayBy = FileCategory.DISPLAY_BY_YEAR;
			 if(searchFilterId.equals(SearchFilter.FILTER_BY_OWNER)) displayBy = FileCategory.DISPLAY_BY_OWNER;
			 if(searchFilterId.equals(SearchFilter.FILTER_BY_FILE_TYPE)) displayBy = FileCategory.DISPLAY_BY_FILE_TYPE;
		}
		return getInstanceInfoListByFileList(workSpaceId, params, displayBy);
	}

	public EventInstanceInfo[] getEventInstanceList(String workSpaceId, LocalDate fromDate, LocalDate toDate) throws Exception {

		try {
			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();

			EventInstanceInfo[] eventInstanceInfos = null;
			List<EventInstanceInfo> eventInstanceInfoList = new ArrayList<EventInstanceInfo>();

			TaskWorkCond taskWorkCond = new TaskWorkCond();
			taskWorkCond.setTskAssigneeOrSpaceId(workSpaceId);
			taskWorkCond.setTskRefType(TskTask.TASKREFTYPE_EVENT);
			taskWorkCond.setOrders(new Order[]{new Order("tskCreatedate", false)});

			TaskWork[] totalWorks = getWorkListManager().getTaskWorkList(userId, taskWorkCond);

			List<TaskWork> taskWorkList = new ArrayList<TaskWork>();
			TaskWork[] resultWorks = null;

			if(!CommonUtil.isEmpty(totalWorks)) {
				for(TaskWork taskWork : totalWorks) {
					boolean isAccessableForMe = ModelConverter.isAccessableInstance(taskWork);
					if(isAccessableForMe)
						taskWorkList.add(taskWork);
				}
				if(taskWorkList.size() > 0) {
					resultWorks = new TaskWork[taskWorkList.size()];
					taskWorkList.toArray(resultWorks);
				}
			}
			if(!CommonUtil.isEmpty(resultWorks)) {
				int length = resultWorks.length;
				for(int i=0; i<length; i++) {
					TaskWork task = resultWorks[i];
					EventInstanceInfo eventInstanceInfo = new EventInstanceInfo();
					eventInstanceInfo.setType(Instance.TYPE_EVENT);
					SwdRecord record = (SwdRecord)SwdRecord.toObject(task.getTskDoc());
					String id = record.getRecordId();
					String subject = record.getDataFieldValue("0");
					String content = record.getDataFieldValue("6");
					String startDateStr = record.getDataFieldValue("1");
					LocalDate startLocalDate = null;
					String endDateStr = record.getDataFieldValue("2");
					LocalDate endLocalDate = null;
					SwdDataField relatedUsersField = record.getDataField("5");
					CommunityInfo[] relatedUsers = null;
					if (relatedUsersField != null) {
						String usersRecordId = relatedUsersField.getRefRecordId();
						String[] userIdArray = StringUtils.tokenizeToStringArray(usersRecordId, ";");
						relatedUsers = new UserInfo[userIdArray.length];
						for (int j = 0; j<userIdArray.length; j++) {
							relatedUsers[j] = ModelConverter.getUserInfoByUserId(userIdArray[j]);
						}
					}
					if (!CommonUtil.isEmpty(startDateStr)) {
						startLocalDate = LocalDate.convertGMTStringToLocalDate(startDateStr);
					}
					if (!CommonUtil.isEmpty(endDateStr)) {
						endLocalDate = LocalDate.convertGMTStringToLocalDate(endDateStr);
					}
					String owner = task.getTskAssignee();
					LocalDate createdDate = new LocalDate(task.getTskCreateDate().getTime());
					String modifier = task.getLastTskAssignee();
					LocalDate modifiedDate = new LocalDate(task.getTaskLastModifyDate().getTime());

					eventInstanceInfo.setId(id);
					eventInstanceInfo.setSubject(subject);
					eventInstanceInfo.setContent(content);
					eventInstanceInfo.setStart(startLocalDate);
					eventInstanceInfo.setEnd(endLocalDate);
					eventInstanceInfo.setRelatedUsers(relatedUsers);
					eventInstanceInfo.setOwner(ModelConverter.getUserInfoByUserId(owner));
					eventInstanceInfo.setCreatedDate(createdDate);
					eventInstanceInfo.setType(Instance.TYPE_EVENT);
					eventInstanceInfo.setStatus(WorkInstance.STATUS_COMPLETED);
					eventInstanceInfo.setWorkSpace(null);
					eventInstanceInfo.setLastModifier(ModelConverter.getUserInfoByUserId(modifier));
					eventInstanceInfo.setLastModifiedDate(modifiedDate);

					/*CommunityInfo[] participants = eventInstanceInfo.getRelatedUsers();
					boolean isParticipant = false;
					if(!CommonUtil.isEmpty(participants))
						isParticipant =  calendarService.isParticipant(participants);
					if(isParticipant || owner.equals(userId) || modifier.equals(userId))*/
						eventInstanceInfoList.add(eventInstanceInfo);

					/*String tskAccessLevel = task.getTskAccessLevel();
					String tskAccessValue = task.getTskAccessValue();

					if(startLocalDate.getTime() >= fromDate.getTime() && startLocalDate.getTime() <= toDate.getTime()) {
						if(!CommonUtil.isEmpty(tskAccessLevel)) {
							if(Integer.parseInt(tskAccessLevel) == AccessPolicy.LEVEL_PRIVATE) {
								if(owner.equals(userId) || modifier.equals(userId))
									eventInstanceInfoList.add(eventInstanceInfo);
							} else if(Integer.parseInt(tskAccessLevel) == AccessPolicy.LEVEL_CUSTOM) {
								if(!CommonUtil.isEmpty(tskAccessValue)) {
									String[] accessValues = tskAccessValue.split(";");
									if(!CommonUtil.isEmpty(accessValues)) {
										for(String accessValue : accessValues) {
											if(!owner.equals(accessValue) && !modifier.equals(accessValue)) {
												if(accessValue.equals(userId))
													eventInstanceInfoList.add(eventInstanceInfo);
											}
										}
										if(owner.equals(userId) || modifier.equals(userId))
											eventInstanceInfoList.add(eventInstanceInfo);
									}
								}
							} else {
								eventInstanceInfoList.add(eventInstanceInfo);
							}
						} else {
							eventInstanceInfoList.add(eventInstanceInfo);
						}
					}*/
				}
			}
			if(eventInstanceInfoList.size() > 0) {
				eventInstanceInfos = new EventInstanceInfo[eventInstanceInfoList.size()];
				eventInstanceInfoList.toArray(eventInstanceInfos);
			}

			return eventInstanceInfos;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		//return calendarService.getEventInstanceInfosByWorkSpaceId(workSpaceId, fromDate, toDate);

	}

	public InstanceInfoList getMemoInstanceList(String workSpaceId, RequestParams params) throws Exception {
		return getInstanceInfoListByRefType(workSpaceId, params, TskTask.TASKREFTYPE_MEMO, -1, "");
	}

	public InstanceInfoList getBoardInstanceList(String workSpaceId, RequestParams params) throws Exception {
		return getInstanceInfoListByWorkId(workSpaceId, params, SmartWork.ID_BOARD_MANAGEMENT);

		// 위의 getInstanceInfoListByRefType()를 이용하여 수정바랍니다.
		// 아래의 getIWorkInstanceList는 WorkSpace별로 인스턴스를 가져올수가 없으면 반환되는 인스턴스도 IWInstanceInfo[]이기때문에 사용할 수 없음.
		// WorkSpace별로 BoardInstanceInfo[]로 가져올 수 있어야 함..
		//return getIWorkInstanceList(SmartWork.ID_BOARD_MANAGEMENT, params);

	}

	public InstanceInfoList getPWorkInstanceList_bak(String workId, RequestParams params) throws Exception {

		try{
			//date to localdate - Date startTime = new Date();
			LocalDate startTime = new LocalDate();
			Long start = startTime.getTime();
			//TODO workId = category 프로세스 인스턴스정보에는 패키지 컬럼이 없고 다이어 그램 컬럼에 정보가 들어가 있다
			//임시로 프로세스 다이어그램아이디 필드를 이용하고 프로세스인스턴스가 생성되는 시점(업무 시작, 처리 개발 완료)에 패키지 아이디 컬럼을 추가해 그곳에서 조회하는걸로 변경한다
	
			User user = SmartUtil.getCurrentUser();
			PrcProcessCond prcCond = new PrcProcessCond();
			prcCond.setDiagramId(workId);
			prcCond.setCompanyId(user.getCompanyId());
			PrcProcess[] prc = getPrcManager().getProcesses(user.getId(), prcCond, IManager.LEVEL_LITE);
			if (prc == null)
				return null;
			
			PrcProcessInstCond prcInstCond = new PrcProcessInstCond();
			prcInstCond.setCompanyId(user.getCompanyId());
			prcInstCond.setProcessId(prc[0].getProcessId());
			
			long totalCount = getPrcManager().getProcessInstSize(user.getId(), prcInstCond);
			
			int currentPage = params.getCurrentPage();
			int pageCount = params.getPageSize();
			SortingField sf = params.getSortingField();
			
			//임시로 무조건 오더링 한다
			if (sf != null || true) {
				//TODO fieldId 가 없음 프로세스 업무는 아래 처럼 컬럼(모델 필드명) 이름을 직접 주어야 한다
				String fieldName = "creationDate";
				boolean isAsc = false;
				prcInstCond.setOrders(new Order[]{new Order(fieldName, isAsc)});
			}
			
			prcInstCond.setPageNo(currentPage);
			prcInstCond.setPageSize(pageCount);
			
			PrcProcessInst[] prcInsts = getPrcManager().getProcessInsts(user.getId(), prcInstCond, IManager.LEVEL_LITE);
			
			InstanceInfoList instanceInfoList = new InstanceInfoList();
			instanceInfoList.setInstanceDatas(ModelConverter.getPWInstanceInfoArrayByPrcProcessInstArray(prcInsts));
			instanceInfoList.setPageSize(pageCount);
			instanceInfoList.setTotalPages((int)totalCount);
			instanceInfoList.setCurrentPage(currentPage);
			instanceInfoList.setTotalPages(InstanceInfoList.TYPE_PROCESS_INSTANCE_LIST);
	
			return instanceInfoList;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}
	public InstanceInfoList getPWorkInstanceList_bak2(String companyId, String userId, String workId, RequestParams params) throws Exception {

		try{
			//date to localdate - Date startTime = new Date();
			LocalDate startTime = new LocalDate();
			Long start = startTime.getTime();
			//TODO workId = category 프로세스 인스턴스정보에는 패키지 컬럼이 없고 다이어 그램 컬럼에 정보가 들어가 있다
			//임시로 프로세스 다이어그램아이디 필드를 이용하고 프로세스인스턴스가 생성되는 시점(업무 시작, 처리 개발 완료)에 패키지 아이디 컬럼을 추가해 그곳에서 조회하는걸로 변경한다
			PrcProcessCond prcCond = new PrcProcessCond();
			prcCond.setDiagramId(workId);
			prcCond.setCompanyId(companyId);
			PrcProcess[] prc = getPrcManager().getProcesses(userId, prcCond, IManager.LEVEL_LITE);
			if (prc == null)
				return null;
			PrcProcessInstCond prcInstCond = new PrcProcessInstCond();
			prcInstCond.setCompanyId(companyId);
			prcInstCond.setProcessId(prc[0].getProcessId());
			long totalCount = getPrcManager().getProcessInstSize(userId, prcInstCond);
			int currentPage = params.getCurrentPage();
			int pageCount = params.getPageSize();
			SortingField sf = params.getSortingField();
			//임시로 무조건 오더링 한다
			if (sf != null || true) {
				//TODO fieldId 가 없음 프로세스 업무는 아래 처럼 컬럼(모델 필드명) 이름을 직접 주어야 한다
				String fieldName = "creationDate";
				boolean isAsc = false;
				prcInstCond.setOrders(new Order[]{new Order(fieldName, isAsc)});
			}
			prcInstCond.setPageNo(currentPage);
			prcInstCond.setPageSize(pageCount);
			PrcProcessInst[] prcInsts = getPrcManager().getProcessInsts(userId, prcInstCond, IManager.LEVEL_LITE);
			InstanceInfoList instanceInfoList = new InstanceInfoList();
			PWInstanceInfo[] pWInstanceInfos = new PWInstanceInfo[prcInsts.length];
			for (int i = 0; i < prcInsts.length; i++) {
				PWInstanceInfo pwInstInfo = new PWInstanceInfo();
				PrcProcessInst prcInst = prcInsts[i];
				pwInstInfo.setId(prcInst.getObjId());
				pwInstInfo.setOwner(ModelConverter.getUserInfoByUserId(prcInst.getCreationUser()));
				int status = -1;
				if (prcInst.getStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_RUNNING)) {
					status = Instance.STATUS_COMPLETED;
				} else if (prcInst.getStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_COMPLETE)) {
					status = Instance.STATUS_COMPLETED;
				}
				pwInstInfo.setStatus(status);
				pwInstInfo.setSubject(prcInst.getTitle());
				int type = WorkInstance.TYPE_PROCESS;
				pwInstInfo.setType(type);
				WorkInfo workInfo = ModelConverter.getWorkInfoByPackageId(prcInst.getDiagramId());
				pwInstInfo.setWork(workInfo);
				TskTask lastTskTask = ModelConverter.getLastExecutedTskTaskByPrcInstId(prcInst.getObjId());
				TaskInstanceInfo lastTaskInfo = null;
				TaskInstanceInfo runningTaskInfo = null;
				if (lastTskTask != null) {
					pwInstInfo.setLastModifiedDate(new LocalDate(lastTskTask.getExecutionDate().getTime()));//마지막태스크 수행일
					pwInstInfo.setLastModifier(ModelConverter.getUserInfoByUserId(lastTskTask.getAssignee()));//마지막태스크 수행자
					lastTaskInfo = new TaskInstanceInfo();
					String id = lastTskTask.getObjId();
					String subject = lastTskTask.getTitle();
					int tskType = WorkInstance.TYPE_TASK;
					String name = lastTskTask.getName();
					String assignee = lastTskTask.getAssignee();
					String performer = lastTskTask.getAssignee();
					int tskStatus = -1;
					if (lastTskTask.getStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ASSIGN)) {
						tskStatus = Instance.STATUS_COMPLETED;
					} else if (lastTskTask.getStatus().equalsIgnoreCase(TskTask.TASKSTATUS_COMPLETE)) {
						tskStatus = Instance.STATUS_COMPLETED;
					}
					UserInfo owner = ModelConverter.getUserInfoByUserId(lastTskTask.getCreationUser());
					UserInfo lastModifier = ModelConverter.getUserInfoByUserId(lastTskTask.getModificationUser()); 
					LocalDate lastModifiedDate = new LocalDate(lastTskTask.getModificationDate().getTime());
					lastTaskInfo.setId(id);
					lastTaskInfo.setLastModifiedDate(lastModifiedDate);
					lastTaskInfo.setLastModifier(lastModifier);
					lastTaskInfo.setOwner(owner);
					lastTaskInfo.setStatus(status);
					lastTaskInfo.setSubject(subject);
					lastTaskInfo.setType(type);
					lastTaskInfo.setWork(workInfo);
					lastTaskInfo.setWorkSpace(ModelConverter.getWorkSpaceInfo(prcInst.getWorkSpaceType(), prcInst.getWorkSpaceId()));
					lastTaskInfo.setName(name);
					lastTaskInfo.setTaskType(type);
					lastTaskInfo.setAssignee(ModelConverter.getUserInfoByUserId(assignee));
					lastTaskInfo.setPerformer(ModelConverter.getUserInfoByUserId(performer));
					//WorkInstanceInfo workInstanceInfo = paretProcessInstObj;
					pwInstInfo.setLastTask(lastTaskInfo);//마지막 태스크
				}
				TskTask runningTask = null;
				if (prcInst.getStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_RUNNING)) {
					runningTask = ModelConverter.getLastTskTaskByInstanceId(prcInst.getObjId());
					if (runningTask != null) {
						runningTaskInfo = new TaskInstanceInfo();
						String id = runningTask.getObjId();
						String subject = runningTask.getTitle();
						int tskType = WorkInstance.TYPE_TASK;
						String name = runningTask.getName();
						String assignee = runningTask.getAssignee();
						String performer = runningTask.getAssignee();
						int tskStatus = -1;
						if (runningTask.getStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ASSIGN)) {
							tskStatus = Instance.STATUS_COMPLETED;
						} else if (runningTask.getStatus().equalsIgnoreCase(TskTask.TASKSTATUS_COMPLETE)) {
							tskStatus = Instance.STATUS_COMPLETED;
						}
						UserInfo owner = ModelConverter.getUserInfoByUserId(lastTskTask.getCreationUser());
						UserInfo lastModifier = ModelConverter.getUserInfoByUserId(lastTskTask.getModificationUser()); 
						LocalDate lastModifiedDate = new LocalDate(lastTskTask.getModificationDate().getTime());
						runningTaskInfo.setId(id);
						runningTaskInfo.setLastModifiedDate(lastModifiedDate);
						runningTaskInfo.setLastModifier(lastModifier);
						runningTaskInfo.setOwner(owner);
						runningTaskInfo.setStatus(status);
						runningTaskInfo.setSubject(subject);
						runningTaskInfo.setType(type);
						runningTaskInfo.setWork(workInfo);
						runningTaskInfo.setWorkSpace(ModelConverter.getWorkSpaceInfo(runningTask.getWorkSpaceType(), runningTask.getWorkSpaceId()));
						runningTaskInfo.setName(name);
						runningTaskInfo.setTaskType(type);
						runningTaskInfo.setAssignee(ModelConverter.getUserInfoByUserId(assignee));
						runningTaskInfo.setPerformer(ModelConverter.getUserInfoByUserId(performer));
	//					pwInstInfo.setRunningTasks(new TaskInstanceInfo[]{runningTaskInfo});//실행중태스크
					}
				}
				pwInstInfo.setWorkSpace(ModelConverter.getWorkSpaceInfo(prcInst.getWorkSpaceType(), prcInst.getWorkSpaceId()));
				pWInstanceInfos[i] = pwInstInfo;
			}
	//		instanceInfoList.setInstanceDatas(ModelConverter.getPWInstanceInfoArrayByPrcProcessInstArray(prcInsts));
			instanceInfoList.setInstanceDatas(pWInstanceInfos);

			if((int)totalCount == 0)
				currentPage = 0;

			instanceInfoList.setPageSize(pageCount);
			instanceInfoList.setTotalPages((int)totalCount);
			instanceInfoList.setCurrentPage(currentPage);
			instanceInfoList.setTotalPages(InstanceInfoList.TYPE_PROCESS_INSTANCE_LIST);
	
			return instanceInfoList;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}

	@Override
	public WorkInstance getWorkInstanceById(int workType, String workId, String instanceId) throws Exception {
		//TODO 인스턴스로 패키지 타입을 알수가 없다 테이블에 컬럼을 생성하기는 했지만 초기 테스트시에는 데이터가 없기 때문에
		//인스턴스에 diagramId = pkgId 가 있으면 프로세스 업무 없으면 정보관리 업무로 판단한다

		try{
			User user = SmartUtil.getCurrentUser();
	
			if(workType == SmartWork.TYPE_PROCESS) {
				PrcProcessInst prcInst = getPrcManager().getProcessInst(user.getId(), instanceId, IManager.LEVEL_LITE);
				if (prcInst == null)
					return null;
				return getProcessWorkInstanceById(user.getCompanyId(), user.getId(), prcInst);
			} else if(workType == SmartWork.TYPE_INFORMATION){
				SwfFormCond swfFormCond = new SwfFormCond();
				swfFormCond.setCompanyId(user.getCompanyId());
				swfFormCond.setPackageId(workId);
				SwfForm[] swfForms = getSwfManager().getForms(user.getId(), swfFormCond, IManager.LEVEL_LITE);
				SwdRecordCond swdRecordCond = new SwdRecordCond();
				swdRecordCond.setCompanyId(user.getCompanyId());
				swdRecordCond.setFormId(swfForms[0].getId());
				swdRecordCond.setRecordId(instanceId);
				SwdRecord swdRecord = getSwdManager().getRecord(user.getId(), swdRecordCond, IManager.LEVEL_LITE);
				return getInformationWorkInstanceById(user.getCompanyId(), user.getId(), swdRecord);
				//return SmartTest.getInformationWorkInstance1();
			} else if(workType == SmartWork.TYPE_SCHEDULE) {
				return null;
			}

			return null;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}

	public ProcessWorkInstance getProcessWorkInstanceById(String companyId, String userId, PrcProcessInst prcInst) throws Exception {
		
		try{
			return ModelConverter.getProcessWorkInstanceByPrcProcessInst(userId, null, prcInst);
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}	
	}

	public InformationWorkInstance getInformationWorkInstanceById(String companyId, String userId, SwdRecord swdRecord) throws Exception {

		try{
			return ModelConverter.getInformationWorkInstanceBySwdRecord(userId, null, swdRecord);
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}
	private Map getLongTimeByTodayWorkHour(SwcWorkHour workHour) throws Exception {
		//type = start, end
		long workStartTime = 30600000;//오전 8시30분의 밀리세컨드
		long workEndTime = 64800000;//오후 6시00분의 밀리세컨드
		
		if (workHour != null) {
			Calendar now = Calendar.getInstance();
			Calendar tempDate = Calendar.getInstance();
			Date startDate = null;
			Date endDate = null;
			int day = now.get(Calendar.DAY_OF_WEEK);
			switch (day) {  
			   case 1 :
				   // "일" ;
				   startDate = workHour.getSunStartTime();
				   endDate = workHour.getSunEndTime();
				   tempDate.setTime(startDate);
				   workStartTime = (tempDate.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000) + (tempDate.get(Calendar.MINUTE) * 60 * 1000) 
						   + (tempDate.get(Calendar.SECOND) * 1000) + (tempDate.get(Calendar.MILLISECOND));
				   tempDate.setTime(endDate);
				   workEndTime =  (tempDate.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000) + (tempDate.get(Calendar.MINUTE) * 60 * 1000) 
						   + (tempDate.get(Calendar.SECOND) * 1000) + (tempDate.get(Calendar.MILLISECOND));
				   break ;
			   case 2 :
				   // "월" ;
				   startDate = workHour.getMonStartTime();
				   endDate = workHour.getMonEndTime();
				   tempDate.setTime(startDate);
				   workStartTime = (tempDate.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000) + (tempDate.get(Calendar.MINUTE) * 60 * 1000) 
						   + (tempDate.get(Calendar.SECOND) * 1000) + (tempDate.get(Calendar.MILLISECOND));
				   tempDate.setTime(endDate);
				   workEndTime =  (tempDate.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000) + (tempDate.get(Calendar.MINUTE) * 60 * 1000) 
						   + (tempDate.get(Calendar.SECOND) * 1000) + (tempDate.get(Calendar.MILLISECOND));
				   break ;
			   case 3 :
				   // "화" ;
				   startDate = workHour.getTueStartTime();
				   endDate = workHour.getTueEndTime();
				   tempDate.setTime(startDate);
				   workStartTime = (tempDate.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000) + (tempDate.get(Calendar.MINUTE) * 60 * 1000) 
						   + (tempDate.get(Calendar.SECOND) * 1000) + (tempDate.get(Calendar.MILLISECOND));
				   tempDate.setTime(endDate);
				   workEndTime =  (tempDate.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000) + (tempDate.get(Calendar.MINUTE) * 60 * 1000) 
						   + (tempDate.get(Calendar.SECOND) * 1000) + (tempDate.get(Calendar.MILLISECOND));
				   break ;
			   case 4 :
				   // "수" ;
				   startDate = workHour.getWedStartTime();
				   endDate = workHour.getWedEndTime();
				   tempDate.setTime(startDate);
				   workStartTime = (tempDate.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000) + (tempDate.get(Calendar.MINUTE) * 60 * 1000) 
						   + (tempDate.get(Calendar.SECOND) * 1000) + (tempDate.get(Calendar.MILLISECOND));
				   tempDate.setTime(endDate);
				   workEndTime =  (tempDate.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000) + (tempDate.get(Calendar.MINUTE) * 60 * 1000) 
						   + (tempDate.get(Calendar.SECOND) * 1000) + (tempDate.get(Calendar.MILLISECOND));
				   break ;
			  
			   case 5 :
				   // "목" ;
				   startDate = workHour.getThuStartTime();
				   endDate = workHour.getThuEndTime();
				   tempDate.setTime(startDate);
				   workStartTime = (tempDate.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000) + (tempDate.get(Calendar.MINUTE) * 60 * 1000) 
						   + (tempDate.get(Calendar.SECOND) * 1000) + (tempDate.get(Calendar.MILLISECOND));
				   tempDate.setTime(endDate);
				   workEndTime =  (tempDate.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000) + (tempDate.get(Calendar.MINUTE) * 60 * 1000) 
						   + (tempDate.get(Calendar.SECOND) * 1000) + (tempDate.get(Calendar.MILLISECOND));
				   break ;
			   case 6 :
				   // "금" ;
				   startDate = workHour.getFriStartTime();
				   endDate = workHour.getFriEndTime();
				   tempDate.setTime(startDate);
				   workStartTime = (tempDate.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000) + (tempDate.get(Calendar.MINUTE) * 60 * 1000) 
						   + (tempDate.get(Calendar.SECOND) * 1000) + (tempDate.get(Calendar.MILLISECOND));
				   tempDate.setTime(endDate);
				   workEndTime =  (tempDate.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000) + (tempDate.get(Calendar.MINUTE) * 60 * 1000) 
						   + (tempDate.get(Calendar.SECOND) * 1000) + (tempDate.get(Calendar.MILLISECOND));
				   break ;
			   case 7 :
				   // "토" ;  
				   startDate = workHour.getSatStartTime();
				   endDate = workHour.getSatEndTime();
				   tempDate.setTime(startDate);
				   workStartTime = (tempDate.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000) + (tempDate.get(Calendar.MINUTE) * 60 * 1000) 
						   + (tempDate.get(Calendar.SECOND) * 1000) + (tempDate.get(Calendar.MILLISECOND));
				   tempDate.setTime(endDate);
				   workEndTime =  (tempDate.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000) + (tempDate.get(Calendar.MINUTE) * 60 * 1000) 
						   + (tempDate.get(Calendar.SECOND) * 1000) + (tempDate.get(Calendar.MILLISECOND));
			  }
		}
		Map resultMap = new HashMap();
		resultMap.put("startTime", workStartTime);
		resultMap.put("endTime", workEndTime);
		return resultMap;
	}
	
	private TaskWork[] getTaskWorkByFromToDate(String contextId, String spaceId, Date fromDate, Date toDate, int maxSize) throws Exception {
		User cuser = SmartUtil.getCurrentUser();
		String userId = null;
		String companyId = null;
		if (cuser != null) {
			userId = cuser.getId();
			companyId = cuser.getCompanyId();
		}
		if (CommonUtil.isEmpty(contextId) || CommonUtil.isEmpty(spaceId) || CommonUtil.isEmpty(companyId))
			return null;
		
		//해당 날짜의 인스턴스를 조회 한다
		//사용자 공간(contextId = us.sp) 일경우는 task의 assignee(owner)가 spaceId 인것들을 조회 하고 
		//이외의 것들(부서, 그룹... 등의 공간) 일경우는 task의 spaceId 가 spaceId 인것을을 조회 한다
		TaskWorkCond taskWorkCond = new TaskWorkCond();
		if (contextId.equalsIgnoreCase("us.sp")) {
			
			//커런트 유져와 공간아이디가 같다면
			//assignee가 유져아이디 와 spaceid가 유져아이디인 테스크를 조회한다
			//커런트 유져와 공간아이디가 같지 않다면
			//spaceid가 공간아이디인 테스크를 조회한다
			if (userId.equalsIgnoreCase(spaceId)) {
				taskWorkCond.setTskAssigneeOrSpaceId(spaceId);
			} else {
				taskWorkCond.setTskWorkSpaceId(spaceId);
			}
		} else {
			taskWorkCond.setTskWorkSpaceId(spaceId);
		}
		
		taskWorkCond.setTskExecuteDateFrom(fromDate);
		taskWorkCond.setTskExecuteDateTo(toDate);
		
		taskWorkCond.setOrders(new Order[]{new Order("tskcreatedate", true)});
		
//		taskWorkCond.setPageNo(0);
//		taskWorkCond.setPageSize(maxSize);

		TaskWork[] tasks = getWorkListManager().getTaskWorkList(userId, taskWorkCond);
		
		return tasks;
	}
	
	@Override
	public TaskInstanceInfo[][] getTaskInstancesByWorkHours(String contextId, String spaceId, LocalDate date, int maxSize) throws Exception {
		try{
			User cuser = SmartUtil.getCurrentUser();
			String userId = null;
			String companyId = null;
			if (cuser != null) {
				userId = cuser.getId();
				companyId = cuser.getCompanyId();
			}
			if (CommonUtil.isEmpty(contextId) || CommonUtil.isEmpty(spaceId) || CommonUtil.isEmpty(companyId))
				return null;

			Date tempFromDate = null;
			Date tempToDate = null;
			if (date != null) {
				tempFromDate = new Date();
				tempToDate = new Date();

				tempFromDate.setTime(date.getLocalTime());
				tempToDate.setTime(date.getLocalTime());
				
				tempFromDate = DateUtil.toFromDate(tempFromDate, DateUtil.CYCLE_DAY);
				tempFromDate.setTime(tempFromDate.getTime() - TimeZone.getDefault().getRawOffset());
				
				tempToDate = DateUtil.toToDate(tempToDate, DateUtil.CYCLE_DAY);
				tempToDate.setTime(tempToDate.getTime() - TimeZone.getDefault().getRawOffset());
			}
			
			TaskWork[] tasks = getTaskWorkByFromToDate(contextId, spaceId, tempFromDate, tempToDate, maxSize);
			if (tasks == null)
				return null;
			//회사 워크아워 정책을 조회 한다
			SwcWorkHourCond workHourCond = new SwcWorkHourCond();
			workHourCond.setCompanyId(companyId);
			SwcWorkHour[] workHours = getSwcManager().getWorkhours(userId, workHourCond, IManager.LEVEL_LITE);
			Map workHourTimeMap = null;
			if (workHours == null || workHours.length == 0) {
				workHourTimeMap = getLongTimeByTodayWorkHour(null);
			} else {
				workHourTimeMap = getLongTimeByTodayWorkHour(workHours[0]);
			}
			//업무 시간 전, 업무시간, 업무시간 후로 인스턴스를 분류 한다
			
			List<TaskWork> beforeWorkTimeList = new ArrayList<TaskWork>();
			List<TaskWork> workTimeList = new ArrayList<TaskWork>();
			List<TaskWork> afterWorkTimeList = new ArrayList<TaskWork>();
			if (tasks != null & tasks.length != 0) {
				for (int i = 0; i < tasks.length; i++) {
					TaskWork task = tasks[i];
					Date executeDate = task.getTaskLastModifyDate();
					LocalDate localExecuteDate = new LocalDate(executeDate.getTime());
					
					Calendar cal = Calendar.getInstance();
					cal.setTime(new Date(localExecuteDate.getLocalDate()));
					
					long executeTime = (cal.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000) + (cal.get(Calendar.MINUTE) * 60 * 1000) 
					   + (cal.get(Calendar.SECOND) * 1000) + (cal.get(Calendar.MILLISECOND));
					
					if (executeTime < (Long)workHourTimeMap.get("startTime")) {
						beforeWorkTimeList.add(task);
					} else if (executeTime < (Long)workHourTimeMap.get("endTime") && executeTime > (Long)workHourTimeMap.get("startTime")) {
						workTimeList.add(task);
					} else {
						afterWorkTimeList.add(task);
					}
				}
			}
			TaskInstanceInfo[] beforeTaskInstanceInfo = null;
			TaskInstanceInfo[] taskInstanceInfo = null;
			TaskInstanceInfo[] afterInstanceInfo = null;
			
			if (beforeWorkTimeList.size() != 0) {
				TaskWork[] taskArray = new TaskWork[beforeWorkTimeList.size()];
				beforeWorkTimeList.toArray(taskArray);
				beforeTaskInstanceInfo = (TaskInstanceInfo[])ModelConverter.getTaskInstanceInfoArrayByTaskWorkArray(userId, taskArray);
				
				if (beforeTaskInstanceInfo != null && beforeTaskInstanceInfo.length > maxSize) {
					TaskInstanceInfo[] tempTaskInstanceInfo = new TaskInstanceInfo[maxSize + 1];
					for (int i = 0; i < maxSize; i++) {
						tempTaskInstanceInfo[i] = beforeTaskInstanceInfo[i];
					}
					TaskInstanceInfo moreInstance = new TaskInstanceInfo();
					moreInstance.setType(-21);
					tempTaskInstanceInfo[maxSize] = moreInstance;
					beforeTaskInstanceInfo = tempTaskInstanceInfo;
				}
			}
			if (workTimeList.size() != 0) {
				TaskWork[] taskArray = new TaskWork[workTimeList.size()];
				workTimeList.toArray(taskArray);
				taskInstanceInfo = (TaskInstanceInfo[])ModelConverter.getTaskInstanceInfoArrayByTaskWorkArray(userId, taskArray);
				
				if (taskInstanceInfo != null && taskInstanceInfo.length > maxSize) {
					TaskInstanceInfo[] tempTaskInstanceInfo = new TaskInstanceInfo[maxSize + 1];
					for (int i = 0; i < maxSize; i++) {
						tempTaskInstanceInfo[i] = taskInstanceInfo[i];
					}
					TaskInstanceInfo moreInstance = new TaskInstanceInfo();
					moreInstance.setType(-21);
					tempTaskInstanceInfo[maxSize] = moreInstance;
					taskInstanceInfo = tempTaskInstanceInfo;
				}
				
			}
			if (afterWorkTimeList.size() != 0) {
				TaskWork[] taskArray = new TaskWork[afterWorkTimeList.size()];
				afterWorkTimeList.toArray(taskArray);
				afterInstanceInfo = (TaskInstanceInfo[])ModelConverter.getTaskInstanceInfoArrayByTaskWorkArray(userId, taskArray);
				
				if (afterInstanceInfo != null && afterInstanceInfo.length > maxSize) {
					TaskInstanceInfo[] tempTaskInstanceInfo = new TaskInstanceInfo[maxSize + 1];
					for (int i = 0; i < maxSize; i++) {
						tempTaskInstanceInfo[i] = afterInstanceInfo[i];
					}
					TaskInstanceInfo moreInstance = new TaskInstanceInfo();
					moreInstance.setType(-21);
					tempTaskInstanceInfo[maxSize] = moreInstance;
					afterInstanceInfo = tempTaskInstanceInfo;
				}
			}	
			
			//리턴타입으로 생성후 리턴한다
			
			return new TaskInstanceInfo[][]{beforeTaskInstanceInfo, taskInstanceInfo, afterInstanceInfo};
			
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}
	@Override
	public TaskInstanceInfo[][] getTaskInstancesByDates(String contextId, String spaceId, LocalDate fromDate, LocalDate toDate, int maxSize) throws Exception {
		try{
			User cuser = SmartUtil.getCurrentUser();
			String userId = null;
			String companyId = null;
			if (cuser != null) {
				userId = cuser.getId();
				companyId = cuser.getCompanyId();
			}
			if (CommonUtil.isEmpty(contextId) || CommonUtil.isEmpty(spaceId) || CommonUtil.isEmpty(companyId))
				return null;
			
			Date tempFromDate = null;
			Date tempToDate = null;
			if (fromDate != null) {
				tempFromDate = new Date();
				tempFromDate.setTime(fromDate.getLocalTime());
				tempFromDate = DateUtil.toFromDate(tempFromDate, DateUtil.CYCLE_DAY);
				tempFromDate.setTime(tempFromDate.getTime() - TimeZone.getDefault().getRawOffset());
			} 
			if (toDate != null) {
				tempToDate = new Date();
				tempToDate.setTime(toDate.getLocalTime());
				tempToDate = DateUtil.toToDate(tempToDate, DateUtil.CYCLE_DAY);
				tempToDate.setTime(tempToDate.getTime() - TimeZone.getDefault().getRawOffset());
			}
			
			TaskWork[] tasks = getTaskWorkByFromToDate(contextId, spaceId, tempFromDate, tempToDate, maxSize);

			//회사 워크아워 정책을 조회 한다
			SwcWorkHourCond workHourCond = new SwcWorkHourCond();
			workHourCond.setCompanyId(companyId);
			SwcWorkHour[] workHours = getSwcManager().getWorkhours(userId, workHourCond, IManager.LEVEL_LITE);
			
			int startOfWeek = 2;
			if (workHours != null && workHours.length != 0)
				startOfWeek = Integer.parseInt(workHours[0].getStartDayOfWeek());
			
			Map<Integer, Integer> dayOfWeekMappingMap = new HashMap<Integer, Integer>();
			int weekNum = startOfWeek;
			for (int i = 1; i <= 7; i++) {
				dayOfWeekMappingMap.put(weekNum, i);
				if (weekNum == 7) {
					weekNum = 1;
				} else {
					weekNum = weekNum + 1;
				}
			}
			List instanceInfoList1 = new ArrayList();
			List instanceInfoList2 = new ArrayList();
			List instanceInfoList3 = new ArrayList();
			List instanceInfoList4 = new ArrayList();
			List instanceInfoList5 = new ArrayList();
			List instanceInfoList6 = new ArrayList();
			List instanceInfoList7 = new ArrayList();
			
			if (tasks != null && tasks.length != 0) {
				for (int i = 0; i < tasks.length; i++) {
					Date executeDate = tasks[i].getTaskLastModifyDate();
					LocalDate temp = new LocalDate(executeDate.getTime());
					
					switch (dayOfWeekMappingMap.get(temp.getDayOfWeek())) {
					case 1:
						instanceInfoList1.add(tasks[i]);
						break;
					case 2:
						instanceInfoList2.add(tasks[i]);
						break;
					case 3:
						instanceInfoList3.add(tasks[i]);
						break;
					case 4:
						instanceInfoList4.add(tasks[i]);
						break;
					case 5:
						instanceInfoList5.add(tasks[i]);
						break;
					case 6:
						instanceInfoList6.add(tasks[i]);
						break;
					case 7:
						instanceInfoList7.add(tasks[i]);
						break;
					}
				}
			}

			TaskInstanceInfo[] instanceInfo1 = null;
			TaskInstanceInfo[] instanceInfo2 = null;
			TaskInstanceInfo[] instanceInfo3 = null;
			TaskInstanceInfo[] instanceInfo4 = null;
			TaskInstanceInfo[] instanceInfo5 = null;
			TaskInstanceInfo[] instanceInfo6 = null;
			TaskInstanceInfo[] instanceInfo7 = null;
			
	
			if (instanceInfoList1.size() != 0) {
				TaskWork[] taskArray = new TaskWork[instanceInfoList1.size()];
				instanceInfoList1.toArray(taskArray);
				instanceInfo1 = (TaskInstanceInfo[])ModelConverter.getTaskInstanceInfoArrayByTaskWorkArray(userId, taskArray);
				
				if (instanceInfo1 != null && instanceInfo1.length > maxSize) {
					TaskInstanceInfo[] tempTaskInstanceInfo = new TaskInstanceInfo[maxSize + 1];
					for (int i = 0; i < maxSize; i++) {
						tempTaskInstanceInfo[i] = instanceInfo1[i];
					}
					TaskInstanceInfo moreInstance = new TaskInstanceInfo();
					moreInstance.setType(-21);
					tempTaskInstanceInfo[maxSize] = moreInstance;
					instanceInfo1 = tempTaskInstanceInfo;
				}
				
			}
			if (instanceInfoList2.size() != 0) {
				TaskWork[] taskArray = new TaskWork[instanceInfoList2.size()];
				instanceInfoList2.toArray(taskArray);
				instanceInfo2 = (TaskInstanceInfo[])ModelConverter.getTaskInstanceInfoArrayByTaskWorkArray(userId, taskArray);
				
				if (instanceInfo2 != null && instanceInfo2.length > maxSize) {
					TaskInstanceInfo[] tempTaskInstanceInfo = new TaskInstanceInfo[maxSize + 1];
					for (int i = 0; i < maxSize; i++) {
						tempTaskInstanceInfo[i] = instanceInfo2[i];
					}
					TaskInstanceInfo moreInstance = new TaskInstanceInfo();
					moreInstance.setType(-21);
					tempTaskInstanceInfo[maxSize] = moreInstance;
					instanceInfo2 = tempTaskInstanceInfo;
				}
			}
			if (instanceInfoList3.size() != 0) {
				TaskWork[] taskArray = new TaskWork[instanceInfoList3.size()];
				instanceInfoList3.toArray(taskArray);
				instanceInfo3 = (TaskInstanceInfo[])ModelConverter.getTaskInstanceInfoArrayByTaskWorkArray(userId, taskArray);
				
				if (instanceInfo3 != null && instanceInfo3.length > maxSize) {
					TaskInstanceInfo[] tempTaskInstanceInfo = new TaskInstanceInfo[maxSize + 1];
					for (int i = 0; i < maxSize; i++) {
						tempTaskInstanceInfo[i] = instanceInfo3[i];
					}
					TaskInstanceInfo moreInstance = new TaskInstanceInfo();
					moreInstance.setType(-21);
					tempTaskInstanceInfo[maxSize] = moreInstance;
					instanceInfo3 = tempTaskInstanceInfo;
				}
			}
			if (instanceInfoList4.size() != 0) {
				TaskWork[] taskArray = new TaskWork[instanceInfoList4.size()];
				instanceInfoList4.toArray(taskArray);
				instanceInfo4 = (TaskInstanceInfo[])ModelConverter.getTaskInstanceInfoArrayByTaskWorkArray(userId, taskArray);
				
				if (instanceInfo4 != null && instanceInfo4.length > maxSize) {
					TaskInstanceInfo[] tempTaskInstanceInfo = new TaskInstanceInfo[maxSize + 1];
					for (int i = 0; i < maxSize; i++) {
						tempTaskInstanceInfo[i] = instanceInfo4[i];
					}
					TaskInstanceInfo moreInstance = new TaskInstanceInfo();
					moreInstance.setType(-21);
					tempTaskInstanceInfo[maxSize] = moreInstance;
					instanceInfo4 = tempTaskInstanceInfo;
				}
			}
			if (instanceInfoList5.size() != 0) {
				TaskWork[] taskArray = new TaskWork[instanceInfoList5.size()];
				instanceInfoList5.toArray(taskArray);
				instanceInfo5 = (TaskInstanceInfo[])ModelConverter.getTaskInstanceInfoArrayByTaskWorkArray(userId, taskArray);
				
				if (instanceInfo5 != null && instanceInfo5.length > maxSize) {
					TaskInstanceInfo[] tempTaskInstanceInfo = new TaskInstanceInfo[maxSize + 1];
					for (int i = 0; i < maxSize; i++) {
						tempTaskInstanceInfo[i] = instanceInfo5[i];
					}
					TaskInstanceInfo moreInstance = new TaskInstanceInfo();
					moreInstance.setType(-21);
					tempTaskInstanceInfo[maxSize] = moreInstance;
					instanceInfo5 = tempTaskInstanceInfo;
				}
			}
			if (instanceInfoList6.size() != 0) {
				TaskWork[] taskArray = new TaskWork[instanceInfoList6.size()];
				instanceInfoList6.toArray(taskArray);
				instanceInfo6 = (TaskInstanceInfo[])ModelConverter.getTaskInstanceInfoArrayByTaskWorkArray(userId, taskArray);
				
				if (instanceInfo6 != null && instanceInfo6.length > maxSize) {
					TaskInstanceInfo[] tempTaskInstanceInfo = new TaskInstanceInfo[maxSize + 1];
					for (int i = 0; i < maxSize; i++) {
						tempTaskInstanceInfo[i] = instanceInfo6[i];
					}
					TaskInstanceInfo moreInstance = new TaskInstanceInfo();
					moreInstance.setType(-21);
					tempTaskInstanceInfo[maxSize] = moreInstance;
					instanceInfo6 = tempTaskInstanceInfo;
				}
			}
			if (instanceInfoList7.size() != 0) {
				TaskWork[] taskArray = new TaskWork[instanceInfoList7.size()];
				instanceInfoList7.toArray(taskArray);
				instanceInfo7 = (TaskInstanceInfo[])ModelConverter.getTaskInstanceInfoArrayByTaskWorkArray(userId, taskArray);
				
				if (instanceInfo7 != null && instanceInfo7.length > maxSize) {
					TaskInstanceInfo[] tempTaskInstanceInfo = new TaskInstanceInfo[maxSize + 1];
					for (int i = 0; i < maxSize; i++) {
						tempTaskInstanceInfo[i] = instanceInfo7[i];
					}
					TaskInstanceInfo moreInstance = new TaskInstanceInfo();
					moreInstance.setType(-21);
					tempTaskInstanceInfo[maxSize] = moreInstance;
					instanceInfo7 = tempTaskInstanceInfo;
				}
			}
//			return new TaskInstanceInfo[][]{monInstanceInfo, tueInstanceInfo, wedInstanceInfo, thuInstanceInfo, friInstanceInfo, satInstanceInfo, sunInstanceInfo};
			return new TaskInstanceInfo[][]{instanceInfo1, instanceInfo2, instanceInfo3, instanceInfo4, instanceInfo5, instanceInfo6, instanceInfo7};
			
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}
	@Override
	public TaskInstanceInfo[][] getTaskInstancesByWeeks(String contextId, String spaceId, LocalDate month, int maxSize) throws Exception {
		try{
			User cuser = SmartUtil.getCurrentUser();
			String userId = null;
			String companyId = null;
			if (cuser != null) {
				userId = cuser.getId();
				companyId = cuser.getCompanyId();
			}
			if (CommonUtil.isEmpty(contextId) || CommonUtil.isEmpty(spaceId) || CommonUtil.isEmpty(companyId))
				return null;
			

			Date tempFromDate = null;
			Date tempToDate = null;
			if (month != null) {
				tempFromDate = new Date();
				tempToDate = new Date();

				tempFromDate.setTime(month.getLocalTime());
				tempToDate.setTime(month.getLocalTime());
				
				tempFromDate = DateUtil.toFromDate(tempFromDate, DateUtil.CYCLE_MONTH);
				tempFromDate.setTime(tempFromDate.getTime() - TimeZone.getDefault().getRawOffset());
				
				tempToDate = DateUtil.toToDate(tempToDate, DateUtil.CYCLE_MONTH);
				tempToDate.setTime(tempToDate.getTime() - TimeZone.getDefault().getRawOffset());
				
			}
			
			TaskWork[] tasks = getTaskWorkByFromToDate(contextId, spaceId, tempFromDate, tempToDate, maxSize);	
			
			Date temp = new Date(month.getLocalTime());
			temp = DateUtil.toToDate(temp, DateUtil.CYCLE_MONTH);
			LocalDate endOfLocalDate = new LocalDate(temp.getTime() - TimeZone.getDefault().getRawOffset());
			int endOfWeek = endOfLocalDate.getWeekOfMonth(1);
			
			Map<Integer, List<TaskWork>> weekMappingMap = new HashMap<Integer, List<TaskWork>>();
			
			for (int i = 0; i < endOfWeek; i++) {
				List<TaskWork> tempList = new ArrayList<TaskWork>();
				weekMappingMap.put(i, tempList);
			}
			for (int i = 0; i < tasks.length; i++) {
				TaskWork task = tasks[i];
				Date executeDate = tasks[i].getTaskLastModifyDate();
				LocalDate tempExecuteDate = new LocalDate(executeDate.getTime());
				int weekOfMonth = tempExecuteDate.getWeekOfMonth(1);
				weekMappingMap.get(weekOfMonth-1).add(task);
			}
			
			TaskInstanceInfo[][] result = new TaskInstanceInfo[endOfWeek][];
			for (int i : weekMappingMap.keySet()) {
				List taskWorksList = weekMappingMap.get(i);
				TaskWork[] taskWorks = new TaskWork[taskWorksList.size()];
				taskWorksList.toArray(taskWorks);
				result[i] = (TaskInstanceInfo[])ModelConverter.getTaskInstanceInfoArrayByTaskWorkArray(userId, taskWorks);

				if (result[i] != null && result[i].length > maxSize) {
					TaskInstanceInfo[] tempTaskInstanceInfo = new TaskInstanceInfo[maxSize + 1];
					for (int j = 0; j < maxSize; j++) {
						tempTaskInstanceInfo[j] = result[i][j];
					}
					TaskInstanceInfo moreInstance = new TaskInstanceInfo();
					moreInstance.setType(-21);
					tempTaskInstanceInfo[maxSize] = moreInstance;
					result[i] = tempTaskInstanceInfo;
				}
			}
			
			return result;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}

	@Override
	public TaskInstanceInfo[] getTaskInstancesByDate(String contextId, String spaceId, LocalDate fromDate, LocalDate toDate, int maxSize) throws Exception {
		try{
			User cuser = SmartUtil.getCurrentUser();
			String userId = null;
			String companyId = null;
			if (cuser != null) {
				userId = cuser.getId();
				companyId = cuser.getCompanyId();
			}
			if (CommonUtil.isEmpty(contextId) || CommonUtil.isEmpty(spaceId) || CommonUtil.isEmpty(companyId))
				return null;
			
			Date tempFromDate = null;
			Date tempToDate = null;
			if (fromDate != null) {
				tempFromDate = new Date();
				tempFromDate.setTime(fromDate.getLocalTime());
				tempFromDate.setTime(tempFromDate.getTime() - TimeZone.getDefault().getRawOffset());
			}
			if (toDate != null) {
				tempToDate = new Date();
				tempToDate.setTime(toDate.getLocalTime());
				tempToDate.setTime(tempToDate.getTime() - TimeZone.getDefault().getRawOffset());
			}
			TaskWork[] tasks = getTaskWorkByFromToDate(contextId, spaceId, tempFromDate, tempToDate, maxSize);

			return (TaskInstanceInfo[])ModelConverter.getTaskInstanceInfoArrayByTaskWorkArray(userId, tasks);
			
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}
	
	@Override
	public InstanceInfo[] getSpaceInstancesByDate(String spaceId, LocalDate fromDate, int maxSize) throws Exception {
		try{
			User cuser = SmartUtil.getCurrentUser();
			String userId = null;
			if (cuser != null)
				userId = cuser.getId();
			
			TaskWorkCond cond = new TaskWorkCond();
			cond.setTskWorkSpaceId(spaceId);
			cond.setTskStatus(TskTask.TASKSTATUS_COMPLETE);
			cond.setOrders(new Order[]{new Order("taskLastModifyDate", false)});
			cond.setPageSize(maxSize);
			cond.setTskExecuteDateTo(new LocalDate(fromDate.getGMTDate()));
			TaskWork[] tasks = getWorkListManager().getTaskWorkList(userId, cond);
			List<InstanceInfo> instanceInfoList = new ArrayList<InstanceInfo>();
			List<String> prcInstIdList = new ArrayList<String>();
			if(!CommonUtil.isEmpty(tasks)) {
				for (int i = 0; i < tasks.length; i++) {
					TaskWork task = tasks[i];
					if (instanceInfoList.size() == 10)
						break;
					if (prcInstIdList.contains(task.getTskPrcInstId()))
						continue;
					prcInstIdList.add(task.getTskPrcInstId());
					instanceInfoList.add(ModelConverter.getWorkInstanceInfoByTaskWork(task));
				}
			}

			InstanceInfo[] spaceInstances = null;

			OpinionCond opinionCond = new OpinionCond();
			opinionCond.setRefId(spaceId);
			opinionCond.setPageSize(maxSize);
			Opinion[] opinions = getOpinionManager().getOpinions(userId, opinionCond, IManager.LEVEL_ALL);
			if(!CommonUtil.isEmpty(opinions)) {
				int opinionLength = opinions.length;
				for(int i=0; i<opinionLength; i++) {
					Opinion opinion = opinions[i];
					CommentInstanceInfo commentInstanceInfo = new CommentInstanceInfo();
					String modificationUser = opinion.getModificationUser() == null ? opinion.getCreationUser() : opinion.getModificationUser();
					Date modificationDate = opinion.getModificationDate() == null ? opinion.getCreationDate() : opinion.getModificationDate();
					commentInstanceInfo.setId(opinion.getObjId());
					commentInstanceInfo.setCommentType(CommentInstance.COMMENT_TYPE_ON_WORK_MANUAL);
					commentInstanceInfo.setComment(opinion.getOpinion());
					commentInstanceInfo.setCommentor(ModelConverter.getUserInfoByUserId(opinion.getCreationUser()));
					commentInstanceInfo.setLastModifiedDate(new LocalDate(modificationDate.getTime()));
					commentInstanceInfo.setType(Instance.TYPE_COMMENT);
					commentInstanceInfo.setOwner(ModelConverter.getUserInfoByUserId(opinion.getCreationUser()));
					commentInstanceInfo.setCreatedDate(new LocalDate(opinion.getCreationDate().getTime()));
					commentInstanceInfo.setLastModifier(ModelConverter.getUserInfoByUserId(modificationUser));
					instanceInfoList.add(commentInstanceInfo);
				}
			}
			if(instanceInfoList.size() > 0) {
				Collections.sort(instanceInfoList, Collections.reverseOrder());
				spaceInstances = new InstanceInfo[instanceInfoList.size()];
				instanceInfoList.toArray(spaceInstances);
			}

			if(!CommonUtil.isEmpty(spaceInstances)) {
				if(spaceInstances.length > maxSize) {
					List<InstanceInfo> resultInstanceInfoList = new ArrayList<InstanceInfo>();
					for(int i=0; i<maxSize; i++) {
						InstanceInfo instanceInfo = spaceInstances[i];
						resultInstanceInfoList.add(instanceInfo);
					}
					spaceInstances = new InstanceInfo[resultInstanceInfoList.size()];
					resultInstanceInfoList.toArray(spaceInstances);
				}
			}

			return spaceInstances;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			throw e;			
			// Exception Handling Required			
		}
	}
	
	private void addSubDepartmentUsers(String user, String parentDeptId, List<String> userList) throws Exception {

		SwoDepartmentCond deptCond = new SwoDepartmentCond();
		deptCond.setParentId(parentDeptId);
		SwoDepartment[] subDeptObjs = getSwoManager().getDepartments(user, deptCond, IManager.LEVEL_LITE);
		if (subDeptObjs == null)
			return;
		for (int i = 0; i < subDeptObjs.length; i++) {
			SwoDepartment subDeptObj = subDeptObjs[i];
			SwoUserCond userCond = new SwoUserCond();
			userCond.setDeptId(subDeptObj.getId());
			SwoUser[] teamUsers = getSwoManager().getUsers(user, userCond, IManager.LEVEL_LITE);
			if (teamUsers != null) {
				for (int j = 0; j < teamUsers.length; j++) {
					SwoUser teamUser = teamUsers[j];
					String teamUserId = teamUser.getId();
					
					if (!userList.contains(teamUserId)); {
						userList.add(teamUserId);
					}
				}
			}
			//재귀호출
			addSubDepartmentUsers(user, subDeptObj.getId(), userList);
		}	
	}
	
	@Override
	public TaskInstanceInfo[] getCastTaskInstancesByDate(LocalDate fromDate, int maxSize) throws Exception {
		try{
			User cuser = SmartUtil.getCurrentUser();
			String userId = null;
			String departmentId = null;
			if (cuser != null) {
				userId = cuser.getId();
				departmentId = cuser.getDepartmentId();	
			}	
			List<String> relatedUserIdArray = new ArrayList<String>();
			relatedUserIdArray.add(userId);
			
			if (departmentId != null) {

				SwoUserCond userCond = new SwoUserCond();
				userCond.setDeptId(departmentId);
				userCond.setRoleId("DEPT MEMBER");//모든 부서원들을 가져온다, 내 아디디는 무조건 포함되기 때문에 내가 부서장이면 나머지 부서원, 부서원이면 나머지 부서원을 가져온다
				
				SwoUser[] relatedUserObjs = getSwoManager().getUsers(userId, userCond, IManager.LEVEL_LITE);
				if (relatedUserObjs != null) {
					for (int i = 0; i < relatedUserObjs.length; i++) {
						SwoUser relatedUserObj = relatedUserObjs[i];
						if (!relatedUserObj.getId().equalsIgnoreCase(userId))
							relatedUserIdArray.add(relatedUserObj.getId());//자기 부서원들을 array에 포함시킨다
					}
				}
				//자기 하위부서의 사람들도 포함시킨다(재귀함수를 이용)
				addSubDepartmentUsers(userId, departmentId, relatedUserIdArray);//userDeptId의 자식 부서들의 사용자들을 array에 추가시킨다
			}
			StringBuffer userSelectStr = new StringBuffer();
			boolean isFirst = true;
			for (int i = 0; i < relatedUserIdArray.size(); i++) {
				if (isFirst) {
					userSelectStr.append("'").append(relatedUserIdArray.get(i)).append("'");
					isFirst = false;
				} else {
					userSelectStr.append(",'").append(relatedUserIdArray.get(i)).append("'");
				}
			}
			
			TaskWorkCond cond = new TaskWorkCond();
			cond.setTskAssigneeIdIns(userSelectStr.toString());
			cond.setTskAssignee(userId);
			cond.setTskModifyDateFrom(fromDate);
			long totalSize = getWorkListManager().getCastWorkListSize(userId, cond);

			cond.setPageNo(0);
			cond.setPageSize(maxSize);

			cond.setOrders(new Order[]{new Order("tskcreatedate", false)});

			TaskWork[] tasks = getWorkListManager().getCastWorkList(userId, cond);
			TaskInstanceInfo[] taskInfos = null;
			if(!CommonUtil.isEmpty(tasks))
				taskInfos = ModelConverter.getTaskInstanceInfoArrayByTaskWorkArray(userId, tasks);

			if(!CommonUtil.isEmpty(taskInfos)) {
				if (totalSize > maxSize) {
					TaskInstanceInfo[] tempTaskInfos = new TaskInstanceInfo[taskInfos.length + 1];
					for (int i = 0; i < taskInfos.length + 1; i++) {
						if (i == taskInfos.length) {
							TaskInstanceInfo moreInstance = new TaskInstanceInfo();
							moreInstance.setType(-21);
							tempTaskInfos[i] = moreInstance;
						} else {
							tempTaskInfos[i] = taskInfos[i];
						}
					}
					taskInfos = tempTaskInfos;
				}
			}

			return taskInfos;
			//return ModelConverter.getTaskInstanceInfoArrayByTaskWorkArray(userId, tasks);
			//return SmartTest.getTaskInstancesByDate(null, null, null, null, maxSize);
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}

	@Override
	public TaskInstanceInfo[] getInstanceTaskHistoriesById(String instId) throws Exception {

		try{
			User user = SmartUtil.getCurrentUser();
			TskTaskCond tskCond = new TskTaskCond();
			tskCond.setExtendedProperties(new Property[] {new Property("recordId", instId)});
			TskTask[] tskTasks = getTskManager().getTasks(user.getId(), tskCond, IManager.LEVEL_LITE);
			TaskInstanceInfo[] taskInstanceInfos = null;
			if(tskTasks != null) {
				taskInstanceInfos = new TaskInstanceInfo[tskTasks.length];
				IWInstanceInfo iWInstanceInfo = new IWInstanceInfo();
				String processInstId = tskTasks[0].getProcessInstId();

				TskTask lastSwTask = ModelConverter.getLastTskTaskByInstanceId(processInstId);

				TaskInstanceInfo lastTask = ModelConverter.getTaskInstanceInfoByTskTask(iWInstanceInfo, null, lastSwTask);
				iWInstanceInfo.setLastTask(lastTask);

				for(int i=0; i<tskTasks.length; i++) {
					TaskInstanceInfo taskInstanceInfo = ModelConverter.getTaskInstanceInfoByTskTask(iWInstanceInfo, null, tskTasks[i]);
					taskInstanceInfos[i] = taskInstanceInfo;
				}
			}
			return taskInstanceInfos;
		}catch (Exception e){
			return null;
		}

	}

	@Override
	public InstanceInfoList[] getInstanceRelatedWorksById(String instId) throws Exception {

		try{
			User user = SmartUtil.getCurrentUser();
			Map<String, Integer> refFormMap = getSwfManager().getReferenceFormIdSizeMap(user.getId(), instId);

			InstanceInfoList[] instanceInfoLists = new InstanceInfoList[refFormMap.size()];

			Iterator iterator = refFormMap.entrySet().iterator();
			int count = 0;
			while(iterator.hasNext()) {
				InstanceInfoList instanceInfoList = new InstanceInfoList();
				Entry entry = (Entry)iterator.next();
				String myFormId = CommonUtil.toNotNull(entry.getKey());

				SwfFormCond swfFormCond = new SwfFormCond();
				swfFormCond.setCompanyId(user.getCompanyId());
				swfFormCond.setId(myFormId);
				SwfForm[] swfForms = getSwfManager().getForms(user.getId(), swfFormCond, IManager.LEVEL_ALL);
				if(swfForms == null)
					return null;
				SwfField[] swfFields = swfForms[0].getFields();

				SwdRecordExtend[] swdRecordExtends = getSwdManager().getCtgPkg(swfForms[0].getPackageId());

				SwdRecordCond swdRecordCond = new SwdRecordCond();
				swdRecordCond.setFormId(myFormId);
				swdRecordCond.setReferencedRecordId(instId);

				swdRecordCond.setOrders(new Order[]{new Order(FormField.ID_CREATED_DATE, false)});

				SwdRecord[] swdRecords = getSwdManager().getRecords(user.getId(), swdRecordCond, IManager.LEVEL_LITE);

				SwdDomainCond swdDomainCond = new SwdDomainCond();
				swdDomainCond.setCompanyId(user.getCompanyId());
				swdDomainCond.setFormId(myFormId);

				SwdDomain swdDomain = getSwdManager().getDomain(user.getId(), swdDomainCond, IManager.LEVEL_LITE);

				String formId = swdDomain.getFormId();
				String formName = swdDomain.getFormName();
				String titleFieldId = swdDomain.getTitleFieldId();

				if(swdRecords != null) {
					IWInstanceInfo[] iWInstanceInfos = new IWInstanceInfo[swdRecords.length];
		
					for(int i = 0; i < swdRecords.length; i++) {
						IWInstanceInfo iWInstanceInfo = new IWInstanceInfo();
						SwdRecord swdRecord = swdRecords[i];
						iWInstanceInfo.setId(swdRecord.getRecordId());
						iWInstanceInfo.setOwner(ModelConverter.getUserInfoByUserId(swdRecord.getCreationUser()));
						int type = WorkInstance.TYPE_INFORMATION;
						iWInstanceInfo.setType(type);
						iWInstanceInfo.setStatus(WorkInstance.STATUS_COMPLETED);
						iWInstanceInfo.setWorkSpace(null);
			
						WorkCategoryInfo groupInfo = null;
						if (!CommonUtil.isEmpty(swdRecordExtends[0].getSubCtgId()))
							groupInfo = new WorkCategoryInfo(swdRecordExtends[0].getSubCtgId(), swdRecordExtends[0].getSubCtg());
			
						WorkCategoryInfo categoryInfo = new WorkCategoryInfo(swdRecordExtends[0].getParentCtgId(), swdRecordExtends[0].getParentCtg());
			
						WorkInfo workInfo = new SmartWorkInfo(formId, formName, SmartWork.TYPE_INFORMATION, groupInfo, categoryInfo);
		
						iWInstanceInfo.setWork(workInfo);
						iWInstanceInfo.setLastModifier(ModelConverter.getUserInfoByUserId(swdRecord.getModificationUser()));
						iWInstanceInfo.setLastModifiedDate(new LocalDate((swdRecord.getModificationDate()).getTime()));
		
						SwdDataField[] swdDataFields = swdRecord.getDataFields();
						List<FieldData> fieldDataList = new ArrayList<FieldData>();
			
						for(SwdDataField swdDataField : swdDataFields) {
							if(swdDataField.getId().equals(titleFieldId))
								iWInstanceInfo.setSubject(swdDataField.getValue());
							for(SwfField swfField : swfFields) {
								String formatType = swfField.getFormat().getType();
								if(swdDataField.getDisplayOrder() > -1 && !formatType.equals("richEditor") && !formatType.equals("imageBox") && !formatType.equals("dataGrid")) {
									if(swdDataField.getId().equals(swfField.getId())) {
										FieldData fieldData = new FieldData();
										fieldData.setFieldId(swdDataField.getId());
										fieldData.setFieldType(formatType);
										String value = swdDataField.getValue();
										if(formatType.equals(FormField.TYPE_USER)) {
											if(value != null) {
												String[] users = value.split(";");
												String resultUser = "";
												if(users.length > 0 && users.length < 4) {
													for(int j=0; j<users.length; j++) {
														resultUser += users[j] + ", ";
													}
													resultUser = resultUser.substring(0, resultUser.length()-2);
												} else if(users.length > 0 && users.length > 3) {
													for(int j=0; j<3; j++) {
														resultUser += users[j] + ", ";
													}
													resultUser = resultUser.substring(0, resultUser.length()-2);
													resultUser = resultUser + " " + SmartMessage.getString("content.sentence.with_other_users", (new Object[]{(users.length - 3)}));
												}
												value = resultUser;
											}
										} else if(formatType.equals(FormField.TYPE_CURRENCY)) {
											String symbol = swfField.getFormat().getCurrency();
											fieldData.setSymbol(symbol);
										} else if(formatType.equals(FormField.TYPE_PERCENT)) {
											// TO-DO
										} else if(formatType.equals(FormField.TYPE_DATE)) {
											if(value != null)
												value = LocalDate.convertGMTStringToLocalDate(value).toLocalDateSimpleString();
										} else if(formatType.equals(FormField.TYPE_TIME)) {
											if(value != null)
												value = LocalDate.convertGMTStringToLocalDate(value).toLocalTimeSimpleString();
										} else if(formatType.equals(FormField.TYPE_DATETIME)) {
											if(value != null)
												value = LocalDate.convertGMTStringToLocalDate(value).toLocalDateTimeSimpleString();
										} else if(formatType.equals(FormField.TYPE_FILE)) { 
											List<IFileModel> fileModelList = getDocManager().findFileGroup(value);
											List<Map<String, String>> fileList = new ArrayList<Map<String,String>>();
											int fileModelListLength = fileModelList.size();
											for(int j=0; j<fileModelListLength; j++) {
												Map<String, String> fileMap = new LinkedHashMap<String, String>();
												IFileModel fileModel = fileModelList.get(j);
												String fileId = fileModel.getId();
												String fileName = fileModel.getFileName();
												String fileType = fileModel.getType();
												String fileSize = fileModel.getFileSize() + "";
												fileMap.put("fileId", fileId);
												fileMap.put("fileName", fileName);
												fileMap.put("fileType", fileType);
												fileMap.put("fileSize", fileSize);
												fileList.add(fileMap);
											}
											if(fileList.size() > 0)
												fieldData.setFiles(fileList);
										}
										fieldData.setValue(value);
										fieldDataList.add(fieldData);
									}
								}
							}
						}
						FieldData[] fieldDatas = new FieldData[fieldDataList.size()];
						fieldDataList.toArray(fieldDatas);
						iWInstanceInfo.setDisplayDatas(fieldDatas);
						iWInstanceInfos[i] = iWInstanceInfo;
					}
					instanceInfoList.setInstanceDatas(iWInstanceInfos);
				}

				//instanceInfoList.setSortedField(sortingField);
				instanceInfoList.setType(InstanceInfoList.TYPE_INFORMATION_INSTANCE_LIST);
				//instanceInfoList.setPageSize(pageSize);
				//instanceInfoList.setTotalPages(totalPages);
				//instanceInfoList.setCurrentPage(currentPage);
				instanceInfoLists[count] = instanceInfoList;
				count++;
			}
			return instanceInfoLists;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}
	private String executeTask(Map<String, Object> requestBody, HttpServletRequest request, String action) throws Exception {
		User cuser = SmartUtil.getCurrentUser();
		String userId = null;
		if (cuser != null)
			userId = cuser.getId();
		
		if (action == null || action.equalsIgnoreCase("EXECUTE") || action.equalsIgnoreCase("RETURN") || action.equalsIgnoreCase("SAVE")) {
			
			/*{
			workId=pkg_cf3b0087995f4f99a41c93e2fe95b22d, 
			instanceId=402880eb35426e06013542712d7c0002, 
			taskInstId=402880eb35426e060135427135d20004, 
			formId=frm_5aeb1a53f9cf439dbe83693be9e27624, 
			formName=승인자 결재, 
			frmSmartForm={
				8=승인자 의견, 
				12={
					users=[
						{
							id=kmyu@maninsoft.co.kr, 
							name=연구소장 유광민
						}
					]
				}, 
				16={users=[{id=kmyu@maninsoft.co.kr, name=연구소장 유광민}]}, 
				76={users=[{id=kmyu@maninsoft.co.kr, name=연구소장 유광민}]}
			}
			}*/
		

			if (logger.isInfoEnabled()) {
				logger.info(action + " Task Start [processInstanceId : " + (String)requestBody.get("instanceId") + ", " + (String)requestBody.get("formName") + "( taskId : " + (String)requestBody.get("taskInstId") + ") ] by " + userId);
			}
			//태스크인스턴스 아이디를 이용하여 저장 되어 있는 태스크를 조회 하고 실행 가능 여부를 판단한다
			String taskInstId = (String)requestBody.get("taskInstId");
			if (CommonUtil.isEmpty(taskInstId))
				throw new Exception(action +" TaskId Is Null");
			TskTask task = getTskManager().getTask(userId, taskInstId, IManager.LEVEL_ALL);
			if (task == null)
				throw new Exception("Not Exist Task : taskId = " + taskInstId);
			if (!task.getStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ASSIGN))
				throw new Exception("Task Is Not Executable(" + action + ") Status : taskId = " + taskInstId +" (status - " + task.getStatus() + ")");
			if (!task.getAssignee().equalsIgnoreCase(userId)) 
				throw new Exception("Task is Not Executable(" + action + ") Assignee : taskId = " + taskInstId + " (assignee - " + task.getAssignee() + " But performer - " + userId + ")");
			
			//태스크에 사용자가 입력한 업무 데이터를 셋팅한다
			String formId = (String)requestBody.get("formId");
			SwfForm form = getSwfManager().getForm(userId, formId);
			SwfField[] formFields = form.getFields();
			List domainFieldList = new ArrayList();
			
			for (SwfField field: formFields) {
				SwdField domainField = new SwdField();
				domainField.setFormFieldId(field.getId());
				domainField.setFormFieldName(field.getName());
				domainField.setFormFieldType(field.getSystemType());
				domainField.setArray(field.isArray());
				domainField.setSystemField(field.isSystem());
				domainFieldList.add(domainField);
			}
			SwdField[] domainFields = new SwdField[domainFieldList.size()];
			domainFieldList.toArray(domainFields);
			
			SwdRecord recordObj = getSwdRecordByRequestBody(userId, domainFields, requestBody, request);
			String taskDocument = null;
			String groupId = null;
			Map<String, List<Map<String, String>>> fileGroupMap = null;
			if(recordObj != null) {
				taskDocument = recordObj.toString();
				groupId = recordObj.getFileGroupId();
				fileGroupMap = recordObj.getFileGroupMap();
			}
			task.setDocument(taskDocument);
			if (logger.isInfoEnabled()) {
				logger.info(action + " Task [processInstanceId : " + (String)requestBody.get("instanceId") + ", " + (String)requestBody.get("formName") + "( taskId : " + (String)requestBody.get("taskInstId") + "), document : " + recordObj.toString() + " ] ");
			}
			//태스크의 실제 완료 시간을 입력한다
			if (task.getRealStartDate() == null)
				task.setRealStartDate(new LocalDate(new Date().getTime()));
			task.setRealEndDate(new LocalDate(new Date().getTime()));
			//태스크를 실행한다

			Map<String, Object> frmAccessSpaceMap = (Map<String, Object>)requestBody.get("frmAccessSpace");
			if(!CommonUtil.isEmpty(frmAccessSpaceMap)) {
				Set<String> keySet = frmAccessSpaceMap.keySet();
				Iterator<String> itr = keySet.iterator();
				List<Map<String, String>> users = null;
				String workSpaceId = null;
				String workSpaceType = null;
				String accessLevel = null;
				String accessValue = null;

				while (itr.hasNext()) {
					String fieldId = (String)itr.next();
					Object fieldValue = frmAccessSpaceMap.get(fieldId);
					if (fieldValue instanceof LinkedHashMap) {
						Map<String, Object> valueMap = (Map<String, Object>)fieldValue;
						users = (ArrayList<Map<String,String>>)valueMap.get("users");
						if(!CommonUtil.isEmpty(users)) {
							String symbol = ";";
							if(users.size() == 1) {
								accessValue = users.get(0).get("id");
							} else {
								accessValue = "";
								for(int i=0; i < users.subList(0, users.size()).size(); i++) {
									Map<String, String> user = users.get(i);
									accessValue += user.get("id") + symbol;
								}
							}
						}
					} else if(fieldValue instanceof String) {
						if(fieldId.equals("selWorkSpace")) {
							workSpaceId = (String)fieldValue;
						} else if(fieldId.equals("selWorkSpaceType")) {
							workSpaceType = (String)fieldValue;
						} else if(fieldId.equals("selAccessLevel")) {
							accessLevel = (String)fieldValue;
						}
					}
				}

				task.setWorkSpaceId(workSpaceId);
				task.setWorkSpaceType(workSpaceType);
				task.setAccessLevel(accessLevel);
				task.setAccessValue(accessValue);
			}

			if (action.equalsIgnoreCase("save")) {
				getTskManager().setTask(userId, task, IManager.LEVEL_ALL);
			} else {
				getTskManager().executeTask(userId, task, action);
				SmartUtil.removeNoticeByExecutedTaskId(task.getAssignee(), task.getObjId());
			}
			if (logger.isInfoEnabled()) {
				logger.info(action + " Task Done [processInstanceId : " + (String)requestBody.get("instanceId") + ", " + (String)requestBody.get("formName") + "( taskId : " + (String)requestBody.get("taskInstId") + ")] ");
			}

			if(fileGroupMap.size() > 0) {
				for(Map.Entry<String, List<Map<String, String>>> entry : fileGroupMap.entrySet()) {
					String fileGroupId = entry.getKey();
					List<Map<String, String>> fileGroups = entry.getValue();

					try {
						for(int i=0; i < fileGroups.subList(0, fileGroups.size()).size(); i++) {
							Map<String, String> file = fileGroups.get(i);
							String fileId = file.get("fileId");
							String fileName = file.get("fileName");
							String fileSize = file.get("fileSize");
							getDocManager().insertFiles("Files", taskInstId, fileGroupId, fileId, fileName, fileSize);
						}
					} catch (Exception e) {
						throw new DocFileException("file upload fail...");
					}
				}
			}
			if(groupId != null) {
				List<IFileModel> iFileModelList = getDocManager().findFileGroup(groupId);
				if(iFileModelList.size() > 0) {
					for(int i=0; i<iFileModelList.size(); i++) {
						IFileModel fileModel = iFileModelList.get(i);
						String fileId = fileModel.getId();
						String filePath = fileModel.getFilePath();
						if(fileModel.isDeleteAction()) {
							getDocManager().deleteFile(fileId);
							File f = new File(filePath);
							if(f.exists())
								f.delete();
						}
					}
				}
			}

			return taskInstId;
		}  else if (action.equalsIgnoreCase("delegate")) {
			String taskInstId = "";
			String delegateUserId = "";
			if (CommonUtil.isEmpty(taskInstId) || CommonUtil.isEmpty(delegateUserId))
				return null;
			
			TskTask task = getTskManager().getTask(userId, taskInstId, IManager.LEVEL_ALL);
			
			if (task == null || !task.getAssignee().equalsIgnoreCase(userId))
				throw new Exception("Task("+taskInstId+") Is Null Or Mismatch Between AssigneeId("+task.getAssignee()+") And PerformerId("+userId+")");
			
			task.setAssignee(delegateUserId);
			
			if (logger.isInfoEnabled())
				logger.info("Delegate Task "+ task.getName() +"("+taskInstId+") From " + userId + " To " + delegateUserId);

			Map<String, Object> frmAccessSpaceMap = (Map<String, Object>)requestBody.get("frmAccessSpace");
			if(!CommonUtil.isEmpty(frmAccessSpaceMap)) {
				Set<String> keySet = frmAccessSpaceMap.keySet();
				Iterator<String> itr = keySet.iterator();
				List<Map<String, String>> users = null;
				String workSpaceId = null;
				String workSpaceType = null;
				String accessLevel = null;
				String accessValue = null;

				while (itr.hasNext()) {
					String fieldId = (String)itr.next();
					Object fieldValue = frmAccessSpaceMap.get(fieldId);
					if (fieldValue instanceof LinkedHashMap) {
						Map<String, Object> valueMap = (Map<String, Object>)fieldValue;
						users = (ArrayList<Map<String,String>>)valueMap.get("users");
						if(!CommonUtil.isEmpty(users)) {
							String symbol = ";";
							if(users.size() == 1) {
								accessValue = users.get(0).get("id");
							} else {
								accessValue = "";
								for(int i=0; i < users.subList(0, users.size()).size(); i++) {
									Map<String, String> user = users.get(i);
									accessValue += user.get("id") + symbol;
								}
							}
						}
					} else if(fieldValue instanceof String) {
						if(fieldId.equals("selWorkSpace")) {
							workSpaceId = (String)fieldValue;
						} else if(fieldId.equals("selWorkSpaceType")) {
							workSpaceType = (String)fieldValue;
						} else if(fieldId.equals("selAccessLevel")) {
							accessLevel = (String)fieldValue;
						}
					}
				}

				task.setWorkSpaceId(workSpaceId);
				task.setWorkSpaceType(workSpaceType);
				task.setAccessLevel(accessLevel);
				task.setAccessValue(accessValue);
			}

			getTskManager().setTask(userId, task, IManager.LEVEL_ALL);
			return taskInstId;
		}
		return null;
	}

	@Override
	public String performTaskInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		return executeTask(requestBody, request, "execute");
// execute, return, delegate, save 함수를 한곳(executeTask(Map<String, Object> requestBody, HttpServletRequest request, String action))으로 모으기전 execute 소스 - 테스트 진행 후 한곳으로 합쳐지는게 불가능하다면 이전으로 돌리기 위해 주석 처리함
//		/*{
//			workId=pkg_cf3b0087995f4f99a41c93e2fe95b22d, 
//			instanceId=402880eb35426e06013542712d7c0002, 
//			taskInstId=402880eb35426e060135427135d20004, 
//			formId=frm_5aeb1a53f9cf439dbe83693be9e27624, 
//			formName=승인자 결재, 
//			frmSmartForm={
//				8=승인자 의견, 
//				12={
//					users=[
//						{
//							id=kmyu@maninsoft.co.kr, 
//							name=연구소장 유광민
//						}
//					]
//				}, 
//				16={users=[{id=kmyu@maninsoft.co.kr, name=연구소장 유광민}]}, 
//				76={users=[{id=kmyu@maninsoft.co.kr, name=연구소장 유광민}]}
//			}
//		}*/
//		
//		try {
//			
//			User cuser = SmartUtil.getCurrentUser();
//			String userId = null;
//			if (cuser != null)
//				userId = cuser.getId();
//
//			if (logger.isInfoEnabled()) {
//				logger.info("ExecuteTask Task Start [processInstanceId : " + (String)requestBody.get("instanceId") + ", " + (String)requestBody.get("formName") + "( taskId : " + (String)requestBody.get("taskInstId") + ") ] by " + userId);
//			}
//			//태스크인스턴스 아이디를 이용하여 저장 되어 있는 태스크를 조회 하고 실행 가능 여부를 판단한다
//			String taskInstId = (String)requestBody.get("taskInstId");
//			if (CommonUtil.isEmpty(taskInstId))
//				throw new Exception("ExecuteTaskId Is Null");
//			TskTask task = getTskManager().getTask(userId, taskInstId, IManager.LEVEL_ALL);
//			if (task == null)
//				throw new Exception("Not Exist Task Object(data) : taskId = " + taskInstId);
//			if (!task.getStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ASSIGN))
//				throw new Exception("Task Is Not Executable Status : taskId = " + taskInstId +" (status - " + task.getStatus() + ")");
//			if (!task.getAssignee().equalsIgnoreCase(userId)) 
//				throw new Exception("Task is Not Executable Assignee : taskId = " + taskInstId + " (assignee - " + task.getAssignee() + " But performer - " + userId + ")");
//			
//			//태스크에 사용자가 입력한 업무 데이터를 셋팅한다
//			String formId = (String)requestBody.get("formId");
//			SwfForm form = getSwfManager().getForm(userId, formId);
//			SwfField[] formFields = form.getFields();
//			List domainFieldList = new ArrayList();
//			
//			for (SwfField field: formFields) {
//				SwdField domainField = new SwdField();
//				domainField.setFormFieldId(field.getId());
//				domainField.setFormFieldName(field.getName());
//				domainField.setFormFieldType(field.getSystemType());
//				domainField.setArray(field.isArray());
//				domainField.setSystemField(field.isSystem());
//				domainFieldList.add(domainField);
//			}
//			SwdField[] domainFields = new SwdField[domainFieldList.size()];
//			domainFieldList.toArray(domainFields);
//			
//			SwdRecord recordObj = getSwdRecordByRequestBody(userId, domainFields, requestBody, request);
//			String taskDocument = null;
//			if (recordObj != null)
//				taskDocument = recordObj.toString();
//			task.setDocument(taskDocument);
//			if (logger.isInfoEnabled()) {
//				logger.info("ExecuteTask Task [processInstanceId : " + (String)requestBody.get("instanceId") + ", " + (String)requestBody.get("formName") + "( taskId : " + (String)requestBody.get("taskInstId") + "), document : " + recordObj.toString() + " ] ");
//			}
//			//태스크의 실제 완료 시간을 입력한다
//			if (task.getRealStartDate() == null)
//				task.setRealStartDate(new LocalDate(new Date().getTime()));
//			task.setRealEndDate(new LocalDate(new Date().getTime()));
//			//태스크를 실행한다
//			TskTask executedTask = getTskManager().executeTask(userId, task, "execute");
//			String prcInstId = executedTask.getProcessInstId();
//			if (logger.isInfoEnabled()) {
//				logger.info("ExecuteTask Task Done [processInstanceId : " + (String)requestBody.get("instanceId") + ", " + (String)requestBody.get("formName") + "( taskId : " + (String)requestBody.get("taskInstId") + ")] ");
//			}
//			return prcInstId;
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		}
	}
	@Override
	public String returnTaskInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		return executeTask(requestBody, request, "return");
	}
	@Override
	public String reassignTaskInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		return executeTask(requestBody, request, "delegate");
	}
	@Override
	public String tempSaveTaskInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		return executeTask(requestBody, request, "save");
	}
	@Override
	public void addLikeToInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		
		/*{
				workType=21, 
				workInstanceId=dr_402880e336aa7f640136aa80552d0002
		}*/
		Integer workType = (Integer)requestBody.get("workType");
		String workInstanceId = (String)requestBody.get("workInstanceId");

		User cuser = SmartUtil.getCurrentUser();
		String userId = null;
		if (cuser != null)
			userId = cuser.getId();
		
		if (CommonUtil.isEmpty(userId))
			return;

		Like like = new Like();

		like.setRefType(workType);
		like.setRefId(workInstanceId);
		like.setCreationUser(userId);
		like.setCreationDate(new LocalDate());
		
		getLikeManager().createLike(userId, like);
		
	}
	@Override
	public void removeLikeFromInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		/*{
				workType=21, 
				workInstanceId=dr_402880e336aa7f640136aa80552d0002
		}*/
		Integer workType = (Integer)requestBody.get("workType");
		String workInstanceId = (String)requestBody.get("workInstanceId");
		
		User cuser = SmartUtil.getCurrentUser();
		String userId = null;
		if (cuser != null)
			userId = cuser.getId();

		LikeCond cond = new LikeCond();
		cond.setRefType(workType);
		cond.setRefId(workInstanceId);
		cond.setCreationUser(userId);
		Like[] likes = getLikeManager().getLikes(userId, cond, IManager.LEVEL_ALL);
		
		if (likes == null)
			return;
		for (int i = 0; i < likes.length; i++) {
			Like like = likes[i];
			getLikeManager().removeLike(userId, like.getObjId());
		}
	}

	@Override
	public AsyncMessageList getMyMessageInstancesByType(int type, int maxSize) throws Exception {

		AsyncMessageList asyncMessageList = new AsyncMessageList();
		getMyMessageInstancesByType(asyncMessageList, type, null, maxSize);

		return asyncMessageList;
	}

	@Override
	public AsyncMessageInstanceInfo[] getMyMessageInstancesByType(AsyncMessageList asyncMessageList, int type, LocalDate fromDate, int maxSize) throws Exception {
		try {
			User user = SmartUtil.getCurrentUser();
			String userId = user.getId();
			MessageCond messageCond = new MessageCond();
			if(fromDate != null)
				messageCond.setCreationDateTo(fromDate);
			if(type == Instance.TYPE_ASYNC_MESSAGE) {
				messageCond.setTargetUser(userId);
			} else if(type == Instance.TYPE_SENT_ASYNC_MESSAGE) {
				messageCond.setSendUser(userId);
			}

			long totalSize = getMessageManager().getMessageSize(userId, messageCond);
			if(asyncMessageList != null)
				asyncMessageList.setTotalSize((int)totalSize);

			messageCond.setPageNo(0);
			messageCond.setPageSize(maxSize);
			messageCond.setOrders(new Order[]{new Order(MessageCond.A_MODIFICATIONDATE, false)});

			Message[] messages = getMessageManager().getMessages(userId, messageCond, IManager.LEVEL_ALL);

			if(CommonUtil.isEmpty(messages))
				return null;

			List<AsyncMessageInstanceInfo> asyncMessageInstanceInfoList = new ArrayList<AsyncMessageInstanceInfo>();
			AsyncMessageInstanceInfo[] asyncMessageInstanceInfos = null;
			if(!CommonUtil.isEmpty(messages)) {
				for(Message message : messages) {
					String deleteUser = message.getDeleteUser();
					if(deleteUser != null && deleteUser.equals(userId))
						continue;
					AsyncMessageInstanceInfo asyncMessageInstanceInfo = new AsyncMessageInstanceInfo();
					asyncMessageInstanceInfo.setSender(ModelConverter.getUserInfoByUserId(message.getSendUser()));
					asyncMessageInstanceInfo.setReceiver(ModelConverter.getUserInfoByUserId(message.getTargetUser()));
					asyncMessageInstanceInfo.setChatters(null);
					asyncMessageInstanceInfo.setSendDate(new LocalDate(message.getCreationDate().getTime()));
					boolean isChecked = message.getIsChecked();
					if(isChecked) asyncMessageInstanceInfo.setMsgStatus(AsyncMessageInstance.MESSAGE_STATUS_READ);
					else asyncMessageInstanceInfo.setMsgStatus(AsyncMessageInstance.MESSAGE_STATUS_UNREAD);
					asyncMessageInstanceInfo.setBriefMessage(StringUtil.subString(message.getContent(), 0, 120, "..."));
					asyncMessageInstanceInfo.setMessage(message.getContent());
					asyncMessageInstanceInfo.setId(message.getObjId());
					asyncMessageInstanceInfo.setType(Instance.TYPE_ASYNC_MESSAGE);
					asyncMessageInstanceInfo.setOwner(ModelConverter.getUserInfoByUserId(message.getCreationUser()));
					asyncMessageInstanceInfo.setCreatedDate(new LocalDate(message.getCreationDate().getTime()));
					asyncMessageInstanceInfo.setLastModifier(ModelConverter.getUserInfoByUserId(message.getModificationUser()));
					asyncMessageInstanceInfo.setLastModifiedDate(new LocalDate(message.getModificationDate().getTime()));
					asyncMessageInstanceInfoList.add(asyncMessageInstanceInfo);
				}
			}
			if(asyncMessageInstanceInfoList.size() > 0) {
				if(totalSize > maxSize)
					asyncMessageInstanceInfoList.add(new AsyncMessageInstanceInfo());
				asyncMessageInstanceInfos = new AsyncMessageInstanceInfo[asyncMessageInstanceInfoList.size()];
				asyncMessageInstanceInfoList.toArray(asyncMessageInstanceInfos);
			}
			if(asyncMessageList != null)
				asyncMessageList.setMessages(asyncMessageInstanceInfos);

			return asyncMessageInstanceInfos;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void createAsyncMessage(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		try {
			String senderId = (String)requestBody.get("senderId");
			String message = (String)requestBody.get("message");
			List receivers = (ArrayList)requestBody.get("receivers");
			String chatId = (String)requestBody.get("chatId");
			List chattersList = (ArrayList)requestBody.get("chatters");		
			if(receivers != null && receivers.size() != 0){
				for(int index=0; index<receivers.size(); index++){
					Message msg = new Message();
					msg.setContent(message);
					msg.setSendUser(senderId);
					msg.setTargetUser((String)receivers.get(index));
					
					StringBuffer chattersBuff = null;
					if (chattersList != null && chattersList.size() != 0) {
						chattersBuff = new StringBuffer();
						boolean first = true;
						for (int i = 0; i < chattersList.size(); i++) {
							String chattersId = (String)chattersList.get(i);
							if (first) {
								chattersBuff.append(chattersId);	
								first = false;
							} else {
								chattersBuff.append(",").append(chattersId);
							}
						}
						msg.setChattersId(chattersBuff.toString());
					}
					msg.setChatId(chatId);
					
					getMessageManager().createMessage(senderId, msg);
					
					PublishNotice pubNoticeObj = new PublishNotice((String)receivers.get(index), PublishNotice.TYPE_MESSAGE, PublishNotice.REFTYPE_MESSAGE, msg.getObjId());
					getPublishNoticeManager().setPublishNotice("linkadvisor", pubNoticeObj, IManager.LEVEL_ALL);
					SmartUtil.increaseNoticeCountByNoticeType((String)receivers.get(index), Notice.TYPE_MESSAGE);					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void removeAsyncMessage(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		try {
			User user = SmartUtil.getCurrentUser();
			String userId = user.getId();

			String messageId = (String)requestBody.get("workInstanceId");

			Message msg = getMessageManager().getMessage(userId, messageId, IManager.LEVEL_ALL);
			
			String deleteUser = msg.getDeleteUser();
			if(SmartUtil.isBlankObject(deleteUser)) {
				String sendUser = msg.getSendUser();
				String targetUser = msg.getTargetUser();
				if(sendUser.equals(targetUser)){
					getMessageManager().removeMessage(userId, messageId);
				} else {
					msg.setDeleteUser(userId);
					getMessageManager().setMessage(userId, msg, IManager.LEVEL_ALL);				
				}
			} else {
				getMessageManager().removeMessage(userId, messageId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void setAsyncMessage(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		try {
			User user = SmartUtil.getCurrentUser();
			String userId = user.getId();

			String messageId = (String)requestBody.get("instanceId");

			Message msg = getMessageManager().getMessage(userId, messageId, IManager.LEVEL_ALL);
			msg.setChecked(true);
			msg.setCheckedTime(new LocalDate());

			getMessageManager().setMessage(userId, msg, IManager.LEVEL_LITE);

			SmartUtil.publishCurrent(userId, Notice.TYPE_MESSAGE);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public ChatInstanceInfo[] fetchAsyncMessagesByChatid(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String chatId = request.getParameter("chatId");
		String receiverId = request.getParameter("receiverId");
		
		User user = SmartUtil.getCurrentUser();

		MessageCond msgCond = new MessageCond();
		msgCond.setChecked(false);
		msgCond.setTargetUser(receiverId);
		msgCond.setChatId(chatId);
		
		Message[] messages = getMessageManager().getMessages(user.getId(), msgCond, null);
		
		if (messages == null || messages.length == 0)
			return null;
		
		ChatInstanceInfo[] chatInstInfos = new ChatInstanceInfo[messages.length];
		for (int i = 0; i < messages.length; i++) {
			Message message = messages[i];
			ChatInstanceInfo chatInstInfo = new ChatInstanceInfo();
			chatInstInfo.setChatId(chatId);
			chatInstInfo.setSenderId(message.getSendUser());
			chatInstInfo.setSenderInfo(ModelConverter.getUserInfoByUserId(message.getSendUser()));
			chatInstInfo.setChatMessage(message.getContent());
			
			chatInstInfo.setLastModifiedDate(new LocalDate(message.getCreationDate().getTime()));
			
			chatInstInfos[i] = chatInstInfo;
		}
		for (int i = 0; i < messages.length; i++) {
			getMessageManager().removeMessage(user.getId(), messages[i].getObjId());

			PublishNoticeCond pubNoticeObjCond = new PublishNoticeCond(messages[i].getTargetUser(), PublishNotice.TYPE_MESSAGE, PublishNotice.REFTYPE_MESSAGE, messages[i].getObjId());
			PublishNotice pubNotice = getPublishNoticeManager().getPublishNotice("linkadvisor", pubNoticeObjCond, IManager.LEVEL_ALL);
			
			if (!CommonUtil.isEmpty(pubNotice)) {
				getPublishNoticeManager().removePublishNotice(user.getId(), pubNotice.getObjId());
				SmartUtil.increaseNoticeCountByNoticeType(messages[i].getTargetUser(), Notice.TYPE_MESSAGE);	
			}
			
		}
		return chatInstInfos;
	}
	@Override
	public void commentOnTaskForward(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		User user = SmartUtil.getCurrentUser();
		String userId = user.getId();
		
		//태스크인스턴스 아이디를 이용하여 저장 되어 있는 태스크를 조회 하고 실행 가능 여부를 판단한다
		String taskInstId = (String)requestBody.get("taskInstId");
		String comments = (String)requestBody.get("comments");
		
		if (CommonUtil.isEmpty(taskInstId))
			throw new Exception("TaskId ("+taskInstId+") Is Null");
		TskTask task = getTskManager().getTask(userId, taskInstId, IManager.LEVEL_ALL);
		if (task == null)
			throw new Exception("Not Exist Task : taskId = " + taskInstId);
		if (!task.getStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ASSIGN))
			throw new Exception("Task Is Not Executable Status : taskId = " + taskInstId +" (status - " + task.getStatus() + ")");
		if (!task.getAssignee().equalsIgnoreCase(userId)) 
			throw new Exception("Task is Not Executable Assignee : taskId = " + taskInstId + " (assignee - " + task.getAssignee() + " But performer - " + userId + ")");
		if (!task.getType().equalsIgnoreCase(TskTask.TASKTYPE_REFERENCE))
			throw new Exception("Task is Not ReferenceTask : taskId = " + task.getObjId() + ", type = " + task.getType());
		
		//태스크의 실제 완료 시간을 입력한다
		if (task.getRealStartDate() == null)
			task.setRealStartDate(new LocalDate(new Date().getTime()));
		task.setRealEndDate(new LocalDate(new Date().getTime()));
		
		//참조의견을 저장한다
		task.setDocument(comments);
		
		getTskManager().executeTask(userId, task, "execute");
		SmartUtil.removeNoticeByExecutedTaskId(task.getAssignee(), task.getObjId());
	}
	@Override
	public ApprovalLineInst getApprovalLineInstById(String instId) throws Exception {
		if (CommonUtil.isEmpty(instId))
			return null;
		
		User user = SmartUtil.getCurrentUser();
		
		AprApprovalLine aprLine = getAprManager().getApprovalLine(user.getId(), instId, IManager.LEVEL_ALL);
		if (aprLine == null)
			return null;
		
		AprApproval[] apraprs = aprLine.getApprovals();
		
//		String refAppLineDefId = aprLine.getRefAppLineDefId();
//		if (!CommonUtil.isEmpty(refAppLineDefId)) {
//			AprApprovalLineDef aprLineDef = SwManagerFactory.getInstance().getAprManager().getApprovalLineDef(user.getId(), refAppLineDefId, IManager.LEVEL_ALL); 
//		}
		
		ApprovalLineInst approvalLineInst = new ApprovalLineInst();
		Approval[] aprs = new Approval[apraprs.length];
		for (int i = 0; i < apraprs.length; i++) {
			AprApproval aprapr = apraprs[i];
			Approval apr = new Approval();

//			Instance.STATUS_NOT_YET = 0;
//			Instance STATUS_RUNNING = 1;
//			Instance STATUS_DELAYED_RUNNING = 2;
//			Instance STATUS_RETURNED = 3;
//			Instance STATUS_COMPLETED = 4;
//			Instance STATUS_PLANNED = 5;
//			Instance STATUS_REJECTED = 6;
//			Instance STATUS_ABORTED = 7;
//			Instance STATUS_DRAFTED = 8;
			
			//TODO aprapr상태값 확인 필요
			if (aprapr.getStatus() == null) {
				apr.setStatus(Instance.STATUS_NOT_YET);
			} else if (aprapr.getStatus().equalsIgnoreCase("21")) {
				//완료
				apr.setStatus(Instance.STATUS_COMPLETED);
			} else if (aprapr.getStatus().equalsIgnoreCase("23")) {
				//반려
				apr.setStatus(Instance.STATUS_RETURNED);
			} else if (aprapr.getStatus().equalsIgnoreCase("24")) {
				//취소
				apr.setStatus(Instance.STATUS_ABORTED);
			}	
			apr.setName(aprapr.getName());
			
//			Instance APPROVER_CHOOSE_ON_RUNNING = 1;
//			Instance APPROVER_MY_BOSS = 2;
//			Instance APPROVER_CHOOSE_USER = 3;
			apr.setApproverType((aprapr.getType() == null) ? Approval.APPROVER_CHOOSE_ON_RUNNING :  Integer.parseInt(aprapr.getType()));
			
			apr.setApprover(ModelConverter.getUserByUserId(aprapr.getApprover()));
			//apr.setMeanTimeDays();
			//apr.setMeanTimeHours();
			//apr.setMeanTimeMinutes();
			apr.setDueDate(aprapr.getDueDate());
			if (aprapr.getStatus() != null)
				apr.setCompletedDate(new LocalDate(aprapr.getModificationDate().getTime()));
			apr.setMandatory(aprapr.isMandatory());
			apr.setModifiable(aprapr.isModifiable());
			
			aprs[i] = apr;
		}
		approvalLineInst.setApprovals(aprs);
		approvalLineInst.setName(aprLine.getName());
		return approvalLineInst;
	}
	@Override
	public void commentOnTaskApproval(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		
		User user = SmartUtil.getCurrentUser();
		String userId = user.getId();
		
		//태스크인스턴스 아이디를 이용하여 저장 되어 있는 태스크를 조회 하고 실행 가능 여부를 판단한다
		String taskInstId = (String)requestBody.get("taskInstId");
		String comments = (String)requestBody.get("comments");
		String result = (String)requestBody.get("result");
		
		if (CommonUtil.isEmpty(taskInstId))
			throw new Exception("TaskId ("+taskInstId+") Is Null");
		TskTask task = getTskManager().getTask(userId, taskInstId, IManager.LEVEL_ALL);
		if (task == null)
			throw new Exception("Not Exist Task : taskId = " + taskInstId);
		if (!task.getStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ASSIGN))
			throw new Exception("Task Is Not Executable Status : taskId = " + taskInstId +" (status - " + task.getStatus() + ")");
		if (!task.getAssignee().equalsIgnoreCase(userId)) 
			throw new Exception("Task is Not Executable Assignee : taskId = " + taskInstId + " (assignee - " + task.getAssignee() + " But performer - " + userId + ")");
		if (!task.getType().equalsIgnoreCase(TskTask.TASKTYPE_APPROVAL))
			throw new Exception("Task is Not ReferenceTask : taskId = " + task.getObjId() + ", type = " + task.getType());
		
		//태스크의 실제 완료 시간을 입력한다
		if (task.getRealStartDate() == null)
			task.setRealStartDate(new LocalDate(new Date().getTime()));
		task.setRealEndDate(new LocalDate(new Date().getTime()));
		
		//승인의견을 저장한다
		task.setDocument(comments);
		String action = null;
		if (result.equalsIgnoreCase("approved")) {
			//승인
			action = "execute";
		} else if (result.equalsIgnoreCase("")) {
			//반려
			action = "return";
			
		} else if (result.equalsIgnoreCase("rejected")) {
			//기각
			action = "cancel";
			
		} else {
			throw new Exception("Approval Task Failed : Action Is Null");
		}
		
		getTskManager().executeTask(userId, task, action);
		SmartUtil.removeNoticeByExecutedTaskId(task.getAssignee(), task.getObjId());
		
	}
}
