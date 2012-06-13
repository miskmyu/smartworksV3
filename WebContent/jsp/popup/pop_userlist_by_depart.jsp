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
	String departmentId = request.getParameter("departmentId");
	boolean isMultiSelectable = false;
	isMultiSelectable = Boolean.parseBoolean(request.getParameter("multiUsers"));
	
	CommunityInfo[] communities = smartWorks.getAllComsByDepartmentId(CommonUtil.toNotNull(departmentId), false);
	String iconType = null;
%>

<form name="frmUserSelections">
	<ul>
		<%
		if (!SmartUtil.isBlankObject(communities)) {
			for (CommunityInfo community : communities) {
				if (community.getClass().equals(UserInfo.class)) {
					UserInfo user = (UserInfo)community;
					if(user.getRole() == User.USER_ROLE_LEADER){
						iconType = "icon_user_leader";
					} else if(user.getRole() == User.USER_ROLE_MEMBER){
						iconType = "icon_user_member";
					}
		%>
					<li>
						<span class="dep">
							<%if(isMultiSelectable){ %><input type="checkbox" class="js_checkbox fl_nowidth" comName="<%=user.getLongName() %>" value="<%=user.getId()%>"/><%} %>						
							<a <%if(!isMultiSelectable){ %>href="" class="js_pop_select_user"<%} %> userId="<%=user.getId()%>" userName="<%=user.getName()%>" userPosition="<%=CommonUtil.toNotNull(user.getPosition())%>" userPicture="<%=user.getMinPicture()%>">
								<img src="<%=user.getMinPicture() %>" class="profile_size_s"><span class="<%=iconType%>"></span><%=user.getLongName()%>
							</a>
						</span>
					</li>
				<%
				} else if (community.getClass().equals(DepartmentInfo.class)) {
					DepartmentInfo department = (DepartmentInfo)community;
					iconType = "btn_tree_plus fn vm";
				%>
					<li class="js_drill_down">
						<span class="dep">
							<%if(isMultiSelectable){ %><input type="checkbox" class="js_checkbox fl_nowidth" comName="<%=department.getName() %>" value="<%=department.getId()%>"/><%} %>
							<a href="pop_userlist_by_depart.sw?multiUsers=<%=isMultiSelectable %>" departmentId="<%=department.getId()%>" class="js_popup js_expandable">
								<span class="<%=iconType%>"></span>
								<span> <%=department.getName()%> </span>
							</a>
						</span>
						<div style="display: none" class="menu_2dep js_drill_down_target"></div>
					</li>
		<%
				}
			}
		}
		%>
	</ul>
</form>
