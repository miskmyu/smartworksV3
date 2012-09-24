<%@page import="net.smartworks.server.engine.common.manager.IManager"%>
<%@page import="net.smartworks.server.service.factory.SwServiceFactory"%>
<%@page import="net.smartworks.server.engine.common.loginuser.model.LoginUser"%>
<%@page import="net.smartworks.server.engine.factory.SwManagerFactory"%>
<%@page import="java.util.TimeZone"%>
<%@page import="net.smartworks.model.community.WorkSpace"%>
<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="org.claros.commons.configuration.PropertyFile"%>
<%@page import="org.claros.commons.mail.utility.Constants"%>
<%@page import="org.claros.commons.mail.protocols.Protocol"%>
<%@page import="org.claros.commons.mail.protocols.ProtocolFactory"%>
<%@page import="org.claros.commons.auth.models.AuthProfile"%>
<%@page import="org.claros.commons.mail.models.ConnectionMetaHandler"%>
<%@page import="org.claros.commons.mail.models.ConnectionProfile"%>
<%@page import="java.util.Locale"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.server.engine.scheduling.manager.impl.SchdulingManagerImpl"%>
<%@page import="net.smartworks.server.service.impl.SchedulingService"%>
<%@page	import="org.springframework.security.web.context.HttpSessionSecurityContextRepository"%>
<%@page import="java.util.Calendar"%>
<%@page import="net.smartworks.server.engine.security.model.Login"%>
<%@page import="org.springframework.security.core.Authentication"%>
<%@page	import="org.springframework.security.core.context.SecurityContext"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="true"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<!-- For Development Purpose -->
<%
	SecurityContext context = (SecurityContext) request.getSession().getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
	if (!SmartUtil.isBlankObject(context)) {
		Authentication auth = context.getAuthentication();
		if(!SmartUtil.isBlankObject(auth)) {
			String connectUserId = ((Login) auth.getPrincipal()).getId();
			if(SmartUtil.isBlankObject(session.getAttribute(connectUserId))) {
				System.out.println("-------------------------------------------");
				System.out.println(((Login) auth.getPrincipal()).getPosition() + " " + ((Login) auth.getPrincipal()).getName() + " 님이 접속하였습니다.");
				System.out.println("ID : " + ((Login) auth.getPrincipal()).getId());
				System.out.println("DEPT : " + ((Login) auth.getPrincipal()).getDepartment());
				System.out.println("ConnectTime : " + (new LocalDate()).toLocalDateValue() ); 
				System.out.println("-------------------------------------------");
	
				UserInfo[] userInfos = SwServiceFactory.getInstance().getCommunityService().getAvailableChatter(request);
				SmartUtil.publishAChatters(userInfos);

				session.setAttribute(connectUserId, new LocalDate());
			}
		}
	} else {
		response.sendRedirect("loginc.sw");
		return;
	}

	String cid = (String) session.getAttribute("cid");
	String wid = (String) session.getAttribute("wid");
	if (SmartUtil.isBlankObject(cid)) {
		session.setAttribute("cid", ISmartWorks.CONTEXT_HOME);
	}
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User currentUser = SmartUtil.getCurrentUser();
	
%>
<fmt:setLocale value="<%=currentUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<head>
<script type="">
currentUser = {
	userId : "<%=currentUser.getId()%>",
	name : "<%=currentUser.getName()%>",
	longName : "<%=currentUser.getLongName()%>",
	nickName : "<%=currentUser.getNickName()%>",
	company : "<%=currentUser.getCompany()%>",
	companyId : "<%=currentUser.getCompanyId()%>",
	department : "<%=currentUser.getDepartment()%>",
	departmentId : "<%=currentUser.getDepartmentId()%>",
	minPicture : "<%=currentUser.getMinPicture()%>",
	midPicture : "<%=currentUser.getMidPicture()%>",
	orgPicture : "<%=currentUser.getOrgPicture()%>",
	locale : "<%=currentUser.getLocale()%>",
	timeZone : "<%=currentUser.getTimeZone()%>",
	timeOffset : "<%=currentUser.getTimeOffsetInHour()%>",
	signPicture : "<%=currentUser.getSignPicture()%>"
};

