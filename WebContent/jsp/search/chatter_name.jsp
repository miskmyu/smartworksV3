<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.model.community.info.CommunityInfo"%>
<%@page import="net.smartworks.model.community.info.WorkSpaceInfo"%>
<%@ page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ page import="net.smartworks.model.community.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	User cUser = SmartUtil.getCurrentUser();

	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	String userNaming = (String)session.getAttribute("userNaming"); 
	boolean nickNameBase = SmartUtil.isBlankObject(userNaming) ? false : userNaming.equals(User.NAMING_NICKNAME_BASE) ? true : false;

	String key = request.getParameter("key");
	WorkSpaceInfo[] communities = smartWorks.searchCommunity(key);
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<ul>
	<%
	if(communities != null){
		for (CommunityInfo community : communities) {
			if(!community.getClass().equals(UserInfo.class)) continue;
			String picName = community.getMinPicture();
			String comName = "", comId = "", userId = "";
			String online = "chat_offline";
			if(community.getClass().equals(UserInfo.class)){
				UserInfo user = (UserInfo)community;
				if(user.getId().equals(cUser.getId())) continue;
				comName = (nickNameBase) ? user.getNickName() : user.getLongName();
				userId = user.getId();
				online = (user.isOnline()) ? "chat_online" : "chat_offline";
			}else{
				comName = community.getName();
				comId = community.getId();
			}
	%>
			<li>
				<a href="" comId="<%=comId %>" userId="<%=userId %>">
					<img class="mr2 profile_size_s" src="<%=picName%>" title="<%=comName%>"><%=comName %>
 					<span class="<%=online%>"></span>
				</a>
			</li>
	<%
		}
	}else{
	%>
		<li><span><fmt:message key="search.message.no_searched_data"/></span></li>
	<%
	}
	%>
</ul>

