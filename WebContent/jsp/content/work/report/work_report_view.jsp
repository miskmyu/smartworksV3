<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.model.report.MatrixReport"%>
<%@page import="net.smartworks.model.KeyMap"%>
<%@page import="net.smartworks.model.work.InformationWork"%>
<%@page import="net.smartworks.model.work.FormField"%>
<%@page import="net.smartworks.model.report.ChartReport"%>
<%@page import="net.smartworks.model.report.Report"%>
<%@page import="net.smartworks.model.work.SmartWork"%>
<%@page import="net.smartworks.model.work.Work"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.util.SmartTest"%>
<%@page import="net.smartworks.model.community.User"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>

<%
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	String strReportType = request.getParameter("reportType");
	String chartType = request.getParameter("chartType");
	User cUser = SmartUtil.getCurrentUser();
	
	int reportType = SmartUtil.isBlankObject(strReportType) ? -1 : Integer.parseInt(strReportType);

%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />
<!--  전체 레이아웃 -->
<div class="js_work_report_view_page" reportType="<%=reportType%>" isChartView="true">

	<%
	if(reportType == Report.TYPE_CHART){
	%>
	
		<!-- 컨텐츠 -->
		<div class="fl mt5 ml20">
			<a href="" class="js_toggle_chart_table"><fmt:message key="report.button.view_table"/></a>
			<a href="" class="js_toggle_chart_table" style="display:none"><fmt:message key="report.button.view_chart"/></a>
		</div>
		<div class="fr mt5 ml10 js_stacked_chart" <%if(!chartType.equals(ChartReport.CHART_TYPES_STRING[ChartReport.CHART_TYPE_BAR]) 
					&& !chartType.equals(ChartReport.CHART_TYPES_STRING[ChartReport.CHART_TYPE_COLUMN])){ %> style="display:none" <%} %>>
			<div><fmt:message key="report.button.stacked_chart"/><input type="checkbox" name="chkStackedChart" class="js_change_stacked_chart"></div>
		</div>
		<div class="fr js_chart_type">
			<select name="selReportChartType" class="js_change_chart_type">
				<option
					value="<%=ChartReport.CHART_TYPES_STRING[ChartReport.CHART_TYPE_COLUMN]%>"
					<%if (chartType.equals(ChartReport.CHART_TYPES_STRING[ChartReport.CHART_TYPE_COLUMN])) {%>
					selected <%}%>>
					<fmt:message key="report.chart.type.column" />
				</option>
				<option
					value="<%=ChartReport.CHART_TYPES_STRING[ChartReport.CHART_TYPE_BAR]%>"
					<%if (chartType.equals(ChartReport.CHART_TYPES_STRING[ChartReport.CHART_TYPE_BAR])) {%>
					selected <%}%>>
					<fmt:message key="report.chart.type.bar" />
				</option>
				<option
					value="<%=ChartReport.CHART_TYPES_STRING[ChartReport.CHART_TYPE_LINE]%>"
					<%if (chartType.equals(ChartReport.CHART_TYPES_STRING[ChartReport.CHART_TYPE_LINE])) {%>
					selected <%}%>>
					<fmt:message key="report.chart.type.line" />
				</option>
				<option
					value="<%=ChartReport.CHART_TYPES_STRING[ChartReport.CHART_TYPE_PIE]%>"
					<%if (chartType.equals(ChartReport.CHART_TYPES_STRING[ChartReport.CHART_TYPE_PIE])) {%>
					selected <%}%>>
					<fmt:message key="report.chart.type.pie" />
				</option>
				<option
					value="<%=ChartReport.CHART_TYPES_STRING[ChartReport.CHART_TYPE_AREA]%>"
					<%if (chartType.equals(ChartReport.CHART_TYPES_STRING[ChartReport.CHART_TYPE_AREA])) {%>
					selected <%}%>>
					<fmt:message key="report.chart.type.area" />
				</option>
	<%-- 			<option
					value="<%=ChartReport.CHART_TYPES_STRING[ChartReport.CHART_TYPE_GAUGE]%>"
					<%if (chartType.equals(ChartReport.CHART_TYPES_STRING[ChartReport.CHART_TYPE_GAUGE])) {%>
					selected <%}%>>
					<fmt:message key="report.chart.type.gauge" />
				</option>
	 --%>			<option
					value="<%=ChartReport.CHART_TYPES_STRING[ChartReport.CHART_TYPE_RADAR]%>"
					<%if (chartType.equals(ChartReport.CHART_TYPES_STRING[ChartReport.CHART_TYPE_RADAR])) {%>
					selected <%}%>>
					<fmt:message key="report.chart.type.radar" />
				</option>
				<option
					value="<%=ChartReport.CHART_TYPES_STRING[ChartReport.CHART_TYPE_SCATTER]%>"
					<%if (chartType.equals(ChartReport.CHART_TYPES_STRING[ChartReport.CHART_TYPE_SCATTER])) {%>
					selected <%}%>>
					<fmt:message key="report.chart.type.scatter" />
				</option>
			</select>
			
		</div>
	<%
	}else if(reportType == Report.TYPE_MATRIX){
	%>
	<%
	}else if(reportType == Report.TYPE_TABLE){
	%>
	<%
	}
	%>
	<br/>
	<div id="chart_target">
	</div>
</div>
<!-- 전체 레이아웃//-->