
<!-- Name 			: space_profile.jsp								 				 -->
<!-- Description	: 화면 왼쪽의 Navigation Bar 에서 현재공간의 프로파일및 정보를 보여준다... -->
<!-- Author			: Maninsoft, Inc.												 -->
<!-- Created Date	: 2011.9.														 -->

<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ page import="net.smartworks.model.community.*"%>
<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 현재사용자 정보도 가져온다 
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();

	// 호출할때 전달된 cid(Context ID, 패이지 컨택스트를 지정하는 변수), 
	// wid(WorkSpace Id, 공간을 저정하는 변수) 를 가져옮..
	String cid = request.getParameter("cid");
	if (SmartUtil.isBlankObject(cid))
		cid = ISmartWorks.CONTEXT_HOME;
	String wid = request.getParameter("wid");
	if (SmartUtil.isBlankObject(wid))
		wid = cUser.getId();

	Group thisGroup = null;
	Department thisDepartment = null;
	User thisUser = null;
	String spaceId = SmartUtil.getSpaceIdFromContentContext(cid);
	
	// 전달된 스페이스가 그룹, 부서, 사용자중 어떤것인지 판단하여 해당 클래수로 캐스팅한다...
	if (SmartUtil.isSameContextPrefix(ISmartWorks.CONTEXT_PREFIX_GROUP_SPACE, cid)) {
		thisGroup = (Group) smartWorks.getWorkSpaceById(spaceId);
	} else if (SmartUtil.isSameContextPrefix(ISmartWorks.CONTEXT_PREFIX_DEPARTMENT_SPACE, cid)) {
		thisDepartment = (Department) smartWorks.getWorkSpaceById(spaceId);
	} else if (SmartUtil.isSameContextPrefix(ISmartWorks.CONTEXT_PREFIX_USER_SPACE, cid)) {
		thisUser = (User) smartWorks.getWorkSpaceById(spaceId);
	
	// 스페이스가 없는 경우에는 워크스페이스가 현재사용자가 아니면 그 워크스페이스로 이동할 수 있게 한다...
	} else if (!wid.equals(cUser.getId())) {
		WorkSpace workSpace = smartWorks.getWorkSpaceById(wid);
		if (SmartUtil.isBlankObject(workSpace)) {
			thisUser = SmartUtil.getCurrentUser();
		} else if (workSpace.getClass() == User.class) {
			thisUser = (User) workSpace;
		} else if (workSpace.getClass() == Group.class) {
			thisGroup = (Group) workSpace;
		} else if (workSpace.getClass() == Department.class) {
			thisDepartment = (Department) workSpace;
		}

	// 위의 모든 경우에 해당되지 않으면, 현재사용자 공간으로 이동한다...
	} else {
		thisUser = SmartUtil.getCurrentUser();
	}
%>

<ul class="js_space_profile_page" spaceId="<%=spaceId%>">
	<%
	// 그룹인경우....
	if (thisGroup != null) {
		String target = thisGroup.getSpaceController() + "?cid=" + thisGroup.getSpaceContextId() + "&wid=" + thisGroup.getId(); 
		//thisGroup.setPublic(true);
	%>
		<li>
			<a href="<%=target %>"><img class="profile_size_66" src="<%=thisGroup.getOrgPicture()%>"></a>
		</li>
		<li class="info m0">
			<div><b><%=thisGroup.getName()%></b>
			<%
			if (!thisGroup.isPublic()) {
			%>
			<span class="icon_private"></span>
			<%
			}
			%>
			</div>
			<div class="bul_org"><fmt:message key="group.role.leader" /> : <%=thisGroup.getLeader().getLongName()%></div>
			<div class="bul_org"><fmt:message key="group.created_date" /> : <%=thisGroup.getOpenDate().toLocalDateSimpleString()%></div>
			<div class="bul_org"><fmt:message key="group.members_count" /> : <%=thisGroup.getNumberOfGroupMember()%></div>
		</li>
		<!-- 가입하기/초대하기 아이콘 -->
		<div class="tc cb">
			<%
			if(true){//thisGroup.amIInvitableMember()){
			%>
				<button class="js_invite_group_members"><fmt:message key="group.button.invite_member"/></button>
			<%
			}
			if(thisGroup.isPublic() && !thisGroup.amIMember()){
				if(thisGroup.isAutoApproval()){
			%>
					<button class="js_join_group_request" isAutoApproval="true"><fmt:message key="group.button.join"/></button>
				<%
				}else if(thisGroup.amIJoinRequester()){
				%>
					<span><fmt:message key="group.title.waiting_approval"/></span>
				<%
				}else{
				%>
					<button class="js_join_group_request" isAutoApproval="false"><fmt:message key="group.button.join_request"/></button>
			<%	
				}
			}else if(thisGroup.amIMember()){
			%>
				<span>
					<a href="" class="js_leave_group_request"><fmt:message key="group.button.leave"/></a>
				</span>
			<%
			}
			%>
		</div>
		<!-- 가입하기/초대하기 아이콘 //-->
	<%
	// 부서인경우....
	} else if (thisDepartment != null) {
		String target = thisDepartment.getSpaceController() + "?cid=" + thisDepartment.getSpaceContextId() + "&wid=" + thisDepartment.getId(); 
	%>
		<li>
			<a href="<%=target %>"><img class="profile_size_66" src="<%=thisDepartment.getOrgPicture()%>"></a>
		</li>
		<li class="info m0">
			<div><b><%=thisDepartment.getName()%></b></div>
			<div class="bul_org"><fmt:message key="department.role.head" /> : <%=thisDepartment.getHead() == null ? "" : thisDepartment.getHead().getLongName()%></div>
			<div class="bul_org"><fmt:message key="department.subdepartment_count" /> : <%=thisDepartment.getNumberOfSubDepartment()%></div>
			<div class="bul_org"><fmt:message key="department.members_count" /> : <%=thisDepartment.getNumberOfMember()%></div>
		</li>
	<%
	// 사용자인 경우....
	} else if (thisUser != null) {
		String target = thisUser.getSpaceController() + "?cid=" + thisUser.getSpaceContextId() + "&wid=" + thisUser.getId(); 
		String userDetailInfo = SmartUtil.getUserDetailInfo(thisUser.getUserInfo());
	%>
		<li>
			<a href="<%=target %>" class="js_pop_user_info" userId="<%=thisUser.getId()%>" longName="<%=thisUser.getLongName() %>" minPicture="<%=thisUser.getMinPicture() %>" profile="<%=thisUser.getOrgPicture()%>" userDetail="<%=userDetailInfo%>"><img class="profile_size_66" src="<%=thisUser.getOrgPicture()%>"></a>
		</li>
		<li class="info m0">
			<div><%=thisUser.getPosition()%></div>
			<div><b><%=thisUser.getName()%></b></div>
			<div class="bul_org"><%=thisUser.getDepartment()%></div>
		</li>
	<%
	}
	%>
</ul>
