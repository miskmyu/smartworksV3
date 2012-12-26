/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 9. 20.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package pro.ucity.scheduler;

import java.sql.Connection;
import java.util.Date;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import pro.ucity.model.Adapter;
import pro.ucity.model.OPSituation;

public class DBReadScheduler extends QuartzJobBean  {
	
	
	public static boolean isDbReadSchedulerRunning = false;
	public static long schedulerCount = 0;
	public static Connection connection = null;
	public static DataSource dataSource = null;
	
	private static Logger logger = Logger.getLogger(DBReadScheduler.class);
	
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		
		if(!isDbReadSchedulerRunning){
			try{
				startScheduler();
			}catch (Exception e){
				logger.error("[ERROR] DB접속 끊김.Thread 종료 : DBReadScheduler");
				return;
			}
		}else{
			logger.info("진행중인 스케쥴려가 있습니다.");	
		}
	}
	
	synchronized static void startScheduler(){
		isDbReadSchedulerRunning = true;
		schedulerCount++;
		
		if(schedulerCount==1){
			try{
				Thread.sleep(5000);
				logger.info( schedulerCount + "진행중인 태스크 재시동 시작 : " + new Date());
//				UcityUtil.resumePollingForRunningTasks(null);
				logger.info( schedulerCount + "진행중인 태스크 재시동 종료 : " + new Date());
			}catch (Exception e){
				logger.error("startScheduler error : DBReadScheduler.startScheduler.78");
			}
		}
		logger.info( schedulerCount + "번째 스케쥴러 동작 시작 : " + new Date());
		Adapter.readHistoryTableToStart();
		OPSituation.readHistoryTableToStart();
		logger.info( schedulerCount +"번째 스케쥴러 동작 종료 : " + new Date());

		isDbReadSchedulerRunning = false;		
	}
}
