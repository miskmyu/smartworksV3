
<!-- Name 			: my_departments.jsp										 -->
<!-- Description	: 좌측의 Navigation Bar 의 나의 커뮤너티에서 나의 부서들을 보여주는 공간-->
<!-- Author			: Maninsoft, Inc.											 -->
<!-- Created Date	: 2011.9.													 -->

<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.info.DepartmentInfo"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ page import="net.smartworks.model.community.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다.
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();

	// 서버에 현재사용자의 모든 부서(상위부서들 포함)들을  가져온다... 
	DepartmentInfo[] departments = smartWorks.getMyDepartments();
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<ul>
	<%
	if (departments != null) {
		for (DepartmentInfo department : departments) {
	%>
			<li class="fvrt_item">
				<a href="<%=department.getSpaceController() %>?cid=<%=department.getSpaceContextId()%>&wid=<%=department.getId() %>" title="<%=CommonUtil.toNotNull(department.getDesc())%>">
					<span class="icon_pe"><img src="<%=department.getMinPicture()%>" class="profile_size_s"></span> 
					<span class="nav_sub_area"><%=department.getName()%></span>
					<div class="check_option"><div title="<fmt:message key='nav.works.my_favorite_works'/>" class="js_check_favorite_com icon_fvrt <%if(department.isFavorite()){ %> checked <%} %>" comId="<%=department.getId() %>" ></div></div>
				</a>
			</li>
	<%
		}
		}
	%>
</ul>
