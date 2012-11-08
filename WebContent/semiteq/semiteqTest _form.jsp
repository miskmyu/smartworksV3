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
	
	////////////////////////////////////////////////////////
	//���μ��� ���̵�� ���μ����� ���� ������ ��������
	
	String processId = "prc_0bce1ba358e4409bbcdffd3e5fd16b93";
	
	PrcProcessCond prcCond = new PrcProcessCond();
	prcCond.setProcessId(processId);
	
	PrcProcess[] prcs = SwManagerFactory.getInstance().getPrcManager().getProcesses("", prcCond, null);
	
	//���μ��� ���̵�� �˻��߱� ������ ���μ����� �׻� 1����
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
	����� �̸� : <%= userName%> , �μ��̸� : <%=dept.getName() %>
</body>
</html>