<%@page import="net.smartworks.model.report.TableReport"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.model.report.MatrixReport"%>
<%@page import="net.smartworks.model.KeyMap"%>
<%@page import="net.smartworks.model.work.InformationWork"%>
<%@page import="net.smartworks.model.work.FormField"%>
<%@page import="net.smartworks.model.report.ChartReport"%>
<%@page import="net.smartworks.model.report.Report"%>
<%@page import="net.smartworks.model.work.SmartWork"%>
<%@page import="net.smartworks.model.work.Work"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.util.SmartTest"%>
<%@page import="net.smartworks.model.community.User"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>

<%
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	String reportId = request.getParameter("reportId");
	User cUser = SmartUtil.getCurrentUser();

	int reportType = Report.TYPE_TABLE;
	SmartWork work = (SmartWork)session.getAttribute("smartWork");
	String workId = work.getId();
	Report report = null;
	TableReport table = null;
	if (!SmartUtil.isBlankObject(workId))
		work = (SmartWork) smartWorks.getWorkById(workId);
	if (!SmartUtil.isBlankObject(reportId))
		report = smartWorks.getReportById(reportId);

	if(report != null && report.getClass().equals(TableReport.class))
		table = (TableReport) report;

	FormField[] fields = null;
	if ((work != null) && (work.getType() == SmartWork.TYPE_INFORMATION)) {
		InformationWork informationWork = (InformationWork) work;
		if (informationWork.getForm() != null) {
			FormField[] informationFields = informationWork.getForm().getFields();
			if(informationFields != null){
				fields = new FormField[informationFields.length + FormField.DEFAULT_INFORMATION_FIELDS.length];
				for(int i=0; i< informationFields.length; i++)
					fields[i] = informationFields[i];
				for(int i=0; i< FormField.DEFAULT_INFORMATION_FIELDS.length; i++)
					fields[informationFields.length+i] = FormField.DEFAULT_INFORMATION_FIELDS[i];
			}else{
				fields = FormField.DEFAULT_INFORMATION_FIELDS;
			}
		}
	} else if((work != null) && (work.getType() == SmartWork.TYPE_PROCESS)){
		fields = FormField.DEFAULT_PROCESS_FIELDS;
	} else{
		fields = new FormField[] {};
	}
	

%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!-- 목록 -->
<table>
	<tr>
		<th width="400px"><fmt:message key="report.title.select_display_fields" /></th>
		<td colspan="4">
		</td>		
	</tr>
