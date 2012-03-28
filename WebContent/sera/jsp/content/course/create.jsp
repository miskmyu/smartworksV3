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
	<div class="tit_newcourse">
		당신의 특별한 SERA Course 시작해보세요! 내 느낌과 내 스타일대로 만들고 싶은 코스를 자유롭게 만들어 보세요!<br />
		잠깐 스치는 생각, 다양한 기분과 이야기도 세라에선 당신만의 특별한 Story가 됩니다.
	</div>
	<div class="tit_dep2">
		<h2>코스 만들기</h2>
		<div>
			지적재산권, 음란물, 청소년 유해매체를 포함한 타인의 권리를 침해하는 자료 등 이용약관에 명시한 불법게시물을 <br />
			올리실 경우 경고 없이 삭제될 수있으며, 서비스 이용 제한 및 법적인 처벌을 받을 수 있습니다.
		</div>
	</div>
</div>
<!-- Header Title //-->
<!-- Input Form -->
<div class="form_layout">
	<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td><div class="form_label w101">코스 제목</div>
				<div class="form_value">
					<input type="text" class="fieldline fl" style="width: 493px">
					<div class="fr ml5">
						<span class="t_red">0</span> /1000kbyte
					</div>
				</div></td>
		</tr>
		<tr>
			<td><div class="form_label w101">코스 목적</div>
				<div class="form_value">
					<input type="text" class="fieldline fl" style="width: 493px">
					<div class="fr ml5">
						<span class="t_red">0</span> /1000kbyte
					</div>
				</div></td>
		</tr>
		<tr>
			<td><div class="form_label w101">상세설명</div>
				<div class="form_value">
					<textarea class="fieldline fl" name="textarea" rows="3" value=""
						style="width: 491px"></textarea>
					<div class="cb t_refe pt2">* 선택입력란입니다</div>
				</div></td>
		</tr>
		<tr>
			<td><div class="form_label w101">이미지</div>
				<div class="form_value">
					<input type="text" class="fieldline fl" style="width: 493px">
					</input>
					<div class="btn_mid_l ml5">
						<div class="btn_mid_r">찾아보기</div>
					</div>
					<div class="cb t_refe pt2">* 이미지를 등록하지 않은 경우 SERA에서 제공하는 기본
						이미지가 제공되어 보여집니다</div>
				</div></td>
		</tr>
		<tr>
			<td><div class="form_label w101">카테고리</div>
				<div class="form_value">
					<label> <input name="" type="checkbox" value="" /> 예술</label> <label>
						<input name="" type="checkbox" value="" /> 엔터테인먼트</label> <label>
						<input name="" type="checkbox" value="" /> 스타일</label> <label> <input
						name="" type="checkbox" value="" /> 생활</label> <label> <input
						name="" type="checkbox" value="" /> 영화/애니메이션</label> <label> <input
						name="" type="checkbox" value="" /> 게임</label> <br /> <label> <input
						name="" type="checkbox" value="" /> 영화</label> <label> <input
						name="" type="checkbox" value="" /> 이벤트</label> <label> <input
						name="" type="checkbox" value="" /> 스포츠</label> <label> <input
						name="" type="checkbox" value="" /> 이슈</label> <label> <input
						name="" type="checkbox" value="" /> 시사</label> <label> <input
						name="" type="checkbox" value="" /> 경제</label> <label> <input
						name="" type="checkbox" value="" /> 비즈니스</label> <label> <input
						name="" type="checkbox" value="" /> 미디어</label> <br /> <label> <input
						name="" type="checkbox" value="" /> 환경</label> <label> <input
						name="" type="checkbox" value="" /> 동물</label> <label> <input
						name="" type="checkbox" value="" /> 비영리/사회운동</label> <label> <input
						name="" type="checkbox" value="" /> 역사</label> <label> <input
						name="" type="checkbox" value="" /> 문학</label> <label> <input
						name="" type="checkbox" value="" /> 심리</label> <label> <input
						name="" type="checkbox" value="" /> 인물</label> <br /> <label> <input
						name="" type="checkbox" value="" /> 과학</label> <label> <input
						name="" type="checkbox" value="" /> 첨단기술</label> <label> <input
						name="" type="checkbox" value="" /> 의학</label> <label> <input
						name="" type="checkbox" value="" /> 건축</label> <label> <input
						name="" type="checkbox" value="" /> 교육</label> <label> <input
						name="" type="checkbox" value="" /> 기타</label>
				</div></td>
		</tr>
		<tr>
			<td><div class="form_label w101">키워드</div>
				<div class="form_value">
					<input type="text" style="width: 100px" class="fieldline fl" />
					<div class="btn_mid_l ml5">
						<div class="btn_mid_r">추 가</div>
					</div>
					<div class="cb t_refe pt2">* 코스의 이해를 도울수 있는 키워드 입력 (코스 검색 또는
						추천코스에 사용 됨)</div>
				</div></td>
		</tr>
		<tr>
			<td><div class="form_label w101">코스 기간</div>
				<div class="form_value">
					<input type="text" style="width: 100px" class="fieldline fl" />
					일(DAYS)
					<div class="fl t_refe pt2 ml5">* 코스 기간은 6개월 이내로 설정해 주세요</div>
					<div class="cb pt5">
						<label> <input name="" type="checkbox" value="" /> 사용자 정의</label>
						<input name="" class="fieldline form_date_input" type="text" /> <span
							class="icon_fb_date psr"></span> ~ <input name=""
							class="fieldline form_date_input" type="text" /> <span
							class="icon_fb_date psr"></span>
					</div>
					<div class="cb t_refe pt2">* 코스의 이해를 도울수 있는 키워드 입력 (코스 검색 또는
						추천코스에 사용 됨)</div>
				</div></td>
		</tr>
		<tr>
			<td><div class="form_label w101">공개 설정</div>
				<div class="form_value">
					<label> <input name="" type="checkbox" value="" /> 공개</label> <label>
						<input name="" type="checkbox" value="" /> 비공개</label>
					<div class="cb t_refe pt2">* 비공개 코스 설정 시 코스의 제반 내용이 노출되지 않습니다</div>
				</div></td>
		</tr>
		<tr>
			<td><div class="form_label w101">멘티 인원 제한</div>
				<div class="form_value">
					<label> <input name="" type="checkbox" value="" /> 무제한</label> <label>
						<input name="" type="checkbox" value="" /> 직접입력</label> <input
						class=" fieldline" type="text" style="width: 80px" /> 명
					<div class="cb t_refe pt2">* 코스 인원 제한 시 정해진 인원만 코스를 이용할 수
						있습니다</div>
				</div></td>
		</tr>
		<tr>
			<td><div class="form_label w101">멘티 가입 승인</div>
				<div class="form_value">
					<label> <input name="" type="checkbox" value="" /> 자동승인</label> <label>
						<input name="" type="checkbox" value="" /> 멘토승인</label>
					<div class="cb t_refe pt2">* 회원이 코스를 참여하는 데 있어 승인절차를 선택할 수
						있습니다</div>
				</div></td>
		</tr>
		<tr>
			<td><div class="form_label w101">코스 유료 설정</div>
				<div class="form_value">
					<label> <input name="" type="checkbox" value="" /> 무료</label> <label>
						<input name="" type="checkbox" value="" /> 유료</label> (금액 입력: <input
						class=" fieldline" type="text" style="width: 80px" /> 원)
					<div class="cb t_refe pt2">* 코스 유료화는 관리자 승인 후 개설이 가능하므로 유료버전의
						코스인 경우 바로 코스 생성이 되지 않습니다</div>
				</div></td>
		</tr>

	</table>
</div>
<!-- Btn -->
<div class="btn_space">
	<div style="clear: both; display: inline-block">
		<div class="btn_blu_l mr10">
			<div class="btn_blu_r">취 소</div>
		</div>
		<div href="mentorProfile.sw" class="btn_blu_l js_sera_content">
			<div class="btn_blu_r">멘토 프로필 입력하기</div>
		</div>
	</div>
</div>
<!-- Btn //-->
<!-- Input Form //-->