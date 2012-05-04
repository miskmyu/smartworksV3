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
	Course course = smartWorks.getCourseById(courseId);

%>
<script type="text/javascript">

	// 개인정보프로파일 수정하기 버튼을 클릭하면, 
	// 모든정보를 JSON형식으로 Serialize해서 서버의 update_my_profile.sw 서비스를 호출하여 수정한다.
	function submitForms(e) {
		var settingProfile = $('.js_setting_profile_page');
		if (SmartWorks.GridLayout.validate(settingProfile.find('form.js_validation_required'),  settingProfile.find('.sw_error_message'))) {
			var forms = settingProfile.find('form');
			var courseId = settingProfile.attr('courseId');
			var paramsJson = {};
			paramsJson['courseId'] = courseId;
			for(var i=0; i<forms.length; i++){
				var form = $(forms[i]);
				if(form.attr('name') === 'frmSmartForm'){
					paramsJson['formId'] = form.attr('formId');
					paramsJson['formName'] = form.attr('formName');
				}
				paramsJson[form.attr('name')] = mergeObjects(form.serializeObject(), SmartWorks.GridLayout.serializeObject(form));
			}
			console.log(JSON.stringify(paramsJson));
			var progressSpan = settingProfile.find('.js_progress_span');
			smartPop.progressCont(progressSpan);
			var url = "set_course_profile.sw";
			$.ajax({
				url : url,
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					// 사용자정보 수정이 정상적으로 완료되었으면, 현재 페이지에 그대로 있는다.
					smartPop.closeProgress();
					document.location.href = data.href;
				},
				error : function(e) {
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.ERROR, "코스정보를 수정하는 중에 오류가 발생하였습니다. 관리자에게 문의하시기 바랍니다!");
				}
			});
		}
	};
</script>

