
<!-- Name 			: my_departments.jsp												-->
<!-- Description	: 좌측의 Navigation Bar의 나의 커뮤너티에서 나의 커뮤너티그룹들을 보여주는 공간	-->
<!-- Author			: Maninsoft, Inc.													-->
<!-- Created Date	: 2011.9.													 		-->

<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.info.GroupInfo"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ page import="net.smartworks.model.community.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다.
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();

	// 서버에 현재사용자의 모든 커뮤너티그룹들을  가져온다... 
	GroupInfo[] groups = smartWorks.getMyGroups();
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<ul>
<!-- 	<li><a href="" class="tr">모든그룹목록</a></li>
 -->
	<%
	if (groups != null) {
		for (GroupInfo group : groups) {
			boolean isPublic = group.isPublic();
	%>
			<li class="fvrt_item">
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
					<div class="check_option"><div title="<fmt:message key='nav.works.my_favorite_works'/>" class="js_check_favorite_com icon_fvrt <%if(group.isFavorite()){ %> checked <%} %>" comId="<%=group.getId() %>" ></div></div>
				</a>
			</li>
	<%
		}
	}
	%>
</ul>
