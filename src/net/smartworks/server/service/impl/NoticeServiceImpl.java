/*	
 * $Id$
 * created by    : SHIN HYUN SEONG
 * creation-date : 2011. 10. 31.
 * =========================================================
 * Copyright (c) 2011 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.smartworks.model.community.User;
import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.instance.CommentInstance;
import net.smartworks.model.instance.Instance;
import net.smartworks.model.instance.SortingField;
import net.smartworks.model.instance.info.AsyncMessageInstanceInfo;
import net.smartworks.model.instance.info.CommentInstanceInfo;
import net.smartworks.model.instance.info.InstanceInfo;
import net.smartworks.model.instance.info.InstanceInfoList;
import net.smartworks.model.instance.info.RequestParams;
import net.smartworks.model.instance.info.WorkInstanceInfo;
import net.smartworks.model.mail.MailFolder;
import net.smartworks.model.notice.Notice;
import net.smartworks.model.notice.NoticeBox;
import net.smartworks.model.notice.NoticeMessage;
import net.smartworks.model.sera.FriendInformList;
import net.smartworks.model.sera.SeraNotice;
import net.smartworks.model.sera.info.SeraUserInfo;
import net.smartworks.server.engine.common.manager.IManager;
import net.smartworks.server.engine.common.model.Order;
import net.smartworks.server.engine.common.model.Property;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.engine.infowork.domain.model.SwdRecordCond;
import net.smartworks.server.engine.mail.model.MailContent;
import net.smartworks.server.engine.message.manager.IMessageManager;
import net.smartworks.server.engine.message.model.Message;
import net.smartworks.server.engine.message.model.MessageCond;
import net.smartworks.server.engine.opinion.manager.IOpinionManager;
import net.smartworks.server.engine.opinion.model.Opinion;
import net.smartworks.server.engine.opinion.model.OpinionCond;
import net.smartworks.server.engine.organization.manager.ISwoManager;
import net.smartworks.server.engine.organization.model.SwoGroup;
import net.smartworks.server.engine.organization.model.SwoGroupCond;
import net.smartworks.server.engine.organization.model.SwoGroupMember;
import net.smartworks.server.engine.process.process.manager.IPrcManager;
import net.smartworks.server.engine.process.process.model.PrcProcessInst;
import net.smartworks.server.engine.process.process.model.PrcProcessInstCond;
import net.smartworks.server.engine.process.task.manager.ITskManager;
import net.smartworks.server.engine.process.task.model.TskTask;
import net.smartworks.server.engine.process.task.model.TskTaskCond;
import net.smartworks.server.engine.publishnotice.manager.IPublishNoticeManager;
import net.smartworks.server.engine.publishnotice.model.PublishNotice;
import net.smartworks.server.engine.publishnotice.model.PublishNoticeCond;
import net.smartworks.server.engine.worklist.manager.IWorkListManager;
import net.smartworks.server.engine.worklist.model.TaskWork;
import net.smartworks.server.engine.worklist.model.TaskWorkCond;
import net.smartworks.server.service.IMailService;
import net.smartworks.server.service.INoticeService;
import net.smartworks.server.service.ISeraService;
import net.smartworks.server.service.factory.SwServiceFactory;
import net.smartworks.server.service.util.ModelConverter;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartTest;
import net.smartworks.util.SmartUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class NoticeServiceImpl implements INoticeService {

	private static IPublishNoticeManager getPublishNoticeManager() {
		return SwManagerFactory.getInstance().getPublishNoticeManager();
	}
	private static IMessageManager getMessageManager() {
		return SwManagerFactory.getInstance().getMessageManager();
	}
	private static ITskManager getTskManager() {
		return SwManagerFactory.getInstance().getTskManager();
	}
	private static IPrcManager getPrcManager() {
		return SwManagerFactory.getInstance().getPrcManager();
	}
	private static IOpinionManager getOpinionManager() {
		return SwManagerFactory.getInstance().getOpinionManager();
	}
	private static IWorkListManager getWorkListManager() {
		return SwManagerFactory.getInstance().getWorkListManager();
	}
	private static ISwoManager getSwoManager() {
		return SwManagerFactory.getInstance().getSwoManager();
	}

	@Autowired
	private IMailService mailService;
	@Autowired
	private ISeraService seraService;

	public Notice[] getNotices(String userId, int noticeType) throws Exception {

		Notice message = null;
		Notice comment = null;
		Notice assigned = null;
		Notice notificationMessage = null;
		Notice mailBox = null;
		Notice savedBox = null;
		Notice friend = null;

		//---------------------------------------------------------------------------------------
		if (noticeType == Notice.TYPE_MESSAGE || noticeType == Notice.TYPE_INVALID) {
			message = new Notice();
			message.setType(Notice.TYPE_MESSAGE);

			PublishNoticeCond messageCond = new PublishNoticeCond(userId, PublishNotice.TYPE_MESSAGE, PublishNotice.REFTYPE_MESSAGE, null);
			long totalMessageSize = getPublishNoticeManager().getPublishNoticeSize(userId, messageCond);
			
			message.setLength((int)totalMessageSize);
		}
		//---------------------------------------------------------------------------------------

		if (noticeType == Notice.TYPE_COMMENT || noticeType == Notice.TYPE_INVALID) {
			
			comment = new Notice();
			comment.setType(Notice.TYPE_COMMENT);
			//내가 작성한 정보관리 업무 + 내가 수행한 태스크가 속해있는 프로세스 인스턴스업무

			PublishNoticeCond commentCond = new PublishNoticeCond(userId, PublishNotice.TYPE_COMMENT, null, null);
			long totalCommentSize = getPublishNoticeManager().getPublishNoticeSize(userId, commentCond);
			
			comment.setLength((int)totalCommentSize);
		}
		
		//---------------------------------------------------------------------------------------

		if (noticeType == Notice.TYPE_ASSIGNED || noticeType == Notice.TYPE_INVALID) {
			assigned = new Notice();
			assigned.setType(Notice.TYPE_ASSIGNED);
			
			PublishNoticeCond assignedCond = new PublishNoticeCond(userId, PublishNotice.TYPE_ASSIGNED, PublishNotice.REFTYPE_ASSIGNED_TASK, null);
			long totalAssignedSize = getPublishNoticeManager().getPublishNoticeSize(userId, assignedCond);
			
			assigned.setLength((int)totalAssignedSize);
		}
		
		
		//---------------------------------------------------------------------------------------

		if (noticeType == Notice.TYPE_NOTIFICATION || noticeType == Notice.TYPE_INVALID) {

			notificationMessage = new Notice();
			notificationMessage.setType(Notice.TYPE_NOTIFICATION);
			
			// TOTAL DELAYED TASK

			PublishNoticeCond delayedTaskCond = new PublishNoticeCond(userId, PublishNotice.TYPE_NOTIFICATION, PublishNotice.REFTYPE_DELAYED_TASK, null);
			long totalDelayedTaskSize = getPublishNoticeManager().getPublishNoticeSize(userId, delayedTaskCond);
			
//			TaskWorkCond delayedTaskCond = new TaskWorkCond();
//			delayedTaskCond.setTskStatus(TskTask.TASKSTATUS_ASSIGN);
//			delayedTaskCond.setTskAssignee(userId);
//			delayedTaskCond.setPrcStatus(PrcProcessInst.PROCESSINSTSTATUS_RUNNING);
//			delayedTaskCond.setExpectEndDateTo(new LocalDate());
//			
//			long totalDelayedTaskSize = SwManagerFactory.getInstance().getWorkListManager().getTaskWorkListSize(userId, delayedTaskCond);
			
			// TOTAL REQUESTER

			PublishNoticeCond requestCond = new PublishNoticeCond(userId, PublishNotice.TYPE_NOTIFICATION, PublishNotice.REFTYPE_GROUPJOINREQUEST, null);
			long totalRequestSize = getPublishNoticeManager().getPublishNoticeSize(userId, requestCond);
			
			notificationMessage.setLength((int)totalDelayedTaskSize + (int)totalRequestSize);

		}
		//---------------------------------------------------------------------------------------

		if (noticeType == Notice.TYPE_MAILBOX || noticeType == Notice.TYPE_INVALID) {
			mailBox = new Notice();
			mailBox.setType(Notice.TYPE_MAILBOX);
			mailBox.setLength(mailService.getUnreadEmails());
		}
		//---------------------------------------------------------------------------------------

		if (noticeType == Notice.TYPE_SAVEDBOX || noticeType == Notice.TYPE_INVALID) {
			savedBox = new Notice();
			savedBox.setType(Notice.TYPE_SAVEDBOX);
			savedBox.setLength(0);
		}

		//---------------------------------------------------------------------------------------

		if (noticeType == SeraNotice.TYPE_FRIEND) {
			friend = new Notice();
			friend.setType(SeraNotice.TYPE_FRIEND);
			SeraUserInfo[] seraUserInfos = seraService.getFriendRequestsByUserId(userId, null, FriendInformList.MAX_ALL_FRIEND_LIST);
			int length = 0;
			if(!CommonUtil.isEmpty(seraUserInfos))
				length = seraUserInfos.length;
			friend.setLength(length);
		}

		//---------------------------------------------------------------------------------------

		Notice[] returnNotice = null;
		switch (noticeType) {
		case Notice.TYPE_MESSAGE:
			returnNotice = new Notice[1];
			returnNotice[0] = message;
			break;
		case Notice.TYPE_COMMENT:
			returnNotice = new Notice[1];
			returnNotice[0] = comment;
			break;
		case Notice.TYPE_ASSIGNED:
			returnNotice = new Notice[1];
			returnNotice[0] = assigned;
			break;
		case Notice.TYPE_NOTIFICATION:
			returnNotice = new Notice[1];
			returnNotice[0] = notificationMessage;
			break;
		case Notice.TYPE_MAILBOX:
			returnNotice = new Notice[1];
			returnNotice[0] = mailBox;
			break;
		case Notice.TYPE_SAVEDBOX:
			returnNotice = new Notice[1];
			returnNotice[0] = savedBox;
			break;
		case SeraNotice.TYPE_FRIEND:
			returnNotice = new Notice[1];
			returnNotice[0] = friend;
			break;
		case Notice.TYPE_INVALID:
			returnNotice = new Notice[6];
			returnNotice[0] = notificationMessage;
			returnNotice[1] = message;
			returnNotice[2] = comment;
			returnNotice[3] = assigned;
			returnNotice[4] = mailBox;
			returnNotice[5] = savedBox;
			break;
		}
		return returnNotice;
	}
	
	//PublishNotice 테이블 사용으로 변경되어짐 삭제 예정
	public Notice[] getNotices_old(String userId, int noticeType) throws Exception {

		Notice message = null;
		Notice comment = null;
		Notice assigned = null;
		Notice notificationMessage = null;
		Notice mailBox = null;
		Notice savedBox = null;
		Notice friend = null;

		//---------------------------------------------------------------------------------------
		if (noticeType == Notice.TYPE_MESSAGE || noticeType == Notice.TYPE_INVALID) {
			message = new Notice();
			message.setType(Notice.TYPE_MESSAGE);

			MessageCond messageCond = new MessageCond();
			messageCond.setTargetUser(userId);
			messageCond.setReadStatus(MessageCond.TYPE_STATUS_UNREAD);
			long totalMessageSize = getMessageManager().getMessageSize(userId, messageCond);
			message.setLength((int)totalMessageSize);
		}
		//---------------------------------------------------------------------------------------

		if (noticeType == Notice.TYPE_COMMENT || noticeType == Notice.TYPE_INVALID) {
			
			comment = new Notice();
			comment.setType(Notice.TYPE_COMMENT);
			//내가 작성한 정보관리 업무 + 내가 수행한 태스크가 속해있는 프로세스 인스턴스업무
			TskTaskCond myTaskCond = new TskTaskCond();
			myTaskCond.setStatus(TskTask.TASKSTATUS_COMPLETE);
			myTaskCond.setAssignee(userId);
			
			TskTask[] myTask = getTskManager().getTasks(userId, myTaskCond, IManager.LEVEL_LITE);
			if (myTask != null) {
				List instanceIdList = new ArrayList();
				
				Map recordIdPrcInstIdMap = new HashMap();
				for (int i = 0; i < myTask.length; i++) {
					TskTask task = myTask[i];
					String tskType = task.getType();
					if (tskType.equalsIgnoreCase(TskTask.TASKTYPE_COMMON)) {
						String prcObjId = task.getProcessInstId();
						if (!instanceIdList.contains(prcObjId)) {
							instanceIdList.add(prcObjId);
						}
					} else if (tskType.equalsIgnoreCase(TskTask.TASKTYPE_SINGLE)) {
						String tskDef = task.getDef();
						String tskForm = task.getForm();
						if (CommonUtil.isEmpty(tskDef))
							continue;
						String[] temp = StringUtils.tokenizeToStringArray(tskDef, "|");
						if (CommonUtil.isEmpty(temp) || temp.length != 2)
							continue;
						String formId = temp[0];
						String recordId = temp[1];
						if (!instanceIdList.contains(recordId)) {
							instanceIdList.add(recordId);
							recordIdPrcInstIdMap.put(recordId, task.getProcessInstId());
						}
					}
				}
				if (instanceIdList.size() != 0) {
					String[] opinionRefIds = new String[instanceIdList.size()];
					instanceIdList.toArray(opinionRefIds);
					
					OpinionCond opinionCond = new OpinionCond();
					opinionCond.setRefIdIns(opinionRefIds);
					
					long totalCommentSize = getOpinionManager().getOpinionSize(userId, opinionCond);
					comment.setLength((int)totalCommentSize);
					
				} else {
					comment.setLength(0);
				}
				
			} else {
				comment.setLength(0);
			}
		}
		
		//---------------------------------------------------------------------------------------

		if (noticeType == Notice.TYPE_ASSIGNED || noticeType == Notice.TYPE_INVALID) {
			assigned = new Notice();
			assigned.setType(Notice.TYPE_ASSIGNED);
			
			TaskWorkCond taskCond = new TaskWorkCond();
			taskCond.setTskStatus(TskTask.TASKSTATUS_ASSIGN);
			
			Date lastTaskCreationDate = null;
			taskCond.setTskAssignee(userId);
			taskCond.setPageNo(0);
			taskCond.setPrcStatus(PrcProcessInst.PROCESSINSTSTATUS_RUNNING);
			
			long totalSize = getWorkListManager().getTaskWorkListSize(userId, taskCond);
			
			assigned.setLength((int)totalSize);
		}
		
		
		//---------------------------------------------------------------------------------------

		if (noticeType == Notice.TYPE_NOTIFICATION || noticeType == Notice.TYPE_INVALID) {

			notificationMessage = new Notice();
			notificationMessage.setType(Notice.TYPE_NOTIFICATION);
			
			// TOTAL DELAYED TASK
			TaskWorkCond delayedTaskCond = new TaskWorkCond();
			delayedTaskCond.setTskStatus(TskTask.TASKSTATUS_ASSIGN);
			
			Date lastDelayedTaskCreationDate = null;
			delayedTaskCond.setTskAssignee(userId);
			delayedTaskCond.setPrcStatus(PrcProcessInst.PROCESSINSTSTATUS_RUNNING);
			delayedTaskCond.setExpectEndDateTo(new LocalDate());
			
			long totalDelayedTaskSize = getWorkListManager().getTaskWorkListSize(userId, delayedTaskCond);
			
			// TOTAL REQUESTER
			SwoGroupCond groupCond = new SwoGroupCond();
			groupCond.setGroupLeader(userId);
			SwoGroup[] groups = getSwoManager().getGroups(userId, groupCond, IManager.LEVEL_ALL);
			
			long totalRequestSize = 0;
			if (groups != null) {
				Map<Long, Map<SwoGroupMember, SwoGroup>> joinRequestDateMap = new HashMap<Long, Map<SwoGroupMember, SwoGroup>>();
				for (int i = 0; i < groups.length; i++) {
					SwoGroup group = groups[i];
					SwoGroupMember[] groupMember = group.getSwoGroupMembers();
					if (groupMember == null || groupMember.length == 0)
						continue;
					for (int j = 0; j < groupMember.length; j++) {
						SwoGroupMember member = groupMember[j];
						String joinType = member.getJoinType();
						String joinStatus = member.getJoinStatus();
						if (joinType.equalsIgnoreCase(SwoGroupMember.JOINTYPE_REQUEST) && joinStatus.equalsIgnoreCase(SwoGroupMember.JOINSTATUS_READY)) {
							totalRequestSize += 1;
						}
					}
				}
			}
			notificationMessage.setLength((int)totalDelayedTaskSize + (int)totalRequestSize);

		}
		//---------------------------------------------------------------------------------------

		if (noticeType == Notice.TYPE_MAILBOX || noticeType == Notice.TYPE_INVALID) {
			mailBox = new Notice();
			mailBox.setType(Notice.TYPE_MAILBOX);
			mailBox.setLength(mailService.getUnreadEmails());
		}
		//---------------------------------------------------------------------------------------

		if (noticeType == Notice.TYPE_SAVEDBOX || noticeType == Notice.TYPE_INVALID) {
			savedBox = new Notice();
			savedBox.setType(Notice.TYPE_SAVEDBOX);
			savedBox.setLength(0);
		}

		//---------------------------------------------------------------------------------------

		if (noticeType == SeraNotice.TYPE_FRIEND) {
			friend = new Notice();
			friend.setType(SeraNotice.TYPE_FRIEND);
			SeraUserInfo[] seraUserInfos = SwServiceFactory.getInstance().getSeraService().getFriendRequestsByUserId(userId, null, FriendInformList.MAX_ALL_FRIEND_LIST);
			int length = 0;
			if(!CommonUtil.isEmpty(seraUserInfos))
				length = seraUserInfos.length;
			friend.setLength(length);
		}

		//---------------------------------------------------------------------------------------

		Notice[] returnNotice = null;
		switch (noticeType) {
		case Notice.TYPE_MESSAGE:
			returnNotice = new Notice[1];
			returnNotice[0] = message;
			break;
		case Notice.TYPE_COMMENT:
			returnNotice = new Notice[1];
			returnNotice[0] = comment;
			break;
		case Notice.TYPE_ASSIGNED:
			returnNotice = new Notice[1];
			returnNotice[0] = assigned;
			break;
		case Notice.TYPE_NOTIFICATION:
			returnNotice = new Notice[1];
			returnNotice[0] = notificationMessage;
			break;
		case Notice.TYPE_MAILBOX:
			returnNotice = new Notice[1];
			returnNotice[0] = mailBox;
			break;
		case Notice.TYPE_SAVEDBOX:
			returnNotice = new Notice[1];
			returnNotice[0] = savedBox;
			break;
		case SeraNotice.TYPE_FRIEND:
			returnNotice = new Notice[1];
			returnNotice[0] = friend;
			break;
		case Notice.TYPE_INVALID:
			returnNotice = new Notice[6];
			returnNotice[0] = notificationMessage;
			returnNotice[1] = message;
			returnNotice[2] = comment;
			returnNotice[3] = assigned;
			returnNotice[4] = mailBox;
			returnNotice[5] = savedBox;
			break;
		}
		return returnNotice;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.smartworks.service.impl.ISmartWorks#getNoticesForMe(java.lang.String)
	 * 
	 * 현재사용자에게 알려줄 모든 Notice 들의 갯수만 제공해주는 서비스이다.
	 * 
	 * Notice[] : return
	 * 		
	 */
	@Override
	public Notice[] getNoticesForMe() throws Exception {

		try{
			User user = SmartUtil.getCurrentUser();
			mailService.checkEmail();
			return getNotices(user.getId(), Notice.TYPE_INVALID);
			//return new Notice[] {};//SmartTest.getNoticesForMe();
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}

	/*
	 * 
	 * 전달된 Notice 를 삭제해주는 서비스.
	 * 
	 * NoticeId : input
	 * 		삭제할 Notice ID 값 
	 * 		
	 */
	@Override
	public void removeNoticeInstance(String noticeId) throws Exception {
		
		try{
			User user = SmartUtil.getCurrentUser();
			PublishNoticeCond pubNotiCond = new PublishNoticeCond();
			pubNotiCond.setRefId(noticeId);
			pubNotiCond.setAssignee(user.getId());

			PublishNotice notice = getPublishNoticeManager().getPublishNotice(user.getId(), pubNotiCond, null);
			if (CommonUtil.isEmpty(notice))
				return;
			
			int noticeType = notice.getType();
			String objId = notice.getObjId();
			String assignee = notice.getAssignee();
			
			getPublishNoticeManager().removePublishNotice(user.getId(), objId);
			SmartUtil.increaseNoticeCountByNoticeType(assignee, noticeType);
			
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			// Exception Handling Required			
		}
	}
	private String getPrcInstIdByRecordId(String recordId) throws Exception {

		User user = SmartUtil.getCurrentUser();
		PrcProcessInstCond cond = new PrcProcessInstCond();
		cond.setExtendedProperties(new Property[]{new Property("recordId", recordId)});
		PrcProcessInst prcInst = getPrcManager().getProcessInst(user.getId(), cond, IManager.LEVEL_LITE);
		if (prcInst == null)
			return null;
		return prcInst.getObjId();
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.smartworks.service.impl.ISmartWorks#getNoticeBoxForMe10(int,
	 * net.smartworks.util.LocalDate)
	 * 
	 * Notice Message Box 에서 보여줄 notice 리스트를 제공하는 서비스로서,
	 * 아이디값이 없으면 최근 10개항목을 제공해주고, 있으면 그 아이디 이전 10개를 제공해준다.
	 * 
	 * noticeType : input
	 * 		Notice.TYPE_NOTIFICATION
	 * 		Notice.TYPE_MESSAGE
	 * 		Notice.TYPE_COMMENT
	 * 		Notice.TYPE_ASSIGNED
	 * 		Notice.TYPE_MAILBOX
	 * 		Notice.TYPE_SAVEDBOX
	 * 
	 * lastNoticeId : input
	 * 		null or "" 이면 최근항목 (최대) 10개만 가져오고, 아이디값이 있으면 그아이디값을 제외한 이전 (최대)10개항목을 가져온다.
	 * 
	 * NoticeBox : return
	 * 		
	 * 	
	 */
	public NoticeBox getNoticeBoxForMe10(int noticeType, String lastNoticeId) throws Exception {

		try{
			User user = SmartUtil.getCurrentUser();
			
			switch(noticeType){
			case Notice.TYPE_MAILBOX:
				RequestParams params = new RequestParams();
				params.setPageSize(10);
				params.setCurrentPage(1);
				params.setSortingField(new SortingField(MailContent.A_SENTDATE, false));
				params.setUnreadEmail(true);
				InstanceInfoList mailsList =  mailService.getMailInstanceList(Integer.toString(MailFolder.TYPE_SYSTEM_INBOX), params);
				InstanceInfo[] instances = mailsList.getInstanceDatas();
				NoticeBox noticeBox = new NoticeBox();
				NoticeMessage[] notices = new NoticeMessage[(instances==null) ? 0 : instances.length];
				if(instances!=null){
					for(int i=0; i<instances.length; i++){
						notices[i] = new NoticeMessage(instances[i].getId(), 0, instances[i].getOwner(), instances[i].getCreatedDate());
						notices[i].setInstance(instances[i]);
					}
				}
				noticeBox.setNoticeMessages(notices);
				noticeBox.setNoticeType(Notice.TYPE_MAILBOX);
				if(instances!= null && instances.length>0){
					noticeBox.setDateOfLastNotice(instances[instances.length-1].getCreatedDate());
					noticeBox.setRemainingLength(mailsList.getTotalSize()-instances.length);
				}
				return noticeBox;
	
			case Notice.TYPE_ASSIGNED:

				NoticeBox assignTaskNoticeBox = new NoticeBox();
				
				if (CommonUtil.isEmpty(user.getCompanyId()) || CommonUtil.isEmpty(user.getId()))
					return assignTaskNoticeBox;
				
				PublishNoticeCond assignedCond = new PublishNoticeCond();
				assignedCond.setType(PublishNotice.TYPE_ASSIGNED);
				assignedCond.setRefType(PublishNotice.REFTYPE_ASSIGNED_TASK);
				assignedCond.setAssignee(user.getId());


				//노티스카운트 벨리데이션체크
				TskTaskCond myAssigneTaskCond = new TskTaskCond();
				myAssigneTaskCond.setAssignee(user.getId());
				myAssigneTaskCond.setStatus(TskTask.TASKSTATUS_ASSIGN);
				
				//해당 벨리데이션을 카운트 비교후 맞지 않을때에만 수행을 하려면 아래 주석을 풀고 컴파일 (현재는 무조건 검사하도록 한다)
				
				//long totalMyTaskSize = getTskManager().getTaskSize(user.getId(), myAssigneTaskCond);
				//if (totalSize != totalMyTaskSize) {
					
					Map<String, PublishNotice> noticeTaskIdMap = new HashMap<String, PublishNotice>();
					Map<String, TskTask> myTaskIdsMap = new HashMap<String, TskTask>();
					
					PublishNotice[] tempNotices = getPublishNoticeManager().getPublishNotices(user.getId(), assignedCond, IManager.LEVEL_ALL);
					TskTask[] myTasks = getTskManager().getTasks(user.getId(), myAssigneTaskCond, IManager.LEVEL_LITE);
					for (int i = 0; i < tempNotices.length; i++) {
						noticeTaskIdMap.put(tempNotices[i].getRefId(), tempNotices[i]);
					}
					for (int i = 0; i < myTasks.length; i++) {
						myTaskIdsMap.put(myTasks[i].getObjId(), myTasks[i]);
					}
					
					if (noticeTaskIdMap.size() != 0) {
						Iterator itr = noticeTaskIdMap.keySet().iterator();
						while (itr.hasNext()) {
							String taskId = (String)itr.next();
							PublishNotice tempNotice = (PublishNotice)noticeTaskIdMap.get(taskId);
							TskTask myTask = (TskTask)myTaskIdsMap.get(tempNotice.getRefId());
							if (myTask != null) {
								String status = myTask.getStatus();
								if (status.equalsIgnoreCase(TskTask.TASKSTATUS_ASSIGN)) {
									continue;
								} else {
									getPublishNoticeManager().removePublishNotice(user.getId(), tempNotice.getObjId());
								}
							} else {
								getPublishNoticeManager().removePublishNotice(user.getId(), tempNotice.getObjId());
							}
						}
					}
					if (myTaskIdsMap.size() != 0) {
						Iterator itr = myTaskIdsMap.keySet().iterator();
						while (itr.hasNext()) {
							String taskId = (String)itr.next();
							TskTask myTask = (TskTask)myTaskIdsMap.get(taskId);
							PublishNotice notice = (PublishNotice)noticeTaskIdMap.get(myTask.getObjId());
							if (notice == null) {
								
								PublishNotice newNotice = new PublishNotice();
								newNotice.setType(PublishNotice.TYPE_ASSIGNED);
								newNotice.setRefType(PublishNotice.REFTYPE_ASSIGNED_TASK);
								newNotice.setRefId(taskId);
								newNotice.setAssignee(myTask.getAssignee());
								getPublishNoticeManager().setPublishNotice(user.getId(), newNotice, IManager.LEVEL_ALL);
							}
						}
					}
				//}

				SmartUtil.increaseNoticeCountByNoticeType(user.getId(), noticeType);
				
				long totalSize = getPublishNoticeManager().getPublishNoticeSize(user.getId(), assignedCond);
				
				if (!CommonUtil.isEmpty(lastNoticeId)) {
					assignedCond.setRefId(lastNoticeId);//TaskId
					PublishNotice[] lastNotice = getPublishNoticeManager().getPublishNotices(user.getId(), assignedCond, null);
					if (lastNotice != null && lastNotice.length != 0) {
						assignedCond.setRefId(null);
						Date lastTaskCreationDate = lastNotice[0].getCreationDate();
						assignedCond.setCreationDateTo(lastTaskCreationDate);
					}
				}
				
				PublishNotice[] pubNotis = getPublishNoticeManager().getPublishNotices(user.getId(), assignedCond, null);
				if (pubNotis == null || pubNotis.length == 0)
					return assignTaskNoticeBox;
				
				String[] assignedTaskIdIns = new String[pubNotis.length];
				for (int i = 0; i < pubNotis.length; i++) {
					assignedTaskIdIns[i] = pubNotis[i].getRefId();
				}
				
				TaskWorkCond taskCond = new TaskWorkCond();
				taskCond.setTskObjIdIns(assignedTaskIdIns);
				
				taskCond.setPageNo(0);
				taskCond.setOrders(new Order[]{new Order("tskCreatedate", false)});
				taskCond.setPageSize(10);
				TaskWork[] tasks = getWorkListManager().getTaskWorkList(user.getId(), taskCond);
				
				if(tasks == null)
					return assignTaskNoticeBox;
				
				InstanceInfo[] instInfos = ModelConverter.getInstanceInfoArrayByTaskWorkArray(user.getId(), tasks);
				
				NoticeMessage[] assignTaskNotice = new NoticeMessage[instInfos.length];
				for(int i=0; i<instInfos.length; i++){
					assignTaskNotice[i] = new NoticeMessage(instInfos[i].getId(), 0, instInfos[i].getOwner(), instInfos[i].getCreatedDate());
					assignTaskNotice[i].setInstance(instInfos[i]);
				}
				assignTaskNoticeBox.setNoticeMessages(assignTaskNotice);
				assignTaskNoticeBox.setNoticeType(Notice.TYPE_ASSIGNED);
				assignTaskNoticeBox.setDateOfLastNotice(new LocalDate(instInfos[instInfos.length -1].getCreatedDate().getTime()));
				assignTaskNoticeBox.setRemainingLength((int)totalSize - instInfos.length);
				return assignTaskNoticeBox;
				
			case Notice.TYPE_COMMENT:

				NoticeBox commentNoticeBox = new NoticeBox();
				
				//댓글 타입
				//프로세스인스턴스, 정보관리업무레코드, 프로세스메뉴얼
				
				//내가 작성한 정보관리 업무 + 내가 수행한 태스크가 속해있는 프로세스 인스턴스업무
				
				PublishNoticeCond commentCond = new PublishNoticeCond(user.getId(), PublishNotice.TYPE_COMMENT, null, null);
				
				long totalCommentSize = getPublishNoticeManager().getPublishNoticeSize(user.getId(), commentCond);
				
				if (totalCommentSize != 0) {
					PublishNotice[] tempNotice = getPublishNoticeManager().getPublishNotices(user.getId(), commentCond, IManager.LEVEL_ALL);
					if (tempNotice != null && tempNotice.length != 0) {
						String[] opinionIdIns = new String[tempNotice.length];
						for (int i = 0; i < tempNotice.length; i++) {
							opinionIdIns[i] = tempNotice[i].getRefId();
						}
						OpinionCond opinionCond = new OpinionCond();
						opinionCond.setObjIdIns(opinionIdIns);
						opinionCond.setOrders(new Order[]{new Order(Opinion.A_CREATIONDATE, false)});
						opinionCond.setPageNo(0);
						opinionCond.setPageSize(10);
						Opinion[] tempOpinions = getOpinionManager().getOpinions(user.getId(), opinionCond, IManager.LEVEL_ALL);
						if (tempOpinions != null && tempOpinions.length != 0) {
							for (int i = 0; i < tempOpinions.length; i++) {
								Opinion tempOpinion = tempOpinions[i];
								int opinionType = tempOpinion.getRefType();
								if (opinionType != 4)//정보관리업무 4, 프로세스 2
									continue;
								String formId = tempOpinion.getRefFormId();
								String recordId = tempOpinion.getRefId();
								
								SwdRecordCond recCond = new SwdRecordCond();
								recCond.setFormId(formId);
								recCond.setRecordId(recordId);
								long size = SwManagerFactory.getInstance().getSwdManager().getRecordSize(user.getId(), recCond);
								
								if (size != 0)
									continue;
								
								PublishNoticeCond pubCond = new PublishNoticeCond();
								pubCond.setRefId(tempOpinion.getObjId());
								getPublishNoticeManager().removePublishNotice(user.getId(), pubCond);
							}
						} else {
							for (int i = 0; i < tempNotice.length; i++) {
								getPublishNoticeManager().removePublishNotice(user.getId(), tempNotice[i].getObjId());
							}
						}
						SmartUtil.increaseNoticeCountByNoticeType(user.getId(), noticeType);
					}
				}
				totalCommentSize = getPublishNoticeManager().getPublishNoticeSize(user.getId(), commentCond);
				
				if (!CommonUtil.isEmpty(lastNoticeId)) {
					commentCond.setRefId(lastNoticeId);//opinionId
					PublishNotice[] lastNotice = getPublishNoticeManager().getPublishNotices(user.getId(), commentCond, null);
					if (lastNotice != null && lastNotice.length != 0) {
						Date lastNoticeCreationDate = lastNotice[0].getCreationDate();
						commentCond.setCreationDateTo(lastNoticeCreationDate);
					}
				}
				
				PublishNotice[] commentNotices = getPublishNoticeManager().getPublishNotices(user.getId(), commentCond, null);
				if (commentNotices == null || commentNotices.length == 0)
					return commentNoticeBox;
				
				String[] opinionIdIns = new String[commentNotices.length];
				for (int i = 0; i < commentNotices.length; i++) {
					opinionIdIns[i] = commentNotices[i].getRefId();
				}
				
				OpinionCond opinionCond = new OpinionCond();
				opinionCond.setObjIdIns(opinionIdIns);
				opinionCond.setOrders(new Order[]{new Order(Opinion.A_CREATIONDATE, false)});
				opinionCond.setPageNo(0);
				opinionCond.setPageSize(10);
				
				Opinion[] opinions = getOpinionManager().getOpinions(user.getId(), opinionCond, IManager.LEVEL_ALL);

				List<CommentInstanceInfo> commentInstanceInfoList = new ArrayList<CommentInstanceInfo>();
				CommentInstanceInfo[] commentInstanceInfos = null;
				if(!CommonUtil.isEmpty(opinions)) {
					int opinionLength = opinions.length;
					for(int i=0; i<opinionLength; i++) {
						Opinion opinion = opinions[i];
						CommentInstanceInfo commentInstanceInfo = new CommentInstanceInfo();
						String modificationUser = opinion.getModificationUser() == null ? opinion.getCreationUser() : opinion.getModificationUser();
						Date modificationDate = opinion.getModificationDate() == null ? opinion.getCreationDate() : opinion.getModificationDate();
						commentInstanceInfo.setId(opinion.getObjId());
						if (opinion.getRefType() == 4) {
							//정보관리 업무
							commentInstanceInfo.setCommentType(CommentInstance.COMMENT_TYPE_ON_WORK_INSTANCE);
							//commentInstanceInfo.setWorkInstance(ModelConverter.getIWInstanceInfoByRecordId(null, opinion.getRefId()));
							WorkInstanceInfo wInfo = ModelConverter.getWorkInstanceInfoByPrcProcessInstId(getPrcInstIdByRecordId(opinion.getRefId()));
							wInfo.setId(opinion.getRefId());
							commentInstanceInfo.setWorkInstance(wInfo);
						} else if (opinion.getRefType() == 2) {
							//프로세스인스턴스
							commentInstanceInfo.setCommentType(CommentInstance.COMMENT_TYPE_ON_WORK_INSTANCE);
							WorkInstanceInfo pInfo = ModelConverter.getWorkInstanceInfoByPrcProcessInstId(opinion.getRefId());
							pInfo.setId(opinion.getRefId());
							commentInstanceInfo.setWorkInstance(pInfo);
						} else if (opinion.getRefType() == 6) {
							//프로세스메뉴얼
							commentInstanceInfo.setCommentType(CommentInstance.COMMENT_TYPE_ON_WORK_MANUAL);
							//TODO
						}
						commentInstanceInfo.setComment(opinion.getOpinion());
						commentInstanceInfo.setCommentor(ModelConverter.getUserInfoByUserId(opinion.getCreationUser()));
						commentInstanceInfo.setLastModifiedDate(new LocalDate(modificationDate.getTime()));
						commentInstanceInfo.setType(Instance.TYPE_COMMENT);
						commentInstanceInfo.setOwner(ModelConverter.getUserInfoByUserId(opinion.getCreationUser()));
						commentInstanceInfo.setCreatedDate(new LocalDate(opinion.getCreationDate().getTime()));
						commentInstanceInfo.setLastModifier(ModelConverter.getUserInfoByUserId(modificationUser));
						
						commentInstanceInfoList.add(commentInstanceInfo);
					}
				}
				if(commentInstanceInfoList.size() > 0) {
					commentInstanceInfos = new CommentInstanceInfo[commentInstanceInfoList.size()];
					commentInstanceInfoList.toArray(commentInstanceInfos);
				}
				
				NoticeMessage[] commentNotice = new NoticeMessage[commentInstanceInfos.length];
				for(int i=0; i<commentInstanceInfos.length; i++){
					commentNotice[i] = new NoticeMessage(commentInstanceInfos[i].getId(), 0, commentInstanceInfos[i].getOwner(), commentInstanceInfos[i].getCreatedDate());
					commentNotice[i].setInstance(commentInstanceInfos[i]);
				}
				commentNoticeBox.setNoticeMessages(commentNotice);
				commentNoticeBox.setNoticeType(Notice.TYPE_COMMENT);
				commentNoticeBox.setDateOfLastNotice(new LocalDate(commentInstanceInfos[commentInstanceInfos.length -1].getCreatedDate().getTime()));
				commentNoticeBox.setRemainingLength((int)totalCommentSize - commentInstanceInfos.length);
				
				return commentNoticeBox;
				
			case Notice.TYPE_MESSAGE:

				NoticeBox messageNoticeBox = new NoticeBox();
				
				PublishNoticeCond messageNoticeCond = new PublishNoticeCond(user.getId(), PublishNotice.TYPE_MESSAGE, null, null);
				
				long totalMessageSize = getPublishNoticeManager().getPublishNoticeSize(user.getId(), messageNoticeCond);
				
				if (!CommonUtil.isEmpty(lastNoticeId)) {
					messageNoticeCond.setRefId(lastNoticeId);//messageid
					PublishNotice[] lastNotice = getPublishNoticeManager().getPublishNotices(user.getId(), messageNoticeCond, null);
					if (lastNotice != null && lastNotice.length != 0) {
						Date lastNoticeCreationDate = lastNotice[0].getCreationDate();
						messageNoticeCond.setCreationDateTo(lastNoticeCreationDate);
					}
				}
				
				PublishNotice[] messageNotices = getPublishNoticeManager().getPublishNotices(user.getId(), messageNoticeCond, null);
				if (messageNotices == null || messageNotices.length == 0)
					return messageNoticeBox;
				
				String[] messageIdIns = new String[messageNotices.length];
				for (int i = 0; i < messageNotices.length; i++) {
					messageIdIns[i] = messageNotices[i].getRefId();
				}

				MessageCond messageCond = new MessageCond();
				messageCond.setObjIdIns(messageIdIns);
				messageCond.setOrders(new Order[]{new Order(Opinion.A_CREATIONDATE, false)});
				messageCond.setPageNo(0);
				messageCond.setPageSize(10);
				
				Message[] messages = getMessageManager().getMessages(user.getId(), messageCond, IManager.LEVEL_ALL);
				
				if (messages == null)
					return messageNoticeBox;
				
				AsyncMessageInstanceInfo[] asyncMessageInstanceInfos = new AsyncMessageInstanceInfo[messages.length];
				for (int i = 0; i < messages.length; i++) {
					Message message = messages[i];
					AsyncMessageInstanceInfo asyncMessage = new AsyncMessageInstanceInfo();
					
					asyncMessage.setId(message.getObjId());
					asyncMessage.setSender(ModelConverter.getUserInfoByUserId(message.getSendUser()));
					asyncMessage.setReceiver(ModelConverter.getUserInfoByUserId(message.getTargetUser()));
					asyncMessage.setChatters(null);
					asyncMessage.setSendDate(new LocalDate(message.getCreationDate().getTime()));
					asyncMessage.setMsgStatus(message.getStatus() == null || message.getStatus() == "" ? -1 : Integer.parseInt(message.getStatus()));
					
					asyncMessage.setMessage(message.getContent());
					asyncMessage.setCreatedDate(new LocalDate(message.getCreationDate().getTime()));
					asyncMessageInstanceInfos[i] = asyncMessage;
				}
				
				NoticeMessage[] messageNotice = new NoticeMessage[asyncMessageInstanceInfos.length];
				for(int i=0; i<asyncMessageInstanceInfos.length; i++){
					messageNotice[i] = new NoticeMessage(asyncMessageInstanceInfos[i].getId(), 0, asyncMessageInstanceInfos[i].getOwner(), asyncMessageInstanceInfos[i].getCreatedDate());
					messageNotice[i].setInstance(asyncMessageInstanceInfos[i]);
				}
				messageNoticeBox.setNoticeMessages(messageNotice);
				messageNoticeBox.setNoticeType(Notice.TYPE_MESSAGE);
				messageNoticeBox.setDateOfLastNotice(new LocalDate(asyncMessageInstanceInfos[asyncMessageInstanceInfos.length -1].getCreatedDate().getTime()));
				messageNoticeBox.setRemainingLength((int)totalMessageSize - asyncMessageInstanceInfos.length);
				
				return messageNoticeBox;
				
			case Notice.TYPE_NOTIFICATION:

				NoticeBox notificationNoticeBox = new NoticeBox();
				
				//NoticeMessage noticeMessate = new NoticeMessage();
				//TYPE_SYSTEM_NOTICE 	= 1;  //기능없음
				//TYPE_EVENT_ALARM 		= 2;	//알람?
				//TYPE_TASK_DELAYED 		= 3;
				//TYPE_JOIN_REQUEST 		= 4;
				//TYPE_INSTANCE_CREATED 	= 5;	//??
				
				if (CommonUtil.isEmpty(user.getCompanyId()) || CommonUtil.isEmpty(user.getId()))
					return notificationNoticeBox;
		
				Map<Long, NoticeMessage> NoticeMessageMap = new HashMap<Long, NoticeMessage>();
				
				//------------------------Delayed Task---------------------------------------------------------------
				
				PublishNoticeCond delayedCond = new PublishNoticeCond();
				delayedCond.setType(PublishNotice.TYPE_NOTIFICATION);
				delayedCond.setRefType(PublishNotice.REFTYPE_DELAYED_TASK);
				delayedCond.setAssignee(user.getId());

				long totalDelayedTaskSize = getPublishNoticeManager().getPublishNoticeSize(user.getId(), delayedCond);
				
				if (!CommonUtil.isEmpty(lastNoticeId)) {
					delayedCond.setRefId(lastNoticeId);//TaskId
					PublishNotice[] lastNotice = getPublishNoticeManager().getPublishNotices(user.getId(), delayedCond, null);
					if (lastNotice != null && lastNotice.length != 0) {
						Date lastTaskCreationDate = lastNotice[0].getCreationDate();
						delayedCond.setCreationDateTo(lastTaskCreationDate);
					}
				}
				
				PublishNotice[] delayedNotices = getPublishNoticeManager().getPublishNotices(user.getId(), delayedCond, null);
				if (delayedNotices != null && delayedNotices.length != 0) {
					
					String[] delayedTaskIdIns = new String[delayedNotices.length];
					for (int i = 0; i < delayedNotices.length; i++) {
						delayedTaskIdIns[i] = delayedNotices[i].getRefId();
					}

					TaskWorkCond delayedTaskCond = new TaskWorkCond();
					delayedTaskCond.setTskObjIdIns(delayedTaskIdIns);
					
					delayedTaskCond.setPageNo(0);
					delayedTaskCond.setOrders(new Order[]{new Order("tskCreatedate", false)});
					delayedTaskCond.setPageSize(10);
					TaskWork[] delayedTasks = getWorkListManager().getTaskWorkList(user.getId(), delayedTaskCond);
					
					if(delayedTasks != null) {
						InstanceInfo[] delayedTaskInstInfos = ModelConverter.getInstanceInfoArrayByTaskWorkArray(user.getId(), delayedTasks);
						for(int i=0; i<delayedTaskInstInfos.length; i++){
							NoticeMessage delayedTaskNotice = new NoticeMessage(delayedTaskInstInfos[i].getId(), 0, delayedTaskInstInfos[i].getOwner(), delayedTaskInstInfos[i].getCreatedDate());
							delayedTaskNotice.setInstance(delayedTaskInstInfos[i]);
							delayedTaskNotice.setType(NoticeMessage.TYPE_TASK_DELAYED);
							//NoticeMessageList.add(delayedTaskNotice);
							NoticeMessageMap.put(delayedTaskInstInfos[i].getCreatedDate().getTime(), delayedTaskNotice);
						}
					}
				}
				
				//---------------------------Join Request---------------------------------------------------------------------------------------
				
				//내가 운영하는 그룹정보
				
				PublishNoticeCond reqJoinGropuCond = new PublishNoticeCond();
				reqJoinGropuCond.setType(PublishNotice.TYPE_NOTIFICATION);
				reqJoinGropuCond.setRefType(PublishNotice.REFTYPE_GROUPJOINREQUEST);
				reqJoinGropuCond.setAssignee(user.getId());

				long totalReqJoinGroupSize = getPublishNoticeManager().getPublishNoticeSize(user.getId(), reqJoinGropuCond);
				
				if (!CommonUtil.isEmpty(lastNoticeId)) {
					reqJoinGropuCond.setRefId(lastNoticeId);//TaskId
					PublishNotice[] lastNotice = getPublishNoticeManager().getPublishNotices(user.getId(), reqJoinGropuCond, null);
					if (lastNotice != null && lastNotice.length != 0) {
						Date lastTaskCreationDate = lastNotice[0].getCreationDate();
						reqJoinGropuCond.setCreationDateTo(lastTaskCreationDate);
					}
				}
				
				PublishNotice[] reqJoinGroupNotices = getPublishNoticeManager().getPublishNotices(user.getId(), reqJoinGropuCond, null);
				if (reqJoinGroupNotices != null && reqJoinGroupNotices.length != 0) {
					for (int i = 0; i < reqJoinGroupNotices.length; i++) {
						PublishNotice reqJoinGroupNotice = reqJoinGroupNotices[i];
						String refId = reqJoinGroupNotice.getRefId();

						String[] ids = StringUtils.tokenizeToStringArray(refId, "|");
						if (ids == null || ids.length != 2)
							continue;
						String groupId = ids[0];
						String requester = ids[1];
						
						UserInfo memberInfo = ModelConverter.getUserInfoByUserId(requester);
						NoticeMessage joinRequestNotice = new NoticeMessage(groupId + "_" + requester, 0, memberInfo, new LocalDate(reqJoinGroupNotice.getCreationDate().getTime()));
						joinRequestNotice.setGroup(ModelConverter.getGroupInfoBySwoGroup(null, getSwoManager().getGroup(user.getId(), groupId, IManager.LEVEL_ALL)));
						joinRequestNotice.setType(NoticeMessage.TYPE_JOIN_REQUEST);
						joinRequestNotice.setMessage(memberInfo.getLongName());
						//NoticeMessageList.add(joinRequestNotice);
						NoticeMessageMap.put(reqJoinGroupNotice.getCreationDate().getTime(), joinRequestNotice);
					}
				}
				
				//---------------------------------------------------------------------------------------------------------
						
				TreeMap resultTreeMap = new TreeMap(Collections.reverseOrder());
				resultTreeMap.putAll(NoticeMessageMap);
				Iterator itr = resultTreeMap.keySet().iterator();
				
				boolean lastFlag = true;
				if (!CommonUtil.isEmpty(lastNoticeId))
					lastFlag = false;
				
				List NoticeMessageList = new ArrayList();
				
				while (itr.hasNext()) {
					NoticeMessage message = (NoticeMessage)resultTreeMap.get(itr.next());
					if (message.getId().equalsIgnoreCase(lastNoticeId)) {
						lastFlag = true;
						continue;
					}
					if (lastFlag) {
						NoticeMessageList.add(message);
						if (NoticeMessageList.size() == 10)
							break;
					}
				}
				NoticeMessage[] resultNoticeMessage = null;
				if(NoticeMessageList.size() > 0) {
					resultNoticeMessage = new NoticeMessage[NoticeMessageList.size()];
					NoticeMessageList.toArray(resultNoticeMessage);
					notificationNoticeBox.setDateOfLastNotice(resultNoticeMessage[resultNoticeMessage.length-1].getIssuedDate());
					notificationNoticeBox.setRemainingLength((int)(totalReqJoinGroupSize + totalDelayedTaskSize) - resultNoticeMessage.length);
				}

				notificationNoticeBox.setNoticeMessages(resultNoticeMessage);
				notificationNoticeBox.setNoticeType(Notice.TYPE_NOTIFICATION);
				
				return notificationNoticeBox;
				
			case Notice.TYPE_SAVEDBOX:
				return SmartTest.getNoticeBoxForMe10(noticeType);
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
	 * (non-Javadoc)
	 * 
	 * @see net.smartworks.service.impl.ISmartWorks#getNoticeBoxForMe10(int,
	 * net.smartworks.util.LocalDate)
	 * 
	 * Notice Message Box 에서 보여줄 notice 리스트를 제공하는 서비스로서,
	 * 아이디값이 없으면 최근 10개항목을 제공해주고, 있으면 그 아이디 이전 10개를 제공해준다.
	 * 
	 * noticeType : input
	 * 		Notice.TYPE_NOTIFICATION
	 * 		Notice.TYPE_MESSAGE
	 * 		Notice.TYPE_COMMENT
	 * 		Notice.TYPE_ASSIGNED
	 * 		Notice.TYPE_MAILBOX
	 * 		Notice.TYPE_SAVEDBOX
	 * 
	 * lastNoticeId : input
	 * 		null or "" 이면 최근항목 (최대) 10개만 가져오고, 아이디값이 있으면 그아이디값을 제외한 이전 (최대)10개항목을 가져온다.
	 * 
	 * NoticeBox : return
	 * 		
	 * 	
	 */
	public NoticeBox getNoticeBoxForMe10_old(int noticeType, String lastNoticeId) throws Exception {

		try{
			User user = SmartUtil.getCurrentUser();
			
			switch(noticeType){
			case Notice.TYPE_MAILBOX:
				RequestParams params = new RequestParams();
				params.setPageSize(10);
				params.setCurrentPage(1);
				params.setSortingField(new SortingField(MailContent.A_SENTDATE, false));
				InstanceInfoList mailsList =  mailService.getMailInstanceList(Integer.toString(MailFolder.TYPE_SYSTEM_INBOX), params);
				InstanceInfo[] instances = mailsList.getInstanceDatas();
				NoticeBox noticeBox = new NoticeBox();
				NoticeMessage[] notices = new NoticeMessage[instances.length];
				for(int i=0; i<instances.length; i++){
					notices[i] = new NoticeMessage(instances[i].getId(), 0, instances[i].getOwner(), instances[i].getCreatedDate());
					notices[i].setInstance(instances[i]);
				}
				noticeBox.setNoticeMessages(notices);
				noticeBox.setNoticeType(Notice.TYPE_MAILBOX);
				noticeBox.setDateOfLastNotice(new LocalDate());
				noticeBox.setRemainingLength(48);
				return noticeBox;
	
			case Notice.TYPE_ASSIGNED:

				NoticeBox assignTaskNoticeBox = new NoticeBox();
				
				if (CommonUtil.isEmpty(user.getCompanyId()) || CommonUtil.isEmpty(user.getId()))
					return assignTaskNoticeBox;
		
				TaskWorkCond taskCond = new TaskWorkCond();
				taskCond.setTskStatus(TskTask.TASKSTATUS_ASSIGN);
				
				Date lastTaskCreationDate = null;
				if (!CommonUtil.isEmpty(lastNoticeId)) {
					TskTaskCond lastTaskCond = new TskTaskCond();	
					lastTaskCond.setProcessInstId(lastNoticeId);
					lastTaskCond.setAssignee(user.getId());
					lastTaskCond.setStatus(TskTask.TASKSTATUS_ASSIGN);
					TskTask[] lastTasks = getTskManager().getTasks(user.getId(), lastTaskCond, IManager.LEVEL_LITE);
					if (lastTasks != null) {
						lastTaskCreationDate = lastTasks[0].getCreationDate();
					}
				}
				if (lastTaskCreationDate != null) {
					taskCond.setLastInstanceDate(new LocalDate(lastTaskCreationDate.getTime()));
				} else {
					taskCond.setLastInstanceDate(new LocalDate());
				}
				taskCond.setTskAssignee(user.getId());
				taskCond.setPageNo(0);
				taskCond.setPrcStatus(PrcProcessInst.PROCESSINSTSTATUS_RUNNING);
				
				long totalSize = getWorkListManager().getTaskWorkListSize(user.getId(), taskCond);
				
				taskCond.setOrders(new Order[]{new Order("tskCreatedate", false)});
				
				taskCond.setPageSize(10);
				TaskWork[] tasks = getWorkListManager().getTaskWorkList(user.getId(), taskCond);
				
				if(tasks == null)
					return assignTaskNoticeBox;
				
				InstanceInfo[] instInfos = ModelConverter.getInstanceInfoArrayByTaskWorkArray(user.getId(), tasks);
				
				NoticeMessage[] assignTaskNotice = new NoticeMessage[instInfos.length];
				for(int i=0; i<instInfos.length; i++){
					assignTaskNotice[i] = new NoticeMessage(instInfos[i].getId(), 0, instInfos[i].getOwner(), instInfos[i].getCreatedDate());
					assignTaskNotice[i].setInstance(instInfos[i]);
				}
				assignTaskNoticeBox.setNoticeMessages(assignTaskNotice);
				assignTaskNoticeBox.setNoticeType(Notice.TYPE_ASSIGNED);
				assignTaskNoticeBox.setDateOfLastNotice(new LocalDate(instInfos[instInfos.length -1].getCreatedDate().getTime()));
				assignTaskNoticeBox.setRemainingLength((int)totalSize - instInfos.length);
				return assignTaskNoticeBox;
				
			case Notice.TYPE_COMMENT:

				NoticeBox commentNoticeBox = new NoticeBox();
				
				//댓글 타입
				//프로세스인스턴스, 정보관리업무레코드, 프로세스메뉴얼
				
				//내가 작성한 정보관리 업무 + 내가 수행한 태스크가 속해있는 프로세스 인스턴스업무
				TskTaskCond myTaskCond = new TskTaskCond();
				myTaskCond.setStatus(TskTask.TASKSTATUS_COMPLETE);
				myTaskCond.setAssignee(user.getId());
				
				TskTask[] myTask = getTskManager().getTasks(user.getId(), myTaskCond, IManager.LEVEL_LITE);
				if (myTask == null)
					return commentNoticeBox;
				List instanceIdList = new ArrayList();
				
				Map recordIdPrcInstIdMap = new HashMap();
				for (int i = 0; i < myTask.length; i++) {
					TskTask task = myTask[i];
					String tskType = task.getType();
					if (tskType.equalsIgnoreCase(TskTask.TASKTYPE_COMMON)) {
						String prcObjId = task.getProcessInstId();
						if (!instanceIdList.contains(prcObjId)) {
							instanceIdList.add(prcObjId);
						}
					} else if (tskType.equalsIgnoreCase(TskTask.TASKTYPE_SINGLE)) {
						String tskDef = task.getDef();
						String tskForm = task.getForm();
						if (CommonUtil.isEmpty(tskDef))
							continue;
						String[] temp = StringUtils.tokenizeToStringArray(tskDef, "|");
						if (CommonUtil.isEmpty(temp) || temp.length != 2)
							continue;
						String formId = temp[0];
						String recordId = temp[1];
						if (!instanceIdList.contains(recordId)) {
							instanceIdList.add(recordId);
							recordIdPrcInstIdMap.put(recordId, task.getProcessInstId());
						}
					}
				}
				if (instanceIdList.size() == 0)
					return commentNoticeBox;
				
				String[] opinionRefIds = new String[instanceIdList.size()];
				instanceIdList.toArray(opinionRefIds);
				
				
				OpinionCond opinionCond = new OpinionCond();
				opinionCond.setRefIdIns(opinionRefIds);
				
				long totalCommentSize = getOpinionManager().getOpinionSize(user.getId(), opinionCond);
				
				opinionCond.setOrders(new Order[]{new Order(Opinion.A_CREATIONDATE, false)});
				opinionCond.setPageNo(0);
				opinionCond.setPageSize(10);
				
				if (!CommonUtil.isEmpty(lastNoticeId)) {
					Opinion lastOpinion = getOpinionManager().getOpinion(user.getId(), lastNoticeId, null);
					if (lastOpinion != null) {
						opinionCond.setWrittenDateTo(lastOpinion.getCreationDate());
					}
				}
				
				Opinion[] opinions = getOpinionManager().getOpinions(user.getId(), opinionCond, IManager.LEVEL_ALL);

				List<CommentInstanceInfo> commentInstanceInfoList = new ArrayList<CommentInstanceInfo>();
				CommentInstanceInfo[] commentInstanceInfos = null;
				if(!CommonUtil.isEmpty(opinions)) {
					int opinionLength = opinions.length;
					for(int i=0; i<opinionLength; i++) {
						Opinion opinion = opinions[i];
						CommentInstanceInfo commentInstanceInfo = new CommentInstanceInfo();
						String modificationUser = opinion.getModificationUser() == null ? opinion.getCreationUser() : opinion.getModificationUser();
						Date modificationDate = opinion.getModificationDate() == null ? opinion.getCreationDate() : opinion.getModificationDate();
						commentInstanceInfo.setId(opinion.getObjId());
						if (opinion.getRefType() == 4) {
							//정보관리 업무
							commentInstanceInfo.setCommentType(CommentInstance.COMMENT_TYPE_ON_WORK_INSTANCE);
							//commentInstanceInfo.setWorkInstance(ModelConverter.getIWInstanceInfoByRecordId(null, opinion.getRefId()));
							WorkInstanceInfo wInfo = ModelConverter.getWorkInstanceInfoByPrcProcessInstId((String)recordIdPrcInstIdMap.get(opinion.getRefId()));
							wInfo.setId(opinion.getRefId());
							commentInstanceInfo.setWorkInstance(wInfo);
						} else if (opinion.getRefType() == 2) {
							//프로세스인스턴스
							commentInstanceInfo.setCommentType(CommentInstance.COMMENT_TYPE_ON_WORK_INSTANCE);
							WorkInstanceInfo pInfo = ModelConverter.getWorkInstanceInfoByPrcProcessInstId(opinion.getRefId());
							pInfo.setId(opinion.getRefId());
							commentInstanceInfo.setWorkInstance(pInfo);
						} else if (opinion.getRefType() == 6) {
							//프로세스메뉴얼
							commentInstanceInfo.setCommentType(CommentInstance.COMMENT_TYPE_ON_WORK_MANUAL);
							//TODO
						}
						commentInstanceInfo.setComment(opinion.getOpinion());
						commentInstanceInfo.setCommentor(ModelConverter.getUserInfoByUserId(opinion.getCreationUser()));
						commentInstanceInfo.setLastModifiedDate(new LocalDate(modificationDate.getTime()));
						commentInstanceInfo.setType(Instance.TYPE_COMMENT);
						commentInstanceInfo.setOwner(ModelConverter.getUserInfoByUserId(opinion.getCreationUser()));
						commentInstanceInfo.setCreatedDate(new LocalDate(opinion.getCreationDate().getTime()));
						commentInstanceInfo.setLastModifier(ModelConverter.getUserInfoByUserId(modificationUser));
						
						commentInstanceInfoList.add(commentInstanceInfo);
					}
				}
				if(commentInstanceInfoList.size() > 0) {
					commentInstanceInfos = new CommentInstanceInfo[commentInstanceInfoList.size()];
					commentInstanceInfoList.toArray(commentInstanceInfos);
				}
				
				NoticeMessage[] commentNotice = new NoticeMessage[commentInstanceInfos.length];
				for(int i=0; i<commentInstanceInfos.length; i++){
					commentNotice[i] = new NoticeMessage(commentInstanceInfos[i].getId(), 0, commentInstanceInfos[i].getOwner(), commentInstanceInfos[i].getCreatedDate());
					commentNotice[i].setInstance(commentInstanceInfos[i]);
				}
				commentNoticeBox.setNoticeMessages(commentNotice);
				commentNoticeBox.setNoticeType(Notice.TYPE_COMMENT);
				commentNoticeBox.setDateOfLastNotice(new LocalDate(commentInstanceInfos[commentInstanceInfos.length -1].getCreatedDate().getTime()));
				commentNoticeBox.setRemainingLength((int)totalCommentSize - commentInstanceInfos.length);
				
				return commentNoticeBox;
				
			case Notice.TYPE_MESSAGE:

				NoticeBox messageNoticeBox = new NoticeBox();
				
				MessageCond messageCond = new MessageCond();
				messageCond.setTargetUser(user.getId());
				messageCond.setChecked(false);
				
				long totalMessageSize = getMessageManager().getMessageSize(user.getId(), messageCond);

				messageCond.setOrders(new Order[]{new Order(Opinion.A_CREATIONDATE, false)});
				messageCond.setPageNo(0);
				messageCond.setPageSize(10);
				
				if (!CommonUtil.isEmpty(lastNoticeId)) {
					Message lastMessage = getMessageManager().getMessage(user.getId(), lastNoticeId, null);
					if (lastMessage != null) {
						messageCond.setCreationDateTo(lastMessage.getCreationDate());
					}
				}
				
				Message[] messages = getMessageManager().getMessages(user.getId(), messageCond, IManager.LEVEL_ALL);
				
				if (messages == null)
					return messageNoticeBox;
				
				AsyncMessageInstanceInfo[] asyncMessageInstanceInfos = new AsyncMessageInstanceInfo[messages.length];
				for (int i = 0; i < messages.length; i++) {
					Message message = messages[i];
					AsyncMessageInstanceInfo asyncMessage = new AsyncMessageInstanceInfo();
					
					asyncMessage.setSender(ModelConverter.getUserInfoByUserId(message.getSendUser()));
					asyncMessage.setReceiver(ModelConverter.getUserInfoByUserId(message.getTargetUser()));
					asyncMessage.setChatters(null);
					asyncMessage.setSendDate(new LocalDate(message.getCreationDate().getTime()));
					asyncMessage.setMsgStatus(message.getStatus() == null || message.getStatus() == "" ? -1 : Integer.parseInt(message.getStatus()));
					
					asyncMessage.setMessage(message.getContent());
					asyncMessage.setCreatedDate(new LocalDate(message.getCreationDate().getTime()));
					asyncMessageInstanceInfos[i] = asyncMessage;
				}
				
				NoticeMessage[] messageNotice = new NoticeMessage[asyncMessageInstanceInfos.length];
				for(int i=0; i<asyncMessageInstanceInfos.length; i++){
					messageNotice[i] = new NoticeMessage(asyncMessageInstanceInfos[i].getId(), 0, asyncMessageInstanceInfos[i].getOwner(), asyncMessageInstanceInfos[i].getCreatedDate());
					messageNotice[i].setInstance(asyncMessageInstanceInfos[i]);
				}
				messageNoticeBox.setNoticeMessages(messageNotice);
				messageNoticeBox.setNoticeType(Notice.TYPE_MESSAGE);
				messageNoticeBox.setDateOfLastNotice(new LocalDate(asyncMessageInstanceInfos[asyncMessageInstanceInfos.length -1].getCreatedDate().getTime()));
				messageNoticeBox.setRemainingLength((int)totalMessageSize - asyncMessageInstanceInfos.length);
				
				return messageNoticeBox;
				
			case Notice.TYPE_NOTIFICATION:

				NoticeBox notificationNoticeBox = new NoticeBox();
				
				//NoticeMessage noticeMessate = new NoticeMessage();
				//TYPE_SYSTEM_NOTICE 	= 1;  //기능없음
				//TYPE_EVENT_ALARM 		= 2;	//알람?
				//TYPE_TASK_DELAYED 		= 3;
				//TYPE_JOIN_REQUEST 		= 4;
				//TYPE_INSTANCE_CREATED 	= 5;	//??
				
				if (CommonUtil.isEmpty(user.getCompanyId()) || CommonUtil.isEmpty(user.getId()))
					return notificationNoticeBox;
		
				Map<Long, NoticeMessage> NoticeMessageMap = new HashMap<Long, NoticeMessage>();
				
				//------------------------Delayed Task---------------------------------------------------------------
				
				TaskWorkCond delayedTaskCond = new TaskWorkCond();
				delayedTaskCond.setTskStatus(TskTask.TASKSTATUS_ASSIGN);
				
				Date lastDelayedTaskCreationDate = null;
				delayedTaskCond.setTskAssignee(user.getId());
				delayedTaskCond.setPrcStatus(PrcProcessInst.PROCESSINSTSTATUS_RUNNING);
				delayedTaskCond.setExpectEndDateTo(new LocalDate());
				
				long totalDelayedTaskSize = getWorkListManager().getTaskWorkListSize(user.getId(), delayedTaskCond);
				TaskWork[] delayedTasks = getWorkListManager().getTaskWorkList(user.getId(), delayedTaskCond);
				
				if(delayedTasks != null) {
					InstanceInfo[] delayedTaskInstInfos = ModelConverter.getInstanceInfoArrayByTaskWorkArray(user.getId(), delayedTasks);
					for(int i=0; i<delayedTaskInstInfos.length; i++){
						NoticeMessage delayedTaskNotice = new NoticeMessage(delayedTaskInstInfos[i].getId(), 0, delayedTaskInstInfos[i].getOwner(), delayedTaskInstInfos[i].getCreatedDate());
						delayedTaskNotice.setInstance(delayedTaskInstInfos[i]);
						delayedTaskNotice.setType(NoticeMessage.TYPE_TASK_DELAYED);
						//NoticeMessageList.add(delayedTaskNotice);
						NoticeMessageMap.put(delayedTaskInstInfos[i].getCreatedDate().getTime(), delayedTaskNotice);
					}
				}
					
				//---------------------------Join Request---------------------------------------------------------------------------------------
				
				//내가 운영하는 그룹정보
				SwoGroupCond groupCond = new SwoGroupCond();
				groupCond.setGroupLeader(user.getId());
				SwoGroup[] groups = getSwoManager().getGroups(user.getId(), groupCond, IManager.LEVEL_ALL);
				
				long totalRequestSize = 0;
				if (groups != null) {
					Map<Long, Map<SwoGroupMember, SwoGroup>> joinRequestDateMap = new HashMap<Long, Map<SwoGroupMember, SwoGroup>>();
					for (int i = 0; i < groups.length; i++) {
						SwoGroup group = groups[i];
						SwoGroupMember[] groupMember = group.getSwoGroupMembers();
						if (groupMember == null || groupMember.length == 0)
							continue;
						for (int j = 0; j < groupMember.length; j++) {
							SwoGroupMember member = groupMember[j];
							String joinType = member.getJoinType();
							String joinStatus = member.getJoinStatus();
							if (joinType.equalsIgnoreCase(SwoGroupMember.JOINTYPE_REQUEST) && joinStatus.equalsIgnoreCase(SwoGroupMember.JOINSTATUS_READY)) {
								
								UserInfo memberInfo = ModelConverter.getUserInfoByUserId(member.getUserId());
								NoticeMessage joinRequestNotice = new NoticeMessage(group.getId() + "_" + member.getUserId(), 0, memberInfo, new LocalDate(member.getCreationDate().getTime()));
								joinRequestNotice.setGroup(ModelConverter.getGroupInfoBySwoGroup(null, group));
								joinRequestNotice.setType(NoticeMessage.TYPE_JOIN_REQUEST);
								joinRequestNotice.setMessage(memberInfo.getLongName());
								//NoticeMessageList.add(joinRequestNotice);
								NoticeMessageMap.put(member.getCreationDate().getTime(), joinRequestNotice);
								totalRequestSize += 1;
							}
						}
					}
				}	
				//---------------------------------------------------------------------------------------------------------
						
				TreeMap resultTreeMap = new TreeMap(Collections.reverseOrder());
				resultTreeMap.putAll(NoticeMessageMap);
				Iterator itr = resultTreeMap.keySet().iterator();
				
				boolean lastFlag = true;
				if (!CommonUtil.isEmpty(lastNoticeId))
					lastFlag = false;
				
				List NoticeMessageList = new ArrayList();
				
				while (itr.hasNext()) {
					NoticeMessage message = (NoticeMessage)resultTreeMap.get(itr.next());
					if (message.getId().equalsIgnoreCase(lastNoticeId)) {
						lastFlag = true;
						continue;
					}
					if (lastFlag) {
						NoticeMessageList.add(message);
						if (NoticeMessageList.size() == 10)
							break;
					}
				}
				NoticeMessage[] resultNoticeMessage = null;
				if(NoticeMessageList.size() > 0) {
					resultNoticeMessage = new NoticeMessage[NoticeMessageList.size()];
					NoticeMessageList.toArray(resultNoticeMessage);
					notificationNoticeBox.setDateOfLastNotice(resultNoticeMessage[resultNoticeMessage.length-1].getIssuedDate());
					notificationNoticeBox.setRemainingLength((int)(totalRequestSize + totalDelayedTaskSize) - resultNoticeMessage.length);
				}

				notificationNoticeBox.setNoticeMessages(resultNoticeMessage);
				notificationNoticeBox.setNoticeType(Notice.TYPE_NOTIFICATION);
				
				return notificationNoticeBox;
				
			case Notice.TYPE_SAVEDBOX:
				return SmartTest.getNoticeBoxForMe10(noticeType);
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
	 * (non-Javadoc)
	 * 
	 * @see net.smartworks.service.impl.ISmartWorks#getBroadcastingMessages()
	 */
	@Override
	public String[] getBroadcastingMessages() throws Exception {
		try{
			return SmartTest.getBroadcastingMessages();
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}
}
