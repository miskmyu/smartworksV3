<%@page import="net.smartworks.model.community.Group"%>
<%@page import="net.smartworks.model.work.AccessPolicy"%>
<%@page import="net.smartworks.model.community.Department"%>
<%@page import="net.smartworks.model.community.Community"%>
<%@page import="net.smartworks.model.work.SmartWork"%>
<%@page import="net.smartworks.model.work.Work"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%

	String wid = request.getParameter("wid");
	if (wid == null)
		wid = SmartUtil.getCurrentUser().getId();

	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");

	String workId = request.getParameter("workId");
	User cUser = SmartUtil.getCurrentUser();

	Work work = smartWorks.getWorkById(workId);
	if(work==null) work = new Work();
	Community[] communities = smartWorks.getMyCommunities();
%>

<table id="glo_btn_space">
	<colgroup>
		<col class="field_btn">
		<col class="item_btn">
		<col class="item_btn">
		<col class="field_btn">
	</colgroup>
	<tbody>
		<tr>
			<td></td>
			<td><span class="btn_wh"> <a href=""> <span
						class="Btn01Start"></span> <span class="Btn01Center"><fmt:message key="common.button.complete"/></span> <span
						class="Btn01End"></span> </a> </span>
			</td>
			<td><span class="btn_wh"> <a href=""> <span
						class="Btn01Start"></span> <span class="Btn01Center"><fmt:message key="common.button.cancel"/></span> <span
						class="Btn01End"></span> </a> </span>
			</td>
			<td>
				<form name="form" id="" class="float_right">
					<select name="jumpMenu" id="jumpMenu">
						<option value="<%=cUser.getId()%>"><fmt:message key="common.upload.space.self"/></option>
						<optgroup label="<fmt:message key="common.upload.space.department"/>">
						<%for(Community community : communities){
							if(community.getClass().equals(Department.class)){%>
						<option value="<%=community.getId()%>"><%=community.getName() %></option>
						<%
							}
						}
							%>
						</optgroup>
						<optgroup label="<fmt:message key="common.upload.space.group"/>">
						<%for(Community community : communities){
							if(community.getClass().equals(Group.class)){%>
						<option value="<%=community.getId()%>"><%=community.getName() %></option>
						<%
							}
						}
							%>
						</optgroup>
					</select>
				</form>

				<form name="form" id="" class="float_right form_space">
					<select name="jumpMenu" id="jumpMenu">
						<% int accessLevel = work.getAccessPolicy().getLevel();
						if( accessLevel ==  AccessPolicy.LEVEL_PUBLIC){
							%>
							<option selected value="<%=AccessPolicy.LEVEL_PUBLIC%>"><fmt:message key="common.upload.open.public"/></option>
							<option value="<%=AccessPolicy.LEVEL_PRIVATE%>"> <fmt:message key="common.upload.open.private"/></option>
							<option value="<%=AccessPolicy.LEVEL_CUSTOM%>"><fmt:message key="common.upload.open.custom"/></option>
						<%
						}else if(work.getAccessPolicy().getLevel() ==  AccessPolicy.LEVEL_CUSTOM){
							%>
							<option value="<%=AccessPolicy.LEVEL_PRIVATE%>"> <fmt:message key="common.upload.open.private"/></option>
							<option selected value="<%=AccessPolicy.LEVEL_CUSTOM%>"><fmt:message key="common.upload.open.custom"/></option>
						<%
						}else if(work.getAccessPolicy().getLevel() ==  AccessPolicy.LEVEL_PUBLIC){
							%>
							<option value="<%=AccessPolicy.LEVEL_PRIVATE%>"> <fmt:message key="common.upload.open.private"/></option>
						<%
						}
							%>
					</select>
				</form>
			</td>
		</tr>
	</tbody>
</table>
