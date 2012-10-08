
<!-- Name 			: my_departments.jsp												-->
<!-- Description	: 좌측의 Navigation Bar의 나의 커뮤너티에서 나의 커뮤너티그룹들을 보여주는 공간	-->
<!-- Author			: Maninsoft, Inc.													-->
<!-- Created Date	: 2011.9.													 		-->

<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.info.GroupInfo"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ page import="net.smartworks.model.community.*"%>
<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다.
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");

	// 서버에 현재사용자의 모든 커뮤너티그룹들을  가져온다... 
	GroupInfo[] groups = smartWorks.getMyGroups();
%>
<ul>
	<%
	if (groups != null) {
		for (GroupInfo group : groups) {
			boolean isPublic = group.isPublic();
			//사용자의 그룹이 공개인지 비공개인지 알수가 없어 이름뒤에 임시로 "공개", "비공개" 를 붙혀준다, 차후 이미지로 변경되어야 함
			String isPublicStr = "";
			if (isPublic) {
				isPublicStr = " (공개)";
			} else {
				isPublicStr = " (비공개)";
			}
	%>
			<li>
				<a href="<%=group.getSpaceController() %>?cid=<%=group.getSpaceContextId()%>&wid=<%=group.getId() %>" title="<%=group.getDesc()%>">
					<span class="icon_pe"><img src="<%=group.getMinPicture()%>" class="profile_size_s"></span> 
					<span class="nav_sub_area"><%=group.getName() + isPublicStr%></span>
				</a>
			</li>
	<%
		}
	}
	%>
</ul>
