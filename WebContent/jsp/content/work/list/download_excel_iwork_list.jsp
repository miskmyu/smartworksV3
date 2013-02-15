<%@page contentType="text/html;charset=utf-8"%>
<%@page import="net.smartworks.util.SmartMessage"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="net.smartworks.model.instance.FieldData"%>
<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.model.instance.info.IWInstanceInfo"%>
<%@page import="net.smartworks.model.work.FormField"%>
<%@page import="net.smartworks.model.instance.SortingField"%>
<%@page import="net.smartworks.model.instance.info.InstanceInfoList"%>
<%@page import="net.smartworks.model.work.InformationWork"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.instance.info.RequestParams"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@page language="java" import="java.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%

	ISmartWorks smartWorks = (ISmartWorks)request.getAttribute("smartWorks");
	RequestParams params = (RequestParams)request.getAttribute("requestParams");

	User cUser = SmartUtil.getCurrentUser();
	InformationWork work = (InformationWork)session.getAttribute("smartWork");
	String workId = work.getId();
	if(SmartUtil.isBlankObject(params)){
		String savedWorkId = (String)session.getAttribute("workId");
		params = (RequestParams)session.getAttribute("requestParams");
		if(!workId.equals(savedWorkId) || SmartUtil.isBlankObject(params)){
			params = new RequestParams();
			params.setPageSize(20);
			params.setCurrentPage(1);
		}
	}
	session.setAttribute("requestParams", params);
	session.setAttribute("workId", workId);
	InstanceInfoList instanceList = smartWorks.getIWorkInstanceList(workId, params);
	
	String workName = work.getName()+ "_LIST.xls";
	String name = new String(workName.getBytes(),"ISO8859_1"); 
	
	String arg1 = "Content-Disposition";
	String arg2 = "attachment; filename=" + name;
  	response.setHeader(arg1, arg2);
  	response.setContentType("application/vnd.ms-excel;charset=euc-kr");
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!-- 목록 테이블 -->
<table border='1px'>
	<%	
	SortingField sortedField = null;
	int pageSize = 20, totalPages = 1, currentPage = 1;
	if (instanceList != null && work != null) {
		int type = instanceList.getType();
		sortedField = instanceList.getSortedField();
		if(sortedField==null) sortedField = new SortingField();
	%>
		<tr class="tit_bg">
	 		<th class="r_line" style="width:40px;">
				<span><fmt:message key="common.title.number"/></span>
			</th>
			<%
			FormField[] fields = work.getDisplayFields();
			if (fields != null) {
				for (FormField field : fields) {
			%>
			 		<th class="r_line">
			 			<%=field.getName()%>
					</th>
			<%
				}
			}
			%>
			<th class="r_line">
				<fmt:message key='common.title.last_modifier' />
			</th>		
	 		<th style="width:40px;">
				<span><fmt:message key="common.title.views"/></span>
			</th>
		</tr>	
	<%
		pageSize = instanceList.getPageSize();
		totalPages = instanceList.getTotalPages();
		currentPage = instanceList.getCurrentPage();
		int currentCount = instanceList.getTotalSize()-(currentPage-1)*pageSize;
		FormField[] displayFields = work.getDisplayFields();
		if(instanceList.getInstanceDatas() != null) {
			IWInstanceInfo[] instanceInfos = (IWInstanceInfo[]) instanceList.getInstanceDatas();
			for (IWInstanceInfo instanceInfo : instanceInfos) {
				UserInfo owner = instanceInfo.getOwner();
				UserInfo lastModifier = instanceInfo.getLastModifier();
				FieldData[] fieldDatas = instanceInfo.getDisplayDatas();
			%>
				<tr class="instance_list js_content_work_space">
					<td class="tc"><%=currentCount%></td>
					<%
					currentCount--;
					if ((fieldDatas != null) && (fieldDatas.length == displayFields.length)) {
						NumberFormat nf = NumberFormat.getNumberInstance();
						int count = 0;
						for (FieldData data : fieldDatas) {
					%>
							<td <%if(data.getFieldType().equals(FormField.TYPE_CURRENCY) || 
								data.getFieldType().equals(FormField.TYPE_NUMBER) || 
								data.getFieldType().equals(FormField.TYPE_PERCENT)){ %>
										class="tr pr10"
									<%}else if(data.getFieldType().equals(FormField.TYPE_FILE)){%>
										class="tc"
									<%}%>>
									<%if(data.getFieldType().equals(FormField.TYPE_FILE) && !SmartUtil.isBlankObject(data.getValue())){%>
									<%}else if(data.getFieldType().equals(FormField.TYPE_NUMBER)){%><%=data.getValue() != null ? CommonUtil.toNotNull(nf.format(Float.parseFloat(data.getValue()))) : CommonUtil.toNotNull(data.getValue())%>
									<%}else if(data.getFieldType().equals(FormField.TYPE_PERCENT)){%><%=data.getValue() != null ? CommonUtil.toNotNull(nf.format(Float.parseFloat(data.getValue()))) + "%" : CommonUtil.toNotNull(data.getValue())%>
									<%}else if(data.getFieldType().equals(FormField.TYPE_CURRENCY)){%><%=data.getSymbol()%><%=data.getValue() != null ? CommonUtil.toNotNull(nf.format(Float.parseFloat(data.getValue()))) : CommonUtil.toNotNull(data.getValue())%>
									<%}else if(data.getFieldType().equals(FormField.TYPE_IMAGE) ||
												data.getFieldType().equals(FormField.TYPE_RICHTEXT_EDITOR) ||
												data.getFieldType().equals(FormField.TYPE_DATA_GRID)){%>
									<%}else if(data.getFieldType().equals(FormField.TYPE_CHECK_BOX)){%><%=(data.getValue() != null && data.getValue().equals("true")) ? SmartMessage.getString("common.title.boolean.true") : SmartMessage.getString("common.title.boolean.false")%>
									<%}else{%><%=CommonUtil.toNotNull(data.getValue())%><%} %>
							</td>
					<%
						}
					}
					%>
					<td>
						<div class="noti_pic">
						</div>
						<div class="noti_in_s">
							<span class="t_name"><%=lastModifier.getLongName()%></span>
						</div>
					</td>
					<td class="tc"><%=instanceInfo.getViews() %></td>
				</tr>
		<%
			}
		}
	}else if(!SmartUtil.isBlankObject(work)){
	%>
		<tr class="tit_bg">
	 		<th class="r_line" style="width:40px;">
				<span><fmt:message key="common.title.number"/></span>
			</th>
			<%
			sortedField = new SortingField();
			FormField[] fields = work.getDisplayFields();
			if (fields != null) {
				for (FormField field : fields) {
			%>
			 		<th class="r_line">
				 		<span class="<%
						if(sortedField.getFieldId().equals(field.getId())){
							if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
						%>"></span>
					</th>
			<%
				}
			}
			%>
			<th>
				<fmt:message key='common.title.last_modifier' /> 
				<span class="<%if(sortedField.getFieldId().equals(FormField.ID_LAST_MODIFIER)){
					if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} %>"></span>
			</th>		
	 		<th class="r_line" style="width:40px;">
				<span><fmt:message key="common.title.views"/></span>
			</th>
		</tr>	
	<%
	}
	%>
</table>
<%
if(instanceList == null || work == null || SmartUtil.isBlankObject(instanceList.getInstanceDatas())){
%>
	<div class="tc"><fmt:message key="common.message.no_instance"/></div>
<%
}
%>
