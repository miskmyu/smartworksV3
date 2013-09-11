<!-- Name 			: iwork_space.jsp						 -->
<!-- Description	: 정보관리업무 인스턴스 공간을 표시하는 페이지    -->
<!-- Author			: Maninsoft, Inc.						 -->
<!-- Created Date	: 2011.9.								 -->

<%@page import="net.smartworks.server.service.factory.SwServiceFactory"%>
<%@page import="com.google.gdata.data.introspection.IWorkspace"%>
<%@page import="net.smartworks.model.company.CompanyOption"%>
<%@page import="net.smartworks.model.work.info.WorkInfo"%>
<%@page import="net.smartworks.model.work.info.SmartWorkInfo"%>
<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.model.instance.Instance"%>
<%@page import="net.smartworks.model.instance.WorkInstance"%>
<%@page import="net.smartworks.model.security.AccessPolicy"%>
<%@page import="net.smartworks.model.community.info.GroupInfo"%>
<%@page import="net.smartworks.model.community.info.DepartmentInfo"%>
<%@page import="net.smartworks.model.community.info.CommunityInfo"%>
<%@page import="net.smartworks.model.instance.info.TaskInstanceInfo"%>
<%@page import="net.smartworks.model.instance.TaskInstance"%>
<%@page import="net.smartworks.model.work.InformationWork"%>
<%@page import="net.smartworks.model.community.WorkSpace"%>
<%@page import="net.smartworks.model.instance.InformationWorkInstance"%>
<%@page import="net.smartworks.model.work.Work"%>
<%@page import="net.smartworks.model.work.SmartWork"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<script type="text/javascript">

// 완료버튼 클릭시 create_new_iwork.sw 서비스를 실행하기 위해 submit하는 스크립트..
function submitForms(tempSave) {
	var iworkSpace = $('.js_iwork_space_page');
	var workId = iworkSpace.attr("workId");
	var instanceId = iworkSpace.attr("instId");
	var tempSavedId = iworkSpace.attr("tempSavedId");
	var scheduleWork = iworkSpace.find('form[name="frmScheduleWork"]');
	
	// 계획업무로 지정하기가 선택되어 있으면, 계획업무관련 입력필드들을 validation하기위한 클래스를 추가한다.. 
	if(scheduleWork.find($('input[name="chkScheduleWork"]')).is(':checked')){
		scheduleWork.addClass('js_validation_required');
	}else{
		scheduleWork.removeClass('js_validation_required');	
	}
	
	var approvers = iworkSpace.find('.js_approval_box input[type="hidden"]');
	if(!isEmpty(approvers)){
		for(var i=0; i<approvers.length; i++){
			var approver = $(approvers[i]);
			var autoComplete = approver.parents('.js_approval_box').find('.js_auto_complete');
			if(isEmpty(approver.attr('value'))) autoComplete.addClass('required');
			else autoComplete.removeClass('required');
		}
	}
	
	// new_iwork에 있는 활성화되어 있는 모든 입력화면들을 validation하여 이상이 없으면 submit를 진행한다...
	if (SmartWorks.GridLayout.validate(iworkSpace.find('form.js_validation_required'), $('.js_space_error_message'))) {
		var forms = iworkSpace.find('form');
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
		if(tempSave){
			paramsJson['isTempSave'] = true;
			paramsJson['instanceId'] = instanceId;
		}
		if(!isEmpty(tempSavedId)){
			paramsJson['tempSavedId'] = tempSavedId;
		}
		console.log(JSON.stringify(paramsJson));
		var url = "create_new_iwork.sw";
		// 서비스요청 프로그래스바를 나타나게 한다....
		var progressSpan = iworkSpace.find('.js_progress_span');
		smartPop.progressCont(progressSpan);
		// create_new_iwork.sw서비스를 요청한다..
		$.ajax({
			url : url,
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				
				// 성공시에 프로그래스바를 제거하고 성공메시지를 보여준다...
				if(tempSave){
					iworkSpace.attr('instId', data.instanceId);
					iworkSpace.attr('tempSavedId', data.intanceIdq);
				}else{
					window.location.reload(true);
				}
				smartPop.closeProgress();
			},
			error : function(e) {
				// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
				smartPop.closeProgress();
				if(e.responseText === "duplicateKeyException")
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("duplicateKeyException"));
				else
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("createIWorkError"));
			}
		});
	}
	return;
}
</script>

