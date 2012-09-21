/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 9. 20.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package pro.ucity.scheduler;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class TesterScheduler extends QuartzJobBean  {

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {

		System.out.println(" 1 스케쥴러 동작 시간 : " + new Date());
		
	}
	
}
