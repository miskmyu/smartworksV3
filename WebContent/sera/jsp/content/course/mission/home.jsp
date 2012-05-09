<%@page import="net.smartworks.model.instance.Instance"%>
<%@page import="java.util.Calendar"%>
<%@page import="net.smartworks.model.sera.info.MissionInstanceInfo"%>
<%@page import="net.smartworks.model.sera.Course"%>
<%@page import="net.smartworks.model.instance.info.EventInstanceInfo"%>
<%@page import="java.util.Date"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@ page contentType="text/html; charset=utf-8"%>

<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 그리고 현재사용자 정보도 가져온다.	
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();

	String courseId = request.getParameter("courseId");
	Course course = (Course)session.getAttribute("course");
	if(SmartUtil.isBlankObject(course) || !course.getId().equals(courseId)) course = smartWorks.getCourseById(courseId);
	
%>
<!-- Nav SNB -->
<div class="t_gray mb5">* 미션 내용을 등록하려면 캘린더에서 일자를 클릭하여 등록합니다. 수정은 수행창에서 수정가능합니다. </div>
<div id="panel_section" class="js_misson_home_page" courseId="<%=courseId%>">

 	<div class="header js_view_mission_list">
		<div class="icon_mytext"><a href="" class="js_mission_calendar current" courseId="<%=courseId%>">미션캘린더</a></div>
		<div> | <a href="" class="js_mission_list" courseId="<%=courseId%>">미션목록</a></div>
	</div>

	<div class="js_mission_list_target">
		<jsp:include page="/sera/jsp/content/course/mission/calendar.jsp">
			<jsp:param value="<%=courseId %>" name="courseId"/>
		</jsp:include>
	</div>
</div>
<!-- Nav SNB //-->
