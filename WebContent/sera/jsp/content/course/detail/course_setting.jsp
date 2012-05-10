<%@page import="net.smartworks.util.SeraTest"%>
<%@page import="net.smartworks.model.security.AccessPolicy"%>
<%@page import="net.smartworks.model.sera.Team"%>
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
<script type="text/javascript">

	function submitForms(e) {
		var settingTeam = $('.js_course_setting_page');
		if(settingTeam.find('input[name="chkUserDefineDays"]').attr('checked')==='checked'){
			settingTeam.find('input[name="txtTeamStartDate"]').addClass('required');
			settingTeam.find('input[name="txtTeamEndDate"]').addClass('required');
			settingTeam.find('input[name="txtTeamDays"]').removeClass('required').removeClass('error');			
			
		}else{
			settingTeam.find('input[name="txtTeamStartDate"]').removeClass('required').removeClass('error');
			settingTeam.find('input[name="txtTeamEndDate"]').removeClass('required').removeClass('error');
			settingTeam.find('input[name="txtTeamDays"]').addClass('required');			
		}
		if (SmartWorks.GridLayout.validate(settingTeam.find('form.js_validation_required'),  settingTeam.find('.sw_error_message'))) {
			var forms = settingTeam.find('form');
			var courseId = forms.attr('courseId');
			var teamId = forms.attr('teamId');
			var paramsJson = {};
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
			var progressSpan = settingTeam.find('.js_progress_span');
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
					smartPop.showInfo(smartPop.INFO, "팀 정보가 성공적으로 수정되었습니다.", function(){
						document.location.href = "courseHome.sw?courseId=" + courseId;											
					});
				},
				error : function(e) {
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.ERROR, "팀 수정에 문제가 발생하였습니다. 관리자에게 문의하시기 바랍니다!");
				}
			});
		}
	};
</script>

<!-- Header Title -->
<div class="header_tit">
	<div class="tit_dep2 m0">
		<h2>코스 설정</h2>
		<div>코스에 참여하고 있는 회원으로 코스를 탈퇴하거나, 불만사항을 접수하고 개설한 팀을 관리하할 수 있습니다.</div>
	</div>
