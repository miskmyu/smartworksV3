/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2011. 11. 23.
 * =========================================================
 * Copyright (c) 2011 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.service.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import net.smartworks.model.approval.Approval;
import net.smartworks.model.approval.ApprovalLine;
import net.smartworks.model.community.Community;
import net.smartworks.model.community.Department;
import net.smartworks.model.community.Group;
import net.smartworks.model.community.User;
import net.smartworks.model.community.WorkSpace;
import net.smartworks.model.community.info.CommunityInfo;
import net.smartworks.model.community.info.DepartmentInfo;
import net.smartworks.model.community.info.GroupInfo;
import net.smartworks.model.community.info.InstanceSpaceInfo;
import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.community.info.WorkSpaceInfo;
import net.smartworks.model.filter.Condition;
import net.smartworks.model.filter.SearchFilter;
import net.smartworks.model.filter.info.SearchFilterInfo;
import net.smartworks.model.instance.ImageInstance;
import net.smartworks.model.instance.InformationWorkInstance;
import net.smartworks.model.instance.Instance;
import net.smartworks.model.instance.ProcessWorkInstance;
import net.smartworks.model.instance.TaskInstance;
import net.smartworks.model.instance.WorkInstance;
import net.smartworks.model.instance.info.BoardInstanceInfo;
import net.smartworks.model.instance.info.EventInstanceInfo;
import net.smartworks.model.instance.info.FileInstanceInfo;
import net.smartworks.model.instance.info.IWInstanceInfo;
import net.smartworks.model.instance.info.ImageInstanceInfo;
import net.smartworks.model.instance.info.InstanceInfo;
import net.smartworks.model.instance.info.InstanceInfoList;
import net.smartworks.model.instance.info.MemoInstanceInfo;
import net.smartworks.model.instance.info.PWInstanceInfo;
import net.smartworks.model.instance.info.ReportInstanceInfo;
import net.smartworks.model.instance.info.TaskInstanceInfo;
import net.smartworks.model.instance.info.WorkInstanceInfo;
import net.smartworks.model.report.ChartReport;
import net.smartworks.model.report.Report;
import net.smartworks.model.security.AccessPolicy;
import net.smartworks.model.security.EditPolicy;
import net.smartworks.model.security.SpacePolicy;
import net.smartworks.model.security.WritePolicy;
import net.smartworks.model.work.FileCategory;
import net.smartworks.model.work.FormField;
import net.smartworks.model.work.InformationWork;
import net.smartworks.model.work.ProcessWork;
import net.smartworks.model.work.SmartDiagram;
import net.smartworks.model.work.SmartForm;
import net.smartworks.model.work.SmartWork;
import net.smartworks.model.work.Work;
import net.smartworks.model.work.WorkCategory;
import net.smartworks.model.work.info.FileCategoryInfo;
import net.smartworks.model.work.info.ImageCategoryInfo;
import net.smartworks.model.work.info.SmartFormInfo;
import net.smartworks.model.work.info.SmartTaskInfo;
import net.smartworks.model.work.info.SmartWorkInfo;
import net.smartworks.model.work.info.WorkCategoryInfo;
import net.smartworks.model.work.info.WorkInfo;
import net.smartworks.server.engine.authority.manager.ISwaManager;
import net.smartworks.server.engine.authority.model.SwaDepartment;
import net.smartworks.server.engine.authority.model.SwaDepartmentCond;
import net.smartworks.server.engine.authority.model.SwaGroup;
import net.smartworks.server.engine.authority.model.SwaGroupCond;
import net.smartworks.server.engine.authority.model.SwaResource;
import net.smartworks.server.engine.authority.model.SwaResourceCond;
import net.smartworks.server.engine.authority.model.SwaUser;
import net.smartworks.server.engine.authority.model.SwaUserCond;
import net.smartworks.server.engine.category.manager.ICtgManager;
import net.smartworks.server.engine.category.model.CtgCategory;
import net.smartworks.server.engine.category.model.CtgCategoryCond;
import net.smartworks.server.engine.common.collection.manager.IColManager;
import net.smartworks.server.engine.common.collection.model.ColList;
import net.smartworks.server.engine.common.collection.model.ColListCond;
import net.smartworks.server.engine.common.collection.model.ColObject;
import net.smartworks.server.engine.common.manager.IManager;
import net.smartworks.server.engine.common.menuitem.manager.IItmManager;
import net.smartworks.server.engine.common.menuitem.model.ItmMenuItem;
import net.smartworks.server.engine.common.menuitem.model.ItmMenuItemList;
import net.smartworks.server.engine.common.menuitem.model.ItmMenuItemListCond;
import net.smartworks.server.engine.common.model.Filter;
import net.smartworks.server.engine.common.model.Order;
import net.smartworks.server.engine.common.model.Property;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.common.util.MisUtil;
import net.smartworks.server.engine.common.util.StringUtil;
import net.smartworks.server.engine.docfile.manager.IDocFileManager;
import net.smartworks.server.engine.docfile.model.FileDownloadHistoryCond;
import net.smartworks.server.engine.docfile.model.FileWork;
import net.smartworks.server.engine.docfile.model.FileWorkCond;
import net.smartworks.server.engine.docfile.model.IFileModel;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.engine.folder.manager.IFdrManager;
import net.smartworks.server.engine.folder.model.FdrFolder;
import net.smartworks.server.engine.folder.model.FdrFolderCond;
import net.smartworks.server.engine.infowork.domain.manager.ISwdManager;
import net.smartworks.server.engine.infowork.domain.model.SwdDataField;
import net.smartworks.server.engine.infowork.domain.model.SwdDomain;
import net.smartworks.server.engine.infowork.domain.model.SwdField;
import net.smartworks.server.engine.infowork.domain.model.SwdRecord;
import net.smartworks.server.engine.infowork.domain.model.SwdRecordCond;
import net.smartworks.server.engine.infowork.form.manager.ISwfManager;
import net.smartworks.server.engine.infowork.form.model.SwfField;
import net.smartworks.server.engine.infowork.form.model.SwfForm;
import net.smartworks.server.engine.infowork.form.model.SwfFormCond;
import net.smartworks.server.engine.infowork.form.model.SwfFormModel;
import net.smartworks.server.engine.infowork.form.model.SwfFormat;
import net.smartworks.server.engine.like.manager.ILikeManager;
import net.smartworks.server.engine.like.model.Like;
import net.smartworks.server.engine.like.model.LikeCond;
import net.smartworks.server.engine.opinion.manager.IOpinionManager;
import net.smartworks.server.engine.opinion.model.OpinionCond;
import net.smartworks.server.engine.organization.manager.ISwoManager;
import net.smartworks.server.engine.organization.model.SwoDepartmentExtend;
import net.smartworks.server.engine.organization.model.SwoGroup;
import net.smartworks.server.engine.organization.model.SwoGroupCond;
import net.smartworks.server.engine.organization.model.SwoGroupMember;
import net.smartworks.server.engine.organization.model.SwoUser;
import net.smartworks.server.engine.organization.model.SwoUserCond;
import net.smartworks.server.engine.organization.model.SwoUserExtend;
import net.smartworks.server.engine.pkg.manager.IPkgManager;
import net.smartworks.server.engine.pkg.model.PkgPackage;
import net.smartworks.server.engine.pkg.model.PkgPackageCond;
import net.smartworks.server.engine.process.approval.manager.IAprManager;
import net.smartworks.server.engine.process.approval.model.AprApproval;
import net.smartworks.server.engine.process.approval.model.AprApprovalLine;
import net.smartworks.server.engine.process.approval.model.AprApprovalLineCond;
import net.smartworks.server.engine.process.process.manager.IPrcManager;
import net.smartworks.server.engine.process.process.model.PrcProcess;
import net.smartworks.server.engine.process.process.model.PrcProcessCond;
import net.smartworks.server.engine.process.process.model.PrcProcessInst;
import net.smartworks.server.engine.process.process.model.PrcProcessInstCond;
import net.smartworks.server.engine.process.process.model.PrcProcessInstExtend;
import net.smartworks.server.engine.process.process.model.PrcSwProcess;
import net.smartworks.server.engine.process.process.model.PrcSwProcessCond;
import net.smartworks.server.engine.process.task.manager.ITskManager;
import net.smartworks.server.engine.process.task.model.TskTask;
import net.smartworks.server.engine.process.task.model.TskTaskCond;
import net.smartworks.server.engine.process.task.model.TskTaskDef;
import net.smartworks.server.engine.process.task.model.TskTaskDefCond;
import net.smartworks.server.engine.process.xpdl.util.ProcessModelHelper;
import net.smartworks.server.engine.process.xpdl.xpdl2.Activities;
import net.smartworks.server.engine.process.xpdl.xpdl2.Activity;
import net.smartworks.server.engine.process.xpdl.xpdl2.Implementation7;
import net.smartworks.server.engine.process.xpdl.xpdl2.PackageType;
import net.smartworks.server.engine.process.xpdl.xpdl2.ProcessType1;
import net.smartworks.server.engine.process.xpdl.xpdl2.Task;
import net.smartworks.server.engine.process.xpdl.xpdl2.TaskApplication;
import net.smartworks.server.engine.process.xpdl.xpdl2.WorkflowProcesses;
import net.smartworks.server.engine.report.model.RptReport;
import net.smartworks.server.engine.resource.model.IFormModel;
import net.smartworks.server.engine.worklist.model.TaskWork;
import net.smartworks.server.service.ICommunityService;
import net.smartworks.server.service.IInstanceService;
import net.smartworks.server.service.IWorkService;
import net.smartworks.service.ISmartWorks;
import net.smartworks.util.LocalDate;
import net.smartworks.util.Semaphore;
import net.smartworks.util.SmartConfUtil;
import net.smartworks.util.SmartMessage;
import net.smartworks.util.SmartUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import commonj.sdo.Sequence;

@Component
public class ModelConverter {
	
	private static ISwoManager getSwoManager() {
		return SwManagerFactory.getInstance().getSwoManager();
	}
	private static ISwdManager getSwdManager() {
		return SwManagerFactory.getInstance().getSwdManager();
	}
	private static IPkgManager getPkgManager() {
		return SwManagerFactory.getInstance().getPkgManager();
	}
	private static IPrcManager getPrcManager() {
		return SwManagerFactory.getInstance().getPrcManager();
	}
	private static ICtgManager getCtgManager() {
		return SwManagerFactory.getInstance().getCtgManager();
	}
	private static ISwaManager getSwaManager() {
		return SwManagerFactory.getInstance().getSwaManager();
	}
	private static ISwfManager getSwfManager() {
		return SwManagerFactory.getInstance().getSwfManager();
	}
	private static IColManager getColManager() {
		return SwManagerFactory.getInstance().getColManager();
	}
	private static ITskManager getTskManager() {
		return SwManagerFactory.getInstance().getTskManager();
	}
	private static IItmManager getItmManager() {
		return SwManagerFactory.getInstance().getItmManager();
	}
	private static IDocFileManager getDocManager() {
		return SwManagerFactory.getInstance().getDocManager();
	}
	private static IAprManager getAprManager() {
		return SwManagerFactory.getInstance().getAprManager();
	}
	private static IFdrManager getFdrManager() {
		return SwManagerFactory.getInstance().getFdrManager();
	}
	private static IOpinionManager getOpinionManager() {
		return SwManagerFactory.getInstance().getOpinionManager();
	}

	private static IWorkService workService;
	private static IInstanceService instanceService;
	private static ICommunityService communityService;

	@Autowired(required=true)
	public void setWorkService(IWorkService workService) {
		ModelConverter.workService = workService;
	}
	@Autowired(required=true)
	public void setInstanceService(IInstanceService instanceService) {
		ModelConverter.instanceService = instanceService;
	}
	@Autowired(required=true)
	public void setCommunityService(ICommunityService communityService) {
		ModelConverter.communityService = communityService;
	}

	public static String[] getGroupIdsByNotBelongToClosedGroup(User currentUser) throws Exception {
		try {
			User user = currentUser;
			if(user == null)
				user = SmartUtil.getCurrentUser();

			String userId = user.getId();

			SwoGroupCond swoGroupCond = new SwoGroupCond();
			swoGroupCond.setGroupType(SwoGroup.GROUP_TYPE_PRIVATE);
			//swoGroupCond.setStatus(SwoGroup.GROUP_STATUS_OPEN);
			SwoGroup[] swoGroups = getSwoManager().getGroups(userId, swoGroupCond, IManager.LEVEL_ALL);

			List<String> groupList = new ArrayList<String>();
			String[] groupIds = null;

			if(!CommonUtil.isEmpty(swoGroups)) {
				for(SwoGroup swoGroup : swoGroups) {
					String groupId = swoGroup.getId();
					SwoGroupMember[] swoGroupMembers = swoGroup.getSwoGroupMembers();
					if(!CommonUtil.isEmpty(swoGroupMembers)) {
						boolean isExist = false;
						for(SwoGroupMember swoGroupMember : swoGroupMembers) {
							String memberId = swoGroupMember.getUserId();
							if(userId.equals(memberId)) {
								isExist = true;
								break;
							}
						}
						if(!isExist)
							groupList.add(groupId);
					}
				}
			}
			if(groupList.size() > 0) {
				groupIds = new String[groupList.size()];
				groupList.toArray(groupIds);
			}
			return groupIds;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static String[] getWorkSpaceIdIns(User currentUser) throws Exception {
		try {
			User user = currentUser;
			if(user == null)
				user = SmartUtil.getCurrentUser();

			String userId = user.getId();

			List<String> workSpaceIdInList = new ArrayList<String>();
			String[] workSpaceIdIns = null;

			// 나의 공간
			workSpaceIdInList.add(userId);
			// 나의 부서
			DepartmentInfo[] myDepartments = communityService.getMyDepartments();
			if(!CommonUtil.isEmpty(myDepartments)) {
				for(DepartmentInfo myDepartment : myDepartments) {
					String myDeptId = myDepartment.getId();
					workSpaceIdInList.add(myDeptId);
				}
			}
			// 나의 그룹
			GroupInfo[] myGroups = communityService.getMyGroups();
			if(!CommonUtil.isEmpty(myGroups)) {
				for(GroupInfo myGroup : myGroups) {
					String myGroupId = myGroup.getId();
					workSpaceIdInList.add(myGroupId);
				}
			}
			if(workSpaceIdInList.size() > 0) {
				workSpaceIdIns = new String[workSpaceIdInList.size()];
				workSpaceIdInList.toArray(workSpaceIdIns);
			}

			return workSpaceIdIns;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	private static PkgPackage getPkgPackageByPackageId(String packageId) throws Exception {
		if (CommonUtil.isEmpty(packageId))
			return null;
		PkgPackageCond pkgCond = new PkgPackageCond();
		pkgCond.setPackageId(packageId);
		PkgPackage pkg = getPkgManager().getPackage("", pkgCond, IManager.LEVEL_LITE);
		return pkg;
	}
	private static CtgCategory getCtgCategoryByCategoryId(String categoryId) throws Exception {
		if (CommonUtil.isEmpty(categoryId))
			return null;
		return getCtgManager().getCategory("", categoryId, IManager.LEVEL_LITE);
	}
	private static PrcProcessInst getPrcProcessInstByProcessInst(String prcInstanceId) throws Exception {
		if (CommonUtil.isEmpty(prcInstanceId))
			return null;
		return getPrcManager().getProcessInst("", prcInstanceId, IManager.LEVEL_LITE);
	}
	
	public static WorkSpace getWorkSpace(String workSpaceType, String workSpaceId) throws Exception {
		if (workSpaceType == null || workSpaceId == null)
			return null;
		
		String userId = SmartUtil.getCurrentUser().getId();
		WorkSpaceInfo workSpaceInfo = getWorkSpaceInfo(CommonUtil.toDefault(workSpaceType, String.valueOf(ISmartWorks.SPACE_TYPE_USER)), CommonUtil.toDefault(workSpaceId, userId));
		return (workSpaceInfo==null) ? null : workSpaceInfo.getWorkSpace();
	}
	// #########################################  INFO  ########################################################################

	public static String[] getLikersUserIdArray(String userId, int refType, String refId) throws Exception {
		if ( CommonUtil.isEmpty(refType) || CommonUtil.isEmpty(refId))
			return null;
		ILikeManager likeMgr = SwManagerFactory.getInstance().getLikeManager();
		LikeCond cond = new LikeCond();
		//cond.setRefType(refType);
		cond.setRefId(refId);
		Like[] likes = likeMgr.getLikes(userId, cond, IManager.LEVEL_ALL);
		if (likes == null || likes.length == 0)
			return null;
		String[] likesArray = new String[likes.length];
		for (int i = 0; i < likes.length; i++) {
			Like like = likes[i];
			String createUserId = like.getCreationUser();
			likesArray[i] = createUserId;
		}
		return likesArray;
	}

	public static WorkSpaceInfo getWorkSpaceInfo(String workSpaceType, String workSpaceId) throws Exception {
		if(CommonUtil.isEmpty(workSpaceType) || CommonUtil.isEmpty(workSpaceId))
			return null;
		User user = SmartUtil.getCurrentUser();
		String userId = user.getId();
		WorkSpaceInfo workSpaceInfo = null;
		switch (Integer.parseInt(workSpaceType)) {
		case ISmartWorks.SPACE_TYPE_WORK_INSTANCE :
			InstanceSpaceInfo instanceSpaceInfo = new InstanceSpaceInfo();
			InstanceInfo instanceInfo = null;
			WorkInfo workInfo = new WorkInfo();
			TskTaskCond tskCond = new TskTaskCond();
			tskCond.setExtendedProperties(new Property[] {new Property("recordId", workSpaceId)});
			tskCond.setTypeIns(new String[]{TskTask.TASKTYPE_SINGLE});
			tskCond.setOrders(new Order[]{new Order(TskTask.A_CREATIONDATE, false)});
			
			TskTask[] tskTasks = getTskManager().getTasks(userId, tskCond, IManager.LEVEL_LITE);
			WorkSpaceInfo wsInfo = null;
			String instanceId = workSpaceId;
			String subject = null;
			String domainId = null;
			String workId = null;
			String workName = null;
			boolean workIsRunning = false;
			String owner = null;
			LocalDate createDate = null;
			String lastModifier = null;
			LocalDate lastModifiedDate = null;

			if(!CommonUtil.isEmpty(tskTasks)) {
				TskTask tskTask = tskTasks[0];
				String refType = CommonUtil.toNotNull(tskTask.getRefType());
				SwdRecord swdRecord = (SwdRecord)SwdRecord.toObject(tskTask.getDocument());
				if(refType.equals(TskTask.TASKREFTYPE_BOARD)) {
					instanceInfo = new BoardInstanceInfo();
				} else if(refType.equals(TskTask.TASKREFTYPE_EVENT)) {
					instanceInfo = new EventInstanceInfo();
				} else if(refType.equals(TskTask.TASKREFTYPE_FILE)) {
					instanceInfo = new FileInstanceInfo();
				} else if(refType.equals(TskTask.TASKREFTYPE_IMAGE)) {
					instanceInfo = new ImageInstanceInfo();
				} else if(refType.equals(TskTask.TASKREFTYPE_MEMO)) {
					instanceInfo = new MemoInstanceInfo();
				} else {
					instanceInfo = new PWInstanceInfo();
				}
				if(swdRecord != null) {
//					String wsType = swdRecord.getWorkSpaceType();
//					String wsId = swdRecord.getWorkSpaceId();
//					wsInfo = getWorkSpaceInfo(wsType, wsId);
					subject = tskTask.getTitle();
					domainId = swdRecord.getDomainId();
//					SwdDomain swdDomain = getSwdManager().getDomain(userId, domainId, IManager.LEVEL_LITE);
//					String formId = swdDomain.getFormId();
					String formId = swdRecord.getFormId();
					SwfFormCond swfFormCond = new SwfFormCond();
					swfFormCond.setId(formId);
					SwfForm[] swfForms = getSwfManager().getForms(userId, swfFormCond, IManager.LEVEL_LITE);
					if(!CommonUtil.isEmpty(swfForms)) {
						SwfForm swfForm = swfForms[0];
						workId = swfForm.getPackageId();
						workName = swfForm.getName();
						workIsRunning = (swfForm.getStatus().equalsIgnoreCase(IFormModel.STATUS_DEPLOYED));
					}
					owner = swdRecord.getCreationUser();
					createDate = new LocalDate(swdRecord.getCreationDate() != null ? swdRecord.getCreationDate().getTime() : new Date().getTime());
					lastModifier = swdRecord.getModificationUser();
					lastModifiedDate = new LocalDate(swdRecord.getModificationDate() != null ? swdRecord.getModificationDate().getTime() : new Date().getTime());
				}
				workInfo.setType(SmartWork.TYPE_INFORMATION);
			} else {
				instanceInfo = new PWInstanceInfo();
				PrcProcessInst prcProcessInst = getPrcManager().getProcessInst(userId, workSpaceId, IManager.LEVEL_LITE);								
				if(!CommonUtil.isEmpty(prcProcessInst)) {
					subject = prcProcessInst.getTitle();
					workId = prcProcessInst.getDiagramId();
					workName = prcProcessInst.getName();
					PkgPackageCond pkgCond = new PkgPackageCond();
					pkgCond.setPackageId(prcProcessInst.getPackageId());				
					PkgPackage pkg = SwManagerFactory.getInstance().getPkgManager().getPackage(userId, pkgCond, IManager.LEVEL_LITE);									
					workIsRunning = !CommonUtil.isEmpty(pkg) && (pkg.getStatus().equalsIgnoreCase(IFormModel.STATUS_DEPLOYED));
				}
				workInfo.setType(SmartWork.TYPE_PROCESS);
			}
			workInfo.setId(workId);
			workInfo.setName(workName);
			workInfo.setRunning(workIsRunning);
			instanceInfo.setId(instanceId);
			instanceInfo.setSubject(subject);
			instanceInfo.setWorkInfo(workInfo);
			instanceInfo.setWorkSpaceInfo(wsInfo);
			instanceInfo.setOwner(getUserInfoByUserId(owner));
			instanceInfo.setCreatedDate(createDate);
			instanceInfo.setLastModifier(getUserInfoByUserId(lastModifier));
			instanceInfo.setLastModifiedDate(lastModifiedDate);
			instanceSpaceInfo.setId(instanceId);
			instanceSpaceInfo.setName(subject);
			instanceSpaceInfo.setInstance(instanceInfo);
			instanceSpaceInfo.setInstanceWork(workInfo);
			instanceSpaceInfo.setInstanceWorkSpaceId(instanceInfo.getWorkSpaceId());
			workSpaceInfo = instanceSpaceInfo;
			workSpaceInfo.setSpaceType(ISmartWorks.SPACE_TYPE_WORK_INSTANCE);
			break;
		case ISmartWorks.SPACE_TYPE_DEPARTMENT :
			DepartmentInfo departmentInfo = getDepartmentInfoByDepartmentId(workSpaceId);
			workSpaceInfo = departmentInfo;
			workSpaceInfo.setSpaceType(ISmartWorks.SPACE_TYPE_DEPARTMENT);
			break;
		case ISmartWorks.SPACE_TYPE_GROUP :
			GroupInfo groupInfo = getGroupInfoByGroupId(workSpaceId);
			workSpaceInfo = groupInfo;
			workSpaceInfo.setSpaceType(ISmartWorks.SPACE_TYPE_GROUP);
			break;
		case ISmartWorks.SPACE_TYPE_USER : 
			UserInfo userInfo = getUserInfoByUserId(workSpaceId);
			workSpaceInfo = userInfo;
			workSpaceInfo.setName(userInfo.getLongName());
			workSpaceInfo.setSpaceType(ISmartWorks.SPACE_TYPE_USER);
			break;
		}
		
		return workSpaceInfo;
	}
	public static TaskInstanceInfo getTaskInstanceInfo(User currentUser, TaskWork task) throws Exception {
		TaskInstanceInfo taskInfo = new TaskInstanceInfo();

		taskInfo.setId(task.getTskObjId());
		taskInfo.setSubject(task.getPrcTitle());
		if (task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_APPROVAL)) {
			taskInfo.setTaskType(TaskInstance.TYPE_APPROVAL_TASK_ASSIGNED);
		} else if (task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_COMMON)) {
			taskInfo.setTaskType(TaskInstance.TYPE_PROCESS_TASK_ASSIGNED);
		} else if (task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_REFERENCE)) {
			taskInfo.setTaskType(TaskInstance.TYPE_INFORMATION_TASK_FORWARDED);
		} else if (task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_SINGLE)) {
			taskInfo.setTaskType(TaskInstance.TYPE_INFORMATION_TASK_ASSIGNED);
		}

		SmartWorkInfo workInfo = new SmartWorkInfo();
		workInfo.setId(task.getPackageId());
		workInfo.setName(task.getPackageName());
		
		/*TYPE_INFORMATION = 21;
		TYPE_PROCESS = 22;
		TYPE_SCHEDULE = 23;*/
		if (task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_COMMON)) {
			workInfo.setType(SmartWork.TYPE_PROCESS);
			taskInfo.setType(SmartWork.TYPE_PROCESS);
		} else if (task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_SINGLE)) {
			workInfo.setType(SmartWork.TYPE_INFORMATION);
			taskInfo.setType(SmartWork.TYPE_INFORMATION);
		} else if (task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_APPROVAL)) {
			if (task.getPrcType().equalsIgnoreCase(TskTask.TASKTYPE_SINGLE)) {
				workInfo.setType(SmartWork.TYPE_INFORMATION);
				taskInfo.setType(SmartWork.TYPE_INFORMATION);
			} else {
				workInfo.setType(SmartWork.TYPE_PROCESS);
				taskInfo.setType(SmartWork.TYPE_PROCESS);
			}
		} else if (task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_REFERENCE)) {
			if (task.getPrcType().equalsIgnoreCase(TskTask.TASKTYPE_SINGLE)) {
				workInfo.setType(SmartWork.TYPE_INFORMATION);
				taskInfo.setType(SmartWork.TYPE_INFORMATION);
			} else {
				workInfo.setType(SmartWork.TYPE_PROCESS);
				taskInfo.setType(SmartWork.TYPE_PROCESS);
			}
		}
		if (task.getParentCtgId() != null) {
			workInfo.setMyCategory(new WorkCategoryInfo(task.getParentCtgId(), task.getParentCtgName()));
			workInfo.setMyGroup(new WorkCategoryInfo(task.getChildCtgId(), task.getChildCtgName()));
		} else {
			workInfo.setMyCategory(new WorkCategoryInfo(task.getChildCtgId(), task.getChildCtgName()));
		}
		String packageStatus = task.getPackageStatus();
		boolean isRunningPackage = true;
		boolean isEditingPackage = false;
		if (packageStatus != null && packageStatus.equalsIgnoreCase("DEPLOYED")) {
			isRunningPackage = true;
		} else {
			if (packageStatus.equalsIgnoreCase("CHECKED-OUT"))
				isEditingPackage = true;
			isRunningPackage = false;
		}
		workInfo.setRunning(isRunningPackage);
		workInfo.setEditing(isEditingPackage);
		
		taskInfo.setWorkInfo(workInfo);
		taskInfo.setWorkSpaceInfo(getWorkSpaceInfo(task.getTskWorkSpaceType(), task.getTskWorkSpaceId()));
		
		if (task.getTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ASSIGN)) {
			if (task.getTskExpectEndDate() != null) {
				if(task.getTskExpectEndDate().getTime()<(new LocalDate()).getTime())				
					taskInfo.setStatus(TaskInstance.STATUS_DELAYED_RUNNING);
				else
					taskInfo.setStatus(TaskInstance.STATUS_RUNNING);
			} else {
				taskInfo.setStatus(TaskInstance.STATUS_RUNNING);
			}
		} else if (task.getTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_COMPLETE)) {
			taskInfo.setStatus(TaskInstance.STATUS_COMPLETED);
		} else if (task.getTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_CANCEL)) {
			taskInfo.setStatus(TaskInstance.STATUS_REJECTED);
		} else if (task.getTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ABORTED)) {
			taskInfo.setStatus(TaskInstance.STATUS_ABORTED);
		} else if (task.getTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_RETURNED)) {
			taskInfo.setStatus(TaskInstance.STATUS_RETURNED);
		}

		//taskInfo.setNumberOfAssociatedWorks(numberOfAssociatedWorks);
		taskInfo.setOwner(getUserInfoByUserId(task.getTskAssignee()));
		taskInfo.setLastModifiedDate(new LocalDate( task.getTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ASSIGN) ? task.getTskCreateDate().getTime() : task.getTaskLastModifyDate().getTime()));
		taskInfo.setLastModifier(getUserInfoByUserId(task.getTskAssignee()));
		taskInfo.setCreatedDate(new LocalDate(task.getTskCreateDate().getTime()));
		taskInfo.setName(task.getTskName());
		taskInfo.setWorkInstance(getWorkInstanceInfoByTaskWork(currentUser, task));//WorkInstanceInfo
		taskInfo.setAssignee(getUserInfoByUserId(task.getTskAssignee()));
		taskInfo.setPerformer(getUserInfoByUserId(task.getTskAssignee()));
		taskInfo.setFormId(task.getTskForm());
		taskInfo.setStartTask(task.getIsStartActivity()!=null && task.getIsStartActivity().equalsIgnoreCase("true"));
		taskInfo.setComments(task.getTskDoc());

		return taskInfo;
	}
	public static TaskInstanceInfo[] getTaskInstanceInfoArrayByTaskWorkArray_THREAD(String userId, TaskWork[] tasks, int maxSize) throws Exception {

		User currentUser = SmartUtil.getCurrentUser();
		if (tasks == null || tasks.length == 0)
			return null;
		List<TaskInstanceInfo> resultInfoList = new ArrayList<TaskInstanceInfo>();

		int taskLenth = tasks.length;

		Semaphore semaphore = new Semaphore(taskLenth);
		Thread currentThread = Thread.currentThread();
		TaskParallelProcessing[] taskPP = new TaskParallelProcessing[taskLenth];
		for (int i = 0; i < taskLenth; i++) {
			taskPP[i] = new TaskParallelProcessing(semaphore, currentThread, currentUser, tasks[i]);
			taskPP[i].start();
		}

		synchronized (currentThread) {
			currentThread.wait();
		}
		for(int i=0; i<taskPP.length; i++) {
			resultInfoList.add((TaskInstanceInfo)taskPP[i].getResult());
		}

		if(maxSize < taskLenth) {
			TaskInstanceInfo taskInstanceInfo = new TaskInstanceInfo();
			taskInstanceInfo.setType(-21);
			resultInfoList.add(taskInstanceInfo);
		}
		TaskInstanceInfo[] resultInfo = new TaskInstanceInfo[resultInfoList.size()];
		resultInfoList.toArray(resultInfo);
		return resultInfo;
	}

	public static TaskInstanceInfo[] getTaskInstanceInfoArrayByTaskWorkArray(String userId, TaskWork[] tasks, int maxSize) throws Exception {
		if (tasks == null || tasks.length == 0)
			return null;
		List<TaskInstanceInfo> resultInfoList = new ArrayList<TaskInstanceInfo>();

		int taskLenth = tasks.length;

		for (int i = 0; i < taskLenth; i++) {
			if(i == maxSize)
				continue;
			TaskWork task = tasks[i];
			TaskInstanceInfo taskInfo = new TaskInstanceInfo();
			taskInfo.setId(task.getTskObjId());
			taskInfo.setSubject(task.getPrcTitle());
			if (task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_APPROVAL)) {
				taskInfo.setTaskType(TaskInstance.TYPE_APPROVAL_TASK_ASSIGNED);
			} else if (task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_COMMON)) {
				taskInfo.setTaskType(TaskInstance.TYPE_PROCESS_TASK_ASSIGNED);
			} else if (task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_REFERENCE)) {
				taskInfo.setTaskType(TaskInstance.TYPE_INFORMATION_TASK_FORWARDED);
			} else if (task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_SINGLE)) {
				taskInfo.setTaskType(TaskInstance.TYPE_INFORMATION_TASK_CREATED);
			}

			SmartWorkInfo workInfo = new SmartWorkInfo();
			workInfo.setId(task.getPackageId());
			workInfo.setName(task.getPackageName());
			
