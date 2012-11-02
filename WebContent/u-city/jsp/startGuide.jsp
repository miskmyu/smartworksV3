<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="pro.ucity.sso.filter.UcityAuthenticationSuccesHandler"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="pro.ucity.model.Service"%>
<%@page import="pro.ucity.model.OPSituation"%>
<%@ page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head></head>
<body>
<%!public static Object getBean(String beanName, HttpServletRequest request) {

	WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());

	return (Object) wac.getBean(beanName);
}%>
<%
	String userviceCode = request.getParameter("userviceCode");
	String serviceCode = request.getParameter("serviceCode");
	String eventCode = request.getParameter("eventCode");
	String statusCode = request.getParameter("statusCode");

	UcityAuthenticationSuccesHandler uas = (UcityAuthenticationSuccesHandler)getBean("authenticationSuccessHandler", request);
	uas.setDefaultTargetUrl("/situationManual.sw?userviceCode="+ userviceCode + "&serviceCode=" + serviceCode + "&eventCode=" + eventCode + "&statusCode=" + statusCode);
%>
<script type="text/javascript">
// Popup window code
	var userviceCode = "<%=request.getParameter("userviceCode")%>";
	var serviceCode = "<%=request.getParameter("serviceCode")%>";
	var eventCode = "<%=request.getParameter("eventCode")%>";
	var statusCode = "<%=request.getParameter("statusCode")%>";
	window.resizeTo(1000, 680)
	window.location.href = 'http://bpm.cnuct.kr:8862/smartworksV3/situationManual.sw?userviceCode='+ userviceCode + '&serviceCode=' + serviceCode + '&eventCode=' + eventCode + '&statusCode=' + statusCode;
//	window.open('http://10.2.10.147:8862/smartworksV3/situationManual.sw?userviceCode='+ userviceCode + '&serviceCode=' + serviceCode + '&eventCode=' + eventCode + '&statusCode=' + statusCode,'','toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=1000,height=768 top=10 left=10');
</script>
</body>
</html>
