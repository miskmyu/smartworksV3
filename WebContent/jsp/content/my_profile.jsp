<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.model.KeyMap"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.util.LocaleInfo"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();
	KeyMap[] timeZoneNames = LocalDate.getAvailableTimeZoneNames(cUser.getLocale());
%>
<script type="text/javascript">
function submitForms(e) {
	if (SmartWorks.GridLayout.validate($('form.js_validation_required'))) {
		var forms = $('form');
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
		var url = "update_my_profile.sw";
		$.ajax({
			url : url,
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				$.modal.close();
				popConfirm("성공적으로 완료하였습니다. 생성된 항목페이지로 이동하시겠습니까??", 
						function(){
							document.location.href = data.href;					
						},
						function(){
							document.location.href = document.location.href;
						});
			},
			error : function(e) {
				$.modal.close();
				popShowInfo(swInfoType.ERROR, "새로운 업무를 생성중에 이상이 발생하였습니다.");
			}
		});
	}
}
</script>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!-- 컨텐츠 레이아웃-->
<div class="section_portlet">
	<div class="portlet_t">
		<div class="portlet_tl"></div>
	</div>
	<div class="portlet_l" style="display: block;">
		<ul class="portlet_r" style="display: block;">

			<!-- 타이틀 -->
			<div class="body_titl">
				<div class="body_titl_iworks title"><fmt:message key="content.title.my_profile"></fmt:message></div>

				<!-- 우측 버튼 -->
				<div class="txt_btn">
					<div class="po_right essen_gn"><fmt:message key="profile.title.required_field" /></div>
				</div>
				<!-- 우측 버튼 //-->
				<div class="solid_line"></div>
			</div>
			<!-- 타이틀 -->
			
	<!-- 컨텐츠 -->
	<form name="frmMyProfileSetting" class="js_validation_required">
	<div class="contents_space">
			<form name="frmMyProfileSetting" class="js_validation_required">
			<div class="photo_section">
				<div class="js_my_profile_field js_auto_load_profile"></div>
				<div class="t_text_s11"><fmt:message key="profile.title.size_desc"/></div>
			</div>
			
			<span class="table_nomal600 ">
					<table>
						<tr>
							<td>
							<fmt:message key="profile.title.company" />
							</td>
							<td>

									<input name="txtUserProfileCompany" type="text" class="required"
										readonly="readonly" value="<%=CommonUtil.toNotNull(cUser.getCompany())%>" title="">

							</td>
						</tr>
						<tr>
							<td>
							<fmt:message
									key="profile.title.user_id" />
							</td>
							<td>
							<input
								name="txtUserProfileUserId" type="text" readonly="readonly"
								value="<%=CommonUtil.toNotNull(cUser.getId())%>">
							</td>
						</tr>

						<tr>
							<td>
							<fmt:message
									key="profile.title.user_name" />
							</td>
							<td>

									<input name="txtUserProfileUserName" class="fieldline" type="text" class="required"
										value="<%=CommonUtil.toNotNull(cUser.getName())%>">

							</td>
						</tr>

						<tr>
							<td>
							<fmt:message
									key="profile.title.employee_id" />
							</td>
							<td>

									<input name="txtUserProfileEmpId" class="fieldline" type="text"
										value="<%=CommonUtil.toNotNull(cUser.getEmployeeId())%>" title="">

							</td>
						</tr>

						<tr>
							<td><fmt:message
									key="profile.title.password" /><span class="essen_n"></span>
							</td>
							<td>

									<input name="pwUserProfilePW" class="fieldline" type="password" class="required"
										value="<%=CommonUtil.toNotNull(cUser.getPassword())%>" title="">

							</td>
						</tr>
						<tr>
							<td><fmt:message
									key="profile.title.password_confirm" /><span class="essen_n"></span>
							</td>
							<td>

									<input name="pwUserProfilePWCfm" type="password" class="required fieldline"
										value="<%=CommonUtil.toNotNull(cUser.getPassword())%>" title="">

							</td>
						</tr>
						<tr>
							<td>
							<fmt:message
									key="profile.title.department" /><span class="essen_n"></span>
							</td>
							<td>

									<input name="txtUserProfileDepartment" type="text" class="required fieldline"
										companyId="<%=cUser.getCompanyId()%>"
										value="<%=CommonUtil.toNotNull(cUser.getDepartment())%>" title="">

							</td>
						</tr>
						<tr>
							<td>
							<fmt:message
									key="profile.title.position" /><span class="essen_n"></span>
							</td>
							<td>

									<input name="txtUserProfilePosition" type="text" class="required fieldline"
										value="<%=CommonUtil.toNotNull(cUser.getPosition())%>" title="">

							</td>
						</tr>
						<tr>
							<td>
							<fmt:message key="profile.title.locale" /><span class="essen_n"></span>
							</td>
							<td>
							<select name="selUserProfileLocale">
									<%
										for (String locale : LocaleInfo.supportingLocales) {
											String strKey = "common.title.locale." + locale;
									%>
									<option value="<%=locale%>" <%if (cUser.getLocale().equals(locale)) {%> selected
										<%}%>>
										<fmt:message key="<%=strKey%>" />
									</option>
									<%
										}
									%>
							</select>
							</td>
						</tr>
						<tr>
							<td><fmt:message
									key="profile.title.timezone" /><span class="essen_n"></span>
							</td>
							<td><select name="selUserProfileTimeZone">
									<%
										for (KeyMap timeZoneName : timeZoneNames) {
									%>
									<option value="<%=timeZoneName.getId()%>"
										<%if (cUser.getTimeZone().equals(timeZoneName.getId())) {%>
										selected <%}%>><%=timeZoneName.getKey()%></option>
									<%
										}
									%>
							</select>
							</td>
						</tr>
						<tr>
							<td><fmt:message
									key="profile.title.phone_no" />
							</td>
							<td>

									<input name="txtUserProfilePhoneNo" class="fieldline" type="text"
										value="<%=CommonUtil.toNotNull(cUser.getPhoneNo())%>" title="">

							</td>
						</tr>
						<tr>
							<td><fmt:message
									key="profile.title.cell_phone_no" />
							</td>
							<td>

									<input name="txtUserProfileCellNo" class="fieldline" type="text"
										value="<%=CommonUtil.toNotNull(cUser.getCellPhoneNo())%>" title="">

							</td>
						</tr>
					</table>
				</span>
			</form>
	</div>
	<!-- 컨텐츠 //-->
			
			<!-- 버튼 영역 -->
			<div class="glo_btn_space">
				<div class="float_right padding_r10">
					<a href="" onclick='submitForms(); return false;'> <span
						class="btn_gray"><span class="Btn01Start"></span> <span
							class="Btn01Center"><fmt:message key="popup.button.modify_my_profile"/></span> <span class="Btn01End"></span>
					</a> </span> <span class="btn_gray space_l5"> <a href=""
						onclick="return true;"> <span
							class="Btn01Start"></span> <span class="Btn01Center"><fmt:message key="common.button.cancel"/></span> <span
							class="Btn01End"></span> </a> </span>
				</div>
			</div>
			<!-- 버튼 영역 //-->
			
			
		</ul>
	</div>
	<div class="portlet_b" style="display: block;"></div>
</div>
<!-- 컨텐츠 레이아웃//-->		



			
			


