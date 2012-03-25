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
<!-- Course Define -->
<div class="course_df_section">
	<div class="course_df_block">
		<div class="course_df_img">
			<img src="../images/cus_df_img.jpg" />
		</div>
		<div class="course_df">
			<h1>자화상 그리기</h1>
			<ul class="mt8">
				<li>
					<div class="icon_cs_mentorname">
						<span>멘토명</span>
					</div> <span class="t_s14">홍길동</span></li>
				<li>
					<div class="icon_cs_openday">
						<span>개설일</span>
					</div> <span>2012.02.22</span></li>
				<li>
					<div class="icon_cs_menteenum">
						<span>멘티수</span>
					</div> <span class="t_s14">24</span></li>
			</ul>
		</div>
		<div class="course_info">
			<dl>
				<dt>코스알림</dt>
				<dd>- [알림] 3월 14일 번개팅합니다</dd>
				<dd>- 미션6이 등록되었습니다</dd>
				<dd>- [이벤트] 미션2 선착순 3명!...</dd>
				<dd>- [이벤트] 미션2 선착순 3명!...</dd>
			</dl>
		</div>
	</div>
	<div class="course_menu">
		<div class="btn_green_l mt8 fl">
			<div class="btn_green_r">
				<span class="icon_green_down mr5"></span>친구초대
			</div>
		</div>
		<!-- Menu Dep1-->
		<div class="course_menu_d1">
			<ul>
				<li>홈</li>
				<li>미션</li>
				<li>코스알림</li>
				<li class="current">코스설정</li>
			</ul>
		</div>
		<!-- Menu Dep1//-->
		<div class="icon_twitter fr ml5mt7">
			<a href="">트위터</a>
		</div>
		<div class="icon_facebook fr ml5mt7">
			<a href="">페이스북</a>
		</div>
		<div class="fr" style="margin: 8px 30px 0 0">
			<div class="btn_mid_l">
				<div class="btn_mid_r">팀구성하기</div>
			</div>
		</div>
	</div>
	<!-- Menu Dep2 -->
	<div class="course_menu_d2">
		<div class="menu001" style="visibility: hidden"></div>
		<div class="menu002 current" style="visibility: hidden">
			<ul>
				<li><a href="">미션등록/수행</a>
				</li>
				<li><a href="">전체보기</a>
				</li>
				<li><a href="">내글보기</a>
				</li>
				<li class="end"><a href="">미션별 보기</a>
				</li>
			</ul>
		</div>
		<div class="menu003" style="visibility: hidden"></div>
		<div class="menu004">
			<ul>
				<li class="current"><a href=""> 코스관리</a>
				</li>
				<li><a href="">멘티관리</a>
				</li>
				<li class="end"><a href=""> <span
						class="icon_bul_select mr5"></span>팀관리 </a></li>
			</ul>
		</div>
	</div>
	<!-- Menu Dep2 //-->
</div>
<!-- Course Define //-->
<!-- Header Title -->
<div class="header_tit">
	<div class="tit_dep2 m0">
		<h2>코스관리</h2>
		<div>코스 개설자(멘토)는 코스에 대한 정보를 수정하고 관리할 수 있습니다.</div>
	</div>
</div>
<!-- Header Title //-->
<!-- Input Form -->
<div class="form_layout">
	<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td><div class="form_label w101">코스 제목</div>
				<div class="form_value">세라코스 - 선린 인터넷 고등학교 편</div>
			</td>
		</tr>
		<tr>
			<td><div class="form_label w101">코스 목적</div>
				<div class="form_value">
					<input type="text" class="fieldline fl" style="width: 493px">
					<div class="fr ml5">
						<span class="t_red">0</span> /150kbyte
					</div>
				</div>
			</td>
		</tr>
		<tr>
			<td><div class="form_label w101">상세설명</div>
				<div class="form_value">
					<textarea class="fieldline fl" name="textarea" rows="3" value=""
						style="width: 491px"></textarea>
					<div class="cb t_refe pt2">* 선택입력란입니다</div>
				</div>
			</td>
		</tr>
		<tr>
			<td><div class="form_label w101">코스 이미지</div>
				<div class="form_value">
					<input type="text" class="fieldline fl" style="width: 493px">
					</input>
					<div class="btn_mid_l ml5">
						<div class="btn_mid_r">찾아보기</div>
					</div>
				</div>
			</td>
		</tr>
		<tr>
			<td><div class="form_label w101">키워드</div>
				<div class="form_value">
					<div class="fl">
						<span class="mr10">관심분야</span> <span class="mr10">성실성</span> <span
							class="mr10">리더십</span> <span class="mr10">열정</span>
					</div>
					<input type="text" style="width: 100px" class="fieldline fl" />
					<div class="btn_mid_l ml5">
						<div class="btn_mid_r">추 가</div>
					</div>
				</div>
			</td>
		</tr>
		<tr>
			<td><div class="form_label w101">코스 기간</div>
				<div class="form_value">
					<input type="text" style="width: 100px" class="fieldline fl mr5" />
					<span class="fl mr15">일(DAYS)</span>
					<div class="t_refe pt2 fl">* 코스 기간은 6개월 이내로 설정해 주세요</div>

					<div class="cb pt10">
						<label> <input name="" type="checkbox" value="" /> 사용자 정의</label>
						<input name="" class="fieldline form_date_input" type="text" /> <span
							class="icon_fb_date psr"></span> ~ <input name=""
							class="fieldline form_date_input" type="text" /> <span
							class="icon_fb_date psr"></span>
					</div>
					<div class="cb t_refe pt2">* 정해진 날짜를 통해 코스를 진행해야 하는 경우 코스
						상세기간 입력으로 해당 날짜를 입력해주세요</div>
				</div>
			</td>
		</tr>
		<tr>
			<td><div class="form_label w101">공개 설정</div>
				<div class="form_value">
					<label> <input name="" type="checkbox" value="" /> 공개</label> <label>
						<input name="" type="checkbox" value="" /> 비공개</label>
				</div>
			</td>
		</tr>
		<tr>
			<td><div class="form_label w101">멘티 인원 제한</div>
				<div class="form_value">
					<label> <input name="" type="checkbox" value="" /> 무제한</label> <label>
						<input name="" type="checkbox" value="" /> 직접입력</label> <input
						class=" fieldline" type="text" style="width: 80px" /> 명
					<div class="cb t_refe pt2">* 현재 등록된 인원은 제외, 인원 제한을 지정한 다음부터
						적용됩니다.</div>
				</div>
			</td>
		</tr>
		<tr>
			<td><div class="form_label w101">멘티 가입 승인</div>
				<div class="form_value">
					<label> <input name="" type="checkbox" value="" /> 자동승인</label> <label>
						<input name="" type="checkbox" value="" /> 멘토승인</label>
					<div class="cb t_refe pt2">* 회원이 코스를 참여하는 데 있어 승인절차를 선택할 수
						있습니다</div>
				</div>
			</td>
		</tr>
	</table>
</div>
<!-- Btn -->
<div class="btn_space">
	<div style="clear: both; display: inline-block">
		<div class="btn_blu_l mr10">
			<div class="btn_blu_r">코스 수정</div>
		</div>
		<div class="btn_red_l">
			<div class="btn_red_r">코스 삭제</div>
		</div>
	</div>
</div>
<!-- Btn //-->
<!-- Input Form //-->
