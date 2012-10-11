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

import pro.ucity.model.Adapter;
import pro.ucity.model.OPSituation;
import pro.ucity.util.UcityUtil;

public class DBReadScheduler extends QuartzJobBean  {
	
	
	public static boolean isDbReadSchedulerRunning = false;
	public static long schedulerCount = 0;
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
		if(schedulerCount==1){
			try{
				System.out.println( schedulerCount + "진행중인 태스크 재시동 시작 : " + new Date());
//				UcityUtil.resumePollingForRunningTasks(null);
				System.out.println( schedulerCount + "진행중인 태스크 재시동 종료 : " + new Date());
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		System.out.println( schedulerCount + "번째 스케쥴러 동작 시작 : " + new Date());
		Adapter.readHistoryTableToStart();
		OPSituation.readHistoryTableToStart();
		System.out.println( schedulerCount +"번째 스케쥴러 동작 종료 : " + new Date());
		isDbReadSchedulerRunning = false;		
	}
}
