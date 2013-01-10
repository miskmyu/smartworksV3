<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.model.work.info.WorkInfo"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ page import="net.smartworks.model.work.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	User cUser = SmartUtil.getCurrentUser();

%>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!--  전체 레이아웃 -->
	<!-- 팝업 컨텐츠 -->
		<div class="pop_list_area mail_address">
			<ul>
				<li class="js_drill_down">
					<div>
						<a href="pop_departlist_by_depart.sw?multiUsers=false" departmentId="<%=cUser.getCompanyId()%>" class="js_popup js_expandable">
							<span class="btn_tree_plus vm"></span>
							<a href="" class="js_pop_select_depart" departId="<%=cUser.getCompanyId()%>" departName="<%=cUser.getCompany()%>">
								<span><%=cUser.getCompany() %></span>
							</a>
						</a>
					</div>
					<div style="display: none" class="menu_2dep js_drill_down_target"></div>
				</li>
			</ul>
		</div>
	<!-- 팝업 컨텐츠 //--> 	
<!-- 전체 레이아웃//-->