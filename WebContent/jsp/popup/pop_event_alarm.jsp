<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.model.work.info.WorkInfo"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ page import="net.smartworks.model.work.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	User cUser = SmartUtil.getCurrentUser();

%>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!--  전체 레이아웃 -->
<!-- 팝업 컨텐츠 -->
<div class="pop_list_area pop_corner_all mail_address space_section js_pop_event_alarm_page">
 	<div class="form_title">
		<div class="icon_pop_Info"><fmt:message key="popup.title.event_alarm"/></div>
		<div class="solid_line"></div>
	</div>
	<ul>
	</ul>
	<!-- 하단버튼영역 -->
	<div class="glo_btn_space" style="border-top:none!important">
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
<!-- 팝업 컨텐츠 //-->

 	
<!-- 전체 레이아웃//-->