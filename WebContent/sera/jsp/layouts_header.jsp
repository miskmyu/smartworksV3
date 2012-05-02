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
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	session.setAttribute("currentMenu", "seraCLUB");
	session.setAttribute("noUser", true);
%>

<head>

	<script type="text/javascript">
		var currentUser = {
			locale : "<%=java.util.Locale.getDefault().getLanguage()%>",
			timeZone : "<%=TimeZone.getDefault().getID()%>",
			timeOffset : "<%=TimeZone.getDefault().getRawOffset()%>"
		};
	</script>
	
	<link href="css/default.css" type="text/css" rel="stylesheet" />
	<link href="sera/css/pop.css" type="text/css" rel="stylesheet" />
	<link href="sera/css/form.css" type="text/css" rel="stylesheet" />
	<link href="sera/css/page.css" type="text/css" rel="stylesheet" />
	<link href="sera/css/help_center.css" type="text/css" rel="stylesheet" />
	
	<link href="css/ui-lightness/jquery-ui-1.8.16.custom.css" rel="stylesheet" type="text/css" title="ui-theme" />
	<link href="css/ext/ext-all.css" type="text/css" rel="stylesheet" />
	<link href="css/fileuploader/fileuploader.css" type="text/css" rel="stylesheet"/>
	<link href="css/fullcalendar/fullcalendar.css" type="text/css" rel="stylesheet"/>
	<link href="smarteditor/css/default_kor.css" rel="stylesheet" type="text/css" />
		
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	
	<!-- 
	<script type="text/javascript" src="js/jquery/jquery-1.7.1.min.js"></script>
	 -->
	<script type="text/javascript" src="js/jquery/jquery-1.6.2.min.js"></script>
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
	<!--
	<script type="text/javascript" src="js/jquery/gcal.js"></script>
	-->
	<script type="text/javascript" src="js/jquery/fullcalendar.js"></script>
	
	<script type="text/javascript" src="js/jstorage/jstorage.js"></script>
	<script type="text/javascript" src="js/faye/faye-browser-min.js"></script>
	<script type="text/javascript" src="js/ext/bootstrap.js"></script>
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
	<script type="text/javascript" src="sera/js/sera-nav.js"></script>
	
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
	<script type="text/javascript" src="js/sw/sw-act-settings.js"></script>
	<script type="text/javascript" src="js/sw/sw-act-builder.js"></script>
	
	<script type="text/javascript" src="sera/js/sera-actions.js"></script>
	<script type="text/javascript" src="sera/js/sera-formFields.js"></script>
	<script type="text/javascript" src="sera/js/window_popup.js"></script>
	
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
	
	<script type="text/javascript" src="smarteditor/js/HuskyEZCreator.js" charset="utf-8"></script>
	<script type="text/javascript" src="js/jquery/fileuploader/fileuploader.js" ></script>
	
	<title>세라캠퍼스에 오신걸 환영합니다.</title>
</head>

<body>

	<div id="wrap">
		<!-- Header -->
		<div id="sera_header">
			<tiles:insertAttribute name="sera_header" />
		</div>
		<!-- Header//-->
	</div>
</body>
</html>

<script type="text/javascript">
	function NavigateParent(url){
	    parent.location.href = url;   
	};
</script>
