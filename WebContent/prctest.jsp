<%@page import="net.smartworks.server.engine.sera.model.CourseDetail"%>
<%@page import="net.smartworks.server.engine.sera.manager.ISeraManager"%>
<%@page import="net.smartworks.server.engine.organization.model.SwoGroup"%>
<%@page import="net.smartworks.server.engine.organization.model.SwoGroupCond"%>
<%@page import="net.smartworks.server.engine.infowork.domain.model.SwdDataField"%>
<%@page import="net.smartworks.server.engine.infowork.domain.model.SwdRecord"%>
<%@page import="net.smartworks.server.service.IInstanceService"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.TimeZone"%>
<%@page import="net.smartworks.server.engine.common.util.DateUtil"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="net.smartworks.server.engine.config.model.SwcWorkHour"%>
<%@page import="org.apache.commons.logging.LogFactory"%>
<%@page import="org.apache.commons.logging.Log"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.server.engine.common.model.util.IdGen"%>
<%@page import="net.smartworks.server.engine.organization.model.SwoDepartment"%>
<%@page import="net.smartworks.server.engine.common.manager.IManager"%>
<%@page import="net.smartworks.server.engine.process.process.model.PrcProcessInstExtend"%>
<%@page import="net.smartworks.server.engine.process.process.manager.impl.PrcManagerImpl"%>
<%@page import="net.smartworks.model.instance.WorkInstance"%>
<%@page import="net.smartworks.server.service.impl.InstanceServiceImpl"%>
<%@page import="net.smartworks.server.service.impl.WorkServiceImpl"%>
<%@page import="net.smartworks.model.work.Work"%>
<%@page import="net.smartworks.server.service.IWorkService"%>
<%@page import="net.smartworks.model.work.SmartWork"%>
<%@page import="net.smartworks.model.work.WorkCategory"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@page import="net.smartworks.server.engine.process.approval.model.AprApproval"%>
<%@page import="net.smartworks.server.engine.factory.SwManagerFactory"%>
<%@page import="net.smartworks.server.engine.process.approval.manager.IAprManager"%>
<%@page import="net.smartworks.server.engine.process.process.model.PrcProcessCond"%>
<%@page import="net.smartworks.server.engine.process.deploy.manager.IDepManager"%>
<%@page import="net.smartworks.server.engine.organization.model.SwoUserCond"%>
<%@page import="net.smartworks.server.engine.organization.model.SwoUser"%>
<%@page import="net.smartworks.server.engine.organization.manager.ISwoManager"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.server.engine.process.link.model.LnkLink"%>
<%@page import="net.smartworks.server.engine.process.link.manager.ILnkManager"%>
<%@page import="net.smartworks.server.engine.process.process.model.PrcProcess"%>
<%@page import="net.smartworks.server.engine.process.process.manager.IPrcManager"%>
<%@page import="net.smartworks.server.engine.process.task.model.TskTaskDef"%>
<%@page import="net.smartworks.server.engine.process.task.model.TskTask"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="net.smartworks.server.engine.process.task.manager.impl.TskManagerImpl"%>
<%@page import="net.smartworks.server.engine.process.task.manager.ITskManager"%>
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
/* 
	ISwoManager swoMgr = SwManagerFactory.getInstance().getSwoManager();
	ISeraManager seraMgr = SwManagerFactory.getInstance().getSeraManager();

	SwoGroupCond cond = new SwoGroupCond();
	//runningCourseCond.setGroupLeader("");
	String[] idIns = new String[43];
	idIns[0] = "group_78";
	idIns[1] = "group_80";
	idIns[2] = "group_81";
	idIns[3] = "group_93";
	idIns[4] = "group_94";
	idIns[5] = "group_96";
	idIns[6] = "group_97";
	idIns[7] = "group_98";
	idIns[8] = "group_100";
	idIns[9] = "group_112";
	idIns[10] = "group_115";
	idIns[11] = "group_116";
	idIns[12] = "group_117";
	idIns[13] = "group_119";
	idIns[14] = "group_127";
	idIns[15] = "group_129";
	idIns[16] = "group_136";
	idIns[17] = "group_137";
	idIns[18] = "group_140";
	idIns[19] = "group_146";
	idIns[20] = "group_155";
	idIns[21] = "group_164";
	idIns[22] = "group_165";
	idIns[23] = "group_188";
	idIns[24] = "group_200";
	idIns[25] = "group_201";
	idIns[26] = "group_205";
	idIns[27] = "group_209";
	idIns[28] = "group_218";
	idIns[29] = "group_225";
	idIns[30] = "group_227";
	idIns[31] = "group_229";
	idIns[32] = "group_230";
	idIns[33] = "group_231";
	idIns[34] = "group_232";
	idIns[35] = "group_233";
	idIns[36] = "group_234";
	idIns[37] = "group_236";
	idIns[38] = "group_237";
	idIns[39] = "group_238";
	idIns[40] = "group_239";
	idIns[41] = "group_241";
	idIns[42] = "group_242";
	cond.setGroupIdIns(idIns);
	

	for (int i = 42 ; i > -1; i--) {
		
		SwoGroup group = swoMgr.getGroup("", idIns[i], IManager.LEVEL_ALL);
		
		Date date = group.getCreationDate();
		
		//Date date = new Date();		
		
		long longDate = date.getTime();
		long resultDate = longDate - (i * 10000);
		date.setTime(resultDate);
		group.setCreationDate(date);
		group.setPicture(null);
		
		swoMgr.setGroup("", group, IManager.LEVEL_ALL);
		System.out.println(i + "번째 Group : " + group.getId());
		
		
		Thread.sleep(300);
		
		CourseDetail couresDetail = seraMgr.getCourseDetailById(group.getId());
		couresDetail.setCreateDate(date);
		couresDetail.setStart(date);
		couresDetail.setEnd(date);
		
		seraMgr.setCourseDetail(couresDetail);
		System.out.println(i + "번째 CourseDetail : " + group.getId());

		Thread.sleep(300);
	}
	
	
	 */
	
	

%>

</body>
</html>
