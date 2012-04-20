<%@page import="net.smartworks.model.instance.Instance"%>
<%@page import="net.smartworks.model.instance.info.InstanceInfo"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.model.sera.Mentor"%>
<%@page import="net.smartworks.model.sera.Course"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@ page contentType="text/html; charset=utf-8"%>

<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 그리고 현재사용자 정보도 가져온다.	
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();

	String courseId = request.getParameter("courseId");
	Course course = (Course) session.getAttribute("course");
	if (SmartUtil.isBlankObject(course) || !course.getId().equals(courseId))
		course = smartWorks.getCourseById(courseId);
	InstanceInfo[] notices = smartWorks.getCourseNotices(courseId, new LocalDate(), 10);
	
	session.setAttribute("cid", ISmartWorks.CONTEXT_GROUP_SPACE + courseId);
	session.setAttribute("wid", courseId);
		
%>
<!-- Header Title -->
<div class="header_tit">
	<div class="tit_dep2 m0">
		<h2>코스알림</h2>
		<div>* 본 코스에 대한 공지사항 및 멘티들에게 전하는 소식을 입력할 수 있습니다.</div>
	</div>
</div>
<!-- Header Title //-->
<!-- Input Form -->
<div class="form_layout">
	<jsp:include page="/sera/jsp/content/social/start/new_board.jsp"/>
</div>
<!-- Input Form //-->

<!-- Panel Section -->
<div class="panel_section">
	<div class="js_user_instance_list">
		<jsp:include page="/sera/jsp/content/sera_instances.jsp">
			<jsp:param value="<%=Instance.TYPE_BOARD %>" name="instanceType"/>
			<jsp:param value="<%=cUser.getId() %>" name="userId"/>
		</jsp:include>
	</div>
</div>
<!-- Panel Section //-->
