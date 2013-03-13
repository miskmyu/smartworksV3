/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2013. 3. 13.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.scheduling.schedulJob;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class EventNoticeJob  extends QuartzJobBean   {
	
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		try {
			
			System.out.println("Event Scheduler Start!!!");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
