<%@page import="net.smartworks.model.work.info.SmartWorkInfo"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.model.work.info.WorkInfo"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ page import="net.smartworks.model.work.*"%>
<%
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	WorkInfo[] works = smartWorks.getMyAllWorksByCategoryId(request.getParameter("categoryId"));
	String iconType = null;
	String classType = "js_pop_select_work";
	String targetContent = null;
%>

<ul>
	<%
		if (works != null) {
			for (WorkInfo work : works) {
				if (work.getType() == SmartWork.TYPE_PROCESS) {
					iconType = "icon_pworks";
					targetContent = "start_pwork.sw";
				} else if (work.getType() == SmartWork.TYPE_INFORMATION) {
					iconType = "icon_iworks";
					targetContent = "new_iwork.sw";
				} else if (work.getType() == SmartWork.TYPE_SCHEDULE) {
					iconType = "icon_sworks";
					targetContent = "plan_swork.sw";
				} else if (work.getType() == WorkCategory.TYPE_CATEGORY) {
					iconType = "icon_gworks";
					targetContent = "pop_worklist_by_category.sw";
				}
				if (work.getType() != WorkCategory.TYPE_CATEGORY) {
					String fullpathName = ((SmartWorkInfo)work).getFullpathName();
	%>
	<li>
		<span class="dep">
			<a href="<%=targetContent%>?workId=<%=work.getId()%>" class="<%=classType%>" workId="<%=work.getId()%>" fullpathName="<%=fullpathName%>" iconClass="<%=work.getIconClass()%>" workType=<%=work.getType() %>><span class="<%=work.getIconClass()%>"></span><%=work.getName()%></a>
		</span>
	</li>
	<%
		} else {
	%>
	<li class="js_drill_down">
		<span class="dep">
			<a href="<%=targetContent%>" categoryId="<%=work.getId()%>" class="js_popup"><span class="<%=work.getIconClass()%>"></span>
			<span><%=work.getName()%></span>
			</a>
		</span>
		<div style="display: none" class="menu_2dep js_drill_down_target"></div></li>
	<%
		}
	%>
	<%
		}
		}
	%>
</ul>