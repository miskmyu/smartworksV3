/*	
 * $Id$
 * created by    : SHIN HYUN SEONG
 * creation-date : 2011. 10. 31.
 * =========================================================
 * Copyright (c) 2011 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.service.impl;

import java.util.Date;

import net.smartworks.model.community.User;
import net.smartworks.model.instance.SortingField;
import net.smartworks.model.instance.info.InstanceInfo;
import net.smartworks.model.instance.info.InstanceInfoList;
import net.smartworks.model.instance.info.RequestParams;
import net.smartworks.model.mail.MailFolder;
import net.smartworks.model.notice.Notice;
import net.smartworks.model.notice.NoticeBox;
import net.smartworks.model.notice.NoticeMessage;
import net.smartworks.server.engine.common.manager.IManager;
import net.smartworks.server.engine.common.model.Order;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.factory.SwManagerFactory;
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
				
				if (CommonUtil.isEmpty(user.getCompanyId()) || CommonUtil.isEmpty(user.getId()))
					return null;
		
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
					return null;
				
				InstanceInfo[] instInfos = ModelConverter.getInstanceInfoArrayByTaskWorkArray(user.getId(), tasks);
				
				NoticeBox assignTaskNoticeBox = new NoticeBox();
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
				
				
				
				
				
				
				
				
				
				
				
				
				
				
			case Notice.TYPE_MESSAGE:
			case Notice.TYPE_NOTIFICATION:
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
