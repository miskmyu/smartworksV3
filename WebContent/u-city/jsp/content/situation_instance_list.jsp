<%@page import="net.smartworks.model.instance.ProcessWorkInstance"%>
<%@page import="net.smartworks.model.instance.WorkInstance"%>
<%@page import="pro.ucity.util.UcityUtil"%>
<%@page import="net.smartworks.util.SmartTest"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.server.engine.common.model.Property"%>
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
	String FIELD_ID_SERVICE_NAME = "serviceName";
	String FIELD_ID_EVENT_NAME = "eventName";
	String FIELD_ID_TYPE = "type";
	String FIELD_ID_EXTERNAL_DISPLAY = "externalDisplay";
	String FIELD_ID_EVENT_PLACE = "eventPlace";
	String FIELD_ID_EVENT_TIME = "eventTime";
	String FIELD_ID_IS_SMS = "isSms";
	String FIELD_ID_RUNNING_TASK_NAME = "runningTaskName";

	ISmartWorks smartWorks = (ISmartWorks)request.getAttribute("smartWorks");
	RequestParams params = (RequestParams)request.getAttribute("requestParams");
	User cUser = SmartUtil.getCurrentUser();
	ProcessWork work = (ProcessWork)session.getAttribute("smartWork");
	
	String auditId = request.getParameter("auditId");
	
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


	InstanceInfoList instanceList = null;
	if(SmartUtil.isBlankObject(auditId))
		instanceList = smartWorks.getAllUcityPWorkInstanceList(false, params, -1);
	else
		instanceList = smartWorks.getAllUcityPWorkInstanceList(true, params, Integer.parseInt(auditId));


