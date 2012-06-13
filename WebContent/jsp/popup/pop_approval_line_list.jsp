<%@page import="net.smartworks.model.RecordList"%>
<%@page import="net.smartworks.model.approval.ApprovalLine"%>
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
	getIntanceList = function(paramsJson){
		console.log(JSON.stringify(paramsJson));
		var url = "set_instance_list_params.sw";
		$.ajax({
			url : url,
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				$('#approval_line_list_page').html(data);
			},
			error : function(e) {
				smartPop.showInfo(smartPop.ERROR, smartMessage.get('iworkListError'));
			}
		});
	};
	
	selectListParam = function(progressSpan, isGray){
		var forms = $('form#approval_line_list_page:visible');
		var paramsJson = {};
		paramsJson["href"] = "jsp/popup/pop_approval_line_list.jsp";
		for(var i=0; i<forms.length; i++){
			var form = $(forms[i]);
			paramsJson[form.attr('name')] = mergeObjects(form.serializeObject(), SmartWorks.GridLayout.serializeObject(form));
		}
		getIntanceList(paramsJson);		
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
	RecordList  recordList = smartWorks.getApprovalLineList(params);

%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<div class="list_contents form_contents">

	<!-- 목록 테이블 -->
	<table>
	<%
	SortingField sortedField = null;
	int pageSize = 0, totalPages = 0, currentPage = 0;
	if (recordList != null) {
		int type = recordList.getType();
		if(sortedField==null) sortedField = new SortingField();
		pageSize = recordList.getPageSize();
		totalPages = recordList.getTotalPages();
		currentPage = recordList.getCurrentPage();
		if(recordList.getRecords() != null) {
			ApprovalLine[] approvalLines = (ApprovalLine[])recordList.getRecords();

	%>
			<tr class="tit_bg js_instance_list_header">
				<th class="r_line"><fmt:message key="settings.title.approval.name"/></th>
				<th class="r_line"><fmt:message key="settings.title.approval.desc"/></th>
				<th class="r_line"><fmt:message key="settings.title.approval.level"/></th>
			</tr>

			<%
			if(!SmartUtil.isBlankObject(approvalLines)){
				for (ApprovalLine approvalLine : approvalLines) {
			%>
					<tr class="js_pop_select_approval_line" approvalLineId=<%=CommonUtil.toNotNull(approvalLine.getId()) %>>
						<td><a href=""><%=approvalLine.getName()%></a></td>
						<td><a href=""><%=approvalLine.getDesc() %></a></td>
						<td><a href=""><%=approvalLine.getApprovalLevel() %><fmt:message key="settings.title.approval_level"/></a></td>
					</tr>
			<%
				}
			}else{
			%>
				<tr><td><fmt:message key="common.message.no_instance"/></td></tr>
			<%
			}
		}
	}
	%>
	</table>

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
