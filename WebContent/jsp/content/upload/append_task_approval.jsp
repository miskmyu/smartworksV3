
<!-- Name 			: append_task_forward.js							 -->
<!-- Description	: 새업무를 등록시 다른사용자에게 업무전달을 위한 화	      	 -->
<!-- Author			: Maninsoft, Inc.									 -->
<!-- Created Date	: 2011.9.											 -->

<%@page import="net.smartworks.model.instance.Instance"%>
<%@page import="net.smartworks.model.approval.ApprovalLineInst"%>
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
	String approvalInstId = "";
	if(!SmartUtil.isBlankObject(taskInstId)){
		workInstance = (WorkInstance)session.getAttribute("workInstance");
		tasks = workInstance.getTasks();
		if(!SmartUtil.isBlankObject(tasks)){
			for(TaskInstanceInfo task : tasks){
				if(task.isRunningApprovalForMe(cUser.getId(), taskInstId)){
					approvalTask = task;
					approvalInstId = task.getApprovalId();
					subject = task.getSubject();
					content = task.getContent();
					break;
				}
			}
		}
	}
	
	ApprovalLine approvalLine = null;
	ApprovalLineInst approvalLineInst = null;
	if(!SmartUtil.isBlankObject(approvalInstId)){
		approvalLineInst = smartWorks.getApprovalLineInstById(approvalInstId);		
	}else{
		approvalLine = smartWorks.getApprovalLineById(approvalLineId);
	}

%>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<div class="js_append_task_approval_page" workInstId="<%=workInstId %>" approvalLineId="<%=approvalLineId %>" approvalInstId="<%=approvalInstId %>" taskInstId="<%=taskInstId%>" >
	<!-- 결재선 Section -->
	<div class="approval_section">
		<div class="tit"><span><fmt:message key="common.button.approval"/></span></div>
		<div class="approval_group">
			<div class="fr mb2">
                <div class="fl mr5"><%=approvalLine.getName() %></div>
				<a href="" class="js_pop_approval_line"><div class="fl icon_approval"></div></a>
			</div>
			<div class="js_select_approval_line"></div>
			<div class="cb">
				<form class="js_validation_required" name="frmApprovalLine">
					<input name="hdnApprovalLineId" value="<%=approvalLine.getId() %>" type="hidden">		
					<%
					if((!SmartUtil.isBlankObject(approvalLine) && !SmartUtil.isBlankObject(approvalLine.getApprovals())) 
							|| (!SmartUtil.isBlankObject(approvalLineInst) && !SmartUtil.isBlankObject(approvalLineInst.getApprovals()))){
						Approval[] approvals = null;
						if(!SmartUtil.isBlankObject(approvalLine)){
							approvals = approvalLine.getApprovals();	
						}else{
							approvals = approvalLineInst.getApprovals();
						}
						
						for(int i=0; i<approvals.length; i++){
							Approval approval = approvals[i];
					%>
						<!-- 결재선 -->
						<div class="approval_area">
							<div class="label"><%=approval.getName() %></div>
							<div class="approval"></div>
							<%
							if(SmartUtil.isBlankObject(approvalLineInst) && approval.getApproverType() == Approval.APPROVER_CHOOSE_ON_RUNNING){
							%>
								<div class="name">
									<div class="noti_pic">
										<img class="profile_size_s" src="images/no_user_picture_min.jpg">
									</div>
									<div class="noti_in up"> 결재자 선택
											<div class="t_name"></div>
											<div class="t_name"></div>
											<div class="t_date"></div>
									</div>
								</div>	
											
							<%
							}else if(!SmartUtil.isBlankObject(approval.getApprover())){
								User approver = approval.getApprover();
								String completedDateStr = (SmartUtil.isBlankObject(approval.getCompletedDate())) ? "" : approval.getCompletedDate().toLocalDateSimpleString();
								
							%>
								<div class="name">
									<div class="noti_pic">
										<img class="profile_size_s" title="images/no_user_picture_min.jpg">
									</div>
									<div class="noti_in">
										<div class="t_name"><a href="<%=approver.getSpaceController() %>?cid=<%=approver.getSpaceContextId() %>"><%=approver.getLongName() %></a></div>
										<div class="t_date"><%=completedDateStr %></div>
									</div>
									<%
									if(SmartUtil.isBlankObject(approvalLineInst)){
									%>
										<input name="usrLevelApprover<%=i+1 %>" value="<%=approver.getId() %>" type="hidden">
									<%
									}
									%>
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
