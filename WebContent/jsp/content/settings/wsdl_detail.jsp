<%@page import="net.smartworks.model.service.WSDLOperation"%>
<%@page import="net.smartworks.model.service.WSDLPort"%>
<%@page import="net.smartworks.model.service.WSDLDetail"%>
<%@page import="net.smartworks.model.service.Variable"%>
<%@page import="net.smartworks.model.service.WebService"%>
<%@page import="net.smartworks.model.approval.Approval"%>
<%@page import="net.smartworks.model.approval.ApprovalLine"%>
<%@page import="net.smartworks.model.community.Community"%>
<%@page import="net.smartworks.model.calendar.CompanyEvent"%>
<%@page import="net.smartworks.model.calendar.CompanyCalendar"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.model.calendar.WorkHour"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.model.calendar.WorkHourPolicy"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 현재사용자 정보도 가져온다..
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();
	
	String serviceId = request.getParameter("serviceId");
	String wsdlUri = request.getParameter("wsdlUri");
	String selectedPortStr = request.getParameter("selectedPort");
	String selectedOperationStr = request.getParameter("selectedOperation");
	int selectedPort = (SmartUtil.isBlankObject(selectedPortStr)) ? 0 : Integer.parseInt(selectedPortStr);
	int selectedOperation = (SmartUtil.isBlankObject(selectedOperationStr)) ? 0 : Integer.parseInt(selectedOperationStr);
	
	WSDLDetail wsdlDetail = new WSDLDetail();
	if(!SmartUtil.isBlankObject(wsdlUri)){
		if(SmartUtil.isBlankObject(wsdlDetail) || !wsdlUri.equals(wsdlDetail.getWsdlUri())) wsdlDetail = smartWorks.getWsdlDetailFromUri(wsdlUri);
	}else if(!SmartUtil.isBlankObject(serviceId)){
		WebService webService = (WebService)session.getAttribute("webService");
		wsdlDetail = webService.getWSDLDetail();
	}else{
		wsdlDetail = (WSDLDetail)session.getAttribute("wsdlDetail");
	}
	session.setAttribute("wsdlDetail", wsdlDetail);		
	
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<tr class="js_wsdl_detail">
	<th><fmt:message key="settings.title.webservice.port"/></th>
	<td>
		<select name="selWebServicePort" class="js_webservice_port">
			<%
			WSDLPort[] ports = null;
			if(wsdlDetail != null) {
				ports = wsdlDetail.getPorts();
				if(!SmartUtil.isBlankObject(ports) && ports.length>0){
					for(int i=0; i<ports.length; i++){
			%>
						<option <%if(i==selectedPort){%>selected<%} %> index="<%=i %>" value="<%=ports[i].getPort()%>"> <%=ports[i].getPort() %></option>
			<%
					}
				}
			}
			%>
		</select>
	</td>
</tr>
<tr class="js_wsdl_detail">
	<th><fmt:message key="settings.title.webservice.operation"/></th>
	<td>
		<select name="selWebServiceOperation" class="js_webservice_operation">
			<%
			if(!SmartUtil.isBlankObject(ports) && ports.length>selectedPort){
				WSDLOperation[] operations = ports[selectedPort].getOperations();
				if(!SmartUtil.isBlankObject(operations) && operations.length>0){
					for(int i=0; i<operations.length; i++){
				
			%>
						<option <%if(i==selectedOperation){%>selected<%} %> index="<%=i%>" value="<%=operations[i].getOperation()%>"><%=operations[i].getOperation() %></option>
			<%
					}
				}
			}
			%>
		</select>
	</td>
</tr>
<tr class="js_wsdl_detail">
	<%
	boolean inputVarExisting = ((!SmartUtil.isBlankObject(ports) && ports.length>selectedPort) 
								&& (!SmartUtil.isBlankObject(ports[selectedPort].getOperations()) && ports[selectedPort].getOperations().length>selectedOperation)
								&& (!SmartUtil.isBlankObject(ports[selectedPort].getOperations()[selectedOperation].getInputVariables()) && ports[selectedPort].getOperations()[selectedOperation].getInputVariables().length>0))
								? true : false;
	%>
	<th <%if(inputVarExisting){%>class="required_label"<%} %>><fmt:message key="settings.title.webservice.input_variables"/></th>
	<td>
		<table style="width:100%">
			<tr>
				<th class="tc" style="width:33%"><fmt:message key="settings.title.variable.name"/></th>
				<th class="tc" style="width:33%"><fmt:message key="settings.title.variable.element_name"/></th>
				<th class="tc" style="width:33%"><fmt:message key="settings.title.variable.element_type"/></th>
			</tr>
			<%
			if(inputVarExisting){
				Variable[] inputVariables = ports[selectedPort].getOperations()[selectedOperation].getInputVariables();
				for(int count=1; count<inputVariables.length+1; count++){
					Variable inputVariable = inputVariables[count-1]; 
			%>
					<tr>
						<td><input class="fieldline required" name="txtInputVariableName<%=count %>" type="text" value="<%=CommonUtil.toNotNull(inputVariable.getName())%>"></td>
						<td><input class="tc" readonly name="txtInputElementName<%=count %>" type="text" value="<%=CommonUtil.toNotNull(inputVariable.getElementName())%>"></td>
						<td><input class="tc" readonly name="txtInputElementType<%=count %>" type="text" value="<%=CommonUtil.toNotNull(inputVariable.getElementType())%>"></td>
					</tr>				
			<%
				}
			}
			%>
		</table>
	</td>
</tr>
<tr class="js_wsdl_detail">
	<%
	boolean returnVarExisting = ((!SmartUtil.isBlankObject(ports) && ports.length>selectedPort) 
								&& (!SmartUtil.isBlankObject(ports[selectedPort].getOperations()) && ports[selectedPort].getOperations().length>selectedOperation)
								&& (!SmartUtil.isBlankObject(ports[selectedPort].getOperations()[selectedOperation].getReturnVariables()) && ports[selectedPort].getOperations()[selectedOperation].getReturnVariables().length>0))
								? true : false;
	%>
	<th <%if(returnVarExisting){ %>class="required_label"<%} %>><fmt:message key="settings.title.webservice.return_variables"/></th>
	<td>
		<table style="width:100%">
			<tr>
				<th class="tc" style="width:33%"><fmt:message key="settings.title.variable.name"/></th>
				<th class="tc" style="width:33%"><fmt:message key="settings.title.variable.element_name"/></th>
				<th class="tc" style="width:33%"><fmt:message key="settings.title.variable.element_type"/></th>
			</tr>
			<%
			if(returnVarExisting){
				Variable[] returnVariables = ports[selectedPort].getOperations()[selectedOperation].getReturnVariables();
				for(int count=1; count<returnVariables.length+1; count++){
					Variable returnVariable = returnVariables[count-1]; 
			%>
					<tr>
						<td><input class="fieldline required" name="txtReturnVariableName<%=count %>" type="text" value="<%=CommonUtil.toNotNull(returnVariable.getName())%>"></td>
						<td><input class="tc" readonly name="txtReturnElementName<%=count %>" type="text" value="<%=CommonUtil.toNotNull(returnVariable.getElementName())%>"></td>
						<td><input class="tc" readonly name="txtReturnElementType<%=count %>" type="text" value="<%=CommonUtil.toNotNull(returnVariable.getElementType())%>"></td>
					</tr>				
			<%
				}
			}
			%>
		</table>
	</td>
</tr>