//			TYPE_INFORMATION = 21;
//			TYPE_PROCESS = 22;
//			TYPE_SCHEDULE = 23;
			if (task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_COMMON)) {
				workInfo.setType(SmartWork.TYPE_PROCESS);
				taskInfo.setType(SmartWork.TYPE_PROCESS);
			} else if (task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_SINGLE)) {
				workInfo.setType(SmartWork.TYPE_INFORMATION);
				taskInfo.setType(SmartWork.TYPE_INFORMATION);
			} else if (task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_APPROVAL)) {
				if (task.getPrcType().equalsIgnoreCase(TskTask.TASKTYPE_SINGLE)) {
					workInfo.setType(SmartWork.TYPE_INFORMATION);
					taskInfo.setType(SmartWork.TYPE_INFORMATION);
				} else {
					workInfo.setType(SmartWork.TYPE_PROCESS);
					taskInfo.setType(SmartWork.TYPE_PROCESS);
				}
			} else if (task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_REFERENCE)) {
				if (task.getPrcType().equalsIgnoreCase(TskTask.TASKTYPE_SINGLE)) {
					workInfo.setType(SmartWork.TYPE_INFORMATION);
					taskInfo.setType(SmartWork.TYPE_INFORMATION);
				} else {
					workInfo.setType(SmartWork.TYPE_PROCESS);
					taskInfo.setType(SmartWork.TYPE_PROCESS);
				}
			}
			if (task.getParentCtgId() != null) {
				workInfo.setMyCategory(new WorkCategoryInfo(task.getParentCtgId(), task.getParentCtgName()));
				workInfo.setMyGroup(new WorkCategoryInfo(task.getChildCtgId(), task.getChildCtgName()));
			} else {
				workInfo.setMyCategory(new WorkCategoryInfo(task.getChildCtgId(), task.getChildCtgName()));
			}
			String packageStatus = task.getPackageStatus();
			boolean isRunningPackage = true;
			boolean isEditingPackage = false;
			if (packageStatus != null && packageStatus.equalsIgnoreCase("DEPLOYED")) {
				isRunningPackage = true;
			} else {
				if (packageStatus.equalsIgnoreCase("CHECKED-OUT"))
					isEditingPackage = true;
				isRunningPackage = false;
			}
			workInfo.setRunning(isRunningPackage);
			workInfo.setEditing(isEditingPackage);
			
			taskInfo.setWorkInfo(workInfo);
			taskInfo.setWorkSpaceInfo((getWorkSpaceInfo(task.getTskWorkSpaceType(), task.getTskWorkSpaceId())));
			
			if (task.getTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ASSIGN)) {
				if (task.getTskExpectEndDate() != null) {
					if(task.getTskExpectEndDate().getTime()<(new LocalDate()).getTime())
						taskInfo.setStatus(TaskInstance.STATUS_DELAYED_RUNNING);
					else
						taskInfo.setStatus(TaskInstance.STATUS_RUNNING);
				} else {
					taskInfo.setStatus(TaskInstance.STATUS_RUNNING);
				}
			} else if (task.getTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_COMPLETE)) {
				taskInfo.setStatus(TaskInstance.STATUS_COMPLETED);
			} else if (task.getTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_CANCEL)) {
				taskInfo.setStatus(TaskInstance.STATUS_REJECTED);
			} else if (task.getTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_RETURNED)) {
				taskInfo.setStatus(TaskInstance.STATUS_RETURNED);
			}

			//taskInfo.setNumberOfAssociatedWorks(numberOfAssociatedWorks);
			taskInfo.setOwner(getUserInfoByUserId(task.getTskAssignee()));
			taskInfo.setLastModifiedDate(new LocalDate( task.getTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ASSIGN) ? task.getTskCreateDate().getTime() : task.getTaskLastModifyDate().getTime()));
			taskInfo.setLastModifier(getUserInfoByUserId(task.getTskAssignee()));
			taskInfo.setCreatedDate(new LocalDate(task.getTskCreateDate().getTime()));
			taskInfo.setName(task.getTskName());
			taskInfo.setWorkInstance(getWorkInstanceInfoByTaskWork(null, task));//WorkInstanceInfo
			taskInfo.setAssignee(getUserInfoByUserId(task.getTskAssignee()));
			taskInfo.setPerformer(getUserInfoByUserId(task.getTskAssignee()));
			taskInfo.setFormId(task.getTskForm());
			taskInfo.setStartTask((task.getIsStartActivity()!=null) && task.getIsStartActivity().equalsIgnoreCase("true"));

			if (task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_SINGLE) && taskInfo.getWorkInstance().getCreatedDate().getTime() != taskInfo.getLastModifiedDate().getTime()) {
				taskInfo.setTaskType(TaskInstance.TYPE_INFORMATION_TASK_UPDATED);
			}

			resultInfoList.add(taskInfo);
		}

		if( maxSize < taskLenth) {
			TaskInstanceInfo taskInstanceInfo = new TaskInstanceInfo();
			taskInstanceInfo.setType(-21);
			resultInfoList.add(taskInstanceInfo);
		}
		TaskInstanceInfo[] resultInfo = new TaskInstanceInfo[resultInfoList.size()];
		resultInfoList.toArray(resultInfo);
		return resultInfo;
	}
	public static WorkInstanceInfo[] getWorkInstanceInfosByTaskWorks(TaskWork[] tasks) throws Exception {
		if(CommonUtil.isEmpty(tasks))
			return null;
		User currentUser = SmartUtil.getCurrentUser();
		int tasksLength = tasks.length;
		WorkInstanceInfo[] workInstanceInfos = new WorkInstanceInfo[tasksLength];
		for(int i=0; i<tasksLength; i++) {
			TaskWork task = tasks[i];
			workInstanceInfos[i] = getWorkInstanceInfoByTaskWork(currentUser, task);
		}
		return workInstanceInfos;
	}

	public static WorkInstanceInfo getWorkInstanceInfoByTaskWork(User currentUser, TaskWork task) throws Exception {
		if (task == null)
			return null;
		User cUser = currentUser;
		String userId = null;
		String companyId = null;
		if (cUser != null) {
			userId = cUser.getId();
			companyId = cUser.getCompanyId();
		} else {
			cUser = SmartUtil.getCurrentUser();
			userId = cUser.getId();
			companyId = cUser.getCompanyId();
		}

		WorkInstanceInfo workInstanceInfo = null;
		
		if (task.getTskRefType() != null && task.getTskRefType().equalsIgnoreCase(TskTask.TASKREFTYPE_BOARD)) {
			BoardInstanceInfo tempWorkInstanceInfo = new BoardInstanceInfo();
			tempWorkInstanceInfo.setType(Instance.TYPE_BOARD);
			SwdRecord record = (SwdRecord)SwdRecord.toObject(task.getTskDoc());
			String content = record.getDataFieldValue("1");
			String fileGroupId = record.getDataFieldValue("2");
			if(!CommonUtil.isEmpty(fileGroupId)) {
				tempWorkInstanceInfo.setFileGroupId(fileGroupId);
				List<IFileModel> fileModelList = getDocManager().findFileGroup(fileGroupId);
				List<Map<String, String>> fileList = new ArrayList<Map<String,String>>();
				int fileModelListSize = fileModelList.size();
				if(fileList != null && fileModelListSize > 0) {
					for(int i=0; i<fileModelListSize; i++) {
						Map<String, String> fileMap = new LinkedHashMap<String, String>();
						IFileModel fileModel = fileModelList.get(i);
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
			tempWorkInstanceInfo.setBriefContent(StringUtil.subString(content, 0, 44, "..."));
			workInstanceInfo = tempWorkInstanceInfo;
		} else if (task.getTskRefType() != null && task.getTskRefType().equalsIgnoreCase(TskTask.TASKREFTYPE_EVENT)) {
			EventInstanceInfo tempWorkInstanceInfo = new EventInstanceInfo();
			tempWorkInstanceInfo.setType(Instance.TYPE_EVENT);
			SwdRecord record = (SwdRecord)SwdRecord.toObject(task.getTskDoc());
			
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
				for (int i = 0; i < userIdArray.length; i++) {
					relatedUsers[i] = ModelConverter.getUserInfoByUserId(userIdArray[i]);
				}
			}
			if (!CommonUtil.isEmpty(startDateStr)) {
				Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss").parse(startDateStr);
				startLocalDate = new LocalDate(startDate.getTime());
			}
			if (!CommonUtil.isEmpty(endDateStr)) {
				Date endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss").parse(endDateStr);
				endLocalDate = new LocalDate(endDate.getTime());
			}
			tempWorkInstanceInfo.setContent(content);
			tempWorkInstanceInfo.setStart(startLocalDate);
			tempWorkInstanceInfo.setEnd(endLocalDate);
			tempWorkInstanceInfo.setRelatedUsers(relatedUsers);
			
			workInstanceInfo = tempWorkInstanceInfo;
		} else if (task.getTskRefType() != null && task.getTskRefType().equalsIgnoreCase(TskTask.TASKREFTYPE_FILE)) {
			FileInstanceInfo tempWorkInstanceInfo = new FileInstanceInfo();
			tempWorkInstanceInfo.setType(Instance.TYPE_FILE);
			SwdRecord record = (SwdRecord)SwdRecord.toObject(task.getTskDoc());
			String fileGroupId = record.getDataFieldValue("5");
			if(!CommonUtil.isEmpty(fileGroupId)) {
				tempWorkInstanceInfo.setFileGroupId(fileGroupId);
				List<IFileModel> fileModelList = getDocManager().findFileGroup(fileGroupId);
				List<Map<String, String>> fileList = new ArrayList<Map<String,String>>();
				int fileModelListSize = fileModelList.size();
				if(fileList != null && fileModelListSize > 0) {
					for(int i=0; i<fileModelListSize; i++) {
						Map<String, String> fileMap = new LinkedHashMap<String, String>();
						IFileModel fileModel = fileModelList.get(i);
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
			String content = record.getDataFieldValue("4");
			tempWorkInstanceInfo.setContent(content);
			workInstanceInfo = tempWorkInstanceInfo;
		} else if (task.getTskRefType() != null && task.getTskRefType().equalsIgnoreCase(TskTask.TASKREFTYPE_IMAGE)) {
			ImageInstanceInfo tempWorkInstanceInfo = new ImageInstanceInfo();
			tempWorkInstanceInfo.setType(Instance.TYPE_IMAGE);

			SwdRecord record = (SwdRecord)SwdRecord.toObject(task.getTskDoc());
			String fileGroupId = record.getDataFieldValue("5");//TODO 첨부파일 파일 그룹아이디를 가져오기 위한 하드코딩
			String content = record.getDataFieldValue("4");
			
			List<IFileModel> files = getDocManager().findFileGroup(fileGroupId);
			String fileId = null;
			String originImgSrc = "";
			String imgSrc = "";
			if (files != null && files.size() != 0) {
				fileId = files.get(0).getId();
				String filePath = files.get(0).getFilePath();
				String extension = filePath.lastIndexOf(".") > 1 ? filePath.substring(filePath.lastIndexOf(".")) : null;
				filePath = StringUtils.replace(filePath, "\\", "/");
				if(filePath.indexOf(companyId) != -1)
					originImgSrc = Community.PICTURE_PATH + filePath.substring(filePath.indexOf(companyId), filePath.length());
				filePath = filePath.replaceAll(extension, Community.IMAGE_TYPE_THUMB + extension);
				if(filePath.indexOf(companyId) != -1)
					imgSrc = Community.PICTURE_PATH + filePath.substring(filePath.indexOf(companyId), filePath.length());
			}
			if(!CommonUtil.isEmpty(fileGroupId)) {
				tempWorkInstanceInfo.setFileGroupId(fileGroupId);
				List<IFileModel> fileModelList = getDocManager().findFileGroup(fileGroupId);
				List<Map<String, String>> fileList = new ArrayList<Map<String,String>>();
				int fileModelListSize = fileModelList.size();
				if(fileList != null && fileModelListSize > 0) {
					for(int i=0; i<fileModelListSize; i++) {
						Map<String, String> fileMap = new LinkedHashMap<String, String>();
						IFileModel fileModel = fileModelList.get(i);
						String id = fileModel.getId();
						String name = fileModel.getFileName();
						String type = fileModel.getType();
						String size = fileModel.getFileSize() + "";
						fileMap.put("fileId", id);
						fileMap.put("fileName", name);
						fileMap.put("fileType", type);
						fileMap.put("fileSize", size);
						fileList.add(fileMap);
					}
					if(fileList.size() > 0)
						tempWorkInstanceInfo.setFiles(fileList);
				}
			}
			tempWorkInstanceInfo.setFileId(fileId);
			tempWorkInstanceInfo.setOriginImgSource(originImgSrc);
			tempWorkInstanceInfo.setImgSource(imgSrc);
			tempWorkInstanceInfo.setContent(content);

			workInstanceInfo = tempWorkInstanceInfo;
		} else if (task.getTskRefType() != null && task.getTskRefType().equalsIgnoreCase(TskTask.TASKREFTYPE_MEMO)) {
			MemoInstanceInfo tempWorkInstanceInfo = new MemoInstanceInfo();
			tempWorkInstanceInfo.setType(Instance.TYPE_MEMO);
			SwdRecord record = (SwdRecord)SwdRecord.toObject(task.getTskDoc());
			String content = record.getDataFieldValue("4");
			tempWorkInstanceInfo.setContent(content);
			workInstanceInfo = tempWorkInstanceInfo;
		} else {
			workInstanceInfo = new WorkInstanceInfo();
			workInstanceInfo.setType(Instance.TYPE_WORK);
		}
		
		SmartWorkInfo workInfo = new SmartWorkInfo();
		
		String packageStatus = task.getPackageStatus();
		boolean isRunningPackage = true;
		boolean isEditingPackage = false;
		if (packageStatus != null && packageStatus.equalsIgnoreCase("DEPLOYED")) {
			isRunningPackage = true;
		} else {
			if (packageStatus.equalsIgnoreCase("CHECKED-OUT"))
				isEditingPackage = true;
			isRunningPackage = false;
		}
		workInfo.setRunning(isRunningPackage);
		workInfo.setEditing(isEditingPackage);
		
		workInfo.setId(task.getPackageId());
		workInfo.setName(task.getPackageName());
		/*TYPE_INFORMATION = 21;
		TYPE_PROCESS = 22;
		TYPE_SCHEDULE = 23;*/
/*		if(task.getTskRefType() != null) {
			if(task.getTskRefType().equalsIgnoreCase(TskTask.TASKREFTYPE_BOARD))
				workInfo.setType(SocialWork.TYPE_BOARD);
			else if(task.getTskRefType().equalsIgnoreCase(TskTask.TASKREFTYPE_EVENT))
				workInfo.setType(SocialWork.TYPE_EVENT);
			else if(task.getTskRefType().equalsIgnoreCase(TskTask.TASKREFTYPE_FILE))
				workInfo.setType(SocialWork.TYPE_FILE);
			else if(task.getTskRefType().equalsIgnoreCase(TskTask.TASKREFTYPE_IMAGE))
				workInfo.setType(SocialWork.TYPE_IMAGE);
			else if(task.getTskRefType().equalsIgnoreCase(TskTask.TASKREFTYPE_MEMO))
				workInfo.setType(SocialWork.TYPE_MEMO);
			else if(task.getTskRefType().equalsIgnoreCase(TskTask.TASKREFTYPE_MOVIE))
				workInfo.setType(SocialWork.TYPE_MOVIE);
		} else {*/
			if(task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_COMMON)) {
				workInfo.setType(SmartWork.TYPE_PROCESS);
			} else if(task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_SINGLE)) {
				workInfo.setType(SmartWork.TYPE_INFORMATION);
			} else if(task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_REFERENCE)) {
				String packageId = task.getPackageId();
				PkgPackageCond pkgCond = new PkgPackageCond();
				pkgCond.setPackageId(packageId);
				PkgPackage pkg = getPkgManager().getPackage(userId, pkgCond, IManager.LEVEL_LITE);
				if (pkg.getType().equalsIgnoreCase("SINGLE")) {
					workInfo.setType(SmartWork.TYPE_INFORMATION);
				} else if (pkg.getType().equalsIgnoreCase("PROCESS")) {
					workInfo.setType(SmartWork.TYPE_PROCESS);
				}
			} else if (task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_APPROVAL)) {
				String packageId = task.getPackageId();
				PkgPackageCond pkgCond = new PkgPackageCond();
				pkgCond.setPackageId(packageId);
				PkgPackage pkg = getPkgManager().getPackage(userId, pkgCond, IManager.LEVEL_LITE);
				if (pkg.getType().equalsIgnoreCase(TskTask.TASKTYPE_SINGLE)) {
					workInfo.setType(SmartWork.TYPE_INFORMATION);
				} else if (pkg.getType().equalsIgnoreCase(TskTask.TASKTYPE_COMMON)) {
					workInfo.setType(SmartWork.TYPE_PROCESS);
				}
			}
		//}
		if (task.getParentCtgId() != null) {
			workInfo.setMyCategory(new WorkCategoryInfo(task.getParentCtgId(), task.getParentCtgName()));
			workInfo.setMyGroup(new WorkCategoryInfo(task.getChildCtgId(), task.getChildCtgName()));
		} else {
			workInfo.setMyCategory(new WorkCategoryInfo(task.getChildCtgId(), task.getChildCtgName()));
		}
		TaskInstanceInfo lastTask = new TaskInstanceInfo();
		lastTask.setId(task.getLastTskObjId());
		lastTask.setName(task.getLastTskName());
		if (task.getLastTskType().equalsIgnoreCase(TskTask.TASKTYPE_APPROVAL)) {
			lastTask.setTaskType(TaskInstance.TYPE_APPROVAL_TASK_ASSIGNED);
		} else if (task.getLastTskType().equalsIgnoreCase(TskTask.TASKTYPE_COMMON)) {
			lastTask.setTaskType(TaskInstance.TYPE_PROCESS_TASK_ASSIGNED);
		} else if (task.getLastTskType().equalsIgnoreCase(TskTask.TASKTYPE_REFERENCE)) {
			lastTask.setTaskType(TaskInstance.TYPE_INFORMATION_TASK_FORWARDED);
		} else if (task.getLastTskType().equalsIgnoreCase(TskTask.TASKTYPE_SINGLE)) {
			lastTask.setTaskType(TaskInstance.TYPE_INFORMATION_TASK_ASSIGNED);
		}
		lastTask.setWorkInstance(workInstanceInfo);
		lastTask.setAssignee(getUserInfoByUserId(task.getLastTskAssignee()));
		lastTask.setPerformer(getUserInfoByUserId(task.getLastTskAssignee()));
		lastTask.setSubject(task.getPrcTitle());
		lastTask.setWorkInfo(workInfo);
		lastTask.setWorkSpaceInfo(getWorkSpaceInfo(task.getLastTskWorkSpaceType(), task.getLastTskWorkSpaceId()));
		
		if (task.getLastTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ASSIGN)) {
			if (task.getTskExpectEndDate() != null) {
				if(task.getTskExpectEndDate().getTime()<(new LocalDate()).getTime())
					lastTask.setStatus(TaskInstance.STATUS_DELAYED_RUNNING);
				else
					lastTask.setStatus(TaskInstance.STATUS_RUNNING);
			} else {
				lastTask.setStatus(TaskInstance.STATUS_RUNNING);
			}
		} else if (task.getLastTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_COMPLETE)) {
			lastTask.setStatus(TaskInstance.STATUS_COMPLETED);
		} else if (task.getLastTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_CANCEL)) {
			lastTask.setStatus(TaskInstance.STATUS_REJECTED);
		} else if (task.getLastTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ABORTED)) {
			lastTask.setStatus(TaskInstance.STATUS_ABORTED);
		} else if (task.getLastTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_RETURNED)) {
			lastTask.setStatus(TaskInstance.STATUS_RETURNED);
		}
		
		lastTask.setOwner(getUserInfoByUserId(task.getLastTskAssignee()));
		Date createdDate = task.getTskCreateDate();
		Date lastTskDate = task.getLastTskCreateDate() == null ? createdDate : task.getLastTskCreateDate();
		lastTask.setLastModifiedDate(new LocalDate(task.getLastTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ASSIGN) ? lastTskDate.getTime() : lastTskDate.getTime()));
		lastTask.setLastModifier(getUserInfoByUserId(task.getLastTskAssignee()));
		
		workInstanceInfo.setLastTask(lastTask);
		workInstanceInfo.setLastTaskCount(task.getLastTskCount());
		if (task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_COMMON)) {
			workInstanceInfo.setId(task.getPrcObjId());
		} else if (task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_SINGLE)) {
			/*String singleWorkInfos = task.getTskDef();
			String recordId = null;
			String domainId = null;
			if (!CommonUtil.isEmpty(singleWorkInfos)) {
				String[] singleWorkInfo = StringUtils.tokenizeToStringArray(singleWorkInfos, "|");	
				domainId = singleWorkInfo[0];
				recordId = singleWorkInfo[1];
			}
			workInstanceInfo.setId(recordId);*/
			String tskDoc = task.getTskDoc();
			String recordId = null;
			if(!CommonUtil.isEmpty(tskDoc)) {
				SwdRecord swdRecord = (SwdRecord)SwdRecord.toObject(tskDoc);
				recordId = swdRecord.getRecordId();
			}
			workInstanceInfo.setId(recordId);
		} else if(task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_REFERENCE) || task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_APPROVAL)) {
			String processInstId = task.getTskPrcInstId();
			TskTaskCond tskTaskCond = new TskTaskCond();
			tskTaskCond.setProcessInstId(processInstId);
			TskTask[] tskTasks = getTskManager().getTasks(userId, tskTaskCond, IManager.LEVEL_LITE);
			workInstanceInfo.setId(processInstId);
			if(!CommonUtil.isEmpty(tskTasks)) {
				for(TskTask tskTask : tskTasks) {
					if(tskTask.getType().equals(TskTask.TASKTYPE_SINGLE)) {
						String tskDoc = tskTask.getDocument();
						String recordId = null;
						if(!CommonUtil.isEmpty(tskDoc)) {
							SwdRecord swdRecord = (SwdRecord)SwdRecord.toObject(tskDoc);
							recordId = swdRecord.getRecordId();
						}
						workInstanceInfo.setId(recordId);
						break;
					}
				}
			}
			
		}

		String tskWorkSpaceId = task.getTskPrcInstId();
		if(task.getTskType().equals(SwfFormModel.TYPE_SINGLE)) {
			SwdRecord record = (SwdRecord)SwdRecord.toObject(task.getTskDoc());
			tskWorkSpaceId = record.getRecordId();
		}
		// Start - modified by ysjung at 2013-3-3
		workInstanceInfo.setSubInstanceCount(instanceService.getSubInstanceCountInInstance(tskWorkSpaceId));
		workInstanceInfo.setSubInstances(instanceService.getRecentSubInstancesInInstance(tskWorkSpaceId, WorkInstance.DEFAULT_SUB_INSTANCE_FETCH_COUNT));
		// End - modified by ysjung at 2013-3-3
		
		workInstanceInfo.setSubject(task.getPrcTitle());
		if (task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_REFERENCE) || task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_APPROVAL))
			workInstanceInfo.setSubject(task.getTskTitle());
		workInstanceInfo.setWorkInfo(workInfo);
		workInstanceInfo.setWorkSpaceInfo(getWorkSpaceInfo(task.getPrcWorkSpaceType(), task.getPrcWorkSpaceId()));
		
		if (task.getTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ASSIGN)) {
			if(CommonUtil.toDefault((String)MisUtil.processInstStatusMap().get("returned"), "returned").equalsIgnoreCase(task.getPrcStatus())){
				workInstanceInfo.setStatus(Instance.STATUS_RETURNED);
			}else if(task.getTskExpectEndDate()!=null && task.getTskExpectEndDate().getTime()<(new LocalDate()).getTime()){
				workInstanceInfo.setStatus(Instance.STATUS_DELAYED_RUNNING);
			}else{
				workInstanceInfo.setStatus(Instance.STATUS_RUNNING);
			}
		} else if (task.getTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_COMPLETE)) {
			workInstanceInfo.setStatus(Instance.STATUS_COMPLETED);
		} else if (task.getTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_CANCEL)) {
			workInstanceInfo.setStatus(Instance.STATUS_REJECTED);
		} else if (task.getTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ABORTED)) {
			workInstanceInfo.setStatus(Instance.STATUS_ABORTED);
		} else if (task.getTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_RETURNED)) {
			workInstanceInfo.setStatus(Instance.STATUS_RETURNED);
		}
		workInstanceInfo.setOwner(getUserInfoByUserId(task.getPrcCreateUser()));
		workInstanceInfo.setCreatedDate(new LocalDate(task.getTskCreateDate().getTime()));
		Date modifiedDate = task.getTaskLastModifyDate() == null ? task.getTskCreateDate() : task.getTaskLastModifyDate();
		workInstanceInfo.setLastModifiedDate(new LocalDate(modifiedDate.getTime()));
		workInstanceInfo.setLastModifier(getUserInfoByUserId(task.getTskAssignee()));
		
		return workInstanceInfo;
	}
	public static InstanceInfo[] getInstanceInfoArrayByTaskWorkArray(String userId, TaskWork[] tasks) throws  Exception {
		
		if(CommonUtil.isEmpty(tasks))
			return null;
		List<InstanceInfo> resultInfoList = new ArrayList<InstanceInfo>();

		for (int i = 0; i < tasks.length; i++) {
			TaskWork task = tasks[i];
			if (task.getPrcObjId() == null)
				continue;
			
			if (TskTask.TASKTYPE_COMMON.equalsIgnoreCase(task.getTskType()) || (TskTask.TASKTYPE_SINGLE.equalsIgnoreCase(task.getTskType()) && "true".equalsIgnoreCase(task.getIsStartActivity()))) {
				if (task.getIsStartActivity() != null && task.getIsStartActivity().equalsIgnoreCase("true") 
						&& !CommonUtil.toDefault((String)MisUtil.processInstStatusMap().get("returned"), "returned").equalsIgnoreCase(task.getPrcStatus())) {
					PWInstanceInfo instInfo = new PWInstanceInfo();
					
					if ((task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_SINGLE) && task.getIsStartActivity().equalsIgnoreCase("true"))) {
						String def = task.getTskDef();
						String[] ids = StringUtils.tokenizeToStringArray(def, "|");
						//ids[0] = domainId
						//ids[1] = recordId
						if (ids != null && ids.length == 2 ) {
							instInfo.setId(ids[1]);
						} else {
							instInfo.setId(task.getPrcObjId());
						}
					} else {
						instInfo.setId(task.getPrcObjId());
					}
					instInfo.setSubject(task.getPrcTitle());
					instInfo.setType(Instance.TYPE_WORK);
					
					
					SmartWorkInfo workInfo = new SmartWorkInfo();
					workInfo.setId(task.getPackageId());
					workInfo.setName(task.getPackageName());
					
					String packageStatus = task.getPackageStatus();
					boolean isRunningPackage = true;
					boolean isEditingPackage = false;
					if (packageStatus != null && packageStatus.equalsIgnoreCase("DEPLOYED")) {
						isRunningPackage = true;
					} else {
						if (packageStatus.equalsIgnoreCase("CHECKED-OUT"))
							isEditingPackage = true;
						isRunningPackage = false;
					}
					workInfo.setRunning(isRunningPackage);
					workInfo.setEditing(isEditingPackage);
					
					/*TYPE_INFORMATION = 21;
					TYPE_PROCESS = 22;
					TYPE_SCHEDULE = 23;*/
					if (task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_COMMON)) {
						workInfo.setType(SmartWork.TYPE_PROCESS);
					} else if (task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_SINGLE)) {
						workInfo.setType(SmartWork.TYPE_INFORMATION);
					}
					instInfo.setCreatedDate(new LocalDate(task.getPrcCreateDate().getTime()));
					
					if (task.getParentCtgId() != null) {
						workInfo.setMyCategory(new WorkCategoryInfo(task.getParentCtgId(), task.getParentCtgName()));
						workInfo.setMyGroup(new WorkCategoryInfo(task.getChildCtgId(), task.getChildCtgName()));
					} else {
						workInfo.setMyCategory(new WorkCategoryInfo(task.getChildCtgId(), task.getChildCtgName()));
					}
					
					instInfo.setWorkInfo(workInfo);
					instInfo.setWorkSpaceInfo(getWorkSpaceInfo(task.getPrcWorkSpaceType(), task.getPrcWorkSpaceId()));
					
					if (task.getPrcStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_RUNNING)) {
						if(task.getTskExpectEndDate()!=null && task.getTskExpectEndDate().getTime()<(new LocalDate()).getTime()){
							instInfo.setStatus(Instance.STATUS_DELAYED_RUNNING);							
						}else{
							instInfo.setStatus(Instance.STATUS_RUNNING);
						}
					} else if (task.getPrcStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_COMPLETE)) {
						instInfo.setStatus(Instance.STATUS_COMPLETED);
					} else if (task.getPrcStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_RETURN)) {
						instInfo.setStatus(Instance.STATUS_RETURNED);
					} else if (task.getPrcStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_ABORTED)) {
						instInfo.setStatus(Instance.STATUS_ABORTED);
					} else if (task.getPrcStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_CANCEL)) {
						instInfo.setStatus(Instance.STATUS_REJECTED);
					}
					
					instInfo.setOwner(getUserInfoByUserId(task.getPrcCreateUser()));
					instInfo.setLastModifiedDate(new LocalDate( task.getLastTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ASSIGN) ? task.getLastTskCreateDate().getTime() : task.getLastTskExecuteDate().getTime()));
					instInfo.setLastModifier(getUserInfoByUserId(task.getLastTskAssignee()));
					
					TaskInstanceInfo lastTask = new TaskInstanceInfo();
					lastTask.setId(task.getLastTskObjId());
					lastTask.setName(task.getLastTskName());
					
					if (task.getLastTskType().equalsIgnoreCase(TskTask.TASKTYPE_APPROVAL)) {
						lastTask.setTaskType(TaskInstance.TYPE_APPROVAL_TASK_ASSIGNED);
					} else if (task.getLastTskType().equalsIgnoreCase(TskTask.TASKTYPE_COMMON)) {
						lastTask.setTaskType(TaskInstance.TYPE_PROCESS_TASK_ASSIGNED);
					} else if (task.getLastTskType().equalsIgnoreCase(TskTask.TASKTYPE_REFERENCE)) {
						lastTask.setTaskType(TaskInstance.TYPE_INFORMATION_TASK_FORWARDED);
					} else if (task.getLastTskType().equalsIgnoreCase(TskTask.TASKTYPE_SINGLE)) {
						lastTask.setTaskType(TaskInstance.TYPE_INFORMATION_TASK_ASSIGNED);
					}
					
					
					lastTask.setWorkInstance(instInfo);
					lastTask.setAssignee(getUserInfoByUserId(task.getLastTskAssignee()));
					lastTask.setPerformer(getUserInfoByUserId(task.getLastTskAssignee()));
					lastTask.setSubject(task.getPrcTitle());
					lastTask.setWorkInfo(workInfo);
					lastTask.setWorkSpaceInfo(getWorkSpaceInfo(task.getLastTskWorkSpaceType(), task.getLastTskWorkSpaceId()));
					
					if (task.getLastTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ASSIGN)) {
						if(task.getTskExpectEndDate()!=null && task.getTskExpectEndDate().getTime()<(new LocalDate()).getTime()){
							lastTask.setStatus(Instance.STATUS_DELAYED_RUNNING);							
						}else{
							lastTask.setStatus(Instance.STATUS_RUNNING);
						}
					} else if (task.getLastTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_COMPLETE)) {
						lastTask.setStatus(TaskInstance.STATUS_COMPLETED);
					} else if (task.getLastTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_CANCEL)) {
						lastTask.setStatus(TaskInstance.STATUS_REJECTED);
					} else if (task.getLastTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ABORTED)) {
						lastTask.setStatus(TaskInstance.STATUS_ABORTED);
					} else if (task.getLastTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_RETURNED)) {
						lastTask.setStatus(TaskInstance.STATUS_RETURNED);
					}
					
					lastTask.setOwner(getUserInfoByUserId(task.getLastTskAssignee()));
					lastTask.setLastModifiedDate(new LocalDate( task.getLastTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ASSIGN) ? task.getLastTskCreateDate().getTime() : task.getLastTskExecuteDate().getTime()));
					lastTask.setLastModifier(getUserInfoByUserId(task.getLastTskAssignee()));

					instInfo.setLastTask(lastTask);
					instInfo.setLastTaskCount(task.getLastTskCount());

					resultInfoList.add(instInfo);
					
				} else {
					////////////////////////////////////////////
					PWInstanceInfo instInfo = new PWInstanceInfo();
					instInfo.setId(task.getPrcObjId());
					instInfo.setSubject(task.getPrcTitle());
					instInfo.setType(Instance.TYPE_WORK);
					
					SmartWorkInfo workInfo = new SmartWorkInfo();
					workInfo.setId(task.getPackageId());
					workInfo.setName(task.getPackageName());
					/*TYPE_INFORMATION = 21;
					TYPE_PROCESS = 22;
					TYPE_SCHEDULE = 23;*/
					if (task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_COMMON)) {
						workInfo.setType(SmartWork.TYPE_PROCESS);
					} else if (task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_SINGLE)) {
						workInfo.setType(SmartWork.TYPE_INFORMATION);
					}
					instInfo.setCreatedDate(new LocalDate(task.getPrcCreateDate().getTime()));
					
					if (task.getParentCtgId() != null) {
						workInfo.setMyCategory(new WorkCategoryInfo(task.getParentCtgId(), task.getParentCtgName()));
						workInfo.setMyGroup(new WorkCategoryInfo(task.getChildCtgId(), task.getChildCtgName()));
					} else {
						workInfo.setMyCategory(new WorkCategoryInfo(task.getChildCtgId(), task.getChildCtgName()));
					}
					String packageStatus = task.getPackageStatus();
					boolean isRunningPackage = true;
					boolean isEditingPackage = false;
					if (packageStatus != null && packageStatus.equalsIgnoreCase("DEPLOYED")) {
						isRunningPackage = true;
					} else {
						if (packageStatus.equalsIgnoreCase("CHECKED-OUT"))
							isEditingPackage = true;
						isRunningPackage = false;
					}
					workInfo.setRunning(isRunningPackage);
					workInfo.setEditing(isEditingPackage);
					instInfo.setWorkInfo(workInfo);
					instInfo.setWorkSpaceInfo(getWorkSpaceInfo(task.getPrcWorkSpaceType(), task.getPrcWorkSpaceId()));
					
					if (task.getPrcStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_RUNNING)) {
						if(TskTask.TASKSTATUS_CANCEL.equalsIgnoreCase(task.getLastTskStatus())){
							instInfo.setStatus(Instance.STATUS_REJECTED);
						}else if(task.getTskExpectEndDate()!=null && task.getTskExpectEndDate().getTime()<(new LocalDate()).getTime()){
							instInfo.setStatus(Instance.STATUS_DELAYED_RUNNING);							
						}else{
							instInfo.setStatus(Instance.STATUS_RUNNING);
						}
						
					} else if (task.getPrcStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_COMPLETE)) {
						instInfo.setStatus(Instance.STATUS_COMPLETED);
					} else if (task.getPrcStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_RETURN)) {
						instInfo.setStatus(Instance.STATUS_RETURNED);
					} else if (task.getPrcStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_ABORTED)) {
						instInfo.setStatus(Instance.STATUS_ABORTED);
					} else if (task.getPrcStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_CANCEL)) {
						instInfo.setStatus(Instance.STATUS_REJECTED);
					}
					
					instInfo.setOwner(getUserInfoByUserId(task.getPrcCreateUser()));
					instInfo.setLastModifiedDate(new LocalDate( task.getLastTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ASSIGN) ? task.getLastTskCreateDate().getTime() : task.getLastTskExecuteDate().getTime()));
					instInfo.setLastModifier(getUserInfoByUserId(task.getLastTskAssignee()));
					
					TaskInstanceInfo lastTask = new TaskInstanceInfo();
					lastTask.setId(task.getLastTskObjId());
					lastTask.setName(task.getLastTskName());

					if (task.getLastTskType().equalsIgnoreCase(TskTask.TASKTYPE_APPROVAL)) {
						lastTask.setTaskType(TaskInstance.TYPE_APPROVAL_TASK_ASSIGNED);
					} else if (task.getLastTskType().equalsIgnoreCase(TskTask.TASKTYPE_COMMON)) {
						lastTask.setTaskType(TaskInstance.TYPE_PROCESS_TASK_ASSIGNED);
					} else if (task.getLastTskType().equalsIgnoreCase(TskTask.TASKTYPE_REFERENCE)) {
						lastTask.setTaskType(TaskInstance.TYPE_INFORMATION_TASK_FORWARDED);
					} else if (task.getLastTskType().equalsIgnoreCase(TskTask.TASKTYPE_SINGLE)) {
						lastTask.setTaskType(TaskInstance.TYPE_INFORMATION_TASK_ASSIGNED);
					}
					
					lastTask.setWorkInstance(instInfo);
					lastTask.setAssignee(getUserInfoByUserId(task.getLastTskAssignee()));
					lastTask.setPerformer(getUserInfoByUserId(task.getLastTskAssignee()));
					lastTask.setSubject(task.getPrcTitle());
					lastTask.setWorkInfo(workInfo);
					lastTask.setWorkSpaceInfo(getWorkSpaceInfo(task.getLastTskWorkSpaceType(), task.getLastTskWorkSpaceId()));
					
					if (task.getLastTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ASSIGN)) {
						if(task.getTskExpectEndDate()!=null && task.getTskExpectEndDate().getTime()<(new LocalDate()).getTime()) {
							lastTask.setStatus(TaskInstance.STATUS_DELAYED_RUNNING);
						} else {
							lastTask.setStatus(TaskInstance.STATUS_RUNNING);
						}
					} else if (task.getLastTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_COMPLETE)) {
						lastTask.setStatus(TaskInstance.STATUS_COMPLETED);
					} else if (task.getLastTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_CANCEL)) {
						lastTask.setStatus(TaskInstance.STATUS_REJECTED);
					} else if (task.getLastTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ABORTED)) {
						lastTask.setStatus(TaskInstance.STATUS_ABORTED);
					} else if (task.getLastTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_RETURNED)) {
						lastTask.setStatus(TaskInstance.STATUS_RETURNED);
					}
					
					lastTask.setOwner(getUserInfoByUserId(task.getLastTskAssignee()));
					lastTask.setLastModifiedDate(new LocalDate( task.getLastTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ASSIGN) ? task.getLastTskCreateDate().getTime() : task.getLastTskExecuteDate().getTime()));
					lastTask.setLastModifier(getUserInfoByUserId(task.getLastTskAssignee()));

					
					instInfo.setLastTask(lastTask);
					instInfo.setLastTaskCount(task.getLastTskCount());
					
					/////////////////////////////////////////
					
					TaskInstanceInfo tskInfo = new TaskInstanceInfo();
					tskInfo.setId(task.getTskObjId());
					tskInfo.setName(task.getTskName());

					if (task.getLastTskType().equalsIgnoreCase(TskTask.TASKTYPE_APPROVAL)) {
						tskInfo.setTaskType(TaskInstance.TYPE_APPROVAL_TASK_ASSIGNED);
					} else if (task.getLastTskType().equalsIgnoreCase(TskTask.TASKTYPE_COMMON)) {
						tskInfo.setTaskType(TaskInstance.TYPE_PROCESS_TASK_ASSIGNED);
					} else if (task.getLastTskType().equalsIgnoreCase(TskTask.TASKTYPE_REFERENCE)) {
						tskInfo.setTaskType(TaskInstance.TYPE_INFORMATION_TASK_FORWARDED);
					} else if (task.getLastTskType().equalsIgnoreCase(TskTask.TASKTYPE_SINGLE)) {
						tskInfo.setTaskType(TaskInstance.TYPE_INFORMATION_TASK_ASSIGNED);
					}
					

					tskInfo.setType(Instance.TYPE_TASK);
					
					tskInfo.setWorkInstance(instInfo);
					tskInfo.setAssignee(getUserInfoByUserId(task.getTskAssignee()));
					tskInfo.setPerformer(getUserInfoByUserId(task.getTskAssignee()));
					tskInfo.setSubject(task.getPrcTitle());
					tskInfo.setWorkInfo(workInfo);
					tskInfo.setWorkSpaceInfo(getWorkSpaceInfo(task.getTskWorkSpaceType(), task.getTskWorkSpaceId()));
					
					if (task.getPrcStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_RUNNING)) {
						if(task.getTskExpectEndDate()!=null && task.getTskExpectEndDate().getTime()<(new LocalDate()).getTime()){
							tskInfo.setStatus(Instance.STATUS_DELAYED_RUNNING);							
						}else{
							tskInfo.setStatus(Instance.STATUS_RUNNING);
						}
						
					} else if (task.getPrcStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_COMPLETE)) {
						tskInfo.setStatus(Instance.STATUS_COMPLETED);
					} else if (task.getPrcStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_RETURN)) {
						tskInfo.setStatus(Instance.STATUS_RETURNED);
					} else if (task.getPrcStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_ABORTED)) {
						tskInfo.setStatus(Instance.STATUS_ABORTED);
					} else if (task.getPrcStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_CANCEL)) {
						tskInfo.setStatus(Instance.STATUS_REJECTED);
					}
					
					tskInfo.setOwner(getUserInfoByUserId(task.getPrcCreateUser()));
					tskInfo.setLastModifiedDate(new LocalDate(task.getTaskLastModifyDate().getTime()));
					tskInfo.setLastModifier(getUserInfoByUserId(task.getLastTskAssignee()));
					tskInfo.setCreatedDate(new LocalDate(task.getTskCreateDate().getTime()));
					
					resultInfoList.add(tskInfo);
				}
			} else {
				////////////////////////////////////////////
				IWInstanceInfo instInfo = new IWInstanceInfo();
				if (task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_REFERENCE) || 
						task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_APPROVAL) ||
							task.getTskType().equalsIgnoreCase("SINGLE")) {
					String def = task.getTskDef();
					String[] ids = StringUtils.tokenizeToStringArray(def, "|");
					//ids[0] = domainId
					//ids[1] = recordId
					if (ids != null && ids.length == 2 ) {
						instInfo.setId(ids[1]);
					} else {
						instInfo.setId(task.getPrcObjId());
					}
					instInfo.setSubject(task.getTskTitle());
				} else {
					instInfo.setId(task.getPrcObjId());
					instInfo.setSubject(task.getPrcTitle());
				}
