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
	String FIELD_ID_IS_SMS = "isSms";

	ISmartWorks smartWorks = (ISmartWorks)request.getAttribute("smartWorks");
	RequestParams params = (RequestParams)request.getAttribute("requestParams");
	User cUser = SmartUtil.getCurrentUser();
	ProcessWork work = (ProcessWork)session.getAttribute("smartWork");

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

	InstanceInfoList instanceList = smartWorks.getAllUcityPWorkInstanceList(false, params);
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
				<span class="js_progress_span"></span>
			</th>
			<th>
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FIELD_ID_SERVICE_NAME%>">U-서비스명
			 		<span class="<%
					if(sortedField.getFieldId().equals(FIELD_ID_SERVICE_NAME)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
					%>"></span>
				</a>/				
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FIELD_ID_EVENT_NAME%>">이벤트명
			 		<span class="<%
					if(sortedField.getFieldId().equals(FIELD_ID_EVENT_NAME)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
					%>"></span>
				</a>
				<span class="js_progress_span"></span>
			</th>				
			<th>
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FIELD_ID_TYPE%>">구분
			 		<span class="<%
					if(sortedField.getFieldId().equals(FIELD_ID_TYPE)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
					%>"></span>
				</a>				
				<span class="js_progress_span"></span>
			</th>
			<th>
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_LAST_TASK%>">진행단계
			 		<span class="<%
					if(sortedField.getFieldId().equals(FormField.ID_LAST_TASK)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
					%>"></span>
				</a>						
				<span class="js_progress_span"></span>
			</th>
			<th>
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FIELD_ID_EXTERNAL_DISPLAY%>">외부표출
			 		<span class="<%
					if(sortedField.getFieldId().equals(FIELD_ID_EXTERNAL_DISPLAY)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
					%>"></span>
				</a>						
				<span class="js_progress_span"></span>
			</th>
			<th>
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FIELD_ID_IS_SMS%>">SMS발송
			 		<span class="<%
					if(sortedField.getFieldId().equals(FIELD_ID_IS_SMS)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
					%>"></span>
				</a>						
				<span class="js_progress_span"></span>
			</th>
			<th>
				<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_CREATED_DATE%>">발생일시
					<span class="<%if(sortedField.getFieldId().equals(FormField.ID_CREATED_DATE)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} %>"></span>
				</a>
				<span class="js_progress_span"></span>
			</th>
			<th>
				<a href="" class="js_select_field_sorting" fieldId="<%=FIELD_ID_EVENT_PLACE%>">발생장소
					<span class="<%if(sortedField.getFieldId().equals(FIELD_ID_EVENT_PLACE)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} %>"></span>
				</a>
				<span class="js_progress_span"></span>
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
				String serviceType = "";
				String externalDisplay = "";
				String eventPlace = "";
				boolean isSms = false;
				if(!SmartUtil.isBlankObject(extendedProperties)){
					for(int i=0; i<extendedProperties.length; i++){
						Property extendedProperty = extendedProperties[i];
						if(extendedProperty.getName().equals(FIELD_ID_SERVICE_NAME)){
							serviceName = CommonUtil.toNotNull(extendedProperty.getValue());
						}else if(extendedProperty.getName().equals(FIELD_ID_EVENT_NAME)){
							eventName = CommonUtil.toNotNull(extendedProperty.getValue());							
						}else if(extendedProperty.getName().equals(FIELD_ID_TYPE)){
							serviceType = CommonUtil.toNotNull(extendedProperty.getValue());							
						}else if(extendedProperty.getName().equals(FIELD_ID_EXTERNAL_DISPLAY)){
							externalDisplay = CommonUtil.toNotNull(extendedProperty.getValue());							
						}else if(extendedProperty.getName().equals(FIELD_ID_EVENT_PLACE)){
							eventPlace = CommonUtil.toNotNull(extendedProperty.getValue());							
						}else if(extendedProperty.getName().equals(FIELD_ID_IS_SMS)){
							isSms = "Y".equals(extendedProperty.getValue());			
						}
					}
				}
				
				UserInfo owner = instanceInfo.getOwner();
				UserInfo lastModifier = instanceInfo.getLastModifier();
				TaskInstanceInfo lastTask = instanceInfo.getLastTask();
				String target =  "situationDetail.sw?cid=" + instanceInfo.getContextId() + "&workId=" + instanceInfo.getWork().getId();
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
				<tr class="instance_list js_ucity_content" href="<%=target%>">
					<td class="tc vm">
						<a class="js_ucity_content" href="<%=target %>">					
							<span class="<%=statusImage%>" title="<fmt:message key='<%=statusTitle%>'/>"></span>
						</a>
					</td>
					<td>
						<a class="js_ucity_content" href="<%=target %>"><%=serviceName%></a>
					</td>
					<td>
						<a class="js_ucity_content" href="<%=target %>"><%=eventName%></a>
					</td>
					<td>
						<a class="js_ucity_content" href="<%=target %>"><%=serviceType%></a>
					</td>
					<td>
						<a class="js_ucity_content" href="<%=target %>"><%=lastTask.getName()%></a></td>
					<td>
						<a class="js_ucity_content" href="<%=target %>">					
							<div class="noti_in_s">
								<div class="t_date"><%=instanceInfo.getCreatedDate().toLocalString()%></div>
							</div>
						</a>
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
				<span class="js_progress_span"></span>
			</th>
			<th>
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FIELD_ID_SERVICE_NAME%>">U-서비스명
			 		<span class="<%
					if(sortedField.getFieldId().equals(FIELD_ID_SERVICE_NAME)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
					%>"></span>
				</a>/				
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FIELD_ID_EVENT_NAME%>">이벤트명
			 		<span class="<%
					if(sortedField.getFieldId().equals(FIELD_ID_EVENT_NAME)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
					%>"></span>
				</a>
				<span class="js_progress_span"></span>
			</th>				
			<th>
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FIELD_ID_TYPE%>">구분
			 		<span class="<%
					if(sortedField.getFieldId().equals(FIELD_ID_TYPE)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
					%>"></span>
				</a>				
				<span class="js_progress_span"></span>
			</th>
			<th>
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_LAST_TASK%>">진행단계
			 		<span class="<%
					if(sortedField.getFieldId().equals(FormField.ID_LAST_TASK)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
					%>"></span>
				</a>						
				<span class="js_progress_span"></span>
			</th>
			<th>
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FIELD_ID_EXTERNAL_DISPLAY%>">외부표출
			 		<span class="<%
					if(sortedField.getFieldId().equals(FIELD_ID_EXTERNAL_DISPLAY)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
					%>"></span>
				</a>						
				<span class="js_progress_span"></span>
			</th>
			<th>
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FIELD_ID_IS_SMS%>">SMS발송
			 		<span class="<%
					if(sortedField.getFieldId().equals(FIELD_ID_IS_SMS)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
					%>"></span>
				</a>						
				<span class="js_progress_span"></span>
			</th>
			<th>
				<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_CREATED_DATE%>">발생일시
					<span class="<%if(sortedField.getFieldId().equals(FormField.ID_CREATED_DATE)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} %>"></span>
				</a>
				<span class="js_progress_span"></span>
			</th>
			<th>
				<a href="" class="js_select_field_sorting" fieldId="<%=FIELD_ID_EVENT_PLACE%>">발생장소
					<span class="<%if(sortedField.getFieldId().equals(FIELD_ID_EVENT_PLACE)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} %>"></span>
				</a>
				<span class="js_progress_span"></span>
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
if(!SmartUtil.isBlankObject(sortedField)){
%>
	<form name="frmSortingField">
		<input name="hdnSortingFieldId" type="hidden" value="<%=sortedField.getFieldId()%>">
		<input name="hdnSortingIsAscending" type="hidden" value="<%=sortedField.isAscending()%>">
	</form>
<%
}
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
		<span class="js_progress_span"></span>
	</div>
	
	<div class="num_box">
		<span class="js_progress_span"></span>
		<select class="js_select_page_size" name="selPageSize" title="<fmt:message key='common.title.count_in_page'/>">
			<option <%if (pageSize == 20) {%> selected <%}%>>20</option>
			<option <%if (pageSize == 30) {%> selected <%}%>>30</option>
			<option <%if (pageSize == 50) {%> selected <%}%>>50</option>
			<option <%if (pageSize == 100) {%> selected <%}%>>100</option>
		</select>
	</div>
	<!-- 페이징 //-->
</form>
