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
	<div class="tit_course">자신이 멘토로 운영하는 코스와 멘티로 참여하는 코스를 한번에 확인할 수 있습니다.</div>
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
				String achievedPoint =course.getAchievedRatio() + "%";
		%>
				<ul class="category_box <%=endClass%>">
					<li class="photo">
						<a href="courseHome.sw?courseId=<%=course.getId() %>"> <img width="218" height="148" src="<%=course.getMidPicture()%>"></a>
					</li>
					<li class="subject"><a href="courseHome.sw?courseId=<%=course.getId() %>"><%=course.getName() %></a></li>
					<li class="maker"><a href="othersPAGE.sw?userId=<%=course.getOwner().getName()%>"><%=course.getOwner().getName() %></a></li>
					<li class="info">
						<dl>
							<dd class="menteeNo"><%=course.getNumberOfGroupMember() %>명</dd>
							<dd class="makeDate"><%if(!SmartUtil.isBlankObject(course.getOpenDate())){%><%=course.getOpenDate().toLocalDateSimpleString() %><%} %></dd>
							<dd class="category"><%=course.getCategory() %></dd>
						</dl></li>
					<li class="detail"><a href="courseHome.sw?courseId=<%=course.getId() %>"><%=course.getBriefDesc() %></a></li>
					<!-- Gauge -->
					<li class="gauge_box w_auto mt5">
						<dl>
							<dd class="gauge_outline w170">
								<label class="gauge" style="width: <%=achievedPoint%>;"></label>
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
				String achievedPoint =course.getAchievedRatio() + "%";
		%>
				<ul class="category_box <%=endClass%>">
					<li class="photo">
						<a href="courseHome.sw?courseId=<%=course.getId() %>"> <img width="218" height="148" src="<%=course.getMidPicture()%>"></a>
					</li>
					<li class="subject"><a href="courseHome.sw?courseId=<%=course.getId() %>"><%=course.getName() %></a></li>
					<li class="maker"><a href="othersPAGE.sw?userId=<%=course.getOwner().getId()%>"><%=course.getOwner().getName() %></a></li>
					<li class="info">
						<dl>
							<dd class="menteeNo"><%=course.getNumberOfGroupMember() %>명</dd>
							<dd class="makeDate"><%if(!SmartUtil.isBlankObject(course.getOpenDate())){%><%=course.getOpenDate().toLocalDateSimpleString() %><%} %></dd>
							<dd class="category"><%=course.getCategory() %></dd>
						</dl></li>
					<li class="detail"><a href="courseHome.sw?courseId=<%=course.getId() %>"><%=course.getBriefDesc() %></a></li>
					<!-- Gauge -->
					<li class="gauge_box w_auto mt5">
						<dl>
							<dd class="gauge_outline w170">
								<label class="gauge" style="width: <%=achievedPoint%>;"></label>
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

