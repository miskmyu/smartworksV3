/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 9. 28.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.report.manager;

import java.util.Map;

import net.smartworks.model.report.Data;

public interface IReportManager {
	
	public Data getReportData(String dbType, Map<String, Object> requestBody) throws Exception;
	
}
