
<!-- Name 			: my_profile.jsp										 -->
<!-- Description	: 현재사용자 프로파일 정보를 조회하고 수정하는 화면 				 -->
<!-- Author			: Maninsoft, Inc.										 -->
<!-- Created Date	: 2011.9.												 -->

<%@page import="net.smartworks.model.mail.EmailServer"%>
<%@page import="net.smartworks.model.mail.MailAccount"%>
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
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 현재사용자 정보도 가져온다 
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();

	// 사용가능한 타임존들을 가져와서, 타임존 선택박스에 리스트로 보여준다.
	KeyMap[] timeZoneNames = LocalDate.getAvailableTimeZoneNames(cUser.getLocale());

	MailAccount[] mailAccounts = smartWorks.getMyMailAccounts();
	//MailAccount[] mailAccounts = cUser.getMailAccounts();
	MailAccount mailAccount = (SmartUtil.isBlankObject(mailAccounts) || mailAccounts.length<1) ? new MailAccount() : mailAccounts[0];
	EmailServer[] emailServers = smartWorks.getEmailServers();
%>
<script type="text/javascript">

	// 개인정보프로파일 수정하기 버튼을 클릭하면, 
	// 모든정보를 JSON형식으로 Serialize해서 서버의 update_my_profile.sw 서비스를 호출하여 수정한다.
	function submitForms(e) {
		var myProfile = $('.js_my_profile_page');
		if (SmartWorks.GridLayout.validate(myProfile.find('form.js_validation_required'), $('.js_profile_error_message'))) {
			var forms = myProfile.find('form');
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
			var progressSpan = myProfile.find('.js_progress_span');
			smartPop.progressCont(progressSpan);
			$.ajax({
				url : url,
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					// 사용자정보 수정이 정상적으로 완료되었으면, 현재 페이지에 그대로 있는다.
					smartPop.showInfo(smartPop.INFO, smartMessage.get('setMyProfileSucceed'), function(){
						document.location.href = "home.sw";
						smartPop.closeProgress();
					});
				},
				error : function(e) {
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.ERROR, smartMessage.get('setMyProfileError'));
				}
			});
		}
	};
