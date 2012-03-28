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
<div class="my_comment_section">
	<!-- Photo Section -->
	<div class="photo_section">
		<div class="my_photo">
			<img src="" />친구사진
		</div>
		<!-- Btn -->
		<div class="btn_green_l cb" style="margin: 8px 0 0 10px">
			<div class="btn_green_r">
				<span class="icon_green_down mr5"></span>친구요청
			</div>
		</div>
		<!-- Btn //-->
	</div>
	<!-- Photo Section //-->
	<!-- My Comment -->
	<div class="my_comment">
		<div class="header">사랑사랑님</div>
		<div class="comment_txt ">
			<textarea name="" cols="" rows="5">전공 공부만큼 영어를 좋아했는데 입사하고 나니 영어에는 손을 뚝 끊어버리게 되네요~ 전공 공부만큼 영어를 좋아했는데 입사하고 나니 영어에는 손을 뚝 끊어버리게 되네요~전공 공부만큼 영어를 좋아했는데 입사하고 나니 영어에는 손을 뚝 끊어버리게 되네요~전공 공부만큼 영어를 좋아했는데 입사하고 나니 영어에는 손을 뚝 끊어버리게 되네요~전공 공부만큼 영어를
                </textarea>
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
					<a href=""> 전체공개<span class="icon_bul_select ml5"></span> </a> |
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
	</div>
	<!-- My Comment //-->
</div>

