<%@page import="net.smartworks.server.engine.process.task.model.TskTask"%>
<%@page import="net.smartworks.model.instance.Instance"%>
<%@page import="net.smartworks.model.instance.info.EventInstanceInfo"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.model.sera.Course"%>
<%@page import="net.smartworks.model.sera.info.CourseInfo"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 그리고 현재사용자 정보도 가져온다.	
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();

%>
<!-- Header Title -->
<div class="header_tit">
	<div class="tit_event">
		내 코스에서 개최하는 오프라인 모임이나, 세라 친구들과 이벤트를 만들어 보세요.<br /> 삶의 소중한 시간으로 채워지는
		만남과 열정이 만들어집니다.
	</div>
</div>
<!-- Header Title //-->
<!-- Input Form -->
<div class="form_layout">
	<jsp:include page="/sera/jsp/content/social/start/new_event.jsp"/>
</div>
<!-- Panel Section -->
<div class="panel_section">
	<div class="js_user_instance_list">
		<jsp:include page="/sera/jsp/content/sera_instances.jsp">
			<jsp:param value="<%=Instance.TYPE_EVENT %>" name="instanceType"/>
			<jsp:param value="<%=TskTask.TASKREFTYPE_EVENT%>" name="userId"/>
		</jsp:include>
	</div>
</div>
<!-- Panel Section //-->