<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 현재사용자 정보도 가져온다..
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();
	
	String cid = request.getParameter("cid");
	String wid = request.getParameter("wid");
	String lastHref = SmartUtil.getLastHref(request);

	String instId = SmartUtil.getSpaceIdFromContentContext(cid);
	String workId = request.getParameter("workId");
	String formId = request.getParameter("formId");
	String taskInstId = request.getParameter("taskInstId");
	if(SmartUtil.isBlankObject(workId) && !SmartUtil.isBlankObject(formId)) workId = smartWorks.getWorkIdByFormId(formId);
	
	InformationWorkInstance instance  = null;
	boolean isTempSaveWork = CommonUtil.toBoolean(request.getParameter("isTempSaveWork"));
	String tempSavedTaskId = null;
	if (isTempSaveWork) {
		tempSavedTaskId = (String)request.getParameter("tempSaveTaskId");
		instance = (InformationWorkInstance)smartWorks.getSavedWorkInstanceById(SmartWork.TYPE_INFORMATION, workId, tempSavedTaskId);
	} else {
		instance = (InformationWorkInstance)smartWorks.getWorkInstanceById(SmartWork.TYPE_INFORMATION, workId, instId);
	}
	
	User owner = instance.getOwner();
	WorkSpace workSpace = instance.getWorkSpace();
	InformationWork work = (InformationWork)instance.getWork();
	int numberOfRelatedWorks = instance.getNumberOfRelatedWorks();
	int numberOfUpdateHistories = instance.getNumberOfUpdateHistories();
	int numberOfForwardHistories = instance.getNumberOfForwardHistories();
	int numberOfDownloadHistories = instance.getNumberOfDownloadHistories();
	TaskInstanceInfo[] tasks = instance.getTasks();
	TaskInstanceInfo approvalTask = null;
	TaskInstanceInfo forwardedTask = null;
	String approvalTaskInstId = null;
	if(tasks != null && instance.getStatus() != Instance.STATUS_REJECTED && instance.getStatus() != Instance.STATUS_REJECTED){
		if(!SmartUtil.isBlankObject(taskInstId)){
			for(TaskInstanceInfo task : tasks){
				if(task.isRunningForwardedForMe(cUser.getId(), taskInstId)){
					forwardedTask = task;
					break;
				}else if(task.isRunningApprovalForMe(cUser.getId(), taskInstId, null)){
					approvalTask = task;
					approvalTaskInstId = taskInstId;
					break;
				}
			}
		}
		if(SmartUtil.isBlankObject(taskInstId) && SmartUtil.isBlankObject(approvalTask)){
			approvalTask = instance.getMyRunningApprovalTask();
			if(!SmartUtil.isBlankObject(approvalTask)){
				taskInstId = approvalTask.getId();
				approvalTaskInstId = taskInstId;
			}else{
				approvalTask = instance.getApprovalTask();
				if(!SmartUtil.isBlankObject(approvalTask)){
					approvalTaskInstId = approvalTask.getId();
				}
			}
		}
		if(SmartUtil.isBlankObject(taskInstId) && SmartUtil.isBlankObject(forwardedTask)){
			forwardedTask = instance.getMyRunningForwardedTask();
			if(!SmartUtil.isBlankObject(forwardedTask)){
				taskInstId = forwardedTask.getId();
			}			
		}
	}
	if(SmartUtil.isBlankObject(forwardedTask) && (instance.getStatus() == Instance.STATUS_REJECTED || !instance.isApprovalWork())){
		taskInstId = "";
		approvalTask = null;
	}
	
	String lastWid = (String)session.getAttribute("wid");
	
 	session.setAttribute("cid", cid);
	if(SmartUtil.isBlankObject(wid))
		session.removeAttribute("wid");
	else
		session.setAttribute("wid", wid);

 	session.setAttribute("workInstance", instance);
	session.setAttribute("workSpaceId", instance.getId());
	session.removeAttribute("workSpace");
	session.setAttribute("tasks", tasks);
	if(SmartUtil.isBlankObject(forwardedTask)){
		session.removeAttribute("forwardTask");
	}else{
		session.setAttribute("forwardTask", forwardedTask);		
		numberOfForwardHistories--;
	}
		
	// 현재 사용자가 속해있는 부서나 커뮤너티 목록들을 가져온다..
	CommunityInfo[] communities = smartWorks.getMyCommunitiesForUpload(workId);
	
	CompanyOption companyOption = SmartUtil.getCompanyOption();
	
	String currentHref = SmartUtil.getCurrentHref(request, "iwork_space.sw");
	
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />
<!-- 컨텐츠 레이아웃-->
     <div class="section_portlet js_iwork_space_page" currentHref="<%=currentHref %>" lastHref="<%=lastHref%>" workId="<%=workId%>" instId="<%=instId%>" isTempSaved="<%=instance.isTempSaved() %>"  <%if(!CommonUtil.isEmpty(tempSavedTaskId)){ %>tempSavedId=<%=tempSavedTaskId%><%} %> repeatEventId="<%=instance.getRepeatEventId()%>">

 		<div class="portlet_t">
      		<div class="portlet_tl"></div>
    	</div>
    	<div class="portlet_l" style="display: block;">
		    <ul class="portlet_r" style="display: block;">	
		    	<!-- 타이틀 -->	    		            
            	<div class="body_titl_pic js_form_header">		
                        <div class="noti_pic mr7"><a class="js_pop_user_info" href="<%=instance.getOwner().getSpaceController() %>?cid=<%=instance.getOwner().getSpaceContextId()%>&wid=<%=instance.getOwner().getId() %>" userId="<%=instance.getOwner().getId()%>" longName="<%=instance.getOwner().getLongName() %>" minPicture="<%=instance.getOwner().getMinPicture() %>" profile="<%=instance.getOwner().getOrgPicture()%>" userDetail="<%=SmartUtil.getUserDetailInfo(instance.getOwner().getUserInfo())%>"><img class="profile_size_m" src="<%=instance.getOwner().getMidPicture() %>" /></a></div>
                        <div class="noti_in_bodytitle case_2line">
                        	<div>
	                            <span class="t_name"><%=instance.getOwner().getLongName()%></span>
	                            <%if(workSpace != null && !workSpace.getId().equals(owner.getId())){ %><span class="arr">▶</span>
	                            	<span class="<%=workSpace.getIconClass()%> fix_pos">
	                            		<%if(workSpace.getSpaceType() != ISmartWorks.SPACE_TYPE_WORK_INSTANCE || SmartUtil.isBlankObject(workSpace.getInstanceWork())){ %>
	                            			<a href='<%=workSpace.getSpaceController()%>?cid=<%=workSpace.getSpaceContextId()%>'><%=workSpace.getName() %></a>
	                            		<%}else{
	                            			WorkInfo instanceWork = (WorkInfo)workSpace.getInstanceWork();
	                            		%>
											<a href='<%=workSpace.getSpaceController()%>?cid=<%=workSpace.getSpaceContextId()%>&workId=<%=instanceWork.getId()%>&wId=<%=workSpace.getInstanceWorkSpaceId()%>'>
												<span class='tb'><%=workSpace.getName()%></span> 
											</a>
	                            		<%} %>
	                            	</span><%} %>
	                            <span class="t_date"><%=instance.getCreatedDate().toLocalString() %></span>
                            </div>
                            <div>
	                           	<span class="<%=work.getIconClass() %> t_date"> <%=work.getFullpathName() %>
	                           		<span class="title_picico ml5"><%=instance.getSubject()%></span>
	                           	</span>
	                           	
                           	</div>
                        </div>
		                    
		            <!-- 우측 버튼-->
		            <div class="fr"  style="line-height: 30px">
		            
			            <!-- 주소복사 -->
			            <div class="txt_btn t_s11 h_auto fr" style="visibility: hidden">
			                <a id="js_copy_address" href=""><fmt:message key="common.button.copy_url"/></a>
			            </div>
		            
						<!-- 전자결재, 업무전달 버튼들 -->
						<div class="fr cb" style="height:22px">
