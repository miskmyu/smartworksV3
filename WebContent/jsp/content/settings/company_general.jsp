<%@page import="net.smartworks.model.company.CompanyGeneral"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 현재사용자 정보도 가져온다..
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();

	CompanyGeneral companyGeneral = smartWorks.getCompanyGeneral();

%>
<script type="text/javascript" async="async">

	// 회사일반정보프로파일 수정하기 버튼을 클릭하면, 
	// 모든정보를 JSON형식으로 Serialize해서 서버의 set_company_general.sw 서비스를 호출하여 수정한다.
	function submitForms(e) {
		var companyGeneral = $('.js_company_general_page');
		var mailHost = companyGeneral.find('input[name="txtMailHost"]');
		if(!isEmpty(mailHost) && mailHost.attr('value') !== ""){
			companyGeneral.find('input[name="txtMailAccount"]').addClass('required');
			var mailPassword = companyGeneral.find('input[name="pasMailPassword"]').addClass('required');
			var mailPasswordConfirm = companyGeneral.find('input[name="pasMailPasswordConfirm"]').addClass('required');
			if(mailPassword.attr('value') !== mailPasswordConfirm.attr('value')){
				companyGeneral.find('.js_profile_error_message').html(smartMessage.get('mailPasswordConfirmError'));
				return;
			}
		}else{
			companyGeneral.find('input[name="txtMailAccount"]').removeClass('required').attr('value', '');
			companyGeneral.find('input[name="pasMailPassword"]').removeClass('required').attr('value', '');
			companyGeneral.find('input[name="pasMailPasswordConfirm"]').removeClass('required').attr('value', '');		
		}
		if (SmartWorks.GridLayout.validate(companyGeneral.find('form.js_validation_required'), companyGeneral.find('.js_profile_error_message'))) {
			var forms = companyGeneral.find('form');
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
			var url = "set_company_general.sw";
			var progressSpan = companyGeneral.find('.js_progress_span');
			smartPop.progressCont(progressSpan);
			$.ajax({
				url : url,
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					// 사용자정보 수정이 정상적으로 완료되었으면, 현재 페이지에 그대로 있는다.
					smartPop.closeProgress();
				},
				error : function(e) {
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.ERROR, smartMessage.get('setCompanyGeneralError'));
				}
			});
		}
	};
