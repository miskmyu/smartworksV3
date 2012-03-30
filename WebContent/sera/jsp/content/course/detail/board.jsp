<%@page import="net.smartworks.model.instance.info.InstanceInfo"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.model.sera.Mentor"%>
<%@page import="net.smartworks.model.sera.Course"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@ page contentType="text/html; charset=utf-8"%>

<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 그리고 현재사용자 정보도 가져온다.	
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();

	String courseId = request.getParameter("courseId");
	Course course = (Course) session.getAttribute("course");
	if (SmartUtil.isBlankObject(course) || !course.getId().equals(courseId))
		course = smartWorks.getCourseById(courseId);
	InstanceInfo[] notices = smartWorks.getCourseNotices(courseId, new LocalDate(), 10);
%>
<!-- Header Title -->
<div class="header_tit">
	<div class="tit_dep2 m0">
		<h2>코스알림</h2>
		<div>* 본 코스에 대한 공지사항 및 멘티들에게 전하는 소식을 입력할 수 있습니다.</div>
	</div>
</div>
<!-- Header Title //-->
<!-- Input Form -->
<div class="form_layout">
	<table>
		<tr>
			<td colspan="3">
				<div class="form_label">코스명</div>
				<div class="form_value w580">코스명1</div>
			</td>
		</tr>
		<tr>
			<td colspan="3">
				<div class="form_label">알림제목</div>
				<div class="form_value w580">
					<input type="text" class="fieldline">
				</div>
			</td>
		</tr>
		<tr>
			<td colspan="3">
				<div class="form_value w100">
					<textarea class="fieldline" name="" rows="3" value="">코스 알림내용을 적어주세요</textarea>
				</div>
			</td>
		</tr>
		<tr>
			<td colspan="3">
				<div class="cb" style="margin: -15px 0 0 0">
					<!-- 좌측 영역 -->
					<div class="option">
						<!-- 버튼 -->
						<div class="btn_wstyle_l">
							<div class="btn_wstyle_r">텍스트</div>
						</div>
						<!-- 버튼 //-->
						<!-- select -->
						<div class="txt ml10">
							<a href=""> 전체공개<span class="icon_bul_select ml5"></span> </a> |
						</div>
						<!-- select //-->
						<!-- 태그넣기 -->
						<div class="txt">
							<a href=""> 태그넣기<span class="icon_bul_select ml5"></span> </a>
						</div>
						<!-- 태그넣기//-->
					</div>
					<!-- 좌측 영역//-->
					<!-- 우측 버튼 영역 -->
					<div class="attach_file">
						<ul>
							<li class="t_s11"><span class="t_red">0</span> /1000kbyte</li>
							<li class="icon_memo ml10"><a href=""> </a></li>
							<li class="icon_video"><a href=""> </a></li>
							<li class="icon_photo"><a href=""> </a></li>
							<li class="icon_link"><a href=""> </a></li>
							<!-- Btn 등록-->
							<li class="btn_default_l ml10">
								<div class="btn_default_r">등록</div>
							</li>
							<!-- Btn 등록//-->
						</ul>
					</div>
					<!-- 우측 버튼 영역 //-->
				</div>
			</td>
		</tr>
	</table>
</div>
<!-- Input Form //-->

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
								<div class="event_name">코스명1</div>
								<div class="event_more_info">
									미션1<br /> 미션제목
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