<!-- 							<span class="js_progress_span"></span>
 -->							<%
							if(forwardedTask == null){
							%>
								<a href="" class="js_toggle_forward_btn" title="<fmt:message key='common.button.forward'/>"><span class="icon_forward_w"></span></a>
							<%
							}
							if(approvalTask == null && forwardedTask == null){
							%>
								<%
								if(instance.getOwner().getId().equals(cUser.getId())){
								%>
									<a href="" class="js_toggle_approval_btn" title="<fmt:message key='common.button.approval'/>"><span class="icon_approval_w"></span></a>
								<%
								}
								%>
							<%
							}
							%>
							<%
							if(cUser.isUseMail()){
							%>
		                		<a href="" class="<%if(companyOption.isHtmlWriterPlugined()){%>js_doc_writer_plugin<%}else{ %>js_email_content_btn<%} %>" docTarget="email" title="<fmt:message key='common.button.email'/>"><span class="icon_mail_w"></span></a>
		                	<%
		                	}
		                	%>
		                	<a class="<%if(companyOption.isHtmlWriterPlugined()){%>js_doc_writer_plugin<%}else{ %>js_print_content_btn <%} %>" href="" docTarget="print" title="<fmt:message key='common.button.print'/>"><span class="icon_print_w"></span></a>
		                	<%
		                	if(companyOption.isPdfWriterPlugined()){
		                	%>
		                		<a class="js_doc_writer_plugin" href="" docTarget="pdf" title="<fmt:message key='common.button.pdf'/>"><span class="icon_pdf_w"></span></a>
		                	<%
		                	}
		                	%>
						</div>
						<!-- 전자결재, 업무전달 버튼들 //-->
					</div>
					<!-- 우측 버튼//-->
		                    
                	<div class="solid_line cb"></div>
                </div>
		            <!-- 타이틀 -->
		            
				<!-- 업무전달화면이 나타나는 곳 -->
				<div class="js_form_task_forward  js_form_task" <%if(forwardedTask==null){ %>style="display:none"<%} %>>
					<%
					if(forwardedTask!=null){
					%>
						<jsp:include page="/jsp/content/upload/append_task_forward.jsp">
							<jsp:param value="<%=taskInstId %>" name="taskInstId"/>
							<jsp:param value="<%=forwardedTask.getForwardId() %>" name="forwardId"/>
						</jsp:include>
					<%
					}
					%>
				</div>
				
				<!--  전자결재화면이 나타나는 곳 -->
				<div class="js_form_task_approval js_form_task" <%if(approvalTask==null && !instance.isApprovalWork()){ %>style="display:none"<%} %>>
					<%
					if(approvalTask!=null || instance.isApprovalWork()){
					%>
						<jsp:include page="/jsp/content/upload/append_task_approval.jsp">
							<jsp:param value="<%=approvalTaskInstId %>" name="taskInstId"/>
						</jsp:include>
					<%
					}
					%>
				</div>
				
				<!-- 상세보기 컨텐츠 -->
				<div class="contents_space">				            
			       <div class="up js_form_content">      
			       </div>
				</div>
		
				<!-- 버튼 영역 -->
				<div class="glo_btn_space">
				
					<!-- 수정, 삭제버튼 -->
				    <div class="fr">
						<%
						boolean isEditableInstance = SwServiceFactory.getInstance().getCommunityService().isEditable_Board_EventWorkInstanceBySpacePolicy(instance);
						if(!instance.isTempSaved() && work.getEditPolicy().isEditableForMe(owner.getId()) && approvalTask==null && !instance.isApprovalWork() || isEditableInstance){
						%>
					        <span class="btn_gray js_btn_modify">
					        	<a href="" class="js_modify_iwork_instance">
						            <span class="txt_btn_start"></span>
						            <span class="txt_btn_center"><fmt:message key="common.button.modify"/></span>
						            <span class="txt_btn_end"></span>
					            </a>
					   		</span>
					   	<%
					   	}
						if(!instance.isTempSaved()){
					   	%>
				
					        <span class="btn_gray js_btn_do_forward" style="display:none">
					        	<a href="" class="js_forward_work_instance">
						            <span class="txt_btn_start"></span>
						            <span class="txt_btn_center"><fmt:message key="common.button.do_forward"/></span>
						            <span class="txt_btn_end"></span>
					            </a>
					   		</span>
					
					        <span class="btn_gray js_btn_reply_forward" style="display:none">
					        	<a href="" class="js_reply_forward">
						            <span class="txt_btn_start"></span>
						            <span class="txt_btn_center"><fmt:message key="common.button.reply_forward"/></span>
						            <span class="txt_btn_end"></span>
					            </a>
					   		</span>
					
					        <span class="btn_gray js_btn_do_approval" style="display:none">
					        	<a href="" class="js_approval_work_instance">
						            <span class="txt_btn_start"></span>
						            <span class="txt_btn_center"><fmt:message key="common.button.do_approval"/></span>
						            <span class="txt_btn_end"></span>
					            </a>
					   		</span>
					
					        <span class="btn_gray js_btn_approve_approval" style="display:none">
					        	<a href="" class="js_reply_approval">
						            <span class="txt_btn_start"></span>
						            <span class="txt_btn_center"><fmt:message key="common.button.approve_approval"/></span>
						            <span class="txt_btn_end"></span>
					            </a>
					   		</span>
					
					        <span class="btn_gray js_btn_submit_approval" style="display:none">
					        	<a href="" class="js_reply_approval">
						            <span class="txt_btn_start"></span>
						            <span class="txt_btn_center"><fmt:message key="common.button.submit_approval"/></span>
						            <span class="txt_btn_end"></span>
					            </a>
					   		</span>
					
					        <span class="btn_gray js_btn_return_approval" style="display:none">
					        	<a href="" class="js_reply_approval">
						            <span class="txt_btn_start"></span>
						            <span class="txt_btn_center"><fmt:message key="common.button.return_approval"/></span>
						            <span class="txt_btn_end"></span>
					            </a>
					   		</span>
					
					        <span class="btn_gray js_btn_reject_approval" style="display:none">
					        	<a href="" class="js_reply_approval">
						            <span class="txt_btn_start"></span>
						            <span class="txt_btn_center"><fmt:message key="common.button.reject_approval"/></span>
						            <span class="txt_btn_end"></span>
					            </a>
					   		</span>
					
					        <span class="btn_gray js_btn_do_email" style="display:none">
					        	<a href="" class="js_email_iwork_instance">
						            <span class="txt_btn_start"></span>
						            <span class="txt_btn_center"><fmt:message key="common.button.do_email"/></span>
						            <span class="txt_btn_end"></span>
					            </a>
					   		</span>
				
							<%
							if(work.getEditPolicy().isEditableForMe(owner.getId()) && approvalTask==null && !instance.isApprovalWork() || isEditableInstance){
							%>
								<span class="btn_gray ml5 js_btn_delete">
						        	<a href="" class="js_delete_iwork_instance">
							            <span class="txt_btn_start"></span>
							            <span class="txt_btn_center"><fmt:message key="common.button.delete"/></span>
							            <span class="txt_btn_end"></span>
							    	</a>
						   		</span>
						        <span class="btn_gray js_btn_save" style="display:none">
						        	<a href="" class="js_save_iwork_instance">
							            <span class="txt_btn_start"></span>
							            <span class="txt_btn_center"><fmt:message key="common.button.save"/></span>
							            <span class="txt_btn_end"></span>
						            </a>
						   		</span>
					   	<%
					   		}
						}else{
						%>
							<span class="btn_gray ml5"> 
								<!--  완료버튼을 클릭시 해당 업로드 화면페이지에 있는 submitForms()함수를 실행한다.. -->
								<a href="" class="js_complete_action"  onclick='submitForms();return false;'> 
									<span class="txt_btn_start"></span>
									<span class="txt_btn_center"><fmt:message key="common.button.complete"/></span> 
									<span class="txt_btn_end"></span> 
								</a>
							</span> 
									
							<span class="btn_gray ml5"> 
								<!--  완료버튼을 클릭시 해당 업로드 화면페이지에 있는 submitForms()함수를 실행한다.. -->
								<a href="" class="js_temp_save_action"  onclick='submitForms(true);return false;'> 
									<span class="txt_btn_start"></span>
									<span class="txt_btn_center"><fmt:message key="common.button.temp_save"/></span> 
									<span class="txt_btn_end"></span> 
								</a>
							</span> 
							
							<span class="btn_gray ml5">
					        	<a href="" class="js_delete_iwork_instance">
						            <span class="txt_btn_start"></span>
						            <span class="txt_btn_center"><fmt:message key="common.button.delete"/></span>
						            <span class="txt_btn_end"></span>
						    	</a>
					   		</span>
						<%
						}
					   	%>
				
						<span class="btn_gray ml5 js_btn_cancel" style="display:none">
				        	<a href="" class="js_cancel_iwork_instance">
					            <span class="txt_btn_start"></span>
					            <span class="txt_btn_center"><fmt:message key="common.button.cancel"/></span>
					            <span class="txt_btn_end"></span>
					    	</a>
				   		</span>
					</div>
					<!-- 수정, 삭제버튼 //--> 
					  					  
					<!--  접근권한 및 등록할 공간정보를 선택하는 박스들 -->
					<form name="frmAccessSpace" class="js_validation_required"  <%if(CommonUtil.isEmpty(tempSavedTaskId)){ %>style="display:none"<%} %>>
						<div id="" class="fr form_space">						
							<input name="selWorkSpaceType" type="hidden" value="<%=workSpace.getSpaceType()%>">
							<select name="selWorkSpace" class="js_select_work_space">
								<%
								if(!workId.equals(SmartWork.ID_BOARD_MANAGEMENT)){ 
								%>
									<option  <%if(workSpace.getId().equals(cUser.getId())){ %>selected<%} %> value="<%=cUser.getId()%>" workSpaceType="<%=ISmartWorks.SPACE_TYPE_USER%>"><fmt:message key="common.upload.space.self" /></option>
								<%
								}
								%>
								<optgroup class="js_optgroup_department" label="<fmt:message key="common.upload.space.department"/>">
									<%
									// 현재사용자가 속해있는 부서들을 선택하는 옵션들을 구성한다..
									for (CommunityInfo community : communities) {
										if (community.getClass().equals(DepartmentInfo.class)) {
									%>
											<option <%if(workSpace.getId().equals(community.getId())){ %>selected<%} %> value="<%=community.getId()%>"  workSpaceType="<%=ISmartWorks.SPACE_TYPE_DEPARTMENT%>"><%=community.getName()%></option>
									<%
										}
									}
									%>
								</optgroup>
								<optgroup class="js_optgroup_group" label="<fmt:message key="common.upload.space.group"/>">
									<%
									// 현재사용자가 속해있는 그룹들을 선택하는 옵션들을 구성한다..
									for (CommunityInfo community : communities) {
										if (community.getClass().equals(GroupInfo.class)) {
									%>
											<option <%if(workSpace.getId().equals(community.getId())){ %>selected<%} %> value="<%=community.getId()%>"  workSpaceType="<%=ISmartWorks.SPACE_TYPE_GROUP%>"><%=community.getName()%></option>
									<%
										}
									}
									%>
								</optgroup>
							</select>
						</div>
				
						<div id="" class="fr form_space">
							<!--  현재업무의 접근(읽기)권한 중에 선택가능한 권한들을 구성한다... -->
							<select name="selAccessLevel" class="js_select_access_level">
								<%
								// 읽기권한이 공개 이면, 공개, 비공개, 사용자 지정중에 선택할 수 있다..
								
								int workAccessLevel = (SmartUtil.isBlankObject(work.getAccessPolicy())) ? AccessPolicy.LEVEL_PUBLIC : work.getAccessPolicy().getLevel();
								int instanceAccessLevel = (SmartUtil.isBlankObject(instance.getAccessPolicy())) ? AccessPolicy.LEVEL_PUBLIC : instance.getAccessPolicy().getLevel();
								if (workAccessLevel == AccessPolicy.LEVEL_PUBLIC) {
								%>
									<option value="<%=AccessPolicy.LEVEL_PUBLIC%>" <%if(instanceAccessLevel == AccessPolicy.LEVEL_PUBLIC){%>selected<%}%>><fmt:message key="common.security.access.public" /></option>
									<option value="<%=AccessPolicy.LEVEL_PRIVATE%>" <%if(instanceAccessLevel == AccessPolicy.LEVEL_PRIVATE){%>selected<%}%>><fmt:message key="common.security.access.private" /></option>
									<option class="js_access_level_custom" value="<%=AccessPolicy.LEVEL_CUSTOM%>" <%if(instanceAccessLevel == AccessPolicy.LEVEL_CUSTOM){%>selected<%}%>><fmt:message key="common.security.access.custom" /></option>
								<%
								// 읽기권한이 사용자지정이면, 비공개 또는 사용자지정 중에서 선택할 수 있다..
								} else if (workAccessLevel == AccessPolicy.LEVEL_CUSTOM) {
								%>
									<option value="<%=AccessPolicy.LEVEL_PRIVATE%>" <%if(instanceAccessLevel == AccessPolicy.LEVEL_PRIVATE){%>selected<%}%>><fmt:message key="common.security.access.private" /></option>
									<option class="js_access_level_custom" value="<%=AccessPolicy.LEVEL_CUSTOM%>" <%if(instanceAccessLevel == AccessPolicy.LEVEL_CUSTOM){%>selected<%}%>><fmt:message key="common.security.access.custom" /></option>
								<%
								// 읽기권한이 비공개이면, 비공개만 해당된다...
								} else if (workAccessLevel == AccessPolicy.LEVEL_PRIVATE) {
								%>
									<option value="<%=AccessPolicy.LEVEL_PRIVATE%>" <%if(instanceAccessLevel == AccessPolicy.LEVEL_PRIVATE){%>selected<%}%>><fmt:message key="common.security.access.private" /></option>
								<%
								}
								%>
							</select>
						</div>
				
						<!-- 접근권한이 사용자지정인 경우에 공개할 사용자들을 선택하는 화면 -->
						<%
						if(workAccessLevel == AccessPolicy.LEVEL_PUBLIC || workAccessLevel == AccessPolicy.LEVEL_CUSTOM) {
						%>
							<div class="fr form_space js_access_level_custom" style="display:none">
								<span class="js_type_userField" fieldId="txtAccessableUsers" multiUsers="true">
									<div class="form_value">
										<div class="icon_fb_space">
											<div class="fieldline community_names js_community_names sw_required">
												<%
												if(workAccessLevel == AccessPolicy.LEVEL_CUSTOM){
													CommunityInfo[] communitiesToOpen = instance.getAccessPolicy().getCommunitiesToOpen();
													if(!SmartUtil.isBlankObject(communitiesToOpen)){
														for(CommunityInfo community : communitiesToOpen){
															String comName = (community.getClass().equals(UserInfo.class)) ? ((UserInfo)community).getLongName() : community.getName();
												%>
															<span class="js_community_item user_select" comId="<%=community.getId() %>" comName="<%=comName%>"><%=comName %><a class="js_remove_community" href="">&nbsp;x</a></span>		
												
												<%												
														}
													}
												}
												%>
												<input class="js_auto_complete" href="community_name.sw" type="text">
											</div>
											<div class="js_community_list com_list" style="display: none"></div>
											<span class="js_community_popup"></span><a href="" class="js_userpicker_button"><span class="icon_fb_users"></span></a>
										</div>
									</div>
								</span>
							</div>
						<%
						}
						%>
						<!-- 접근권한이 사용자지정인 경우에 공개할 사용자들을 선택하는 화면 //-->
						
					</form>
					<!--  접근권한 및 등록할 공간정보를 선택하는 박스들 //-->
					
					<!--  실행시 표시되는 프로그래스아이콘을 표시할 공간 -->
					<div class="fr form_space js_progress_span"></div>
					
					<div class="txt_btn task_information">
					    <div class="po_left"><fmt:message key="common.title.last_modification"/> :  
					    	<%
				    		User lastModifier = instance.getLastModifier();
				    		String userDetailInfo = SmartUtil.getUserDetailInfo(lastModifier.getUserInfo());
				    		%>
				    		<a class="js_pop_user_info" href="<%=lastModifier.getSpaceController() %>?cid=<%=lastModifier.getSpaceContextId()%>&wid=<%=lastModifier.getId() %>" userId="<%=lastModifier.getId()%>" longName="<%=lastModifier.getLongName() %>" minPicture="<%=lastModifier.getMinPicture() %>" profile="<%=lastModifier.getOrgPicture()%>" userDetail="<%=userDetailInfo%>"><img src="<%=lastModifier.getMinPicture() %>" class="profile_size_s" /> <%=lastModifier.getLongName() %></a>
					    	<span class="t_date"> <%= instance.getLastModifiedDate().toLocalString() %> </span>
					    </div>
					    <%if(numberOfUpdateHistories > 1){ %><div class="po_left pt3"><a href="" class="js_toggle_update_histories"><fmt:message key="common.title.update_history"/><span class="t_up_num">[<%=numberOfUpdateHistories %>]</span></a></div><%} %>
					    <%if(numberOfForwardHistories > 0){ %><div class="po_left pt3"><a href="" class="js_toggle_forward_histories"><fmt:message key="common.title.forward_history"/><span class="t_up_num">[<%=numberOfForwardHistories %>]</span></a></div><%} %>
					    <%if(numberOfRelatedWorks > 0){ %><div class="po_left pt3"><a href="" class="js_toggle_related_instances"><fmt:message key="common.title.refering_works"/><span class="t_up_num">[<%=numberOfRelatedWorks %>]</span></a></div><%} %>
					    <%if(numberOfDownloadHistories > 0){ %><div class="po_left pt3"><a href="" class="js_toggle_download_histories"><fmt:message key="common.title.download_history"/><span class="t_up_num">[<%=numberOfDownloadHistories %>]</span></a></div><%} %>
					</div>     

					<!-- 실행시 데이터 유효성 검사이상시 에러메시지를 표시할 공간 -->
					<div class="form_space sw_error_message js_space_error_message" style="text-align:right; color: red; line-height:20px"></div>
				</div>
				<!-- 버튼 영역 //-->     				
			
				<div class="js_instance_histories"></div>
			</ul>
		</div>
		<div class="portlet_b" style="display: block;"></div>
	</div> 
	<!-- 컨텐츠 레이아웃//-->