</script>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!-- 컨텐츠 레이아웃-->
<div class="section_portlet js_my_profile_page">
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
					<div class="fr"><span class="vb icon_required"></span><fmt:message key="profile.title.required_field" /></div>
				</div>
				<!-- 우측 버튼 //-->
				<div class="solid_line"></div>
			</div>
			<!-- 타이틀 -->
			
			<!-- 컨텐츠 -->
			<form name="frmMyProfileSetting" class="js_validation_required">
				<div class="contents_space oh">
					<div class="photo_section">
					
						<!--  *** js_my_profile_field : sw_act_work.js에서 화면로딩이 완료되면 이 클래스로 찾아서,  	-->
						<!--      현재사용자의 사진을 보여주고, 다른 사진을 올리줄 있도록하는 기능을 제공한다. 			-->
						<div class="js_my_profile_field js_auto_load_profile"></div>
						<div class="t_text_s11"><fmt:message key="profile.title.size_desc"/></div>
					</div>					
					<table class="table_normal600" style="width:70%">
						<tr>
							<td style="width:200px"><fmt:message key="profile.title.user_id" /></td>
							<td>
								<input name="txtUserProfileUserId" type="text" readonly="readonly"
									value="<%=CommonUtil.toNotNull(cUser.getId())%>">
							</td>
						</tr>		
						<tr>
							<td><fmt:message key="profile.title.user_name" /></td>
							<td>
								<input name="txtUserProfileUserName" readonly="readonly" type="text" value="<%=CommonUtil.toNotNull(cUser.getName())%>">		
							</td>
						</tr>
						<tr>
							<td><fmt:message key="profile.title.employee_id" /></td>
							<td>
								<input name="txtUserProfileEmpId" readonly="readonly" type="text" value="<%=CommonUtil.toNotNull(cUser.getEmployeeId())%>">		
							</td>
						</tr>		
						<tr>
							<td class="required_label"><fmt:message key="profile.title.password" /></td>
							<td>
								<input name="pwUserProfilePW" class="fieldline required" type="password" value="<%=CommonUtil.toNotNull(cUser.getPassword())%>">		
							</td>
						</tr>
						<tr>
							<td class="required_label"><fmt:message key="profile.title.password_confirm" /></td>
							<td>
								<input name="pwUserProfilePWCfm" type="password" class="required fieldline" value="<%=CommonUtil.toNotNull(cUser.getPassword())%>">		
							</td>
						</tr>
						<tr>
							<td><fmt:message key="profile.title.department" /></td>
							<td>
								<input name="txtUserProfileDepartment" readonly="readonly" type="text" companyId="<%=cUser.getCompanyId()%>" value="<%=CommonUtil.toNotNull(cUser.getDepartment())%>">		
							</td>
						</tr>
						<tr>
							<td><fmt:message key="profile.title.position" /></td>
							<td>
								<input name="txtUserProfilePosition" readonly="readonly" type="text" value="<%=CommonUtil.toNotNull(cUser.getPosition())%>">		
							</td>
						</tr>
						<tr>
							<td class="required_label"><fmt:message key="profile.title.locale" /></td>
							<td>
								<select name="selUserProfileLocale">
									<%
									for (String locale : LocaleInfo.supportingLocales) {
										String strKey = "common.title.locale." + locale;
									%>
										<option value="<%=locale%>" <%if (cUser.getLocale().equals(locale)) {%> selected <%}%>><fmt:message key="<%=strKey%>" /></option>
									<%
									}
									%>
								</select>
							</td>
						</tr>
						<tr>
							<td  class="required_label"><fmt:message key="profile.title.timezone" /></td>
							<td>
								<select name="selUserProfileTimeZone">
									<%
									for (KeyMap timeZoneName : timeZoneNames) {
									%>
										<option value="<%=timeZoneName.getId()%>" <%if (cUser.getTimeZone().equals(timeZoneName.getId())) {%> selected <%}%>><%=timeZoneName.getKey()%></option>
									<%
									}
									%>
								</select>
							</td>
						</tr>
						<tr>
							<td><fmt:message key="profile.title.phone_no" /></td>
							<td>
								<input name="txtUserProfilePhoneNo" class="fieldline" type="text" value="<%=CommonUtil.toNotNull(cUser.getPhoneNo())%>" title="">
							</td>
						</tr>
						<tr>
							<td><fmt:message key="profile.title.cell_phone_no" /></td>
							<td>
								<input name="txtUserProfileCellNo" class="fieldline" type="text" value="<%=CommonUtil.toNotNull(cUser.getCellPhoneNo())%>" title="">
							</td>
						</tr>
						<tr>
							<td><fmt:message key="profile.title.use.sign_picture" /></td>
							<td><input name="chkUseSignPicture" type="checkbox" <%if(cUser.isUseSignPicture()){ %>checked<%} %>></td>
						</tr>
						<tr>
							<td><fmt:message key="profile.title.sign_picture" /></td>
							<td><div class="js_my_signpic_field js_auto_load_profile"></div></td>
						</tr>
						<%
						if(!SmartUtil.isBlankObject(emailServers)){
						%>
							<tr>
								<td><fmt:message key="profile.title.email.use" /></td>
								<td>
									<input name="chkUserProfileUseEmail" class="js_toggle_use_email" type="checkbox" <%if(cUser.isUseMail()){ %>checked<%} %>>
								</td>
							</tr>
							<tr class="js_email_account_info" <%if(!cUser.isUseMail()){ %>style="display:none" <%} %>>
								<td  class="required_label"><fmt:message key="profile.title.email.id" /></td>
								<td>
										<input style="width:45%" name="txtUserProfileEmailId" class="fieldline required" type="text" value="<%=CommonUtil.toNotNull(mailAccount.getUserName())%>" title="">
										@
										<select style="width:45%" name="selUserProfileEmailServerName" class="fieldline">
											<%
											for(int i=0; i<emailServers.length; i++){
												EmailServer emailServer = emailServers[i];
											%>
												<option value="<%=emailServer.getId() %>" <%if(emailServer.getId().equals(mailAccount.getEmailServerId())){ %>selected<%} %>><%=CommonUtil.toNotNull(emailServer.getName()) %></option>
											<%
											}
											%>
										</select>
								</td>
							</tr>
							<tr class="js_email_account_info" <%if(!cUser.isUseMail()){ %>style="display:none" <%} %>>
								<td class="required_label"><fmt:message key="profile.title.email.password" /></td>
								<td>
									<input name="pwUserProfileEmailPW" class="fieldline required" type="password" value="<%=CommonUtil.toNotNull(mailAccount.getPassword())%>">		
								</td>
							</tr>
							<tr class="js_email_account_info" <%if(!cUser.isUseMail()){ %>style="display:none" <%} %>>
								<td class="required_label"><fmt:message key="profile.title.email.password_confirm" /></td>
								<td>
									<input name="pwUserProfileEmailPWCfm" type="password" class="required fieldline" value="<%=CommonUtil.toNotNull(mailAccount.getPassword())%>">		
								</td>
							</tr>
						<%
						}
						%>
					</table>
				</form>
			</div>
			<!-- 컨텐츠 //-->
			
			<!-- 버튼 영역 -->
			<div class="glo_btn_space">
				<div class="fr pr10">
					<!-- 실행시 데이터 유효성 검사이상시 에러메시지를 표시할 공간 -->
					<span class="form_space sw_error_message js_profile_error_message" style="text-align:right; color: red"></span>
					<!--  실행시 표시되는 프로그래스아이콘을 표시할 공간 -->
					<span class="js_progress_span"></span>
					<span class="btn_gray">
						<a href="" onclick='submitForms(); return false;'>
							<span class="txt_btn_start"></span>
							<span class="txt_btn_center"><fmt:message key="popup.button.modify_my_profile"/></span>
							<span class="txt_btn_end"></span>
						</a> 
					</span>
					 <span class="btn_gray ml5"> 
						 <a href="" onclick="return true;"> 
						 	<span class="txt_btn_start"></span>
						 	<span class="txt_btn_center"><fmt:message key="common.button.cancel"/></span>
						 	<span class="txt_btn_end"></span> 
						 </a>
					</span>
				</div>
			</div>
			<!-- 버튼 영역 //-->
						
		</ul>
	</div>
	<div class="portlet_b" style="display: block;"></div>
</div>
<!-- 컨텐츠 레이아웃//-->		
