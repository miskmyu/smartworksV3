<%@page import="org.springframework.security.web.context.HttpSessionSecurityContextRepository"%>
<%@page import="org.springframework.security.core.context.SecurityContext"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
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
	
	if (type == 0) {
		if(count >0 ) data = "<em class='icon_number'>" + count +  "<span> </span></em>";
		$('#notification_count').html(data);
	} else if (type == 11) {
		if(count >0 ) data = "<em class='icon_number course'>" + count +  "<span> </span></em>";
		$('#my_course_count').html(data);
	} else if (type == 12) {
		if(count >0 ) data = "<em class='icon_number friend'>" + count +  "<span> </span></em>";
		$('#friend_count').html(data);
	} else if (type == 1) {
		if(count >0 ) data = "<em class='icon_number message'>" + count +  "<span> </span></em>";
		$('#message_count').html(data);
	} else if (type == 13) {
		if(count >0 ) data = "<em class='icon_number calendar'>" + count +  "<span> </span></em>";
		$('#calendar_count').html(data);
	} else if (type == 14) {
		if(count >0 ) data = "<em class='icon_number badge'>" + count +  "<span> </span></em>";
		$('#badge_count').html(data);
	} else if (type == 15) {
		if(count >0 ) data = "<em class='icon_number event'>" + count +  "<span> </span></em>";
		$('#event_count').html(data);
	}
};
</script>
<%
	boolean isAuthenticated = false;
	SecurityContext context = (SecurityContext) request.getSession().getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
	if (!SmartUtil.isBlankObject(context)) {
		isAuthenticated = true;
	}
		
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 그리고 현재사용자 정보도 가져온다.	
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks"); 
	User cUser = (isAuthenticated) ? SmartUtil.getCurrentUser() : null;;
	
	String currentMenu = (String)session.getAttribute("currentMenu");
	if(SmartUtil.isBlankObject(currentMenu)) currentMenu = "none";
	Boolean noUser = (Boolean)session.getAttribute("noUser");

	Notice[] notices = (isAuthenticated && !noUser) ? smartWorks.getSeraNoticesForMe() : null;
	if(SmartUtil.isBlankObject(notices)) notices = new Notice[]{};
	
%>

