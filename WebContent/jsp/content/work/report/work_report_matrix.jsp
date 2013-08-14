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
	String targetWorkId = request.getParameter("targetWorkId");
	String strTargetWorkType = request.getParameter("targetWorkType");
	int targetWorkType = (SmartUtil.isBlankObject(strTargetWorkType)) ? Work.TYPE_NONE : Integer.parseInt(strTargetWorkType);
	User cUser = SmartUtil.getCurrentUser();

	int reportType = Report.TYPE_MATRIX;
	SmartWork work = (SmartWork)session.getAttribute("smartWork");
	if(!SmartUtil.isBlankObject(targetWorkId) && work.getId().equals(SmartWork.ID_REPORT_MANAGEMENT)){
		work = (SmartWork) smartWorks.getWorkById(targetWorkId);
	}
	Report report = null;
	MatrixReport matrix = null;
	if (!SmartUtil.isBlankObject(reportId))
		report = smartWorks.getReportById(reportId);

	if(report != null && report.getClass().equals(MatrixReport.class))
		matrix = (MatrixReport) report;

	FormField[] fields = null;
	if ((work != null) && (work.getType() == SmartWork.TYPE_INFORMATION)) {
		InformationWork informationWork = (InformationWork) work;
		if (informationWork.getForm() != null) {
			fields = informationWork.getForm().getFields();
		}
	} else {
		fields = new FormField[] {};
	}

%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<tr>
	<th width="200px"><fmt:message key="report.title.xaxis_mat" /></th>
	<td class="js_select_chart_axis" colspan="4">
		<select name="selReportXAxis">
			<%
			String xAxisId = null;
			if (fields != null) {
				if (matrix != null && matrix.getXAxis() != null)
					xAxisId = matrix.getXAxis().getId();
				for (FormField field : fields) {
			%>
					<option type="<%=field.getType()%>" value="<%=field.getId()%>"
						<%if (xAxisId != null && xAxisId.equals(field.getId())) {%> selected
						<%}%>><%=field.getName()%></option>
			<%
				}
			}
			%>
			<jsp:include page="/jsp/content/work/field/default_fields.jsp">
				<jsp:param name="workType" value="<%=CommonUtil.toNotNull(work.getType()) %>" />			
				<jsp:param name="fieldId" value="<%=CommonUtil.toNotNull(xAxisId) %>" />
			</jsp:include>
		</select>
	
		<%
		String xFieldType = "";
		if (matrix != null)
			xFieldType = matrix.getXAxis().getType();
		String xAxisSelector = null;
		if (matrix != null && matrix.getXAxisSelector() != null)
			xAxisSelector = matrix.getXAxisSelector();
		%>
	
		<span class="js_axis_selector_date" <%if (!xFieldType.equals(FormField.TYPE_DATE) && !xFieldType.equals(FormField.TYPE_DATETIME)) {%> style="display: none" <%}%>>
			<select name="selReportXAxisSelectorDate">
				<%
				for (KeyMap selector : Report.AXIS_SELECTORS_DATE) {
				%>
					<option value="<%=selector.getId()%>"
						<%if (xAxisSelector != null && xAxisSelector.equals(selector.getId())) {%> selected <%}%>>
						<fmt:message key="<%=selector.getKey() %>" />
					</option>
				<%
				}
				%>
			</select>
		</span>
		<span class="js_axis_selector_user" <%if (!xFieldType.equals(FormField.TYPE_USER)) {%> style="display: none" <%}%>>
			<select name="selReportXAxisSelectorUser">
				<%
				for (KeyMap selector : Report.AXIS_SELECTORS_USER) {
				%>
					<option value="<%=selector.getId()%>"
						<%if (xAxisSelector != null && xAxisSelector.equals(selector.getId())) {%> selected <%}%>>
						<fmt:message key="<%=selector.getKey() %>" />
					</option>
				<%
				}
				%>
			</select>
		</span>
		<%
		String xAxisSort = null;
		int xAxisMaxRecords = -1;
		if (matrix != null && matrix.getXAxisSort() != null){
			xAxisSort = matrix.getXAxisSort();
			xAxisMaxRecords = matrix.getXAxisMaxRecords();
		}
		%>
		<span>
			<input type="radio" name="rdoReportXAxisSort" value="<%=Report.AXIS_SORT_DESCEND.getId()%>"
				<%if (xAxisSort == null || xAxisSort.equals(Report.AXIS_SORT_DESCEND.getId())) {%> checked <%}%>> 
				<fmt:message key="<%=Report.AXIS_SORT_DESCEND.getKey() %>" /> 
			<input type="radio" name="rdoReportXAxisSort" value="<%=Report.AXIS_SORT_ASCEND.getId()%>"
				<%if (xAxisSort != null && xAxisSort.equals(Report.AXIS_SORT_ASCEND.getId())) {%> checked <%}%>> 
				<fmt:message key="<%=Report.AXIS_SORT_ASCEND.getKey() %>" />
		</span>
		<span>
			<select class="js_select_xaxis_max" name="selReportXAxisMaxRecords">
				<option value="unlimited" <%if (xAxisMaxRecords < 1) {%> selected <%}%>>
					<fmt:message key="report.title.xaxis.unlimited" />
				</option>
				<option value="max" <%if (xAxisMaxRecords > 0) {%> selected <%}%>>
					<fmt:message key="report.title.xaxis.max" />
				</option>
			</select>
			<span <%if (xAxisMaxRecords < 1) {%> style="display:none" <%}%>>
				<input type="text" name="numReportXAxisMaxRecords" class="fieldline required number" style="width:30px" value="<%=xAxisMaxRecords%>"/><fmt:message key="report.title.xaxis.records" />
			</span>
		</span>
	</td>		
