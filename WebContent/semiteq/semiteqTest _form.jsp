<%@page import="net.smartworks.server.engine.infowork.form.model.SwfForm"%>
<%@page import="net.smartworks.server.engine.infowork.form.model.SwfFormCond"%>
<%@page import="net.smartworks.server.engine.process.process.model.PrcProcess"%>
<%@page import="net.smartworks.server.engine.process.process.model.PrcProcessCond"%>
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
	cond.setName("유광민");

	SwoUser user = SwManagerFactory.getInstance().getSwoManager().getUser("", cond, null);
	String userName = "";
	if (user != null) {
		userName = user.getName();
	} else {
		userName = "사람이 아니무니다";
	}
	
	String userDeptId = user.getDeptId();
	
	SwoDepartment dept = SwManagerFactory.getInstance().getSwoManager().getDepartment("", userDeptId, null);
	
	////////////////////////////////////////////////////////
	//프로세스 아이디로 프로세스에 속한 폼정보 가져오기
	
	String processId = "prc_0bce1ba358e4409bbcdffd3e5fd16b93";
	
	PrcProcessCond prcCond = new PrcProcessCond();
	prcCond.setProcessId(processId);
	
	PrcProcess[] prcs = SwManagerFactory.getInstance().getPrcManager().getProcesses("", prcCond, null);
	
	//프로세스 아이디로 검색했기 때문에 프로세스는 항상 1개다
	PrcProcess prc = prcs[0];
	
	String packageId = prc.getDiagramId();
	
	SwfFormCond formCond = new SwfFormCond();
	formCond.setPackageId(packageId);
	
	SwfForm[] forms = SwManagerFactory.getInstance().getSwfManager().getForms("", formCond, null);
	
	for (int i = 0; i < forms.length ; i++) {
		SwfForm form = forms[i];
		
		out.println(form.getName());
		
	}

%>
<body>
	사용자 이름 : <%= userName%> , 부서이름 : <%=dept.getName() %>
</body>
</html>