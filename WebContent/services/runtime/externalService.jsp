<%@page import="net.smartworks.server.service.factory.SwServiceFactory"%>
<%@page import="net.smartworks.server.engine.resource.util.lang.ExceptionUtil"%>
<%@page import="net.smartworks.server.engine.process.task.model.TskTask"%>
<%@page import="net.smartworks.server.engine.common.manager.IManager"%>
<%@page import="net.smartworks.server.engine.common.collection.manager.IColManager"%>
<%@page import="net.smartworks.server.engine.factory.SwManagerFactory"%>
<%@page import="net.smartworks.server.engine.common.collection.model.ColMapCond"%>
<%@page import="net.smartworks.server.engine.common.collection.model.ColMap"%>
<%@page import="net.smartworks.server.engine.common.model.Property"%>
<%@page import="net.smartworks.server.engine.common.model.Properties"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@ page contentType="text/xml; charset=UTF-8" %>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%!
//
%>
<%
	response.setHeader("Pragma", "no-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Expires", "0");
	
	String userId = request.getParameter("userId");
	String method = CommonUtil.toNotNull(request.getParameter("method"));
	StringBuffer buffer = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
	
	try {
		if(method.equals("")) {
			buffer.append("<Result status=\"Failed\"><message>Invalid method! Not found method parameter</message><trace/></Result>");
		
		} else if(method.equals("initiateProcess")) {
			//프로세스를 시작한다.
			
			String title = request.getParameter("title");
			String processId = request.getParameter("processId");
			String seqNo = CommonUtil.toNotNull(request.getParameter("seqNo"));
			String userSabon = CommonUtil.toNotNull(request.getParameter("userSabon"));
			String propsXml = request.getParameter("propsXml");
			System.out.println("################### BPMS INITIATE PROCESS ################################");
			System.out.println(">>>title" + title);
			System.out.println(">>>processId : " + processId);
			System.out.println(">>>propsXml : " + propsXml);
			System.out.println(">>>userId : " + userId);

			Properties props = (Properties)Properties.toObject(propsXml);

			Property[] propArray = null;
			if (props != null)
				propArray = props.getProperties();

			String instanceId = SwServiceFactory.getInstance().getInstanceService().initiateProcessByExternalFormInfo(userId, title, processId, propArray);

		/* 	ColMap colMap = new ColMap();
			ColMapCond colMapCond = new ColMapCond();
			colMapCond.setToRef(seqNo + "_" + userSabon);
			IColManager colMgr = SwManagerFactory.getInstance().getColManager();
			ColMap[] colMaps = colMgr.getMaps(userId, colMapCond, IManager.LEVEL_ALL);
			if (colMaps == null || colMaps.length == 0) {
				colMap.setName("");
				colMap.setType("keymapping");
				colMap.setFromType("instanceId");
				colMap.setFromRef(instanceId);
				colMap.setToType("seqNo_userSabon");
				colMap.setToRef(seqNo + "_" + userSabon);
				colMgr.setMap(userId, colMap, IManager.LEVEL_ALL);
			} */

			System.out.println("################### BPMS INITIATE PROCESS END ################################");
			buffer.append("<Result status=\"Ok\">");
			buffer.append("<InstanceId>").append(instanceId).append("</InstanceId>");
			buffer.append("</Result>");
		
		} else if(method.equals("executeTask")) {
			//업무태스크를 실행한다 , 할당자가 누구이건 파라미터로 넘어온 실행자로 업무를 처리한다
			
			String action = request.getParameter("action");
			String taskId = request.getParameter("taskId");
			String propsXml = request.getParameter("propsXml");

			Properties props = (Properties)Properties.toObject(propsXml);

			Property[] propArray = null;
			if (props != null)
				propArray = props.getProperties();

			TskTask task = SwServiceFactory.getInstance().getInstanceService().executeTaskByExternalFormInfo(userId, action, taskId, propArray);
			buffer.append("<Result status=\"Ok\">");
			buffer.append("<InstanceId>").append(task.getProcessInstId()).append("</InstanceId>");
			buffer.append("</Result>");
			
		} else if(method.equals("executeInstanceByUserInfo")) {
			//userId 로들어온 아이디 값과 현재 할당 되어 있는 태스크의 담당자가 틀리면 업무를 처리하지 않고 Exception 을 발생한다
			String action = request.getParameter("action");
			String instanceId = request.getParameter("instanceId");
			String propsXml = request.getParameter("propsXml");

			Properties props = (Properties)Properties.toObject(propsXml);
			
			Property[] propArray = null;
			if (props != null)
				propArray = props.getProperties();
			
			TskTask task = SwServiceFactory.getInstance().getInstanceService().executeInstanceByUserIdAndExternalFormInfo(userId, action, instanceId, propArray);
			buffer.append("<Result status=\"Ok\">");
			buffer.append("<InstanceId>").append(task.getProcessInstId()).append("</InstanceId>");
			buffer.append("</Result>");

		} else if(method.equals("executeInstanceByMainKey")) { //주요 Key 값에 따른 업무 처리 (Key값이 포함되어 있는 instanceId를 찾아(LinkMap) 업무처리 한다.)
			
		//업무를 위임한다
		} else if(method.equals("entrustTask")) {
		//업무리스트를 가져온다
		} else if(method.equals("getTodoList")) {
			
					
		} else {
			buffer.append("<Result status=\"Failed\"><message>Invalid method! Not found method parameter</message><trace/></Result>");
		}
		
	} catch (Throwable th) {
		buffer.append("<Result status=\"Failed\">");
		buffer.append("<message>");
		buffer.append("Failed to execute method (" + method + ") - " + th.getMessage());
		buffer.append("</message>");
		buffer.append("<trace><![CDATA[");
		buffer.append(ExceptionUtil.getTraceMessage("", th));
		buffer.append("]]></trace>");
		buffer.append("</Result>");
		LogFactory.getLog("externalService").error(buffer.toString());
	}
%><%= buffer.toString() %>