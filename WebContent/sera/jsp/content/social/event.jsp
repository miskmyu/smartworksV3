<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.model.sera.Course"%>
<%@page import="net.smartworks.model.sera.info.CourseInfo"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 그리고 현재사용자 정보도 가져온다.	
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();

	CourseInfo[] myCourses = smartWorks.getCoursesById(cUser.getId(), Course.MY_ALL_COURSES, new LocalDate(), -1);

%>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!-- Header Title -->
<div class="header_tit">
	<div class="tit_event">
		내 코스에서 개최하는 오프라인 모임이나, 세라 친구들과 이벤트를 만들어 보세요.<br /> 삶의 소중한 시간으로 채워지는
		만남과 열정이 만들어집니다.
	</div>
</div>
<!-- Header Title //-->
<!-- Input Form -->
<div class="form_layout js_new_event_page">
<!-- 	<table border="0" cellspacing="0" cellpadding="0">
 -->		<form name="frmNewEvent" class="js_validation_required js_click_start_form">
		<!-- 새로운 이벤트를 등록하기 위한 입력화면을 스마트폼을 이용하여 자동으로 그린다.. -->
		<!-- js_new_event_fields :  js/sw/sw-formFields.js 에서 loadNewEventFields()가 찾아서 이벤트입력화면을 이곳에 그려준다.. -->
		<div class="js_new_event_fields" eventNameTitle="<fmt:message key='common.upload.event.name'/>" placeHolderTitle="<fmt:message key='common.upload.message.event'/>"
			startDateTitle="<fmt:message key='common.upload.event.start_date'/>" endDateTitle="<fmt:message key='common.upload.event.end_date'/>"  alarmPolicyTitle="<fmt:message key='common.upload.button.set_alarm'/>"
			placeTitle="<fmt:message key='common.upload.event.place'/>" relatedUsersTitle="<fmt:message key='common.upload.event.related_users'/>" 
			contentTitle="<fmt:message key='common.upload.event.content' />">
		</div>
	</form>
	<div class="js_hidden_form_content" style="display:none">
	</div>
</div>
<!-- Input Form //-->
<div class="js_new_event_page"> 
 </div>
