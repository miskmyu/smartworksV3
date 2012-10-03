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

public class DBReadScheduler extends QuartzJobBean  {
	
	
	public static boolean isDbReadSchedulerRunning = false;
	public static int schedulerCount = 0;
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		if(!isDbReadSchedulerRunning){
			startScheduler();
		}else{
			System.out.println("진행중인 스케줄러가 있어 그냥 지나감...!!!");			
		}
	}
	
	synchronized static void startScheduler(){
		isDbReadSchedulerRunning = true;
		schedulerCount++;
		System.out.println( schedulerCount + "번째 스케쥴러 동작 시작 : " + new Date());
		Adapter.readHistoryTableToStart();
//		OPSituation.readHistoryTableToStart();
		System.out.println( schedulerCount +"번째 스케쥴러 동작 종료 : " + new Date());
		isDbReadSchedulerRunning = false;		
	}
}
