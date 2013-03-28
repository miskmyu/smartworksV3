
<!-- Name 			: append_task_forward.js							 -->
<!-- Description	: 새업무를 등록시 다른사용자에게 업무전달을 위한 화	      	 -->
<!-- Author			: Maninsoft, Inc.									 -->
<!-- Created Date	: 2011.9.											 -->

<%@page import="org.springframework.util.StringUtils"%>
<%@page import="net.smartworks.model.instance.info.RequestParams"%>
<%@page import="net.smartworks.model.instance.info.InstanceInfoList"%>
<%@page import="net.smartworks.model.instance.TaskInstance"%>
<%@page import="net.smartworks.model.instance.Instance"%>
<%@page import="net.smartworks.model.community.info.UserInfo"%>
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
	
	String forwardId = request.getParameter("forwardId");
	String taskInstId = request.getParameter("taskInstId");
	
	TaskInstanceInfo forwardTask = null;
	String workInstId = "";
	String subject = "";
	String content = "";
	String forwarderId = "";
	String forwarderName = "";
	String forwardDate = "";
	if(!SmartUtil.isBlankObject(forwardId)){
		forwardTask = (TaskInstanceInfo)session.getAttribute("forwardTask");
		if(!SmartUtil.isBlankObject(forwardTask) && forwardId.equals(forwardTask.getForwardId())){
			subject = forwardTask.getSubject();
			content = forwardTask.getContent();
			forwarderId = forwardTask.getAssigner().getId();
			forwarderName = forwardTask.getAssigner().getLongName();
			forwardDate = forwardTask.getCreatedDate().toLocalDateTimeSimpleString();
		}
	}
%>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!-- 업무계획하기 -->
<div class="js_append_task_forward_page" forwardId="<%=forwardId%>" taskInstId="<%=taskInstId%>">
	<div class="forward_section">
		<div class="tit m0"><span><fmt:message key="common.button.forward"/></span></div>
	</div>
	
		<form name='frmTaskForward' class="form_layout js_validation_required">
			<!-- 업무전달을 위한 입력화면들을 자동으로 그려주는 곳 -->
			<!-- js_task_forward_fields : js/sw/sw-formFields.js 의 loadTaskForwardFields()에서 자동으로 화면을 그려준다. -->
			<div class="js_task_forward_fields" 
			<%
				subject = StringUtils.replace(subject, "\"", "&quot;");
				content = StringUtils.replace(content, "\"", "&quot;");
			%>
				subjectTitle="<fmt:message key='forward.title.subject'/>" subject="<%=CommonUtil.toNotNull(subject)%>" 
				forwardeeTitle="<fmt:message key='forward.title.forwardee'/>" 
				CommentsTitle="<fmt:message key='forward.title.comments' />" content="<%=CommonUtil.toNotNull(content)%>"
				forwarderTitle="<fmt:message key='forward.title.forwarder' />" forwarderId="<%=CommonUtil.toNotNull(forwarderId)%>" forwarderName="<%=CommonUtil.toNotNull(forwarderName)%>"
				forwardDateTitle="<fmt:message key='forward.title.forward_date' />" forwardDate="<%=CommonUtil.toNotNull(forwardDate)%>">
			</div>
			<%
			if(!SmartUtil.isBlankObject(forwardId)){
			%>
				<div class="list_reply js_sub_instance_list"  style="width:600px;margin-left:50px;margin-bottom:7px">
				
					<div class="up_point_sgr pos_works"></div>
					<ul class="bg p10 js_comment_list">
						<jsp:include page="/jsp/content/upload/sub_instances_in_forward.jsp" >
							<jsp:param value="<%=forwardId %>" name="forwardId"/>
							<jsp:param value="<%=taskInstId %>" name="taskInstId"/>							
							<jsp:param value="<%=WorkInstance.DEFAULT_SUB_INSTANCE_FETCH_COUNT %>" name="fetchCount"/>
						</jsp:include>
						<%
						if(!SmartUtil.isBlankObject(taskInstId)){
						%>
							<li>
								<div class="sub_instance_list">
									<div class="det_title">	
										<span class="icon_status_running vm" title="<fmt:message key='content.status.running'/>" ></span>
										<img src="<%=cUser.getMinPicture()%>" class="profile_size_c"/>
							        	<span class="comment_box">
											<textarea style="width:79%" class="up_textarea" name="txtaCommentContent" placeholder="<fmt:message key='forward.message.leave_comment'/>"></textarea>
							        	</span>		
							        </div>						
								</div>
							</li>
						<%
						}
						%>
					</ul>
				</div>
			<%
			}
			%>					
		</form>
		<div class="dash_line"></div>
</div>
<!-- 업무계획하기 //-->
<script type="text/javascript">
$(function() {
	loadTaskForwardFields();
});
</script>
