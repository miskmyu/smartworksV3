<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.model.sera.info.SeraUserInfo"%>
<%@page import="net.smartworks.util.SmartTest"%>
<%@page import="net.smartworks.model.sera.Course"%>
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
	User cUser = SmartUtil.getCurrentUser();

	boolean isMultiSelectable = false;
	isMultiSelectable = Boolean.parseBoolean(request.getParameter("multiUsers"));
	
	SeraUserInfo[] friends = smartWorks.getFriendsById(cUser.getId(), null,  -1, null);
	String iconType = null;
%>

<form name="frmUserSelections">
	<ul>
		<%
		if (!SmartUtil.isBlankObject(friends)) {
			for (SeraUserInfo friend : friends) {
		%>
					<li>
						<span class="dep">
							<%if(isMultiSelectable){ %><input type="checkbox" class="js_checkbox fl_nowidth" comName="<%=friend.getNickName() %>" value="<%=friend.getId()%>"/><%} %>						
							<a <%if(!isMultiSelectable){ %>href="" class="js_pop_select_user"<%} %> userId="<%=friend.getId()%>">
								<img src="<%=friend.getMinPicture() %>" class="profile_size_s"><%=friend.getNickName()%>
							</a>
						</span>
					</li>
		<%
			}
		}
		%>
	</ul>
</form>
