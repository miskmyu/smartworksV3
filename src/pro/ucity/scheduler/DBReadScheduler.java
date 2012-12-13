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

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import net.smartworks.server.engine.factory.SwManagerFactory;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import pro.ucity.model.Adapter;
import pro.ucity.model.OPSituation;
import pro.ucity.util.UcityUtil;

public class DBReadScheduler extends QuartzJobBean  {
	
	
	public static boolean isDbReadSchedulerRunning = false;
	public static long schedulerCount = 0;
	public static Connection connection = null;
	public static DataSource dataSource = null;
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
//		if(dataSource == null){
//			System.out.println("매번 Connection 하는지 체크 한번만 떠야 됨.");
//			try{
//		        Context init = new InitialContext();
//		        Context envinit = (Context)init.lookup("java:comp/env");
//		        dataSource = (DataSource)envinit.lookup("bpm/tibero");
////			    connection = ds.getConnection();			
////				connection = SwManagerFactory.getInstance().getUcityContantsManager().getDataSource().getConnection();
//			}catch (Exception e){
//				java.lang.System.out.println("[ERROR] DB접속 끊김.Thread 종료");
//				return;
//			}
//		}
		
		if(!isDbReadSchedulerRunning){
			try{
//				connection = SwManagerFactory.getInstance().getUcityContantsManager().getDataSource().getConnection();
//				connection = dataSource.getConnection();	
				startScheduler();
//				connection.close();
			}catch (Exception e){
				java.lang.System.out.println("[ERROR] DB접속 끊김.Thread 종료");
				return;
			}
		}else{
			System.out.println("진행중인 스케줄러가 있어 그냥 지나감...!!!");			
		}
	}
	
	synchronized static void startScheduler(){
		isDbReadSchedulerRunning = true;
		schedulerCount++;
		
		if(schedulerCount==1){
			try{
				Thread.sleep(5000);
				System.out.println( schedulerCount + "진행중인 태스크 재시동 시작 : " + new Date());
//				UcityUtil.resumePollingForRunningTasks(null);
				System.out.println( schedulerCount + "진행중인 태스크 재시동 종료 : " + new Date());
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		System.out.println( schedulerCount + "번째 스케쥴러 동작 시작 : " + new Date());
		Adapter.readHistoryTableToStart();
//		OPSituation.readHistoryTableToStart();
		System.out.println( schedulerCount +"번째 스케쥴러 동작 종료 : " + new Date());

		isDbReadSchedulerRunning = false;		
	}
}