<script type="text/javascript">

	var mode = "view";
	<%
	boolean isApprovalTask = (SmartUtil.isBlankObject(approvalTaskInstId) || SmartUtil.isBlankObject(approvalTask)) ? false : (approvalTask.getTaskType() == TaskInstance.TYPE_APPROVAL_TASK_ASSIGNED && (approvalTask.getStatus() == Instance.STATUS_RUNNING || approvalTask.getStatus() == Instance.STATUS_DELAYED_RUNNING)) ? true : false;
	
	if((isApprovalTask && instance.getStatus() == Instance.STATUS_RETURNED) || instance.isTempSaved()){
	%>
		mode = "edit";
	<%
	}
	%>
	var iworkSpace = $('.js_iwork_space_page');
	var workId = iworkSpace.attr("workId");
	var instId = iworkSpace.attr("instId");
	var isTempSaved = "false";
	var taskInstId = "";
	<%
	if (isTempSaveWork) {
	%>
		isTempSaved = "true";
		taskInstId = "<%=instance.getId()%>";
	<%
	}
	%>
	var formContent = iworkSpace.find('div.js_form_content');
	new SmartWorks.GridLayout({
		target : formContent,
		mode : mode,
		workId : workId,
		recordId : instId,
		isTempSaved : isTempSaved,
		taskInstId : taskInstId,
		onSuccess : function(){
		}
	});
	
	<%
	if(SmartUtil.isBlankObject(approvalTask) && SmartUtil.isBlankObject(forwardedTask) && instance.isApprovalWork()){
	%>
		iworkSpace.find('.js_btn_approve_approval').hide().siblings().hide();			
	<%
	}
	if(numberOfForwardHistories>0){
	%>
		iworkSpace.find('.js_toggle_forward_histories').click();
	<%
	}	
	%>
</script>
	<%
	if (!isTempSaveWork) {
	%>
		<jsp:include page="/jsp/content/work/space/space_instance_list.jsp">
			<jsp:param value="<%=work.getId() %>" name="workId"/>
			<jsp:param value="<%=instId %>" name="instId"/> 
		</jsp:include>
<%
	}
%>
<!-- 목록 버튼 -->
<div class="tc">
	<div class="btn_gray" >
    	<a href="<%=instance.getWork().getController(lastWid) %>?cid=<%=instance.getWork().getContextId() %>&wid=<%=CommonUtil.toNotNull(lastWid) %>" class="js_content js_goto_work_list_btn"> 
    		<span class="txt_btn_start"></span> 
    		<span class="txt_btn_center"><fmt:message key="common.button.list"/></span> 
    		<span class="txt_btn_end"></span>
    	</a>
	</div>
</div>
<!-- 목록 버튼//-->
