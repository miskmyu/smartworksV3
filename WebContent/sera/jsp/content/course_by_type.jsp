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
	String categoryName = request.getParameter("categoryName");
	int courseType = (SmartUtil.isBlankObject(typeStr)) ? 0 : Integer.parseInt(typeStr);
	CourseInfo[] courses = null;
	if(courseType != Course.TYPE_CATEGORIES)
		courses = smartWorks.getCoursesByType(courseType, null, Course.LIST_PAGE_SIZE);
	else if(!SmartUtil.isBlankObject(categoryName))
		courses = smartWorks.getCoursesByCategory(categoryName, null, Course.LIST_PAGE_SIZE);
		
	String courseName = "";
	switch(courseType){
	case Course.TYPE_RECOMMENDED_COURSES:
		courseName = "추천코스";
		break;
	case Course.TYPE_FAVORITE_COURSES:
		courseName = "인기코스";
		break;
	case Course.TYPE_ALL_COURSES:
		courseName = "전체코스";
		break;
	case Course.TYPE_CLOSED_COURSES:
		courseName = "지난코스";
		break;
	case Course.TYPE_CATEGORIES:
		courseName = categoryName;
		break;
	}
%>
<div class="js_course_by_type_page" courseType="<%=courseType%>" categoryName="<%=categoryName%>">
	<ul class="search">
		<li><input class="srch_style1 fl js_search_course_key" type="text"></li>
		<li><img class="js_search_course_btn" id="btnSearchIn" height="34" width="62" style="cursor: pointer;" alt="검색" src="sera/images/btn_srch3.gif"></li>
	</ul>
	
	<div class="tit_header"><h2><%=courseName %></h2></div>
	<div class="course_listbox1">
		<ul class="course_list js_course_by_type_list">
			<%
			if(!SmartUtil.isBlankObject(courses)){
				for(int i=0; i<courses.length; i++){
					CourseInfo course = courses[i];
					if(i==Course.LIST_PAGE_SIZE){
			%>
						<div class="js_more_course_by_type more cb" courseType="<%=courseType%>" categoryName="<%=categoryName%>" lastId="<%=courses[i-1].getId()%>">
							<div class="icon_more">더보기<span class="ml3 js_progress_span"></span></div>
						</div>
					<%
						break;
					}
					%>
					<li>
						<dl>
							<dd class="mb10"><a href="courseHome.sw?courseId=<%=course.getId() %>"> <img height="100" width="120" src="<%=course.getOrgPicture()%>"></a></dd>
							<dd class="t_s11 mb3"><%=LocalDate.getDiffDate(course.getOpenDate(), course.getCloseDate()) %>Days</dd>
							<dd class="t_s11 mb3"><%=course.getOpenDate().toLocalDateSimpleString()%> ~ <%=course.getCloseDate().toLocalDateSimpleString()%></dd>
							<dd class="title"><a href="courseHome.sw?courseId=<%=course.getId() %>"> <%=course.getName() %></a></dd>
						</dl>
					</li>
			<%
				}
			}
			%>
		</ul>
	</div>
</div>


<script type="text/javascript">
$(function() {
	$(document).keypress(function(e) {
		var keyCode = e.which || e.keyCode;
		if(keyCode == 13) {
			if (!$('.login_section').is(':visible')) {
				$('.js_course_by_type_page .js_search_course_btn').click();
				return false;
			}
		}else{
			return true;
		}
	});

	$('.j_btn_login_form').live('click', function(e) {
		$('.login_section').show();
		return false;
	});
});
</script>
