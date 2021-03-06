<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.service.impl.SmartWorks"%>
<%@page import="net.smartworks.model.instance.FieldData"%>
<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.model.instance.info.IWInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.InstanceInfoList"%>
<%@page import="net.smartworks.model.instance.info.RequestParams"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.model.work.info.WorkInfo"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ page import="net.smartworks.model.work.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser(); 
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<div class="pop_corner_all">
	<!-- 팝업 타이틀 -->
	<div class="form_title">
		<div class="pop_title"><fmt:message key="approval.title.select_approval_line"/></div>
		<div class="txt_btn">
			<a href="" onclick="smartPop.close();return false;"><div class="pop_btn_x"></div></a>
		</div>
		<div class="solid_line"></div>
	</div>
	<form name="frmApprovalLineList" id="approval_line_list_page">
		<jsp:include page="/jsp/popup/pop_approval_line_list.jsp"></jsp:include>
	</form>
	<!-- 하단버튼영역 -->
	<div class="glo_btn_space">
		<div class="fr">
			<span class="btn_gray"> 
				<a onclick="smartPop.close();return false;" href="">
					<span class="txt_btn_start"></span> 
					<span class="txt_btn_center"><fmt:message key="common.button.close"/></span>
					<span class="txt_btn_end"></span> 
				</a> 
			</span>
		</div>
	</div>
	<!-- 하단버튼영역 //-->
</div>
<!-- POP //-->
