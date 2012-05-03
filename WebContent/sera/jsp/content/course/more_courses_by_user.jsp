<%@page import="net.smartworks.model.sera.Course"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.model.sera.info.CourseInfo"%>
<%@page import="net.smartworks.model.sera.CourseList"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@ page contentType="text/html; charset=utf-8"%>

<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 그리고 현재사용자 정보도 가져온다.	
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");

	String typeStr = request.getParameter("courseType");
	String userId = request.getParameter("userId");
	String lastId = request.getParameter("lastId");
	int courseType = (SmartUtil.isBlankObject(typeStr)) ? 0 : Integer.parseInt(typeStr);
	CourseInfo[] courses = smartWorks.getCoursesByUser(userId, courseType, lastId, CourseList.MAX_COURSE_LIST);

	if(!SmartUtil.isBlankObject(courses)){
		for(int i=0; i<courses.length; i++){
			if (i == CourseList.MAX_COURSE_LIST)
				break;
			CourseInfo course = courses[i];
			String endClass = ((i+1) % 3 == 0) ? "end" : "";
			String achievedPoint =course.getAchievedRatio() + "%";
			if(i==CourseList.MAX_COURSE_LIST){
	%>
				<div class="js_more_courses_by_user more cb" courseType="<%=courseType%>" useId="<%=userId %>" lastId="<%=courses[i-2].getId()%>">
					<div class="icon_more">더보기<span class="ml3 js_progress_span"></span></div>
				</div>
			<%
				break;
			}
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
	%>
