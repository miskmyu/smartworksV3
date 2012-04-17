<%@page import="net.smartworks.model.community.WorkSpace"%>
<%@page import="net.smartworks.model.instance.info.AsyncMessageInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.YTVideoInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.EventInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.BoardInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.CommentInstanceInfo"%>
<%@page import="net.smartworks.model.instance.WorkInstance"%>
<%@page import="net.smartworks.model.instance.info.WorkInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.MemoInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.ImageInstanceInfo"%>
<%@page import="net.smartworks.model.instance.Instance"%>
<%@page import="net.smartworks.model.instance.info.InstanceInfo"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@ page contentType="text/html; charset=utf-8"%>

<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 그리고 현재사용자 정보도 가져온다.	
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");

	User cUser = SmartUtil.getCurrentUser();
%>

<div class="my_comment_section">
	<div class="my_photo"><img class="profile_size_b" src="<%=cUser.getMidPicture() %>" /></div>
	<!-- My Comment -->
	<div class="my_comment">
		<div class="header"><%=cUser.getNickName() %>님</div>
		<jsp:include page="/sera/jsp/content/sera_note.jsp">
			<jsp:param value="<%=ISmartWorks.SPACE_TYPE_USER %>" name="spaceType"/>
			<jsp:param value="<%=cUser.getId() %>" name="spaceId"/>
		</jsp:include>
	</div>
	<!-- My Comment //-->
</div>
<!-- Comment Pannel-->
<div class="panel_section js_my_instance_list_page">
	<div class="header">
		<div class="icon_mytext"><a href="" class="js_view_user_instances current" userId="<%=cUser.getId()%>">내글보기</a></div><div> | <a href="" class="js_view_all_instances">전체보기</a></div>
	</div>

	<div class="js_user_instance_list">
		<jsp:include page="/sera/jsp/content/sera_instances.jsp">
			<jsp:param value="<%=cUser.getId() %>" name="userId"/>
		</jsp:include>
	</div>
</div>
<!-- Comment Pannel-->