
<%@page import="net.smartworks.model.sera.info.SeraUserInfo"%>
<%@page import="net.smartworks.model.sera.FriendList"%>
<%@page import="net.smartworks.model.sera.CourseList"%>
<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.model.sera.info.CourseInfo"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ page import="net.smartworks.model.community.*"%>
<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 현재사용자 정보도 가져온다 
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();
%>

<!-- Aside -->
<div class="aside m0">
	<!-- Aside Block1 -->
	<div class="aside_ad">
		<div>광고</div>
	</div>
	<!-- Aside Block1 //-->
	<!-- Aside Block1 -->
	<div class="aside_ad m0">
		<div>광고</div>
	</div>
	<!-- Aside Block1 //-->
	<!-- Aside Block1 -->
	<div class="aside_ad m0">
		<div>광고</div>
	</div>
	<!-- Aside Block1 //-->
	<!-- Aside Block1 -->
	<div class="aside_ad m0">
		<div>광고</div>
	</div>
	<!-- Aside Block1 //-->
	<!-- Aside Block1 -->
	<div class="aside_ad m0">
		<div>광고</div>
	</div>
	<!-- Aside Block1 //-->
	<!-- Aside Block1 -->
	<div class="aside_ad m0">
		<div>광고</div>
	</div>
	<!-- Aside Block1 //-->
</div>
<!-- Aside //-->
