<%@page import="net.smartworks.model.sera.SeraNotice"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ page import="net.smartworks.model.community.*"%>
<%@ page import="net.smartworks.model.notice.*"%>

<script>

// 서버에서 SmartUtil.publishMessage()로 NOTICE_COUNT를 현재사용자에게 메시지를 보내면 처리하는 함수이다.
function updateNoticeCount(message){
	var type = message.type;
	var count = message.count;
	var data = "<span></span>";
	if (count > 0)
		data = "<em class='icon_number'>" + count + "<span></span></em>";
	else
		data = "";
	
	if (type == 0) {
		$('#notification_count').html(data);
	} else if (type == 11) {
		$('#my_course_count').html(data);
	} else if (type == 12) {
		$('#friend_count').html(data);
	} else if (type == 1) {
		$('#message_count').html(data);
	} else if (type == 13) {
		$('#calendar_count').html(data);
	} else if (type == 14) {
		$('#badge_count').html(data);
	} else if (type == 15) {
		$('#event_count').html(data);
	}
};
</script>
<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 그리고 현재사용자 정보도 가져온다.	
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();

	Notice[] notices = smartWorks.getSeraNoticesForMe();
	
%>

<div class="hd_shadow js_header_page">
	<!-- GNB -->
	<div class="gnb">
		<div class="top_logo"><a href="logins.sw"></a></div>
		<ul class="top_menu2">
			<li class="fl">
				<a href="Course.sw"><img width="101" height="28" title="Course" alt="Course" src="sera/images/sera2_main_btnTopMenu1_off.png"></a>
			</li>
			<li class="fl">
				<a href="myPAGE.sw"><img width="101" height="28" title="myPAGE" alt="myPAGE" src="sera/images/sera2_main_btnTopMenu2_on.png"></a>
			</li>
			<li class="fl">
				<a href="http://blog.seracampus.com" class="js_sera_content"><img width="101" height="28" title="seraCLUB" alt="seraCLUB" src="sera/images/sera2_main_btnTopMenu3_off.png"></a>
			</li>
		</ul>
		<ul class="util_menu">
			<li class="about"><a href="aboutSera.sw">about SERA</a></li>
			<li class="news"><a href="seraNews.sw">sera 소식</a></li>
			<li class="btn_login"><a href="logout"><img width="49" height="19" title="로그아웃" alt="로그아웃" src="sera/images/sera2_main_btnLogout.png"></a></li>
		</ul>

		<!-- Search -->
		<div class="top_srch_section">
			<input type="text" class="top_srch" />
			<div class="icon_srch"></div>
		</div>
		<!-- Search //-->
	</div>
	<!-- GNB //-->
	<!-- Top Navi -->
	<div class="top_navi">
		<div class="personal_info fl mt5">
			<a href="myPAGE.sw">
				<div class="profile_pic fl"><img class="profile_size_c" src="<%=cUser.getMidPicture() %>"></div>
			</a>
			<div class="pofile_con">
				<div class="nick_name"><%=cUser.getNickName() %>님<span class="bul_down"><a href="myProfile.sw" class="js_sera_content"></a> </span></div>
			</div>
			<!-- Btn in Gnb - 코스 만들기-->
			<div class="btn_gnb">
				<div href="createCourse.sw" class="btn_gnb_l js_sera_content"><div class="btn_gnb_r">코스 만들기</div></div>
			</div>
		</div>
		<!-- Global Navi Icon -->
		<div class="gni">
			<ul>
				<li class="icon_alarm">
					<a href="" id="notification_count" class="js_notification_list_btn" title="알림">
						<%
			 			if (notices.length > SeraNotice.INDEX_NOTIFICATION && notices[SeraNotice.INDEX_NOTIFICATION].getLength() > 0) {
			 			%> 
							<em class="num_ic"><%=notices[SeraNotice.INDEX_NOTIFICATION].getLength() %><span> </span></em>
						<%
						} 
						%>
					</a>
				</li>
				<li class="icon_mycourse">
					<a href="myCourses.sw" id="my_course_count" class="js_sera_content" title="내코스">
						<%
			 			if (notices.length > SeraNotice.INDEX_MY_COURSE && notices[SeraNotice.INDEX_MY_COURSE].getLength() > 0) {
			 			%> 
							<em class="num_ic"><%=notices[SeraNotice.INDEX_MY_COURSE].getLength() %><span> </span></em>
						<%
						} 
						%>
					</a>
				</li>
				<li class="icon_friend">
					<a href="socialFriend.sw" id="friend_count" class="js_sera_content" title="친구">
						<%
			 			if (notices.length > SeraNotice.INDEX_FRIEND && notices[SeraNotice.INDEX_FRIEND].getLength() > 0) {
			 			%> 
							<em class="num_ic" style="left:17px"><%=notices[SeraNotice.INDEX_FRIEND].getLength() %><span> </span></em>
						<%
						} 
						%>
					</a>
				</li>
				<li class="icon_notes">
					<a href="socialNote.sw" id="message_count" class="js_sera_content" title="쪽지">
						<%
			 			if (notices.length > SeraNotice.INDEX_MESSAGE && notices[SeraNotice.INDEX_MESSAGE].getLength() > 0) {
			 			%> 
							<em class="num_ic" style="left:20px"><%=notices[SeraNotice.INDEX_MESSAGE].getLength() %><span> </span></em>
						<%
						} 
						%>
					</a>
				</li>
				<li class="icon_calendar">
					<a class="js_sera_content" id="calendar_count" title="캘린더">
						<%
			 			if (notices.length > SeraNotice.INDEX_CALENDAR && notices[SeraNotice.INDEX_CALENDAR].getLength() > 0) {
			 			%> 
							<em class="num_ic" style="left:24px"><%=notices[SeraNotice.INDEX_CALENDAR].getLength() %><span> </span></em>
						<%
						} 
						%>
					</a>
				</li>
				<li class="icon_badge">
					<a class="js_sera_content" id="badge_count" title="뱃지">
						<%
			 			if (notices.length > SeraNotice.INDEX_BADGE && notices[SeraNotice.INDEX_BADGE].getLength() > 0) {
			 			%> 
							<em class="num_ic" style="left:15px"><%=notices[SeraNotice.INDEX_BADGE].getLength() %><span> </span></em>
						<%
						} 
						%>
					</a>
				</li>
				<li class="icon_event">
					<a href="socialEvent.sw" id="event_count" class="js_sera_content" title="이벤트">
						<%
			 			if (notices.length > SeraNotice.INDEX_EVENT && notices[SeraNotice.INDEX_EVENT].getLength() > 0) {
			 			%> 
							<em class="num_ic" style="left:19px"><%=notices[SeraNotice.INDEX_EVENT].getLength() %><span> </span></em>
						<%
						} 
						%>
					</a>
				</li>
			</ul>
		</div>
	</div>
	<!-- Top Navi //-->
	<div class="js_notification_list_box"></div>
</div>
