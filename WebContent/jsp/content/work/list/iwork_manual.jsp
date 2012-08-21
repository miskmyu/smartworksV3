<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.model.instance.WorkInstance"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.model.instance.CommentInstance"%>
<%@page import="net.smartworks.model.instance.MemoInstance"%>
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

	InformationWork work = (InformationWork)session.getAttribute("smartWork");
	String workId = work.getId();
%>
<script type="text/javascript">

// 완료버튼 클릭시 create_new_iwork.sw 서비스를 실행하기 위해 submit하는 스크립트..
function submitForms() {
	var iworkManual = $('.js_iwork_manual_page');
	var helpUrl = iworkManual.find('input[name="txtHelpUrl"]').attr('value');
	if(!isEmpty(helpUrl) && (helpUrl.length<8 || helpUrl.substring(0,7).toLowerCase() != 'http://')){
		smartPop.showInfo(smartPop.ERROR, smartMessage.get("helpUrlSyntaxError"));
		return;		
	}
	
	var forms = iworkManual.find('form');
	var workId = iworkManual.attr('workId');
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
	var url = "set_iwork_manual.sw";
	
	// 서비스요청 프로그래스바를 나타나게 한다....
	var progressSpan = iworkManual.find('.js_progress_span');
	smartPop.progressCont(progressSpan);
	
	// set_iwork_manual.sw서비스를 요청한다..
	$.ajax({
		url : url,
		contentType : 'application/json',
		type : 'POST',
		data : JSON.stringify(paramsJson),
		success : function(data, status, jqXHR) {
			
			// 성공시에 프로그래스바를 제거하고 성공메시지를 보여준다...
			window.location.reload(true);
			smartPop.closeProgress();
		},
		error : function(e) {
			// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
			smartPop.closeProgress();
			smartPop.showInfo(smartPop.ERROR, smartMessage.get("setWorkManualError"));
		}
	});
	return;
}

</script>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />


