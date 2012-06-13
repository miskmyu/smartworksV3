
<!-- Name 			: append_task_forward.js							 -->
<!-- Description	: 새업무를 등록시 다른사용자에게 업무전달을 위한 화	      	 -->
<!-- Author			: Maninsoft, Inc.									 -->
<!-- Created Date	: 2011.9.											 -->

<%@page import="net.smartworks.util.SmartMessage"%>
<%@page import="net.smartworks.model.instance.TaskInstance"%>
<%@page import="net.smartworks.util.LocalDate"%>
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

	WorkInstance workInstance = null; 
	TaskInstanceInfo[] tasks = null;
	TaskInstanceInfo approvalTask = null;
	String subject = "";
	String content = "";
	String workInstId = "";
	String approvalInstId = "";
	if(!SmartUtil.isBlankObject(taskInstId)){
		workInstance = (WorkInstance)session.getAttribute("workInstance");
		workInstId = workInstance.getId();
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
		if(approvalLineInst == null){
			approvalLineInst = new ApprovalLineInst(approvalInstId, "");
			Approval approval1 = new Approval("", Approval.APPROVER_CHOOSE_ON_RUNNING, cUser, 0, 0, 0 );
			approval1.setStatus(Instance.STATUS_DRAFTED);
			approval1.setCompletedDate(new LocalDate());
			Approval approval2 = new Approval("승인", Approval.APPROVER_CHOOSE_ON_RUNNING, cUser, 0, 0, 0 );
			approval2.setStatus(Instance.STATUS_COMPLETED);
			approval2.setCompletedDate(new LocalDate());
			Approval approval3 = new Approval("대표이사", Approval.APPROVER_CHOOSE_ON_RUNNING, cUser, 0, 0, 0 );
			approval2.setStatus(Instance.STATUS_COMPLETED);
			approval2.setCompletedDate(new LocalDate());
			approvalLineInst.setApprovals(new Approval[]{approval1, approval2, approval3});
		}
	}else{
		approvalLine = smartWorks.getApprovalLineById(null);
	}

