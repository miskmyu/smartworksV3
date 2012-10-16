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
	String chartType = request.getParameter("chartType");
	User cUser = SmartUtil.getCurrentUser();

%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />
<!--  전체 레이아웃 -->
<div class="js_work_report_view">

	<!-- 컨텐츠 -->
	<div class="fr">
		<select>
			<option>시간대별</option>
			<option>요일별</option>
			<option>월별</option>
			<option>계절별</option>
		</select>
 		<select>
			<option>올해</option>
			<option>최근1년</option>
			<option>최근3년</option>
			<option>전체</option>
		</select>
		<select>
			<option>모든서비스</option>
			<option>환경</option>
			<option>수질</option>
		</select>
		<select>
			<option>모든이벤트</option>
			<option>강풍</option>
			<option>풍량</option>
		</select>
		
	</div>
	<div id="chart_target" class="form_contents js_work_report_view" style="height:300px;">
	</div>
</div>
<!-- 전체 레이아웃//-->