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
<script type="text/javascript">
	popGetIntanceList = function(paramsJson){
		console.log(JSON.stringify(paramsJson));
		var url = "set_instance_list_params.sw";
		$.ajax({
			url : url,
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				var selectWorkItem = $('.js_select_work_item_page');
				selectWorkItem.find('#iwork_list_page').html(data);
			},
			error : function(e) {
				smartPop.showInfo(smartPop.ERROR, smartMessage.get('iworkListError'));
			}
		});
	};
	
	popSelectListParam = function(progressSpan, isGray){
		var selectWorkItem = $('.js_select_work_item_page');
		var forms = selectWorkItem.find('form:visible');
		var paramsJson = {};
		var workId = selectWorkItem.find('form[name="frmSortingField"]').attr('workId');
		paramsJson["href"] = "jsp/popup/pop_iwork_instance_list.jsp?workId=" + workId;
		for(var i=0; i<forms.length; i++){
			var form = $(forms[i]);
			paramsJson[form.attr('name')] = mergeObjects(form.serializeObject(), SmartWorks.GridLayout.serializeObject(form));
		}
		popGetIntanceList(paramsJson);		
	};
</script>
<%
	ISmartWorks smartWorks = (ISmartWorks)request.getAttribute("smartWorks");
	String workId = request.getParameter("workId");
	if(SmartUtil.isBlankObject(workId)){
		String formId = request.getParameter("formId");
		workId = smartWorks.getWorkIdByFormId(formId);
	}
	RequestParams params = (RequestParams)request.getAttribute("requestParams");
	if(SmartUtil.isBlankObject(params)){
		params = new RequestParams();
		params.setPageSize(5);
		params.setCurrentPage(1);		
	}
	User cUser = SmartUtil.getCurrentUser();
	InformationWork work = (InformationWork) smartWorks.getWorkById(workId);
	InstanceInfoList instanceList = smartWorks.getIWorkInstanceList(workId, params);
	
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<div class="list_contents form_contents js_pop_instance_list_page">

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
			<tr class="tit_bg js_instance_list_header">
				<%
				FormField[] fields = work.getDisplayFields();
				if (fields != null) {
					for (FormField field : fields) {
						if (field.getType().equalsIgnoreCase("fileField"))
							continue;
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
				<th class="r_line" style="min-width:140px">
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
					<tr class="instance_list js_pop_select_work_item" href="<%=target%>" workId="<%=workId%>" instId="<%=instanceInfo.getId()%>">
						<%
						currentCount--;
						if ((fieldDatas != null) && (fieldDatas.length == displayFields.length)) {
							NumberFormat nf = NumberFormat.getNumberInstance();
							int count = 0;
							for (FieldData data : fieldDatas) {
								if (data.getFieldType().equalsIgnoreCase("fileField"))
									continue;
						%>
								<td fieldId="<%=data.getFieldId()%>" <%if(data.getFieldType().equals(FormField.TYPE_CURRENCY) || 
									data.getFieldType().equals(FormField.TYPE_NUMBER) || 
									data.getFieldType().equals(FormField.TYPE_PERCENT)){ %>
											class="tr pr10"
										<%}else if(data.getFieldType().equals(FormField.TYPE_FILE)){%>
											class="tc"
										<%}%>><%if(data.getFieldType().equals(FormField.TYPE_FILE) && !SmartUtil.isBlankObject(data.getValue())){%>
										<%if(!SmartUtil.isBlankObject(data.getFiles())){%><img src="images/icon_file.gif" class="js_pop_files_detail" filesDetail="<%=data.getFilesHtml(work.getId(), null, instanceInfo.getId())%>"><%	} %>
										<%}else if(data.getFieldType().equals(FormField.TYPE_NUMBER)){%><%=data.getValue() != null ? CommonUtil.toNotNull(nf.format(Float.parseFloat(data.getValue()))) : CommonUtil.toNotNull(data.getValue())%>
										<%}else if(data.getFieldType().equals(FormField.TYPE_PERCENT)){%><%=data.getValue() != null ? CommonUtil.toNotNull(nf.format(Float.parseFloat(data.getValue()))) + "%" : CommonUtil.toNotNull(data.getValue())%>
										<%}else if(data.getFieldType().equals(FormField.TYPE_CURRENCY)){%><%=data.getSymbol()%><%=data.getValue() != null ? CommonUtil.toNotNull(nf.format(Float.parseFloat(data.getValue()))) : CommonUtil.toNotNull(data.getValue())%>
										<%}else if(data.getFieldType().equals(FormField.TYPE_IMAGE) ||
													data.getFieldType().equals(FormField.TYPE_RICHTEXT_EDITOR) ||
													data.getFieldType().equals(FormField.TYPE_DATA_GRID)){%>
										<%}else{%><%=CommonUtil.toNotNull(data.getValue())%><%} %>
										<%if(displayFields[count++].getId().equals(work.getKeyField())){%>
											<%if(instanceInfo.getSubInstanceCount()>0){ %><font class="t_sub_count">[<b><%=instanceInfo.getSubInstanceCount() %></b>]</font><%} %>
											<%if(instanceInfo.isNew()){ %><span class="icon_new"></span><%} %><%}%></td>
						<%
							}
						}
						%>
						<td>
							<div class="noti_pic js_content_work_space">
								<img src="<%=lastModifier.getMinPicture()%>" title="<%=lastModifier.getLongName()%>" class="profile_size_s" />
							</div>
							<div class="noti_in_s">
								<span class="t_name"><%=lastModifier.getLongName()%></span>
								<div class="t_date"><%=instanceInfo.getLastModifiedDate().toLocalString()%></div>
							</div>
						</td>
					</tr>
		<%
				}
			}
		}else if(!SmartUtil.isBlankObject(work)){
		%>
			<tr class="tit_bg">
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
		<form name="frmSortingField" class="js_pop_select_work_item" workId="<%=workId%>" >
			<input name="hdnSortingFieldId" type="hidden" value="<%=sortedField.getFieldId()%>">
			<input name="hdnSortingIsAscending" type="hidden" value="<%=sortedField.isAscending()%>">
		</form>
	<%
	}
	%>
	<!-- 목록 테이블 //-->

	<form name="frmInstanceListPaging"  class="js_pop_select_work_item">
		<!-- 페이징 -->
		<div class="paginate">
			<%
				if (currentPage > 0 && totalPages > 0 && currentPage <= totalPages) {
					boolean isFirst10Pages = (currentPage <= 10) ? true : false;
					boolean isLast10Pages = (((currentPage - 1)  / 10) == ((totalPages - 1) / 10)) ? true
							: false;
					int startPage = ((currentPage - 1) / 10) * 10 + 1;
					int endPage = isLast10Pages ? totalPages : startPage + 9;
					if (!isFirst10Pages) {
			%>
			<a class="pre_end js_select_paging" href="" title="<fmt:message key='common.title.first_page'/>">
				<span class="spr"></span><input name="hdnPrevEnd" type="hidden" value="false"> </a>		
			<a class="pre js_select_paging" href="" title="<fmt:message key='common.title.prev_10_pages'/> ">
				<span class="spr"></span><input name="hdnPrev10" type="hidden" value="false"></a>
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
				<span class="spr"></span><input name="hdnNext10" type="hidden" value="false"/></a>
			<a class="next_end js_select_paging" title="<fmt:message key='common.title.last_page'/> ">
			<span class="spr"><input name="hdnNextEnd" type="hidden" value="false"/></span> </a>
			<%
				}
				}
			%>
			<span class="js_progress_span"></span>
		</div>
		<input type="hidden"  name="selPageSize" value="10" >
	</form>
</div>
