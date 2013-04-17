<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>

<link href="css/default.css" type="text/css" rel="stylesheet" />

<link href="css/ui-lightness/jquery-ui-1.8.16.custom.css" rel="stylesheet" type="text/css" title="ui-theme" />
<link href="css/ext/ext-all.css" type="text/css" rel="stylesheet" />
<link href="css/fileuploader/fileuploader.css" type="text/css" rel="stylesheet"/>
<link href="css/fullcalendar/fullcalendar.css" type="text/css" rel="stylesheet"/>
<link href="smarteditor/css/default_kor.css" rel="stylesheet" type="text/css" />
<link rel="shortcut icon" href="images/favicon/smartworks.ico"/>


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
<script type="text/javascript" src="js/jquery/fullcalendar.js"></script>
<script type="text/javascript" src="js/jquery/jquery-ui-1.8.21.custom.min.js"></script>


<script type="text/javascript" src='js/smartform/smartworks.js'></script>





<%
	String instanceId = (String)request.getParameter("instanceId");
	String lang = (String)request.getParameter("lang");
%>

<script type="text/javascript">

	var APPNAME_INSTANCE_VIEWER = "InstanceViewerApp";
	var lang = "<%=lang%>";
	function getServiceUrl() {
		var splitArray = window.location.href.split('/');
		return splitArray[0] + '//' + splitArray[2] + '/';
	};

	function getGeneralParams(){
		return 	'serviceUrl=' + getServiceUrl() + 
				'&compId=Maninsoft' + 
				'&userId=kmyu@maninsoft.co.kr|' + lang + 
				'&defaultLanguage=' + lang;	
	};
	function loadInstanceViewer(target, params){
		var options = {
				instanceId : ''
		};
		SmartWorks.extend(options, params);
		loadFlash(target, APPNAME_INSTANCE_VIEWER, getGeneralParams() 
												+ '&prcInstId=' + options.instanceId);
	};
	function loadFlash(target, appName, params){
		var htm = 	'<object type="application:x-shockwave-flash" classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"' +
						'id="' + appName + '" width="100%" height="100%"' + 'codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">' +
						'<param name="src" value="resources/flash/' + appName + '.swf" />' +
						'<param name="quality" value="high" />' +
						'<param name="wmode" value="transparent">' +
						'<param name="bgcolor" value="#ffffff" />' +
						'<param name="allowScriptAccess" value="always" />' +
						'<param name="flashVars" value="' + smartEncode(params) + '" />' +
						'<embed src="resources/flash/' + appName + '.swf" wmode="transparent" quality="high" bgcolor="#ffffff"' +
							'width="100%" height="100%" name="VinoTester" align="middle" play="true" loop="false" quality="high" allowScriptAccess="sameDomain"' +
							'type="application/x-shockwave-flash" pluginspage="http://www.adobe.com/go/getflashplayer" flashVars="' + smartEncode(params) + '">' +
						'</embed>' +
					'</object>';
		$(target).html(htm);	
	};
	var smartEncode = function(value){
		if(isEmpty(value)) return value;
		value = value.replace(/\"/g,"&quot;");
		value = value.replace(/\'/g,"&squo;");
		value = value.replace(/</g,"&lt;");
		value = value.replace(/>/g,"&gt;");
		return value;
	};

	var smartDecode = function(value){
		if(isEmpty(value)) return value;
		value = value.replace(/&quot;/g,"\"");
		value = value.replace(/&squo;/g,"\'");
		value = value.replace(/&lt;/g,"<");
		value = value.replace(/&gt;/g,">");
		return value;
	};
	function isEmpty(str) {
	    return (!str || 0 === str.length || 'null' === str);
	};
</script>
</head>

<body>
	<!-- 프로세스다이어그램 -->
	<div class="define_space js_process_instance_viewer" height:1024px;"></div>
	
	
<script type="text/javascript">
	var target = $('.js_process_instance_viewer');
	var instanceId = "<%=instanceId%>";
	loadInstanceViewer(target, {
			instanceId : instanceId
	});
</script>	
	
</body>
</html>
