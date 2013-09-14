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
	String reportId = request.getParameter("reportId");
	String targetWorkId = request.getParameter("targetWorkId");
	User cUser = SmartUtil.getCurrentUser();

	SmartWork work = (SmartWork)session.getAttribute("smartWork");
	String workId = work.getId();
	Report report = null;
	String filterId = "";
	int reportType = Report.TYPE_CHART;
	if(reportId != null && reportId.equals(Report.REPORT_ID_NONE)) reportId = null;
	if (reportId != null) {
		report = smartWorks.getReportById(reportId);
		reportType = report.getType();
		if (report.getSearchFilter() != null)
			filterId = report.getSearchFilter().getId();
	}
	
	String borderClass = workId.equals(SmartWork.ID_REPORT_MANAGEMENT) ? "border" : "border_no_topline";
	int targetWorkType = (report!=null) ? report.getTargetWorkType() : Work.TYPE_NONE;
	int dataSourceType = (report!=null) ? report.getDataSourceType() : Report.DATA_SOURCE_DEFAULT;
	
	String targetWorkName = request.getParameter("targetWorkName");
	String targetWorkIcon = request.getParameter("targetWorkIcon");
	if(!workId.equals(SmartWork.ID_REPORT_MANAGEMENT)){
		targetWorkId = work.getId();
		targetWorkName = work.getFullpathName();
		targetWorkIcon = work.getIconClass();
	}
	
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />
<!--  전체 레이아웃 -->
<div class="<%=borderClass%>">
<form name="frmWorkReport" class="form_contents js_validation_required js_work_report_edit_page js_form_filter_name" workId="<%=workId%>" targetWorkId="<%=targetWorkId %>" reportId="<%=reportId%>" targetWorkName="<%=targetWorkName%>" targetWorkIcon="<%=targetWorkIcon%>">
	<table class="table_normal js_report_title">
		<tr class="js_work_report_name" <%if(!workId.equals(SmartWork.ID_REPORT_MANAGEMENT) && !SmartUtil.isBlankObject(report)){%>style="display:none"<%} %>>
			<th class="required_label" width="200px"><fmt:message key="report.title.report_name" /></th>
			<td colspan="4">
				<input name="txtWorkReportName" <%if(workId.equals(SmartWork.ID_REPORT_MANAGEMENT) && !SmartUtil.isBlankObject(report)){%>disabled="disabled"<%} if(!SmartUtil.isBlankObject(report)){ %> value="<%=report.getName()%>"<%} %> type="text" class="fieldline required" style="width:300px">
			</td>
		</tr>

		<tr class="js_work_report_type">
			<th width="200px"><fmt:message key="report.title.report_type" /></th>
			<td colspan="4" class="">
				<input name="rdoWorkReportType" type="radio" value="<%=Report.TYPE_CHART%>"
					href="work_report_chart.sw?reportId=<%=CommonUtil.toNotNull(reportId)%>"
					<%if (reportType == Report.TYPE_CHART) {%> checked <%}%>><fmt:message key="report.type.chart" />
				<input name="rdoWorkReportType" type="radio" value="<%=Report.TYPE_MATRIX%>"
					href="work_report_matrix.sw?reportId=<%=CommonUtil.toNotNull(reportId)%>"
					<%if (reportType == Report.TYPE_MATRIX) {%> checked <%}%>><fmt:message key="report.type.matrix" />
