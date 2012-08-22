
<!-- Name 			: append_task_forward.js							 -->
<!-- Description	: 새업무를 등록시 다른사용자에게 업무전달을 위한 화	      	 -->
<!-- Author			: Maninsoft, Inc.									 -->
<!-- Created Date	: 2011.9.											 -->

<%@page import="net.smartworks.model.instance.info.RequestParams"%>
<%@page import="net.smartworks.model.instance.info.InstanceInfoList"%>
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
	
	String forwardId = request.getParameter("forwardId");
	String taskInstId = request.getParameter("taskInstId");
	
	WorkInstance workInstance = null;
	TaskInstanceInfo[] tasks = null;
	TaskInstanceInfo forwardedTask = null;
	String workInstId = "";
	String subject = "";
	String content = "";
	String forwarderId = "";
	String forwarderName = "";
	String forwardDate = "";
	if(!SmartUtil.isBlankObject(forwardId)){
		workInstance = (WorkInstance)session.getAttribute("workInstance");
		RequestParams params = (RequestParams)request.getAttribute("requestParams");

		if(SmartUtil.isBlankObject(params)){
			params = new RequestParams();
			params.setPageSize(20);
			params.setCurrentPage(1);
		}
		InstanceInfoList instanceList = smartWorks.getForwardTasksById(forwardId, params);
		if(!SmartUtil.isBlankObject(instanceList)){
			tasks = (TaskInstanceInfo[])instanceList.getInstanceDatas();
		}
		if(!SmartUtil.isBlankObject(tasks)){
			TaskInstanceInfo forwardTask = tasks[0];
			subject = forwardTask.getSubject();
			content = forwardTask.getContent();
			forwarderId = forwardTask.getAssigner().getId();
			forwarderName = forwardTask.getAssigner().getLongName();
			forwardDate = forwardTask.getCreatedDate().toLocalDateTimeSimpleString();
		}		
	}
%>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!-- 업무계획하기 -->
<div class="js_append_task_forward_page" workInstId="<%=workInstId%>" forwardId="<%=forwardId%>" taskInstId="<%=taskInstId%>">
	<div class="forward_section">
		<div class="tit m0"><span><fmt:message key="common.button.forward"/></span></div>
	</div>
	
		<form name='frmTaskForward' class="form_layout js_validation_required">
			<!-- 업무전달을 위한 입력화면들을 자동으로 그려주는 곳 -->
			<!-- js_task_forward_fields : js/sw/sw-formFields.js 의 loadTaskForwardFields()에서 자동으로 화면을 그려준다. -->
			<div class="js_task_forward_fields" 
				subjectTitle="<fmt:message key='forward.title.subject'/>" subject="<%=CommonUtil.toNotNull(subject)%>" 
				forwardeeTitle="<fmt:message key='forward.title.forwardee'/>" 
				CommentsTitle="<fmt:message key='forward.title.comments' />" content="<%=CommonUtil.toNotNull(content)%>"
				forwarderTitle="<fmt:message key='forward.title.forwarder' />" forwarderId="<%=CommonUtil.toNotNull(forwarderId)%>" forwarderName="<%=CommonUtil.toNotNull(forwarderName)%>"
				forwardDateTitle="<fmt:message key='forward.title.forward_date' />" forwardDate="<%=CommonUtil.toNotNull(forwardDate)%>">
			</div>
			<%
			if(!SmartUtil.isBlankObject(forwardId) && !SmartUtil.isBlankObject(tasks)){
			%>
				<div class="list_reply">
				
					<div class="up_point_sgr pos_works"></div>
					<ul class="bg p10">
						<%
						for(TaskInstanceInfo task : tasks){
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
							if(task.getId().equals(taskInstId)){
								forwardedTask = task;
								continue;
							}
					%>
							<li class="sub_instance_list">
								<div class="det_title">	
									<span class="<%=statusImage%> vm" title="<fmt:message key='<%=statusTitle%>'/>" ></span>
									<a class="js_pop_user_info vm" href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>" userId="<%=owner.getId()%>" longName="<%=owner.getLongName() %>" minPicture="<%=owner.getMinPicture() %>" profile="<%=owner.getOrgPicture()%>" userDetail="<%=SmartUtil.getUserDetailInfo(owner)%>">
										<img src="<%=owner.getMinPicture()%>" class="profile_size_c"/>
									</a>
									<span class="txt_info">
										<div>
											<a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>">
												<span class="t_name"><%=owner.getLongName()%></span>
											</a>
											<span class="t_date"><%=task.getLastModifiedDate().toLocalString()%></span>
										</div>
										<div>fffff<%if(task.getStatus()==TaskInstance.STATUS_COMPLETED){ %><%=CommonUtil.toNotNull(task.getComments())%><%if(task.isNew()){ %><span class="icon_new"></span><%} %><%} %></div>
									</span>
								</div>
							</li>					
						<%
						}
						if(!SmartUtil.isBlankObject(forwardedTask)){
						%>
							<div class="sub_instance_list">
								<div class="det_title">	
									<span class="icon_status_running vm" title="<fmt:message key='content.status.running'/>" ></span>
									<img src="<%=cUser.getMinPicture()%>" class="profile_size_c"/>
						        	<span class="comment_box">
										<textarea style="width:79%" class="up_textarea" name="txtaCommentContent" placeholder="<fmt:message key='forward.message.leave_comment'/>"></textarea>
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
		<div class="dash_line"></div>
</div>
<!-- 업무계획하기 //-->
<script type="text/javascript">
	loadTaskForwardFields();
</script>
