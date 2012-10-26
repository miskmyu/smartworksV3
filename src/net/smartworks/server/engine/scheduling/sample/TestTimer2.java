/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 10. 22.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.scheduling.sample;

import net.fortuna.ical4j.model.Date;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class TestTimer2  extends QuartzJobBean  {

	@Override
	protected void executeInternal(JobExecutionContext jobContext) throws JobExecutionException {
		
		JobDataMap jobMap = jobContext.getMergedJobDataMap();
		String name = null;
		if (jobMap != null)
			name = (String)jobMap.get("name");
		
		System.out.println(name + " 스케쥴러2 실행 ");
		
	}

}
