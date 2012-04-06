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
	<div class="tit_dep2 m0">
		<h2>팀 관리</h2>
		<div>코스 개설자(멘토)는 본인이 구성한 팀 뿐만 아니라, 코스 내에 활동하는 모든 팀을 관리할 수 있습니다.</div>
	</div>
</div>
<!-- Header Title //-->
<!-- Input Form -->
<div class="form_layout">
	<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td><div class="form_label w101">팀 이름</div>
				<div class="form_value">선린 짱 모임</div></td>
		</tr>
		<tr>
			<td><div class="form_label w101">팀 설명</div>
				<div class="form_value w569">
					<textarea class="fieldline fl" name="textarea" rows="3" value=""></textarea>
				</div></td>
		</tr>

		<tr>
			<td><div class="form_label w101">코스 기간</div>
				<div class="form_value">
					<input type="text" style="width: 100px" class="fieldline fl mr5" />
					<span class="fl mr15">일(DAYS)</span>
					<div class="t_refe pt2 fl">* 기간은 해당코스 기간을 넘을 수 없습니다</div>

					<div class="cb pt10">
						<label> <input name="" type="checkbox" value="" /> 사용자 정의</label>
						<input name="" class="fieldline form_date_input" type="text" /> <span
							class="icon_fb_date psr"></span> ~ <input name=""
							class="fieldline form_date_input" type="text" /> <span
							class="icon_fb_date psr"></span>
					</div>
				</div></td>
		</tr>
		<tr>
			<td><div class="form_label w101">코스 공개 설정</div>
				<div class="form_value">
					<label> <input name="" type="checkbox" value="" /> 공개</label> <label>
						<input name="" type="checkbox" value="" /> 비공개</label>
					<div class="cb t_refe pt2">* 비공개 설정 시, 코스의 멘토외에는 팀의 모든 활동이
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
</div>
<!-- Btn -->
<div class="btn_space">
	<div style="clear: both; display: inline-block">
		<div class="btn_blu_l mr10">
			<div class="btn_blu_r">팀 수정</div>
		</div>
		<div class="btn_red_l">
			<div class="btn_red_r">팀 삭제</div>
		</div>
	</div>
</div>
<!-- Btn //-->
<!-- Input Form //-->