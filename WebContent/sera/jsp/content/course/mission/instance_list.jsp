<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@ page contentType="text/html; charset=utf-8"%>

<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 그리고 현재사용자 정보도 가져온다.	
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();
	
	String courseId = request.getParameter("courseId");
	String missionId = request.getParameter("missionId");
%>
<!-- Panel Section -->
<div class="panel_section js_mission_instance_list_page" courseId="<%=courseId %>" missionId="<%=missionId%>">

	<div class="js_user_instance_list">
		<jsp:include page="/sera/jsp/content/sera_instances.jsp">
			<jsp:param value="<%=cUser.getId() %>" name="userId"/>
			<jsp:param value="<%=courseId %>" name="courseId"/>
			<jsp:param value="<%=missionId %>" name="missionId"/>
		</jsp:include>
	</div>

</div>
<!-- Panel Section //-->
