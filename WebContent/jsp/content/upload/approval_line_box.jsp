
<!-- Name 			: append_task_forward.js							 -->
<!-- Description	: 새업무를 등록시 다른사용자에게 업무전달을 위한 화	      	 -->
<!-- Author			: Maninsoft, Inc.									 -->
<!-- Created Date	: 2011.9.											 -->

<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.model.instance.Instance"%>
<%@page import="net.smartworks.model.approval.ApprovalLineInst"%>
<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.model.approval.Approval"%>
<%@page import="net.smartworks.model.approval.ApprovalLine"%>
<%@page import="net.smartworks.model.instance.info.TaskInstanceInfo"%>
<%@page import="net.smartworks.model.instance.WorkInstance"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 현재사용자 정보도 가져온다..
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();

	String approvalLineId = request.getParameter("approvalLineId");

	ApprovalLine approvalLine = smartWorks.getApprovalLineById(approvalLineId);

%>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<form class="js_validation_required" name="frmApprovalLine">
	<input name="hdnApprovalLineId" value="<%=approvalLine.getId() %>" type="hidden">		
	<%
	if(!SmartUtil.isBlankObject(approvalLine) && !SmartUtil.isBlankObject(approvalLine.getApprovals())){
		Approval[] approvals = approvalLine.getApprovals();	
		
		for(int i=0; i<approvals.length; i++){
			Approval approval = approvals[i];
	%>
		<!-- 결재선 -->
		<div class="approval_area">
			<div class="label"><%=approval.getName() %></div>
			<div class="approval"></div>
			<%
			if(approval.getApproverType() == Approval.APPROVER_CHOOSE_ON_RUNNING){
			%>
				<a href="" class="name js_selected_approver_info js_userpicker_button">
					<div class="noti_pic">
						<img class="profile_size_s" src="images/no_user_picture_min.jpg">
					</div>
					<div class="noti_in up"><fmt:message key="approval.title.select_approver"/>
					</div>
				</a>								
			<%
			}else if(!SmartUtil.isBlankObject(approval.getApprover())){
				User approver = approval.getApprover();
				String completedDateStr = (SmartUtil.isBlankObject(approval.getCompletedDate())) ? "" : approval.getCompletedDate().toLocalDateTimeSimpleString();
				
			%>
				<div class="name">
					<div class="noti_pic">
						<img class="profile_size_s" src="<%=approver.getMinPicture()%>" title="<%=approver.getLongName()%>">
					</div>
					<div class="noti_in">
						<div class="t_name"><%=CommonUtil.toNotNull(approver.getPosition()) %></div>
						<div class="t_name"><%=approver.getName() %></div>
						<div class="t_date"><%=completedDateStr %></div>
					</div>
					<input name="usrLevelApprover<%=i+1 %>" value="<%=approver.getId() %>" type="hidden">
				</div>
			<%
			}
			%>
		</div>
		<!-- 결재선 //-->
	<%
		}
	}
	%>
</form>

<script type="text/javascript">
	$('.js_append_task_approval_page .js_approval_line_name').html('<%=approvalLine.getName()%>');
</script>
