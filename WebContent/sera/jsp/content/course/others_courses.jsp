<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.model.sera.Course"%>
<%@page import="net.smartworks.model.sera.SeraUser"%>
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

	String otherUserId = request.getParameter("userId");
	SeraUser otherUser = smartWorks.getSeraUserById(otherUserId);
	CourseList courseList = smartWorks.getCoursesById(otherUserId, CourseList.MAX_COURSE_LIST);
%>
<div class="my_comment_section">
	<!-- Photo Section -->
	<div class="photo_section">
		<a href="othersPAGE.sw?userId=<%=otherUser.getId() %>">
			<div class="my_photo"><img class="profile_size_b" src="<%=otherUser.getMidPicture() %>" /></div>
		</a>
		<!-- Btn -->
		<%
		if(!otherUser.isFriend()){
		%>
			<div class="btn_fgreen_l cb js_fried_request_btn" userId="<%=otherUser.getId() %>" style="margin: 8px 0 0 10px">
				<div class="btn_fgreen_r"><span class="icon_green_down"></span>친구요청</div>
			</div>
		<%
		}
		%>
		<!-- Btn //-->
	</div>
	<!-- Photo Section //-->
	<!-- My Comment -->
	<div class="my_comment">
		<div class="header"><%=otherUser.getNickName() %></div>
		<jsp:include page="/sera/jsp/content/sera_note.jsp">
			<jsp:param value="<%=ISmartWorks.SPACE_TYPE_USER %>" name="spaceType"/>
			<jsp:param value="<%=otherUserId%>" name="spaceId"/>
		</jsp:include>
	</div>
	<!-- My Comment //-->
</div>

<!-- Panel Section -->
<div class="content_section">

	<!-- Content Section -->
	<div class="content_section">
		<div class="header mt10">
			<div><%=otherUser.getNickName() %>님의 운영코스</div>
		</div>
		<!-- 리스트1 -->
		<div class="category_list js_course_list">
			<%
			if(courseList.getRunnings()>0){
				for(int i=0; i<courseList.getRunningCourses().length; i++){
					if(i == CourseList.MAX_COURSE_LIST)
						break;
					CourseInfo course = courseList.getRunningCourses()[i];
					String endClass = ((i+1) % 3 == 0) ? "end" : "";
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
								<dd class="makeDate"><%=course.getOpenDate().toLocalDateSimpleString() %></dd>
								<dd class="category"><%=course.getCategory() %></dd>
							</dl></li>
						<li class="detail"><a href="courseHome.sw?courseId=<%=course.getId() %>"><%=CommonUtil.toNotNull(course.getBriefDesc()) %></a></li>
						<!-- Gauge -->
						<li class="gauge_box w_auto mt5">
							<dl>
								<dd class="gauge_outline w170">
									<label class="gauge" style="width: <%=course.getAchievedRatio()%>%;"></label>
								</dd>
								<dd class="process">(<%=course.getAchievedPoint() %>/<%=course.getTargetPoint() %>)</dd>
							</dl></li>
						<!-- Gauge //-->
					</ul>
			<%
				}
			}
			if(courseList.getRunningCourses().length==CourseList.MAX_COURSE_LIST+1){
			%>
				<!-- 더보기 -->
				<div class="more cb js_more_courses_by_user" courseType="<%=Course.MY_RUNNING_COURSE%>" userId="<%=otherUserId %>" lastId="<%=courseList.getRunningCourses()[CourseList.MAX_COURSE_LIST-2].getId()%>">
					<div class="icon_more">더보기<span class="ml3 js_progress_span"></span></div>
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
			<div><%=otherUser.getNickName() %>님의 참여코스</div>
		</div>
		<!-- 리스트1 -->
		<div class="category_list js_course_list">
		<%
		if(courseList.getAttendings()>0){
			for(int i=0; i<courseList.getAttendingCourses().length; i++){
				if(i == CourseList.MAX_COURSE_LIST)
					break;
				CourseInfo course = courseList.getAttendingCourses()[i];
				String endClass = ((i+1) % 3 == 0) ? "end" : "";
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
							<dd class="makeDate"><%=course.getOpenDate().toLocalDateSimpleString() %></dd>
							<dd class="category"><%=course.getCategory() %></dd>
						</dl></li>
					<li class="detail"><a href="courseHome.sw?courseId=<%=course.getId() %>"><%=CommonUtil.toNotNull(course.getBriefDesc()) %></a></li>
					<!-- Gauge -->
					<li class="gauge_box w_auto mt5">
						<dl>
							<dd class="gauge_outline w170">
								<label class="gauge" style="width: <%=course.getAchievedRatio()%>%;"></label>
							</dd>
							<dd class="process">(<%=course.getAchievedPoint() %>/<%=course.getTargetPoint() %>)</dd>
						</dl></li>
					<!-- Gauge //-->
				</ul>
		<%
			}
		}
		if(courseList.getAttendingCourses().length==CourseList.MAX_COURSE_LIST+1){
		%>
			<!-- 더보기 -->
			<div class="more cb js_more_courses_by_user" courseType="<%=Course.MY_ATTENDING_COURSE%>" userId="<%=otherUserId %>" lastId="<%=courseList.getAttendingCourses()[CourseList.MAX_COURSE_LIST-2].getId()%>">
				<div class="icon_more">더보기<span class="ml3 js_progress_span"></span></div>
			</div>
			<!-- 더보기 //-->
		<%
		}
		%>
		</div>
		<!-- 리스트1 //-->
	</div>
	<!-- Content Section2 //-->

</div>
<!-- Panel Section //-->
