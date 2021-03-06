
<!-- Name 			: new_board.jsp									 -->
<!-- Description	: 스마트폼을 이용하여 새 공지를 등록하는 화면 	      	 -->
<!-- Author			: Maninsoft, Inc.								 -->
<!-- Created Date	: 2011.9.										 -->

<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="net.smartworks.model.mail.MailAccount"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.model.mail.MailFolder"%>
<%@page import="net.smartworks.model.instance.MailInstance"%>
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
function submitForms(action) {

	if(action === "send"){
	smartPop.confirm( smartMessage.get('sendMailConfirm'), function(){
		var newMail = $('.js_new_mail_page');
	
		// new_board 에 있는 활성화되어 있는 모든 입력화면들을 validation하여 이상이 없으면 submit를 진행한다...
		if(!SmartWorks.GridLayout.validate(newMail.find('form.js_validation_required'), $('.js_upload_error_message'))) return
	
		// 그려진 화면에 있는 입력화면들을 JSON형식으로 Serialize한다...
		var forms = newMail.find('form');
		var paramsJson = {};
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
		var fromUsers = new Array();
		var senderUserTitle = newMail.attr('senderUserTitle');
		var isDraftFolder = newMail.attr('isDraftFolder');
		var user;
		//if (senderUserTitle != null && senderUserTitle !='' && senderUserTitle != 'undefined') {
		if (!isEmpty(senderUserTitle)) {
			user = {id : newMail.attr('senderId'), name: newMail.attr('senderUserTitle')};
		} else {
			user = {id : newMail.attr('senderId'), name: currentUser.longName};
		}
		fromUsers.push(user);
		paramsJson['from'] = {users: fromUsers };
		console.log(JSON.stringify(paramsJson));
		// 서비스요청 프로그래스바를 나타나게 한다....
		var progressSpan = newMail.find('.js_progress_span');
		smartPop.progressCont(progressSpan);
		var url = "";
		if(action === "send")
			url = "send_mail.sw";
		else if(action === "save")
			url = "save_mail.sw";
		// send_mail.sw서비스를 요청한다..
		$.ajax({
			url : url,
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				// 성공시에 프로그래스바를 제거하고 성공메시지를 보여준다...
				var lastHref = newMail.attr('lastHref');
				if(action === "send"){
					if(isDraftFolder === "true"){
						paramsJson = {};
						var msgIds = new Array();
						msgIds.push(newMail.attr('msgId'));
						paramsJson['ids'] = msgIds;
						paramsJson['folderId'] = newMail.attr('folderId');
						console.log(JSON.stringify(paramsJson));
						$.ajax({
							url : "delete_mails.sw",
							contentType : 'application/json',
							type : 'POST',
							data : JSON.stringify(paramsJson),
							success : function(data, status, jqXHR) {
							},
							error : function(e) {
							}
						});
					}
					smartPop.showInfo(smartPop.INFO, smartMessage.get("sendMailSucceed"), function(){
						if(isEmpty(lastHref))
							window.location.reload(true);
						else
							//refreshCurrentContent(newMail);
							document.location.href = lastHref; 
						smartPop.closeProgress();				
					});
				}else{
					if(isEmpty(lastHref))
						window.location.reload(true);
					else
						//refreshCurrentContent(newMail);
						document.location.href = lastHref; 
					smartPop.closeProgress();								
				}
	
			},
			error : function(e) {
				// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
				smartPop.closeProgress();
				if(e.responseText === "duplicateKeyException")
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("duplicateKeyException"));
				else
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("sendMailError"));
			}
		});
	});}else{
		var newMail = $('.js_new_mail_page');
		
		// new_board 에 있는 활성화되어 있는 모든 입력화면들을 validation하여 이상이 없으면 submit를 진행한다...
		if(!SmartWorks.GridLayout.validate(newMail.find('form.js_validation_required'), $('.js_upload_error_message'))) return
	
		// 그려진 화면에 있는 입력화면들을 JSON형식으로 Serialize한다...
		var forms = newMail.find('form');
		var paramsJson = {};
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
		var fromUsers = new Array();
		var senderUserTitle = newMail.attr('senderUserTitle');
		var user;
		//if (senderUserTitle != null && senderUserTitle !='' && senderUserTitle != 'undefined') {
		if (!isEmpty(senderUserTitle)) {
			user = {id : newMail.attr('senderId'), name: newMail.attr('senderUserTitle')};
		} else {
			user = {id : newMail.attr('senderId'), name: currentUser.longName};
		}
		fromUsers.push(user);
		paramsJson['from'] = {users: fromUsers };
		console.log(JSON.stringify(paramsJson));
		// 서비스요청 프로그래스바를 나타나게 한다....
		var progressSpan = newMail.find('.js_progress_span');
		smartPop.progressCont(progressSpan);
		var url = "";
		if(action === "send")
			url = "send_mail.sw";
		else if(action === "save")
			url = "save_mail.sw";
		// send_mail.sw서비스를 요청한다..
		$.ajax({
			url : url,
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				// 성공시에 프로그래스바를 제거하고 성공메시지를 보여준다...
				var lastHref = newMail.attr('lastHref');
				if(action === "send"){
					smartPop.showInfo(smartPop.INFO, smartMessage.get("sendMailSucceed"), function(){
						if(isEmpty(lastHref))
							window.location.reload(true);
						else
							//refreshCurrentContent(newMail);
							document.location.href = lastHref; 
						smartPop.closeProgress();				
					});
				}else{
					if(isEmpty(lastHref))
						window.location.reload(true);
					else
						//refreshCurrentContent(newMail);
						document.location.href = lastHref; 
					smartPop.closeProgress();								
				}
	
			},
			error : function(e) {
				// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
				smartPop.closeProgress();
				if(e.responseText === "duplicateKeyException")
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("duplicateKeyException"));
				else
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("sendMailError"));
			}
		});
	}
}
</script>

