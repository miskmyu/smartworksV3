<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@ page contentType="text/html; charset=utf-8"%>

<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 그리고 현재사용자 정보도 가져온다.	
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();
	
	String courseId = request.getParameter("courseId");
%>
<!-- Panel Section -->
<div class="panel_section js_course_instance_list_page" courseId="<%=courseId %>" >
	<div class="header">
		<div class="icon_mytext"><a href="" class="js_view_user_instances" userId="<%=cUser.getId()%>" courseId="<%=courseId%>">내글보기</a></div>
		<div> | <a href="" class="js_view_all_instances current" courseId="<%=courseId%>">전체보기</a></div>
	</div>

	<div class="js_user_instance_list">
		<jsp:include page="/sera/jsp/content/sera_instances.jsp">
			<jsp:param value="<%=courseId %>" name="courseId"/>
		</jsp:include>
	</div>

</div>
<!-- Panel Section //-->
