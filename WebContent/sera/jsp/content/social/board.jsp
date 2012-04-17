<%@page import="net.smartworks.model.instance.Instance"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@ page contentType="text/html; charset=utf-8"%>

<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 그리고 현재사용자 정보도 가져온다.	
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();
%>
<!-- Header Title -->
<div class="header_tit">
	<div class="tit_notes">쪽지를 이용하여 친구,멘토에게 하고 싶은 이야기를 빠르게 전할 수 있습니다.</div>
</div>
<!-- Header Title //-->
<!-- Input Form -->
<div class="form_layout">
	<jsp:include page="/sera/jsp/content/social/start/new_board.jsp"/>
</div>
<!-- Input Form //-->
<!-- Panel Section -->
<div class="panel_section">
	<div class="js_user_instance_list">
		<jsp:include page="/sera/jsp/content/sera_instances.jsp">
			<jsp:param value="<%=Instance.TYPE_BOARD %>" name="instanceType"/>
			<jsp:param value="<%=cUser.getId() %>" name="userId"/>
		</jsp:include>
	</div>
</div>
<!-- Panel Section //-->
