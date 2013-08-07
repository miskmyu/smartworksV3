/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2013. 3. 13.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.scheduling.schedulJob;

import java.util.Date;

import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.community.info.WorkSpaceInfo;
import net.smartworks.model.instance.info.EventInstanceInfo;
import net.smartworks.model.notice.Notice;
import net.smartworks.model.notice.NoticeMessage;
import net.smartworks.model.work.FormField;
import net.smartworks.model.work.SmartForm;
import net.smartworks.server.engine.common.manager.IManager;
import net.smartworks.server.engine.common.model.Filter;
import net.smartworks.server.engine.common.model.Order;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.engine.infowork.domain.model.SwdDomain;
import net.smartworks.server.engine.infowork.domain.model.SwdDomainCond;
import net.smartworks.server.engine.infowork.domain.model.SwdRecord;
import net.smartworks.server.engine.organization.model.SwoUser;
import net.smartworks.server.engine.process.task.model.TskTask;
import net.smartworks.server.engine.process.task.model.TskTaskCond;
import net.smartworks.server.engine.publishnotice.model.AlarmNotice;
import net.smartworks.server.engine.publishnotice.model.AlarmNoticeCond;
import net.smartworks.server.engine.publishnotice.model.MessageNotice;
import net.smartworks.server.service.util.ModelConverter;
import net.smartworks.service.ISmartWorks;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartUtil;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class EventNoticeJob  extends QuartzJobBean   {
	
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		try {
			
			System.out.println("Event Scheduler Start!!!");
			LocalDate localDate = new LocalDate((new Date()).getTime());
			LocalDate currentTime = LocalDate.convertLocalDateTimeStringToLocalDate(localDate.toDateTimeSimpleString());
			
			AlarmNoticeCond cond = new AlarmNoticeCond();
			Filter filter = new Filter();
			filter.setLeftOperandType(Filter.OPERANDTYPE_STRING);
			filter.setLeftOperandValue(AlarmNotice.A_NOTICETIME);
			filter.setOperator("<=");
			filter.setRightOperandType(Filter.OPERANDTYPE_DATE);
			filter.setRightOperandValue(currentTime.toGMTDateString());
			cond.setFilter(new Filter[]{filter});
			long alarmNoticeSize = SwManagerFactory.getInstance().getPublishNoticeManager().getAlarmNoticeSize("", cond);
			if(alarmNoticeSize>0){
				AlarmNotice[] alarmNotices = SwManagerFactory.getInstance().getPublishNoticeManager().getAlarmNotices("", cond, IManager.LEVEL_LITE);
				if(alarmNotices!=null){
					for(int i=0; i<alarmNotices.length; i++){
						AlarmNotice alarmNotice = alarmNotices[i];

						SwoUser targetUser = SwManagerFactory.getInstance().getSwoManager().getUser("", alarmNotice.getTargetUser(), IManager.LEVEL_ALL);
						if (targetUser == null)
							continue;
						UserInfo user =ModelConverter.getUserInfoByUserId(alarmNotice.getTargetUser());
						NoticeMessage noticeMessage = new NoticeMessage();
						noticeMessage.setType(NoticeMessage.TYPE_EVENT_ALARM);
						noticeMessage.setIssuedDate(new LocalDate((new Date()).getTime(), targetUser.getTimeZone(), targetUser.getLocale()));
						noticeMessage.setIssuer(ModelConverter.getUserInfoByUserId(targetUser.getId()));
						
						SwdDomainCond domainCond = new SwdDomainCond();
						domainCond.setFormId(SmartForm.ID_EVENT_MANAGEMENT);
						SwdDomain domain = SwManagerFactory.getInstance().getSwdManager().getDomain(alarmNotice.getTargetUser(), domainCond, IManager.LEVEL_LITE);
						SwdRecord record = SwManagerFactory.getInstance().getSwdManager().getRecord(alarmNotice.getTargetUser(), domain.getObjId(), alarmNotice.getRecordId(), IManager.LEVEL_ALL);

						EventInstanceInfo eventInstance = new EventInstanceInfo();
						if(record!=null){
							eventInstance.setId(record.getRecordId());
							eventInstance.setSubject(record.getDataField(domain.getTitleFieldId()).getValue());
							String startTimeStr = record.getDataField(FormField.ID_NUM_EVENT_START_TIME).getValue();
							String endTimeStr = record.getDataField(FormField.ID_NUM_EVENT_END_TIME).getValue();
							LocalDate startTime = null;
							LocalDate endTime = null;
							if(startTimeStr != null){
								try{
									startTime = new LocalDate(LocalDate.convertGMTStringToLocalDate(startTimeStr).getTime(), targetUser.getTimeZone(), targetUser.getLocale());
								}catch (Exception e){}
							}
							if(endTimeStr != null){
								try{
									endTime = new LocalDate(LocalDate.convertGMTStringToLocalDate(endTimeStr).getTime(), targetUser.getTimeZone(), targetUser.getLocale());
								}catch (Exception e){}
							}
							String noticeId = CommonUtil.newId();
							eventInstance.setStart(startTime);
							eventInstance.setEnd(endTime);
							eventInstance.setOwner(user);
							eventInstance.setWorkId(alarmNotice.getWorkId());
							WorkSpaceInfo workSpace = ModelConverter.getWorkSpaceInfo(CommonUtil.toDefault(record.getWorkSpaceType(), String.valueOf(ISmartWorks.SPACE_TYPE_USER)), CommonUtil.toDefault(record.getWorkSpaceId(), alarmNotice.getTargetUser()));
							eventInstance.setWorkSpaceInfo(workSpace);
							noticeMessage.setEvent(eventInstance);
							noticeMessage.setId(noticeId);
							SmartUtil.pushEventAlarm(alarmNotice.getTargetUser(), alarmNotice.getCompanyId(),noticeMessage);

							MessageNotice messageNotice = new MessageNotice();
							messageNotice.setObjId(noticeId);
							messageNotice.setType("" + NoticeMessage.TYPE_EVENT_ALARM);
							messageNotice.setAssignee(alarmNotice.getTargetUser());
							messageNotice.setCompanyId(alarmNotice.getCompanyId());
							messageNotice.setRefId(alarmNotice.getRecordId());
							messageNotice.setRefType("RECORD");
							messageNotice.setWorkId(alarmNotice.getWorkId());
							messageNotice.setWorkSpaceId(record.getWorkSpaceId());
							messageNotice.setWorkSpaceType(record.getWorkSpaceType());
							
							SwManagerFactory.getInstance().getPublishNoticeManager().setMessageNotice(alarmNotice.getTargetUser(), messageNotice, IManager.LEVEL_ALL);
							SmartUtil.increaseNoticeCountByNoticeType(alarmNotice.getTargetUser(), Notice.TYPE_NOTIFICATION);
							
						}
											
						cond = new AlarmNoticeCond();
						cond.setObjId(alarmNotice.getObjId());
						SwManagerFactory.getInstance().getPublishNoticeManager().removeAlarmNotice("", cond);
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
