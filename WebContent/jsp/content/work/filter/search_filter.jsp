<%@page import="java.net.URLEncoder"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.model.filter.KeyMap"%>
<%@page import="net.smartworks.model.filter.ConditionOperator"%>
<%@page import="net.smartworks.model.filter.Condition"%>
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
	String companyId = (String) session.getAttribute("companyId");
	String userId = (String) session.getAttribute("userId");

	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	String workId = request.getParameter("workId");
	String filterId = request.getParameter("filterId");

	User cUser = SmartUtil.getCurrentUser();
	InformationWork work = (InformationWork) smartWorks.getWorkById(companyId, workId);
	FormField[] fields = null;
	SearchFilter filter = null;
	if ((work != null) && (work.getForm() != null))
		fields = work.getForm().getFields();
	if (work != null && filterId != null)
		filter = work.getSearchFilterById(filterId, cUser);
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />


<!-- 상세 필터 -->
<div class="filter_group">
	<table>
		<tr>
			<td>
				<%
					if (fields != null && filter == null) {
				%>
				<form name="frmNewSearchFilter" class="filter_area">
					<select name="selFilterLeftOperand"
						class="js_select_filter_operand">
						<%
							for (FormField field : fields) {
						%>
						<option title="<%=field.getPageName()%>"
							value="<%=field.getId()%>"><%=field.getName()%></option>
						<%
							}
						%>
						<option title="<%=FormField.FIELD_OWNER.getPageName()%>"
							value="<%=FormField.ID_OWNER%>">
							<fmt:message key='common.title.owner' />
						</option>
						<option title="<%=FormField.FIELD_CREATED_DATE.getPageName()%>"
							value="<%=FormField.ID_CREATED_DATE%>">
							<fmt:message key='common.title.created_date' />
						</option>
						<option title="<%=FormField.FIELD_LAST_MODIFIER.getPageName()%>"
							value="<%=FormField.ID_LAST_MODIFIER%>">
							<fmt:message key='common.title.last_modifier' />
						</option>
						<option
							title="<%=FormField.FIELD_LAST_MODIFIED_DATE.getPageName()%>"
							value="<%=FormField.ID_LAST_MODIFIED_DATE%>">
							<fmt:message key='common.title.last_modified_date' />
						</option>
					</select> <span class="js_filter_operator"> <%
 	String fieldType = fields[0].getType();
 		if (fieldType.equals(FormField.TYPE_TEXT) || fieldType.equals(FormField.TYPE_RICHTEXT_EDITOR) || fieldType.equals(FormField.TYPE_COMBO)
 				|| fieldType.equals(FormField.TYPE_IMAGE) || fieldType.equals(FormField.TYPE_EMAIL)) {
 %> <jsp:include page="/jsp/content/work/filter/string_field.jsp"></jsp:include>
						<%
							} else if (fieldType.equals(FormField.TYPE_NUMBER) || fieldType.equals(FormField.TYPE_CURRENCY) || fieldType.equals(FormField.TYPE_PERCENT)) {
						%> <jsp:include page="/jsp/content/work/filter/number_field.jsp"></jsp:include>
						<%
							} else if (fieldType.equals(FormField.TYPE_USER)) {
						%> <jsp:include page="/jsp/content/work/filter/user_field.jsp"></jsp:include>
						<%
							} else if (fieldType.equals(FormField.TYPE_FILE)) {
						%> <jsp:include page="/jsp/content/work/filter/file_field.jsp"></jsp:include>
						<%
							} else if (fieldType.equals(FormField.TYPE_OTHER_WORK)) {
						%> <jsp:include page="/jsp/content/work/filter/work_field.jsp"></jsp:include>
						<%
							} else if (fieldType.equals(FormField.TYPE_CHECK_BOX)) {
						%> <jsp:include page="/jsp/content/work/filter/boolean_field.jsp"></jsp:include>
						<%
							} else if (fieldType.equals(FormField.TYPE_DATE)) {
						%> <jsp:include page="/jsp/content/work/filter/date_field.jsp"></jsp:include>
						<%
							} else if (fieldType.equals(FormField.TYPE_TIME)) {
						%> <jsp:include page="/jsp/content/work/filter/time_field.jsp"></jsp:include>
						<%
							} else if (fieldType.equals(FormField.TYPE_DATETIME)) {
						%> <jsp:include page="/jsp/content/work/filter/datetime_field.jsp"></jsp:include>
						<%
							} else {
						%> <jsp:include page="/jsp/content/work/filter/string_field.jsp"></jsp:include>
						<%
							}
						%> </span>
				</form> <%
 	} else if (fields != null && filter != null) {
 		Condition[] conditions = filter.getConditions();
 		if (conditions != null) {
 			for (Condition condition : conditions) {
 				FormField leftOperand = condition.getLeftOperand();
 				String operator = condition.getOperator();
 				Object rightOperand = condition.getRightOperand();
 %>
				<form name="frmExistingSearchFilter" class="filter_area">
					<select name="selFilterLeftOperand"
						class="js_select_filter_operand">
						<%
							for (FormField field : fields) {
						%>
						<option title="<%=field.getPageName()%>"
							value="<%=field.getId()%>"
							<%if (leftOperand.getId().equals(field.getId())) {%> selected
							<%}%>><%=field.getName()%></option>
						<%
							}
						%>
						<option title="<%=FormField.FIELD_OWNER.getPageName()%>"
							value="<%=FormField.ID_OWNER%>"
							<%if (leftOperand.getId().equals(FormField.ID_OWNER)) {%>
							selected <%}%>>
							<fmt:message key='common.title.owner' />
						</option>
						<option title="<%=FormField.FIELD_CREATED_DATE.getPageName()%>"
							value="<%=FormField.ID_CREATED_DATE%>"
							<%if (leftOperand.getId().equals(FormField.ID_CREATED_DATE)) {%>
							selected <%}%>>
							<fmt:message key='common.title.created_date' />
						</option>
						<option title="<%=FormField.FIELD_LAST_MODIFIER.getPageName()%>"
							value="<%=FormField.ID_LAST_MODIFIER%>"
							<%if (leftOperand.getId().equals(FormField.ID_LAST_MODIFIER)) {%>
							selected <%}%>>
							<fmt:message key='common.title.last_modifier' />
						</option>
						<option
							title="<%=FormField.FIELD_LAST_MODIFIED_DATE.getPageName()%>"
							value="<%=FormField.ID_LAST_MODIFIED_DATE%>"
							<%if (leftOperand.getId().equals(FormField.ID_LAST_MODIFIED_DATE)) {%>
							selected <%}%>>
							<fmt:message key='common.title.last_modified_date' />
						</option>
					</select> <span class="js_filter_operator"> <%
 	String fieldType = leftOperand.getType();
 				if (fieldType.equals(FormField.TYPE_TEXT) || fieldType.equals(FormField.TYPE_RICHTEXT_EDITOR) || fieldType.equals(FormField.TYPE_COMBO)
 						|| fieldType.equals(FormField.TYPE_IMAGE) || fieldType.equals(FormField.TYPE_EMAIL)) {
  					String operandValue = URLEncoder.encode((String)rightOperand, "UTF-8");
 %> <jsp:include page="/jsp/content/work/filter/string_field.jsp">
							<jsp:param name="operator" value="<%=operator%>" />
							<jsp:param name="operandValue" value="<%=operandValue%>" />
						</jsp:include> <%
 	} else if (fieldType.equals(FormField.TYPE_NUMBER) || fieldType.equals(FormField.TYPE_CURRENCY)
 						|| fieldType.equals(FormField.TYPE_PERCENT)) {
			String operandValue = (String)rightOperand;
 %> <jsp:include page="/jsp/content/work/filter/number_field.jsp"><jsp:param
								name="operator" value="<%=operator%>" /><jsp:param
								name="operandValue" value="<%=operandValue %>" /></jsp:include> <%
 	} else if (fieldType.equals(FormField.TYPE_USER)) {
 		String operandValue = URLEncoder.encode(((User)rightOperand).getLongName(), "UTF-8");
 %> <jsp:include page="/jsp/content/work/filter/user_field.jsp"><jsp:param
								name="operator" value="<%=operator%>" /><jsp:param
								name="operandValue" value="<%=operandValue %>" /><jsp:param
								name="operandId" value="<%=((User)rightOperand).getId()%>" /></jsp:include> <%
 	} else if (fieldType.equals(FormField.TYPE_FILE)) {
			String operandValue = URLEncoder.encode((String)rightOperand, "UTF-8");
 %> <jsp:include page="/jsp/content/work/filter/file_field.jsp"><jsp:param
								name="operator" value="<%=operator%>" /><jsp:param
								name="operandValue" value="<%=operandValue %>" /></jsp:include> <%
 	} else if (fieldType.equals(FormField.TYPE_OTHER_WORK)) {
			String operandValue = URLEncoder.encode(((Work)rightOperand).getName(), "UTF-8");
 %> <jsp:include page="/jsp/content/work/filter/work_field.jsp"><jsp:param
								name="operator" value="<%=operator%>" /><jsp:param
								name="operandValue" value="<%=operandValue %>" /><jsp:param
								name="operandId" value="<%=rightOperand%>" /></jsp:include> <%
 	} else if (fieldType.equals(FormField.TYPE_CHECK_BOX)) {
		String operandValue = URLEncoder.encode((String)rightOperand, "UTF-8");
 %> <jsp:include page="/jsp/content/work/filter/boolean_field.jsp"><jsp:param
								name="operator" value="<%=operator%>" /><jsp:param
								name="operandValue" value="<%=operandValue %>" /></jsp:include> <%
 	} else if (fieldType.equals(FormField.TYPE_DATE)) {
 %> <jsp:include page="/jsp/content/work/filter/date_field.jsp"><jsp:param
								name="operator" value="<%=operator%>" /><jsp:param
								name="operandValue" value="<%=((LocalDate)rightOperand).toLocalDateSimpleString() %>" /></jsp:include> <%
 	} else if (fieldType.equals(FormField.TYPE_TIME)) {
 %> <jsp:include page="/jsp/content/work/filter/time_field.jsp"><jsp:param
								name="operator" value="<%=operator%>" /><jsp:param
								name="operandValue" value="<%=((LocalDate)rightOperand).toLocalTimeShortString() %>" /></jsp:include> <%
 	} else if (fieldType.equals(FormField.TYPE_DATETIME)) {
 %> <jsp:include page="/jsp/content/work/filter/datetime_field.jsp"><jsp:param
								name="operator" value="<%=operator%>" /><jsp:param
								name="operandValue" value="<%=((LocalDate)rightOperand).toLocalDateSimpleString()%>" /><jsp:param
								name="operandValueSecond" value="<%=((LocalDate)rightOperand).toLocalTimeShortString() %>" /></jsp:include> <%
 	} else {
		String operandValue = URLEncoder.encode((String)rightOperand, "UTF-8");
 %> <jsp:include page="/jsp/content/work/filter/string_field.jsp"><jsp:param
								name="operator" value="<%=operator%>" /><jsp:param
								name="operandValue" value="<%=operandValue%>" /></jsp:include> <%
 	}
 %> </span>
				</form> <%
 	}

 		}
 	}
 %>
			</td>

			<td class="btn_plus"><img src="images/btn_plus.gif" />
			</td>

		</tr>
	</table>

	<div class="filter_btn_space">
		<div class="float_right">
			<span class="btn_wh"> <span class="Btn01Start"></span> <span
				class="Btn01Center">필터저장</span> <span class="Btn01End"></span> </span> <span
				class="btn_wh"> <span class="Btn01Start"></span> <span
				class="Btn01Center">필터실행</span> <span class="Btn01End"></span> </span>

		</div>
	</div>

</div>
