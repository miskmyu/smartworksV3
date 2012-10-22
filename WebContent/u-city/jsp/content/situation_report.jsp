<%@page import="net.smartworks.model.report.info.ReportInfo"%>
<%@page import="net.smartworks.model.security.AccessPolicy"%>
<%@page import="net.smartworks.model.filter.info.SearchFilterInfo"%>
<%@page import="net.smartworks.model.filter.SearchFilter"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
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
	User cUser = SmartUtil.getCurrentUser();

	SmartWork work = (SmartWork)session.getAttribute("smartWork");
	String workId = work.getId();
	String lastReportId = work.getLastReportId();
	Report lastReport = null;
	int lastReportType = -1;
	String lastChartType = null;
	if(lastReportId != null){
		lastReport = smartWorks.getReportById(lastReportId);
		lastReportType = lastReport.getType();
		if(lastReport.getType() == Report.TYPE_CHART) lastChartType = ((ChartReport)lastReport).getChartTypeInString();
	}
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!--  전체 레이아웃 -->
<div class="js_work_report_page" workId="<%=workId %>" reportId="<%=lastReportId%>" reportType="<%=lastReportType %>" chartType="<%=lastChartType%>">
	
	<div class="list_title_space">
					<div class="title monitoring mb2">통합 상황 모니터링</div>
	
		<!-- 우측 영역 -->
		<div class="fr txt_btn js_work_report_list_box" style="position: relative; top: 5px;">
			<a class="js_view_situation_report" href="situationReportView.sw?workId=<%=workId%>&workType=<%=work.getType()%>">상황발생 추이 보기</a>
			<a class="js_close_situation_report" href="" style="display:none">상황발생 추이 닫기</a>
		</div>			
		<!-- 우측 영역 //-->
	</div>

	<div class="cb"></div>
		<div class="js_work_report_view border_no_topline" style="display:none;"></div>
<!-- 전체 레이아웃//-->