<%
	//스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 현재사용자 정보도 가져온다..
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();
	String lastHref = SmartUtil.getLastHref(request);

	String receiverId = request.getParameter("receiverId");
	String folderId = request.getParameter("folderId");
	String msgId = request.getParameter("msgId");
	String sSendType = request.getParameter("sendType");
	int sendType = (SmartUtil.isBlankObject(sSendType)) ? MailFolder.SEND_TYPE_NONE : Integer.parseInt(sSendType);
	MailInstance instance = new MailInstance();
	boolean isDraftFolder = false;
	if(!SmartUtil.isBlankObject(folderId) && !SmartUtil.isBlankObject(msgId)){
		instance = smartWorks.getMailInstanceById(folderId, msgId, sendType);
		MailFolder folder = smartWorks.getMailFolderById(folderId);
		isDraftFolder = (!SmartUtil.isBlankObject(folder) && folder.getType() == MailFolder.TYPE_SYSTEM_DRAFTS);
	}else if(sendType == MailFolder.SEND_TYPE_WORK_CONTENT){
		String mailContents = (String)request.getAttribute("mailContents");
		instance.setMailContents(mailContents);
	}else if(!SmartUtil.isBlankObject(receiverId)){
		User receiver = smartWorks.getUserById(receiverId);
		instance.setReceivers(new User[]{receiver});
	}else{
		instance.setMailContents("");
	}
	
	MailAccount[] mailAccounts = smartWorks.getMyMailAccounts();
	MailAccount myMailAccount = (SmartUtil.isBlankObject(mailAccounts)) ? null : mailAccounts[0];
	String mailSignature = "";
	if(!isDraftFolder && !SmartUtil.isBlankObject(myMailAccount) && myMailAccount.isUseSignature()){
		instance.setMailContents(instance.getMailContents());
		mailSignature = myMailAccount.getSignature();
	}