<div class="contents_space js_iwork_manual_page js_sub_instance_list js_space_sub_instance" workId="<%=work.getId()%>">
	<div class="border">

		<form name="frmIWorkManual" class="js_validation_required">		
			<!-- 업무 정의 영역 -->
			<div class="js_work_desc_view"><%if(!SmartUtil.isBlankObject(work.getDesc())) {%><%=work.getDesc()%> <%}else{ %><fmt:message key="common.message.no_work_desc" /><%}%></div>
			<div class="js_work_desc_edit" style="display:none">
				<div><fmt:message key="builder.title.work_desc"/> : </div>
				<textarea name="txtaWorkDesc" class="fieldline" rows="4" style="width:99%"><%=CommonUtil.toNotNull(work.getDesc())%></textarea>
			</div>
			<!-- 업무 정의 영역 //-->
			
			<!-- 업무설명 영역 -->
			<div class="det_contents">
				<table>
					<colgroup>
						<col class="item">
						<col class="field">
					</colgroup>
					<tbody>
						<tr>
							<%
							if(work.getForm() != null){
								String desc = work.getForm().getDescription();
							%>
								<td class="vt">
								<div class="manual_df_img">
									<img src="<%=work.getForm().getOrgImage()%>"/>
								</div>
								</td>
								<td class ="dline_left_gray vt pl10" style="width:100%">
	 								<div class="js_form_desc_view"><%if(!SmartUtil.isBlankObject(desc)){%><%=desc%><%}else{ %><fmt:message key="common.message.no_form_desc"/><%} %></div>
									<div class="js_form_desc_edit"  style="display:none">
	 									<span><fmt:message key="builder.title.form_desc"/> : </span>
	 									<span class="fr js_select_editor_box">
		 									<input name="rdoEditor" type="radio" checked value="text"/><fmt:message key="builder.button.text"/>
											<input name="rdoEditor" type="radio" value="editor"/><fmt:message key="builder.button.editor"/>
										</span>
										<textarea class="fieldline js_form_desc_text" name="txtaFormDesc" cols="" rows="18" style="height: 262px"><%=CommonUtil.toNotNull(desc) %></textarea>
										<div class="js_form_desc_editor"></div>
									</div>
								</td>
							<%
							}
							%>
						</tr>
					</tbody>
				</table>
			</div>	    
		    <div class="js_manual_attachments_field" style="display:none" 
		    	manualFileText="<fmt:message key='work.title.manual_file'/>" helpUrlText="<fmt:message key='work.title.help_url'/>"
		    	manualFile="<%=work.getManualFileId()%>" helpUrl="<%=CommonUtil.toNotNull(work.getHelpUrl())%>"></div>
		</form>
		<!-- 업무 설명 영역 //-->
	
	   <!-- 댓글 -->
	   <div class="reply_point pos_reply js_work_comment_list"></div>
	   <div class="reply_section js_work_comment_list">  
	        <div class="list_reply">
	            <ul class="js_comment_list">
	            	<li class="js_comment_instance" style="display:none">
						<div class="det_title">
							<div class="noti_pic">
								<a class="js_pop_user_info" href="<%=cUser.getSpaceController() %>?cid=<%=cUser.getSpaceContextId()%>" userId="<%=cUser.getId()%>" longName="<%=cUser.getLongName() %>" minPicture="<%=cUser.getMinPicture() %>" profile="<%=cUser.getOrgPicture()%>" userDetail="<%=SmartUtil.getUserDetailInfo(cUser.getUserInfo())%>">
									<img src="<%=cUser.getMinPicture()%>" class="profile_size_c"/>
								</a>
							</div>
							<div class="noti_in">
								<a href="<%=cUser.getSpaceController() %>?cid=<%=cUser.getSpaceContextId()%>">
									<span class="t_name"><%=cUser.getLongName()%></span>
								</a>
								<span class="t_date"><%=(new LocalDate()).toLocalString()%></span>
								<div class="js_comment_content"></div>
							</div>
						</div>
	            	</li>
	            	<%
	            	if(work.getCommentCount()>WorkInstance.DEFAULT_SUB_INSTANCE_FETCH_COUNT){
	            	%>
		            	<li>
		            		<img class="repl_tinfo">
	            			<a href="comment_list_in_manual.sw?workId=<%=work.getId()%>&fetchCount=<%=WorkInstance.FETCH_ALL_SUB_INSTANCE %>" class="js_show_all_comments">
	            				<span><strong><fmt:message key="common.title.show_all_comments"><fmt:param><%=work.getCommentCount() %></fmt:param><</fmt:message></strong></span>
	            			</a>
		            	</li>
					<%
	            	}
					if(work.getCommentCount()>0) {
					%>
						<jsp:include page="/jsp/content/work/list/comment_list_in_manual.jsp">
							<jsp:param value="<%=work.getId()%>" name="workId"/>
							<jsp:param value="<%=WorkInstance.DEFAULT_SUB_INSTANCE_FETCH_COUNT%>" name="fetchCount"/>
						</jsp:include>
					<%
					}
					%>
				</ul>
	        </div>
	        
	        <div class="reply_input js_return_on_comment">
				<div class="noti_pic">
					<img src="<%=cUser.getMinPicture()%>" class="profile_size_c"/>
				</div>
				<div class="noti_in">
					<textarea style="width:95%" class="up_textarea" rows="2" name="txtaCommentContent" placeholder="<fmt:message key='work.message.leave_question'/>"></textarea>
				</div>
	        </div>
	    
	    </div>
	    <!-- 댓글 //-->
	    
	    <!-- 라인 -->
		<div class="solid_line_s t10 mb5"></div>
		
		<!-- 우측 버튼 -->
		<div class="txt_btn">
	
			<!-- 수정하기 -->
			<div class="fr ml5">
				<%
				if(work.amIBuilderUser()) {
				%>
					<span class="btn_gray js_modify_work_manual">
						<a href="">
							<span class="txt_btn_start"></span> 
							<span class="txt_btn_center"><fmt:message key='common.button.modify' /> </span>
							<span class="txt_btn_end"></span>
						</a>
					</span>
					<span class="btn_gray js_save_work_manual" style="display:none"> 
						<a href="">
							<span class="txt_btn_start"></span> 
							<span class="txt_btn_center"><fmt:message key='common.button.save' /> </span>
							<span class="txt_btn_end"></span>
						</a>
					</span>
					<span class="btn_gray js_cancel_work_manual" style="display:none"> 
						<a href="">
							<span class="txt_btn_start"></span> 
							<span class="txt_btn_center"><fmt:message key='common.button.cancel' /> </span>
							<span class="txt_btn_end"></span>
						</a>
					</span>
				<%
				}
				%>
			</div>
			<!-- 수정하기 //-->
	
			<!-- 최종수정자 -->
			<div class="po_left">
				<img class="pho_user profile_size_s vb" title="<fmt:message key="common.title.last_modifier" />" src="<%=work.getLastModifier().getMinPicture()%>"> 
				<span class="t_name"><%=work.getLastModifier().getLongName()%></span>
				<span class="t_date"><%=work.getLastModifiedDate().toLocalString()%></span>
			</div>
			<!-- 최종수정자 //-->
	
			<span class="po_left pt3">
				<%
				if (!SmartUtil.isBlankObject(work.getManualFileId())) {
				%>
					<span class="fl mr7 js_manual_file" title="<fmt:message key='work.title.manual_file'/>" groupId="<%=work.getManualFileId()%>"></span> 
				<%
				}
				if (!SmartUtil.isBlankObject(work.getHelpUrl())) {
				%> 
					<a href="<%=work.getHelpUrl()%>" class="icon_web_manual" title="<fmt:message key='work.title.help_url'/>" target="_blank"></a>
				<%
				}
				%>
			</span>
	
			<!-- 우측 권한 아이콘-->
			<span class="btn_r"> 
				<%
				switch (work.getAccessPolicy().getLevel()) {
				case AccessPolicy.LEVEL_PUBLIC:
				%>
					<div class="fr"><fmt:message key="common.security.access.public" /></div>
				<%
					break;
				case AccessPolicy.LEVEL_PRIVATE:
				%>
					<div class="fr"><fmt:message key="common.security.access.private" /></div> 
				<%
					break;
				case AccessPolicy.LEVEL_CUSTOM:
				%>
					<div class="fr"><fmt:message key="common.security.access.custom" /></div> 
				<%
					break;
				}
				%>
					
				<div class="ch_right"><span class="icon_body_read" title="<fmt:message key='common.security.title.access'/>"></span></div>
				<%
				switch (work.getWritePolicy().getLevel()) {
				case WritePolicy.LEVEL_PUBLIC:
				%>
					<div class="fr"><fmt:message key="common.security.write.public" /></div> 
				<%
					break;
				case WritePolicy.LEVEL_CUSTOM:
				%>
					<div class="fr"><fmt:message key="common.security.write.custom" /></div> 
				<%
					break;
				}
				%>
					
				<div class="ch_right"><span class="icon_body_register"  title="<fmt:message key='common.security.title.write'/>"></span></div> 
				<%
				switch (work.getEditPolicy().getLevel()) {
				case EditPolicy.LEVEL_PUBLIC:
				 %>
					<div class="fr"><fmt:message key="common.security.edit.public" /></div> 
				<%
					break;
				case EditPolicy.LEVEL_PRIVATE:
				%>
					<div class="fr"><fmt:message key="common.security.edit.private" /></div> 
				<%
				 	break;
				case EditPolicy.LEVEL_CUSTOM:
				%>
					<div class="fr"><fmt:message key="common.security.edit.custom" /></div> 
				<%
				 	break;
				 }
				 %>
				<div class="ch_right"><span class="icon_body_modify"  title="<fmt:message key='common.security.title.edit'/>"></span></div>
			</span>
			<!-- 우측 권한 아이콘-->
			<!--  실행시 표시되는 프로그래스아이콘을 표시할 공간 -->
			<div class="fr form_space js_progress_span" ></div>
		</div>
		<!-- 우측 버튼 //-->
	</div>
</div>

<script>
	var manualFile = $('.js_iwork_manual_page').find('.js_manual_file');
	if(!isEmpty(manualFile)){
		var groupId = manualFile.attr('groupId');
		viewFiles(groupId, manualFile);
	}
</script>
