
<!-- Name 			: append_task_forward.js							 -->
<!-- Description	: 새업무를 등록시 다른사용자에게 업무전달을 위한 화	      	 -->
<!-- Author			: Maninsoft, Inc.									 -->
<!-- Created Date	: 2011.9.											 -->

<%@page import="net.smartworks.util.SmartMessage"%>
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
		<div class="up_point pos_works"></div> 
            
		<!-- 컨텐츠 -->
		   	<div class="form_wrap up history_list">    
			    <!-- 리스트 -->       
				<ul class="p10">
					<%
					int historyCount = 0;
					for(int i=0; i<histories.length; i++){
						TaskInstanceInfo task = histories[i];
						if(SmartUtil.isBlankObject(task)) continue;
						historyCount++;
						UserInfo owner = task.getAssignee();
						WorkInstanceInfo workInstance = task.getWorkInstance();
						String statusImage = "";
						String statusTitle = "";
						String activity = "";
						String forwardedUserName = "";
						int forwardedUserCount = 0;
						if(!SmartUtil.isBlankObject(task.getApprovalId())){
							for(int j=i+1; j<histories.length; j++){
								if(task.getApprovalId().equals(histories[j].getApprovalId())){
									if(histories[j].getTaskType() == TaskInstance.TYPE_APPROVAL_TASK_ASSIGNED){
										task.setStatus(histories[j].getStatus());
									}else if(histories[j].getTaskType() == TaskInstance.TYPE_APPROVAL_TASK_FORWARDED){
										if(SmartUtil.isBlankObject(forwardedUserName))
											forwardedUserName = task.getAssignee().getLongName();
										else
											forwardedUserCount++;
									}
									histories[j] = null;
								}

							}
						}else if(!SmartUtil.isBlankObject(task.getForwardId())){
							for(int j=i+1; j<histories.length; j++){
								if(task.getForwardId().equals(histories[j].getForwardId())){
									if(histories[j].getStatus()!= Instance.STATUS_COMPLETED) task.setStatus(histories[j].getStatus());
									histories[j] = null;
									forwardedUserCount++;
								}

							}
						}
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
							owner = task.getAssigner();
					%>
							<li class="sub_instance_list js_show_instance" instanceId="<%=workInstance.getId() %>" taskInstId="<%=task.getId() %>" formId="<%=task.getFormId()%>" isApproval="true">
								<div class="det_title" style="line-height: 16px">
						        	<span class="number"><%=historyCount %></span>
						            <span class="<%=statusImage %> vm" title="<fmt:message key='statusTitle'/>"></span>
						            <span class="task_state">
						            	<span class="icon_txt blue"><fmt:message key='common.button.approval'/></span>
						            </span>
						            <a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>">
						                <img class="profile_size_c" src="<%=owner.getMinPicture()%>">
						            </a>
						            <span class="vm">
						                <div>
						                    <a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>">
						                        <span class="t_name"><%=owner.getLongName()%></span>
						                    </a>
						                    <%
						                    if(!SmartUtil.isBlankObject(forwardedUserName)){ 
						                    %>
						                    	<span class="t_gray">/ [<fmt:message key="common.title.cc_receivers"/>]<%=forwardedUserName%><%if(forwardedUserCount>0){ %><fmt:message key="content.sentence.with_other_users"><fmt:param><%=forwardedUserCount %></fmt:param></fmt:message><%} %></span>
						                    <%
						                    } 
						                    %>
						                    <span class="ml5 t_date"><%=task.getLastModifiedDate().toLocalDateTimeSimpleString()%></span>
						                </div>
						                <div class="tb"><%=task.getSubject()%><%if(task.isNew()){ %><span class="ml5 icon_new"></span><%} %></div>
						            </span>
					            </div>
							</li>					
						<%						
						}else if(!SmartUtil.isBlankObject(task.getForwardId())){
							owner = task.getAssigner();
						%>
							<li class="sub_instance_list js_show_instance" instanceId="<%=workInstance.getId() %>" taskInstId="<%=task.getId() %>" formId="<%=task.getFormId()%>" isForward="true">
								<div class="det_title" style="line-height: 16px">
						        	<span class="number"><%=historyCount %></span>
						            <span class="<%=statusImage %> vm" title="<fmt:message key='statusTitle'/>"></span>
						            <span class="task_state">
						            	<span class="icon_txt orange"><fmt:message key='common.button.forward'/></span>
						            </span>
						            <a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>">
						                <img class="profile_size_c" src="<%=owner.getMinPicture()%>">
						            </a>
						            <span class="vm">
						                <div>
						                    <a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>">
						                        <span class="t_name"><%=owner.getLongName()%></span>
						                    </a>
						                    <span class="t_gray"><span class="arr">▶</span><%=task.getOwner().getLongName() %><%if(forwardedUserCount>0){ %><fmt:message key="content.sentence.with_other_users"><fmt:param><%=forwardedUserCount %></fmt:param></fmt:message><%} %></span>
						                    <span class="ml5 t_date"><%=task.getLastModifiedDate().toLocalDateTimeSimpleString()%></span>
						                </div>
						                <div class="tb"><%=task.getSubject()%><%if(task.isNew()){ %><span class="ml5 icon_new"></span><%} %></div>
						            </span>
					            </div>
							</li>					
						<%						
						}else{
							if(i==0){
								activity = SmartMessage.getString("common.title.created");								
							}else{
								switch(task.getTaskType()){
								case TaskInstance.TYPE_INFORMATION_TASK_CREATED:
									activity = SmartMessage.getString("common.title.created");
									break;
								case TaskInstance.TYPE_INFORMATION_TASK_UPDATED:
									activity = SmartMessage.getString("common.title.updated");
									break;
								case TaskInstance.TYPE_INFORMATION_TASK_ASSIGNED:
									activity = SmartMessage.getString("common.title.assigned");
									break;
								case TaskInstance.TYPE_INFORMATION_TASK_DELETED:
									activity = SmartMessage.getString("common.title.deleted");
									break;								
								}
							}
						%>
							<li class="sub_instance_list js_show_instance" instanceId="<%=workInstance.getId() %>" taskInstId="<%=task.getId()%>" formId="<%=task.getFormId()%>">
								<div class="det_title" style="line-height: 16px">
						        	<span class="number"><%=historyCount %></span>
						            <span class="<%=statusImage %> vm" title="<fmt:message key='statusTitle'/>"></span>
						            <span class="task_state">
						            	<span class="icon_txt gray"><%=activity %></span>
						            </span>
						            <a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>">
						                <img class="profile_size_c" src="<%=owner.getMinPicture()%>">
						            </a>
						            <span class="vm">
						                <div>
						                    <a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>">
						                        <span class="t_name"><%=owner.getLongName()%></span>
						                    </a>
						                    <span class="ml5 t_date"><%=task.getLastModifiedDate().toLocalDateTimeSimpleString()%></span>
						                </div>
						                <div class="tb"><%=CommonUtil.toNotNull(task.getSubject())%><%if(task.isNew()){ %><span class="ml5 icon_new"></span><%} %></div>
						            </span>
						        </div>
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
