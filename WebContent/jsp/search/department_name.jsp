<%@page import="net.smartworks.model.community.info.DepartmentInfo"%>
<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.model.community.info.CommunityInfo"%>
<%@page import="net.smartworks.model.community.info.WorkSpaceInfo"%>
<%@ page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ page import="net.smartworks.model.community.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	User cUser = SmartUtil.getCurrentUser();

	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	String key = request.getParameter("key");
	DepartmentInfo[] departments = smartWorks.searchDepartment(key, request);
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<ul>
	<%
	if(departments != null){
		for (DepartmentInfo department : departments) {
			String picName = department.getMinPicture();
			String comContext = ISmartWorks.CONTEXT_PREFIX_DEPARTMENT_SPACE + department.getId();
			String comName = department.getFullpathName();
			String comId = department.getId();
	%>
				<li>
					<a comName="<%=comName%>" comId="<%=comId %>" class="js_select_community">
						<img src="<%=picName%>" class="profile_size_s"><%=comName%></a>
				</li>
	<%
		}
	}else{
	%>
		<li><span><fmt:message key="search.message.no_searched_data"/></span></li>
	<%
	}
	%>
</ul>
