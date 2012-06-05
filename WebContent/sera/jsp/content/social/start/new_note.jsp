<%@page import="java.text.DecimalFormat"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.model.work.SmartWork"%>
<%@page import="net.smartworks.model.work.Work"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.util.SmartTest"%>
<%@page import="net.smartworks.model.community.User"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>

<script type="text/javascript">

//완료버튼 클릭시 create_new_event.sw 서비스를 실행하기 위해 submit하는 스크립트..
function submitForms() {
	var newNote = $('.js_new_note_page');
	if (SmartWorks.GridLayout.validate(newNote.find('form.js_validation_required'),  newNote.find('.sw_error_message'))) {
		var forms = newNote.find('form');
		var receiverItems = forms.find('.js_community_item');
		var message = forms.find('textarea[name="txtNoteContent"]').attr('value');
		var paramsJson = {};
		var chatters = new Array();
		paramsJson['senderId'] = currentUser.userId;
		var receivers = new Array();
		if(!isEmpty(receiverItems)){
			for(var i=0; i<receiverItems.length; i++){
				var receiverId = $(receiverItems[i]).attr('comId');
				receivers.push(receiverId);
				chatters.push(receiverId);
			}
		}
		paramsJson['receivers'] = receivers;
		paramsJson['chatters'] = chatters;
		paramsJson['message'] = message;
		console.log(JSON.stringify(paramsJson));
		var progressSpan = newNote.find('.js_progress_span');
		smartPop.progressCont(progressSpan);
		var url = "create_async_message.sw";
		$.ajax({
			url : url,
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				smartPop.closeProgress();
				smartPop.showInfo(smartPop.INFO, "쪽지가 성공적으로 전달되었습니다.", function(){
					forms.find('.js_community_item').remove();
					forms.find('textarea[name="txtNoteContent"]').attr('value', '');
				});
			},
			error : function(e) {
				smartPop.closeProgress();
				smartPop.showInfo(smartPop.ERROR, "쪽지를 남기는데 문제가 발생하였습니다. 관리자에게 문의하시기 바랍니다!");
			}
		});
	}
}

</script>

<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 현재사용자 정보도 가져온다..
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();
%>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<div class="up_wrap js_new_note_page">
	<div class="up_point pos_works js_up_pointer"></div>
	<div class="form_wrap up up_padding">
		<!-- 폼- 확장 -->
		<form name="frmNewNote" class="js_validation_required js_click_start_form">
			<div class="js_new_note_fields"></div>
		</form>
		<!-- 새이벤트를 등록하기위한 완료 버튼과 취소 버튼 -->
		<div class="js_upload_buttons"></div>
	</div>
</div>
<script>
	loadNewNoteFields();
	var form = $('form[name="frmNewNote"]').find('.required_label').removeClass('required_label');
</script>