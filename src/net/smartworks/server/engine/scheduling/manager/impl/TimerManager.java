/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 10. 22.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.scheduling.manager.impl;

import org.quartz.CronTrigger;
import org.quartz.impl.StdScheduler;

public class TimerManager {
	
	private StdScheduler fb;

	public StdScheduler getFb() {
		return fb;
	}
	public void setFb(StdScheduler fb) {
		this.fb = fb;
	}
}
