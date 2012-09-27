/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 9. 20.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package pro.ucity.scheduler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import pro.ucity.model.Adapter;
import pro.ucity.model.OPSituation;
import pro.ucity.util.UcityUtil;

public class TesterScheduler2 extends QuartzJobBean  {
	
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
				
		System.out.println("스케쥴러 동작 시간 : " + new Date());
		Adapter.readHistoryTableToStart();
		OPSituation.readHistoryTableToStart();
	}
}
