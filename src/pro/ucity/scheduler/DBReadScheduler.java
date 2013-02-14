/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 9. 20.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package pro.ucity.scheduler;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import pro.ucity.model.Adapter;
import pro.ucity.model.OPSituation;
import pro.ucity.util.UcityUtil;


@Service("DBReadScheduler")
public class DBReadScheduler{
	
	
	public static boolean isDbReadSchedulerRunning = false;
	public static long schedulerCount = 0;
	
	private static Logger logger = Logger.getLogger(DBReadScheduler.class);
	
	
	public void startDb() {
		
		if(!isDbReadSchedulerRunning){
			try{
				startScheduler();
			}catch (Exception e){
				logger.error("[ERROR] DB접속 끊김.Thread 종료 : DBReadScheduler");
				return;
			}
		}else{
			logger.info("진행중인 스케줄러가 있습니다.");
			return;
		}
	}
	
	public synchronized void startScheduler(){
		isDbReadSchedulerRunning = true;
		schedulerCount++;
		
		if(schedulerCount==1){
			try{
				Thread.sleep(5000);
				logger.info( schedulerCount + "진행중인 태스크 재시동 시작 : " + new Date());
				UcityUtil.resumePollingForRunningTasks(null);
				logger.info( schedulerCount + "진행중인 태스크 재시동 종료 : " + new Date());
			}catch (Exception e){
				logger.error("startScheduler error : DBReadScheduler.startScheduler.78");
			}
		}
		Adapter.scheduler();
		OPSituation.scheduler();

		isDbReadSchedulerRunning = false;	
		
		return;
	}
}
