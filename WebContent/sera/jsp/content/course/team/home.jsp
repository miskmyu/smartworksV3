<%@page import="net.smartworks.util.SeraTest"%>
<%@page import="net.smartworks.model.sera.Team"%>
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
	Course course = (Course)session.getAttribute("course");
	if(SmartUtil.isBlankObject(course) || !course.getId().equals(courseId)) course = smartWorks.getCourseById(courseId);
	
	Team myTeam = smartWorks.getMyTeamByCourse(courseId);
	if(!SmartUtil.isBlankObject(myTeam)){
%>
		<div class="panel_section js_team_home_page" courseId="<%=courseId %>" teamId="<%=myTeam.getId() %>">		
			<div class="header_tit">
				<div class="tit_dep2 team m0">
					<h2><%=myTeam.getName() %></h2>
				</div>
			</div>
			<!--  Tab //-->
		 	<div class="header mb5 js_view_team_home">
				<div><a href="" class="js_team_activity current" courseId="<%=courseId%>" teamId="<%=myTeam.getId()%>">팀 활동</a></div>
				<div> | <a href="" class="js_team_members" courseId="<%=courseId%>" teamId="<%=myTeam.getId()%>">팀 구성원</a></div>
				<%
				if(cUser.getId().equals(myTeam.getLeader())){
				%>
					<div> | <a href="" class="js_team_modify" courseId="<%=courseId%>" teamId="<%=myTeam.getId()%>">팀 설정</a></div>
				<%
				}
				%>
			</div>

			<div class="js_team_home_target">
				<jsp:include page="/sera/jsp/content/course/team/activity.jsp">
					<jsp:param value="<%=courseId %>" name="courseId"/>
					<jsp:param value="<%=myTeam.getId() %>" name = "teamId"/>
				</jsp:include>
			</div>
		</div>
	<%
	}else{
	%>
		<div class="panel_section js_team_home_page" courseId="<%=courseId %>">		
			
			<!--  Tab //-->
		 	<div class="header mb15 js_view_team_home">
				<div><a href="" class="js_team_create current" courseId="<%=courseId%>">팀 구성하기</a></div>
				<div> | <a href="" class="js_team_join_requests" courseId="<%=courseId%>">팀원 초대 목록</a></div>
			</div>
			
			<div class="js_team_home_target">
				<jsp:include page="/sera/jsp/content/course/team/create.jsp">
					<jsp:param value="<%=courseId %>" name="courseId"/>
				</jsp:include>
			</div>
	<%
	}
	%>
