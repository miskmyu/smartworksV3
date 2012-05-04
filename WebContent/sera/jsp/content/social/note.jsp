<%@page import="net.smartworks.model.instance.Instance"%>
<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.model.instance.info.AsyncMessageInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.AsyncMessageList"%>
<%@page import="net.smartworks.model.instance.AsyncMessageInstance"%>
<%@page import="net.smartworks.model.instance.info.InstanceInfo"%>
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
	<jsp:include page="/sera/jsp/content/social/start/new_note.jsp"/>
</div>
<!-- Panel Section -->
<div class="panel_section js_social_note_page">
	<div class="header">
		<div class="icon_mytext"><a href="" class="js_view_my_note current" instanceType="<%=Instance.TYPE_ASYNC_MESSAGE %>" userId="<%=cUser.getId()%>">받은 쪽지함</a></div>
		<div> | <a href="" class="js_view_my_note" instanceType="<%=Instance.TYPE_SENT_ASYNC_MESSAGE %>" userId="<%=cUser.getId()%>">보낸 쪽지함</a></div>
	</div>
	<div class="js_user_instance_list">
		<jsp:include page="/sera/jsp/content/sera_instances.jsp">
			<jsp:param value="<%=Instance.TYPE_ASYNC_MESSAGE %>" name="instanceType"/>
			<jsp:param value="<%=cUser.getId() %>" name="userId"/>
		</jsp:include>
	</div>
</div>
<!-- Panel Section //-->
