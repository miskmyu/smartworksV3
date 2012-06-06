<%@page import="net.smartworks.model.sera.Team"%>
<%@page import="net.smartworks.model.sera.info.TeamInfo"%>
<%@page import="org.springframework.security.web.context.HttpSessionSecurityContextRepository"%>
<%@page import="org.springframework.security.core.context.SecurityContext"%>
<%@page import="net.smartworks.model.sera.info.ReviewInstanceInfo"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.model.instance.Instance"%>
<%@page import="net.smartworks.model.instance.info.InstanceInfo"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.model.sera.Mentor"%>
<%@page import="net.smartworks.model.sera.Course"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@ page contentType="text/html; charset=utf-8"%>

<%
	boolean isAuthenticated = false;
	SecurityContext context = (SecurityContext) request.getSession().getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
	if (!SmartUtil.isBlankObject(context)) {
		isAuthenticated = true;
	}
	
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 그리고 현재사용자 정보도 가져온다.	
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();
	
	String courseId = request.getParameter("courseId");
	Course course = smartWorks.getCourseById(courseId);
	Team myTeam = smartWorks.getMyTeamByCourse(courseId);
	
	InstanceInfo[] notices = (cUser.isAnonymusUser()) ? null : smartWorks.getCourseNotices(courseId, new LocalDate(), 5);
	String mentorId = (SmartUtil.isBlankObject(course.getLeader())) ? "" : course.getLeader().getId();
	String mentorName = (SmartUtil.isBlankObject(course.getLeader())) ? "" : course.getLeader().getName();
	boolean myRunningCourse = (cUser.isAnonymusUser()) ? false : course.isMyRunningCourse();
	boolean myAttendingCourse = (cUser.isAnonymusUser()) ? false : course.isMyAttendingCourse();

	session.setAttribute("course", course);
	
%>
<!-- Course Define -->
<div class="course_df_section js_course_home_page" courseId="<%=courseId%>">
	<div class="course_df_block">
		<div class="course_df_img">
			<a href="courseHome.sw?courseId=<%=courseId%>"><img class="profile_size_b" src="<%=course.getMidPicture() %>" /></a>
		</div>
		<div class="course_df">
			<h1><%=course.getName() %></h1>
			<ul class="mt8">
				<li>
					<div class="icon_cs_mentorname"><span>멘토명</span></div> 
					<a href="<%if(!mentorId.equals(cUser.getId())){%>othersPAGE.sw?userId=<%=mentorId%><%}%>"><span class="t_s14"><%=mentorName%></span></a>
				</li>
				<li>
					<div class="icon_cs_openday">
						<span>개설일</span>
					</div> 
					<%
					if(!SmartUtil.isBlankObject(course.getCreatedDate())){
					%>
						<span><%=course.getCreatedDate().toLocalDateSimpleString() %></span>
					<%
					}
					%>
				</li>
				<li>
					<div class="icon_cs_menteenum">
						<span>멘티수</span>
					</div> <span class="t_s14"><%=course.getNumberOfGroupMember() %></span>
				</li>
			</ul>
		</div>
		<a href="" class="js_course_board_list">
			<div class="course_info">
				<dl>
					<dt>코스 게시판</dt>
					<%
					if(!SmartUtil.isBlankObject(notices)){
						for(int i=0; i<notices.length; i++){
							InstanceInfo notice = notices[i];
							if(notice.getType()==Instance.TYPE_BOARD){
					%>
								<dd>[알림] <%=notice.getSubject() %></dd>
							<%
							}else if(notice.getType()==Instance.TYPE_EVENT){
							%>
								<dd>[이벤트] <%=notice.getSubject() %></dd>
							<%
							}
						}
					}
					%>
				</dl>
			</div>
		</a>
	</div>
	<div class="course_menu js_course_main_menu">
		<%
		if(course.isMyAttendingCourse() || course.isMyRunningCourse()){
		%>
			<div class="btn_fgreen_l mt8 fl js_invite_course_members_btn">
				<div class="btn_fgreen_r"><span class="icon_green_down"></span>멘티초대</div>
			</div>
		<%
		}
		%>
		<!-- Menu Dep1-->
		<div class="course_menu_d1">
			<ul class="js_course_menu" isJoinCourse=<%=course.isJoinCourse() %>>
				<li class="<%if(!cUser.isAnonymusUser() && course.isJoinCourse()) {%>current<%}%>"><a href="" class="js_course_home">홈</a></li>
				<li><a href="" class="js_course_mission">미션</a></li>
				<li><a href="" class="js_course_board">코스게시판</a></li>
				<li><a href="" class="js_create_team">팀활동</a></li>
				<li class="<%if(cUser.isAnonymusUser() || !course.isJoinCourse()) {%>current<%}%>"><a href="" class="js_course_general">코스개요</a></li>
				<li><a href="" class="js_course_setting">코스설정</a></li>
			</ul>
		</div>
		<!-- Menu Dep1//-->
		<div class="icon_twitter fr ml5mt7">
			<a href="">트위터</a>
		</div>
		<div class="icon_facebook fr ml5mt7">
			<a href="">페이스북</a>
		</div>
	</div>
	<!-- Menu Dep2 -->
	<div class="course_menu_d2 js_course_sub_menu" style="display:none">
		<div class="menu001 current" style="display:none"></div>
		<div class="menu002" style="display:none"></div>
		<div class="menu003" style="display:none"></div>
		<div class="menu004 js_course_team_menu" style="display:none">
			<%
			if(myRunningCourse){
				TeamInfo[] courseTeams = smartWorks.getTeamsByCourse(courseId);
			%>
				<ul>
					<li class="end js_select_course_team">
						<select>
							<option value="">팀구성하기</option>
							<%
							if(!SmartUtil.isBlankObject(courseTeams)){
								for(int i=0; i<courseTeams.length; i++){
									TeamInfo courseTeam = courseTeams[i];
							%>
								<option value="<%=courseTeam.getId()%>"><%=courseTeam.getName() %></option>
							<%
								}
							}
							%>
							
						</select>
					</li>
				</ul>
			<%
			}
			%>
		</div>
		<div class="menu005" style="display:none"></div>
		<div class="menu006 js_course_setting_menu" style="display:none">
			<%
			if(myRunningCourse){
			%>
				<ul>
					<li class="js_course_setting_profile"><a href="">코스관리</a></li>
					<li class="js_create_mission"><a href="">미션등록</a></li>
					<li  class="end js_course_setting_mentee"><a href="">멘티관리</a></li>
				</ul>
			<%
			}
			%>
		</div>

	</div>
	<!-- Menu Dep2 //-->
</div>
<!-- Course Define //-->
<!-- Course Section -->
<div class="course_section js_course_content" courseId="<%=courseId%>">
	<%
	if(cUser.isAnonymusUser() || !course.isJoinCourse()) {
	%>
		<jsp:include page="/sera/jsp/content/course/detail/general.jsp">
			<jsp:param value="<%=courseId %>" name="courseId"/>
		</jsp:include>
	<%
	}else{
	%>
		<jsp:include page="/sera/jsp/content/course/detail/instance_list.jsp">
			<jsp:param value="<%=courseId %>" name="courseId"/>
		</jsp:include>
	<%
	}
	%>
</div>
<!-- Comment Pannel-->
