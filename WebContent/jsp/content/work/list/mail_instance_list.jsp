<%@page import="org.claros.commons.mail.models.EmailPriority"%>
<%@page import="net.smartworks.model.instance.info.MailInstanceInfo"%>
<%@page import="net.smartworks.model.mail.MailFolder"%>
<%@page import="net.smartworks.model.work.MailWork"%>
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
	MailWork work = (MailWork)session.getAttribute("smartWork");
	String folderId = work.getId();
	if(SmartUtil.isBlankObject(params)){
		String savedWorkId = (String)session.getAttribute("workId");
		params = (RequestParams)session.getAttribute("requestParams");
		if(!folderId.equals(savedWorkId) || SmartUtil.isBlankObject(params)){
			params = new RequestParams();
			params.setPageSize(20);
			params.setCurrentPage(1);
			params.setSortingField(new SortingField("date", false));
		}
	}
	session.setAttribute("requestParams", params);
	session.setAttribute("workId", folderId);

	InstanceInfoList instanceList = smartWorks.getMailInstanceList(folderId, params);
	
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!-- 목록 테이블 -->
<table>
	<tbody>
		<%
		SortingField sortedField = null;
		int pageSize = 0, totalPages = 0, currentPage = 0;
		if (instanceList != null && (instanceList.getInstanceDatas() != null) && (work != null)) {
			int type = instanceList.getType();
			sortedField = instanceList.getSortedField();
			if(sortedField==null) sortedField = new SortingField();
		%>
			<tr class="tit_bg">
				<th><input type="checkbox" /></th>
				<th><div class="icon_important"></div></th>
				<th><div class="icon_mail_read"></div></th>
<!-- 				<th class="r_line"><div class="icon_file checked"></div></th>
 -->				<%
				if(folderId.equals(MailFolder.ID_INBOX) || folderId.equals(MailFolder.ID_JUNK)){
				%>
					<th class="r_line">
			 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_SENDER%>"><fmt:message key='common.title.sender'/>
					 		<span class="<%
							if(sortedField.getFieldId().equals(FormField.ID_SENDER)){
								if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
							%>"></span>
						</a>				
						<span class="js_progress_span"></span>
					</th>
				<%
				}else if(folderId.equals(MailFolder.ID_SENT) || folderId.equals(MailFolder.ID_DRAFTS)){
				%>
					<th class="r_line">
			 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_RECEIVERS%>"><fmt:message key='common.title.receivers'/>
					 		<span class="<%
							if(sortedField.getFieldId().equals(FormField.ID_RECEIVERS)){
								if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
							%>"></span>
						</a>				
						<span class="js_progress_span"></span>
					</th>
				<%
				}
				%>
				<th class="r_line">
		 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_SUBJECT%>"><fmt:message key='common.title.instance_subject'/>
				 		<span class="<%
						if(sortedField.getFieldId().equals(FormField.ID_SUBJECT)){
							if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
						%>"></span>
					</a>				
					<span class="js_progress_span"></span>
				</th>
				<%
				if(folderId.equals(MailFolder.ID_INBOX) || folderId.equals(MailFolder.ID_JUNK)){
				%>
					<th class="r_line">
			 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_SEND_DATE%>"><fmt:message key='common.title.received_date'/>
					 		<span class="<%
							if(sortedField.getFieldId().equals(FormField.ID_SEND_DATE)){
								if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
							%>"></span>
						</a>
						<span class="js_progress_span"></span>
					</th>				
				<%
				}else if(folderId.equals(MailFolder.ID_SENT) || folderId.equals(MailFolder.ID_DRAFTS)){
				%>
					<th class="r_line">
			 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_SEND_DATE%>"><fmt:message key='common.title.send_date'/>
					 		<span class="<%
							if(sortedField.getFieldId().equals(FormField.ID_SEND_DATE)){
								if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
							%>"></span>
						</a>
						<span class="js_progress_span"></span>
					</th>				
				<%
				}
				%>
			</tr>

			<%
			pageSize = instanceList.getPageSize();
			totalPages = instanceList.getTotalPages();
			currentPage = instanceList.getCurrentPage();
			int currentCount = instanceList.getTotalSize()-(currentPage-1)*pageSize;
			if(instanceList.getInstanceDatas() != null) {
				MailInstanceInfo[] instanceInfos = (MailInstanceInfo[]) instanceList.getInstanceDatas();
				for (MailInstanceInfo instanceInfo : instanceInfos) {
					UserInfo owner = instanceInfo.getOwner();
					UserInfo lastModifier = instanceInfo.getLastModifier();
					String cid = SmartWorks.CONTEXT_PREFIX_MAIL_SPACE + instanceInfo.getId();
					String wid = instanceInfo.getWorkSpace().getId();
					String target = "mail_space.sw?folderId=" + folderId + "&msgId=" + instanceInfo.getId();
				%>
					<tr class="instance_list <%if(instanceInfo.isUnread()){%>not_read<%}%>">
						<td class="tc"><input name="chkSelectMail" type="checkbox" value="<%=instanceInfo.getId()%>"/></td>
						<td><div class="<%if(instanceInfo.getPriority()>0 && instanceInfo.getPriority()<EmailPriority.NORMAL){ %>icon_important<%}%>"></div></td>
						<td><div class="<%if(instanceInfo.isUnread()) {%>icon_mail_read checked<%}%>"></div></td>
<%-- 						<td><div class="<%if(instanceInfo.isMultipart()){ %>icon_file<%}%>"></div></td>
 --%>						<td><a href="<%=target%>" class="js_content"><%=CommonUtil.toNotNull(instanceInfo.getSender().getName())%></a></td>
						<td><a href="<%=target%>" class="js_content"><%=CommonUtil.toNotNull(instanceInfo.getSubject())%></a></td>
						<td class="tr"><a href="<%=target%>" class="js_content"><%=CommonUtil.toNotNull(instanceInfo.getSendDate().toLocalString())%></a></td>
					</tr>
		<%
					currentCount--;
				}
			}
		}else if(!SmartUtil.isBlankObject(work)){
			sortedField = new SortingField();
		%>
			<tr class="tit_bg">
				<th><input type="checkbox" /></th>
				<th><div class="icon_important"></div></th>
				<th><div class="icon_mail_read"></div></th>
<!-- 				<th class="r_line"><div class="icon_file checked"></div></th>
 -->				<%
				if(folderId.equals(MailFolder.ID_INBOX) || folderId.equals(MailFolder.ID_JUNK)){
				%>
					<th class="r_line">
			 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_SENDER%>"><fmt:message key='common.title.sender'/>
					 		<span class="<%
							if(sortedField.getFieldId().equals(FormField.ID_SENDER)){
								if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
							%>"></span>
						</a>				
						<span class="js_progress_span"></span>
					</th>
				<%
				}else if(folderId.equals(MailFolder.ID_SENT) || folderId.equals(MailFolder.ID_DRAFTS)){
				%>
					<th class="r_line">
			 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_RECEIVERS%>"><fmt:message key='common.title.receivers'/>
					 		<span class="<%
							if(sortedField.getFieldId().equals(FormField.ID_RECEIVERS)){
								if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
							%>"></span>
						</a>				
						<span class="js_progress_span"></span>
					</th>
				<%
				}
				%>
				<th class="r_line">
		 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_SUBJECT%>"><fmt:message key='common.title.instance_subject'/>
				 		<span class="<%
						if(sortedField.getFieldId().equals(FormField.ID_SUBJECT)){
							if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
						%>"></span>
					</a>				
					<span class="js_progress_span"></span>
				</th>
				<%
				if(folderId.equals(MailFolder.ID_INBOX) || folderId.equals(MailFolder.ID_JUNK)){
				%>
					<th class="r_line">
			 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_SEND_DATE%>"><fmt:message key='common.title.send_date'/>
					 		<span class="<%
							if(sortedField.getFieldId().equals(FormField.ID_SEND_DATE)){
								if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
							%>"></span>
						</a>
						<span class="js_progress_span"></span>
					</th>				
				<%
				}else if(folderId.equals(MailFolder.ID_SENT) || folderId.equals(MailFolder.ID_DRAFTS)){
				%>
					<th class="r_line">
			 			<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_SEND_DATE%>"><fmt:message key='common.title.received_date'/>
					 		<span class="<%
							if(sortedField.getFieldId().equals(FormField.ID_SEND_DATE)){
								if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} 
							%>"></span>
						</a>
						<span class="js_progress_span"></span>
					</th>				
				<%
				}
				%>
			</tr>
		<%
		}
		%>
	</tbody>
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
			<option <%if (pageSize == 10) {%> selected <%}%>>10</option>
			<option <%if (pageSize == 20) {%> selected <%}%>>20</option>
			<option <%if (pageSize == 30) {%> selected <%}%>>30</option>
			<option <%if (pageSize == 50) {%> selected <%}%>>50</option>
		</select>
	</div>
	<!-- 페이징 //-->
</form>
