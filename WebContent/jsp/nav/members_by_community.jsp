<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ page import="net.smartworks.model.community.*"%>

<%
	String companyId = (String) session.getAttribute("companyId");
	String userId = (String) session.getAttribute("userId");
	
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	UserInfo[] members = null;
	{
		String cid = request.getParameter("cid");
		if (cid == null)
			cid = ISmartWorks.CONTEXT_HOME;

		if (SmartUtil.isSameContextPrefix(ISmartWorks.CONTEXT_PREFIX_GROUP_SPACE, cid)) {
			members = smartWorks.getGroupById(companyId, SmartUtil.getSpaceIdFromContentContext(cid)).getMembers();
		} else if (SmartUtil.isSameContextPrefix(ISmartWorks.CONTEXT_PREFIX_DEPARTMENT_SPACE, cid)) {
			members = smartWorks.getDepartmentById(companyId, SmartUtil.getSpaceIdFromContentContext(cid)).getMembers();
		}
	}
%>

<ul>
	<%
		if (members != null) {
			String contextId = null;
			for (UserInfo member : members) {
				contextId = ISmartWorks.CONTEXT_PREFIX_USER_SPACE + member.getId();
	%>
	<li><img src="<%=member.getMinPicture()%>" border="0"> <a
		href="user_space.sw?cid=<%=contextId%>"><%=member.getPosition()%>
			<%=member.getName()%></a></li>
	<%
		}
		}
	%>
</ul>