<%-- 				<input name="rdoWorkReportType" type="radio" value="<%=Report.TYPE_TABLE%>"
					href="work_report_table.sw?reportId=<%=CommonUtil.toNotNull(reportId)%>"
					<%if (reportType == Report.TYPE_TABLE) {%> checked <%}%>> <fmt:message key="report.type.table" />
 --%>			</td>
		</tr>
		<%if(SmartWork.ID_ALL_WORKS.equals(targetWorkId)){ %>
			<tr class="js_target_work_type" reportId="<%=CommonUtil.toNotNull(reportId)%>">
				<th width="200px"><fmt:message key="report.title.target_work_type" /></th>
				<td colspan="4" class="">
					<select name="selTargetWorkType">
						<option value="<%=Work.TYPE_NONE%>" <%if(targetWorkType == Work.TYPE_NONE) {%> selected <%} %> ><fmt:message key='report.title.all_works' /></option>
						<option value="<%=SmartWork.TYPE_INFORMATION%>" <%if(targetWorkType == SmartWork.TYPE_INFORMATION) {%> selected <%} %> ><fmt:message key='report.title.information_works' /></option>
						<option value="<%=SmartWork.TYPE_PROCESS%>" <%if(targetWorkType == SmartWork.TYPE_PROCESS) {%> selected <%} %> ><fmt:message key='report.title.process_works' /></option>
	<%-- 					<option value="<%=SmartWork.TYPE_SCHEDULE%>" <%if(targetWorkType == SmartWork.TYPE_SCHEDULE) {%> selected <%} %> ><fmt:message key='report.title.schedule_works' /></option>
	 --%>			</select>
	 			</td>
			</tr>
		<%} %>
	</table>
	<table class="table_normal js_form_by_report_type">
		<%
		if (reportType == Report.TYPE_CHART) {
		%>
			<jsp:include page="/jsp/content/work/report/work_report_chart.jsp">
				<jsp:param name="reportId" value="<%=CommonUtil.toNotNull(reportId) %>" />
				<jsp:param name="dataSourceType" value="<%=reportType %>" />
				<jsp:param name="reportType" value="<%=reportType %>" />
				<jsp:param name="targetWorkId" value="<%=targetWorkId %>" />
				<jsp:param name="targetWorkType" value="<%=targetWorkType %>" />
			</jsp:include>
		<%
		} else if (reportType == Report.TYPE_MATRIX) {
		%>
			<jsp:include page="/jsp/content/work/report/work_report_matrix.jsp">
				<jsp:param name="reportId" value="<%=CommonUtil.toNotNull(reportId) %>" />
				<jsp:param name="reportType" value="<%=reportType %>" />
				<jsp:param name="targetWorkId" value="<%=targetWorkId %>" />
				<jsp:param name="targetWorkType" value="<%=targetWorkType %>" />
			</jsp:include>
		<%
		} else if (reportType == Report.TYPE_TABLE) {
		%>
			<jsp:include page="/jsp/content/work/report/work_report_table.jsp">
				<jsp:param name="reportId" value="<%=CommonUtil.toNotNull(reportId) %>" />
				<jsp:param name="reportType" value="<%=reportType %>" />
				<jsp:param name="targetWorkId" value="<%=targetWorkId %>" />
				<jsp:param name="targetWorkType" value="<%=targetWorkType %>" />
			</jsp:include>
		<%
		}
		%>
	</table>

	<table class="table_normal js_form_by_report_type">
		<tr class="js_report_search_filter">
			<th width="200px"><fmt:message key="report.title.search_filter" /></th>
			<td colspan="4" class="">
				<select name="selReportFilterName" class="js_select_search_filter">
					<option value="<%=SearchFilter.FILTER_ALL_INSTANCES%>" <%if(SmartUtil.isBlankObject(filterId) || filterId.equals(SearchFilter.FILTER_ALL_INSTANCES)) {%> selected <%} %> ><fmt:message key='filter.name.all_instances' /></option>
					<option value="<%=SearchFilter.FILTER_MY_INSTANCES%>" <%if(filterId.equals(SearchFilter.FILTER_MY_INSTANCES)) {%> selected <%} %> ><fmt:message key='filter.name.my_instances' /></option>
					<option value="<%=SearchFilter.FILTER_RECENT_INSTANCES%>" <%if(filterId.equals(SearchFilter.FILTER_RECENT_INSTANCES)) {%> selected <%} %> ><fmt:message key='filter.name.recent_instances' /></option>
					<option value="<%=SearchFilter.FILTER_MY_RECENT_INSTANCES%>" <%if(filterId.equals(SearchFilter.FILTER_MY_RECENT_INSTANCES)) {%> selected <%} %> ><fmt:message key='filter.name.my_recent_instances' /></option>
					<option value="<%=SearchFilter.FILTER_RECENT_1YEAR_INSTANCES%>" <%if(filterId.equals(SearchFilter.FILTER_RECENT_1YEAR_INSTANCES)) {%> selected <%} %> ><fmt:message key='filter.name.recent_1year_instances' /></option>
					<option value="<%=SearchFilter.FILTER_RECENT_3YEARS_INSTANCES%>" <%if(filterId.equals(SearchFilter.FILTER_RECENT_3YEARS_INSTANCES)) {%> selected <%} %> ><fmt:message key='filter.name.recent_3years_instances' /></option>
					<%
					SearchFilterInfo[] filters = work.getSearchFilters();
					if (filters != null) {
						for (SearchFilterInfo filter : filters) {
							if(SmartUtil.isBlankObject(filter.getId())) continue;
					%>
						<option value="<%=filter.getId()%>" <%if(filterId.equals(filter.getId())) {%> selected <%} %> ><%=filter.getName()%></option>
					<%
						}
					}
					%>
				</select>
				<a href="search_filter.sw?workId=<%=workId%>&targetWorkId=<%=targetWorkId%>" class="js_edit_search_filter" title="<fmt:message key='filter.button.edit_search_filter' />">
					<div class="icon_btn_edit" style="vertical-align:middle;"></div>
				</a>
				<!-- 상세필터 및 새업무등록하기 화면 -->
				<div id="search_filter" class="filter_section"></div>
				<!-- 상세필터 -->
			</td>
		</tr>
	</table>
