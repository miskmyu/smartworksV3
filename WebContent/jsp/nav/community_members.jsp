
<!-- Name 			: community_members.jsp										 	 -->
<!-- Description	: 좌측의 Navigation Bar 에서 커뮤너티공간인경우 멤버들을 보여주는 공간 	 -->
<!-- Author			: Maninsoft, Inc.												 -->
<!-- Created Date	: 2011.9.														 -->

<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.model.community.info.CommunityInfo"%>
<%@page import="net.smartworks.model.community.info.DepartmentInfo"%>
<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ page import="net.smartworks.model.community.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 현재사용자 정보도 가져온다 
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();

	UserInfo[] members = null;
	DepartmentInfo[] children = null;
	User leader = null;

	// 호출할때 전달된 cid(Context ID, 패이지 컨택스트를 지정하는 변수) 를 가져옮..
	String communityId = request.getParameter("wid");
	int workSpaceType = Integer.parseInt(request.getParameter("workSpaceType"));
	// 공간이 그룹일때와 부서일때를 구분하여 해당 정보를 서버에게 가져온다...
	if (workSpaceType == ISmartWorks.SPACE_TYPE_GROUP) {
		Group group = smartWorks.getGroupById(communityId);
		members = group.getMembers();
		leader = group.getLeader();
	} else if (workSpaceType == ISmartWorks.SPACE_TYPE_DEPARTMENT) {
		Department department = smartWorks.getDepartmentById(communityId);
		members = department.getMembers();
		children = department.getChildren();
		leader = department.getHead();
	}
%>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!-- 커뮤너티 멤버와 검색박스가 있는 헤더  -->
<ul class="nav_tit js_community_members" communityId="<%=communityId%>">

	<!-- 커뮤너티멤버 라벨과 클릭시 아래의 멤버선택트리화면을 접었다 폈다하는 기능 제공  -->
	<!-- *** js_collapse_parent_siblings : sw_act_nav.js 에서 이클래스의 클릭이벤트를 받아서 -->
	<!--            아래의 js_callapsible 클래스를 찾아 toggle 한다 -->
	<li>
		<%
		if(SmartUtil.isBlankObject(children)){
		%>
			<a href="" class="js_collapse_parent_siblings arr_on"><fmt:message key="nav.communities.group_members" /></a>
		<%}else{ %>
			<a href="" class="js_collapse_parent_siblings arr_on"><fmt:message key="nav.communities.department_members" /></a>
		<%
		}
		%>
		<span></span><!--  프로그래스아이콘이 실행되는 곳 -->
	</li>
	
	<!--  검색박스를 제공하여, 초성검색 기능을 제공 -->
	<li class="nav_srch js_srch_com_members">
		<div class="srch srch_wsize">
			<input id="" class="nav_input js_auto_complete" type="text" title="<fmt:message key='search.search_people'/>"
				placeholder="<fmt:message key='search.search_people'/>" href="community_member.sw">
			<div class='srch_icon js_srch_x'></div>
		</div>
		<!-- nav 검색 리스트 -->
		<div class="nav_srch_list m0" style="display: none"></div>
		<!-- nav 검색 리스트 -->
	</li>
	<!--  검색박스를 제공하여, 초성검색 기능을 제공 //-->
	
</ul>
<!-- 커뮤너티멤버 와 검색박스가 있는 헤더 // -->

<!--  커뮤너티멤버를 찾을수 있는 트리 화면  -->
<div class='nav_sub_list pt10 js_collapsible js_nav_com_members'>
	<%
	if (workSpaceType == ISmartWorks.SPACE_TYPE_GROUP && !SmartUtil.isBlankObject(members)) {
	%>
		<div id="community_group">
			<ul>
				<li>
					<!-- 리더 -->
					<a href="<%=leader.getSpaceController()%>?cid=<%=leader.getSpaceContextId()%>&wid=<%=leader.getId()%>" class="js_pop_user_info" userId="<%=leader.getId()%>" longName="<%=leader.getLongName() %>" minPicture="<%=leader.getMinPicture() %>" profile="<%=leader.getOrgPicture()%>" userDetail="<%=SmartUtil.getUserDetailInfo(leader.getUserInfo())%>">
					    <span class="icon_pe"><span class="leader"></span><img class="profile_size_s" src="<%=leader.getMinPicture()%>"></span>
					    <span class="nav_sub_area"><%=leader.getLongName() %>(<fmt:message key="group.role.leader"/>)</span>
					</a>
				</li>
			</ul>
			<div class="members">
				<%
				int i=0;
				boolean isLeaderCounted = false;
				for(; i<members.length; i++){
					UserInfo member = members[i];
					if(member.getId().equals(leader.getId())){
						isLeaderCounted = true;
						continue;
					}
					if(i==(45+((isLeaderCounted)?1:0))){
						break;
					}else{
					%>
						<a title="<%=member.getLongName() %>" href="<%=member.getSpaceController()%>?cid=<%=member.getSpaceContextId()%>&wid=<%=member.getId()%>" class="js_pop_user_info" userId="<%=member.getId()%>" longName="<%=member.getLongName() %>" minPicture="<%=member.getMinPicture() %>" profile="<%=member.getOrgPicture()%>" userDetail="<%=SmartUtil.getUserDetailInfo(member.getUserModel().getUserInfo())%>">
							<span class="icon_pe">
						    	<img class="profile_size_s" src="<%=member.getMinPicture()%>">
						    </span>
						</a>
				<%
					}
				}
				%>
			</div>
			<%
			if(i+((isLeaderCounted)?1:0)<members.length){
				int remains = members.length-i;
			%>
				<div class="fr">
					<fmt:message key="content.sentence.with_other_users">
						<fmt:param><%=remains %></fmt:param>							
					</fmt:message>
				</div>
			<%
			}
			%>
					
		</div>
		<!-- 구성원, 하위부서 //-->
	<%
	}else if (workSpaceType == ISmartWorks.SPACE_TYPE_DEPARTMENT) {
		CommunityInfo[] communities = smartWorks.getAllComsByDepartmentId(CommonUtil.toNotNull(communityId), false);
	%>
		<!-- 내부 메뉴 -->
		<div id='community_department'>
			<ul>
					<%
					if (!SmartUtil.isBlankObject(communities)) {
						String iconType = "";
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
										<a href="<%=user.getSpaceController()%>?cid=<%=user.getSpaceContextId()%>&wid=<%=user.getId()%>" class="js_pop_user_info" userId="<%=user.getId()%>" longName="<%=user.getLongName() %>" minPicture="<%=user.getMinPicture() %>" profile="<%=user.getOrgPicture()%>" userDetail="<%=SmartUtil.getUserDetailInfo(user.getUserModel().getUserInfo())%>">
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
										<a href="communitylist_by_depart.sw" departmentId="<%=department.getId()%>" class="js_popup js_expandable"><span class="<%=iconType%>"></span></a>
										<a href="<%=department.getSpaceController()%>?cid=<%=department.getSpaceContextId()%>&wid=<%=department.getId()%>"><span class="js_department"> <%=department.getName()%> </span></a> 
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
		<!--내부메뉴//-->
	<%
	}
	%>		
</div>
<!--  커뮤너티멤버를 찾을수 있는 트리 화면 //-->
