/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 11. 9.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.listener;

import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdScheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import pro.ucity.util.UcityUtil;

public class SwServletContextListener implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent event) {
//		System.out.println("SmartWorks Server Down Begin! - SwServletContextlistener !!!!!!!!!!!!!!!!!!!");
//		try {
//			UcityUtil.stopAllThread();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		System.out.println("SmartWorks Server Down Done! - SwServletContextlistener !!!!!!!!!!!!!!!!!!!");
//		
//		System.out.println("SmartWorks Schedulers Kill !!!!!!!!!!!!!!!!!!!!!!!!!!");
//		if (event!=null && event.getServletContext()!=null) {
//	        ServletContext context = event.getServletContext();
//	        //StdSchedulerFactory sch = (StdSchedulerFactory) context.getAttribute("org.quartz.impl.StdSchedulerFactory.KEY");
//	        
//		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
//	        StdScheduler sch = (StdScheduler) ctx.getBean("schedulerFactoryBean");
//	        if (sch != null)
//	        	sch.shutdown();
	        
	        /*if(sch!=null){
	            try {
	                Collection<Scheduler> col = sch.getAllSchedulers();
	        		System.out.println("SmartWorks Schedulers col Size : " + col.size());
	                for(Scheduler s:col){ 
	                	System.out.println("SmartWorks Schedulers col Kill : " + s.getSchedulerName());
	                    s.shutdown();
	                }
	            } catch (SchedulerException e) {
	                e.printStackTrace();
	            }
	        }*/
		try {
			UcityUtil.stopAllThread();
			
			Thread.sleep(3000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try
        {
            // Get a reference to the Scheduler and shut it down
            WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
            Scheduler scheduler = (Scheduler) context.getBean("schedulerFactoryBean");

//            System.out.println("scheduler null!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            try{
            	scheduler.shutdown(true);
            }catch(Exception e){
            	e.printStackTrace();
            	UcityUtil.stopAllThread();
            	scheduler.shutdown();
            }
//            System.out.println("scheduler boolean!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            try {
    			UcityUtil.stopAllThread();
    			
    			Thread.sleep(3000);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
            scheduler.shutdown();
            // Sleep for a bit so that we don't get any errors
            Thread.sleep(1000);
        }
			catch (Exception e)
        {
            e.printStackTrace();
        }
	}
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("SmartWorks Server Start Begin! - SwServletContextlistener !!!!!!!!!!!!!!!!!!");
		System.out.println("SmartWorks Server Start Done! - SwServletContextlistener !!!!!!!!!!!!!!!!!!");
	}
}