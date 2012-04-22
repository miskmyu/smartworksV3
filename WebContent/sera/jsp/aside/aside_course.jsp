
<%@page import="net.smartworks.model.sera.Course"%>
<%@page import="net.smartworks.model.sera.info.SeraUserInfo"%>
<%@page import="net.smartworks.model.sera.FriendList"%>
<%@page import="net.smartworks.model.sera.CourseList"%>
<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.model.sera.info.CourseInfo"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ page import="net.smartworks.model.community.*"%>
<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 현재사용자 정보도 가져온다 
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();
	CourseInfo[] favoriteCourses = smartWorks.getCoursesByType(Course.TYPE_FAVORITE_COURSES, null, 3);
	CourseInfo[] recommendedCourses = smartWorks.getCoursesByType(Course.TYPE_RECOMMENDED_COURSES, null, 3);
%>

<!-- Aside -->
<div class="aside m0">

	<%
	if(favoriteCourses==null) favoriteCourses = new CourseInfo[]{};
	if(recommendedCourses==null) recommendedCourses = new CourseInfo[]{};
	
	if(favoriteCourses.length>0){
		CourseInfo course = favoriteCourses[0];
	%>
		<!-- AD Block1 -->
		<div class="aside_block">
			<a href="courseHome.sw?courseId=<%=course.getId() %>" class="js_sera_content">
				<div class="header">
					<div class=""><%=course.getName() %></div>
				</div>
				<div class="content">
					<!-- 코스 이미지-->
					<div class="ad_photo">
						<img class="profile_size_72"
							src="<%=course.getOrgPicture()%>">
					</div>
					<!-- 코스 이미지//-->
					<!-- 코스설명 -->
					<div class="ad_text"><%=course.getBriefDesc() %></div>
					<!-- 코스설명 //-->
				</div>
			</a>
		</div>
		<!-- AD Block1 //-->	
	<%
	}
	if(recommendedCourses.length>0){
		CourseInfo course = recommendedCourses[0];
	%>
		<!-- AD Block1 -->
		<div class="aside_block">
			<a href="courseHome.sw?courseId=<%=course.getId() %>" class="js_sera_content">
				<div class="header">
					<div class=""><%=course.getName() %></div>
				</div>
				<div class="content">
					<!-- 코스 이미지-->
					<div class="ad_photo">
						<img class="profile_size_72"
							src="<%=course.getOrgPicture()%>">
					</div>
					<!-- 코스 이미지//-->
					<!-- 코스설명 -->
					<div class="ad_text"><%=course.getBriefDesc() %></div>
					<!-- 코스설명 //-->
				</div>
			</a>
		</div>
		<!-- AD Block1 //-->	
	<%
	}
	if(favoriteCourses.length>1){
		CourseInfo course = favoriteCourses[1];
	%>
		<!-- AD Block1 -->
		<div class="aside_block">
			<a href="courseHome.sw?courseId=<%=course.getId() %>" class="js_sera_content">
				<div class="header">
					<div class=""><%=course.getName() %></div>
				</div>
				<div class="content">
					<!-- 코스 이미지-->
					<div class="ad_photo">
						<img class="profile_size_72"
							src="<%=course.getOrgPicture()%>">
					</div>
					<!-- 코스 이미지//-->
					<!-- 코스설명 -->
					<div class="ad_text"><%=course.getBriefDesc() %></div>
					<!-- 코스설명 //-->
				</div>
			</a>
		</div>
		<!-- AD Block1 //-->	
	<%
	}
	if(recommendedCourses.length>1){
		CourseInfo course = recommendedCourses[1];
	%>
		<!-- AD Block1 -->
		<div class="aside_block">
			<a href="courseHome.sw?courseId=<%=course.getId() %>" class="js_sera_content">
				<div class="header">
					<div class=""><%=course.getName() %></div>
				</div>
				<div class="content">
					<!-- 코스 이미지-->
					<div class="ad_photo">
						<img class="profile_size_72"
							src="<%=course.getOrgPicture()%>">
					</div>
					<!-- 코스 이미지//-->
					<!-- 코스설명 -->
					<div class="ad_text"><%=course.getBriefDesc() %></div>
					<!-- 코스설명 //-->
				</div>
			</a>
		</div>
		<!-- AD Block1 //-->	
	<%
	}
	if(favoriteCourses.length>2){
		CourseInfo course = favoriteCourses[2];
	%>
		<!-- AD Block1 -->
		<div class="aside_block">
			<a href="courseHome.sw?courseId=<%=course.getId() %>" class="js_sera_content">
				<div class="header">
					<div class=""><%=course.getName() %></div>
				</div>
				<div class="content">
					<!-- 코스 이미지-->
					<div class="ad_photo">
						<img class="profile_size_72"
							src="<%=course.getOrgPicture()%>">
					</div>
					<!-- 코스 이미지//-->
					<!-- 코스설명 -->
					<div class="ad_text"><%=course.getBriefDesc() %></div>
					<!-- 코스설명 //-->
				</div>
			</a>
		</div>
		<!-- AD Block1 //-->	
	<%
	}
	if(recommendedCourses.length>2){
		CourseInfo course = favoriteCourses[2];
	%>
		<!-- AD Block1 -->
		<div class="aside_block">
			<a href="courseHome.sw?courseId=<%=course.getId() %>" class="js_sera_content">
				<div class="header">
					<div class=""><%=course.getName() %></div>
				</div>
				<div class="content">
					<!-- 코스 이미지-->
					<div class="ad_photo">
						<img class="profile_size_72"
							src="<%=course.getOrgPicture()%>">
					</div>
					<!-- 코스 이미지//-->
					<!-- 코스설명 -->
					<div class="ad_text"><%=course.getBriefDesc() %></div>
					<!-- 코스설명 //-->
				</div>
			</a>
		</div>
		<!-- AD Block1 //-->	
	<%
	}
	%>
</div>
<!-- Aside //-->
