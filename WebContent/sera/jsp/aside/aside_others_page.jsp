
<!-- Name 			: nav.jsp										 		 -->
<!-- Description	: 화면 왼쪽의 Navigation Bar 를 구성하는 화면  				 -->
<!-- Author			: Y.S. JUNG												 -->
<!-- Created Date	: 2011.9.												 -->

<%@page import="net.smartworks.model.sera.Constants"%>
<%@page import="net.smartworks.model.work.SmartWork"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.model.instance.info.BoardInstanceInfo"%>
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

	User otherUser = (User) session.getAttribute("otherUser");
	CourseList courseList = smartWorks.getCoursesById(otherUser.getId(), CourseList.MAX_BRIEF_COURSE_LIST);
	FriendList friendList = smartWorks.getFriendsById(otherUser.getId(), FriendList.MAX_BRIEF_FRIEND_LIST);
	BoardInstanceInfo[] seraTrends = smartWorks.getSeraTrends(10);
%>
<div class="aside_block">
	<div class="header">
			<span class="icon_as_mycourse">
				<a href="othersCourses.sw?userId=<%=otherUser.getId() %>" class="js_sera_content">
					<%=otherUser.getNickName()%>님의 코스 
					<span class="num_cus">(<%=courseList.getRunnings() + courseList.getAttendings()%>)</span>
					<span class="icon_as_more"></span>
				</a>
			</span>
	</div>
		
	<div class="content">
		<dl>
			<dt>운영코스 (<%=courseList.getRunnings()%>)</dt>
			<dd>
				<%
				if (courseList.getRunnings() > 0) {
				%>
					<ul>
						<%
						for (int i = 0; i < courseList.getRunningCourses().length; i++) {
							if(i==CourseList.MAX_BRIEF_COURSE_LIST) break;
							CourseInfo course = courseList.getRunningCourses()[i];
						%>
							<li>
								<a href="courseHome.sw?courseId=<%=course.getId()%>"><span class="t_blue"><%=course.getName()%></span></a>
							</li>
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
			<dt>참여코스 (<%=courseList.getAttendings()%>)</dt>
			<dd>
				<%
				if (courseList.getAttendings() > 0) {
				%>
					<ul>
						<%
						for (int i = 0; i < courseList.getAttendingCourses().length; i++) {
							if(i==CourseList.MAX_BRIEF_COURSE_LIST) break;
							CourseInfo course = courseList.getAttendingCourses()[i];
						%>
							<li>
								<a href="courseHome.sw?courseId=<%=course.getId()%>"><span class="t_blue"><%=course.getName()%></span></a>
							</li>
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
		<span class=" icon_as_badge">
			<%=otherUser.getNickName()%>님의 뱃지(0)
			<span class="icon_as_more" style="display:none"></span>
		</span>
	</div>
		
	<div class="list">
		<dl>
<!--
 			<dd><div class="badge_df"></div></dd>
			<dd><div class="badge_df"></div></dd>
			<dd><div class="badge_df"></div></dd>
			<dd><div class="badge_df"></div></dd>
			<dd><div class="badge_df"></div></dd>
			<dd><div class="badge_df"></div></dd>
			<dd><div class="badge_df"></div></dd>
			<dd><div class="badge_df"></div></dd>
			<dd><div class="badge_df"></div></dd>
			<dd><div class="badge_df"></div></dd>
			<dd><div class="badge_df"></div></dd>
 -->
 		</dl>
	</div>
</div>
<!-- Aside Block2 //-->
<!-- Aside Block3 -->
<div class="aside_block m0">
	<div class="header">
		<span class=" icon_as_friend">
			<a href="othersFriend.sw?userId=<%=otherUser.getId() %>" class="js_sera_content">
			<%=otherUser.getNickName()%>님의 친구 (<%=friendList.getTotalFriends()%>)
			<span class="icon_as_more"></span>
			</a>
		</span>
	</div>
		
	<div class="list">
		<dl>
			<%
				if(friendList.getFriends() != null) {
					for (int i = 0; i < friendList.getFriends().length; i++) {
						SeraUserInfo friend = friendList.getFriends()[i];
			%>
			<dd>
				<a href="othersPAGE.sw?userId=<%=friend.getId()%>"><img class="friend_df" src="<%=friend.getMinPicture()%>" title="<%=friend.getNickName()%>"></a>
			</dd>
			<%
					}
				}
			%>
		</dl>
	</div>
</div>
<!-- Aside Block3 //-->
 <!-- Aside Block4 -->
<div class="aside_block m0">
	<div class="header">
		<span class=" icon_as_srtrend">
			<a href="seraTrend.sw" class="">트렌드 세라
				<span class="icon_as_more"></span>
			</a>
		</span>
	</div>
	<div class="content">
		<dl>
			<dd>
				<ul>
					<%
					if(!SmartUtil.isBlankObject(seraTrends)){
						for(int i=0; i<seraTrends.length; i++){
							String target = "seraBoardItem.sw?workId=" + SmartWork.ID_BOARD_MANAGEMENT + "&instId=" + seraTrends[i].getId() + "&wid=" + Constants.SERA_WID_SERA_TREND;
					%>
								<li><a href="<%=target%>"><%=CommonUtil.toNotNull(seraTrends[i].getSubject()) %></a></li>							
					<%
						}
					}
					%>
					</ul>
			</dd>
		</dl>
	</div>
</div>
<!-- Aside Block4 //-->