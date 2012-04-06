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
	Course course = smartWorks.getCourseById(courseId);
	String mentorId = (SmartUtil.isBlankObject(course.getLeader())) ? "" : course.getLeader().getId();
	String mentorName = (SmartUtil.isBlankObject(course.getLeader())) ? "" : course.getLeader().getName();
	boolean myRunningCourse = (cUser.getId().equals(mentorId));
	
	session.setAttribute("course", course);
	
%>
<!-- Course Define -->
<div class="course_df_section js_course_home_page" courseId="<%=courseId%>">
	<div class="course_df_block">
		<div class="course_df_img">
			<img src="<%=course.getMidPicture() %>" />
		</div>
		<div class="course_df">
			<h1><%=course.getName() %></h1>
			<ul class="mt8">
				<li>
					<div class="icon_cs_mentorname">
						<span>멘토명</span>
					</div> <span class="t_s14"><%=mentorName%></span></li>
				<li>
					<div class="icon_cs_openday">
						<span>개설일</span>
					</div> <span><%=course.getOpenDate().toLocalString() %></span></li>
				<li>
					<div class="icon_cs_menteenum">
						<span>멘티수</span>
					</div> <span class="t_s14"><%=course.getNumberOfGroupMember() %></span></li>
			</ul>
		</div>
		<div class="course_info">
			<dl>
				<dt>코스알림</dt>
				<dd>- [알림] 3월 14일 번개팅합니다</dd>
				<dd>- 미션6이 등록되었습니다</dd>
				<dd>- [이벤트] 미션2 선착순 3명!...</dd>
				<dd>- [이벤트] 미션2 선착순 3명!...</dd>
			</dl>
		</div>
	</div>
	<div class="course_menu js_course_main_menu">
		<div class="btn_green_l mt8 fl">
			<div class="btn_green_r">
				<span class="icon_green_down mr5"></span>친구초대
			</div>
		</div>
		<!-- Menu Dep1-->
		<div class="course_menu_d1">
			<ul class="js_course_menu">
				<li class="current"><a href="" class="js_course_detail">홈</a></li>
				<li><a href="" class="js_course_mission">미션</a></li>
				<li><a href="" class="js_course_board">코스알림</a></li>
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
		<div class="fr" style="margin: 8px 30px 0 0">
			<div class="btn_mid_l">
				<div class="btn_mid_r">팀구성하기</div>
			</div>
		</div>
	</div>
	<!-- Menu Dep2 -->
	<div class="course_menu_d2 js_course_sub_menu" style="display:none">
		<div class="menu001 current" style="display:none"></div>
		<div class="menu002 js_course_mission_menu" style="display:none">
			<ul>
				<li class="js_course_mission_set"><a href="">미션등록/수행</a></li>
				<li class="js_course_mission_list"><a href="">전체보기</a></li>
				<li class="js_course_mission_mine"><a href="">내글보기</a></li>
				<li class="end js_course_by_mission"><a href="">미션별 보기</a></li>
			</ul>
		</div>
		<div class="menu003" style="display:none"></div>
		<div class="menu004 js_course_setting_menu" style="display:none">
			<%
			if(myRunningCourse){
			%>
				<ul>
					<li class="js_course_setting_profile"><a href=""> 코스관리</a></li>
					<li  class="js_course_setting_mentee"><a href="">멘티관리</a></li>
					<li class="end js_course_setting_team"><a href="" ><span class="icon_bul_select mr5"></span>팀관리 </a></li>
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
<div class="course_section js_course_content">
	<jsp:include page="/sera/jsp/content/course/detail/general.jsp">
		<jsp:param value="<%=courseId %>" name="courseId"/>
	</jsp:include>
</div>
<!-- Comment Pannel-->