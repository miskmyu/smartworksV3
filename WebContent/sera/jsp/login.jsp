<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>세라캠퍼스에 오신걸 환영합니다.</title>
	<link type="text/css" rel="stylesheet" href="css/default.css">
	<link type="text/css" rel="stylesheet" href="sera/css/form.css">
	<link type="text/css" rel="stylesheet" href="sera/css/page.css">
	<script type="text/javascript" src="js/jquery/jquery-1.6.2.min.js"></script>
	<script type="text/javascript" src="sera/js/rolling_img.js"></script>
</head>
<body>
	<div id="wrap" class="main_bg">
		<div id="sera_header_main">
			<!-- GNB -->
			<div class="gnb">
				<div class="top_logo" style="visibility: hidden; width: 360px">
				</div>
				<ul class="top_menu2">
					<li class="fl">
						<a href=""> <img width="101" height="28" src="sera/images/sera2_main_btnTopMenu1_off.png" /></a>
					</li>
					<li class="fl">
						<a href=""> <img width="101" height="28" src="sera/images/sera2_main_btnTopMenu2_on.png" /></a>
					</li>
					<li class="fl">
						<a href=""> <img width="101" height="28" src="sera/images/sera2_main_btnTopMenu3_off.png" /> </a>
					</li>
				</ul>
				<ul class="util_menu fr">
					<li class="about"><a href="">about SERA</a></li>
					<li class="news"><a href="">sera 소식</a></li>
					<li class="btn_login"><a href=""> <img width="49" height="19" title="로그아웃" alt="로그아웃" src="sera/images/sera2_main_btnLogout.png" /> </a></li>
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
			<div class="login_section" style="visibility: visible; z-index: 1000;">
				<form action="j_spring_security_check" method="post">
					<ul class="login_box">
						<li class="login_frm">
							<dl>
								<dd><input id="j_username" name="j_username" class="" type="text"></dd>
								<dd><input id="j_password" name="j_password" class="" type="password"></dd>
							</dl></li>
						<li class="login_btn">
							<input class="btn_login btn_img_login" width="41" type="submit" height="41" title="로그인" src="sera/images/layer_btn_login.png" value="">
						</li>
					</ul>
					<ul class="login_util">
						<li style=""><a href="joinUs.sw">회원가입</a> &nbsp;|&nbsp; <a href="">비밀번호찾기</a></li>
					</ul>
				</form>
			</div>
			<!-- Login Section //-->

			<!-- Top Logo_Srch -->
			<div class="logo_srch">
				<h1 class="logo">
					<a href="/"> <img width="201" height="36" alt="" src="sera/images/sera2_logo.png"> </a>
				</h1>
				<ul class="srch">
					<li><input type="text" placeholder="SERA를 검색하세요." style="width: 235px; height: 20px" title="검색"></li>
					<li><div class="icon_srch_m"></div></li>
				</ul>
			</div>
			<!-- Top Logo_Srch //-->
			<!-- Slogun -->
			<div class="sera_slogun">
				<img width="875" height="32" alt="" src="sera/images/main_slogun.png">
			</div>
			<!-- Slogun//-->
		</div>
		<!-- Intro AD -->
		<div id="intro_main_section">
			<!-- Left-->
			<ul class="sera_guide">
				<li class="btn_start">
					<a href=""> <img width="285" height="65" title="SERA Campus 시작하기" alt="SERA Campus 시작하기" src="sera/images/main_btn_start.png"></a>
				</li>
				<li class="img_guide"><img width="285" height="74" src="sera/images/main_img_cpsguid.png"></li>
				<li class="guide_menu">
					<dl>
						<dd>
							<a href=""> <img width="61" height="61" title="코스개설하기" alt="코스개설하기" src="sera/images/mbtn_cus_make.png"> </a>
						</dd>
						<dd>
							<a href=""> <img width="61" height="61" title="코스참여하기" alt="코스참여하기" src="sera/images/mbtn_cus_part.png"> </a>
						</dd>
						<dd>
							<a href=""> <img width="61" height="61" title="친구설정하기" alt="친구설정하기" src="sera/images/mbtn_cus_friend.png"> </a>
						</dd>
						<dd>
							<a href=""> <img width="61" height="61" title="myPage" alt="myPage" src="sera/images/mbtn_cus_mypage.png"> </a>
						</dd>
					</dl>
				</li>
				<li class="sponsor">
					<dl>
						<dd class="btn_pre"></dd>
						<dd></dd>
						<dd class="spon_lst">
							<label> 
								<a href="#"> <img width="119" height="42" src="sera/images/sera2_main_banner_cj.png"></a> 
							</label> 
							<label>
								<a href="#"> <img width="119" height="42" src="sera/images/sera2_main_banner_oliveyoung.png"></a> 
							</label> 
							<label>
								<a href="#"> <img width="119" height="42" src="sera/images/sera2_main_banner_CFP.png"></a> 
							</label> 
							<label>
								<a href="#"> <img width="119" height="42" src="sera/images/sera2_main_banner_Sokang.png"></a> 
							</label> 
							<label>
								<a href="#"> <img width="119" height="42" src="sera/images/sera2_main_banner_KU.png"></a> 
							</label> 
							<label>
								<a href="#"> <img width="119" height="42" src="sera/images/sera2_main_banner_millet.png"></a> 
							</label>
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
			<ul class="sera_friend">
				<li class="lst_normal">
					<dl class="friend_sum">
						<dd class="photo">
							<a href="http://blog.seracampus.com/?p=703"> 
								<img width="90" height="57" alt="" src="sera/images/element_sample4.png"> 
							</a>
						</dd>
						<dd class="info">
							<strong> <a href="http://blog.seracampus.com/?p=703">SERAian 인터뷰</a></strong><br> 이상호
						</dd>
						<dd class="btn_go">
							<a href="http://blog.seracampus.com/?p=703"> 
								<img width="22" height="21" title="바로가기" alt="바로가기" src="sera/images/main_btn_go.png"> 
							</a>
						</dd>
					</dl>
				</li>
				<li class="lst_normal">
					<dl class="friend_sum">
						<dd class="photo">
							<a target="_blank" href="http://www.youtube.com/watch?v=KneS3Mstmes"> 
								<img width="90" height="57" alt="" src="sera/images/element_sample5.png"> 
							</a>
						</dd>
						<dd class="info">
							<strong><a target="_blank" href="http://www.youtube.com/watch?v=KneS3Mstmes"> TEDx 과천 <br> 전하진 대표 </a> </strong>
						</dd>
						<dd class="btn_go">
							<a target="_blank" href="http://www.youtube.com/watch?v=KneS3Mstmes"> 
								<img width="22" height="21" title="바로가기" alt="바로가기" src="sera/images/main_btn_go.png"> 
							</a>
						</dd>
					</dl>
				</li>
				<li class="lst_normal">
					<dl class="friend_sum">
						<dd class="photo">
							<a href="http://blog.seracampus.com/?p=508"> <img width="90"height="57" alt="" src="sera/images/element_sample3.png"> </a>
						</dd>
						<dd class="info">
							<strong> <a href="http://blog.seracampus.com/?p=508">Recruiting Mentor</a> </strong> <br> 조동원 대표
						</dd>
						<dd class="btn_go">
							<a href="http://blog.seracampus.com/?p=508"> 
								<img width="22" height="21" src="sera/images/main_btn_go.png"> 
							</a>
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
				<ul class="category_box intro_margin">
					<li class="photo"><label style="position: absolute;"
						class="ribbon_mission"></label> <a href=""> <img width="218"
							height="148" src="sera/images/course_01.jpg"> </a></li>
					<li class="subject"><a href="">어느 젊은 마법사의 코스</a></li>
					<li class="maker"><a href="">SERA캠퍼스</a></li>
					<li class="info">
						<dl>
							<dd class="menteeNo">34명</dd>
							<dd class="makeDate">2012.02.07</dd>
							<dd class="category"></dd>
						</dl></li>
					<li class="detail"><a href="/Course/Detail/212">안녕하세요
							선린인터넷고등학교 여러분! 세라캠퍼스에 오신걸 환영합니다. ^^* 꿈그리기란, 어렵고 전문적인 것이 아니라
							'나'자신을 관찰하고 그것을 밖으로 표현하는 그림입니다...</a></li>
					<!-- Gauge -->
					<li class="gauge_box w_auto mt5">
						<dl>
							<dd class="gauge_outline w170">
								<label class="gauge" style="width: 50%;"></label>
							</dd>
							<dd class="process">(6/10)</dd>
						</dl></li>
					<!-- Gauge //-->
				</ul>
				<ul class="category_box intro_margin">
					<li class="photo"><label style="position: absolute;"
						class="ribbon_mission"></label> <a href=""> <img width="218"
							height="148" src="sera/images/course_01.jpg"> </a></li>
					<li class="subject"><a href="">어느 젊은 마법사의 코스</a></li>
					<li class="maker"><a href="">SERA캠퍼스</a></li>
					<li class="info">
						<dl>
							<dd class="menteeNo">34명</dd>
							<dd class="makeDate">2012.02.07</dd>
							<dd class="category"></dd>
						</dl></li>
					<li class="detail"><a href="/Course/Detail/212">안녕하세요
							선린인터넷고등학교 여러분! 세라캠퍼스에 오신걸 환영합니다. ^^* 꿈그리기란, 어렵고 전문적인 것이 아니라
							'나'자신을 관찰하고 그것을 밖으로 표현하는 그림입니다...</a></li>
					<!-- Gauge -->
					<li class="gauge_box w_auto mt5">
						<dl>
							<dd class="gauge_outline w170">
								<label class="gauge" style="width: 50%;"></label>
							</dd>
							<dd class="process">(6/10)</dd>
						</dl></li>
					<!-- Gauge //-->
				</ul>
				<ul class="category_box intro_margin">
					<li class="photo"><a href=""> <img width="218"
							height="148" src="sera/images/course_01.jpg"> </a></li>
					<li class="subject"><a href="">어느 젊은 마법사의 코스</a></li>
					<li class="maker"><a href="">SERA캠퍼스</a></li>
					<li class="info">
						<dl>
							<dd class="menteeNo">34명</dd>
							<dd class="makeDate">2012.02.07</dd>
							<dd class="category"></dd>
						</dl></li>
					<li class="detail"><a href="/Course/Detail/212">안녕하세요
							선린인터넷고등학교 여러분! 세라캠퍼스에 오신걸 환영합니다. ^^* 꿈그리기란, 어렵고 전문적인 것이 아니라
							'나'자신을 관찰하고 그것을 밖으로 표현하는 그림입니다...</a></li>
					<!-- Gauge -->
					<li class="gauge_box w_auto mt5">
						<dl>
							<dd class="gauge_outline w170">
								<label class="gauge" style="width: 50%;"></label>
							</dd>
							<dd class="process">(6/10)</dd>
						</dl></li>
					<!-- Gauge //-->
				</ul>
				<ul class="category_box intro_margin">
					<li class="photo"><a href=""> <img width="218"
							height="148" src="sear/images/course_01.jpg"> </a></li>
					<li class="subject"><a href="">어느 젊은 마법사의 코스</a></li>
					<li class="maker"><a href="">SERA캠퍼스</a></li>
					<li class="info">
						<dl>
							<dd class="menteeNo">34명</dd>
							<dd class="makeDate">2012.02.07</dd>
							<dd class="category"></dd>
						</dl></li>
					<li class="detail"><a href="/Course/Detail/212">안녕하세요
							선린인터넷고등학교 여러분! 세라캠퍼스에 오신걸 환영합니다. ^^* 꿈그리기란, 어렵고 전문적인 것이 아니라
							'나'자신을 관찰하고 그것을 밖으로 표현하는 그림입니다...</a></li>
					<!-- Gauge -->
					<li class="gauge_box w_auto mt5">
						<dl>
							<dd class="gauge_outline w170">
								<label class="gauge" style="width: 50%;"></label>
							</dd>
							<dd class="process">(6/10)</dd>
						</dl></li>
					<!-- Gauge //-->
				</ul>
				<ul class="category_box intro_margin">
					<li class="photo"><a href=""> <img width="218"
							height="148" src="sera/images/course_01.jpg"> </a></li>
					<li class="subject"><a href="">어느 젊은 마법사의 코스</a></li>
					<li class="maker"><a href="">SERA캠퍼스</a></li>
					<li class="info">
						<dl>
							<dd class="menteeNo">34명</dd>
							<dd class="makeDate">2012.02.07</dd>
							<dd class="category"></dd>
						</dl></li>
					<li class="detail"><a href="/Course/Detail/212">안녕하세요
							선린인터넷고등학교 여러분! 세라캠퍼스에 오신걸 환영합니다. ^^* 꿈그리기란, 어렵고 전문적인 것이 아니라
							'나'자신을 관찰하고 그것을 밖으로 표현하는 그림입니다...</a></li>
					<!-- Gauge -->
					<li class="gauge_box w_auto mt5">
						<dl>
							<dd class="gauge_outline w170">
								<label class="gauge" style="width: 50%;"></label>
							</dd>
							<dd class="process">(6/10)</dd>
						</dl></li>
					<!-- Gauge //-->
				</ul>
				<ul class="category_box intro_margin">
					<li class="photo"><a href=""> <img width="218"
							height="148" src="sera/images/course_01.jpg"> </a></li>
					<li class="subject"><a href="">어느 젊은 마법사의 코스</a></li>
					<li class="maker"><a href="">SERA캠퍼스</a></li>
					<li class="info">
						<dl>
							<dd class="menteeNo">34명</dd>
							<dd class="makeDate">2012.02.07</dd>
							<dd class="category"></dd>
						</dl></li>
					<li class="detail"><a href="/Course/Detail/212">안녕하세요
							선린인터넷고등학교 여러분! 세라캠퍼스에 오신걸 환영합니다. ^^* 꿈그리기란, 어렵고 전문적인 것이 아니라
							'나'자신을 관찰하고 그것을 밖으로 표현하는 그림입니다...</a></li>
					<!-- Gauge -->
					<li class="gauge_box w_auto mt5">
						<dl>
							<dd class="gauge_outline w170">
								<label class="gauge" style="width: 50%;"></label>
							</dd>
							<dd class="process">(6/10)</dd>
						</dl></li>
					<!-- Gauge //-->
				</ul>
			</div>
			<!-- 리스트 L //-->
			<!-- 리스트 R -->
			<div class="intro_category fr">
				<h2 class="mb10">
					<span class="ct_tit_02"> </span>
				</h2>
				<ul class="category_box intro_margin">
					<li class="photo"><label style="position: absolute;"
						class="ribbon_like"></label> <a href=""> <img width="218"
							height="148" src="sera/images/course_01.jpg"> </a></li>
					<li class="subject"><a href="">어느 젊은 마법사의 코스</a></li>
					<li class="maker"><a href="">SERA캠퍼스</a></li>
					<li class="info">
						<dl>
							<dd class="menteeNo">34명</dd>
							<dd class="makeDate">2012.02.07</dd>
							<dd class="category"></dd>
						</dl></li>
					<li class="detail"><a href="/Course/Detail/212">안녕하세요
							선린인터넷고등학교 여러분! 세라캠퍼스에 오신걸 환영합니다. ^^* 꿈그리기란, 어렵고 전문적인 것이 아니라
							'나'자신을 관찰하고 그것을 밖으로 표현하는 그림입니다...</a></li>
					<!-- Gauge -->
					<li class="gauge_box w_auto mt5">
						<dl>
							<dd class="gauge_outline w170">
								<label class="gauge" style="width: 50%;"></label>
							</dd>
							<dd class="process">(6/10)</dd>
						</dl></li>
					<!-- Gauge //-->
				</ul>
				<ul class="category_box intro_margin">
					<li class="photo"><label style="position: absolute;"
						class="ribbon_like"></label> <a href=""> <img width="218"
							height="148" src="sera/images/course_01.jpg"> </a></li>
					<li class="subject"><a href="">어느 젊은 마법사의 코스</a></li>
					<li class="maker"><a href="">SERA캠퍼스</a></li>
					<li class="info">
						<dl>
							<dd class="menteeNo">34명</dd>
							<dd class="makeDate">2012.02.07</dd>
							<dd class="category"></dd>
						</dl></li>
					<li class="detail"><a href="/Course/Detail/212">안녕하세요
							선린인터넷고등학교 여러분! 세라캠퍼스에 오신걸 환영합니다. ^^* 꿈그리기란, 어렵고 전문적인 것이 아니라
							'나'자신을 관찰하고 그것을 밖으로 표현하는 그림입니다...</a></li>
					<!-- Gauge -->
					<li class="gauge_box w_auto mt5">
						<dl>
							<dd class="gauge_outline w170">
								<label class="gauge" style="width: 50%;"></label>
							</dd>
							<dd class="process">(6/10)</dd>
						</dl></li>
					<!-- Gauge //-->
				</ul>
				<ul class="category_box intro_margin">
					<li class="photo"><a href=""> <img width="218"
							height="148" src="sera/images/course_01.jpg"> </a></li>
					<li class="subject"><a href="">어느 젊은 마법사의 코스</a></li>
					<li class="maker"><a href="">SERA캠퍼스</a></li>
					<li class="info">
						<dl>
							<dd class="menteeNo">34명</dd>
							<dd class="makeDate">2012.02.07</dd>
							<dd class="category"></dd>
						</dl></li>
					<li class="detail"><a href="/Course/Detail/212">안녕하세요
							선린인터넷고등학교 여러분! 세라캠퍼스에 오신걸 환영합니다. ^^* 꿈그리기란, 어렵고 전문적인 것이 아니라
							'나'자신을 관찰하고 그것을 밖으로 표현하는 그림입니다...</a></li>
					<!-- Gauge -->
					<li class="gauge_box w_auto mt5">
						<dl>
							<dd class="gauge_outline w170">
								<label class="gauge" style="width: 50%;"></label>
							</dd>
							<dd class="process">(6/10)</dd>
						</dl></li>
					<!-- Gauge //-->
				</ul>
				<ul class="category_box intro_margin">
					<li class="photo"><a href=""> <img width="218"
							height="148" src="sera/images/course_01.jpg"> </a></li>
					<li class="subject"><a href="">어느 젊은 마법사의 코스</a></li>
					<li class="maker"><a href="">SERA캠퍼스</a></li>
					<li class="info">
						<dl>
							<dd class="menteeNo">34명</dd>
							<dd class="makeDate">2012.02.07</dd>
							<dd class="category"></dd>
						</dl></li>
					<li class="detail"><a href="/Course/Detail/212">안녕하세요
							선린인터넷고등학교 여러분! 세라캠퍼스에 오신걸 환영합니다. ^^* 꿈그리기란, 어렵고 전문적인 것이 아니라
							'나'자신을 관찰하고 그것을 밖으로 표현하는 그림입니다...</a></li>
					<!-- Gauge -->
					<li class="gauge_box w_auto mt5">
						<dl>
							<dd class="gauge_outline w170">
								<label class="gauge" style="width: 50%;"></label>
							</dd>
							<dd class="process">(6/10)</dd>
						</dl></li>
					<!-- Gauge //-->
				</ul>
				<ul class="category_box intro_margin">
					<li class="photo"><a href=""> <img width="218"
							height="148" src="sera/images/course_01.jpg"> </a></li>
					<li class="subject"><a href="">어느 젊은 마법사의 코스</a></li>
					<li class="maker"><a href="">SERA캠퍼스</a></li>
					<li class="info">
						<dl>
							<dd class="menteeNo">34명</dd>
							<dd class="makeDate">2012.02.07</dd>
							<dd class="category"></dd>
						</dl></li>
					<li class="detail"><a href="/Course/Detail/212">안녕하세요
							선린인터넷고등학교 여러분! 세라캠퍼스에 오신걸 환영합니다. ^^* 꿈그리기란, 어렵고 전문적인 것이 아니라
							'나'자신을 관찰하고 그것을 밖으로 표현하는 그림입니다...</a></li>
					<!-- Gauge -->
					<li class="gauge_box w_auto mt5">
						<dl>
							<dd class="gauge_outline w170">
								<label class="gauge" style="width: 50%;"></label>
							</dd>
							<dd class="process">(6/10)</dd>
						</dl></li>
					<!-- Gauge //-->
				</ul>
				<ul class="category_box intro_margin">
					<li class="photo"><a href=""> <img width="218"
							height="148" src="sera/images/course_01.jpg"> </a></li>
					<li class="subject"><a href="">어느 젊은 마법사의 코스</a></li>
					<li class="maker"><a href="">SERA캠퍼스</a></li>
					<li class="info">
						<dl>
							<dd class="menteeNo">34명</dd>
							<dd class="makeDate">2012.02.07</dd>
							<dd class="category"></dd>
						</dl></li>
					<li class="detail"><a href="/Course/Detail/212">안녕하세요
							선린인터넷고등학교 여러분! 세라캠퍼스에 오신걸 환영합니다. ^^* 꿈그리기란, 어렵고 전문적인 것이 아니라
							'나'자신을 관찰하고 그것을 밖으로 표현하는 그림입니다...</a></li>
					<!-- Gauge -->
					<li class="gauge_box w_auto mt5">
						<dl>
							<dd class="gauge_outline w170">
								<label class="gauge" style="width: 50%;"></label>
							</dd>
							<dd class="process">(6/10)</dd>
						</dl></li>
					<!-- Gauge //-->
				</ul>
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
		smartPop.showInfo(smartPop.ERROR, smartMessage.get('illegalAcountError'));
	<%} else if (type.equals("logout")) {%>
		smartPop.showInfo(smartPop.INFO, smartMessage.get('logoutSucceed'));
	<%} else if (type.equals("expiredSession")) {%>
		if(top.document.location.href === "logins.sw")
			smartPop.showInfo(smartPop.WARN, smartMessage.get('sessionTimeouted'));
		else
			top.document.location.href = "logins.sw";
	<%}%>
	$(document).keypress(function(e) {
		if ((e.which && e.which == 13) || (e.keyCode && e.keyCode == 13)) {
			$('input[type="submit"]').click();
			return false;
		} else {
			return true;
		}
	});
});
</script>
