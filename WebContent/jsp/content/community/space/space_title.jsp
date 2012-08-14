<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.model.community.Group"%>
<%@page import="net.smartworks.model.community.Department"%>
<%@page import="net.smartworks.model.community.WorkSpace"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 현재사용자 정보도 가져온다..
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();	
	WorkSpace workSpace = (WorkSpace)session.getAttribute("workSpace");
%>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

	<%
	if(workSpace.getClass().equals(User.class)){
		User user = (User)workSpace;
	%>
		<!-- Title -->
		<div class="body_titl space">
			<div class="user fl"></div>
				<div class="info">
					<div class="title"><%=user.getLongName() %></div>
					<div></div>
				</div>
			<div class="solid_line"></div>
		</div>
		<!-- Title //-->
	<%
	}else if(workSpace.getClass().equals(Department.class)){
		Department department = (Department)workSpace;
	%>
		<!-- Title -->
		<div class="body_titl space">
			<div class="depart fl"></div>
			<div class="noti_in_bodytitle">
				<div class="title"><%=CommonUtil.toNotNull(department.getFullpathName()) %></div>
				<div><%=CommonUtil.toNotNull(department.getDesc()) %></div>
			</div>
			<%--<%if(department.amIAdministrator(cUser)){ %>
			 <div class="txt_btn">	
				<a class="fr js_content" href="update_department_space.sw?departmentId=<%=department.getId()%>"><fmt:message key="common.title.space_setting"/></a><%} %>
			</div> --%>
			<div class="solid_line cb"></div>
		</div>
		<!-- Title //-->
	<%
	}else if(workSpace.getClass().equals(Group.class)){
		Group group = (Group)workSpace;
	%>
		<!-- Title -->
		<div class="body_titl space">
			<div class="group fl"></div>
			<div class="noti_in_bodytitle">
				<div class="title"><%=group.getName() %></div>
				<div><%=CommonUtil.toNotNull(group.getDesc()) %></div>
			</div>
			<%if(group.amIAdministrator(cUser)){ %>
			<div class="txt_btn">	
				<a class="fr js_content" href="update_group_space.sw?groupId=<%=group.getId()%>"><fmt:message key="common.title.space_setting"/></a>
			</div>
			<%} %>
			<div class="solid_line cb"></div>
		</div>
		<!-- Title //-->
	<%
	}
	%>
