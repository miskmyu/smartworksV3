<%@page import="net.smartworks.model.work.AppWork"%>
<%@page import="net.smartworks.model.work.info.AppWorkInfo"%>
<%@page import="net.smartworks.model.work.info.WorkInfoList"%>
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
				var selectWorkItem = $('.js_download_from_appstore_page');
				selectWorkItem.find('#app_work_list_page').html(data);
			},
			error : function(e) {
				smartPop.showInfo(smartPop.ERROR, smartMessage.get('iworkListError'));
			}
		});
	};
	
	popSelectListParam = function(progressSpan, isGray){
		var selectWorkItem = $('.js_download_from_appstore_page');
		var forms = selectWorkItem.find('form:visible');
		var paramsJson = {};
		paramsJson["href"] = "jsp/popup/pop_app_work_list.jsp";
		for(var i=0; i<forms.length; i++){
			var form = $(forms[i]);
			paramsJson[form.attr('name')] = mergeObjects(form.serializeObject(), SmartWorks.GridLayout.serializeObject(form));
		}
		popGetIntanceList(paramsJson);		
	};
</script>
<%
	ISmartWorks smartWorks = (ISmartWorks)request.getAttribute("smartWorks");
	RequestParams params = (RequestParams)request.getAttribute("requestParams");
	if(SmartUtil.isBlankObject(params)){
		params = new RequestParams();
		params.setPageSize(10);
		params.setCurrentPage(1);		
	}
	User cUser = SmartUtil.getCurrentUser();
	WorkInfoList workList = smartWorks.getAppWorkList(params);
	
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<div class="list_contents form_contents js_pop_app_work_list_page js_pop_instance_list_page">

	<!-- 목록 테이블 -->
	<table>
		<%
		SortingField sortedField = null;
		int pageSize = 20, totalPages = 1, currentPage = 1;
		if (workList != null) {
			sortedField = workList.getSortedField();
			if(sortedField==null) sortedField = new SortingField();
		%>
				<tr class="tit_bg">
 			 		<th class="r_line"><fmt:message key="common.title.action"/></th>
			 		<th class="r_line">
			 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_APP_WORK_NAME%>"><fmt:message key="builder.title.work_name"/>
					 		<span class="<%if(sortedField.getFieldId().equals(FormField.ID_APP_WORK_NAME)){
								if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}}%>"></span>
						</a>
						<span class="js_progress_span"></span>
					</th>
			 		<th class="r_line">
			 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_APP_WORK_TYPE%>"><fmt:message key="builder.title.work_type"/>
					 		<span class="<%if(sortedField.getFieldId().equals(FormField.ID_APP_WORK_TYPE)){
								if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}}%>"></span>
						</a>
						<span class="js_progress_span"></span>
					</th>
			 		<th class="r_line">
			 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_APP_WORK_INDUSTRY%>"><fmt:message key="builder.title.work_industry"/>
					 		<span class="<%if(sortedField.getFieldId().equals(FormField.ID_APP_WORK_INDUSTRY)){
								if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}}%>"></span>
						</a>
						<span class="js_progress_span"></span>
					</th>
			 		<th class="r_line">
			 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_APP_WORK_JOB%>"><fmt:message key="builder.title.work_job"/>
					 		<span class="<%if(sortedField.getFieldId().equals(FormField.ID_APP_WORK_JOB)){
								if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}}%>"></span>
						</a>
						<span class="js_progress_span"></span>
					</th>
					
					<th class="r_line" style="min-width:140px">
						<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_APP_PUBLISHED_COMPANY %>">
							<fmt:message key="common.title.published_company"/><span class="<%if(sortedField.getFieldId().equals(FormField.ID_APP_PUBLISHED_COMPANY)){
								if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} %>"></span>
						</a>/
						<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_APP_PUBLISHED_DATE%>">
							<fmt:message key="common.title.published_date"/><span class="<%if(sortedField.getFieldId().equals(FormField.ID_APP_PUBLISHED_DATE)){
								if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} %>"></span>
						</a>
						<span class="js_progress_span"></span>
					</th>		
				</tr>	
			<%
			pageSize = workList.getPageSize();
			totalPages = workList.getTotalPages();
			currentPage = workList.getCurrentPage();
			int currentCount = workList.getTotalSize()-(currentPage-1)*pageSize;
			if(workList.getWorkDatas() != null) {
				AppWorkInfo[] workInfos = (AppWorkInfo[]) workList.getWorkDatas();
				for (AppWorkInfo workInfo : workInfos) {
					currentCount--;
			%>
					<tr class="app_work_list" workId="<%=workInfo.getId()%>" workName="<%=workInfo.getName()%>">
						<td fieldId="" class="tc"><div class="buttonSet"><button class="js_download_app_work_btn"><fmt:message key="common.button.download"/></button></div></td>
						<td fieldId="" class="tc"><span class="<%=workInfo.getIconClass()%>"><%=workInfo.getName() %><%if(workInfo.isNew()){ %><span class="icon_new"></span><%}%></span></td>
						<td fieldId="" class="tc"><%=SmartWork.getWorkTypeName(workInfo.getWorkType()) %></td>
						<td fieldId="" class="tc"><%=AppWork.getCategoryNameByIndustry(workInfo.getCategoryIdByIndustry()) %></td>
						<td fieldId="" class="tc"><%=AppWork.getCategoryNameByJob(workInfo.getCategoryIdByJob()) %></td>
						
						<td>
							<div class="noti_in_s">
								<span class="t_name"><%=workInfo.getPublishedCompany()%></span>
								<div class="t_date"><%=workInfo.getPublishedDate().toLocalString()%></div>
							</div>
						</td>
					</tr>
		<%
				}
			}
		}else{
		%>
			<tr class="tit_bg">
				<%
				sortedField = new SortingField();
				%>
			 		<th class="r_line"><fmt:message key="common.title.action"/></th>
		 		<th class="r_line">
		 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_APP_WORK_NAME%>"><fmt:message key="builder.title.work_name"/>
				 		<span class="<%if(sortedField.getFieldId().equals(FormField.ID_APP_WORK_NAME)){
							if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}}%>"></span>
					</a>
					<span class="js_progress_span"></span>
				</th>
		 		<th class="r_line">
		 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_APP_WORK_TYPE%>"><fmt:message key="builder.title.work_type"/>
				 		<span class="<%if(sortedField.getFieldId().equals(FormField.ID_APP_WORK_TYPE)){
							if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}}%>"></span>
					</a>
					<span class="js_progress_span"></span>
				</th>
		 		<th class="r_line">
		 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_APP_WORK_INDUSTRY%>"><fmt:message key="builder.title.work_industry"/>
				 		<span class="<%if(sortedField.getFieldId().equals(FormField.ID_APP_WORK_INDUSTRY)){
							if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}}%>"></span>
					</a>
					<span class="js_progress_span"></span>
				</th>
		 		<th class="r_line">
		 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_APP_WORK_JOB%>"><fmt:message key="builder.title.work_job"/>
				 		<span class="<%if(sortedField.getFieldId().equals(FormField.ID_APP_WORK_JOB)){
							if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}}%>"></span>
					</a>
					<span class="js_progress_span"></span>
				</th>
				
				<th class="r_line" style="min-width:140px">
					<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_APP_PUBLISHED_COMPANY %>">
						<fmt:message key="common.title.published_company"/><span class="<%if(sortedField.getFieldId().equals(FormField.ID_APP_PUBLISHED_COMPANY)){
							if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} %>"></span>
					</a>/
					<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_APP_PUBLISHED_DATE%>">
						<fmt:message key="common.title.published_date"/><span class="<%if(sortedField.getFieldId().equals(FormField.ID_APP_PUBLISHED_DATE)){
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
	if(workList == null || SmartUtil.isBlankObject(workList.getWorkDatas())){
	%>
		<div class="tc"><fmt:message key="common.message.no_instance"/></div>
	<%
	}
	if(!SmartUtil.isBlankObject(sortedField)){
	%>
		<form name="frmSortingField" class="js_pop_select_appwork_item">
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
