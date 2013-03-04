
<!-- Name 			: new_board.jsp									 -->
<!-- Description	: 스마트폼을 이용하여 새 공지를 등록하는 화면 	      	 -->
<!-- Author			: Maninsoft, Inc.								 -->
<!-- Created Date	: 2011.9.										 -->

<%@page import="net.smartworks.model.work.SmartWork"%>
<%@page import="net.smartworks.model.work.Work"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.util.SmartTest"%>
<%@page import="net.smartworks.model.community.User"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>

<script type="text/javascript">

//완료버튼 클릭시 create_new_board.sw 서비스를 실행하기 위해 submit하는 스크립트..
function submitForms(tempSave) {

	var newBoard = $('.js_new_board_page');

	// new_board 에 있는 활성화되어 있는 모든 입력화면들을 validation하여 이상이 없으면 submit를 진행한다...
	if(!SmartWorks.GridLayout.validate(newBoard.find('form.js_validation_required'), $('.js_upload_error_message'))) return

	var form = newBoard.find('form[name="frmNewBoard"]');
	var uploader = form.find('.qq-uploader');
	var groupId = uploader.attr('groupId');
	var fileList = uploader.find('.qq-upload-list li');
	var fileName = $(fileList[0]).attr('fileName');
	var title = form.find('input[name="txtBoardName"]').attr("value");
	var contents = form.find('textarea[name="txtBoardDetails"]').attr("value");
	var duration = form.find('input[name="txtBoardDuration"]').attr("value");

	var formContent = newBoard.find('.js_hidden_form_content');
	
	var validDuration = false;
	if(!isEmpty(duration)){
		if((new Date())>(new Date(duration))){
			smartPop.showInfo(smartPop.ERROR, smartMessage.get("boardDurationDateError"));
		}else{
			validDuration = true;
		}
	}

	if(!isEmpty(formContent) && validDuration) {

		var workId = newBoard.attr('workId');
		var instanceId = newBoard.attr("instanceId");
		$.ajax({
			url : "get_form_xml.sw",
			data : {
				workId : workId
			},
			success : function(formXml, status, jqXHR) {

				// 화면 xml을 가져오면 가져온 값과 입력된 사진정보 및 설명값들을 가지고 스마트폼을 이용해 화면을 그린다...
				new SmartWorks.GridLayout({
					target : formContent,
					formXml : formXml,
					formValues : createBoardDataFields({
						formXml : formXml,
						groupId : groupId,
						fileName : fileName,
						fileList : fileList,
						title : title,
						contents : contents,
						duration : duration
					}),
					mode : "edit"
				});

				// 그려진 화면에 있는 입력화면들을 JSON형식으로 Serialize한다...
				var forms = newBoard.find('form');
				var paramsJson = {};
				for(var i=0; i<forms.length; i++){
					var form = $(forms[i]);
					// 폼이 스마트폼이면 formId와 formName 값을 전달한다...
					if(form.attr('name') === 'frmSmartForm'){
						paramsJson['formId'] = form.attr('formId');
						paramsJson['formName'] = form.attr('formName');
					}else if(form.attr('name') === 'frmNewBoard'){
						continue;
					}
					// 폼이름 키값으로 하여 해당 폼에 있는 모든 입력항목들을 JSON형식으로 Serialize 한다...
					paramsJson[form.attr('name')] = mergeObjects(form.serializeObject(), SmartWorks.GridLayout.serializeObject(form));
				}
				if(tempSave){
					paramsJson['isTempSave'] = true;
					paramsJson['instanceId'] = instanceId;					
				}
				console.log(JSON.stringify(paramsJson));
				// 서비스요청 프로그래스바를 나타나게 한다....
				var progressSpan = newBoard.find('.js_progress_span');
				smartPop.progressCont(progressSpan);
				var url = "create_new_board.sw";
				// create_new_memo.sw서비스를 요청한다..
				$.ajax({
					url : url,
					contentType : 'application/json',
					type : 'POST',
					data : JSON.stringify(paramsJson),
					success : function(data, status, jqXHR) {
						// 성공시에 프로그래스바를 제거하고 성공메시지를 보여준다...
						smartPop.closeProgress();
						if(tempSave){
							newBoard.attr('instanceId', data.instanceId);
						}else{
							refreshCurrentContent(newBoard);
							//window.location.reload(true);
						}
					},
					error : function(e) {
						// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
						smartPop.closeProgress();
						if(e.responseText === "duplicateKeyException")
							smartPop.showInfo(smartPop.ERROR, smartMessage.get("duplicateKeyException"));
						else
							smartPop.showInfo(smartPop.ERROR, smartMessage.get("createBoardError"));
					}
				});
			}
		});
	}

}
</script>

<%
	//스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 현재사용자 정보도 가져온다..
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();
	
	String currentLocation = (String)session.getAttribute("lastLocation");
	String currentHref = SmartUtil.getLastHref(request);
	
%>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />
<div class="up_wrap js_new_board_page" workId="<%=SmartWork.ID_BOARD_MANAGEMENT%>" currentHref="<%=currentHref%>" currentLocation="<%=currentLocation%>">
	<div class="up_point pos_works js_up_pointer"></div>
	<div class="form_wrap up">

		<!-- 폼- 확장 -->
		<form name="frmNewBoard" class="js_validation_required js_click_start_form">
			<!-- 새로운 공지를 등록하기 위한 입력화면을 스마트폼을 이용하여 자동으로 그린다.. -->
			<!-- js_new_board_fields :  js/sw/sw-formFields.js 에서 loadNewBoardFields()가 찾아서 공지입력화면을 이곳에 그려준다.. -->
			<div class="js_new_board_fields" placeHolderTitle="<fmt:message key='common.upload.message.board'/>" boardNameTitle="<fmt:message key='common.upload.board.name'/>" boardDetailsTitle="<fmt:message key='common.upload.board.details'/>" boardFilesTitle="<fmt:message key='common.upload.board.files'/>" boardDurationTitle="<fmt:message key='common.upload.board.duration'/>">
			</div>
		</form>
		<div class="js_hidden_form_content" style="display:none">
		</div>
		<!-- 새공지를 등록하기위한 완료 버튼과 취소 버튼 -->
		<div class="js_upload_buttons"></div>
	</div>
</div>
<script>
	loadNewBoardFields();
</script>