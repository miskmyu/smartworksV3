package net.smartworks.server.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.smartworks.model.instance.info.InstanceInfoList;
import net.smartworks.model.instance.info.RequestParams;
import net.smartworks.model.report.Data;
import net.smartworks.model.report.Report;
import net.smartworks.model.report.ReportPane;

public interface IReportService {

	public Report getReportById(String reportId) throws Exception;

	public Data getReportData(HttpServletRequest request) throws Exception;

	public Data getReportDataByDef(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public String setWorkReport(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public String setWorkReportPane(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public int getUserReportCount(String targetWorkId) throws Exception;

	public InstanceInfoList getReportInstanceList(String targetWorkId, int targetWorkType, String producedBy, RequestParams params) throws Exception;

	public ReportPane[] getMyDashboard() throws Exception;
	
}
