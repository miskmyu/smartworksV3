<%@page import="java.io.CharArrayWriter"%>
<%@page import="java.nio.charset.Charset"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="net.smartworks.model.work.info.SmartTaskInfo"%>
<%@page import="net.smartworks.model.instance.Instance"%>
<%@page import="net.smartworks.model.instance.SortingField"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.service.impl.SmartWorks"%>
<%@page import="net.smartworks.model.instance.info.TaskInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.PWInstanceInfo"%>
<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.model.instance.info.InstanceInfo"%>
<%@page import="net.smartworks.model.work.ProcessWork"%>
<%@page import="net.smartworks.model.instance.FieldData"%>
<%@page import="net.smartworks.model.instance.info.InstanceInfoList"%>
<%@page import="net.smartworks.model.instance.info.RequestParams"%>
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
	RequestParams params = (RequestParams)request.getAttribute("requestParams");
	User cUser = SmartUtil.getCurrentUser();
	ProcessWork work = (ProcessWork)session.getAttribute("smartWork");
	SmartTaskInfo[] tasks = work.getDiagram().getTasks();
	SmartTaskInfo startTask = null;
	if(!SmartUtil.isBlankObject(tasks)){
		for(int i=0; i<tasks.length; i++){
			if(tasks[i].isStartTask()){
				startTask = tasks[i];
				break;
			}
		}
		startTask = tasks[0];
	}
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

	InstanceInfoList instanceList = smartWorks.getPWorkInstanceList(workId, params);
	
	String workName = work.getName()+ "_LIST.xls";
	String name = new String(workName.getBytes(),"ISO8859_1"); 
	
	String arg1 = "Content-Disposition";
	String arg2 = "attachment; filename=" + name;
  	response.setHeader(arg1, arg2);
  	response.setContentType("application/vnd.ms-excel;charset=euc-kr");
	
	
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!-- 목록페이지 -->
<table border='1px'>
	<%
	SortingField sortedField = new SortingField();
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
			<th class="r_line">
	 			<fmt:message key='common.title.status'/>
			</th>
			<th class="r_line">
	 			<fmt:message key='common.title.owner'/>
			</th>				
			<th class="r_line">
	 			<fmt:message key='common.title.instance_subject'/>
			</th>
			<th class="r_line">
	 			<fmt:message key='common.title.last_task'/>
			</th>
			<th>
				<fmt:message key='common.title.last_modifier' />
			</th>
		</tr>	
		<%	
		pageSize = instanceList.getPageSize();
		totalPages = instanceList.getTotalPages();
		currentPage = instanceList.getCurrentPage();
		int currentCount = instanceList.getTotalSize()-(currentPage-1)*pageSize;
		if(instanceList.getInstanceDatas() != null) {
			PWInstanceInfo[] instanceInfos = (PWInstanceInfo[])instanceList.getInstanceDatas();
			for (PWInstanceInfo instanceInfo : instanceInfos) {
				UserInfo owner = instanceInfo.getOwner();
				UserInfo lastModifier = instanceInfo.getLastModifier();
				TaskInstanceInfo lastTask = instanceInfo.getLastTask();
				String target = instanceInfo.getController() + "?cid=" + instanceInfo.getContextId() + "&workId=" + workId;
				String statusImage = "";
				String statusTitle = "";
				switch (instanceInfo.getStatus()) {
				// 인스턴스가 현재 진행중인 경우..
				case Instance.STATUS_RUNNING:
					statusImage = "icon_status_running";
					statusTitle = "content.status.running";
					break;
				// 인스턴스가 지연진행중인 경우....
				case Instance.STATUS_DELAYED_RUNNING:
					statusImage = "icon_status_d_running";
					statusTitle = "content.status.delayed_running";
					break;
				// 인스턴스가 반려된 경우...
				case Instance.STATUS_RETURNED:
					statusImage = "icon_status_returned";
					statusTitle = "content.status.returned";
					break;
				// 인스턴스가 완료된 경우...
				case Instance.STATUS_COMPLETED:
					statusImage = "icon_status_completed";
					statusTitle = "content.status.completed";
					break;
				// 기타 잘못되어 상태가 없는 경우..
				default:
					statusImage = "icon_status_not_yet";
					statusTitle = "content.status.not_yet";
				}
			%>
				<tr class="instance_list js_content_work_space" href="<%=target%>">
					<td class="tc vm"><%=currentCount%></td>
					<td class="tc vm">
						<fmt:message key='<%=statusTitle%>'/>
					</td>
					<td>
						<span class="t_name"><%=owner.getLongName()%></span>
					</td>
					<td>
						<%=instanceInfo.getSubject()%>
					</td>
					<td>
						<%=lastTask.getName()%>
					<td>
						<%
						if(!SmartUtil.isBlankObject(lastModifier)){
						%>
							<span class="t_name"><%=lastModifier.getLongName()%></span>
						<%
						}
						%>
					</td>
				</tr>
	<%
				currentCount--;
			}
		}
	}else if(!SmartUtil.isBlankObject(work)){
			sortedField = new SortingField();
	%>
		<tr class="tit_bg">
	 		<th class="r_line" style="width:40px;">
				<span><fmt:message key="common.title.number"/></span>
			</th>
			<th class="r_line">
	 			<fmt:message key='common.title.status'/>
			</th>
			<th class="r_line">
	 			<fmt:message key='common.title.owner'/>
			</th>				
			<th class="r_line">
	 			<fmt:message key='common.title.instance_subject'/>
			</th>
			<th class="r_line">
	 			<fmt:message key='common.title.last_task'/>
			</th>
			<th>
				<fmt:message key='common.title.last_modifier' />
			</th>
		</tr>	
	<%		
	}
	%>
</table>
<!-- 목록페이지 //-->

<%
if(instanceList == null || work == null || SmartUtil.isBlankObject(instanceList.getInstanceDatas())){
%>
	<div class="tc"><fmt:message key="common.message.no_instance"/></div>
<%
}
%>
