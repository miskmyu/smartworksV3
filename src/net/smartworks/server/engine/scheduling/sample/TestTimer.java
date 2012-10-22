/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 10. 22.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.scheduling.sample;

import net.fortuna.ical4j.model.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class TestTimer  extends QuartzJobBean  {

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		
		System.out.println("스케쥴러 실행 " + new Date().getTime());
		
	}

}
