/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 10. 24.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.scheduling.manager;

import org.quartz.impl.StdScheduler;

import net.smartworks.server.engine.scheduling.exception.ScheduleException;
import net.smartworks.server.engine.scheduling.model.ScheduleDef;
import net.smartworks.server.engine.scheduling.model.ScheduleDefCond;

public interface IScheduleManager {

	public ScheduleDef getScheduleDef(String userId, String id, String level) throws ScheduleException;
	public ScheduleDef getScheduleDef(String userId, ScheduleDefCond cond, String level) throws ScheduleException;
	public void setScheduleDef(String userId, ScheduleDef obj, String level) throws ScheduleException;
	public void removeScheduleDef(String userId, String id) throws ScheduleException;
	public void removeScheduleDef(String userId, ScheduleDefCond cond) throws ScheduleException;
	public long getScheduleDefSize(String userId, ScheduleDefCond cond) throws ScheduleException;
	public ScheduleDef[] getScheduleDefs(String userId, ScheduleDefCond cond, String level) throws ScheduleException;
	
	public void fireScheduleDef(String userId, ScheduleDef obj, StdScheduler scheduleFactory) throws ScheduleException;
	public void stopScheduleDef(String userId, ScheduleDef obj, StdScheduler scheduleFactory) throws ScheduleException;
}