<!-- Panel Section -->
<div class="content_section">

	<!-- Panel2 -->
	<div>
		<div class="header mt20">
			<div class="fl">
				<span class="t_myid">사랑사랑님</span>의 친구 (35)
			</div>

			<div class="fr">
				<input class="fl fieldline" style="width: 150px" type="text" />
				<button type="button" class="fl ml5">검색</button>
			</div>
		</div>

		<div class="panel_area">

			<!-- 목록1-->
			<div class="panel_rds_block mb10">
				<ul>
					<li class="pl0pr10"><img src="../images/photo_mid48_2.jpg" />
					</li>
					<li class="w90"><span> 피터팬<br /> <span class="cb t_id">identity</span>
					</span>
					</li>
					<li class="bo_l w370"><span>
							목표내용목표내용목표내용목표내용목표내용목표내용목표내용목표내용<br /> <span class="t_id">hongildong@korea.com</span>
					</span>
					</li>
					<li class="fr bo_l"><span> <!-- Btn -->
							<div class="btn_green_l">
								<div class="btn_green_r">
									<span class="icon_green_down mr5"></span>친구 요청
								</div>
							</div> <!-- Btn //--> </span>
					</li>
				</ul>
			</div>
			<!-- 목록1//-->

			<!-- 목록1-->
			<div class="panel_rds_block mb10">
				<ul>
					<li class="pl0pr10"><img src="../images/photo_mid48_2.jpg" />
					</li>
					<li class="w90"><span> 피터팬<br /> <span class="cb t_id">identity</span>
					</span>
					</li>
					<li class="bo_l w370"><span>
							목표내용목표내용목표내용목표내용목표내용목표내용목표내용목표내용<br /> <span class="t_id">hongildong@korea.com</span>
					</span>
					</li>
					<li class="fr bo_l"><span> <!-- Btn -->
							<div class="btn_green_l">
								<div class="btn_green_r">
									<span class="icon_green_down mr5"></span>친구 요청
								</div>
							</div> <!-- Btn //--> </span>
					</li>
				</ul>
			</div>
			<!-- 목록1//-->

			<!-- 목록1-->
			<div class="panel_rds_block mb10">
				<ul>
					<li class="pl0pr10"><img src="../images/photo_mid48_2.jpg" />
					</li>
					<li class="w90"><span> 피터팬<br /> <span class="cb t_id">identity</span>
					</span>
					</li>
					<li class="bo_l w370"><span>
							목표내용목표내용목표내용목표내용목표내용목표내용목표내용목표내용<br /> <span class="t_id">hongildong@korea.com</span>
					</span>
					</li>
					<li class="fr bo_l"><span> <!-- Btn -->
							<div class="icon_ourfriend" style="">우리는 친구</div> <!-- Btn //-->
					</span>
					</li>
				</ul>
			</div>
			<!-- 목록1//-->
			<!-- 목록1-->
			<div class="panel_rds_block mb10">
				<ul>
					<li class="pl0pr10"><img src="../images/photo_mid48_2.jpg" />
					</li>
					<li class="w90"><span> 피터팬<br /> <span class="cb t_id">identity</span>
					</span>
					</li>
					<li class="bo_l w370"><span>
							목표내용목표내용목표내용목표내용목표내용목표내용목표내용목표내용<br /> <span class="t_id">hongildong@korea.com</span>
					</span>
					</li>
					<li class="fr bo_l"><span> <!-- Btn -->
							<div class="btn_green_l">
								<div class="btn_green_r">
									<span class="icon_green_down mr5"></span>친구 요청
								</div>
							</div> <!-- Btn //--> </span>
					</li>
				</ul>
			</div>
			<!-- 목록1//-->
			<!-- 목록1-->
			<div class="panel_rds_block mb10">
				<ul>
					<li class="pl0pr10"><img src="../images/photo_mid48_2.jpg" />
					</li>
					<li class="w90"><span> 피터팬<br /> <span class="cb t_id">identity</span>
					</span>
					</li>
					<li class="bo_l w370"><span>
							목표내용목표내용목표내용목표내용목표내용목표내용목표내용목표내용<br /> <span class="t_id">hongildong@korea.com</span>
					</span>
					</li>
					<li class="fr bo_l"><span> <!-- Btn -->
							<div class="btn_green_l">
								<div class="btn_green_r">
									<span class="icon_green_down mr5"></span>친구 요청
								</div>
							</div> <!-- Btn //--> </span>
					</li>
				</ul>
			</div>
			<!-- 목록1//-->
			<!-- 목록1-->
			<div class="panel_rds_block mb10">
				<ul>
					<li class="pl0pr10"><img src="../images/photo_mid48_2.jpg" />
					</li>
					<li class="w90"><span> 피터팬<br /> <span class="cb t_id">identity</span>
					</span>
					</li>
					<li class="bo_l w370"><span>
							목표내용목표내용목표내용목표내용목표내용목표내용목표내용목표내용<br /> <span class="t_id">hongildong@korea.com</span>
					</span>
					</li>
					<li class="fr bo_l"><span> <!-- Btn -->
							<div class="btn_green_l">
								<div class="btn_green_r">
									<span class="icon_green_down mr5"></span>친구 요청
								</div>
							</div> <!-- Btn //--> </span>
					</li>
				</ul>
			</div>
			<!-- 목록1//-->
			<!-- 목록1-->
			<div class="panel_rds_block mb10">
				<ul>
					<li class="pl0pr10"><img src="../images/photo_mid48_2.jpg" />
					</li>
					<li class="w90"><span> 피터팬<br /> <span class="cb t_id">identity</span>
					</span>
					</li>
					<li class="bo_l w370"><span>
							목표내용목표내용목표내용목표내용목표내용목표내용목표내용목표내용<br /> <span class="t_id">hongildong@korea.com</span>
					</span>
					</li>
					<li class="fr bo_l"><span> <!-- Btn -->
							<div class="btn_green_l">
								<div class="btn_green_r">
									<span class="icon_green_down mr5"></span>친구 요청
								</div>
							</div> <!-- Btn //--> </span>
					</li>
				</ul>
			</div>
			<!-- 목록1//-->
			<!-- 목록1-->
			<div class="panel_rds_block mb10">
				<ul>
					<li class="pl0pr10"><img src="../images/photo_mid48_2.jpg" />
					</li>
					<li class="w90"><span> 피터팬<br /> <span class="cb t_id">identity</span>
					</span>
					</li>
					<li class="bo_l w370"><span>
							목표내용목표내용목표내용목표내용목표내용목표내용목표내용목표내용<br /> <span class="t_id">hongildong@korea.com</span>
					</span>
					</li>
					<li class="fr bo_l"><span> <!-- Btn -->
							<div class="btn_green_l">
								<div class="btn_green_r">
									<span class="icon_green_down mr5"></span>친구 요청
								</div>
							</div> <!-- Btn //--> </span>
					</li>
				</ul>
			</div>
			<!-- 목록1//-->
			<!-- 목록1-->
			<div class="panel_rds_block mb10">
				<ul>
					<li class="pl0pr10"><img src="../images/photo_mid48_2.jpg" />
					</li>
					<li class="w90"><span> 피터팬<br /> <span class="cb t_id">identity</span>
					</span>
					</li>
					<li class="bo_l w370"><span>
							목표내용목표내용목표내용목표내용목표내용목표내용목표내용목표내용<br /> <span class="t_id">hongildong@korea.com</span>
					</span>
					</li>
					<li class="fr bo_l"><span> <!-- Btn -->
							<div class="btn_green_l">
								<div class="btn_green_r">
									<span class="icon_green_down mr5"></span>친구 요청
								</div>
							</div> <!-- Btn //--> </span>
					</li>
				</ul>
			</div>
			<!-- 목록1//-->

		</div>
	</div>
	<!-- Panel2 //-->

</div>
<!-- Panel Section //-->
