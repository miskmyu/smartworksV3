<%@page import="net.smartworks.model.community.info.DepartmentInfo"%>
<%@page import="net.smartworks.server.engine.organization.model.SwoDepartment"%>
<%@page import="net.smartworks.model.community.info.CommunityInfoList"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.model.community.info.GroupInfo"%>
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
	if(SmartUtil.isBlankObject(params)){
		String savedWorkId = (String)session.getAttribute("workId");
		params = (RequestParams)session.getAttribute("requestParams");
		if(!Work.ID_GROUP_WORK.equals(savedWorkId) || SmartUtil.isBlankObject(params)){
			params = new RequestParams();
			params.setPageSize(20);
			params.setCurrentPage(1);
		}
	}
	session.setAttribute("requestParams", params);
	session.setAttribute("workId", Work.ID_DEPARTMENT_WORK);
	CommunityInfoList instanceList = smartWorks.getCommunityInstanceList(CommunityInfoList.TYPE_DEPARTMENT_INFO_LIST, params);
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!-- 목록 테이블 -->
<table>
	<%	
	SortingField sortedField = null;
	int pageSize = 20, totalPages = 1, currentPage = 1;
	if (instanceList != null) {
		sortedField = instanceList.getSortedField();
		if(sortedField==null) sortedField = new SortingField();
	%>
		<tr class="tit_bg">
	 		<th class="r_line" style="width:40px;">
				<span><fmt:message key="common.title.number"/></span>
			</th>
	 		<th class="r_line" style="width:20px;"></th>
			<th class="r_line">
				<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_NAME%>">
					<fmt:message key='department.title.name' /><span class="<%if(sortedField.getFieldId().equals(FormField.ID_NAME)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} %>"></span>
				</a>
			</th>
			<th class="r_line"><fmt:message key='settings.title.department.parent_name' /></th>
			<th class="r_line"><fmt:message key='department.title.desc' /></th>
			<th class="r_line"><fmt:message key='department.role.head' /></th>
			<th class="r_line"><fmt:message key='department.members_count' /></th>
			<th class="r_line"><fmt:message key='department.subdepartment_count' /></th>
			<th class="r_line">
				<a href="" class="js_select_field_sorting" fieldId="<%=SwoDepartment.A_MODIFICATIONUSER%>">
					<fmt:message key='common.title.last_modifier' /><span class="<%if(sortedField.getFieldId().equals(SwoDepartment.A_MODIFICATIONUSER)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} %>"></span>
				</a>/
				<a href="" class="js_select_field_sorting" fieldId="<%=SwoDepartment.A_MODIFICATIONDATE%>">
					<fmt:message key='common.title.last_modified_date' /><span class="<%if(sortedField.getFieldId().equals(SwoDepartment.A_MODIFICATIONDATE)){
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
		if(instanceList.getCommunityDatas() != null) {
			DepartmentInfo[] departmentInfos = (DepartmentInfo[])instanceList.getCommunityDatas();
			for (DepartmentInfo department : departmentInfos) {
				UserInfo lastModifier = department.getLastModificationUser();
				UserInfo leader = department.getHead();
				String leaderPicture = (SmartUtil.isBlankObject(leader)) ? "" : leader.getMinPicture();
				String leaderLongName = (SmartUtil.isBlankObject(leader)) ? "" : leader.getLongName();
				LocalDate lastModifiedDate = department.getLastModificationDate();
				String target = "department_space.sw?cid=" + ISmartWorks.CONTEXT_PREFIX_DEPARTMENT_SPACE + department.getId();
			%>
				<tr class="instance_list" href="<%=target%>">
					<td class="tc"><%=currentCount--%></td>
					<td class="tc"><div title="<fmt:message key='nav.works.my_favorite_works'/>" class="js_check_favorite_com icon_fvrt <%if(department.isFavorite()){ %> checked <%} %>" comId="<%=department.getId() %>" ></div></td>
					<td>
						<a href="<%=target %>">
							<div class="noti_pic">
								<img src="<%=department.getMinPicture()%>" title="<%=department.getName()%>" class="profile_size_s" />
								<span><%=department.getName()%></span>
							</div>
						</a>
					</td>
					<td>
						<a href="<%=target %>">
							<div><%=department.getFullpathName()%></div>
						</a>
					</td>
					<td>
						<a href="<%=target %>">
							<div><%=CommonUtil.toNotNull(department.getDesc())%></div>
						</a>
					</td>
					<td>
						<%if(!SmartUtil.isBlankObject(leader)){ %>
							<a href="<%=target %>">					
								<div class="noti_pic">
									<img src="<%=leaderPicture%>" title="<%=leaderLongName%>" class="profile_size_s" />
									<span><%=leaderLongName%></span>
								</div>
							</a>
						<%} %>
					</td>
					<td>
						<a href="<%=target %>">
							<div class="tc"><%=department.getNumberOfMembers()%></div>
						</a>
					</td>
					<td>
						<a href="<%=target %>">
							<div class="tc"><%=department.getNumberOfDescendents()%></div>
						</a>
					</td>
					<td>
						<a href="<%=target %>">
							<div class="noti_pic">
								<img src="<%=lastModifier.getMinPicture()%>" title="<%=lastModifier.getLongName()%>" class="profile_size_s" />
							</div>
							<div class="noti_in_s">
								<span class="t_name"><%=lastModifier.getLongName()%></span>
								<div class="t_date"><%=lastModifiedDate.toLocalString()%></div>
							</div>
						</a>
					</td>
				</tr>
		<%
			}
		}
	}else{
		sortedField = new SortingField();
		sortedField.setFieldId(SwoDepartment.A_MODIFICATIONDATE);
	%>
		<tr class="tit_bg">
	 		<th class="r_line" style="width:40px;">
				<span><fmt:message key="common.title.number"/></span>
			</th>
	 		<th class="r_line" style="width:20px;"></th>
			</th>
			<th class="r_line">
				<a href="" class="js_select_field_sorting" fieldId="<%=FormField.ID_NAME%>">
					<fmt:message key='department.title.name' /><span class="<%if(sortedField.getFieldId().equals(FormField.ID_NAME)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} %>"></span>
				</a>
			</th>
			<th class="r_line"><fmt:message key='settings.title.department.parent_name' /></th>
			<th class="r_line"><fmt:message key='department.title.desc' /></th>
			<th class="r_line"><fmt:message key='department.role.head' /></th>
			<th class="r_line"><fmt:message key='department.members_count' /></th>
			<th class="r_line"><fmt:message key='department.subdepartment_count' /></th>
			<th class="r_line">
				<a href="" class="js_select_field_sorting" fieldId="<%=SwoDepartment.A_MODIFICATIONUSER%>">
					<fmt:message key='common.title.last_modifier' /><span class="<%if(sortedField.getFieldId().equals(SwoDepartment.A_MODIFICATIONUSER)){
						if(sortedField.isAscending()){ %>icon_in_up<%}else{ %>icon_in_down<%}} %>"></span>
				</a>/
				<a href="" class="js_select_field_sorting" fieldId="<%=SwoDepartment.A_MODIFICATIONDATE%>">
					<fmt:message key='common.title.last_modified_date' /><span class="<%if(sortedField.getFieldId().equals(SwoDepartment.A_MODIFICATIONDATE)){
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
if(instanceList == null || SmartUtil.isBlankObject(instanceList.getCommunityDatas())){
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
