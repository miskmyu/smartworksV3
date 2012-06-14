<%@page import="net.smartworks.model.work.ScheduleWork"%>
<%@page import="net.smartworks.model.work.SmartForm"%>
<%@page import="net.smartworks.model.work.InformationWork"%>
<%@page import="net.smartworks.model.work.SmartWork"%>
<%@page import="net.smartworks.model.work.Work"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.util.SmartTest"%>
<%@page import="net.smartworks.model.community.User"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>

<script type="text/javascript">
function submitForms(e) {
	var planSwork = $('.js_plan_swork_page');
	var scheduleWork = planSwork.find('form[name="frmScheduleWork"]');
	if(scheduleWork.find($('input[name="chkScheduleWork"]')).is(':checked')){
		scheduleWork.addClass('js_validation_required');
	}else{
		scheduleWork.removeClass('js_validation_required');	
	}

	if (SmartWorks.GridLayout.validate(planSwork.find('form.js_validation_required'), $('.js_upload_error_message'))) {
		var forms = planSwork.find('form');
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
		var url = "create_new_iwork.sw";
		smartPop.progress("새로운 업무를 생성 중입니다.");
		$.ajax({
			url : url,
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				$.modal.close();
				smartPop.confirm("성공적으로 완료하였습니다. 생성된 항목페이지로 이동하시겠습니까??", 
						function(){
							document.location.href = data.href;					
						},
						function(){
							document.location.href = document.location.href;
						});
			},
			error : function(e) {
				$.modal.close();
				smartPop.showInfo(smartPop.ERROR, "새로운 업무를 생성중에 이상이 발생하였습니다.");
			}
		});
	}
	return;
}

</script>
<%
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	String workId = request.getParameter("workId");
	String workName = request.getParameter("workName");
	User cUser = SmartUtil.getCurrentUser();
	ScheduleWork work = (ScheduleWork)smartWorks.getWorkById(workId);
	
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<div class="form_wrap up mb2 js_form_wrap js_plan_swork_page">
	<div class="form_title js_form_header">
		<div class="icon_sworks title"><%=work.getFullpathName()%></div>
		<div class="txt_btn">
			<div class="po_right image_posi">
				<a href="" class="js_toggle_approval_btn"><img src="images/btn_approvep.gif" title="<fmt:message key='common.button.approval'/>" /> </a>
			</div>
			<div class="po_right image_posi">
				<a href="" class="js_toggle_forward_btn"><img src="images/btn_referw.gif" title="<fmt:message key='common.button.forward'/>" /> </a>
			</div>
		</div>
		<div class="solid_line"></div>
	</div>
	<div class="js_form_task_approval" style="display:none"></div>
	<div class="js_form_task_forward" style="display:none"></div>
	<div class="js_form_content" workType="swork"></div>
	<jsp:include page="/jsp/content/upload/check_schedule_work.jsp"></jsp:include>
	<!-- 폼- 확장 //-->
	<div class="js_upload_buttons"></div>
</div>
