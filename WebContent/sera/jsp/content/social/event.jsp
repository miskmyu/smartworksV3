<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@ page contentType="text/html; charset=utf-8"%>

<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 그리고 현재사용자 정보도 가져온다.	
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();
	String cid = request.getParameter("cid");
	if (SmartUtil.isBlankObject(cid))
		cid = ISmartWorks.CONTEXT_PREFIX_HOME + cUser.getId();
	String wid = request.getParameter("wid");
	if (SmartUtil.isBlankObject(wid))
		wid = cUser.getId();
%>
<!-- Header Title -->
<div class="header_tit">
	<div class="tit_event">
		내 코스에서 개최하는 오프라인 모임이나, 세라 친구들과 이벤트를 만들어 보세요.<br /> 삶의 소중한 시간으로 채워지는
		만남과 열정이 만들어집니다.
	</div>
</div>
<!-- Header Title //-->
<!-- Input Form -->
<div class="form_layout">
	<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td colspan="3"><div class="form_label">이벤트명</div>
				<div class="form_value w580">
					<input type="text" class="fieldline" placeholder="이벤트 이름을 입력하세요!">
				</div>
			</td>
		</tr>
		<tr>
			<td colspan="3">
				<div class="form_label">코 스 명</div>
				<div class="form_value w580">
					<select>
						<option>선택하기</option>
						<option>해당없음</option>
						<option>전체코스</option>
						<option>코스명 1</option>
						<option>코스명 2</option>
					</select>
				</div>
			</td>
		</tr>
		<tr>
			<td><div class="form_label">시작일자</div>
				<div class="form_value">
					<div class="icon_fb_space form_datetime_input">
						<input type="text" class="fieldline"> <a class="" href="">
							<span class="icon_fb_time"></span> </a>
					</div>
				</div></td>
			<td><div class="form_label" style="width: 65px">종료일자</div>
				<div class="form_value">
					<div class="icon_fb_space form_datetime_input">
						<input type="text" class="fieldline"> <a class="" href="">
							<span class="icon_fb_time"></span> </a>
					</div>
				</div></td>
			<td><div class="form_label">미리알림</div>
				<div class="form_value">
					<select class="form_select_box" name="selEventAlarmPolicy">
						<option value="설정안함">설정안함</option>
						<option value="정시">정시</option>
						<option value="5분전">5분전</option>
						<option value="10분전">10분전</option>
						<option value="15분전">15분전</option>
						<option value="30분전">30분전</option>
						<option value="한시간전">한시간전</option>
						<option value="하루전">하루전</option>
					</select>
				</div></td>
		</tr>
		<tr>
			<td colspan="3">
				<div class="form_label">장 소</div>
				<div class="form_value w580">
					<input type="text" class="fieldline">
				</div>
			</td>
		</tr>
		<tr>
			<td colspan="3">
				<div class="form_label">참 여 자</div>
				<div class="form_value w580">
					<div class="icon_fb_space">
						<div class="fieldline community_names js_community_names">
							<input class="m0 js_auto_complete" type="text"
								href="community_name.sw" style="width: 100px;">
						</div>
						<div class="js_community_list srch_list_nowid"
							style="display: none;"></div>
						<span class="js_community_popup"></span> <a
							class="js_userpicker_button" href=""> <span
							class="icon_fb_users"></span> </a>
					</div>
				</div>
			</td>
		</tr>
		<tr>
			<td colspan="3">
				<div class="form_label">이벤트 내용</div>
				<div class="form_value w580">
					<textarea class="fieldline" name="" rows="3" value=""></textarea>
				</div>
			</td>
		</tr>
		<tr>
			<td colspan="3">
				<div class="cb" style="margin: -15px 0 0 90px;">
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

							<!-- Btn 취소-->
							<li class="btn_default_l ml5">
								<div class="btn_default_r">취소</div>
							</li>
							<!-- Btn 취소//-->

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
