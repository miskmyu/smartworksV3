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
		<jsp:include page="/sera/jsp/content/course/team/create.jsp">
			<jsp:param value="<%=courseId %>" name="courseId"/>
		</jsp:include>
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
			
			<!--  Tab 
			<div id="" class="tab">
				<ul class="clear">
					<li class="current">
						<span><a href="" class="js_team_modify current" courseId="<%=courseId%>" teamId="<%=teamId%>">팀 설정</a></span>
					</li>
					<li class="">
						<span><a href="" class="js_team_members" courseId="<%=courseId%>" teamId="<%=teamId%>">팀 구성원</a></span>
					</li>
				</ul>
			</div>
			<!--  Tab //-->
		 	<div class="header mb5 js_view_team_management">
				<div><a href="" class="js_team_modify current" courseId="<%=courseId%>" teamId="<%=teamId%>">팀 설정</a></div>
				<div> | <a href="" class="js_team_members" courseId="<%=courseId%>" teamId="<%=teamId%>">팀 구성원</a></div>
			</div>
			<div class="t_gray mb10">
				<div>코스 개설자(멘토)는 본인이 구성한 팀 뿐만 아니라, 코스 내에 활동하는 모든 팀을 관리할 수 있습니다.</div>
			</div>
			
			<div class="js_team_management_target">
				<jsp:include page="/sera/jsp/content/course/team/modify.jsp">
					<jsp:param value="<%=courseId %>" name="courseId"/>
					<jsp:param value="<%=teamId %>" name="teamId"/>
				</jsp:include>
			</div>
		</div>

	<%
	}
	%>
	
