<%@page import="net.smartworks.model.community.info.DepartmentInfo"%>
<%@page import="net.smartworks.model.community.Department"%>
<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.model.community.info.CommunityInfo"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.model.work.info.WorkInfo"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ page import="net.smartworks.model.work.*"%>
<%
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	String departmentId = (String)request.getParameter("departmentId");
	CommunityInfo[] departments = (CommunityInfo[])smartWorks.getAllComsByDepartmentId(CommonUtil.toNotNull(departmentId), true);
	String iconType = null;
%>

<ul>
	<%
	if (!SmartUtil.isBlankObject(departments)) {
		for (CommunityInfo community : departments) {
			DepartmentInfo department = (DepartmentInfo)community;
			iconType = "btn_tree_plus fn vm";
		%>
			<li class="js_drill_down">
				<span class="dep">
					<a href="pop_departlist_by_depart.sw?multiUsers=false" departmentId="<%=department.getId()%>" class="js_popup js_expandable">
						<span class="<%=iconType%>"></span>
						<a href="" class="js_pop_select_depart" deaprtId="<%=department.getId()%>" departName="<%=department.getFullpathName()%>" departPicture="<%=department.getMinPicture()%>">
							<span> <%=department.getName()%> </span>
						</a>
					</a>
				</span>
				<div style="display: none" class="menu_2dep js_drill_down_target"></div>
			</li>
	<%
		}
	}
	%>
</ul>