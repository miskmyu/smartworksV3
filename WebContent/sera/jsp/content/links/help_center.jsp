<%@page import="net.smartworks.model.sera.Course"%>
<%@page import="net.smartworks.model.sera.info.CourseInfo"%>
<%@page import="net.smartworks.model.sera.CourseList"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@ page contentType="text/html; charset=utf-8"%>

<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 그리고 현재사용자 정보도 가져온다.	
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
%>

<div id="course_list_section">
	<!-- SNB Left -->
	<div class="snb2">
		<ul class="snb_menu">
			<li><img height="112" width="149" src="sera/images/function_title_helpcenter.gif"></li>
			<li class="selected"><a href="helpCenter.sw">사용 설명서</a></li>
			<li class=""><a href="helpFAQ.sw">자주 묻는 질문</a></li>
			<!-- <li class=""><a href="helpQNA.sw">Q&A </a></li> -->
		</ul>
	</div>
	<!-- SNB Left//-->
	<!-- Content -->
	<div id="content_list_section">
		<!-- Arrow -->
		<div style="width: auto; padding: 0 14px 10px 0; float: right;">
			<ul>
				<li class="fl"><label class="mr10"> <img id="btnPrev" width="9" height="9" style="cursor: pointer;" src="sera/images/arrow_pre.gif"> </label></li>
				<li class="fl"><strong id="spanNav">2</strong> / <span id="spanNavFull">3</span></li>
				<li class="fl"><label class="ml10"> <img id="btnNext" width="9" height="9" style="cursor: pointer;" src="sera/images/arrow_next.gif"> </label></li>
			</ul>
		</div>
		<!-- Arrow //-->
		<!-- Tutorial -->
		<div id="menu1" class="content_box" style="display: block;">
			<div class="tutorial_menu">
				<ul>
					<li>
						<span id="btnHelp1" style="cursor: pointer;"> 
							<span id="CLImageRollover0container" style="cursor: pointer;"> 
								<img id="CLImageRollover0" class="cmdRollover org" width="90" height="93" title="코스개설하기" alt="코스개설하기" src="sera/images/helpcenter/helpcenter_guidebtn1_on.gif" style="">
								<img id="CLImageRollover0_on" style="display: none;" src="sera/images/helpcenter/helpcenter_guidebtn1_on.gif"> 
							</span> 
						</span>
					</li>
					<li>
						<span id="btnHelp2" style="cursor: pointer;"> 
							<span id="CLImageRollover1container" style="cursor: pointer;"> 
								<img id="CLImageRollover1" class="cmdRollover org" width="90" height="93" title="코스참여하기" alt="코스참여하기" src="sera/images/helpcenter/helpcenter_guidebtn2_off.gif" style="">
								<img id="CLImageRollover1_on" style="display: none;" src="sera/images/helpcenter/helpcenter_guidebtn2_on.gif"> 
							</span> 
						</span>
					</li>
					<li>
						<span id="btnHelp3" style="cursor: pointer;"> 
							<span id="CLImageRollover2container" style="cursor: pointer;"> 
								<img id="CLImageRollover2" class="cmdRollover org" width="90" height="93" title="친구설정하기" alt="친구설정하기" src="sera/images/helpcenter/helpcenter_guidebtn3_off.gif" style="">
								<img id="CLImageRollover2_on" style="display: none;" src="sera/images/helpcenter/helpcenter_guidebtn3_on.gif"> 
							</span> 
						</span>
					</li>
					<li>
						<span id="btnHelp4" style="cursor: pointer;"> 
							<span id="CLImageRollover3container" style="cursor: pointer;"> 
								<img id="CLImageRollover3" class="cmdRollover org" width="90" height="93" title="mypage" alt="myPage" src="sera/images/helpcenter/helpcenter_guidebtn4_off.gif" style="">
								<img id="CLImageRollover3_on" style="display: none;" src="sera/images/helpcenter/helpcenter_guidebtn4_on.gif"> 
							</span> 
						</span>
					</li>
				</ul>
			</div>
			<div id="divHelpPage" class="tutorial_visual" style="width: 587px; height: 690px;">
				<!-- 튜토리얼 이미지 삽입 공간 W:587px H:690px -->
				<div>
					<object class="thum_image_size">
					<param value="http://www.youtube.com/watch?v=LoWlnAzML88" name="movie">
					<param value="always" name="allowScriptAccess">
					<param value="true" name="allowFullScreen">
					<embed allowfullscreen="true" allowscriptaccess="always" type="application/x-shockwave-flash" src="http://www.youtube.com/watch?v=LoWlnAzML88">
					</object>
				</div>

			</div>
		</div>
		<!-- Tutorial //-->
	</div>
	<!-- Content //-->
