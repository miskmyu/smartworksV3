<%@page import="net.smartworks.server.engine.organization.model.SwoDepartment"%>
<%@page import="net.smartworks.server.engine.organization.model.SwoUserCond"%>
<%@page import="net.smartworks.server.engine.organization.model.SwoUser"%>
<%@page import="net.smartworks.server.engine.common.manager.IManager"%>
<%@page import="net.smartworks.server.engine.factory.SwManagerFactory"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
</head>
<%
	SwoUserCond cond = new SwoUserCond();
	cond.setName("������");
	SwoUser user = SwManagerFactory.getInstance().getSwoManager().getUser("", cond, null);
	String userName = "";
	if (user != null) {
		userName = user.getName();
	} else {
		userName = "����� �ƴϹ��ϴ�";
	}
	
	String userDeptId = user.getDeptId();
	
	SwoDepartment dept = SwManagerFactory.getInstance().getSwoManager().getDepartment("", userDeptId, null);
	
	
	

%>
<body>
	����� �̸� : <%= userName%> , �μ��̸� : <%=dept.getName() %>
</body>
</html>