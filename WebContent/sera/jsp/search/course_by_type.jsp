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
	int courseType = (SmartUtil.isBlankObject(typeStr)) ? 0 : Integer.parseInt(typeStr);
	String categoryName = request.getParameter("categoryName");
	String key = request.getParameter("key");
	if(SmartUtil.isBlankObject(key))
		return;
	
	CourseInfo[] courses = null;
	if(courseType == Course.TYPE_CATEGORIES)
		courses = smartWorks.searchCourseByCategory(categoryName, key);
	else
		courses = smartWorks.searchCourseByType(courseType, key);

	if(!SmartUtil.isBlankObject(courses)){
		for(int i=0; i<courses.length; i++){
			CourseInfo course = courses[i];
	%>
			<li>
				<dl>
					<dd class="mb10"><a href="courseHome.sw?courseId=<%=course.getId() %>"> <img height="100" width="120" src="<%=course.getOrgPicture()%>"></a></dd>
					<dd class="mb3"><%=LocalDate.getDiffDate(course.getOpenDate(), course.getCloseDate()) %>Days</dd>
					<dd class="mb3"><a href="courseHome.sw?courseId=<%=course.getId() %>"> <strong><%=course.getName() %></strong></a></dd>
				</dl>
			</li>
	<%
		}
	}
	%>
