
<!-- Name 			: worklist_by_category.jsp										 	 		-->
<!-- Description	: 좌측의 Navigation Bar의 나의 공간에서 전체업무의 하위 카테고리의 업무들을 보여주는 공간 -->
<!-- Author			: Maninsoft, Inc.															-->
<!-- Created Date	: 2011.9.													 				-->

<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.model.work.info.WorkInfo"%>
<%@page import="net.smartworks.model.work.info.SmartWorkInfo"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ page import="net.smartworks.model.work.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다.
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();
	
	// 서버에서 전달된 카테고리아이디를 가지고 하위 카테고리와 업무들을 가져온다...
	WorkInfo[] works = smartWorks.getMyAllWorksByCategoryId(request.getParameter("categoryId"));
	String classType = "js_content", workContext = null, targetContent = null;
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<ul>
	<%
	if (works != null) {
		for (WorkInfo work : works) {
			
			// 카테고리가 아닌경우는 업무이니, 클릭하면 업무목록공간으로 이동하게 한다...
			if (work.getType() != WorkCategory.TYPE_CATEGORY) {
	%>
				<li class="fvrt_item">
					
					<a href="<%=work.getController()%>?cid=<%=work.getContextId()%>" class="<%=classType%>">
						<span class="<%=work.getIconClass()%>"></span>
						<span><%=work.getName()%></span>
					</a>
					<div class="check_option"><div title="<fmt:message key='nav.works.my_favorite_works'/>" class="js_check_favorite_work icon_fvrt <%if(((SmartWorkInfo)work).isFavorite()){ %> checked <%} %>" workId="<%=work.getId() %>" ></div></div>
				</li>
				
			<%
			
			// 카테고리인 경우에는 드릴다운하여 하위업무나 카테고리를 선택할수 있게 한다...
			} else {
			%>
				<!--  *** js_drill_down : sw_act_work.js에서 이클래스의 클릭이벤트를 받아서 트리구조르 드릴다운할수 있게 한다.. -->
				<li class="js_drill_down">
					<a href="my_worklist_by_category.sw" categoryId="<%=work.getId()%>">
						<span class="<%=work.getIconClass()%>"></span>
						<span><%=work.getName()%></span>
					</a>
					<div class="js_drill_down_target" style="display: none"></div>
				</li>
	<%
			}
		}
	}
	%>
</ul>