<div class="js_setting_profile_page" courseId="<%=course.getId()%>">
	<!-- Header Title -->
	<div class="header_tit">
		<div class="tit_dep2 m0">
			<h2>코스관리</h2>
			<div>코스 개설자(멘토)는 코스에 대한 정보를 수정하고 관리할 수 있습니다.</div>
		</div>
	</div>
	<!-- Header Title //-->
	<!-- Input Form -->
	<!-- Input Form -->
	<form name="frmSetCourseProfile" class="form_layout js_validation_required">
		<table class="js_create_course_table" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
					<div class="form_label w110">코스 제목</div>
					<div class="form_value"><%=course.getName() %></div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label w110">코스 목적</div>
					<div class="form_value">
						<input name="txtCourseObject" type="text" class="fieldline fl required" style="width: 490px" value="<%=course.getObject()%>">
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label w110">상세설명</div>
					<div class="form_value">
						<textarea name="txtaCourseDesc" class="fieldline fl" name="textarea" rows="7" style="width: 490px"><%=CommonUtil.toNotNull(course.getDesc()) %></textarea>
						<div class="cb t_refe pt2">* 선택입력란입니다</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label w110">이미지</div>
					<div class="form_value">
						<div class="js_course_profile_field js_auto_load_course_profile" imgSrc="<%=course.getOrgPicture()%>"></div>
						<div class="cb t_refe pt2">* 이미지를 등록하지 않은 경우 SERA에서 제공하는 기본 이미지가 제공되어 보여집니다</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label w110">키워드</div>
					<div class="form_value">
						<input name="txtCourseKeywords" type="text" style="width: 490px" class="fieldline fl required" value="<%=CommonUtil.toNotNull(course.getKeywordsAsCommaString())%>"/>
						<div class="cb t_refe pt2">* 코스의 이해를 도울수 있는 키워드 입력 (코스 검색 또는 추천코스에 사용 됨), 2개이상은 콤마(,)로 구분</div>
					</div>
				</td>
			</tr>
			<tr>
				<td><div class="form_label w110">코스 기간</div>
					<div class="form_value">
						<input name="txtCourseDays" type="text" style="width: 100px" class="fieldline fl tr number" />
						<div class="fl mt2 ml2"> 일(Days)</div>
						<div class="t_refe pt2 fl"> * 코스 기간은 6개월 이내로 설정해 주세요</div>
						
						<div class="cb pt10">
							<input class="fl pt2" name="chkUserDefineDays" type="checkbox" <%if(!SmartUtil.isBlankObject(course.getOpenDate()) && !SmartUtil.isBlankObject(course.getCloseDate())){ %>checked<%} %>/>
							<label class="fl">사용자 정의</label>
							<div class="fl js_course_start_date_field" <%if(!SmartUtil.isBlankObject(course.getOpenDate())){ %>openDate="<%=course.getOpenDate().toLocalDateSimpleString()%>"<%} %>></div>
							<div class="fl mr5" style="line-height: 20px"> ~ </div> 
							<div class="fl js_course_end_date_field" <%if(!SmartUtil.isBlankObject(course.getCloseDate())){ %>closeDate="<%=course.getCloseDate().toLocalDateSimpleString()%>"<%} %>></div>
						</div>
						<div class="cb t_refe pt2">* 정해진 날짜를 통해 코스를 진행해야 하는 경우 코스 상세기간 입력으로 해당 날짜를 입력해주세요</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label w110">공개 설정</div>
					<div class="form_value">
						<input name="chkCourseSecurity" type="radio" <%if(course.isPublic()){ %>checked<%} %> value="<%=AccessPolicy.LEVEL_PUBLIC %>" /><label>공개</label>
						<input name="chkCourseSecurity" type="radio" <%if(!course.isPublic()){ %>checked<%} %> value="<%=AccessPolicy.LEVEL_PRIVATE %>" />비공개
						<div class="cb t_refe pt2">* 비공개 코스 설정 시 코스의 제반 내용이 노출되지 않습니다</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label w110">멘티 인원 제한</div>
					<div class="form_value">
						<input name="chkCourseUsers" type="radio" <%if(course.getMaxMentees() == -1) {%>checked<%} %> value="unlimited" /><label>무제한</label> 
						<input name="chkCourseUsers" type="radio" <%if(course.getMaxMentees() > 0) {%>checked<%} %> value="userInput" />직접입력 
						<input name="txtCourseUsers" class="fieldline number tr" type="text" style="width: 80px" value="<%if(course.getMaxMentees()>0){ %><%=course.getMaxMentees()%><%}%>"/> 명
						<div class="cb t_refe pt2">* 코스 인원 제한 시 정해진 인원만 코스를 이용할 수 있습니다</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label w110">멘티 가입 승인</div>
					<div class="form_value">
						<input name="chkJoinApproval" type="radio" <%if(course.isAutoApproval()){ %>checked<%} %> value="autoApporval" /><label>자동승인</label>
						<input name="chkJoinApproval" type="radio" <%if(!course.isAutoApproval()){ %>checked<%} %> value="mentorApproval" />멘토승인
						<div class="cb t_refe pt2">* 회원이 코스를 참여하는 데 있어 승인절차를 선택할 수 있습니다</div>
					</div>
				</td>
			</tr>
		</table>
	</form>
	<div class="sw_error_message tl" style="color: red"></div>
	<!-- Btn -->
	<div class="btn_space">
		<div style="clear: both; display: inline-block">
			<div class="btn_blu_l mr10 js_modify_course_btn">
				<div class="btn_blu_r">코스 수정</div>
			</div>
			<div class="btn_blu_l mr10">
				<div class="btn_blu_r">멘토프로필 수정</div>
			</div>
			<div class="btn_red_l js_remove_course_btn">
				<div class="btn_red_r">코스 삭제</div>
			</div>
		</div>
	</div>
	<!-- Btn //-->
	<!-- Input Form //-->
</div>

<script type="text/javascript">
	loadCreateCourseFields();
</script>