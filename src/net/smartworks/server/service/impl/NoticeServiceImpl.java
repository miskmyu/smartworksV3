/*	
 * $Id$
 * created by    : SHIN HYUN SEONG
 * creation-date : 2011. 10. 31.
 * =========================================================
 * Copyright (c) 2011 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import net.smartworks.server.engine.common.manager.IManager;
import net.smartworks.server.engine.common.model.Order;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.engine.message.model.Message;
import net.smartworks.server.engine.message.model.MessageCond;
import net.smartworks.server.engine.opinion.model.Opinion;
import net.smartworks.server.engine.opinion.model.OpinionCond;
import net.smartworks.server.engine.process.process.model.PrcProcessInst;
import net.smartworks.server.engine.process.task.model.TskTask;
import net.smartworks.server.engine.process.task.model.TskTaskCond;
import net.smartworks.server.engine.worklist.model.TaskWork;
import net.smartworks.server.engine.worklist.model.TaskWorkCond;
import net.smartworks.server.service.IMailService;
import net.smartworks.server.service.INoticeService;
import net.smartworks.server.service.util.ModelConverter;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartTest;
import net.smartworks.util.SmartUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class NoticeServiceImpl implements INoticeService {

	private IMailService mailService;

	@Autowired
	public void setMailService(IMailService mailService){
		this.mailService = mailService;
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
			return new Notice[] {};//SmartTest.getNoticesForMe();
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
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
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
	@Override
	public NoticeBox getNoticeBoxForMe10(int noticeType, String lastNoticeId) throws Exception {

		try{
			User user = SmartUtil.getCurrentUser();
			
			switch(noticeType){
			case Notice.TYPE_MAILBOX:
				RequestParams params = new RequestParams();
				params.setPageSize(10);
				params.setCurrentPage(1);
				params.setSortingField(new SortingField("date", false));
				InstanceInfoList mailsList =  mailService.getMailInstanceList(MailFolder.ID_INBOX, params);
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
					TskTask[] lastTasks = SwManagerFactory.getInstance().getTskManager().getTasks(user.getId(), lastTaskCond, IManager.LEVEL_LITE);
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
				
				long totalSize = SwManagerFactory.getInstance().getWorkListManager().getTaskWorkListSize(user.getId(), taskCond);
				
				taskCond.setOrders(new Order[]{new Order("tskCreatedate", false)});
				
				taskCond.setPageSize(10);
				TaskWork[] tasks = SwManagerFactory.getInstance().getWorkListManager().getTaskWorkList(user.getId(), taskCond);
				
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
				
				TskTask[] myTask = SwManagerFactory.getInstance().getTskManager().getTasks(user.getId(), myTaskCond, IManager.LEVEL_LITE);
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
				
				long totalCommentSize = SwManagerFactory.getInstance().getOpinionManager().getOpinionSize(user.getId(), opinionCond);
				
				opinionCond.setOrders(new Order[]{new Order(Opinion.A_CREATIONDATE, false)});
				opinionCond.setPageNo(0);
				opinionCond.setPageSize(10);
				
				if (!CommonUtil.isEmpty(lastNoticeId)) {
					Opinion lastOpinion = SwManagerFactory.getInstance().getOpinionManager().getOpinion(user.getId(), lastNoticeId, null);
					if (lastOpinion != null) {
						opinionCond.setWrittenDateTo(lastOpinion.getCreationDate());
					}
				}
				
				Opinion[] opinions = SwManagerFactory.getInstance().getOpinionManager().getOpinions(user.getId(), opinionCond, IManager.LEVEL_ALL);

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
				
				long totalMessageSize = SwManagerFactory.getInstance().getMessageManager().getMessageSize(user.getId(), messageCond);

				messageCond.setOrders(new Order[]{new Order(Opinion.A_CREATIONDATE, false)});
				messageCond.setPageNo(0);
				messageCond.setPageSize(10);
				
				if (!CommonUtil.isEmpty(lastNoticeId)) {
					Message lastMessage = SwManagerFactory.getInstance().getMessageManager().getMessage(user.getId(), lastNoticeId, null);
					if (lastMessage != null) {
						messageCond.setCreationDateTo(lastMessage.getCreationDate());
					}
				}
				
				Message[] messages = SwManagerFactory.getInstance().getMessageManager().getMessages(user.getId(), messageCond, IManager.LEVEL_ALL);
				
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
				
				NoticeMessage noticeMessate = new NoticeMessage();
				
				
				
				
				
				
				
				
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
