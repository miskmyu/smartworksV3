
<!-- Name 			: pop_new_mail_filder.jsp								 -->
<!-- Description	: 새로운 메일폴더를 생성하는 팝업화면 							 -->
<!-- Author			: Maninsoft, Inc.										 -->
<!-- Created Date	: 2011.9.												 -->

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

	String sFolderType = request.getParameter("folderType");
	int folderType = (SmartUtil.isBlankObject(sFolderType)) ? 0 : Integer.parseInt(sFolderType);
	String parentId = request.getParameter("parentId");
	String parentName = request.getParameter("parentName");
	String folderId = request.getParameter("folderId");
	String folderName = request.getParameter("folderName");
	String folderDesc = request.getParameter("folderDesc");
	
%>
<script type="text/javascript">

	// 개인정보프로파일 수정하기 버튼을 클릭하면, 
	// 모든정보를 JSON형식으로 Serialize해서 서버의 update_my_profile.sw 서비스를 호출하여 수정한다.
	function submitForms(e) {
		var newMailFolder = $('.js_new_mail_folder_page');
		if (SmartWorks.GridLayout.validate(newMailFolder.find('form.js_validation_required'), $('.js_pop_error_message'))) {
			var forms = newMailFolder.find('form');
			var parentId = newMailFolder.attr('parentId');
			var selParent = newMailFolder.find('select[name="selParentId"] option:selected');
			var folderId = newMailFolder.attr('folderId');
			var folderType = forms.find('input[name="chkFolderType"]:checked').attr('value');
			var paramsJson = {};
			if(isEmpty(folderType)) folderType = <%=MailFolder.TYPE_USER%>;
			if(!isEmpty(selParent)) paramsJson['parentId'] = selParent.attr('value');
			else if(!isEmpty(parentId)) paramsJson['parentId'] = parentId;
			if(!isEmpty(folderId)) paramsJson['folderId'] = folderId;
			paramsJson['folderType'] = "" + folderType;
			paramsJson['folderName'] = forms.find('input[name="txtFolderName"]').attr('value');
			paramsJson['folderDesc'] = forms.find('input[name="txtFolderDesc"]').attr('value');
			console.log(JSON.stringify(paramsJson));
			var progressSpan = newMailFolder.find('.js_progress_span');
			smartPop.progressCont(progressSpan);
			var url = "new_mail_folder.sw";
			if(!isEmpty(folderId)) url = "set_mail_folder.sw"
			$.ajax({
				url : url,
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					// 사용자정보 수정이 정상적으로 완료되었으면, 현재 페이지에 그대로 있는다.
					window.location.reload(true);
					smartPop.closeProgress();
					smartPop.close();
				},
				error : function(e) {
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.ERROR, isEmpty(folderId) ? smartMessage.get('createMailFolderError') : smartMessage.get('setMailFolderError'));
				}
			});
		}
	};
</script>

<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!--  전체 레이아웃 -->
<div class="pop_corner_all js_new_mail_folder_page" parentId="<%=CommonUtil.toNotNull(parentId) %>" folderId="<%=CommonUtil.toNotNull(folderId)%>" folderName="<%=CommonUtil.toNotNull(folderName)%>" folderDesc="<%=CommonUtil.toNotNull(folderDesc)%>">

	<!-- 팝업 타이틀 -->
	<div class="form_title">
		<%
		if(SmartUtil.isBlankObject(folderId)){
		%>
			<div class="pop_title"><fmt:message key="mail.title.new_mail_folder"></fmt:message></div>
		<%
		}else{
		%>
			<div class="pop_title"><fmt:message key="mail.title.text_mail_folder"></fmt:message></div>
		<%
		}
		%>
		<div class="txt_btn">
			<a href="" onclick="smartPop.close();return false;"><div class="btn_x"></div></a>
		</div>
		<div class="solid_line"></div>
	</div>
	<!-- 팝업 타이틀 //-->
	<!-- 컨텐츠 -->
	<form name="frmNewMailFolder" class="js_validation_required">
		<div class="contents_space">
			<table>
				<%
				if(SmartUtil.isBlankObject(parentId) && SmartUtil.isBlankObject(folderId)){
				%>
					<tr>
						<td  class="required_label"><fmt:message key="mail.title.folder_type" /></td>
						<td>
							<input name="chkFolderType" class="required" type="radio" value="<%=MailFolder.TYPE_GROUP%>"><fmt:message key="mail.title.group"/>	
							<input name="chkFolderType" class="required" type="radio" value="<%=MailFolder.TYPE_USER%>"><fmt:message key="mail.title.folder"/>	
						</td>
					</tr>
				<%
				}else if(folderType == MailFolder.TYPE_USER){
				%>
					<tr>
						<td class="required_label"><fmt:message key="mail.title.parent_name" /></td>
						<td>
							<select name="selParentId">
								<option value=""><fmt:message key="common.title.none"/></option>
								<%
								MailFolder[] folders = smartWorks.getMailFolders();
								if(!SmartUtil.isBlankObject(folders)){
									for(int i=0; i<folders.length; i++){
										MailFolder folder = folders[i];
										if(folder.getType()!=MailFolder.TYPE_GROUP) continue;
								%>
										<option value=<%=folder.getId() %> <%if(parentId.equals(folder.getId())){ %>selected<%} %>><%=folder.getFullName() %></option>
								<%
									}
								}
								%>
							</select>
						</td>
					</tr>
				<%
				}
				%>
				<tr>
					<td  class="required_label"><fmt:message key="mail.title.folder_name" /></td>
					<td>
						<input name="txtFolderName" class="fieldline required" type="text" value="<%=CommonUtil.toNotNull(folderName)%>">		
					</td>
				</tr>
				<tr>
					<td><fmt:message key="common.title.desc" /></td>
					<td>
						<textarea name="txtFolderDesc" class="fieldline" rows="4"><%=CommonUtil.toNotNull(folderDesc) %></textarea>	
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
					<%
					if(SmartUtil.isBlankObject(folderId)){
					%>
						<span class="txt_btn_center"><fmt:message key="common.button.create"/></span>
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
			 <span class="btn_gray ml5"> 
				 <a href="" class="js_close_new_mail_folder"> 
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
