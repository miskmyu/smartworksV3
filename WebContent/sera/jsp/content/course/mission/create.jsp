<%@page import="net.smartworks.model.instance.Instance"%>
<%@page import="java.util.Calendar"%>
<%@page import="net.smartworks.model.sera.info.MissionInstanceInfo"%>
<%@page import="net.smartworks.model.sera.Course"%>
<%@page import="net.smartworks.model.instance.info.EventInstanceInfo"%>
<%@page import="java.util.Date"%>
<%@page import="net.smartworks.util.LocalDate"%>
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
	String startDateStr = request.getParameter("startDate");
	if(!SmartUtil.isBlankObject(startDateStr)){
		LocalDate startDate = LocalDate.convertLocalDateStringToLocalDate(startDateStr);
		if(startDate.getTime()<course.getOpenDate().getTime() || startDate.getTime()>course.getCloseDate().getTime())
			startDateStr = (new LocalDate()).toLocalDateSimpleString();
	}
	
%>
<script type="text/javascript">

	// 개인정보프로파일 수정하기 버튼을 클릭하면, 
	// 모든정보를 JSON형식으로 Serialize해서 서버의 create_new_mission.sw 서비스를 호출하여 수정한다.
	function submitForms(e) {
		var createMission = $('.js_create_mission_page');
		if (SmartWorks.GridLayout.validate(createMission.find('form.js_validation_required'),  createMission.find('.sw_error_message'))) {
			var forms = createMission.find('form');
			var courseOpenDate = new Date(createMission.attr('courseOpenDate'));
			var courseCloseDate = new Date(createMission.attr('courseCloseDate'));
			var openDate = new Date(forms.find('input[name="txtMissionOpenDate"]').attr('value'))
			var closeDate = new Date(forms.find('input[name="txtMissionCloseDate"]').attr('value'))
			if(openDate.getTime()>closeDate.getTime()){
				smartPop.showInfo(smartPop.ERROR, "미션기간의 시작일자가 종료일자보다 이후입니다. 시각일자를 종료일자보다 이전으로 수정바랍니다!");
				return false;
			}else if(openDate.getTime()<courseOpenDate.getTime() || closeDate.getTime()>courseCloseDate.getTime()){
				smartPop.showInfo(smartPop.ERROR, "미션기간은 코스기간 내에서만 설정가능합니다. 코스기간 내 일자로 수정바랍니다!");
				return false;
			}
			var paramsJson = {};
			paramsJson['courseId'] = createMission.attr('courseId');
			for(var i=0; i<forms.length; i++){
				var form = $(forms[i]);
				if(form.attr('name') === 'frmSmartForm'){
					paramsJson['formId'] = form.attr('formId');
					paramsJson['formName'] = form.attr('formName');
				}
				paramsJson[form.attr('name')] = mergeObjects(form.serializeObject(), SmartWorks.GridLayout.serializeObject(form));
			}
			console.log(JSON.stringify(paramsJson));
			var progressSpan = createMission.find('.js_progress_span');
			smartPop.progressCont(progressSpan);
			var url = "create_new_mission.sw";
			$.ajax({
				url : url,
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					// 사용자정보 수정이 정상적으로 완료되었으면, 현재 페이지에 그대로 있는다.
					smartPop.showInfo(smartPop.INFO, '미션이 정상적으로 생성되었습니다!', function(){
						$('.js_course_mission').click();
						smartPop.closeProgress();						
					});
				},
				error : function(e) {
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.ERROR, "미션을 생성하는데 문제가 발생하였습니다. 관리자에게 문의하시기 바랍니다!");
				}
			});
		}
	};
</script>

<div class="js_create_mission_page" courseId="<%=courseId%>" courseOpenDate="<%=course.getOpenDate().toLocalDateSimpleString()%>" courseCloseDate="<%=course.getCloseDate().toLocalDateSimpleString()%>">
	<!-- Header Title -->
	<div class="header_tit">
		<div class="tit_dep2 m0">
			<h2>미션 등록</h2>
			<div>
			    * 미션을 등록하려면 <span class="t_red">미션 제목</span>을 입력하시고 <span class="t_red">미션 수행 기간</span>을 설정하여 주십시오.<br />
	    		* 미션을 등록하게 되면, 달력에서 <span class="t_red">미션 수행 마감일</span>에 내용이 표시됩니다.<br />
	    		* 등록된 미션을 수정하기 위해서는 이미 등록된 미션을 클릭하여 수정하십시오.
			</div>
		</div>
	</div>
	<!-- Input Form //-->
	<form name="frmCreateMission" class="form_layout js_validation_required">
		<table class="js_create_mission_table" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
					<div class="form_label">미션<%=course.getLastMissionIndex()+2 %> 제목</div>
					<div class="form_value w560">
						<input name="txtMissionName" type="text" class="fieldline fl required">
					</div>
				</td>
			</tr>
			<tr>
				<td><div class="form_label">미션 기간</div>
					<div class="form_value">
						<div class="fl js_mission_open_date_field" openDate="<%=startDateStr%>"></div>
						<div class="fl mr5" style="line-height: 20px"> ~ </div> 
						<div class="fl js_mission_close_date_field"></div>
					</div>
				</td>
			</tr>
			<tr>
				<td><div class="form_label">선행 미션*</div>
					<div class="form_value">
						<select name="selPrevMission">
							<option value="">없 음</option>
							<%
							MissionInstanceInfo[] missions = course.getMissions();
							if (missions != null) {
								for(int i=0; i<missions.length; i++){
									MissionInstanceInfo mission = missions[i];
							%>
								<option value="<%=mission.getId() %>">미션<%=mission.getIndex()+1 %> <%=mission.getSubject() %></option>
							<%
								}
							}
							%>
						</select>
					</div>
					<div class="cb">
						<div class="t_refe">* 선택사항 : 선행미션을 선택하면, 선택한 미션을 수행해야 해당 미션을 수행할 수 있습니다.</div>
					</div>
				</td>
			</tr>
			<tr>
				<td><div class="form_label">미션 내용</div>
					<div class="form_value w560">
						<div class="fl js_mission_content_field"></div>
					</div>
				</td>
			</tr>
			<tr>
				<td><div class="form_label">첨부 파일</div>
					<div class="form_value">
						<div class="fl js_mission_attachment_field" width="490px"></div>
					</div>
				</td>
			</tr>
		</table>
	</form>
	
	<div class="sw_error_message tl" style="color: red"></div>
	
	<!-- Btn -->
	<div class="btn_space">
		<div style="clear: both; display: inline-block">
			<div href="" class="btn_blu_l mr10 js_create_mission_btn">
				<div class="btn_blu_r">미션 등록</div>
			</div>
		
			<div href="" class="btn_red_l js_cancel_create_mission_btn">
				<div class="btn_red_r">취 소</div>
			</div>
		</div>
	</div>
	<!-- Btn //-->
</div>

<script type="text/javascript">
	loadCreateMissionFields();
</script>
