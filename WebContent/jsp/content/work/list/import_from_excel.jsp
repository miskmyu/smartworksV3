<%@page import="net.smartworks.model.work.SocialWork"%>
<%@page import="net.smartworks.server.engine.infowork.form.model.SwfFormModel"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="com.sun.xml.internal.txw2.Document"%>
<%@page import="net.smartworks.model.work.ProcessWork"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.model.KeyMap"%>
<%@page import="net.smartworks.model.filter.ConditionOperator"%>
<%@page import="net.smartworks.model.filter.Condition"%>
<%@page import="net.smartworks.model.work.FormField"%>
<%@page import="net.smartworks.model.work.SmartForm"%>
<%@page import="net.smartworks.model.filter.SearchFilter"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.model.security.EditPolicy"%>
<%@page import="net.smartworks.model.security.WritePolicy"%>
<%@page import="net.smartworks.model.security.AccessPolicy"%>
<%@page import="net.smartworks.model.work.InformationWork"%>
<%@page import="net.smartworks.model.work.SmartWork"%>
<%@page import="net.smartworks.model.work.Work"%> 
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();
	SmartWork work = (SmartWork)session.getAttribute("smartWork");
	String workId = work.getId();
	int workType = work.getType();
	
%>

<script type="text/javascript">

// 완료버튼 클릭시 create_new_iwork.sw 서비스를 실행하기 위해 submit하는 스크립트..
function submitForms() {
	var importFromExcel = $('.js_import_from_excel_page');
	var workId = importFromExcel.attr("workId");
	// new_iwork에 있는 활성화되어 있는 모든 입력화면들을 validation하여 이상이 없으면 submit를 진행한다...
	if (SmartWorks.GridLayout.validate(importFromExcel.find('form.js_validation_required'), $('.js_import_error_message'))) {
		var forms = importFromExcel.find('form');
		var paramsJson = {};
		paramsJson['workId'] = workId;
		for(var i=0; i<forms.length; i++){
			var form = $(forms[i]);
			
			// 폼이 스마트폼이면 formId와 formName 값을 전달한다...
			if(form.attr('name') === 'frmSmartForm'){
				paramsJson['formId'] = form.attr('formId');
				paramsJson['formName'] = form.attr('formName');
			}
			
			// 폼이름 키값으로 하여 해당 폼에 있는 모든 입력항목들을 JSON형식으로 Serialize 한다...
			paramsJson[form.attr('name')] = mergeObjects(form.serializeObject(), SmartWorks.GridLayout.serializeObject(form));
			
		}
		console.log(JSON.stringify(paramsJson));
		var url = "upload_excel_to_work.sw";
		
		// 서비스요청 프로그래스바를 나타나게 한다....
		var progressSpan = importFromExcel.find('.js_progress_span');
		smartPop.progressCont(progressSpan);
		
		// create_new_iwork.sw서비스를 요청한다..
		$.ajax({
			url : url,
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				
				// 성공시에 프로그래스바를 제거하고 성공메시지를 보여준다...
				smartPop.showInfo(smartPop.ERROR, data + smartMessage.get("importFromExcelSuccess"), function(e){
					window.location.reload();
					smartPop.closeProgress();					
				});
			},
			error : function(e) {
				// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
				smartPop.closeProgress();
				smartPop.showInfo(smartPop.ERROR, smartMessage.get("importFromExcelError"));
			}
		});
	}
	return;
}

</script>

<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!-- 상세 필터 -->
<div class="filter_group js_import_from_excel_page" workType="<%=workType%>" workId="<%=workId%>">
	<form name="frmImportFromExcel" class="js_validation_required">
		<div class="js_excel_import_field"></div>
	</form>
	<div class="glo_btn_space">
		<div class="fr">
			<span class="btn_gray">
				<a href="" class="js_download_excel_template"> 
					<span class="txt_btn_start"></span> 
					<span class="txt_btn_center"><fmt:message key="common.button.download_excel_temp"/></span> 
					<span class="txt_btn_end"></span>
				</a> 
			</span> 
			<span class="btn_gray">
				<a href="" class="js_excel_import_excute"> 
					<span class="txt_btn_start"></span> 
					<span class="txt_btn_center"><fmt:message key="common.button.import_from_excel"/></span> 
					<span class="txt_btn_end"></span>
				</a> 
			</span> 
			<span class="btn_gray ml5">
				<a href="" class="js_excel_import_close"> 
					<span class="txt_btn_start"></span> 
					<span class="txt_btn_center"><fmt:message key="common.button.cancel"/></span> 
					<span class="txt_btn_end"></span> 
				</a> 
			</span>
		</div>
		<!--  실행시 표시되는 프로그래스아이콘을 표시할 공간 -->
		<div class="fr form_space js_progress_span" ></div>
		
		<!-- 실행시 데이터 유효성 검사이상시 에러메시지를 표시할 공간 -->
		<span class="form_space sw_error_message js_import_error_message" style="text-align:right; color: red"></span>
		
	</div>
</div>

<script type="text/javascript">
	loadExcelImportField();
</script>