function logout() {
	document.location.href = "logout.sw?userId=" + currentUser.userId;
};

</script>

<link href="css/default.css" type="text/css" rel="stylesheet" />
<link href="u-city/css/layout.css" type="text/css" rel="stylesheet" />
<link href="u-city/css/detail.css" type="text/css" rel="stylesheet" />
<link href="u-city/css/form.css" type="text/css" rel="stylesheet" />
<link href="css/black/media.css" type="text/css" rel="stylesheet"/>

<link href="css/ui-lightness/jquery-ui-1.8.16.custom.css" rel="stylesheet" type="text/css" title="ui-theme" />
<link href="css/ext/ext-all.css" type="text/css" rel="stylesheet" />
<link href="css/fileuploader/fileuploader.css" type="text/css" rel="stylesheet"/>
<link href="smarteditor/css/default_kor.css" rel="stylesheet" type="text/css" />

<meta http-equiv="X-UA-Compatible" content="IE=8" />
<!--[if lte IE 8]><link rel="stylesheet" href="css/black/ie8.css" type="text/css" media="all"><![endif]-->

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
 
<script type="text/javascript" src="js/jquery/jquery-1.6.2.min.js"></script>
<!--  
<script type="text/javascript" src="js/jquery/jquery-1.7.1.min.js"></script>
 -->
<script type="text/javascript" src="js/jquery/jquery.ui.core.min.js"></script>
<script type="text/javascript" src="js/jquery/jquery.validate.js"></script>
<script type="text/javascript" src="js/jquery/jquery.effects.core.js"></script>
<script type="text/javascript" src="js/jquery/jquery.effects.explode.js"></script>
<script type="text/javascript" src="js/jquery/jquery-ui-1.8.16.custom.min.js"></script>
<script type="text/javascript" src="js/jquery/jquery.ui.widget.min.js"></script>
<script type="text/javascript" src="js/jquery/jquery.ui.mouse.min.js"></script>
<script type="text/javascript" src="js/jquery/jquery.ui.draggable.min.js"></script>
<script type="text/javascript" src="js/jquery/jquery.ui.resizable.min.js"></script>
<script type="text/javascript" src="js/jquery/jquery.ui.slider.min.js"></script>
<script type="text/javascript" src="js/jquery/jquery.ui.datepicker.min.js"></script>
<script type="text/javascript" src="js/jquery/jquery.ui.datepicker-ko.js"></script>
<script type="text/javascript" src="js/jquery/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript" src="js/jquery/jquery-ui-timepicker-ko.js"></script>
<script type="text/javascript" src="js/jquery/history/jquery.history.js"></script>
<script type="text/javascript" src="js/jquery/jquery.json-2.3.min.js"></script>
<script type="text/javascript" src="js/jquery/jquery.zclip.min.js"></script>
<script type="text/javascript" src="js/jquery/jshashtable-2.1.js"></script>
<script type="text/javascript" src="js/jquery/jquery.numberformatter-1.2.2.min.js"></script>
<script type="text/javascript" src="js/jquery/jquery.formatCurrency-1.4.0.min.js"></script>
<script type="text/javascript" src="js/jquery/jquery.simplemodal.1.4.2.min.js"></script>
<script type="text/javascript" src="js/jquery/jquery-ui-1.8.21.custom.min.js"></script>

<script type="text/javascript" src="js/ext/ext-all.js"></script>

