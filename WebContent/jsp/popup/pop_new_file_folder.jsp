
<!-- Name 			: pop_new_category.jsp									 -->
<!-- Description	: 새로운 업무카테고리를 생성하는 팝업화면 						 -->
<!-- Author			: Maninsoft, Inc.										 -->
<!-- Created Date	: 2011.9.												 -->

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

	String workSpaceId = request.getParameter("workSpaceId");
	String parentId = request.getParameter("parentId");
	String folderId = request.getParameter("folderId");
	String folderName = request.getParameter("folderName");
	
%>
<script type="text/javascript">

	// 개인정보프로파일 수정하기 버튼을 클릭하면, 
	// 모든정보를 JSON형식으로 Serialize해서 서버의 update_my_profile.sw 서비스를 호출하여 수정한다.
	function submitForms(e) {
		var newFileFolder = $('.js_new_file_folder_page');
		if (SmartWorks.GridLayout.validate(newFileFolder.find('form.js_validation_required'), $('.js_pop_error_message'))) {
			var forms = newFileFolder.find('form');
			var workSpaceId = newFileFolder.attr('workSpaceId');
			var folderId = newFileFolder.attr('folderId');
			var paramsJson = {};
			if(!isEmpty(workSpaceId)) paramsJson['workSpaceId'] = workSpaceId;
			if(!isEmpty(folderId)) paramsJson['folderId'] = folderId;
			for(var i=0; i<forms.length; i++){
				var form = $(forms[i]);
				if(form.attr('name') === 'frmSmartForm'){
					paramsJson['formId'] = form.attr('formId');
					paramsJson['formName'] = form.attr('formName');
				}
				paramsJson[form.attr('name')] = mergeObjects(form.serializeObject(), SmartWorks.GridLayout.serializeObject(form));
			}
			console.log(JSON.stringify(paramsJson));
			var progressSpan = newFileFolder.find('.js_progress_span');
			smartPop.progressCont(progressSpan);
			var url = "create_new_file_folder.sw";
			if(!isEmpty(folderId)) url = "set_file_folder.sw"
			$.ajax({
				url : url,
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					// 사용자정보 수정이 정상적으로 완료되었으면, 현재 페이지에 그대로 있는다.
					smartPop.closeProgress();
					smartPop.close();
					$('.js_file_list_page .js_file_display_by').change();
				},
				error : function(e) {
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.ERROR, isEmpty(folderId) ? smartMessage.get('createFileFolderError') : smartMessage.get('setFileFolderError'));
				}
			});
		}
	};
</script>

<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!--  전체 레이아웃 -->
<div class="pop_corner_all js_new_file_folder_page" workSpaceId="<%=CommonUtil.toNotNull(workSpaceId)%>" parentId="<%=CommonUtil.toNotNull(parentId)%>" folderId="<%=CommonUtil.toNotNull(folderId)%>" folderName="<%=CommonUtil.toNotNull(folderName)%>">

	<!-- 팝업 타이틀 -->
	<div class="form_title">
		<%
		if(SmartUtil.isBlankObject(folderId)){
		%>
			<div class="pop_title"><fmt:message key="common.title.new_file_folder"></fmt:message></div>
		<%
		}else{
		%>
			<div class="pop_title"><fmt:message key="common.title.text_file_folder"></fmt:message></div>
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
	<form name="frmNewFileFolder" class="js_validation_required">
		<div class="contents_space">
			<table>
				<tr>
					<td  class="required_label"><fmt:message key="common.title.folder_name" /></td>
					<td>
						<input name="txtFolderName" class="fieldline required" type="text" value="<%=CommonUtil.toNotNull(folderName)%>">
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
				 <a href="" class="js_close_new_folder"> 
				 	<span class="txt_btn_start"></span>
				 	<span class="txt_btn_center"><fmt:message key="common.button.cancel"/></span>
				 	<span class="txt_btn_end"></span> 
				 </a>
			</span>
		</div>

		<span class="fr form_space js_progress_span"></span>
		
		<!-- 실행시 데이터 유효성 검사이상시 에러메시지를 표시할 공간 -->
		<div class="form_space sw_error_message js_pop_error_message" style="color: red;line-height:20px"></div>

	</div>
	<!-- 버튼 영역 //-->
	
</div>
<!-- 전체 레이아웃//-->
