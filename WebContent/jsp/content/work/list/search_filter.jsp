<%@page import="net.smartworks.model.work.SocialWork"%>
<%@page import="net.smartworks.server.engine.infowork.form.model.SwfFormModel"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="com.sun.xml.internal.txw2.Document"%>
<%@page import="net.smartworks.model.work.ProcessWork"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.model.KeyMap"%>
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
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	String filterId = request.getParameter("filterId");
	User cUser = SmartUtil.getCurrentUser();
	SmartWork work = (SmartWork)session.getAttribute("smartWork");
	String workId = work.getId();

	String filterName = "";
	FormField[] fields = null;
	SearchFilter filter = null;
	if ((work != null) && (work.getClass().equals(InformationWork.class))) {
		InformationWork informationWork = (InformationWork) work;
		if (informationWork.getForm() != null) {
			fields = informationWork.getForm().getFields();
		}
	} else if((work != null) && (work.getClass().equals(ProcessWork.class))) {
		fields = FormField.DEFAULT_PROCESS_FIELDS;
	} else{
		fields = new FormField[] {};		
	}
	
	int workType = work.getType();
	if(work.getId().equals(SmartWork.ID_FILE_MANAGEMENT)) workType = SocialWork.TYPE_FILE;
	String formType = "";
	if (work != null && !SmartUtil.isBlankObject(filterId)) {
		if(work.getClass().equals(InformationWork.class))
			formType = SwfFormModel.TYPE_SINGLE;
		else if(work.getClass().equals(ProcessWork.class))
			formType = SwfFormModel.TYPE_PROCESS;
		filter = smartWorks.getSearchFilterById(formType, workId, filterId);
	}
	if(!filterId.equals(SearchFilter.FILTER_ALL_INSTANCES) && !filterId.equals(SearchFilter.FILTER_MY_INSTANCES) && !filterId.equals(SearchFilter.FILTER_RECENT_INSTANCES) &&
			!filterId.equals(SearchFilter.FILTER_MY_RECENT_INSTANCES) && !filterId.equals(SearchFilter.FILTER_MY_RUNNING_INSTANCES))
		filterName = CommonUtil.toNotNull(filter.getName());
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />


