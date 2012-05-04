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
		특별한 코스의 멘토가 누구인지 차근차근 알려주는 멘토 프로필!<br /> 멘티와 친구들이 공유하는 멘토와의 만남입니다.
	</div>
	<div class="tit_dep2">
		<h2>멘토 프로필 작성</h2>
		<div>
			사실이 아닌 자료 등을 올리 실 경우 경고가 이루어질 수 있으며, 서비스 이용 제한 등 불이익을 받을 수 있습니다.<br />
			<span class="t_refe">* 선택입력란입니다.</span>
		</div>
	</div>
</div>
<!-- Header Title //-->
<!-- Input Form -->
<div class="form_layout">
	<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td><div class="form_label">멘토이름</div>
				<div class="form_value">성춘향</div>
			</td>
		</tr>
		<tr>
			<td><div class="form_label">멘토 아이디</div>
				<div class="form_value">betraybetray</div>
			</td>
		</tr>
		<tr>
			<td><div class="form_label">멘토닉네임 *</div>
				<div class="form_value w570">
					<input type="text" class="fieldline">
				</div>
			</td>
		</tr>
		<tr>
			<td><div class="form_label">학 력</div>
				<div class="form_value w570">
					<textarea class="fieldline" name="textarea" rows="3" value=""></textarea>
				</div>
			</td>
		</tr>
		<tr>
			<td><div class="form_label">경 력</div>
				<div class="form_value w570">
					<textarea class="fieldline" name="textarea" rows="3" value=""></textarea>
				</div>
			</td>
		</tr>
		<tr>
			<td><div class="form_label">세라활동 *</div>
				<div class="form_value w570">
					<div class="fl" style="width: 280px">
						<textarea class="fieldline" name="" rows="3" value="">멘토활동</textarea>
					</div>
					<div class="fr" style="width: 280px">
						<textarea class="fieldline" name="" rows="3" value="">멘티활동</textarea>
					</div>
				</div>
			</td>
		</tr>
		<tr>
			<td><div class="form_label">강의활동 *</div>
				<div class="form_value w570">
					<textarea class="fieldline" name="textarea" rows="3" value=""></textarea>
				</div>
			</td>
		</tr>
		<tr>
			<td><div class="form_label">수상경력*</div>
				<div class="form_value w570">
					<textarea class="fieldline" name="textarea" rows="3" value=""></textarea>
				</div>
			</td>
		</tr>
		<tr>
			<td><div class="form_label">기타활동*</div>
				<div class="form_value w570">
					<textarea class="fieldline" name="textarea" rows="3" value=""></textarea>
				</div>
			</td>
		</tr>
	</table>
</div>

<!-- Btn -->
<div class="btn_space">
	<div style="clear: both; display: inline-block">
		<div href="createCourse.sw" class="btn_blu_l mr10 js_sera_content">
			<div class="btn_blu_r">코스 만들기</div>
		</div>

		<div class="btn_blu_l">
			<div class="btn_blu_r">취 소</div>
		</div>
	</div>
</div>
<!-- Btn //-->

<!-- Input Form //-->