</tr>

<%
	String zFieldType="", zAxisId=null, zAxisSelector=null, zAxisSort=null, 
			xSecondFieldType="", xSecondAxisId=null, xSecondAxisSelector=null, xSecondAxisSort=null, 
			zSecondFieldType="", zSecondAxisId=null, zSecondAxisSelector=null, zSecondAxisSort=null;
	if (matrix != null) {
		if (matrix.getZAxis() != null){
			zFieldType = matrix.getZAxis().getType();
			zAxisId = matrix.getZAxis().getId();
		}
		if (matrix.getZAxisSelector() != null)
			zAxisSelector = matrix.getZAxisSelector();
		if (matrix.getZAxisSort() != null)
			zAxisSort = matrix.getZAxisSort();
	}
	if (matrix != null) {
		if (matrix.getXSecondAxis() != null){
			xSecondFieldType = matrix.getXSecondAxis().getType();
			xSecondAxisId = matrix.getXSecondAxis().getId();
		}
		if (matrix.getXSecondAxisSelector() != null)
			xSecondAxisSelector = matrix.getXSecondAxisSelector();
		if (matrix.getXSecondAxisSort() != null)
			xSecondAxisSort = matrix.getXSecondAxisSort();
		if (matrix.getZSecondAxis() != null){
			zSecondFieldType = matrix.getZSecondAxis().getType();
			zSecondAxisId = matrix.getZSecondAxis().getId();
		}
		if (matrix.getZSecondAxisSelector() != null)
			zSecondAxisSelector = matrix.getZSecondAxisSelector();
		if (matrix.getZSecondAxisSort() != null)
			zSecondAxisSort = matrix.getZSecondAxisSort();
	}
%>
<%-- <tr class="js_add_chart_xsecondaxis" <%if (xSecondAxisId != null) {%> style="display: none" <%}%>>
	<td colspan="5">
		<a href=""><fmt:message key="report.button.add_xsecondaxis_mat" /></a>
	</td>
</tr>

