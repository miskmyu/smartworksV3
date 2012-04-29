<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ page import="net.smartworks.model.community.*"%>
<%@ page import="net.smartworks.model.notice.*"%>
<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 그리고 현재사용자 정보도 가져온다.	
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
%>

<div class="hd_shadow bg_no">
	<!-- GNB -->
	<div class="gnb">
		
		<ul class="top_menu2" style="margin-left: 340px">
			<li class="fl"><a href="Course.sw"> <img width="101" height="28" src="sera/images/sera2_main_btnTopMenu1_off.png" /></a></li>
			<li class="fl"><a href="" class="j_btn_login_form"> <img width="101" height="28" src="sera/images/sera2_main_btnTopMenu2_off.png" /></a></li>
			<li class="fl"><a href="http://blog.seracampus.com"> <img width="101" height="28"src="sera/images/sera2_main_btnTopMenu3_off.png" /> </a></li>
		</ul>
		<ul class="util_menu fr">
			<li class="about"><a href="aboutSera.sw">about SERA</a></li>
			<li class="news"><a href="seraNews.sw">sera 소식</a></li>
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
       <h1 class="logo"> <a href="logins.sw"> <img width="201" height="36" alt="" src="sera/images/sera2_logo.png"> </a> </h1>
       <ul class="srch">
         <li><input type="text" placeholder="SERA를 검색하세요." style="width:235px; height:20px" title="검색"></li>
         <li> <div class="icon_srch_m"> </div></li>
       </ul>
     </div>
     <!-- Top Navi //-->
</div>

<script type="text/javascript">
$(function() {
	$(document).keypress(function(e) {
		var e = window.event || e;
		var keyCode = e.which || e.keyCode;
        if (keyCode == $.ui.keyCode.ENTER) {
			if ($('.login_section').is(':visible')) {
				$('input[type="submit"]').click();
				return false;
			}
		} else {
			return true;
		}
	});

	$('.j_btn_login_form').live('click', function(e) {
		$('.login_section').show();
		return false;
	});
});
</script>
