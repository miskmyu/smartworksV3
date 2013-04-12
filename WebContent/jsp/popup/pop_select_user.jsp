<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.model.work.info.WorkInfo"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ page import="net.smartworks.model.work.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	User cUser = SmartUtil.getCurrentUser();

	boolean isMultiSelectable = false;
	isMultiSelectable = Boolean.parseBoolean(request.getParameter("multiUsers"));
%>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!--  전체 레이아웃 -->
	<!-- 팝업 컨텐츠 -->
	<div class="pop_list_area">
		<%if(isMultiSelectable){ %>
			<form class="pop_list_area mail_address" name="frmUserSelections">	
				<ul>
					<li class="js_drill_down">
						<div>
							<a href="pop_userlist_by_depart.sw?multiUsers=<%=isMultiSelectable %>" departmentId="<%=cUser.getCompanyId()%>" class="js_popup js_expandable">
								<span class="btn_tree_plus vm"></span>
								<span><fmt:message key="organization.title.tree"/></span>
							</a>
						</div>
						<div style="display: none" class="menu_2dep js_drill_down_target"></div>
					</li>
					<li class="js_drill_down">
						<div>
							<a href="pop_userlist_by_group.sw?multiUsers=<%=isMultiSelectable %>" groupId="<%=cUser.getCompanyId()%>" class="js_popup js_expandable">
								<span class="btn_tree_plus vm"></span>
								<span><fmt:message key="group.title.tree"/></span>
							</a>
						</div>
						<div style="display: none" class="menu_2dep js_drill_down_target"></div>
					</li>
				</ul>
			</form>
		<%}else{ %>
			<jsp:include page="/jsp/popup/pop_userlist_by_depart.jsp"></jsp:include>
		<%} %>

	<!-- 하단버튼영역 -->
 	<%
 	if(isMultiSelectable){
 	%>
		<div class="glo_btn_space">
	
			<div class="fr">
				<span class="btn_gray"> <a class="js_pop_select_users" href=""> <span
				class="txt_btn_start"></span> <span class="txt_btn_center"><fmt:message key="common.button.complete"/></span> <span
				class="txt_btn_end"></span> </a> </span>
				
				<span class="btn_gray"> <a href="" onclick="$.modal.close();return false;"> <span
				class="txt_btn_start"></span> <span class="txt_btn_center"><fmt:message key="common.button.cancel"/></span> <span
				class="txt_btn_end"></span> </a> </span>
			</div>
	
		</div>
	<%
	}
	%>
	<!-- 하단버튼영역 //-->
	
		</div>
	<!-- 팝업 컨텐츠 //-->

 	
<!-- 전체 레이아웃//-->