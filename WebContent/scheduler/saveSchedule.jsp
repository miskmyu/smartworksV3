<%@page import="org.quartz.impl.StdScheduler"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="net.smartworks.server.engine.scheduling.model.ScheduleDef"%>
<%@page import="net.smartworks.server.engine.factory.SwManagerFactory"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%!
	public static Object getBean(String beanName, HttpServletRequest request) {
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
		return (Object) wac.getBean(beanName);
	}
%>
<%
	String method = request.getParameter("method");
	if (method.equalsIgnoreCase("save")) {
		String objId = request.getParameter("objId");
		String name = request.getParameter("name");
		String packageType = request.getParameter("packageType");
		String packageId = request.getParameter("packageId");
		String targetFormId = request.getParameter("targetFormId");
		String targetFieldId = request.getParameter("targetFieldId");
		String status = request.getParameter("status");
		String groupName = request.getParameter("groupName");
		String jobName = request.getParameter("jobName");
		String triggerName = request.getParameter("triggerName");
		String targetClass = request.getParameter("targetClass");
		String cronExpression = request.getParameter("cronExpression");
		String isAutoStart = request.getParameter("isAutoStart");
		
		ScheduleDef def = null;
		if (!CommonUtil.isEmpty(objId)) {
			def = SwManagerFactory.getInstance().getScheduleManager().getScheduleDef("", objId, null);
			if (def == null) {
				def = new ScheduleDef();
				def.setObjId(objId);
			}
		} else {
			def = new ScheduleDef();
		}
		def.setName(name);
		def.setTargetPackageType(packageType);
		def.setTargetPackageId(packageId);
		def.setTargetFormId(targetFormId);
		def.setTargetFieldId(targetFieldId);
		def.setStatus(status);
		def.setGroupName(groupName);
		def.setJobName(jobName);
		def.setTriggerName(triggerName);
		def.setTargetClass(targetClass);
		def.setCronExpression(cronExpression);
		def.setIsAutoStart(isAutoStart);
		
		SwManagerFactory.getInstance().getScheduleManager().setScheduleDef("", def, null);
		
	} else if (method.equalsIgnoreCase("delete")) {
		String objId = request.getParameter("objId");
		SwManagerFactory.getInstance().getScheduleManager().removeScheduleDef("", objId);
	} else if (method.equalsIgnoreCase("run")) {
		StdScheduler sc = (StdScheduler)getBean("schedulerFactoryBean", request);
		String objId = request.getParameter("objId");
		ScheduleDef def = SwManagerFactory.getInstance().getScheduleManager().getScheduleDef("", objId, null);
		SwManagerFactory.getInstance().getScheduleManager().fireScheduleDef("", def, sc);
	} else if (method.equalsIgnoreCase("stop")) {
		StdScheduler sc = (StdScheduler)getBean("schedulerFactoryBean", request);
		String objId = request.getParameter("objId");
		ScheduleDef def = SwManagerFactory.getInstance().getScheduleManager().getScheduleDef("", objId, null);
		SwManagerFactory.getInstance().getScheduleManager().stopScheduleDef("", def, sc);
	}
%>