%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!-- 목록페이지 -->
<table>
	<%
	SortingField sortedField = new SortingField();
	int pageSize = 20, totalPages = 1, currentPage = 1;
	if (instanceList != null && work != null) {
		int type = instanceList.getType();
		sortedField = instanceList.getSortedField();
		if(sortedField==null) sortedField = new SortingField();
	%>
		<tr class="tit_bg">
			<th>
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_STATUS%>">상태
			 		<span class="<%
					if(sortedField.getFieldId().equals(FormField.ID_STATUS)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
					%>"></span>
				</a>				
			</th>
			<th>
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FIELD_ID_SERVICE_NAME%>">U-서비스명
			 		<span class="<%
					if(sortedField.getFieldId().equals(FIELD_ID_SERVICE_NAME)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
					%>"></span>
				</a>
			</th>
			<th>				
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FIELD_ID_EVENT_NAME%>">이벤트명
			 		<span class="<%
					if(sortedField.getFieldId().equals(FIELD_ID_EVENT_NAME)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
					%>"></span>
				</a>
			</th>				
			<th>구분</th>
			<th>
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_LAST_TASK%>">진행단계
			 		<span class="<%
					if(sortedField.getFieldId().equals(FormField.ID_LAST_TASK)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
					%>"></span>
				</a>						
			</th>
			<th>
				<a href="" class="js_select_field_sorting" fieldId="<%=FIELD_ID_EVENT_TIME%>">발생일시
					<span class="<%if(sortedField.getFieldId().equals(FIELD_ID_EVENT_TIME)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} %>"></span>
				</a>
			</th>
			<th>
				<a href="" class="js_select_field_sorting" fieldId="<%=FIELD_ID_EVENT_PLACE%>">발생장소
					<span class="<%if(sortedField.getFieldId().equals(FIELD_ID_EVENT_PLACE)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} %>"></span>
				</a>
			</th>
			<th>
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FIELD_ID_EXTERNAL_DISPLAY%>">외부표출
			 		<span class="<%
					if(sortedField.getFieldId().equals(FIELD_ID_EXTERNAL_DISPLAY)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
					%>"></span>
				</a>						
			</th>
			<th>
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FIELD_ID_IS_SMS%>">SMS발송
			 		<span class="<%
					if(sortedField.getFieldId().equals(FIELD_ID_IS_SMS)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
					%>"></span>
				</a>						
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
				
				Property[] extendedProperties = instanceInfo.getExtentedProperty();
				String serviceName = "";
				String eventName = "";
				String externalDisplay = "";
				String eventPlace = "";
				String eventTime = "";
				String isSms = "";
				String titleExternalDisplay = "";
				String runningTaskName = "";
				if(!SmartUtil.isBlankObject(extendedProperties)){
					for(int i=0; i<extendedProperties.length; i++){
						Property extendedProperty = extendedProperties[i];
						if(extendedProperty.getName().equals(FIELD_ID_SERVICE_NAME)){
							serviceName = CommonUtil.toNotNull(extendedProperty.getValue());
						}else if(extendedProperty.getName().equals(FIELD_ID_EVENT_NAME)){
							eventName = CommonUtil.toNotNull(extendedProperty.getValue());							
						}else if(extendedProperty.getName().equals(FIELD_ID_EXTERNAL_DISPLAY)){
							externalDisplay = CommonUtil.toNotNull(extendedProperty.getValue());
							titleExternalDisplay = externalDisplay;
							if(externalDisplay.length() > 12)
								externalDisplay = externalDisplay.substring(0,11) + "...";
						}else if(extendedProperty.getName().equals(FIELD_ID_EVENT_PLACE)){
							eventPlace = CommonUtil.toNotNull(extendedProperty.getValue());							
						}else if(extendedProperty.getName().equals(FIELD_ID_EVENT_TIME)){
							eventTime = CommonUtil.toNotNull(extendedProperty.getValue());							
						}else if(extendedProperty.getName().equals(FIELD_ID_IS_SMS)){
							isSms = ("true".equals(extendedProperty.getValue())) ? "예" : "아니요";			
						}else if(extendedProperty.getName().equals(FIELD_ID_RUNNING_TASK_NAME)){
							runningTaskName = CommonUtil.toNotNull(extendedProperty.getValue());							
						}
					}
				}
				
				UserInfo owner = instanceInfo.getOwner();
				UserInfo lastModifier = instanceInfo.getLastModifier();
//				TaskInstanceInfo lastTask = instanceInfo.getLastTask();
				TaskInstanceInfo[] runningTasks = instanceInfo.getRunningTasks();
				String occurredTaskNames = "";
				String releasedTaskNames = "";
				if(!SmartUtil.isBlankObject(runningTasks)){
					for(int k=0; k<runningTasks.length; k++){
						if("발생".equals(UcityUtil.getServiceTypeName(runningTasks[k].getName()))){
							occurredTaskNames = occurredTaskNames + (SmartUtil.isBlankObject(occurredTaskNames) ? "" : ", ") + runningTasks[k].getName();
						}else if("종료".equals(UcityUtil.getServiceTypeName(runningTasks[k].getName()))){
							releasedTaskNames = releasedTaskNames + (SmartUtil.isBlankObject(releasedTaskNames) ? "" : ", ") + runningTasks[k].getName();
						}
					}
				}
				String serviceType = "";
				String runningTaskNames = "";
				String titleRunrunningTaskNames = "";
				ProcessWorkInstance instance = null; //(ProcessWorkInstance)smartWorks.getWorkInstanceById(SmartWork.TYPE_PROCESS, workId, instanceInfo.getId());
				if(!SmartUtil.isBlankObject(occurredTaskNames) && UcityUtil.isAbendable(instance))
					occurredTaskNames = "비정상(" + occurredTaskNames + ")";
				if(!SmartUtil.isBlankObject(releasedTaskNames) && UcityUtil.isAbendable(instance))
					releasedTaskNames = "비정상(" + releasedTaskNames + ")";
				if(!SmartUtil.isBlankObject(occurredTaskNames) && !SmartUtil.isBlankObject(releasedTaskNames)){
					serviceType =  "발생</br>종료";
					titleRunrunningTaskNames = occurredTaskNames + ", " + releasedTaskNames;
					if(occurredTaskNames.length() > 16)
						occurredTaskNames = occurredTaskNames.substring(0,16) + "...";
					if(releasedTaskNames.length() > 16)
						releasedTaskNames = releasedTaskNames.substring(0,16) + "...";
					runningTaskNames = occurredTaskNames + "</br>" + releasedTaskNames;
				}else if(!SmartUtil.isBlankObject(occurredTaskNames)){
					serviceType = "발생";
					runningTaskNames = occurredTaskNames;
					titleRunrunningTaskNames = runningTaskNames;
					if(runningTaskNames.length() > 16)
						runningTaskNames = runningTaskNames.substring(0,16) + "...";
				}else if(!SmartUtil.isBlankObject(releasedTaskNames)){
					serviceType = "종료";
					runningTaskNames = releasedTaskNames;
					titleRunrunningTaskNames = runningTaskNames;
					if(runningTaskNames.length() > 16)
						runningTaskNames = runningTaskNames.substring(0,16) + "...";
				}else{
					serviceType = "종료";
				}


				String target =  "situationDetail.sw?cid=" + instanceInfo.getContextId() + "&workId=" + instanceInfo.getWorkId();
				String statusImage = "";
				String statusTitle = "";
				switch (instanceInfo.getStatus()) {
				// 인스턴스가 현재 진행중인 경우..
				case Instance.STATUS_RUNNING:
					statusImage = "icon_status_running";
					statusTitle = "처리중";
					break;
				// 인스턴스가 지연진행중인 경우....
				case Instance.STATUS_DELAYED_RUNNING:
					statusImage = "icon_status_d_running";
					statusTitle = "지연처리중";
					break;
				// 인스턴스가 완료된 경우...
				case Instance.STATUS_COMPLETED:
					statusImage = "icon_status_completed";
					statusTitle = "완료";
					break;
				// 인스턴스가 완료된 경우...
				case Instance.STATUS_ABORTED:
					statusImage = "icon_status_aborted";
					statusTitle = "이상종료";
					runningTaskNames = runningTaskName;
					break;
				// 기타 잘못되어 상태가 없는 경우..
				default:
					statusImage = "icon_status_not_yet";
					statusTitle = "미진행";
				}
			%>
				<tr class="instance_list js_ucity_content" href="<%=target%>">
					<td class="tc vm">
						<a class="js_ucity_content" href="<%=target %>">					
							<span class="<%=statusImage%>" title="<%=statusTitle%>"></span>
						</a>
					</td>
					<td>
						<a class="js_ucity_content" href="<%=target %>"><%=serviceName%></a>
					</td>
					<td>
						<a class="js_ucity_content" href="<%=target %>"><%=eventName%></a>
					</td>
					<td>
						<%if(!SmartUtil.isBlankObject(serviceType)){ %>
	 						<a class="js_ucity_content" href="<%=target %>"><%=serviceType %></a>
	 					<%} %>
 					</td>
					<td>
						<%if(!SmartUtil.isBlankObject(runningTaskNames) || instanceInfo.getStatus() == Instance.STATUS_ABORTED ){ %>
	 						<a class="js_ucity_content" href="<%=target %>">
	 							<span title="<%=titleRunrunningTaskNames%>"><%=runningTaskNames %></span>
	 						</a>
	 					<%} %>
 					</td>
					<td>
						<a class="js_ucity_content" href="<%=target %>"><%=eventTime %></a>
					</td>
					<td>
						<a class="js_ucity_content" href="<%=target %>"><%=eventPlace%></a>
					</td>
					<td>	
						<a class="js_ucity_content" href="<%=target %>">
							<span title="<%=titleExternalDisplay%>"><%=externalDisplay%></span>
						</a>
					</td>
					<td>
						<a class="js_ucity_content" href="<%=target %>"><%=isSms%></a>
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
			<th>
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_STATUS%>">상태
			 		<span class="<%
					if(sortedField.getFieldId().equals(FormField.ID_STATUS)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
					%>"></span>
				</a>				
			</th>
			<th>
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FIELD_ID_SERVICE_NAME%>">U-서비스명
			 		<span class="<%
					if(sortedField.getFieldId().equals(FIELD_ID_SERVICE_NAME)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
					%>"></span>
				</a>
			</th>
			<th>				
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FIELD_ID_EVENT_NAME%>">이벤트명
			 		<span class="<%
					if(sortedField.getFieldId().equals(FIELD_ID_EVENT_NAME)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
					%>"></span>
				</a>
			</th>				
			<th>구분</th>
			<th>
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_LAST_TASK%>">진행단계
			 		<span class="<%
					if(sortedField.getFieldId().equals(FormField.ID_LAST_TASK)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
					%>"></span>
				</a>						
			</th>
			<th>
				<a href="" class="js_select_field_sorting" fieldId="<%=FIELD_ID_EVENT_TIME%>">발생일시
					<span class="<%if(sortedField.getFieldId().equals(FIELD_ID_EVENT_TIME)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} %>"></span>
				</a>
			</th>
			<th>
				<a href="" class="js_select_field_sorting" fieldId="<%=FIELD_ID_EVENT_PLACE%>">발생장소
					<span class="<%if(sortedField.getFieldId().equals(FIELD_ID_EVENT_PLACE)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} %>"></span>
				</a>
			</th>
			<th>
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FIELD_ID_EXTERNAL_DISPLAY%>">외부표출
			 		<span class="<%
					if(sortedField.getFieldId().equals(FIELD_ID_EXTERNAL_DISPLAY)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
					%>"></span>
				</a>						
			</th>			
			<th>
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FIELD_ID_IS_SMS%>">SMS발송
			 		<span class="<%
					if(sortedField.getFieldId().equals(FIELD_ID_IS_SMS)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
					%>"></span>
				</a>						
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
}else{
%>
<!-- 목록 테이블 //-->

<form name="frmInstanceListPaging">
	<!-- 페이징 -->
	<div class="paginate">
		<%
		if (currentPage > 0 && totalPages > 0 && currentPage <= totalPages) {
			boolean isFirst10Pages = (currentPage <= 10) ? true : false;
			boolean isLast10Pages = (((currentPage - 1)  / 10) == ((totalPages - 1) / 10)) ? true : false;
			int startPage = ((currentPage - 1) / 10) * 10 + 1;
			int endPage = isLast10Pages ? totalPages : startPage + 9;
			if (!isFirst10Pages) {
		%>
				<a class="pre_end js_select_paging" href="" title="<fmt:message key='common.title.first_page'/>">
					<span class="spr"></span>
					<input name="hdnPrevEnd" type="hidden" value="false"> 
				</a>		
				<a class="pre js_select_paging" href="" title="<fmt:message key='common.title.prev_10_pages'/> ">
					<span class="spr"></span>
					<input name="hdnPrev10" type="hidden" value="false">
				</a>
			<%
			}
			for (int num = startPage; num <= endPage; num++) {
				if (num == currentPage) {
			%>
					<strong><%=num%></strong>
					<input name="hdnCurrentPage" type="hidden" value="<%=num%>"/>
				<%
				} else {
				%>
					<a class="num js_select_current_page" href=""><%=num%></a>
				<%
				}
			}
			if (!isLast10Pages) {
			%>
				<a class="next js_select_paging" title="<fmt:message key='common.title.next_10_pages'/> ">
					<span class="spr"></span>
					<input name="hdnNext10" type="hidden" value="false"/>
				</a>
				<a class="next_end js_select_paging" title="<fmt:message key='common.title.last_page'/> ">
					<span class="spr"><input name="hdnNextEnd" type="hidden" value="false"/></span> 
				</a>
		<%
			}
		}
		%>
	</div>
	
<%-- 	<div class="num_box">
		<select class="js_select_page_size" name="selPageSize" title="<fmt:message key='common.title.count_in_page'/>">
			<option <%if (pageSize == 20) {%> selected <%}%>>20</option>
			<option <%if (pageSize == 30) {%> selected <%}%>>30</option>
			<option <%if (pageSize == 50) {%> selected <%}%>>50</option>
			<option <%if (pageSize == 100) {%> selected <%}%>>100</option>
		</select>
	</div> --%>
	<!-- 페이징 //-->
</form>

<%
}
if(!SmartUtil.isBlankObject(sortedField)){
%>
	<form name="frmSortingField">
		<input name="hdnSortingFieldId" type="hidden" value="<%=sortedField.getFieldId()%>">
		<input name="hdnSortingIsAscending" type="hidden" value="<%=sortedField.isAscending()%>">
	</form>
<%
}
%>
