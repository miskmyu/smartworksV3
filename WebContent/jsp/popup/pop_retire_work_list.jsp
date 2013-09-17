<%@page import="net.smartworks.model.work.info.UsedWorkInfo"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="net.smartworks.model.instance.SortingField"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.service.impl.SmartWorks"%>
<%@page import="net.smartworks.model.instance.info.IWInstanceInfo"%>
<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.model.instance.info.InstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.InstanceInfoList"%>
<%@page import="net.smartworks.model.instance.info.RequestParams"%>
<%@page import="net.smartworks.model.instance.FieldData"%>
<%@page import="net.smartworks.model.work.FormField"%>
<%@page import="net.smartworks.model.work.SmartForm"%>
<%@page import="net.smartworks.model.filter.SearchFilter"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.model.security.EditPolicy"%>
<%@page import="net.smartworks.model.security.WritePolicy"%>
<%@page import="net.smartworks.model.security.AccessPolicy"%>
<%@page import="net.smartworks.model.work.InformationWork"%>
<%@page import="net.smartworks.model.work.SmartWork"%>
<%@page import="net.smartworks.model.work.Work"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	ISmartWorks smartWorks = (ISmartWorks)request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();
	
	String retiredUserId = request.getParameter("retiredUserId");
	UsedWorkInfo[] usedWorkList =  smartWorks.getUsedWorkListByUserId(retiredUserId);
	
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<div class="list_contents js_pop_instance_list_page" style="height:400px; border:1px solid #c7c7c7;width:666px !important">

	<!-- 목록 테이블 -->
	<table style="width:666px !important">
		<%
		if (usedWorkList != null) {
		%>
			<thead>
				<tr class="tit_bg">
					<th class="r_line" width="40px"><input type="checkbox" class="js_toggle_select_all" /></th>
					<th class="r_line" width="500px"><fmt:message key="common.title.work_name"/></th>		
					<th class="r_line" width="100px"><fmt:message key="common.title.work_instance_count"/></th>		
				</tr>
			</thead>
 			<tbody style="height:360px; overflow-y:auto; position:absolute">
				<%
				for(int i=0; i<usedWorkList.length; i++){
					UsedWorkInfo usedWork = usedWorkList[i];
				%>
					<tr>
						<td class="tc"  width="40px"><input name="chkSelectRetireWork" type="checkbox" value="<%=usedWork.getWork().getId() %>"/></td>
						<td class="tl" width="500px"><%=usedWork.getWorkFullpathName() %></td>
						<td class="tr" width="80px"><%=usedWork.getCreatedInstanceCount()%></td>
					</tr>				
				<%
				}
				%>
			</tbody>
		<%
		}else{
		%>
			<thead>
				<tr class="tit_bg">
					<th class="r_line" width="40px"><input type="checkbox" checked class="js_toggle_select_all" /></th>
					<th class="r_line" width="500px"><fmt:message key="common.title.work_name"/></th>		
					<th class="r_line" width="100px"><fmt:message key="common.title.work_instance_count"/></th>		
				</tr>
			</thead>
		<%
		}
		%>
	</table>
	<%
	if(usedWorkList == null){
	%>
		<div class="tc"><fmt:message key="common.message.no_instance"/></div>
	<%
	}
	%>
</div>
