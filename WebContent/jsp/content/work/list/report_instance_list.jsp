<%@page import="net.smartworks.model.work.ReportWork"%>
<%@page import="net.smartworks.model.report.ChartReport"%>
<%@page import="net.smartworks.model.instance.info.ReportInstanceInfo"%>
<%@page import="net.smartworks.model.report.Report"%>
<%@page import="net.smartworks.util.SmartTest"%>
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
	
	String targetWorkId = request.getParameter("targetWorkId");
	String strTargetWorkType = request.getParameter("targetWorkType");
	String producedBy = request.getParameter("producedBy");
	ReportWork work = (ReportWork)session.getAttribute("smartWork");
	String workId = work.getId();
	if(SmartUtil.isBlankObject(params)){
		String savedTargetWorkId = (String)session.getAttribute("targetWorkId");
		params = (RequestParams)session.getAttribute("requestParams");
		if(!targetWorkId.equals(savedTargetWorkId) || SmartUtil.isBlankObject(params)){
			params = new RequestParams();
			params.setPageSize(20);
			params.setCurrentPage(1);
		}
	}
	session.setAttribute("requestParams", params);
	session.setAttribute("targetWorkId", targetWorkId);
	session.setAttribute("producedBy", producedBy);

	InstanceInfoList instanceList = smartWorks.getReportInstanceList(targetWorkId, SmartUtil.isBlankObject(strTargetWorkType) ? -1 : Integer.parseInt(strTargetWorkType), producedBy, params);
	
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
	 		<th class="r_line" style="width:40px;">
				<span><fmt:message key="common.title.number"/></span>
			</th>
			<th class="r_line">
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_SUBJECT%>"><fmt:message key='common.title.instance_subject'/>
			 		<span class="<%
					if(sortedField.getFieldId().equals(FormField.ID_SUBJECT)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
					%>"></span>
				</a>				
				<span class="js_progress_span"></span>
			</th>
			<%if(SmartWork.ID_ALL_WORKS.equals(targetWorkId)) {%>
				<th class="r_line">
		 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_TARGET_WORK_TYPE%>"><fmt:message key="report.title.target_work_type"/>
				 		<span class="<%
						if(sortedField.getFieldId().equals(FormField.ID_TARGET_WORK_TYPE)){
							if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
						%>"></span>
					</a>				
					<span class="js_progress_span"></span>
				</th>
			<%}%>
			<th class="r_line">
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_REPORT_TYPE%>"><fmt:message key="report.title.report_type"/>
			 		<span class="<%
					if(sortedField.getFieldId().equals(FormField.ID_REPORT_TYPE)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
					%>"></span>
				</a>				
				<span class="js_progress_span"></span>
			</th>
			<th class="r_line">
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_CHART_TYPE%>"><fmt:message key="report.chart.type"/>
			 		<span class="<%
					if(sortedField.getFieldId().equals(FormField.ID_CHART_TYPE)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
					%>"></span>
				</a>				
				<span class="js_progress_span"></span>
			</th>
			<th class="r_line">
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_ACCESS_LEVEL%>"><fmt:message key="group.title.type"/>
			 		<span class="<%
					if(sortedField.getFieldId().equals(FormField.ID_ACCESS_LEVEL)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
					%>"></span>
				</a>				
				<span class="js_progress_span"></span>
			</th>
			<%if(Report.PRODUCED_BY_USER.equals(producedBy)){ %>
				<th>
					<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_LAST_MODIFIER %>">
						<fmt:message key='common.title.last_modifier' />
						<span class="<%if(sortedField.getFieldId().equals(FormField.ID_LAST_MODIFIER)){
							if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} %>"></span>
					</a>/
					<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_LAST_MODIFIED_DATE%>">
						<fmt:message key='common.title.last_modified_date' />
						<span class="<%if(sortedField.getFieldId().equals(FormField.ID_LAST_MODIFIED_DATE)){
							if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} %>"></span>
					</a>
					<span class="js_progress_span"></span>
				</th>
			<%} %>
		</tr>	
		<%	
		pageSize = instanceList.getPageSize();
		totalPages = instanceList.getTotalPages();
		currentPage = instanceList.getCurrentPage();
		int currentCount = instanceList.getTotalSize()-(currentPage-1)*pageSize;
		if(instanceList.getInstanceDatas() != null) {
			ReportInstanceInfo[] instanceInfos = (ReportInstanceInfo[])instanceList.getInstanceDatas();
			for (ReportInstanceInfo instanceInfo : instanceInfos) {
				UserInfo owner = instanceInfo.getOwner();
				UserInfo lastModifier = instanceInfo.getLastModifier();
				String target = instanceInfo.getController() + "?cid=" + instanceInfo.getContextId();
			%>
				<tr class="instance_list js_select_work_report" href="work_report_edit.sw" reportId="<%=instanceInfo.getId()%>">
					<td class="tc vm"><%=currentCount%></td>
					<td>
						<a href="">					
							<%=instanceInfo.getSubject()%>
							<%if(instanceInfo.isNew() && Report.PRODUCED_BY_USER.equals(producedBy)){ %><span class="icon_new"></span><%} %>
						</a>
					</td>
					<%if(SmartWork.ID_ALL_WORKS.equals(targetWorkId)) {
						String targetWorkTypeKey = "report.title.all_works";
						switch(instanceInfo.getTargetWorkType()){
						case SmartWork.TYPE_INFORMATION:
							targetWorkTypeKey = "report.title.information_works";
							break;
						case SmartWork.TYPE_PROCESS:
							targetWorkTypeKey = "report.title.process_works";
							break;
						case SmartWork.TYPE_SCHEDULE:
							targetWorkTypeKey = "report.title.schedule_works";	
							break;
						}
					%>
						<td>
							<a href="">					
								<fmt:message key="<%=targetWorkTypeKey %>"/>
							</a>
						</td>
					<%
					}
					String reportTypeKey = "";
					switch(instanceInfo.getReportType()){
					case Report.TYPE_CHART:
						reportTypeKey = "report.type.chart";
						break;
					case Report.TYPE_MATRIX:
						reportTypeKey = "report.type.matrix";
						break;
					case Report.TYPE_TABLE:
						reportTypeKey = "report.type.table";
						break;
					}
					
					String chartTypeKey = "";
					if(instanceInfo.getChartType()>0 && instanceInfo.getChartType()<ChartReport.CHART_TYPES_STRING.length){
						chartTypeKey = "report.chart.type." + ChartReport.CHART_TYPES_STRING[instanceInfo.getChartType()];
					}
					
					String accessLevelKey = "";
					if(!SmartUtil.isBlankObject(instanceInfo.getAccessPolicy())){
						if(instanceInfo.getAccessPolicy().getLevel() == AccessPolicy.LEVEL_PUBLIC){
							accessLevelKey = "common.security.access.public";
						}else if(instanceInfo.getAccessPolicy().getLevel() == AccessPolicy.LEVEL_PRIVATE){
							accessLevelKey = "common.security.access.private";
						}
					}
					%>
					<td>
						<%if(!SmartUtil.isBlankObject(reportTypeKey)){ %>
							<a href="">					
								<fmt:message key="<%=reportTypeKey%>"/>
							</a>
						<%} %>
					</td>
					<td>
						<%if(!SmartUtil.isBlankObject(chartTypeKey) && instanceInfo.getReportType()==Report.TYPE_CHART){ %>
							<a href="">					
								<fmt:message key="<%=chartTypeKey %>"/>
							</a>
						<%} %>
					</td>
					<td>
						<%if(!SmartUtil.isBlankObject(reportTypeKey)){ %>
							<a href="">					
								<fmt:message key="<%=accessLevelKey%>"/>
							</a>
						<%} %>
					</td>
					
					<%if(Report.PRODUCED_BY_USER.equals(producedBy)){ %>
						<td>
							<%
							if(!SmartUtil.isBlankObject(lastModifier)){
							%>
								<a href="">					
									<div class="noti_pic">
										<img src="<%=lastModifier.getMinPicture()%>" title="<%=lastModifier.getLongName()%>" class="profile_size_s" />
									</div>
									<div class="noti_in_s">
										<span class="t_name"><%=lastModifier.getLongName()%></span>
										<div class="t_date"><%=instanceInfo.getLastModifiedDate().toLocalString()%></div>
									</div>
								</a>
							<%
							}
							%>
						</td>
					<%} %>
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
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_SUBJECT%>"><fmt:message key='common.title.instance_subject'/>
			 		<span class="<%
					if(sortedField.getFieldId().equals(FormField.ID_SUBJECT)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
					%>"></span>
				</a>				
				<span class="js_progress_span"></span>
			</th>
			<%if(SmartWork.ID_ALL_WORKS.equals(targetWorkId)) {%>
				<th class="r_line">
		 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_TARGET_WORK_TYPE%>"><fmt:message key="report.title.target_work_type"/>
				 		<span class="<%
						if(sortedField.getFieldId().equals(FormField.ID_TARGET_WORK_TYPE)){
							if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
						%>"></span>
					</a>				
					<span class="js_progress_span"></span>
				</th>
			<%}%>
			<th class="r_line">
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_REPORT_TYPE%>"><fmt:message key="report.title.report_type"/>
			 		<span class="<%
					if(sortedField.getFieldId().equals(FormField.ID_REPORT_TYPE)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
					%>"></span>
				</a>				
				<span class="js_progress_span"></span>
			</th>
			<th class="r_line">
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_CHART_TYPE%>"><fmt:message key="report.chart.type"/>
			 		<span class="<%
					if(sortedField.getFieldId().equals(FormField.ID_CHART_TYPE)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
					%>"></span>
				</a>				
				<span class="js_progress_span"></span>
			</th>
			<th class="r_line">
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_ACCESS_LEVEL%>"><fmt:message key="group.title.type"/>
			 		<span class="<%
					if(sortedField.getFieldId().equals(FormField.ID_ACCESS_LEVEL)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
					%>"></span>
				</a>				
				<span class="js_progress_span"></span>
			</th>
			<%if(Report.PRODUCED_BY_USER.equals(producedBy)){ %>
				<th>
					<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_LAST_MODIFIER %>">
						<fmt:message key='common.title.last_modifier' />
						<span class="<%if(sortedField.getFieldId().equals(FormField.ID_LAST_MODIFIER)){
							if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} %>"></span>
					</a>/
					<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_LAST_MODIFIED_DATE%>">
						<fmt:message key='common.title.last_modified_date' />
						<span class="<%if(sortedField.getFieldId().equals(FormField.ID_LAST_MODIFIED_DATE)){
							if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} %>"></span>
					</a>
					<span class="js_progress_span"></span>
				</th>
			<%} %>
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
		<select <%if(Report.PRODUCED_BY_SMARTWORKS.equals(producedBy)){ %>disabled="disabled"<%} %> class="js_select_page_size" name="selPageSize" title="<fmt:message key='common.title.count_in_page'/>">
			<option <%if (pageSize == 20) {%> selected <%}%>>20</option>
			<option <%if (pageSize == 30) {%> selected <%}%>>30</option>
			<option <%if (pageSize == 50) {%> selected <%}%>>50</option>
			<option <%if (pageSize == 100) {%> selected <%}%>>100</option>
		</select>
	</div>
	<!-- 페이징 //-->
</form>
