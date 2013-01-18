<%@page import="pro.ucity.sso.filter.UcityAuthenticationSuccesHandler"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="pro.ucity.model.Service"%>
<%@page import="pro.ucity.model.OPSituation"%>
<%@ page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head></head>
<body>

<%-- <%!public static Object getBean(String beanName, HttpServletRequest request) {

	WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());

	return (Object) wac.getBean(beanName);
}%>
<%
	UcityAuthenticationSuccesHandler uas = (UcityAuthenticationSuccesHandler)getBean("authenticationSuccessHandler", request);
	uas.setDefaultTargetUrl("/situationMonitoring.sw");
%> --%>
<script type="text/javascript">
// Popup window code
	window.open('http://bpm.cnuct.kr:8862/smartworksV3/situationMonitoring.sw?page=monitoring','MonitoringPop','toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=1000,height=768 top=10 left=10');
</script>
</body>
</html>
