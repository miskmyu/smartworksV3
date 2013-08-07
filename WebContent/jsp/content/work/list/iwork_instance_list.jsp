<%@page import="net.smartworks.model.instance.info.TaskInstanceInfo"%>
<%@page import="net.smartworks.model.instance.Instance"%>
<%@page import="net.smartworks.util.SmartMessage"%>
<%@page import="java.util.Map"%>
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
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!-- 목록 테이블 -->
<table>
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
			 			<a href="" class="js_select_field_sorting" fieldId="<%=field.getId()%>"><%=field.getName()%>
					 		<span class="<%
							if(sortedField.getFieldId().equals(field.getId())){
								if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}}%>"></span>
						</a>
						<span class="js_progress_span"></span>
					</th>
			<%
				}
			}
			%>
			<th class="r_line">
				<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_LAST_MODIFIER %>">
					<fmt:message key='common.title.last_modifier' /><span class="<%if(sortedField.getFieldId().equals(FormField.ID_LAST_MODIFIER)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} %>"></span>
				</a>/
				<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_LAST_MODIFIED_DATE%>">
					<fmt:message key='common.title.last_modified_date' /><span class="<%if(sortedField.getFieldId().equals(FormField.ID_LAST_MODIFIED_DATE)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} %>"></span>
				</a>
				<span class="js_progress_span"></span>
			</th>		
	 		<th style="width:60px;">
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_INSTANCE_VIEWS%>">
	 				<fmt:message key="common.title.views"/>
			 		<span class="<%if(sortedField.getFieldId().equals(FormField.ID_INSTANCE_VIEWS)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}}%>"></span>
				</a>
				<span class="js_progress_span"></span>
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
				String target = instanceInfo.getController() + "?cid=" + instanceInfo.getContextId() + "&workId=" + workId;
			%>
				<tr class="instance_list js_content_work_space" href="<%=target%>">
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
									<%
									if(displayFields[count].getId().equals(work.getKeyField().getId()) && instanceInfo.isApproval()){
									%>
										<span class="icon_approval_w" title="<fmt:message key='common.button.approval'/>"></span>
									<%
									}
									%>									
									<a class="js_content_work_space" href="<%=target %>" <%if(data.getFieldType().equals(FormField.TYPE_DEPARTMENT)){%>title="<%=CommonUtil.toNotNull(data.getValue())%>"<%}%>>									
										<%if(data.getFieldType().equals(FormField.TYPE_FILE) && !SmartUtil.isBlankObject(data.getValue())){%>
										<%	if(!SmartUtil.isBlankObject(data.getFiles())){%>
												<img src="images/icon_file.gif" class="js_pop_files_detail" filesDetail="<%=data.getFilesHtml(work.getId(), null, instanceInfo.getId())%>">
										<%	} %>
										<%}else if(data.getFieldType().equals(FormField.TYPE_NUMBER)){%><%=data.getValue() != null ? CommonUtil.toNotNull(nf.format(Float.parseFloat(data.getValue()))) : CommonUtil.toNotNull(data.getValue())%>
										<%}else if(data.getFieldType().equals(FormField.TYPE_PERCENT)){%><%=data.getValue() != null ? CommonUtil.toNotNull(nf.format(Float.parseFloat(data.getValue()))) + "%" : CommonUtil.toNotNull(data.getValue())%>
										<%}else if(data.getFieldType().equals(FormField.TYPE_CURRENCY)){%><%=data.getSymbol()%><%=data.getValue() != null ? CommonUtil.toNotNull(nf.format(Float.parseFloat(data.getValue()))) : CommonUtil.toNotNull(data.getValue())%>
										<%}else if(data.getFieldType().equals(FormField.TYPE_IMAGE) ||
													data.getFieldType().equals(FormField.TYPE_RICHTEXT_EDITOR) ||
													data.getFieldType().equals(FormField.TYPE_DATA_GRID)){%>
										<%}else if(data.getFieldType().equals(FormField.TYPE_CHECK_BOX)){%><%=(data.getValue() != null && data.getValue().equals("true")) ? SmartMessage.getString("common.title.boolean.true") : SmartMessage.getString("common.title.boolean.false")%>
										<%}else if(data.getFieldType().equals(FormField.TYPE_DEPARTMENT)){%><%=CommonUtil.toNotNull(SmartUtil.getDepartNameFromFullpath(data.getValue())) %>
										<%}else{%><%=CommonUtil.toNotNull(data.getValue())%><%} %>
										<%
										if(displayFields[count++].getId().equals(work.getKeyField().getId())){
										%>
											<%if(instanceInfo.getSubInstanceCount()>0){ %><font class="t_sub_count">[<b><%=instanceInfo.getSubInstanceCount() %></b>]</font><%} %>
											<%if(instanceInfo.isNew()){ %><span class="icon_new"></span><%} %>
										<%
										}
										%>
									</a>
							</td>
					<%
						}
					}
					%>
					<td>
						<a class="js_content_work_space" href="<%=target %>">
							<div class="noti_pic">
								<img src="<%=lastModifier.getMinPicture()%>" title="<%=lastModifier.getLongName()%>" class="profile_size_s" />
							</div>
							<div class="noti_in_s">
								<span class="t_name"><%=lastModifier.getLongName()%></span>
								<div class="t_date"><%=instanceInfo.getLastModifiedDate().toLocalString()%></div>
							</div>
						</a>
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
			 			<a href="" class="js_select_field_sorting" fieldId="<%=field.getId()%>"><%=field.getName()%>
					 		<span class="<%
							if(sortedField.getFieldId().equals(field.getId())){
								if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
							%>"></span>
						</a>
						<span class="js_progress_span"></span>
					</th>
			<%
				}
			}
			%>
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
	 		<th class="r_line" style="width:60px;">
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_INSTANCE_VIEWS%>">
	 				<fmt:message key="common.title.views"/>
			 		<span class="<%if(sortedField.getFieldId().equals(FormField.ID_INSTANCE_VIEWS)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}}%>"></span>
				</a>
				<span class="js_progress_span"></span>
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
