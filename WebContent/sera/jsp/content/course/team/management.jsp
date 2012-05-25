<%@page import="net.smartworks.model.security.AccessPolicy"%>
<%@page import="net.smartworks.model.sera.Team"%>
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
	String teamId = request.getParameter("teamId");
	Team team = SmartUtil.isBlankObject(teamId) ? null :  smartWorks.getTeamById(teamId);
	session.setAttribute("team", team);

	if(SmartUtil.isBlankObject(team)){
%>
		<div class="panel_section js_team_management_page" courseId="<%=courseId%>">
			
			<!--  Tab //-->
		 	<div class="header mb15 js_view_team_management">
				<div><a href="" class="js_team_create current" courseId="<%=courseId%>">팀 구성하기</a></div>
				<div> | <a href="" class="js_team_join_requests" courseId="<%=courseId%>">팀원 초대 목록</a></div>
			</div>
			
			<div class="js_team_management_target">
				<jsp:include page="/sera/jsp/content/course/team/create.jsp">
					<jsp:param value="<%=courseId %>" name="courseId"/>
				</jsp:include>
			</div>
	<%
	}else{
	%>
	
		<div class="panel_section js_team_management_page" courseId="<%=courseId%>">
			<!-- Header Title -->
			<div class="header_tit">
				<div class="tit_dep2 team m0">
					<h2><%=team.getName()%></h2>
				</div>
			</div>
			<!-- Header Title //-->
			
			<!--  Tab //-->
		 	<div class="header mb15 js_view_team_management">
				<div><a href="" class="js_team_activity current" courseId="<%=courseId%>" teamId="<%=teamId%>">팀 활동</a></div>
				<div> | <a href="" class="js_team_modify" courseId="<%=courseId%>" teamId="<%=teamId%>">팀 설정</a></div>
				<div> | <a href="" class="js_team_members" courseId="<%=courseId%>" teamId="<%=teamId%>">팀 구성원</a></div>
			</div>
			
			<div class="js_team_management_target">
				<jsp:include page="/sera/jsp/content/course/team/activity.jsp">
					<jsp:param value="<%=courseId %>" name="courseId"/>
					<jsp:param value="<%=teamId %>" name="teamId"/>
				</jsp:include>
			</div>
		</div>

	<%
	}
	%>
	