//				String singleWorkInfos = task.getTskDef();
//				String recordId = null;
//				String domainId = null;
//				if (!CommonUtil.isEmpty(singleWorkInfos)) {
//					String[] singleWorkInfo = StringUtils.tokenizeToStringArray(singleWorkInfos, "|");	
//					domainId = singleWorkInfo[0];
//					recordId = singleWorkInfo[1];
//				}
//				instInfo.setId(recordId);
				instInfo.setType(Instance.TYPE_WORK);
				
				SmartWorkInfo workInfo = new SmartWorkInfo();
				workInfo.setId(task.getPackageId());
				workInfo.setName(task.getPackageName());
				/*TYPE_INFORMATION = 21;
				TYPE_PROCESS = 22;
				TYPE_SCHEDULE = 23;*/
				if (TskTask.TASKTYPE_COMMON.equalsIgnoreCase(task.getTskType())) {
					workInfo.setType(SmartWork.TYPE_PROCESS);
				} else if (TskTask.TASKTYPE_SINGLE.equalsIgnoreCase(task.getTskType())) {
					workInfo.setType(SmartWork.TYPE_INFORMATION);
				} else if (TskTask.TASKTYPE_REFERENCE.equalsIgnoreCase(task.getTskType())) {
					if (TskTask.TASKTYPE_SINGLE.equalsIgnoreCase(task.getPrcType())) {
						workInfo.setType(SmartWork.TYPE_INFORMATION);
					} else {
						workInfo.setType(SmartWork.TYPE_PROCESS);
					}
				} else if (TskTask.TASKTYPE_APPROVAL.equalsIgnoreCase(task.getTskType())) {
					if (TskTask.TASKTYPE_SINGLE.equalsIgnoreCase(task.getPrcType())) {
						workInfo.setType(SmartWork.TYPE_INFORMATION);
					} else {
						workInfo.setType(SmartWork.TYPE_PROCESS);
					}
				}
				instInfo.setCreatedDate(new LocalDate(task.getPrcCreateDate().getTime()));
				if (task.getParentCtgId() != null) {
					workInfo.setMyCategory(new WorkCategoryInfo(task.getParentCtgId(), task.getParentCtgName()));
					workInfo.setMyGroup(new WorkCategoryInfo(task.getChildCtgId(), task.getChildCtgName()));
				} else {
					workInfo.setMyCategory(new WorkCategoryInfo(task.getChildCtgId(), task.getChildCtgName()));
				}
				String packageStatus = task.getPackageStatus();
				boolean isRunningPackage = true;
				boolean isEditingPackage = false;
				if (packageStatus != null && packageStatus.equalsIgnoreCase("DEPLOYED")) {
					isRunningPackage = true;
				} else {
					if (packageStatus.equalsIgnoreCase("CHECKED-OUT"))
						isEditingPackage = true;
					isRunningPackage = false;
				}
				workInfo.setRunning(isRunningPackage);
				workInfo.setEditing(isEditingPackage);
				instInfo.setWorkInfo(workInfo);
				instInfo.setWorkSpaceInfo(getWorkSpaceInfo(task.getPrcWorkSpaceType(), task.getPrcWorkSpaceId()));
				
				if (task.getPrcStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_RUNNING)) {
					if(task.getTskExpectEndDate()!=null && task.getTskExpectEndDate().getTime()<(new LocalDate()).getTime()) {
						instInfo.setStatus(TaskInstance.STATUS_DELAYED_RUNNING);
					} else {
						instInfo.setStatus(Instance.STATUS_RUNNING);
					}
				} else if (task.getPrcStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_COMPLETE)) {
					instInfo.setStatus(Instance.STATUS_COMPLETED);
				} else if (task.getPrcStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_RETURN)) {
					instInfo.setStatus(Instance.STATUS_RETURNED);
				} else if (task.getPrcStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_ABORTED)) {
					instInfo.setStatus(Instance.STATUS_ABORTED);
				} else if (task.getPrcStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_CANCEL)) {
					instInfo.setStatus(Instance.STATUS_REJECTED);
				}
				
				instInfo.setOwner(getUserInfoByUserId(task.getPrcCreateUser()));
				Date lastTskCreateDate = task.getLastTskCreateDate() == null ? new Date() : task.getLastTskCreateDate();
				Date lastExecuteDate = task.getLastTskExecuteDate() == null ? new Date() : task.getLastTskExecuteDate();
				instInfo.setLastModifiedDate(new LocalDate( task.getLastTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ASSIGN) ? lastTskCreateDate.getTime() : lastExecuteDate.getTime()));
				instInfo.setLastModifier(getUserInfoByUserId(task.getLastTskAssignee()));
				
				TaskInstanceInfo lastTask = new TaskInstanceInfo();
				lastTask.setId(task.getLastTskObjId());
				lastTask.setName(task.getLastTskName());

				if (task.getLastTskType().equalsIgnoreCase(TskTask.TASKTYPE_APPROVAL)) {
					lastTask.setTaskType(TaskInstance.TYPE_APPROVAL_TASK_ASSIGNED);
				} else if (task.getLastTskType().equalsIgnoreCase(TskTask.TASKTYPE_COMMON)) {
					lastTask.setTaskType(TaskInstance.TYPE_PROCESS_TASK_ASSIGNED);
				} else if (task.getLastTskType().equalsIgnoreCase(TskTask.TASKTYPE_REFERENCE)) {
					lastTask.setTaskType(TaskInstance.TYPE_INFORMATION_TASK_FORWARDED);
				} else if (task.getLastTskType().equalsIgnoreCase(TskTask.TASKTYPE_SINGLE)) {
					lastTask.setTaskType(TaskInstance.TYPE_INFORMATION_TASK_ASSIGNED);
				}
				
				lastTask.setWorkInstance(instInfo);
				lastTask.setAssignee(getUserInfoByUserId(task.getLastTskAssignee()));
				lastTask.setPerformer(getUserInfoByUserId(task.getLastTskAssignee()));
				lastTask.setSubject(task.getPrcTitle());
				lastTask.setWorkInfo(workInfo);
				lastTask.setWorkSpaceInfo(getWorkSpaceInfo(task.getLastTskWorkSpaceType(), task.getLastTskWorkSpaceId()));
				
				if (task.getLastTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ASSIGN)) {
					if(task.getTskExpectEndDate()!=null && task.getTskExpectEndDate().getTime()<(new LocalDate()).getTime()) {
						lastTask.setStatus(TaskInstance.STATUS_DELAYED_RUNNING);
					} else {
						lastTask.setStatus(TaskInstance.STATUS_RUNNING);
					}
				} else if (task.getLastTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_COMPLETE)) {
					lastTask.setStatus(TaskInstance.STATUS_COMPLETED);
				} else if (task.getLastTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_CANCEL)) {
					lastTask.setStatus(TaskInstance.STATUS_REJECTED);
				} else if (task.getLastTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ABORTED)) {
					lastTask.setStatus(TaskInstance.STATUS_ABORTED);
				} else if (task.getLastTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_RETURNED)) {
					lastTask.setStatus(TaskInstance.STATUS_RETURNED);
				}
				
				lastTask.setOwner(getUserInfoByUserId(task.getLastTskAssignee()));
				lastTask.setLastModifiedDate(new LocalDate(task.getLastTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ASSIGN) ? lastTskCreateDate.getTime() : lastExecuteDate.getTime()));
				lastTask.setLastModifier(getUserInfoByUserId(task.getLastTskAssignee()));

				
				instInfo.setLastTask(lastTask);
				instInfo.setLastTaskCount(task.getLastTskCount());
				
				//TODO KM 할당 업무 (프로세스 업무제외, 참조 태스크, 승인태스크) 도 TaskInstanceInfo 로 객체를 생성해서 넘겨야 한다
				
				TaskInstanceInfo tskInfo = new TaskInstanceInfo();
				tskInfo.setId(task.getTskObjId());
				tskInfo.setName(task.getTskName());
				tskInfo.setCreatedDate(new LocalDate(task.getPrcCreateDate().getTime()));
				tskInfo.setSubject(task.getPrcTitle());
				
