
<!-- Name 			: my_favorite_works.jsp				 					 -->
<!-- Description	: 현재사용자의 자주가는 업무들을 보여주는 화면  					 -->
<!-- Author			: Maninsoft, Inc.										 -->
<!-- Created Date	: 2011.9.												 -->

<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.model.community.info.GroupInfo"%>
<%@page import="net.smartworks.model.community.info.DepartmentInfo"%>
<%@page import="net.smartworks.model.community.info.CommunityInfo"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.model.work.info.SmartWorkInfo"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ page import="net.smartworks.model.work.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 현재사용자 정보도 가져온다 
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();

	// 서버에서 현재사용자의 자주가는 업무들을 가져온다...
	CommunityInfo[] communities = smartWorks.getMyFavoriteCommunities();
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<ul>
	<%
	if (communities != null) {
		for (CommunityInfo community : communities) {
			if(community.getClass().equals(DepartmentInfo.class)){
				DepartmentInfo department = (DepartmentInfo)community;
	%>
				<li class="fvrt_item js_favorite_coms">
					<a href="<%=department.getSpaceController() %>?cid=<%=department.getSpaceContextId()%>&wid=<%=department.getId() %>" title="<%=CommonUtil.toNotNull(department.getDesc())%>">
						<span class="icon_pe"><img src="<%=department.getMinPicture()%>" class="profile_size_s"></span> 
						<span class="nav_sub_area"><%=department.getName()%></span>
						<div class="check_option"><div title="<fmt:message key='common.button.delete'/>" class="js_check_favorite_com btn_remove_work" comId="<%=community.getId() %>"></div></div>
					</a>
				</li>
			<%
			}else if(community.getClass().equals(GroupInfo.class)){
				GroupInfo group = (GroupInfo)community;	
				boolean isPublic = group.isPublic();			
			%>		
				<li class="fvrt_item js_favorite_coms">
					<a href="<%=group.getSpaceController() %>?cid=<%=group.getSpaceContextId()%>&wid=<%=group.getId() %>" title="<%=CommonUtil.toNotNull(group.getDesc())%>">
						<span class="icon_pe"><img src="<%=group.getMinPicture()%>" class="profile_size_s"></span> 
						<span class="nav_sub_area">
						<%=group.getName()%>
							<!-- 공개,비공개 아이콘: 공개일때만 style="display:none"주세요 -->
							<%
							if (!isPublic) {
							%>						
								<span class="icon_private"></span>
							<%
							}
							%>
							<!-- 공개,비공개 아이콘 //-->
						</span>
						<div class="check_option"><div title="<fmt:message key='common.button.delete'/>" class="js_check_favorite_com btn_remove_work" comId="<%=community.getId() %>"></div></div>
					</a>
				</li>
			<%
			}else if(community.getClass().equals(UserInfo.class)){
				UserInfo user = (UserInfo)community;	
			%>		
				<li class="fvrt_item js_favorite_coms">
					<a href="<%=user.getSpaceController() %>?cid=<%=user.getSpaceContextId()%>&wid=<%=user.getId() %>" title="<%=CommonUtil.toNotNull(user.getLongName())%>">
						<span class="icon_pe"><img src="<%=user.getMinPicture()%>" class="profile_size_s"></span> 
						<span class="nav_sub_area"><%=user.getLongName()%></span>
						<div class="check_option"><div title="<fmt:message key='common.button.delete'/>" class="js_check_favorite_com btn_remove_work" comId="<%=community.getId() %>"></div></div>
					</a>
				</li>
	<%
		 	}
		}
	}
	%>
</ul>