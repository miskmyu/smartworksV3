<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@ page contentType="text/html; charset=utf-8"%>

<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 그리고 현재사용자 정보도 가져온다.	
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");

	User cUser = SmartUtil.getCurrentUser();


%>
<div class="my_comment_section">
	<div class="my_photo">
		<img src="<%=cUser.getMidPicture() %>" width="120px" height="120px" />
	</div>
	<!-- My Comment -->
	<div class="my_comment">
		<div class="header"><%=cUser.getNickName() %></div>
		<div class="comment_txt ">
			<textarea name="" cols="" rows="5" placeholder="전공 공부만큼 영어를 좋아했는데 입사하고 나니 영어에는 손을 뚝 끊어버리게 되네요~ 전공 공부만큼 영어를 좋아했는데 입사하고"></textarea>
		</div>

		<div class="cb mt6">
			<!-- 좌측 영역 -->
			<div class="option">
				<!-- 버튼 -->
				<div class="btn_wstyle_l">
					<div class="btn_wstyle_r">텍스트</div>
				</div>
				<!-- 버튼 //-->
				<!-- 전체공개 -->
				<div class="txt ml10">
					<a href=""> 전체공개<span class="icon_bul_select ml5"></span> </a>
					|
				</div>
				<!-- 전체공개 //-->

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
					<li class="icon_memo ml10"><a href=""> </a>
					</li>
					<li class="icon_video"><a href=""> </a>
					</li>
					<li class="icon_photo"><a href=""> </a>
					</li>
					<li class="icon_link"><a href=""> </a>
					</li>
					<!-- Btn 등록-->
					<li class="btn_default_l ml10">
						<div class="btn_default_r">등록</div></li>
					<!-- Btn 등록//-->
				</ul>
			</div>
			<!-- 우측 버튼 영역 //-->
		</div>

	</div>
	<!-- My Comment //-->
