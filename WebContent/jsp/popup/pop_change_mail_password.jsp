
<!-- Name 			: pop_change_mail_password.jsp							-->
<!-- Description	: 메일계정의 암호를 변경하는 팝업화면 							 -->
<!-- Author			: Maninsoft, Inc.										 -->
<!-- Created Date	: 2011.9.												 -->

<%@page import="net.smartworks.model.mail.EmailServer"%>
<%@page import="net.smartworks.model.mail.MailFolder"%>
<%@page import="net.smartworks.model.community.Group"%>
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

	String mailServerId = request.getParameter("mailServerId");
	String emailId = request.getParameter("emailId");
	String userName = request.getParameter("userName");
	String oldPassword = request.getParameter("oldPassword");
	
	EmailServer mailServer = smartWorks.getEmailServerById(mailServerId);
	String pwChangeAPI = "";
	String pwChangeDefaultData = "";
	String pwChangeParamId = "";
	String pwChangeParamOldPW = "";
	String pwChangeParamNewPW = "";
	if(!SmartUtil.isBlankObject(mailServer)){
		pwChangeAPI = mailServer.getPwChangeAPI();
		pwChangeDefaultData = mailServer.getPwChangeDefaultData();
		pwChangeParamId = mailServer.getPwChangeParamId();
		pwChangeParamOldPW = mailServer.getPwChangeParamOldPW();
		pwChangeParamNewPW = mailServer.getPwChangeParamNewPW();
		
	}
	
%>
<script type="text/javascript">

	// 개인정보프로파일 수정하기 버튼을 클릭하면, 
	// 모든정보를 JSON형식으로 Serialize해서 서버의 update_my_profile.sw 서비스를 호출하여 수정한다.
	function submitForms(e) {
		var changeMailPassword = $('.js_change_mail_password_page');
		if (SmartWorks.GridLayout.validate(changeMailPassword.find('form.js_validation_required'), $('.js_pop_error_message'))) {
			var forms = changeMailPassword.find('form');
			var userName = changeMailPassword.attr('userName');
			var userNameWithMailAddress = '';
			if (userName.indexOf('@') === -1) {
				userNameWithMailAddress = userName;
			} else {
				userNameWithMailAddress = userName.substr(0, userName.indexOf('@'));
			}
			var pwChangeAPI = changeMailPassword.attr('pwChangeAPI');
			var pwChangeDefaultData = changeMailPassword.attr('pwChangeDefaultData');
			var pwChangeParamId = changeMailPassword.attr('pwChangeParamId');
			var pwChangeParamOldPW = changeMailPassword.attr('pwChangeParamOldPW');
			var pwChangeParamNewPW = changeMailPassword.attr('pwChangeParamNewPW');

			var oldPassword = forms.find('input[name="pwOldPassword"]').attr('value');
			var newPassword = forms.find('input[name="pwNewPassword"]').attr('value');
			var newPasswordConfirm = forms.find('input[name="pwNewPasswordConfirm"]').attr('value');
			if(newPassword != newPasswordConfirm){
				smartPop.showInfo(smartPop.ERROR, smartMessage.get('newMailPasswordConfirmError'));
				return;				
			}

			var requestData = pwChangeAPI + "?" + pwChangeDefaultData + "&" + pwChangeParamId + "=" + userNameWithMailAddress + "&" + pwChangeParamOldPW + "=" + oldPassword + "&" + pwChangeParamNewPW + "=" + newPassword;
			var paramsJson = {};
			paramsJson['requestData'] = requestData;
			paramsJson['userName'] = userName;
			paramsJson['oldPassword'] = oldPassword;
			paramsJson['newPassword'] = newPassword;
			console.log(JSON.stringify(paramsJson));
			var progressSpan = changeMailPassword.find('.js_progress_span');
			smartPop.progressCont(progressSpan);
			$.ajax({
				url : 'change_mail_password_request.sw',
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					// 사용자정보 수정이 정상적으로 완료되었으면, 현재 페이지에 그대로 있는다.
					smartPop.confirm(smartMessage.get('changeMailPasswordSucceed'),function(){
						window.location.reload(true);
						smartPop.closeProgress();
						smartPop.close();						
					}, function(){
						smartPop.closeProgress();						
					});
				},
				error : function(e) {
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.ERROR, smartMessage.get('changeMailPasswordError'));
				}
			});
		}
	};
</script>

<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!--  전체 레이아웃 -->
<div class="pop_corner_all js_change_mail_password_page" userName="<%=CommonUtil.toNotNull(userName)%>" pwChangeAPI="<%=pwChangeAPI%>" pwChangeDefaultData="<%=pwChangeDefaultData%>" pwChangeParamId="<%=pwChangeParamId%>" pwChangeParamOldPW="<%=pwChangeParamOldPW%>" pwChangeParamNewPW="<%=pwChangeParamNewPW%>" >

	<!-- 팝업 타이틀 -->
	<div class="form_title">
		<div class="pop_title"><fmt:message key="mail.title.change_password" /></div>
		<div class="txt_btn">
			<a href="" onclick="smartPop.close();return false;"><div class="btn_x"></div></a>
		</div>
		<div class="solid_line"></div>
	</div>
	<!-- 팝업 타이틀 //-->
	<!-- 컨텐츠 -->
	<form name="frmChangeMailPassword" class="js_validation_required">
		<div class="contents_space">
			<table>
				<tr>
					<td><fmt:message key="profile.title.email.id" /></td>
					<td><%=CommonUtil.toNotNull(emailId) %></td>
				</tr>
				<tr>
					<td><fmt:message key="profile.title.email.username" /></td>
					<td><%=CommonUtil.toNotNull(userName) %></td>
				</tr>
				<tr>
					<td class="required_label"><fmt:message key="profile.title.email.password" /></td>
					<td>
						<input name="pwOldPassword" class="fieldline required" type="password" value="<%=CommonUtil.toNotNull(oldPassword)%>">		
					</td>
				</tr>
				<tr>
					<td class="required_label"><fmt:message key="mail.title.new_password" /></td>
					<td>
						<input name="pwNewPassword" class="fieldline required" type="password">		
					</td>
				</tr>
				<tr>
					<td class="required_label"><fmt:message key="profile.title.password_confirm" /></td>
					<td>
						<input name="pwNewPasswordConfirm" class="fieldline required" type="password">		
					</td>
				</tr>
			</table>
		</div>
	</form>
	<!-- 컨텐츠 //-->
	<!-- 버튼 영역 -->
	<div class="glo_btn_space">
		<div class="fr">
			<span class="btn_gray">
				<a href="" onclick='submitForms(); return false;'>
					<span class="txt_btn_start"></span>
						<span class="txt_btn_center"><fmt:message key="common.button.modify"/></span>
					<span class="txt_btn_end"></span>
				</a> 
			</span>
			 <span class="btn_gray ml5"> 
				 <a href="" class="js_close_change_mail_password"> 
				 	<span class="txt_btn_start"></span>
				 	<span class="txt_btn_center"><fmt:message key="common.button.cancel"/></span>
				 	<span class="txt_btn_end"></span> 
				 </a>
			</span>
		</div>
		
		<!--  실행시 표시되는 프로그래스아이콘을 표시할 공간 -->
		<span class="fr form_space js_progress_span"></span>
	
		<!-- 실행시 데이터 유효성 검사이상시 에러메시지를 표시할 공간 -->
		<div class="form_space sw_error_message js_pop_error_message" style="color: red; line-height: 20px"></div>
			
	</div>
	<!-- 버튼 영역 //-->

</div>
<!-- 전체 레이아웃//-->
