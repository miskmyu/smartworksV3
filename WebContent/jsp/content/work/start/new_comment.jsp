
<!-- Name 			: new_comment.jsp								 -->
<!-- Description	: 스마트폼을 이용하여 새 메모를 등록하는 화면 	      	 -->
<!-- Author			: Maninsoft, Inc.								 -->
<!-- Created Date	: 2011.9.										 -->

<%@page import="net.smartworks.model.instance.WorkInstance"%>
<%@page import="net.smartworks.model.work.SmartWork"%>
<%@page import="net.smartworks.model.work.Work"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.util.SmartTest"%>
<%@page import="net.smartworks.model.community.User"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>

<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 현재사용자 정보도 가져온다..
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();
	
	WorkInstance workInstance = (WorkInstance)session.getAttribute("workInstance");
	String instanceId = (SmartUtil.isBlankObject(workInstance)) ? "" : workInstance.getId();
	int workType = (SmartUtil.isBlankObject(workInstance) || SmartUtil.isBlankObject(workInstance.getWork()) ) ? -1 : workInstance.getWork().getType();
	
%>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<div class="up_wrap js_new_comment_page" instanceId="<%=instanceId%>" workType="<%=workType %>">
	<div class="up_point pos_works js_up_pointer"></div>
	<div class="form_wrap up">
        <div class="reply_input js_return_on_comment">
			<div class="noti_pic">
				<img src="<%=cUser.getMinPicture()%>" class="profile_size_c"/>
			</div>
			<div class="noti_in">
				<textarea style="width:95%" name="txtaCommentContent" placeholder="<fmt:message key='work.message.leave_comment'/>"></textarea>
			</div>
        </div>
	</div>
</div>