</script>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!-- 컨텐츠 레이아웃-->
<div class="section_portlet setting_section js_company_general_page" companyId="<%=companyGeneral.getId()%>">
	<div class="portlet_t"><div class="portlet_tl"></div></div>
	<div class="portlet_l" style="display: block;">
		<ul class="portlet_r" style="display: block;">
			<!-- 타이틀 -->
			<div class="body_titl">
				<div class="body_titl_area ti_company title_noico"><fmt:message key="settings.title.company.general"/></div>
				<div class="solid_line"></div>
			</div>
			<!-- 타이틀 //-->
			<!-- 컨텐츠 -->
			<form name="frmCompanyGeneral" class="contents_space js_validation_required">
				<div class="gray_style table_normal600">
					<table>
						<colgroup>
							<col width="30%" />
							<col width="70%" />
						</colgroup>
						<tbody><tr><th class="tc"><fmt:message key="profile.title.company"/></th><td><%=companyGeneral.getName() %></td></tr></tbody>
					</table>
				</div>
				<!-- 목록영역 -->
				<div class="default_title_space pt20"><div class="title"><fmt:message key="settings.title.company.general"/></div></div>
				
				<div class="boTb">
					<table>
						<colgroup>
							<col width="25%" />
							<col width="75%" />
						</colgroup>
						<tbody>
							<tr class="end">
								<th rowspan="2"><fmt:message key="settings.title.company.start_date"/></th>
								<td>
									<%
									String hireYear = "", hireMonth = "", hireDay = "";
									if(!SmartUtil.isBlankObject(companyGeneral.getStartDate())){
										hireYear = "" + companyGeneral.getStartDate().getYear();
										hireMonth = "" + (companyGeneral.getStartDate().getMonth()+1);
										hireDay = "" + companyGeneral.getStartDate().getDateOnly();
									}
									%>
									<input style="width:40px" maxlength="4" name="txtCompanyStartYear" class="fieldline tc" type="text" value="<%=hireYear %>" /><fmt:message key="common.title.year"/>
									<input style="width:20px" maxlength="2" name="txtCompanyStartMonth" class="fieldline tc" type="text" value="<%=hireMonth %>" /><fmt:message key="common.title.month"/>
									<input style="width:20px" maxlength="2" name="txtCompanyStartDay" class="fieldline tc" type="text" value="<%=hireDay %>" /><fmt:message key="common.title.day"/>
 								</td>
							</tr>
						</tbody>
					</table>
					<table>
						<colgroup>
							<col width="25%" />
							<col width="75%" />
						</colgroup>
						<tbody>
							<tr class="end">
								<th rowspan="2"><fmt:message key="settings.title.company.logo_setting"/></th>
								<td>
									<div class="js_company_logo_field js_company_logo" imgSource="<%=companyGeneral.getCompanyLogo()%>"></div>
									<div class="t_s11" style="vertical-align:bottom"><fmt:message key="settings.title.company.logo_desc"/></div>

 								</td>
							</tr>
						</tbody>
					</table>
					<table>
						<colgroup>
							<col width="25%" />
							<col width="75%" />
						</colgroup>
						<tbody>
							<tr class="end">
								<th rowspan="2"><fmt:message key="settings.title.company.titlelogo_setting"/></th>
								<td>
									<div class="js_company_titlelogo_field js_company_titlelogo" imgSource="<%=companyGeneral.getCompanyTitleLogo()%>"></div>
									<div class="t_s11" style="vertical-align:bottom"><fmt:message key="settings.title.company.titlelogo_desc"/></div>

 								</td>
							</tr>
						</tbody>
					</table>
					<table>
						<colgroup>
							<col width="25%" />
							<col width="75%" />
						</colgroup>
						<tbody>
							<tr class="end">
								<th rowspan="2"><fmt:message key="settings.title.company.loginimage_setting"/></th>
								<td>
									<div class="js_company_loginimage_field js_company_loginimage" imgSource="<%=companyGeneral.getCompanyLoginImage()%>"></div>
									<div class="t_s11" style="vertical-align:bottom"><fmt:message key="settings.title.company.loginimage_desc"/></div>

 								</td>
							</tr>
						</tbody>
					</table>
				</div>
				<!-- 로그인화면 이미지 설정 -->
				<div class="default_title_space pt20">
					<div class="title"><fmt:message key="settings.title.company.mail_setting"/></div>
				</div>
				
				<div class="boTb">
					<table>
						<colgroup>
							<col width="25%" />
							<col width="75%" />
						</colgroup>
						<tbody>
							<tr>
								<th><fmt:message key="settings.title.company.mail_host"/></th>
								<td><input name="txtMailHost" class="fieldline" type="text" value="<%=companyGeneral.getSendMailHost()%>" /></td>
							</tr>
							<tr>
								<th><fmt:message key="settings.title.company.send_mail_account"/></th>
								<td><input name="txtMailAccount" class="fieldline" type="text" value="<%=companyGeneral.getSendMailAccount()%>" /></td>
							</tr>
							<tr>
								<th><fmt:message key="settings.title.company.send_mail_password"/></th>
								<td><input name="pasMailPassword" class="fieldline" type="password" value="<%=companyGeneral.getSendMailPassword()%>" /></td>
							</tr>
							<tr>
								<th><fmt:message key="settings.title.company.send_mail_password_confirm"/></th>
								<td><input name="pasMailPasswordConfirm" class="fieldline" type="password" value="<%=companyGeneral.getSendMailPassword()%>" /></td>
							</tr>
							<tr>
								<th><fmt:message key="settings.title.company.mail_notification"></fmt:message></th>
								<td><input name="chkMailNotification" type="checkbox" <% if(companyGeneral.isSendMailNotification() == true){out.println("checked");} %> /></td>
							</tr>
							<tr class="end">
								<th><fmt:message key="settings.title.company.test_after_saving"/></th>
								<td><input name="chkTestAfterSaving" type="checkbox"/></td>
							</tr>
						</tbody>
					</table>
				</div>
				<!-- 메시징서비스 설정 -->
				<div class="default_title_space pt20"><div class="title"><fmt:message key="settings.title.company.service_setting"/></div></div>
				
				<div class="boTb">
					<table>
						<colgroup>
							<col width="25%" />
							<col width="75%" />
						</colgroup>
						<tbody>
							<tr>
								<th><fmt:message key="settings.title.company.use_messaging"/></th>
								<td><input name="chkUseMessagingService" type="checkbox" <% if(companyGeneral.isUseMessagingService() == true){%>checked<%} %> /></td>
							</tr>
							<tr>
								<th><fmt:message key="settings.title.company.use_chatting"/></th>
								<td><input name="chkUseChattingService" type="checkbox" <% if(companyGeneral.isUseChattingService() == true){%>checked<%} %> /></td>
							</tr>
							<tr class="end">
								<th><fmt:message key="settings.title.company.use_return"/></th>
								<td><input name="chkUseReturnFunction" type="checkbox" <% if(companyGeneral.isUseReturnFunction() == true){%>checked<%} %> /></td>
							</tr>
						</tbody>
					</table>
				</div>
				<!-- 로그인화면 이미지 설정 -->

				<!-- 버튼 영역 -->
				<div class="glo_btn_space">
					<div class="fr ml10">
						<span class="btn_gray"> 
							<a href="" onclick='submitForms(); return false;'>
								<span class="txt_btn_start"></span> 
								<span class="txt_btn_center"><fmt:message key="common.button.save"/></span> 
								<span class="txt_btn_end"></span>
							</a>
						</span> 
					</div>
					<!--  실행시 표시되는 프로그래스아이콘을 표시할 공간 -->
					<span class="fr form_space js_progress_span"></span>
					<!-- 실행시 데이터 유효성 검사이상시 에러메시지를 표시할 공간 -->
					<div class="form_space sw_error_message js_profile_error_message" style="text-align:right; color: red; line-height: 20px"></div>
				</div>
				<!-- 목록영역 //-->
			</form>
		</ul>
	</div>
	<div class="portlet_b" style="display: block;"></div>
</div>

<script type="text/javascript">
	var companyLogos = $('div.js_company_logo');
	if(!isEmpty(companyLogos)) {
		for(var i=0; i<companyLogos.length; i++) {			
			loadCompanyLogoField();
		}		
	}
	var companyTitleLogos = $('div.js_company_titlelogo');
	if(!isEmpty(companyTitleLogos)) {
		for(var i=0; i<companyTitleLogos.length; i++) {			
			loadCompanyTitleLogoField();
		}		
	}
	var companyLoginImages = $('div.js_company_loginimage');
	if(!isEmpty(companyLoginImages)) {
		for(var i=0; i<companyLoginImages.length; i++) {			
			loadCompanyLoginImageField();
		}		
	}
</script>
<!-- 컨텐츠 레이아웃//-->
