<%@page import="java.util.Date"%>
<%@page import="net.smartworks.server.engine.process.approval.model.AprApproval"%>
<%@page import="net.smartworks.server.engine.process.approval.model.AprApprovalLine"%>
<%@page import="net.smartworks.server.engine.common.model.Property"%>
<%@page import="net.smartworks.server.engine.common.manager.IManager"%>
<%@page import="net.smartworks.server.engine.factory.SwManagerFactory"%>
<%@page import="net.smartworks.server.engine.process.task.model.TskTask"%>
<%@page import="net.smartworks.server.engine.process.task.model.TskTaskCond"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
</head>
<body>
<%
	String[] instanceIds = new String[1];
	instanceIds[0]="5ef4e5633b792c1a013b827bb2710693";

	for (int k = 0; k < instanceIds.length; k++) {
		String instanceId = instanceIds[k];
		
		TskTaskCond taskCond = new TskTaskCond();
		taskCond.setProcessInstId(instanceId);
		taskCond.setType("APPROVAL");
		TskTask[] approvalTasks = SwManagerFactory.getInstance().getTskManager().getTasks("", taskCond, IManager.LEVEL_ALL);
		
		for (int i = 0; i < approvalTasks.length; i++) {
			
			TskTask apprTask = approvalTasks[i];
			
			String refTaskId = apprTask.getExtendedPropertyValue("taskRef");
			if (CommonUtil.isEmpty(refTaskId)) {
				System.out.println("######################### instanceId : " + instanceId + " - RefTask ID is Null ! ##########################");
				continue;
			}
			TskTask refTask = SwManagerFactory.getInstance().getTskManager().getTask("", refTaskId, IManager.LEVEL_ALL);
			if (CommonUtil.isEmpty(refTask)) {
				System.out.println("######################### instanceId : " + instanceId + " - RefTask is Null ! ##########################");
				continue;
			}
			String domainId = refTask.getExtendedPropertyValue("domainId");
			String recordId = refTask.getExtendedPropertyValue("recordId");
		
			String taskDef = domainId + "|" + recordId; 
			String approvalLine = refTask.getExtendedPropertyValue("approvalLine");
			String subject = refTask.getExtendedPropertyValue("subject");
			String content = refTask.getExtendedPropertyValue("m_Description");
			
			apprTask.setApprovalId(approvalLine);
			apprTask.setDef(taskDef);
			
			apprTask.addExtendedProperty(new Property("refAppLineDefId", null));
			apprTask.addExtendedProperty(new Property("txtApprovalSubject", subject));
			apprTask.addExtendedProperty(new Property("txtApprovalComments", content));
			
			SwManagerFactory.getInstance().getTskManager().setTask("", apprTask, IManager.LEVEL_ALL);
			
			AprApprovalLine aprLine = SwManagerFactory.getInstance().getAprManager().getApprovalLine("", approvalLine, IManager.LEVEL_ALL);
			if (aprLine != null) {
				AprApproval[] apr = aprLine.getApprovals();
				for (int j = 0; j < apr.length; j++) {
					Date modifyDate = apr[j].getModificationDate();
					if (modifyDate != null)
						continue;
					apr[j].setModificationDate(aprLine.getCreationDate());
				}
				SwManagerFactory.getInstance().getAprManager().setApprovalLine("", aprLine, IManager.LEVEL_ALL);
			}
		}
		System.out.println("@@ "+k+" @@ Approval Instance Done! - InstanceId [" +  instanceId + "]");
		Thread.sleep(100);
	}

%>
</body>
</html>