</div>
<!-- Header Title //-->
<!-- Contents Section -->
<div class="content_section js_course_setting_page" courseId="<%=courseId%>">
	<!-- 코스 탈퇴하기 -->
	<a href="" class="js_toggle_course_setting_btn"><div class="tit_cus1"></div></a>
	<!-- 설정 상세 -->
	<div class="panel_block w635 mt5 js_leave_course_detail" style="display: none">
		<div class="content">
			<h3>코스 탈퇴하기</h3>
			<div class="mt5">
				<div class="tb">[코스명 <%=course.getName()%>] 코스에서 탈퇴하시겠습니까?</div>
				코스에서 탈퇴하는 경우 현재 본인이 등록한 미션 수행 내용과 코스에 등록되어 있는 글, 정보 등이 모두 삭제됩니다. 그래도
				탈퇴를 원하신다면 탈퇴버튼을 클릭해 주십시오.
				<div class="t_red mb10">*욕설이나 비방하는 글은 삼가해 주세요.</div>
				<textarea name="txtaLeaveReason" cols="" rows="5" placeholder="멘티님이 코스를 탈퇴하는 이유를 적어주세요."></textarea>
			</div>
			<!-- Btn -->
			<div class="btn_space">
				<div style="clear: both; display: inline-block">
					<div class="btn_default_l mr10 js_leave_course_btn">
						<div class="btn_default_r">탈 퇴</div>
					</div>
					<div class="btn_default_l js_cancel_leave_btn">
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
	<a href="" class="js_toggle_course_setting_btn"><div class="tit_cus2 mt20"></div></a>
	<!-- 설정 상세 -->
	<div class="panel_block w635 mt5 js_defective_report_detail" style="display:none">
		<div class="content">
			<h3>코스 신고하기</h3>
			<div class="mt5">
				<div class="tb">[코스명 <%=course.getName() %>] 코스에서 탈퇴하시겠습니까?</div>
				코스 또는 개설자(멘토)에 대한 불만사항이나 잘못된 운영을 하고 있다면 알려주세요.
				<div class="t_red mb10">*욕설이나 비방하는 글은 삼가해 주세요.</div>
				<textarea name="txtaDefectiveReport" cols="" rows="5" placeholder="신고 내용을 적어 주세요"></textarea>
			</div>
			<!-- Btn -->
			<div class="btn_space">
				<div style="clear: both; display: inline-block">
					<div class="btn_default_l mr10 js_defective_report_btn">
						<div class="btn_default_r">신 고</div>
					</div>
					<div class="btn_default_l js_cancel_report_btn">
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
	<a href="" class="js_toggle_course_setting_btn"><div class="tit_cus3 mt20"></div></a>
	<!-- 설정 상세 -->
	<%
	Team myTeam = smartWorks.getMyTeamByCourse(courseId);
	if(!SmartUtil.isBlankObject(myTeam)){
	%>
		<div class="panel_block w635 mt5 js_create_team_detail" style="display:none">
			<form name="frmSetTeam" class="content js_validation_required" courseId="<%=courseId %>" teamId="<%=myTeam.getId() %>" >
				<table class="mt5">
					<tr>
						<td>
							<div class="form_label w101">멘토이름</div>
							<div class="form_value"><%=course.getLeader().getNickName() %></div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="form_label w101">팀 설명</div>
							<div class="form_value" style="width: 510px">
								<textarea name="txtaTeamDesc" cols="" rows=""><%=CommonUtil.toNotNull(myTeam.getDesc()) %></textarea>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="form_label w101">코스 기간</div>
							<div class="form_value">
								<input name="txtTeamDays" type="text" style="width: 100px" class="fieldline fl mr5 number tr" />
								<span class="fl mr15">일(DAYS)</span>
								<div class="t_refe pt2 fl">* 기간은 해당코스 기간을 넘을 수 없습니다</div>
								<div class="cb pt10">
									<label> <input name="chkUserDefineDays" type="checkbox" <%if(!SmartUtil.isBlankObject(myTeam.getStart())){ %>checked<%} %>/> 사용자 정의</label>
									<div class="fl js_team_start_date_field" startDate="<%=myTeam.getStart().toLocalDateSimpleString() %>" style="width:110px"></div>
									<div class="fl mr10">~</div> 
									<div class="fl js_team_end_date_field" endDate="<%=myTeam.getEnd().toLocalDateSimpleString() %>" style="width:110px"></div>
								</div>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="form_label w101">팀 공개 설정</div>
							<div class="form_value">
								<label><input name="chkTeamSecurity" <%if(myTeam.getAccessPolicy()==AccessPolicy.LEVEL_PUBLIC){ %>checked<%} %> type="checkbox" value="<%=AccessPolicy.LEVEL_PUBLIC %>" /> 공개</label>
								<label><input name="chkTeamSecurity" <%if(myTeam.getAccessPolicy()==AccessPolicy.LEVEL_PRIVATE){ %>checked<%} %> type="checkbox" value="<%=AccessPolicy.LEVEL_PRIVATE %>" /> 비공개</label>
								<div class="cb t_refe pt2">* 비공개 설정 시, 코스의 멘토 외에는 팀의 모든 활동이 노출되지 않습니다</div>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="form_label w101">구성 인원</div>
							<div class="form_value">
								<input name="txtTeamUsers" class="fieldline number required tr" type="text" value="<%=myTeam.getMaxMembers() %>" style="width: 80px" /> 명
							</div>
						</td>
					</tr>
				</table>
				<div class="sw_error_message tl" style="color: red"></div>
				<!-- Btn -->
				<div class="btn_space">
					<div style="clear: both; display: inline-block">
						<div class="btn_default_l mr10 js_modify_team_btn">
							<div class="btn_default_r">수 정</div>
						</div>
						<div class="btn_default_l js_remove_team_btn">
							<div class="btn_default_r">팀 삭제</div>
						</div>
					</div>
				</div>
				<!-- Btn //-->
	
			</form>
		</div>
		<!-- 설정 상세 -->
	<%
	}
	%>
	<!-- 팀 관리 //-->

</div>
<!-- Contents Section //-->
<script type="text/javascript">
	loadCreateTeamFields();
</script>