%>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!-- 컨텐츠 레이아웃-->
<div class="section_portlet js_new_mail_page js_mail_space_page" lastHref="<%=lastHref %>" currentHref="<%=lastHref %>" msgId="<%=msgId %>" folderId="<%=folderId%>" isDraftFolder="<%=isDraftFolder %>" senderId="<%=cUser.getMailId()%>" senderUserTitle="<%=myMailAccount.getSenderUserTitle()%>">
	<div class="portlet_t"><div class="portlet_tl"></div></div>
	<div class="portlet_l" style="display: block;">
		<ul class="portlet_r" style="display: block;">

			<!-- 타이틀 -->
			<div class="body_titl">
				<div class="body_titl_area ti_mail title">
					<div class="title myspace_h"><fmt:message key="nav.mail.new_mail"/> </div>
				</div>

				<!-- 메일 검색 -->
				<div class="mail_srch">
					<div class="srch_wh srch_wsize_mail">
						<input id="" class="nav_input" type="text" href="" placeholder="<fmt:message key='search.search_mail'/>" title="<fmt:message key='search.search_mail'/>">
						<button onclick="" title="<fmt:message key='search.search'/>"></button>
					</div>
				</div>
				<!-- 메일 검색//-->


				<div class="solid_line cb"></div>
			</div>
			<!-- 타이틀 //-->

			<!-- 컨텐츠 -->
			<div class="contents_space">
				<div class="buttonSet">
					<button class="tb js_send_mail_btn"><span class="icon_mail_send"></span><fmt:message key="mail.button.send"/></button>
					<button class="js_save_mail_btn"><fmt:message key="mail.button.save"/></button>
					<button class="js_cancel_mail_btn"><fmt:message key="common.button.cancel"/></button>
				</div>
				<div class="table_line"></div>
				<!-- 메일 리스트-->
				<div class="list_contents mail_list_section">
					<!-- Form -->
					<div class="form_wrap">
						<!-- 폼- 확장 -->
						<form name="frmNewMail" class="form_title js_validation_required">
							<div class="js_write_mail_fields" receiversTitle="<fmt:message key='common.title.receivers'/>" ccReceiversTitle="<fmt:message key='common.title.cc_receivers'/>" 
								bccReceiversTitle="<fmt:message key='common.title.bcc_receivers'/>" priorityTitle="<fmt:message key='common.title.priority'/>" subjectTitle="<fmt:message key='common.title.subject'/>" attachmentsTitle="<fmt:message key='common.title.attachments'/>"
								<%if(!SmartUtil.isBlankObject(instance)){ %> receivers="<%=StringEscapeUtils.escapeHtml(CommonUtil.toNotNull(instance.getReceiversHtml())) %>" ccReceivers="<%=StringEscapeUtils.escapeHtml(CommonUtil.toNotNull(instance.getCcReceiversHtml())) %>" bccReceivers="<%=StringEscapeUtils.escapeHtml(CommonUtil.toNotNull(instance.getBccReceiversHtml())) %>" 
									priority="<%=instance.getPriority()%>" subject="<%=StringEscapeUtils.escapeHtml(CommonUtil.toNotNull(instance.getSubject())) %>" contents="<%=StringEscapeUtils.escapeHtml(CommonUtil.toNotNull(instance.getMailContents())) %>" mailSignature="<%=StringEscapeUtils.escapeHtml(CommonUtil.toNotNull(mailSignature)) %>" attachments="<%=StringEscapeUtils.escapeHtml(CommonUtil.toNotNull(instance.getAttachmentsHtml()))%>"<%} %>>
							</div>
						</form>
					</div>
					<!-- Form //-->
				</div>
				<!-- 메일 리스트//-->
				<div class="table_line"></div>
				<div class="buttonSet">
					<button class="tb js_send_mail_btn"><span class="icon_mail_send"></span><fmt:message key="mail.button.send"/></button>
					<button class="js_save_mail_btn"><fmt:message key="mail.button.save"/></button>
					<button class="js_cancel_mail_btn"><fmt:message key="common.button.cancel"/></button>
				</div>
			</div>
			<!-- 컨텐츠 //-->

		</ul>
		<div style="display:none">
			<iframe id="attachmentIframe"/>
		</div>
	</div>
	<div class="portlet_b" style="display: block;"></div>
</div>

<script>
	loadWriteMailFields();
</script>
