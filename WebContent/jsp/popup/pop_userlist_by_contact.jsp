<%@page import="net.smartworks.util.SmartMessage"%>
<%@page import="net.smartworks.model.community.info.GroupInfo"%>
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
	String categoryId = request.getParameter("categoryId");
	boolean isMultiSelectable = false;
	isMultiSelectable = Boolean.parseBoolean(request.getParameter("multiUsers"));
	
 	CommunityInfo[] communities = smartWorks.getAllComsByCategoryId(CommonUtil.toNotNull(categoryId));
	String iconType = null;
%>

<form name="frmUserSelections">
	<ul>
		<%
		if (!SmartUtil.isBlankObject(communities)) {
			for (CommunityInfo community : communities) {
				if (community.getClass().equals(UserInfo.class)) {
					UserInfo user = (UserInfo)community;
		%>
					<li>
						<span class="dep">
							<%if(isMultiSelectable){ %><input type="checkbox" class="js_checkbox fl_nowidth" comName="<%=user.getLongName() %>" value="<%=user.getId()%>"/><%} %>						
							<a <%if(!isMultiSelectable){ %>href="" class="js_pop_select_user"<%} %> userId="<%=user.getId()%>"><%=user.getEmailAddressShown()%></a>
						</span>
					</li>
				<%
				} else if (community.getClass().equals(GroupInfo.class)) {
					GroupInfo group = (GroupInfo)community;
					iconType = "btn_tree_plus fn vm";
					if(SmartUtil.isBlankObject(group.getId())){
						group.setId("uncategorized");
						group.setName(SmartMessage.getString("common.title.uncategorized"));
					}
				%>
					<li class="js_drill_down">
						<span class="dep">
							<%if(isMultiSelectable){ %><input type="checkbox" class="js_checkbox fl_nowidth" comName="<%=group.getName() %>" value="<%=group.getId()%>"/><%} %>
							<a href="pop_userlist_by_contact.sw?multiUsers=<%=isMultiSelectable %>" categoryId="<%=group.getId()%>" class="js_popup js_expandable">
								<span class="<%=iconType%>"></span>
								<span><%=group.getName()%></span>
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
