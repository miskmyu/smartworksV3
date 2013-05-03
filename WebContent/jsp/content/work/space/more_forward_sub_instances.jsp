
<!-- Name 			: append_task_forward.js							 -->
<!-- Description	: 새업무를 등록시 다른사용자에게 업무전달을 위한 화	      	 -->
<!-- Author			: Maninsoft, Inc.									 -->
<!-- Created Date	: 2011.9.											 -->

<%@page import="org.springframework.util.StringUtils"%>
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
	int fetchCount = Integer.parseInt(request.getParameter("fetchCount"));
	
	
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
			params.setPageSize(100);
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

<div class="js_more_forward_sub_instances_page" workInstId="<%=workInstId%>" forwardId="<%=forwardId%>" taskInstId="<%=taskInstId%>">
	<%
	if(!SmartUtil.isBlankObject(forwardId) && !SmartUtil.isBlankObject(tasks)){
	%>
		<div class="list_reply" style="width:600px;margin-left:50px;margin-bottom:7px">
		
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
			%>
					<li>
						<div class="det_title">	
							<span class="<%=statusImage%> vm" title="<fmt:message key='<%=statusTitle%>'/>" ></span>
							<a class="js_pop_user_info vm" href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>&wid=<%=owner.getId() %>" userId="<%=owner.getId()%>" longName="<%=owner.getLongName() %>" minPicture="<%=owner.getMinPicture() %>" profile="<%=owner.getOrgPicture()%>" userDetail="<%=SmartUtil.getUserDetailInfo(owner)%>">
								<img src="<%=owner.getMinPicture()%>" class="profile_size_c"/>
							</a>
							<span class="txt_info">
								<div>
									<a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>&wid=<%=owner.getId()%>">
										<span class="t_name"><%=owner.getLongName()%></span>
									</a>
									<span class="t_date"><%=task.getLastModifiedDate().toLocalString()%></span>
								</div>
								<div><%if(task.getStatus()==TaskInstance.STATUS_COMPLETED){ %><%=CommonUtil.toNotNull(task.getComments())%><%if(task.isNew()){ %><span class="ml5 icon_new"></span><%} %><%} %></div>
							</span>
						</div>
					</li>					
				<%
				}
				%>
			</ul>
		</div>
	<%
	}
	%>					
</div>
