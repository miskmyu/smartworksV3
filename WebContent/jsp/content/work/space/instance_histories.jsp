
<!-- Name 			: append_task_forward.js							 -->
<!-- Description	: 새업무를 등록시 다른사용자에게 업무전달을 위한 화	      	 -->
<!-- Author			: Maninsoft, Inc.									 -->
<!-- Created Date	: 2011.9.											 -->

<%@page import="net.smartworks.model.instance.info.WorkInstanceInfo"%>
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
	
	TaskInstanceInfo[] histories = ((TaskInstanceInfo[])session.getAttribute("tasks")).clone();
	
	
%>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!-- 업무계획하기 -->
<div class="js_instance_histories_page">
	<%
	if(!SmartUtil.isBlankObject(histories)){
	%>
		<div class="reply">
			<div class="up_point_sgr pos_works"></div>
			<ul class="bg p10">
				<%
				for(int i=0; i<histories.length; i++){
					TaskInstanceInfo task = histories[i];
					if(SmartUtil.isBlankObject(task)) continue;
					UserInfo owner = task.getAssignee();
					WorkInstanceInfo workInstance = task.getWorkInstance();
					String statusImage = "";
					String statusTitle = "";
					String activity = "";
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
					if(!SmartUtil.isBlankObject(task.getApprovalId())){
						activity = "전자결재";
				%>
							<li class="sub_instance_list">
								<span class="<%=statusImage%> vm fl" title="<fmt:message key='<%=statusTitle%>'/>" ></span>
								<span class="approval_stage"><%=activity %></span>
								<a class="js_pop_user_info noti_pic" href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>" userId="<%=owner.getId()%>" longName="<%=owner.getLongName() %>" minPicture="<%=owner.getMinPicture() %>" profile="<%=owner.getOrgPicture()%>" userDetail="<%=SmartUtil.getUserDetailInfo(owner)%>">
									<img src="<%=owner.getMinPicture()%>" class="profile_size_c"/>
								</a>
								<span class="fl">
									<a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>">
										<span class="t_name"><%=owner.getLongName()%></span>
									</a>
									<span class="t_date"><%=task.getLastModifiedDate().toLocalString()%></span>
 									<div><%=task.getSubject()%><%if(task.isNew()){ %><span class="ml5 icon_new"></span><%} %></div>
								</span>
							</li>					
				<%						
					}else if(!SmartUtil.isBlankObject(task.getForwardId())){
						
					}else{
						switch(task.getTaskType()){
						case TaskInstance.TYPE_INFORMATION_TASK_CREATED:
							activity = "최초 생성";
							break;
						case TaskInstance.TYPE_INFORMATION_TASK_UPDATED:
							activity = "수정";
							break;
						case TaskInstance.TYPE_INFORMATION_TASK_ASSIGNED:
							activity = "할당";
							break;
						case TaskInstance.TYPE_INFORMATION_TASK_DELETED:
							activity = "삭제";
							break;
						
						}
				%>
						<li class="sub_instance_list">
							<span class="<%=statusImage%> vm fl" title="<fmt:message key='<%=statusTitle%>'/>" ></span>
							<span class="approval_stage"><%=activity %></span>
							<a class="js_pop_user_info noti_pic" href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>" userId="<%=owner.getId()%>" longName="<%=owner.getLongName() %>" minPicture="<%=owner.getMinPicture() %>" profile="<%=owner.getOrgPicture()%>" userDetail="<%=SmartUtil.getUserDetailInfo(owner)%>">
								<img src="<%=owner.getMinPicture()%>" class="profile_size_c"/>
							</a>
							<span class="fl">
								<a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>">
									<span class="t_name"><%=owner.getLongName()%></span>
								</a>
								<span class="t_date"><%=task.getLastModifiedDate().toLocalString()%></span>
								<div><%if(task.isNew()){ %><span class="ml5 icon_new"></span><%} %></div>
							</span>
						</li>					
				<%
					}
				}
				%>
			</ul>
		</div>
	<%
	}
	%>					
</div>