</div>

<script type="text/javascript">
	var navIdx = 1;
	var navCategory = 1;
	var navFullIdx = 6;
	$("#btnHelp1").click( function() {
		navIdx = 1;
		navCategory = 1;
		navFullIdx = 6;
		$("#btnHelp1").find(".org").attr("src", "sera/images/helpcenter/helpcenter_guidebtn1_on.gif");
		$("#btnHelp2").find(".org").attr("src", "sera/images/helpcenter/helpcenter_guidebtn2_off.gif");
		$("#btnHelp3").find(".org").attr("src", "sera/images/helpcenter/helpcenter_guidebtn3_off.gif");
		$("#btnHelp4").find(".org").attr("src", "sera/images/helpcenter/helpcenter_guidebtn4_off.gif");
		$(".helpImg").hide();
		$("#spanNav").html(navIdx);
		$("#spanNavFull").html(navFullIdx);
		$("#img" + navCategory.toString() + "_" + navIdx.toString()).show();
	});
	$("#btnHelp2").click( function() {
		navIdx = 1;
		navCategory = 2;
		navFullIdx = 3;
		$(".helpImg").hide();
		$("#btnHelp1").find(".org").attr("src", "sera/images/helpcenter/helpcenter_guidebtn1_off.gif");
		$("#btnHelp2").find(".org").attr("src", "sera/images/helpcenter/helpcenter_guidebtn2_on.gif");
		$("#btnHelp3").find(".org").attr("src", "sera/images/helpcenter/helpcenter_guidebtn3_off.gif");
		$("#btnHelp4").find(".org").attr("src", "sera/images/helpcenter/helpcenter_guidebtn4_off.gif");
		$("#spanNav").html(navIdx);
		$("#spanNavFull").html(navFullIdx);
		$("#img" + navCategory.toString() + "_" + navIdx.toString()).show();
	});
	$("#btnHelp3").click( function() {
		navIdx = 1;
		navCategory = 3;
		navFullIdx = 3;
		$(".helpImg").hide();
		$("#btnHelp1").find(".org").attr("src", "sera/images/helpcenter/helpcenter_guidebtn1_off.gif");
		$("#btnHelp2").find(".org").attr("src", "sera/images/helpcenter/helpcenter_guidebtn2_off.gif");
		$("#btnHelp3").find(".org").attr("src", "sera/images/helpcenter/helpcenter_guidebtn3_on.gif");
		$("#btnHelp4").find(".org").attr("src", "sera/images/helpcenter/helpcenter_guidebtn4_off.gif");
		$("#spanNav").html(navIdx);
		$("#spanNavFull").html(navFullIdx);
		$("#img" + navCategory.toString() + "_" + navIdx.toString()).show();
	});
	$("#btnHelp4").click( function() {
		navIdx = 1;
		navCategory = 4;
		navFullIdx = 2;
		$(".helpImg").hide();
		$("#btnHelp1").find(".org").attr("src", "sera/images/helpcenter/helpcenter_guidebtn1_off.gif");
		$("#btnHelp2").find(".org").attr("src", "sera/images/helpcenter/helpcenter_guidebtn2_off.gif");
		$("#btnHelp3").find(".org").attr("src", "sera/images/helpcenter/helpcenter_guidebtn3_off.gif");
		$("#btnHelp4").find(".org").attr("src", "sera/images/helpcenter/helpcenter_guidebtn4_on.gif");
		$("#spanNav").html(navIdx);
		$("#spanNavFull").html(navFullIdx);
		$("#img" + navCategory.toString() + "_" + navIdx.toString()).show();
	});
	$("#btnPrev").click( function() {
		if (navIdx > 1) {
			navIdx--;
			$(".helpImg").hide();
			$("#img" + navCategory.toString() + "_" + navIdx.toString()).show();
			$("#spanNav").html(navIdx);
			$("#spanNavFull").html(navFullIdx);
		} else {
			alert("처음입니다.");
		}
	});
	$("#btnNext").click(function() {
		if (navIdx < navFullIdx) {
			navIdx++;
			$(".helpImg").hide();
			$("#img" + navCategory.toString() + "_" + navIdx.toString()).show();
			$("#spanNav").html(navIdx);
			$("#spanNavFull").html(navFullIdx);
		} else {
			alert("마지막입니다.");
		}
	});

</script>
					