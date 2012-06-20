<%@page import="net.smartworks.model.community.Group"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 현재사용자 정보도 가져온다..
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();
	
	String cid = request.getParameter("cid");
	String groupId = SmartUtil.getSpaceIdFromContentContext(cid);
	Group group = smartWorks.getGroupById(groupId);

	session.setAttribute("cid", cid);
	session.setAttribute("wid", groupId);
	session.setAttribute("workSpace", group);
	session.setAttribute("lastLocation", "group_space.sw");
%>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<div class="js_group_space_page">
	<jsp:include page="/jsp/content/upload/select_upload_action.jsp"></jsp:include>
	
	<!-- 오늘, 내일 그리고 모레이후에 대한 달력과 이벤트들을 보여주는 화면 -->
	<jsp:include page="/jsp/content/today/three_days_event.jsp" />
	<!-- 오늘, 내일 그리고 모레이후에 대한 달력과 이벤트들을 보여주는 화면 //-->
	
	<div class="js_space_instance_list">
		<jsp:include page="/jsp/content/community/space/space_tab_timeline.jsp"></jsp:include>
	</div>
</div>