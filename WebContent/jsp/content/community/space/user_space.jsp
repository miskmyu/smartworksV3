<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 현재사용자 정보도 가져온다..
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();
	
	String cid = request.getParameter("cid");
	String userId = SmartUtil.getSpaceIdFromContentContext(cid);
	User user = (cUser.getId().equals(userId)) ? cUser : smartWorks.getUserById(userId);
	session.setAttribute("cid", cid);
	session.setAttribute("wid", userId);
	session.setAttribute("workSpace", user);
	session.setAttribute("lastLocation", "user_space.sw");
%>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<div class="js_user_space_page">

	<jsp:include page="/jsp/content/community/space/space_title.jsp"></jsp:include>
		
	<jsp:include page="/jsp/content/upload/select_upload_action.jsp"></jsp:include>
	
	<div class="js_space_instance_list">
		<jsp:include page="/jsp/content/community/space/space_tab_timeline.jsp"></jsp:include>
	</div>
</div>