</div>
<!-- Comment Pannel-->
<div class="panel_section">
	<div class="header">
		<div class="icon_mytext">
			<a href="">내글보기</a>
		</div>
		<div>
			| <a href="" class="current">뉴스피드</a>
		</div>
	</div>
	<!-- Panel1 -->
	<div>
		<object width="560" height="315">
			<param name="movie" value="https://www.youtube.com/v/u1zgFlCw8Aw?fs=1"></param>
			<param name="allowFullScreen" value="true"></param>
			<param name="allowScriptAccess" value="always"></param>
			<embed src="https://www.youtube.com/v/u1zgFlCw8Aw?fs=1"
			  type="application/x-shockwave-flash"
			  allowfullscreen="true"
			  allowscriptaccess="always"
			  width="560" height="315">
			</embed>
		</object>	
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
				</div></li>
			<!-- photo//-->
			<!-- comment -->
			<li class="fr">
				<div class="point"></div>
				<div class="panel_block fr">
					<dl class="content">
						<dt class="name">
							코스명 <span class="t_redb">[미션1. 자화상 그리기]</span> <span
								class="icon_delete fr"><a href="">삭제</a>
							</span>
						</dt>
						<dd>
							<div class="text">시간이 지날수록 제 얼굴이 찐빵으로 그려지네요~시간이 지날수록 제
								얼굴이 찐빵으로 그려지네요~시간이 지날수록 제 얼굴이 찐빵으로 그려지네요~시간이 지날수록 제 얼굴이 찐빵으로
								그려지네요~시간이 지날수록 제 얼굴이 찐빵으로 그려지네요~시간이 지날수록 제 얼굴이 찐빵으로
								그려지네요~시간이 지날수록 제 얼굴이 찐빵으로 그려지네요~시간이 지날수록 제 얼굴이 찐빵으로
								그려지네요~시간이 지날수록 제 얼굴이 찐빵으로 그려지네요~시간이 지날수록 제 얼굴이 찐빵으로
								그려지네요~시간이 지날수록 제 얼굴이 찐빵으로 그려지네요~</div>
							<!-- Thum Image-->
							<div class="thum_image">
								<img src="../images/thum_image.jpg" />
							</div>
							<!-- Thum Image//-->
						</dd>
						<!-- Util -->
						<dd class="util">
							<span><a href="">댓글달기</a> | </span> <span><a href="">공감하기</a>
								| </span> <span class=""><a href="">더보기</a> | </span> <span
								class="date">2012 년 03 월 09 일</span>
						</dd>
						<!-- Util //-->
					</dl>
					<div class="stat_notice">7개의 댓글이 모두보기</div>
					<!-- Reply-->
					<div class="reply_section">
						<div class="photo">
							<img src="../images/photo_mid48.jpg" />
						</div>
						<div class="reply_text">
							<span class="name">닉네임은 일곱자 : </span> 점점 시간이 지날수록 얼굴이 찐빵으로
							그려지네요~~점점 시간이 지날수록 얼굴이 찐빵으로 그려지네요~~점점 시간이 지날수록 얼굴이 찐빵으로
							그려지네요~~점점 시간이 지날수록 얼굴이 찐빵으로 그려지네요~~점점 시간이 지날수록 얼굴이 찐빵으로
							그려지네요~~점점 시간이 지날수록 얼굴이 찐빵으로 그려지네요~~
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
							그려지네요~~점점 시간이 지날수록 얼굴이 찐빵으로 그려지네요~~점점 시간이 지날수록 얼굴이 찐빵으로
							그려지네요~~점점 시간이 지날수록 얼굴이 찐빵으로 그려지네요~~점점 시간이 지날수록 얼굴이 찐빵으로
							그려지네요~~점점 시간이 지날수록 얼굴이 찐빵으로 그려지네요~~
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
							<textarea name="" cols="" rows="" class="fr"
								style="width: 100%" placeholder="댓글을 남겨주세요!"></textarea>
						</div>
						<div class="btn_default_l cb fr mt5">
							<div class="btn_default_r">등록</div>
						</div>
					</div>
					<!-- Reply//-->
				</div></li>
			<!-- comment //-->
		</ul>
	</div>
	<!-- Panel1 //-->
	<!-- Panel2 -->
	<div>
		<ul class="panel_area">
			<!-- photo-->
			<li class="">
				<div class="photo_bg">
					<img src="../images/photo_default72_2.jpg" />
					<div class="rgt_name">사랑사랑</div>
				</div>
				<div class="grade">
					<div class="icon_mentor"></div>
					<div class="icon_star current"></div>
					<div class="icon_heart"></div>
				</div></li>
			<!-- photo//-->
			<!-- comment -->
			<li class="fr">
				<div class="point"></div>
				<div class="panel_block fr">
					<dl class="content">
						<dt class="icon_sm_notes">
							쪽지 <span class="icon_delete fr"><a href="">삭제</a>
							</span>
						</dt>
						<dd>
							<div class="notes">시간이 지날수록 제 얼굴이 찐빵으로 그려지네요~시간이 지날수록 제
								얼굴이 찐빵으로 그려지네요~시간이 지날수록 제 얼굴이 찐빵으로 그려지네요~시간이 지날수록 제 얼굴이 찐빵으로
								그려지네요~시간이 지날수록 제 얼굴이 찐빵으로 그려지네요~시간이 지날수록 제 얼굴이 찐빵으로
								그려지네요~시간이 지날수록 제 얼굴이 찐빵으로 그려지네요~시간이 지날수록 제 얼굴이 찐빵으로
								그려지네요~시간이 지날수록 제 얼굴이 찐빵으로 그려지네요~시간이 지날수록 제 얼굴이 찐빵으로
								그려지네요~시간이 지날수록 제 얼굴이 찐빵으로 그려지네요~</div>
						</dd>
						<!-- Util -->
						<dd class="util">
							<span><a href="">댓글달기</a>
							</span> <span> | <a href="">공감하기</a>
							</span> <span> | <a href="">더보기</a>
							</span> <span> | <span class="date">2012 년 03 월 09 일</span>
							</span>
						</dd>
						<!-- Util //-->
					</dl>
				</div></li>
			<!-- comment //-->
		</ul>
	</div>
	<!-- Panel2 //-->
	<!-- Panel3 -->
	<div>
		<ul class="panel_area">
			<!-- photo-->
			<li class="">
				<div class="photo_bg">
					<img src="../images/photo_default72_2.jpg" />
					<div class="rgt_name">사랑사랑</div>
				</div>
				<div class="grade">
					<div class="icon_mentor"></div>
					<div class="icon_star current"></div>
					<div class="icon_heart"></div>
				</div></li>
			<!-- photo//-->
			<!-- comment -->
			<li class="fr">
				<div class="point"></div>
				<div class="panel_block fr">
					<dl class="content">
						<dt class="icon_sm_notes">
							쪽지 <span class="icon_delete fr"><a href="">삭제</a>
							</span>
						</dt>
						<dd>
							<div class="notes">시간이 지날수록 제 얼굴이 찐빵으로 그려지네요~시간이 지날수록 제
								얼굴이 찐빵으로 그려지네요~시간이 지날수록 제 얼굴이 찐빵으로 그려지네요~시간이 지날수록 제 얼굴이 찐빵으로
								그려지네요~시간이 지날수록 제 얼굴이 찐빵으로 그려지네요~시간이 지날수록 제 얼굴이 찐빵으로
								그려지네요~시간이 지날수록 제 얼굴이 찐빵으로 그려지네요~시간이 지날수록 제 얼굴이 찐빵으로
								그려지네요~시간이 지날수록 제 얼굴이 찐빵으로 그려지네요~시간이 지날수록 제 얼굴이 찐빵으로
								그려지네요~시간이 지날수록 제 얼굴이 찐빵으로 그려지네요~</div>
						</dd>
						<!-- Util -->
						<dd class="util">
							<span><a href="">댓글달기</a>
							</span> <span> | <a href="">공감하기</a>
							</span> <span> | <a href="">더보기</a>
							</span> <span> | <span class="date">2012 년 03 월 09 일</span>
							</span>
						</dd>
						<!-- Util //-->
					</dl>
				</div></li>
			<!-- comment //-->
		</ul>
	</div>
	<!-- Panel3 //-->
	<!-- 더보기 -->
	<div class="more">
		<div class="icon_more">더보기</div>
	</div>
	<!-- 더보기 //-->
</div>
<!-- Comment Pannel-->