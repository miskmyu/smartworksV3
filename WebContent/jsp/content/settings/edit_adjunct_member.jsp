<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.model.KeyMap"%>
<%@page import="net.smartworks.model.community.Department"%>
<%@page import="net.smartworks.util.LocaleInfo"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 현재사용자 정보도 가져온다..
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();

	String departId = request.getParameter("departId");
	Department department = (SmartUtil.isBlankObject(departId)) ? new Department() : smartWorks.getDepartmentById(departId);

	// 사용가능한 타임존들을 가져와서, 타임존 선택박스에 리스트로 보여준다.
	KeyMap[] timeZoneNames = LocalDate.getAvailableTimeZoneNames(cUser.getLocale());

%>
<script type="text/javascript">

	// 근무시간정책을 수정하기 버튼을 클릭하면, 
	// 모든정보를 JSON형식으로 Serialize해서 서버의 set_work_hour_policy.sw 서비스를 호출하여 수정한다.
	function submitForms(e) {
		var editAdjunctMember = $('.js_edit_adjunct_member_page');
		if (SmartWorks.GridLayout.validate(editAdjunctMember.find('form.js_validation_required'), editAdjunctMember.find('.js_profile_error_message'))) {
			var forms = editAdjunctMember.find('form');
			var paramsJson = {};
			for(var i=0; i<forms.length; i++){
				var form = $(forms[i]);
				if(form.attr('name') === 'frmSmartForm'){
					paramsJson['formId'] = form.attr('formId');
					paramsJson['formName'] = form.attr('formName');
				}
				paramsJson[form.attr('name')] = mergeObjects(form.serializeObject(), SmartWorks.GridLayout.serializeObject(form));
			}
			console.log(JSON.stringify(paramsJson));
			
			var url = "add_adjunct_member.sw";
			smartPop.confirm( smartMessage.get("addAdjunctConfirmation"), function(){
				var progressSpan = editAdjunctMember.find('.js_progress_span');
				smartPop.progressCont(progressSpan);
				$.ajax({
					url : url,
					contentType : 'application/json',
					type : 'POST',
					data : JSON.stringify(paramsJson),
					success : function(data, status, jqXHR) {
						// 사용자정보 수정이 정상적으로 완료되었으면, 현재 페이지에 그대로 있는다.
						document.location.href = "organization_management.sw";					
						smartPop.closeProgress();
					},
					error : function(e) {
						smartPop.closeProgress();
						smartPop.showInfo(smartPop.ERROR, smartMessage.get('addAdjunctError'));
					}
				});
			});
		}
	};

</script>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<div class="js_edit_adjunct_member_page" departId="<%=departId%>">

	<!-- 사용자추가 -->
	<!-- 타이틀 영역 -->
	<div style="display:block">
		<div class="default_title_space">
			<div class="title"><fmt:message key="settings.title.add_adjunct_user"/></div>
		</div>
		<!-- 타이틀 영역 //-->
		
		<form name="frmAdjunctMember" class="boTb js_validation_required">
			<table class="mt10">
				<tbody>
					<tr>
						<th width="22%" ><fmt:message key="profile.title.department"/></th>
						<%
						if(!SmartUtil.isBlankObject(departId)){
						%>
							<td width="78%"><%=CommonUtil.toNotNull(department.getFullpathName()) %></td>
							<input name="hdnDepartmentId" type="hidden" value="<%=CommonUtil.toNotNull(department.getId())%>">
						<%
						}
						%>
					</tr>
					<tr>
						<th class="required_label"><fmt:message key="profile.title.user_name"/></th>
						<td class="form_col js_type_userField" fieldid="txtAdjunctUser" colspan="1" multiusers="false">
							<div class="form_value" style="width:100%">
								<div class="icon_fb_space">
									<div class="fieldline community_names js_community_names sw_required">
										<input class="m0 js_auto_complete" style="width:100px" href="user_name.sw" type="text">
									</div>
									<div class="js_community_list srch_list_nowid" style="display:none"></div>
									<span class="js_community_popup"></span>
									<a href="" class="js_userpicker_button"><span class="icon_fb_user"></span></a>
								</div>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
		
		<!-- Btn -->
		<div class="glo_btn_space">
			<!-- 실행시 데이터 유효성 검사이상시 에러메시지를 표시할 공간 -->
			<div class="fr ml10">
				<span class="btn_gray">
					<a href="" onclick='submitForms(); return false;'>
						<span class="txt_btn_start"></span>
						<span class="txt_btn_center"><fmt:message key="common.button.add_new"/></span>
						<span class="txt_btn_end"></span>
					</a>
				</span>
			</div>	
			<!--  실행시 표시되는 프로그래스아이콘을 표시할 공간 -->
			<span class="fr form_space js_progress_span"></span>
			<div class="form_space sw_error_message js_profile_error_message" style="text-align:right; color: red; line-height:20px"></div>
		</div>
		<!-- Btn //-->
	</div>
	<!-- 사용자추가 //-->
</div>										
