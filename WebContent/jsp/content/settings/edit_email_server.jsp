<%@page import="net.smartworks.model.mail.EmailServer"%>
<%@page import="net.smartworks.model.community.Community"%>
<%@page import="net.smartworks.model.calendar.CompanyEvent"%>
<%@page import="net.smartworks.model.calendar.CompanyCalendar"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.model.calendar.WorkHour"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.model.calendar.WorkHourPolicy"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 현재사용자 정보도 가져온다..
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();
	
	String emailServerId = request.getParameter("emailServerId");
	EmailServer emailServer = new EmailServer();
	if(!SmartUtil.isBlankObject(emailServerId)){
		emailServer =  smartWorks.getEmailServerById(emailServerId);
	}

%>
<script type="text/javascript">

	// 근무시간정책을 수정하기 버튼을 클릭하면, 
	// 모든정보를 JSON형식으로 Serialize해서 서버의 set_work_hour_policy.sw 서비스를 호출하여 수정한다.
	function submitForms(e) {
		var editEmailServer = $('.js_edit_email_server_page');
		if (SmartWorks.GridLayout.validate(editEmailServer.find('form.js_validation_required'), editEmailServer.find('.js_profile_error_message'))) {
			var forms = editEmailServer.find('form');
			var paramsJson = {};
			paramsJson['emailServerId'] = editEmailServer.attr('emailServerId');
			for(var i=0; i<forms.length; i++){
				var form = $(forms[i]);
				if(form.attr('name') === 'frmSmartForm'){
					paramsJson['formId'] = form.attr('formId');
					paramsJson['formName'] = form.attr('formName');
				}
				paramsJson[form.attr('name')] = mergeObjects(form.serializeObject(), SmartWorks.GridLayout.serializeObject(form));
 			}
			console.log(JSON.stringify(paramsJson));
			
			var url = "set_email_server.sw";
			var emailServerId = editEmailServer.attr('emailServerId'); 
			var confirmMessage = smartMessage.get("saveConfirmation");
			if(isEmpty(emailServerId)){
				url = "create_email_server.sw";
				confirmMessage = smartMessage.get("createConfirmation")
			}
			smartPop.confirm( confirmMessage, function(){
				var progressSpan = editEmailServer.find('.js_progress_span');
				smartPop.progressCont(progressSpan);
				$.ajax({
					url : url,
					contentType : 'application/json',
					type : 'POST',
					data : JSON.stringify(paramsJson),
					success : function(data, status, jqXHR) {
						// 사용자정보 수정이 정상적으로 완료되었으면, 현재 페이지에 그대로 있는다.
						document.location.href = "email_server.sw";					
						smartPop.closeProgress();
					},
					error : function(e) {
						smartPop.closeProgress();
						smartPop.showInfo(smartPop.ERROR, isEmpty(emailServerId) ? smartMessage.get('createEmailServerError') : smartMessage.get('setEmailServerError'));
					}
				});
			});
		}
	};
	
	function closePage() {
		$('.js_edit_email_server_page').parent().slideUp(500).html('');
	};	
