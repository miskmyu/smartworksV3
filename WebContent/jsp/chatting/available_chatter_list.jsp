<%@page import="net.smartworks.model.instance.info.InstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.TaskInstanceInfo"%>
<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.model.instance.info.EventInstanceInfo"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ page import="net.smartworks.model.community.*"%>
<%@ page import="net.smartworks.model.notice.*"%>
<%@ page import="net.smartworks.model.instance.*"%>
<%@ page import="net.smartworks.model.work.*"%>
<%@ page import="net.smartworks.util.LocalDate"%>
<%
	User cUser = SmartUtil.getCurrentUser();
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	String userNaming = (String)session.getAttribute("userNaming");  
	boolean nickNameBase = SmartUtil.isBlankObject(userNaming) ? false : User.NAMING_NICKNAME_BASE.equals(userNaming) ? true : false;
	UserInfo[] chatters = smartWorks.getAvailableChatter(request);
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />
<ul>
	<%
	if(!SmartUtil.isBlankObject(chatters)) {
		for (UserInfo chatter : chatters) {
			if(chatter.getId().equals(cUser.getId()))
				continue;
			String userName = (nickNameBase) ? chatter.getNickName() : chatter.getLongName();
			String online = (chatter.isOnline()) ? "chat_online" : "chat_offline";
	%>
			<li>
				<a href="" comId="<%=chatter.getId() %>" userId="<%=chatter.getId() %>">
					<img class="mr2 profile_size_s" src="<%=chatter.getMinPicture()%>" title="<%=userName%>"><%=userName %>
						<span class="<%=online%>"></span>
				</a>
			</li>
	<%
		}
	}
	%>
</ul>