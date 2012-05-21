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
			<li class="selected js_select_course_btn" courseType="<%=Course.TYPE_RECOMMENDED_COURSES%>">추천코스</li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_FAVORITE_COURSES%>">인기코스</li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_ALL_COURSES%>">전체코스</li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CLOSED_COURSES%>">지난코스</li>

			<li class="categoryLine"></li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>">예술</li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>">엔터테인먼트</li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>">스타일</li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>">생활</li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>">영화/애니메이션</li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>">게임</li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>">영화</li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>">이벤트</li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>">스포츠</li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>">이슈</li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>">시사</li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>">경제</li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>">비즈니스</li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>">미디어</li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>">환경</li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>">동물</li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>">비영리/사회운동</li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>">역사</li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>">문학</li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>">심리</li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>">인물</li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>">과학</li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>">첨단기술</li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>">의학</li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>">건축</li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>">교육</li>
			<li class="js_select_course_btn" courseType="<%=Course.TYPE_CATEGORIES%>">기타</li>
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
