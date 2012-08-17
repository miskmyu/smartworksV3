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
	
	CommunityInfo[] communities = smartWorks.getAllComsByDepartmentId(CommonUtil.toNotNull(departmentId), false);
	String iconType = null;
%>
<div class="">
	<ul>
		<%
		if (!SmartUtil.isBlankObject(communities)) {
			for (CommunityInfo community : communities) {
				if (community.getClass().equals(UserInfo.class)) {
					UserInfo user = (UserInfo)community;
					if(user.getRole() == User.USER_ROLE_LEADER){
						iconType = "leader";
					} else if(user.getRole() == User.USER_ROLE_MEMBER){
						iconType = "";
					}
		%>
					<li>
						<span class="dep">
							<a href="<%=user.getSpaceController()%>?cid=<%=user.getSpaceContextId()%>&wid=<%=user.getId()%>">
								<span class="<%=iconType%>"></span><img src="<%=user.getMinPicture() %>" class="profile_size_s"><%=user.getLongName()%>
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
							<a href="communitylist_by_depart.sw" departmentId="<%=department.getId()%>" class="js_popup js_expandable">
								<span class="<%=iconType%>"></span>
							</a>
							<a href="<%=department.getSpaceController()%>?cid=<%=department.getSpaceContextId()%>&wid=<%=department.getId()%>">
								<span class="js_department"> <%=department.getName()%> </span>
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
</div>