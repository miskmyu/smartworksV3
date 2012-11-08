<%@page import="net.smartworks.server.engine.common.model.Order"%>
<%@page import="net.smartworks.server.engine.process.process.model.PrcProcessCond"%>
<%@page import="net.smartworks.server.engine.process.process.model.PrcProcess"%>
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
	
	PrcProcessCond prcCond = new PrcProcessCond();
	//public Order(String columnName, boolean isAsc)
	prcCond.setOrders(new Order[]{new Order(PrcProcess.A_NAME, false)});

	PrcProcess[] prcs = SwManagerFactory.getInstance().getPrcManager().getProcesses("kmyu@maninsoft.co.kr", prcCond, IManager.LEVEL_ALL);
	
	for (int i = 0; i < prcs.length ; i++) {
		
		PrcProcess prc = prcs[i];
		out.println(prc.getName() + "</br>");
	}
	
%>
<body>
</body>
</html>