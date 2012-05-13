<%@page import="net.smartworks.model.sera.Mentor"%>
<%@page import="net.smartworks.model.security.AccessPolicy"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
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
	Course course = (Course)session.getAttribute("course");
	String mentorId = course.getLeader().getId();
	Mentor mentor = smartWorks.getMentorById(mentorId);

%>
<script type="text/javascript">

	// 개인정보프로파일 수정하기 버튼을 클릭하면, 
	// 모든정보를 JSON형식으로 Serialize해서 서버의 update_my_profile.sw 서비스를 호출하여 수정한다.
	function submitForms(e) {
		var settingMentor = $('.js_setting_mentor_page');
		if (SmartWorks.GridLayout.validate(settingMentor.find('form.js_validation_required'),  settingMentor.find('.sw_error_message'))) {
			var forms = settingMentor.find('form');
			var courseId = settingMentor.attr('courseId');
			var mentorId = settingMentor.attr('mentorId');
			var paramsJson = {};
			paramsJson['mentorId'] = mentorId;
			for(var i=0; i<forms.length; i++){
				var form = $(forms[i]);
				if(form.attr('name') === 'frmSmartForm'){
					paramsJson['formId'] = form.attr('formId');
					paramsJson['formName'] = form.attr('formName');
				}
				paramsJson[form.attr('name')] = mergeObjects(form.serializeObject(), SmartWorks.GridLayout.serializeObject(form));
			}
			console.log(JSON.stringify(paramsJson));
			var progressSpan = settingMentor.find('.js_progress_span');
			smartPop.progressCont(progressSpan);
			var url = "set_mentor_profile.sw";
			$.ajax({
				url : url,
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					// 사용자정보 수정이 정상적으로 완료되었으면, 현재 페이지에 그대로 있는다.
					smartPop.showInfo(smartPop.INFO, "멘토정보가 정상적으로 수정되었습니다!", function(){
						document.location.href = "courseHome.sw?courseId=" + courseId;						
						smartPop.closeProgress();
					});
				},
				error : function(e) {
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.ERROR, "멘토정보를 수정하는 중에 오류가 발생하였습니다. 관리자에게 문의하시기 바랍니다!");
				}
			});
		}
	};
</script>

<div class="js_setting_mentor_page" courseId="<%=course.getId()%>" mentorId="<%=mentorId%>">
	<!-- Header Title -->
	<div class="header_tit">
		<div class="tit_dep2 m0">
			<h2>멘토프로필 관리</h2>
			<div>코스 개설자(멘토)는 멘토에 대한 정보를 수정하고 관리할 수 있습니다.</div>
		</div>
	</div>
	<!-- Header Title //-->
	<!-- Input Form -->
	<!-- Input Form -->
	<form name="frmSetCourseMentor" class="form_layout js_validation_required">
		<table class="js_mentor_profile_table" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
					<div class="form_label">멘토이름</div>
					<div class="form_value"><img class="profile_size_s" style="line-height:20px" src="<%=mentor.getMinPicture()%>"/><%=mentor.getNickName()%></div>
					<input name="txtCourseMentor" type="hidden" value="<%=mentor.getId() %>"/>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label">학 력</div>
					<div class="form_value w570">
						<textarea name="txtaMentorEducations" class="fieldline required" name="textarea" rows="3"><%=CommonUtil.toNotNull(mentor.getEducations()) %></textarea>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label">경 력</div>
					<div class="form_value w570">
						<textarea name="txtaMentorWorks" class="fieldline required" name="textarea" rows="3"><%=CommonUtil.toNotNull(mentor.getWorks()) %></textarea>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label">세라활동 *</div>
					<div class="form_value" style="width: 580px">
						<div class="fl" style="width: 285px">
							<textarea name="txtaMentorHistory" class="fieldline" name="" rows="3" value="" placeholder="멘토활동"><%=CommonUtil.toNotNull(mentor.getMentorHistory()) %></textarea>
						</div>
						<div class="fr" style="width: 285px">
							<textarea name="txtaMenteeHistory" class="fieldline" name="" rows="3" placeholder="멘티활동"><%=CommonUtil.toNotNull(mentor.getMenteeHistory()) %></textarea>
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label">강의활동 *</div>
					<div class="form_value w570">
						<textarea name="txtaMentorLectures" class="fieldline" name="textarea" rows="3"><%=CommonUtil.toNotNull(mentor.getLectures()) %></textarea>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label">수상경력*</div>
					<div class="form_value w570">
						<textarea name="txtaMentorAwards" class="fieldline" name="textarea" rows="3"><%=CommonUtil.toNotNull(mentor.getAwards()) %></textarea>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label">기타활동*</div>
					<div class="form_value w570">
						<textarea name="txtaMentorEtc" class="fieldline" name="textarea" rows="3"><%=CommonUtil.toNotNull(mentor.getEtc()) %></textarea>
					</div>
				</td>
			</tr>
		</table>
	</form>
	<div class="sw_error_message tl" style="color: red"></div>
	<!-- Btn -->
	<div class="btn_space">
		<div style="clear: both; display: inline-block">
			<div class="btn_blu_l mr10 js_complete_modify_mentor_btn">
				<div class="btn_blu_r">멘토프로필 수정</div>
			</div>
			<div class="btn_blu_l js_cancel_modify_mentor_btn">
				<div class="btn_blu_r">취소</div>
			</div>
		</div>
	</div>
	<!-- Btn //-->
	<!-- Input Form //-->
</div>