</table>
<form name="frmWorkReportTableFields" class="list_contents js_validation_required">

	<%
	if(fields !=null){
		FormField[] displayFields = (table!=null) ? table.getDisplayFields() : null;
	%>
		<!-- 보이는 항목,안보이는 항목 설정-->
		<div class="oh js_tab_work_settings_page">
			<div class="left40 gray_style">
				<table class="js_display_field_items">
					<tr>
						<th><fmt:message key="builder.title.display_fields"/></th>
						<th width="50px"><fmt:message key="builder.title.move_item"/></th>
					</tr>
					<tr class="list_action_item" style="display:none">
						<td class="js_input_display_field"></td>
						<td class="btn_move_field" >
							<span class="move_actions">
								<span class="js_up_field_item" title="<fmt:message key="builder.button.move_up_item"/>"><a href="" class="icon_up"></a></span>
								<span style="display:none" class="js_down_field_item" title="<fmt:message key="builder.button.move_down_item"/>"><a href="" class="icon_down"></a></span>
								<span class="js_hide_field_item" title="<fmt:message key="builder.button.hide_item"/>"><a href="" class="icon_hide"></a></span>
							</span>
						</td>
					</tr>
					<%
					if(!SmartUtil.isBlankObject(displayFields) && displayFields.length>0){
						int count = 0;;
						for(FormField formField : displayFields){
					%>
							<tr class="list_action_item">
								<td><input name="hdnDisplayFields" type="hidden" value="<%=formField.getId()%>" fieldName="<%=formField.getName() %>"/><%=formField.getName() %></td>
								<td class="btn_move_field" >
									<span class="move_actions">
										<span <%if(count==0){ %>style="display:none"<%} %> class="js_up_field_item" title="<fmt:message key="builder.button.move_up_item"/>"><a href="" class="icon_up"></a></span>
										<span <%if(count==displayFields.length-1){ %>style="display:none"<%} %> class="js_down_field_item icon_down" title="<fmt:message key="builder.button.move_down_item"/>" ><a href="" class="icon_down"></a></span>
										<span class="js_hide_field_item" title="<fmt:message key="builder.button.hide_item"/>"><a href="" class="icon_hide"></a></span>
									</span>
								</td>
							</tr>
						<%
							count++;
						}
						%>
					<%
					}
					%>
				</table>
			</div>

			<div class="right40 gray_style">
				<table class="js_hidden_field_items">
					<tr>
						<th width="20px"><fmt:message key="builder.title.move_item"/></th>
						<th><fmt:message key="builder.title.hidden_fields"/></th>
					</tr>
					<tr class="list_action_item" style="display:none"> 
						<td class="btn_move_field">
							<span class="move_actions" title="<fmt:message key="builder.button.show_item"/>"><span class="js_show_field_item"><a href="" class="icon_show"></a></span></span>
						</td>
						<td></td>
					</tr>
					<%
					if(!SmartUtil.isBlankObject(fields) && fields.length>0){
						for(FormField formField : fields){
							boolean isDisplayField = false;
							if(!SmartUtil.isBlankObject(displayFields)){
								for(FormField disField : displayFields){
									if(formField.getId().equals(disField.getId())){
										isDisplayField = true;
										break;
									}
								}
								if(isDisplayField) continue;
							}
					%>
							<tr class="list_action_item" fieldId="<%=formField.getId()%>">
								<td class="btn_move_field">
									<span class="move_actions" title="<fmt:message key="builder.button.show_item"/>"><span class="js_show_field_item"><a href="" class="icon_show"></a></span></span>
								</td>
								<td><%=formField.getName()%></td>
							</tr>
					<%
						}
					}
					%>
				</table>
			</div>
		</div>
		<!-- 보이는 항목, 안보이는 항목 //-->
	<%
	}
	%>
</form>
<br>
<table>
	<tr>
		<th width="200px"><fmt:message key="report.title.sorting_field" /></th>
		<td colspan="4">
			<select name="selReportSortingField">
				<%
				String sortingFieldId = null;
				if (fields != null) {
					if (table != null && table.getSortingField() != null)
						sortingFieldId = table.getSortingField().getId();
					for (FormField field : fields) {
				%>
						<option type="<%=field.getType()%>" value="<%=field.getId()%>"
							<%if (sortingFieldId != null && sortingFieldId.equals(field.getId())) {%> selected
							<%}%>><%=field.getName()%></option>
				<%
					}
				}
				%>
			</select>
		<%
		boolean sortingAscend = (table != null) ? table.isSortingAscend() : false;
		%>
		<span>
			<input type="radio" name="rdoReportXAxisSort" value="<%=Report.AXIS_SORT_DESCEND.getId()%>"
				<%if (!sortingAscend) {%> checked <%}%>> 
				<fmt:message key="<%=Report.AXIS_SORT_DESCEND.getKey() %>" /> 
			<input type="radio" name="rdoReportXAxisSort" value="<%=Report.AXIS_SORT_ASCEND.getId()%>"
				<%if (sortingAscend) {%> checked <%}%>> 
				<fmt:message key="<%=Report.AXIS_SORT_ASCEND.getKey() %>" />
		</span>
		
		</td>		
	</tr>
	<tr>
		<th width="200px"><fmt:message key="common.title.count_in_page" /></th>
		<td colspan="4">
			<select name="selReportPageSize">
				<%
				int pageSize = (table!=null) ? table.getPageSize() : 20;
				%>
					<option <%if (pageSize == 10) {%> selected <%}%>>20</option>
					<option <%if (pageSize == 20) {%> selected <%}%>>20</option>
					<option <%if (pageSize == 30) {%> selected <%}%>>30</option>
					<option <%if (pageSize == 50) {%> selected <%}%>>50</option>
					<option <%if (pageSize == 100) {%> selected <%}%>>100</option>
			</select>
		
		</td>		
	</tr>
</table>