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

<!--  전체 레이아웃 -->
<div class="pop_corner_all js_download_from_appstore_page">

	<!-- 팝업 타이틀 -->
	<div class="pop_title_space w_margin"> 
		<div class="txt_btn">
			<div class="title_work"><fmt:message key="builder.button.download_from_appstore"/></div>
			
			<!-- 검색 -->
			<form class="po_left" name="frmSearchInstance">
				<div class="srch_wh srch_wsize">
					<input class="nav_input" type="text" placeholder="<fmt:message key='search.search_instance'/>" name="txtSearchInstance">
					<button onclick="popSelectListParam($(this).parents('form').next(), false);return false;" title="<fmt:message key='search.search_instance'/>"></button>
				</div>
			</form>
			<span class="js_progress_span"></span>			
			<div class="txt_btn">			
				<a href="" onclick="smartPop.close();return false;"><div class="btn_x"></div></a>
			</div>
		
		</div>
	</div>
	<!-- 팝업 타이틀 //-->

	<div id="app_work_list_page">
		<jsp:include page="/jsp/popup/pop_app_work_list.jsp"></jsp:include>
	</div>

	<!-- 하단버튼영역 -->
	<div class="glo_btn_space">

		<div class="fr">
			<span class="btn_gray"> 
				<a onclick="$.modal.close();return false;" href=""> 
					<span class="txt_btn_start"></span> 
					<span class="txt_btn_center"><fmt:message key="common.button.close"/></span> 
					<span class="txt_btn_end"></span> </a> </span>
		</div>

	</div>
	<!-- 하단버튼영역 //-->

</div>
<!-- 전체 레이아웃//-->