%>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<div class="js_append_task_approval_page" workInstId="<%=workInstId %>" approvalInstId="<%=approvalInstId %>" taskInstId="<%=taskInstId%>" >
	<!-- 결재선 Section -->
	<div class="approval_section">
		<div class="tit"><span><fmt:message key="common.button.approval"/></span></div>
		<div class="approval_group">
			<%
			if(!SmartUtil.isBlankObject(approvalLine)){
			%>
				<div class="fr mb2">
	                <div class="fl mr5 js_approval_line_name"><%=approvalLine.getName() %></div>
					<a href="" class="js_pop_approval_line"><div class="fl icon_approval"></div></a>
				</div>
			<%
			}
			%>
			<div class="cb js_approval_line_box">
				<form class="js_validation_required" name="frmApprovalLine">
					<%if(!SmartUtil.isBlankObject(approvalLine)){ %><input name="hdnApprovalLineId" value="<%=approvalLine.getId() %>" type="hidden"><%} %>		
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
							String signPicture = approval.getApprover() != null ? approval.getApprover().getSignPicture() : "";
							String statusIcon = "";
							if(approval.getStatus() == Instance.STATUS_COMPLETED){
								statusIcon = "approval_status_completed_" + cUser.getLocale();
							}else if(approval.getStatus() == Instance.STATUS_RETURNED){
								statusIcon = "approval_status_returned_" + cUser.getLocale();
							}else if(approval.getStatus() == Instance.STATUS_REJECTED){
								statusIcon = "approval_status_rejected_" + cUser.getLocale();
							}else if(approval.getStatus() == Instance.STATUS_DRAFTED){
								statusIcon = "approval_status_drafted_" + cUser.getLocale();
							}
							
							String approvalName = (approval.getStatus()==Instance.STATUS_DRAFTED) ? SmartMessage.getString("approval.title.draft") : approval.getName();
					%>
						<!-- 결재선 -->
						<div class="approval_area">
							<div class="label"><%=approvalName %></div>
							<div class="approval <%=statusIcon%>"><%if(!SmartUtil.isBlankObject(signPicture)){ %><img src="<%=signPicture%>"/><%} %></div>
							<%
							if(SmartUtil.isBlankObject(approvalLineInst) && approval.getApproverType() == Approval.APPROVER_CHOOSE_ON_RUNNING){
							%>
								<a class="name js_selected_approver_info js_userpicker_button">
									<div class="noti_pic">
										<img class="profile_size_s" src="images/no_user_picture_min.jpg">
									</div>
									<div class="noti_in up">
										<fmt:message key="approval.title.select_approver"/>
									</div>
								</a>	
								<input name="usrLevelApprover<%=i+1 %>" value="" type="hidden">
											
							<%
							}else if(!SmartUtil.isBlankObject(approval.getApprover())){
								User approver = approval.getApprover();
								String completedDateStr = (SmartUtil.isBlankObject(approval.getCompletedDate())) ? "" : approval.getCompletedDate().toLocalDateTimeSimpleString();
								
							%>
								<div class="name">
									<div class="noti_pic">
										<img class="profile_size_s" src="<%=approver.getMinPicture()%>" title="<%=approver.getLongName()%>">
									</div>
									<div class="noti_in">
										<div class="t_name"><%=CommonUtil.toNotNull(approver.getPosition()) %></div>
										<div class="t_name"><%=approver.getName() %></div>
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
			<span class="js_community_popup"></span>
		</div>
	</div>
	<!-- 결재선 Section //-->
	<!-- 전자결재 화면이 나타나는 곳 -->
	<div class="">
		<form class="form_layout js_validation_required" name="frmTaskApproval">
			<div class="js_task_approval_fields"
				subjectTitle="<fmt:message key='approval.title.subject'/>" subject="<%=CommonUtil.toNotNull(subject)%>"
				forwardeeTitle="<fmt:message key='approval.title.forwardee'/>"
				CommentsTitle="<fmt:message key='approval.title.comments' />" content="<%=CommonUtil.toNotNull(content)%>">
			</div>
			<%
			if(!SmartUtil.isBlankObject(approvalInstId) && !SmartUtil.isBlankObject(tasks)){
			%>
				<div class="replay">
					<div class="up_point_sgr pos_works"></div>
					<ul class="bg p10">
						<%
						for(TaskInstanceInfo task : tasks){
							if(!task.getApprovalId().equals(approvalTask.getApprovalId())) continue;
							UserInfo owner = task.getAssignee();
							String statusImage = "";
							String statusTitle = "";
							switch (task.getStatus()) {
							// 인스턴스가 현재 진행중인 경우..
							case Instance.STATUS_RUNNING:
								statusImage = "icon_status_running";
								statusTitle = "content.status.running";
								break;
							// 인스턴스가 지연진행중인 경우....
							case Instance.STATUS_DELAYED_RUNNING:
								statusImage = "icon_status_d_running";
								statusTitle = "content.status.delayed_running";
								break;
							// 인스턴스가 반려된 경우...
							case Instance.STATUS_RETURNED:
								statusImage = "icon_status_returned";
								statusTitle = "content.status.returned";
								break;
								// 인스턴스가 반려된 경우...
							case Instance.STATUS_COMPLETED:
								statusImage = "icon_status_completed";
								statusTitle = "content.status.completed";
								break;
								// 기타 잘못되어 상태가 없는 경우..
							default:
								statusImage = "icon_status_not_yet";
								statusTitle = "content.status.not_yet";
							}
							if(approvalTask.getId().equals(task.getId()))
								continue;
					%>
								<li class="sub_instance_list">
										<span class="<%=statusImage%> tc vm" title="<fmt:message key='<%=statusTitle%>'/>" ></span>
										<span ><%=task.getName() %></span>
										<a class="js_pop_user_info" href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>" userId="<%=owner.getId()%>" profile="<%=owner.getOrgPicture()%>" userDetail="<%=SmartUtil.getUserDetailInfo(owner)%>">
											<img src="<%=owner.getMinPicture()%>" class="profile_size_c"/>
										</a>
										<span>
											<a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>">
												<span class="t_name"><%=owner.getLongName()%></span>
											</a>
											<span class="t_date"><%=task.getLastModifiedDate().toLocalString()%></span>
											<div><%if(task.getStatus()==TaskInstance.STATUS_COMPLETED){ %><%=CommonUtil.toNotNull(task.getComments())%><%if(task.isNew()){ %><span class="icon_new"></span><%} %><%} %></div>
										</span>
								</li>					
						<%
						}
						%>
						<li class="sub_instance_list">
								<span class="icon_status_running tc vm" title="<fmt:message key='content.status.running'/>" ></span>
								<span ><%=approvalTask.getName() %></span>
								<img src="<%=cUser.getMinPicture()%>" class="profile_size_c"/>
					        	<span class="comment_box">
									<textarea style="width:90%" class="up_textarea" name="txtaCommentContent" placeholder="<fmt:message key='approval.message.leave_comment'/>"></textarea>
					        	</span>								
						</li>
						
					</ul>
				</div>
			<%
			}
			%>					
		</form>
		<div class="dash_line"></div>
	</div>
</div>

<script type="text/javascript">
	loadTaskApprovalFields();
</script>
