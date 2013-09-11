<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.model.community.Department"%>
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
	String parentId = request.getParameter("parentId");
	Department department = (SmartUtil.isBlankObject(departId)) ? new Department() : smartWorks.getDepartmentById(departId);
	if(SmartUtil.isBlankObject(parentId) && !SmartUtil.isBlankObject(department.getParent())) parentId = department.getParent().getId();	
	Department parentDepart = (SmartUtil.isBlankObject(parentId)) ? new Department() : smartWorks.getDepartmentById(parentId);
%>
<script type="text/javascript">

	// 근무시간정책을 수정하기 버튼을 클릭하면, 
	// 모든정보를 JSON형식으로 Serialize해서 서버의 set_work_hour_policy.sw 서비스를 호출하여 수정한다.
	function submitForms(e) {
		var editDepartment = $('.js_edit_department_page');
		if (SmartWorks.GridLayout.validate(editDepartment.find('form.js_validation_required'), editDepartment.find('.js_profile_error_message'))) {
			var forms = editDepartment.find('form');
			var paramsJson = {};
			paramsJson['departmentId'] = editDepartment.attr('departId');
			paramsJson['parentId'] = editDepartment.attr('parentId');
			for(var i=0; i<forms.length; i++){
				var form = $(forms[i]);
				if(form.attr('name') === 'frmSmartForm'){
					paramsJson['formId'] = form.attr('formId');
					paramsJson['formName'] = form.attr('formName');
				}
				paramsJson[form.attr('name')] = mergeObjects(form.serializeObject(), SmartWorks.GridLayout.serializeObject(form));
			}
			console.log(JSON.stringify(paramsJson));
			
			var url = "set_department.sw";
			var departId = editDepartment.attr('departId'); 
			var confirmMessage = smartMessage.get("saveConfirmation");
			if(isEmpty(departId)){
				url = "create_department.sw";
				confirmMessage = smartMessage.get("createConfirmation");
			}
			smartPop.confirm( confirmMessage, function(){
				var progressSpan = editDepartment.find('.js_progress_span');
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
						smartPop.showInfo(smartPop.ERROR, isEmpty(departId) ? smartMessage.get('createDepartmentError') : smartMessage.get('setDepartmentError'));
					}
				});
			});
		}
	};

</script>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<div class="js_edit_department_page" departId="<%=departId %>" parentId="<%=parentId%>">

	<!-- 부서추가 -->
	<!-- 타이틀 영역 -->
	<div style="display:block">
		<div class="default_title_space">
			<%
			if(SmartUtil.isBlankObject(departId)){
			%>
				<div class="title"><fmt:message key="settings.title.add_department"/></div>
			<%
			}else{
			%>
				<div class="title"><fmt:message key="settings.title.modify_department"/></div>
			<%
			}
			%>
		</div>
		<!-- 타이틀 영역 //-->
		
		<form name="frmEditDepartment" class="boTb js_validation_required">
			<table class="mt10">
				<tbody>
					<tr>
						<%
						String parentFullpathName = (SmartUtil.isBlankObject(department.getParent())) ? "" : department.getParent().getFullpathName();
						String parentName = (SmartUtil.isBlankObject(department.getParent())) ? "" : department.getParent().getName();
						if(SmartUtil.isBlankObject(departId) && !SmartUtil.isBlankObject(parentDepart)){
							parentFullpathName = parentDepart.getFullpathName(); 
							parentName = parentDepart.getName(); 
						}
						%>
						<th width="20%" ><fmt:message key="settings.title.department.parent_name"/></th>
						<%
						if(!SmartUtil.isBlankObject(department)){
						%>
							<td width="80%" class="form_col js_type_departmentField" fieldId="parentDepartment" multiUsers="false">
								<div style="width:100%" class="form_value">
									<div class="icon_fb_space">
										<div class="fieldline community_names js_community_names sw_required">
											<span class="js_community_item user_select" comId="<%=CommonUtil.toNotNull(parentId)%>" comName="<%=CommonUtil.toNotNull(parentName)%>" title="<%=CommonUtil.toNotNull(parentFullpathName)%>"><%=CommonUtil.toNotNull(parentName) %>
												<a href="" class="js_remove_community"> x</a>
											</span>
											<input class="m0 js_auto_complete" style="width:100px" href="department_name.sw" type="text">
										</div>
										<div class="js_community_list srch_list_nowid" style="display:none"></div>
										<span class="js_community_popup"></span>
										<a href="" class="js_departpicker_button"><span class="icon_fb_depart"></span></a>
									</div>
								</div>
							</td>
 						<%
						}
						%>
<%-- 							<input name="txtParentName" readonly type="text" value="<%=parentName %>" />
							<input name="hdnParentId" type="hidden" value="<%=CommonUtil.toNotNull(parentId) %>" />
 --%>					</tr>
					<tr class="end">
						<th class="required_label"><fmt:message key="profile.title.department"/></th>
						<td><input name="txtDepartmentName" class="fieldline required" type="text" value="<%=CommonUtil.toNotNull(department.getName()) %>" /></td>
					</tr>
				</tbody>
			</table>
		</form>
	
		<!-- Btn -->
		<div class="tr mt8">
			<div class="fr ml10">
				<span class="btn_gray">
					<a href="" onclick='submitForms(); return false;'>
						<span class="txt_btn_start"></span>
						<%
						if(SmartUtil.isBlankObject(departId)){
						%>
							<span class="txt_btn_center"><fmt:message key="common.button.add_new"/></span>
						<%
						}else{
						%>
							<span class="txt_btn_center"><fmt:message key="common.button.modify"/></span>
						<%
						}
						%>
						<span class="txt_btn_end"></span>
					</a>
				</span>
				<%
				if(!SmartUtil.isBlankObject(departId)){
				%>
					<span class="btn_gray">
						<a href="" class="js_delete_department">
							<span class="txt_btn_start"></span>
								<span class="txt_btn_center"><fmt:message key="common.button.delete"/></span>
							<span class="txt_btn_end"></span>
						</a>
					</span>
				<%
				}
				%>
			</div>	
			<!--  실행시 표시되는 프로그래스아이콘을 표시할 공간 -->
			<span class="fr form_space js_progress_span"></span>
			<!-- 실행시 데이터 유효성 검사이상시 에러메시지를 표시할 공간 -->
			<div class="form_space sw_error_message js_profile_error_message" style="text-align:right; color: red; line-height:20px"></div>
		</div>
		<!-- Btn //-->
	</div>
	<!-- 부서추가 //-->
</div>									
