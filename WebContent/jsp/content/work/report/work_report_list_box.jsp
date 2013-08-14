<%@page import="net.smartworks.model.report.ChartReport"%>
<%@page import="net.smartworks.model.report.info.ReportInfo"%>
<%@page import="net.smartworks.model.filter.info.SearchFilterInfo"%>
<%@page import="net.smartworks.model.report.Report"%>
<%@page import="net.smartworks.model.work.FormField"%>
<%@page import="net.smartworks.model.work.SmartForm"%>
<%@page import="net.smartworks.model.filter.SearchFilter"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.model.security.EditPolicy"%>
<%@page import="net.smartworks.model.security.WritePolicy"%>
<%@page import="net.smartworks.model.security.AccessPolicy"%>
<%@page import="net.smartworks.model.work.InformationWork"%>
<%@page import="net.smartworks.model.work.SmartWork"%>
<%@page import="net.smartworks.model.work.Work"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();

	SmartWork work = (SmartWork)session.getAttribute("smartWork");
	String workId = work.getId();
	String lastReportId = work.getLastReportId();
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<select name="selMyReportList" class="js_select_work_report" href="work_report_view.sw?workId=<%=workId%>&workType=<%=work.getType()%>">							
	<option value="<%=Report.REPORT_ID_NONE %>" 
		<%if(SmartUtil.isBlankObject(lastReportId) || lastReportId.equals(Report.REPORT_ID_NONE)){ %> selected <%} %>>
		<fmt:message key="report.title.no_report" />
	</option>
	<%
	Report[] defaultReports = null;
	if(work.getType() == SmartWork.TYPE_INFORMATION) defaultReports =  ChartReport.DEFAULT_CHARTS_INFORMATION;
	else if(work.getType() == SmartWork.TYPE_PROCESS) defaultReports =  ChartReport.DEFAULT_CHARTS_PROCESS;
	else if(work.getType() == SmartWork.TYPE_SCHEDULE) defaultReports =  ChartReport.DEFAULT_CHARTS_SCHEDULE;
	if (defaultReports != null) {
		for (Report report : defaultReports) {
			String chartType = null;
			if(report.getType() == Report.TYPE_CHART) chartType = ((ChartReport)report).getChartTypeInString();
	%>
			<option value="<%=report.getId()%>" reportType="<%=report.getType()%>" <%if(chartType!=null){ %>chartType="<%=chartType%>"<%}%>
				<%if(report.getId().equals(lastReportId)){ %> selected <%} %>><%=report.getName()%>
			</option>
	<%
		}
	}
	ReportInfo[] reports = work.getReports();
	if (reports != null) {
		for (ReportInfo report : reports) {
			String chartType = report.getChartTypeInString();
			if(SmartUtil.isBlankObject(report.getId())) continue;
	%>
			<option value="<%=report.getId()%>" reportType="<%=report.getType()%>" <%if(chartType!=null){ %>chartType="<%=chartType%>"<%}%>
				<%if(report.getId().equals(lastReportId)){ %> selected <%} %>><%=report.getName()%>
			</option>
	<%
		}
	}
	%>
</select>