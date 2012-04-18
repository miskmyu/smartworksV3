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
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();

	String instanceId = request.getParameter("instanceId");
	int fetchCount = Integer.parseInt(request.getParameter("fetchCount"));
	InstanceInfo[] subInstances = (InstanceInfo[])session.getAttribute("subComments");
	if(SmartUtil.isBlankObject(subInstances))
		subInstances = smartWorks.getRecentSubInstancesInInstance(instanceId, fetchCount);

	if (subInstances != null) {
		for (InstanceInfo instance : subInstances) {
			CommentInstanceInfo comment = null;
			if(instance.getType() == Instance.TYPE_COMMENT)
				comment = (CommentInstanceInfo)instance;
			else
				continue;
	%>
			<!-- Reply-->
			<div class="reply_section">
				<div class="photo">
					<img src="<%=comment.getOwner().getMinPicture() %>"  class="profile_size_m"/>
				</div>
				<div class="reply_text">
					<span class="name"><%=comment.getOwner().getNickName() %> : </span><div><%=comment.getComment() %></div><div class="icon_date"><%=comment.getLastModifiedDate().toLocalString() %></div>
				</div>
			</div>
			<!-- Reply//-->
	
	<%
		}
	}
	%>
