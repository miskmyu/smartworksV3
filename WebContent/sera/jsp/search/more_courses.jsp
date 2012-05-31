<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.model.sera.info.CourseInfo"%>
<%@page import="net.smartworks.model.sera.GlobalSearchList"%>
<%@page import="net.smartworks.model.sera.FriendInformList"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.model.sera.info.SeraUserInfo"%>
<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.model.sera.FriendList"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@ page contentType="text/html; charset=utf-8"%>

<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 그리고 현재사용자 정보도 가져온다.	
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();

	String key = request.getParameter("key");
	String lastId = request.getParameter("lastId");

	CourseInfo[] courses = smartWorks.searchCourses(key, lastId, GlobalSearchList.MAX_COURSE_LIST);

	if(!SmartUtil.isBlankObject(courses)){
		for(int i=0; i<courses.length; i++){
			CourseInfo course = courses[i];
			if(i==GlobalSearchList.MAX_COURSE_LIST){
	%>
				<div class="js_more_search_course_btn more cb" key="<%=key %>" lastId="<%=courses[i-1].getId()%>">
					<div class="icon_more">더보기<span class="ml3 js_progress_span"></span></div>
				</div>
			<%
				break;
			}
			%>
			<li>
				<dl>
					<dd class="mb10">
						<a href="courseHome.sw?courseId=<%=course.getId()%>"> <img width="120" height="100" src="<%=course.getOrgPicture()%>"> </a>
					</dd>
					<dd class="mb3"><%=LocalDate.getDiffDate(course.getOpenDate(), course.getCloseDate())%> Days</dd>
					<dd class="mb3 title"><a href="courseHome.sw?courseId=<%=course.getId() %>"> <%=course.getName() %></a></dd>
				</dl>
			</li>
	<%
		}
	}
	%>
