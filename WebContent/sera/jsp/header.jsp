<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ page import="net.smartworks.model.community.*"%>
<%@ page import="net.smartworks.model.notice.*"%>

<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 그리고 현재사용자 정보도 가져온다.	
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();
%>

<div class="hd_shadow">
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
		<div class="gni fr">
			<ul>
				<li class="icon_alarm"><a class="js_sera_content" title="알림"></a></li>
				<li class="icon_mycourse"><a href="myCourses.sw" class="js_sera_content" title="내코스"></a></li>
				<li class="icon_friend"><a href="socialFriend.sw" class="js_sera_content" title="친구"></a></li>
				<li class="icon_notes"><a class="js_sera_content" title="쪽지"></a></li>
				<li class="icon_calendar"><a class="js_sera_content" title="캘린더"></a></li>
				<li class="icon_badge"><a class="js_sera_content" title="뱃지"></a></li>
				<li class="icon_event"><a href="socialEvent.sw" class="js_sera_content" title="이벤트"></a></li>
			</ul>
		</div>
	</div>
	<!-- Top Navi //-->
</div>