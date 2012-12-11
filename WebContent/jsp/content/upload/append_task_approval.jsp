
<!-- Name 			: append_task_forward.js							 -->
<!-- Description	: 새업무를 등록시 다른사용자에게 업무전달을 위한 화	      	 -->
<!-- Author			: Maninsoft, Inc.									 -->
<!-- Created Date	: 2011.9.											 -->

<%@page import="net.smartworks.util.SmartTest"%>
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
	String processTaskInstId = request.getParameter("processTaskInstId");
	String approvalLineId = request.getParameter("approvalLineId");

	TaskInstanceInfo approvalTask = null;
	String subject = "";
	String content = "";
	UserInfo[] forwardees = null;
	boolean isLazyReferenceTask = false;
	String approvalInstId = "";
	
	String workInstId = null;
	TaskInstanceInfo[] tasks = null;
	WorkInstance workInstance = (WorkInstance)session.getAttribute("workInstance");
	if (workInstance != null && SmartUtil.isBlankObject(approvalLineId)) {
		workInstId = workInstance.getId();
		tasks = workInstance.getTasks();
		if(!SmartUtil.isBlankObject(tasks)){
			for(TaskInstanceInfo task : tasks){
				if(task.isRunningApprovalForMe(cUser.getId(), taskInstId, processTaskInstId)){
					approvalTask = task;
					approvalInstId = task.getApprovalId();
					subject = task.getSubject();
					content = task.getContent();
					taskInstId = task.getId();
					break;
				}
			}
			if(SmartUtil.isBlankObject(approvalTask)){
				for(TaskInstanceInfo task : tasks){
					if(!SmartUtil.isBlankObject(task.getApprovalId()) && task.getStatus()!=Instance.STATUS_REJECTED){
						approvalInstId = task.getApprovalId();
						subject = task.getSubject();
						content = task.getContent();
						for(TaskInstanceInfo tsk : tasks){
							if(tsk.isRunningApprovalForMe(cUser.getId(), null, processTaskInstId)){
								approvalTask = tsk;
								taskInstId = tsk.getId();
								break;
							}
						}
						break;
					}
				}			
			}
		}
	}
	ApprovalLine approvalLine = null;
	ApprovalLineInst approvalLineInst = null;
	String drafter = "";
	String draftDate = "";
	if(!SmartUtil.isBlankObject(approvalInstId) && SmartUtil.isBlankObject(approvalLineId)){
		approvalLineInst = smartWorks.getApprovalLineInstById(approvalInstId);
		drafter = SmartUtil.getUsersHtml(new UserInfo[]{workInstance.getOwner().getUserInfo()});
		draftDate = workInstance.getCreatedDate().toLocalDateTimeSimpleString();
	}else if(!SmartUtil.isBlankObject(approvalLineId)){
		approvalLine = smartWorks.getApprovalLineById(approvalLineId);
	}else{
		approvalLine = smartWorks.getApprovalLineById(null);		
	}

	boolean isForwarded = SmartUtil.isBlankObject(approvalTask) ? false : (approvalTask.getTaskType() == TaskInstance.TYPE_APPROVAL_TASK_FORWARDED) ? true : false;
	String forwardId = SmartUtil.isBlankObject(approvalTask) ? "" : approvalTask.getForwardId();
	
	if(!SmartUtil.isBlankObject(workInstance)){
		forwardees = workInstance.getForwardees();
		isLazyReferenceTask = workInstance.isLazyreferenceTask();
	}
