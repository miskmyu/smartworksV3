<%@page import="net.smartworks.util.SmartMessage"%>
<%@page import="net.smartworks.model.community.info.DepartmentInfo"%>
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

	String userId = request.getParameter("userId");
	String departId = request.getParameter("departId");
	String parentId = request.getParameter("parentId");
	User user = (SmartUtil.isBlankObject(userId)) ? new User() : smartWorks.getUserById(userId);
	Department department = (!SmartUtil.isBlankObject(departId)) ? smartWorks.getDepartmentById(departId) : ((!SmartUtil.isBlankObject(parentId)) ? smartWorks.getDepartmentById(parentId) : smartWorks.getDepartmentById(cUser.getCompanyId()));

	// 사용가능한 타임존들을 가져와서, 타임존 선택박스에 리스트로 보여준다.
	KeyMap[] timeZoneNames = LocalDate.getAvailableTimeZoneNames(cUser.getLocale());

	
%>
<script type="text/javascript">

	// 근무시간정책을 수정하기 버튼을 클릭하면, 
	// 모든정보를 JSON형식으로 Serialize해서 서버의 set_work_hour_policy.sw 서비스를 호출하여 수정한다.
	function submitForms(e) {
		var editMember = $('.js_edit_member_page');
		if(!editMember.find('input[name="txtMemberId"]').hasClass('sw_dup_checked')){
			smartPop.showInfo(smartPop.ERROR, smartMessage.get('duplicationCheckError'));
			return;
		}
		if (SmartWorks.GridLayout.validate(editMember.find('form.js_validation_required'), editMember.find('.js_profile_error_message'))) {
			var forms = editMember.find('form');
			var paramsJson = {};
			paramsJson['userId'] = editMember.attr('userId');
			paramsJson['departmentId'] = editMember.attr('departid');
			paramsJson['parentId'] = editMember.attr('parentid');
			for(var i=0; i<forms.length; i++){
				var form = $(forms[i]);
				if(form.attr('name') === 'frmSmartForm'){
					paramsJson['formId'] = form.attr('formId');
					paramsJson['formName'] = form.attr('formName');
				}
				paramsJson[form.attr('name')] = mergeObjects(form.serializeObject(), SmartWorks.GridLayout.serializeObject(form));
			}
			console.log(JSON.stringify(paramsJson));
			
			var url = "set_member.sw";
			var userId = editMember.attr('userId'); 
			var confirmMessage = smartMessage.get("saveConfirmation");
			if(isEmpty(userId)){
				url = "create_member.sw";
				confirmMessage = smartMessage.get("createConfirmation");
			}
			smartPop.confirm( confirmMessage, function(){
				var progressSpan = editMember.find('.js_progress_span');
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
						smartPop.showInfo(smartPop.ERROR, isEmpty(userId) ? smartMessage.get('createMemberError') : smartMessage.get('setMemberError'));
					}
				});
			});
		}
	};