//				if (task.getLastTskType().equalsIgnoreCase(TskTask.TASKTYPE_APPROVAL)) {
//					tskInfo.setTaskType(TaskInstance.TYPE_APPROVAL_TASK_ASSIGNED);
//					
//					//TODO tskInfo.setApprovalId("needs task approval id");
//					
//				} else if (task.getLastTskType().equalsIgnoreCase(TskTask.TASKTYPE_COMMON)) {
//					tskInfo.setTaskType(TaskInstance.TYPE_PROCESS_TASK_ASSIGNED);
//				} else if (task.getLastTskType().equalsIgnoreCase(TskTask.TASKTYPE_REFERENCE)) {
//					tskInfo.setTaskType(TaskInstance.TYPE_INFORMATION_TASK_FORWARDED);
//					tskInfo.setForwardId(task.getPrcObjId());
//				} else if (task.getLastTskType().equalsIgnoreCase(TskTask.TASKTYPE_SINGLE)) {
//					tskInfo.setTaskType(TaskInstance.TYPE_INFORMATION_TASK_ASSIGNED);
//				}
				
				if (task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_APPROVAL)) {
					tskInfo.setApprovalId(task.getTskApprovalId());
					tskInfo.setTaskType(TaskInstance.TYPE_APPROVAL_TASK_ASSIGNED);
					
				} else if (task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_REFERENCE)) {
					
					tskInfo.setForwardId(task.getTskForwardId());
					tskInfo.setSubject(task.getTskTitle());
					if (TskTask.TASKTYPE_SINGLE.equalsIgnoreCase(task.getPrcType())) {
						tskInfo.setTaskType(TaskInstance.TYPE_INFORMATION_TASK_FORWARDED);
					} else {
						tskInfo.setTaskType(TaskInstance.TYPE_PROCESS_TASK_FORWARDED);
					}
					
				} else if (task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_COMMON)) {
					tskInfo.setTaskType(TaskInstance.TYPE_PROCESS_TASK_ASSIGNED);
				} else if (task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_SINGLE)) {
					tskInfo.setTaskType(TaskInstance.TYPE_INFORMATION_TASK_ASSIGNED);
				}

				tskInfo.setType(Instance.TYPE_TASK);
				tskInfo.setWorkInstance(instInfo);
				tskInfo.setAssignee(getUserInfoByUserId(task.getTskAssignee()));
				tskInfo.setPerformer(getUserInfoByUserId(task.getTskAssignee()));
				tskInfo.setWorkInfo(workInfo);
				tskInfo.setWorkSpaceInfo(getWorkSpaceInfo(task.getTskWorkSpaceType(), task.getTskWorkSpaceId()));
				
				if (task.getPrcStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_RUNNING)) {
					if(task.getTskExpectEndDate()!=null && task.getTskExpectEndDate().getTime()<(new LocalDate()).getTime()) {
						tskInfo.setStatus(TaskInstance.STATUS_DELAYED_RUNNING);
					} else {
						tskInfo.setStatus(TaskInstance.STATUS_RUNNING);
					}
				} else if (task.getPrcStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_COMPLETE)) {
					tskInfo.setStatus(TaskInstance.STATUS_COMPLETED);
				} else if (task.getPrcStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_RETURN)) {
					tskInfo.setStatus(TaskInstance.STATUS_RETURNED);
				} else if (task.getPrcStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_ABORTED)) {
					tskInfo.setStatus(TaskInstance.STATUS_ABORTED);
				} else if (task.getPrcStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_CANCEL)) {
					tskInfo.setStatus(TaskInstance.STATUS_REJECTED);
				}
				
				tskInfo.setOwner(getUserInfoByUserId(task.getPrcCreateUser()));
				tskInfo.setLastModifiedDate(new LocalDate(task.getLastTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ASSIGN) ? lastTskCreateDate.getTime() : lastExecuteDate.getTime()));
				tskInfo.setLastModifier(getUserInfoByUserId(task.getLastTskAssignee()));
				
				resultInfoList.add(tskInfo);
				// KM 할당 업무 (프로세스 업무제외, 참조 태스크, 승인태스크) 도 TaskInstanceInfo 로 객체를 생성해서 넘겨야 한다
				
				
				//resultInfoList.add(instInfo);
			}
		}

		InstanceInfo[] resultInfo = new InstanceInfo[resultInfoList.size()];
		resultInfoList.toArray(resultInfo);
		return resultInfo;
	}
	
	public static InstanceInfo getInstanceInfoByProcessInstId(String processInstId) throws Exception {
		PrcProcessInst prcInst = getPrcProcessInstByProcessInst(processInstId);
		return getInstanceInfoByPrcInst(null, prcInst);
	}
	public static InstanceInfo[] getInstanceInfoArrayByPrcInstArray(PrcProcessInst[] prcInsts) throws Exception {
		if (CommonUtil.isEmpty(prcInsts))
			return null;
		InstanceInfo[] instanceInfos = new InstanceInfo[prcInsts.length];
		for (int i = 0; i < prcInsts.length; i++) {
			PrcProcessInst prcInst = prcInsts[i];
			instanceInfos[i] = getInstanceInfoByPrcInst(null, prcInst);
		}
		return instanceInfos;
	}
	public static String getAccessValue(String userId, String resourceId) throws Exception {
		try {
			String accessValue = null;
			SwaUserCond swaUserCond = new SwaUserCond();
			swaUserCond.setResourceId(resourceId);
			swaUserCond.setMode(SwaResource.MODE_READ);
			SwaUser[] swaUsers = getSwaManager().getUsers(userId, swaUserCond, IManager.LEVEL_LITE);
			if(!CommonUtil.isEmpty(swaUsers)) {
				accessValue = "";
				for(SwaUser swaUser : swaUsers) {
					String authUserId = swaUser.getUserId() + ";";
					accessValue = accessValue + authUserId;
				}
			}
			return accessValue;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static void setPolicyToWork(SmartWork work, String resourceId) throws Exception {

		User user = SmartUtil.getCurrentUser();
		String userId = user.getId();
		/* -- 공개여부 --
		 공개 / 비공개*/

		/* -- 형태 --
		 블로그형 : v2.0 구조
		 위키형 : 누구나 수정 가능*/

		 /*-- 작성권한 --
		 전체 / 선택사용자*/
		//resourceId = formId , processId;
		AccessPolicy accessPolicy = new AccessPolicy();
		WritePolicy writePolicy = new WritePolicy();
		EditPolicy editPolicy = new EditPolicy();

		SwaResourceCond swaResourceCond = new SwaResourceCond();
		swaResourceCond.setResourceId(resourceId);
		SwaResource[] swaResources = getSwaManager().getResources(userId, swaResourceCond, IManager.LEVEL_LITE);

		if (CommonUtil.isEmpty(swaResources)) {
			accessPolicy.setLevel(AccessPolicy.LEVEL_DEFAULT);
			writePolicy.setLevel(WritePolicy.LEVEL_DEFAULT);
			editPolicy.setLevel(EditPolicy.LEVEL_DEFAULT);
		} else {
			for(SwaResource swaResource : swaResources) {
				Set<CommunityInfo> communityInfoSet = new LinkedHashSet<CommunityInfo>();
				CommunityInfo[] communitieInfos = null;
				String mode = swaResource.getMode();
				String permission = swaResource.getPermission();
				if(CommonUtil.toNotNull(mode).equals(SwaResource.MODE_READ)) {
					if(permission.equals(SwaResource.PERMISSION_ALL)) {
						accessPolicy.setLevel(AccessPolicy.LEVEL_PUBLIC);
					} else if(permission.equals(SwaResource.PERMISSION_SELECT)) {
						accessPolicy.setLevel(AccessPolicy.LEVEL_CUSTOM);
						SwaUserCond swaUserCond = new SwaUserCond();
						swaUserCond.setResourceId(resourceId);
						swaUserCond.setMode(SwaResource.MODE_READ);
						SwaUser[] swaUsers = getSwaManager().getUsers(userId, swaUserCond, IManager.LEVEL_LITE);
						if(!CommonUtil.isEmpty(swaUsers)) {
							for(SwaUser swaUser : swaUsers) {
								String authUserId = swaUser.getUserId();
								String type = swaUser.getType();
								if(type.equals(SwaUser.TYPE_USER)) {
									UserInfo userInfo = getUserInfoByUserId(authUserId);
									if(userInfo != null)
										communityInfoSet.add(userInfo);
								} else if(type.equals(SwaUser.TYPE_DEPT)) {
									DepartmentInfo departmentInfo = getDepartmentInfoByDepartmentId(authUserId);
									if(departmentInfo != null)
										communityInfoSet.add(departmentInfo);
								} else {
									GroupInfo groupInfo = getGroupInfoByGroupId(authUserId);
									if(groupInfo != null)
										communityInfoSet.add(groupInfo);
								}
							}
							if(communityInfoSet.size() > 0) {
								communitieInfos = new CommunityInfo[communityInfoSet.size()];
								communityInfoSet.toArray(communitieInfos);
							}
						}
						accessPolicy.setCommunitiesToOpen(communitieInfos);
					} else {
						accessPolicy.setLevel(AccessPolicy.LEVEL_PRIVATE);
					}
				} else if(CommonUtil.toNotNull(mode).equals(SwaResource.MODE_WRITE)) {
					if(permission.equals(SwaResource.PERMISSION_ALL)) {
						writePolicy.setLevel(WritePolicy.LEVEL_PUBLIC);
					} else {
						writePolicy.setLevel(WritePolicy.LEVEL_CUSTOM);
						SwaUserCond swaUserCond = new SwaUserCond();
						swaUserCond.setResourceId(resourceId);
						swaUserCond.setMode(SwaResource.MODE_WRITE);
						SwaUser[] swaUsers = getSwaManager().getUsers(userId, swaUserCond, IManager.LEVEL_LITE);
						if(!CommonUtil.isEmpty(swaUsers)) {
							for(SwaUser swaUser : swaUsers) {
								String authUserId = swaUser.getUserId();
								String type = swaUser.getType();
								if(type.equals(SwaUser.TYPE_USER)) {
									UserInfo userInfo = getUserInfoByUserId(authUserId);
									if(userInfo != null)
										communityInfoSet.add(userInfo);
								} else if(type.equals(SwaUser.TYPE_DEPT)) {
									DepartmentInfo departmentInfo = getDepartmentInfoByDepartmentId(authUserId);
									if(departmentInfo != null)
										communityInfoSet.add(departmentInfo);
								} else {
									GroupInfo groupInfo = getGroupInfoByGroupId(authUserId);
									if(groupInfo != null)
										communityInfoSet.add(groupInfo);
								}
							}
							if(communityInfoSet.size() > 0) {
								communitieInfos = new CommunityInfo[communityInfoSet.size()];
								communityInfoSet.toArray(communitieInfos);
							}
						}
						writePolicy.setCommunitiesToWrite(communitieInfos);
					}
				} else if(CommonUtil.toNotNull(mode).equals(SwaResource.MODE_MODIFY)) {
					if(permission.equals(SwaResource.PERMISSION_ALL)){
						editPolicy.setLevel(EditPolicy.LEVEL_PUBLIC);
					} else if(permission.equals(SwaResource.PERMISSION_SELECT)) {
						editPolicy.setLevel(EditPolicy.LEVEL_CUSTOM);
						SwaUserCond swaUserCond = new SwaUserCond();
						swaUserCond.setResourceId(resourceId);
						swaUserCond.setMode(SwaResource.MODE_MODIFY);
						SwaUser[] swaUsers = getSwaManager().getUsers(userId, swaUserCond, IManager.LEVEL_LITE);
						if(!CommonUtil.isEmpty(swaUsers)) {
							for(SwaUser swaUser : swaUsers) {
								String authUserId = swaUser.getUserId();
								String type = swaUser.getType();
								if(type.equals(SwaUser.TYPE_USER)) {
									UserInfo userInfo = getUserInfoByUserId(authUserId);
									if(userInfo != null)
										communityInfoSet.add(userInfo);
								} else if(type.equals(SwaUser.TYPE_DEPT)) {
									DepartmentInfo departmentInfo = getDepartmentInfoByDepartmentId(authUserId);
									if(departmentInfo != null)
										communityInfoSet.add(departmentInfo);
								} else {
									GroupInfo groupInfo = getGroupInfoByGroupId(authUserId);
									if(groupInfo != null)
										communityInfoSet.add(groupInfo);
								}
							}
							if(communityInfoSet.size() > 0) {
								communitieInfos = new CommunityInfo[communityInfoSet.size()];
								communityInfoSet.toArray(communitieInfos);
							}
						}
						editPolicy.setCommunitiesToEdit(communitieInfos);
					}else{
						editPolicy.setLevel(EditPolicy.LEVEL_PRIVATE);
					}
				}
			}
		}

		work.setAccessPolicy(accessPolicy);
		work.setWritePolicy(writePolicy);
		work.setEditPolicy(editPolicy);
			
	}
	
	public static TskTask getLastExecutedTskTaskByPrcInstId(String processInstId) throws Exception {
		TskTaskCond tskCond = new TskTaskCond();
		tskCond.setProcessInstId(processInstId);
		String fieldName = "creationDate";
		boolean isAsc = false;
		tskCond.setOrders(new Order[]{new Order(fieldName, isAsc)});
		tskCond.setPageNo(0);
		tskCond.setPageSize(1);
		tskCond.setStatus(TskTask.TASKSTATUS_COMPLETE);
		tskCond.setTypeNotIns(TskTask.NOTUSERTASKTYPES);
		
		TskTask[] lastSwTask = getTskManager().getTasks("", tskCond, IManager.LEVEL_LITE);
		if (lastSwTask == null)
			return null;
		return lastSwTask[0];
	}
	public static TskTask getLastTskTaskByInstanceId(String processInstId) throws Exception {
		TskTaskCond tskCond = new TskTaskCond();
		tskCond.setProcessInstId(processInstId);
		String fieldName = "creationDate";
		boolean isAsc = false;
		tskCond.setOrders(new Order[]{new Order(fieldName, isAsc)});
		tskCond.setPageNo(0);
		tskCond.setPageSize(1);
		tskCond.setTypeNotIns(TskTask.NOTUSERTASKTYPES);
		
		TskTask[] lastSwTask = getTskManager().getTasks("", tskCond, IManager.LEVEL_LITE);
		if (lastSwTask == null)
			return null;
		return lastSwTask[0];
	}

	public static InstanceInfo getInstanceInfoByPrcInst(InstanceInfo instInfo, PrcProcessInst prcInst) throws Exception {
		if (prcInst == null)
			return null;
		if (instInfo == null) 
			instInfo = new InstanceInfo();

		instInfo.setId(prcInst.getObjId());//processInstanceId
		instInfo.setSubject(prcInst.getTitle());
		
		TskTask lastTask = getLastExecutedTskTaskByPrcInstId(prcInst.getObjId());
		if (lastTask == null) {
			instInfo.setLastModifier(new UserInfo());
			instInfo.setLastModifiedDate(new LocalDate(1)); //TODO LastModifiedDate now
		} else {
			instInfo.setLastModifier(getUserInfoByUserId(lastTask.getAssignee()));
			instInfo.setLastModifiedDate(new LocalDate(lastTask.getExecutionDate().getTime())); //TODO LastModifiedDate now
		}
		
		instInfo.setOwner(getUserInfoByUserId(prcInst.getCreationUser()));
		if (prcInst.getStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_COMPLETE)) {
			instInfo.setStatus(Instance.STATUS_COMPLETED);
		} else if (prcInst.getStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_RUNNING)) {
			if(prcInst.getExpectEndDate()!=null && prcInst.getExpectEndDate().getTime()<(new LocalDate()).getTime())
				instInfo.setStatus(TaskInstance.STATUS_DELAYED_RUNNING);
			else
				instInfo.setStatus(Instance.STATUS_RUNNING);
		} else if (prcInst.getStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_RETURN)) {
			instInfo.setStatus(Instance.STATUS_RETURNED);
		} else if (prcInst.getStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_ABORTED)) {
			instInfo.setStatus(Instance.STATUS_ABORTED);
		} else if (prcInst.getStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_CANCEL)) {
			instInfo.setStatus(Instance.STATUS_REJECTED);
		}
		
		if (prcInst.getType() != null && prcInst.getType().equalsIgnoreCase(PrcProcessInst.PROCESSINSTTYPE_PROCESS)) {
			instInfo.setType(WorkInstance.TYPE_PROCESS);
		} else if (prcInst.getType() != null && prcInst.getType().equalsIgnoreCase(PrcProcessInst.PROCESSINSTTYPE_INFORMATION)) {
			instInfo.setType(WorkInstance.TYPE_INFORMATION);
		} else if (prcInst.getType() != null && prcInst.getType().equalsIgnoreCase(PrcProcessInst.PROCESSINSTTYPE_SCHEDULE)) {
			instInfo.setType(WorkInstance.TYPE_SCHEDULE);
		}
		String packageId = prcInst.getPackageId();
		if (packageId == null) {
			PrcSwProcessCond swPrcCond = new PrcSwProcessCond();
			swPrcCond.setProcessId(prcInst.getProcessId());
			PrcSwProcess[] swPrc = getPrcManager().getSwProcesses("", swPrcCond);
			if (swPrc == null || swPrc.length == 0)
				return null;
			packageId = swPrc[0].getPackageId();
			
		}
			
		instInfo.setWorkInfo(getSmartWorkInfoByPackageId(packageId));
		instInfo.setWorkSpaceInfo(getWorkSpaceInfo(prcInst.getWorkSpaceType(), prcInst.getWorkSpaceId()));
		
		return instInfo;
	}
	
	public static WorkInfo getWorkInfoByTask(TskTask task) throws Exception {
		String formId = task.getForm();
		SwfForm form = getSwfManager().getForm("", formId);
		if (form == null)
			return null;
		String packageId = form.getPackageId();
		return getWorkInfoByPackageId(packageId);
	}
	public static WorkInfo getWorkInfoByPackageId(String packageId) throws Exception {
		PkgPackage pkg = getPkgPackageByPackageId(packageId);
		return getSmartWorkInfoByPkgPackage(null, pkg);
	}
	public static WorkInfo[] getWorkInfoArrayByPkgPackageArray(PkgPackage[] pkgs) throws Exception {
		if (CommonUtil.isEmpty(pkgs))
			return null;
		WorkInfo[] workInfos = new WorkInfo[pkgs.length];
		for (int i = 0; i < pkgs.length; i++) {
			PkgPackage pkg = pkgs[i];
			workInfos[i] = getWorkInfoByPkgPackage(null, pkg);
		}
		return workInfos;
	}
	public static WorkInfo getWorkInfoByPkgPackage(WorkInfo workInfo, PkgPackage pkg) throws Exception {
		if (pkg == null)
			return null;
		if (workInfo == null) 
			workInfo = new WorkInfo();

		workInfo.setId(pkg.getPackageId());
		workInfo.setName(pkg.getName());
		if (pkg.getType().equalsIgnoreCase("PROCESS")) {
			workInfo.setType(SmartWork.TYPE_PROCESS);	
		} else if (pkg.getType().equalsIgnoreCase("SINGLE")) {
			workInfo.setType(SmartWork.TYPE_INFORMATION);	
		} else if (pkg.getType().equalsIgnoreCase("GANTT")) {
			workInfo.setType(SmartWork.TYPE_SCHEDULE);	
		}
		workInfo.setDesc(pkg.getDescription());
		return workInfo;
	}

	public static SmartWorkInfo[] convertPkgPackagesToSmartWorkInfos(PkgPackage[] pkgPackages) throws Exception {
		try {
			if(CommonUtil.isEmpty(pkgPackages))
				return null;

			int packageLength = pkgPackages.length;
			SmartWorkInfo[] smartWorkInfos = new SmartWorkInfo[packageLength];

			for(int i=0; i<packageLength; i++) {
				PkgPackage pkgPackage = pkgPackages[i];
				SmartWorkInfo smartWorkInfo = new SmartWorkInfo();
				getWorkInfoByPkgPackage(smartWorkInfo, pkgPackage);
				Map<String, WorkCategoryInfo> pkgCtgPathMap = getPkgCtgInfoMapByPackage(pkgPackage);
				smartWorkInfo.setMyCategory(pkgCtgPathMap.get("category"));
				smartWorkInfo.setMyGroup(pkgCtgPathMap.get("group"));
				String packageStatus = pkgPackage.getStatus();
				boolean isRunningPackage = false;
				boolean isEditingPackage = false;
				if (packageStatus.equalsIgnoreCase("DEPLOYED")) {
					isRunningPackage = true;
				} else {
					if (packageStatus.equalsIgnoreCase("CHECKED-OUT"))
						isEditingPackage = true;
					isRunningPackage = false;
				}
				smartWorkInfo.setRunning(isRunningPackage);
				smartWorkInfo.setEditing(isEditingPackage);
				smartWorkInfo.setFavorite(true);
				smartWorkInfos[i] = smartWorkInfo;
			}

			return smartWorkInfos;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static SmartWorkInfo getSmartWorkInfoByPackageId(String packageId) throws Exception {
		PkgPackage pkg = getPkgPackageByPackageId(packageId);
		return getSmartWorkInfoByPkgPackage(null, pkg);
	}
	public static SmartWorkInfo[] getSmartWorkInfoArrayByPkgPackageArray(PkgPackage[] pkgs) throws Exception {
		if (CommonUtil.isEmpty(pkgs))
			return null;
		int pkgsLength = pkgs.length;
		SmartWorkInfo[] smartWorkInfos = new SmartWorkInfo[pkgsLength];
		for (int i = 0; i < pkgsLength; i++) {
			PkgPackage pkg = pkgs[i];
			smartWorkInfos[i] = getSmartWorkInfoByPkgPackage(null, pkg);
		}

		return smartWorkInfos;
	}
	public static SmartWorkInfo getSmartWorkInfoByPkgPackage(SmartWorkInfo workInfo, PkgPackage pkg) throws Exception {
		if (pkg == null)
			return null;
		if (workInfo == null) 
			workInfo = new SmartWorkInfo();

		getWorkInfoByPkgPackage(workInfo, pkg);

		Map<String, WorkCategoryInfo> pkgCtgPathMap = getPkgCtgInfoMapByPackage(pkg);
		workInfo.setMyCategory(pkgCtgPathMap.get("category"));
		workInfo.setMyGroup(pkgCtgPathMap.get("group"));

		User user = SmartUtil.getCurrentUser();
		if(CommonUtil.isEmpty(user.getCompanyId()) || CommonUtil.isEmpty(user.getId()))
			return null;

		ItmMenuItemListCond itemListCond = new ItmMenuItemListCond();
		itemListCond.setCompanyId(user.getCompanyId());
		itemListCond.setUserId(user.getId());
		ItmMenuItemList itmList = getItmManager().getMenuItemList(user.getId(), itemListCond, IManager.LEVEL_ALL);

		String pkgId = pkg.getPackageId();
		if(itmList != null) {
			ItmMenuItem[] items = itmList.getMenuItems();
			if(items != null) {
				int itemsLength = items.length;
				for(int i=0; i<itemsLength; i++) {
					ItmMenuItem item = items[i];
					if(item != null) {
						String itemPkgId = item.getPackageId();
						if(pkgId.equals(itemPkgId))
							workInfo.setFavorite(true);
					}
				}
			}
		}
		String packageStatus = pkg.getStatus();
		boolean isRunningPackage = false;
		boolean isEditingPackage = false;
		if (packageStatus.equalsIgnoreCase("DEPLOYED")) {
			isRunningPackage = true;
		} else {
			if (packageStatus.equalsIgnoreCase("CHECKED-OUT"))
				isEditingPackage = true;
			isRunningPackage = false;
		}
		workInfo.setRunning(isRunningPackage);
		workInfo.setEditing(isEditingPackage);
		return workInfo;
	}	
	
	public static WorkCategoryInfo[] getWorkCategoryInfoArrayByCtgCategoryArray(CtgCategory[] argCtgs) throws Exception {
		if (CommonUtil.isEmpty(argCtgs))
			return null;
		
		WorkCategoryInfo[] workCtgs = new WorkCategoryInfo[argCtgs.length];
		for (int i =0; i < argCtgs.length; i ++) {
			CtgCategory ctg = argCtgs[i];
			WorkCategoryInfo workCtg = (WorkCategoryInfo)getWorkCategoryInfoByCtgCategory(null, ctg);
			workCtgs[i] = workCtg; 
		}
		return workCtgs;
	}
	public static WorkCategoryInfo getWorkCategoryInfoByCtgCategory(WorkCategoryInfo workCtgInfo, CtgCategory argCtg) throws Exception {
		if (argCtg == null)
			return null;
		if (workCtgInfo == null) 
			workCtgInfo = new WorkCategoryInfo();

		CtgCategory ctg = (CtgCategory)argCtg;
		String ctgId = ctg.getObjId();
		String ctgName = ctg.getName();
		WorkCategoryInfo workCtg = new WorkCategoryInfo(ctgId, ctgName);
		workCtg.setRunning(isExistRunningPackageByCategoryId(ctgId));
		// Added by ysjung at 2013-3-2
		workCtg.setDesc(ctg.getDescription());
		return workCtg;
	}

	public static void setAccessPolicyByMember(String accessLevel, AccessPolicy accessPolicy, Set<CommunityInfo> communityInfoSet, String accessValue) throws Exception {
		if(accessLevel.equals("1")) {
			accessPolicy.setLevel(AccessPolicy.LEVEL_PRIVATE);
		} else if(accessLevel.equals("2")) {
			accessPolicy.setLevel(AccessPolicy.LEVEL_CUSTOM);
			if(!CommonUtil.isEmpty(accessValue)) {
				String[] accessValues = accessValue.split(";");
				if(!CommonUtil.isEmpty(accessValues)) {
					for(String accessUser : accessValues) {
						UserInfo userInfo = getUserInfoByUserId(accessUser);
						DepartmentInfo departmentInfo = getDepartmentInfoByDepartmentId(accessUser);
						GroupInfo groupInfo = getGroupInfoByGroupId(accessUser);
						if(userInfo != null)
							communityInfoSet.add(userInfo);
						else if(departmentInfo != null)
							communityInfoSet.add(departmentInfo);
						else if(groupInfo != null)
							communityInfoSet.add(groupInfo);
					}
				}
			}
		} else if(accessLevel.equals("3")) {
			accessPolicy.setLevel(AccessPolicy.LEVEL_PUBLIC);
		}
	}

	public static void setAccessPolicyByNoMember(String accessLevel, AccessPolicy accessPolicy, Set<CommunityInfo> communityInfoSet, String accessValue) throws Exception {
		if(accessLevel.equals("2")) {
			accessPolicy.setLevel(AccessPolicy.LEVEL_CUSTOM);
			if(!CommonUtil.isEmpty(accessValue)) {
				String[] accessValues = accessValue.split(";");
				if(!CommonUtil.isEmpty(accessValues)) {
					for(String accessUser : accessValues) {
						UserInfo userInfo = getUserInfoByUserId(accessUser);
						DepartmentInfo departmentInfo = getDepartmentInfoByDepartmentId(accessUser);
						GroupInfo groupInfo = getGroupInfoByGroupId(accessUser);
						if(userInfo != null)
							communityInfoSet.add(userInfo);
						else if(departmentInfo != null)
							communityInfoSet.add(departmentInfo);
						else if(groupInfo != null)
							communityInfoSet.add(groupInfo);
					}
				}
			}
		} else {
			accessPolicy.setLevel(AccessPolicy.LEVEL_PRIVATE);
		}
	}

	public static boolean isAccessableInstance(Object object) throws Exception {
		if(object == null)
			return false;
		boolean isAccessableForMe = false;
		User user = SmartUtil.getCurrentUser();
		String userId = user.getId();

		AccessPolicy accessPolicy = new AccessPolicy();
		String workSpaceId = null;
		String workSpaceType = null;
		String accessLevel = null;
		String accessValue = null;
		String ownerId = null;
		String modifierId = null;
		if(object.getClass().equals(SwdRecord.class)) {
			SwdRecord swdRecord = (SwdRecord)object;
			workSpaceId = swdRecord.getWorkSpaceId() == null ? userId : swdRecord.getWorkSpaceId();
			workSpaceType = swdRecord.getWorkSpaceType() == null ? "4" : swdRecord.getWorkSpaceType();
			accessLevel = swdRecord.getAccessLevel() == null ? "3" : swdRecord.getAccessLevel();
			accessValue = swdRecord.getAccessValue();
			ownerId = swdRecord.getCreationUser();
			modifierId = swdRecord.getModificationUser();
		} else if(object.getClass().equals(PrcProcessInstExtend.class)) {
			PrcProcessInstExtend prcProcessInstExtend = (PrcProcessInstExtend)object;
			workSpaceId = prcProcessInstExtend.getPrcWorkSpaceId() == null ? userId : prcProcessInstExtend.getPrcWorkSpaceId();
			workSpaceType = prcProcessInstExtend.getPrcWorkSpaceType() == null ? "4" : prcProcessInstExtend.getPrcWorkSpaceType();
			accessLevel = prcProcessInstExtend.getPrcAccessLevel() == null ? "3" : prcProcessInstExtend.getPrcAccessLevel();
			accessValue = prcProcessInstExtend.getPrcAccessValue();
			ownerId = prcProcessInstExtend.getPrcCreateUser();
			modifierId = prcProcessInstExtend.getPrcModifyUser();
		} else if(object.getClass().equals(TaskWork.class)) {
			TaskWork taskWork = (TaskWork)object;
			workSpaceId = taskWork.getTskWorkSpaceId() == null ? userId : taskWork.getTskWorkSpaceId();
			workSpaceType = taskWork.getTskWorkSpaceType() == null ? "4" : taskWork.getTskWorkSpaceType();
			accessLevel = taskWork.getTskAccessLevel() == null ? "3" : taskWork.getTskAccessLevel();
			accessValue = taskWork.getTskAccessValue();
			ownerId = taskWork.getTskAssignee();
			modifierId = taskWork.getLastTskAssignee();
		} else if(object.getClass().equals(FileWork.class)) {
			FileWork fileWork = (FileWork)object;
			workSpaceId = fileWork.getTskWorkSpaceId() == null ? userId : fileWork.getTskWorkSpaceId();
			workSpaceType = fileWork.getTskWorkSpaceType() == null ? "4" : fileWork.getTskWorkSpaceType();
			accessLevel = fileWork.getTskAccessLevel() == null ? "3" : fileWork.getTskAccessLevel();
			accessValue = fileWork.getTskAccessValue();
			ownerId = fileWork.getTskAssignee();
			modifierId = fileWork.getLastTskAssignee();
		}

		Set<CommunityInfo> communityInfoSet = new LinkedHashSet<CommunityInfo>();
		CommunityInfo[] communitieInfos = null;

		if(workSpaceType.equals("4")) { //사용자공간
			setAccessPolicyByMember(accessLevel, accessPolicy, communityInfoSet, accessValue);
		} else if(workSpaceType.equals("5")) { //그룹공간
			GroupInfo[] groupInfos = communityService.getMyGroups();
			boolean isMember = false;
			if(!CommonUtil.isEmpty(groupInfos)) {
				for(GroupInfo groupInfo : groupInfos) {
					if(workSpaceId.equals(groupInfo.getId())) {
						isMember = true;
						break;
					}
				}
			}
			if(isMember) {
				setAccessPolicyByMember(accessLevel, accessPolicy, communityInfoSet, accessValue);
			} else {
				setAccessPolicyByNoMember(accessLevel, accessPolicy, communityInfoSet, accessValue);
			}
		} else if(workSpaceType.equals("6")) { //부서공간
			DepartmentInfo[] departmentInfos = communityService.getMyDepartments();
			boolean isMember = false;
			for(DepartmentInfo departmentInfo : departmentInfos) {
				if(workSpaceId.equals(departmentInfo.getId())) {
					isMember = true;
					break;
				}
			}
			if(isMember) {
				setAccessPolicyByMember(accessLevel, accessPolicy, communityInfoSet, accessValue);
			} else {
				setAccessPolicyByNoMember(accessLevel, accessPolicy, communityInfoSet, accessValue);
			}
		} else if(workSpaceType.equals("2")) { //업무인스턴스공간
			 //workspaceid가 dr_로 시작하는 정보관리업무는 어떤 업무인지 알수가 없음. 업무정보가 TskTask에 추가된 후에 개발 예정...
		}
		if(communityInfoSet.size() > 0) {
			communitieInfos = new CommunityInfo[communityInfoSet.size()];
			communityInfoSet.toArray(communitieInfos);
		}

		accessPolicy.setCommunitiesToOpen(communitieInfos);
		isAccessableForMe = accessPolicy.isAccessableForMe(ownerId, modifierId);

		return isAccessableForMe;
	}

	public static boolean isAccessibleAllInstance(String resourceId, String userId) throws Exception {

		if(CommonUtil.isEmpty(resourceId))
			return true;

		SwaResourceCond swaResourceCond = new SwaResourceCond();
		swaResourceCond.setResourceId(resourceId);
		swaResourceCond.setMode(SwaResource.MODE_READ);
		SwaResource swaResource = getSwaManager().getResource(userId, swaResourceCond, IManager.LEVEL_LITE);

		if(!CommonUtil.isEmpty(swaResource)) {
			String permission = swaResource.getPermission();
			if(permission.equals(SwaResource.PERMISSION_SELECT)) {
				SwaUserCond swaUserCond = new SwaUserCond();
				swaUserCond.setResourceId(resourceId);
				swaUserCond.setMode(SwaResource.MODE_READ);
				SwaUser[] swaUsers = getSwaManager().getUsers(userId, swaUserCond, IManager.LEVEL_LITE);
				if(!CommonUtil.isEmpty(swaUsers)) {
					for(SwaUser swaUser : swaUsers) {
						String authUserId = swaUser.getUserId();
						String type = swaUser.getType();
						if(type.equals(SwaUser.TYPE_USER)) {
							if(authUserId.equals(userId))
								return true;
						} else if(type.equals(SwaUser.TYPE_DEPT)) {
							DepartmentInfo[] departmentInfos = communityService.getMyDepartments();
							if(!CommonUtil.isEmpty(departmentInfos)) {
								for(DepartmentInfo departmentInfo : departmentInfos) {
									String deptId = departmentInfo.getId();
									if(authUserId.equals(deptId))
										return true;
								}
							}
						} else if(type.equals(SwaUser.TYPE_GROUP)){
							GroupInfo[] groupInfos = communityService.getMyGroups();
							if(!CommonUtil.isEmpty(groupInfos)) {
								for(GroupInfo groupInfo : groupInfos) {
									String groupId = groupInfo.getId();
									if(authUserId.equals(groupId))
										return true;
								}
							}
						}
					}
				}
			} else if(permission.equals(SwaResource.PERMISSION_NO)) {
				return false;
			} else if(permission.equals(SwaResource.PERMISSION_ALL)) {
				return true;
			}
		} else {
			return true;
		}
		return false;
	}

	public static boolean isWritablePackage(String resourceId, String userId) throws Exception {

		if(CommonUtil.isEmpty(resourceId))
			return true;

		SwaResourceCond swaResourceCond = new SwaResourceCond();
		swaResourceCond.setResourceId(resourceId);
		swaResourceCond.setMode(SwaResource.MODE_WRITE);
		SwaResource swaResource = getSwaManager().getResource(userId, swaResourceCond, IManager.LEVEL_LITE);

		if(!CommonUtil.isEmpty(swaResource)) {
			String permission = swaResource.getPermission();
			if(permission.equals(SwaResource.PERMISSION_SELECT)) {
				SwaUserCond swaUserCond = new SwaUserCond();
				swaUserCond.setResourceId(resourceId);
				swaUserCond.setMode(SwaResource.MODE_WRITE);
				SwaUser[] swaUsers = getSwaManager().getUsers(userId, swaUserCond, IManager.LEVEL_LITE);
				if(!CommonUtil.isEmpty(swaUsers)) {
					for(SwaUser swaUser : swaUsers) {
						String authUserId = swaUser.getUserId();
						String type = swaUser.getType();
						if(type.equals(SwaUser.TYPE_USER)) {
							if(authUserId.equals(userId))
								return true;
						} else if(type.equals(SwaUser.TYPE_DEPT)) {
							DepartmentInfo[] departmentInfos = communityService.getMyDepartments();
							if(!CommonUtil.isEmpty(departmentInfos)) {
								for(DepartmentInfo departmentInfo : departmentInfos) {
									String deptId = departmentInfo.getId();
									if(authUserId.equals(deptId))
										return true;
								}
							}
						} else if(type.equals(SwaUser.TYPE_GROUP)){
							GroupInfo[] groupInfos = communityService.getMyGroups();
							if(!CommonUtil.isEmpty(groupInfos)) {
								for(GroupInfo groupInfo : groupInfos) {
									String groupId = groupInfo.getId();
									if(authUserId.equals(groupId))
										return true;
								}
							}
						}
					}
				}
			} else if(permission.equals(SwaResource.PERMISSION_ALL)) {
				return true;
			}
		} else {
			return true;
		}
		return false;
	}

	public static PkgPackage[] getMyWritablePackages(PkgPackage[] pkgs) throws Exception {

		User user = SmartUtil.getCurrentUser();
		String userId = user.getId();
		Set<PkgPackage> newPkgSet = new LinkedHashSet<PkgPackage>();
		PkgPackage[] newPkgs = null;
		if(!CommonUtil.isEmpty(pkgs)) {
			for(PkgPackage pkg : pkgs) {
				String resourceId = getResourceIdByPkgPackage(pkg);
				if(!CommonUtil.isEmpty(resourceId)) {
					if(isWritablePackage(resourceId, userId))
						newPkgSet.add(pkg);
				} else {
					newPkgSet.add(pkg);
				}
			}
		}
		if(newPkgSet.size() > 0) {
			newPkgs = new PkgPackage[newPkgSet.size()];
			newPkgSet.toArray(newPkgs);
		}
		return newPkgs;
	}

	private static boolean isExistRunningPackageByCategoryId(String categoryId) throws Exception {

		PkgPackageCond cond = new PkgPackageCond();
		cond.setCategoryId(categoryId);
		cond.setStatus(PkgPackage.STATUS_DEPLOYED);
		long runningPackageCount = getPkgManager().getPackageSize("ModelConverter", cond);
		if (runningPackageCount > 0)
			return true;

		CtgCategoryCond ctgCond = new CtgCategoryCond();
		ctgCond.setParentId(categoryId);

		CtgCategory[] ctg = getCtgManager().getCategorys("ModelConverter", ctgCond, IManager.LEVEL_LITE);
		if (ctg == null) {
			return false;
		} else {
			for (int i = 0; i < ctg.length; i++) {
				if(isExistRunningPackageByCategoryId(ctg[i].getObjId())) {
					return true;
				}
			}
			return false;
		}
	}

	public static UserInfo[] convertSwoUserExtendsToUserInfos(SwoUserExtend[] swoUserExtends) throws Exception {
		if(CommonUtil.isEmpty(swoUserExtends))
			return null;
		UserInfo[] userInfos = null;
		List<UserInfo> userInfoList = new ArrayList<UserInfo>();

		for(SwoUserExtend swoUserExtend : swoUserExtends) {
			UserInfo userInfo = new UserInfo();
			userInfo.setId(swoUserExtend.getId());
			userInfo.setName(swoUserExtend.getName());
			userInfo.setNickName(swoUserExtend.getNickName());
			userInfo.setRole(SwoUser.USER_ROLE_DEPT_LEADER.equals(swoUserExtend.getRoleId()) ? User.USER_ROLE_LEADER : User.USER_ROLE_MEMBER);
			userInfo.setDepartment(new DepartmentInfo(swoUserExtend.getDepartmentId(), swoUserExtend.getDepartmentName(), swoUserExtend.getDepartmentDesc()));
			userInfo.setSmallPictureName(swoUserExtend.getSmallPictureName());
			userInfo.setBigPictureName(swoUserExtend.getBigPictureName());
			userInfo.setPosition(swoUserExtend.getPosition());
			userInfo.setCellPhoneNo(swoUserExtend.getCellPhoneNo());
			userInfo.setPhoneNo(swoUserExtend.getPhoneNo());
			userInfo.setUseSignPicture(swoUserExtend.isUseSign());
			userInfo.setSignPicture(swoUserExtend.getSign());
			userInfoList.add(userInfo);
		}
		if(userInfoList.size() > 0) {
			userInfos = new UserInfo[userInfoList.size()];
			userInfoList.toArray(userInfos);
		}
		return userInfos;
	}

	public static UserInfo getUserInfoByUserId(String userId) throws Exception {
		if (CommonUtil.isEmpty(userId))
			return null;
		SwoUserExtend userExtend = getSwoManager().getUserExtend(userId, userId, true);
		return getUserInfoBySwoUserExtend(null, userExtend);
	}

	public static UserInfo getUserInfoBySwoUserExtend(UserInfo userInfo, SwoUserExtend userExtend) throws Exception {
		if (userExtend == null)
			return new UserInfo();
		if (userInfo == null) 
			userInfo = new UserInfo();
		
		userInfo.setId(userExtend.getId());
		userInfo.setName(userExtend.getName());
		userInfo.setNickName(userExtend.getNickName());
		DepartmentInfo departInfo = new DepartmentInfo(userExtend.getDepartmentId(), userExtend.getDepartmentName(), userExtend.getDepartmentDesc());
		userInfo.setDepartment(departInfo);
		userInfo.setSmallPictureName(userExtend.getSmallPictureName());
		userInfo.setBigPictureName(userExtend.getBigPictureName());
		userInfo.setPosition(userExtend.getPosition());
		userInfo.setRole(userExtend.getRoleId().equals("DEPT LEADER") ? User.USER_ROLE_LEADER : User.USER_ROLE_MEMBER);
		userInfo.setCellPhoneNo(userExtend.getCellPhoneNo());
		userInfo.setPhoneNo(userExtend.getPhoneNo());
		userInfo.setUseSignPicture(userExtend.isUseSign());
		userInfo.setSignPicture(userExtend.getSign());
		userInfo.setEmployeeId(userExtend.getEmployeeId());
		userInfo.setHireDate(userExtend.getHireDate()==null?null:new LocalDate(userExtend.getHireDate().getTime()));
		userInfo.setBirthday(userExtend.getBirthDay()==null?null:new LocalDate(userExtend.getBirthDay().getTime()));
		userInfo.setLunarBirthday(userExtend.isLunarBirthday());
		
		userInfo.setSpaceType(ISmartWorks.SPACE_TYPE_USER);
		
		return userInfo;
	}

	public static Map<String, WorkCategoryInfo> getPkgCtgInfoMapByPackage(PkgPackage pkg) throws Exception {
		
		String categoryId = pkg.getCategoryId();
		if (CommonUtil.isEmpty(categoryId) || categoryId.equalsIgnoreCase(CtgCategory.ROOTCTGID))
			return null;

		CtgCategory ctg = getCtgCategoryByCategoryId(categoryId);
		CtgCategory parentCtg = getCtgCategoryByCategoryId(ctg.getParentId());
		
		Map<String, WorkCategoryInfo> resultMap = new HashMap<String, WorkCategoryInfo>();
		if (parentCtg == null || parentCtg.getObjId().equalsIgnoreCase(CtgCategory.ROOTCTGID)) {
			resultMap.put("category", (WorkCategoryInfo)getWorkCategoryInfoByCtgCategory(null, ctg));
			resultMap.put("group", null);
		} else {
			resultMap.put("category", (WorkCategoryInfo)getWorkCategoryInfoByCtgCategory(null, parentCtg));
			resultMap.put("group", (WorkCategoryInfo)getWorkCategoryInfoByCtgCategory(null, ctg));
		}
		return resultMap;
	}
	
	public static InstanceInfo[] getInstanceInfoArrayByPrcProcessInstArray(PrcProcessInst[] prcInsts) throws Exception {
		if (CommonUtil.isEmpty(prcInsts))
			return null;
		
		InstanceInfo[] instanceInfos = new InstanceInfo[prcInsts.length];
		for (int i =0; i < prcInsts.length; i ++) {
			PrcProcessInst prcInst = prcInsts[i];
			InstanceInfo instanceInfo = (InstanceInfo)getInstanceInfoByPrcProcessInst(null, prcInst);
			instanceInfos[i] = instanceInfo; 
		}
		return instanceInfos;
	}
	public static InstanceInfo getInstanceInfoByPrcProcessInst(InstanceInfo instanceInfo, PrcProcessInst prcInst) throws Exception {
		if (prcInst == null)
			return null;
		if (instanceInfo == null) 
			instanceInfo = new InstanceInfo();
		
		String id = prcInst.getObjId();
		String subject = prcInst.getTitle();
		int type = WorkInstance.TYPE_PROCESS;
		WorkInfo work = getWorkInfoByPackageId(prcInst.getDiagramId());//TODO prcInst.getPackageId 로 변경 해야함 현재(개발기간)에는 컬럼에 데이터가 없음
		
		int status = -1;
		if (prcInst.getStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_RUNNING)) {
//			status = Instance.STATUS_COMPLETED;
			status = Instance.STATUS_RUNNING;
		} else if (prcInst.getStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_COMPLETE)) {
			status = Instance.STATUS_COMPLETED;
		} else if (prcInst.getStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_RETURN)) {
			status = Instance.STATUS_RETURNED;
		} else if (prcInst.getStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_ABORTED)) {
			status = Instance.STATUS_ABORTED;
		} else if (prcInst.getStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_CANCEL)) {
			status = Instance.STATUS_REJECTED;
		}
		
		UserInfo owner = ModelConverter.getUserInfoByUserId(prcInst.getCreationUser());
		
		TskTask lastTask = getLastExecutedTskTaskByPrcInstId(prcInst.getObjId());
		if (lastTask == null) {
			instanceInfo.setLastModifier(new UserInfo());
			instanceInfo.setLastModifiedDate(new LocalDate(1)); //TODO LastModifiedDate now
		} else {
			instanceInfo.setLastModifier(getUserInfoByUserId(lastTask.getAssignee()));
			instanceInfo.setLastModifiedDate(new LocalDate(lastTask.getExecutionDate().getTime())); //TODO LastModifiedDate now
			if(status == Instance.STATUS_RUNNING && lastTask.getExpectEndDate()!=null && lastTask.getExpectEndDate().getTime()<(new LocalDate()).getTime()){
				status = Instance.STATUS_DELAYED_RUNNING;
			}
		}
		
		instanceInfo.setId(id);
		instanceInfo.setOwner(owner);
		instanceInfo.setStatus(status);
		instanceInfo.setSubject(subject);
		instanceInfo.setType(type);
		instanceInfo.setWorkInfo(work);
		instanceInfo.setWorkSpaceInfo(getWorkSpaceInfo(prcInst.getWorkSpaceType(), prcInst.getWorkSpaceId()));
		
		return instanceInfo;
	}
	public static WorkInstanceInfo getWorkInstanceInfoByPrcProcessInstId(String prcInstId) throws Exception {
		if (CommonUtil.isEmpty(prcInstId))
			return null;
		PrcProcessInst prcInst = getPrcManager().getProcessInst("", prcInstId, IManager.LEVEL_LITE);
		return getWorkInstanceInfoByPrcProcessInst(null, prcInst);
	}
	public static WorkInstanceInfo getWorkInstanceInfoByPrcProcessInst(WorkInstanceInfo workInstanceInfo, PrcProcessInst prcInst) throws Exception {
		if (prcInst == null)
			return null;
		if (workInstanceInfo == null) 
			workInstanceInfo = new WorkInstanceInfo();
		
		getInstanceInfoByPrcProcessInst(workInstanceInfo, prcInst);
		
		TskTaskCond tskCond = new TskTaskCond();
		tskCond.setProcessInstId(prcInst.getObjId());
		String fieldName = "creationDate";
		tskCond.setStatus(TskTask.TASKSTATUS_ASSIGN);
		
		//TskTask[] runningTask = getTskManager().getTasks("", tskCond, IManager.LEVEL_LITE);
		
		//TaskInstanceInfo[] runningTasks = getTaskInstanceInfoArrayByTskTaskArray(workInstanceInfo, runningTask);
		
		//workInstanceInfo.setRunningTasks(runningTasks);
		
		return workInstanceInfo;
	}
	public static PWInstanceInfo[] getPWInstanceInfoArrayByPrcProcessInstArray(PrcProcessInst[] prcInsts) throws Exception {
		if (CommonUtil.isEmpty(prcInsts))
			return null;
		
		PWInstanceInfo[] pWInstanceInfos = new PWInstanceInfo[prcInsts.length];
		for (int i =0; i < prcInsts.length; i ++) {
			PrcProcessInst prcInst = prcInsts[i];
			PWInstanceInfo pWInstanceInfo = (PWInstanceInfo)getPWInstanceInfoByPrcProcessInst(null, prcInst);
			pWInstanceInfos[i] = pWInstanceInfo; 
		}
		return pWInstanceInfos;
	}
	public static PWInstanceInfo getPWInstanceInfoByPrcProcessInst(PWInstanceInfo pWInstanceInfo, PrcProcessInst prcInst) throws Exception {
		if (prcInst == null)
			return null;
		if (pWInstanceInfo == null) 
			pWInstanceInfo = new PWInstanceInfo();
		
		getWorkInstanceInfoByPrcProcessInst(pWInstanceInfo, prcInst);
		
		TskTaskCond tskCond = new TskTaskCond();
		tskCond.setProcessInstId(prcInst.getObjId());
		String fieldName = "creationDate";
		boolean isAsc = false;
		tskCond.setOrders(new Order[]{new Order(fieldName, isAsc)});
		tskCond.setPageNo(0);
		tskCond.setPageSize(1);
		
//		TskTask[] lastSwTask = getTskManager().getTasks("", tskCond, IManager.LEVEL_LITE);
		TskTask lastSwTask = getLastTskTaskByInstanceId(prcInst.getObjId());
		
		TaskInstanceInfo lastTask = getTaskInstanceInfoByTskTask(pWInstanceInfo, null, lastSwTask);
		pWInstanceInfo.setLastTask(lastTask); 
		
		return pWInstanceInfo;
	}
	
	public static InstanceInfo getInstanceInfoByTskTask(InstanceInfo instanceInfo, TskTask task) throws Exception {
		if (task == null)
			return null;
		if (instanceInfo == null) 
			instanceInfo = new InstanceInfo();

		String id = task.getObjId();
		String subject = task.getTitle();
		int type = WorkInstance.TYPE_TASK;
		WorkInfo work = getWorkInfoByTask(task);
		
		int status = 0;
		if (task.getStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ASSIGN)) {
			if(task.getExpectEndDate()!=null && task.getExpectEndDate().getTime()<(new LocalDate()).getTime())
				status = Instance.STATUS_DELAYED_RUNNING;
			else
				status = Instance.STATUS_RUNNING;
		} else if (task.getStatus().equalsIgnoreCase(TskTask.TASKSTATUS_COMPLETE)) {
			status = Instance.STATUS_COMPLETED;
			if (task.getIsApprovalSourceTask() != null && task.getIsApprovalSourceTask().equalsIgnoreCase("true")) {
				if (task.getTargetApprovalStatus().equalsIgnoreCase(AprApproval.APPROVAL_STATUS_RUNNING)) {
					if(task.getExpectEndDate()!=null && task.getExpectEndDate().getTime()<(new LocalDate()).getTime()){
						status = Instance.STATUS_DELAYED_RUNNING;
					}else{
						status = Instance.STATUS_RUNNING;						
					}
				}else{
					status = Instance.STATUS_COMPLETED;											
				}
			}
		} else if (task.getStatus().equalsIgnoreCase(TskTask.TASKSTATUS_RETURNED)) {
			status = Instance.STATUS_RETURNED;
		} else if (task.getStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ABORTED)) {
			status = Instance.STATUS_ABORTED;
		} else if (task.getStatus().equalsIgnoreCase(TskTask.TASKSTATUS_CREATE)) {
			status = Instance.STATUS_PLANNED;
		} else if (task.getStatus().equalsIgnoreCase(TskTask.TASKSTATUS_CANCEL)) {
			status = Instance.STATUS_REJECTED;
		}
		UserInfo owner = ModelConverter.getUserInfoByUserId(task.getCreationUser());
		LocalDate createdDate = new LocalDate(task.getCreationDate().getTime());
		UserInfo lastModifier = ModelConverter.getUserInfoByUserId(task.getModificationUser()); 
		LocalDate lastModifiedDate = new LocalDate(task.getModificationDate().getTime());

		instanceInfo.setId(id);
		instanceInfo.setOwner(owner);
		instanceInfo.setCreatedDate(createdDate);
		instanceInfo.setLastModifiedDate(lastModifiedDate);
		instanceInfo.setLastModifier(lastModifier);
		instanceInfo.setStatus(status);
		instanceInfo.setSubject(subject);
		instanceInfo.setType(type);
		instanceInfo.setWorkInfo(work);
		if (task.getWorkSpaceId() != null) {
			instanceInfo.setWorkSpaceInfo(getWorkSpaceInfo(task.getWorkSpaceType(), task.getWorkSpaceId()));
		} else {
			User user = SmartUtil.getCurrentUser();
			instanceInfo.setWorkSpaceInfo(new WorkSpaceInfo(user.getId(), null));
		}
		return instanceInfo;
	}

	public static TaskInstanceInfo[] getTaskInstanceInfoArrayByTskTaskArray(WorkInstanceInfo workInstObj, TskTask[] swTasks) throws Exception {
		if (CommonUtil.isEmpty(swTasks))
			return null;

		TaskInstanceInfo[] taskInstanceInfos = new TaskInstanceInfo[swTasks.length];
		for (int i =0; i < swTasks.length; i ++) {
			TskTask task = swTasks[i];
			TaskInstanceInfo taskInstanceInfo = (TaskInstanceInfo)getTaskInstanceInfoByTskTask(workInstObj, null, task);
			taskInstanceInfos[i] = taskInstanceInfo; 
		}
		return taskInstanceInfos;
	}

	public static TaskInstanceInfo getTaskInstanceInfoByTskTask(WorkInstanceInfo paretWorkInstObj, TaskInstanceInfo taskInstInfo, TskTask swTask) throws Exception {
		if (swTask == null)
			return null;
		if (taskInstInfo == null)
			taskInstInfo = new TaskInstanceInfo();

		getInstanceInfoByTskTask (taskInstInfo, swTask);

		String name = swTask.getName();
		int type = WorkInstance.TYPE_TASK;
		int taskType = 0;

		String tskType = swTask.getType();
		String tskStatus = swTask.getStatus();
		if (tskStatus.equalsIgnoreCase("11")) {
			if(TskTask.TASKSTATUS_CANCEL.equalsIgnoreCase(swTask.getTargetApprovalStatus())){
				taskInstInfo.setStatus(Instance.STATUS_REJECTED);
			}else{
				LocalDate now = new LocalDate();
				if(swTask.getExpectEndDate()!=null && swTask.getExpectEndDate().getTime()<now.getTime())
					taskInstInfo.setStatus(Instance.STATUS_DELAYED_RUNNING);
				else
					taskInstInfo.setStatus(Instance.STATUS_RUNNING);
				}
		} else if (tskStatus.equals("21")) {
			if("true".equalsIgnoreCase(swTask.getIsApprovalSourceTask()) && !"APPROVAL".equalsIgnoreCase(swTask.getType())){
				if(AprApproval.APPROVAL_STATUS_RUNNING.equals(swTask.getTargetApprovalStatus())){
					if(swTask.getExpectEndDate()!=null && swTask.getExpectEndDate().getTime()<(new LocalDate()).getTime()){
						taskInstInfo.setStatus(Instance.STATUS_DELAYED_RUNNING);					
					}else{
						taskInstInfo.setStatus(Instance.STATUS_RUNNING);
					}
				}else{
					taskInstInfo.setStatus(Instance.STATUS_COMPLETED);
				}
			} else{
				taskInstInfo.setStatus(Instance.STATUS_COMPLETED);
			}
		}

		if(tskType.equals(TskTask.TASKTYPE_SINGLE)) {
			if(tskStatus.equals("11")) {
				taskType = TaskInstance.TYPE_INFORMATION_TASK_ASSIGNED;
			} else if(tskStatus.equals("21")) {
				taskType = TaskInstance.TYPE_INFORMATION_TASK_UPDATED;
			}
		} else if(tskType.equals(TskTask.TASKTYPE_REFERENCE)) {
			String approvalId = swTask.getApprovalId();
			if (!CommonUtil.isEmpty(approvalId)) {
				taskType = TaskInstance.TYPE_APPROVAL_TASK_FORWARDED;
			} else {
				if (paretWorkInstObj.getWorkId() != null && paretWorkInstObj.getWorkType() == ProcessWork.TYPE_PROCESS) {
					taskType = TaskInstance.TYPE_PROCESS_TASK_FORWARDED;
				} else {
					taskType = TaskInstance.TYPE_INFORMATION_TASK_FORWARDED;
				}
			}
			taskInstInfo.setComments(swTask.getDocument());
			taskInstInfo.setContent(swTask.getExtendedPropertyValue("workContents"));
			taskInstInfo.setForwardId(swTask.getForwardId());
			if (!CommonUtil.isEmpty(swTask.getApprovalId()))
				taskInstInfo.setApprovalId(swTask.getApprovalId());
//			taskInstInfo.setAssigner(getUserInfoByUserId(swTask.getExtendedPropertyValue("processInstCreationUser")));
			taskInstInfo.setAssigner(getUserInfoByUserId(swTask.getAssigner()));
		} else if(tskType.equals(TskTask.TASKTYPE_APPROVAL)) {
			taskType = TaskInstance.TYPE_APPROVAL_TASK_ASSIGNED;
			taskInstInfo.setComments(swTask.getDocument());
			taskInstInfo.setContent(swTask.getExtendedPropertyValue("txtApprovalComments"));
			taskInstInfo.setApprovalId(swTask.getApprovalId());
			taskInstInfo.setApprovalTaskId(swTask.getFromRefId());
//			taskInstInfo.setAssigner(getUserInfoByUserId(swTask.getExtendedPropertyValue("processInstCreationUser")));
			taskInstInfo.setAssigner(getUserInfoByUserId(swTask.getAssigner()));
		} else if(tskType.equalsIgnoreCase(TskTask.TASKTYPE_SUBFLOW)) {
			taskType = TaskInstance.TYPE_PROCESS_SUB_TASK_CREATED;
			taskInstInfo.setSubTask(true);
			
			String targetPrcId = swTask.getExtendedPropertyValue("targetPrcId");
			//DB 조회 **********************
			PrcProcessCond prcCond = new PrcProcessCond();
			prcCond.setProcessId(targetPrcId);
			PrcProcess[] prcs = SwManagerFactory.getInstance().getPrcManager().getProcesses("", prcCond, IManager.LEVEL_LITE);
			if (prcs != null) {
				String targetPkgId = prcs[0].getDiagramId();
				String targetPkgName = prcs[0].getName();
				taskInstInfo.setSubWorkId(targetPkgId);
				taskInstInfo.setSubWorkFullpathName(targetPkgName);
			}
			String targetPrcInstId = swTask.getExtendedPropertyValue("targetPrcInstId");
			taskInstInfo.setSubWorkInstanceId(targetPrcInstId);			
		} else if (tskType.equalsIgnoreCase(TskTask.TASKTYPE_COMMON)) {
			
			FileDownloadHistoryCond cond = new FileDownloadHistoryCond();
			cond.setRefTaskId(swTask.getObjId());
			long downloadCount = SwManagerFactory.getInstance().getDocManager().getFileDownloadHistorySize("", cond);
			taskInstInfo.setNumberOfDownloadHistories((int)downloadCount);
			String approvalLineId = swTask.getExtendedPropertyValue("ApprovalLine");
			if(!SmartUtil.isBlankObject(approvalLineId)){
				taskInstInfo.setApprovalLineId(approvalLineId);
			}
		}
		
		if (swTask.getIsApprovalSourceTask() != null && swTask.getIsApprovalSourceTask().equalsIgnoreCase("true")) {
			taskInstInfo.setApprovalWork(true);
		}
		
		String assignee = swTask.getAssignee();
		String performer = swTask.getAssignee();
		String formId = swTask.getForm();

// 프로세스인스턴스가 태스크인스턴스를 포함하고 태스크인스턴스는 프로세스 인스턴스를 포함하기 때문에 무한 루프가 발생한다
// 하여 태스크 인스턴스를 만들때는 부모 프로세스 인스턴스의 객체 래퍼런스를 가져와서 태스크에다가 주입한다
//		WorkInstanceInfo workInstanceInfo = getWorkInstanceInfoByPrcProcessInstId(swTask.getProcessInstId());
		WorkInstanceInfo workInstanceInfo = paretWorkInstObj;

		taskInstInfo.setName(name);
		taskInstInfo.setTaskType(taskType);
		taskInstInfo.setType(type);
		taskInstInfo.setAssignee(getUserInfoByUserId(assignee));
		taskInstInfo.setPerformer(getUserInfoByUserId(performer));
		taskInstInfo.setWorkInstance(workInstanceInfo);
		taskInstInfo.setFormId(formId);
		
		return taskInstInfo;
	}

	// #########################################  specific class  ########################################################################

	public static User[] getUserArrayBySwoUserExtendArray(SwoUserExtend[] userExtends) throws Exception {
		if (CommonUtil.isEmpty(userExtends))
			return null;
		User[] users = new User[userExtends.length];
		for (int i =0; i < userExtends.length; i++) {
			SwoUserExtend userExtend = userExtends[i];
			users[i] = getUserBySwoUserExtend(null, userExtend);
		}
		return users;
	}
	public static User getUserByUserId(String userId) throws Exception {
		if (CommonUtil.isEmpty(userId))
			return null;
		SwoUserExtend userExtend = getSwoManager().getUserExtend(userId, userId, true);
		return getUserBySwoUserExtend(null, userExtend);
	}
	public static User getUserBySwoUserExtend(User user, SwoUserExtend userExtend) throws Exception {
		if (userExtend == null)
			return null;
		if (user == null)
			user = new User();

		user.setId(userExtend.getId());
		user.setName(userExtend.getName());
		user.setPassword(userExtend.getPassword());
		user.setCompanyId(userExtend.getCompanyId());
		user.setCompany(userExtend.getCompanyName());
		user.setDepartmentId(userExtend.getDepartmentId());
		user.setDepartment(userExtend.getDepartmentName());
		user.setBigPictureName(userExtend.getBigPictureName());
		user.setSmallPictureName(userExtend.getSmallPictureName());
		user.setPosition(userExtend.getPosition());
		user.setLocale(userExtend.getLocale());
		user.setCompany(userExtend.getCompanyName());
		user.setTimeZone(userExtend.getTimeZone());
		user.setUserLevel(userExtend.getAuthId().equals("EXTERNALUSER") ? User.USER_LEVEL_EXTERNAL_USER : userExtend.getAuthId().equals("USER") ? User.USER_LEVEL_INTERNAL_USER : userExtend.getAuthId().equals("ADMINISTRATOR") ? User.USER_LEVEL_AMINISTRATOR : User.USER_LEVEL_SYSMANAGER);
		user.setRole(userExtend.getRoleId().equals("DEPT LEADER") ? User.USER_ROLE_LEADER : User.USER_ROLE_MEMBER);
		user.setEmployeeId(userExtend.getEmployeeId());
		user.setPhoneNo(userExtend.getPhoneNo());
		user.setCellPhoneNo(userExtend.getCellPhoneNo());
		user.setUseSignPicture(userExtend.isUseSign());
		user.setSignPicture(userExtend.getSign());
		if (userExtend.getHireDate() != null)
			user.setHireDate(new LocalDate(userExtend.getHireDate().getTime()));
		if (userExtend.getBirthDay() != null)
			user.setBirthday(new LocalDate(userExtend.getBirthDay().getTime()));
		user.setLunarBirthday(userExtend.isLunarBirthday());
		user.setHomePhoneNo(userExtend.getHomePhoneNo());
		user.setHomeAddress(userExtend.getHomeAddress());

		return user;
	}

	public static DepartmentInfo getDepartmentInfoByDepartmentId(String departmentId) throws Exception {
		if (CommonUtil.isEmpty(departmentId))
			return null;
		User cUser = SmartUtil.getCurrentUser();
		SwoDepartmentExtend departmentExtend = getSwoManager().getDepartmentExtend(cUser.getId(), departmentId, true);
		return getDepartmentInfoBySwoUserExtend(null, departmentExtend);
	}

	public static DepartmentInfo getDepartmentInfoBySwoUserExtend(DepartmentInfo departmentInfo, SwoDepartmentExtend departmentExtend) throws Exception {
		if (departmentExtend == null)
			return null;
		if (departmentInfo == null) 
			departmentInfo = new DepartmentInfo();

		departmentInfo.setId(departmentExtend.getId());
		departmentInfo.setName(departmentExtend.getName());
		departmentInfo.setDesc(departmentExtend.getDescription());
		departmentInfo.setSmallPictureName(departmentExtend.getSmallPictureName());
		departmentInfo.setBigPictureName(departmentExtend.getBigPictureName());
		departmentInfo.setFullpathName(getDepartmentInfoFullpathNameByDepartmentId(departmentExtend.getId()));
		departmentInfo.setHead(getUserInfoByUserId(departmentExtend.getHeadId()));
		
		departmentInfo.setSpaceType(ISmartWorks.SPACE_TYPE_DEPARTMENT);
		return departmentInfo;
	}
	public static String getDepartmentInfoFullpathNameByDepartmentId(String departmentInfoId) throws Exception {
		
		User user = SmartUtil.getSystemUser();
		String fullpathName = "";
		try{
			SwoDepartmentExtend department = getSwoManager().getDepartmentExtend(user.getId(), departmentInfoId, true);
			fullpathName = getDepartmentInfoParentsfullpathNameByDepartmentId(department,"");
		}catch (Exception e){
		}
		return fullpathName;
	}
	
	public static String getDepartmentInfoParentsfullpathNameByDepartmentId(SwoDepartmentExtend departmentInfo, String fullpathName) throws Exception {
		
		if(departmentInfo.getParentId()!= null){
			fullpathName = departmentInfo.getName() + (SmartUtil.isBlankObject(fullpathName) ? "" : "▶") + fullpathName;
			try{
				SwoDepartmentExtend departmentId = getSwoManager().getDepartmentExtend(null, departmentInfo.getParentId(), true);								
				fullpathName = getDepartmentInfoParentsfullpathNameByDepartmentId(departmentId, fullpathName);
			}catch (Exception e){
			}
		}
		return fullpathName;
	}
	
	public static Department getDepartmentByDepartmentId(String departmentId) throws Exception {
		if (CommonUtil.isEmpty(departmentId))
			return null;
		User cUser = SmartUtil.getCurrentUser();
		SwoDepartmentExtend departmentExtend = getSwoManager().getDepartmentExtend(cUser.getId(), departmentId, true);
		return getDepartmentBySwoDepartment(null, departmentExtend);
	}

	public static Department getDepartmentBySwoDepartment(Department department, SwoDepartmentExtend departmentExtend) throws Exception {
		if (departmentExtend == null)
			return null;
		if (department == null)
			department = new Department();

		User cUser = SmartUtil.getCurrentUser();

		department.setId(departmentExtend.getId());
		department.setName(departmentExtend.getName());
		department.setDesc(departmentExtend.getDescription());
		department.setBigPictureName(departmentExtend.getBigPictureName());
		department.setSmallPictureName(departmentExtend.getSmallPictureName());
		department.setFullpathName(getFullpathNameByDepartmentId(departmentExtend.getId()));

		DepartmentInfo parent = getDepartmentInfoByDepartmentId(departmentExtend.getParentId());
		if(parent != null) {
			department.setParent(parent);
		} else {
			parent = new DepartmentInfo();
			department.setParent(parent);
		}

		User head = getUserByUserId(departmentExtend.getHeadId());
		if(head != null) {
			department.setHead(head);
		}

		List<UserInfo> userInfoList = new ArrayList<UserInfo>();
		SwoUserExtend[] userExtends = getSwoManager().getUsersOfDepartment(cUser.getId(), department.getId());
		if(userExtends != null) {
			for(SwoUserExtend swoUserExtend : userExtends) {
				UserInfo member = new UserInfo();
				member.setId(swoUserExtend.getId());
				member.setName(swoUserExtend.getName());
				member.setPosition(swoUserExtend.getPosition());
				member.setRole(swoUserExtend.getAuthId().equals("EXTERNALUSER") ? User.USER_LEVEL_EXTERNAL_USER : swoUserExtend.getAuthId().equals("USER") ? User.USER_LEVEL_INTERNAL_USER : swoUserExtend.getAuthId().equals("ADMINISTRATOR") ? User.USER_LEVEL_AMINISTRATOR : User.USER_LEVEL_SYSMANAGER);
				member.setSmallPictureName(swoUserExtend.getSmallPictureName());
				member.setDepartment(new DepartmentInfo(swoUserExtend.getDepartmentId(), swoUserExtend.getDepartmentName(), swoUserExtend.getDepartmentDesc()));
				member.setUseSignPicture(swoUserExtend.isUseSign());
				member.setSignPicture(swoUserExtend.getSign());
				userInfoList.add(member);
			}

			UserInfo[] members = new UserInfo[userInfoList.size()];
			userInfoList.toArray(members);
			department.setMembers(members);
		}

		List<DepartmentInfo> departmentInfoList = new ArrayList<DepartmentInfo>();
		SwoDepartmentExtend[] departmentExtends = getSwoManager().getChildrenOfDepartment(cUser.getId(), department.getId());
		if(departmentExtends != null) {
			for(SwoDepartmentExtend swoDepartmentExtend : departmentExtends) {
				DepartmentInfo child = new DepartmentInfo();
				child.setId(swoDepartmentExtend.getId());
				child.setName(swoDepartmentExtend.getName());
				child.setDesc(swoDepartmentExtend.getDescription());
				departmentInfoList.add(child);
			}
	
			DepartmentInfo[] children = new DepartmentInfo[departmentInfoList.size()];
			departmentInfoList.toArray(children);
			department.setChildren(children);
		}

		SwaDepartmentCond deptAuthCond = new SwaDepartmentCond();
		deptAuthCond.setDeptId(departmentExtend.getId());
		SwaDepartment[] authDepts = SwManagerFactory.getInstance().getSwaManager().getAuthDepartments(cUser.getId(), deptAuthCond, null);

		SwaDepartment boardWrite = null;
		SwaDepartment boardEdit = null;
		SwaDepartment eventWrite = null;
		SwaDepartment eventEdit = null;
		
		if (authDepts == null || authDepts.length == 0) {

			boardWrite = new SwaDepartment();
			boardWrite.setDeptAuthType(SwaDepartment.DEPT_AUTHTYPE_BOARD_WRITE);
			boardWrite.setDeptId(departmentExtend.getId());
			boardWrite.setRoleKey(SwaDepartment.DEPT_ROLEKYE_ADMIN + ";" + SwaDepartment.DEPT_ROLEKYE_LEADER);

			boardEdit = new SwaDepartment();
			boardEdit.setDeptAuthType(SwaDepartment.DEPT_AUTHTYPE_BOARD_EDIT);
			boardEdit.setDeptId(departmentExtend.getId());
			boardEdit.setRoleKey(SwaDepartment.DEPT_ROLEKYE_OWNER + ";" + SwaDepartment.DEPT_ROLEKYE_ADMIN + ";" + SwaDepartment.DEPT_ROLEKYE_LEADER);

			eventWrite = new SwaDepartment();
			eventWrite.setDeptAuthType(SwaDepartment.DEPT_AUTHTYPE_EVENT_WRITE);
			eventWrite.setDeptId(departmentExtend.getId());
			eventWrite.setRoleKey(SwaDepartment.DEPT_ROLEKYE_ADMIN + ";" + SwaDepartment.DEPT_ROLEKYE_LEADER);
			
			eventEdit = new SwaDepartment();
			eventEdit.setDeptAuthType(SwaDepartment.DEPT_AUTHTYPE_EVENT_EDIT);
			eventEdit.setDeptId(departmentExtend.getId());
			eventEdit.setRoleKey(SwaDepartment.DEPT_ROLEKYE_OWNER + ";" + SwaDepartment.DEPT_ROLEKYE_ADMIN + ";" + SwaDepartment.DEPT_ROLEKYE_LEADER);
			
			SwManagerFactory.getInstance().getSwaManager().setAuthDepartment(cUser.getId(), boardWrite, null);
			SwManagerFactory.getInstance().getSwaManager().setAuthDepartment(cUser.getId(), boardEdit, null);
			SwManagerFactory.getInstance().getSwaManager().setAuthDepartment(cUser.getId(), eventWrite, null);
			SwManagerFactory.getInstance().getSwaManager().setAuthDepartment(cUser.getId(), eventEdit, null);
			
		} else {
			for (int i = 0; i < authDepts.length; i++) {
				SwaDepartment deptAuth = authDepts[i];
				String deptAuthType = deptAuth.getDeptAuthType();
				if (deptAuthType.equalsIgnoreCase(SwaDepartment.DEPT_AUTHTYPE_BOARD_WRITE)) {
					boardWrite = deptAuth;
				} else if (deptAuthType.equalsIgnoreCase(SwaDepartment.DEPT_AUTHTYPE_BOARD_EDIT)) {
					boardEdit = deptAuth;
				} else if (deptAuthType.equalsIgnoreCase(SwaDepartment.DEPT_AUTHTYPE_EVENT_WRITE)) {
					eventWrite = deptAuth;
				} else if (deptAuthType.equalsIgnoreCase(SwaDepartment.DEPT_AUTHTYPE_EVENT_EDIT)) {
					eventEdit = deptAuth;
				}
			}
		}
		
		SpacePolicy boardWritePolicy = new SpacePolicy();
		String roleKeysStr = boardWrite.getRoleKey();
		String[] roleKeys = StringUtils.tokenizeToStringArray(roleKeysStr, ";");
		for (int i = 0; i < roleKeys.length; i++) {
			if (roleKeys[i].equalsIgnoreCase(SwaDepartment.DEPT_ROLEKYE_ADMIN)) {
				boardWritePolicy.setSystemAdministratorChecked(true);
			} else if (roleKeys[i].equalsIgnoreCase(SwaDepartment.DEPT_ROLEKYE_CUSTOM)) {
				boardWritePolicy.setCustomChecked(true);
				String usersStr = boardWrite.getCustomUser();
				String[] users = StringUtils.tokenizeToStringArray(usersStr, ";");
				
				//SwoUserExtend[] userExtendsArray = SwManagerFactory.getInstance().getSwoManager().getUsersExtend(cUser.getId(), users);
				//UserInfo[] userInfoArray = new UserInfo[userExtendsArray.length];
				//for (int j = 0; j < userExtendsArray.length; j++) {
				//	userInfoArray[j] = ModelConverter.getUserInfoBySwoUserExtend(null, userExtendsArray[j]);
				//}
				boardWritePolicy.setCustoms(getWorkSpaceInfoByIds(users));
			} else if (roleKeys[i].equalsIgnoreCase(SwaDepartment.DEPT_ROLEKYE_LEADER)) {
				boardWritePolicy.setLeaderChecked(true);
			} else if (roleKeys[i].equalsIgnoreCase(SwaDepartment.DEPT_ROLEKYE_MEMBER)) {
				boardWritePolicy.setMembersChecked(true);
			} else if (roleKeys[i].equalsIgnoreCase(SwaDepartment.DEPT_ROLEKYE_OWNER)) {
				boardWritePolicy.setOwnerChecked(true);
			}
		}
		
		SpacePolicy boardEditPolicy = new SpacePolicy();
		String roleKeysBoardEditStr = boardEdit.getRoleKey();
		String[] roleKeyBoardEdits = StringUtils.tokenizeToStringArray(roleKeysBoardEditStr, ";");
		for (int i = 0; i < roleKeyBoardEdits.length; i++) {
			if (roleKeyBoardEdits[i].equalsIgnoreCase(SwaDepartment.DEPT_ROLEKYE_ADMIN)) {
				boardEditPolicy.setSystemAdministratorChecked(true);
			} else if (roleKeyBoardEdits[i].equalsIgnoreCase(SwaDepartment.DEPT_ROLEKYE_CUSTOM)) {
				boardEditPolicy.setCustomChecked(true);
				String usersStr = boardEdit.getCustomUser();
				String[] users = StringUtils.tokenizeToStringArray(usersStr, ";");
				
				//SwoUserExtend[] userExtendsArray = SwManagerFactory.getInstance().getSwoManager().getUsersExtend(cUser.getId(), users);
				//UserInfo[] userInfoArray = new UserInfo[userExtendsArray.length];
				//for (int j = 0; j < userExtendsArray.length; j++) {
				//	userInfoArray[j] = ModelConverter.getUserInfoBySwoUserExtend(null, userExtendsArray[j]);
				//}
				
				boardEditPolicy.setCustoms(getWorkSpaceInfoByIds(users));
			} else if (roleKeyBoardEdits[i].equalsIgnoreCase(SwaDepartment.DEPT_ROLEKYE_LEADER)) {
				boardEditPolicy.setLeaderChecked(true);
			} else if (roleKeyBoardEdits[i].equalsIgnoreCase(SwaDepartment.DEPT_ROLEKYE_MEMBER)) {
				boardEditPolicy.setMembersChecked(true);
			} else if (roleKeyBoardEdits[i].equalsIgnoreCase(SwaDepartment.DEPT_ROLEKYE_OWNER)) {
				boardEditPolicy.setOwnerChecked(true);
			}
		}
		
		SpacePolicy eventWritePolicy = new SpacePolicy();
		String roleKeysEventWriteStr = eventWrite.getRoleKey();
		String[] roleKeyEventWrites = StringUtils.tokenizeToStringArray(roleKeysEventWriteStr, ";");
		for (int i = 0; i < roleKeyEventWrites.length; i++) {
			if (roleKeyEventWrites[i].equalsIgnoreCase(SwaDepartment.DEPT_ROLEKYE_ADMIN)) {
				eventWritePolicy.setSystemAdministratorChecked(true);
			} else if (roleKeyEventWrites[i].equalsIgnoreCase(SwaDepartment.DEPT_ROLEKYE_CUSTOM)) {
				eventWritePolicy.setCustomChecked(true);
				String usersStr = eventWrite.getCustomUser();
				String[] users = StringUtils.tokenizeToStringArray(usersStr, ";");
				
				//SwoUserExtend[] userExtendsArray = SwManagerFactory.getInstance().getSwoManager().getUsersExtend(cUser.getId(), users);
				//UserInfo[] userInfoArray = new UserInfo[userExtendsArray.length];
				//for (int j = 0; j < userExtendsArray.length; j++) {
				//	userInfoArray[j] = ModelConverter.getUserInfoBySwoUserExtend(null, userExtendsArray[j]);
				//}
				eventWritePolicy.setCustoms(getWorkSpaceInfoByIds(users));
			} else if (roleKeyEventWrites[i].equalsIgnoreCase(SwaDepartment.DEPT_ROLEKYE_LEADER)) {
				eventWritePolicy.setLeaderChecked(true);
			} else if (roleKeyEventWrites[i].equalsIgnoreCase(SwaDepartment.DEPT_ROLEKYE_MEMBER)) {
				eventWritePolicy.setMembersChecked(true);
			} else if (roleKeyEventWrites[i].equalsIgnoreCase(SwaDepartment.DEPT_ROLEKYE_OWNER)) {
				eventWritePolicy.setOwnerChecked(true);
			}
		}
		
		SpacePolicy eventEditPolicy = new SpacePolicy();
		String roleKeysEventEditStr = eventEdit.getRoleKey();
		String[] roleKeyEventEdits = StringUtils.tokenizeToStringArray(roleKeysEventEditStr, ";");
		for (int i = 0; i < roleKeyEventEdits.length; i++) {
			if (roleKeyEventEdits[i].equalsIgnoreCase(SwaDepartment.DEPT_ROLEKYE_ADMIN)) {
				eventEditPolicy.setSystemAdministratorChecked(true);
			} else if (roleKeyEventEdits[i].equalsIgnoreCase(SwaDepartment.DEPT_ROLEKYE_CUSTOM)) {
				eventEditPolicy.setCustomChecked(true);
				String usersStr = eventEdit.getCustomUser();
				String[] users = StringUtils.tokenizeToStringArray(usersStr, ";");
				//SwoUserExtend[] userExtendsArray = SwManagerFactory.getInstance().getSwoManager().getUsersExtend(cUser.getId(), users);
				//UserInfo[] userInfoArray = new UserInfo[userExtendsArray.length];
				//for (int j = 0; j < userExtendsArray.length; j++) {
				//	userInfoArray[j] = ModelConverter.getUserInfoBySwoUserExtend(null, userExtendsArray[j]);
				//}
				eventEditPolicy.setCustoms(getWorkSpaceInfoByIds(users));
			} else if (roleKeyEventEdits[i].equalsIgnoreCase(SwaDepartment.DEPT_ROLEKYE_LEADER)) {
				eventEditPolicy.setLeaderChecked(true);
			} else if (roleKeyEventEdits[i].equalsIgnoreCase(SwaDepartment.DEPT_ROLEKYE_MEMBER)) {
				eventEditPolicy.setMembersChecked(true);
			} else if (roleKeyEventEdits[i].equalsIgnoreCase(SwaDepartment.DEPT_ROLEKYE_OWNER)) {
				eventEditPolicy.setOwnerChecked(true);
			}
		}
		
		department.setBoardWritePolicy(boardWritePolicy);
		department.setBoardEditPolicy(boardEditPolicy);
		department.setEventWritePolicy(eventWritePolicy);
		department.setEventEditPolicy(eventEditPolicy);
		
		return department;
	}
	
	public static String getFullpathNameByDepartmentId(String departmentId) throws Exception {
		
		User user = SmartUtil.getSystemUser();
		SwoDepartmentExtend department = getSwoManager().getDepartmentExtend(user.getId(), departmentId, true);
		String fullpathName = getParentsfullpathNameByDepartmentId(department,"");
		return fullpathName;
	}
	
	public static String getParentsfullpathNameByDepartmentId(SwoDepartmentExtend department, String fullpathName) throws Exception {
		
		if(department.getParentId()!= null){
			fullpathName = department.getName() + (SmartUtil.isBlankObject(fullpathName) ? "" : "▶") + fullpathName;
			SwoDepartmentExtend departmentId = getSwoManager().getDepartmentExtend(null, department.getParentId(), true);								
			fullpathName = getParentsfullpathNameByDepartmentId(departmentId, fullpathName);		
		}
		return fullpathName;
	}

	public static GroupInfo getGroupInfoByGroupId(String groupId) throws Exception {
		if (CommonUtil.isEmpty(groupId))
			return null;
		User cUser = SmartUtil.getCurrentUser();
		SwoGroup swoGroup = null;
		try {
			swoGroup = getSwoManager().getGroup(cUser.getId(), groupId, IManager.LEVEL_ALL);
		} catch (Exception e) {
			swoGroup = null;
		}
		if(swoGroup == null)
			return null;
		return getGroupInfoBySwoGroup(null, swoGroup);
	}

	public static GroupInfo getGroupInfoBySwoGroup(GroupInfo groupInfo, SwoGroup swoGroup) throws Exception {
		if (swoGroup == null)
			return null;
		if (groupInfo == null) 
			groupInfo = new GroupInfo();

		groupInfo.setId(swoGroup.getId());
		groupInfo.setName(swoGroup.getName());
		groupInfo.setDesc(swoGroup.getDescription());
		groupInfo.setLeader(getUserInfoByUserId(swoGroup.getGroupLeader()));
		groupInfo.setOwner(getUserInfoByUserId(swoGroup.getCreationUser()));
		groupInfo.setCreatedDate(new LocalDate(swoGroup.getCreationDate().getTime()));
		SwoGroupMember[] groupMembers = swoGroup.getSwoGroupMembers();
		int numberOfGroupMembers = 0;
		if(!SmartUtil.isBlankObject(groupMembers)) numberOfGroupMembers = groupMembers.length;
		groupInfo.setNumberOfGroupMember(numberOfGroupMembers);
		String picture = CommonUtil.toNotNull(swoGroup.getPicture());
		if(!picture.equals("")) {
			String extension = picture.lastIndexOf(".") > 0 ? picture.substring(picture.lastIndexOf(".") + 1) : null;
			String pictureId = picture.substring(0, (picture.length() - extension.length())-1);
			groupInfo.setSmallPictureName(pictureId + Community.IMAGE_TYPE_THUMB + "." + extension);
		} else {
			groupInfo.setSmallPictureName(picture);
		}
		if (swoGroup.getGroupType() != null && swoGroup.getGroupType().equalsIgnoreCase("C")) {
			groupInfo.setPublic(false);
		} else {
			groupInfo.setPublic(true);
		}
		
		groupInfo.setSpaceType(ISmartWorks.SPACE_TYPE_GROUP);
		
		return groupInfo;
	}

	public static Group getGroupByGroupId(String groupId) throws Exception {
		if (CommonUtil.isEmpty(groupId))
			return null;
		User cUser = SmartUtil.getCurrentUser();
		SwoGroupCond swoGroupCond = new SwoGroupCond();
		swoGroupCond.setOrders(new Order[]{new Order("groupLeader", false)});
		SwoGroup swoGroup = getSwoManager().getGroup(cUser.getId(), groupId, IManager.LEVEL_ALL);

		return getGroupBySwoGroup(null, swoGroup);
	}

	public static Group getGroupBySwoGroup(Group group, SwoGroup swoGroup) throws Exception {
		try {
			User user = SmartUtil.getCurrentUser();
			String userId = user.getId();
			
			if (swoGroup == null)
				return null;
			if (group == null)
				group = new Group();
	
			group.setId(swoGroup.getId());
			group.setName(swoGroup.getName());
			group.setDesc(swoGroup.getDescription());
			group.setPublic(swoGroup.getGroupType().equals("O") ? true : false);
			//group.setContinue(swoGroup.getStatus().equals("C") ? true : false);
			User leader = getUserByUserId(swoGroup.getGroupLeader());
			if(leader != null)
				group.setLeader(leader);
	
			User owner = getUserByUserId(swoGroup.getCreationUser());
			if(owner != null)
				group.setOwner(owner);

			LocalDate openDate = new LocalDate(swoGroup.getCreationDate().getTime());
			group.setOpenDate(openDate);

			List<UserInfo> groupMemberList = new ArrayList<UserInfo>();
			List<UserInfo> groupJoinRequestMemberList = new ArrayList<UserInfo>();

			SwoGroupMember[] swoGroupMembers = swoGroup.getSwoGroupMembers();
			if(!CommonUtil.isEmpty(swoGroupMembers)) {
				groupMemberList.add(getUserInfoByUserId(swoGroup.getGroupLeader()));
				for(SwoGroupMember swoGroupMember : swoGroupMembers) {
					if(!swoGroupMember.getUserId().equals(swoGroup.getGroupLeader())) {
						UserInfo groupMember = getUserInfoByUserId(swoGroupMember.getUserId());
						if (swoGroupMember.getJoinType().equalsIgnoreCase(SwoGroupMember.JOINTYPE_REQUEST) && swoGroupMember.getJoinStatus().equalsIgnoreCase(SwoGroupMember.JOINSTATUS_READY)) {
							groupJoinRequestMemberList.add(groupMember);
						}
						groupMemberList.add(groupMember);
					}
				}
				UserInfo[] groupMembers = new UserInfo[groupMemberList.size()];
				groupMemberList.toArray(groupMembers);
				group.setMembers(groupMembers);
				group.setNumberOfGroupMember(groupMembers.length);
			}

			String picture = CommonUtil.toNotNull(swoGroup.getPicture());
			if(!picture.equals("")) {
				String extension = picture.lastIndexOf(".") > 0 ? picture.substring(picture.lastIndexOf(".") + 1) : null;
				String pictureId = picture.substring(0, (picture.length() - extension.length())-1);
				group.setBigPictureName(pictureId + Community.IMAGE_TYPE_THUMB + "." + extension);
				group.setSmallPictureName(pictureId + Community.IMAGE_TYPE_THUMB + "." + extension);
			} else {
				group.setBigPictureName(picture);
				group.setSmallPictureName(picture);
			}
			
			
			SwaGroupCond groupAuthCond = new SwaGroupCond();
			groupAuthCond.setGroupId(swoGroup.getId());
			SwaGroup[] groupDepts = SwManagerFactory.getInstance().getSwaManager().getAuthGroups(userId, groupAuthCond, null);

			SwaGroup boardWrite = null;
			SwaGroup boardEdit = null;
			SwaGroup eventWrite = null;
			SwaGroup eventEdit = null;
			SwaGroup inviteMemter = null;
			
			if (groupDepts == null || groupDepts.length == 0) {

				boardWrite = new SwaGroup();
				boardWrite.setGroupAuthType(SwaGroup.GROUP_AUTHTYPE_BOARD_WRITE);
				boardWrite.setGroupId(swoGroup.getId());
				boardWrite.setRoleKey(SwaGroup.GROUP_ROLEKYE_LEADER);

				boardEdit = new SwaGroup();
				boardEdit.setGroupAuthType(SwaGroup.GROUP_AUTHTYPE_BOARD_EDIT);
				boardEdit.setGroupId(swoGroup.getId());
				boardEdit.setRoleKey(SwaGroup.GROUP_ROLEKYE_OWNER + ";" + SwaGroup.GROUP_ROLEKYE_LEADER);

				eventWrite = new SwaGroup();
				eventWrite.setGroupAuthType(SwaGroup.GROUP_AUTHTYPE_EVENT_WRITE);
				eventWrite.setGroupId(swoGroup.getId());
				eventWrite.setRoleKey(SwaGroup.GROUP_ROLEKYE_LEADER);
				
				eventEdit = new SwaGroup();
				eventEdit.setGroupAuthType(SwaGroup.GROUP_AUTHTYPE_EVENT_EDIT);
				eventEdit.setGroupId(swoGroup.getId());
				eventEdit.setRoleKey(SwaGroup.GROUP_ROLEKYE_OWNER + ";" + SwaGroup.GROUP_ROLEKYE_LEADER);

				inviteMemter = new SwaGroup();
				inviteMemter.setGroupAuthType(SwaGroup.GROUP_AUTHTYPE_MEMBER_INVITE);
				inviteMemter.setGroupId(swoGroup.getId());
				inviteMemter.setRoleKey(SwaGroup.GROUP_ROLEKYE_LEADER);
				
				SwManagerFactory.getInstance().getSwaManager().setAuthGroup(userId, boardWrite, null);
				SwManagerFactory.getInstance().getSwaManager().setAuthGroup(userId, boardEdit, null);
				SwManagerFactory.getInstance().getSwaManager().setAuthGroup(userId, eventWrite, null);
				SwManagerFactory.getInstance().getSwaManager().setAuthGroup(userId, eventEdit, null);
				SwManagerFactory.getInstance().getSwaManager().setAuthGroup(userId, inviteMemter, null);
				
			} else {
				for (int i = 0; i < groupDepts.length; i++) {
					SwaGroup groupAuth = groupDepts[i];
					String deptAuthType = groupAuth.getGroupAuthType();
					if (deptAuthType.equalsIgnoreCase(SwaGroup.GROUP_AUTHTYPE_BOARD_WRITE)) {
						boardWrite = groupAuth;
					} else if (deptAuthType.equalsIgnoreCase(SwaGroup.GROUP_AUTHTYPE_BOARD_EDIT)) {
						boardEdit = groupAuth;
					} else if (deptAuthType.equalsIgnoreCase(SwaGroup.GROUP_AUTHTYPE_EVENT_WRITE)) {
						eventWrite = groupAuth;
					} else if (deptAuthType.equalsIgnoreCase(SwaGroup.GROUP_AUTHTYPE_EVENT_EDIT)) {
						eventEdit = groupAuth;
					} else if (deptAuthType.equalsIgnoreCase(SwaGroup.GROUP_AUTHTYPE_MEMBER_INVITE)) {
						inviteMemter = groupAuth;
					}
				}
			}
			
			SpacePolicy boardWritePolicy = new SpacePolicy();
			String roleKeysStr = boardWrite.getRoleKey();
			String[] roleKeys = StringUtils.tokenizeToStringArray(roleKeysStr, ";");
			for (int i = 0; i < roleKeys.length; i++) {
				if (roleKeys[i].equalsIgnoreCase(SwaGroup.GROUP_ROLEKYE_CUSTOM)) {
					boardWritePolicy.setCustomChecked(true);
					String usersStr = boardWrite.getCustomUser();
					
					String[] users = StringUtils.tokenizeToStringArray(usersStr, ";");
					//String[] users = convertUserIdsByUserAndDeptAndGroupIdArray(tempUsers);
					
					boardWritePolicy.setCustoms(getWorkSpaceInfoByIds(users));
					
				} else if (roleKeys[i].equalsIgnoreCase(SwaGroup.GROUP_ROLEKYE_LEADER)) {
					boardWritePolicy.setLeaderChecked(true);
				} else if (roleKeys[i].equalsIgnoreCase(SwaGroup.GROUP_ROLEKYE_MEMBER)) {
					boardWritePolicy.setMembersChecked(true);
				} else if (roleKeys[i].equalsIgnoreCase(SwaGroup.GROUP_ROLEKYE_OWNER)) {
					boardWritePolicy.setOwnerChecked(true);
				}
			}
			
			SpacePolicy boardEditPolicy = new SpacePolicy();
			String roleKeysBoardEditStr = boardEdit.getRoleKey();
			String[] roleKeyBoardEdits = StringUtils.tokenizeToStringArray(roleKeysBoardEditStr, ";");
			for (int i = 0; i < roleKeyBoardEdits.length; i++) {
				if (roleKeyBoardEdits[i].equalsIgnoreCase(SwaGroup.GROUP_ROLEKYE_ADMIN)) {
					boardEditPolicy.setSystemAdministratorChecked(true);
				} else if (roleKeyBoardEdits[i].equalsIgnoreCase(SwaGroup.GROUP_ROLEKYE_CUSTOM)) {
					boardEditPolicy.setCustomChecked(true);
					String usersStr = boardEdit.getCustomUser();

					String[] users = StringUtils.tokenizeToStringArray(usersStr, ";");
					//String[] users = convertUserIdsByUserAndDeptAndGroupIdArray(tempUsers);
					
					//SwoUserExtend[] userExtendsArray = SwManagerFactory.getInstance().getSwoManager().getUsersExtend(userId, users);
					//UserInfo[] userInfoArray = new UserInfo[userExtendsArray.length];
					//for (int j = 0; j < userExtendsArray.length; j++) {
					//	userInfoArray[j] = ModelConverter.getUserInfoBySwoUserExtend(null, userExtendsArray[j]);
					//}
					boardEditPolicy.setCustoms(getWorkSpaceInfoByIds(users));
					
				} else if (roleKeyBoardEdits[i].equalsIgnoreCase(SwaGroup.GROUP_ROLEKYE_LEADER)) {
					boardEditPolicy.setLeaderChecked(true);
				} else if (roleKeyBoardEdits[i].equalsIgnoreCase(SwaGroup.GROUP_ROLEKYE_MEMBER)) {
					boardEditPolicy.setMembersChecked(true);
				} else if (roleKeyBoardEdits[i].equalsIgnoreCase(SwaGroup.GROUP_ROLEKYE_OWNER)) {
					boardEditPolicy.setOwnerChecked(true);
				}
			}
			
			SpacePolicy eventWritePolicy = new SpacePolicy();
			String roleKeysEventWriteStr = eventWrite.getRoleKey();
			String[] roleKeyEventWrites = StringUtils.tokenizeToStringArray(roleKeysEventWriteStr, ";");
			for (int i = 0; i < roleKeyEventWrites.length; i++) {
				if (roleKeyEventWrites[i].equalsIgnoreCase(SwaGroup.GROUP_ROLEKYE_CUSTOM)) {
					eventWritePolicy.setCustomChecked(true);
					String usersStr = eventWrite.getCustomUser();
					
					String[] users = StringUtils.tokenizeToStringArray(usersStr, ";");
					//String[] users = convertUserIdsByUserAndDeptAndGroupIdArray(tempUsers);
					
					//SwoUserExtend[] userExtendsArray = SwManagerFactory.getInstance().getSwoManager().getUsersExtend(userId, users);
					//UserInfo[] userInfoArray = new UserInfo[userExtendsArray.length];
					//for (int j = 0; j < userExtendsArray.length; j++) {
					//	userInfoArray[j] = ModelConverter.getUserInfoBySwoUserExtend(null, userExtendsArray[j]);
					//}
					eventWritePolicy.setCustoms(getWorkSpaceInfoByIds(users));
					
				} else if (roleKeyEventWrites[i].equalsIgnoreCase(SwaGroup.GROUP_ROLEKYE_LEADER)) {
					eventWritePolicy.setLeaderChecked(true);
				} else if (roleKeyEventWrites[i].equalsIgnoreCase(SwaGroup.GROUP_ROLEKYE_MEMBER)) {
					eventWritePolicy.setMembersChecked(true);
				} else if (roleKeyEventWrites[i].equalsIgnoreCase(SwaGroup.GROUP_ROLEKYE_OWNER)) {
					eventWritePolicy.setOwnerChecked(true);
				}
			}
			
			SpacePolicy eventEditPolicy = new SpacePolicy();
			String roleKeysEventEditStr = eventEdit.getRoleKey();
			String[] roleKeyEventEdits = StringUtils.tokenizeToStringArray(roleKeysEventEditStr, ";");
			for (int i = 0; i < roleKeyEventEdits.length; i++) {
				if (roleKeyEventEdits[i].equalsIgnoreCase(SwaGroup.GROUP_ROLEKYE_ADMIN)) {
					eventEditPolicy.setSystemAdministratorChecked(true);
				} else if (roleKeyEventEdits[i].equalsIgnoreCase(SwaGroup.GROUP_ROLEKYE_CUSTOM)) {
					eventEditPolicy.setCustomChecked(true);
					String usersStr = eventEdit.getCustomUser();

					String[] users = StringUtils.tokenizeToStringArray(usersStr, ";");
					//String[] users = convertUserIdsByUserAndDeptAndGroupIdArray(tempUsers);
					
					//SwoUserExtend[] userExtendsArray = SwManagerFactory.getInstance().getSwoManager().getUsersExtend(userId, users);
					//UserInfo[] userInfoArray = new UserInfo[userExtendsArray.length];
					//for (int j = 0; j < userExtendsArray.length; j++) {
					//	userInfoArray[j] = ModelConverter.getUserInfoBySwoUserExtend(null, userExtendsArray[j]);
					//}
					eventEditPolicy.setCustoms(getWorkSpaceInfoByIds(users));
				} else if (roleKeyEventEdits[i].equalsIgnoreCase(SwaGroup.GROUP_ROLEKYE_LEADER)) {
					eventEditPolicy.setLeaderChecked(true);
				} else if (roleKeyEventEdits[i].equalsIgnoreCase(SwaGroup.GROUP_ROLEKYE_MEMBER)) {
					eventEditPolicy.setMembersChecked(true);
				} else if (roleKeyEventEdits[i].equalsIgnoreCase(SwaGroup.GROUP_ROLEKYE_OWNER)) {
					eventEditPolicy.setOwnerChecked(true);
				}
			}
			
			SpacePolicy inviteMemberPolicy = new SpacePolicy();
			String roleKeysInviteMemberStr = inviteMemter.getRoleKey();
			String[] roleKeyInviteMembers = StringUtils.tokenizeToStringArray(roleKeysInviteMemberStr, ";");
			for (int i = 0; i < roleKeyInviteMembers.length; i++) {
				if (roleKeyInviteMembers[i].equalsIgnoreCase(SwaGroup.GROUP_ROLEKYE_CUSTOM)) {
					inviteMemberPolicy.setCustomChecked(true);
					String usersStr = eventEdit.getCustomUser();

					String[] users = StringUtils.tokenizeToStringArray(usersStr, ";");
					//String[] users = convertUserIdsByUserAndDeptAndGroupIdArray(tempUsers);
					
					//SwoUserExtend[] userExtendsArray = SwManagerFactory.getInstance().getSwoManager().getUsersExtend(userId, users);
					//UserInfo[] userInfoArray = new UserInfo[userExtendsArray.length];
					//for (int j = 0; j < userExtendsArray.length; j++) {
					//	userInfoArray[j] = ModelConverter.getUserInfoBySwoUserExtend(null, userExtendsArray[j]);
					//}
					
					inviteMemberPolicy.setCustoms(getWorkSpaceInfoByIds(users));
				} else if (roleKeyInviteMembers[i].equalsIgnoreCase(SwaGroup.GROUP_ROLEKYE_LEADER)) {
					inviteMemberPolicy.setLeaderChecked(true);
				} else if (roleKeyInviteMembers[i].equalsIgnoreCase(SwaGroup.GROUP_ROLEKYE_MEMBER)) {
					inviteMemberPolicy.setMembersChecked(true);
				}
			}
			
			group.setBoardWritePolicy(boardWritePolicy);
			group.setBoardEditPolicy(boardEditPolicy);
			group.setEventWritePolicy(eventWritePolicy);
			group.setEventEditPolicy(eventEditPolicy);
			group.setInvitableMembers(inviteMemberPolicy);
						
			int maxMembers = swoGroup.getMaxMember();
			group.setMaxMembers(maxMembers);
			
			boolean autoApproval = swoGroup.isAutoApproval();
			group.setAutoApproval(autoApproval);
			
			UserInfo[] joinRequestMembers = new UserInfo[groupJoinRequestMemberList.size()];
			groupJoinRequestMemberList.toArray(joinRequestMembers);
			group.setJoinRequesters(joinRequestMembers);
			
			return group;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	private static WorkSpaceInfo[] getWorkSpaceInfoByIds(String[] ids) throws Exception {
		
		List userList = new ArrayList();
		List deptList = new ArrayList();
		List groupList = new ArrayList();
		List workSpaceList = new ArrayList();
		
		for (int j = 0; j < ids.length; j++) {
			String id = ids[j];
			if (id.indexOf("dept_") != -1) {
				deptList.add(id);
			} else if (id.indexOf("group_") != -1) {
				groupList.add(id);
			} else {
				userList.add(id);
			}
		}
		
		if (userList.size() != 0) {
			String[] users = new String[userList.size()];
			userList.toArray(users);
			SwoUserExtend[] userExtendsArray = SwManagerFactory.getInstance().getSwoManager().getUsersExtend("", users);
			UserInfo[] userInfoArray = new UserInfo[userExtendsArray.length];
			for (int j = 0; j < userExtendsArray.length; j++) {
				workSpaceList.add(ModelConverter.getUserInfoBySwoUserExtend(null, userExtendsArray[j]));
			}
		}
		if (deptList.size() != 0) {
			for (int j = 0; j < deptList.size(); j++) {
				workSpaceList.add(getDepartmentInfoByDepartmentId((String)deptList.get(j)));
			}
		}
		if (groupList.size() != 0) {
			for (int j = 0; j < groupList.size(); j++) {
				workSpaceList.add(getGroupInfoByGroupId((String)groupList.get(j)));
			}
		}
		
		WorkSpaceInfo[] workSpaceInfo = new WorkSpaceInfo[workSpaceList.size()];
		workSpaceList.toArray(workSpaceInfo);
		
		return workSpaceInfo;
	}
	
	
	public static Work getWorkByCtgCategory(Work work, CtgCategory ctg) throws Exception {
		if (ctg == null)
			return null;
		if (work == null)
			work = new Work();
			
		String ctgId = ctg.getObjId();
		String ctgName = ctg.getName();
		String ctgDesc = ctg.getDescription();
		work.setId(ctgId);
		work.setName(ctgName);
		work.setDesc(ctgDesc);
		//Start - Modified by ysjung at 2013.3.2 
		work.setType(WorkCategory.TYPE_CATEGORY);
		if(work.getClass().equals(WorkCategory.class)){
			((WorkCategory)work).setRunning(isExistRunningPackageByCategoryId(ctgId));
		}
		//End - Modified by ysjung at 2013.3.2 
		
		return work;
	}
	public static WorkCategory getWorkCategoryByCtgCategory(WorkCategory workCategory, CtgCategory ctg) throws Exception {
		if (ctg == null)
			return null;
		if (workCategory == null)
			workCategory = new WorkCategory();
		
		getWorkByCtgCategory(workCategory, ctg);
		
		return workCategory;
	}
	
	
	public static Work getWorkByPkgPackage(Work work, PkgPackage pkg) throws Exception {
		if (pkg == null)
			return null;
		if (work == null)
			work = new Work();
			
		String packageId = pkg.getPackageId();
		String packageName = pkg.getName();
		String packageDesc = pkg.getDescription();
		//TODO
		int ctgType = -1;
		work.setId(packageId);
		work.setName(packageName);
		work.setDesc(packageDesc);
		work.setType(-1);
		
		return work;
	}
	public static SmartWork getSmartWorkByPkgPackage(String userId, SmartWork smartWork, PkgPackage pkg) throws Exception {
		if (pkg == null)
			return null;
		if (smartWork == null)
			smartWork = new SmartWork();
		
		getWorkByPkgPackage(smartWork, pkg);
		
		setPolicyToWork(smartWork, getResourceIdByPkgPackage(pkg));

		smartWork.setCreater(getUserByUserId(pkg.getCreationUser()));
		smartWork.setCreatedDate(new LocalDate(pkg.getCreationDate().getTime()));
		smartWork.setLastModifier(getUserByUserId(pkg.getModificationUser()));
		smartWork.setLastModifiedDate(new LocalDate(pkg.getModificationDate().getTime()));
		
		smartWork.setSearchFilters(getSearchFilterInfoByPkgPackage(userId, pkg));
		
		Map<String, WorkCategory> pkgCtgPathMap = getPkgCtgMapByPackage(pkg);
		smartWork.setMyCategory(pkgCtgPathMap.get("category"));
		smartWork.setMyGroup(pkgCtgPathMap.get("group"));
		
		String packageStatus = pkg.getStatus();
		boolean isRunningPackage = false;
		boolean isEditingPackage = false;
		User editingUser = null;
		LocalDate editingStartDate = null;
		if (packageStatus.equalsIgnoreCase("DEPLOYED")) {
			isRunningPackage = true;
			isEditingPackage = false;
		} else if (packageStatus.equalsIgnoreCase("CHECKED-OUT") ) {
			isRunningPackage = false;
			isEditingPackage = true;
			editingUser = getUserByUserId(pkg.getModificationUser());
			editingStartDate = new LocalDate(pkg.getModificationDate().getTime());
		} else if (packageStatus.equalsIgnoreCase("CHECKED-IN")) {
			isRunningPackage = false;
			isEditingPackage = false;
		}
		smartWork.setRunning(isRunningPackage);
		smartWork.setEditing(isEditingPackage);
		smartWork.setEditingUser(editingUser);
		smartWork.setEditingStartDate(editingStartDate);
		
		return smartWork;
	}
	public static ProcessWork getProcessWorkByPkgPackageId(String userId,String packageId) throws Exception {
		return getProcessWorkByPkgPackage(userId, null, getPkgPackageByPackageId(packageId));
	}
	public static ProcessWork getProcessWorkByPkgPackage(String userId, ProcessWork processWork, PkgPackage pkg) throws Exception {
		if (pkg == null)
			return null;
		if (processWork == null)
			processWork = new ProcessWork();
		
		getSmartWorkByPkgPackage(userId, processWork, pkg);
		if(pkg.getType().equals("PROCESS")){
			processWork.setType(SmartWork.TYPE_PROCESS);
		}else if(pkg.getType().equals("GANTT")){
			processWork.setType(SmartWork.TYPE_SCHEDULE);			
		}
		processWork.setHelpUrl(pkg.getHelpUrl());
		processWork.setManualFileId(pkg.getManualFileName());// Manual File Group Id
		processWork.setDesc(pkg.getDescription());
		
		List<IFileModel> file = SwManagerFactory.getInstance().getDocManager().findFileGroup(pkg.getManualFileName());
		if (file != null && file.size() != 0) {
			processWork.setManualFileName(file.get(0).getFileName());
			processWork.setManualFilePath(file.get(0).getFilePath());
		}
		//processWork.setManualFileName(pkg.getManualFileName());
		//processWork.setManualFilePath(SmartConfUtil.getInstance().getImageServer() + SmartUtil.getCurrentUser().getCompanyId() + "/workDef/" + pkg.getPackageId() + "/" + pkg.getManualFileName());

		processWork.setDiagram(getSmartDiagramByPkgInfo(userId, pkg));

		OpinionCond opinionCond = new OpinionCond();
		opinionCond.setRefId(pkg.getPackageId());
		opinionCond.setRefType(6);
		long commentCount = getOpinionManager().getOpinionSize(userId, opinionCond);
		processWork.setCommentCount((int)commentCount);

		//상세필터
		processWork.setSearchFilters(ModelConverter.getSearchFilterInfoByPkgPackage(userId, pkg));

		return processWork;
	}
	private static SmartDiagram getSmartDiagramByPkgInfo(String userId, PkgPackage pkg) throws Exception {
		
		SmartDiagram smartDiagram = new SmartDiagram();
		smartDiagram.setId(pkg.getPackageId());
		smartDiagram.setName(pkg.getName());
		
		PrcSwProcessCond cond = new PrcSwProcessCond();
		cond.setPackageId(pkg.getPackageId());
		PrcSwProcess[] prc = SwManagerFactory.getInstance().getPrcManager().getSwProcesses(userId, cond);

		if (prc != null && prc.length != 0) {
			smartDiagram.setDescription(prc[0].getDescription());
			smartDiagram.setMinImageName(SmartConfUtil.getInstance().getImageServer() + SmartUtil.getCurrentUser().getCompanyId() + "/workDef/" + pkg.getPackageId() + "/" + prc[0].getProcessId() + "_tn.png");
			smartDiagram.setOrgImageName(SmartConfUtil.getInstance().getImageServer() + SmartUtil.getCurrentUser().getCompanyId() + "/workDef/" + pkg.getPackageId() + "/" + prc[0].getProcessId() + ".png");
		}
		smartDiagram.setTasks(getSmartTaskInfosByPkgId(userId, pkg.getPackageId()));
		
		return smartDiagram;
	}
	public static SmartTaskInfo[] getSmartTaskInfosByPkgId(String userId, String packageId) throws Exception {
		
		PrcSwProcessCond swPrcCond = new PrcSwProcessCond();
		swPrcCond.setPackageId(packageId);
		PrcSwProcess[] swPrcs = getPrcManager().getSwProcesses(userId, swPrcCond);
		
		if (swPrcs == null)
			return null;
		if (swPrcs.length != 1)
			throw new Exception("More Then 1 SwProcess Package!");
		
		//XPDL Parsing
		Map activityPerformerMap = new HashMap();
		
		String processXpdl = swPrcs[0].getContent();
		PackageType pt = ProcessModelHelper.load(processXpdl);
		WorkflowProcesses prcs = pt.getWorkflowProcesses();
		List prcList = prcs.getWorkflowProcess();
		
		Map taskFormNameMap = new HashMap();
		
		for (Iterator prcItr = prcList.iterator(); prcItr.hasNext();) {
			ProcessType1 prc = (ProcessType1) prcItr.next();
			Activities acts = prc.getActivities();
			List actList = new ArrayList();
			if(acts != null)
				actList = acts.getActivity();
			if(actList.size() > 0) {
				for (Iterator actIter = actList.iterator(); actIter.hasNext();) {
					Activity act = (Activity) actIter.next();
					String actId = act.getId();
					
					Sequence attrs = act.getAnyAttribute();
					if (attrs != null && attrs.size() > 0) {
						for (int i=0; i<attrs.size(); i++) {
							commonj.sdo.Property attr = attrs.getProperty(i);
							String attrName = attr.getName();
							Object attrValue = attrs.getValue(i);
							if (CommonUtil.isEmpty(attrName) || attrValue == null)
								continue;
							if (attrName.equals("PerformerName")) {
								activityPerformerMap.put(actId, attrValue);
							} 
						}
					}
//					Performers performers = act.getPerformers();
//	
//					List performerList = null;
//					if (performers != null)
//						performerList = performers.getPerformer();
//					if (performerList != null && !performerList.isEmpty()) {
//						String peformer = ((Performer)performerList.get(0)).getValue();
//						activityPerformerMap.put(actId, peformer);
//					}
					
					//폼네임을 구한다
					Implementation7 impl = act.getImplementation();
					if (impl == null)
						continue;
					Task task = impl.getTask();
					if (task == null)
						continue;
					TaskApplication taskApp = task.getTaskApplication();
					if (taskApp == null)
						continue;
					Sequence taskAppAttrs = taskApp.getAnyAttribute();
					String formId = taskApp.getName();
					String formName = null;
					if (taskAppAttrs != null && taskAppAttrs.size() > 0) {
						for (int i=0; i<taskAppAttrs.size(); i++) {
							commonj.sdo.Property taskAppAttr = taskAppAttrs.getProperty(i);
							String attrName = taskAppAttr.getName();
							Object attrValue = taskAppAttrs.getValue(i);
							if (CommonUtil.isEmpty(attrName) || attrValue == null)
								continue;
							if (attrName.equals("FormName")) {
								formName = (String)attrValue;
							} 
						}
						if (!CommonUtil.isEmpty(formId) && !CommonUtil.isEmpty(formName))
							taskFormNameMap.put(formId, formName);
					}
				}
			}
		}
		//Parsing End
		
		String processId = swPrcs[0].getProcessId();
		TskTaskDefCond tskDefCond = new TskTaskDefCond();
		tskDefCond.setType(TskTask.TASKTYPE_COMMON);
		tskDefCond.setExtendedProperties(new Property[]{new Property("processId", processId)});
		
		TskTaskDef[] tskDefs = getTskManager().getTaskDefs(userId, tskDefCond, IManager.LEVEL_ALL);
		List smartTaskInfoList = new ArrayList();
		Map<Integer, SmartTaskInfo> sortingTaskMap = new HashMap<Integer, SmartTaskInfo>();
		if (tskDefs != null) {
			for (TskTaskDef taskDef: tskDefs) {
				String actId = taskDef.getExtendedPropertyValue("activityId");
				String isStartActivityStr = taskDef.getExtendedPropertyValue("startActivity");
				boolean isStartActivity = CommonUtil.toBoolean(isStartActivityStr);
				String approvalLineId = taskDef.getExtendedPropertyValue("ApprovalLine");
				SmartTaskInfo smartTaskInfo = new SmartTaskInfo();
				
				if (taskDef != null && taskDef.getAssignee() != null && taskDef.getAssignee().indexOf("@") != -1) {
					UserInfo performerInfo = getUserInfoByUserId(taskDef.getAssignee());
					smartTaskInfo.setAssignedUser(performerInfo);
				} else {
					smartTaskInfo.setAssigningName((String)activityPerformerMap.get(actId));
				}
				
				smartTaskInfo.setId(actId);
				smartTaskInfo.setName(taskDef.getName());
				smartTaskInfo.setStartTask(isStartActivity);
				if(approvalLineId!=null){
					smartTaskInfo.setApprovalLineId(approvalLineId);					
				}
				
				SmartFormInfo formInfo = new SmartFormInfo();
				formInfo.setDescription(taskDef.getDescription());
				formInfo.setId(taskDef.getForm());
				formInfo.setName(CommonUtil.toDefault((String)taskFormNameMap.get(taskDef.getForm()), taskDef.getName()));
				
				String minImageName = SmartConfUtil.getInstance().getImageServer() + SmartUtil.getCurrentUser().getCompanyId() + "/workDef/" + swPrcs[0].getPackageId() + "/" + taskDef.getForm() + "_tn.png";
				String orgImageName = SmartConfUtil.getInstance().getImageServer() + SmartUtil.getCurrentUser().getCompanyId() + "/workDef/" + swPrcs[0].getPackageId() + "/" + taskDef.getForm() + ".png";
				
				formInfo.setMinImageName(minImageName);
				formInfo.setOrgImageName(orgImageName);
				smartTaskInfo.setForm(formInfo);
				
//				smartTaskInfoList.add(smartTaskInfo);
				sortingTaskMap.put(Integer.parseInt(actId), smartTaskInfo);
			}
		}
//		SmartTaskInfo[] smartTaskInfoArray = new SmartTaskInfo[smartTaskInfoList.size()];
//		smartTaskInfoList.toArray(smartTaskInfoArray);
		if (sortingTaskMap == null || sortingTaskMap.size() == 0)
			return null;
		SmartTaskInfo[] smartTaskInfoArray = new SmartTaskInfo[sortingTaskMap.size()];
		TreeMap<Integer, SmartTaskInfo> sortingMap = new TreeMap<Integer, SmartTaskInfo>(sortingTaskMap);
		Iterator itr = sortingMap.keySet().iterator();
		int i = 0;
		while (itr.hasNext()) {
			int key = (Integer)itr.next();
			smartTaskInfoArray[i] = (SmartTaskInfo)sortingMap.get(key);
			i++;
		}
		return smartTaskInfoArray;
	}
	public static SearchFilterInfo[] getSearchFilterInfoByPkgPackage(String userId, PkgPackage pkg) throws Exception {
		if (pkg == null)
			return null;
		
		ColListCond listCond = new ColListCond();
		String pkgType = pkg.getType();
		if (pkgType.equalsIgnoreCase("PROCESS") || pkgType.equalsIgnoreCase("GANTT")) {
			//processinst.cond.admin@maninsoft.co.kr
			listCond.setType("processinst.cond." + userId);
		} else {
			//record.cond.admin@maninsoft.co.kr
			listCond.setType("record.cond." + userId);
		}
		String resourceId = getResourceIdByPkgPackage(pkg);
		if (CommonUtil.isEmpty(resourceId))
			return null;
		listCond.setCorrelation(resourceId);

		ColList filterList = getColManager().getList(userId, listCond, IManager.LEVEL_ALL);

		return getSearchFilterInfoArrayByColList(pkgType, filterList);
	}

	public static SearchFilterInfo[] getSearchFilterInfoArrayByColList(String type, ColList list) throws Exception {
		if (list == null)
			return null;

		ColObject[] filterItemArray = list.getItems();

		if (CommonUtil.isEmpty(filterItemArray))
			return null;

		List<SearchFilterInfo> filterInfoList = new ArrayList<SearchFilterInfo>();
		for (int i = 0; i < filterItemArray.length; i++) {

			ColObject filterItem = filterItemArray[i];

			String id = filterItem.getRef();
			String name = filterItem.getLabel();

			SearchFilterInfo searchFilterInfo = new SearchFilterInfo();
			searchFilterInfo.setId(id);
			searchFilterInfo.setName(name);

			filterInfoList.add(searchFilterInfo);
		}
		SearchFilterInfo[] searchFilterInfos = new SearchFilterInfo[filterInfoList.size()];
		filterInfoList.toArray(searchFilterInfos);
		
		return searchFilterInfos;
	}

	public static SearchFilter[] getSearchFilterArrayByPkgPackage(String userId, PkgPackage pkg) throws Exception {
		if (pkg == null)
			return null;
		
		ColListCond listCond = new ColListCond();
		String pkgType = pkg.getType();
		if (pkgType.equalsIgnoreCase("PROCESS") || pkgType.equalsIgnoreCase("GANTT")) {
			//processinst.cond.admin@maninsoft.co.kr
			listCond.setType("processinst.cond." + userId);
		} else {
			//record.cond.admin@maninsoft.co.kr
			listCond.setType("record.cond." + userId);
		}
		String resourceId = getResourceIdByPkgPackage(pkg);
		if (CommonUtil.isEmpty(resourceId))
			return null;
		listCond.setCorrelation(resourceId);
		
		ColList filterList = getColManager().getList(userId, listCond, IManager.LEVEL_ALL);
		
		return getSearchFilterArrayByColList(pkgType, filterList);
	}
	public static SearchFilter[] getSearchFilterArrayByColList(String type, ColList list) throws Exception {
		if (list == null)
			return null;
		
		ColObject[] filterItemArray = list.getItems();
		
		if (CommonUtil.isEmpty(filterItemArray))
			return null;
		
		List<SearchFilter> filterList = new ArrayList<SearchFilter>();
		for (int i = 0; i < filterItemArray.length; i++) {

			ColObject filterItem = filterItemArray[i];

			String id = filterItem.getRef();
			String name = filterItem.getLabel();
			String conditionStr = filterItem.getExpression();
			Condition[] conditions = null;
			if (!CommonUtil.isEmpty(conditionStr)) {
				Filter[] filters = null;
				if (type.equalsIgnoreCase("PROCESS") || type.equalsIgnoreCase("GANTT")) {
					PrcProcessInstCond prcCond = (PrcProcessInstCond)PrcProcessInstCond.toObject(conditionStr);
					if (prcCond == null)
						continue;
					filters = prcCond.getFilter();
				} else {
					SwdRecordCond recCond = (SwdRecordCond)SwdRecordCond.toObject(conditionStr);
					if (recCond == null)
						continue;
					filters = recCond.getFilter();
				}
				if (filters == null)
					continue;
				Condition[] condArray = new Condition[filters.length];
				for (int j = 0; j < filters.length; j++) {
					Filter filter = filters[i];
					String leftOperType = filter.getLeftOperandType();
					String leftOperValue = filter.getLeftOperandValue();
					String rightOperType = filter.getRightOperandType();
					String rightOperValue = filter.getRightOperandValue();
					String operator = filter.getOperator();
					
					Condition cond = new Condition(new FormField(leftOperValue, leftOperValue, leftOperType) , operator, new FormField(rightOperValue, rightOperValue, rightOperType));
					condArray[i] = cond;
				}
				conditions = condArray;
			}
			
			SearchFilter searchFilter = new SearchFilter();
			searchFilter.setId(id);
			searchFilter.setName(name);
			searchFilter.setConditions(conditions);
			
			filterList.add(searchFilter);
		}
		SearchFilter[] searchFilter = new SearchFilter[filterList.size()];
		filterList.toArray(searchFilter);
		
		return searchFilter;
	}

	public static SearchFilter getSearchFilterByFilterId(String type, String workId, String filterId) throws Exception {
		if(CommonUtil.isEmpty(workId) || CommonUtil.isEmpty(filterId))
			return null;
		User cUser = SmartUtil.getCurrentUser();
		String userId = cUser.getId();

		PkgPackageCond packageCond = new PkgPackageCond();
		packageCond.setPackageId(workId);
		PkgPackage pkgPackage = getPkgManager().getPackage(userId, packageCond, IManager.LEVEL_ALL);

		String lnkType = null;
		String lnkCorr = null;
		if(type.equalsIgnoreCase("PROCESS") || type.equalsIgnoreCase("GANTT")) {
			lnkType = "processinst.cond." + userId;
		} else {
			lnkType = "record.cond." + userId;
		}
		lnkCorr = getResourceIdByPkgPackage(pkgPackage);

		ColList colList = null;
		ColObject[] colObjects = null;
		List<ColObject> colObjectsList = new ArrayList<ColObject>();
		ColListCond colListCond = new ColListCond();
		colListCond.setType(lnkType);
		colListCond.setCorrelation(lnkCorr);

		colList = getColManager().getList(userId, colListCond, IManager.LEVEL_ALL);
		if(colList != null) {
			colObjects = colList.getItems();
			if(!CommonUtil.isEmpty(colObjects)) {
				for(int i=0; i<colObjects.length; i++) {
					ColObject colObject = colObjects[i];
					if(CommonUtil.toNotNull(colObject.getRef()).equals(filterId)) {
						colObjectsList.add(colObject);
					}
				}
			}
		}
		if(colObjectsList.size() > 0) {
			colObjects = new ColObject[colObjectsList.size()];
			colObjectsList.toArray(colObjects);
		}

		if (CommonUtil.isEmpty(colObjects) || colObjects.length != 1)
			return null;

		ColObject colObject = colObjects[0];
		String id = colObject.getRef();
		String name = colObject.getLabel();
		String conditionStr = colObject.getExpression();

		Condition[] conditions = null;
		if (!CommonUtil.isEmpty(conditionStr)) {
			Filter[] filters = null;
			if (type.equalsIgnoreCase("PROCESS") || type.equalsIgnoreCase("GANTT")) {
				PrcProcessInstCond prcCond = (PrcProcessInstCond)PrcProcessInstCond.toObject(conditionStr);
				if (prcCond == null)
					return null;
				filters = prcCond.getFilter();
			} else {
				SwdRecordCond recCond = (SwdRecordCond)SwdRecordCond.toObject(conditionStr);
				if (recCond == null)
					return null;
				filters = recCond.getFilter();
			}
			if (filters == null)
				return null;
			Condition[] condArray = new Condition[filters.length];
			for (int i=0; i<filters.length; i++) {
				Filter filter = filters[i];
				String leftOperType = filter.getLeftOperandType();
				String leftOperValue = filter.getLeftOperandValue();
				String rightOperValue = filter.getRightOperandValue();
				String operator = filter.getOperator();
				Object rightOperand = null;
				if(leftOperType.equals(FormField.TYPE_USER)) {
					rightOperand = getUserByUserId(rightOperValue);
				} else if(leftOperType.equals(FormField.TYPE_OTHER_WORK)) {
					//start 2012.08.23 다른업무참조항목 상세필터 저장,수정,삭제,조회 수정
					//rightOperand = workService.getWorkById(rightOperValue);
					//end jybae
					rightOperand = (String)rightOperValue;
				} else if(leftOperType.equals(FormField.TYPE_DATETIME)) {
					rightOperand = LocalDate.convertGMTStringToLocalDate(rightOperValue);
				} else if(leftOperType.equals(FormField.TYPE_DATE)) {
					rightOperand = LocalDate.convertGMTSimple2StringToLocalDate(rightOperValue);
				} else if(leftOperType.equals(FormField.TYPE_TIME)) {
					rightOperand = LocalDate.convertGMTTimeStringToLocalDate2(rightOperValue);
				} else {
					rightOperand = (String)rightOperValue;
				}
				Condition cond = new Condition(new FormField(leftOperValue, null, leftOperType), operator, rightOperand);
				condArray[i] = cond;
			}
			conditions = condArray;
		}

		SearchFilter searchFilter = new SearchFilter();
		searchFilter.setId(id);
		searchFilter.setName(name);
		searchFilter.setConditions(conditions);

		return searchFilter;
		
	}

	public static String getResourceIdByPkgPackage(PkgPackage pkg) throws Exception {
		if (pkg == null)
			return null;

		String type = pkg.getType();
		String packageId = pkg.getPackageId();
		String resourceId = null;

		if(type.equals(SwfFormModel.TYPE_SINGLE)) {
			SwfFormCond swfFormCond = new SwfFormCond();
			swfFormCond.setPackageId(packageId);
			SwfForm[] swfForms = getSwfManager().getForms("", swfFormCond, IManager.LEVEL_LITE);
			if(!CommonUtil.isEmpty(swfForms)) {
				SwfForm swfForm = swfForms[0];
				resourceId = swfForm.getId();
			}
		} else if(type.equals(SwfFormModel.TYPE_PROCESS) || type.equals(SwfFormModel.TYPE_GANTT)) {
			PrcSwProcessCond prcSwProcessCond = new PrcSwProcessCond();
			prcSwProcessCond.setPackageId(packageId);
			PrcSwProcess[] swProcesses = getPrcManager().getSwProcesses("", prcSwProcessCond);
			if(!CommonUtil.isEmpty(swProcesses)) {
				PrcSwProcess swProcess = swProcesses[0];
				resourceId = swProcess.getProcessId();
			}
		}

/*		if (type.equalsIgnoreCase("PROCESS") || type.equalsIgnoreCase("GANTT")) {
			PrcProcessCond prcCond = new PrcProcessCond();
			prcCond.setDiagramId(packageId);
			PrcProcess[] prc = getPrcManager().getProcesses("", prcCond, IManager.LEVEL_LITE);
			if (CommonUtil.isEmpty(prc)) {
				return null;
			} else {
				return prc[0].getProcessId();
			}
		} else if (type.equalsIgnoreCase("SINGLE")) {
			SwfFormCond formCond = new SwfFormCond();
			formCond.setPackageId(packageId);
			
			SwfForm[] form = getSwfManager().getForms("", formCond, IManager.LEVEL_LITE);
			if (CommonUtil.isEmpty(form)) {
				return null;
			} else {
				return form[0].getId();
			}
		}*/
		return resourceId;
	}
	
	public static Map<String, WorkCategory> getPkgCtgMapByPackage(PkgPackage pkg) throws Exception {
		
		String categoryId = pkg.getCategoryId();
		if (CommonUtil.isEmpty(categoryId) || categoryId.equalsIgnoreCase(CtgCategory.ROOTCTGID))
			return null;
		
		CtgCategory ctg = getCtgCategoryByCategoryId(categoryId);
		CtgCategory parentCtg = getCtgCategoryByCategoryId(ctg.getParentId());
		
		Map<String, WorkCategory> resultMap = new HashMap<String, WorkCategory>();
		if (parentCtg == null || parentCtg.getObjId().equalsIgnoreCase(CtgCategory.ROOTCTGID)) {
			resultMap.put("category", (WorkCategory)getWorkCategoryByCtgCategory(null, ctg));
			resultMap.put("group", null);
		} else {
			resultMap.put("category", (WorkCategory)getWorkCategoryByCtgCategory(null, parentCtg));
			resultMap.put("group", (WorkCategory)getWorkCategoryByCtgCategory(null, ctg));
		}
		return resultMap;
	}
	public static FormField[] getFormFieldArrayBySwfFieldArray(SwfField[] fields) throws Exception {
		if (CommonUtil.isEmpty(fields))
			return null;
		
		FormField[] formFields = new FormField[fields.length];
		for (int i = 0; i < fields.length; i++) {
			SwfField field = fields[i];
			formFields[i] = getFormFieldBySwfField(null, field);
		}
		return formFields;
	}
	public static FormField getFormFieldBySwfField(FormField formField, SwfField swField) throws Exception {
		if (swField == null)
			return null;
		if (formField == null)
			formField = new FormField();
		
		/*<field id="4" name="내용" systemType="text" array="false" required="true" system="false">
			<format type="richEditor" viewingType="richEditor"/>
		</field>*/
		String id = swField.getId();
		String name = swField.getName();
		SwfFormat format = swField.getFormat();
		String type = format.getType();
		
		formField.setId(id);
		formField.setName(name);
		formField.setType(type);
		
		return formField;
	}
	public static SmartForm getSmartFormBySwfFrom(SmartForm smartForm, SwfForm swForm) throws Exception {
		if (swForm == null)
			return null;
		if (smartForm == null)
			smartForm = new SmartForm();
		
		String description = swForm.getDescription();
		String id = swForm.getId();
		
		String minImageName = SmartConfUtil.getInstance().getImageServer() + SmartUtil.getCurrentUser().getCompanyId() + "/workDef/" + swForm.getPackageId() + "/" + swForm.getId() + "_tn.png";
		String orgImageName = SmartConfUtil.getInstance().getImageServer() + SmartUtil.getCurrentUser().getCompanyId() + "/workDef/" + swForm.getPackageId() + "/" + swForm.getId() + ".png";
		
		String name = swForm.getName();
		SwfField[] swFields = swForm.getFields();
		
		smartForm.setDescription(description);
		smartForm.setFields(getFormFieldArrayBySwfFieldArray(swFields));
		smartForm.setId(id);
		smartForm.setMinImageName(minImageName);
		smartForm.setName(name);
		smartForm.setOrgImageName(orgImageName);
		
		return smartForm;
	}
	public static Instance getInstanceByPrcProcessInst(String userId, Instance instance, PrcProcessInst prcInst) throws Exception {
		if (prcInst == null)
			return null;
		if (instance == null)
			instance = new Instance();
		
		instance.setId(prcInst.getObjId());//processInstanceId
		instance.setSubject(prcInst.getTitle());
		instance.setCreatedDate(new LocalDate(prcInst.getCreationDate().getTime()));
		
		TskTask lastTask = getLastExecutedTskTaskByPrcInstId(prcInst.getObjId());
		if (lastTask == null) {
			instance.setLastModifier(new User());
			instance.setLastModifiedDate(new LocalDate(1)); //TODO LastModifiedDate now
		} else {
			instance.setLastModifier(getUserByUserId(lastTask.getAssignee()));
			instance.setLastModifiedDate(new LocalDate(lastTask.getExecutionDate().getTime())); //TODO LastModifiedDate now
		}
		
		instance.setOwner(getUserByUserId(prcInst.getCreationUser()));
		if (prcInst.getStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_COMPLETE)) {
			instance.setStatus(Instance.STATUS_COMPLETED);
		} else if (prcInst.getStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_RUNNING)) {
			if(prcInst.getExpectEndDate()!=null && prcInst.getExpectEndDate().getTime()<(new LocalDate()).getTime())
				instance.setStatus(Instance.STATUS_DELAYED_RUNNING);
			else
				instance.setStatus(Instance.STATUS_RUNNING);
		} else if (prcInst.getStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_RETURN)) {
			instance.setStatus(Instance.STATUS_RETURNED);
		} else if (prcInst.getStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_ABORTED)) {
			instance.setStatus(Instance.STATUS_ABORTED);
		} else if (prcInst.getStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_CANCEL)) {
			instance.setStatus(Instance.STATUS_REJECTED);
		}
		if (prcInst.getType() != null && prcInst.getType().equalsIgnoreCase(PrcProcessInst.PROCESSINSTTYPE_PROCESS)) {
			instance.setType(WorkInstance.TYPE_PROCESS);
		} else if (prcInst.getType() != null && prcInst.getType().equalsIgnoreCase(PrcProcessInst.PROCESSINSTTYPE_INFORMATION)) {
			instance.setType(WorkInstance.TYPE_INFORMATION);
		} else if (prcInst.getType() != null && prcInst.getType().equalsIgnoreCase(PrcProcessInst.PROCESSINSTTYPE_SCHEDULE)) {
			instance.setType(WorkInstance.TYPE_SCHEDULE);
		}
		
		String packageId = prcInst.getDiagramId();
		
		instance.setWork(getProcessWorkByPkgPackageId(userId, packageId));
		//TODO workspaceid > ??
		WorkSpaceInfo workSpaceInfo = getWorkSpaceInfo(CommonUtil.toDefault(prcInst.getWorkSpaceType(), String.valueOf(ISmartWorks.SPACE_TYPE_USER)), CommonUtil.toDefault(prcInst.getWorkSpaceId(), userId));		
		instance.setWorkSpace((workSpaceInfo==null) ? null : workSpaceInfo.getWorkSpace());
		
		return instance;
	}
	public static WorkInstance getWorkInstanceByPrcProcessInst(String userId, WorkInstance workInstance, PrcProcessInst prcInst) throws Exception {
		if (prcInst == null)
			return null;
		if (workInstance == null)
			workInstance = new WorkInstance();
		
		getInstanceByPrcProcessInst(userId, workInstance, prcInst);
		
		PWInstanceInfo pwInstInfo = getPWInstanceInfoByPrcProcessInst(null, prcInst);
		
		TskTaskCond tskCond = new TskTaskCond();
		tskCond.setProcessInstId(prcInst.getObjId());
		tskCond.setTypeNotIns(new String[]{"route", "and", "xor", "SERVICE"});

		tskCond.setOrders(new Order[] {new Order(TskTask.A_CREATIONDATE, true)});
		
		TskTask[] tasks = getTskManager().getTasks("", tskCond, IManager.LEVEL_ALL);
		
		workInstance.setTasks(getTaskInstanceInfoArrayByTskTaskArray(pwInstInfo, tasks));
		workInstance.setNumberOfSubInstances(-1);
		
		return workInstance;
	}

	public static ProcessWorkInstance getProcessWorkInstanceByPrcProcessInst(String userId, ProcessWorkInstance processWorkInstance, PrcProcessInst prcInst) throws Exception {
		if (prcInst == null)
			return null;
		if (processWorkInstance == null)
			processWorkInstance = new ProcessWorkInstance();
		
		getWorkInstanceByPrcProcessInst(userId, processWorkInstance, prcInst);
		
		TskTaskCond taskForwardCond = new TskTaskCond();
		taskForwardCond.setProcessInstId(prcInst.getObjId());
		long forwardCount = SwManagerFactory.getInstance().getTskManager().getFirstForwardTasksOnGroupByForwardIdSize(userId, taskForwardCond);
		processWorkInstance.setNumberOfForwardHistories((int)forwardCount);
		
		return processWorkInstance;
	}

	public static InformationWork getInformationWorkByPkgPackageId(String userId,String packageId) throws Exception {
		return getInformationWorkByPkgPackage(userId, null, getPkgPackageByPackageId(packageId));
	}
	public static InformationWork getInformationWorkByPkgPackage(String userId, InformationWork informationWork, PkgPackage pkg) throws Exception {
		if (pkg == null)
			return null;
		if (informationWork == null)
			informationWork = new InformationWork();
		
		getSmartWorkByPkgPackage(userId, informationWork, pkg);
		informationWork.setType(SmartWork.TYPE_INFORMATION);
		
		informationWork.setHelpUrl(pkg.getHelpUrl());
		informationWork.setManualFileId(pkg.getManualFileName());// Manual File Group Id
		
		List<IFileModel> file = SwManagerFactory.getInstance().getDocManager().findFileGroup(pkg.getManualFileName());
		if (file != null && file.size() != 0) {
			informationWork.setManualFileName(file.get(0).getFileName());
			
			//SmartConfUtil.getInstance().getImageServer() + SmartUtil.getCurrentUser().getCompanyId() + "/workDef/"
			
			informationWork.setManualFilePath(file.get(0).getFilePath());
		}
		
		//informationWork.setManualFileName(pkg.getManualFileName());
		//informationWork.setManualFilePath(SmartConfUtil.getInstance().getImageServer() + SmartUtil.getCurrentUser().getCompanyId() + "/workDef/" + pkg.getPackageId() + "/" + pkg.getManualFileName());

		return informationWork;
	}

