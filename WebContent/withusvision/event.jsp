<%@page import="net.smartworks.server.engine.common.model.Order"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="java.util.Date"%>
<%@page import="net.smartworks.server.engine.common.util.DateUtil"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.server.engine.infowork.domain.model.SwdRecordCond"%>
<%@page import="net.smartworks.server.engine.infowork.domain.model.SwdRecord"%>
<%@page import="net.smartworks.server.engine.common.manager.IManager"%>
<%@page import="net.smartworks.server.engine.process.process.model.PrcProcessInstExtend"%>
<%@page import="net.smartworks.server.engine.process.process.model.PrcProcessInstCond"%>
<%@page import="java.io.File"%>
<%@page import="net.smartworks.server.engine.process.task.model.TskTaskCond"%>
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
 <style type="text/css">
	.party_tbl_new {
	 border-top:1px solid #bbbbbb;
	}
	.party_tbl_new tr td {
	 font-family: "굴림";
	 font-size: 12px;
	 color: #666666;
	 text-align: center;
	 border-bottom:1px solid #bbbbbb; border-left:1px solid #bbbbbb;
	}
	.party_tbl_new tr th {
	 font-family: "굴림";
	 font-size: 12px;
	 color: #333333;
	 font-weight: bold;
	 text-align: center;
	 border-bottom:1px solid #bbbbbb; border-left:1px solid #bbbbbb; background-color: #f3f3f3;
	}
	.border_last{
	 border-right:1px solid #bbbbbb; 
	}
</style>


</head>
<body>
<%

	String formId = "frm_event_SYSTEM";
	String domainId = "frm_event_SYSTEM";
	
	SwdRecordCond cond = new SwdRecordCond();
	cond.setFormId(formId);
	cond.setDomainId(domainId);
	cond.setOrders(new Order[]{new Order("startdate", false)});
	SwdRecord[] records = SwManagerFactory.getInstance().getSwdManager().getRecords("", cond, IManager.LEVEL_ALL);
	if (records != null && records.length != 0) {
%>
<table class="party_tbl_new" width="100%" cellspacing="0" cellpadding="0">
	<tr>
		<th width="10%">제목</th>
		<th width="10%">시작일</th>
		<th width="10%">종료일</th>
		<th width="10%">장소</th>
		<th width="20%">참여자</th>	
		<th width="39%">내용</th>	
	</tr>
<%		
		for (int i = 0; i < records.length; i++) {
			
			SwdRecord record = records[i];
			
			String title = CommonUtil.toNotNull(record.getDataFieldValue("0"));
			String startDateStr = record.getDataFieldValue("1");
			String endDateStr = record.getDataFieldValue("2");
			String startDate = "";
			String endDate = "";
			if (!CommonUtil.isEmpty(startDateStr)) {
				
				Date sDate = DateUtil.toDate(startDateStr,"yyyy-MM-dd hh:mm:ss");
				long time = sDate.getTime() + (1000 * 60 * 60 * 9);
				sDate.setTime(time);
				startDate = DateUtil.toDateString(sDate);
				
			}
			if (!CommonUtil.isEmpty(endDateStr)) {
				Date eDate = DateUtil.toDate(endDateStr,"yyyy-MM-dd hh:mm:ss");
				long time = eDate.getTime() + (1000 * 60 * 60 * 9);
				eDate.setTime(time);
				endDate = DateUtil.toDateString(eDate);
			}
			
			
			String place = CommonUtil.toNotNull(record.getDataFieldValue("4"));
			String people = CommonUtil.toNotNull(record.getDataFieldValue("5"));
			String content = CommonUtil.toNotNull(record.getDataFieldValue("6"));
%>
	<tr>
 		<td width="10%"><%=title %></td>
		<td width="10%"><%=startDate %></td>
		<td width="10%"><%=endDate %></td>
		<td width="10%"><%=place %></td>
		<td width="20%"><%=people %></td>
		<td width="40%"><%=content %></td>
	</tr>
<%
		}
%>
</table>
<%
	}
%>




</body>









</html>
