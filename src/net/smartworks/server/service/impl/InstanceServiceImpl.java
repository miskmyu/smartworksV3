package net.smartworks.server.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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

import javax.persistence.criteria.CriteriaBuilder.Case;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.smartworks.model.approval.Approval;
import net.smartworks.model.approval.ApprovalLine;
import net.smartworks.model.approval.ApprovalLineInst;
import net.smartworks.model.calendar.RepeatEvent;
import net.smartworks.model.calendar.WorkHourPolicy;
import net.smartworks.model.community.Group;
import net.smartworks.model.community.User;
import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.community.info.WorkSpaceInfo;
import net.smartworks.model.filter.Condition;
import net.smartworks.model.filter.SearchFilter;
import net.smartworks.model.instance.AsyncMessageInstance;
import net.smartworks.model.instance.CommentInstance;
import net.smartworks.model.instance.EventInstance;
import net.smartworks.model.instance.FieldData;
import net.smartworks.model.instance.ImageInstance;
import net.smartworks.model.instance.InformationWorkInstance;
import net.smartworks.model.instance.Instance;
import net.smartworks.model.instance.ProcessWorkInstance;
import net.smartworks.model.instance.RunningCounts;
import net.smartworks.model.instance.SortingField;
import net.smartworks.model.instance.TaskInstance;
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
import net.smartworks.model.notice.NoticeMessage;
import net.smartworks.model.security.AccessPolicy;
import net.smartworks.model.work.FileCategory;
import net.smartworks.model.work.FormField;
import net.smartworks.model.work.SmartForm;
import net.smartworks.model.work.SmartWork;
import net.smartworks.model.work.SocialWork;
import net.smartworks.model.work.Work;
import net.smartworks.model.work.info.SmartWorkInfo;
import net.smartworks.model.work.info.WorkCategoryInfo;
import net.smartworks.model.work.info.WorkInfo;
import net.smartworks.server.engine.autoindex.manager.IIdxManager;
import net.smartworks.server.engine.autoindex.model.AutoIndexDef;
import net.smartworks.server.engine.autoindex.model.AutoIndexDefCond;
import net.smartworks.server.engine.autoindex.model.AutoIndexInst;
import net.smartworks.server.engine.autoindex.model.AutoIndexInstCond;
import net.smartworks.server.engine.autoindex.model.AutoIndexRule;
import net.smartworks.server.engine.common.manager.IManager;
import net.smartworks.server.engine.common.model.Filter;
import net.smartworks.server.engine.common.model.Filters;
import net.smartworks.server.engine.common.model.InstanceVariable;
import net.smartworks.server.engine.common.model.InstanceVariables;
import net.smartworks.server.engine.common.model.MappingService;
import net.smartworks.server.engine.common.model.MisObjectCond;
import net.smartworks.server.engine.common.model.Order;
import net.smartworks.server.engine.common.model.Property;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.common.util.DateUtil;
import net.smartworks.server.engine.common.util.JsonUtil;
import net.smartworks.server.engine.common.util.MisUtil;
import net.smartworks.server.engine.common.util.StringUtil;
import net.smartworks.server.engine.common.util.WebServiceUtil;
import net.smartworks.server.engine.config.manager.ISwcManager;
import net.smartworks.server.engine.config.model.SwcWebService;
import net.smartworks.server.engine.config.model.SwcWebServiceParameter;
import net.smartworks.server.engine.config.model.SwcWorkHour;
import net.smartworks.server.engine.config.model.SwcWorkHourCond;
import net.smartworks.server.engine.docfile.exception.DocFileException;
import net.smartworks.server.engine.docfile.manager.IDocFileManager;
import net.smartworks.server.engine.docfile.model.FileDownloadHistory;
import net.smartworks.server.engine.docfile.model.FileDownloadHistoryCond;
import net.smartworks.server.engine.docfile.model.FileWork;
import net.smartworks.server.engine.docfile.model.FileWorkCond;
import net.smartworks.server.engine.docfile.model.IFileModel;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.engine.folder.manager.IFdrManager;
import net.smartworks.server.engine.folder.model.FdrFolder;
import net.smartworks.server.engine.folder.model.FdrFolderCond;
import net.smartworks.server.engine.folder.model.FdrFolderFile;
import net.smartworks.server.engine.infowork.domain.manager.ISwdManager;
import net.smartworks.server.engine.infowork.domain.model.SwdDataField;
import net.smartworks.server.engine.infowork.domain.model.SwdDataRef;
import net.smartworks.server.engine.infowork.domain.model.SwdDataRefCond;
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
import net.smartworks.server.engine.organization.model.SwoGroup;
import net.smartworks.server.engine.organization.model.SwoGroupMember;
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
import net.smartworks.server.engine.process.deploy.model.AcpActualParameters;
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
import net.smartworks.server.engine.publishnotice.model.MessageNotice;
import net.smartworks.server.engine.publishnotice.model.MessageNoticeCond;
import net.smartworks.server.engine.worklist.manager.IWorkListManager;
import net.smartworks.server.engine.worklist.model.TaskWork;
import net.smartworks.server.engine.worklist.model.TaskWorkCond;
import net.smartworks.server.service.ICalendarService;
import net.smartworks.server.service.ICommunityService;
import net.smartworks.server.service.IInstanceService;
import net.smartworks.server.service.ISeraService;
import net.smartworks.server.service.factory.SwServiceFactory;
import net.smartworks.server.service.util.ModelConverter;
import net.smartworks.server.service.util.TaskInfoParallelProcessing;
import net.smartworks.server.service.util.TaskParallelProcessing;
import net.smartworks.service.ISmartWorks;
import net.smartworks.util.LocalDate;
import net.smartworks.util.Semaphore;
import net.smartworks.util.SmartMessage;
import net.smartworks.util.SmartTest;
import net.smartworks.util.SmartUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import pro.ucity.manager.ucityWorkList.model.UcityWorkList;
import pro.ucity.manager.ucityWorkList.model.UcityWorkListCond;
import pro.ucity.model.Audit;

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
	private static IFdrManager getFdrManager() {
		return SwManagerFactory.getInstance().getFdrManager();
	}

	@Autowired
	private ICommunityService communityService;
	@Autowired
	private ICalendarService calendarService;
	@Autowired
	private ISeraService seraService;

	public BoardInstanceInfo[] getBoardInstancesByWorkSpaceId(String spaceId, int maxLength) throws Exception {

		try {
			String workId = SmartWork.ID_BOARD_MANAGEMENT;

			User user = SmartUtil.getCurrentUser();
			String userId = user.getId();
			String companyId = user.getCompanyId();

			SwdRecordCond swdRecordCond = new SwdRecordCond();
			swdRecordCond.setCompanyId(companyId);
			String domainId = "frm_notice_SYSTEM";
			swdRecordCond.setDomainId(domainId);
			String[] workSpaceIdIns = ModelConverter.getWorkSpaceIdIns(user);
			if(spaceId != null) {
				if(spaceId.equals(userId))
					swdRecordCond.setCreatorOrSpaceId(spaceId);
				else
					swdRecordCond.setWorkSpaceId(spaceId);
			} else {
				swdRecordCond.setWorkSpaceIdIns(workSpaceIdIns);
			}
			swdRecordCond.setLikeAccessValues(workSpaceIdIns);

			swdRecordCond.setPageNo(0);
			swdRecordCond.setPageSize(maxLength);
			swdRecordCond.setOrders(new Order[]{new Order(FormField.ID_CREATED_DATE, false)});
			
			//공지 종료가 오늘 날짜보다 큰것만 가져온다
			//TODO localDate 로 변경
			Calendar cal = Calendar.getInstance();			
			String searchDate = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + (cal.get(Calendar.DATE) - 1) + " 15:00:00.000";
			swdRecordCond.setFilter(new Filter[]{new Filter(">=","duration", Filter.OPERANDTYPE_DATETIME, searchDate)});
			
			//END
			
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
					String workSpaceType = swdRecord.getWorkSpaceType();
					if(workSpaceType == null)
						workSpaceType = String.valueOf(ISmartWorks.SPACE_TYPE_USER);
					boardInstanceInfo.setWorkSpaceInfo(ModelConverter.getWorkSpaceInfo(workSpaceType, workSpaceId));

					WorkInfo workInfo = new WorkInfo(workId, null, SocialWork.TYPE_BOARD);
					boardInstanceInfo.setWorkInfo(workInfo);
					
					boardInstanceInfo.setSubInstanceCount(getSubInstancesInInstanceCount(boardInstanceInfo.getId()));

					boardInstanceInfo.setLastModifier(ModelConverter.getUserInfoByUserId(swdRecord.getModificationUser()));
					boardInstanceInfo.setLastModifiedDate(new LocalDate((swdRecord.getModificationDate()).getTime()));

					SwdDataField[] swdDataFields = swdRecord.getDataFields();
					if(!CommonUtil.isEmpty(swdDataFields)) {
						int swdDataFieldsLength = swdDataFields.length;
						for(int j=0; j<swdDataFieldsLength; j++) {
							SwdDataField swdDataField = swdDataFields[j];
							String value = swdDataField.getValue();
							if(swdDataField.getId().equals("0")) {
								boardInstanceInfo.setSubject(StringUtil.subString(value, 0, 36, "..."));
							} else if(swdDataField.getId().equals("1")) {
								boardInstanceInfo.setBriefContent(StringUtil.subString(value, 0, 40, "..."));
							} else if(swdDataField.getId().equals("3")) {
								if(!CommonUtil.isEmpty(value)) {
									Date result = DateUtil.toDate(value, "yyyy-MM-dd HH:mm:ss");
									boardInstanceInfo.setDuration(new LocalDate(result.getTime()));
								}
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
		return getBoardInstancesByWorkSpaceId(null, 5);
	}

	@Override
	public BoardInstanceInfo[] getCommunityRecentBoardInstances(String spaceId) throws Exception {
		return getBoardInstancesByWorkSpaceId(spaceId, 5);
	}

	@Override
	public InstanceInfo[] getMyRecentInstances() throws Exception {
		try{
			User cuser = SmartUtil.getCurrentUser();
			String userId = null;
			if (cuser != null)
				userId = cuser.getId();
			
			TaskWorkCond cond = new TaskWorkCond();
			cond.setPackageStatus("DEPLOYED");
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
				instanceInfoList.add(ModelConverter.getWorkInstanceInfoByTaskWork(cuser, task));
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
	public TaskInstanceInfo getTaskInstanceById(String taskInstId) throws Exception {
		User cUser = SmartUtil.getCurrentUser();
		String userId = cUser.getId();
		
		TskTask task = SwManagerFactory.getInstance().getTskManager().getTask(userId, taskInstId, null);
		if (task == null)
			return null;
		
		TskTask[] tasks = new TskTask[1];
		tasks[0] = task;
		
		IWInstanceInfo workInstObj = ModelConverter.getIWInstanceInfoByRecordId(null, tasks[0].getProcessInstId());
		TaskInstanceInfo[] taskInstanceInfo = ModelConverter.getTaskInstanceInfoArrayByTskTaskArray(workInstObj, tasks);
		if (taskInstanceInfo == null || taskInstanceInfo.length == 0)
			return null;
		
		return taskInstanceInfo[0];
	}

	public InstanceInfo[] getMyRunningInstances(LocalDate lastInstanceDate, int requestSize, boolean assignedOnly, boolean runningOnly, RequestParams params) throws Exception {
		
		try{
			//정보관리업무에서 파생된 업무는 IWInstanceInfo
			//프로세스 태스크및 프로세스에서 파생된 업무는 PWInstanceInfo
			
			User user = SmartUtil.getCurrentUser();
			if (CommonUtil.isEmpty(user.getCompanyId()) || CommonUtil.isEmpty(user.getId()))
				return null;
	
			TaskWorkCond taskCond = new TaskWorkCond();
			if (assignedOnly && (runningOnly == false)) {
				taskCond.setTskStatus(TskTask.TASKSTATUS_ASSIGN);
				taskCond.setTskAssignee(user.getId());
			}
			if (runningOnly && (assignedOnly == false)){
				taskCond.setTskStartOnly(user.getId());
				taskCond.setPrcStatusIns(new String[]{PrcProcessInst.PROCESSINSTSTATUS_RUNNING, PrcProcessInst.PROCESSINSTSTATUS_RETURN});
			}
			if (runningOnly && assignedOnly) {
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

			long totalSize = getWorkListManager().getTaskWorkListSize(user.getId(), taskCond);
			
			taskCond.setPageNo(0);
			taskCond.setPageSize(requestSize);
			//taskCond.setPrcStatusIns(new String[]{PrcProcessInst.PROCESSINSTSTATUS_RUNNING, PrcProcessInst.PROCESSINSTSTATUS_RETURN});
			
			taskCond.setOrders(new Order[]{new Order("tskCreatedate", false)});
			
			TaskWork[] tasks = getWorkListManager().getTaskWorkList(user.getId(), taskCond);
			
			if(tasks != null) {
				
				if (totalSize > requestSize) {
					InstanceInfo[] tempResult = ModelConverter.getInstanceInfoArrayByTaskWorkArray(user.getId(), tasks);
					
					InstanceInfo[] result = new InstanceInfo[tempResult.length + 1];
					for (int i = 0; i < tempResult.length; i++) {
						result[i] = tempResult[i];
					}
					result[tempResult.length] = new TaskInstanceInfo();
					return result;
				} else {
					InstanceInfo[] result =  ModelConverter.getInstanceInfoArrayByTaskWorkArray(user.getId(), tasks);
					return result;
				}
			}
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
	public InstanceInfo[] getMyRunningInstances(LocalDate lastInstanceDate, int requestSize, boolean assignedOnly, boolean runningOnly) throws Exception {

		return getMyRunningInstances(lastInstanceDate, requestSize, assignedOnly, runningOnly, null);
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
			totalTaskCond.setTskStartOnly(user.getId());
			//totalTaskCond.setLastInstanceDate(new LocalDate());
			//totalTaskCond.setPrcStatus(PrcProcessInst.PROCESSINSTSTATUS_RUNNING);
			totalTaskCond.setPrcStatusIns(new String[]{PrcProcessInst.PROCESSINSTSTATUS_RUNNING, PrcProcessInst.PROCESSINSTSTATUS_RETURN});
			
			long runningTaskSize = getWorkListManager().getTaskWorkListSize(user.getId(), totalTaskCond);
			
			TaskWorkCond assignedTaskCond = new TaskWorkCond();

			assignedTaskCond.setTskAssignee(user.getId());
			assignedTaskCond.setTskStatus(TskTask.TASKSTATUS_ASSIGN);
			
			long assignedTaskSize = getWorkListManager().getTaskWorkListSize(user.getId(), assignedTaskCond);
			
			RunningCounts runningCounts = new RunningCounts();
			runningCounts.setRunningOnly((int)runningTaskSize);
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
					List<Map<String, String>> resultUsers = null, resultDepartments=null;
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
					}else if(formatType.equals(FormField.TYPE_DEPARTMENT)) {
						if(value != null && refRecordId != null) {
							String[] values = value.split(";");
							String[] refRecordIds = refRecordId.split(";");
							resultDepartments = new ArrayList<Map<String,String>>();
							if(values.length > 0 && refRecordIds.length > 0) {
								for(int j=0; j<values.length; j++) {
									Map<String, String> map = new LinkedHashMap<String, String>();
									map.put("comId", refRecordIds[j]);
									map.put("name", values[j]);
									resultDepartments.add(map);
								}
							} else {
								Map<String, String> map = new LinkedHashMap<String, String>();
								map.put("comId", refRecordId);
								map.put("name", value);
								resultDepartments.add(map);
							}
						}
						swdDataField.setDepartments(resultDepartments);
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
			
			if (logger.isDebugEnabled()) {
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
			
			String recordId = (String)requestBody.get("recordId");
			String taskInstId = (String)requestBody.get("taskInstId");
			String prcInstId = null;
			if (!CommonUtil.isEmpty(taskInstId)) {
				TskTask task = SwManagerFactory.getInstance().getTskManager().getTask(userId, taskInstId, IManager.LEVEL_LITE);
				if (task != null)
					prcInstId = task.getProcessInstId();
			}
			
			boolean isFirstSetMode = !retry; //초기 데이터 입력인지 수정인지를 판단한다0.
			
			//레코드 폼정보를 가져온다
			if (CommonUtil.isEmpty(formId))
				return null;
			SwfForm form = getSwfManager().getForm(null, formId);
			if (form == null)
				return null;
			String formType = form.getFormType();
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

			record.setExtendedAttributeValue("recordId", recordId);
			record.setExtendedAttributeValue("prcInstId", prcInstId);
			record.setExtendedAttributeValue("formType", formType);
			
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
			
			if (logger.isDebugEnabled()) {
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
	
	public SwdDataField getAutoIndexSwdDataField(String userId, SwfForm form, SwfField field, SwdRecord oldRecord, String mode, Map infoMap, boolean isFirst) throws Exception {
		if (field == null)
			return null;

		String formatType = field.getFormat().getType();
		if (!formatType.equalsIgnoreCase("autoIndex"))
			return null;
		
		String fieldId = field.getId();
		String formId = form.getId();
		
		//사용자가 업무를 완료 하는 시점에 시퀀스를 다시 조회 하고 내용을 autoIndexInst에 저장하기위하여 호출한다
		String instanceId = (String)infoMap.get("instanceId");
		String refType = (String)infoMap.get("refType"); //COMMON, SINGLE
		
		boolean isSaveMode = false;
		IIdxManager idxMgr = SwManagerFactory.getInstance().getAutoIndexManager();
		if (!CommonUtil.isEmpty(mode) && mode.equalsIgnoreCase("save")) {
			isSaveMode = true;
		}
		
		AutoIndexDefCond autoIndexDefCond = new AutoIndexDefCond();
		autoIndexDefCond.setFormId(form.getId());
		autoIndexDefCond.setFieldId(fieldId);
		AutoIndexDef autoIndexDef = idxMgr.getAutoIndexDef(userId, autoIndexDefCond, null);
		if (autoIndexDef == null) {
			return null;
		}
		
		StringBuffer valueBuff = new StringBuffer();
		
		AutoIndexRule[] rules = autoIndexDef.getRules();
		if (rules == null || rules.length == 0) {
			return null;
		}

		String selectedListValue = null;
		for (int i = 0; i < rules.length; i++) {
			AutoIndexRule rule = rules[i];
			String ruleId = rule.getRuleId();
			
			if (ruleId.equalsIgnoreCase("ruleId.code")) {
				valueBuff.append(rule.getCodeValue()).append(CommonUtil.toNotNull(rule.getSeperator()));
				
//				if (isSaveMode) {
//					AutoIndexInst autoIndexInst = new AutoIndexInst();
//					autoIndexInst.setInstanceId(instanceId);
//					autoIndexInst.setFormId(formId);
//					autoIndexInst.setFieldId(fieldId);
//					autoIndexInst.setRefType(refType);
//					autoIndexInst.setIdType(ruleId);
//					autoIndexInst.setIdValue(rule.getCodeValue());
//					autoIndexInst.setSeperator(rule.getSeperator());
//					idxMgr.setAutoIndexInst(userId, autoIndexInst, IManager.LEVEL_ALL);
//				}
				
			} else if (ruleId.equalsIgnoreCase("ruleId.date")) {
				String dateFormat = rule.getType();
				if (CommonUtil.isEmpty(dateFormat)) {
					dateFormat = "yyyyMMdd";
				} else {
					if (dateFormat.equalsIgnoreCase("YYYYMMDD")) {
						dateFormat = "yyyyMMdd";
					} else if (dateFormat.equalsIgnoreCase("YYYYMM")) {
						dateFormat = "yyyyMM";
					} else if (dateFormat.equalsIgnoreCase("YYYY")) {
						dateFormat = "yyyy";
					} else if (dateFormat.equalsIgnoreCase("MMDD")) {
						dateFormat = "MMdd";
					} else if (dateFormat.equalsIgnoreCase("MM")) {
						dateFormat = "MM";
					} else if (dateFormat.equalsIgnoreCase("DD")) {
						dateFormat = "dd";
					}
				}
				LocalDate now = new LocalDate();
				SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
				String dateStr = sdf.format(now);
				valueBuff.append(dateStr).append(CommonUtil.toNotNull(rule.getSeperator()));

//				if (isSaveMode) {
//					AutoIndexInst autoIndexInst = new AutoIndexInst();
//					autoIndexInst.setInstanceId(instanceId);
//					autoIndexInst.setFormId(formId);
//					autoIndexInst.setFieldId(fieldId);
//					autoIndexInst.setRefType(refType);
//					autoIndexInst.setIdType(ruleId);
//					autoIndexInst.setIdValue(dateStr);
//					autoIndexInst.setSeperator(rule.getSeperator());
//					idxMgr.setAutoIndexInst(userId, autoIndexInst, IManager.LEVEL_ALL);
//				}
			} else if (ruleId.equalsIgnoreCase("ruleId.sequence")) {
				
				/*<autoIndexRule ruleId="ruleId.sequence" increment="2"
					incrementBy="incrementBy.day" digits="3" seperator="_" />*/
				
				int increment = rule.getIncrement();
				String incrementBy = rule.getIncrementBy();
				String digits = rule.getDigits();
				
				boolean isIncrementValue = true;
				String result_value = null;
				String beforeSeq = null;
				if (!CommonUtil.isEmpty(instanceId)) {
					AutoIndexInstCond lastAutoIndexInstCond = new AutoIndexInstCond();
					lastAutoIndexInstCond.setFormId(formId);
					lastAutoIndexInstCond.setInstanceId(instanceId);
					lastAutoIndexInstCond.setFieldId(fieldId);
					lastAutoIndexInstCond.setIdType("ruleId.sequence");
					AutoIndexInst[] autoIndex = SwManagerFactory.getInstance().getAutoIndexManager().getAutoIndexInsts(userId, lastAutoIndexInstCond, null);
					if (autoIndex != null && autoIndex.length > 0) {
						isIncrementValue = false;
						beforeSeq = autoIndex[0].getIdValue();
					}
				}
				
				//이전 레코드를 조회한다
				AutoIndexInstCond autoCond = new AutoIndexInstCond();
				autoCond.setFormId(formId);
				autoCond.setFieldId(fieldId);
				autoCond.setIdType("ruleId.sequence");
				autoCond.setOrders(new Order[]{new Order("creationDate", false)});
				AutoIndexInst[] autoIndexInsts = SwManagerFactory.getInstance().getAutoIndexManager().getAutoIndexInsts(userId, autoCond, IManager.LEVEL_ALL);
				
				String seqValue = "0";
				if (autoIndexInsts != null && autoIndexInsts.length != 0) {
					seqValue = autoIndexInsts[0].getIdValue();
				}
				
				if (isIncrementValue) {
					
					//조회된 자동채번아이디에서 시퀀스 부분을 조회하고 그값에다가 incrementBy, increment를 이용해 증가한다
					//증가주기가 '매번','하루','한달','일년' 에 따라 증가유무를 결정한다
					int value = Integer.parseInt(seqValue);
					Date now = new Date();
					if (autoIndexInsts != null && autoIndexInsts.length != 0) {
						Date indexCreateDate = autoIndexInsts[0].getCreationDate();
						indexCreateDate.setTime(indexCreateDate.getTime() + TimeZone.getDefault().getRawOffset());

						if (incrementBy.equalsIgnoreCase("incrementBy.item")) {
							value = value + increment;
						} else if (incrementBy.equalsIgnoreCase("incrementBy.day")) {
							SimpleDateFormat sf = new SimpleDateFormat("yyyy.MM.dd");
							if(sf.format(indexCreateDate).equalsIgnoreCase(sf.format(now))) {
								value = value + increment;
							} else {
								value = 1;
							}
						} else if (incrementBy.equalsIgnoreCase("incrementBy.month")) {
							SimpleDateFormat sf = new SimpleDateFormat("yyyy.MM");
							if(sf.format(indexCreateDate).equalsIgnoreCase(sf.format(now))) {
								value = value + increment;
							} else {
								value = 1;
							}
						} else if (incrementBy.equalsIgnoreCase("incrementBy.year")) {
							SimpleDateFormat sf = new SimpleDateFormat("yyyy");
							if(sf.format(indexCreateDate).equalsIgnoreCase(sf.format(now))) {
								value = value + increment;
							} else {
								value = 1;
							}
						}
					} else {
						value = value + increment;
					}
					//digits 를 적용한다
					//%04d 의 의미
					//% -  명령의시작
					//0 - 채워질 문자
					//4 - 총 자리수
					//d - 십진정수
					result_value = String.format("%0"+digits+"d", value);
				} else {
					result_value = beforeSeq;
				}
				
				valueBuff.append(result_value).append(CommonUtil.toNotNull(rule.getSeperator()));
				
				if (isSaveMode && isIncrementValue) {
					AutoIndexInst autoIndexInst = new AutoIndexInst();
					autoIndexInst.setInstanceId(instanceId);
					autoIndexInst.setFormId(formId);
					autoIndexInst.setFieldId(fieldId);
					autoIndexInst.setRefType(refType);
					autoIndexInst.setIdType(ruleId);
					autoIndexInst.setIdValue(result_value);
					autoIndexInst.setSeperator(rule.getSeperator());
					idxMgr.setAutoIndexInst(userId, autoIndexInst, IManager.LEVEL_ALL);
				}
			} else if (ruleId.equalsIgnoreCase("ruleId.list")) {
				
//				String itemsStr = rule.getItems();
//				String[] items = StringUtils.tokenizeToStringArray(itemsStr, "||");
//				if (items == null || items.length == 0) {
//					items = new String[1];
//					items[0] = " ";
//				}
//				selectedListValue = items[0];

				selectedListValue = oldRecord.getDataField(fieldId).getSelectedValue();
				if (!CommonUtil.isEmpty(instanceId) && isFirst) {
					AutoIndexInstCond lastAutoIndexInstCond = new AutoIndexInstCond();
					lastAutoIndexInstCond.setFormId(formId);
					lastAutoIndexInstCond.setInstanceId(instanceId);
					lastAutoIndexInstCond.setFieldId(fieldId);
					lastAutoIndexInstCond.setIdType("ruleId.list");
					lastAutoIndexInstCond.setOrders(new Order[]{new Order("creationDate", false)});
					AutoIndexInst[] autoIndex = SwManagerFactory.getInstance().getAutoIndexManager().getAutoIndexInsts(userId, lastAutoIndexInstCond, null);
					if (autoIndex != null && autoIndex.length > 0) {
						selectedListValue = autoIndex[0].getIdValue();
					}
				}
				
				valueBuff.append(selectedListValue).append(CommonUtil.toNotNull(rule.getSeperator()));
				
				if (isSaveMode) {
					AutoIndexInst autoIndexInst = new AutoIndexInst();
					autoIndexInst.setInstanceId(instanceId);
					autoIndexInst.setFormId(formId);
					autoIndexInst.setFieldId(fieldId);
					autoIndexInst.setRefType(refType);
					autoIndexInst.setIdType(ruleId);
					autoIndexInst.setIdValue(selectedListValue);
					autoIndexInst.setSeperator(rule.getSeperator());
					idxMgr.setAutoIndexInst(userId, autoIndexInst, IManager.LEVEL_ALL);
				}
			}
		}
		
		SwdDataField dataField = toDataField(userId, field, valueBuff.toString());
		if (!CommonUtil.isEmpty(selectedListValue))
			dataField.setSelectedValue(selectedListValue);
		return dataField;
	}
	
	private void setResultFieldMapByFields(String userId, SwfForm form, Map<String, SwdDataField> resultMap, SwfField field, SwdRecord newRecord, SwdRecord oldRecord, boolean isFirst) throws Exception {
		
		try{
			if (resultMap == null)
				resultMap = new HashMap<String, SwdDataField>();
			
			String fieldId = field.getId();
			String fieldType = field.getSystemType();
			
			SwfMappings mappings = field.getMappings();
			String formatType = field.getFormat().getType();
			
			if (mappings == null || formatType.equalsIgnoreCase("autoIndex")) {
				if (formatType.equalsIgnoreCase("autoIndex")) {
					
					boolean isEditMode = false;
					if (isEditMode) {
//						//수정모드 = 업무를 수정하는 단계로 이전에 사용자 선택 리스트가 있다면 이전에 사용자가 선택하였던 아이템을 넘겨야 한다
//
//						SwdDataField oldDataField = oldRecord.getDataField(fieldId);
//						//TODO 레코드 아이디로 콤보박스가 선택되어진 값을 가져와서 입력해준다
//						//oldDataField.setSelectedValue(selectedValue);
//						
//						resultMap.put(fieldId, oldDataField);
//						newRecord.setDataField(fieldId, oldDataField);
						return;
						
					} else {
						//입력모드 = 업무를 처음 작성하는 단계로 아이디값을 새로 따야한다
						
						String formType = oldRecord.getExtendedAttributeValue("formType");
						Map infoMap = new HashMap();
						if (formType.equalsIgnoreCase("SINGLE")) {
							infoMap.put("refType", TskTask.TASKTYPE_SINGLE);
							infoMap.put("instanceId", oldRecord.getExtendedAttributeValue("recordId"));
						} else if (formType.equalsIgnoreCase("PROCESS")) {
							infoMap.put("refType", TskTask.TASKTYPE_COMMON);
							String tempInstanceId = oldRecord.getExtendedAttributeValue("prcInstId");
							if (CommonUtil.isEmpty(tempInstanceId))
								tempInstanceId = oldRecord.getExtendedAttributeValue("recordId");
							infoMap.put("instanceId", tempInstanceId);
						}
						
						SwdDataField dataField = getAutoIndexSwdDataField(userId, form, field, oldRecord, "view", infoMap, isFirst);
						
						if (dataField == null) {
							resultMap.put(fieldId, oldRecord.getDataField(fieldId));
							newRecord.setDataField(fieldId, oldRecord.getDataField(fieldId));
							return;
						}
						resultMap.put(fieldId, dataField);
						newRecord.setDataField(fieldId, dataField);
						return;
					}
				} else {
					resultMap.put(fieldId, oldRecord.getDataField(fieldId));
					newRecord.setDataField(fieldId, oldRecord.getDataField(fieldId));
					return;
				}
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
						if (dataField != null) {
							myDataField.setValue(dataField.getValue() != null ? dataField.getValue().trim() : null);
						//resultMap.put(fieldId, new SwdDataField());
							//if (field.getFormat().getType().equalsIgnoreCase("userField")) {
								myDataField.setRefForm(dataField.getRefForm());
								myDataField.setRefFormField(dataField.getRefFormField());
								myDataField.setRefRecordId(dataField.getRefRecordId());
							//}
						}	
							
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
							
							if (CommonUtil.isEmpty(mappingRecords)) {

								SwdDataField tempDataField = oldRecord.getDataField(fieldId);
								//tempDataField.setValue("");
								resultStack.push(tempDataField);							
								
								continue;
							} else {
								populateRecords(userId, mappingRecords);
							}
							
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
											subDataField.setValue(subMappingDataField.getValue() != null ? subMappingDataField.getValue().trim() : null);
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
									dataField.setValue(mappingDataField.getValue() != null ? mappingDataField.getValue().trim() : null);
									dataField.setRefRecordId(mappingDataField.getRefRecordId());
								}
								//setResultTreeMap
								long timeKey = mappingRecord.getCreationDate().getTime();
								if (!resultTreeMap.containsKey(timeKey)) 
									resultTreeMap.put(timeKey, dataField);
								
								SwfFormat format = field.getFormat();
								if (format == null)
									continue;
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
							
							//Id를 새로 만들기 전에 이미 생성된 아이디가 있다면 새로 만들지 않고 기존것을 사용한다
							if (!CommonUtil.isEmpty(oldRecord.getDataFieldValue(fieldId))) {
								dataField.setValue(oldRecord.getDataFieldValue(fieldId));
							} else {
								dataField.setValue(CommonUtil.newId());
							}
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
							
						} else if (functionId.equals("mis:getCurrentTime")) {
							SwdDataField dataField = new SwdDataField();
							dataField.setId(fieldId);
							dataField.setType(fieldType);
							dataField.setRefRecordId(null);
							dataField.setRefForm(null);
							dataField.setRefFormField(null);

							long time = System.currentTimeMillis(); 
							SimpleDateFormat dayTime = new SimpleDateFormat("HH:mm");
							String value = dayTime.format(new Date(time));
							
							dataField.setValue(value);
							resultStack.push(dataField);
						
						// datetime 조건 생성
						}else if(functionId.equals("mis:getCurrentDateTime")){
							SwdDataField dataField = new SwdDataField();
							dataField.setId(fieldId);
							dataField.setType(fieldType);
							dataField.setRefRecordId(null);
							dataField.setRefForm(null);
							dataField.setRefFormField(null);
							
							
							long time = System.currentTimeMillis(); 
							SimpleDateFormat dayTime = new SimpleDateFormat("yyyy.MM.dd HH:mm");
							String value = dayTime.format(new Date(time));
							
							dataField.setValue(value);
							
							resultStack.push(dataField);
							
							
							
						} else if (functionId.equals("mis:getCurrentUser")) {
							SwdDataField dataField = toDataField(userId, field, userId);
							dataField.setId(fieldId);
							resultStack.push(dataField);
							
						} else if (functionId.equals("mis:getDeptId")){		
							if(func != null){
								funcDeptId = func.getDeptId();
//								SwoDepartment funcdept = getSwoManager().getDepartment(userId, funcDeptId, "all");
//								if(funcdept != null){
//									funcDeptName = funcdept.getName();
//								}
							}
							SwdDataField dataField = toDataField(userId, field, funcDeptId);
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
											dataField.setValue(oldRecord.getDataFieldValue(fieldId) != null ? oldRecord.getDataFieldValue(fieldId).trim() : null);
										} else {
											dataField = toUserDataField(userId, oldRecord.getDataFieldValue(fieldId));
										}
										
										SwdDataField[] subDataFields = new SwdDataField[returnWebService.length];
										for (int j = 0; j < returnWebService.length; j++) {
											
											if (oldRecord.getDataFieldValue(fieldId) != null &&  oldRecord.getDataFieldValue(fieldId) != "") {
												if (oldRecord.getDataFieldValue(fieldId).equalsIgnoreCase(returnWebService[j])) 
													dataField.setValue(returnWebService[j] != null ? returnWebService[j].trim() : null);
											} 
											
											SwdDataField subDataField = new SwdDataField();
											if (fieldFormat == null || !"userField".equals(fieldFormat.getType())) {
												subDataField.setId(field.getId());
												subDataField.setType(field.getSystemType());
												subDataField.setValue(returnWebService[j] != null ? returnWebService[j].trim() : null);
											} else {
												subDataField = toUserDataField(userId, returnWebService[j]);
											}
											subDataFields[j] = subDataField;
										}
										if (dataField.getValue() == null || dataField.getValue() == "") {
											if (returnWebService.length != 0)
												dataField.setValue(returnWebService[0] != null ? returnWebService[0].trim() : null);
										}
										dataField.setDataFields(subDataFields);
										resultStack.push(dataField);
									} else {
										SwfFormat fieldFormat = field.getFormat();
										SwdDataField dataField = new SwdDataField();
										if (fieldFormat == null || !"userField".equals(fieldFormat.getType())) {
											dataField.setId(field.getId());
											dataField.setType(field.getSystemType());
											dataField.setValue(returnWebService[0] != null ? returnWebService[0].trim() : null);
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
						
						String[] tempStrArray = StringUtils.tokenizeToStringArray(expr, "'");
						List numList = new ArrayList();
						for (int i = 0; i < tempStrArray.length; i++) {
							try {
								int tempNum = Integer.parseInt(tempStrArray[i]);
								numList.add(tempNum);
							} catch (Exception e) {
								continue;
							}
						}
						SwfField[] fields = form.getFields();
						
						for (int i = 0; i < numList.size(); i++) {
							int num = (Integer)numList.get(i);
							
							SwfField targetField = null;
							for (SwfField tempField : fields) {
								if (tempField.getId().equalsIgnoreCase(num+"")) {
									targetField = tempField;
									break;
								}
							}
							if (!resultMap.containsKey(num) && targetField != null)
								setResultFieldMapByFields(userId, form, resultMap, targetField, newRecord, oldRecord, isFirst);
						}
						
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
	private void populateRecords(String user, SwdRecord[] objs) throws Exception {
		if (objs == null || objs.length == 0)
			return;

		for (int i = 0; i < objs.length; i++) {
			
			SwdRecord obj = objs[i];
			
			String formId = obj.getFormId();
			String recordId = obj.getRecordId();
			if (recordId == null)
				return;
			
			SwdDataRefCond cond = new SwdDataRefCond();
			cond.setMyFormId(formId);
			cond.setMyRecordId(recordId);
			SwdDataRef[] dataRefs = this.getSwdManager().getDataRefs(user, cond, null);
			if (CommonUtil.isEmpty(dataRefs))
				return;
			
			String fieldId;
			SwdDataField dataField;
			for (SwdDataRef dataRef : dataRefs) {
				fieldId = dataRef.getMyFormFieldId();
				dataField = obj.getDataField(fieldId);
				if (dataField == null)
					continue;
				dataField.setRefForm(dataRef.getRefFormId());
				dataField.setRefFormField(dataRef.getRefFormFieldId());
				dataField.setRefRecordId(dataRef.getRefRecordId());
			}
		}
	}
	private SwdDataField toDataField(String user, SwfField field, String id) throws Exception {
		if (CommonUtil.isEmpty(id))
			return null;
		SwfFormat fieldFormat = field.getFormat();
		SwdDataField obj = null;
		if (fieldFormat == null || (!"userField".equals(fieldFormat.getType()) && !"departmentField".equals(fieldFormat.getType()))) {
			obj = new SwdDataField();
			obj.setId(field.getId());
			obj.setType(field.getSystemType());
			obj.setName(field.getName());
			obj.setValue(id);
		} else if("userField".equals(fieldFormat.getType())){
			obj = toUserDataField(user, id);
		}else if("departmentField".equals(fieldFormat.getType())){
			obj = toDepartmentDataField(user, id);
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
		dataField.setValue(userModel.getPosition() == null || userModel.getPosition().equalsIgnoreCase("") ? userModel.getName() : userModel.getPosition() + " " + userModel.getName());
		return dataField;
	}
	private SwdDataField toDepartmentDataField(String user, String id) throws Exception {
		if (CommonUtil.isEmpty(id))
			return null;
		SwoDepartment departModel = getSwoManager().getDepartment(user, id, IManager.LEVEL_LITE);
		if (departModel == null)
			return null;
		SwdDataField dataField = new SwdDataField();
		dataField.setRefForm("frm_depart_SYSTEM");
		dataField.setRefFormField("4");
		dataField.setRefRecordId(id);
		dataField.setValue(ModelConverter.getDepartmentInfoFullpathNameByDepartmentId(id));
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
	private String saveTempWorkInstance(String userId, String tempSavedId, Map<String, Object> requestBody, HttpServletRequest request, boolean isIwork) throws Exception {
		if (requestBody == null)
			return null;
		
		String workId = (String)requestBody.get("workId");
		String formId = (String)requestBody.get("formId");
		String formName = (String)requestBody.get("formName");
		String title = "";
		
		if (isIwork) {
			SwdDomainCond domainCond = new SwdDomainCond();
			domainCond.setFormId(formId);
			SwdDomain domain = getSwdManager().getDomain(userId, domainCond, null);
			SwdRecord record = getSwdRecordByRequestBody(userId, domain.getFields(), requestBody, request);
			String titleFieldId = domain.getTitleFieldId();
			if (!CommonUtil.isEmpty(titleFieldId)) {
				title = record.getDataFieldValue(titleFieldId);
			}
		} else {
			//프로세스 업무의 타이틀을 가져온
			
			
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
			
			//formId 로 실행할 태스크를 조회한다
			TskTaskDefCond tskDefCond = new TskTaskDefCond();
			tskDefCond.setForm(formId);
			TskTaskDef[] taskDefs = SwManagerFactory.getInstance().getTskManager().getTaskDefs(userId, tskDefCond, IManager.LEVEL_ALL);
			if (taskDefs == null || taskDefs.length == 0)
				throw new Exception("Not Exist Task Definition : formId = " + formId);
			TskTaskDef taskDef = taskDefs[0];
			
			if (!CommonUtil.isEmpty(taskDef.getExtendedPropertyValue("subjectFieldId"))) {
				title = CommonUtil.toNotNull(recordObj.getDataFieldValue(taskDef.getExtendedPropertyValue("subjectFieldId")));
			} else {
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
			}
		}
		TskTask tempSaveTask = null;
		if (!CommonUtil.isEmpty(tempSavedId)) {
			tempSaveTask = getTskManager().getTask(userId, tempSavedId, IManager.LEVEL_LITE);
		}
		if (tempSaveTask == null) {
			tempSaveTask = new TskTask();
		}
		tempSaveTask.setName(formName);
		//tempSaveTask.setDocument(requestBody.toString());
		tempSaveTask.setDocument(JsonUtil.getJsonStringByMap(requestBody));
		tempSaveTask.setAssignee(userId);
		tempSaveTask.setAssigner(userId);
		tempSaveTask.setDef(workId + "|" + formId);
		tempSaveTask.setForm(formId);
		tempSaveTask.setTitle(CommonUtil.toDefault(title, "(No Title) - " + new LocalDate()));
		
		if (isIwork) {
			tempSaveTask.setRefType(TskTask.TASKTYPE_SINGLE);
		} else {
			tempSaveTask.setRefType(TskTask.TASKTYPE_COMMON);
		}
		
		tempSaveTask = SwManagerFactory.getInstance().getTskManager().setTempTask(userId, tempSaveTask);
		
		return tempSaveTask.getObjId();
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
			User cuser = SmartUtil.getCurrentUser();
			String userId = null;
			String companyId = null;
			if (cuser != null) {
				userId = cuser.getId();
				companyId = cuser.getCompanyId();
			}
			//임시저장이라면 임시저장타입의 태스크를 생성한후 taskDoc 에 reqeustBody.toString() 자체를 저장하고 이후 조회시 파싱하여 보낸다
			boolean isTempSave = CommonUtil.toBoolean((Boolean)requestBody.get("isTempSave"));
			String tempSavedId = (String)requestBody.get("tempSavedId");
			if (isTempSave) {
				return this.saveTempWorkInstance(userId, tempSavedId, requestBody, request, true);
			} else {
				if (!CommonUtil.isEmpty(tempSavedId)) {
					getTskManager().removeTask(userId, tempSavedId);
				}
			}
			
			Map<String, Object> frmSmartFormMap = (Map<String, Object>)requestBody.get("frmSmartForm");
			Map<String, Object> frmAccessSpaceMap = (Map<String, Object>)requestBody.get("frmAccessSpace");
			Map<String, Object> frmTaskForwardMap = (Map<String, Object>)requestBody.get("frmTaskForward");
	
			String domainId = null; // domainId 가 없어도 내부 서버에서 폼아이디로 검색하여 저장
			String formId = (String)requestBody.get("formId");
			String formName = (String)requestBody.get("formName");
			String instanceId = (String)requestBody.get("instanceId");
			
			
			boolean isCreateRecord = false;
			if(CommonUtil.isEmpty(instanceId)) {
				instanceId = "dr_" + CommonUtil.newId();
				isCreateRecord = true;
			}
			int formVersion = 1;

			String servletPath = request.getServletPath();
			RepeatEvent repeatEvent = null;
			String repeatId = null;
			if(servletPath.equals("/create_new_event.sw")){
				Map<String, Object> eventRepeatPolicyMap = (Map<String, Object>)requestBody.get("repeatPolicy");
				if(!SmartUtil.isBlankObject(eventRepeatPolicyMap)){
					repeatEvent = new RepeatEvent( 	(String)eventRepeatPolicyMap.get("repeatBy"), 
													(String)eventRepeatPolicyMap.get("repeatWeek"),
													(String)eventRepeatPolicyMap.get("repeatDay"),
													(String)eventRepeatPolicyMap.get("repeatDate"),
													(String)eventRepeatPolicyMap.get("repeatEnd"),
													(String)eventRepeatPolicyMap.get("repeatEndDate"),
													(String)eventRepeatPolicyMap.get("repeatEndCount"));
					if(!SmartUtil.isBlankObject(repeatEvent)){
						repeatId = "repeat_" + CommonUtil.newId();
					}
				}
			}
			
			SwdDomainCond swdDomainCond = new SwdDomainCond();
			swdDomainCond.setFormId(formId);
			SwdDomain swdDomain = getSwdManager().getDomain(userId, swdDomainCond, IManager.LEVEL_LITE);

			String tableName = null;
			String keyColumn = null;
			boolean keyDuplicable = true;
			if(!SmartUtil.isBlankObject(swdDomain)) {
				domainId = swdDomain.getObjId();
				tableName = swdDomain.getTableName();
				keyColumn = swdDomain.getKeyColumn();
				keyDuplicable = swdDomain.isKeyDuplicable();
			}

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
			List<Map<String, String>> departments = null;
			String groupId = null;
			List<String> groupIdList = new ArrayList();
			Map<String, List<Map<String, String>>> fileGroupMap = new HashMap<String, List<Map<String, String>>>();
			while (itr.hasNext()) {
				String fieldId = (String)itr.next();
				SwdField field = fieldInfoMap.get(fieldId);
				String fieldType = field.getFormFieldType();
				String value = null;
				String type = null;
				String refForm = null;
				String refFormField = null;
				String refRecordId = null;
				Object fieldValue = frmSmartFormMap.get(fieldId);
				String autoIndexSelectedValue = null;
				
				
				if(!keyDuplicable) {
					if(fieldId.equals(keyColumn)) {
						long objectCount = getSwdManager().getObjectsCountByFormFieldId(domainId, fieldId, tableName, instanceId, String.valueOf(fieldValue));
						if(objectCount > 0)
							throw new DuplicateKeyException("duplicateKeyException");
					}
				}

				if (fieldType.equalsIgnoreCase("complex")) {
					
					SwdDataField fieldData = new SwdDataField();
					fieldData.setId(fieldId);
					fieldData.setName(fieldInfoMap.get(fieldId).getFormFieldName());
					fieldData.setRefForm(refForm);
					fieldData.setRefFormField(refFormField);
					fieldData.setRefRecordId(refRecordId);
					
					value = JsonUtil.getJsonStringByMap((LinkedHashMap)frmSmartFormMap.get(fieldId));
					
					value = StringUtils.replace(value, "﻿", "");// 에디터로 작성된 내용중 자판에 없는 특수문자가 하나 숨어 들어온다 그문자를 제거하는 소스
					fieldData.setValue(value);
					
					fieldDataList.add(fieldData);
				} else {
					if (fieldValue instanceof LinkedHashMap) {
						Map<String, Object> valueMap = (Map<String, Object>)fieldValue;
						groupId = (String)valueMap.get("groupId");
						refForm = (String)valueMap.get("refForm");
						String autoIndexValue = (String)valueMap.get("value");
						autoIndexSelectedValue = (String)valueMap.get("selectedValue");
						users = (ArrayList<Map<String,String>>)valueMap.get("users");
						departments = (ArrayList<Map<String,String>>)valueMap.get("departments");

						if(!CommonUtil.isEmpty(groupId)) {
							files = (ArrayList<Map<String,String>>)valueMap.get("files");
							if(!CommonUtil.isEmpty(files)) {
								fileGroupMap.put(groupId, files);
								value = groupId;
								groupIdList.add(groupId);
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
							
//							SwoDepartmentCond swoDepartmentCond = new SwoDepartmentCond();
//							swoDepartmentCond.setId(refRecordId);
//							String deptName = getSwoManager().getDepartment(userId, swoDepartmentCond, IManager.LEVEL_LITE).getName();
//							value = deptName;
						
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
						} else if(!CommonUtil.isEmpty(departments)) {
							refForm = "frm_depart_SYSTEM";
							refFormField = "4";
							String resultRefRecordId = "";
							String resultValue = "";
							String symbol = ";";
							if(departments.size() == 1) {
								resultRefRecordId = departments.get(0).get("id");
								resultValue = departments.get(0).get("name");
							} else {
								for(int i=0; i < departments.subList(0, departments.size()).size(); i++) {
									Map<String, String> department = departments.get(i);
									resultRefRecordId += department.get("id") + symbol;
									resultValue += department.get("name") + symbol;
								}
							}
							refRecordId = resultRefRecordId;
							value = resultValue;
						} else if(!CommonUtil.isEmpty(autoIndexValue)) {
							value = autoIndexValue;
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
							} else {
								if(type.equals("datetime")) {
									if(value.length() == FieldData.SIZE_DATETIME) {
										value = LocalDate.convertLocalDateTimeStringToLocalDate(value).toGMTDateString();								
									} else if(value.length() == FieldData.SIZE_DATE) {
										value = LocalDate.convertLocalDateStringToLocalDate(value).toGMTDateString();
									}
								} else if(type.equals("time")) {
									value = LocalDate.convertLocalTimeStringToLocalDate(value).toGMTTimeString2();
								}
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
					} else {
						if (fieldInfoMap.get(fieldId).getFormFieldType().equalsIgnoreCase("boolean"))
							value = "true";
					}
					SwdDataField fieldData = new SwdDataField();
					fieldData.setId(fieldId);
					fieldData.setName(fieldInfoMap.get(fieldId).getFormFieldName());
					fieldData.setRefForm(refForm);
					fieldData.setRefFormField(refFormField);
					fieldData.setRefRecordId(refRecordId);
					
					//unescape
					if (fieldInfoMap.get(fieldId).getFormFieldType().equalsIgnoreCase("text")) {
						//value = StringUtil.unescape(value);
						value = SmartUtil.smartDecode(value);
						value = StringUtils.replace(value, "&nbsp;", "<span class=\"Apple-tab-span\" style=\"white-space:pre\"> </span>");
					}
					value = StringUtils.replace(value, "﻿", ""); // 에디터로 작성된 내용중 자판에 없는 특수문자가 하나 숨어 들어온다 그문자를 제거하는 소스

					fieldData.setValue(value);
					if (!CommonUtil.isEmpty(autoIndexSelectedValue))
						fieldData.setSelectedValue(autoIndexSelectedValue);
		
					fieldDataList.add(fieldData);
					
				}
			}
	
			if(!SmartUtil.isBlankObject(repeatId)){
				SwdDataField fieldData = new SwdDataField();
				fieldData.setId(FormField.ID_NUM_REPEAT_EVENT_ID);
				fieldData.setName(fieldInfoMap.get(FormField.ID_NUM_REPEAT_EVENT_ID).getFormFieldName());
				fieldData.setValue(repeatId);
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
				
				if(String.valueOf(AccessPolicy.LEVEL_CUSTOM).equals(accessLevel) && CommonUtil.isEmpty(accessValue)) {
					accessValue = ModelConverter.getAccessValue(userId, formId);
				}

				//authProxy
				boolean isDefaultAccessPolicy = SwManagerFactory.getInstance().getSwaManager().compareAccessPolicyWithAuthProxy(userId, formId, accessLevel, accessValue);
				//사용자가 선택한 접근권한과 기본 빌더의 접근권한을 비교하여 사용자가 선택한 접근권한이라면 나중에 빌더의 권한이 바뀌더라도 적용되지 않는다
				if (!isDefaultAccessPolicy) 
					obj.setIsUserSetAccessLevel("true");
				
				
				obj.setAccessLevel(accessLevel);
				obj.setAccessValue(accessValue);
				
				
				//워크스페이스(workspaceid) 가 비공개 그룹이라면 무조건 비공개 그룹의 선택 공개로 저장이 된다
				populatePrivateGroupAuth(userId, obj);
				
			}

			String recId = obj.getRecordId();
			if (CommonUtil.isEmpty(recId)) {
				recId = CommonUtil.newId();
				obj.setRecordId(recId);
			}
			
			//참조 업무, 승인 업무 데이터 생성
			setReferenceApprovalToRecord(userId, obj, requestBody);
			
			//TODO 좋은방법이 멀까?
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

			if (!CommonUtil.isEmpty((String)requestBody.get("makeNewNotClone")))
				obj.setExtendedAttributeValue("makeNewNotClone", (String)requestBody.get("makeNewNotClone"));
			
			//필드중에 자동채번 필드가 있다면 시퀀스를 저장 시점으로 다시 갱신하기 위하여 아래를 호출한다
			populateAutoIndexField(userId, TskTask.TASKTYPE_SINGLE, formId, obj.getRecordId(), obj);

			instanceId = getSwdManager().setRecord(userId, obj, IManager.LEVEL_ALL);
			

			TskTaskCond tskCond = new TskTaskCond();
			tskCond.setExtendedProperties(new Property[] {new Property("recordId", instanceId)});
			//tskCond.setModificationUser(userId);
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
			if (groupIdList.size() != 0) {
				for (int i = 0; i < groupIdList.size(); i++) {
					if (CommonUtil.isEmpty(groupIdList.get(i)))
						continue;
					String tempGroupId = groupIdList.get(i);
					List<IFileModel> iFileModelList = getDocManager().findFileGroup(tempGroupId);
					if(iFileModelList.size() > 0) {
						for(int j=0; j<iFileModelList.size(); j++) {
							IFileModel fileModel = iFileModelList.get(j);
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
			}
			if (isCreateRecord)
				populateSpaceNotice(obj, taskInstId);
						
			if(!SmartUtil.isBlankObject(repeatEvent)){
				SwdRecord eventRecord = obj;
				
				repeatEvent.setStartTime(LocalDate.convertGMTStringToLocalDate2(eventRecord.getDataField(FormField.ID_NUM_EVENT_START_TIME).getValue()));
				if(eventRecord.getDataField(FormField.ID_NUM_EVENT_END_TIME) != null)
					repeatEvent.setEndTime(LocalDate.convertGMTStringToLocalDate2(eventRecord.getDataField(FormField.ID_NUM_EVENT_END_TIME).getValue()));

				if(repeatEvent!=null && eventRecord!=null && (repeatEvent.getRepeatCount()>0 || repeatEvent.getRepeatEndDate()!=null) && repeatEvent.getStartTime()!=null
						&& (repeatEvent.getRepeatCount()>0 || repeatEvent.getRepeatEndDate().getTime()>repeatEvent.getStartTime().getTime())){
					int repeatCount=0; long increment=0; boolean isMonthIncrement=false; int diffMonths;
					long thisYear=0, thisMonth=0, thisDate=0, thisTime=0; 
					switch(repeatEvent.getRepeatInterval()){
					case RepeatEvent.REPEAT_INTERVAL_EVERY_DAY:
						increment = LocalDate.ONE_DAY;
						repeatCount = (repeatEvent.getRepeatCount()>0) ? repeatEvent.getRepeatCount() : (int)(repeatEvent.getRepeatEndDate().getTime() - repeatEvent.getStartTime().getTime())/(int)increment+1;
						break;
					case RepeatEvent.REPEAT_INTERVAL_EVERY_WEEK:
						increment = LocalDate.ONE_WEEK;
						repeatCount = (repeatEvent.getRepeatCount()>0) ? repeatEvent.getRepeatCount() : (int)(repeatEvent.getRepeatEndDate().getTime() - repeatEvent.getStartTime().getTime()+LocalDate.ONE_DAY)/(int)increment+1;
						break;
					case RepeatEvent.REPEAT_INTERVAL_BI_WEEK:
						increment = LocalDate.ONE_WEEK*2;
						repeatCount = (repeatEvent.getRepeatCount()>0) ? repeatEvent.getRepeatCount() : (int)(repeatEvent.getRepeatEndDate().getTime() - repeatEvent.getStartTime().getTime()+LocalDate.ONE_DAY)/(int)increment+1;
						break;
					case RepeatEvent.REPEAT_INTERVAL_EVERY_MONTH_DATE:
					case RepeatEvent.REPEAT_INTERVAL_EVERY_MONTH_CUSTOM:
						increment = 1;
						isMonthIncrement=true;
						diffMonths = (repeatEvent.getRepeatEndDate()!=null) ? (repeatEvent.getRepeatEndDate().getYear()*12 + repeatEvent.getRepeatEndDate().getMonth() - repeatEvent.getStartTime().getYear()*12 - repeatEvent.getStartTime().getMonth()) : 0 ;
						repeatCount = (repeatEvent.getRepeatCount()>0) ? repeatEvent.getRepeatCount() : diffMonths +1;
						
						break;
					case RepeatEvent.REPEAT_INTERVAL_BI_MONTH_DATE:
					case RepeatEvent.REPEAT_INTERVAL_BI_MONTH_CUSTOM:
						increment = 2;
						isMonthIncrement=true;
						diffMonths = (repeatEvent.getRepeatEndDate()!=null) ? (repeatEvent.getRepeatEndDate().getYear()*12 + repeatEvent.getRepeatEndDate().getMonth() - repeatEvent.getStartTime().getYear()*12 - repeatEvent.getStartTime().getMonth()) : 0 ;
						repeatCount = (repeatEvent.getRepeatCount()>0) ? repeatEvent.getRepeatCount() : diffMonths/2 +1;
						break;
					}
					
					for(int i=1; i<repeatCount; i++){
						LocalDate startTime=null, endTime=null;
						if(isMonthIncrement){
							if(repeatEvent.getWeekOfMonth()>=0 && repeatEvent.getDayOfWeek()>=0){
								startTime = LocalDate.convertLocalDateWithDiffMonth(repeatEvent.getStartTime(), (int)(i*increment), repeatEvent.getWeekOfMonth(), repeatEvent.getDayOfWeek());
							}else{
								startTime = LocalDate.convertLocalDateWithDiffMonth(repeatEvent.getStartTime(), (int)(i*increment));
							}
						}else{
							startTime = new LocalDate(repeatEvent.getStartTime().getTime()+i*increment);
						}
						SwdDataField startTimeField = eventRecord.getDataField(FormField.ID_NUM_EVENT_START_TIME);
						startTimeField.setValue(startTime.toGMTDateString());
						eventRecord.setDataField(FormField.ID_NUM_EVENT_START_TIME, startTimeField);
						if(repeatEvent.getEndTime()!=null){
							if(isMonthIncrement){
								if(repeatEvent.getWeekOfMonth()>=0 && repeatEvent.getDayOfWeek()>=0){
									startTime = LocalDate.convertLocalDateWithDiffMonth(repeatEvent.getStartTime(), (int)(i*increment), repeatEvent.getWeekOfMonth(), repeatEvent.getDayOfWeek());										
								}else{
									startTime = LocalDate.convertLocalDateWithDiffMonth(repeatEvent.getStartTime(), (int)(i*increment));
								}
							}else{
								endTime = new LocalDate(repeatEvent.getEndTime().getTime()+i*increment);									
							}
							SwdDataField endTimeField = eventRecord.getDataField(FormField.ID_NUM_EVENT_END_TIME);
							endTimeField.setValue(endTime.toGMTDateString());
							eventRecord.setDataField(FormField.ID_NUM_EVENT_END_TIME, endTimeField);
						}
						try{
							if(startTime!=null && !(repeatEvent.getEndTime()!=null && endTime==null)){
								eventRecord.setRecordId("dr_" + CommonUtil.newId());
								String repeatInstanceId = getSwdManager().setRecord(userId, eventRecord, IManager.LEVEL_ALL);
							}
						}catch (Exception e){
							e.printStackTrace();
							break;
						}
					}
				}
			}
			
			return instanceId;

		} catch (DuplicateKeyException dke) {
			throw new DuplicateKeyException("duplicateKeyException");
		} catch (Exception e) {
			e.printStackTrace();
			return null;			
		}
	}
	private void removeMessageNotice(String workId, String referenceId) throws Exception {
		if (CommonUtil.isEmpty(referenceId))
			return;
		MessageNoticeCond cond = new MessageNoticeCond();
		cond.setRefId(referenceId);
		cond.setWorkId(workId);
		SwManagerFactory.getInstance().getPublishNoticeManager().removeMessageNotice("", cond);
	}
	
	private void populateSpaceNotice(SwdRecord record, String taskInstanceId) throws Exception {
		//정보처리 업무 알림 ??
		if (record == null)
			return;
		
		//비공개라면 자신밖에 볼수 없기 때문에 알림이 필요 없
		if (record.getAccessLevel().equalsIgnoreCase(AccessPolicy.LEVEL_PRIVATE + ""))
			return;
			
		SwfFormCond cond = new SwfFormCond();
		cond.setId(record.getFormId());
		SwfForm[] form = SwManagerFactory.getInstance().getSwfManager().getForms("", cond, IManager.LEVEL_LITE);
		if (form == null || form.length == 0)
			return;
		
		String workId = form[0].getPackageId();
		String workSpaceType = record.getWorkSpaceType();
		String workSpaceId = record.getWorkSpaceId();
		String recordId = record.getRecordId();
		String refType = "RECORD";
		String accessLevel = record.getAccessLevel();
		String accessValue = record.getAccessValue();
		
		populateMessageNotice(workId, workSpaceType, workSpaceId, refType, recordId, taskInstanceId, accessLevel, accessValue);
		
	}
	private void populateSpaceNotice(TskTask task) throws Exception {
		//프로세스 업무
		if (task == null)
			return;

		//비공개라면 자신밖에 볼수 없기 때문에 알림이 필요 없
		if (task.getAccessLevel().equalsIgnoreCase(AccessPolicy.LEVEL_PRIVATE + ""))
			return;
		
		User user = SmartUtil.getCurrentUser();
		String userId = user.getId();
		
		PrcProcessInst prcInst = SwManagerFactory.getInstance().getPrcManager().getProcessInst(userId, task.getProcessInstId(), IManager.LEVEL_LITE);
		if (prcInst == null)
			return;
		
		String workId = prcInst.getPackageId();
		String workSpaceType = task.getWorkSpaceType();
		String workSpaceId = task.getWorkSpaceId();
		String taskId = task.getObjId();
		String refType = "TASK";
		String accessLevel = task.getAccessLevel();
		String accessValue = task.getAccessValue();
		
		populateMessageNotice(workId, workSpaceType, workSpaceId, refType, taskId, taskId, accessLevel, accessValue);
			
	}
	private void populateMessageNotice(String workId, String workSpaceType, String workSpaceId, String refType, String refId, String taskId, String accessLevel, String accessValue) throws Exception {
		
		User user = SmartUtil.getCurrentUser();
		String userId = user.getId();
		
		if (workSpaceId.equalsIgnoreCase(userId))
			return;

		//workSpaceId 에 속한 사용자를 가져온다
		List targetUserId = new ArrayList();
		
		if (accessLevel.equalsIgnoreCase(AccessPolicy.LEVEL_CUSTOM + "")) {
			if (CommonUtil.isEmpty(accessValue))
				return;
			String[] accessUsers = StringUtils.tokenizeToStringArray(accessValue, ";");
			
			List workSpaceUserList = new ArrayList();
			if (workSpaceType.equalsIgnoreCase(ISmartWorks.SPACE_TYPE_DEPARTMENT + "")) {
				UserInfo[] userInfos = SwServiceFactory.getInstance().getCommunityService().getAllUsersByDepartmentId(workSpaceId);
				if (userInfos == null || userInfos.length == 0)
					return;
				for (int j = 0; j < userInfos.length; j++) {
					if (!userId.equalsIgnoreCase(userInfos[j].getId()))
						workSpaceUserList.add(userInfos[j].getId());
				}
			} else if (workSpaceType.equalsIgnoreCase(ISmartWorks.SPACE_TYPE_GROUP + "")) {
				SwoGroup group = SwManagerFactory.getInstance().getSwoManager().getGroup(userId, workSpaceId, null);
				if (group == null)
					return;
				
				SwoGroupMember[] groupMembers = group.getSwoGroupMembers();
				for (int j = 0; j < groupMembers.length; j++) {
					SwoGroupMember groupMember = groupMembers[j];
					String joinType = groupMember.getJoinType();
					String joinStatus = groupMember.getJoinStatus();
					if (joinStatus.equalsIgnoreCase(SwoGroupMember.JOINSTATUS_COMPLETE)) {
						if (!userId.equalsIgnoreCase(groupMember.getUserId()))
							workSpaceUserList.add(groupMember.getUserId());
					}
				}
			} else if (workSpaceType.equalsIgnoreCase(ISmartWorks.SPACE_TYPE_USER + "")) {
				workSpaceUserList.add(workSpaceId);
			}
			
			for (int i = 0; i < accessUsers.length; i++) {
				if (!userId.equalsIgnoreCase(accessUsers[i])) {
					
					if (accessUsers[i].indexOf("dept_") != -1) {
						
						UserInfo[] userInfos = SwServiceFactory.getInstance().getCommunityService().getAllUsersByDepartmentId(accessUsers[i]);
						if (userInfos == null || userInfos.length == 0)
							return;
						for (int j = 0; j < userInfos.length; j++) {
							if (!userId.equalsIgnoreCase(userInfos[j].getId())) {
								if (workSpaceUserList.contains(userInfos[j].getId())) {
									targetUserId.add(userInfos[j].getId());
								}
							}
						}
						
					} else if (accessUsers[i].indexOf("group_") != -1) {
						
						SwoGroup group = SwManagerFactory.getInstance().getSwoManager().getGroup(userId, accessUsers[i], null);
						if (group == null)
							return;
						
						SwoGroupMember[] groupMembers = group.getSwoGroupMembers();
						for (int j = 0; j < groupMembers.length; j++) {
							SwoGroupMember groupMember = groupMembers[j];
							String joinType = groupMember.getJoinType();
							String joinStatus = groupMember.getJoinStatus();
							if (joinStatus.equalsIgnoreCase(SwoGroupMember.JOINSTATUS_COMPLETE)) {
								if (!userId.equalsIgnoreCase(groupMember.getUserId())) {
									if (workSpaceUserList.contains(groupMember.getUserId())) {
										targetUserId.add(groupMember.getUserId());
									}
								}
							}
						}
						
					} else {
						if (workSpaceUserList.contains(accessUsers[i]))
							targetUserId.add(accessUsers[i]);
					}
					
				}
			}
			
		} else {
			if (workSpaceType.equalsIgnoreCase(ISmartWorks.SPACE_TYPE_DEPARTMENT + "")) {
				//부서공간
				UserInfo[] userInfos = SwServiceFactory.getInstance().getCommunityService().getAllUsersByDepartmentId(workSpaceId);
				if (userInfos == null || userInfos.length == 0)
					return;
				for (int i = 0; i < userInfos.length; i++) {
					if (!userId.equalsIgnoreCase(userInfos[i].getId()))
						targetUserId.add(userInfos[i].getId());
				}
			} else if (workSpaceType.equalsIgnoreCase(ISmartWorks.SPACE_TYPE_GROUP + "")) {
				//그룹공간
				SwoGroup group = SwManagerFactory.getInstance().getSwoManager().getGroup(userId, workSpaceId, null);
				if (group == null)
					return;
				
				SwoGroupMember[] groupMembers = group.getSwoGroupMembers();
				for (int i = 0; i < groupMembers.length; i++) {
					SwoGroupMember groupMember = groupMembers[i];
					String joinType = groupMember.getJoinType();
					String joinStatus = groupMember.getJoinStatus();
					if (joinStatus.equalsIgnoreCase(SwoGroupMember.JOINSTATUS_COMPLETE)) {
						if (!userId.equalsIgnoreCase(groupMember.getUserId()))
							targetUserId.add(groupMember.getUserId());
					}
				}
			} else if (workSpaceType.equalsIgnoreCase(ISmartWorks.SPACE_TYPE_USER + "")) {
				if (!userId.equalsIgnoreCase(workSpaceId))
					targetUserId.add(workSpaceId);
			}
		}
		
		//notice를 생성한다
		if (targetUserId.size() == 0)
			 return;
		for (int i = 0; i < targetUserId.size(); i++) {
			MessageNotice sn = new MessageNotice();
			sn.setType(NoticeMessage.TYPE_INSTANCE_CREATED +"");
			sn.setWorkId(workId);
			sn.setWorkSpaceType(workSpaceType);
			sn.setWorkSpaceId(workSpaceId);
			sn.setAssignee((String)targetUserId.get(i));
			sn.setRefType(refType);
			sn.setRefId(refId);
			sn.setTaskId(taskId);
			SwManagerFactory.getInstance().getPublishNoticeManager().setMessageNotice(userId, sn, IManager.LEVEL_ALL);
			//공간알림 카운트를 증가시킨다.
			SmartUtil.increaseNoticeCountByNoticeType((String)targetUserId.get(i), Notice.TYPE_NOTIFICATION);
		}
	}
	
	private void populatePrivateGroupAuth(String userId, TskTask obj) throws Exception {
		if (obj == null)
			return;
		String workSpaceId = obj.getWorkSpaceId();
		if (CommonUtil.isEmpty(workSpaceId))
			return;
		if (workSpaceId.indexOf("group_") == -1)
			return;
		SwoGroup group = SwManagerFactory.getInstance().getSwoManager().getGroup(userId, workSpaceId, IManager.LEVEL_LITE);
		if (group == null)
			return;
		String groupType = group.getGroupType();
		if (groupType.equalsIgnoreCase(SwoGroup.GROUP_TYPE_PUBLIC))
			return;

		if (!obj.getAccessLevel().equalsIgnoreCase("1")) {
			obj.setAccessLevel("2");
			obj.setAccessValue(workSpaceId);
		}
//		StringBuffer accessValueBuf = new StringBuffer();
//		SwoGroupMember[] member = group.getSwoGroupMembers();
//		if (member == null || member.length == 0) {
//			obj.setAccessValue(userId);
//		} else {
//			boolean isFirst = true;
//			for (int i = 0; i < member.length; i++) {
//				String memberId = member[i].getUserId();
//				
//				accessValueBuf.append("")
//			}
//			
//			
//		}
		
	}
	private void populatePrivateGroupAuth(String userId, SwdRecord obj) throws Exception {
		if (obj == null)
			return;
		String workSpaceId = obj.getWorkSpaceId();
		if (CommonUtil.isEmpty(workSpaceId))
			return;
		if (workSpaceId.indexOf("group_") == -1)
			return;
		SwoGroup group = SwManagerFactory.getInstance().getSwoManager().getGroup(userId, workSpaceId, IManager.LEVEL_LITE);
		if (group == null)
			return;
		String groupType = group.getGroupType();
		if (groupType.equalsIgnoreCase(SwoGroup.GROUP_TYPE_PUBLIC))
			return;

		if (!obj.getAccessLevel().equalsIgnoreCase("1")) {
			obj.setAccessLevel("2");
			obj.setAccessValue(workSpaceId);
		}
//		StringBuffer accessValueBuf = new StringBuffer();
//		SwoGroupMember[] member = group.getSwoGroupMembers();
//		if (member == null || member.length == 0) {
//			obj.setAccessValue(userId);
//		} else {
//			boolean isFirst = true;
//			for (int i = 0; i < member.length; i++) {
//				String memberId = member[i].getUserId();
//				
//				accessValueBuf.append("")
//			}
//			
//			
//		}
		
	}
	private void populateAutoIndexField(String userId, String type, String formId, String instanceId, SwdRecord record) throws Exception {
		Map infoMap = new HashMap();
		if (type.equalsIgnoreCase(TskTask.TASKTYPE_COMMON)) {
			infoMap.put("instanceId", instanceId);
			infoMap.put("refType", TskTask.TASKTYPE_COMMON);
		} else {
			infoMap.put("instanceId", instanceId);
			infoMap.put("refType", TskTask.TASKTYPE_SINGLE);
		}
		
		SwfForm form = SwManagerFactory.getInstance().getSwfManager().getForm(userId, formId);
		SwfField[] fields = form.getFields();
		for (int i = 0; i < fields.length; i++) {
			SwfField field = fields[i];
			SwdDataField dataField = record.getDataField(field.getId());
			SwdDataField tempDataField = getAutoIndexSwdDataField(userId, form, field, record, "save", infoMap, false);
			if (tempDataField != null)
				record.setDataField(dataField.getId(), tempDataField);
		}
	}
	
	@Override
	public void removeInformationWorkInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		try{
			String workId = (String)requestBody.get("workId");
			String instanceId = (String)requestBody.get("instanceId");
			String repeatEventId = (String)requestBody.get("repeatEventId");
			

			User user = SmartUtil.getCurrentUser();
			SwfFormCond swfFormCond = new SwfFormCond();
			swfFormCond.setCompanyId(user.getCompanyId());
			swfFormCond.setPackageId(workId);
	
			SwfForm[] swfForms = getSwfManager().getForms(user.getId(), swfFormCond, IManager.LEVEL_LITE);
	
			SwdRecordCond swdRecordCond = new SwdRecordCond();
			swdRecordCond.setPackageId(workId);
			swdRecordCond.setFormId(swfForms[0].getId());
			if(SmartUtil.isBlankObject(repeatEventId)){
				swdRecordCond.setRecordId(instanceId);				
			}else{				
				String tableColName = getSwdManager().getTableColName(swfForms[0].getId(), FormField.ID_NUM_REPEAT_EVENT_ID);
				if(tableColName != null){
					Filter filter = new Filter();
					filter.setLeftOperandType(Filter.OPERANDTYPE_STRING);
					filter.setLeftOperandValue(tableColName);
					filter.setOperator("=");
					filter.setRightOperandType(Filter.OPERANDTYPE_STRING);
					filter.setRightOperandValue(repeatEventId);
					swdRecordCond.setFilter(new Filter[]{filter});
				}else{
					swdRecordCond.setRecordId(instanceId);									
				}				
			}

	
			//getSwdManager().removeRecord(user.getId(), swdRecordCond);
			
			// 삭제할 레코드 조회
			SwdRecord[] records = getSwdManager().getRecords(user.getId(), swdRecordCond, IManager.LEVEL_LITE);
			if (records == null || records.length == 0)
				return;
			
			int recordSize = (SmartUtil.isBlankObject(repeatEventId)) ? 1 : records.length;
			for(int i=0; i<recordSize; i++){
				// 삭제할 도메인 아이디 조회
				SwdRecord record = records[i];
				String domainId = record.getDomainId();
				if (domainId == null) {
					SwdDomainCond domainCond = new SwdDomainCond();
					domainCond.setFormId(record.getFormId());
					SwdDomain domain = getSwdManager().getDomain(user.getId(), domainCond, IManager.LEVEL_LITE);
					domainId = domain.getObjId();
				}
	
				getSwdManager().removeRecord(user.getId(), record.getDomainId(), record.getRecordId());
				
				AutoIndexInstCond cond = new AutoIndexInstCond();
				cond.setInstanceId(record.getRecordId());
				AutoIndexInst[] autoIndex = SwManagerFactory.getInstance().getAutoIndexManager().getAutoIndexInsts(user.getId(), cond, null);
				if (autoIndex != null) {
					for (int j = 0; j < autoIndex.length; j++) {
						SwManagerFactory.getInstance().getAutoIndexManager().removeAutoIndexInst(user.getId(), autoIndex[j].getObjId());
					}
				}
				removeMessageNotice(workId, instanceId);
			}			
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

//			Opinion opinion = new Opinion();
//			opinion.setObjId(commentId);
//
//			getOpinionManager().setOpinion(userId, opinion, IManager.LEVEL_ALL);
			
			OpinionCond opinionCond = new OpinionCond();
			opinionCond.setObjId(commentId);
			
			Opinion opinion = getOpinionManager().getOpinion(userId, opinionCond, IManager.LEVEL_ALL);
			if (opinion == null)
				return;
			String refCommentId = opinion.getObjId();
			getOpinionManager().removeOpinion(userId, opinion.getObjId());
			//알림이 있다면 알림도 지운다
			

//			PublishNotice pubNoticeObj = new PublishNotice(tskTask.getAssignee(), PublishNotice.TYPE_COMMENT, PublishNotice.REFTYPE_COMMENT_INFORWORK, opinion.getObjId());
//			getPublishNoticeManager().setPublishNotice("linkadvisor", pubNoticeObj, IManager.LEVEL_ALL);
//			
			PublishNoticeCond pubNoticeCond = new PublishNoticeCond();
			pubNoticeCond.setRefId(refCommentId);
			PublishNotice pubNotice = getPublishNoticeManager().getPublishNotice(userId, pubNoticeCond, null);
			if (pubNotice == null)
				return;
			String pubTargetUser = pubNotice.getAssignee();
			getPublishNoticeManager().removePublishNotice(userId, pubNotice.getObjId());
			
			SmartUtil.increaseNoticeCountByNoticeType(pubTargetUser, Notice.TYPE_COMMENT);
			
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
			List<Map<String, String>> departments = null;
			Map<String, List<Map<String, String>>> fileGroupMap = new HashMap<String, List<Map<String,String>>>();
			String groupId = null;
			while (itr.hasNext()) {
				String fieldId = (String)itr.next();
				String value = null;
				String refForm = null;
				String refFormField = null;
				String refRecordId = null;
				SwdField fieldTemp = fieldInfoMap.get(fieldId);
				
				String formFieldType = fieldTemp.getFormFieldType();
				
				if (formFieldType.equalsIgnoreCase("complex")) {
					
//					Map map = (LinkedHashMap)smartFormInfoMap.get(fieldId);
//					if(map.size() == 0)
//						continue;
//					//42={gridDatas=[{45=aa, 46=SET, 47=, 48=},{45=11, 46=SET, 47=, 48=}]}
//					
//					Map<String, ArrayList> gridFieldResultDataMap = new HashMap<String, ArrayList>();
//					
//					List inputGridDataList = (ArrayList)map.get("gridDatas");
//					
//					for (int i = 0; i < inputGridDataList.size(); i++) {
//						Map inputGridDataMap = (LinkedHashMap)inputGridDataList.get(i);
//						
//						Iterator gridFieldIdItr = inputGridDataMap.keySet().iterator();
//						while (gridFieldIdItr.hasNext()) {
//							String gridFieldId = (String)gridFieldIdItr.next();
//							List gridFieldResultDataList = gridFieldResultDataMap.get(gridFieldId);
//							if (gridFieldResultDataList == null) {
//								ArrayList tempList = new ArrayList();
//								tempList.add(inputGridDataMap.get(gridFieldId));
//								gridFieldResultDataMap.put(gridFieldId, tempList);
//							} else {
//								gridFieldResultDataList.add(inputGridDataMap.get(gridFieldId));
//							}
//						}
//					}
					
					
					SwdDataField fieldData = new SwdDataField();
					fieldData.setId(fieldId);
					fieldData.setName(fieldInfoMap.get(fieldId).getFormFieldName());
					fieldData.setRefForm(refForm);
					fieldData.setRefFormField(refFormField);
					fieldData.setRefRecordId(refRecordId);
					
					value = JsonUtil.getJsonStringByMap((LinkedHashMap)smartFormInfoMap.get(fieldId));
					
					value = StringUtils.replace(value, "﻿", "");// 에디터로 작성된 내용중 자판에 없는 특수문자가 하나 숨어 들어온다 그문자를 제거하는 소스
					fieldData.setValue(value);

					fieldDataList.add(fieldData);
					
				} else {
					if (formFieldType.equalsIgnoreCase("boolean")) {
						value = "false";
					}
					
					Object fieldValue = smartFormInfoMap.get(fieldId);
					String autoIndexSelectedValue = null;
					if (fieldValue instanceof LinkedHashMap) {
						Map<String, Object> valueMap = (Map<String, Object>)fieldValue;
						groupId = (String)valueMap.get("groupId");
						refForm = (String)valueMap.get("refForm");
						String autoIndexValue = (String)valueMap.get("value");
						autoIndexSelectedValue = (String)valueMap.get("selectedValue");
						users = (ArrayList<Map<String,String>>)valueMap.get("users");
						departments = (ArrayList<Map<String,String>>)valueMap.get("departments");
		
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
							} else {
								//TODO 데모를 위한 코딩 변경 필요!!!!!!
								if (CommonUtil.isEmpty(value)) {
									if (!CommonUtil.isEmpty(autoIndexValue)) {
										value = autoIndexValue;
									}
								}
							}
//							SwoDepartmentCond swoDepartmentCond = new SwoDepartmentCond();
//							swoDepartmentCond.setId(refRecordId);
//							String deptName = "";
//							SwoDepartment swoDepartment = getSwoManager().getDepartment(userId, swoDepartmentCond, IManager.LEVEL_LITE);
//							if(swoDepartment != null)
//								deptName = swoDepartment.getName();
//							value = deptName;
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
						} else if(!CommonUtil.isEmpty(departments)) {
							refForm = "frm_depart_SYSTEM";
							refFormField = "4"; 
							String resultRefRecordId = "";
							String resultValue = "";
							String symbol = ";";
							if(departments.size() == 1) {
								resultRefRecordId = departments.get(0).get("id");
								resultValue = departments.get(0).get("name");
							} else {
								for(int i=0; i < departments.subList(0, departments.size()).size(); i++) {
									Map<String, String> department = departments.get(i);
									resultRefRecordId += department.get("id") + symbol;
									resultValue += department.get("name") + symbol;
								}
							}
							refRecordId = resultRefRecordId;
							value = resultValue;
						} else if(!CommonUtil.isEmpty(autoIndexValue)) {
							value = autoIndexValue;
						}
					} else if(fieldValue instanceof String) {
						
						if (fieldTemp.getFormFieldType().equalsIgnoreCase("boolean")) {
							String tempValue = (String)smartFormInfoMap.get(fieldId);
							if (tempValue == null || tempValue.equalsIgnoreCase("off") || tempValue.equalsIgnoreCase("false")) {
								value = "false";
							} else if (tempValue.equalsIgnoreCase("on") || tempValue.equalsIgnoreCase("true")) {
								value = "true";
							}
						} else {
							value = (String)smartFormInfoMap.get(fieldId);
						}
						
						if(formId.equals(SmartForm.ID_MEMO_MANAGEMENT)) {
							if(fieldId.equals("12"))
								value = StringUtil.subString(value, 0, 20, "...");
						} else if(formId.equals(SmartForm.ID_EVENT_MANAGEMENT)) {
							if(fieldId.equals("1") || fieldId.equals("2")) {
//								if(!value.isEmpty())
//									value = LocalDate.convertStringToLocalDate(value).toGMTDateString();
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

					//unescape
					if (fieldInfoMap.get(fieldId).getFormFieldType().equalsIgnoreCase("text")) {
						//value = StringUtil.unescape(value);
						value = SmartUtil.smartDecode(value);
						value = StringUtils.replace(value, "&nbsp;", "<span class=\"Apple-tab-span\" style=\"white-space:pre\"> </span>");
					}
					value = StringUtils.replace(value, "﻿", "");// 에디터로 작성된 내용중 자판에 없는 특수문자가 하나 숨어 들어온다 그문자를 제거하는 소스
					fieldData.setValue(value);
					if (!CommonUtil.isEmpty(autoIndexSelectedValue))
						fieldData.setSelectedValue(autoIndexSelectedValue);

					fieldDataList.add(fieldData);
				}
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
	private void setReferenceApprovalToRecord(String userId, SwdRecord obj, Map<String, Object> requestBody) throws Exception {

		Map<String, Object> frmTaskForwardMap = (Map<String, Object>)requestBody.get("frmTaskForward");
		Map<String, Object> frmApprovalLine = (Map<String, Object>)requestBody.get("frmApprovalLine");
		
		List<Map<String, String>> users = null;
		
		if (frmTaskForwardMap != null) {
			
			Set<String> keySet = frmTaskForwardMap.keySet();
			Iterator itr = keySet.iterator();

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
		
		} else if (frmApprovalLine != null) {
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
						aprAprDefs[i].setDueDate("" + (aprs[i].getMeanTimeMinutes() + aprs[i].getMeanTimeHours()*60 + aprs[i].getMeanTimeDays()*60*24));
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
						aprAprDefs[i].setDueDate("" + (aprs[i].getMeanTimeMinutes() + aprs[i].getMeanTimeHours()*60 + aprs[i].getMeanTimeDays()*60*24));
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
					

					if (hdnApprovalLineId.equalsIgnoreCase("system.approvalLine.default.3level") || hdnApprovalLineId.equalsIgnoreCase("system.approvalLine.default.2level")) {
						apr.setName(aprAprDefs[i-1].getName());
					} else {
						apr.setName(aprAprDefs[i-1].getAprName());
					}
					
					apr.setType(aprAprDefs[i-1].getType());
					apr.setApprover(id);
					apr.setMandatory(true);
					apr.setModifiable(true);
					apr.setCreationDate(new LocalDate());
					apr.setCreationUser(id);
					apr.setDueDate(aprAprDefs[i-1].getDueDate());
					
					approvals[i-1] = apr;
				}
				apprLine.setApprovals(approvals);
				apprLine.setExtendedPropertyValue("recordId", obj.getRecordId());
				apprLine.setExtendedPropertyValue("txtApprovalComments", txtApprovalComments);
				apprLine.setExtendedPropertyValue("txtApprovalSubject", txtApprovalSubject);
				
				//참조한 approvallindef 가 있다면 그아이디를 입력한다
				apprLine.setRefAppLineDefId(hdnApprovalLineId);					

				obj.setExtendedAttributeValue("txtApprovalSubject", txtApprovalSubject);
				obj.setExtendedAttributeValue("txtApprovalComments", txtApprovalComments);
				obj.setExtendedAttributeValue("refAppLineDefId", hdnApprovalLineId);
				
				getAprManager().setApprovalLine(userId, apprLine, IManager.LEVEL_ALL);
				obj.setExtendedAttributeValue("approvalLine", apprLine.getObjId());
				

				//TODO 전자결재 참조업무 생성
				Map<String, Object> txtApprovalForwardee = (Map<String, Object>)frmTaskApproval.get("txtApprovalForwardee");
				if (txtApprovalForwardee != null) {
					ArrayList<Map<String, String>> forwardee = (ArrayList<Map<String,String>>)txtApprovalForwardee.get("users");
					
					String isLazyReferenceTask = (String)frmTaskApproval.get("isLazyReferenceTask");
					
					if (CommonUtil.isEmpty(isLazyReferenceTask)) {
						isLazyReferenceTask = "false";
					} else if (isLazyReferenceTask.equalsIgnoreCase("on")) {
						isLazyReferenceTask = "true";
					} else {
						isLazyReferenceTask = "false";
					}
					
					String txtForwardForwardee = null;
					if(!CommonUtil.isEmpty(forwardee)) {
						String symbol = ";";
						if(forwardee.size() == 1) {
							txtForwardForwardee = forwardee.get(0).get("id");
						} else {
							txtForwardForwardee = "";
							for(int i=0; i < forwardee.subList(0, forwardee.size()).size(); i++) {
								Map<String, String> user = forwardee.get(i);
								if (user.get("id").equalsIgnoreCase(userId))
									continue;
								txtForwardForwardee += user.get("id") + symbol;
							}
						}
					}
					obj.setExtendedAttributeValue("txtForwardSubject", txtApprovalSubject);
					obj.setExtendedAttributeValue("txtForwardForwardee", txtForwardForwardee);
					obj.setExtendedAttributeValue("txtForwardComments", txtApprovalComments);
					obj.setExtendedAttributeValue("isLazyReferenceTask", isLazyReferenceTask);
				}
			}
		}
	}
	private void setReferenceApprovalToTask(String userId, TskTask obj, Map<String, Object> requestBody) throws Exception {
		
		Map<String, Object> frmTaskForwardMap = (Map<String, Object>)requestBody.get("frmTaskForward");
		Map<String, Object> frmApprovalLine = (Map<String, Object>)requestBody.get("frmApprovalLine");
		
		List<Map<String, String>> users = null;
		
		if (frmTaskForwardMap != null) {
			
			Set<String> keySet = frmTaskForwardMap.keySet();
			Iterator itr = keySet.iterator();

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
			
			obj.setExtendedAttributeValue("subject", txtForwardSubject);
			obj.setExtendedAttributeValue("referenceUser", txtForwardForwardee);
			obj.setExtendedAttributeValue("workContents", txtForwardComments);
			
			obj.setExtendedPropertyValue("subject", txtForwardSubject);
			obj.setExtendedPropertyValue("referenceUser", txtForwardForwardee);
			obj.setExtendedPropertyValue("workContents", txtForwardComments);
			
		} else if (frmApprovalLine != null) {
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
//			Map<String, Object> txtApprovalForwardee = (Map<String, Object>)requestBody.get("txtApprovalForwardee");
//			ArrayList<Map<String, String>> forwardee = (ArrayList<Map<String,String>>)txtApprovalForwardee.get("users");
			
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
						aprAprDefs[i].setDueDate("" + (aprs[i].getMeanTimeMinutes() + aprs[i].getMeanTimeHours()*60 + aprs[i].getMeanTimeDays()*60*24));
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
						aprAprDefs[i].setDueDate("" + (aprs[i].getMeanTimeMinutes() + aprs[i].getMeanTimeHours()*60 + aprs[i].getMeanTimeDays()*60*24));
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
					if (hdnApprovalLineId.equalsIgnoreCase("system.approvalLine.default.3level") || hdnApprovalLineId.equalsIgnoreCase("system.approvalLine.default.2level")) {
						apr.setName(aprAprDefs[i-1].getName());
					} else {
						apr.setName(aprAprDefs[i-1].getAprName());
					}
					apr.setType(aprAprDefs[i-1].getType());
					apr.setApprover(id);
					apr.setMandatory(true);
					apr.setModifiable(true);
					apr.setCreationDate(new LocalDate());
					apr.setCreationUser(id);
					apr.setDueDate(aprAprDefs[i-1].getDueDate());
					
					approvals[i-1] = apr;
				}

//				LocalDate now = new LocalDate();
//				if(approvals!=null && approvals.length>0 && !SmartUtil.isBlankObject(approvals[0].getDueDate())){
//					int dueDate = Integer.parseInt(approvals[0].getDueDate());
//					approvals[0].setExpectEndDate(new LocalDate(now.getTime() + dueDate*LocalDate.ONE_MINUTE));
//					obj.setExpectEndDate(new LocalDate(approvals[0].getExpectEndDate().getTime()));
//				}else{
//					approvals[0].setExpectEndDate(new LocalDate(now.getTime() + 30*LocalDate.ONE_MINUTE));					
//				}
				
				apprLine.setApprovals(approvals);
				apprLine.setExtendedPropertyValue("recordId", obj.getObjId());
				apprLine.setExtendedPropertyValue("txtApprovalComments", txtApprovalComments);
				apprLine.setExtendedPropertyValue("txtApprovalSubject", txtApprovalSubject);
				
				//참조한 approvallindef 가 있다면 그아이디를 입력한다
				apprLine.setRefAppLineDefId(hdnApprovalLineId);					

				obj.setExtendedPropertyValue("txtApprovalSubject", txtApprovalSubject);
				obj.setExtendedPropertyValue("txtApprovalComments", txtApprovalComments);
				obj.setExtendedPropertyValue("refAppLineDefId", hdnApprovalLineId);
				
				getAprManager().setApprovalLine(userId, apprLine, IManager.LEVEL_ALL);
				obj.setExtendedPropertyValue("approvalLine", apprLine.getObjId());
				
				obj.setIsApprovalSourceTask("true");
				obj.setTargetApprovalStatus(AprApproval.APPROVAL_STATUS_RUNNING + "");
				
			}
		}
	}
	@Override
	public String startProcessWorkInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		/*{
			"workId":"pkg_cf3b0087995f4f99a41c93e2fe95b22d",
			"formId":"frm_c19b1fe4bceb4732acbb8a4cd2a57474",
			"formName":"기안품의",
			 frmTaskForward=   {
							      txtForwardSubject=전달 제목입니다.,
							      txtForwardComments=전달 내용입니다.,
							      txtForwardForwardee= {
												         users= [
														            {
														               id=kmyu@maninsoft.co.kr,
														               name=선임 유광민
														            }
												         		]
							      						}
		     },
		        frmApprovalLine=   {
								      hdnApprovalLineId=system.approvalLine.default.3level,
								      usrLevelApprover1=kmyu@maninsoft.co.kr,
								      usrLevelApprover2=kmyu@maninsoft.co.kr,
								      usrLevelApprover3=kmyu@maninsoft.co.kr
								   },
			   frmTaskApproval=   {
			      txtApprovalSubject=전자 결재 제목,
			      txtApprovalComments=전자결재 제목,
			      txtApprovalForwardee=      {
			         users=         [
			            {
			               id=kmyu@maninsoft.co.kr,
			               name=선임 유광민
			            }
			         ]
			      }
			   },
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
			
			boolean isTempSave = CommonUtil.toBoolean((Boolean)requestBody.get("isTempSave"));
			String tempSavedId = (String)requestBody.get("tempSavedId");
			if (isTempSave) {
				return this.saveTempWorkInstance(userId, tempSavedId, requestBody, request, false);
			} else if (!CommonUtil.isEmpty(tempSavedId)) {
				getTskManager().removeTask(userId, tempSavedId);
			}
			
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
			
			TskTaskDef taskDef = null;
			String taskDefId = null;
			if (!CommonUtil.isEmpty(formId)) {
				//formId 로 실행할 태스크를 조회한다
				TskTaskDefCond tskDefCond = new TskTaskDefCond();
				tskDefCond.setForm(formId);
				TskTaskDef[] taskDefs = SwManagerFactory.getInstance().getTskManager().getTaskDefs(userId, tskDefCond, IManager.LEVEL_ALL);
				if (taskDefs == null || taskDefs.length == 0)
					throw new Exception("Not Exist Task Definition : formId = " + formId);
				taskDef = taskDefs[0];
				taskDefId = taskDef.getObjId();
			} else {
				//패키지 정보로 프로세스 첫번째 taskdef를 찾는다
				Property[] extProps = new Property[] {new Property("processId", processId), new Property("startActivity", "true")};
				TskTaskDefCond taskCond = new TskTaskDefCond();
				taskCond.setExtendedProperties(extProps);
				TskTaskDef[] taskDefs = getTskManager().getTaskDefs(userId, taskCond, IManager.LEVEL_ALL);
				if (CommonUtil.isEmpty(taskDefs))
					throw new Exception(new StringBuffer("No start activity. -> processId:").append(processId).toString());
				taskDef = taskDefs[0];
				taskDefId = taskDef.getObjId();
			}
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
			
			String title = null;
			if (!CommonUtil.isEmpty(taskDef.getExtendedPropertyValue("subjectFieldId"))) {
				title = CommonUtil.toNotNull(recordObj.getDataFieldValue(taskDef.getExtendedPropertyValue("subjectFieldId")));
			} else {
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

				if(String.valueOf(AccessPolicy.LEVEL_CUSTOM).equals(accessLevel) && CommonUtil.isEmpty(accessValue)) {
					accessValue = ModelConverter.getAccessValue(userId, processId);
				}

				task.setWorkSpaceId(workSpaceId);
				task.setWorkSpaceType(workSpaceType);
				task.setAccessLevel(accessLevel);
				task.setAccessValue(accessValue);
				
				//프로세스업무의 시작 공간 위치가 비공개 그룹이라면 해당 업무는 비공개 그룹원들에게만 공개가 된다
				populatePrivateGroupAuth(userId, task);
				
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
			

			//참조자, 전자결재, 연결업무 정보를 셋팅한다
			setReferenceApprovalToTask(userId, task, requestBody);
			
			//UCITY ucityAdvisor에서 사용할 값을 셋팅한다 
			setUcityExtendedProperty(requestBody, task);
			
			//태스크를 실행하며 프로세스업무를 실행한다
			task = getTskManager().executeTask(userId, task, "execute");
			
			//공간 알림에 값을 추가한다.
			this.populateSpaceNotice(task);

			//자동채번을 위하여 아래를 호출한다
			populateAutoIndexField(userId, TskTask.TASKTYPE_COMMON, formId, task.getProcessInstId(), recordObj);
			
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
	public TaskInstanceInfo[] getSubInstancesInForward(String forwardId, int length, LocalDate to) throws Exception {
		try{
			if (CommonUtil.isEmpty(forwardId)) 
				return null;
			
			User cuser = SmartUtil.getCurrentUser();
			String userId = null;
			if (cuser != null)
				userId = cuser.getId();
						
			TaskInstanceInfo[] subInstancesInInstances = null;
			List<TaskInstanceInfo> instanceInfoList = new ArrayList<TaskInstanceInfo>();
			
			TskTaskCond cond = new TskTaskCond();
			cond.setForwardId(forwardId);
			cond.setModificationDateTo(to);
			long tasksSize = getTskManager().getTaskSize(userId, cond);
			if (tasksSize != 0) {
				
				TaskWorkCond workCond = new TaskWorkCond();
				workCond.setTskForwardId(forwardId);
				workCond.setOrders(new Order[]{new Order("taskLastModifyDate", false)});
				workCond.setPageSize(length);
				workCond.setTskModifyDateTo(to);
				TaskWork[] tasks = getWorkListManager().getTaskWorkList(userId, workCond, true);
				
				List<String> prcInstIdList = new ArrayList<String>();
				if(!CommonUtil.isEmpty(tasks)) {
					int i = (tasks.length>length) ? tasks.length-length : 0;
					for (; i < tasks.length; i++) {
						TaskWork task = tasks[i];
						instanceInfoList.add(ModelConverter.getTaskInstanceInfo(cuser, task));
					}
				}
			}

			if(instanceInfoList.size() > 0) {
				Collections.sort(instanceInfoList);
				if(tasksSize>length) instanceInfoList.add(null);
				subInstancesInInstances = new TaskInstanceInfo[instanceInfoList.size()];
				instanceInfoList.toArray(subInstancesInInstances);
			}
			return subInstancesInInstances;

		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}

	@Override
	public int getSubInstancesInForwardCount(String forwardId) throws Exception {
		try{
			if (CommonUtil.isEmpty(forwardId)) 
				return 0;
			
			User cuser = SmartUtil.getCurrentUser();
			String userId = null;
			if (cuser != null)
				userId = cuser.getId();
			
			TskTaskCond cond = new TskTaskCond();
			cond.setForwardId(forwardId);
			long tasksSize = getTskManager().getTaskSize(userId, cond);

			return (int)(tasksSize);
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return 0;			
			// Exception Handling Required			
		}
	}

	@Override
	public InstanceInfo[] getSubInstancesInInstance(String instanceId, int length, LocalDate to) throws Exception {
		try{
			if (CommonUtil.isEmpty(instanceId)) 
				return null;
			
			User cuser = SmartUtil.getCurrentUser();
			String userId = null;
			if (cuser != null)
				userId = cuser.getId();
						
			InstanceInfo[] subInstancesInInstances = null;
			List<InstanceInfo> instanceInfoList = new ArrayList<InstanceInfo>();
			
			TskTaskCond cond = new TskTaskCond();
			cond.setWorkSpaceId(instanceId);
			cond.setStatus(TskTask.TASKSTATUS_COMPLETE);
			long tasksSize = getTskManager().getTaskSize(userId, cond);
			if (tasksSize != 0) {
				
				TaskWorkCond workCond = new TaskWorkCond();
				workCond.setOrders(new Order[]{new Order("taskLastModifyDate", false)});
				workCond.setPageSize(length);
				workCond.setTskWorkSpaceId(instanceId);
				workCond.setTskStatus(TskTask.TASKSTATUS_COMPLETE);
				workCond.setTskModifyDateTo(to);
				TaskWork[] tasks = getWorkListManager().getTaskWorkList(userId, workCond);
				
				List<String> prcInstIdList = new ArrayList<String>();
				if(!CommonUtil.isEmpty(tasks)) {
					int i = (tasks.length>length) ? tasks.length-length : 0;
					for (; i < tasks.length; i++) {
						TaskWork task = tasks[i];
//							if (instanceInfoList.size() == 10)
//								break;
//						if (prcInstIdList.contains(task.getTskPrcInstId()))
//							continue;
//						prcInstIdList.add(task.getTskPrcInstId());
						instanceInfoList.add(ModelConverter.getWorkInstanceInfoByTaskWork(cuser, task));
					}
				}
			}

			OpinionCond opinionCond = new OpinionCond();
			opinionCond.setRefId(instanceId);
			opinionCond.setModificationDateTo(to);

			long opinionsSize = getOpinionManager().getOpinionSize(userId, opinionCond);
			
			if (opinionsSize != 0) {

				//if(length == WorkInstance.DEFAULT_SUB_INSTANCE_FETCH_COUNT)
				opinionCond.setPageSize(length);
				opinionCond.setOrders(new Order[]{new Order(Opinion.A_CREATIONDATE, false)});
				
				Opinion[] opinions = getOpinionManager().getOpinions(userId, opinionCond, IManager.LEVEL_ALL);
				if(!CommonUtil.isEmpty(opinions)) {
					int opinionLength = opinions.length;
					int i = (opinionLength>length) ? opinionLength-length : 0;
					for(; i<opinionLength; i++) {
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
				int moreInstances = ((tasksSize + opinionsSize) > length && instanceInfoList.size()==length) ? 1 : 0;
				Collections.sort(instanceInfoList);
				subInstancesInInstances = new InstanceInfo[instanceInfoList.size()+moreInstances];
				instanceInfoList.toArray(subInstancesInInstances);
			}

			if(!CommonUtil.isEmpty(subInstancesInInstances)) {
				if(length != -1 && subInstancesInInstances.length > length) {
					List<InstanceInfo> resultInstanceInfoList = new ArrayList<InstanceInfo>();
					for(int i=subInstancesInInstances.length-length; i<subInstancesInInstances.length; i++) {
						InstanceInfo instanceInfo = subInstancesInInstances[i];
						resultInstanceInfoList.add(instanceInfo);
					}
					resultInstanceInfoList.add(null);
					subInstancesInInstances = new InstanceInfo[resultInstanceInfoList.size()];
					resultInstanceInfoList.toArray(subInstancesInInstances);
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

	@Override
	public InstanceInfo[] getRecentSubInstancesInInstance(String instanceId, int length) throws Exception {
		return getSubInstancesInInstance(instanceId, length, null);
	}

	@Override
	public int getSubInstancesInInstanceCount(String instanceId) throws Exception {
		try{
			if (CommonUtil.isEmpty(instanceId)) 
				return 0;
			
			User cuser = SmartUtil.getCurrentUser();
			String userId = null;
			if (cuser != null)
				userId = cuser.getId();
			
			TskTaskCond cond = new TskTaskCond();
			cond.setWorkSpaceId(instanceId);
			cond.setStatus(TskTask.TASKSTATUS_COMPLETE);
			long tasksSize = getTskManager().getTaskSize(userId, cond);

			OpinionCond opinionCond = new OpinionCond();
			opinionCond.setRefId(instanceId);

			long opinionsSize = getOpinionManager().getOpinionSize(userId, opinionCond);
			

			return (int)(tasksSize + opinionsSize);
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return 0;			
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
			if(workId.equals(SmartWork.ID_BOARD_MANAGEMENT) || workId.equals(SmartWork.ID_EVENT_MANAGEMENT))
				swdRecordCond.setWorkSpaceIdIns(workSpaceIdIns);

			String[] groupIdsByNotBelongToClosedGroup = ModelConverter.getGroupIdsByNotBelongToClosedGroup(user);
			swdRecordCond.setWorkSpaceIdNotIns(groupIdsByNotBelongToClosedGroup);

			swdRecordCond.setLikeAccessValues(workSpaceIdIns);

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

				iWInstanceInfo.setWorkSpaceInfo(workSpaceInfo);

				WorkCategoryInfo groupInfo = null;
				if (!CommonUtil.isEmpty(swdRecordExtends[0].getSubCtgId()))
					groupInfo = new WorkCategoryInfo(swdRecordExtends[0].getSubCtgId(), swdRecordExtends[0].getSubCtg());
	
				WorkCategoryInfo categoryInfo = new WorkCategoryInfo(swdRecordExtends[0].getParentCtgId(), swdRecordExtends[0].getParentCtg());
	
				WorkInfo workInfo = new SmartWorkInfo(formId, formName, SmartWork.TYPE_INFORMATION, groupInfo, categoryInfo);

				iWInstanceInfo.setWorkInfo(workInfo);
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
													value = localDateValue.toLocalDateSimpleString();
											}
										} else if(formatType.equals(FormField.TYPE_TIME)) {
											LocalDate localDateValue = null;
											if(value != null) {
												localDateValue = LocalDate.convertGMTTimeStringToLocalDate(value);
												if(localDateValue != null)
													value = localDateValue.toLocalTimeSimpleString();
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
	
	//프로세스인스턴스안의 실행중인 태스크가 지연처리라면 프로세스인스턴스의 상태도 지연처리다
		private boolean isDelayedProcessInstanceWithSetRunningTasks(String userId, String prcInstId, PWInstanceInfo pworkInfo) throws Exception {
			if (prcInstId == null)
				return false;		
			
			//실행중이 태스크들을 구한다
			TskTaskCond tskCond = new TskTaskCond();
			tskCond.setStatus(TskTask.TASKSTATUS_ASSIGN);
			tskCond.setProcessInstId(prcInstId);
			tskCond.setType(TskTask.TASKTYPE_COMMON);
			
			TskTask[] tasks = getTskManager().getTasks(userId, tskCond, IManager.LEVEL_LITE);
			if (tasks == null || tasks.length == 0)
				return false;
			
			pworkInfo.setRunningTasks(ModelConverter.getTaskInstanceInfoArrayByTskTaskArray(pworkInfo, tasks));
			
			for (int i = 0; i < tasks.length; i++) {
				TskTask task = tasks[i];
				
				//GMT 시간임
				Date expactEndDate = task.getExpectEndDate();
				long expactEndDateTime = expactEndDate.getTime();
				
				Date now = new Date();
//				System.out.println("GMT : " + TimeZone.getDefault().getRawOffset());
				long nowTime = now.getTime() - TimeZone.getDefault().getRawOffset();
				
				if (expactEndDateTime < nowTime)
					return true;
			}
			return false;
		}
	
	//프로세스인스턴스안의 실행중인 태스크가 지연처리라면 프로세스인스턴스의 상태도 지연처리다
	private boolean isDelayedProcessInstance(String userId, String prcInstId) throws Exception {
		if (prcInstId == null)
			return false;		
		
		//실행중이 태스크들을 구한다
		TskTaskCond tskCond = new TskTaskCond();
		tskCond.setStatus(TskTask.TASKSTATUS_ASSIGN);
		tskCond.setProcessInstId(prcInstId);
		tskCond.setType(TskTask.TASKTYPE_COMMON);
		
		TskTask[] tasks = getTskManager().getTasks(userId, tskCond, IManager.LEVEL_LITE);
		if (tasks == null || tasks.length == 0)
			return false;
		
		for (int i = 0; i < tasks.length; i++) {
			TskTask task = tasks[i];
			
			//GMT 시간임
			Date expactEndDate = task.getExpectEndDate();
			long expactEndDateTime = expactEndDate.getTime();
			
			Date now = new Date();
//			System.out.println("GMT : " + TimeZone.getDefault().getRawOffset());
			long nowTime = now.getTime() - TimeZone.getDefault().getRawOffset();
			
			if (expactEndDateTime < nowTime)
				return true;
		}
		return false;
	}

	
	public InstanceInfoList getAllUcityPWorkInstanceList(boolean runningOnly, RequestParams params, int auditId) throws Exception {
		String[] taskNames = Audit.getTaskNamesByAuditId(auditId);
		if (taskNames == null || taskNames.length == 0)
			return getAllUcityPWorkInstanceListByTaskNames(runningOnly, params, null);

		User user = SmartUtil.getCurrentUser();
		String userId = user.getId();
		
		return getAllUcityPWorkInstanceListByTaskNames(runningOnly, params, taskNames);
	}

	public InstanceInfoList getAllUcityPWorkInstanceListByTaskNames(boolean runningOnly, RequestParams params, String[] taskNames) throws Exception {
	
		try {
			User user = SmartUtil.getCurrentUser();
			String userId = user.getId();
			
			UcityWorkListCond ucityWorkListCond = new UcityWorkListCond();
			
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
						else if(rightOperandInt == Instance.STATUS_ABORTED)
							rightOperand = PrcProcessInst.PROCESSINSTSTATUS_ABORTED;
					}

					filter.setLeftOperandType(formFieldType);
					if (tableColName.equalsIgnoreCase("lastTask_tskname")) {
						tableColName = "runningTaskName";
					} else if (tableColName.equalsIgnoreCase("prcStatus")) {
						tableColName = "status";
					} else if (tableColName.equalsIgnoreCase("prcTitle")) {
						tableColName = "title";
					} else if (tableColName.equalsIgnoreCase("prcCreateDate")) {
						tableColName = "createdTime";
					} else if (tableColName.equalsIgnoreCase("lastTask_tskexecuteDate")) {
						tableColName = "modifiedTime";
					}
					
					filter.setLeftOperandValue(tableColName);
					filter.setOperator(operator);
					filter.setRightOperandType(formFieldType);
					filter.setRightOperandValue(rightOperand);
					filterList.add(filter);
				}

				Filter[] filters = new Filter[filterList.size()];
				filterList.toArray(filters);

				ucityWorkListCond.setFilter(filters);
			}

			LocalDate priviousDate = new LocalDate(new LocalDate().getTime() - LocalDate.ONE_DAY*7);
			
			String filterId = params.getFilterId();
			if(filterId != null) {
				if(filterId.equals(SearchFilter.FILTER_ALL_INSTANCES)) {
				} else if(filterId.equals(SearchFilter.FILTER_RECENT_INSTANCES)) {
					ucityWorkListCond.addFilter(new Filter(">=", FormField.ID_LAST_MODIFIED_DATE, Filter.OPERANDTYPE_DATE, priviousDate.toGMTSimpleDateString()));
				} else if(filterId.equals(SearchFilter.FILTER_RUNNING_INSTANCES)) {
					ucityWorkListCond.addFilter(new Filter("=", "status", Filter.OPERANDTYPE_STRING, PrcProcessInst.PROCESSINSTSTATUS_RUNNING));
				}
			}
			
			String searchKey = params.getSearchKey();
			ucityWorkListCond.setSearchKey(CommonUtil.toNull(searchKey));
			
			if (taskNames != null && taskNames.length != 0) {
				ucityWorkListCond.setRunningTaskNameIns(taskNames);
			}

			long totalCount = SwManagerFactory.getInstance().getUcityWorkListManager().getUcityWorkListSize(user.getId(), ucityWorkListCond);

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
				ucityWorkListCond.setPageNo(currentPage-1);

			ucityWorkListCond.setPageSize(pageSize);

			SortingField sf = params.getSortingField();

			//화면에서 사용하고 있는 컬럼의 상수값과 실제 프로세스 인스턴스 데이터 베이스의 컬럼 이름이 맞지 않아 컨버팅 작업
			//한군데에서 관리 하도록 상수로 변경 필요
			//이벤트시간으로 정렬되게 수정
			if (sf == null) {
				sf = new SortingField();
				sf.setFieldId("eventTime");
//				sf.setFieldId(FormField.ID_CREATED_DATE);
				sf.setAscending(false);
			}
			String sfColumnNameTemp = sf.getFieldId();
			
			if (sfColumnNameTemp.equalsIgnoreCase("status")) {
				sfColumnNameTemp = "status"; 
			} else if (sfColumnNameTemp.equalsIgnoreCase("subject")) {
				sfColumnNameTemp = "title";
			} else if (sfColumnNameTemp.equalsIgnoreCase("lastTask")) {
				sfColumnNameTemp = "runningTaskName"; 
			} else if (sfColumnNameTemp.equalsIgnoreCase("creator")) {
				sfColumnNameTemp = "creationUser"; 
			} else if (sfColumnNameTemp.equalsIgnoreCase("createdTime")) {
				sfColumnNameTemp = "creationDate"; 
			} else if (sfColumnNameTemp.equalsIgnoreCase("modifier")) {
				sfColumnNameTemp = "modificationUser"; 
			} else if (sfColumnNameTemp.equalsIgnoreCase("modifiedTime")) {
				sfColumnNameTemp = "modificationDate"; 
			} 
			if (CommonUtil.isEmpty(sfColumnNameTemp)){
				sfColumnNameTemp = "creationDate";
			}

			ucityWorkListCond.setOrders(new Order[]{new Order(sfColumnNameTemp, sf.isAscending())});

			// 이상종료 인 것은 조회 하지 않기
			if(runningOnly)
				ucityWorkListCond.setStatus(PrcProcessInst.PROCESSINSTSTATUS_RUNNING);
			UcityWorkList[] workLists = SwManagerFactory.getInstance().getUcityWorkListManager().getUcityWorkLists(userId, ucityWorkListCond, null);
			
			InstanceInfoList instanceInfoList = new InstanceInfoList();
			List<PWInstanceInfo> pwInstanceInfoList = new ArrayList<PWInstanceInfo>();
			PWInstanceInfo[] pWInstanceInfos = null;
			
			if (workLists != null) {
				for (int i = 0; i < workLists.length; i++) {
					UcityWorkList workList = workLists[i];
					PWInstanceInfo pworkInfo = new PWInstanceInfo();
					
					pworkInfo.setId(workList.getPrcInstId());
					pworkInfo.setCreatedDate(new LocalDate(workList.getCreationDate().getTime()));
					pworkInfo.setLastModifiedDate(new LocalDate(workList.getModificationDate().getTime()));
					pworkInfo.setLastModifier(ModelConverter.getUserInfoByUserId(workList.getModificationUser()));
					
					if (!CommonUtil.isEmpty(workList.getRunningTaskId())) {
	//					TaskWorkCond taskWorkCond = new TaskWorkCond();
	//					taskWorkCond.setTskObjId(workList.getRunningTaskId());
	//					TaskWork[] taskWork = SwManagerFactory.getInstance().getWorkListManager().getTaskWorkList(userId, taskWorkCond);
	//					pworkInfo.setLastTask(ModelConverter.getTaskInstanceInfo(user, taskWork[0]));
						TskTask lastTask = SwManagerFactory.getInstance().getTskManager().getTask(userId, workList.getRunningTaskId(), IManager.LEVEL_ALL);
						if (lastTask != null) {
							UserInfo assigneeInfo = ModelConverter.getUserInfoByUserId(lastTask.getAssignee());
							pworkInfo.setLastTask(new TaskInstanceInfo(lastTask.getObjId(), lastTask.getName(), SmartWork.TYPE_PROCESS, assigneeInfo , assigneeInfo, new LocalDate(lastTask.getModificationDate().getTime())));
							pworkInfo.setLastTaskCount(1);
						}
					}
					pworkInfo.setOwner(ModelConverter.getUserInfoByUserId(workList.getCreationUser()));
					int status = -1;
					if (workList.getStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_RUNNING)) {
						boolean isDelayedProcessInst = isDelayedProcessInstanceWithSetRunningTasks(userId, workList.getPrcInstId(), pworkInfo);
						if (isDelayedProcessInst) {
							status = Instance.STATUS_DELAYED_RUNNING;
						} else {
							status = Instance.STATUS_RUNNING;
						}
						
					} else if (workList.getStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_COMPLETE)) {
						status = Instance.STATUS_COMPLETED;
					} else if (workList.getStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_ABORTED)) {
						status = Instance.STATUS_ABORTED;
					}
					pworkInfo.setStatus(status);
					pworkInfo.setSubject(workList.getTitle());
					pworkInfo.setType(WorkInstance.TYPE_PROCESS);
//Start InstanceInfo Model Changed by ysjung
					//pworkInfo.setWork(ModelConverter.getWorkInfoByPackageId(workList.getPackageId()));
					pworkInfo.setWorkInfo(ModelConverter.getWorkInfoByPackageId(workList.getPackageId()));
					//pworkInfo.setWorkInfo(workId, workName, workType, isWorkRunning, workFullPathName);
//End InstanceInfo Model Changed by ysjung
					//pworkInfo.setWorkSpace(workSpace);
					
					Property p1 = new Property("serviceName",workList.getServiceName());
					Property p2 = new Property("eventName",workList.getEventName());
					Property p3 = new Property("type",workList.getType());
					Property p4 = new Property("externalDisplay",workList.getExternalDisplay());
					Property p5 = new Property("eventPlace",workList.getEventPlace());
					Property p6 = new Property("isSms", CommonUtil.toBoolean(workList.getIsSms())+"");
					Property p7 = new Property("eventId",workList.getEventId());
					Property p8;
					Property p9 = new Property("runningTaskName", workList.getRunningTaskName());
					if (workList.getEventTime() != null) {
						Date tempDate = new Date();
						tempDate.setTime(workList.getEventTime().getTime() + TimeZone.getDefault().getRawOffset());
						 p8 = new Property("eventTime", DateUtil.toDateString(tempDate).replace(".000", ""));
					} else {
						p8 = new Property("eventTime", null);
					}
					Property[] properties = new Property[]{p1, p2, p3, p4, p5, p6, p7, p8, p9};

					pworkInfo.setExtentedProperty(properties);
					
					pwInstanceInfoList.add(pworkInfo);
				}
			}
			if(pwInstanceInfoList.size() > 0) {
				pWInstanceInfos = new PWInstanceInfo[pwInstanceInfoList.size()];
				pwInstanceInfoList.toArray(pWInstanceInfos);
			}
			
			instanceInfoList.setInstanceDatas(pWInstanceInfos);
			instanceInfoList.setPageSize(pageSize);
			instanceInfoList.setTotalSize((int)totalCount);
			instanceInfoList.setSortedField(sf);
			instanceInfoList.setTotalPages(totalPages);
			instanceInfoList.setCurrentPage(currentPage);
			instanceInfoList.setType(InstanceInfoList.TYPE_PROCESS_INSTANCE_LIST);
			
			return instanceInfoList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}	
	
	
	
	
	
	//태스크를 조회 한후 그인스턴스아이디로 업무를 조회하는 방식(prcInstIdIns)은 인스턴스의 아이디가 많아지면 
	//prcInsId in (','','' .......) 갯수만큼 늘어나므로 성능에 영향이 크다
	//변경됨
	public InstanceInfoList getAllUcityPWorkInstanceList_old(boolean runningOnly, RequestParams params, int auditId) throws Exception {
		String[] taskNames = Audit.getTaskNamesByAuditId(auditId);
		if (taskNames == null || taskNames.length == 0)
			return getAllUcityPWorkInstanceList(runningOnly, params, null);

		User user = SmartUtil.getCurrentUser();
		String userId = user.getId();
		
		TskTaskCond taskCond = new TskTaskCond();
		taskCond.setNameIns(taskNames);
		if (runningOnly) {
			taskCond.setStatusIns(new String[]{TskTask.TASKSTATUS_ASSIGN});
		} else {
			taskCond.setStatusIns(new String[]{TskTask.TASKSTATUS_ASSIGN, TskTask.TASKSTATUS_ABORTED});
		}
		TskTask[] tasks = SwManagerFactory.getInstance().getTskManager().getTasks(userId, taskCond,IManager.LEVEL_ALL);
		
		List prcIdList = new ArrayList();
		if(tasks!=null){
			for (int i = 0; i < tasks.length; i++) {
				TskTask task = tasks[i];
				String prcInstId = task.getProcessInstId();
				if (!prcIdList.contains(prcInstId)) {
					prcIdList.add(prcInstId);
				}
			}
		}
		if (prcIdList.size() != 0) {
			String[] prcIds = new String[prcIdList.size()];
			prcIdList.toArray(prcIds);
			return getAllUcityPWorkInstanceList(runningOnly, params, prcIds);
		} else {
			return getAllUcityPWorkInstanceList(runningOnly, params, new String[]{""});
		}
	}
	
	//삭제예정
	public InstanceInfoList getAllUcityPWorkInstanceList(boolean runningOnly, RequestParams params, String[] instanceIdIns) throws Exception {
	
		try {
			User user = SmartUtil.getCurrentUser();
			String userId = user.getId();
			
			UcityWorkListCond ucityWorkListCond = new UcityWorkListCond();
			
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
						else if(rightOperandInt == Instance.STATUS_ABORTED)
							rightOperand = PrcProcessInst.PROCESSINSTSTATUS_ABORTED;
					}

					filter.setLeftOperandType(formFieldType);
					if (tableColName.equalsIgnoreCase("lastTask_tskname")) {
						tableColName = "runningTaskName";
					} else if (tableColName.equalsIgnoreCase("prcStatus")) {
						tableColName = "status";
					} else if (tableColName.equalsIgnoreCase("prcTitle")) {
						tableColName = "title";
					} else if (tableColName.equalsIgnoreCase("prcCreateDate")) {
						tableColName = "createdTime";
					} else if (tableColName.equalsIgnoreCase("lastTask_tskexecuteDate")) {
						tableColName = "modifiedTime";
					}
					
					filter.setLeftOperandValue(tableColName);
					filter.setOperator(operator);
					filter.setRightOperandType(formFieldType);
					filter.setRightOperandValue(rightOperand);
					filterList.add(filter);
				}

				Filter[] filters = new Filter[filterList.size()];
				filterList.toArray(filters);

				ucityWorkListCond.setFilter(filters);
			}

			LocalDate priviousDate = new LocalDate(new LocalDate().getTime() - LocalDate.ONE_DAY*7);
			
			String filterId = params.getFilterId();
			if(filterId != null) {
				if(filterId.equals(SearchFilter.FILTER_ALL_INSTANCES)) {
				} else if(filterId.equals(SearchFilter.FILTER_RECENT_INSTANCES)) {
					ucityWorkListCond.addFilter(new Filter(">=", FormField.ID_LAST_MODIFIED_DATE, Filter.OPERANDTYPE_DATE, priviousDate.toGMTSimpleDateString()));
				} else if(filterId.equals(SearchFilter.FILTER_RUNNING_INSTANCES)) {
					ucityWorkListCond.addFilter(new Filter("=", "status", Filter.OPERANDTYPE_STRING, PrcProcessInst.PROCESSINSTSTATUS_RUNNING));
				}
			}
			
			String searchKey = params.getSearchKey();
			ucityWorkListCond.setSearchKey(CommonUtil.toNull(searchKey));
			
			if (instanceIdIns != null && instanceIdIns.length != 0) {
				ucityWorkListCond.setPrcInstIdIns(instanceIdIns);
			}

			long totalCount = SwManagerFactory.getInstance().getUcityWorkListManager().getUcityWorkListSize(user.getId(), ucityWorkListCond);

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
				ucityWorkListCond.setPageNo(currentPage-1);

			ucityWorkListCond.setPageSize(pageSize);

			SortingField sf = params.getSortingField();

			//화면에서 사용하고 있는 컬럼의 상수값과 실제 프로세스 인스턴스 데이터 베이스의 컬럼 이름이 맞지 않아 컨버팅 작업
			//한군데에서 관리 하도록 상수로 변경 필요
			// 이벤트 시간으로 정렬
			if (sf == null) {
				sf = new SortingField();
	            sf.setFieldId("eventTime");
//				sf.setFieldId(FormField.ID_CREATED_DATE);
				sf.setAscending(false);
			}
			String sfColumnNameTemp = sf.getFieldId();
			
			if (sfColumnNameTemp.equalsIgnoreCase("status")) {
				sfColumnNameTemp = "status"; 
			} else if (sfColumnNameTemp.equalsIgnoreCase("subject")) {
				sfColumnNameTemp = "title";
			} else if (sfColumnNameTemp.equalsIgnoreCase("lastTask")) {
				sfColumnNameTemp = "runningTaskName"; 
			} else if (sfColumnNameTemp.equalsIgnoreCase("creator")) {
				sfColumnNameTemp = "creationUser"; 
			} else if (sfColumnNameTemp.equalsIgnoreCase("createdTime")) {
				sfColumnNameTemp = "creationDate"; 
			} else if (sfColumnNameTemp.equalsIgnoreCase("modifier")) {
				sfColumnNameTemp = "modificationUser"; 
			} else if (sfColumnNameTemp.equalsIgnoreCase("modifiedTime")) {
				sfColumnNameTemp = "modificationDate"; 
			} 
			if (CommonUtil.isEmpty(sfColumnNameTemp)){
				sfColumnNameTemp = "creationDate";
			}

			ucityWorkListCond.setOrders(new Order[]{new Order(sfColumnNameTemp, sf.isAscending())});

			UcityWorkList[] workLists = SwManagerFactory.getInstance().getUcityWorkListManager().getUcityWorkLists(userId, ucityWorkListCond, null);
			
			InstanceInfoList instanceInfoList = new InstanceInfoList();
			List<PWInstanceInfo> pwInstanceInfoList = new ArrayList<PWInstanceInfo>();
			PWInstanceInfo[] pWInstanceInfos = null;
			
			if (workLists != null) {
				for (int i = 0; i < workLists.length; i++) {
					UcityWorkList workList = workLists[i];
					PWInstanceInfo pworkInfo = new PWInstanceInfo();
					
					pworkInfo.setId(workList.getPrcInstId());
					pworkInfo.setCreatedDate(new LocalDate(workList.getCreationDate().getTime()));
					pworkInfo.setLastModifiedDate(new LocalDate(workList.getModificationDate().getTime()));
					pworkInfo.setLastModifier(ModelConverter.getUserInfoByUserId(workList.getModificationUser()));
					
					if (!CommonUtil.isEmpty(workList.getRunningTaskId())) {
	//					TaskWorkCond taskWorkCond = new TaskWorkCond();
	//					taskWorkCond.setTskObjId(workList.getRunningTaskId());
	//					TaskWork[] taskWork = SwManagerFactory.getInstance().getWorkListManager().getTaskWorkList(userId, taskWorkCond);
	//					pworkInfo.setLastTask(ModelConverter.getTaskInstanceInfo(user, taskWork[0]));
						TskTask lastTask = SwManagerFactory.getInstance().getTskManager().getTask(userId, workList.getRunningTaskId(), IManager.LEVEL_ALL);
						if (lastTask != null) {
							UserInfo assigneeInfo = ModelConverter.getUserInfoByUserId(lastTask.getAssignee());
							pworkInfo.setLastTask(new TaskInstanceInfo(lastTask.getObjId(), lastTask.getName(), SmartWork.TYPE_PROCESS, assigneeInfo , assigneeInfo, new LocalDate(lastTask.getModificationDate().getTime())));
							pworkInfo.setLastTaskCount(1);
						}
					}
					pworkInfo.setOwner(ModelConverter.getUserInfoByUserId(workList.getCreationUser()));
					int status = -1;
					if (workList.getStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_RUNNING)) {
						boolean isDelayedProcessInst = isDelayedProcessInstanceWithSetRunningTasks(userId, workList.getPrcInstId(), pworkInfo);
						if (isDelayedProcessInst) {
							status = Instance.STATUS_DELAYED_RUNNING;
						} else {
							status = Instance.STATUS_RUNNING;
						}
						
					} else if (workList.getStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_COMPLETE)) {
						status = Instance.STATUS_COMPLETED;
					} else if (workList.getStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_ABORTED)) {
						status = Instance.STATUS_ABORTED;
					}
					pworkInfo.setStatus(status);
					pworkInfo.setSubject(workList.getTitle());
					pworkInfo.setType(WorkInstance.TYPE_PROCESS);
//Start InstanceInfo Model Changed by ysjung
					//pworkInfo.setWork(ModelConverter.getWorkInfoByPackageId(workList.getPackageId()));
					pworkInfo.setWorkInfo(ModelConverter.getWorkInfoByPackageId(workList.getPackageId()));
					//pworkInfo.setWorkInfo(workId, workName, workType, isWorkRunning, workFullPathName);
//End InstanceInfo Model Changed by ysjung
					//pworkInfo.setWorkSpace(workSpace);
					
					Property p1 = new Property("serviceName",workList.getServiceName());
					Property p2 = new Property("eventName",workList.getEventName());
					Property p3 = new Property("type",workList.getType());
					Property p4 = new Property("externalDisplay",workList.getExternalDisplay());
					Property p5 = new Property("eventPlace",workList.getEventPlace());
					Property p6 = new Property("isSms", CommonUtil.toBoolean(workList.getIsSms())+"");
					Property p7 = new Property("eventId",workList.getEventId());
					Property p8;
					Property p9 = new Property("runningTaskName", workList.getRunningTaskName());
					if (workList.getEventTime() != null) {
						Date tempDate = new Date();
						tempDate.setTime(workList.getEventTime().getTime() + TimeZone.getDefault().getRawOffset());
						 p8 = new Property("eventTime", DateUtil.toDateString(tempDate).replace(".000", ""));
					} else {
						p8 = new Property("eventTime", null);
					}
					Property[] properties = new Property[]{p1, p2, p3, p4, p5, p6, p7, p8, p9};

					pworkInfo.setExtentedProperty(properties);
					
					pwInstanceInfoList.add(pworkInfo);
				}
			}
			if(pwInstanceInfoList.size() > 0) {
				pWInstanceInfos = new PWInstanceInfo[pwInstanceInfoList.size()];
				pwInstanceInfoList.toArray(pWInstanceInfos);
			}
			
			instanceInfoList.setInstanceDatas(pWInstanceInfos);
			instanceInfoList.setPageSize(pageSize);
			instanceInfoList.setTotalSize((int)totalCount);
			instanceInfoList.setSortedField(sf);
			instanceInfoList.setTotalPages(totalPages);
			instanceInfoList.setCurrentPage(currentPage);
			instanceInfoList.setType(InstanceInfoList.TYPE_PROCESS_INSTANCE_LIST);
			
			return instanceInfoList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}	
	
	public InstanceInfoList getAllPWorkInstanceList(boolean runningOnly, RequestParams params) throws Exception {
		try{
			User user = SmartUtil.getCurrentUser();
			String userId = user.getId();
			//TODO workId = category 프로세스 인스턴스정보에는 패키지 컬럼이 없고 다이어 그램 컬럼에 정보가 들어가 있다
			//임시로 프로세스 다이어그램아이디 필드를 이용하고 프로세스인스턴스가 생성되는 시점(업무 시작, 처리 개발 완료)에 패키지 아이디 컬럼을 추가해 그곳에서 조회하는걸로 변경한다
			PrcProcessInstCond prcInstCond = new PrcProcessInstCond();
			if (runningOnly)
				prcInstCond.setStatus(PrcProcessInst.PROCESSINSTSTATUS_RUNNING);
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
//모든 패키지이기 때문에 필터아이디가 넘어 올수 없다 각 필터아이디는 workId(packageId)에 따라 붙기 때문에
//			if(filterId != null) {
//			}
//모든 업무패키지의 권한을 따져야 한다면 아래 내용을 수정하여 작성해야함
//			PkgPackageCond pkgPackageCond = new PkgPackageCond();
//			pkgPackageCond.setPackageId(workId);
//			PkgPackage pkgPackage = getPkgManager().getPackage(userId, pkgPackageCond, IManager.LEVEL_LITE);
//
//			if(!ModelConverter.isAccessibleAllInstance(ModelConverter.getResourceIdByPkgPackage(pkgPackage), userId))
//				prcInstCond.setCreationUser(userId);
//
//			String[] workSpaceIdIns = ModelConverter.getWorkSpaceIdIns(user);
//			//prcInstCond.setWorkSpaceIdIns(workSpaceIdIns);
//
//			String[] groupIdsByNotBelongToClosedGroup = ModelConverter.getGroupIdsByNotBelongToClosedGroup(user);
//			prcInstCond.setWorkSpaceIdNotIns(groupIdsByNotBelongToClosedGroup);
//
//			prcInstCond.setLikeAccessValues(workSpaceIdIns);

			String searchKey = params.getSearchKey();
			prcInstCond.setSearchKey(searchKey);

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
//Start InstanceInfo Model Changed by ysjung
					//pwInstInfo.setWork(workInfo);
					pwInstInfo.setWorkInfo(workInfo);
					//pwInstInfo.setWorkInfo(workId, workName, workType, isWorkRunning, workFullPathName);
//End InstanceInfo Model Changed by ysjung
		
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
							tskStatus = Instance.STATUS_RUNNING;
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
//Start InstanceInfo Model Changed by ysjung
						//lastTaskInfo.setWork(workInfo);
						lastTaskInfo.setWorkInfo(workInfo);
						//lastTaskInfo.setWorkInfo(workId, workName, workType, isWorkRunning, workFullPathName);
//End InstanceInfo Model Changed by ysjung
						lastTaskInfo.setWorkInstance(pwInstInfo);
//Start InstanceInfo Model Changed by ysjung
						//lastTaskInfo.setWorkSpace(ModelConverter.getWorkSpaceInfo(prcInst.getLastTask_tskWorkSpaceType(), prcInst.getLastTask_tskWorkSpaceId()));
						lastTaskInfo.setWorkSpaceInfo(ModelConverter.getWorkSpaceInfo(prcInst.getLastTask_tskWorkSpaceType(), prcInst.getLastTask_tskWorkSpaceId()));
						//lastTaskInfo.setWorkSpaceInfo(workSpaceId, workSpaceName, workSpaceType, workSpaceMinPicture);
//End InstanceInfo Model Changed by ysjung
						lastTaskInfo.setName(name);
						lastTaskInfo.setTaskType(tskType);
						lastTaskInfo.setAssignee(ModelConverter.getUserInfoByUserId(assignee));
						lastTaskInfo.setPerformer(ModelConverter.getUserInfoByUserId(performer));
						//WorkInstanceInfo workInstanceInfo = paretProcessInstObj;
						pwInstInfo.setLastTask(lastTaskInfo);//마지막 태스크
					}
					pwInstInfo.setLastTaskCount(prcInst.getLastTask_tskCount());
//Start InstanceInfo Model Changed by ysjung
					//pwInstInfo.setWorkSpace(ModelConverter.getWorkSpaceInfo(prcInst.getPrcWorkSpaceType(), prcInst.getPrcWorkSpaceId()));
					pwInstInfo.setWorkSpaceInfo(ModelConverter.getWorkSpaceInfo(prcInst.getPrcWorkSpaceType(), prcInst.getPrcWorkSpaceId()));
					//pwInstInfo.setWorkSpaceInfo(workSpaceId, workSpaceName, workSpaceType, workSpaceMinPicture);
//End InstanceInfo Model Changed by ysjung
					pwInstanceInfoList.add(pwInstInfo);
				}
			}

			if(pwInstanceInfoList.size() > 0) {
				pWInstanceInfos = new PWInstanceInfo[pwInstanceInfoList.size()];
				pwInstanceInfoList.toArray(pWInstanceInfos);
			}
			instanceInfoList.setInstanceDatas(pWInstanceInfos);
			instanceInfoList.setPageSize(pageSize);
			instanceInfoList.setTotalSize((int)totalCount);
			instanceInfoList.setSortedField(sf);
			instanceInfoList.setTotalPages(totalPages);
			instanceInfoList.setCurrentPage(currentPage);
			instanceInfoList.setType(InstanceInfoList.TYPE_PROCESS_INSTANCE_LIST);
			return instanceInfoList;
		} catch (Exception e){
			e.printStackTrace();
			return null;			
		}
		
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
			//prcInstCond.setWorkSpaceIdIns(workSpaceIdIns);

			String[] groupIdsByNotBelongToClosedGroup = ModelConverter.getGroupIdsByNotBelongToClosedGroup(user);
			prcInstCond.setWorkSpaceIdNotIns(groupIdsByNotBelongToClosedGroup);

			prcInstCond.setLikeAccessValues(workSpaceIdIns);

			String searchKey = params.getSearchKey();
			prcInstCond.setSearchKey(searchKey);

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
						if(TskTask.TASKSTATUS_CANCEL.equalsIgnoreCase(prcInst.getLastTask_tskTargetApprovalStatus())){
							status = Instance.STATUS_REJECTED;
						}else if(prcInst.getLastTask_tskExpectEndDate().getTime() < (new LocalDate()).getTime()){
							status = Instance.STATUS_DELAYED_RUNNING;
						}else{
							status = Instance.STATUS_RUNNING;
						}
					} else if (prcInst.getPrcStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_COMPLETE)) {
						if("true".equalsIgnoreCase(prcInst.getLastTask_tskIsApprovalSourceTask())){
							if(AprApproval.APPROVAL_STATUS_RUNNING.equals(prcInst.getLastTask_tskTargetApprovalStatus())){
								if(prcInst.getLastTask_tskExpectEndDate()!=null && prcInst.getLastTask_tskExpectEndDate().getTime()<(new LocalDate()).getTime()){
									status = Instance.STATUS_DELAYED_RUNNING;
									
								}else{
									status = Instance.STATUS_DELAYED_RUNNING;									
								}
							}else{
								status = Instance.STATUS_COMPLETED;																	
							}
						}else{
							status = Instance.STATUS_COMPLETED;
						}
					} else if (prcInst.getPrcStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_ABORTED)) {
						status = Instance.STATUS_ABORTED;
					} else if(prcInst.getPrcStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_RETURN)){
						status = Instance.STATUS_RETURNED;
					} else{
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
					pwInstInfo.setWorkInfo(workInfo);
		
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
							if(status == Instance.STATUS_RUNNING && prcInst.getLastTask_tskExpectEndDate().getTime() < (new LocalDate()).getTime()){
								tskStatus = Instance.STATUS_DELAYED_RUNNING;
							}else{
								tskStatus = Instance.STATUS_RUNNING;
							}
						} else if (prcInst.getLastTask_tskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_COMPLETE)) {
							tskStatus = Instance.STATUS_COMPLETED;
						} else if (prcInst.getLastTask_tskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ABORTED)) {
							tskStatus = Instance.STATUS_ABORTED;
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
						lastTaskInfo.setWorkInfo(workInfo);
						lastTaskInfo.setWorkInstance(pwInstInfo);
						lastTaskInfo.setWorkSpaceInfo(ModelConverter.getWorkSpaceInfo(prcInst.getLastTask_tskWorkSpaceType(), prcInst.getLastTask_tskWorkSpaceId()));
						lastTaskInfo.setName(name);
						lastTaskInfo.setTaskType(tskType);
						lastTaskInfo.setAssignee(ModelConverter.getUserInfoByUserId(assignee));
						lastTaskInfo.setPerformer(ModelConverter.getUserInfoByUserId(performer));
						pwInstInfo.setLastTask(lastTaskInfo);//마지막 태스크
					}
					pwInstInfo.setLastTaskCount(prcInst.getLastTask_tskCount());
					pwInstInfo.setWorkSpaceInfo(ModelConverter.getWorkSpaceInfo(prcInst.getPrcWorkSpaceType(), prcInst.getPrcWorkSpaceId()));
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

			String[] likeAccessValues = ModelConverter.getWorkSpaceIdIns(cUser);
			swdRecordCond.setLikeAccessValues(likeAccessValues);

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
				} else if (fieldId.equals(FormField.ID_BOARD_DURATION)) {
					fieldId = "Duration";
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
								} else if(swdDataField.getId().equals("3")) {
									if(!CommonUtil.isEmpty(value)) {
										Date duration = DateUtil.toDate(value, "yyyy-MM-dd HH:mm:ss");
										tempWorkInstanceInfo.setDuration(new LocalDate(duration.getTime()));
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

//Start InstanceInfo Model Changed by ysjung
					//workInstanceInfo.setWorkSpace(workSpaceInfo);
					workInstanceInfo.setWorkSpaceInfo(workSpaceInfo);
					//workInstanceInfo.setWorkSpaceInfo(workSpaceId, workSpaceName, workSpaceType, workSpaceMinPicture);
//End InstanceInfo Model Changed by ysjung

					WorkCategoryInfo groupInfo = null;
					if (!CommonUtil.isEmpty(subCtgId))
						groupInfo = new WorkCategoryInfo(subCtgId, subCtgName);

					WorkCategoryInfo categoryInfo = new WorkCategoryInfo(parentCtgId, parentCtgName);

					WorkInfo workInfo = new SmartWorkInfo(formId, formName, SocialWork.TYPE_BOARD, groupInfo, categoryInfo);

//Start InstanceInfo Model Changed by ysjung
					//workInstanceInfo.setWork(workInfo);
					workInstanceInfo.setWorkInfo(workInfo);
					//workInstanceInfo.setWorkInfo(workId, workName, workType, isWorkRunning, workFullPathName);
//End InstanceInfo Model Changed by ysjung
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
			taskWorkCond.setPackageStatus("DEPLOYED");

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

	
	public InstanceInfoList getSavedInstanceList(String workSpaceId, RequestParams params) throws Exception {
		
		int currentPage = params.getCurrentPage();
		int pageSize = params.getPageSize();
		SortingField sortingField = params.getSortingField();

		User cUser = SmartUtil.getCurrentUser();
		String userId = cUser.getId();
		
		TskTaskCond taskCond = new TskTaskCond();
		taskCond.setStatus(TskTask.TASKSTATUS_TEMPSAVE);
		taskCond.setType(TskTask.TASKTYPE_TEMPSAVE);
		taskCond.setAssignee(userId);
		
		long totalSize = SwManagerFactory.getInstance().getTskManager().getTaskSize(userId, taskCond);
		
		if (totalSize == 0)
			return null;
		
		taskCond.setPageNo(currentPage - 1);
		taskCond.setPageSize(pageSize);
		if (sortingField != null) {
			taskCond.setOrders(new Order[]{new Order(sortingField.getFieldId(), sortingField.isAscending())});
		} else {
			taskCond.setOrders(new Order[]{new Order(TskTask.A_MODIFICATIONDATE, false)});
		}
			
		TskTask[] tasks = SwManagerFactory.getInstance().getTskManager().getTasks(userId, taskCond, IManager.LEVEL_LITE);
		
		InstanceInfoList list = new InstanceInfoList();
		list.setCurrentPage(currentPage);
		list.setPageSize(pageSize);
		list.setSortedField(sortingField);
		list.setTotalSize((int)totalSize);
		
		int totalPages = (int)totalSize % pageSize;
		if(totalPages == 0) {
			totalPages = (int)totalSize / pageSize;
		} else {
			totalPages = (int)totalSize / pageSize + 1;
		}
		list.setTotalPages(totalPages);
		
		//list.setType(type);
		
		InstanceInfo[] instanceDatas = new InstanceInfo[tasks.length];
		for (int i = 0; i < tasks.length; i++) {
			TskTask task = tasks[i];
			//InstanceInfo instanceData = ModelConverter.getInstanceInfoByTskTask(null, task);
			WorkInstanceInfo instanceData = new WorkInstanceInfo();
			
			UserInfo owner = ModelConverter.getUserInfoByUserId(userId);
			instanceData.setId(task.getObjId());
			instanceData.setOwner(owner);
			instanceData.setLastModifiedDate(new LocalDate(task.getModificationDate().getTime()));
			instanceData.setLastModifier(owner);
			
			String taskDef = task.getDef();
			String[] taskDefInfos = StringUtils.tokenizeToStringArray(taskDef, "|");
			
			String taskRefType = task.getRefType();
			
			if (taskRefType.equalsIgnoreCase(TskTask.TASKTYPE_SINGLE)) {
				instanceData.setWorkType(SmartWork.TYPE_INFORMATION);
			} else {
				instanceData.setWorkType(SmartWork.TYPE_PROCESS);
			}
			instanceData.setSubject(task.getTitle());
			instanceData.setWorkId(taskDefInfos[0]);
			instanceData.setWorkName(task.getName());
			instanceData.setWorkRunning(true);
			instanceData.setWorkFullPathName(task.getName());
			TaskInstanceInfo lastTask = new TaskInstanceInfo();
			lastTask.setName(task.getName());
			instanceData.setLastTask(lastTask);
			
			
			instanceDatas[i] = instanceData;
		}
		list.setInstanceDatas(instanceDatas);
		
		return list;
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

			if(!FileCategory.ID_ALL_FILES.equals(parentId)) {
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
			}

			long totalCount = getDocManager().getFileWorkListSize(userId, fileWorkCond);

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
				fileWorkCond.setPageNo(currentPage-1);

			fileWorkCond.setPageSize(pageSize);
			//fileWorkCond.setOrders(new Order[]{new Order("tskCreatedate", false)});

/*			List<FileWork> fileWorkList = new ArrayList<FileWork>();
			FileWork[] finalFileWorks = null;*/

			FileWork[] fileWorks = getDocManager().getFileWorkList(userId, fileWorkCond);

			WorkInstanceInfo[] workInstanceInfos = null;
			/*if(!CommonUtil.isEmpty(fileWorks)) {
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
			}*/
			if(!CommonUtil.isEmpty(fileWorks))
				workInstanceInfos = ModelConverter.getWorkInstanceInfosByFileWorks(fileWorks, TskTask.TASKREFTYPE_IMAGE, displayBy);

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

			instanceInfoList.setTotalSize((int)totalCount);
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
			fileWorkCond.setPackageStatus(PkgPackage.STATUS_DEPLOYED);

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

			/*FileWork[] totalFileWorks = getDocManager().getFileWorkList(userId, fileWorkCond);
			int viewCount = 0;
			if(!CommonUtil.isEmpty(totalFileWorks)) {
				for(FileWork totalFileWork : totalFileWorks) {
					boolean isAccessForMe = ModelConverter.isAccessableInstance(totalFileWork);
					if(isAccessForMe) {
						viewCount = viewCount + 1;
					}
				}
			}*/

			String[] workSpaceIdIns = ModelConverter.getWorkSpaceIdIns(cUser);

			String[] groupIdsByNotBelongToClosedGroup = ModelConverter.getGroupIdsByNotBelongToClosedGroup(cUser);
			fileWorkCond.setWorkSpaceIdNotIns(groupIdsByNotBelongToClosedGroup);

			fileWorkCond.setLikeAccessValues(workSpaceIdIns);

			long totalCount = getDocManager().getFileWorkListSize(userId, fileWorkCond);

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
				fileWorkCond.setPageNo(currentPage-1);

			fileWorkCond.setPageSize(pageSize);
			//fileWorkCond.setOrders(new Order[]{new Order("tskCreatedate", false)});

			/*List<FileWork> fileWorkList = new ArrayList<FileWork>();
			FileWork[] finalFileWorks = null;*/

			FileWork[] fileWorks = getDocManager().getFileWorkList(userId, fileWorkCond);

			WorkInstanceInfo[] workInstanceInfos = null;
			/*if(!CommonUtil.isEmpty(fileWorks)) {
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
			}*/
			if(!CommonUtil.isEmpty(fileWorks))
				workInstanceInfos = ModelConverter.getWorkInstanceInfosByFileWorks(fileWorks, TskTask.TASKREFTYPE_FILE, 0);

			instanceInfoList.setTotalSize((int)totalCount);
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

		/*try {
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

					CommunityInfo[] participants = eventInstanceInfo.getRelatedUsers();
					boolean isParticipant = false;
					if(!CommonUtil.isEmpty(participants))
						isParticipant =  calendarService.isParticipant(participants);
					if(isParticipant || owner.equals(userId) || modifier.equals(userId))
						eventInstanceInfoList.add(eventInstanceInfo);

					String tskAccessLevel = task.getTskAccessLevel();
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
					}
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
		}*/

		return calendarService.getEventInstanceInfosByWorkSpaceId(workSpaceId, fromDate, toDate, 0, false);

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
//Start InstanceInfo Model Changed by ysjung
				//pwInstInfo.setWork(workInfo);
				pwInstInfo.setWorkInfo(workInfo);
				//pwInstInfo.setWorkInfo(workId, workName, workType, isWorkRunning, workFullPathName);
//End InstanceInfo Model Changed by ysjung
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
//Start InstanceInfo Model Changed by ysjung
					//lastTaskInfo.setWork(workInfo);
					//lastTaskInfo.setWorkSpace(ModelConverter.getWorkSpaceInfo(prcInst.getWorkSpaceType(), prcInst.getWorkSpaceId()));
					lastTaskInfo.setWorkInfo(workInfo);
					//lastTaskInfo.setWorkInfo(workId, workName, workType, isWorkRunning, workFullPathName);
					lastTaskInfo.setWorkSpaceInfo(ModelConverter.getWorkSpaceInfo(prcInst.getWorkSpaceType(), prcInst.getWorkSpaceId()));
					//lastTaskInfo.setWorkSpaceInfo(workSpaceId, workSpaceName, workSpaceType, workSpaceMinPicture);
//End InstanceInfo Model Changed by ysjung
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
//Start InstanceInfo Model Changed by ysjung
						//runningTaskInfo.setWork(workInfo);
						//runningTaskInfo.setWorkSpace(ModelConverter.getWorkSpaceInfo(runningTask.getWorkSpaceType(), runningTask.getWorkSpaceId()));
						runningTaskInfo.setWorkInfo(workInfo);
						//runningTaskInfo.setWorkInfo(workId, workName, workType, isWorkRunning, workFullPathName);
						runningTaskInfo.setWorkSpaceInfo(ModelConverter.getWorkSpaceInfo(runningTask.getWorkSpaceType(), runningTask.getWorkSpaceId()));
						//runningTaskInfo.setWorkSpaceInfo(workSpaceId, workSpaceName, workSpaceType, workSpaceMinPicture);
//End InstanceInfo Model Changed by ysjung
						runningTaskInfo.setName(name);
						runningTaskInfo.setTaskType(type);
						runningTaskInfo.setAssignee(ModelConverter.getUserInfoByUserId(assignee));
						runningTaskInfo.setPerformer(ModelConverter.getUserInfoByUserId(performer));
	//					pwInstInfo.setRunningTasks(new TaskInstanceInfo[]{runningTaskInfo});//실행중태스크
					}
				}
//Start InstanceInfo Model Changed by ysjung
				//pwInstInfo.setWorkSpace(ModelConverter.getWorkSpaceInfo(prcInst.getWorkSpaceType(), prcInst.getWorkSpaceId()));
				pwInstInfo.setWorkSpaceInfo(ModelConverter.getWorkSpaceInfo(prcInst.getWorkSpaceType(), prcInst.getWorkSpaceId()));
				//pwInstInfo.setWorkSpaceInfo(workSpaceId, workSpaceName, workSpaceType, workSpaceMinPicture);
//End InstanceInfo Model Changed by ysjung
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

	@Override
	public WorkInstance getSavedWorkInstanceById(int workType, String workId, String instanceId) throws Exception {
	
		if (CommonUtil.isEmpty(instanceId))
			return null;
		TskTask savedTask = getTskManager().getTask("", instanceId, IManager.LEVEL_LITE);
		if (CommonUtil.isEmpty(savedTask))
			return null;

		if (workType == SmartWork.TYPE_INFORMATION) {
			
			String requestBodyStr = savedTask.getDocument();
			Map requestBody = JsonUtil.getMapByJsonString(requestBodyStr);
			String formId = (String)requestBody.get("formId");

			if (CommonUtil.isEmpty(workId))
				workId = (String)requestBody.get("workId");
			
			PkgPackageCond pkgCond = new PkgPackageCond();
			pkgCond.setPackageId(workId);
			PkgPackage pkg = getPkgManager().getPackage("", pkgCond, IManager.LEVEL_LITE);
			
			SwdDomainCond swdDomainCond = new SwdDomainCond();
			swdDomainCond.setFormId(formId);
			SwdDomain swdDomain = getSwdManager().getDomain("", swdDomainCond, IManager.LEVEL_ALL);
			
			SwdField[] swdFields = swdDomain.getFields();
			
			SwdRecord record = ModelConverter.getSwdRecordByRequestBody("", swdFields, requestBody, null);
			
			//InformationWorkInstance workInstance = ModelConverter.getInformationWorkInstanceBySwdRecord("", null, record);
			
			InformationWorkInstance workInstance = new InformationWorkInstance();
			
			//workInstance.setApprovalLine(approvalLine);
			//workInstance.setApprovalWork(isApprovalWork);
			workInstance.setId(instanceId);
			workInstance.setTempSaved(true);
			workInstance.setSubject(savedTask.getTitle());
			workInstance.setType(WorkInstance.TYPE_INFORMATION);
			//workInstance.setStatus();
			User owner = ModelConverter.getUserByUserId(savedTask.getAssignee());
			workInstance.setOwner(owner);
			workInstance.setCreatedDate(new LocalDate(savedTask.getCreationDate().getTime()));
			workInstance.setLastModifier(owner);
			workInstance.setLastModifiedDate(new LocalDate(savedTask.getModificationDate().getTime()));
			//workInstance.setTasks(tasks);
			workInstance.setWork(ModelConverter.getInformationWorkByPkgPackage("", null, pkg));
			workInstance.setWorkSpace(ModelConverter.getWorkSpace(record.getWorkSpaceType(), record.getWorkSpaceId()));
			workInstance.setAccessPolicy(new AccessPolicy(Integer.parseInt(record.getAccessLevel())));
			//workInstance.setForwardees(forwardees);
			//workInstance.setLazyreferenceTask(isLazyreferenceTask);
			//workInstance.setRepeatEventId(repeatEventId);
			
			return workInstance;
			
		} else if (workType == SmartWork.TYPE_PROCESS) {
			
			String requestBodyStr = savedTask.getDocument();
			Map requestBody = JsonUtil.getMapByJsonString(requestBodyStr);
			String formId = (String)requestBody.get("formId");

			if (CommonUtil.isEmpty(workId))
				workId = (String)requestBody.get("workId");
			
			PkgPackageCond pkgCond = new PkgPackageCond();
			pkgCond.setPackageId(workId);
			PkgPackage pkg = getPkgManager().getPackage("", pkgCond, IManager.LEVEL_LITE);
			
			ProcessWorkInstance workInstance = new ProcessWorkInstance();
			
			//workInstance.setApprovalLine(approvalLine);
			//workInstance.setApprovalWork(isApprovalWork);
			workInstance.setId(instanceId);
			workInstance.setTempSaved(true);
			workInstance.setSubject(savedTask.getTitle());
			workInstance.setType(WorkInstance.TYPE_PROCESS);
			//workInstance.setStatus();
			User owner = ModelConverter.getUserByUserId(savedTask.getAssignee());
			workInstance.setOwner(owner);
			workInstance.setCreatedDate(new LocalDate(savedTask.getCreationDate().getTime()));
			workInstance.setLastModifier(owner);
			workInstance.setLastModifiedDate(new LocalDate(savedTask.getModificationDate().getTime()));
			
			UserInfo ownerInfo = ModelConverter.getUserInfoByUserId(savedTask.getAssignee());
			TaskInstanceInfo task = new TaskInstanceInfo();
			task.setName((String)requestBody.get("formName"));
			task.setAssigner(ownerInfo);
			task.setAssignee(ownerInfo);
			task.setFormId(formId);
			task.setLastModifiedDate(new LocalDate(savedTask.getModificationDate().getTime()));
			task.setLastModifier(ownerInfo);
			//task.setWorkInstance(workInstance);
			
			workInstance.setTasks(new TaskInstanceInfo[]{task});
			
			workInstance.setWork(ModelConverter.getProcessWorkByPkgPackage("", null, pkg));
			
			
			
			SwfForm form = getSwfManager().getForm("", formId);
			SwfField[] formFields = form.getFields();
			List domainFieldList = new ArrayList();
			
			//제목으로 사용할 필드 (필수>단문>첫번째)
			for (SwfField field: formFields) {
				SwdField domainField = new SwdField();
				domainField.setFormFieldId(field.getId());
				domainField.setFormFieldName(field.getName());
				domainField.setFormFieldType(field.getSystemType());
				domainField.setArray(field.isArray());
				domainField.setSystemField(field.isSystem());
				domainFieldList.add(domainField);
			}
			SwdField[] swdFields = new SwdField[domainFieldList.size()];
			domainFieldList.toArray(swdFields);
			SwdRecord record = ModelConverter.getSwdRecordByRequestBody("", swdFields, requestBody, null);
			
			workInstance.setWorkSpace(ModelConverter.getWorkSpace(record.getWorkSpaceType(), record.getWorkSpaceId()));
			
			workInstance.setAccessPolicy(new AccessPolicy(Integer.parseInt(record.getAccessLevel())));
			//workInstance.setForwardees(forwardees);
			//workInstance.setLazyreferenceTask(isLazyreferenceTask);
			//workInstance.setRepeatEventId(repeatEventId);
			
			return workInstance;
			
			
		} else {

		}
		
		return null;
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
			InformationWorkInstance result = ModelConverter.getInformationWorkInstanceBySwdRecord(userId, null, swdRecord);
			
			TaskInstanceInfo[] tasks = result.getTasks();
			List<TaskInstanceInfo> taskResult = new ArrayList<TaskInstanceInfo>();
			
			//정보관리 업무를 조회할때에는 tasks에 참조업무는 제외된다 단, 전자결재와 동시에 진행된 참조업무및 로그인사용자에게 할당된 
			//참조업무는 포함되어야 한다
			if (tasks != null) {
				for (int i = 0; i < tasks.length; i++) {
					TaskInstanceInfo task = tasks[i];
					int type = task.getTaskType();
					if (type == TaskInstance.TYPE_APPROVAL_TASK_ASSIGNED || type == TaskInstance.TYPE_APPROVAL_TASK_DRAFTED 
							|| type == TaskInstance.TYPE_APPROVAL_TASK_FORWARDED ) {
						taskResult.add(task);
					} else if (type == TaskInstance.TYPE_INFORMATION_TASK_FORWARDED ) {
						UserInfo userInfo = task.getAssignee();
						if (userInfo != null) {
							String assigneeId = userInfo.getId();
							if (userId.equalsIgnoreCase(assigneeId) && (task.getStatus() == Instance.STATUS_RUNNING || task.getStatus() == Instance.STATUS_DELAYED_RUNNING) ) {
								taskResult.add(task);
							}
						}
					}
				}
			}
			TaskInstanceInfo[] resultTasks = new TaskInstanceInfo[taskResult.size()];
			taskResult.toArray(resultTasks);
			result.setTasks(resultTasks);
			return result;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}
	
	public ImageInstance getImageInstanceById(String companyId, String userId, SwdRecord swdRecord) throws Exception {

		try{
			return ModelConverter.getImageInstanceBySwdRecord(userId, null, swdRecord);
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
	
	public static TaskWork[] getTaskWorkByFromToDate(String contextId, String spaceId, User currentUser, Date fromDate, Date toDate, int maxSize) throws Exception {
		if (CommonUtil.isEmpty(contextId) || CommonUtil.isEmpty(spaceId) || CommonUtil.isEmpty(currentUser) || CommonUtil.isEmpty(currentUser.getId()) || CommonUtil.isEmpty(currentUser.getCompanyId()))
			return null;
		
		String userId = currentUser.getId();
		String companyId = currentUser.getCompanyId();
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

		if(fromDate != null && toDate == null) {
			taskWorkCond.setTskExecuteDateBefore(fromDate);
		} else {
			taskWorkCond.setTskExecuteDateFrom(fromDate);
			taskWorkCond.setTskExecuteDateTo(toDate);
		}
		taskWorkCond.setPackageStatus(PkgPackage.STATUS_DEPLOYED);

		long totalCount = getWorkListManager().getTaskWorkListSize(userId, taskWorkCond);

		if(totalCount <= 0) return null;
		
		if(fromDate != null && toDate == null) {
			taskWorkCond.setOrders(new Order[]{new Order("tskcreatedate", false)});
		} else {
			taskWorkCond.setOrders(new Order[]{new Order("tskcreatedate", true)});
		}

		taskWorkCond.setPageNo(0);
		taskWorkCond.setPageSize(maxSize);

		TaskWork[] tasks = getWorkListManager().getTaskWorkList(userId, taskWorkCond);

		TaskWork[] newTasks = null;
		TaskWork task = new TaskWork();

		if(totalCount > maxSize) {
			if(!CommonUtil.isEmpty(tasks)) {
				newTasks = new TaskWork[maxSize+1];
				for(int i=0; i<maxSize; i++)
					newTasks[i] = tasks[i];
				newTasks[maxSize] = task;
				return newTasks;
			}
		}else{
			newTasks = tasks;
		}
		return newTasks;
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

			Date fromDate = null;
			Date toDate = null;
			Date workStartDate = null;
			Date workEndDate = null;
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
			if (date != null) {
				fromDate = new Date();
				toDate = new Date();

				fromDate.setTime(date.getLocalTime());
				toDate.setTime(date.getLocalTime());
				
				fromDate = DateUtil.toFromDate(fromDate, DateUtil.CYCLE_DAY);
				fromDate.setTime(fromDate.getTime() - TimeZone.getDefault().getRawOffset());
				
				toDate = DateUtil.toToDate(toDate, DateUtil.CYCLE_DAY);
				toDate.setTime(toDate.getTime() - TimeZone.getDefault().getRawOffset());

				workStartDate = new Date(fromDate.getTime() + (Long)workHourTimeMap.get("startTime"));
				workEndDate = new Date(fromDate.getTime() + (Long)workHourTimeMap.get("endTime"));
			}

			Semaphore semaphore = new Semaphore(3);
			Thread currentThread = Thread.currentThread();
			TaskInfoParallelProcessing[] taskInfoPP = new TaskInfoParallelProcessing[3];
			
			taskInfoPP[0] = new TaskInfoParallelProcessing(semaphore, currentThread, contextId, spaceId, cuser, fromDate, workStartDate, maxSize);
			taskInfoPP[0].start();
			taskInfoPP[1] = new TaskInfoParallelProcessing(semaphore, currentThread, contextId, spaceId, cuser, workStartDate, workEndDate, maxSize);
			taskInfoPP[1].start();
			taskInfoPP[2] = new TaskInfoParallelProcessing(semaphore, currentThread, contextId, spaceId, cuser, workEndDate, toDate, maxSize);
			taskInfoPP[2].start();
			synchronized (currentThread) {
				currentThread.wait();
			}
			
			return new TaskInstanceInfo[][] { (TaskInstanceInfo[])taskInfoPP[0].getResult(), (TaskInstanceInfo[])taskInfoPP[1].getResult(), (TaskInstanceInfo[])taskInfoPP[2].getResult() }; 
			
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
			
			Semaphore semaphore = new Semaphore(7);
			Thread currentThread = Thread.currentThread();
			TaskInfoParallelProcessing[] taskInfoPP = new TaskInfoParallelProcessing[7];
			
			for(int i=0; i<7; i++){
				taskInfoPP[i] = new TaskInfoParallelProcessing(semaphore, currentThread, contextId, spaceId, cuser, new Date(tempFromDate.getTime()+LocalDate.ONE_DAY*i), new Date(tempToDate.getTime()-LocalDate.ONE_DAY*(6-i)), maxSize);
				taskInfoPP[i].start();
				
			}
			synchronized (currentThread) {
				currentThread.wait();
			}
			
			return new TaskInstanceInfo[][] {  (TaskInstanceInfo[])taskInfoPP[0].getResult(), (TaskInstanceInfo[])taskInfoPP[1].getResult(), (TaskInstanceInfo[])taskInfoPP[2].getResult(),
												(TaskInstanceInfo[])taskInfoPP[3].getResult(), (TaskInstanceInfo[])taskInfoPP[4].getResult(), (TaskInstanceInfo[])taskInfoPP[5].getResult(), (TaskInstanceInfo[])taskInfoPP[6].getResult() }; 
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
			

			Date fromDate = null;
			Date toDate = null;
			if (month != null) {
				fromDate = new Date();
				toDate = new Date();

				fromDate.setTime(month.getLocalTime());
				toDate.setTime(month.getLocalTime());
				
				fromDate = DateUtil.toFromDate(fromDate, DateUtil.CYCLE_MONTH);
				fromDate.setTime(fromDate.getTime() - TimeZone.getDefault().getRawOffset());
				
				toDate = DateUtil.toToDate(toDate, DateUtil.CYCLE_MONTH);
				toDate.setTime(toDate.getTime() - TimeZone.getDefault().getRawOffset());
				
			}
			
			WorkHourPolicy whp = SwServiceFactory.getInstance().getCalendarService().getCompanyWorkHourPolicy();
			Calendar cal = Calendar.getInstance();
			cal.setTime(fromDate);
			cal.setFirstDayOfWeek(whp.getFirstDayOfWeek());
			cal.setMinimalDaysInFirstWeek(1);
			int firstDayOfFromWeek = cal.get(Calendar.DAY_OF_WEEK);		

			Date temp = new Date(month.getLocalTime());
			temp = DateUtil.toToDate(temp, DateUtil.CYCLE_MONTH);
			LocalDate endOfLocalDate = new LocalDate(temp.getTime() - TimeZone.getDefault().getRawOffset());
			int weeksOfMonth = endOfLocalDate.getWeekOfMonth(1);

			Semaphore semaphore = new Semaphore(weeksOfMonth);
			Thread currentThread = Thread.currentThread();
			TaskInfoParallelProcessing[] taskInfoPP = new TaskInfoParallelProcessing[weeksOfMonth];

			int firstDaysOfNextWeek = 0;
			for(int i=0; i<weeksOfMonth; i++){
				if(i==0){
					taskInfoPP[i] = new TaskInfoParallelProcessing(semaphore, currentThread, contextId, spaceId, cuser, fromDate, new Date(fromDate.getTime()+LocalDate.ONE_DAY*(7-firstDayOfFromWeek+1)), maxSize);
					taskInfoPP[i].start();
					firstDaysOfNextWeek = 7-firstDayOfFromWeek+1;
				}else if(i==weeksOfMonth-1){
					taskInfoPP[i] = new TaskInfoParallelProcessing(semaphore, currentThread, contextId, spaceId, cuser, new Date(fromDate.getTime()+LocalDate.ONE_DAY*(firstDaysOfNextWeek)), toDate, maxSize);
					taskInfoPP[i].start();					
				}else{
					taskInfoPP[i] = new TaskInfoParallelProcessing(semaphore, currentThread, contextId, spaceId, cuser, new Date(fromDate.getTime()+LocalDate.ONE_DAY*firstDaysOfNextWeek), new Date(fromDate.getTime()+LocalDate.ONE_DAY*(firstDaysOfNextWeek+7)), maxSize);
					taskInfoPP[i].start();
					firstDaysOfNextWeek = firstDaysOfNextWeek+7;
					
				}
			}
			
			synchronized (currentThread) {
				currentThread.wait();
			}
			
			TaskInstanceInfo[][] result = new TaskInstanceInfo[weeksOfMonth][];
			for(int i=0; i<weeksOfMonth; i++)
				result[i] = (TaskInstanceInfo[])taskInfoPP[i].getResult();			
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

			if(tempFromDate != null && tempToDate == null) {
				taskWorkCond.setTskExecuteDateBefore(tempFromDate);
			} else {
				taskWorkCond.setTskExecuteDateFrom(tempFromDate);
				taskWorkCond.setTskExecuteDateTo(tempToDate);
			}
			taskWorkCond.setPackageStatus(PkgPackage.STATUS_DEPLOYED);

			long totalCount = getWorkListManager().getTaskWorkListSize(userId, taskWorkCond);

			if(tempFromDate != null && tempToDate == null) {
				taskWorkCond.setOrders(new Order[]{new Order("tskcreatedate", false)});
			} else {
				taskWorkCond.setOrders(new Order[]{new Order("tskcreatedate", true)});
			}

			taskWorkCond.setPageNo(0);
			taskWorkCond.setPageSize(maxSize+1);

			TaskWork[] tasks = getWorkListManager().getTaskWorkList(userId, taskWorkCond);
			if(tasks!=null && tasks.length>maxSize){
				tasks[maxSize] = new TaskWork();
			}
			
			return (TaskInstanceInfo[])ModelConverter.getTaskInstanceInfoArrayByTaskWorkArray(userId, tasks, maxSize);

		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}
	
	@Override
	public TaskInstanceInfo[] getTaskInstancesByTimeline(String contextId, String spaceId, LocalDate fromDate, int maxSize) throws Exception {
		return getTaskInstancesByDate(contextId, spaceId, fromDate, null, maxSize);
	}
	
	@Override
	public InstanceInfo[] getSpaceInstancesByDate(String spaceId, LocalDate fromDate, int maxSize) throws Exception {
		
		InstanceInfo[] instances = 	getSubInstancesInInstance(spaceId, maxSize, fromDate);
		if(instances!=null){
			int length = (instances.length<maxSize) ? instances.length : maxSize;
			InstanceInfo[] resultInstances = new InstanceInfo[instances.length];
			for(int i=0; i<length; i++){
				resultInstances[i] = instances[length-i-1];
			}
			return resultInstances;
		}
		return null;
//		try{
//			User cuser = SmartUtil.getCurrentUser();
//			String userId = null;
//			if (cuser != null)
//				userId = cuser.getId();
//			
//			TaskWorkCond cond = new TaskWorkCond();
//			cond.setTskWorkSpaceId(spaceId);
//			cond.setTskStatus(TskTask.TASKSTATUS_COMPLETE);
//			cond.setOrders(new Order[]{new Order("taskLastModifyDate", false)});
//			cond.setPageSize(maxSize);
//			cond.setTskExecuteDateTo(new LocalDate(fromDate.getGMTDate()));
//			TaskWork[] tasks = getWorkListManager().getTaskWorkList(userId, cond);
//			List<InstanceInfo> instanceInfoList = new ArrayList<InstanceInfo>();
//			List<String> prcInstIdList = new ArrayList<String>();
//			if(!CommonUtil.isEmpty(tasks)) {
//				for (int i = 0; i < tasks.length; i++) {
//					TaskWork task = tasks[i];
//					if (instanceInfoList.size() == 10)
//						break;
//					if (prcInstIdList.contains(task.getTskPrcInstId()))
//						continue;
//					prcInstIdList.add(task.getTskPrcInstId());
//					instanceInfoList.add(ModelConverter.getWorkInstanceInfoByTaskWork(cuser, task));
//				}
//			}
//
//			InstanceInfo[] spaceInstances = null;
//
//			OpinionCond opinionCond = new OpinionCond();
//			opinionCond.setRefId(spaceId);
//			opinionCond.setPageSize(maxSize);
//			Opinion[] opinions = getOpinionManager().getOpinions(userId, opinionCond, IManager.LEVEL_ALL);
//			if(!CommonUtil.isEmpty(opinions)) {
//				int opinionLength = opinions.length;
//				for(int i=0; i<opinionLength; i++) {
//					Opinion opinion = opinions[i];
//					CommentInstanceInfo commentInstanceInfo = new CommentInstanceInfo();
//					String modificationUser = opinion.getModificationUser() == null ? opinion.getCreationUser() : opinion.getModificationUser();
//					Date modificationDate = opinion.getModificationDate() == null ? opinion.getCreationDate() : opinion.getModificationDate();
//					commentInstanceInfo.setId(opinion.getObjId());
//					commentInstanceInfo.setCommentType(CommentInstance.COMMENT_TYPE_ON_WORK_MANUAL);
//					commentInstanceInfo.setComment(opinion.getOpinion());
//					commentInstanceInfo.setCommentor(ModelConverter.getUserInfoByUserId(opinion.getCreationUser()));
//					commentInstanceInfo.setLastModifiedDate(new LocalDate(modificationDate.getTime()));
//					commentInstanceInfo.setType(Instance.TYPE_COMMENT);
//					commentInstanceInfo.setOwner(ModelConverter.getUserInfoByUserId(opinion.getCreationUser()));
//					commentInstanceInfo.setCreatedDate(new LocalDate(opinion.getCreationDate().getTime()));
//					commentInstanceInfo.setLastModifier(ModelConverter.getUserInfoByUserId(modificationUser));
//					instanceInfoList.add(commentInstanceInfo);
//				}
//			}
//			if(instanceInfoList.size() > 0) {
//				Collections.sort(instanceInfoList, Collections.reverseOrder());
//				spaceInstances = new InstanceInfo[instanceInfoList.size()];
//				instanceInfoList.toArray(spaceInstances);
//			}
//
//			if(!CommonUtil.isEmpty(spaceInstances)) {
//				if(spaceInstances.length > maxSize) {
//					List<InstanceInfo> resultInstanceInfoList = new ArrayList<InstanceInfo>();
//					for(int i=0; i<maxSize; i++) {
//						InstanceInfo instanceInfo = spaceInstances[i];
//						resultInstanceInfoList.add(instanceInfo);
//					}
//					resultInstanceInfoList.add(null);
//					spaceInstances = new InstanceInfo[resultInstanceInfoList.size()];
//					resultInstanceInfoList.toArray(spaceInstances);
//				}
//			}
//
//			return spaceInstances;
//		}catch (Exception e){
//			// Exception Handling Required
//			e.printStackTrace();
//			throw e;			
//			// Exception Handling Required			
//		}
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
			//겸직적용
			//DepartmentInfo[] deptInfos = cuser.getDepartments();
			SwoUser user = SwManagerFactory.getInstance().getSwoManager().getUser(userId, userId, IManager.LEVEL_LITE);
			if (user != null && !CommonUtil.isEmpty(user.getAdjunctDeptIds())) {
				String[] ajtDeptInfo = StringUtils.tokenizeToStringArray(user.getAdjunctDeptIds(), ";");
				for (int i = 0; i < ajtDeptInfo.length; i++) {
					String[] ajtDeptIdInfo = StringUtils.tokenizeToStringArray(ajtDeptInfo[i], "|");
					String deptId = ajtDeptIdInfo[0];

					SwoUserCond userCond = new SwoUserCond();
					userCond.setDeptId(deptId);
					userCond.setRoleId("DEPT MEMBER");//모든 부서원들을 가져온다, 내 아디디는 무조건 포함되기 때문에 내가 부서장이면 나머지 부서원, 부서원이면 나머지 부서원을 가져온다
					SwoUser[] relatedUserObjs = getSwoManager().getUsers(userId, userCond, IManager.LEVEL_LITE);
					if (relatedUserObjs != null) {
						for (int j = 0; j < relatedUserObjs.length; j++) {
							SwoUser relatedUserObj = relatedUserObjs[j];
							if (!relatedUserObj.getId().equalsIgnoreCase(userId))
								relatedUserIdArray.add(relatedUserObj.getId());//자기 부서원들을 array에 포함시킨다
						}
					}
					//자기 하위부서의 사람들도 포함시킨다(재귀함수를 이용)
					addSubDepartmentUsers(userId, deptId, relatedUserIdArray);//userDeptId의 자식 부서들의 사용자들을 array에 추가시킨다
				}
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
			cond.setPackageStatus("DEPLOYED");
			long totalSize = getWorkListManager().getCastWorkListSize(userId, cond);

			cond.setPageNo(0);
			cond.setPageSize(maxSize+1);

			cond.setOrders(new Order[]{new Order("tskcreatedate", false)});

			TaskWork[] tasks = getWorkListManager().getCastWorkList(userId, cond);
			if(tasks!=null && tasks.length>maxSize){
				tasks[maxSize] = new TaskWork();
			}
			TaskInstanceInfo[] taskInfos = null;
			if(!CommonUtil.isEmpty(tasks))
				taskInfos = ModelConverter.getTaskInstanceInfoArrayByTaskWorkArray(userId, tasks, maxSize);
			return taskInfos;
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
//Start InstanceInfo Model Changed by ysjung
						//iWInstanceInfo.setWorkSpace(null);
						iWInstanceInfo.setWorkSpaceInfo(null);
						//iWInstanceInfo.setWorkSpaceInfo(workSpaceId, workSpaceName, workSpaceType, workSpaceMinPicture);
//End InstanceInfo Model Changed by ysjung
			
						WorkCategoryInfo groupInfo = null;
						if (!CommonUtil.isEmpty(swdRecordExtends[0].getSubCtgId()))
							groupInfo = new WorkCategoryInfo(swdRecordExtends[0].getSubCtgId(), swdRecordExtends[0].getSubCtg());
			
						WorkCategoryInfo categoryInfo = new WorkCategoryInfo(swdRecordExtends[0].getParentCtgId(), swdRecordExtends[0].getParentCtg());
			
						WorkInfo workInfo = new SmartWorkInfo(formId, formName, SmartWork.TYPE_INFORMATION, groupInfo, categoryInfo);
		
//Start InstanceInfo Model Changed by ysjung
						//iWInstanceInfo.setWork(workInfo);
						iWInstanceInfo.setWorkInfo(workInfo);
						//iWInstanceInfo.setWorkInfo(workId, workName, workType, isWorkRunning, workFullPathName);
//End InstanceInfo Model Changed by ysjung
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
	private void setUcityExtendedProperty(Map<String, Object> requestBody, TskTask obj) throws Exception {
		if (obj == null) 
			return;
		
		String serviceName = (String)requestBody.get("serviceName");
		String eventId = (String)requestBody.get("eventId");
		String eventTime = (String)requestBody.get("eventTime");
		String eventName = (String)requestBody.get("eventName");
		String type = (String)requestBody.get("type");
		String isSms = (String)requestBody.get("isSms");
		String externalDisplay = (String)requestBody.get("externalDisplay");
		String eventPlace = (String)requestBody.get("eventPlace");
		String facilityId = (String)requestBody.get("facilityId");
		String runningTaskName = (String)requestBody.get("runningTaskName");
		
		obj.setExtendedAttributeValue("ucity_serviceName", serviceName);
		obj.setExtendedAttributeValue("ucity_eventId", eventId);
		obj.setExtendedAttributeValue("ucity_eventTime", eventTime);
		obj.setExtendedAttributeValue("ucity_eventName", eventName);
		obj.setExtendedAttributeValue("ucity_type", type);
		obj.setExtendedAttributeValue("ucity_isSms", isSms);
		obj.setExtendedAttributeValue("ucity_externalDisplay", externalDisplay);
		obj.setExtendedAttributeValue("ucity_eventPlace", eventPlace);
		obj.setExtendedAttributeValue("ucity_facilityId", facilityId);
		obj.setExtendedAttributeValue("ucity_runningtaskname", runningTaskName);
	}
	private String executeTask(Map<String, Object> requestBody, HttpServletRequest request, String action) throws Exception {
		User cuser = SmartUtil.getCurrentUser();
		String userId = null;
		if (cuser != null)
			userId = cuser.getId();
		
		if (action == null || action.equalsIgnoreCase("EXECUTE") || action.equalsIgnoreCase("RETURN") ||  action.equalsIgnoreCase("SAVE")) {
			
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

			/*Map<String, Object> frmAccessSpaceMap = (Map<String, Object>)requestBody.get("frmAccessSpace");
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

				if(String.valueOf(AccessPolicy.LEVEL_CUSTOM).equals(accessLevel) && CommonUtil.isEmpty(accessValue)) {
					accessValue = ModelConverter.getAccessValue(userId, (String)requestBody.get("instanceId"));
				}

				task.setWorkSpaceId(workSpaceId);
				task.setWorkSpaceType(workSpaceType);
				task.setAccessLevel(accessLevel);
				task.setAccessValue(accessValue);
			}*/
			if (action.equalsIgnoreCase("save")) {
				getTskManager().setTask(userId, task, IManager.LEVEL_ALL);
			} else {
				//UCITY ucityAdvisor에서 사용할 값을 셋팅한다 
				setUcityExtendedProperty(requestBody, task);

				getTskManager().executeTask(userId, task, action);

				//자동채번을 위하여 아래를 호출한다
				populateAutoIndexField(userId, TskTask.TASKTYPE_COMMON, formId, task.getProcessInstId(), recordObj);
				
 				SmartUtil.removeNoticeByExecutedTaskId(task.getAssignee(), task.getObjId());
			}
			if (logger.isInfoEnabled()) {
				logger.info(action + " Task Done [processInstanceId : " + (String)requestBody.get("instanceId") + ", " + (String)requestBody.get("formName") + "( taskId : " + (String)requestBody.get("taskInstId") + ")] ");
			}
			List<String> groupIdList = new ArrayList<String>();
			if(fileGroupMap.size() > 0) {
				for(Map.Entry<String, List<Map<String, String>>> entry : fileGroupMap.entrySet()) {
					String fileGroupId = entry.getKey();
					groupIdList.add(fileGroupId);
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
			if (groupIdList.size() != 0) {
				for (int i = 0; i < groupIdList.size(); i++) {
					if (CommonUtil.isEmpty(groupIdList.get(i)))
						continue;
					String tempGroupId = groupIdList.get(i);
					List<IFileModel> iFileModelList = getDocManager().findFileGroup(tempGroupId);
					if(iFileModelList.size() > 0) {
						for(int j=0; j<iFileModelList.size(); j++) {
							IFileModel fileModel = iFileModelList.get(j);
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
			}
			/*if(groupId != null) {
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
			}*/

			return taskInstId;
		}  else if (action.equalsIgnoreCase("delegate")) {
			/*{
				   workId=pkg_26f24cff544f4f2e9a1b87599b6d041a,
				   instanceId=ff8080813904bc93013904c459b70007,
				   taskInstId=ff8080813904bc93013904c45be60008,
				   newPerformer=   [
				      {
				         id=kmyu@maninsoft.co.kr,
				         name=선임 연구원 유광민
				      }
				   ]
				}*/
			String taskInstId = (String)requestBody.get("taskInstId");
			String delegateUserId = "";
			List<Map<String, String>> newPerformer = (List<Map<String, String>>)requestBody.get("newPerformer");
			if (newPerformer != null && newPerformer.size() != 0) {
				Map<String, String> userMap = newPerformer.get(0);
				delegateUserId = userMap.get("id");
			}
			
			if (CommonUtil.isEmpty(taskInstId) || CommonUtil.isEmpty(delegateUserId))
				return null;
			
			TskTask task = getTskManager().getTask(userId, taskInstId, IManager.LEVEL_ALL);
			
			if (task == null || !task.getAssignee().equalsIgnoreCase(userId))
				throw new Exception("Task("+taskInstId+") Is Null Or Mismatch Between AssigneeId("+task.getAssignee()+") And PerformerId("+userId+")");
			
			task.setAssignee(delegateUserId);
			
			if (logger.isInfoEnabled())
				logger.info("Delegate Task "+ task.getName() +"("+taskInstId+") From " + userId + " To " + delegateUserId);

			/*Map<String, Object> frmAccessSpaceMap = (Map<String, Object>)requestBody.get("frmAccessSpace");
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

				if(String.valueOf(AccessPolicy.LEVEL_CUSTOM).equals(accessLevel) && CommonUtil.isEmpty(accessValue)) {
					accessValue = ModelConverter.getAccessValue(userId, (String)requestBody.get("instanceId"));
				}

				task.setWorkSpaceId(workSpaceId);
				task.setWorkSpaceType(workSpaceType);
				task.setAccessLevel(accessLevel);
				task.setAccessValue(accessValue);
			}*/

			getTskManager().setTask(userId, task, IManager.LEVEL_ALL);
			return taskInstId;
		} else if (action.equalsIgnoreCase("ABEND")) {
			
			String taskInstId = (String)requestBody.get("taskInstId");
			if (CommonUtil.isEmpty(taskInstId))
				return null;
			
			TskTask task = getTskManager().getTask(userId, taskInstId, IManager.LEVEL_ALL);
			task.setStatus(TskTask.TASKSTATUS_ABORTED);
			task.setExecutionDate(new LocalDate());
			task.setStartDate(new LocalDate());
			task.setRealEndDate(new LocalDate());
			task.setPerformer(userId);
			String prcInstId = task.getProcessInstId();
			PrcProcessInst prcInst = getPrcManager().getProcessInst(userId, prcInstId, IManager.LEVEL_ALL);
			prcInst.setStatus(PrcProcessInst.PROCESSINSTSTATUS_ABORTED);
			getPrcManager().setProcessInst(userId, prcInst, IManager.LEVEL_ALL);
			getTskManager().setTask(userId, task, IManager.LEVEL_ALL);
			
			TskTaskCond runningTaskCond = new TskTaskCond();
			runningTaskCond.setProcessInstId(prcInstId);
			runningTaskCond.setType(TskTask.TASKTYPE_COMMON);
			runningTaskCond.setStatus(TskTask.TASKSTATUS_ASSIGN);
			TskTask[] runningTasks = getTskManager().getTasks(userId, runningTaskCond, IManager.LEVEL_ALL);
			if (runningTasks != null && runningTasks.length != 0) {
				for (int i = 0; i < runningTasks.length; i++) {
					TskTask runningTask = runningTasks[i];
					runningTask.setStatus(TskTask.TASKSTATUS_COMPLETE);
					getTskManager().setTask(userId, runningTask, IManager.LEVEL_ALL);
				}
			}
			return taskInstId;
		} else if (action.equalsIgnoreCase("create")) {
			//이미 생성된 인스턴스에 새로운 태스크를 생성하여 수행한다 기존에 생성되어 흘러가던 태스크와 별개로
			//업무태스크가 생서되어 흘러간다
			
			//인스턴스를 조회한다
			String instanceId = (String)requestBody.get("instanceId");
			String formId = (String)requestBody.get("formId");
			
			//인스턴스의 상태를 조회한후 완료된 건이라면 상태를 다시 실행중으로 변경하여 태스크를 진행한다
			
			PrcProcessInst prcInst = SwManagerFactory.getInstance().getPrcManager().getProcessInst(userId, instanceId, IManager.LEVEL_ALL);
			if (prcInst == null)
				throw new Exception("Not Exist Process Instance : instancId = " + instanceId);
			
			if (!prcInst.getStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_RUNNING)) {
				prcInst.setStatus(PrcProcessInst.PROCESSINSTSTATUS_RUNNING);
			}
			String processId = prcInst.getProcessId();
			
			//formId 로 실행할 태스크를 조회한다
			TskTaskDefCond tskDefCond = new TskTaskDefCond();
			tskDefCond.setForm(formId);
			TskTaskDef[] taskDefs = SwManagerFactory.getInstance().getTskManager().getTaskDefs(userId, tskDefCond, IManager.LEVEL_ALL);
			if (taskDefs == null || taskDefs.length == 0)
				throw new Exception("Not Exist Task Definition : formId = " + formId);
			
			TskTaskDef taskDef = taskDefs[0];
			
			//새로운 태스크를 생성한다
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
			
			String title = null;
			if (!CommonUtil.isEmpty(taskDef.getExtendedPropertyValue("subjectFieldId"))) {
				title = CommonUtil.toNotNull(recordObj.getDataFieldValue(taskDef.getExtendedPropertyValue("subjectFieldId")));
			} else {
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
			task.setIsStartActivity("false");
			
			task.setProcessInstId(instanceId);

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

				if(String.valueOf(AccessPolicy.LEVEL_CUSTOM).equals(accessLevel) && CommonUtil.isEmpty(accessValue)) {
					accessValue = ModelConverter.getAccessValue(userId, processId);
				}

				task.setWorkSpaceId(workSpaceId);
				task.setWorkSpaceType(workSpaceType);
				task.setAccessLevel(accessLevel);
				task.setAccessValue(accessValue);
				
				//프로세스업무의 시작 공간 위치가 비공개 그룹이라면 해당 업무는 비공개 그룹원들에게만 공개가 된다
				populatePrivateGroupAuth(userId, task);
				
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
			
			//참조자, 전자결재, 연결업무 정보를 셋팅한다
			setReferenceApprovalToTask(userId, task, requestBody);
			
			//UCITY ucityAdvisor에서 사용할 값을 셋팅한다 
			setUcityExtendedProperty(requestBody, task);

			//태스크를 실행하며 프로세스업무를 실행한다
			task = getTskManager().executeTask(userId, task, "execute");

			//자동채번을 위하여 아래를 호출한다
			populateAutoIndexField(userId, TskTask.TASKTYPE_COMMON, formId, task.getProcessInstId(), recordObj);
			
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
		}
		return null;
	}

	@Override
	public String createTaskInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		return executeTask(requestBody, request, "create");
	}
	@Override
	public String performTaskInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		return executeTask(requestBody, request, "execute");
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
	public String abendTaskInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		return executeTask(requestBody, request, "abend");
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
			UserInfo senderInfo = ModelConverter.getUserInfoByUserId(message.getSendUser());
			chatInstInfo.setSenderLongName(senderInfo.getLongName());
			chatInstInfo.setSenderNickName(senderInfo.getNickName());
			chatInstInfo.setSenderMinPicture(senderInfo.getMinPicture());
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
		Approval[] aprs = new Approval[apraprs.length + 1];
		
		//기안 단계(ex : 정보관리 작성시 생성되는 SINGLE, COMMON 태스크를 조회 하여 기안 단계의 approval 을 포함하여 리턴한다)
		String taskId = aprLine.getCorrelation();
		TskTask task = getTskManager().getTask(user.getId(), taskId, IManager.LEVEL_LITE);
		
		if (task == null)
			throw new Exception("Not Exist Draft Task!! taskId : " + taskId);
		
		Approval draftApr = new Approval();
		
		PrcProcessInst prcInst = getPrcManager().getProcessInst(user.getId(), task.getProcessInstId(), IManager.LEVEL_LITE);
		if (prcInst != null && !prcInst.getStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_RETURN)) {
			draftApr.setStatus(Instance.STATUS_DRAFTED);
			draftApr.setCompletedDate(new LocalDate(task.getCreationDate().getTime()));
		} else {
			draftApr.setStatus(Instance.STATUS_RUNNING);
			draftApr.setCompletedDate(null);
		}
		draftApr.setName(SmartMessage.getString("approval.title.draft"));
		draftApr.setApproverType(Approval.APPROVER_CHOOSE_ON_RUNNING);
		draftApr.setApprover(ModelConverter.getUserByUserId(task.getAssignee()));
		//draftApr.setDueDate("");
		draftApr.setMandatory(true);
		draftApr.setModifiable(true);
		aprs[0] = draftApr;
		
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
			
			aprs[i + 1] = apr;
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
		} else if (result.equalsIgnoreCase("returned")) {
			//반려
			action = "return";
		} else if (result.equalsIgnoreCase("rejected")) {
			//기각
			action = "cancel";
		} else if (result.equalsIgnoreCase("submited")) {
			//재상신 - 재상신 할경우 수정된 정보관리 폼정보가 온다
			/*{
				   workInstId=dr_402880eb385652400138565240100000,
				   approvalInstId=402880eb38563b3e0138565240350001,
				   taskInstId=402880eb38563b3e0138565276190008,
				   comments=ㅇㄹㄴㄹ,
				   result=submited,
				   formId=frm_board_SYSTEM,
				   formName=게시판,
				   frmSmartForm=   {
				      2      =321,
				      4      =중,
				      6      =321123,
				      14      =      {
				         groupId=fg_e8b797308e397847838a2668ab682d36f2ac,
				         files=         [

				         ]
				      }
				   }
				}*/
		
			String formId = (String)requestBody.get("formId");
			String instanceId = (String)requestBody.get("workInstId");

			SwfFormCond formCond = new SwfFormCond();
			formCond.setId(formId);
			SwfForm[] forms = getSwfManager().getForms(user.getId(), formCond, IManager.LEVEL_LITE);
			String packageId = forms[0].getPackageId();
			
			Map<String, Object> saveWorkInstance = new LinkedHashMap<String, Object>();
			saveWorkInstance.put("workId", packageId);
			saveWorkInstance.put("instanceId", instanceId);
			saveWorkInstance.put("formId", formId);
			saveWorkInstance.put("formName", requestBody.get("formName"));
			saveWorkInstance.put("frmSmartForm", requestBody.get("frmSmartForm"));
			
			
			Map<String, Object> frmAccessSpaceMap = new LinkedHashMap<String, Object>();
			frmAccessSpaceMap.put("selWorkSpace", task.getWorkSpaceId());
			frmAccessSpaceMap.put("selWorkSpaceType", task.getWorkSpaceType());
			frmAccessSpaceMap.put("selAccessLevel", task.getAccessLevel());
			
			String usersStr = task.getAccessValue();
			if (!CommonUtil.isEmpty(usersStr)) {
				String[] usersArray = StringUtils.tokenizeToStringArray(usersStr, ";");
				
				ArrayList<Map<String,String>> userList = new ArrayList<Map<String,String>>();
				for (int i = 0; i < usersArray.length; i++) {
					Map<String, String> userInfoMap = new LinkedHashMap<String, String>();
					userInfoMap.put("id", usersArray[i]);
					userList.add(userInfoMap);
				}
				frmAccessSpaceMap.put("txtAccessableUsers", userList);
			}
			saveWorkInstance.put("frmAccessSpace", frmAccessSpaceMap);
			
			setInformationWorkInstance(saveWorkInstance, request);
			
			action = "execute";
			
		} else {	
			throw new Exception("Approval Task Failed : Action Is Null");
		}
		
		getTskManager().executeTask(userId, task, action);
		SmartUtil.removeNoticeByExecutedTaskId(task.getAssignee(), task.getObjId());
		
	}
	@Override
	public void forwardIworkInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		/*{
			   workId=pkg_309666dd2bb5493c9d7e618b3a0aad96,
			   instanceId=dr_402880eb385121e701385122faa40006,
			   frmTaskForward=   {
			      txtForwardSubject=전달제목,
			      txtForwardComments=전달 내용,
			      txtForwardForwardee=      {
			         users=         [
			            {
			               id=kmyu@maninsoft.co.kr,
			               name=1 유광민
			            }
			         ]
			      }
			   }
			}*/

		String userId = SmartUtil.getCurrentUser().getId();

		Map<String, Object> frmTaskForwardMap = (Map<String, Object>)requestBody.get("frmTaskForward");
		Map<String, Object> frmSmartForm = (Map<String, Object>)requestBody.get("frmSmartForm");

		String workId = (String)requestBody.get("workId");
		String instanceId = (String)requestBody.get("instanceId");
		
		if (frmSmartForm != null) {
			requestBody.put("makeNewNotClone", "true");
			setInformationWorkInstance(requestBody, request);
		} else {

			//제목
			String txtForwardSubject = (String)frmTaskForwardMap.get("txtForwardSubject");
			//설명
			String txtForwardComments = (String)frmTaskForwardMap.get("txtForwardComments");
			
			Map<String, Object> txtForwardForwardeeMap = (Map<String, Object>)frmTaskForwardMap.get("txtForwardForwardee");
			
			ArrayList<Map<String,String>> userArray = (ArrayList<Map<String,String>>)txtForwardForwardeeMap.get("users");
			
			String[] refUsers = SmartUtil.getAllUserIdsFromUserArray(userArray);
			if(CommonUtil.isEmpty(refUsers)) {
				return;
			}
			
			//레코드를 조회한다
			SwfFormCond formCond = new SwfFormCond();
			formCond.setPackageId(workId);
			SwfForm[] forms = getSwfManager().getForms(userId, formCond, IManager.LEVEL_LITE);
			if (forms == null || forms.length > 1)
				throw new Exception("Not Exist Form! Or More Then 1 Forms! - packageId : " + workId );
			
			String formId = forms[0].getId();
			
			SwdDomainCond domainCond = new SwdDomainCond();
			domainCond.setFormId(formId);
			SwdDomain domain = getSwdManager().getDomain(userId, domainCond, IManager.LEVEL_LITE);
			
			if (domain == null)
				throw new Exception("Not Exist Domain! - formId : " + formId);
			
			SwdRecord record = getSwdManager().getRecord(userId, domain.getObjId(), instanceId, IManager.LEVEL_ALL);
			if (record == null)
				throw new Exception("Not Exist Record! - formId : " + formId + ", recordId : " + instanceId);
			
			//정보관리 작성시에 생성된 태스크 및 업무인스턴스(prcprcinst)를 조회 한다.
			String recordId = record.getRecordId();
			PrcProcessInstCond prcInstCond = new PrcProcessInstCond(); 
			prcInstCond.setExtendedProperties(new Property[]{new Property("recordId", recordId)});
			
			PrcProcessInst prcInst = getPrcManager().getProcessInst(userId, prcInstCond, IManager.LEVEL_ALL);
			
			//instance가 없다면 setRecord를 기존 레코드를 이용해 한번한후
			//instance및 task가 생성된 후 참조를 보낸다
			
			if (prcInst == null) {
				SwdRecord recordOfNoTask = getSwdManager().getRecord(userId, domain.getObjId(), instanceId, IManager.LEVEL_ALL);
				getSwdManager().setRecord(recordOfNoTask.getCreationUser(), recordOfNoTask, IManager.LEVEL_ALL);
				prcInst = getPrcManager().getProcessInst(recordOfNoTask.getCreationUser(), prcInstCond, IManager.LEVEL_ALL);
			}
			
			//prcInst.setStatus(PrcProcessInst.PROCESSINSTSTATUS_RUNNING);
			prcInst.setTitle(txtForwardSubject);
			getPrcManager().setProcessInst(userId, prcInst, IManager.LEVEL_ALL);
			
			String prcInstId = prcInst.getObjId();

			TskTaskCond tskCond = new TskTaskCond();
			tskCond.setProcessInstId(prcInstId);
			tskCond.setTypeIns(new String[]{TskTask.TASKTYPE_SINGLE});
			tskCond.setOrders(new Order[]{new Order(TskTask.A_CREATIONDATE, false)});
			
			TskTask[] tasks = getTskManager().getTasks(userId, tskCond, IManager.LEVEL_LITE);
			if (tasks == null || tasks.length == 0)
				throw new Exception("Not Exist Single Tasks - processInstId : " + prcInstId );
			
			//참조자가 존재한다면 참조타입의 태스크를 생성
			
			TskTask refTask = null;
			Set refUserSet = new HashSet();
			String refUser = null;
			
			String forwardId = "fwd_" + CommonUtil.newId();
			
			for (int i = 0; i < refUsers.length; i++) {
				refUser = refUsers[i];
				if (refUserSet.contains(refUser))
					continue;
				
				refTask = new TskTask();
				refTask.setProcessInstId(prcInstId);
				refTask.setType(CommonUtil.toDefault((String)MisUtil.taskDefTypeMap().get("reference"), "reference"));
				//refTask.setPriority(priority);
//						refTask.setTitle(obj.getTitle());
				refTask.setTitle(txtForwardSubject);
				//refTask.setName(obj.getName());
				refTask.setName(txtForwardSubject);
				refTask.setAssigner(userId);
				refTask.setAssignee(refUser);
				refTask.setAssignmentDate(new LocalDate());
				refTask.setStartDate(new LocalDate());
				refTask.setForm(formId);
				refTask.setDef(tasks[0].getDef());
				refTask.setFromRefId(tasks[0].getObjId());
				refTask.setFromRefType(tasks[0].getType());
				refTask.setForwardId(forwardId);
				refTask.setExtendedPropertyValue("subject", txtForwardSubject);
				refTask.setExtendedPropertyValue("taskRef", tasks[0].getObjId());
				refTask.setExtendedPropertyValue("workContents", txtForwardComments);
				//refTask.setExtendedPropertyValue("projectName", projectName);
				
				refTask.setWorkSpaceId(record.getWorkSpaceId());
				refTask.setWorkSpaceType(record.getWorkSpaceType());
				refTask.setAccessLevel(record.getAccessLevel());
				refTask.setAccessValue(record.getAccessValue());
				
				this.getTskManager().setTask(userId, refTask, null);
				
				PublishNotice pubNoticeObj = new PublishNotice(refUser, PublishNotice.TYPE_ASSIGNED, PublishNotice.REFTYPE_ASSIGNED_TASK, refTask.getObjId());
				SwManagerFactory.getInstance().getPublishNoticeManager().setPublishNotice("linkadvisor", pubNoticeObj, IManager.LEVEL_ALL);
				SmartUtil.increaseNoticeCountByNoticeType(refUser, Notice.TYPE_ASSIGNED);
				
				if (logger.isInfoEnabled()) {
					logger.info("Assigned Reference Task [ " + txtForwardSubject + " ( Process Instance Id : " + refTask.getProcessInstId() + " , To User : " + refTask.getAssignee() + ")]");
				}
				refUserSet.add(refUser);
			}
		}
		
	}
	
	public void approvalIworkInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		/*{
			   workId=pkg_309666dd2bb5493c9d7e618b3a0aad96,
			   instanceId=dr_402880eb385121e701385122faa40006,
			   frmApprovalLine=   {
			      hdnApprovalLineId=system.approvalLine.default.3level,
			      usrLevelApprover1=kmyu@maninsoft.co.kr,
			      usrLevelApprover2=kmyu@maninsoft.co.kr,
			      usrLevelApprover3=kmyu@maninsoft.co.kr
			   },
			   frmTaskApproval=   {
			      txtApprovalSubject=기안제목,
			      txtApprovalComments=기안 내용,
			      txtApprovalForwardee=      {
			         users=         [
			            {
			               id=kmyu@maninsoft.co.kr,
			               name=1 유광민
			            },
			            {
			               id=hsshin@maninsoft.co.kr,
			               name=신현성
			            }
			         ]
			      }
			   }
			}*/
		
		String userId = SmartUtil.getCurrentUser().getId();

		String workId = (String)requestBody.get("workId");
		String instanceId = (String)requestBody.get("instanceId");
		
		//레코드를 조회한다
		SwfFormCond formCond = new SwfFormCond();
		formCond.setPackageId(workId);
		SwfForm[] forms = getSwfManager().getForms(userId, formCond, IManager.LEVEL_LITE);
		if (forms == null || forms.length > 1)
			throw new Exception("Not Exist Form! Or More Then 1 Forms! - packageId : " + workId );
		
		String formId = forms[0].getId();
		
		SwdDomainCond domainCond = new SwdDomainCond();
		domainCond.setFormId(formId);
		SwdDomain domain = getSwdManager().getDomain(userId, domainCond, IManager.LEVEL_LITE);
		
		if (domain == null)
			throw new Exception("Not Exist Domain! - formId : " + formId);
		
		SwdRecord record = getSwdManager().getRecord(userId, domain.getObjId(), instanceId, IManager.LEVEL_ALL);
		if (record == null)
			throw new Exception("Not Exist Record! - formId : " + formId + ", recordId : " + instanceId);
		
		String recordId = record.getRecordId();
		PrcProcessInstCond prcInstCond = new PrcProcessInstCond(); 
		prcInstCond.setExtendedProperties(new Property[]{new Property("recordId", recordId)});

		Map<String, Object> frmTaskApproval = (Map<String, Object>)requestBody.get("frmTaskApproval");
		String txtApprovalSubject = (String)frmTaskApproval.get("txtApprovalSubject");
		
		PrcProcessInst prcInst = getPrcManager().getProcessInst(userId, prcInstCond, IManager.LEVEL_ALL);

		//instance가 없다면 setRecord를 기존 레코드를 이용해 한번한후
		//instance및 task가 생성된 후 전자결재를 보낸다
		if (prcInst == null) {
			SwdRecord recordOfNoTask = getSwdManager().getRecord(userId, domain.getObjId(), instanceId, IManager.LEVEL_ALL);
			getSwdManager().setRecord(userId, recordOfNoTask, IManager.LEVEL_ALL);
			prcInst = getPrcManager().getProcessInst(userId, prcInstCond, IManager.LEVEL_ALL);
		}
		
		prcInst.setStatus(PrcProcessInst.PROCESSINSTSTATUS_RUNNING);
		prcInst.setTitle(txtApprovalSubject);
		getPrcManager().setProcessInst(userId, prcInst, IManager.LEVEL_ALL);
		
		requestBody.put("makeNewNotClone", "true");
		
		setInformationWorkInstance(requestBody, request);
		
//		setReferenceApprovalToRecord(userId, record, requestBody);
//		record.setExtendedAttributeValue("makeNewNotClon", "true");
//		getSwdManager().setRecord(userId, record, IManager.LEVEL_ALL);
		
		
		
	}
	@Override
	public void forwardPworkInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		/*{
			   workId=pkg_cf3b0087995f4f99a41c93e2fe95b22d,
			   instanceId=402880eb3865a03b013865a0ee9d0001,
			   formId=frm_c19b1fe4bceb4732acbb8a4cd2a57474,
			   formName=기안품의,
			   frmTaskForward=   {
			      txtForwardSubject=111111111,
			      txtForwardComments=111111111,
			      txtForwardForwardee=      {
			         users=         [
			            {
			               id=hsshin@maninsoft.co.kr,
			               name=신현성
			            }
			         ]
			      }
			   }
			}*/

		String userId = SmartUtil.getCurrentUser().getId();
		
		Map<String, Object> frmTaskForwardMap = (Map<String, Object>)requestBody.get("frmTaskForward");
		

		String instanceId = (String)requestBody.get("instanceId");
		
		//제목
		String txtForwardSubject = (String)frmTaskForwardMap.get("txtForwardSubject");
		//설명
		String txtForwardComments = (String)frmTaskForwardMap.get("txtForwardComments");
		
		Map<String, Object> txtForwardForwardeeMap = (Map<String, Object>)frmTaskForwardMap.get("txtForwardForwardee");
		
		ArrayList<Map<String,String>> userArray = (ArrayList<Map<String,String>>)txtForwardForwardeeMap.get("users");
		
		String[] refUsers = SmartUtil.getAllUserIdsFromUserArray(userArray);
		if(CommonUtil.isEmpty(refUsers)) {
			return;
		}
		
		PrcProcessInst prcInst = getPrcManager().getProcessInst(userId, instanceId, IManager.LEVEL_ALL);
		
		TskTaskCond tskCond = new TskTaskCond();
		tskCond.setProcessInstId(instanceId);
		tskCond.setTypeIns(new String[]{TskTask.TASKTYPE_COMMON});
		tskCond.setIsStartActivity("true");
		tskCond.setOrders(new Order[]{new Order(TskTask.A_CREATIONDATE, true)});
		
		TskTask[] tasks = getTskManager().getTasks(userId, tskCond, IManager.LEVEL_LITE);
		if (tasks == null || tasks.length == 0)
			throw new Exception("Not Exist Start Task - processInstId : " + instanceId );
		
		//참조자가 존재한다면 참조타입의 태스크를 생성
		
		TskTask refTask = null;
		Set refUserSet = new HashSet();
		String refUser = null;
		
		String forwardId = "fwd_" + CommonUtil.newId();
		
		for (int i = 0; i < refUsers.length; i++) {
			refUser = refUsers[i];
			if (refUserSet.contains(refUser))
				continue;
			
			refTask = new TskTask();
			refTask.setProcessInstId(instanceId);
			refTask.setType(CommonUtil.toDefault((String)MisUtil.taskDefTypeMap().get("reference"), "reference"));
			//refTask.setPriority(priority);
//					refTask.setTitle(obj.getTitle());
			refTask.setTitle(txtForwardSubject);
			//refTask.setName(obj.getName());
			refTask.setName(txtForwardSubject);
			refTask.setAssigner(userId);
			refTask.setAssignee(refUser);
			refTask.setAssignmentDate(new LocalDate());
			refTask.setStartDate(new LocalDate());
			refTask.setForm(tasks[0].getForm());
			refTask.setDef(tasks[0].getDef());
			refTask.setFromRefId(tasks[0].getObjId());
			refTask.setFromRefType(tasks[0].getType());
			refTask.setForwardId(forwardId);
			refTask.setExtendedPropertyValue("subject", txtForwardSubject);
			refTask.setExtendedPropertyValue("taskRef", tasks[0].getObjId());
			refTask.setExtendedPropertyValue("workContents", txtForwardComments);
			//refTask.setExtendedPropertyValue("projectName", projectName);
			
			refTask.setWorkSpaceId(prcInst.getWorkSpaceId());
			refTask.setWorkSpaceType(prcInst.getWorkSpaceType());
			refTask.setAccessLevel(prcInst.getAccessLevel());
			refTask.setAccessValue(prcInst.getAccessValue());
			
			this.getTskManager().setTask(userId, refTask, null);
			
			PublishNotice pubNoticeObj = new PublishNotice(refUser, PublishNotice.TYPE_ASSIGNED, PublishNotice.REFTYPE_ASSIGNED_TASK, refTask.getObjId());
			SwManagerFactory.getInstance().getPublishNoticeManager().setPublishNotice("linkadvisor", pubNoticeObj, IManager.LEVEL_ALL);
			SmartUtil.increaseNoticeCountByNoticeType(refUser, Notice.TYPE_ASSIGNED);
			
			if (logger.isInfoEnabled()) {
				logger.info("Assigned Reference Task [ " + txtForwardSubject + " ( Process Instance Id : " + refTask.getProcessInstId() + " , To User : " + refTask.getAssignee() + ")]");
			}
			refUserSet.add(refUser);
		}
		
	}
	@Override
	public void approvalPworkInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		/*{
			   "workId":"pkg_cf3b0087995f4f99a41c93e2fe95b22d",
			   "instanceId":"402880eb38678bbd013867b18ca60060",
			   "frmApprovalLine":{
			      "hdnApprovalLineId":"system.approvalLine.default.3level",
			      "usrLevelApprover1":"kmyu@maninsoft.co.kr",
			      "usrLevelApprover2":"kmyu@maninsoft.co.kr",
			      "usrLevelApprover3":"kmyu@maninsoft.co.kr"
			   },
			   "frmTaskApproval":{
			      "txtApprovalSubject":"333 ",
			      "txtApprovalComments":"333",
			      "txtApprovalForwardee":{
			         "users":[

			         ]
			      }
			   },
			   "formId":"frm_b6ce4e4827f8454eb975cf485172a1cb",
			   "formName":"대표이사승인",
			   "frmSmartForm":{
			      "4":"222ㄹㄹ",
			      "12":"승인",
			      "16":{
			         "users":[
			            {
			               "id":"kmyu@maninsoft.co.kr",
			               "name":"\n\t\t\t\t\t\t\t\t1 유광민\n\t\t\t\t\t\t\t"
			            }
			         ]
			      }
			   }
			}*/
		

		String userId = SmartUtil.getCurrentUser().getId();

		String workId = (String)requestBody.get("workId");
		String instanceId = (String)requestBody.get("instanceId");
		String formId = (String)requestBody.get("formId");
		
		TskTaskCond taskCond = new TskTaskCond();
		taskCond.setProcessInstId(instanceId);
		taskCond.setAssignee(userId);
		taskCond.setForm(formId);
		taskCond.setStatus(TskTask.TASKSTATUS_ASSIGN);
		
		TskTask[] tasks = getTskManager().getTasks(userId, taskCond, IManager.LEVEL_ALL);
		
		if (tasks == null || tasks.length == 0 || tasks.length != 1)
			throw new Exception("More than 1 task Or Not Exist task");
		
		TskTask task = tasks[0];
		
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
			logger.info("Start Approval Task [processInstanceId : " + (String)requestBody.get("instanceId") + ", " + (String)requestBody.get("formName") + "( taskId : " + (String)requestBody.get("taskInstId") + "), document : " + recordObj.toString() + " ] ");
		}
		//태스크의 실제 완료 시간을 입력한다
		if (task.getRealStartDate() == null)
			task.setRealStartDate(new LocalDate(new Date().getTime()));
		task.setRealEndDate(new LocalDate(new Date().getTime()));
		//태스크를 실행한다

		setReferenceApprovalToTask(userId, task, requestBody);
		
		getTskManager().executeTask(userId, task, "execute");
		
		SmartUtil.removeNoticeByExecutedTaskId(task.getAssignee(), task.getObjId());
		
		if (logger.isInfoEnabled()) {
			logger.info("Start Approval Task Done [processInstanceId : " + (String)requestBody.get("instanceId") + ", " + (String)requestBody.get("formName") + "( taskId : " + (String)requestBody.get("taskInstId") + ")] ");
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
						getDocManager().insertFiles("Files", task.getObjId(), fileGroupId, fileId, fileName, fileSize);
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
	}
	@Override
	public EventInstanceInfo[] getCommingEventInstances(String spaceId, int maxLength) throws Exception {
		return calendarService.getEventInstanceInfosByWorkSpaceId(spaceId, new LocalDate(), null, 5);
	}
	@Override
	public BoardInstanceInfo[] getRecentBoardInstances(String spaceId, int maxLength) throws Exception {
		return getBoardInstancesByWorkSpaceId(spaceId, maxLength);
	}
	private void createNewFolder(Map<String, Object> requestBody, HttpServletRequest request, String type) throws Exception {
		try {
			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();
			String companyId = cUser.getCompanyId();

			String workSpaceId = (String)requestBody.get("workSpaceId");
			
			Map<String, Object> frmNewFileFolder = null;
			if (type.equalsIgnoreCase(TskTask.TASKREFTYPE_IMAGE)) {
				frmNewFileFolder = (Map<String, Object>)requestBody.get("frmNewImageFolder");
			} else if (type.equalsIgnoreCase(TskTask.TASKREFTYPE_FILE)) {
				frmNewFileFolder = (Map<String, Object>)requestBody.get("frmNewFileFolder");
			}
			Set<String> keySet = frmNewFileFolder.keySet();
			Iterator<String> itr = keySet.iterator();

			String txtFolderName = null; 

			while (itr.hasNext()) {
				String fieldId = (String)itr.next();
				Object fieldValue = frmNewFileFolder.get(fieldId);
				if(fieldValue instanceof String) {
					String valueString = (String)fieldValue;
					if(fieldId.equals("txtFolderName"))
						txtFolderName = valueString;
				}
			}

			FdrFolderCond fdrFolderCond = new FdrFolderCond();
			fdrFolderCond.setCompanyId(companyId);
			fdrFolderCond.setParentId(FdrFolder.CATEGORY_ROOT_NAME_PKG);
			fdrFolderCond.setCreationUser(userId);

			long folderCount = getFdrManager().getFolderSize(userId, fdrFolderCond);

			FdrFolder fdrFolder = new FdrFolder();
			fdrFolder.setCompanyId(companyId);
			fdrFolder.setParentId(FdrFolder.CATEGORY_ROOT_NAME_PKG); //TO-DO : 폴더하위구조 구현 시 parentId 받아서 처리
			fdrFolder.setName(txtFolderName);
			//fdrFolder.setDescription(description); TO-DO : 폴더에 대한 설명
			fdrFolder.setDisplayOrder((int)folderCount);
			fdrFolder.setWorkspaceId(workSpaceId);
			fdrFolder.setRefType(type);

			getFdrManager().createFolder(userId, fdrFolder);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
	}
	private void setFolder(Map<String, Object> requestBody, HttpServletRequest request, String type) throws Exception {
		User cUser = SmartUtil.getCurrentUser();
		String userId = cUser.getId();
		String workSpaceId = (String)requestBody.get("workSpaceId");
		String folderId = (String)requestBody.get("folderId");
		Map frmNewFolderMap = null;
		if (type.equalsIgnoreCase(TskTask.TASKREFTYPE_FILE)) {
			frmNewFolderMap = (Map)requestBody.get("frmNewFileFolder");
		} else if (type.equalsIgnoreCase(TskTask.TASKREFTYPE_IMAGE)) {
			frmNewFolderMap = (Map)requestBody.get("frmNewImageFolder");
		}
		String newFolderName = (String)frmNewFolderMap.get("txtFolderName");
		
		if (CommonUtil.isEmpty(newFolderName) || CommonUtil.isEmpty(folderId) || CommonUtil.isEmpty(frmNewFolderMap))
			return;
		
		FdrFolder folder = getFdrManager().getFolder(userId, folderId, null);
		if (CommonUtil.isEmpty(folder))
			return;
		
		folder.setName(newFolderName);
		getFdrManager().setFolder(userId, folder, null);
	}
	private void removeFolder(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		//{workSpaceId=kmyu@maninsoft.co.kr, parentId=AllFiles, folderId=402880eb39619295013961a176070005}
		String workSpaceId = (String)requestBody.get("workSpaceId");
		String parentId = (String)requestBody.get("parentId");
		String folderId = (String)requestBody.get("folderId");

		User cUser = SmartUtil.getCurrentUser();
		String userId = cUser.getId();
		
		if (CommonUtil.isEmpty(folderId))
			return;
		
		FdrFolder folder = getFdrManager().getFolder(userId, folderId, null);
		
		if (CommonUtil.isEmpty(folder))
			return;
		
		getFdrManager().removeFolder(userId, folderId);
		
	}
	private void moveFile(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		//{workSpaceId=kmyu@maninsoft.co.kr, tagetFolderId=402880eb39619295013961a176070005, instanceIds=[file_4028801a395699310139569931090000]}
		
		String workSpaceId = (String)requestBody.get("workSpaceId");
		String tagetFolderId = (String)requestBody.get("tagetFolderId");
		List fileIds = (List)requestBody.get("instanceIds");
		
		if (fileIds == null || fileIds.size() == 0)
			return;
		
		User cUser = SmartUtil.getCurrentUser();
		String userId = cUser.getId();
		
		if (tagetFolderId.equalsIgnoreCase(FileCategory.ID_UNCATEGORIZED)) {
			
			FdrFolderCond cond = new FdrFolderCond();
			cond.setFolderFiles(new FdrFolderFile[]{new FdrFolderFile((String)fileIds.get(0))});
			
			FdrFolder[] sourceFolders = getFdrManager().getFolders(userId, cond, IManager.LEVEL_ALL);

			if (sourceFolders != null && sourceFolders.length != 0) {
				FdrFolderFile[] folderFiles = sourceFolders[0].getFolderFiles();
				for (int i = 0; i < folderFiles.length; i++) {
					FdrFolderFile folderFile = folderFiles[i];
					if (fileIds.contains(folderFile.getFileId())) {
						sourceFolders[0].removeFolderFile(folderFile);
					}
				}
				getFdrManager().setFolder(userId, sourceFolders[0], null);
			}
			return;
		}

		FdrFolderCond targetFolderCond = new FdrFolderCond();
		targetFolderCond.setObjId(tagetFolderId);
		FdrFolder[] targetFolders = getFdrManager().getFolders(userId, targetFolderCond, IManager.LEVEL_ALL);
		if (targetFolders == null || targetFolders.length == 0)
			return;
		
		FdrFolderCond cond = new FdrFolderCond();
		cond.setFolderFiles(new FdrFolderFile[]{new FdrFolderFile((String)fileIds.get(0))});
		
		FdrFolder[] sourceFolders = getFdrManager().getFolders(userId, cond, IManager.LEVEL_ALL);

		if (sourceFolders == null || sourceFolders.length == 0) {
			
			for (int i = 0; i < fileIds.size(); i++) {
				String fileId = (String)fileIds.get(i);
				FdrFolderFile folderFile = new FdrFolderFile();
				folderFile.setFileId(fileId);
				targetFolders[0].addFolderFile(folderFile);
			}
			getFdrManager().setFolder(userId, targetFolders[0], null);
			
		} else {
				
			String sourceFolderId = sourceFolders[0].getObjId();
			if (tagetFolderId.equalsIgnoreCase(sourceFolderId))
				return;
			
			FdrFolderFile[] folderFiles = sourceFolders[0].getFolderFiles();
			for (int i = 0; i < folderFiles.length; i++) {
				FdrFolderFile folderFile = folderFiles[i];
				if (fileIds.contains(folderFile.getFileId())) {
					sourceFolders[0].removeFolderFile(folderFile);
				}
			}
			for (int i = 0; i < fileIds.size(); i++) {
				String fileId = (String)fileIds.get(i);
				FdrFolderFile folderFile = new FdrFolderFile();
				folderFile.setFileId(fileId);
				targetFolders[0].addFolderFile(folderFile);
			}
			getFdrManager().setFolder(userId, sourceFolders[0], null);
			getFdrManager().setFolder(userId, targetFolders[0], null);
		}
	}
	@Override
	public void createNewFileFolder(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		createNewFolder(requestBody, request, TskTask.TASKREFTYPE_FILE);
	}
	@Override
	public void setFileFolder(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		setFolder(requestBody, request, TskTask.TASKREFTYPE_FILE);
	}
	@Override
	public void removeFileFolder(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		removeFolder(requestBody, request);
	}
	@Override
	public void createNewImageFolder(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		createNewFolder(requestBody, request, TskTask.TASKREFTYPE_IMAGE);
	}
	@Override
	public void setImageFolder(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		setFolder(requestBody, request, TskTask.TASKREFTYPE_IMAGE);
	}
	@Override
	public void removeImageFolder(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		removeFolder(requestBody, request);
	}
	@Override
	public void removeImageInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		removeInformationWorkInstance(requestBody, request);
	}
	@Override
	public void moveFileInstances(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		moveFile(requestBody, request);
	}
	@Override
	public void moveImageInstances(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		moveFile(requestBody, request);		
	}
	@Override
	public ImageInstance getImageInstanceById(String instId) throws Exception {

		User user = SmartUtil.getCurrentUser();
		SwfFormCond swfFormCond = new SwfFormCond();
		swfFormCond.setCompanyId(user.getCompanyId());
		swfFormCond.setPackageId(SmartWork.ID_FILE_MANAGEMENT);
		SwfForm[] swfForms = getSwfManager().getForms(user.getId(), swfFormCond, IManager.LEVEL_LITE);
		SwdRecordCond swdRecordCond = new SwdRecordCond();
		swdRecordCond.setCompanyId(user.getCompanyId());
		swdRecordCond.setFormId(swfForms[0].getId());
		swdRecordCond.setRecordId(instId);
		SwdRecord swdRecord = getSwdManager().getRecord(user.getId(), swdRecordCond, IManager.LEVEL_LITE);
		return getImageInstanceById(user.getCompanyId(), user.getId(), swdRecord);

	}
	@Override
	public InstanceInfoList getUpdateHistoryList(String instanceId, RequestParams params) throws Exception {

		String userId = SmartUtil.getCurrentUser().getId();

		int currentPage = params.getCurrentPage()-1;
		int pageCount = params.getPageSize();

		TskTaskCond tempTaskCond = new TskTaskCond();
		tempTaskCond.setExtendedProperties(new Property[]{new Property("recordId", instanceId)});
		TskTask[] tasks = SwManagerFactory.getInstance().getTskManager().getTasks(userId, tempTaskCond, IManager.LEVEL_ALL);
		if (tasks == null || tasks.length == 0)
			return new InstanceInfoList();
		
		String prcInstId = tasks[0].getProcessInstId();
		
		TskTaskCond taskCond = new TskTaskCond();
		taskCond.setProcessInstId(prcInstId);
		taskCond.setType(TskTask.TASKTYPE_SINGLE);
		long totalSize = SwManagerFactory.getInstance().getTskManager().getTaskSize(userId, taskCond);
		if (totalSize == 0)
			return new InstanceInfoList();
		
		taskCond.setPageSize(pageCount);
		taskCond.setPageNo(currentPage);
		taskCond.setOrders(new Order[]{new Order("creationDate", false)});
		
		TskTask[] allTasks = SwManagerFactory.getInstance().getTskManager().getTasks(userId, taskCond, IManager.LEVEL_ALL);
		
		InstanceInfoList instanceInfoList = new InstanceInfoList();
		
		IWInstanceInfo workInstObj = ModelConverter.getIWInstanceInfoByRecordId(null, instanceId);
		TaskInstanceInfo[] taskInstanceInfo = ModelConverter.getTaskInstanceInfoArrayByTskTaskArray(workInstObj, allTasks);
		
		instanceInfoList.setInstanceDatas(taskInstanceInfo);
		instanceInfoList.setPageSize(pageCount);
		int totalPages = (int)totalSize % pageCount;
		if(totalPages == 0)
			totalPages = (int)totalSize / pageCount;
		else
			totalPages = (int)totalSize / pageCount + 1;
		
		instanceInfoList.setTotalPages(totalPages);
		instanceInfoList.setCurrentPage(currentPage + 1);
		instanceInfoList.setTotalSize((int)totalSize);
		
		return instanceInfoList;
	}
	@Override
	public InstanceInfoList getDownloadHistoryList(String instanceId, String taskInstanceId, RequestParams params) throws Exception {

		String userId = SmartUtil.getCurrentUser().getId();

		int currentPage = params.getCurrentPage()-1;
		int pageCount = params.getPageSize();
		//instanceId = recordId
		
		FileDownloadHistoryCond cond = new FileDownloadHistoryCond();
		
		TskTask[] tasks = null;
		if (!CommonUtil.isEmpty(taskInstanceId)) {
			cond.setRefTaskId(taskInstanceId);
		} else {
			TskTaskCond taskCond = new TskTaskCond();
			taskCond.setExtendedProperties(new Property[]{new Property("recordId", instanceId)});
			tasks = SwManagerFactory.getInstance().getTskManager().getTasks(userId, taskCond, IManager.LEVEL_ALL);
			if (tasks == null || tasks.length == 0)
				return new InstanceInfoList();
			
			String prcInstId = tasks[0].getProcessInstId();
			cond.setRefPrcInstId(prcInstId);
		}
		
		long totalSize = SwManagerFactory.getInstance().getDocManager().getFileDownloadHistorySize(userId, cond);
		
		cond.setPageSize(pageCount);
		cond.setPageNo(currentPage);
		cond.setOrders(new Order[]{new Order("creationDate", false)});
		
		FileDownloadHistory[] downloadHistorys = SwManagerFactory.getInstance().getDocManager().getFileDownloadHistorys(userId, cond, IManager.LEVEL_ALL);
		if (downloadHistorys == null || downloadHistorys.length == 0)
			return new InstanceInfoList();
		
		InstanceInfoList instanceInfoList = new InstanceInfoList();
		InstanceInfo[] instanceInfos = new InstanceInfo[downloadHistorys.length];
		for (int i = 0; i < downloadHistorys.length; i++) {
			FileDownloadHistory downloadHistory = downloadHistorys[i];
			InstanceInfo instanceInfo = new InstanceInfo();
			instanceInfo.setSubject(downloadHistory.getFileName());
			instanceInfo.setOwner(ModelConverter.getUserInfoByUserId(downloadHistory.getDownloadUserId()));
			instanceInfo.setCreatedDate(new LocalDate(downloadHistory.getCreationDate().getTime()));
			instanceInfos[i] = instanceInfo;
		}
		instanceInfoList.setInstanceDatas(instanceInfos);
		instanceInfoList.setPageSize(pageCount);
		int totalPages = (int)totalSize % pageCount;
		if(totalPages == 0)
			totalPages = (int)totalSize / pageCount;
		else
			totalPages = (int)totalSize / pageCount + 1;
		
		instanceInfoList.setTotalPages(totalPages);
		instanceInfoList.setCurrentPage(currentPage + 1);
		instanceInfoList.setTotalSize((int)totalSize);
		
		return instanceInfoList;
	}
	@Override
	public InstanceInfoList getRelatedWorkList(String instanceId, RequestParams params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public InstanceInfoList getForwardHistoryList(String instanceId, RequestParams params) throws Exception {
		
		String userId = SmartUtil.getCurrentUser().getId();
		String type = "informationWork";

		int currentPage = params.getCurrentPage()-1;
		int pageCount = params.getPageSize();

		TskTaskCond tempTaskCond = new TskTaskCond();
		tempTaskCond.setExtendedProperties(new Property[]{new Property("recordId", instanceId)});
		TskTask[] tasks = SwManagerFactory.getInstance().getTskManager().getTasks(userId, tempTaskCond, IManager.LEVEL_ALL);
		if (tasks == null || tasks.length == 0) {
			TskTaskCond processTaskCond = new TskTaskCond();
			processTaskCond.setProcessInstId(instanceId);
			tasks = SwManagerFactory.getInstance().getTskManager().getTasks(userId, processTaskCond, IManager.LEVEL_ALL);
			if (tasks == null || tasks.length == 0) {
				return new InstanceInfoList();
			} else {
				type = "processWork";
			}
		}
		
		String prcInstId = tasks[0].getProcessInstId();
		
		TskTaskCond taskCond = new TskTaskCond();
		taskCond.setProcessInstId(prcInstId);
		long totalSize = SwManagerFactory.getInstance().getTskManager().getFirstForwardTasksOnGroupByForwardIdSize(userId, taskCond);
		if (totalSize == 0)
			return new InstanceInfoList();
		
		taskCond.setPageSize(pageCount);
		taskCond.setPageNo(currentPage);
		taskCond.setOrders(new Order[]{new Order(TskTask.A_MODIFICATIONDATE, false)});
//		taskCond.setOrders(new Order[]{new Order("creationDate", false)});
		
		TskTask[] allTasks = SwManagerFactory.getInstance().getTskManager().getFirstForwardTasksOnGroupByForwardId(userId, taskCond, IManager.LEVEL_ALL);
		
		Map<String, List> forwardIdMap = new HashMap<String, List>();
		
		for (int i = 0; i < allTasks.length; i++) {
			TskTask task = allTasks[i];
			String referenceTaskId = task.getFromRefId();
			task.setObjId(referenceTaskId);
		}
		InstanceInfoList instanceInfoList = new InstanceInfoList();
		TaskInstanceInfo[] taskInstanceInfo = null;
		if (type.equalsIgnoreCase("informationWork")) {
			IWInstanceInfo workInstObj = ModelConverter.getIWInstanceInfoByRecordId(null, instanceId);
			taskInstanceInfo = ModelConverter.getTaskInstanceInfoArrayByTskTaskArray(workInstObj, allTasks);
		} else {
			PrcProcessInst prcInst = SwManagerFactory.getInstance().getPrcManager().getProcessInst(userId, instanceId, IManager.LEVEL_ALL);
			PWInstanceInfo workInstObj = ModelConverter.getPWInstanceInfoByPrcProcessInst(null, prcInst);
			taskInstanceInfo = ModelConverter.getTaskInstanceInfoArrayByTskTaskArray(workInstObj, allTasks);
		}
		instanceInfoList.setInstanceDatas(taskInstanceInfo);
		instanceInfoList.setPageSize(pageCount);
		int totalPages = (int)totalSize % pageCount;
		if(totalPages == 0)
			totalPages = (int)totalSize / pageCount;
		else
			totalPages = (int)totalSize / pageCount + 1;
		
		instanceInfoList.setTotalPages(totalPages);
		instanceInfoList.setCurrentPage(currentPage + 1);
		instanceInfoList.setTotalSize((int)totalSize);
		
		return instanceInfoList;
	}
	@Override
	public InstanceInfoList getForwardTasksById(String forwardId, RequestParams params) throws Exception {
		if (CommonUtil.isEmpty(forwardId))
			return new InstanceInfoList();
		String userId = SmartUtil.getCurrentUser().getId();
		String type = "informationWork";

		int currentPage = -1;
		int pageCount = -1;
		
		if (params != null) {
			currentPage = params.getCurrentPage()-1;
			pageCount = params.getPageSize();
		}
		TskTaskCond taskCond = new TskTaskCond();
		taskCond.setForwardId(forwardId);
		long totalSize = SwManagerFactory.getInstance().getTskManager().getTaskSize(userId, taskCond);
		if (totalSize == 0)
			return new InstanceInfoList();

		if (params != null) {
			taskCond.setPageSize(pageCount);
			taskCond.setPageNo(currentPage);
			taskCond.setOrders(new Order[]{new Order("creationDate", false)});
		} else {
			taskCond.setOrders(new Order[]{new Order("creationDate", true)});
		}
		
		TskTask[] allTasks = SwManagerFactory.getInstance().getTskManager().getTasks(userId, taskCond, IManager.LEVEL_ALL);
		
		String instanceId = null;
		if (allTasks != null && allTasks.length != 0) {
			TskTask tempTask = allTasks[0];
			String taskDef = tempTask.getDef();
			String taskType = tempTask.getFromRefType();
			if (taskType.equalsIgnoreCase(TskTask.TASKTYPE_SINGLE)) {
				type = "informationWork";
			} else {
				type = "processWork";
			}
			String[] taskDefInfo = StringUtils.tokenizeToStringArray(taskDef, "|");
			if (type.equalsIgnoreCase("informationWork")) {
				instanceId = taskDefInfo[1];
			} else {
				instanceId = tempTask.getProcessInstId();
			}
		} else {
			return new InstanceInfoList();
		}
		InstanceInfoList instanceInfoList = new InstanceInfoList();
		TaskInstanceInfo[] taskInstanceInfo = null;
		if (type.equalsIgnoreCase("informationWork")) {
			IWInstanceInfo workInstObj = ModelConverter.getIWInstanceInfoByRecordId(null, instanceId);
			taskInstanceInfo = ModelConverter.getTaskInstanceInfoArrayByTskTaskArray(workInstObj, allTasks);
		} else {
			PrcProcessInst prcInst = SwManagerFactory.getInstance().getPrcManager().getProcessInst(userId, instanceId, IManager.LEVEL_ALL);
			PWInstanceInfo workInstObj = ModelConverter.getPWInstanceInfoByPrcProcessInst(null, prcInst);
			taskInstanceInfo = ModelConverter.getTaskInstanceInfoArrayByTskTaskArray(workInstObj, allTasks);
		}
		instanceInfoList.setInstanceDatas(taskInstanceInfo);
		if (params != null) {
			instanceInfoList.setPageSize(pageCount);
			int totalPages = (int)totalSize % pageCount;
			if(totalPages == 0)
				totalPages = (int)totalSize / pageCount;
			else
				totalPages = (int)totalSize / pageCount + 1;
			
			instanceInfoList.setTotalPages(totalPages);
			instanceInfoList.setCurrentPage(currentPage + 1);
		}
		instanceInfoList.setTotalSize((int)totalSize);
		return instanceInfoList;
	}
	@Override
	public TaskInstance getTaskInstanceById(String workId, String taskInstId) throws Exception {
		
		User cUser = SmartUtil.getCurrentUser();
		String userId = cUser.getId();
		
		TskTask task = SwManagerFactory.getInstance().getTskManager().getTask(userId, taskInstId, null);
		if (task == null)
			return null;
		
		PrcProcessInst prcInst = SwManagerFactory.getInstance().getPrcManager().getProcessInst(userId, task.getProcessInstId(), IManager.LEVEL_ALL);
		ProcessWorkInstance pworkInstObj = ModelConverter.getProcessWorkInstanceByPrcProcessInst(userId, null, prcInst);
		
		TaskInstance taskInstance = ModelConverter.getTaskInstanceByTskTask(userId, pworkInstObj, task);
		
		return taskInstance;
	}
	
	@Override
	public String getUcityChartXml(String categoryName, String periodName, String serviceName, String eventName) throws Exception {
		return SwManagerFactory.getInstance().getUcityWorkListManager().getUcityChartXml(categoryName, periodName, serviceName, eventName);
	}
    // Excel Download 구현
	@Override
	public void getUcityChartExcel(String categoryName, String periodName, String serviceName, String eventName, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SwManagerFactory.getInstance().getUcityWorkListManager().getUcityChartExcel(categoryName, periodName, serviceName, eventName, request, response);
	}

	//테스크의 이름으로 auditId 숫자만큼 쿼리를 날려서 카운터를 샌다
	private int[][] getUcityAuditTaskCountsByTasks(boolean runningOnly) throws Exception {

		int[][] result = null;
		if (runningOnly) {
			result = new int[1][Audit.MAX_AUDIT_ID];
			for (int i = 0; i < 1; i++) {
				for (int j = 0; j < Audit.MAX_AUDIT_ID; j++) {
					String[] taskNames = Audit.getTaskNamesByAuditId(j);
					TskTaskCond taskCond = new TskTaskCond();
					taskCond.setNameIns(taskNames);
					taskCond.setStatus(TskTask.TASKSTATUS_ASSIGN);
					long size = getTskManager().getTaskSize("", taskCond);
					result[i][j] = (int)size;
				}
			}
		} else {
			result = new int[2][Audit.MAX_AUDIT_ID];
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < Audit.MAX_AUDIT_ID; j++) {
					String[] taskNames = Audit.getTaskNamesByAuditId(j);
					TskTaskCond taskCond = new TskTaskCond();
					taskCond.setNameIns(taskNames);
					long size = 0;
					if (i == 0) {
						taskCond.setStatus(TskTask.TASKSTATUS_ASSIGN);
						size = getTskManager().getTaskSize("", taskCond);
					} else {
						taskCond.setStatus(TskTask.TASKSTATUS_ABORTED);
						size = getTskManager().getTaskSize("", taskCond);
					}
					result[i][j] = (int)size;
				}
			}
		}
		return result;
	}
	//모든 테스크를 우선 가져와서 auditId별로 분류작업을 한다
	private int[][] getUcityAuditTaskCountsByTasks(boolean runningOnly, TskTask[] tasks) throws Exception {

		int[][] result = null;
		if (runningOnly) {
			result = new int[1][Audit.MAX_AUDIT_ID];
			for (int i = 0; i < 1; i++) {
				for (int j = 0; j < Audit.MAX_AUDIT_ID; j++) {
					result[i][j] = 0;
				}
			}
		} else {
			result = new int[2][Audit.MAX_AUDIT_ID];
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < Audit.MAX_AUDIT_ID; j++) {
					result[i][j] = 0;
				}
			}
		}
		if (tasks == null) {
			return result;
		}
		Map<String, Integer> auditNameIdMappingMap = Audit.getAuditNameIdMappingMap();
		
		for (int i = 0; i < tasks.length; i++) {
			TskTask task = tasks[i];
			String taskName = task.getName();
			String taskStatus = task.getStatus();
			
			if (auditNameIdMappingMap.get(taskName) == null)
				continue;
			
			int auditId = auditNameIdMappingMap.get(taskName);
			int typeId = 0;
			if (taskStatus.equalsIgnoreCase(TskTask.TASKSTATUS_ABORTED)) {
				typeId = 1;
			}
			result[typeId][auditId] = result[typeId][auditId] + 1;
		}
		return result;
	}
	@Override
	public int[][] getUcityAuditTaskCounts(boolean runningOnly) throws Exception {

		User cUser = SmartUtil.getCurrentUser();
		String userId = cUser.getId();
		
		/*TskTask[] tasks = null;
		TskTaskCond taskCond = new TskTaskCond();
		if (runningOnly) {
			taskCond.setStatusIns(new String[]{TskTask.TASKSTATUS_ASSIGN});
			tasks = getTskManager().getTasks(userId, taskCond, IManager.LEVEL_LITE);
		} else {
			taskCond.setStatusIns(new String[]{TskTask.TASKSTATUS_ASSIGN, TskTask.TASKSTATUS_ABORTED});
			tasks = getTskManager().getTasks(userId, taskCond, IManager.LEVEL_LITE);
		}
		return getUcityAuditTaskCountsByTasks(runningOnly, tasks);*/
		return getUcityAuditTaskCountsByTasks(runningOnly);
		
	}
	@Override
	public Property[] getUcityExtendedPropertyByTaskInstId(String taskInstId) throws Exception {
		
		if (CommonUtil.isEmpty(taskInstId))
			return null;
		TskTask task = SwManagerFactory.getInstance().getTskManager().getTask("", taskInstId, IManager.LEVEL_LITE);
		if (CommonUtil.isEmpty(task))
			return null;
		
		String prcInstId = task.getProcessInstId();
		
		UcityWorkListCond ucityWorkListCond = new UcityWorkListCond();
		ucityWorkListCond.setPrcInstId(prcInstId);
		UcityWorkList[] workLists = SwManagerFactory.getInstance().getUcityWorkListManager().getUcityWorkLists("", ucityWorkListCond, null);
		
		if (CommonUtil.isEmpty(workLists))
			return null;
		
		Property p1 = new Property("serviceName",workLists[0].getServiceName());
		Property p2 = new Property("eventName",workLists[0].getEventName());
		Property p3 = new Property("type",workLists[0].getType());
		Property p4 = new Property("externalDisplay",workLists[0].getExternalDisplay());
		Property p5 = new Property("eventPlace",workLists[0].getEventPlace());
		Property p6 = new Property("isSms", CommonUtil.toBoolean(workLists[0].getIsSms())+"");
		Property p7 = new Property("eventId",workLists[0].getEventId());
		Property[] properties = new Property[]{p1, p2, p3, p4, p5, p6, p7};
		
		return properties;
	}
	@Override
	public void removeProcessWorkInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		
	}

	//////////////////////////////////////////////////////////////// externalService //////////////////////////////////////////////
	private static String toInstVariableByProps(String user, TskTask task, TskTaskDef taskDef, Property[] props) throws Exception {
		
		if (taskDef == null || CommonUtil.isEmpty(taskDef.getDocument()))
			return null;
		if (taskDef.getDocument().indexOf("actualParameters") < 0)
			return null;
		
		AcpActualParameters actualParams = (AcpActualParameters)AcpActualParameters.toObject(taskDef.getDocument());
		AcpActualParameter[] actualParamArray = actualParams.getActualParameters();
		
		InstanceVariables instVariables = new InstanceVariables();
		for (int i = 0; i < actualParamArray.length; i++) {
			AcpActualParameter actualParam = actualParamArray[i];
			if (actualParam.getMode().equalsIgnoreCase("IN"))
				continue;
			InstanceVariable instVariable = new InstanceVariable();
			
			instVariable.setInstType("task");
			instVariable.setId(actualParam.getId());
			instVariable.setInstId(task.getProcessInstId());
			instVariable.setVariableType(actualParam.getDataType());
			instVariable.setVariableName(actualParam.getName());
			instVariable.setVariableMode(actualParam.getMode());
//			if (actualParam.getTargetType().equalsIgnoreCase("expression")) {
//				instVariable.setVariableValue(actualParam.getExpression());
//				instVariables.addInstanceVariable(instVariable);
//				continue;
//			}
			if (props == null) {
				instVariables.addInstanceVariable(instVariable);
				continue;
			}
			for (int j = 0; j < props.length; j++) {
				Property property = props[j];
				if (property.getName().equalsIgnoreCase(actualParam.getId())) {
					instVariable.setVariableValue(property.getValue());
				} else {
					continue;
				}
			}
			instVariables.addInstanceVariable(instVariable);
		}
		return instVariables.toString();
	}
	private static TskTaskDef getStartTaskDef(String user, String processId) throws Exception {
		Property[] extProps = new Property[] {new Property("processId", processId), new Property("startActivity", "true")};
		TskTaskDefCond taskCond = new TskTaskDefCond();
		taskCond.setExtendedProperties(extProps);
		TskTaskDef[] taskDefs = SwManagerFactory.getInstance().getTskManager().getTaskDefs(user, taskCond, IManager.LEVEL_ALL);
		if (CommonUtil.isEmpty(taskDefs))
			throw new Exception(new StringBuffer("No start activity. -> processId:").append(processId).toString());
		TskTaskDef taskDef = taskDefs[0];
		taskDef.setExtendedPropertyValue("startActivity", "true");
		return taskDefs[0];
	}
	@Override
	public String initiateProcessByExternalFormInfo(String user, String title, String processId, Property[] props) throws Exception {
		//시작 하려하는 프로세스의 시작태스크가 외부업무폼이 아니라면 에러 발생
				if (CommonUtil.isEmpty(processId))
					return null;
				TskTaskDef taskDef = getStartTaskDef(user, processId);
				
				if (taskDef.getType().equalsIgnoreCase("SUBFLOW")) {
					
				} else {
					if (taskDef == null || taskDef.getForm().indexOf("ef_") < 0)
						throw new Exception("initiateProcessByExternalFormInfo fail : StartTaskDef Is Null Or FormId Is Not ExternalForm");
				}

				TskTask task = new TskTask();

				task.setProcessInstId(CommonUtil.newId());
				task.setType(taskDef.getType());
				task.setTitle(title);
				task.setStatus(CommonUtil.toDefault((String)MisUtil.taskStatusMap().get("started"), "started"));
				
				//시작 태스크의 리턴 ActualParameter정보를 가져와서 props 파라미터와 비교하여 instanceVaraible화 시켜서 시작 태스크에 저장하고 업무를 시작한다
				if (taskDef.getType().equalsIgnoreCase("SUBFLOW")) {
					String targetSubPackageId = taskDef.getSubFlowTargetId();
					
					PrcProcessCond cond = new PrcProcessCond();
					cond.setDiagramId(targetSubPackageId);
					PrcProcess[] prcs = SwManagerFactory.getInstance().getPrcManager().getProcesses(user, cond, IManager.LEVEL_ALL);
					
					if (prcs == null || prcs.length == 0)
						return null;
					
					String targetSubProcessId = prcs[0].getProcessId();
					
					TskTaskDef subProcessStartTaskDef = getStartTaskDef(user, targetSubProcessId);
					if (subProcessStartTaskDef == null)
						return null;
					
					//task.setInstVariable(toInstVariableByProps(user, task, subProcessStartTaskDef, props));
					task.setExtendedAttributeValue("externalFormInstValue", toInstVariableByProps(user, task, subProcessStartTaskDef, props));
					
				} else {
					task.setInstVariable(toInstVariableByProps(user, task, taskDef, props));
					
				}
				
				task.setAssigner(user);
				task.setAssignee(user);
				task.setForm(taskDef.getForm());
				task.setDef(taskDef.getObjId());
				
				task.setExtendedPropertyValue("processInstCreationUser", user);
				
				Date now = new Date();
				task.setExpectStartDate(now);
				task.setRealStartDate(now);
				Date expectEndDate = new Date();
				if (taskDef != null &&  !CommonUtil.isEmpty(taskDef.getDueDate())) {
					//dueDate 는 분단위로 설정이 되어 있다
					expectEndDate.setTime(now.getTime() + ((Long.parseLong(taskDef.getDueDate())) * 60 * 1000));
				} else {
					expectEndDate.setTime(now.getTime() + 1800000);
				}
				task.setExpectEndDate(expectEndDate);
				
				task.setIsStartActivity("true");
				task.setWorkSpaceId(user);
				task.setWorkSpaceType("4");
				task.setAccessLevel("3");
				
				if (task.getType().equalsIgnoreCase("SUBFLOW")) {
					task = SwManagerFactory.getInstance().getTskManager().setTask(user, task, IManager.LEVEL_ALL);
				} else {
					task = SwManagerFactory.getInstance().getTskManager().executeTask(user, task, "execute");
				}
				
				return task.getProcessInstId();
	}
	@Override
	public TskTask executeTaskByExternalFormInfo(String user, String action, String taskId, Property[] props) throws Exception {
		if (CommonUtil.isEmpty(taskId))
			return null;
		TskTask task = SwManagerFactory.getInstance().getTskManager().getTask(user, taskId, IManager.LEVEL_ALL);
		if (task == null)
			throw new Exception("Not Exist Task Id : " + taskId);
		if (task.getStatus().equalsIgnoreCase("21"))//완료 , 11:진행중
			throw new Exception("ExecuteTask Id : " + task.getObjId() + " is Not Executable Status."  );
		TskTaskDef taskDef = SwManagerFactory.getInstance().getTskManager().getTaskDef(user, task.getDef(), IManager.LEVEL_ALL);
		task.setInstVariable(toInstVariableByProps(user, task, taskDef, props));
		
		if (!CommonUtil.isEmpty(user)) {
			task.setAssignee(user);
		} else {
			task.setAssignee(task.getAssignee());
		}
		task = SwManagerFactory.getInstance().getTskManager().executeTask(user, task, CommonUtil.toDefault(action, "execute"));
		return task;
	}
	public TskTask executeInstanceByUserIdAndExternalFormInfo(String user, String action, String instanceId, Property[] props) throws Exception {
		//인스턴스 아디디로 업무를 실행시키되 담당자와 실행자가 같지 않으면 Exception발생
		if (CommonUtil.isEmpty(instanceId))
			return null;
		TskTaskCond cond = new TskTaskCond();
		cond.setProcessInstId(instanceId);
		cond.setStatus("11");//진행중, 21:완료
		//할당자가 실행자와 같아야 한다
		cond.setAssignee(user);
		
		TskTask[] tasks = SwManagerFactory.getInstance().getTskManager().getTasks(user, cond, IManager.LEVEL_ALL);
		
		if (tasks == null || tasks.length == 0)
			throw new Exception("Not Exist assignTask to user : " + user + " or Not Exist InstanceId : " + instanceId);
		if (tasks.length > 1) {
			StringBuffer tasksIds = new StringBuffer();
			for (int i = 0 ; i < tasks.length; i++) {
				tasksIds.append(tasks[i].getObjId()).append(",");;
			}
			throw new Exception("More than 1 Task result - instanceId : " + instanceId + ", taskIds : " + tasksIds.toString());
		}
		TskTask tsk = SwManagerFactory.getInstance().getTskManager().getTask(user, tasks[0].getObjId(), IManager.LEVEL_ALL);
		TskTaskDef taskDef = SwManagerFactory.getInstance().getTskManager().getTaskDef(user, tsk.getDef(), IManager.LEVEL_ALL);
		tsk.setInstVariable(toInstVariableByProps(user, tsk, taskDef, props));
		if (!CommonUtil.isEmpty(user)) {
			tsk.setAssignee(user);
		} else {
			tsk.setAssignee(tsk.getAssignee());
		}
		tsk = SwManagerFactory.getInstance().getTskManager().executeTask(user, tsk, CommonUtil.toDefault(action, "execute"));
		return tsk;
	}
	
}
