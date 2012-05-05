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
	<script type="text/javascript" src='sera/js/window_popup.js'></script>
	<title>세라캠퍼스에 오신걸 환영합니다.</title>
</head>

<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 현재사용자 정보도 가져온다
	ISmartWorks smartWorks = (ISmartWorks)SmartUtil.getBean("smartWorks", request);
	CourseInfo[] favoriteCourses = smartWorks.getFavoriteCourses(6);
	CourseInfo[] recommendedCourses = smartWorks.getRecommendedCourses(6);
	session.setAttribute("currentMenu", "none");
	session.setAttribute("noUser", true);
%>

<script type="text/javascript">
	if(isEmpty(parent.location.pathname.match('logins.sw'))){
	    parent.location.href = "logins.sw";
	}
	var currentUser = {
		locale : "<%=java.util.Locale.getDefault().getLanguage()%>",
		timeZone : "<%=TimeZone.getDefault().getID()%>",
		timeOffset : "<%=TimeZone.getDefault().getRawOffset()%>"
	};
</script>

<body onload="javascript:startPopup()">
	<div id="wrap" class="main_bg">
		<div id="sera_header">
			<jsp:include page="/sera/jsp/header.jsp" />
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
						<a id="eventBtn1" href=""> 
							<img id="eventBtnOn1" width="9" height="9" alt="" src="sera/images/main_indicator_on.png" style="display: none;">
							<img id="eventBtnOff1" width="9" height="9" alt="" style="" src="sera/images/main_indicator_off.png"> 
						</a> 
						<a id="eventBtn2" href="http://club.seracampus.com/?p=1017"> 
							<img id="eventBtnOn2" width="9" height="9" alt="" src="sera/images/main_indicator_on.png" style=""> 
							<img id="eventBtnOff2" width="9" height="9" alt="" style="display: none;" src="sera/images/main_indicator_off.png">
						</a> 
						<a id="eventBtn3" href="http://www.seracampus.com/courseHome.sw?courseId=group_242"> 
							<img id="eventBtnOn3" width="9" height="9" alt="" src="sera/images/main_indicator_on.png" style="display: none;">
							<img id="eventBtnOff3" width="9" height="9" alt="" style="" src="sera/images/main_indicator_off.png"> 
						</a> 
						<a id="eventBtn4" href="http://www.youtube.com/watch?v=KneS3Mstmes" target="_blank"> 
							<img id="eventBtnOn4" width="9" height="9" alt="" src="sera/images/main_indicator_on.png" style="display: none;">
							<img id="eventBtnOff4" width="9" height="9" alt="" style="" src="sera/images/main_indicator_off.png"> 
						</a> 
					</label> 
					<a href=""> 
						<img id="eventBanner1" width="405" height="435" alt="" style="position: absolute; z-index: 10;" src="sera/images/N_visual_01.png"> 
					</a> 
					<a href="http://club.seracampus.com/?p=1017"> 
						<img id="eventBanner2" width="405" height="435" alt="" style="position: absolute; z-index: 9;" src="sera/images/N_visual_02.png"> 
					</a> 
					<a href="http://www.seracampus.com/Course/Detail/242"> 
						<img id="eventBanner3" width="405" height="435" alt="" style="position: absolute; z-index: 8;" src="sera/images/N_visual_03.png"> 
					</a>
					<a href="http://www.youtube.com/watch?v=KneS3Mstmes" target="_blank"> 
						<img id="eventBanner4" width="405" height="435" alt="" style="position: absolute; z-index: 7;" src="sera/images/N_visual_04.png"> 
					</a>
				</li>
			</ul>
			<!-- Center Visual//-->
			<!-- Right -->
			<ul class="sera_news">
				<li class="lst_normal">
					<dl class="">
						<dt>
							<a href="seraNews.sw"><div class="icon_mn_news fl">SERA 소식</div></a>
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
							<a href="seraNews.sw"><div class="icon_as_srtrend fl">트렌드 세라</div></a>
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
						if (course.getId() == null)
							continue;
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
						if (course.getId() == null)
							continue;
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
			<jsp:include page="/sera/jsp/footer.jsp" />
		</div>
		<!-- Footer //-->
	</div>
	<%
		String type = (String)request.getAttribute("type");
		if(SmartUtil.isBlankObject(type)) type ="login";
	%>
	<script type="text/javascript">	
		<%
		if(type.equals("failedLogin")) {
		%>
			smartPop.showInfo(smartPop.ERROR, "아이디가 없거나 암호가 잘못되었습니다. 확인 후 다시 시도하시기 바랍니다!");
		<%
		} else if(type.equals("logout")) {
		%>
			smartPop.showInfo(smartPop.INFO, "정상적으로 계정 로그아웃이 처리되었습니다!");
		<%
		}
		%>

		function NavigateParent(url){
		    document.location.href = url;   
		};
	</script>
</body>
</html>
