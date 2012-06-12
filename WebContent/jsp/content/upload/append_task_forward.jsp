
<!-- Name 			: append_task_forward.js							 -->
<!-- Description	: 새업무를 등록시 다른사용자에게 업무전달을 위한 화	      	 -->
<!-- Author			: Maninsoft, Inc.									 -->
<!-- Created Date	: 2011.9.											 -->

<%@page import="net.smartworks.model.instance.TaskInstance"%>
<%@page import="net.smartworks.model.instance.Instance"%>
<%@page import="net.smartworks.model.community.info.UserInfo"%>
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
	TaskInstanceInfo forwardedTask = null;
	String subject = "";
	String content = "";
	String workInstId = "";
	String forwardId = "";
	if(!SmartUtil.isBlankObject(taskInstId)){
		workInstance = (WorkInstance)session.getAttribute("workInstance");
		tasks = workInstance.getTasks();
		if(!SmartUtil.isBlankObject(tasks)){
			for(TaskInstanceInfo task : tasks){
				if(task.isRunningForwardedForMe(cUser.getId(), taskInstId)){
					forwardedTask = task;
					forwardId = task.getForwardId();
					subject = task.getSubject();
					content = task.getContent();
					break;
				}
			}
		}
	}
	
%>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!-- 업무계획하기 -->
<div class="js_append_task_forward_page" workInstId="<%=workInstId%>" forwardId="<%=forwardId%>" taskInstId="<%=taskInstId%>">
	<div class="approval_section">
		<div class="tit"><span><fmt:message key="common.button.forward"/></span></div>
		<form name='frmTaskForward' class="form_layout js_validation_required">
			<%
			if(!SmartUtil.isBlankObject(forwardedTask)){
				UserInfo forwarder = forwardedTask.getAssigner();//;
			%>
			    <div class="po_left"><fmt:message key="common.title.forwarder"/>   
			    	<a href=""><img src="<%=forwarder.getMinPicture() %>" class="profile_size_s" /> <%=forwarder.getLongName() %></a>
			    	<span class="t_date"> <%= forwardedTask.getLastModifiedDate().toLocalString() %> </span>
			    </div>
			<%
			}
			%>
			<span>
				<!-- 업무전달을 위한 입력화면들을 자동으로 그려주는 곳 -->
				<!-- js_task_forward_fields : js/sw/sw-formFields.js 의 loadTaskForwardFields()에서 자동으로 화면을 그려준다. -->
				<div class="js_task_forward_fields" 
					subjectTitle="<fmt:message key='forward.title.subject'/>" subject="<%=CommonUtil.toNotNull(subject)%>" 
					forwardeeTitle="<fmt:message key='forward.title.forwardee'/>" 
					CommentsTitle="<fmt:message key='forward.title.comments' />" content="<%=CommonUtil.toNotNull(content)%>"> 
				</div>
				<div class="replay">
					<ul>
						<%
						if(!SmartUtil.isBlankObject(forwardedTask) && !SmartUtil.isBlankObject(tasks)){
							for(TaskInstanceInfo task : tasks){
								if(!task.getForwardId().equals(forwardedTask.getForwardId())) continue;
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
								if(forwardedTask.getId().equals(task.getId()))
									continue;
						%>
									<li class="sub_instance_list">
										<div class="noti_pic tc vm">
											<span class="<%=statusImage%>" title="<fmt:message key='<%=statusTitle%>'/>" ></span>
										</div>
										<div class="noti_pic">
											<a class="js_pop_user_info" href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>" userId="<%=owner.getId()%>" profile="<%=owner.getOrgPicture()%>" userDetail="<%=SmartUtil.getUserDetailInfo(owner)%>">
												<img src="<%=owner.getMinPicture()%>" class="profile_size_c"/>
											</a>
										</div>
										<div class="noti_in">
											<a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>">
												<span class="t_name"><%=owner.getLongName()%></span>
											</a>
											<span class="t_date"><%=task.getLastModifiedDate().toLocalString()%></span>
											<div><%if(task.getStatus()==TaskInstance.STATUS_COMPLETED){ %><%=CommonUtil.toNotNull(task.getComments())%><%if(task.isNew()){ %><span class="icon_new"></span><%} %><%} %></div>
										</div>
									</li>					
							<%
							}
							%>
							<li class="sub_instance_list">
								<div class="noti_pic">
									<span class="icon_status_running" title="<fmt:message key='content.status.running'/>" ></span>
								</div>
						        <div class="reply_input comment_box js_return_on_forward">
									<div class="noti_pic">
										<img src="<%=cUser.getMinPicture()%>" class="profile_size_c"/>
									</div>
									<div class="noti_in">
										<textarea style="width:95%" class="up_textarea" name="txtaCommentContent" placeholder="<fmt:message key='forward.message.leave_comment'/>"></textarea>
									</div>
						        </div>								
							</li>
							
						<%
						}
						%>
					</ul>
				</div>
			</span>
		</form>
		<div class="dash_line"></div>
	</div>
</div>
<!-- 업무계획하기 //-->
<script type="text/javascript">
	loadTaskForwardFields();
</script>