</script>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<div class="js_edit_member_page" userId="<%=userId%>" departId="<%=departId%>" parentId="<%=parentId%>">

	<!-- 사용자추가 -->
	<!-- 타이틀 영역 -->
	<div style="display:block">
		<div class="default_title_space">
			<%
			if(SmartUtil.isBlankObject(userId)){
			%>
				<div class="title"><fmt:message key="settings.title.add_user"/></div>
			<%
			}else{
			%>
				<div class="title"><fmt:message key="settings.title.modify_user"/></div>
			<%
			}
			%>
		</div>
		<!-- 타이틀 영역 //-->
		
		<form name="frmEditMember" class="boTb js_validation_required">
			<table class="mt10">
				<tbody>
					<tr>
						<th width="22%" ><fmt:message key="profile.title.department"/></th>
						<%
						if(!SmartUtil.isBlankObject(department)){
						%>
							<td width="78%" class="form_col js_type_departmentField" fieldId="memberDepartment" multiUsers="false">
								<div style="width:100%" class="form_value">
									<div class="icon_fb_space">
										<div class="fieldline community_names js_community_names sw_required">
											<span class="js_community_item user_select" comId="<%=department.getId()%>" comName="<%=department.getName()%>" title="<%=CommonUtil.toNotNull(department.getFullpathName())%>"><%=CommonUtil.toNotNull(department.getName()) %>
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
					</tr>
					<tr>
						<th class="required_label"><fmt:message key="profile.title.user_name"/></th>
						<td><input name="txtMemberName" class="fieldline required" type="text" value="<%=CommonUtil.toNotNull(user.getName()) %>" /></td>
					</tr>
					<tr>
						<th class="required_label"><fmt:message key="profile.title.user_id"/></th>
						<td>
						
						<div class="btn_fb_space5">
						
								<input name="txtMemberId" <%if(!SmartUtil.isBlankObject(userId)){ %>class="sw_dup_checked fieldline required email" readonly<%}else{ %>class="fieldline required email" <%} %> type="text" value="<%=CommonUtil.toNotNull(user.getId()) %>" />
								<!-- 유저 아이디가 없을 경우 버튼 활성화 -->
								<%if(smartWorks.getUserById(userId) == null){%>
								<div class="icon_btn_start icon_pos_right">
								<a class="icon_btn_tail js_check_id_duplication" href="" <%if(!SmartUtil.isBlankObject(userId)){%>style="display:none"<%} %>><fmt:message key="settings.button.duplication_check"/></a>
								<a class="icon_btn_tail js_change_id" href="" <%if(SmartUtil.isBlankObject(userId)){%>style="display:none"<%} %>><fmt:message key="settings.button.change_id"/></a>
								</div>
								<%} %>
						</div>
						
						<div class="t_s11 fl"><fmt:message key="settings.sentence.use_email"/></div>
						
						</td>
					</tr>
					<tr>
						<th class="required_label"><fmt:message key="profile.title.password"/></th>
						<td><input name="pasMemberPassword" class="fieldline required" type="password" value="<%=CommonUtil.toNotNull(user.getPassword()) %>" /></td>
					</tr>
					<tr>
						<th class="required_label"><fmt:message key="profile.title.password_confirm"/></th>
						<td><input name="pasMemberPasswordConfirm" class="fieldline required" type="password" value="<%=CommonUtil.toNotNull(user.getPassword()) %>" /></td>
					</tr>
					<tr>
						<th><fmt:message key="profile.title.employee_id"/></th>
						<td><input name="txtMemberEmployeeId" class="fieldline" type="text" value="<%=CommonUtil.toNotNull(user.getEmployeeId()) %>" /></td>
					</tr>
					<tr>
						<th><fmt:message key="profile.title.dateadmitted"/></th>
						<td>
							<%
							String hireYear = "", hireMonth = "", hireDay = "";
							if(!SmartUtil.isBlankObject(user.getHireDate())){
								hireYear = "" + user.getHireDate().getYear();
								hireMonth = "" + (user.getHireDate().getMonth()+1);
								hireDay = "" + user.getHireDate().getDateOnly();
							}
							%>
							<input style="width:40px" maxlength="4" name="txtMemberHireYear" class="fieldline tc" type="text" value="<%=hireYear %>" /><fmt:message key="common.title.year"/>
							<input style="width:20px" maxlength="2" name="txtMemberHireMonth" class="fieldline tc" type="text" value="<%=hireMonth %>" /><fmt:message key="common.title.month"/>
							<input style="width:20px" maxlength="2" name="txtMemberHireDay" class="fieldline tc" type="text" value="<%=hireDay %>" /><fmt:message key="common.title.day"/>
						</td>
					</tr>
					<tr>
						<th><fmt:message key="profile.title.position"/></th>
						<td><input name="txtMemberPosition" class="fieldline" type="text" value="<%=CommonUtil.toNotNull(user.getPosition()) %>" /></td>
					</tr>
					<tr>
						<th><fmt:message key="profile.title.role"/></th>
						<td>
							<select name="selMemberRole">
								<option <%if(user.getRole()==User.USER_ROLE_LEADER){ %>selected<%} %> value="<%=User.USER_ROLE_LEADER %>"><fmt:message key="department.role.head"/></option>
								<option <%if(user.getRole()==User.USER_ROLE_MEMBER){ %>selected<%} %> value="<%=User.USER_ROLE_MEMBER %>"><fmt:message key="department.role.member"/></option>
							</select>
						</td>
					</tr>
					<tr>
						<th><fmt:message key="profile.title.user_level"/></th>
						<td><select name="selMemberUserLevel">
							<option <%if(user.getUserLevel()==User.USER_LEVEL_INTERNAL_USER){ %>selected<%} %> value="<%=User.USER_LEVEL_INTERNAL_USER%>"><fmt:message key="organization.user_level.internal_user"/></option>
							<option <%if(user.getUserLevel()==User.USER_LEVEL_SYSMANAGER){ %>selected<%} %> value="<%=User.USER_LEVEL_SYSMANAGER%>"><fmt:message key="organization.user_level.system_user"/></option>
							<option <%if(user.getUserLevel()==User.USER_LEVEL_AMINISTRATOR){ %>selected<%} %> value="<%=User.USER_LEVEL_AMINISTRATOR%>"><fmt:message key="organization.user_level.administrator"/></option>
							<option <%if(user.getUserLevel()==User.USER_LEVEL_EXTERNAL_USER){ %>selected<%} %> value="<%=User.USER_LEVEL_EXTERNAL_USER%>"><fmt:message key="organization.user_level.external_user"/></option>
						</select></td>
					</tr>
					<tr>
						<th><fmt:message key="profile.title.birthday"/></th>
						<td>
							<%
							String birthYear = "", birthMonth = "", birthDay = "";
							if(!SmartUtil.isBlankObject(user.getBirthday())){
								birthYear = "" + user.getBirthday().getYear();
								birthMonth = "" + (user.getBirthday().getMonth()+1);
								birthDay = "" + user.getBirthday().getDateOnly();
							}
							%>
							<input style="width:40px" maxlength="4" name="txtMemberBirthYear" class="fieldline tc" type="text" value="<%=birthYear %>" /><fmt:message key="common.title.year"/>
							<input style="width:20px" maxlength="2" name="txtMemberBirthMonth" class="fieldline tc" type="text" value="<%=birthMonth %>" /><fmt:message key="common.title.month"/>
							<input style="width:20px" maxlength="2" name="txtMemberBirthDay" class="fieldline tc" type="text" value="<%=birthDay %>" /><fmt:message key="common.title.day"/>
							<select name="selMemberLunarBirthday">
								<option <%if(!user.isLunarBirthday()){ %>selected<%} %> value="false"><fmt:message key="common.title.solar_date"/></option>
								<option <%if(user.isLunarBirthday()){ %>selected<%} %> value="true"><fmt:message key="common.title.lunar_date"/></option>
							</select>
						</td>
					</tr>
					<tr>
						<th class="required_label"><fmt:message key="profile.title.locale" /></th>
						<td>
							<select name="selMemberLocale">
								<%
								for (String locale : LocaleInfo.supportingLocales) {
									String strKey = "common.title.locale." + locale;
								%>
									<option value="<%=locale%>" <%if (user.getLocale().equals(locale)) {%> selected <%}%>><fmt:message key="<%=strKey%>" /></option>
								<%
								}
								%>
							</select>
						</td>
					</tr>
					<tr>
						<th class="required_label"><fmt:message key="profile.title.timezone" /></th>
						<td>
							<select name="selMemberTimeZone">
								<%
								for (KeyMap timeZoneName : timeZoneNames) {
								%>
									<option value="<%=timeZoneName.getId()%>" <%if (user.getTimeZone().equals(timeZoneName.getId())) {%> selected <%}%>><%=timeZoneName.getKey()%></option>
								<%
								}
								%>
							</select>
						</td>
					</tr>
					<tr>
						<th><fmt:message key="profile.title.phone_no"/></th>
						<td><input name="txtMemberPhoneNo" class="fieldline" type="text" value="<%=CommonUtil.toNotNull(user.getPhoneNo()) %>" /></td>
					</tr>
					<tr>
						<th><fmt:message key="profile.title.cell_phone_no"/></th>
						<td><input name="txtMemberCellPhoneNo" class="fieldline" type="text" value="<%=CommonUtil.toNotNull(user.getCellPhoneNo()) %>" /></td>
					</tr>
					<tr>
						<th><fmt:message key="profile.title.home_phone_no"/></th>
						<td><input name="txtMemberHomePhoneNo" class="fieldline" type="text" value="<%=CommonUtil.toNotNull(user.getHomePhoneNo()) %>" /></td>
					</tr>
					<tr class="end">
						<th><fmt:message key="profile.title.home_address"/></th>
						<td><textarea name="txtMemberHomeAddress" class="fieldline" rows="2"><%=CommonUtil.toNotNull(user.getHomeAddress()) %></textarea></td>
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
						if(SmartUtil.isBlankObject(userId)){
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
				if(!SmartUtil.isBlankObject(userId)){
				%>
					<span class="btn_gray">
						<a href="" class="js_delete_member">
							<span class="txt_btn_start"></span>
								<span class="txt_btn_center"><fmt:message key="common.button.delete"/></span>
							<span class="txt_btn_end"></span>
						</a>
					</span>
					<span class="btn_gray">
						<a href="" class="js_retire_member">
							<span class="txt_btn_start"></span>
								<span class="txt_btn_center"><fmt:message key="common.button.retire_member"/></span>
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
	<!-- 사용자추가 //-->
</div>										
