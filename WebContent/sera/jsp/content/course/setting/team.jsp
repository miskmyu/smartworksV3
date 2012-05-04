<%@page import="net.smartworks.model.security.AccessPolicy"%>
<%@page import="net.smartworks.model.sera.Team"%>
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
	Course course = smartWorks.getCourseById(courseId);
	Team team = course.getTeam();
%>

<script type="text/javascript">

	function submitForms(e) {
		var courseSettingTeam = $('.js_course_setting_team_page');
		if(courseSettingTeam.find('input[name="chkUserDefineDays"]').attr('checked')==='checked'){
			courseSettingTeam.find('input[name="txtTeamStartDate"]').addClass('required');
			courseSettingTeam.find('input[name="txtTeamEndDate"]').addClass('required');
			courseSettingTeam.find('input[name="txtTeamDays"]').removeClass('required').removeClass('error');			
			
		}else{
			courseSettingTeam.find('input[name="txtTeamStartDate"]').removeClass('required').removeClass('error');
			courseSettingTeam.find('input[name="txtTeamEndDate"]').removeClass('required').removeClass('error');
			courseSettingTeam.find('input[name="txtTeamDays"]').addClass('required');			
		}
		if (SmartWorks.GridLayout.validate(courseSettingTeam.find('form.js_validation_required'),  courseSettingTeam.find('.sw_error_message'))) {
			var forms = courseSettingTeam.find('form');
			var paramsJson = {};
			paramsJson['courseId'] = courseSettingTeam.attr('courseId');
			paramsJson['teamId'] = courseSettingTeam.attr('teamId');
			for(var i=0; i<forms.length; i++){
				var form = $(forms[i]);
				if(form.attr('name') === 'frmSmartForm'){
					paramsJson['formId'] = form.attr('formId');
					paramsJson['formName'] = form.attr('formName');
				}
				paramsJson[form.attr('name')] = mergeObjects(form.serializeObject(), SmartWorks.GridLayout.serializeObject(form));
			}
			console.log(JSON.stringify(paramsJson));
			var progressSpan = courseSettingTeam.find('.js_progress_span');
			smartPop.progressCont(progressSpan);
			var url = "modify_course_team.sw";
			$.ajax({
				url : url,
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					// 사용자정보 수정이 정상적으로 완료되었으면, 현재 페이지에 그대로 있는다.
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.INFO, "팀정보가 정상적으로 수정되었습니다!", function(){
						$('.js_course_home_page .js_course_main_menu .js_create_team').click();
					});
				},
				error : function(e) {
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.ERROR, "팀정보 수정에 문제가 발생하였습니다. 관리자에게 문의하시기 바랍니다!");
				}
			});
		}
	};
</script>

<!-- Header Title -->
<div class="header_tit">
	<div class="tit_dep2 m0">
		<h2>팀 관리</h2>
		<div>코스 개설자(멘토)는 본인이 구성한 팀 뿐만 아니라, 코스 내에 활동하는 모든 팀을 관리할 수 있습니다.</div>
	</div>
</div>
<!-- Header Title //-->

<%
if(SmartUtil.isBlankObject(team)){
%>
	<<jsp:include page="/sera/jsp/content/course/detail/create_team.jsp">
		<jsp:param value="<%=courseId %>" name="courseId"/>
	</jsp:include>
<%
}else{
%>
	<!-- Input Form -->
	<form name="frmModifyTeam" class="form_layout js_validation_required js_course_setting_team_page" courseId="<%=courseId%>" teamId="<%=team.getId()%>">
		<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td><div class="form_label w101">팀 이름</div>
					<div class="form_value w570"><%=team.getName() %></div></td>
			</tr>
			<tr>
				<td><div class="form_label w101">팀 설명</div>
					<div class="form_value w570">
						<textarea name="txaTeamDesc" class="fieldline fl required" name="textarea" rows="3"><%=team.getDesc() %></textarea>
					</div>
				</td>
			</tr>
	
			<tr>
				<td><div class="form_label w101">코스 기간</div>
					<div class="form_value">
						<input name="txtTeamDays" type="text" style="width: 100px" class="fieldline fl tr number"/>
						<div class="fl mt2 ml2"> 일(Days)</div>
						<div class="fl ml10">
							<input class="fl pt2" name="chkUserDefineDays" checked type="checkbox"/>
							<label class="fl">사용자 정의</label>
							<div class="fl js_team_start_date_field" startDate="<%=team.getStart().toLocalDateSimpleString() %>" style="width:110px"></div>
							<div class="fl mr10">~</div> 
							<div class="fl js_team_end_date_field" endDate="<%=team.getEnd().toLocalDateSimpleString() %>" style="width:110px"></div>
						</div>
						<div class="cb t_refe">* 정해진 날짜를 통해 코스를 진행해야하는 경우 상세기간 입력으로 해당 날짜를 입력해주세요</div>
					</div></td>
			</tr>
			<tr>
				<td><div class="form_label w101">팀 공개 설정</div>
					<div class="form_value">
						<input name="chkTeamSecurity" type="radio" <%if(team.getAccessPolicy() == AccessPolicy.LEVEL_PUBLIC){ %>checked<%} %> value="<%=AccessPolicy.LEVEL_PUBLIC %>" /><label>공개</label>
						<input name="chkTeamSecurity" type="radio" <%if(team.getAccessPolicy() == AccessPolicy.LEVEL_PRIVATE){ %>checked<%} %> value="<%=AccessPolicy.LEVEL_PRIVATE %>" />비공개
						<div class="cb t_refe pt2">* 비공개 코스 설정 시, 코스의 제반내용이 노출되지 않습니다</div>
					</div></td>
			</tr>
			<tr>
				<td><div class="form_label w101">구성 인원</div>
					<div class="form_value">
						<input name="txtTeamUsers" class=" fieldline number required tr" value="<%=team.getMaxMembers() %>" type="text" style="width: 80px" /> 명
					</div></td>
			</tr>
		</table>
	</div>
	<!-- Btn -->
	<div class="btn_space">
		<div style="clear: both; display: inline-block">
			<div class="btn_blu_l mr10 js_modify_team_btn">
				<div class="btn_blu_r">팀 수정</div>
			</div>
			<div class="btn_red_l js_remove_team_btn">
				<div class="btn_red_r">팀 삭제</div>
			</div>
		</div>
	</div>
	<!-- Btn //-->
	<!-- Input Form //-->

<%
}
%>

<script type="text/javascript">
	loadCreateTeamFields();
</script>
