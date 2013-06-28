<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.model.instance.TaskInstance"%>
<%@page import="net.smartworks.model.instance.info.TaskInstanceInfo"%>
<%@page import="net.smartworks.model.instance.WorkInstance"%>
<%@page import="net.smartworks.model.work.info.WorkInfo"%>
<%@page import="net.smartworks.model.instance.info.MemoInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.ImageInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.FileInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.EventInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.BoardInstanceInfo"%>
<%@page import="net.smartworks.model.community.info.GroupInfo"%>
<%@page import="net.smartworks.model.community.info.DepartmentInfo"%>
<%@page import="net.smartworks.model.community.info.WorkSpaceInfo"%>
<%@page import="net.smartworks.model.instance.info.WorkInstanceInfo"%>
<%@page import="net.smartworks.model.work.info.SmartWorkInfo"%>
<%@page import="net.smartworks.model.instance.info.CommentInstanceInfo"%>
<%@page import="net.smartworks.model.instance.Instance"%>
<%@page import="net.smartworks.model.instance.info.InstanceInfo"%>
<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.model.work.SmartWork"%>
<%@page import="net.smartworks.model.work.Work"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
try {
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();

	String forwardId = request.getParameter("forwardId");
	String taskInstId = request.getParameter("taskInstId");
	int fetchCount = Integer.parseInt(request.getParameter("fetchCount"));
	String toDate = request.getParameter("toDate");
	LocalDate to = SmartUtil.isBlankObject(toDate) ? null : LocalDate.convertLocalStringToLocalDate(toDate);
	TaskInstanceInfo[] subInstances = smartWorks.getSubInstancesInForward(forwardId, fetchCount, to);
	int instanceCount = smartWorks.getSubInstancesInForwardCount(forwardId);
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<%
if (subInstances != null) {
	String lastToDate = "";
	if(SmartUtil.isBlankObject(subInstances[subInstances.length-1])){
		lastToDate = subInstances[0].getLastModifiedDate().toLocalDateString2();
%>
		<li>
			<img class="repl_tinfo">
			<a href="sub_instances_in_forward.sw?forwardId=<%=forwardId %>&fetchCount=<%=WorkInstance.FETCH_MORE_SUB_INSTANCE %>&toDate=<%=lastToDate %>&taskInstId=<%=taskInstId %>" class="js_show_more_comments">
				<span><strong><fmt:message key="common.title.show_more_forwards"><fmt:param><%=instanceCount %></fmt:param><</fmt:message></strong></span>
			</a>
		</li>
<%
	}
	for(TaskInstanceInfo task : subInstances){
		if(SmartUtil.isBlankObject(task)) break;
		
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
			continue;
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
						<a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>">
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
}


} catch (Exception e) {
	e.printStackTrace();
}
%>
