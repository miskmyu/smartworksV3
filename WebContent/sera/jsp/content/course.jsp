<%@page import="net.smartworks.model.sera.Course"%>
<%@page import="net.smartworks.model.sera.info.CourseInfo"%>
<%@page import="net.smartworks.model.sera.CourseList"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@ page contentType="text/html; charset=utf-8"%>

<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 그리고 현재사용자 정보도 가져온다.	
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
%>

<div id="course_list_section" class="js_course_page">
	<!-- SNB Left -->
	<div class="snb2">
		<ul class="snb_menu">
			<li><img height="112" width="149" src="sera/images/function_title11.gif"></li>
			<li class="selected js_select_course_btn" courseType="<%=Course.TYPE_RECOMMENDED_COURSES%>"><a href="">추천코스</a></li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_FAVORITE_COURSES%>"><a href="">인기코스</a></li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_ALL_COURSES%>"><a href="">전체코스</a></li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CLOSED_COURSES%>"><a href=""> 지난코스</a></li>

			<li class="categoryLine"></li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>"><a href="">예술</a></li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>"><a href="">엔터테인먼트</a></li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>"><a href="">스타일</a></li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>"><a href="">생활</a></li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>"><a href="">영화/애니메이션</a></li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>"><a href="">게임</a></li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>"><a href="">영화</a></li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>"><a href="">이벤트</a></li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>"><a href="">스포츠</a></li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>"><a href="">이슈</a></li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>"><a href="">시사</a></li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>"><a href="">경제</a></li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>"><a href="">비즈니스</a></li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>"><a href="">미디어</a></li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>"><a href="">환경</a></li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>"><a href="">동물</a></li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>"><a href="">비영리/사회운동</a></li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>"><a href="">역사</a></li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>"><a href="">문학</a></li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>"><a href="">심리</a></li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>"><a href="">인물</a></li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>"><a href="">과학</a></li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>"><a href="">첨단기술</a></li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>"><a href="">의학</a></li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>"><a href="">건축</a></li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>"><a href="">교육</a></li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>"><a href="">기타</a></li>
		</ul>
	</div>
	<!-- SNB Left//-->
	<!-- Content -->
	<div id="content_list_section" class="js_course_list">
		<jsp:include page="/sera/jsp/content/course_by_type.jsp">
			<jsp:param value="<%=Course.TYPE_RECOMMENDED_COURSES %>" name="courseType"/>
		</jsp:include>
	</div>
</div>
