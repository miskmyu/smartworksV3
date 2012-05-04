<%@page import="net.smartworks.model.sera.Course"%>
<%@page import="net.smartworks.model.security.AccessPolicy"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@ page contentType="text/html; charset=utf-8"%>

<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 그리고 현재사용자 정보도 가져온다.	
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();

	String courseId = request.getParameter("courseId");
	Course course = null;// (Course)session.getAttribute("course");
	if(SmartUtil.isBlankObject(course) || !course.getId().equals(courseId)) course = smartWorks.getCourseById(courseId);
	
	if(!SmartUtil.isBlankObject(course.getTeam())){
%>
		
<div class="header_tit">
	<div class="tit_dep2 m0">
		<h2>팀 명</h2>
		<div><%=course.getTeam().getName() %></div>
	</div>
</div>
<div class="my_comment_section">
	<div class="my_photo"><img class="profile_size_b" src="<%=cUser.getMidPicture() %>" /></div>
	<!-- My Comment -->
	<div class="my_comment">
		<div class="header"><%=cUser.getNickName() %>님</div>
		<jsp:include page="/sera/jsp/content/sera_note.jsp">
			<jsp:param value="<%=ISmartWorks.SPACE_TYPE_GROUP %>" name="spaceType"/>
			<jsp:param value="<%=courseId %>" name="spaceId"/>
			<jsp:param value="<%=course.getTeam().getId() %>" name="teamId"/>
		</jsp:include>
	</div>
	<!-- My Comment //-->
</div>
<!-- Comment Pannel-->
<div class="panel_section js_my_instance_list_page">
	<div class="header">
		<div class="icon_mytext"><a href="" class="js_view_user_instances current" userId="<%=cUser.getId()%>" courseId="<%=courseId%>" teamId="<%=course.getTeam().getId()%>">내글보기</a></div>
		<div> | <a href="" class="js_view_all_instances" courseId="<%=courseId%>" teamId="<%=course.getTeam().getId()%>">전체보기</a></div>
	</div>

	<div class="js_user_instance_list">
		<jsp:include page="/sera/jsp/content/sera_instances.jsp">
			<jsp:param value="<%=cUser.getId() %>" name="userId"/>
			<jsp:param value="<%=courseId %>" name="couseId"/>
			<jsp:param value="<%=course.getTeam().getId() %>" name="teamId"/>
		</jsp:include>
	</div>
</div>
<!-- Comment Pannel-->
<%
}else{
%>
		<jsp:include page="/sera/jsp/content/course/detail/create_team.jsp">
			<jsp:param value="<%=courseId %>" name="courseId"/>
		</jsp:include>
<%
}
%>