/*	public static IWInstanceInfo getWorkInstanceInfoBySwdRecord(IWInstanceInfo iWInstanceInfo, SwdRecord swdRecord) throws Exception {
		if (swdRecord == null)
			return null;
		if (iWInstanceInfo == null) 
			iWInstanceInfo = new IWInstanceInfo();


		TskTaskCond tskCond = new TskTaskCond();
		tskCond.setExtendedProperties(new Property[] {new Property("recordId", swdRecord.getRecordId())});
		TskTask[] tasks = getTskManager().getTasks("", tskCond, IManager.LEVEL_LITE);

		String processInstId = tasks[0].getProcessInstId();

//		TskTask[] lastSwTask = getTskManager().getTasks("", tskCond, IManager.LEVEL_LITE);
		TskTask lastSwTask = getLastTskTaskByInstanceId(processInstId);
		
		TaskInstanceInfo lastTask = getTaskInstanceInfoByTskTask(iWInstanceInfo, null, lastSwTask);
		iWInstanceInfo.setLastTask(lastTask); 	
		return iWInstanceInfo;
	}
*/
	public static IWInstanceInfo getIWInstanceInfoByRecordId(IWInstanceInfo iWInstanceInfo, String recordId) throws Exception {
		if (CommonUtil.isEmpty(recordId))
			return null;
		if (iWInstanceInfo == null) 
			iWInstanceInfo = new IWInstanceInfo();
		
		TskTaskCond tskCond = new TskTaskCond();
		tskCond.setExtendedProperties(new Property[] {new Property("recordId", recordId)});
		TskTask[] tasks = getTskManager().getTasks("", tskCond, IManager.LEVEL_LITE);

		String processInstId = "";
		if(tasks != null)
			processInstId = tasks[0].getProcessInstId();

		
		if (!CommonUtil.isEmpty(recordId))
			iWInstanceInfo.setId(recordId);
		
		
//		TskTask[] lastSwTask = getTskManager().getTasks("", tskCond, IManager.LEVEL_LITE);
		TskTask lastSwTask = getLastTskTaskByInstanceId(processInstId);

		TaskInstanceInfo lastTask = getTaskInstanceInfoByTskTask(iWInstanceInfo, null, lastSwTask);
		iWInstanceInfo.setLastTask(lastTask); 
		
		return iWInstanceInfo;
	}
	public static IWInstanceInfo getIWInstanceInfoBySwdRecord(IWInstanceInfo iWInstanceInfo, SwdRecord swdRecord) throws Exception {
		if (swdRecord == null)
			return null;
		if (iWInstanceInfo == null) 
			iWInstanceInfo = new IWInstanceInfo();

		return getIWInstanceInfoByRecordId(iWInstanceInfo, swdRecord.getRecordId());
	}
	public static Instance getInstanceBySwdRecord(String userId, Instance instance, SwdRecord swdRecord) throws Exception {
		if (swdRecord == null)
			return null;
		if (instance == null)
			instance = new Instance();

		instance.setId(swdRecord.getRecordId());//processInstanceId
		SwdDomain swdDomain = getSwdManager().getDomain(userId, swdRecord.getDomainId(), IManager.LEVEL_LITE);
		String titleField = swdDomain.getTitleFieldId();
		String title = swdRecord.getDataFieldValue(titleField);
		instance.setSubject(StringUtil.subString(title, 0, 40, "..."));
		instance.setCreatedDate(new LocalDate(swdRecord.getCreationDate().getTime()));

		instance.setLastModifier(getUserByUserId(swdRecord.getModificationUser()));
		instance.setLastModifiedDate(new LocalDate((swdRecord.getModificationDate()).getTime()));

		instance.setOwner(getUserByUserId(swdRecord.getCreationUser()));
		instance.setStatus(Instance.STATUS_COMPLETED);
		instance.setType(WorkInstance.TYPE_INFORMATION);

		String formId = swdDomain.getFormId();
		SwfForm swfForm = getSwfManager().getForm(userId, formId);

		String packageId = swfForm.getPackageId();

		instance.setWork(getInformationWorkByPkgPackageId(userId, packageId));

		WorkSpaceInfo workSpaceInfo = getWorkSpaceInfo(CommonUtil.toDefault(swdRecord.getWorkSpaceType(), String.valueOf(ISmartWorks.SPACE_TYPE_USER)), CommonUtil.toDefault(swdRecord.getWorkSpaceId(), userId));
		instance.setWorkSpace((workSpaceInfo==null) ? null : workSpaceInfo.getWorkSpace());

		instance.setAccessPolicy(new AccessPolicy(CommonUtil.toInt(swdRecord.getAccessLevel(), 0)));
		return instance;
	}

	public static WorkInstance getWorkInstanceBySwdRecord(String userId, WorkInstance workInstance, SwdRecord swdRecord) throws Exception {
		if (swdRecord == null)
			return null;
		if (workInstance == null)
			workInstance = new WorkInstance();

		getInstanceBySwdRecord(userId, workInstance, swdRecord);
		
		IWInstanceInfo iwInstInfo = getIWInstanceInfoBySwdRecord(null, swdRecord);

		TskTaskCond tskCond = new TskTaskCond();
		tskCond.setExtendedProperties(new Property[] {new Property("recordId", swdRecord.getRecordId())});
		TskTask[] tasks = getTskManager().getTasks("", tskCond, IManager.LEVEL_LITE);

		String processInstId = "";
		if(tasks != null)
			processInstId = tasks[0].getProcessInstId();
		
		PrcProcessInst prcInst = getPrcManager().getProcessInst(userId, processInstId, IManager.LEVEL_LITE);
		
		if (prcInst != null) {
			if (prcInst.getStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_RUNNING)) {
				if(prcInst.getExpectEndDate()!=null && prcInst.getExpectEndDate().getTime()<(new LocalDate()).getTime())
					workInstance.setStatus(Instance.STATUS_DELAYED_RUNNING);
				else
					workInstance.setStatus(Instance.STATUS_RUNNING);
			} else if (prcInst.getStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_COMPLETE)) {
				workInstance.setStatus(Instance.STATUS_COMPLETED);
			} else if (prcInst.getStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_RETURN)) {
				workInstance.setStatus(Instance.STATUS_RETURNED);
			} else if (prcInst.getStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_ABORTED)) {
				workInstance.setStatus(Instance.STATUS_ABORTED);
			} else if (prcInst.getStatus().equalsIgnoreCase(PrcProcessInst.PROCESSINSTSTATUS_CANCEL)) {
				workInstance.setStatus(Instance.STATUS_REJECTED);
			}
		}

		tskCond = new TskTaskCond();
		tskCond.setProcessInstId(processInstId);
		tskCond.setOrders(new Order[]{new Order(TskTask.A_CREATIONDATE, true)});

		tasks = getTskManager().getTasks("", tskCond, IManager.LEVEL_ALL);

		workInstance.setTasks(getTaskInstanceInfoArrayByTskTaskArray(iwInstInfo, tasks));
		workInstance.setNumberOfSubInstances(-1);
		return workInstance;
	}

	public static InformationWorkInstance getApprovalWorkInformationByInstanceId(InformationWorkInstance informationWorkInstance, String instanceId) throws Exception {
		if (CommonUtil.isEmpty(instanceId))
			return null;

		User cUser = SmartUtil.getCurrentUser();
		AprApprovalLineCond approvalLineCond = new AprApprovalLineCond();
		Property[] extProps = new Property[] {new Property("recordId", instanceId)};
		approvalLineCond.setExtendedProperties(extProps);
		approvalLineCond.setOrders(new Order[]{new Order("creationDate", false)});
		AprApprovalLine[] aprApprovalLines = getAprManager().getApprovalLines(cUser.getId(), approvalLineCond, IManager.LEVEL_ALL);
		AprApprovalLine aprApprovalLine = null;
		if (aprApprovalLines != null && aprApprovalLines.length != 0)
			aprApprovalLine = aprApprovalLines[0];
		
		boolean isApprovalWork = false;
		ApprovalLine approvalLine = new ApprovalLine();
		Approval[] approvals = null;
		if(aprApprovalLine != null) {
			isApprovalWork = true;
			if (aprApprovalLine.getStatus() != null && aprApprovalLine.getStatus().equalsIgnoreCase("24"))
				isApprovalWork = false;
			AprApproval[] aprApprovals = aprApprovalLine.getApprovals();
			List<Approval> approvalList = new ArrayList<Approval>();
			if(!CommonUtil.isEmpty(aprApprovals)) {
				for(AprApproval aprApproval : aprApprovals) {
					Approval approval = new Approval();
					approval.setName(aprApproval.getName());
					approval.setApproverType(CommonUtil.toInt(aprApproval.getType(), Approval.APPROVER_CHOOSE_ON_RUNNING));
					approval.setApprover(getUserByUserId(aprApproval.getApprover()));
					String dueDate = CommonUtil.toNotNull(aprApproval.getDueDate());
					int meanTimeDays = 0;
					int meanTimeHours = 0;
					int meanTimeMinutes = 30;
					int daysToMinutes = 60 * 24;
					int hoursToMinutes = 60;
					if(!dueDate.equals("")) {
						int meanTime = Integer.parseInt(dueDate);
						meanTimeDays = meanTime / daysToMinutes;
						meanTime = meanTime % daysToMinutes;
						meanTimeHours = meanTime / hoursToMinutes;
						meanTimeMinutes = meanTime % hoursToMinutes;
					}
					approval.setMeanTimeDays(meanTimeDays);
					approval.setMeanTimeHours(meanTimeHours);
					approval.setMeanTimeMinutes(meanTimeMinutes);
					approvalList.add(approval);
				}
				if(approvalList.size() > 0) {
					approvals = new Approval[approvalList.size()];
					approvalList.toArray(approvals);
				}
			}

			String desc = aprApprovalLine.getDescription();
			int approvalLevel = approvalList.size();

			approvalLine.setDesc(desc);
			approvalLine.setApprovalLevel(approvalLevel);
			approvalLine.setApprovals(approvals);
		}
		informationWorkInstance.setApprovalWork(isApprovalWork);
		informationWorkInstance.setApprovalLine(approvalLine);
		if (aprApprovalLine != null && !CommonUtil.isEmpty(aprApprovalLine.getCorrelation())) {
			String singleTaskId = aprApprovalLine.getCorrelation();
			TskTask task = getTskManager().getTask("", singleTaskId, IManager.LEVEL_ALL);
			if (task != null) {

				String referenceUsers = task.getExtendedPropertyValue("referenceUser");
				String isLazyReferenceTask = task.getExtendedPropertyValue("isLazyReferenceTask");
				if (!CommonUtil.isEmpty(referenceUsers)) {
					
					String[] ids = convertUserIdsByUserAndDeptAndGroupIdArray(StringUtils.tokenizeToStringArray(referenceUsers, ";"));
					
					SwoUserExtend[] users = getSwoManager().getUsersExtend("", ids);
					if (users != null) {
						List userInfoList = new ArrayList();
						for (int i = 0; i < users.length; i++) {
							UserInfo userInfo = getUserInfoBySwoUserExtend(null, users[i]);
							userInfoList.add(userInfo);
						}
						UserInfo[] forwardees = new UserInfo[userInfoList.size()];
						userInfoList.toArray(forwardees);
						informationWorkInstance.setForwardees(forwardees);
					}
				}
				if (!CommonUtil.isEmpty(isLazyReferenceTask)) {
					informationWorkInstance.setLazyreferenceTask(CommonUtil.toBoolean(isLazyReferenceTask));
				}
			}
		}
		
		return informationWorkInstance;
	}
	
	/**
	 * @param ids
	 * @return 사용자아이디 배열
	 * @throws Exception
	 * 사용자 아이디와 부서아이디 그룹아이디가 섞여서 들어온 스트링 어레이를 부서에 속한 사용자, 그룹에 속한 사용자를 모두꺼내어
	 * 사용자 아이디(스트링 어레이)로 변경한다
	 */
	public static String[] convertUserIdsByUserAndDeptAndGroupIdArray(String[] ids) throws Exception {
		
		if (!CommonUtil.isEmpty(ids)) {
			List refUserIdList = new ArrayList();
			//부서를 선택하였을경우
			for (int i = 0; i < ids.length; i++) {
				String users = ids[i];
				if (users.indexOf("dept_") != -1) {
					String deptId = users;
					SwoUserCond userCond = new SwoUserCond();
					userCond.setDeptId(deptId);
					SwoUser[] deptUsers = SwManagerFactory.getInstance().getSwoManager().getUsers("", userCond, IManager.LEVEL_LITE);
					for (int j = 0; j < deptUsers.length; j++) {
						if (!refUserIdList.contains(deptUsers[j]))
							refUserIdList.add(deptUsers[j].getId());
					}
				} else if (users.indexOf("group_") != -1) {
					String groupId = users;
					SwoGroupCond groupCond = new SwoGroupCond();
					groupCond.setId(groupId);
					SwoGroup group = SwManagerFactory.getInstance().getSwoManager().getGroup("", groupCond, IManager.LEVEL_ALL);
					
					SwoGroupMember[] member = group.getSwoGroupMembers();
					if (member != null) {
						for (int j = 0; j < member.length; j++) {
							if (!refUserIdList.contains(member[j].getUserId()))
								refUserIdList.add(member[j].getUserId());
						}
					}
				} else{
					refUserIdList.add(users);
				}
			}
			String[] refUsers = new String[refUserIdList.size()];
			refUserIdList.toArray(refUsers);
			
			return refUsers;
		} else {
			return null;
		}
	}
	public static InformationWorkInstance getInformationWorkInstanceBySwdRecord(String userId, InformationWorkInstance informationWorkInstance, SwdRecord swdRecord) throws Exception {
		if (swdRecord == null)
			return null;
		if (informationWorkInstance == null)
			informationWorkInstance = new InformationWorkInstance();

		getWorkInstanceBySwdRecord(userId, informationWorkInstance, swdRecord);

		int numberOfRelatedWorks = getSwfManager().getReferenceFormSize("", swdRecord.getRecordId());

		informationWorkInstance.setNumberOfRelatedWorks(numberOfRelatedWorks);
		
		TaskInstanceInfo[] taskInfos = informationWorkInstance.getTasks();
		if (taskInfos != null && taskInfos.length != 0) {
			TaskInstanceInfo taskInfo = taskInfos[0];
			if (taskInfo.getWorkInstance() != null) {
				//String recordId = taskInfo.getWorkInstance().getId();
				
				TskTask task = SwManagerFactory.getInstance().getTskManager().getTask(userId, taskInfo.getId(), IManager.LEVEL_LITE);
				
				String instanceId = task.getProcessInstId();
				
				FileDownloadHistoryCond cond = new FileDownloadHistoryCond();
				cond.setRefPrcInstId(instanceId);
				long downloadCount = SwManagerFactory.getInstance().getDocManager().getFileDownloadHistorySize(userId, cond);
				informationWorkInstance.setNumberOfDownloadHistories((int)downloadCount);

				TskTaskCond taskCond = new TskTaskCond();
				taskCond.setType(TskTask.TASKTYPE_SINGLE);
				taskCond.setProcessInstId(instanceId);
				long updateCount = SwManagerFactory.getInstance().getTskManager().getTaskSize(userId, taskCond);
				informationWorkInstance.setNumberOfUpdateHistories((int)updateCount);
				
				TskTaskCond taskForwardCond = new TskTaskCond();
				taskForwardCond.setProcessInstId(instanceId);
				long forwardCount = SwManagerFactory.getInstance().getTskManager().getFirstForwardTasksOnGroupByForwardIdSize(userId, taskCond);
				informationWorkInstance.setNumberOfForwardHistories((int)forwardCount);
			}
		}
		getApprovalWorkInformationByInstanceId(informationWorkInstance, swdRecord.getRecordId());

		if(swdRecord.getDomainId().equals(SmartForm.ID_EVENT_MANAGEMENT)){
			SwdDataField repeatIdField = swdRecord.getDataField(FormField.ID_NUM_REPEAT_EVENT_ID);
			if(repeatIdField!=null && !SmartUtil.isBlankObject(repeatIdField.getValue()))
				informationWorkInstance.setRepeatEventId(repeatIdField.getValue());
			
		}
		return informationWorkInstance;
	}

	public static ImageInstance getImageInstanceBySwdRecord(String userId, ImageInstance imageInstance, SwdRecord swdRecord) throws Exception {
		if (swdRecord == null)
			return null;
		if (imageInstance == null)
			imageInstance = new ImageInstance();

		String companyId = SmartUtil.getCurrentUser().getCompanyId();
		ImageInstance tempWorkInstance = new ImageInstance();
		getWorkInstanceBySwdRecord(userId, tempWorkInstance, swdRecord);
		tempWorkInstance.setType(Instance.TYPE_IMAGE);

		String fileGroupId = swdRecord.getDataFieldValue("5");//TODO 첨부파일 파일 그룹아이디를 가져오기 위한 하드코딩
		String content = swdRecord.getDataFieldValue("4");
		
		List<IFileModel> files = getDocManager().findFileGroup(fileGroupId);
		String fileName = null;
		String originImgSrc = "";
		String imgSrc = "";
		if (files != null && files.size() != 0) {
			fileName = files.get(0).getFileName();
			String filePath = files.get(0).getFilePath();
			String extension = filePath.lastIndexOf(".") > 1 ? filePath.substring(filePath.lastIndexOf(".")) : null;
			filePath = StringUtils.replace(filePath, "\\", "/");
			if(filePath.indexOf(companyId) != -1)
				originImgSrc = Community.PICTURE_PATH + filePath.substring(filePath.indexOf(companyId), filePath.length());
			filePath = filePath.replaceAll(extension, Community.IMAGE_TYPE_THUMB + extension);
			if(filePath.indexOf(companyId) != -1)
				imgSrc = Community.PICTURE_PATH + filePath.substring(filePath.indexOf(companyId), filePath.length());
		}
		if(!CommonUtil.isEmpty(fileGroupId)) {
			tempWorkInstance.setGroupId(fileGroupId);
			List<IFileModel> fileModelList = getDocManager().findFileGroup(fileGroupId);
			List<Map<String, String>> fileList = new ArrayList<Map<String,String>>();
			int fileModelListSize = fileModelList.size();
			if(fileList != null && fileModelListSize > 0) {
				for(int i=0; i<fileModelListSize; i++) {
					Map<String, String> fileMap = new LinkedHashMap<String, String>();
					IFileModel fileModel = fileModelList.get(i);
					String id = fileModel.getId();
					String name = fileModel.getFileName();
					String type = fileModel.getType();
					String size = fileModel.getFileSize() + "";
					fileMap.put("fileId", id);
					fileMap.put("fileName", name);
					fileMap.put("fileType", type);
					fileMap.put("fileSize", size);
					fileList.add(fileMap);
				}
			}
		}
		tempWorkInstance.setFileName(fileName);
		tempWorkInstance.setOriginImgSource(originImgSrc);
		tempWorkInstance.setImgSource(imgSrc);
		tempWorkInstance.setContent(content);
		//start : sjlee
		tempWorkInstance.setSubInstanceCount(instanceService.getSubInstanceCountInInstance(swdRecord.getRecordId()));
		tempWorkInstance.setSubInstances(instanceService.getRecentSubInstancesInInstance(swdRecord.getRecordId(), WorkInstance.DEFAULT_SUB_INSTANCE_FETCH_COUNT));
		//end : sjlee		
		imageInstance = tempWorkInstance;
		return imageInstance;
		
	}
	
	public static ImageCategoryInfo[] getImageCategoriesByType(int displayType, String spaceId) throws Exception {
		try {
			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();
			String companyId = cUser.getCompanyId();
			ImageCategoryInfo[] imageCategoryInfos = null;
			List<ImageCategoryInfo> imageCategoryInfoList = new ArrayList<ImageCategoryInfo>();
			String fileId = "";
			String originImgSrc = "";
			String imgSrc = "";
			ImageInstanceInfo imageInstanceInfo = null;
			Map<String, ImageCategoryInfo> imageCategoryMap = new LinkedHashMap<String, ImageCategoryInfo>();

			FileWorkCond fileWorkCond = new FileWorkCond();
			fileWorkCond.setTskAssigneeOrSpaceId(spaceId);
			fileWorkCond.setTskRefType(TskTask.TASKREFTYPE_IMAGE);
			fileWorkCond.setOrders(new Order[]{new Order("tskCreatedate", true)});
			FileWork[] fileWorks = getDocManager().getFileWorkList(userId, fileWorkCond);
/*
			if(!CommonUtil.isEmpty(fileWorks)) {

				ImageCategoryInfo allImageCategoryInfo = new ImageCategoryInfo();
				allImageCategoryInfo.setId(FileCategory.ID_ALL_FILES);
				allImageCategoryInfo.setName(FileCategory.NAME_ALL_FILES);
				allImageCategoryInfo.setLength(fileWorks.length);

				FileWork fileWork = fileWorks[0];
				fileId = fileWork.getFileId();
				IFileModel fileModel = getDocManager().getFileById(fileId);
				if(fileModel != null) {
					String filePath = fileModel.getFilePath();
					String extension = filePath.lastIndexOf(".") > 1 ? filePath.substring(filePath.lastIndexOf(".")) : null;
					filePath = StringUtils.replace(filePath, "\\", "/");
					if(filePath.indexOf(companyId) != -1)
						originImgSrc = Community.PICTURE_PATH + filePath.substring(filePath.indexOf(companyId), filePath.length());
					filePath = filePath.replaceAll(extension, Community.IMAGE_TYPE_THUMB + extension);
					if(filePath.indexOf(companyId) != -1)
						imgSrc = Community.PICTURE_PATH + filePath.substring(filePath.indexOf(companyId), filePath.length());
				}
				ImageInstanceInfo firstImageInstanceInfo = new ImageInstanceInfo();
				firstImageInstanceInfo.setFileId(fileId);
				firstImageInstanceInfo.setOriginImgSource(originImgSrc);
				firstImageInstanceInfo.setImgSource(imgSrc);
				allImageCategoryInfo.setFirstImage(firstImageInstanceInfo);

				imageCategoryMap.put(FileCategory.ID_ALL_FILES, allImageCategoryInfo);
			}*/
			ImageCategoryInfo allImageCategoryInfo = new ImageCategoryInfo();
			allImageCategoryInfo.setId(FileCategory.ID_ALL_FILES);
			allImageCategoryInfo.setName(FileCategory.NAME_ALL_FILES);
			allImageCategoryInfo.setLength(fileWorks == null ? 0 : fileWorks.length);
			imageCategoryMap.put(FileCategory.ID_ALL_FILES, allImageCategoryInfo);

			switch (displayType) {
			case FileCategory.DISPLAY_BY_CATEGORY:
				FdrFolderCond fdrFolderCond = new FdrFolderCond();
				fdrFolderCond.setCompanyId(companyId);
				fdrFolderCond.setCreationUser(userId);
				fdrFolderCond.setWorkspaceId(spaceId);
				fdrFolderCond.setRefType(TskTask.TASKREFTYPE_IMAGE);
				fdrFolderCond.setOrders(new Order[]{new Order(FdrFolderCond.A_DISPLAYORDER, true)});
				FdrFolder[] fdrFolders = getFdrManager().getFolders(userId, fdrFolderCond, IManager.LEVEL_ALL);
				if(!CommonUtil.isEmpty(fileWorks)) {
					for(FileWork fileWork : fileWorks) {
						int length = 1;
						ImageCategoryInfo imageCategoryInfo = new ImageCategoryInfo();
						imageInstanceInfo = new ImageInstanceInfo();
						String folderId = fileWork.getFolderId();
						String folderName = fileWork.getFolderName();
						if(CommonUtil.isEmpty(folderId)) {
							folderId = FileCategory.ID_UNCATEGORIZED;
							folderName = FileCategory.NAME_UNCATEGORIZED;
						}
						imageCategoryInfo.setId(folderId);
						imageCategoryInfo.setName(folderName);
						fileId = fileWork.getFileId();
						IFileModel fileModel = getDocManager().getFileById(fileId);
						if(fileModel != null) {
							String filePath = fileModel.getFilePath();
							String extension = filePath.lastIndexOf(".") > 1 ? filePath.substring(filePath.lastIndexOf(".")) : null;
							filePath = StringUtils.replace(filePath, "\\", "/");
							if(filePath.indexOf(companyId) != -1)
								originImgSrc = Community.PICTURE_PATH + filePath.substring(filePath.indexOf(companyId), filePath.length());
							filePath = filePath.replaceAll(extension, Community.IMAGE_TYPE_THUMB + extension);
							if(filePath.indexOf(companyId) != -1)
								imgSrc = Community.PICTURE_PATH + filePath.substring(filePath.indexOf(companyId), filePath.length());
						}
						Map<String, String> fileMap = new LinkedHashMap<String, String>();
						List<Map<String, String>> fileList = new ArrayList<Map<String,String>>();
						String fileName = fileModel.getFileName();
						String fileType = fileModel.getType();
						String fileSize = String.valueOf(fileModel.getFileSize());
						fileMap.put("fileId", fileId);
						fileMap.put("fileName", fileName);
						fileMap.put("fileType", fileType);
						fileMap.put("fileSize", fileSize);
						fileList.add(fileMap);
						if(fileList.size() > 0)
							imageInstanceInfo.setFiles(fileList);
						imageInstanceInfo.setFileId(fileId);
						imageInstanceInfo.setOriginImgSource(originImgSrc);
						imageInstanceInfo.setImgSource(imgSrc);
						imageCategoryInfo.setFirstImage(imageInstanceInfo);
						if(!CommonUtil.isEmpty(imageCategoryMap)) {
							for(Map.Entry<String, ImageCategoryInfo> entry : imageCategoryMap.entrySet()) {
								if(entry.getKey().equals(imageCategoryInfo.getId()))
									length = entry.getValue().getLength() + 1;
							}
						}
						imageCategoryInfo.setLength(length);
						imageCategoryMap.put(folderId, imageCategoryInfo);
					}
					if(!CommonUtil.isEmpty(fdrFolders)) {
						for(FdrFolder fdrFolder : fdrFolders) {
							if(CommonUtil.isEmpty(fdrFolder.getFolderFiles())) {
								ImageCategoryInfo imageCategoryInfo = new ImageCategoryInfo();
								imageInstanceInfo = new ImageInstanceInfo();
								String folderId = fdrFolder.getObjId();
								String folderName = fdrFolder.getName();
								int length = 0;
								imageCategoryInfo.setId(folderId);
								imageCategoryInfo.setName(folderName);
								imageCategoryInfo.setLength(length);
								imageInstanceInfo.setFileId("");
								imageInstanceInfo.setOriginImgSource("");
								imageInstanceInfo.setImgSource("");
								imageCategoryInfo.setFirstImage(imageInstanceInfo);
								imageCategoryMap.put(folderId, imageCategoryInfo);
							}
						}
					}
				}

				if(!CommonUtil.isEmpty(imageCategoryMap)) {
					for(Map.Entry<String, ImageCategoryInfo> entry : imageCategoryMap.entrySet()) {
						ImageCategoryInfo imageCategoryInfo = entry.getValue();
						imageCategoryInfoList.add(imageCategoryInfo);
					}
				}
				if(imageCategoryInfoList.size() > 0) {
					if(displayType == FileCategory.DISPLAY_BY_YEAR){
						Collections.sort(imageCategoryInfoList, Collections.reverseOrder());
					}else{
						Collections.sort(imageCategoryInfoList);						
					}
					imageCategoryInfos = new ImageCategoryInfo[imageCategoryInfoList.size()];
					imageCategoryInfoList.toArray(imageCategoryInfos);
				}
				return imageCategoryInfos;
/*				FdrFolderCond fdrFolderCond = new FdrFolderCond();
				fdrFolderCond.setCompanyId(companyId);
				fdrFolderCond.setCreationUser(userId);
				fdrFolderCond.setWorkspaceId(spaceId);
				fdrFolderCond.setRefType(TskTask.TASKREFTYPE_IMAGE);
				fdrFolderCond.setOrders(new Order[]{new Order(FdrFolderCond.A_DISPLAYORDER, true)});
				FdrFolder[] fdrFolders = getFdrManager().getFolders(userId, fdrFolderCond, IManager.LEVEL_ALL);
				FdrFolder addFdrFolder = new FdrFolder();
				addFdrFolder.setObjId(FileCategory.ID_UNCATEGORIZED);
				addFdrFolder.setName(FileCategory.ID_UNCATEGORIZED);
				fdrFolders = FdrFolder.add(fdrFolders, addFdrFolder);
				if(!CommonUtil.isEmpty(fdrFolders)) {
					for(FdrFolder fdrFolder : fdrFolders) {
						ImageCategoryInfo imageCategoryInfo = new ImageCategoryInfo();
						String folderId = fdrFolder.getObjId();
						String folderName = fdrFolder.getName();
						if(folderId.equals(FileCategory.ID_UNCATEGORIZED)) {
							tskTaskCond = new TskTaskCond();
							tskTaskCond.setWorkSpaceId(spaceId);
							tskTaskCond.setRefType(TskTask.TASKREFTYPE_IMAGE);
							tskTaskCond.setOrders(new Order[]{new Order(FdrFolderCond.A_MODIFICATIONDATE, false)});

							tskTasks = getTskManager().getTasks(userId, tskTaskCond, IManager.LEVEL_LITE);

							List<IFileModel[]> fileModelsList = new ArrayList<IFileModel[]>();
							if(!CommonUtil.isEmpty(tskTasks)) {
								int taskLength = tskTasks.length;
								for(int i=0; i<taskLength; i++) {
									String taskInstId = tskTasks[i].getObjId();
									IFileModel[] fileModels = getDocManager().getFilesByTaskInstId(taskInstId);
									if(!CommonUtil.isEmpty(fileModels))
										fileModelsList.add(fileModels);
								}
							}
							List<FdrFolderFile> fdrFolderFileList = new ArrayList<FdrFolderFile>();
							FdrFolderFile[] fdrFolderFiles = null;
							FdrFolderFile[] fdrFolderFiles2 = null;
							if(!CommonUtil.isEmpty(fileModelsList)) {
								int fileModelsListSize = fileModelsList.size();
								for(int j=0; j<fileModelsListSize; j++) {
									IFileModel[] fileModels = fileModelsList.get(j);
									if(!CommonUtil.isEmpty(fileModels)) {
										int fileModelsLength = fileModels.length;
										for(int k=0; k<fileModelsLength; k++) {
											fileId = fileModels[k].getId();
											fdrFolderFiles = new FdrFolderFile[1];
											FdrFolderFile fdrFolderFile = new FdrFolderFile();
											fdrFolderFile.setFileId(fileId);
											fdrFolderFiles[0] = fdrFolderFile;
											fdrFolderCond = new FdrFolderCond();
											fdrFolderCond.setCreationUser(userId);
											fdrFolderCond.setFolderFiles(fdrFolderFiles);
											fdrFolderCond.setOrders(new Order[]{new Order(FdrFolderCond.A_DISPLAYORDER, true)});
											FdrFolder unFdrFolder = getFdrManager().getFolder(userId, fdrFolderCond, IManager.LEVEL_ALL);
											if(unFdrFolder == null) {
												FdrFolderFile fdrFolderFile2 = new FdrFolderFile();
												IFileModel fileModel = getDocManager().getFileById(fileId);
												fdrFolderFile2.setFileId(fileModel.getId());
												fdrFolderFileList.add(fdrFolderFile2);
											}
										}
									}
								}
								if(fdrFolderFileList.size() > 0) {
									fdrFolderFiles2 = new FdrFolderFile[fdrFolderFileList.size()];
									fdrFolderFileList.toArray(fdrFolderFiles2);
									fdrFolder.setFolderFiles(fdrFolderFiles2);
								}
							}
						}
						FdrFolderFile[] fdrFolderFiles = fdrFolder.getFolderFiles();
						int fileLength = 0;
						originImgSrc = "";
						imgSrc = "";
						imageInstanceInfo = new ImageInstanceInfo();
						List<IFileModel> fileModelList = new ArrayList<IFileModel>();
						if(!CommonUtil.isEmpty(fdrFolderFiles)) {
							for(FdrFolderFile fdrFolderFile : fdrFolderFiles) {
								IFileModel fileModel = getDocManager().getFileById(fdrFolderFile.getFileId());
								fileModelList.add(fileModel);
							}
							if(fileModelList.size() > 0)
								Collections.sort(fileModelList, Collections.reverseOrder());

							fileId = fileModelList.get(0).getId();
							IFileModel fileModel = getDocManager().getFileById(fileId);
							if(fileModel != null) {
								String filePath = fileModel.getFilePath();
								String extension = filePath.lastIndexOf(".") > 1 ? filePath.substring(filePath.lastIndexOf(".")) : null;
								filePath = StringUtils.replace(filePath, "\\", "/");
								if(filePath.indexOf(companyId) != -1)
									originImgSrc = Community.PICTURE_PATH + filePath.substring(filePath.indexOf(companyId), filePath.length());
								filePath = filePath.replaceAll(extension, "_thumb" + extension);
								if(filePath.indexOf(companyId) != -1)
									imgSrc = Community.PICTURE_PATH + filePath.substring(filePath.indexOf(companyId), filePath.length());
							}
							fileLength = fdrFolderFiles.length;
						}
						imageCategoryInfo.setId(folderId);
						imageCategoryInfo.setName(folderName);
						imageCategoryInfo.setLength(fileLength);
						imageInstanceInfo.setOriginImgSource(originImgSrc);
						imageInstanceInfo.setImgSource(imgSrc);
						imageCategoryInfo.setFirstImage(imageInstanceInfo);
						imageCategoryInfoList.add(imageCategoryInfo);
					}
				}
				if(imageCategoryInfoList.size() > 0) {
					imageCategoryInfos = new ImageCategoryInfo[imageCategoryInfoList.size()];
					imageCategoryInfoList.toArray(imageCategoryInfos);
				}
				return imageCategoryInfos;*/
			case FileCategory.DISPLAY_BY_YEAR:
				if(!CommonUtil.isEmpty(fileWorks)) {
					for(FileWork fileWork : fileWorks) {
						int length = 1;
						ImageCategoryInfo imageCategoryInfo = new ImageCategoryInfo();
						imageInstanceInfo = new ImageInstanceInfo();
						String writtenId = new LocalDate(fileWork.getWrittenTime().getTime()).toLocalMonthString();
						String writtenName = new LocalDate(fileWork.getWrittenTime().getTime()).toLocalMonthString();
						imageCategoryInfo.setId(writtenId);
						imageCategoryInfo.setName(writtenName);
						fileId = fileWork.getFileId();
						IFileModel fileModel = getDocManager().getFileById(fileId);
						if(fileModel != null) {
							String filePath = fileModel.getFilePath();
							String extension = filePath.lastIndexOf(".") > 1 ? filePath.substring(filePath.lastIndexOf(".")) : null;
							filePath = StringUtils.replace(filePath, "\\", "/");
							if(filePath.indexOf(companyId) != -1)
								originImgSrc = Community.PICTURE_PATH + filePath.substring(filePath.indexOf(companyId), filePath.length());
							filePath = filePath.replaceAll(extension, Community.IMAGE_TYPE_THUMB + extension);
							if(filePath.indexOf(companyId) != -1)
								imgSrc = Community.PICTURE_PATH + filePath.substring(filePath.indexOf(companyId), filePath.length());
						}
						Map<String, String> fileMap = new LinkedHashMap<String, String>();
						List<Map<String, String>> fileList = new ArrayList<Map<String,String>>();
						String fileName = fileModel.getFileName();
						String fileType = fileModel.getType();
						String fileSize = String.valueOf(fileModel.getFileSize());
						fileMap.put("fileId", fileId);
						fileMap.put("fileName", fileName);
						fileMap.put("fileType", fileType);
						fileMap.put("fileSize", fileSize);
						fileList.add(fileMap);
						if(fileList.size() > 0)
							imageInstanceInfo.setFiles(fileList);
						imageInstanceInfo.setFileId(fileId);
						imageInstanceInfo.setOriginImgSource(originImgSrc);
						imageInstanceInfo.setImgSource(imgSrc);
						imageCategoryInfo.setFirstImage(imageInstanceInfo);
						if(!CommonUtil.isEmpty(imageCategoryMap)) {
							for(Map.Entry<String, ImageCategoryInfo> entry : imageCategoryMap.entrySet()) {
								if(entry.getKey().equals(imageCategoryInfo.getId()))
									length = entry.getValue().getLength() + 1;
							}
						}
						imageCategoryInfo.setLength(length);
						imageCategoryMap.put(writtenId, imageCategoryInfo);
					}
				}
				if(!CommonUtil.isEmpty(imageCategoryMap)) {
					for(Map.Entry<String, ImageCategoryInfo> entry : imageCategoryMap.entrySet()) {
						ImageCategoryInfo imageCategoryInfo = entry.getValue();
						imageCategoryInfoList.add(imageCategoryInfo);
					}
				}
				if(imageCategoryInfoList.size() > 0) {
					Collections.sort(imageCategoryInfoList, Collections.reverseOrder());
					imageCategoryInfos = new ImageCategoryInfo[imageCategoryInfoList.size()];
					imageCategoryInfoList.toArray(imageCategoryInfos);
				}
				return imageCategoryInfos;
				/*tskTaskCond = new TskTaskCond();
				tskTaskCond.setWorkSpaceId(spaceId);
				tskTaskCond.setRefType(TskTask.TASKREFTYPE_IMAGE);
				tskTaskCond.setOrders(new Order[]{new Order(FdrFolderCond.A_MODIFICATIONDATE, true)});
				tskTasks = SwManagerFactory.getInstance().getTskManager().getTasks(userId, tskTaskCond, IManager.LEVEL_LITE);

				if(!CommonUtil.isEmpty(tskTasks)) {
					String prevMonthString = "";
					imageCategoryInfos = new ImageCategoryInfo[tskTasks.length];
					int i=-1;
					int count = 0;
					for(TskTask tskTask : tskTasks) {
						String taskInstId = tskTask.getObjId();
						IFileModel[] fileModels = SwManagerFactory.getInstance().getDocManager().getFilesByTaskInstId(taskInstId);
						if(!CommonUtil.isEmpty(fileModels)) {
							IFileModel fileModel = fileModels[0];
							ImageCategoryInfo imageCategoryInfo = new ImageCategoryInfo();
							String monthString = new LocalDate(fileModel.getWrittenTime().getTime()).toLocalMonthString();
							fileId = "";
							originImgSrc = "";
							imgSrc = "";
							imageInstanceInfo = new ImageInstanceInfo();
							if(!CommonUtil.isEmpty(fileModels)) {
								fileId = fileModel.getId();
								if(fileModel != null) {
									String filePath = fileModel.getFilePath();
									String extension = filePath.lastIndexOf(".") > 1 ? filePath.substring(filePath.lastIndexOf(".")) : null;
									filePath = StringUtils.replace(filePath, "\\", "/");
									if(filePath.indexOf(companyId) != -1)
										originImgSrc = Community.PICTURE_PATH + filePath.substring(filePath.indexOf(companyId), filePath.length());
									filePath = filePath.replaceAll(extension, "_thumb" + extension);
									if(filePath.indexOf(companyId) != -1)
										imgSrc = Community.PICTURE_PATH + filePath.substring(filePath.indexOf(companyId), filePath.length());
								}
							}
							if(prevMonthString.equals(monthString)) {
								count = count + 1;
								imageCategoryInfo.setId(monthString);
								imageCategoryInfo.setName(monthString);
								imageCategoryInfo.setLength(count);
								imageInstanceInfo.setFileId(fileId);
								imageInstanceInfo.setOriginImgSource(originImgSrc);
								imageInstanceInfo.setImgSource(imgSrc);
								imageCategoryInfo.setFirstImage(imageInstanceInfo);
								imageCategoryInfos[i] = imageCategoryInfo;
							} else {
								i = i+1;
								count = 1;
								imageCategoryInfo.setId(monthString);
								imageCategoryInfo.setName(monthString);
								imageCategoryInfo.setLength(count);
								imageInstanceInfo.setFileId(fileId);
								imageInstanceInfo.setOriginImgSource(originImgSrc);
								imageInstanceInfo.setImgSource(imgSrc);
								imageCategoryInfo.setFirstImage(imageInstanceInfo);
								imageCategoryInfos[i] = imageCategoryInfo;
								prevMonthString = monthString;
							}
						}
					}
				}
				for(ImageCategoryInfo imageCategoryInfo : imageCategoryInfos) {
					if(imageCategoryInfo != null) {
						imageCategoryInfoList.add(imageCategoryInfo);
					}
				}

				if(imageCategoryInfoList.size() == 0)
					imageCategoryInfos = null;
				if(imageCategoryInfoList.size() > 0) {
					Collections.sort(imageCategoryInfoList, Collections.reverseOrder());
					imageCategoryInfos = new ImageCategoryInfo[imageCategoryInfoList.size()];
					imageCategoryInfoList.toArray(imageCategoryInfos);
				}
				return imageCategoryInfos;*/
			case FileCategory.DISPLAY_BY_OWNER:
				if(!CommonUtil.isEmpty(fileWorks)) {
					for(FileWork fileWork : fileWorks) {
						int length = 1;
						ImageCategoryInfo imageCategoryInfo = new ImageCategoryInfo();
						imageInstanceInfo = new ImageInstanceInfo();
						String ownerId = fileWork.getTskAssignee();
						String ownerName = getUserInfoByUserId(ownerId).getLongName();
						imageCategoryInfo.setId(ownerId);
						imageCategoryInfo.setName(ownerName);
						fileId = fileWork.getFileId();
						IFileModel fileModel = getDocManager().getFileById(fileId);
						if(fileModel != null) {
							String filePath = fileModel.getFilePath();
							String extension = filePath.lastIndexOf(".") > 1 ? filePath.substring(filePath.lastIndexOf(".")) : null;
							filePath = StringUtils.replace(filePath, "\\", "/");
							if(filePath.indexOf(companyId) != -1)
								originImgSrc = Community.PICTURE_PATH + filePath.substring(filePath.indexOf(companyId), filePath.length());
							filePath = filePath.replaceAll(extension, Community.IMAGE_TYPE_THUMB + extension);
							if(filePath.indexOf(companyId) != -1)
								imgSrc = Community.PICTURE_PATH + filePath.substring(filePath.indexOf(companyId), filePath.length());
						}
						Map<String, String> fileMap = new LinkedHashMap<String, String>();
						List<Map<String, String>> fileList = new ArrayList<Map<String,String>>();
						String fileName = fileModel.getFileName();
						String fileType = fileModel.getType();
						String fileSize = String.valueOf(fileModel.getFileSize());
						fileMap.put("fileId", fileId);
						fileMap.put("fileName", fileName);
						fileMap.put("fileType", fileType);
						fileMap.put("fileSize", fileSize);
						fileList.add(fileMap);
						if(fileList.size() > 0)
							imageInstanceInfo.setFiles(fileList);
						imageInstanceInfo.setFileId(fileId);
						imageInstanceInfo.setOriginImgSource(originImgSrc);
						imageInstanceInfo.setImgSource(imgSrc);
						imageCategoryInfo.setFirstImage(imageInstanceInfo);
						if(!CommonUtil.isEmpty(imageCategoryMap)) {
							for(Map.Entry<String, ImageCategoryInfo> entry : imageCategoryMap.entrySet()) {
								if(entry.getKey().equals(imageCategoryInfo.getId()))
									length = entry.getValue().getLength() + 1;
							}
						}
						imageCategoryInfo.setLength(length);
						imageCategoryMap.put(ownerId, imageCategoryInfo);
					}
				}
				if(!CommonUtil.isEmpty(imageCategoryMap)) {
					for(Map.Entry<String, ImageCategoryInfo> entry : imageCategoryMap.entrySet()) {
						ImageCategoryInfo imageCategoryInfo = entry.getValue();
						imageCategoryInfoList.add(imageCategoryInfo);
					}
				}
				if(imageCategoryInfoList.size() > 0) {
					Collections.sort(imageCategoryInfoList, Collections.reverseOrder());
					imageCategoryInfos = new ImageCategoryInfo[imageCategoryInfoList.size()];
					imageCategoryInfoList.toArray(imageCategoryInfos);
				}
				return imageCategoryInfos;
				/*tskTaskCond = new TskTaskCond();
				tskTaskCond.setWorkSpaceId(spaceId);
				tskTaskCond.setRefType(TskTask.TASKREFTYPE_IMAGE);
				tskTaskCond.setOrders(new Order[]{new Order(FdrFolderCond.A_MODIFICATIONDATE, true)});
				tskTasks = SwManagerFactory.getInstance().getTskManager().getTasks(userId, tskTaskCond, IManager.LEVEL_LITE);
				if(!CommonUtil.isEmpty(tskTasks)) {
					String prevOwner = "";
					imageCategoryInfos = new ImageCategoryInfo[tskTasks.length];
					int i=-1;
					int count = 0;
					for(TskTask tskTask : tskTasks) {
						String taskInstId = tskTask.getObjId();
						String ownerId = tskTask.getCreationUser();
						UserInfo owner = getUserInfoByUserId(ownerId);
						String ownerName = owner.getName();
						IFileModel[] fileModels = SwManagerFactory.getInstance().getDocManager().getFilesByTaskInstId(taskInstId);
						if(!CommonUtil.isEmpty(fileModels)) {
							IFileModel fileModel = fileModels[0];
							ImageCategoryInfo imageCategoryInfo = new ImageCategoryInfo();
							fileId = "";
							originImgSrc = "";
							imgSrc = "";
							imageInstanceInfo = new ImageInstanceInfo();
							if(!CommonUtil.isEmpty(fileModels)) {
								fileId = fileModel.getId();
								if(fileModel != null) {
									String filePath = fileModel.getFilePath();
									String extension = filePath.lastIndexOf(".") > 1 ? filePath.substring(filePath.lastIndexOf(".")) : null;
									filePath = StringUtils.replace(filePath, "\\", "/");
									if(filePath.indexOf(companyId) != -1)
										originImgSrc = Community.PICTURE_PATH + filePath.substring(filePath.indexOf(companyId), filePath.length());
									filePath = filePath.replaceAll(extension, "_thumb" + extension);
									if(filePath.indexOf(companyId) != -1)
										imgSrc = Community.PICTURE_PATH + filePath.substring(filePath.indexOf(companyId), filePath.length());
								}
							}
							if(prevOwner.equals(ownerId)) {
								count = count + 1;
								imageCategoryInfo.setId(ownerId);
								imageCategoryInfo.setName(ownerName);
								imageCategoryInfo.setLength(count);
								imageInstanceInfo.setFileId(fileId);
								imageInstanceInfo.setOriginImgSource(originImgSrc);
								imageInstanceInfo.setImgSource(imgSrc);
								imageCategoryInfo.setFirstImage(imageInstanceInfo);
								imageCategoryInfos[i] = imageCategoryInfo;
							} else {
								i = i+1;
								count = 1;
								imageCategoryInfo.setId(ownerId);
								imageCategoryInfo.setName(ownerName);
								imageCategoryInfo.setLength(count);
								imageInstanceInfo.setFileId(fileId);
								imageInstanceInfo.setOriginImgSource(originImgSrc);
								imageInstanceInfo.setImgSource(imgSrc);
								imageCategoryInfo.setFirstImage(imageInstanceInfo);
								imageCategoryInfos[i] = imageCategoryInfo;
								prevOwner = ownerId;
							}
						}
					}
				}
				for(ImageCategoryInfo imageCategoryInfo : imageCategoryInfos) {
					if(imageCategoryInfo != null) {
						imageCategoryInfoList.add(imageCategoryInfo);
					}
				}
				if(imageCategoryInfoList.size() > 0) {
					imageCategoryInfos = new ImageCategoryInfo[imageCategoryInfoList.size()];
					imageCategoryInfoList.toArray(imageCategoryInfos);
				}
				return imageCategoryInfos;*/
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	public static WorkInstanceInfo[] getWorkInstanceInfosByFileWorks(FileWork[] tasks, String tskRefType, int displayBy) throws Exception {
		if(CommonUtil.isEmpty(tasks))
			return null;
		int tasksLength = tasks.length;
		WorkInstanceInfo[] workInstanceInfos = new WorkInstanceInfo[tasksLength];
		for(int i=0; i<tasksLength; i++) {
			FileWork task = tasks[i];
			workInstanceInfos[i] = getWorkInstanceInfoByFileWork(task, tskRefType, displayBy);
		}
		return workInstanceInfos;
	}

	public static WorkInstanceInfo getWorkInstanceInfoByFileWork(FileWork task, String tskRefType, int displayBy) throws Exception {
		if (task == null)
			return null;
		User cUser = SmartUtil.getCurrentUser();
		String companyId = cUser.getCompanyId();

		WorkInstanceInfo workInstanceInfo = null;

		if(tskRefType.equals(TskTask.TASKREFTYPE_IMAGE)) {
			ImageInstanceInfo tempWorkInstanceInfo = new ImageInstanceInfo();
			tempWorkInstanceInfo.setType(Instance.TYPE_IMAGE);
			FileCategoryInfo fileCategoryInfo = new FileCategoryInfo();
			String categoryId = "";
			String categoryName = "";
			if(displayBy != 0) {
				switch (displayBy) {
				case FileCategory.DISPLAY_BY_CATEGORY:
					categoryId = task.getFolderId();
					categoryName = task.getFolderName();
					if(CommonUtil.isEmpty(categoryId)) {
						categoryId = FileCategory.ID_UNCATEGORIZED;
						categoryName = SmartMessage.getString("common.title.uncategorized");
					}
					break;
				case FileCategory.DISPLAY_BY_YEAR:
					categoryId = new LocalDate(task.getWrittenTime().getTime()).toLocalMonthString();
					categoryName = new LocalDate(task.getWrittenTime().getTime()).toLocalMonthString();
					break;
				case FileCategory.DISPLAY_BY_OWNER:
					categoryId = task.getTskAssignee();
					categoryName = getUserInfoByUserId(task.getTskAssignee()).getLongName();
					break;
				}
				fileCategoryInfo.setId(categoryId);
				fileCategoryInfo.setName(categoryName);
				tempWorkInstanceInfo.setFileCategory(fileCategoryInfo);
				tempWorkInstanceInfo.setLastModifiedDate(new LocalDate(task.getWrittenTime().getTime()));
			}
			String fileId = task.getFileId();
			String originImgSrc = "";
			String imgSrc = "";
			if(!CommonUtil.isEmpty(fileId)) {
				IFileModel fileModel = getDocManager().getFileById(fileId);
				if(fileModel != null) {
					String filePath = fileModel.getFilePath();
					String extension = filePath.lastIndexOf(".") > 1 ? filePath.substring(filePath.lastIndexOf(".")) : null;
					filePath = StringUtils.replace(filePath, "\\", "/");
					if(filePath.indexOf(companyId) != -1)
						originImgSrc = Community.PICTURE_PATH + filePath.substring(filePath.indexOf(companyId), filePath.length());
					filePath = filePath.replaceAll(extension, Community.IMAGE_TYPE_THUMB + extension);
					if(filePath.indexOf(companyId) != -1)
						imgSrc = Community.PICTURE_PATH + filePath.substring(filePath.indexOf(companyId), filePath.length());
				}
			}
			Map<String, String> fileMap = new LinkedHashMap<String, String>();
			List<Map<String, String>> fileList = new ArrayList<Map<String,String>>();
			String fileName = task.getFileName();
			String fileType = task.getFileType();
			String fileSize = String.valueOf(task.getFileSize());
			fileMap.put("fileId", fileId);
			fileMap.put("fileName", fileName);
			fileMap.put("fileType", fileType);
			fileMap.put("fileSize", fileSize);
			fileList.add(fileMap);
			if(fileList.size() > 0)
				tempWorkInstanceInfo.setFiles(fileList);
			tempWorkInstanceInfo.setFileId(fileId);
			tempWorkInstanceInfo.setOriginImgSource(originImgSrc);
			tempWorkInstanceInfo.setImgSource(imgSrc);
			workInstanceInfo = tempWorkInstanceInfo;
		} else {
			FileInstanceInfo tempWorkInstanceInfo = new FileInstanceInfo();
			tempWorkInstanceInfo.setType(Instance.TYPE_FILE);
			FileCategoryInfo fileCategoryInfo = new FileCategoryInfo();
			String categoryId = "";
			String categoryName = "";
			if(displayBy != 0) {
				switch (displayBy) {
				case FileCategory.DISPLAY_BY_CATEGORY:
					categoryId = task.getFolderId();
					categoryName = task.getFolderName();
					if(CommonUtil.isEmpty(categoryId)) {
						categoryId = FileCategory.ID_UNCATEGORIZED;
						categoryName = SmartMessage.getString("common.title.uncategorized");
					}
				case FileCategory.DISPLAY_BY_YEAR:
					categoryId = new LocalDate(task.getWrittenTime().getTime()).toLocalMonthString();
					categoryName = new LocalDate(task.getWrittenTime().getTime()).toLocalMonthString();
				case FileCategory.DISPLAY_BY_OWNER:
					categoryId = task.getTskAssignee();
					categoryName = getUserInfoByUserId(task.getTskAssignee()).getLongName(); 
				}
				fileCategoryInfo.setId(categoryId);
				fileCategoryInfo.setName(categoryName);
				tempWorkInstanceInfo.setFileCategory(fileCategoryInfo);
			}
			Map<String, String> fileMap = new LinkedHashMap<String, String>();
			List<Map<String, String>> fileList = new ArrayList<Map<String,String>>();
			String fileGroupId = task.getGroupId();
			String fileId = task.getFileId();
			String fileName = task.getFileName();
			String fileType = task.getFileType();
			String fileSize = String.valueOf(task.getFileSize());
			fileMap.put("fileId", fileId);
			fileMap.put("fileName", fileName);
			fileMap.put("fileType", fileType);
			fileMap.put("fileSize", fileSize);
			fileList.add(fileMap);
			if(fileList.size() > 0)
				tempWorkInstanceInfo.setFiles(fileList);
			tempWorkInstanceInfo.setFileGroupId(fileGroupId);
			workInstanceInfo = new WorkInstanceInfo();
			workInstanceInfo.setSubject(StringUtil.subString(task.getPrcTitle(), 0, 30, "..."));
			tempWorkInstanceInfo.setWorkInstance(workInstanceInfo);
			workInstanceInfo = tempWorkInstanceInfo;
		}

		SmartWorkInfo workInfo = new SmartWorkInfo();
		workInfo.setId(task.getPackageId());
		workInfo.setName(task.getPackageName());
		String packageStatus = task.getPackageStatus();
		boolean isRunning = false;
		boolean isEditing = false;
		if (packageStatus != null) {
			if(PkgPackage.STATUS_DEPLOYED.equalsIgnoreCase(packageStatus)) {
				isRunning = true;
			} else if(PkgPackage.STATUS_UNDER_DEVEL.equalsIgnoreCase(packageStatus)) {
				isEditing = true;
			}
		}
		workInfo.setRunning(isRunning);
		workInfo.setEditing(isEditing);
		/*TYPE_INFORMATION = 21;
		TYPE_PROCESS = 22;
		TYPE_SCHEDULE = 23;*/
		if (task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_COMMON)) {
			workInfo.setType(SmartWork.TYPE_PROCESS);
		} else if (task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_SINGLE)) {
			workInfo.setType(SmartWork.TYPE_INFORMATION);
		}
		if (task.getParentCtgId() != null) {
			workInfo.setMyCategory(new WorkCategoryInfo(task.getParentCtgId(), task.getParentCtgName()));
			workInfo.setMyGroup(new WorkCategoryInfo(task.getChildCtgId(), task.getChildCtgName()));
		} else {
			workInfo.setMyCategory(new WorkCategoryInfo(task.getChildCtgId(), task.getChildCtgName()));
		}
		TaskInstanceInfo lastTask = new TaskInstanceInfo();
		lastTask.setId(task.getLastTskObjId());
		lastTask.setName(task.getLastTskName());
		if (task.getLastTskType().equalsIgnoreCase(TskTask.TASKTYPE_APPROVAL)) {
			lastTask.setTaskType(TaskInstance.TYPE_APPROVAL_TASK_ASSIGNED);
		} else if (task.getLastTskType().equalsIgnoreCase(TskTask.TASKTYPE_COMMON)) {
			lastTask.setTaskType(TaskInstance.TYPE_PROCESS_TASK_ASSIGNED);
		} else if (task.getLastTskType().equalsIgnoreCase(TskTask.TASKTYPE_REFERENCE)) {
			lastTask.setTaskType(TaskInstance.TYPE_INFORMATION_TASK_FORWARDED);
		} else if (task.getLastTskType().equalsIgnoreCase(TskTask.TASKTYPE_SINGLE)) {
			lastTask.setTaskType(TaskInstance.TYPE_INFORMATION_TASK_ASSIGNED);
		}
		lastTask.setWorkInstance(workInstanceInfo);
		lastTask.setAssignee(getUserInfoByUserId(task.getLastTskAssignee()));
		lastTask.setPerformer(getUserInfoByUserId(task.getLastTskAssignee()));
		lastTask.setSubject(StringUtil.subString(task.getPrcTitle(), 0, 30, "..."));
		lastTask.setWorkInfo(workInfo);
		lastTask.setWorkSpaceInfo(getWorkSpaceInfo(task.getLastTskWorkSpaceType(), task.getLastTskWorkSpaceId()));
		
		if (task.getLastTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ASSIGN)) {
			if(task.getTskExpectEndDate()!=null && task.getTskExpectEndDate().getTime()<(new LocalDate()).getTime()) {
				lastTask.setStatus(TaskInstance.STATUS_DELAYED_RUNNING);
			} else {
				lastTask.setStatus(TaskInstance.STATUS_RUNNING);
			}
		} else if (task.getLastTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_COMPLETE)) {
			lastTask.setStatus(TaskInstance.STATUS_COMPLETED);
		} else if (task.getLastTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_CANCEL)) {
			lastTask.setStatus(TaskInstance.STATUS_REJECTED);
		} else if (task.getLastTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ABORTED)) {
			lastTask.setStatus(TaskInstance.STATUS_ABORTED);
		} else if (task.getLastTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_RETURNED)) {
			lastTask.setStatus(TaskInstance.STATUS_RETURNED);
		}
		
		lastTask.setOwner(getUserInfoByUserId(task.getLastTskAssignee()));
		lastTask.setLastModifiedDate(new LocalDate( task.getLastTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ASSIGN) ? task.getLastTskCreateDate().getTime() : task.getLastTskExecuteDate().getTime()));
		lastTask.setLastModifier(getUserInfoByUserId(task.getLastTskAssignee()));
		
		workInstanceInfo.setLastTask(lastTask);
		workInstanceInfo.setLastTaskCount(task.getLastTskCount());
		if (task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_COMMON)) {
			workInstanceInfo.setId(task.getPrcObjId());
		} else if (task.getTskType().equalsIgnoreCase(TskTask.TASKTYPE_SINGLE)) {
			String singleWorkInfos = task.getTskDef();
			String swdRecordId = null;
			String domainId = null;
			if (!CommonUtil.isEmpty(singleWorkInfos)) {
				String[] singleWorkInfo = StringUtils.tokenizeToStringArray(singleWorkInfos, "|");	
				domainId = singleWorkInfo[0];
				swdRecordId = singleWorkInfo[1];
			}
			workInstanceInfo.setId(swdRecordId);
		}
		workInstanceInfo.setSubject(StringUtil.subString(task.getPrcTitle(), 0, 30, "..."));
		workInstanceInfo.setWorkInfo(workInfo);
		workInstanceInfo.setWorkSpaceInfo(getWorkSpaceInfo(task.getPrcWorkSpaceType(), task.getPrcWorkSpaceId()));
		
		if (task.getTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ASSIGN)) {
			if(task.getTskExpectEndDate()!=null && task.getTskExpectEndDate().getTime()<(new LocalDate()).getTime()) {
				workInstanceInfo.setStatus(TaskInstance.STATUS_DELAYED_RUNNING);
			} else {
				workInstanceInfo.setStatus(TaskInstance.STATUS_RUNNING);
			}
		} else if (task.getTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_COMPLETE)) {
			workInstanceInfo.setStatus(TaskInstance.STATUS_COMPLETED);
		} else if (task.getTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_CANCEL)) {
			workInstanceInfo.setStatus(TaskInstance.STATUS_REJECTED);
		} else if (task.getTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_ABORTED)) {
			workInstanceInfo.setStatus(TaskInstance.STATUS_ABORTED);
		} else if (task.getTskStatus().equalsIgnoreCase(TskTask.TASKSTATUS_RETURNED)) {
			workInstanceInfo.setStatus(TaskInstance.STATUS_RETURNED);
		}
		workInstanceInfo.setSubInstanceCount(instanceService.getSubInstanceCountInInstance(workInstanceInfo.getId()));
		workInstanceInfo.setOwner(getUserInfoByUserId(task.getTskAssignee()));
		workInstanceInfo.setCreatedDate(new LocalDate(task.getTskCreateDate().getTime()));
		workInstanceInfo.setLastModifiedDate(new LocalDate(task.getTaskLastModifyDate().getTime()));
		workInstanceInfo.setLastModifier(getUserInfoByUserId(task.getTskAssignee()));
		
		return workInstanceInfo;
	}

	public static FileCategoryInfo[] getFileCategoriesByType(int displayType, String spaceId, String parentId) throws Exception {
		User cUser = SmartUtil.getCurrentUser();
		String userId = cUser.getId();
		FileCategoryInfo[] fileCategoryInfos = null;
		List<FileCategoryInfo> fileCategoryInfoList = new ArrayList<FileCategoryInfo>();
		Map<String, FileCategoryInfo> fileCategoryMap = new LinkedHashMap<String, FileCategoryInfo>();

		FileWorkCond fileWorkCond = new FileWorkCond();
		fileWorkCond.setTskAssigneeOrSpaceId(spaceId);
		fileWorkCond.setOrders(new Order[]{new Order("tskCreatedate", false)});
		FileWork[] fileWorks = getDocManager().getFileWorkList(userId, fileWorkCond);
		if(!CommonUtil.isEmpty(fileWorks)) {
			FileCategoryInfo allFileCategoryInfo = new FileCategoryInfo();
			allFileCategoryInfo.setId(FileCategory.ID_ALL_FILES);
			allFileCategoryInfo.setName(FileCategory.NAME_ALL_FILES);
			allFileCategoryInfo.setLength(fileWorks.length);
			fileCategoryMap.put(FileCategory.ID_ALL_FILES, allFileCategoryInfo);
			switch (displayType) {
			case FileCategory.DISPLAY_BY_CATEGORY:
				FdrFolderCond fdrFolderCond = new FdrFolderCond();
				fdrFolderCond.setCreationUser(userId);
				fdrFolderCond.setWorkspaceId(spaceId);
				fdrFolderCond.setOrders(new Order[]{new Order(FdrFolderCond.A_DISPLAYORDER, true)});
				FdrFolder[] fdrFolders = getFdrManager().getFolders(userId, fdrFolderCond, IManager.LEVEL_ALL);
				if(!CommonUtil.isEmpty(fileWorks)) {
					for(FileWork fileWork : fileWorks) {
						boolean isAccessableForMe = isAccessableInstance(fileWork);
						if(isAccessableForMe) {
							int length = 1;
							FileCategoryInfo fileCategoryInfo = new FileCategoryInfo();
							String folderId = CommonUtil.toNotNull(fileWork.getFolderId()).equals("") ? FileCategory.ID_UNCATEGORIZED : fileWork.getFolderId();
							String folderName = CommonUtil.toNotNull(fileWork.getFolderName()).equals("") ? FileCategory.NAME_UNCATEGORIZED : fileWork.getFolderName();
							fileCategoryInfo.setId(folderId);
							fileCategoryInfo.setName(folderName);
							if(!CommonUtil.isEmpty(fileCategoryMap)) {
								for(Map.Entry<String, FileCategoryInfo> entry : fileCategoryMap.entrySet()) {
									if(entry.getKey().equals(fileCategoryInfo.getId()))
										length = entry.getValue().getLength() + 1;
								}
							}
							fileCategoryInfo.setLength(length);
							fileCategoryMap.put(folderId, fileCategoryInfo);
						}
					}
				}
				if(!CommonUtil.isEmpty(fdrFolders)) {
					for(FdrFolder fdrFolder : fdrFolders) {
						if(CommonUtil.isEmpty(fdrFolder.getFolderFiles())) {
							FileCategoryInfo fileCategoryInfo = new FileCategoryInfo();
							String folderId = fdrFolder.getObjId();
							String folderName = fdrFolder.getName();
							int length = 0;
							fileCategoryInfo.setId(folderId);
							fileCategoryInfo.setName(folderName);
							fileCategoryInfo.setLength(length);
							fileCategoryMap.put(folderId, fileCategoryInfo);
						}
					}
				}
				if(!CommonUtil.isEmpty(fileCategoryMap)) {
					for(Map.Entry<String, FileCategoryInfo> entry : fileCategoryMap.entrySet()) {
						FileCategoryInfo fileCategoryInfo = entry.getValue();
						fileCategoryInfoList.add(fileCategoryInfo);
					}
				}
				if(fileCategoryInfoList.size() > 0) {
					fileCategoryInfos = new FileCategoryInfo[fileCategoryInfoList.size()];
					fileCategoryInfoList.toArray(fileCategoryInfos);
				}
				return fileCategoryInfos;
			case FileCategory.DISPLAY_BY_WORK:
				if(!CommonUtil.isEmpty(fileWorks)) {
					for(FileWork fileWork : fileWorks) {
						int length = 1;
						FileCategoryInfo fileCategoryInfo = new FileCategoryInfo();
						String workId = fileWork.getPackageId();
						String workName = fileWork.getPackageName();
						fileCategoryInfo.setId(workId);
						fileCategoryInfo.setName(workName);
						if(!CommonUtil.isEmpty(fileCategoryMap)) {
							for(Map.Entry<String, FileCategoryInfo> entry : fileCategoryMap.entrySet()) {
								if(entry.getKey().equals(fileCategoryInfo.getId()))
									length = entry.getValue().getLength() + 1;
							}
						}
						fileCategoryInfo.setLength(length);
						fileCategoryMap.put(workId, fileCategoryInfo);
					}
				}
				if(!CommonUtil.isEmpty(fileCategoryMap)) {
					for(Map.Entry<String, FileCategoryInfo> entry : fileCategoryMap.entrySet()) {
						FileCategoryInfo fileCategoryInfo = entry.getValue();
						fileCategoryInfoList.add(fileCategoryInfo);
					}
				}
				if(fileCategoryInfoList.size() > 0) {
					fileCategoryInfos = new FileCategoryInfo[fileCategoryInfoList.size()];
					fileCategoryInfoList.toArray(fileCategoryInfos);
				}
				return fileCategoryInfos;
			case FileCategory.DISPLAY_BY_YEAR:
				if(!CommonUtil.isEmpty(fileWorks)) {
					for(FileWork fileWork : fileWorks) {
						int length = 1;
						FileCategoryInfo fileCategoryInfo = new FileCategoryInfo();
						String writtenId = new LocalDate(fileWork.getWrittenTime().getTime()).toLocalMonthString();
						String writtenName = new LocalDate(fileWork.getWrittenTime().getTime()).toLocalMonthString();
						fileCategoryInfo.setId(writtenId);
						fileCategoryInfo.setName(writtenName);
						if(!CommonUtil.isEmpty(fileCategoryMap)) {
							for(Map.Entry<String, FileCategoryInfo> entry : fileCategoryMap.entrySet()) {
								if(entry.getKey().equals(fileCategoryInfo.getId()))
									length = entry.getValue().getLength() + 1;
							}
						}
						fileCategoryInfo.setLength(length);
						fileCategoryMap.put(writtenId, fileCategoryInfo);
					}
				}
				if(!CommonUtil.isEmpty(fileCategoryMap)) {
					for(Map.Entry<String, FileCategoryInfo> entry : fileCategoryMap.entrySet()) {
						FileCategoryInfo fileCategoryInfo = entry.getValue();
						fileCategoryInfoList.add(fileCategoryInfo);
					}
				}
				if(fileCategoryInfoList.size() > 0) {
					fileCategoryInfos = new FileCategoryInfo[fileCategoryInfoList.size()];
					fileCategoryInfoList.toArray(fileCategoryInfos);
				}
				return fileCategoryInfos;
			case FileCategory.DISPLAY_BY_OWNER:
				if(!CommonUtil.isEmpty(fileWorks)) {
					for(FileWork fileWork : fileWorks) {
						int length = 1;
						FileCategoryInfo fileCategoryInfo = new FileCategoryInfo();
						String ownerId = fileWork.getTskAssignee();
						String ownerName = getUserInfoByUserId(ownerId).getLongName();
						fileCategoryInfo.setId(ownerId);
						fileCategoryInfo.setName(ownerName);
						if(!CommonUtil.isEmpty(fileCategoryMap)) {
							for(Map.Entry<String, FileCategoryInfo> entry : fileCategoryMap.entrySet()) {
								if(entry.getKey().equals(fileCategoryInfo.getId()))
									length = entry.getValue().getLength() + 1;
							}
						}
						fileCategoryInfo.setLength(length);
						fileCategoryMap.put(ownerId, fileCategoryInfo);
					}
				}
				if(!CommonUtil.isEmpty(fileCategoryMap)) {
					for(Map.Entry<String, FileCategoryInfo> entry : fileCategoryMap.entrySet()) {
						FileCategoryInfo fileCategoryInfo = entry.getValue();
						fileCategoryInfoList.add(fileCategoryInfo);
					}
				}
				if(fileCategoryInfoList.size() > 0) {
					fileCategoryInfos = new FileCategoryInfo[fileCategoryInfoList.size()];
					fileCategoryInfoList.toArray(fileCategoryInfos);
				}
				return fileCategoryInfos;
			case FileCategory.DISPLAY_BY_FILE_TYPE:
				if(!CommonUtil.isEmpty(fileWorks)) {
					for(FileWork fileWork : fileWorks) {
						int length = 1;
						FileCategoryInfo fileCategoryInfo = new FileCategoryInfo();
						String typeId = fileWork.getFileType().toLowerCase();
						String typeName = fileWork.getFileType().toLowerCase();
						fileCategoryInfo.setId(typeId);
						fileCategoryInfo.setName(typeName);
						if(!CommonUtil.isEmpty(fileCategoryMap)) {
							for(Map.Entry<String, FileCategoryInfo> entry : fileCategoryMap.entrySet()) {
								if(entry.getKey().equals(fileCategoryInfo.getId()))
									length = entry.getValue().getLength() + 1;
							}
						}
						fileCategoryInfo.setLength(length);
						fileCategoryMap.put(typeId, fileCategoryInfo);
					}
				}
				if(!CommonUtil.isEmpty(fileCategoryMap)) {
					for(Map.Entry<String, FileCategoryInfo> entry : fileCategoryMap.entrySet()) {
						FileCategoryInfo fileCategoryInfo = entry.getValue();
						fileCategoryInfoList.add(fileCategoryInfo);
					}
				}
				if(fileCategoryInfoList.size() > 0) {
					fileCategoryInfos = new FileCategoryInfo[fileCategoryInfoList.size()];
					fileCategoryInfoList.toArray(fileCategoryInfos);
				}
				return fileCategoryInfos;
			}
		}
		return fileCategoryInfos;
	}
	
	public static TaskInstance getTaskInstanceByTskTask(String userId, WorkInstance processWorkInstObj, TskTask swTask) throws Exception {
		
		if (swTask == null)
			return null;
		
		TaskInstance taskInstance = new TaskInstance();
		
		taskInstance.setId(swTask.getObjId());
		taskInstance.setSubject(swTask.getTitle());
		taskInstance.setName(swTask.getName());

		taskInstance.setAssignee(getUserByUserId(swTask.getAssignee()));
		taskInstance.setAssigner(getUserByUserId(swTask.getAssigner()));

		taskInstance.setCreatedDate(new LocalDate(swTask.getCreationDate().getTime()));
		taskInstance.setLastModifier(getUserByUserId(swTask.getModificationUser()));
		taskInstance.setLastModifiedDate(new LocalDate(swTask.getModificationDate().getTime()));
		
		taskInstance.setStartTask(CommonUtil.toBoolean(swTask.getIsStartActivity()));
		
		String tskStatus = swTask.getStatus();
		if (tskStatus.equalsIgnoreCase("11")) {
			if(swTask.getExpectEndDate()!=null && swTask.getExpectEndDate().getTime()<(new LocalDate()).getTime())
				taskInstance.setStatus(Instance.STATUS_DELAYED_RUNNING);
			else
				taskInstance.setStatus(Instance.STATUS_RUNNING);
		} else if (tskStatus.equals("21")) {
			taskInstance.setStatus(Instance.STATUS_COMPLETED);
			taskInstance.setPerformer(getUserByUserId(swTask.getPerformer()));
		}

		int type = WorkInstance.TYPE_TASK;
		int taskType = 0;
		taskInstance.setType(type);

		String tskType = swTask.getType();
		if(tskType.equals(TskTask.TASKTYPE_SINGLE)) {
			if(tskStatus.equals("11")) {
				taskType = TaskInstance.TYPE_INFORMATION_TASK_ASSIGNED;
			} else if(tskStatus.equals("21")) {
				taskType = TaskInstance.TYPE_INFORMATION_TASK_UPDATED;
			}
		} else if(tskType.equals(TskTask.TASKTYPE_REFERENCE)) {
			String approvalId = swTask.getApprovalId();
			if (!CommonUtil.isEmpty(approvalId)) {
				taskType = TaskInstance.TYPE_APPROVAL_TASK_FORWARDED;
			} else {
				if (processWorkInstObj.getWork() != null && processWorkInstObj.getWork().getType() == ProcessWork.TYPE_PROCESS) {
					taskType = TaskInstance.TYPE_PROCESS_TASK_FORWARDED;
				} else {
					taskType = TaskInstance.TYPE_INFORMATION_TASK_FORWARDED;
				}
			}
			taskInstance.setForwardId(swTask.getForwardId());
			if (!CommonUtil.isEmpty(swTask.getApprovalId()))
				taskInstance.setApprovalId(swTask.getApprovalId());
			taskInstance.setAssigner(getUserByUserId(swTask.getExtendedPropertyValue("processInstCreationUser")));
		} else if(tskType.equals(TskTask.TASKTYPE_APPROVAL)) {
			taskType = TaskInstance.TYPE_APPROVAL_TASK_ASSIGNED;
			taskInstance.setApprovalId(swTask.getApprovalId());
			taskInstance.setAssigner(getUserByUserId(swTask.getExtendedPropertyValue("processInstCreationUser")));
		}
		taskInstance.setTaskType(taskType);

		taskInstance.setOwner(getUserByUserId(swTask.getAssignee()));
		
		taskInstance.setWork(processWorkInstObj.getWork());
		taskInstance.setWorkInstance(processWorkInstObj);

		taskInstance.setWorkSpace(processWorkInstObj.getWorkSpace());
		taskInstance.setAccessPolicy(processWorkInstObj.getAccessPolicy());
		
		String formId = swTask.getForm();
		SwfForm form = SwManagerFactory.getInstance().getSwfManager().getForm(userId, formId);
		
		taskInstance.setSmartForm(getSmartFormBySwfFrom(null, form));
		
		return taskInstance;
	}
	private static SwdRecord setDataFieldsInfo(SwdRecord swdRecord, SwfForm swfForm) throws Exception {
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
	public static SwdRecord getSwdRecordByRequestBody(String userId, SwdField[] swdFields, Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
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
				String autoIndexSelectedValue = null;
				if (fieldValue instanceof LinkedHashMap) {
					Map<String, Object> valueMap = (Map<String, Object>)fieldValue;
					groupId = (String)valueMap.get("groupId");
					refForm = (String)valueMap.get("refForm");
					String autoIndexValue = (String)valueMap.get("value");
					autoIndexSelectedValue = (String)valueMap.get("selectedValue");
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
//							if(!value.isEmpty())
//								value = LocalDate.convertStringToLocalDate(value).toGMTDateString();
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
			
			SwfForm swfForm = SwManagerFactory.getInstance().getSwfManager().getForm(userId, formId);
			setDataFieldsInfo(obj, swfForm);
			
			Map<String, Object> frmAccessSpaceMap = (Map<String, Object>)requestBody.get("frmAccessSpace");
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

				if(String.valueOf(AccessPolicy.LEVEL_CUSTOM).equals(accessLevel) && CommonUtil.isEmpty(accessValue)) {
					accessValue = ModelConverter.getAccessValue(userId, formId);
				}
				obj.setWorkSpaceId(workSpaceId);
				obj.setWorkSpaceType(workSpaceType);
				obj.setAccessLevel(accessLevel);
				obj.setAccessValue(accessValue);
				
				//워크스페이스(workspaceid) 가 비공개 그룹이라면 무조건 비공개 그룹의 선택 공개로 저장이 된다
				populatePrivateGroupAuth(userId, obj);
				
			}
			
			return obj;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}
	public static void populatePrivateGroupAuth(String userId, SwdRecord obj) throws Exception {
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
	
	
	public static ReportInstanceInfo[] getReportInstanceInfoArrayByRptReportArray(String userId, RptReport[] reports) throws Exception {

//		ReportInstanceInfo instance = new ReportInstanceInfo("instance1", "부서별 주간 처리 건수", getUserInfo1(), getUserInfo2(), new LocalDate());
//		instance.setAccessPolicy(new AccessPolicy(AccessPolicy.LEVEL_PUBLIC));
//		instance.setReportType(Report.TYPE_CHART);
//		instance.setChartType(ChartReport.CHART_TYPE_BAR);
//		instance.setTargetWorkType(Work.TYPE_NONE);
//		ReportInstanceInfo instance1 = new ReportInstanceInfo("instance1", "부서별 주간 품의처리 건수", getUserInfo1(), getUserInfo2(), new LocalDate());
//		instance1.setAccessPolicy(new AccessPolicy(AccessPolicy.LEVEL_PRIVATE));
//		instance1.setReportType(Report.TYPE_MATRIX);
//		instance1.setTargetWorkType(SmartWork.TYPE_PROCESS);
//		return new ReportInstanceInfo[]{instance, instance1};
		
		if (CommonUtil.isEmpty(reports))
			return null;
		
		ReportInstanceInfo[] reportInstanceInfos = new ReportInstanceInfo[reports.length];
		for (int i = 0; i < reports.length; i++) {
			RptReport report = reports[i];
			UserInfo owner = getUserInfoByUserId(report.getOwner());
			UserInfo lastModifier = report.getOwner().equalsIgnoreCase(report.getModificationUser()) ? owner : getUserInfoByUserId(report.getModificationUser());
			ReportInstanceInfo instance = new ReportInstanceInfo(report.getObjId(), report.getName(), owner, lastModifier, new LocalDate(report.getModificationDate().getTime()));
			instance.setReportType(report.getType());
			instance.setChartType(report.getChartType());
			instance.setTargetWorkType(report.getTargetWorkType());
			instance.setAccessPolicy(new AccessPolicy(Integer.parseInt(report.getAccessLevel())));
			reportInstanceInfos[i] = instance;
		}
		return reportInstanceInfos;
	}
	
	
}