<tr class="js_chart_xsecondaxis" <%if (SmartUtil.isBlankObject(xSecondAxisId)) {%> style="display: none" <%}%>>
	<th width="200px"><fmt:message key="report.title.xsecondaxis_mat" /></th>
	<td class="js_select_chart_axis" colspan="4">
		<select name="selReportXSecondAxis">
			<%
			if (fields != null) {
				for (FormField field : fields) {
			%>
					<option type="<%=field.getType()%>" value="<%=field.getId()%>" 
						<%if (xSecondAxisId != null && xSecondAxisId.equals(field.getId())) {%> selected"<%}%>><%=field.getName()%>
					</option>
			<%
				}
			}
			%>
			<jsp:include page="/jsp/content/work/field/default_fields.jsp">
				<jsp:param name="workType" value="<%=CommonUtil.toNotNull(work.getType()) %>" />			
				<jsp:param name="fieldId" value="<%=CommonUtil.toNotNull(xSecondAxisId) %>" />
			</jsp:include>
		</select>
		<span class="js_axis_selector_date" <%if (!xSecondFieldType.equals(FormField.TYPE_DATE) && !xSecondFieldType.equals(FormField.TYPE_DATETIME)) {%> style="display: none" <%}%>>
			<select name="selReportXSecondAxisSelectorDate">
				<%
				for (KeyMap selector : Report.AXIS_SELECTORS_DATE) {
				%>
					<option value="<%=selector.getId()%>" <%if (xSecondAxisSelector != null && xSecondAxisSelector.equals(selector.getId())) {%> selected <%}%>>
						<fmt:message key="<%=selector.getKey() %>" />
					</option>
				<%
				}
				%>
			</select>
		</span>
		<span class="js_axis_selector_user" <%if (!xSecondFieldType.equals(FormField.TYPE_USER)) {%> style="display: none" <%}%>>
			<select name="selReportXSecondAxisSelectorUser">
				<%
				for (KeyMap selector : Report.AXIS_SELECTORS_USER) {
				%>
					<option value="<%=selector.getId()%>" <%if (xSecondAxisSelector != null && xSecondAxisSelector.equals(selector.getId())) {%> selected <%}%>>
						<fmt:message key="<%=selector.getKey() %>" />
					</option>
				<%
				}
				%>
			</select>
		</span>
		<span>
			<input type="radio" name="rdoReportXSecondAxisSort" value="<%=Report.AXIS_SORT_DESCEND.getId()%>"
				<%if (xSecondAxisSort == null || xSecondAxisSort.equals(Report.AXIS_SORT_DESCEND.getId())) {%> checked <%}%>>
				<fmt:message key="<%=Report.AXIS_SORT_DESCEND.getKey() %>" /> 
			<input type="radio" name="rdoReportXSecondAxisSort" value="<%=Report.AXIS_SORT_ASCEND.getId()%>"
				<%if (xSecondAxisSort != null && xSecondAxisSort.equals(Report.AXIS_SORT_ASCEND.getId())) {%> checked <%}%>>
				<fmt:message key="<%=Report.AXIS_SORT_ASCEND.getKey() %>" />
		</span>
		<span class="js_remove_chart_xsecondaxis"><a class='btn_x_gr' href='' title="<fmt:message key="report.button.remove_xsecondaxis" />">x</a></span>
	</td>
</tr>
 --%>
<tr class="js_chart_zaxis">
	<th width="200px"><fmt:message key="report.title.zaxis_mat" /></th>
	<td class="js_select_chart_axis" colspan="4">
		<select name="selReportZAxis">
			<%
			if (fields != null) {
				for (FormField field : fields) {
			%>
					<option type="<%=field.getType()%>" value="<%=field.getId()%>" 
						<%if (zAxisId != null && zAxisId.equals(field.getId())) {%> selected"<%}%>><%=field.getName()%>
					</option>
			<%
				}
			}
			%>
			<jsp:include page="/jsp/content/work/field/default_fields.jsp">
				<jsp:param name="workType" value="<%=CommonUtil.toNotNull(work.getType()) %>" />			
				<jsp:param name="fieldId" value="<%=CommonUtil.toNotNull(zAxisId) %>" />
			</jsp:include>
		</select>
		<span class="js_axis_selector_date" <%if (!zFieldType.equals(FormField.TYPE_DATE) && !zFieldType.equals(FormField.TYPE_DATETIME)) {%> style="display: none" <%}%>>
			<select name="selReportZAxisSelectorDate">
				<%
				for (KeyMap selector : Report.AXIS_SELECTORS_DATE) {
				%>
					<option value="<%=selector.getId()%>" <%if (zAxisSelector != null && zAxisSelector.equals(selector.getId())) {%> selected <%}%>>
						<fmt:message key="<%=selector.getKey() %>" />
					</option>
				<%
				}
				%>
			</select>
		</span>
		<span class="js_axis_selector_user" <%if (!zFieldType.equals(FormField.TYPE_USER)) {%> style="display: none" <%}%>>
			<select name="selReportZAxisSelectorUser">
				<%
				for (KeyMap selector : Report.AXIS_SELECTORS_USER) {
				%>
					<option value="<%=selector.getId()%>" <%if (zAxisSelector != null && zAxisSelector.equals(selector.getId())) {%> selected <%}%>>
						<fmt:message key="<%=selector.getKey() %>" />
					</option>
				<%
				}
				%>
			</select>
		</span>
		<span>
			<input type="radio" name="rdoReportZAxisSort" value="<%=Report.AXIS_SORT_DESCEND.getId()%>"
				<%if (zAxisSort == null || zAxisSort.equals(Report.AXIS_SORT_DESCEND.getId())) {%> checked <%}%>>
				<fmt:message key="<%=Report.AXIS_SORT_DESCEND.getKey() %>" />
			<input type="radio" name="rdoReportZAxisSort" value="<%=Report.AXIS_SORT_ASCEND.getId()%>"
				<%if (zAxisSort != null && zAxisSort.equals(Report.AXIS_SORT_ASCEND.getId())) {%> checked <%}%>>
				<fmt:message key="<%=Report.AXIS_SORT_ASCEND.getKey() %>" />
		</span>
	</td>
</tr>


<tr class="js_add_chart_zsecondaxis"  <%if (zSecondAxisId != null) {%> style="display: none" <%}%>>
	<td colspan="5">
		<a href=""><fmt:message key="report.button.add_zsecondaxis_mat" /></a>
	</td>
</tr>
<tr class="js_chart_zsecondaxis" <%if (SmartUtil.isBlankObject(zSecondAxisId)) {%> style="display: none" <%}%>>
	<th width="200px"><fmt:message key="report.title.zsecondaxis_mat" /></th>
	<td class="js_select_chart_axis">
		<select name="selReportZSecondAxis">
			<%
			if (fields != null) {
				for (FormField field : fields) {
			%>
					<option type="<%=field.getType()%>" value="<%=field.getId()%>" 
						<%if (zSecondAxisId != null && zSecondAxisId.equals(field.getId())) {%> selected"<%}%>><%=field.getName()%>
					</option>
			<%
				}
			}
			%>
			<jsp:include page="/jsp/content/work/field/default_fields.jsp">
				<jsp:param name="workType" value="<%=CommonUtil.toNotNull(work.getType()) %>" />			
				<jsp:param name="fieldId" value="<%=CommonUtil.toNotNull(zSecondAxisId) %>" />
			</jsp:include>
		</select>
		<span class="js_axis_selector_date" <%if (!zSecondFieldType.equals(FormField.TYPE_DATE) && !zSecondFieldType.equals(FormField.TYPE_DATETIME)) {%> style="display: none" <%}%>>
			<select name="selReportZSecondAxisSelectorDate">
				<%
				for (KeyMap selector : Report.AXIS_SELECTORS_DATE) {
				%>
					<option value="<%=selector.getId()%>" <%if (zSecondAxisSelector != null && zSecondAxisSelector.equals(selector.getId())) {%> selected <%}%>>
						<fmt:message key="<%=selector.getKey() %>" />
					</option>
				<%
				}
				%>
			</select>
		</span>
		<span class="js_axis_selector_user" <%if (!zSecondFieldType.equals(FormField.TYPE_USER)) {%> style="display: none" <%}%>>
			<select name="selReportZSecondAxisSelectorUser">
				<%
				for (KeyMap selector : Report.AXIS_SELECTORS_USER) {
				%>
					<option value="<%=selector.getId()%>" <%if (zSecondAxisSelector != null && zSecondAxisSelector.equals(selector.getId())) {%> selected <%}%>>
						<fmt:message key="<%=selector.getKey() %>" />
					</option>
				<%
				}
				%>
			</select>
		</span>
		<span>
			<input type="radio" name="rdoReportZSecondAxisSort" value="<%=Report.AXIS_SORT_DESCEND.getId()%>"
				<%if (zSecondAxisSort == null || zSecondAxisSort.equals(Report.AXIS_SORT_DESCEND.getId())) {%> checked <%}%>>
				<fmt:message key="<%=Report.AXIS_SORT_DESCEND.getKey() %>" /> 
			<input type="radio" name="rdoReportZSecondAxisSort" value="<%=Report.AXIS_SORT_ASCEND.getId()%>"
				<%if (zSecondAxisSort != null && zSecondAxisSort.equals(Report.AXIS_SORT_ASCEND.getId())) {%> checked <%}%>>
				<fmt:message key="<%=Report.AXIS_SORT_ASCEND.getKey() %>" />
		</span>
		<span class="js_remove_chart_zsecondaxis"><a class='btn_x_gr' href='' title="<fmt:message key="report.button.remove_zsecondaxis" />">x</a></span>
	</td>
</tr>

<tr>
	<th width="200px"><fmt:message key="report.title.yaxis_mat" /></th>
	<td colspan="4" class="">
		<select name="selReportYAxis">
			<%
			String yAxisId = null;
			if (fields != null) {
				if (matrix != null && matrix.getYAxis() != null)
					yAxisId = matrix.getYAxis().getId();
				for (FormField field : fields) {
			%>
					<option type="<%=field.getPageName()%>" value="<%=field.getId()%>" 
						<%if (yAxisId != null && yAxisId.equals(field.getId())) {%> selected <%}%>><%=field.getName()%>
					</option>
			<%
				}
			}
			%>
			<jsp:include page="/jsp/content/work/field/default_fields.jsp">
				<jsp:param name="workType" value="<%=CommonUtil.toNotNull(work.getType()) %>" />			
				<jsp:param name="fieldId" value="<%=CommonUtil.toNotNull(yAxisId) %>" />
			</jsp:include>
		</select>
		<span>
			<select name="selReportYAxisValue">
				<%
				String value = null;
				if (matrix != null && matrix.getValueType() != null)
					value = matrix.getValueType();
				for (KeyMap valueType : Report.VALUE_TYPES) {
				%>
					<option value="<%=valueType.getId()%>" <%if (value != null && value.equals(valueType.getId())) {%> selected <%}%>>
						<fmt:message key="<%=valueType.getKey() %>" />
					</option>
				<%
				}
				%>
			</select>
		</span>
	</td>	
</tr>
