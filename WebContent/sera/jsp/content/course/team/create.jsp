<%@page import="net.smartworks.model.sera.Course"%>
<%@page import="net.smartworks.model.security.AccessPolicy"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@ page contentType="text/html; charset=utf-8"%>

<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 그리고 현재사용자 정보도 가져온다.	
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();

	String courseId = request.getParameter("courseId");
	Course course = (Course)session.getAttribute("course");
	if (SmartUtil.isBlankObject(course) || !course.getId().equals(courseId))
		course = smartWorks.getCourseById(courseId);
%>

<script type="text/javascript">

	function submitForms(e) {
		var createTeam = $('.js_create_team_page');
		if(createTeam.find('input[name="chkUserDefineDays"]').attr('checked')==='checked'){
			createTeam.find('input[name="txtTeamStartDate"]').addClass('required');
			createTeam.find('input[name="txtTeamEndDate"]').addClass('required');
			createTeam.find('input[name="txtTeamDays"]').removeClass('required').removeClass('error').attr('value', '');			
		}else{
			createTeam.find('input[name="txtTeamStartDate"]').removeClass('required').removeClass('error');
			createTeam.find('input[name="txtTeamEndDate"]').removeClass('required').removeClass('error');
			createTeam.find('input[name="txtTeamDays"]').addClass('required');			
		}
		if (SmartWorks.GridLayout.validate(createTeam.find('form.js_validation_required'),  createTeam.find('.sw_error_message'))) {
			var forms = createTeam.find('form');
			var courseOpenDate = new Date(createTeam.attr('courseOpenDate'));
			var courseCloseDate = new Date(createTeam.attr('courseCloseDate'));
			var startDate = new Date(forms.find('input[name="txtTeamStartDate"]').attr('value'));
			var endDate = new Date(forms.find('input[name="txtTeamEndDate"]').attr('value'));
			if(createTeam.find('input[name="chkUserDefineDays"]').attr('checked')!=='checked'){
				var teamDays = parseInt(createTeam.find('input[name="txtTeamDays"]').attr('value'));
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
			paramsJson['courseId'] = createTeam.attr('courseId');
			for(var i=0; i<forms.length; i++){
				var form = $(forms[i]);
				if(form.attr('name') === 'frmSmartForm'){
					paramsJson['formId'] = form.attr('formId');
					paramsJson['formName'] = form.attr('formName');
				}
				paramsJson[form.attr('name')] = mergeObjects(form.serializeObject(), SmartWorks.GridLayout.serializeObject(form));
			}
			console.log(JSON.stringify(paramsJson));
			var progressSpan = createTeam.find('.js_progress_span');
			smartPop.progressCont(progressSpan);
			var url = "create_new_team.sw";
			$.ajax({
				url : url,
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					// 사용자정보 수정이 정상적으로 완료되었으면, 현재 페이지에 그대로 있는다.
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.INFO, "팀이 정상적으로 만들어졌습니다!", function(){
						var selectCourseTeam = $('.js_select_course_team select');
						if(!isEmpty(selectCourseTeam)){
							selectCourseTeam.append('<option value="' + data.teamId + '" selected>' + data.teamName + '</option>' );
							selectCourseTeam.change();
						}else{
							$('.js_create_team').click();
						}
					});
				},
				error : function(e) {
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.ERROR, "팀생성에 문제가 발생하였습니다. 관리자에게 문의하시기 바랍니다!");
				}
			});
		}
	};
</script>

<div class="js_create_team_page" courseId="<%=courseId%>" courseOpenDate="<%=course.getOpenDate().toLocalDateSimpleString()%>" courseCloseDate="<%=course.getCloseDate().toLocalDateSimpleString()%>">
	<!-- Header Title -->
	<div class="mb10">
		<span class="t_red">지적재산권, 음란물, 청소년 유해매체</span>를 포함한 타인의 권리를 침해하는 자료
		등 이용약관에 명시한 불법게시물을 올리실 경우 경고 없이 삭제될 수있으며, 서비스 이용 제한 및 법적인 처벌을 받을 수
		있습니다.
	</div>
	<!-- Header Title //-->
	<!-- Input Form -->
	<form name="frmCreateTeam" class="form_layout js_validation_required">
		<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
					<div class="form_label w101">팀 이름</div>
					<div class="form_value w570">
						<input name="txtTeamName" type="text" maxLength="50" class="fieldline fl required">
					</div>
				</td>
			</tr>
			<tr>
				<td><div class="form_label w101">팀설명</div>
					<div class="form_value w570">
						<textarea name="txtaTeamDesc" class="fieldline fl required" name="textarea" rows="3" style="width: 100%"></textarea>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label w101">팀원 초대</div>
					<div class="form_value">
						<div class="js_team_members_field" courseId="<%=courseId%>"></div> 
						<div class="cb t_refe pt2">* 팀 구성원은 해당 코스에서 활동하는 코스친구만 초대가 가능합니다</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label w101">기간</div>
					<div class="form_value">
						<input name="txtTeamDays" type="text" style="width: 100px" class="fieldline fl tr number" />
						<div class="fl mt2 ml2"> 일(Days)</div>
						<div class="fl ml10">
							<input class="fl pt2" name="chkUserDefineDays" type="checkbox"/>
							<label class="fl">사용자 정의</label>
							<div class="fl js_team_start_date_field" style="width:110px"></div>
							<div class="fl mr10">~</div> 
							<div class="fl js_team_end_date_field" style="width:110px"></div>
						</div>
						<div class="cb t_refe">* 정해진 날짜를 통해 코스를 진행해야하는 경우 상세기간 입력으로 해당 날짜를 입력해주세요</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label w101">팀 공개 설정</div>
					<div class="form_value">
						<input name="chkTeamSecurity" type="radio" checked value="<%=AccessPolicy.LEVEL_PUBLIC %>" /><label>공개</label>
						<input name="chkTeamSecurity" type="radio" value="<%=AccessPolicy.LEVEL_PRIVATE %>" />비공개
						<div class="cb t_refe pt2">* 비공개 코스 설정 시, 코스의 제반내용이 노출되지 않습니다</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label w101">구성 인원</div>
					<div class="form_value">
						<input name="txtTeamUsers" class=" fieldline number required tr" type="text" style="width: 80px" /> 명
					</div>
				</td>
			</tr>
		</table>
	</form>
	
	<div class="sw_error_message tl" style="color: red"></div>

	<!-- Btn -->
	<div class="btn_space">
		<div style="clear: both; display: inline-block">
			<div href="" class="btn_blu_l mr10 js_create_team_btn">
				<div class="btn_blu_r">팀 구성하기</div>
			</div>
			<div href="" class="btn_blu_l js_create_team">
				<div class="btn_blu_r">취 소</div>
			</div>
		</div>
	</div>
	<!-- Btn //-->
	<!-- Input Form //-->
</div>


<script type="text/javascript">
	loadCreateTeamFields();
</script>