%>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<div class="js_append_task_approval_page" workInstId="<%=workInstId %>" approvalInstId="<%=approvalInstId %>" taskInstId="<%=taskInstId%>" forwardId="<%=forwardId%>">
	<!-- 결재선 Section -->
	<div class="approval_section">
		<div class="tit"><span><fmt:message key="common.button.approval"/></span></div>
		<div class="approval_group">
			<%
			if(!SmartUtil.isBlankObject(approvalLine)){
			%>
				<div class="" style="height: 20px">
					<%if(SmartUtil.isBlankObject(approvalLineId)){ %><a href="" class="js_pop_approval_line"><div class="fr icon_approval"></div></a><%} %>
	                <div class="fr mr5 js_approval_line_name"><%=approvalLine.getName() %></div>
				</div>
			<%
			}
			%>
			<div class="fr js_approval_line_box">
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
							String signPicture = (approval.getApprover() != null && approval.getApprover().isUseSignPicture() && approval.getStatus() == Instance.STATUS_COMPLETED) ? approval.getApprover().getSignPicture() : "";
							String statusIcon = "";
							if(approval.getStatus() == Instance.STATUS_COMPLETED){
								if(SmartUtil.isBlankObject(signPicture)) statusIcon = "approval_status approved_" + cUser.getLocale();
							}else if(approval.getStatus() == Instance.STATUS_RETURNED){
								statusIcon = "approval_status returned_" + cUser.getLocale();
							}else if(approval.getStatus() == Instance.STATUS_REJECTED){
								statusIcon = "approval_status rejected_" + cUser.getLocale();
							}else if(approval.getStatus() == Instance.STATUS_DRAFTED){
								if(SmartUtil.isBlankObject(signPicture)) statusIcon = "approval_status drafted_" + cUser.getLocale();
							}
					%>
							<!-- 결재선 -->
							<div class="approval_area js_approval_box">
								<div class="label"><%=approval.getName() %></div>
								<div class="approval <%=statusIcon%>"><%if(!SmartUtil.isBlankObject(signPicture)){ %><img src="<%=signPicture%>"/><%} %></div>
								<%
								if(SmartUtil.isBlankObject(approvalLineInst) && approval.getApproverType() == Approval.APPROVER_CHOOSE_ON_RUNNING){
								%>
									<div class="t_date tc"></div>
									<div class="name">
										<a class="js_selected_approver_info js_userpicker_button" href="">
											<span class="noti_pic">
												<img class="profile_size_s" src="images/no_user_picture_min.jpg">
											</span>
										</a>
										<span class="noti_in">
											<input class="js_auto_complete js_approver_box" href="user_name.sw" type="text" approverIndex="<%=i%>">
										</span>
										<input name="usrLevelApprover<%=i+1 %>" value="" type="hidden">
									</div>			
								<%
								}else if(!SmartUtil.isBlankObject(approval.getApprover())){
									User approver = approval.getApprover();
									String completedDateStr = (SmartUtil.isBlankObject(approval.getCompletedDate())) ? "" : approval.getCompletedDate().toLocalDateTimeSimpleString();
									
								%>
									<div class="t_date tc"><%=completedDateStr %></div>
									<div class="name">
										<span class="noti_pic">
											<img class="profile_size_s" src="<%=approver.getMinPicture()%>" title="<%=approver.getLongName()%>">
										</span>
										<span class="noti_in">
											<div class="t_name"><%=CommonUtil.toNotNull(approver.getPosition()) %></div>
											<div class="t_name"><%=approver.getName() %></div>
										</span>
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
			<span class="js_community_list srch_list_nowid" style="display:none"></span>
			<div class="js_community_popup"></div>
		</div>
	</div>
	<!-- 결재선 Section //-->
	<!-- 전자결재 화면이 나타나는 곳 -->
	<div class="" style="display:block">
		<form class="form_layout js_validation_required" name="frmTaskApproval">
			<div class="js_task_approval_fields"
				subjectTitle="<fmt:message key='approval.title.subject'/>" subject="<%=CommonUtil.toNotNull(subject)%>"
				forwardeeTitle="<fmt:message key='approval.title.forwardee'/>" actionRequired="<%=!SmartUtil.isBlankObject(approvalTask) %>" forwardees="<%=CommonUtil.toNotNull(SmartUtil.getUsersHtml(forwardees)) %>"
				isLazyReferenceTaskTitle="<fmt:message key='approval.title.lazy_reference_task'/>" isLazyReferenceTask="<%=isLazyReferenceTask %>"
				CommentsTitle="<fmt:message key='approval.title.comments' />" content="<%=CommonUtil.toNotNull(content)%>"
				drafterTitle="<fmt:message key='approval.title.drafter' />" drafter="<%=CommonUtil.toNotNull(drafter)%>"
				draftDateTitle="<fmt:message key='approval.title.draft_date' />" draftDate="<%=CommonUtil.toNotNull(draftDate)%>"
				<%
				boolean isReturned = false;
				if (workInstance != null)
					isReturned = (!isForwarded && workInstance.getStatus()==Instance.STATUS_RETURNED);
				%>
				isReturned="<%=isReturned%>" isForwarded="<%=isForwarded%>">
			</div>
			<%
			if(!SmartUtil.isBlankObject(approvalInstId) && !SmartUtil.isBlankObject(tasks)){
			%>
				<div class="list_reply">
						<div class="up_point_sgr pos_works"></div>
						<ul class="bg p10">
							<%
							for(TaskInstanceInfo task : tasks){
								if(!task.getApprovalId().equals(approvalInstId)) continue;
								if(task.getTaskType() == TaskInstance.TYPE_APPROVAL_TASK_FORWARDED && (task.getStatus() == TaskInstance.STATUS_COMPLETED || !task.isTaskForMe(cUser.getId()))) continue;
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
								case Instance.STATUS_REJECTED:
									statusImage = "icon_status_rejected";
									statusTitle = "content.status.rejected";
									break;
								default:
									statusImage = "icon_status_not_yet";
									statusTitle = "content.status.not_yet";
								}
								if(!SmartUtil.isBlankObject(approvalTask) && approvalTask.getId().equals(task.getId()))
									continue;
						%>
									<li class="sub_instance_list">
										<div class="det_title">	
											<span class="<%=statusImage%> vm" title="<fmt:message key='<%=statusTitle%>'/>" ></span>
											<span class="approval_stage"><%=task.getName() %></span>
											<a class="js_pop_user_info vm" href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>&wid=<%=owner.getId() %>" userId="<%=owner.getId()%>" longName="<%=owner.getLongName() %>" minPicture="<%=owner.getMinPicture() %>" profile="<%=owner.getOrgPicture()%>" userDetail="<%=SmartUtil.getUserDetailInfo(owner)%>">
												<img src="<%=owner.getMinPicture()%>" class="profile_size_c"/>
											</a>
											<span class="txt_info">
												<div>
													<a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>">
														<span class="t_name"><%=owner.getLongName()%></span>
													</a>
													<span class="t_date"><%=task.getLastModifiedDate().toLocalString()%></span>
												</div>
												<div><%if(task.getStatus()==TaskInstance.STATUS_COMPLETED 
															|| task.getStatus()==TaskInstance.STATUS_REJECTED 
															|| task.getStatus()==TaskInstance.STATUS_RETURNED){ %><%=CommonUtil.toNotNull(task.getComments())%><%if(task.isNew()){ %><span class="ml5 icon_new"></span><%} %><%} %>
												</div>
											</span>
										</div>
									</li>				
							<%
							}
							if(!SmartUtil.isBlankObject(approvalTask)){
							%>	
								<div class="sub_instance_list">
									<div class="det_title">	
										<span class="icon_status_running vm" title="<fmt:message key='content.status.running'/>" ></span>
										<span class="approval_stage"><%=approvalTask.getName() %></span>
										<img src="<%=cUser.getMinPicture()%>" class="profile_size_c"/>
							        	<span class="comment_box">
							        		<%if(isForwarded){ %>
												<textarea style="width:79%" class="up_textarea" name="txtaCommentContent" placeholder="<fmt:message key='forward.message.leave_comment'/>"></textarea>
											<%}else{ %>
												<textarea style="width:79%" class="up_textarea" name="txtaCommentContent" placeholder="<fmt:message key='approval.message.leave_comment'/>"></textarea>
											<%} %>
							        	</span>	
							       </div>							
								</div>
							<%
							}
							%>
						</ul>
				</div>
			<%
			}
			%>					
		</form>
		<%
		if(!SmartUtil.isBlankObject(approvalTask)){
		%>
	 		<jsp:include page="/jsp/content/upload/check_completion_notice.jsp">
	 			<jsp:param value="approvalWork" name="workType"/>
	 		</jsp:include>
	 	<%
	 	}
	 	%>
		<div class="dash_line"></div>
	</div>
</div>

<script type="text/javascript">
	loadTaskApprovalFields();
</script>
