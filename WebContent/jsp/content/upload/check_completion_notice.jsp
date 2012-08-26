
<!-- Name 			: check_completion_notice.jsp						 -->
<!-- Description	: 새업무를 시작할때 알림을 선택하는 화면        	      	 -->
<!-- Author			: Maninsoft, Inc.									 -->
<!-- Created Date	: 2011.9.											 -->

<%@page import="java.text.DecimalFormat"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%
	//스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 현재사용자 정보도 가져온다..
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();	
	String workType = request.getParameter("workType");
	
%>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!-- 알림 선택 -->
<div>
	<form name='frmCheckCompletionNotice' class="form_layout">
		<!-- 완료시 알림을 원하는지를 선택하는 선택박스 -->
		<div class="pr10">
			<%if("approvalWork".equals(workType)){ %>
				<%-- <input name="chkApprovalCompletionNotice" class="pl15" type="checkbox"/><fmt:message key="common.title.approval_completion_notice" /> --%>
			<%}else{ %>
				<%-- <input name="chkWorkCompletionNotice" class="pl15" type="checkbox"/><fmt:message key="common.title.work_completion_notice" /> --%>
			<%}%>
		</div>
	</form>
</div>
<!-- 알림선택 //-->
