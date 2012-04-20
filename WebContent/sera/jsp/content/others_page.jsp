<%@page import="net.smartworks.model.sera.SeraUser"%>
<%@page import="net.smartworks.util.SmartTest"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@ page contentType="text/html; charset=utf-8"%>

<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 그리고 현재사용자 정보도 가져온다.	
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");

	User cUser = SmartUtil.getCurrentUser();
	
	String otherUserId = request.getParameter("userId");
	SeraUser otherUser = smartWorks.getSeraUserById(otherUserId);
	session.setAttribute("otherUser", otherUser);
	
%>
<div class="my_comment_section">
	<!-- Photo Section -->
	<div class="photo_section">
		<div class="my_photo"><img src="<%=otherUser.getMidPicture() %>" width="120px" height="120px"/></div>
		<!-- Btn -->
		<%
		if(!otherUser.isFriend()){
		%>
			<div class="btn_green_l cb js_friend_request_btn" userId="<%=otherUser.getId() %>" style="margin: 8px 0 0 10px">
				<div class="btn_green_r"><span class="icon_green_down mr5"></span>친구요청</div>
			</div>
		<%
		}
		%>
		<!-- Btn //-->
	</div>
	<!-- Photo Section //-->
	<!-- My Comment -->
	<div class="my_comment">
		<div class="header"><%=otherUser.getNickName() %>님</div>
		<jsp:include page="/sera/jsp/content/sera_note.jsp">
			<jsp:param value="<%=ISmartWorks.SPACE_TYPE_USER %>" name="spaceType"/>
			<jsp:param value="<%=otherUserId%>" name="spaceId"/>
		</jsp:include>
	</div>
	<!-- My Comment //-->
</div>
<!-- Comment Pannel-->
<div class="panel_section">
	<div class="header"></div>

	<div class="js_user_instance_list">
		<jsp:include page="/sera/jsp/content/sera_instances.jsp">
			<jsp:param value="<%=otherUser.getId() %>" name="userId"/>
		</jsp:include>
	</div>
</div>
<!-- Comment Pannel-->
