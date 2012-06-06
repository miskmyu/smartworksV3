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
	String teamId = request.getParameter("teamId");
	Team team = SmartUtil.isBlankObject(teamId) ? null :  (Team)session.getAttribute("team");
	if(!SmartUtil.isBlankObject(teamId) && SmartUtil.isBlankObject(team)) team = smartWorks.getTeamById(teamId);
	Course course = (Course)session.getAttribute("course");
	if (SmartUtil.isBlankObject(course) || !course.getId().equals(courseId))
		course = smartWorks.getCourseById(courseId);
%>

<script type="text/javascript">

	function submitForms(e) {
		var courseTeamManagement = $('.js_course_team_management_page');
		if(courseTeamManagement.find('input[name="chkUserDefineDays"]').attr('checked')==='checked'){
			courseTeamManagement.find('input[name="txtTeamStartDate"]').addClass('required');
			courseTeamManagement.find('input[name="txtTeamEndDate"]').addClass('required');
			courseTeamManagement.find('input[name="txtTeamDays"]').removeClass('required').removeClass('error').attr('value', '');			
			
		}else{
			courseTeamManagement.find('input[name="txtTeamStartDate"]').removeClass('required').removeClass('error');
			courseTeamManagement.find('input[name="txtTeamEndDate"]').removeClass('required').removeClass('error');
			courseTeamManagement.find('input[name="txtTeamDays"]').addClass('required');			
		}
		if (SmartWorks.GridLayout.validate(courseTeamManagement.find('form.js_validation_required'),  courseTeamManagement.find('.sw_error_message'))) {
			var forms = courseTeamManagement.find('form');
			var courseOpenDate = new Date(courseTeamManagement.attr('courseOpenDate'));
			var courseCloseDate = new Date(courseTeamManagement.attr('courseCloseDate'));
			var startDate = new Date(forms.find('input[name="txtTeamStartDate"]').attr('value'));
			var endDate = new Date(forms.find('input[name="txtTeamEndDate"]').attr('value'));
			if(courseTeamManagement.find('input[name="chkUserDefineDays"]').attr('checked')!=='checked'){
				var teamDays = parseInt(courseTeamManagement.find('input[name="txtTeamDays"]').attr('value'));
				if(teamDays <= 0){
					smartPop.showInfo(smartPop.ERROR, "팀기간은 최소 1일 이상이여야 합니다!");
					return false;					
				}
				startDate = new Date();
				endDate = new Date(startDate.getTime() + (teamDays-1)*24*60*60*1000);
			}
			if(startDate.getTime()>endDate.getTime()){
				smartPop.showInfo(smartPop.ERROR, "팀기간의 시작일자가 종료일자보다 이후입니다. 시작일자를 종료일자보다 이전으로 수정바랍니다!");
				return false;
			}else if(startDate.getTime()<courseOpenDate.getTime() || endDate.getTime()>courseCloseDate.getTime()){
				smartPop.showInfo(smartPop.ERROR, "팀기간은 코스기간 내에서만 설정가능합니다. 코스기간 내 일자로 수정바랍니다! (코스기간 : " + courseOpenDate.format("yyyy.mm.dd") + " ~ " + courseCloseDate.format("yyyy.mm.dd") + ")");
				return false;
			}
			var paramsJson = {};
			var courseId = courseTeamManagement.attr('courseId');
			var teamId = courseTeamManagement.attr('teamId');
			paramsJson['courseId'] = courseId;
			paramsJson['teamId'] = teamId;
			for(var i=0; i<forms.length; i++){
				var form = $(forms[i]);
				if(form.attr('name') === 'frmSmartForm'){
					paramsJson['formId'] = form.attr('formId');
					paramsJson['formName'] = form.attr('formName');
				}
				paramsJson[form.attr('name')] = mergeObjects(form.serializeObject(), SmartWorks.GridLayout.serializeObject(form));
			}
			console.log(JSON.stringify(paramsJson));
			var progressSpan = courseTeamManagement.find('.js_progress_span');
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
						var selectCourseTeam = $('.js_select_course_team select');
						if(!isEmpty(selectCourseTeam)){
							selectCourseTeam.find('option[value="' + teamId + '"]').attr('selected', 'selected');
							selectCourseTeam.change();
						}else{
							$('.js_course_team_menu').click();
						}
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

<div class="js_course_team_management_page"  courseId="<%=courseId%>" teamId="<%=team.getId()%>" courseOpenDate="<%=course.getOpenDate().toLocalDateSimpleString()%>" courseCloseDate="<%=course.getCloseDate().toLocalDateSimpleString()%>">
	<!-- Input Form -->
	<form name="frmModifyTeam" class="form_layout js_validation_required">
		<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td><div class="form_label w101">팀 이름</div>
					<div class="form_value w570"><%=team.getName() %></div></td>
			</tr>
			<tr>
				<td><div class="form_label w101">팀 설명</div>
					<div class="form_value w570">
						<textarea name="txtaTeamDesc" class="fieldline fl required" name="textarea" rows="3"><%=team.getDesc() %></textarea>
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
	</form>
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
</div>

<script type="text/javascript">
	loadCreateTeamFields();
</script>
