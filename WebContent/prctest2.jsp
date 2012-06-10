
<%@page import="net.smartworks.server.engine.publishnotice.model.PublishNotice"%>
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
	
	PublishNotice no = new PublishNotice("kmyu@maninsoft.co.kr", PublishNotice.TYPE_COMMENT, PublishNotice.REFTYPE_MESSAGE, "402880e337d559040137d55d531b0002");

	//SwManagerFactory.getInstance().getPublishNoticeManager().setPublishNotice("kmyu@maninsoft.co.kr", no, IManager.LEVEL_ALL);
//	SwManagerFactory.getInstance().getPublishNoticeManager().setPublishNotice("kmyu@maninsoft.co.kr", no, IManager.LEVEL_ALL);
//	PublishNotice result = SwManagerFactory.getInstance().getPublishNoticeManager().getPublishNotice("kmyu@maninsoft.co.kr", "402880e337d4875d0137d489c8600001", IManager.LEVEL_ALL);


%>

</body>
</html>