</script>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<div class="form_wrap up mb2 js_edit_email_server_page" emailServerId="<%=CommonUtil.toNotNull(emailServerId)%>">
	<div class="form_title">
		<%
		if(SmartUtil.isBlankObject(emailServerId)){
		%>
			<div class="icon_iworks title_noico"><fmt:message key="settings.title.email.new"/></div>
		<%
		}else{
		%>
			<div class="icon_iworks title_noico"><fmt:message key="settings.title.email.edit"/></div>
		<%
		}
		%>
		<div class="solid_line"></div>
	</div>

	<form name="frmEditEmailServer" class="form_layout con js_validation_required">
		<table>
			<tbody>
				<tr>
					<th class="required_label" width="15%"><fmt:message key="settings.title.email.name"/></th>
					<td width="85%"><input name="txtEmailServerName" class="up required" type="text" value="<%=CommonUtil.toNotNull(emailServer.getName()) %>" /></td>
				</tr>
				<tr>
					<th class="required_label" width="15%"><fmt:message key="settings.title.email.fetch_server"/></th>
					<td width="85%"><input name="txtEmailServerFetchServer" class="up required" type="text" value="<%=CommonUtil.toNotNull(emailServer.getFetchServer()) %>" /></td>
				</tr>
				<tr>
					<th class="required_label" width="15%"><fmt:message key="settings.title.email.fetch_port"/></th>
					<td width="85%"><input name="txtEmailServerFetchPort" class="up required" type="text" <%if(emailServer.getFetchServerPort()>0){ %>value="<%=emailServer.getFetchServerPort() %>"<%} %> /></td>
				</tr>
				<tr>
					<th class="" width="15%"><fmt:message key="settings.title.email.fetch_protocol"/></th>
					<td width="85%">
						<select name="selEmailServerFetchProtocol">
							<option value="<%=EmailServer.PROTOCOL_POP3 %>" <%if(EmailServer.PROTOCOL_POP3.equals(emailServer.getFetchProtocol())){ %>selected<%} %>><%=EmailServer.PROTOCOL_POP3 %></option>
						</select>
					</td>
				</tr>
				<tr>
					<th width="15%"><fmt:message key="settings.title.email.fetch_ssl"/></th>
					<td width="85%"><input name="txtEmailServerFetchSsl" type="checkbox" <%if(emailServer.isFetchSsl()){ %>checked<%} %>/></td>
				</tr>
				<tr>
					<th class="required_label" width="15%"><fmt:message key="settings.title.email.smtp_server"/></th>
					<td width="85%"><input name="txtEmailServerSmtpServer" class="up required" type="text" value="<%=CommonUtil.toNotNull(emailServer.getSmtpServer()) %>" /></td>
				</tr>
				<tr>
					<th class="required_label" width="15%"><fmt:message key="settings.title.email.smtp_port"/></th>
					<td width="85%"><input name="txtEmailServerSmtpPort" class="up required" type="text" <%if(emailServer.getSmtpServerPort()>0){ %>value="<%=emailServer.getSmtpServerPort() %>"<%} %> /></td>
				</tr>
				<tr>
					<th width="15%"><fmt:message key="settings.title.email.smtp_authenticated"/></th>
					<td width="85%"><input name="txtEmailServerSmtpAuthenticated" type="checkbox" <%if(emailServer.isSmtpAuthenticated()){ %>checked<%} %>/></td>
				</tr>
				<tr>
					<th width="15%"><fmt:message key="settings.title.email.smtp_ssl"/></th>
					<td width="85%"><input name="txtEmailServerSmtpSsl" type="checkbox" <%if(emailServer.isSmtpSsl()){ %>checked<%} %>/></td>
				</tr>
				
				<tr></tr>
				<tr>
					<th width="15%"><fmt:message key="settings.title.email.delete_fetched"/></th>
					<td width="85%"><input name="chkEmailServerDeleteFetched" type="checkbox" <%if(emailServer.isDeleteFetched()){ %>checked<%} %>/></td>
				</tr>
				<tr>
					<th width="15%"><fmt:message key="settings.title.email.pwchange"/></th>
					<td width="85%">
						<span><fmt:message key="settings.title.email.pwchange_url"/></span>						
						<input name="txtPWChangeAPI" class="up" style="width:160px;" type="text" value="<%=CommonUtil.toNotNull(emailServer.getPwChangeAPI())%>"/>
						<span class="pl5"><fmt:message key="settings.title.email.pwchange_default_data"/></span>
						<input name="txtPWChangeDefaultData" class="up" style="width:160px;" type="text" value="<%=CommonUtil.toNotNull(emailServer.getPwChangeDefaultData())%>"/>
						<span class="pl5"><fmt:message key="settings.title.email.pwchange_param_id"/></span>
						<input name="txtPWChangeParamId" class="up" style="width:80px;" type="text" value="<%=CommonUtil.toNotNull(emailServer.getPwChangeParamId())%>"/>
						<span class="pl5"><fmt:message key="settings.title.email.pwchange_param_oldpw"/></span>
						<input name="txtPWChangeParamOldPW" class="up" style="width:80px;" type="text" value="<%=CommonUtil.toNotNull(emailServer.getPwChangeParamOldPW())%>"/>
						<span class="pl5"><fmt:message key="settings.title.email.pwchange_param_newpw"/></span>
						<input name="txtPWChangeParamNewPW" class="up" style="width:80px;" type="text" value="<%=CommonUtil.toNotNull(emailServer.getPwChangeParamNewPW())%>"/>
					</td>
				</tr>
<%-- 				<tr>
					<th width="15%"><fmt:message key="settings.title.email.auto_backup"/></th>
					<td width="85%">
						<input name="chkEmailServerAutoBackup" class="fieldline js_check_mail_auto_backup" type="checkbox" style="width:20px" <%if(emailServer.isAutoBackup()){ %>checked<%} %>/>
						<span <%if(!emailServer.isAutoBackup()){ %>style="display:none"<%} %> >
							<input name="txtEmailServerMailKeepingMonths" class="fieldline tr" type="text" style="width:20px" value="<%=emailServer.getMailKeepingMonths()%>"/>
							<fmt:message key="settings.title.email.save_after_months"/>
						</span>
					</td>
				</tr>
 --%>			</tbody>
		</table>
	</form>

	<!-- 버튼영역 -->
	<div class="glo_btn_space">
		<div class="fr ml10">
			<span class="btn_gray"> 
				<a href="" onclick='submitForms(); return false;'>
					<span class="txt_btn_start"></span>
					<%
					if(SmartUtil.isBlankObject(emailServerId)){
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
			<span class="btn_gray"> 
				<a href="" onclick='closePage();return false;'>
					<span class="txt_btn_start"></span>
					<span class="txt_btn_center"><fmt:message key="common.button.cancel"/></span> 
					<span class="txt_btn_end"></span>
				</a>
			</span>
		</div>
		<!--  실행시 표시되는 프로그래스아이콘을 표시할 공간 -->
		<span class="fr form_space js_progress_span"></span>
		<!-- 실행시 데이터 유효성 검사이상시 에러메시지를 표시할 공간 -->
		<div class="form_space sw_error_message js_profile_error_message" style="text-align:right; color: red; line-height:20px"></div>
	</div>
	<!-- 버튼영역 //-->

</div>
<!-- 추가하기 테이블 //-->
