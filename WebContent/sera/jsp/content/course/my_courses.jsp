<%@page import="net.smartworks.model.sera.info.CourseInfo"%>
<%@page import="net.smartworks.model.sera.CourseList"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@ page contentType="text/html; charset=utf-8"%>

<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 그리고 현재사용자 정보도 가져온다.	
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();

	CourseList courseList = smartWorks.getCoursesById(cUser.getId(), CourseList.MAX_COURSE_LIST);
%>
<!-- Header Title -->
<div class="header_tit">
	<div class="tit_course">자신이 멘토로 운영하는 코스와 멘티로 참여하는 코스를 한번에 확인할수
		있습니다.</div>
</div>
<!-- Header Title //-->
<!-- Content Section -->
<div class="content_section">
	<div class="header mt10">
		<div><%=cUser.getName() %>님의 운영코스</div>
	</div>
	<!-- 리스트1 -->
	<div class="category_list">
		<%
		if(courseList.getRunnings()>0){
			for(int i=0; i<courseList.getRunningCourses().length; i++){
				CourseInfo course = courseList.getRunningCourses()[i];
				String endClass = ((i+1) % 3 == 0) ? "end" : "";
		%>
				<ul href="courseHome.sw?courseId=<%=course.getId() %>" class="category_box <%=endClass%> js_sera_content">
					<li class="photo">
						<a href=""> <img width="218" height="148" src="<%=course.getMidPicture()%>"></a>
					</li>
					<li class="subject"><a href=""><%=course.getName() %></a></li>
					<li class="maker"><a href=""><%=course.getOwner().getName() %></a></li>
					<li class="info">
						<dl>
							<dd class="menteeNo"><%=course.getNumberOfGroupMember() %>명</dd>
							<dd class="makeDate"><%=course.getOpenDate().toLocalString() %></dd>
							<dd class="category"></dd>
						</dl></li>
					<li class="detail"><a href="/Course/Detail/212"><%=course.getBriefDesc() %></a></li>
					<!-- Gauge -->
					<li class="gauge_box w_auto mt5">
						<dl>
							<dd class="gauge_outline w170">
								<label class="gauge" style="width: <%=course.getAchievedPoint()*100/course.getTargetPoint()%>%;"></label>
							</dd>
							<dd class="process">(<%=course.getAchievedPoint() %>/<%=course.getTargetPoint() %>)</dd>
						</dl></li>
					<!-- Gauge //-->
				</ul>
		<%
			}
		}
		if(courseList.getRunnings()>CourseList.MAX_COURSE_LIST){
		%>
			<!-- 더보기 -->
			<div class="more cb">
				<div class="icon_more">더보기</div>
			</div>
			<!-- 더보기 //-->
		<%
		}
		%>
	</div>
	<!-- 리스트1 //-->

</div>
<!-- Content Section //-->

<!-- Content Section2 -->
<div class="content_section">
	<div class="header mt10">
		<div><%=cUser.getName() %>님의 참여코스</div>
	</div>
	<!-- 리스트1 -->
	<div class="category_list">
		<%
		if(courseList.getAttendings()>0){
			for(int i=0; i<courseList.getAttendingCourses().length; i++){
				CourseInfo course = courseList.getAttendingCourses()[i];
				String endClass = ((i+1) % 3 == 0) ? "end" : "";
		%>
				<ul href="courseHome.sw?courseId=<%=course.getId() %>" class="category_box <%=endClass%> js_sera_content">
					<li class="photo">
						<a href=""> <img width="218" height="148" src="<%=course.getMidPicture()%>"></a>
					</li>
					<li class="subject"><a href=""><%=course.getName() %></a></li>
					<li class="maker"><a href=""><%=course.getOwner().getName() %></a></li>
					<li class="info">
						<dl>
							<dd class="menteeNo"><%=course.getNumberOfGroupMember() %>명</dd>
							<dd class="makeDate"><%=course.getOpenDate().toLocalString() %></dd>
							<dd class="category"></dd>
						</dl></li>
					<li class="detail"><a href="/Course/Detail/212"><%=course.getBriefDesc() %></a></li>
					<!-- Gauge -->
					<li class="gauge_box w_auto mt5">
						<dl>
							<dd class="gauge_outline w170">
								<label class="gauge" style="width: <%=course.getAchievedPoint()*100/course.getTargetPoint()%>%;"></label>
							</dd>
							<dd class="process">(<%=course.getAchievedPoint() %>/<%=course.getTargetPoint() %>)</dd>
						</dl></li>
					<!-- Gauge //-->
				</ul>
		<%
			}
		}
		if(courseList.getAttendings()>CourseList.MAX_COURSE_LIST){
		%>
			<!-- 더보기 -->
			<div class="more cb">
				<div class="icon_more">더보기</div>
			</div>
			<!-- 더보기 //-->
		<%
		}
		%>
	</div>
	<!-- 리스트1 //-->

</div>
<!-- Content Section2 //-->

