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
%>
<!-- Header Title -->
<div class="header_tit">
	<div class="tit_dep2 m0">
		<h2>코스 설정</h2>
		<div>코스에 참여하고 있는 회원으로 코스를 탈퇴하거나, 불만사항을 접수하고 개설한 팀을 관리하할 수 있습니다.</div>
	</div>
</div>
<!-- Header Title //-->
<!-- Contents Section -->
<div class="content_section">
	<!-- 코스 탈퇴하기 -->
	<div class="tit_cus1"></div>
	<!-- 설정 상세 -->
	<div class="panel_block w635 mt5" style="display: none">
		<div class="content">
			<h3>코스 탈퇴하기</h3>
			<div class="mt5">
				<div class="tb">[코스명 1 코스명코스명코스명코스명..] 코스에서 탈퇴하시겠습니까?</div>
				코스에서 탈퇴하는 경우 현재 본인이 등록한 미션 수행 내용과 코스에 등록되어 있는 글, 정보 등이 모두 삭제됩니다. 그래도
				탈퇴를 원하신다면 탈퇴버튼을 클릭해 주십시오.
				<div class="t_red mb10">*욕설이나 비방하는 글은 삼가해 주세요.</div>
				<textarea name="" cols="" rows="5">멘티님이 코스를 탈퇴하는 이유를 적어주세요.</textarea>
			</div>
			<!-- Btn -->
			<div class="btn_space">
				<div style="clear: both; display: inline-block">
					<div class="btn_default_l mr10">
						<div class="btn_default_r">탈 퇴</div>
					</div>
					<div class="btn_default_l">
						<div class="btn_default_r">취 소</div>
					</div>
				</div>
			</div>
			<!-- Btn //-->

		</div>
	</div>
	<!-- 설정 상세 -->
	<!-- 코스 탈퇴하기 //-->

	<!-- 코스 신고하기 -->
	<div class="tit_cus2 mt20"></div>
	<!-- 설정 상세 -->
	<div class="panel_block w635 mt5">
		<div class="content">
			<h3>코스 신고하기</h3>
			<div class="mt5">
				<div class="tb">[코스명 1 코스명코스명코스명코스명..] 코스에서 탈퇴하시겠습니까?</div>
				코스 또는 개설자(멘토)에 대한 불만사항이나 잘못된 운영을 하고 있다면 알려주세요.
				<div class="t_red mb10">*욕설이나 비방하는 글은 삼가해 주세요.</div>
				<textarea name="" cols="" rows="5">신고 내용을 적어 주세요</textarea>
			</div>
			<!-- Btn -->
			<div class="btn_space">
				<div style="clear: both; display: inline-block">
					<div class="btn_default_l mr10">
						<div class="btn_default_r">신 고</div>
					</div>
					<div class="btn_default_l">
						<div class="btn_default_r">취 소</div>
					</div>
				</div>
			</div>
			<!-- Btn //-->

		</div>
	</div>
	<!-- 설정 상세 -->
	<!-- 코스 신고하기 //-->

	<!-- 팀 관리 -->
	<div class="tit_cus3 mt20"></div>
	<!-- 설정 상세 -->
	<div class="panel_block w635 mt5">
		<div class="content">
			<h3>코스 신고하기</h3>
			<table class="mt5">
				<tr>
					<td>
						<div class="form_label w101">멘토이름</div>
						<div class="form_value">성춘향</div>
					</td>
				</tr>
				<tr>
					<td>
						<div class="form_label w101">팀 설명</div>
						<div class="form_value" style="width: 510px">
							<textarea name="" cols="" rows=""></textarea>
						</div>
					</td>
				</tr>
				<tr>
					<td><div class="form_label w101">코스 기간</div>
						<div class="form_value">
							<input type="text" style="width: 100px" class="fieldline fl mr5" />
							<span class="fl mr15">일(DAYS)</span>
							<div class="t_refe pt2 fl">* 기간은 해당코스 기간을 넘을 수 없습니다</div>
							<div class="cb pt10">
								<label> <input name="" type="checkbox" value="" /> 사용자
									정의</label> <input name="" class="fieldline form_date_input" type="text" />
								<span class="icon_fb_date psr"></span> ~ <input name=""
									class="fieldline form_date_input" type="text" /> <span
									class="icon_fb_date psr"></span>
							</div>
						</div></td>
				</tr>
				<tr>
					<td><div class="form_label w101">팀 공개 설정</div>
						<div class="form_value">
							<label> <input name="" type="checkbox" value="" /> 공개</label> <label>
								<input name="" type="checkbox" value="" /> 비공개</label>
							<div class="cb t_refe pt2">* 비공개 설정 시, 코스의 멘토 외에는 팀의 모든 활동이
								노출되지 않습니다</div>
						</div></td>
				</tr>
				<tr>
					<td><div class="form_label w101">구성 인원</div>
						<div class="form_value">
							<input class=" fieldline" type="text" style="width: 80px" /> 명
						</div></td>
				</tr>
			</table>
			<!-- Btn -->
			<div class="btn_space">
				<div style="clear: both; display: inline-block">
					<div class="btn_default_l mr10">
						<div class="btn_default_r">수 정</div>
					</div>
					<div class="btn_default_l">
						<div class="btn_default_r">팀 삭제</div>
					</div>
				</div>
			</div>
			<!-- Btn //-->

		</div>
	</div>
	<!-- 설정 상세 -->
	<!-- 팀 관리 //-->

</div>
<!-- Contents Section //-->
