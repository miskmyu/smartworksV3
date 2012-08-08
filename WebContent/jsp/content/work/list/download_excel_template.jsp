<%@page import="net.smartworks.model.work.FormField"%>
<%@page import="net.smartworks.model.work.InformationWork"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page language="java" contentType="application/vnd.ms-excel; name='excel', text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();
	
	String workId = request.getParameter("workId");
	InformationWork work = (InformationWork) smartWorks.getWorkById(workId);

	response.setHeader("Content-Disposition", "attachment; filename=smartworks-data-template.xls"); 
	response.setHeader("Content-Description", "Template file generated By SmartWorks.net to be used to import data");
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<style>td.text {mso-number-format:\@}</style>       		
	</head>
	<body>
		<table style="width:100%;cellpadding:0; cellspacing;0; border:1;" id="<%=work.getId()%>" name="<%=work.getName()%>">
			<thead>
				<tr	style="height:24px">
				
					<th	style="background-color:#D3D3D3;width:20px">NO</th>					
					<%
					FormField[] importFields = work.getForm().getImportFields();
					if(!SmartUtil.isBlankObject(importFields)){
						for(int i=0; i<importFields.length; i++){
							FormField field = importFields[i];
							String type = field.getType();
					%>
							<th	id="<%=field.getId()%>" style="background-color:#D3D3D3;"><%=field.getName()%></th>
					<%
						}
					}
					%>
				</tr>
				<%
				for(int j=0; j<100; j++){
				%>
					<tr style="height:24px; border:1;">
						<th style="text-align:center;width:30px"><%=j+1%></th>
						<%
						if(!SmartUtil.isBlankObject(importFields)){
							String msgFormat = null;
							for(int i=0; i<importFields.length; i++){
								FormField field = importFields[i];
								String type = field.getType();
						%>
								<th	id="<%=field.getId()%>" style="<%=field.getStyle()%>"></th>
						<%
							}
						}
						%>
					</tr>
				<%
				}
				%>
			</thead>
		<tbody>
	</body>
</html>