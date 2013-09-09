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
import net.smartworks.server.engine.report.Exception.RptException;
import net.smartworks.server.engine.report.model.RptReport;
import net.smartworks.server.engine.report.model.RptReportCond;
import net.smartworks.server.engine.report.model.RptReportPane;
import net.smartworks.server.engine.report.model.RptReportPaneCond;

public interface IReportManager {
	
	public Data getReportData(String user, String dbType, Map<String, Object> requestBody) throws Exception;
	
	public RptReport getRptReport(String user, String objId, String level) throws RptException;
	public RptReport getRptReport(String user, RptReportCond cond, String level) throws RptException;
	public void setRptReport(String user, RptReport obj, String level) throws RptException;
	public void removeRptReport(String user, String objId) throws RptException;
	public void removeRptReport(String user, RptReportCond cond) throws RptException;
	public long getRptReportSize(String user, RptReportCond cond) throws RptException;
	public RptReport[] getRptReports(String user, RptReportCond cond, String level) throws RptException;
	
	public RptReportPane getRptReportPane(String user, String objId, String level) throws RptException;
	public RptReportPane getRptReportPane(String user, RptReportPaneCond cond, String level) throws RptException;
	public void setRptReportPane(String user, RptReportPane obj, String level) throws RptException;
	public void removeRptReportPane(String user, String objId) throws RptException;
	public void removeRptReportPane(String user, RptReportPaneCond cond) throws RptException;
	public long getRptReportPaneSize(String user, RptReportPaneCond cond) throws RptException;
	public RptReportPane[] getRptReportPanes(String user, RptReportPaneCond cond, String level) throws RptException;
	
}
