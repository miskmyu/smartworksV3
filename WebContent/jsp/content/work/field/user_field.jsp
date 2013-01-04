
<!-- Name 			: user_field.jsp											 -->
<!-- Description	: 업무목록 페이지들의 상세필터에서 조건이 사용자인 항목 화면 			 -->
<!-- Author			: Maninsoft, Inc.											 -->
<!-- Created Date	: 2011.9.													 -->

<%@page import="net.smartworks.model.work.FormField"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.model.KeyMap"%>
<%@page import="net.smartworks.model.filter.ConditionOperator"%>
<%@page import="net.smartworks.model.work.SmartWork"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 현재사용자 정보도 가져온다..
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();

	// 호출시에 전달한 operator, operandValue, operandValueSecond 값들을 가져온다..
	String operator = request.getParameter("operator"); 
	String operandValue = request.getParameter("operandValue");
	String operandValueSecond = request.getParameter("operandValueSecond");

	// 사용자이름에 있는 스페이스등 때문에 엔코딩 디코딩이 필요함...
	if(operandValue != null) operandValue = URLDecoder.decode(operandValue, "UTF-8");
	String operandId = request.getParameter("operandId");

	// 이름 비교에 사용되는 계산자들의 키맵을 가져온다..
	KeyMap[] stringOpers = ConditionOperator.stringOperators;
%>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<input name="hdnFieldType" type="hidden" value="<%=FormField.TYPE_USER %>"/>
<select name="selFilterOperator" class="selb_size_sec">
	<%
	for (KeyMap stringOper : stringOpers) {
	%>
		<option value="<%=stringOper.getId()%>" <%if (operator != null && operator.equals(stringOper.getId())) {%> selected <%}%>>
			<fmt:message key="<%=stringOper.getKey() %>" />
		</option>
	<%
	}
	%>
</select>

<!-- 사용자를 선택하는 입력화면 -->
<span class="str_field">
	<div class="fl w100 js_type_userField"> 
		<div class="icon_fb_space"> 
			<div class='fieldline community_names js_community_names sw_required'>
				<div class="js_selected_communities user_sel_area">
					<%if (operandValue != null) {%>
						<span class='js_community_item user_select' comId='<%=operandValueSecond%>' comName='<%=operandValue%>'><%=operandValue%>
							<a class='js_remove_community' href=''> x</a>
						</span>
					<%}%>
				</div>
				<input class="m0 js_auto_complete" style="width:100px" href="user_name.sw" type="text">
				<input name="txtFilterStringOperand" type="hidden" value="<%=operandId%>">
			</div>
			<div class="js_community_list com_list" style="display: none"></div>
			<span class="js_community_popup"></span>
			<a href="" class="js_userpicker_button"><span class="icon_fb_user"></span></a>
		</div>
	</div>
</span>

<!--  현재 콘디션을 삭제할 수 있는 샂제 버튼 -->
<span class="btn_delete_posi">
	<span class="btn_delete js_remove_condition"></span> 
</span>