<script type="text/javascript" src="js/sw/sw-common.js"></script>
<script type="text/javascript" src="js/sw/sw-util.js"></script>
<script type="text/javascript" src="js/sw/sw-language.js"></script>
<script type="text/javascript" src="js/sw/sw-language-ko.js"></script>
<script type="text/javascript" src="js/sw/sw-language-en.js"></script>
<script type="text/javascript" src="js/sw/sw-more.js"></script>
<script type="text/javascript" src="js/sw/sw-nav.js"></script>
<script type="text/javascript" src="js/sw/sw-validate.js"></script>
<script type="text/javascript" src="js/sw/sw-flash.js"></script>
<script type="text/javascript" src="js/sw/sw-iframe-autoheight.js"></script>

<script type="text/javascript" src="js/sw/sw-report.js"></script>
<script type="text/javascript" src="js/sw/sw-file.js"></script>
<script type="text/javascript" src="js/sw/sw-webmail.js"></script>
<script type="text/javascript" src='js/sw/sw-formFields.js'></script>
<script type="text/javascript" src='js/sw/sw-popup.js'></script>

<script type="text/javascript" src="js/sw/sw-act-nav.js"></script>
<script type="text/javascript" src="js/sw/sw-act-report.js"></script>
<script type="text/javascript" src="js/sw/sw-act-search.js"></script>
<script type="text/javascript" src="js/sw/sw-act-filter.js"></script>
<script type="text/javascript" src="js/sw/sw-act-work.js"></script>
<script type="text/javascript" src="js/sw/sw-act-space.js"></script>
<script type="text/javascript" src="js/sw/sw-act-form.js"></script>
<script type="text/javascript" src="js/sw/sw-act-settings.js"></script>
<script type="text/javascript" src="js/sw/sw-act-builder.js"></script>
<script type="text/javascript" src="js/sw/sw-act-mail.js"></script>
<script type="text/javascript" src="u-city/js/ucity-actions.js"></script>
<script type="text/javascript" src="u-city/js/ucity-formFields.js"></script>

<script type="text/javascript" src='js/smartform/smartworks.js'></script>
<script type="text/javascript" src='js/smartform/sw-form-layout.js'></script>
<script type="text/javascript" src='js/smartform/sw-form-field-builder.js'></script>
<script type="text/javascript" src='js/smartform/sw-form-dataFields.js'></script>
<script type="text/javascript" src='js/smartform/field/currency_input.js'></script>
<script type="text/javascript" src='js/smartform/field/radio_button.js'></script>
<script type="text/javascript" src='js/smartform/field/check_box.js'></script>
<script type="text/javascript" src='js/smartform/field/combo_box.js'></script>
<script type="text/javascript" src='js/smartform/field/date_chooser.js'></script>
<script type="text/javascript" src='js/smartform/field/email_input.js'></script>
<script type="text/javascript" src='js/smartform/field/file_field.js'></script>
<script type="text/javascript" src='js/smartform/field/number_input.js'></script>
<script type="text/javascript" src='js/smartform/field/percent_input.js'></script>
<script type="text/javascript" src='js/smartform/field/rich_editor.js'></script>
<script type="text/javascript" src='js/smartform/field/text_input.js'></script>
<script type="text/javascript" src='js/smartform/field/time_chooser.js'></script>
<script type="text/javascript" src='js/smartform/field/datetime_chooser.js'></script>
<script type="text/javascript" src='js/smartform/field/user_field.js'></script>
<script type="text/javascript" src='js/smartform/field/ref_form_field.js'></script>
<script type="text/javascript" src='js/smartform/field/image_box.js'></script>
<script type="text/javascript" src='js/smartform/field/videoYT_box.js'></script>
<script type="text/javascript" src='js/smartform/field/auto_index.js'></script>

<script type="text/javascript" src="smarteditor/js/HuskyEZCreator.js" charset="utf-8"></script>
<script type="text/javascript" src="js/jquery/fileuploader/fileuploader.js" ></script>

<title><fmt:message key="head.title"><fmt:param value="<%=currentUser.getCompany() %>" /></fmt:message></title>

</head>

<body>
	<div id="wrap">
		<!-- Contents-->
		<div id="content">
			<tiles:insertAttribute name="content" />
		</div>
		<!-- Contents//-->
	</div>
</body>
</html>
