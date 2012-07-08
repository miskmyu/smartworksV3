
<!-- Name 			: home.jsp												 -->
<!-- Description	: 스마트웍스닷넷 홈인 오늘 페이지를 구성하는 페이 				 -->
<!-- Author			: Maninsoft, Inc.										 -->
<!-- Created Date	: 2011.9.												 -->

<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@ page contentType="text/html; charset=utf-8"%>

<%-- <%
	System.out.println("request url = " + request.getRequestURI());
	StringBuffer url = request.getRequestURL();
	String uri = request.getRequestURI();
	String req = request.toString();
	String[] contents = request.getRequestURI().split("#");
 	if(request.getRequestURI().split("#").length==1){
		response.sendRedirect("smart.sw#home.sw");
	}else{
%>
 --%>
<script>

if(!Request.isAjaxCall()){
		document.location.href = "smart.sw#home.sw";
	}
	
</script>

<%
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();

	session.setAttribute("cid", ISmartWorks.CONTEXT_HOME);
	session.setAttribute("lastLocation", "home.sw");
	session.removeAttribute("wid");
	session.removeAttribute("smartWork");
	session.removeAttribute("workInstance");
	session.removeAttribute("requestParams");

%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!-- 오늘, 내일 그리고 모레이후에 대한 달력과 이벤트들을 보여주는 화면 -->
<jsp:include page="/jsp/content/today/three_days_event.jsp" />
<!-- 오늘, 내일 그리고 모레이후에 대한 달력과 이벤트들을 보여주는 화면 //-->

<!-- 새업무, 사진, 파일, 이벤트... 을 올리는 화면 -->
<jsp:include page="/jsp/content/upload/select_upload_action.jsp" />
<!-- 새업무, 사진, 파일, 이벤트... 을 올리는 화면 //-->

<!-- 현재사용자의 진행중인 업무들 목록으로 보여주는 화면 -->
<jsp:include page="/jsp/content/today/my_running_instance_list.jsp" />
<!-- 현재사용자의 진행중인 업무들 목록으로 보여주는 화면 -->
<%-- 
<%
	}
%> --%>