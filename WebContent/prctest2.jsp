
<%@page import="net.smartworks.server.engine.infowork.domain.model.SwdRecord"%>
<%@page import="net.smartworks.server.engine.common.manager.IManager"%>
<%@page import="net.smartworks.server.engine.factory.SwManagerFactory"%>
<%@page import="net.smartworks.server.engine.sera.model.SeraConstant"%>
<%@page import="net.smartworks.server.engine.infowork.domain.model.SwdRecordCond"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="net.smartworks.server.engine.common.model.Filter"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
</head>
<body>
<%!

public static Object getBean(String beanName, HttpServletRequest request) {

	WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());

	return (Object) wac.getBean(beanName);
}


%>
<%	
	
	SwdRecordCond cond = new SwdRecordCond();
	Filter ft = new Filter("in", "id" , Filter.OPERANDTYPE_STRING, "dr_402880eb36b59d9e0136b5b852560007");
	cond.setFormId(SeraConstant.MISSION_FORMID);
	cond.setFilter(new Filter[]{ft});
	
	SwdRecord[] recs = SwManagerFactory.getInstance().getSwdManager().getRecords("", cond, IManager.LEVEL_ALL);

	if (recs != null) {
		for (int i=0; i < recs.length; i++) {
			SwdRecord rec = recs[i];
			out.println(rec.getRecordId());
		}
			
	} else {
		out.print("result is null");
	}
%>

</body>
</html>
