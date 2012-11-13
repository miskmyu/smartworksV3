/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 11. 9.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.impl.StdScheduler;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class SwServletContextListener implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		System.out.println("SmartWorks Schedulers Kill !!!!!!!!!!!!!!!!!!!!!!!!!!");
		if (event!=null && event.getServletContext()!=null) {
	        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
	        StdScheduler sch = (StdScheduler) ctx.getBean("schedulerFactoryBean");
	        if (sch != null)
	        	sch.shutdown();
	    }
	}
}