</form>

<!-- 등록 취소 버튼 -->
<div class="glo_btn_space js_button_space">
	<div class="fr">
		<span class="btn_gray js_button_save_as"   <%if(SmartUtil.isBlankObject(report)){ %>style="display:none" <%} %>> 
			<a href="" class="js_work_report_saveas"> 
				<span class="txt_btn_start"></span> 
				<span class="txt_btn_center"><fmt:message key="common.button.save_as"/></span> 
				<span class="txt_btn_end"></span>
			</a> 
		</span> 
		
		<span class="btn_gray" <%if(SmartUtil.isBlankObject(report) || report.isSystemReport()){ %>style="display:none" <%} %>> 
			<a href="" class="js_work_report_delete"> 
				<span class="txt_btn_start"></span> 
				<span class="txt_btn_center"><fmt:message key="common.button.delete"/></span> 
				<span class="txt_btn_end"></span>
			</a> 
		</span> 
		<span class="btn_gray js_button_save"  <%if(!SmartUtil.isBlankObject(report) && report.isSystemReport()){ %>style="display:none" <%} %>> 
			<a href="" class="js_work_report_save"> 
				<span class="txt_btn_start"></span> 
				<span class="txt_btn_center"><fmt:message key="common.button.save"/></span> 
				<span class="txt_btn_end"></span>
			</a> 
		</span>

		<span class="btn_gray"> 
			<a href="" class="js_work_report_execute"> 
				<span class="txt_btn_start"></span> 
				<span class="txt_btn_center"><fmt:message key="common.button.execute"/></span> 
				<span class="txt_btn_end"></span>
			</a> 
		</span> 
		<span class="btn_gray" <%if(SmartUtil.isBlankObject(report)){ %>style="display:none" <%} %>> 
			<a href="" class="js_work_report_register"> 
				<span class="txt_btn_start"></span> 
				<span class="txt_btn_center"><fmt:message key="common.button.report_register"/></span> 
				<span class="txt_btn_end"></span>
			</a> 
		</span> 
		<span class="btn_gray">
			<a href="" class="js_work_report_close"> 
				<span class="txt_btn_start"></span> 
				<span class="txt_btn_center"><fmt:message key="common.button.cancel"/></span> 
				<span class="txt_btn_end"></span> 
			</a> 
		</span>
	</div>

	<form name="frmAccessPolicy" class="js_validation_required">
		<div class="fr form_space">	
			<select name="selAccessPolicy">
				<option value="<%=AccessPolicy.LEVEL_PUBLIC%>"><fmt:message key="common.security.access.public"/></option>
				<option value="<%=AccessPolicy.LEVEL_PRIVATE%>"><fmt:message key="common.security.access.private"/></option>
			</select>
		</div>
	</form>

	<form name="frmReportSaveAsName" class="pr10 fr js_validation_required" style="display:none">
		<input class="fieldline required" style="width:160px; line-height: 16px" type="text" name="txtReportSaveAsName"/>
	</form>
	
	<!--  실행시 표시되는 프로그래스아이콘을 표시할 공간 -->
	<span class="fr form_space js_progress_span" ></span>
	
	<!-- 실행시 데이터 유효성 검사이상시 에러메시지를 표시할 공간 -->
	<div class="form_space sw_error_message js_report_error_message" style="text-align:right; color: red; line-height: 20px"></div>
</div>
</div>
<!-- 등록 취소 버튼//-->
