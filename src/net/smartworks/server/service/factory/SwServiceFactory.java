/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 4. 8.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.service.factory;

import net.smartworks.server.service.ICalendarService;
import net.smartworks.server.service.ICommunityService;
import net.smartworks.server.service.IInstanceService;
import net.smartworks.server.service.IMailService;
import net.smartworks.server.service.INoticeService;
import net.smartworks.server.service.ISeraService;
import net.smartworks.server.service.ISettingsService;
import net.smartworks.server.service.IWorkService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SwServiceFactory {

	public SwServiceFactory() {
		super();
		if (logger.isInfoEnabled())
			logger.info(this.getClass().getName() + " created");
	}

	private static SwServiceFactory factory;
	public synchronized static SwServiceFactory createInstance() {
		if(factory == null) 
			factory = new SwServiceFactory();
		return factory;
	}

	public static SwServiceFactory getInstance() {
		return factory;
	}
	protected final Log logger = LogFactory.getLog(getClass());
	
	private IWorkService workService;
	private IInstanceService instanceService;
	private ICommunityService communityService;
	private INoticeService noticeService;
	private ISettingsService settingsService;
	private IMailService mailService;
	private ICalendarService calendarService;
	private ISeraService seraService;

	public ICommunityService getCommunityService() {
		return communityService;
	}
	public void setCommunityService(ICommunityService communityService) {
		this.communityService = communityService;
	}
	public IInstanceService getInstanceService() {
		return instanceService;
	}
	public void setInstanceService(IInstanceService instanceService) {
		this.instanceService = instanceService;
	}
	public IWorkService getWorkService() {
		return workService;
	}
	public void setWorkService(IWorkService workService) {
		this.workService = workService;
	}
	public INoticeService getNoticeService() {
		return noticeService;
	}
	public void setNoticeService(INoticeService noticeService) {
		this.noticeService = noticeService;
	}
	public ISettingsService getSettingsService() {
		return settingsService;
	}
	public void setSettingsService(ISettingsService settingsService) {
		this.settingsService = settingsService;
	}
	public IMailService getMailService() {
		return mailService;
	}
	public void setMailService(IMailService mailService) {
		this.mailService = mailService;
	}
	public ICalendarService getCalendarService() {
		return calendarService;
	}
	public void setCalendarService(ICalendarService calendarService) {
		this.calendarService = calendarService;
	}
	public ISeraService getSeraService() {
		return seraService;
	}
	public void setSeraService(ISeraService seraService) {
		this.seraService = seraService;
	}

}