<!-- 상세 필터 -->
<div class="filter_group js_search_filter_page" filterId="<%=filterId%>" workType="<%=workType%>" workId="<%=workId%>">
	<table>
		<tr>
			<td>
				<form name="frmSearchFilter" style="display: none"
					class="filter_area js_filter_condition js_new_condition js_validation_required">
					<select name="selFilterLeftOperand"
						class="selb_size_fir js_select_filter_operand">
						<%
						if (fields != null) {
							for (FormField field : fields) {
						%>
							<option page="<%=field.getPageName() %>" type="<%=field.getType()%>" value="<%=field.getId()%>"><%=field.getName()%></option>
						<%
							}
						}
						%>
						<jsp:include page="/jsp/content/work/field/default_fields.jsp">
							<jsp:param name="workType" value="<%=workType %>" />
						</jsp:include>
					</select> 
					<span class="js_filter_operator">
					<%
				 	if (!SmartUtil.isBlankObject(fields)) {
				 		String fieldType = fields[0].getType();
				 		if (fieldType.equals(FormField.TYPE_TEXT) || fieldType.equals(FormField.TYPE_RICHTEXT_EDITOR) || fieldType.equals(FormField.TYPE_IMAGE)
				 					|| fieldType.equals(FormField.TYPE_EMAIL)) {
 					%> 
 							<jsp:include page="/jsp/content/work/field/string_field.jsp"></jsp:include>
						<%
						} else if (fieldType.equals(FormField.TYPE_NUMBER) || fieldType.equals(FormField.TYPE_CURRENCY) || fieldType.equals(FormField.TYPE_PERCENT)) {
						%> 
							<jsp:include page="/jsp/content/work/field/number_field.jsp"></jsp:include>
						<%
						} else if (fieldType.equals(FormField.TYPE_USER)) {
						%> 
							<jsp:include page="/jsp/content/work/field/user_field.jsp"></jsp:include>
						<%
						} else if (fieldType.equals(FormField.TYPE_FILE)) {
						%> 
							<jsp:include page="/jsp/content/work/field/file_field.jsp"></jsp:include>
						<%
						} else if (fieldType.equals(FormField.TYPE_OTHER_WORK)) {
						%> 
							<jsp:include page="/jsp/content/work/field/work_field.jsp"></jsp:include>
						<%
						} else if (fieldType.equals(FormField.TYPE_CHECK_BOX)) {
						%> 
							<jsp:include page="/jsp/content/work/field/boolean_field.jsp"></jsp:include>
						<%
						} else if (fieldType.equals(FormField.TYPE_DATE)) {
						%> 
							<jsp:include page="/jsp/content/work/field/date_field.jsp"></jsp:include>
						<%
						} else if (fieldType.equals(FormField.TYPE_TIME)) {
						%> 
							<jsp:include page="/jsp/content/work/field/time_field.jsp"></jsp:include>
						<%
						} else if (fieldType.equals(FormField.TYPE_DATETIME)) {
						%> 
							<jsp:include page="/jsp/content/work/field/datetime_field.jsp"></jsp:include>
						<%
						} else if (fieldType.equals(FormField.TYPE_COMBO)) {
						%> 
							<jsp:include page="/jsp/content/work/field/combo_field.jsp"></jsp:include>
						<%
						} else {
						%> 
							<jsp:include page="/jsp/content/work/field/string_field.jsp"></jsp:include>
					<%
						}
					} else {
					%>
						<jsp:include page="/jsp/content/work/field/string_field.jsp"></jsp:include>
					<%
					}
					%> 
					</span>
				</form> 
				<%
			 	if (fields != null && filter != null) {
			 		Condition[] conditions = filter.getConditions();
			 		if (conditions != null) {
			 			for (Condition condition : conditions) {
			 				FormField leftOperand = condition.getLeftOperand();
			 				String operator = condition.getOperator();
			 				Object rightOperand = condition.getRightOperand();
				 %>
							<form name="frmSearchFilter" class="filter_area js_filter_condition">
								<select name="selFilterLeftOperand" class="selb_size_fir js_select_filter_operand">
									<%
									for (FormField field : fields) {
									%>
										<option page="<%=field.getPageName() %>" type="<%=field.getType()%>" value="<%=field.getId()%>"
									<%if (leftOperand.getId().equals(field.getId())) {%> selected <%}%>><%=field.getName()%></option>
									<%
									}
									%>
									<jsp:include page="/jsp/content/work/field/default_fields.jsp">
										<jsp:param name="workType" value="<%=workType %>" />
										<jsp:param name="fieldId" value="<%=leftOperand.getId() %>" />
									</jsp:include>
								</select> 
								<span class="js_filter_operator"> 
									<%
					 				String fieldType = leftOperand.getType();
					 				if (fieldType.equals(FormField.TYPE_TEXT) || fieldType.equals(FormField.TYPE_RICHTEXT_EDITOR) || fieldType.equals(FormField.TYPE_IMAGE)
					 						|| fieldType.equals(FormField.TYPE_EMAIL)) {
					 					String operandValue = URLEncoder.encode((String) rightOperand, "UTF-8");
	 								%> 
	 									<jsp:include page="/jsp/content/work/field/string_field.jsp">
											<jsp:param name="operator" value="<%=operator%>" />
											<jsp:param name="operandValue" value="<%=operandValue%>" />
										</jsp:include> 
									<%
					 				} else if (fieldType.equals(FormField.TYPE_NUMBER) || fieldType.equals(FormField.TYPE_CURRENCY)
					 						|| fieldType.equals(FormField.TYPE_PERCENT)) {
	 								%> 
	 									<jsp:include page="/jsp/content/work/field/number_field.jsp">
	 										<jsp:param name="operator" value="<%=operator%>" />
	 										<jsp:param name="operandValue" value="<%=rightOperand %>" />
	 									</jsp:include> 
	 								<%
					 				} else if (fieldType.equals(FormField.TYPE_USER)) {
					 					String operandValue = URLEncoder.encode(((User) rightOperand).getLongName(), "UTF-8");
					 					String operandValueSecond = ((User) rightOperand).getId();
	 								%> 
	 									<jsp:include page="/jsp/content/work/field/user_field.jsp">
	 										<jsp:param name="operator" value="<%=operator%>" />
	 										<jsp:param name="operandValue" value="<%=operandValue %>" />
	 										<jsp:param name="operandValueSecond" value="<%=operandValueSecond %>" />
	 										<jsp:param name="operandId" value="<%=((User)rightOperand).getId()%>" />
	 									</jsp:include> 
	 								<%
					 				} else if (fieldType.equals(FormField.TYPE_FILE)) {
					 					String operandValue = URLEncoder.encode((String) rightOperand, "UTF-8");
	 								%> 
	 									<jsp:include page="/jsp/content/work/field/file_field.jsp">
	 										<jsp:param name="operator" value="<%=operator%>" />
	 										<jsp:param name="operandValue" value="<%=operandValue %>" />
	 									</jsp:include> 
	 								<%
					 				} else if (fieldType.equals(FormField.TYPE_OTHER_WORK)) {
					 					String operandValue = URLEncoder.encode(((Work) rightOperand).getName(), "UTF-8");
									 %> 
									 	<jsp:include page="/jsp/content/work/field/work_field.jsp">
									 		<jsp:param name="operator" value="<%=operator%>" />
									 		<jsp:param name="operandValue" value="<%=operandValue %>" />
									 		<jsp:param name="operandId" value="<%=rightOperand%>" />
									 	</jsp:include>
									<%
					 				} else if (fieldType.equals(FormField.TYPE_CHECK_BOX)) {
									%>
										<jsp:include page="/jsp/content/work/field/boolean_field.jsp">
											<jsp:param name="operator" value="<%=operator%>" />
											<jsp:param name="operandValue" value="<%=rightOperand %>" />
										</jsp:include> 
									<%
					 				} else if (fieldType.equals(FormField.TYPE_DATE)) {
					 					String dateValue=null;
					 					if (rightOperand != null) {
					 						dateValue = ((LocalDate) rightOperand).toLocalDateSimpleString();
					 					}
	 								%> 
	 									<jsp:include page="/jsp/content/work/field/date_field.jsp">
	 										<jsp:param name="operator" value="<%=operator%>" />
											<jsp:param name="operandValue" value="<%=dateValue %>" />
										</jsp:include> 
									<%
					 				} else if (fieldType.equals(FormField.TYPE_TIME)) {
					 					String timeValue=null;
					 					if (rightOperand != null) {
					 						timeValue = ((LocalDate) rightOperand).toLocalTimeShortString();
					 					}
	 								%> 
	 									<jsp:include page="/jsp/content/work/field/time_field.jsp">
	 										<jsp:param name="operator" value="<%=operator%>" />
											<jsp:param name="operandValue" value="<%=timeValue %>" />
										</jsp:include> 
									<%
					 				} else if (fieldType.equals(FormField.TYPE_DATETIME)) {
					 					String dateValue=null;
					 					String timeValue=null;
					 					if (rightOperand != null) {
					 						dateValue = ((LocalDate) rightOperand).toLocalDateSimpleString();
					 						timeValue = ((LocalDate) rightOperand).toLocalTimeShortString();
					 					}
	 								%> 
	 									<jsp:include page="/jsp/content/work/field/datetime_field.jsp">
	 										<jsp:param name="operator" value="<%=operator%>" />
											<jsp:param name="operandValue" value="<%=dateValue%>" />
											<jsp:param name="operandValueSecond" value="<%=timeValue %>" />
										</jsp:include> 
									<%
					 				} else if (fieldType.equals(FormField.TYPE_COMBO)) {
	 								%> 
	 									<jsp:include page="/jsp/content/work/field/combo_field.jsp">
	 										<jsp:param name="operator" value="<%=operator%>" />
	 										<jsp:param name="operandValue" value="<%=rightOperand %>" />
	 									</jsp:include> 
	 								<%
					 				} else {
					 					String operandValue = URLEncoder.encode((String) rightOperand, "UTF-8");
	 								%> 
	 									<jsp:include page="/jsp/content/work/field/string_field.jsp">
	 										<jsp:param name="operator" value="<%=operator%>" />
	 										<jsp:param name="operandValue" value="<%=operandValue%>" />
	 									</jsp:include> 
	 								<%
	 								}
	 								%> 
	 							</span>
							</form> 
				<%
			 			}
			 		}
			 	}
			 	%>
			</td>
			
			<!-- 추가 버튼  -->
			<td class="btn_plus_space vb">
				<span class="btn_plus js_add_condition" /> </span>
			</td>
		</tr>
	</table>
	<div class="glo_btn_space">
		<div class="fr">
 			<%
 			if(!(!SmartUtil.isBlankObject(filter) && filter.isSystemFilter())){
			%>
				<span class="btn_gray"> 
					<a href="" class="js_search_filter_save"> 
						<span class="txt_btn_start"></span> 
						<span class="txt_btn_center"><fmt:message key="common.button.save_as"/></span> 
						<span class="txt_btn_end"></span>
					</a> 
				</span> 
			<%
			}
			%>
			<span class="btn_gray"> 
				<a href="" class="js_search_filter_saveas"> 
					<span class="txt_btn_start"></span> 
					<span class="txt_btn_center"><fmt:message key="common.button.save"/></span> 
					<span class="txt_btn_end"></span>
				</a> 
			</span>
 			<%
 			if(!(!SmartUtil.isBlankObject(filter) && filter.isSystemFilter())){
			%>
				<span class="btn_gray"> 
					<a href="" class="js_search_filter_delete"> 
						<span class="txt_btn_start"></span> 
						<span class="txt_btn_center"><fmt:message key="common.button.delete"/></span> 
						<span class="txt_btn_end"></span>
					</a> 
				</span> 
			<%
			}
			%>
	
			<span class="btn_gray"> 
				<a href="" class="js_search_filter_execute"> 
					<span class="txt_btn_start"></span> 
					<span class="txt_btn_center"><fmt:message key="common.button.execute"/></span> 
					<span class="txt_btn_end"></span>
				</a> 
			</span> 
			<span class="btn_gray ml5">
				<a href="" class="js_search_filter_close"> 
					<span class="txt_btn_start"></span> 
					<span class="txt_btn_center"><fmt:message key="common.button.cancel"/></span> 
					<span class="txt_btn_end"></span> 
				</a> 
			</span>
		</div>
		<form name="frmSearchFilterActions" class="fr pr10 js_validation_required">
			<input class="fieldline" style="width:160px; line-height: 16px" type="text" name="txtNewFilterName" value="<%=filterName %>" />
		</form>
		
		<!--  실행시 표시되는 프로그래스아이콘을 표시할 공간 -->
		<div class="fr form_space js_progress_span" ></div>
		
		<!-- 실행시 데이터 유효성 검사이상시 에러메시지를 표시할 공간 -->
		<span class="form_space sw_error_message js_filter_error_message" style="text-align:right; color: red"></span>
		
	</div>
</div>
