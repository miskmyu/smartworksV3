
<!-- Name 			: append_task_forward.js							 -->
<!-- Description	: 새업무를 등록시 다른사용자에게 업무전달을 위한 화	      	 -->
<!-- Author			: Maninsoft, Inc.									 -->
<!-- Created Date	: 2011.9.											 -->

<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.model.approval.Approval"%>
<%@page import="net.smartworks.model.approval.ApprovalLine"%>
<%@page import="net.smartworks.model.instance.info.TaskInstanceInfo"%>
<%@page import="net.smartworks.model.instance.WorkInstance"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 현재사용자 정보도 가져온다..
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();

	String taskInstId = request.getParameter("taskInstId");
	String approvalLineId = request.getParameter("approvalLineId");

	WorkInstance workInstance = null;
	TaskInstanceInfo[] tasks = null;
	TaskInstanceInfo approvalTask = null;
	String subject = "";
	String content = "";
	String workInstId = "";
	String approvalId = "";
	if(!SmartUtil.isBlankObject(taskInstId)){
		workInstance = (WorkInstance)session.getAttribute("workInstance");
		tasks = workInstance.getTasks();
		if(!SmartUtil.isBlankObject(tasks)){
			for(TaskInstanceInfo task : tasks){
				if(task.isRunningApprovalForMe(cUser.getId(), taskInstId)){
					approvalTask = task;
					approvalId = task.getApprovalId();
					subject = task.getSubject();
					content = task.getContent();
					break;
				}
			}
		}
	}
	
	ApprovalLine approvalLine = smartWorks.getApprovalLineById(approvalId);

%>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<div class="js_append_task_approval_page" workInstId="<%=workInstId %>" approvalId="<%=approvalId %>" taskInstId="<%=taskInstId%>" >
	<!-- 결재선 Section -->
	<div class="approval_section">
		<div class="tit"><span><fmt:message key="common.button.approval"/></span></div>
		<div class="approval_group">
			<div class="fr mb2">
				<div class="fl icon_approval js_toggle_approval_line"></div>
                <div class="fl"><%=approvalLine.getName() %></div>
			</div>
			<!-- POP -->
			<div class="js_pop_approval_line"></div>
			<div class="cb">
				<form class="js_validation_required" name="frmApprovalLine">
					<input name="hdnApprovalLineId" value="<%=approvalLine.getId() %>" type="hidden">		
					<%
					if(!SmartUtil.isBlankObject(approvalLine) && !SmartUtil.isBlankObject(approvalLine.getApprovals())){
						Approval[] approvals = approvalLine.getApprovals();
						for(int i=0; i<approvals.length; i++){
							Approval approval = approvals[i];
					%>
						<!-- 결재선 -->
						<div class="approval_area">
							<div class="label"><%=approval.getName() %></div>
							<div class="approval"></div>
							<%
							if(approval.getApproverType() == Approval.APPROVER_CHOOSE_ON_RUNNING){
							%>
								<div class="name form_col js_type_userField" fieldId="usrLevelApprover<%=i+1 %>" multiUsers="false">
									<div class="form_value" style="width:100%">
										<div class="icon_fb_space" >
											<div class="fieldline community_names js_community_names sw_required">
												<input class="m0 js_auto_complete" style="width:0" disabled="disabled" href="user_name.sw" type="text">
											</div>
											<div class="js_community_list srch_list_nowid" style="display: none"></div>
											<span class="js_community_popup"></span>
											<a href="" class="js_userpicker_button"><span class="icon_fb_user"></span></a>
										</div>
									</div>
								</div>							
							<%
							}else if(!SmartUtil.isBlankObject(approval.getApprover())){
								User approver = approval.getApprover();
								
							%>
								<div class="name">
									<div class="noti_pic">
										<img class="profile_size_s" title="<%=approver.getLongName() %>" src="<%=approver.getMinPicture()%>">
									</div>
									<div class="noti_in">
										<div class="t_name"><a href="<%=approver.getSpaceController() %>?cid=<%=approver.getSpaceContextId() %>"><%=approver.getLongName() %></a></div>
										<div class="t_date"> 04.15 05:38</div>
									</div>
									<input name="usrLevelApprover<%=i+1 %>" value="<%=approver.getId() %>" type="hidden">
								</div>
							<%
							}
							%>
						</div>
						<!-- 결재선 //-->
					<%
						}
					}
					%>
				</form>
			</div>
		</div>
	</div>
	<!-- 결재선 Section //-->
	<!-- 전자결재 화면이 나타나는 곳 -->
	<div class="">
		<form class="form_layout js_validation_required" name="frmTaskApproval">
			<span> <!-- 업무전달을 위한 입력화면들을 자동으로 그려주는 곳 --> <!-- js_task_forward_fields : js/sw/sw-formFields.js 의 loadTaskForwardFields()에서 자동으로 화면을 그려준다. -->
				<div class="js_task_approval_fields"
					subjectTitle="<fmt:message key='approval.title.subject'/>" subject="<%=CommonUtil.toNotNull(subject)%>"
					forwardeeTitle="<fmt:message key='approval.title.forwardee'/>"
					CommentsTitle="<fmt:message key='approval.title.comments' />" content="<%=CommonUtil.toNotNull(content)%>">
				</div>
			</span>
		</form>
		<div class="dash_line"></div>
	</div>
</div>

<script type="text/javascript">
	loadTaskApprovalFields();
</script>