<%
if(isAuthenticated){
%>
<div class="case_login">
	<div class="header_section js_header_page">
		<!-- GNB -->
		<div class="gnb">
			<div class="top_logo"><a href="javascript:NavigateParent('logins.sw');"></a></div>
			<ul class="top_menu2">
				<li class="fl">
					<a href="javascript:NavigateParent('Course.sw');"><img width="101" height="28" title="Course" alt="Course" 
						src="sera/images/sera2_main_btnTopMenu1_<%if(currentMenu.equals("Course")){ %>on<%}else{ %>off<%} %>.png">
					</a>
				</li>
				<li class="fl">
					<a href="javascript:NavigateParent('myPAGE.sw');"><img width="101" height="28" title="myPAGE" alt="myPAGE" 
						src="sera/images/sera2_main_btnTopMenu2_<%if(currentMenu.equals("myPAGE")){ %>on<%}else{ %>off<%} %>.png">
					</a>
				</li>
				<li class="fl">
					<a href="javascript:NavigateParent('http://blog.seracampus.com');"><img width="101" height="28" title="seraCLUB" alt="seraCLUB" 
						src="sera/images/sera2_main_btnTopMenu3_<%if(currentMenu.equals("seraCLUB")){ %>on<%}else{ %>off<%} %>.png">
					</a>
				</li>
			</ul>
			<ul class="util_menu">
				<li class="about"><a href="javascript:NavigateParent('aboutSera.sw');">about SERA</a></li>
				<li class="news"><a href="javascript:NavigateParent('seraNews.sw');">sera 소식</a></li>
				<li class="btn_login"><a href="javascript:NavigateParent('logout');"><img width="49" height="19" title="로그아웃" alt="로그아웃" src="sera/images/sera2_main_btnLogout.png"></a></li>
			</ul>
	
			<!-- Search -->
			<div class="top_srch_section">
				<input type="text" class="top_srch" />
				<div class="icon_srch"></div>
			</div>
			<!-- Search //-->
		</div>
		<!-- GNB //-->
		<%
		if(noUser){
		%>
		     <!-- Top Navi -->
		     <div class="logo_srch">
		       <h1 class="logo"> <a href="javascript:NavigateParent('logins.sw');"> <img width="201" height="36" alt="" src="sera/images/sera2_logo.png"> </a> </h1>
		       <ul class="srch">
		         <li><input type="text" placeholder="SERA를 검색하세요." style="width:235px; height:20px" title="검색"></li>
		         <li> <div class="icon_srch_m"> </div></li>
		       </ul>
		     </div>
		     <!-- Top Navi //-->
		<%
		}else{
		%>
			<!-- Top Navi -->
			<div class="top_navi">
				<div class="personal_info fl mt5">
					<a href="javascript:NavigateParent('myPAGE.sw');">
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
								if(!CommonUtil.isEmpty(notices)) {
						 			if (notices.length > SeraNotice.INDEX_NOTIFICATION && notices[SeraNotice.INDEX_NOTIFICATION].getLength() > 0) {
						 			%> 
										<em class="icon_number"><%=notices[SeraNotice.INDEX_NOTIFICATION].getLength() %><span> </span></em>
									<%
						 			}
								} 
								%>
							</a>
						</li>
						<li class="icon_mycourse">
							<a href="myCourses.sw" id="my_course_count" title="내코스">
								<%
								if(!CommonUtil.isEmpty(notices)) {
						 			if (notices.length > SeraNotice.INDEX_MY_COURSE && notices[SeraNotice.INDEX_MY_COURSE].getLength() > 0) {
						 			%> 
										<em class="icon_number course"><%=notices[SeraNotice.INDEX_MY_COURSE].getLength() %><span> </span></em>
									<%
						 			}
								} 
								%>
							</a>
						</li>
						<li class="icon_friend">
							<a href="socialFriend.sw" id="friend_count" title="친구">
								<%
								if(!CommonUtil.isEmpty(notices)) {
						 			if (notices.length > SeraNotice.INDEX_FRIEND && notices[SeraNotice.INDEX_FRIEND].getLength() > 0) {
						 			%> 
										<em class="icon_number friend"><%=notices[SeraNotice.INDEX_FRIEND].getLength() %><span> </span></em>
									<%
									} 
								}
								%>
							</a>
						</li>
						<li class="icon_notes">
							<a href="socialNote.sw" id="message_count" title="쪽지">
								<%
								if(!CommonUtil.isEmpty(notices)) {
						 			if (notices.length > SeraNotice.INDEX_MESSAGE && notices[SeraNotice.INDEX_MESSAGE].getLength() > 0) {
						 			%> 
										<em class="icon_number message"><%=notices[SeraNotice.INDEX_MESSAGE].getLength() %><span> </span></em>
									<%
						 			}
								} 
								%>
							</a>
						</li>
						<li class="icon_calendar">
							<a id="calendar_count" title="캘린더">
								<%
								if(!CommonUtil.isEmpty(notices)) {
						 			if (notices.length > SeraNotice.INDEX_CALENDAR && notices[SeraNotice.INDEX_CALENDAR].getLength() > 0) {
						 			%> 
										<em class="icon_number calendar"><%=notices[SeraNotice.INDEX_CALENDAR].getLength() %><span> </span></em>
									<%
						 			}
								} 
								%>
							</a>
						</li>
						<li class="icon_badge">
							<a id="badge_count" title="뱃지">
								<%
								if(!CommonUtil.isEmpty(notices)) {
						 			if (notices.length > SeraNotice.INDEX_BADGE && notices[SeraNotice.INDEX_BADGE].getLength() > 0) {
						 			%> 
										<em class="icon_number badge"><%=notices[SeraNotice.INDEX_BADGE].getLength() %><span> </span></em>
									<%
						 			}
								} 
								%>
							</a>
						</li>
						<li class="icon_event">
							<a href="socialEvent.sw" id="event_count" title="이벤트">
								<%
								if(!CommonUtil.isEmpty(notices)) {
						 			if (notices.length > SeraNotice.INDEX_EVENT && notices[SeraNotice.INDEX_EVENT].getLength() > 0) {
						 			%> 
										<em class="icon_number event"><%=notices[SeraNotice.INDEX_EVENT].getLength() %><span> </span></em>
									<%
						 			}
								} 
								%>
							</a>
						</li>
					</ul>
				</div>
			</div>
			<!-- Top Navi //-->
			<div class="js_notification_list_box"></div>
		<%
		}
		%>
	</div>
</div>
<%
}else{
%>
<div class="case_logout">
	<div class="header_section">
		<!-- GNB -->
		<div class="gnb">
			
			<ul class="top_menu2" style="margin-left: 340px">
				<li class="fl">
					<a href="javascript:NavigateParent('Course.sw');"> <img width="101" height="28" 
						src="sera/images/sera2_main_btnTopMenu1_<%if(currentMenu.equals("Course")){ %>on<%}else{ %>off<%} %>.png" />
					</a>
				</li>
				<li class="fl">
					<a href="" class="j_btn_login_form"> <img width="101" height="28" 
						src="sera/images/sera2_main_btnTopMenu2_<%if(currentMenu.equals("myPAGE")){ %>on<%}else{ %>off<%} %>.png" />
					</a>
				</li>
				<li class="fl">
					<a href="javascript:NavigateParent('http://blog.seracampus.com');"> <img width="101" height="28"
						src="sera/images/sera2_main_btnTopMenu3_<%if(currentMenu.equals("seraCLUB")){ %>on<%}else{ %>off<%} %>.png" /> </a></li>
			</ul>
			<ul class="util_menu fr">
				<li class="about"><a href="javascript:NavigateParent('aboutSera.sw');">about SERA</a></li>
				<li class="news"><a href="javascript:NavigateParent('seraNews.sw');">sera 소식</a></li>
				<li class="btn_login">
					<a href="" class="j_btn_login_form">
						<img width="49" height="19" title="로그인" alt="로그인" src="sera/images/sera2_main_btnLogin.png" /> 
					</a>
				</li>
			</ul>
			<!-- Search -->
			<div class="top_srch_section" style="display: none">
				<input type="text" class="top_srch" />
				<div class="icon_srch"></div>
			</div>
			<!-- Search //-->
		</div>
		<!-- GNB //-->
		<div class="login_section" style="display: none; z-index: 1000;">
			<div class="login_area">
				<form action="j_spring_security_check" method="post">
					<ul class="login_box">
						<li class="login_frm">
							<dl>
								<dd><input id="j_username" name="j_username" class="" type="text"></dd>
								<dd><input id="j_password" name="j_password" class="" type="password"></dd>
							</dl>
						</li>
						<li class="login_btn"><input class="btn_login btn_img_login" width="41" type="submit" height="41" title="로그인" src="sera/images/layer_btn_login.png" value=""></li>
					</ul>
					<ul class="login_util">
						<li style=""><a href="joinUs.sw">회원가입</a> &nbsp;|&nbsp; <a>비밀번호찾기</a></li>
					</ul>
				</form>
			</div>
		</div>
	     <!-- Top Navi -->
	     <div class="logo_srch">
	       <h1 class="logo"> <a href="javascript:NavigateParent('logins.sw');"> <img width="201" height="36" alt="" src="sera/images/sera2_logo.png"> </a> </h1>
	       <ul class="srch">
	         <li><input type="text" placeholder="SERA를 검색하세요." style="width:235px; height:20px" title="검색"></li>
	         <li> <div class="icon_srch_m"> </div></li>
	       </ul>
	     </div>
	     <!-- Top Navi //-->
	</div>
</div>

<script type="text/javascript">
$(function() {
	$(document).keypress(function(e) {
		var keyCode = e.which || e.keyCode;
		if(keyCode == 13) {
			if ($('.login_section').is(':visible')) {
				$('input[type="submit"]').click();
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

<%
}
%>
