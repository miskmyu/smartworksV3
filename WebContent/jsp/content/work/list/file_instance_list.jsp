<%@page import="net.smartworks.model.work.info.WorkInfo"%>
<%@page import="net.smartworks.model.community.info.WorkSpaceInfo"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="net.smartworks.model.work.info.SmartWorkInfo"%>
<%@page import="net.smartworks.model.work.FileCategory"%>
<%@page import="net.smartworks.model.instance.info.FileInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.WorkInstanceInfo"%>
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
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	RequestParams params = (RequestParams)request.getAttribute("requestParams");
	int displayType = Integer.parseInt(request.getParameter("displayType"));
	if(SmartUtil.isBlankObject(params)){
		String savedWorkId = (String)session.getAttribute("workId");
		params = (RequestParams)session.getAttribute("requestParams");
		if(!SmartWork.ID_FILE_MANAGEMENT.equals(savedWorkId) || SmartUtil.isBlankObject(params)){
			params = new RequestParams();
			params.setPageSize(20);
			params.setCurrentPage(1);
			SearchFilter searchFilter = null;
			switch(displayType){
			case FileCategory.DISPLAY_BY_CATEGORY:
				searchFilter = SearchFilter.getByFileCategoryIdFilter(FileCategory.ID_ALL_FILES);
				break;
			case FileCategory.DISPLAY_BY_WORK:
				searchFilter = SearchFilter.getByWorkIdFilter(FileCategory.ID_ALL_FILES);
				break;
			case FileCategory.DISPLAY_BY_YEAR:
				searchFilter = SearchFilter.getByCreatedDateFilter(FileCategory.ID_ALL_FILES);
				break;
			case FileCategory.DISPLAY_BY_OWNER:
				searchFilter = SearchFilter.getByOwnerFilter(FileCategory.ID_ALL_FILES);
				break;
			case FileCategory.DISPLAY_BY_FILE_TYPE:
				searchFilter = SearchFilter.getByFileTypeFilter(FileCategory.ID_ALL_FILES);
				break;
			}
			params.setSearchFilter(searchFilter);
		}
	}
	session.setAttribute("requestParams", params);
	session.setAttribute("workId", SmartWork.ID_FILE_MANAGEMENT);

	User cUser = SmartUtil.getCurrentUser();
	String cid = (String)session.getAttribute("cid");
	String wid = (String)session.getAttribute("wid");
	InstanceInfoList instanceList = smartWorks.getFileInstanceList(wid, params);
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!-- 목록페이지 -->
<table class="js_file_instance_list_page">
	<%
	SortingField sortedField = new SortingField();
	int pageSize = 20, totalPages = 1, currentPage = 1;
	if (instanceList != null) {
		int type = instanceList.getType();
		sortedField = instanceList.getSortedField();
		if(sortedField==null) sortedField = new SortingField();
	%>
		<tr class="tit_bg">
			<th class="r_line" style="width:40px;"><input type="checkbox" class="js_check_all_file_instance" /></th>
	 		<th class="r_line" style="width:40px;">
				<span><fmt:message key="common.title.number"/></span>
			</th>
			<th class="r_line">
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_FILE_NAME%>"><fmt:message key='common.title.file_name'/>
			 		<span class="<%
					if(sortedField.getFieldId().equals(FormField.ID_FILE_NAME)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
					%>"></span>
				</a>						
				<span class="js_progress_span"></span>
			</th>
			<th class="r_line">
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_FILE_SIZE%>"><fmt:message key='common.title.file_size'/>
			 		<span class="<%
					if(sortedField.getFieldId().equals(FormField.ID_FILE_SIZE)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
					%>"></span>
				</a>						
				<span class="js_progress_span"></span>
			</th>
			<%
			if(displayType==FileCategory.DISPLAY_ALL){
			%>
				<th class="r_line">
		 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_WORK_SPACE%>"><fmt:message key='common.title.work_space_name'/>
				 		<span class="<%
						if(sortedField.getFieldId().equals(FormField.ID_WORK_SPACE)){
							if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
						%>"></span>
					</a>				
					<span class="js_progress_span"></span>
				</th>
			<%
			}
			if(displayType!=FileCategory.DISPLAY_BY_CATEGORY){
			%>			
				<th class="r_line">
		 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_FILE_CATEGORY%>"><fmt:message key='common.title.category_name'/>
				 		<span class="<%
						if(sortedField.getFieldId().equals(FormField.ID_FILE_CATEGORY)){
							if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
						%>"></span>
					</a>				
					<span class="js_progress_span"></span>
				</th>
			<%
			}
			if(displayType!=FileCategory.DISPLAY_BY_WORK){
			%>			
				<th class="r_line">
		 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_TSK_NAME%>"><fmt:message key='common.title.work_name'/>
				 		<span class="<%
						if(sortedField.getFieldId().equals(FormField.ID_TSK_NAME)){
							if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
						%>"></span>
					</a>				
					<span class="js_progress_span"></span>
				</th>
			<%
			}
			%>			
			<th class="r_line">
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_TSK_TITLE%>"><fmt:message key='common.title.instance_subject'/>
			 		<span class="<%
					if(sortedField.getFieldId().equals(FormField.ID_TSK_TITLE)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
					%>"></span>
				</a>				
				<span class="js_progress_span"></span>
			</th>
			<th class="r_line">
				<%
				if(displayType!=FileCategory.DISPLAY_BY_OWNER){
				%>			
					<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_TSK_ASSIGNEE %>">
						<fmt:message key='common.title.owner' />
						<span class="<%if(sortedField.getFieldId().equals(FormField.ID_TSK_ASSIGNEE)){
							if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} %>"></span>
					</a>/
				<%
				}
				%>
				<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_TASK_LAST_MODIFYDATE%>">
					<fmt:message key='common.title.last_modified_date' />
					<span class="<%if(sortedField.getFieldId().equals(FormField.ID_TASK_LAST_MODIFYDATE)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} %>"></span>
				</a>
				<span class="js_progress_span"></span>
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
		if(instanceList.getInstanceDatas() != null) {
			InstanceInfo[] instanceInfos = (InstanceInfo[])instanceList.getInstanceDatas();
			for (InstanceInfo instanceInfo : instanceInfos) {
				FileInstanceInfo fileInstance = (FileInstanceInfo)instanceInfo;
				UserInfo owner = instanceInfo.getOwner();
				UserInfo lastModifier = instanceInfo.getLastModifier();
				String workId = instanceInfo.getWorkId();
				String target = ((WorkInstanceInfo)instanceInfo).getController() + "?cid=" + ((WorkInstanceInfo)instanceInfo).getContextId() + "&workId=" + workId;
				List<Map<String, String>> fileNames = fileInstance.getFiles();
				String fileId = (fileNames.size()==1) ? fileNames.get(0).get("fileId") : "";
				String fileName = (fileNames.size()==1) ? fileNames.get(0).get("fileName") : "";
				String fileSize = (fileNames.size()==1) ? fileNames.get(0).get("fileSize") : "";
				long size = (SmartUtil.isBlankObject(fileSize)) ? 0 : Long.parseLong(fileSize);
				fileSize = SmartUtil.getBytesAsString(size);
			%>
				<tr class="instance_list js_content_work_space" href="<%=target%>">
					<%-- <td class="tc"><input name="chkSelectMail" class="js_check_file_instance" type="checkbox" value="<%=instanceInfo.getId()%>"/></td> --%>
					<td class="tc"><input name="chkSelectMail" class="js_check_file_instance" type="checkbox" value="<%=fileId%>"/></td>
					<td class="tc"><%=currentCount%></td>
					<td>
						<a class="js_content_work_space" href="<%=target %>">
	  						<span class="js_pop_files_detail" filesDetail="<%=fileInstance.getFilesHtml()%>"><%=fileName %></span>
						</a>
					</td>
					<td class="tr">
						<a class="js_content_work_space" href="<%=target %>"><%=fileSize %></a>
					</td>
					<%
					if(displayType==FileCategory.DISPLAY_ALL){
					%>
						<td>
							<a class="js_content_work_space" href="<%=target %>"><%=fileInstance.getWorkSpaceName()%></a>
						</td>
					<%
					}
					if(displayType!=FileCategory.DISPLAY_BY_CATEGORY){
					%>
						<td>
							<a class="js_content_work_space" href="<%=target %>">						
 								<%if(!SmartUtil.isBlankObject(fileInstance.getFileCategory())){%><%=fileInstance.getFileCategory().getName()%><%} %>
 							</a>
 						</td>
					<%
					}
					if(displayType!=FileCategory.DISPLAY_BY_WORK){
					%>
						<td>
							<a class="js_content_work_space" href="<%=target %>">				
								<span class="<%=Work.getIconClass(fileInstance.getWorkId(), fileInstance.getWorkType(), fileInstance.isWorkRunning())%>"><%=fileInstance.getWorkFullPathName()%></span>
							</a>
						</td>
					<%
					}
					%>
					<td>
 						<%if(!SmartUtil.isBlankObject(fileInstance.getWorkInstance())){%>
							<a class="js_content_work_space" href="<%=target %>">					
	 							<%=fileInstance.getWorkInstance().getSubject()%>
								<%if(((WorkInstanceInfo)fileInstance).getSubInstanceCount()>0){ %><font class="t_sub_count">[<b><%=((WorkInstanceInfo)fileInstance).getSubInstanceCount() %></b>]</font><%} %>
								<%if(fileInstance.isNew()){ %><span class="icon_new"></span><%} %> 							
							</a>
 						<%} %>
					</td>
					<td>
						<%
						if(!SmartUtil.isBlankObject(lastModifier)){
						%>
							<a class="js_content_work_space" href="<%=target %>">						
								<%
								if(displayType!=FileCategory.DISPLAY_BY_OWNER){
								%>
									<div class="noti_pic">
										<img src="<%=lastModifier.getMinPicture()%>" title="<%=lastModifier.getLongName()%>" class="profile_size_s" />
									</div>
								<%
								}
								%>
								<div class="noti_in_s">
									<span class="t_name"><%=lastModifier.getLongName()%></span>
									<div class="t_date"><%=instanceInfo.getLastModifiedDate().toLocalString()%></div>
								</div>
							</a>
						<%
						}
						%>
					</td>
					<td class="tc"><%=((FileInstanceInfo)instanceInfo).getViews() %>
				</tr>
	<%
				currentCount--;
			}
		}
	}else{
			sortedField = new SortingField();
	%>
		<tr class="tit_bg">
	 		<th class="r_line" style="width:40px;">
				<span><fmt:message key="common.title.number"/></span>
			</th>
			<th class="r_line">
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_STATUS%>"><fmt:message key='common.title.status'/>
			 		<span class="<%
					if(sortedField.getFieldId().equals(FormField.ID_STATUS)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
					%>"></span>
				</a>				
			</th>
			<th class="r_line">
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_OWNER%>"><fmt:message key='common.title.owner'/>
			 		<span class="<%
					if(sortedField.getFieldId().equals(FormField.ID_OWNER)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
					%>"></span>
				</a>/				
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_CREATED_DATE%>"><fmt:message key='common.title.created_date'/>
			 		<span class="<%
					if(sortedField.getFieldId().equals(FormField.ID_CREATED_DATE)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
					%>"></span>
				</a>
			</th>				
			<th class="r_line">
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_SUBJECT%>"><fmt:message key='common.title.instance_subject'/>
			 		<span class="<%
					if(sortedField.getFieldId().equals(FormField.ID_SUBJECT)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
					%>"></span>
				</a>				
			</th>
			<th class="r_line">
	 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_LAST_TASK%>"><fmt:message key='common.title.last_task'/>
			 		<span class="<%
					if(sortedField.getFieldId().equals(FormField.ID_SUBJECT)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
					%>"></span>
				</a>						
			</th>
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
	 		<th class="r_line" style="width:40px;">
				<span><fmt:message key="common.title.views"/></span>
			</th>
		</tr>	
	<%		
	}
	%>
</table>
<!-- 목록페이지 //-->

<%
if(instanceList == null || SmartUtil.isBlankObject(instanceList.getInstanceDatas())){
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
