<%@page import="java.util.TimeZone"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
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
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link type="text/css" rel="stylesheet" href="css/default.css"/>
	<link type="text/css" rel="stylesheet" href="sera/css/pop.css"/>
	<link type="text/css" rel="stylesheet" href="sera/css/form.css"/>
	<link type="text/css" rel="stylesheet" href="sera/css/page.css"/>
	<script type="text/javascript" src="js/jquery/jquery-1.6.2.min.js"></script>
	<script type="text/javascript" src="js/sw/sw-language.js"></script>
	<script type="text/javascript" src="js/sw/sw-language-ko.js"></script>
	<script type="text/javascript" src="js/sw/sw-language-en.js"></script>
	<script type="text/javascript" src="sera/js/rolling_img.js"></script>
	<script type="text/javascript" src="js/sw/sw-util.js"></script>
	<script type="text/javascript" src='js/sw/sw-popup.js'></script>
	<title>세라캠퍼스에 오신걸 환영합니다.</title>
</head>
<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 현재사용자 정보도 가져온다
	ISmartWorks smartWorks = (ISmartWorks)SmartUtil.getBean("smartWorks", request);
	CourseInfo[] favoriteCourses = smartWorks.getFavoriteCourses(6);
	CourseInfo[] recommendedCourses = smartWorks.getRecommendedCourses(6);
%>

	<script type="text/javascript">
		var currentUser = {
			locale : "<%=java.util.Locale.getDefault().getLanguage()%>",
			timeZone : "<%=TimeZone.getDefault().getID()%>",
			timeOffset : "<%=TimeZone.getDefault().getRawOffset()%>"
		};
	</script>