<!-- Panel Section -->
<div class="panel_section">
	<div>
		<ul class="panel_area">
			<!-- photo-->
			<li class="">
				<div class="photo_bg">
					<img src="../images/photo_default72.jpg" />
					<div class="rgt_name">사랑사랑</div>
				</div>
				<div class="grade">
					<div class="icon_mentor current"></div>
					<div class="icon_star"></div>
					<div class="icon_heart"></div>
				</div>
			</li>
			<!-- photo//-->
			<!-- comment -->
			<li class="fr">
				<div class="point"></div>
				<div class="panel_block fr">
					<dl class="content">
						<dt>
							<div class="event_info">
								<div class="event_name">이벤트명</div>
								<div class="event_more_info">
									이벤트 기간 : 2012년 2월 10일 ~ 2012년 2월 14일<br /> 이벤트 장소: 없음
								</div>
							</div>
							<span class="icon_delete fr"><a href="">삭제</a> </span>
						</dt>
						<dd>
							<div class="text">시간이 지날수록 제 얼굴이 찐빵으로 그려지네요~시간이 지날수록 제 얼굴이
								찐빵으로 그려지네요~시간이 지날수록 제 얼굴이 찐빵으로 그려지네요~시간이 지날수록 제 얼굴이 찐빵으로
								그려지네요~시간이 지날수록 제 얼굴이 찐빵으로 그려지네요~시간이 지날수록 제 얼굴이 찐빵으로 그려지네요~시간이
								지날수록 제 얼굴이 찐빵으로 그려지네요~시간이 지날수록 제 얼굴이 찐빵으로 그려지네요~시간이 지날수록 제 얼굴이
								찐빵으로 그려지네요~시간이 지날수록 제 얼굴이 찐빵으로 그려지네요~시간이 지날수록 제 얼굴이 찐빵으로 그려지네요~</div>
							<!-- Thum Image-->
							<div class="thum_image">
								<img src="../images/thum_image.jpg" />
							</div>
							<!-- Thum Image//-->
						</dd>
						<!-- Util -->
						<dd class="util">
							<span><a href="">답변달기</a> | </span> <span><a href="">댓글달기</a>
								| </span> <span><a href="">공감하기</a> | </span> <span><a href="">더보기</a>
								| </span> <span class="date">2012 년 03 월 09 일</span>
						</dd>
						<!-- Util //-->
					</dl>
					<div class="stat_notice" style="display: none">7개의 댓글이 모두보기</div>
					<!-- Reply-->
					<div class="reply_section">
						<div class="photo">
							<img src="../images/photo_mid48.jpg" />
						</div>
						<div class="reply_text">
							<span class="name">닉네임은 일곱자 : </span> 점점 시간이 지날수록 얼굴이 찐빵으로
							그려지네요~~점점 시간이 지날수록 얼굴이 찐빵으로 그려지네요~~점점 시간이 지날수록 얼굴이 찐빵으로 그려지네요~~점점
							시간이 지날수록 얼굴이 찐빵으로 그려지네요~~점점 시간이 지날수록 얼굴이 찐빵으로 그려지네요~~점점 시간이 지날수록
							얼굴이 찐빵으로 그려지네요~~
							<div class="icon_date">3월 5일</div>
						</div>
					</div>
					<!-- Reply//-->
					<!-- Reply-->
					<div class="reply_section">
						<div class="photo">
							<img src="../images/photo_mid48.jpg" />
						</div>
						<div class="reply_text">
							<span class="name">닉네임은 일곱자 : </span> 점점 시간이 지날수록 얼굴이 찐빵으로
							그려지네요~~점점 시간이 지날수록 얼굴이 찐빵으로 그려지네요~~점점 시간이 지날수록 얼굴이 찐빵으로 그려지네요~~점점
							시간이 지날수록 얼굴이 찐빵으로 그려지네요~~점점 시간이 지날수록 얼굴이 찐빵으로 그려지네요~~점점 시간이 지날수록
							얼굴이 찐빵으로 그려지네요~~
							<div class="icon_date">3월 5일</div>
						</div>
					</div>
					<!-- Reply//-->
					<!-- Reply-->
					<div class="reply_section end">
						<div class="photo">
							<img src="../images/photo_mid48.jpg" />
							<div class="name mt2">닉네임은</div>
						</div>
						<div class="reply_text">
							<textarea name="" cols="" rows="" class="fr" style="width: 100%"
								placeholder="댓글을 남겨주세요!"></textarea>
						</div>
						<div class="btn_default_l cb fr mt5">
							<div class="btn_default_r">등록</div>
						</div>
					</div>
					<!-- Reply//-->
				</div>
			</li>
			<!-- comment //-->
		</ul>
	</div>

	<div>
		<ul class="panel_area">
			<!-- photo-->
			<li class="">
				<div class="photo_bg">
					<img src="../images/photo_default72.jpg" />
					<div class="rgt_name">사랑사랑</div>
				</div>
				<div class="grade">
					<div class="icon_mentor current"></div>
					<div class="icon_star"></div>
					<div class="icon_heart"></div>
				</div>
			</li>
			<!-- photo//-->
			<!-- comment -->
			<li class="fr">
				<div class="point"></div>
				<div class="panel_block fr">
					<dl class="content">
						<dt>
							<div class="event_info">
								<div class="event_name">이벤트명</div>
								<div class="event_more_info">
									이벤트 기간 : 2012년 2월 10일 ~ 2012년 2월 14일<br /> 이벤트 장소: 없음
								</div>
							</div>
							<span class="icon_delete fr"><a href="">삭제</a> </span>
						</dt>
						<dd>
							<div class="text">시간이 지날수록 제 얼굴이 찐빵으로 그려지네요~시간이 지날수록 제 얼굴이
								찐빵으로 그려지네요~시간이 지날수록 제 얼굴이 찐빵으로 그려지네요~시</div>
							<!-- Thum Image-->
							<div class="thum_image">
								<img src="../images/thum_image.jpg" />
							</div>
							<!-- Thum Image//-->
						</dd>
						<!-- Util -->
						<dd class="util">
							<span><a href="">답변달기</a> | </span> <span><a href="">댓글달기</a>
								| </span> <span><a href="">공감하기</a> | </span> <span><a href="">더보기</a>
								| </span> <span class="date">2012 년 03 월 09 일</span>
						</dd>
						<!-- Util //-->
					</dl>
					<div class="stat_notice" style="display: none">7개의 댓글이 모두보기</div>
					<!-- Reply-->
					<div class="reply_section end">
						<div class="photo">
							<img src="../images/photo_mid48.jpg" />
							<div class="name mt2">닉네임은</div>
						</div>
						<div class="reply_text">
							<textarea name="" cols="" rows="" class="fr" style="width: 100%"
								placeholder="답변을 보내세요!"></textarea>
						</div>
						<!-- 우측 버튼 영역 -->
						<div class="attach_file">
							<ul>
								<li class="t_s11"><span class="t_red">0</span> /1000kbyte</li>
								<li class="icon_memo bb ml10"><a href=""> </a></li>
								<li class="icon_video bb"><a href=""> </a></li>
								<li class="icon_photo bb"><a href=""> </a></li>
								<li class="icon_link bb"><a href=""> </a></li>
								<!-- Btn 등록-->
								<li class="btn_default_l ml10">
									<div class="btn_default_r">등록</div>
								</li>
								<!-- Btn 등록//-->
							</ul>
						</div>
						<!-- 우측 버튼 영역 //-->
					</div>
					<!-- Reply//-->
				</div>
			</li>
			<!-- comment //-->
		</ul>
	</div>
</div>
<!-- Panel Section //-->
<script>
	loadNewEventFields();
</script>