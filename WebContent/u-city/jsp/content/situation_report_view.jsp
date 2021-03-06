<%@page import="pro.ucity.model.Event"%>
<%@page import="pro.ucity.model.Service"%>
<%@page import="pro.ucity.model.System"%>
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
<script type="text/javascript">
function ExcelPage() {
	var workReport = $('div.js_work_report_page');
	var categoryName = workReport.find('.js_select_ucity_category option:selected').attr('value');
	var periodName = workReport.find('.js_select_ucity_period option:selected').attr('value');
	var serviceName = workReport.find('.js_select_ucity_service option:selected').attr('value');
	var eventName = workReport.find('.js_select_ucity_event:visible option:selected').attr('value');
	var url = "/smartworksV3/ucity_get_chart_excel.sw?categoryName="+ encodeURIComponent(categoryName) +"&periodName="+ encodeURIComponent(periodName) +"&serviceName="+ encodeURIComponent(serviceName) +"&eventName="+ encodeURIComponent(eventName);
	
    document.getElementById('fileDown').src = url;
	document.getElementById('fileDown').location.reload();
}
</script>
<%
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();

	String[] allServices = Service.getAllServiceNames();
	
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />
<!--  전체 레이아웃 -->
<div class="js_work_report_view">

	<!-- 컨텐츠 -->
	<div class="fr mr10">
		<select class="js_select_ucity_category">
			<option selected value="<%=System.REPORT_OPTION_CATEGORY_BY_TIME%>">시간대별</option>
			<option value="<%=System.REPORT_OPTION_CATEGORY_BY_AMPM%>">오전/오후</option>
			<option value="<%=System.REPORT_OPTION_CATEGORY_BY_DAY%>">요일별</option>
			<option value="<%=System.REPORT_OPTION_CATEGORY_BY_MONTH%>">월별</option>
			<option value="<%=System.REPORT_OPTION_CATEGORY_BY_SEASON%>">계절별</option>
			<option value="<%=System.REPORT_OPTION_CATEGORY_BY_QUARTER%>">분기별</option>
			<option value="<%=System.REPORT_OPTION_CATEGORY_BY_HALFYEAR%>">반기별</option>
		</select>
 		<select class="js_select_ucity_period">
			<option value="<%=System.REPORT_OPTION_THIS_YEAR%>">올해</option>
			<option selected value="<%=System.REPORT_OPTION_RECENT_A_YEAR%>">최근1년</option>
			<option value="<%=System.REPORT_OPTION_RECENT_THREE_YEARS%>">최근3년</option>
			<option value="<%=System.REPORT_OPTION_RECENT_FIVE_YEARS%>">최근5년</option>
			<option value="<%=System.REPORT_OPTION_ALL_HISTORY%>">전체</option>
		</select>
		<select class="js_select_ucity_service">
			<option value="<%=System.REPORT_OPTION_ALL_SERVICES%>">모든서비스</option>
			<%
			for(int i=0; i<allServices.length; i++){
			%>
				<option value="<%=allServices[i]%>"><%=allServices[i] %></option>
			<%
			}
			%>
		</select>
		<select class="js_select_ucity_event" service="<%=System.REPORT_OPTION_ALL_SERVICES%>">
			<option value="<%=System.REPORT_OPTION_ALL_EVENTS%>">모든이벤트</option>
		</select>
		<%
		for(int i=0; i<allServices.length; i++){
			String[] events = Event.getAllEventNames(allServices[i]);
			if(!SmartUtil.isBlankObject(events)){
		%>
				<select class="js_select_ucity_event" service="<%=allServices[i]%>" style="display:none">
				<%if(!allServices[i].equalsIgnoreCase("플랫폼")){ %>
					<option value="<%=System.REPORT_OPTION_ALL_EVENTS%>">모든이벤트</option>
					<%}
					for(int j=0; j<events.length; j++){
					%>
						<option value="<%=events[j]%>"><%=events[j] %></option>
					<%
					}
					%>
				</select>
		<%
			}
		}
		%>
		
	</div>
	<div id="chart_target" class="chart_area js_work_report_view" style="height:300px;">
	</div>

	<div class="glo_btn_space">
		<div class="fr">
           <!-- Excel Download 구현 -->
			<span class="btn_gray"> 
				<a href="javascript:ExcelPage()"> 
					<span class="txt_btn_start"></span> 
					<span class="txt_btn_center"><fmt:message key="common.button.excel_download"/></span> 
					<span class="txt_btn_end"></span>
				</a> 
			</span> 	
			<span class="btn_gray"> 
				<a href="" class="js_situation_report_execute"> 
					<span class="txt_btn_start"></span> 
					<span class="txt_btn_center"><fmt:message key="common.button.execute"/></span> 
					<span class="txt_btn_end"></span>
				</a> 
			</span> 
			<span class="btn_gray">
				<a href="" class="js_situation_report_close"> 
					<span class="txt_btn_start"></span> 
					<span class="txt_btn_center"><fmt:message key="common.button.close"/></span> 
					<span class="txt_btn_end"></span> 
				</a> 
			</span>
		</div>
		
		<!--  실행시 표시되는 프로그래스아이콘을 표시할 공간 -->
		<div class="fr form_space js_progress_span" ></div>
		
		<!-- 실행시 데이터 유효성 검사이상시 에러메시지를 표시할 공간 -->
<!-- 		<span class="form_space sw_error_message " style="text-align:right; color: red"></span>
 -->		
	</div>
</div>
<!-- 파일다운로드시 새창 안띄우기 -->
<iframe id="fileDown" style='visibility:hidden' src="" width="1" height="1"></iframe>
<!-- 파일다운로드시 새창 안띄우기 -->

<!-- 전체 레이아웃//-->