<body>
	<div id="wrap" class="main_bg">
		<div id="sera_header_main">
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
					<li class="btn_login"><a href="" class="j_btn_login_form"><img width="49" height="19" title="로그인" alt="로그인" src="sera/images/sera2_main_btnLogin.png" /></a></li>
				</ul>
				<!-- Search -->
				<div class="top_srch_section" style="display: none">
					<input type="text" class="top_srch" />
					<div class="icon_srch"></div>
				</div>
				<!-- Search //-->
			</div>
			<!-- GNB //-->
			<!-- Login Section -->
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
						<li style=""><a href="joinUs.sw">회원가입</a> &nbsp;|&nbsp; <a href="">비밀번호찾기</a></li>
					</ul>
				</form>
			</div>
			</div>
			<!-- Login Section //-->

			<!-- Top Logo_Srch -->
			<div class="logo_srch">
				<h1 class="logo">
					<a href="logins.sw"> <img width="201" height="36" alt="" src="sera/images/sera2_logo.png"> </a>
				</h1>
				<ul class="srch">
					<li><input type="text" placeholder="SERA를 검색하세요." style="width: 235px; height: 20px" title="검색"></li>
					<li><div class="icon_srch_m"></div></li>
				</ul>
			</div>
			<!-- Top Logo_Srch //-->
			<!-- Slogun -->
			<div class="sera_slogun"><img width="875" height="32" alt="" src="sera/images/main_slogun.jpg"></div>
			<!-- Slogun//-->
		</div>
		<!-- Intro AD -->
		<div id="intro_main_section">
			<!-- Left-->
			<ul class="sera_guide">
				<li class="btn_start">
					<a href="helpCenter.sw"> 
						<img width="285" height="65" title="SERA Campus 시작하기" alt="SERA Campus 시작하기" src="sera/images/main_btn_start.png">
					</a>
				</li>
				<li class="img_guide"><img width="285" height="74" src="sera/images/main_img_cpsguid.png"></li>
				<li class="guide_menu">
					<dl>
						<dd><a href="helpCenter.sw"> <img width="61" height="61" title="코스개설하기" alt="코스개설하기" src="sera/images/mbtn_cus_make.png"> </a></dd>
						<dd><a href="helpCenter.sw"> <img width="61" height="61" title="코스참여하기" alt="코스참여하기" src="sera/images/mbtn_cus_part.png"> </a></dd>
						<dd><a href="helpCenter.sw"> <img width="61" height="61" title="친구설정하기" alt="친구설정하기" src="sera/images/mbtn_cus_friend.png"> </a></dd>
						<dd><a href="helpCenter.sw"> <img width="61" height="61" title="myPage" alt="myPage" src="sera/images/mbtn_cus_mypage.png"> </a></dd>
					</dl>
				</li>
				<li class="sponsor">
					<dl>
						<dd class="btn_pre"></dd>
						<dd></dd>
						<dd class="spon_lst">
							<label> <img width="119" height="42" src="sera/images/sera2_main_banner_cj.png"></label> 
							<label> <img width="119" height="42" src="sera/images/sera2_main_banner_oliveyoung.png"></label> 
							<label> <img width="119" height="42" src="sera/images/sera2_main_banner_CFP.png"></label> 
							<label> <img width="119" height="42" src="sera/images/sera2_main_banner_Sokang.png"></label> 
							<label> <img width="119" height="42" src="sera/images/sera2_main_banner_KU.png"></label> 
							<label> <img width="119" height="42" src="sera/images/sera2_main_banner_millet.png"></label>
						</dd>
						<dd class="btn_next"></dd>
						<dd></dd>
					</dl>
				</li>
				<li><img width="286" height="45" src="sera/images/main_txt_sponsor.png"></li>
			</ul>
			<!-- Left //-->
			<!-- Center Visual -->
			<ul class="sera_visual">
				<li>
					<label id="pnlBanner" style="position: absolute; left: 180px; bottom: 5px; z-index: 1000;">
						<a id="eventBtn1" href="http://blog.seracampus.com/?p=718"> 
							<img id="eventBtnOn1" width="9" height="9" alt="" src="sera/images/main_indicator_on.png" style="display: none;">
							<img id="eventBtnOff1" width="9" height="9" alt="" style="" src="sera/images/main_indicator_off.png"> 
						</a> 
						<a id="eventBtn2" href="http://blog.seracampus.com/?p=786"> 
							<img id="eventBtnOn2" width="9" height="9" alt="" src="sera/images/main_indicator_on.png" style=""> 
							<img id="eventBtnOff2" width="9" height="9" alt="" style="display: none;" src="sera/images/main_indicator_off.png">
						</a> 
						<a id="eventBtn3" href="http://blog.seracampus.com/?p=685"> 
							<img id="eventBtnOn3" width="9" height="9" alt="" src="sera/images/main_indicator_on.png" style="display: none;">
							<img id="eventBtnOff3" width="9" height="9" alt="" style="" src="sera/images/main_indicator_off.png"> 
						</a> 
					</label> 
					<a href="http://blog.seracampus.com/?p=718"> 
						<img id="eventBanner1" width="405" height="435" alt="" style="position: absolute; z-index: 9;" src="sera/images/visual_03.png"> 
					</a> 
					<a href="http://blog.seracampus.com/?p=786"> 
						<img id="eventBanner2" width="405" height="435" alt="" style="position: absolute; z-index: 10;" src="sera/images/visual_01.png"> 
					</a> 
					<a href="http://blog.seracampus.com/?p=685"> 
						<img id="eventBanner3" width="405" height="435" alt="" style="position: absolute; z-index: 1;" src="sera/images/visual_04.png"> 
					</a>
				</li>
			</ul>
			<!-- Center Visual//-->
			<!-- Right -->
			<ul class="sera_news">
				<li class="lst_normal">
					<dl class="">
						<dt>
							<div class="icon_mn_news fl">SERA 소식</div>
							<div class="icon_mn_more"><a href="seraNews.sw" class="mt13"> </a></div>
						</dt>
						<dd>
							<ul>
								<li>- 세라 캠퍼스가 기능을 강화하여 새롭...</li>
								<li>- 장동인 대표의 [클라우드]코스가 신설...</li>
								<li>- TEDx 과천 강의가 업로드...</li>
								<li>- [전하진 칼럼] 스펙보다 열정을 봐라...</li>
								<li>- [세라소식] 게시글 제목은 최근 5개만...</li>
							</ul>
						</dd>
					</dl>
				</li>

				<li class="lst_normal end mt20">
					<dl class="">
						<dt>
							<div class="icon_as_srtrend fl">트렌드 세라</div>
							<div class="icon_mn_more" style="display:none"><a href="" class="mt13"> </a></div>
						</dt>
						<dd>
							<ul>
								<li>- 세라 캠퍼스가 기능을 강화하여 새롭...</li>
								<li>- 장동인 대표의 [클라우드]코스가 신설...</li>
								<li>- TEDx 과천 강의가 업로드...</li>
								<li>- [전하진 칼럼] 스펙보다 열정을 봐라...</li>
								<li>- [세라소식] 게시글 제목은 최근 5개만...</li>
							</ul>
						</dd>
					</dl>
				</li>
			</ul>
			<!-- Right//-->
		</div>
		<!-- Intro AD//-->
		<!-- Content Section -->
		<div id="inro_ct_section">
			<!-- 리스트 L -->
			<div class="intro_category fl">
				<h2 class="mb10"><span class="ct_tit_01"> </span></h2>
				<%
				if(!SmartUtil.isBlankObject(favoriteCourses)){
					for(int i=0; i<favoriteCourses.length; i++){
						CourseInfo course = favoriteCourses[i];
						String achievedPoint =course.getAchievedRatio() + "%";
				%>
						<ul class="category_box intro_margin">
							<li class="photo">
								<label style="position: absolute;" class="ribbon_mission"></label> 
								<a href="courseHome.sw?courseId=<%=course.getId() %>" class="js_sera_content">
									<img width="218" height="148" src="<%=course.getOrgPicture()%>">
								</a>
							</li>
							<li class="subject">
								<a href="courseHome.sw?courseId=<%=course.getId() %>" class="js_sera_content"><%=course.getName() %></a>
							</li>
							<li class="maker"><a href="othersPAGE.sw?userId=<%=course.getOwner().getId()%>"><%=course.getOwner().getNickName() %></a>
							</li>
							<li class="info">
								<dl>
									<dd class="menteeNo"><%=course.getNumberOfGroupMember() %>명</dd>
									<dd class="makeDate"><%=course.getOpenDate() == null ? "" : course.getOpenDate().toLocalDateSimple2String() %></dd>
									<dd class="category"></dd>
								</dl>
							</li>
							<li class="detail"><a href="courseHome.sw?courseId=<%=course.getId() %>" class="js_sera_content"><%=course.getBriefDesc() %></a>
							</li>
							<!-- Gauge -->
							<li class="gauge_box w_auto mt5">
								<dl>
									<dd class="gauge_outline w170">
										<label class="gauge" style="width: <%=achievedPoint%>;"></label>
									</dd>
									<dd class="process">(<%=course.getAchievedPoint() %>/<%=course.getTargetPoint() %>)</dd>
								</dl>
							</li>
							<!-- Gauge //-->
						</ul>
				<%
					}
				}
				%>
			</div>
			<!-- 리스트 L //-->
			<!-- 리스트 R -->
			<div class="intro_category fr">
				<h2 class="mb10">
					<span class="ct_tit_02"> </span>
				</h2>
				<%
				if(!SmartUtil.isBlankObject(recommendedCourses)){
					for(int i=0; i<recommendedCourses.length; i++){
						CourseInfo course = recommendedCourses[i];
						String achievedPoint =course.getAchievedRatio() + "%";
				%>
						<ul class="category_box intro_margin">
							<li class="photo">
								<label style="position: absolute;" class="ribbon_like"></label> 
								<a href="courseHome.sw?courseId=<%=course.getId() %>" class="js_sera_content"> 
									<img width="218" height="148" src="<%=course.getOrgPicture()%>"> 
								</a>
							</li>
							<li class="subject">
								<a href="courseHome.sw?courseId=<%=course.getId() %>" class="js_sera_content"><%=course.getName() %></a>
							</li>
							<li class="maker">
								<a href="othersPAGE.sw?userId=<%=course.getOwner().getId()%>"><%=course.getOwner().getNickName() %></a>
							</li>
							<li class="info">
								<dl>
									<dd class="menteeNo"><%=course.getNumberOfGroupMember() %>명</dd>
									<dd class="makeDate"><%=course.getOpenDate() == null ? "" : course.getOpenDate().toLocalDateSimple2String() %></dd>
									<dd class="category"></dd>
								</dl>
							</li>
							<li class="detail">
								<a href="courseHome.sw?courseId=<%=course.getId() %>" class="js_sera_content"><%=course.getBriefDesc() %></a>
							</li>
							<!-- Gauge -->
							<li class="gauge_box w_auto mt5">
								<dl>
									<dd class="gauge_outline w170">
										<label class="gauge" style="width: <%=achievedPoint%>;"></label>
									</dd>
									<dd class="process">(<%=course.getAchievedPoint() %>/<%=course.getTargetPoint() %>)</dd>
								</dl>
							</li>
							<!-- Gauge //-->
						</ul>
				<%
					}
				}
				%>
			</div>
			<!-- 리스트 R //-->
		</div>
		<!-- Content Section//-->
		<!-- Footer -->
		<div id="sera_footer">
			<ul class="footer_box">
				<li class="copyright"><img width="270" height="20" src="sera/images/sera2_footer_Copyright.png" /></li>
				<li class="policy"><a href="">회사소개</a> | <a href="">이용약관</a> | <a href="">개인정보취급방침</a> | <a href="">고객센터</a></li>
			</ul>
		</div>
		<!-- Footer //-->
	</div>
</body>
</html>

<%
	String type = (String) request.getAttribute("type");
%>
<script type="text/javascript">
$(function() {
	<%if (type.equals("failedLogin")) {%>
		smartPop.showInfo(smartPop.ERROR, '로그인정보가 잘못되었습니다. 확인 후 다시 시도하시기 바랍니다.');
	<%} else if (type.equals("logout")) {%>
		smartPop.showInfo(smartPop.INFO, '성공적으로 로그아웃 되었습니다.');
	<%} else if (type.equals("expiredSession")) {%>
		if (top.document.location.href !== "logins.sw")
			top.document.location.href = "logins.sw";
	<%}%>
	$(document).keypress(function(e) {
			if ((e.which && e.which == 13) || (e.keyCode && e.keyCode == 13)) {
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
