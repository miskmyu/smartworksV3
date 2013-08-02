<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.model.report.ReportPane"%>
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
	String paneId = request.getParameter("paneId");
	String paneName = SmartUtil.unescape(request.getParameter("paneName"));
	String paneColumnSpans = request.getParameter("paneColumnSpans");
	String panePosition = request.getParameter("panePosition");
	String targetWorkName = SmartUtil.unescape(request.getParameter("targetWorkName"));
	String targetWorkIcon = request.getParameter("targetWorkIcon");
	String reportId = request.getParameter("reportId");
	String reportName = SmartUtil.unescape(request.getParameter("reportName"));
	String strReportType = request.getParameter("reportType");
	String chartType = request.getParameter("chartType");
	String isChartView = request.getParameter("isChartView");
	String isStacked = request.getParameter("isStacked");
	String showLegend = request.getParameter("showLegend");
	String stringLabelRotation = request.getParameter("stringLabelRotation");
	User cUser = SmartUtil.getCurrentUser();
	
	int reportType = SmartUtil.isBlankObject(strReportType) ? -1 : Integer.parseInt(strReportType);

	LocalDate now = new LocalDate();
	String borderClass;
	String alignCss;
	if(reportType == Report.TYPE_MATRIX || (reportType == Report.TYPE_CHART && !"true".equals(isChartView))){
		borderClass =  "";
		alignCss = ";vertical-align:top;";
	}else{
		borderClass =  "border";
		alignCss = "";		
	}
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />
<!--  전체 레이아웃 -->
<div class="list_title_space js_work_report_pane_page js_work_report_view_page pane_action_item" style="display:none;<%=alignCss%>" 
		paneId="<%=paneId %>" paneName="<%=paneName %>" paneColumnSpans="<%=paneColumnSpans %>" panePosition="<%=panePosition%>" targetWorkName="<%=targetWorkName %>" targetWorkIcon="<%=targetWorkIcon %>" reportId="<%=reportId %>" reportName="<%=reportName %>" reportType="<%=reportType%>" chartType="<%=chartType %>" 
		isChartView="<%=isChartView%>" isStacked="<%=isStacked%>" showLegend="<%=showLegend%>" stringLabelRotation="<%=stringLabelRotation%>">

	<div class="js_report_pane_name" style="margin-left:10px;">
		<a href="" class="js_refresh_report_pane title" title="<fmt:message key='common.title.refresh'/>"><%=paneName %></a>
		<a href="" class="t_date vb pl10 js_refresh_report_pane" style="line-height:24px;" title="<fmt:message key='common.title.refresh'/>"><%=now.toLocalString() %></a>
		<span class="js_progress_span"></span>
		<span class="pane_action fr">
			<span title="<fmt:message key='report.button.edit_pane'/>" class="js_edit_report_pane btn_text_category"></span>
			<span title="<fmt:message key='report.button.remove_pane'/>" class="js_remove_report_pane btn_remove_category"></span>
		</span>
	</div>
 	<div id="chart_target_<%=panePosition%>" class="js_chart_target_pane <%=borderClass%>" style="padding:0px;">
	</div>
</div>
<!-- 전체 레이아웃//-->