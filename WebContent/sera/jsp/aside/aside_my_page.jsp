
<!-- Name 			: nav.jsp										 		 -->
<!-- Description	: 화면 왼쪽의 Navigation Bar 를 구성하는 화면  				 -->
<!-- Author			: Y.S. JUNG												 -->
<!-- Created Date	: 2011.9.												 -->

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

	CourseList courseList = smartWorks.getCoursesById(cUser.getId(), CourseList.MAX_BRIEF_COURSE_LIST);
	FriendList friendList = smartWorks.getFriendsById(cUser.getId(), FriendList.MAX_BRIEF_FRIEND_LIST);
%>

<!-- Search Section -->
<div class="srch_section">
	<div class="srch">
		<div>
			<input type="text" placeholder="SERA를 검색하세요.">
		</div>
		<div class="icon_admin fr"></div>
	</div>
	<div class="btn_default_l ml4">
		<div class="btn_default_r">채팅하기</div>
	</div>
</div>
<!-- Search Section -->

<!-- Aside -->
<div class="aside m0">
	<!-- Aside Block1 -->
	<div class="aside_block">
		<div class="header">
			<div class="icon_as_mycourse fl">내 코스 <span class="num_cus">(<%=courseList.getRunnings()+courseList.getAttendings() %>)</span></div>
			<div class="icon_as_more"><a href="myCourses.sw" class="mt10 js_sera_content"> </a></div>
		</div>
		<div class="content">
			<dl>
				<dt>운영코스 (<%=courseList.getRunnings() %>)</dt>
				<dd>
					<%
					if(courseList.getRunnings()>0){
					%>
						<ul>
							<%
							for(int i=0; i<courseList.getRunningCourses().length; i++){
								CourseInfo course = courseList.getRunningCourses()[i];
								String missionName = (SmartUtil.isBlankObject(course.getLastMission())) ? "" : course.getLastMission().getName();
							%>
									<li ><a href="courseHome.sw?courseId=<%=course.getId() %>" class="js_sera_content"><span class="t_blue"><%=course.getName() %></span><%=missionName%></a></li>
							<%
							}
							%>
						</ul>
					<%
					}
					%>
				</dd>
			</dl>
		</div>
		<div class="content">
			<dl>
				<dt>참여코스 (<%=courseList.getAttendings() %>)</dt>
				<dd>
					<%
					if(courseList.getAttendings()>0){
					%>
						<ul>
							<%
							for(int i=0; i<courseList.getAttendingCourses().length; i++){
								CourseInfo course = courseList.getAttendingCourses()[i];
								String missionName = (SmartUtil.isBlankObject(course.getLastMission())) ? "" : course.getLastMission().getName();
							%>
									<li><a href="courseHome.sw?courseId=<%=course.getId() %>" class="js_sera_content"><span class="t_blue"><%=course.getName() %></span><%=missionName%></a></li>
							<%
							}
							%>
						</ul>
					<%
					}
					%>
				</dd>
			</dl>
		</div>
	</div>
	<!-- Aside Block1 //-->
	
	  <!-- Aside Block2 -->
	<div class="aside_block m0">
		<div class="header">
			<div class=" icon_as_badge fl">뱃 지 (16)</div>
			<div class="icon_as_more"><a href="socialBadge.sw" class="mt10 js_sera_content"> </a></div>
		</div>
		<div class="list">
			<dl>
				<dd><div class="badge_df"> </div></dd>
				<dd><div class="badge_df"> </div></dd>
				<dd><div class="badge_df"> </div></dd>
				<dd><div class="badge_df"> </div></dd>
				<dd><div class="badge_df"> </div></dd>
				<dd><div class="badge_df"> </div></dd>
				<dd><div class="badge_df"> </div></dd>
				<dd><div class="badge_df"> </div></dd>
				<dd><div class="badge_df"> </div></dd>
				<dd><div class="badge_df"> </div></dd>
				<dd><div class="badge_df"> </div></dd>
			</dl>
		</div>
	</div>
	<!-- Aside Block2 //-->
		
	<!-- Aside Block3 -->
	<div class="aside_block m0">
		<div class="header">
			<div class=" icon_as_friend fl">친 구 (<%=friendList.getTotalFriends() %>)</div>
			<div class="icon_as_more"><a href="socialFriend.sw" class="mt10 js_sera_content"> </a></div>
		</div>
		<div class="list">
			<dl>
				<%
				for(int i=0; i<friendList.getFriends().length; i++){
					UserInfo friend = friendList.getFriends()[i];
				%>
					<dd><a href="othersPAGE.sw?userId=<%=friend.getId()%>"><img class="friend_df" src="<%=friend.getMinPicture()%>"></a></dd>
				<%
				}
				%>
			</dl>
		</div>
	</div>
	<!-- Aside Block3 //-->
	
	 <!-- Aside Block4 -->
	<div class="aside_block">
		<div class="header">
			<div class="icon_as_srtrend fl">트렌드 세라 (23)</div>
			<div class="icon_as_more"><a href="" class="mt10"> </a></div>
		</div>
		<div class="content">
			<dl>
				<dd>
					<ul>
						<li>[정치인 공부하기] 새누리당으로...</li>
						<li>[정치인 공부하기] 한나라당이...</li>
					</ul>
				</dd>
			</dl>
		</div>
	</div>
	<!-- Aside Block4 //-->
</div>      
