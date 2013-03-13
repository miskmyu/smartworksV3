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
import net.smartworks.model.notice.NoticeMessage;
import net.smartworks.server.engine.common.manager.IManager;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.engine.infowork.domain.model.SwdRecord;
import net.smartworks.server.engine.organization.model.SwoUser;
import net.smartworks.server.engine.publishnotice.model.AlarmNotice;
import net.smartworks.server.engine.publishnotice.model.AlarmNoticeCond;
import net.smartworks.server.service.util.ModelConverter;
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
			int currentMinute = (int)(new Date()).getTime()/LocalDate.ONE_MINUTE;
			Date currentTime = new Date(currentMinute*LocalDate.ONE_MINUTE);
			
			AlarmNoticeCond cond = new AlarmNoticeCond();
			cond.setNoticeTime(currentTime);
			long alarmNoticeSize = SwManagerFactory.getInstance().getPublishNoticeManager().getAlarmNoticeSize("", cond);
			if(alarmNoticeSize>0){
				AlarmNotice[] alarmNotices = SwManagerFactory.getInstance().getPublishNoticeManager().getAlarmNotices("", cond, IManager.LEVEL_LITE);
				if(alarmNotices!=null){
					for(int i=0; i<alarmNotices.length; i++){
						AlarmNotice alarmNotice = alarmNotices[i];

						SwoUser targetUser = SwManagerFactory.getInstance().getSwoManager().getUser("", alarmNotice.getTargetUser(), IManager.LEVEL_ALL);
						UserInfo user =ModelConverter.getUserInfoByUserId(alarmNotice.getTargetUser());
						NoticeMessage noticeMessage = new NoticeMessage();
						noticeMessage.setType(NoticeMessage.TYPE_EVENT_ALARM);
						noticeMessage.setIssuedDate(new LocalDate((new Date()).getTime(), targetUser.getTimeZone(), targetUser.getLocale()));
						noticeMessage.setIssuer(ModelConverter.getUserInfoByUserId(targetUser.getId()));
						
//						SwdRecord record = SwManagerFactory.getInstance().getSwdManager().getRecord(userId, domain.getObjId(), instanceId, IManager.LEVEL_ALL);
//
//						EventInstanceInfo eventInstance = ModelConverter.get
//						noticeMessage.setEvent(event)
						SmartUtil.pushEventAlarm(alarmNotice.getTargetUser(), alarmNotice.getCompanyId(),noticeMessage);
											
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
