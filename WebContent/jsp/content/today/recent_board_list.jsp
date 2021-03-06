
<!-- Name 			: recent_board_list.jsp									 -->
<!-- Description	: 현재사용자에게 해당되는 최근 공지사항들 5개만 나타내주는 화면 	 -->
<!-- Author			: Maninsoft, Inc.										 -->
<!-- Created Date	: 2011.9.												 -->

<%@page import="net.smartworks.model.work.SocialWork"%>
<%@page import="net.smartworks.model.work.info.WorkInfo"%>
<%@page import="net.smartworks.model.work.SmartWork"%>
<%@page import="net.smartworks.model.community.info.DepartmentInfo"%>
<%@page import="net.smartworks.model.community.info.GroupInfo"%>
<%@page import="net.smartworks.model.community.info.WorkSpaceInfo"%>
<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.model.instance.info.BoardInstanceInfo"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ page import="net.smartworks.model.calendar.*"%>
<%@ page import="net.smartworks.util.*"%>
<%@ page import="net.smartworks.model.instance.*"%>
<%@ page import="net.smartworks.model.community.*"%>
<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 현재사용자 정보도 가져온다 
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();
	String wid = (String)session.getAttribute("wid");

	BoardInstanceInfo[] boards = null;
	if(SmartUtil.isBlankObject(wid))
		// 현재사용자에게 해당되는 최근 5개의 공지사항을 가져온다.... 
		boards = smartWorks.getMyRecentBoardInstances();
	else
		boards = smartWorks.getCommunityRecentBoardInstances(wid);
%>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<div class="notice">
	<!-- 공지사항 타이틀 -->
	<div class="left_title">
		<a href="<%=WorkInfo.getController(SocialWork.ID_BOARD_MANAGEMENT, SocialWork.TYPE_BOARD) %>?cid=<%=WorkInfo.getContextId(SocialWork.ID_BOARD_MANAGEMENT, SocialWork.TYPE_BOARD)%>" class="more"><fmt:message key="common.button.view_all"/></a>
		<!-- 처음게시판(헤드라인 공지사항)을 표시한다 -->
		<%-- <div class="js_content_list" href="<%=board.getController() %>?cid=<%=board.getContextId()%>&workId=<%=SmartWork.ID_BOARD_MANAGEMENT%>&wid=<%=board.getWorkSpace().getId()%>">
			<span class="title"><img class="profile_size_s" src="<%=board.getOwner().getMinPicture()%>">&nbsp;<%=board.getSubject() %>
				<%if(board.getSubInstanceCount()>0){ %><font class="t_sub_count">[<b><%=board.getSubInstanceCount() %></b>]</font><%} %>
				<%if(board.isNew()){ %><span class="icon_new"></span><%} %>
			</span>
			<span class="index">
				<%
				if(!workSpace.equals("")){
				%>
					<span class="arr">▶</span>
					<span class="<%=workSpace.getIconClass()%>"><%=workSpace.getName() %></span>
				<%
				}
				%>
				<span class="date t_date"><%=board.getLastModifiedDate().toLocalString() %></span>
			</span>
			<span class="info"></span>
			<span class="content"><%=board.getBriefContent() %></span>
		</div> --%>
		<!-- 처음게시판(헤드라인 공지사항)을 표시한다 //-->
	</div>
	<!-- 공지사항 타이틀 //-->
		

	<%
	// 공지사항이 있으면 화면을 그린다...
	if(boards != null && boards.length>0){
		
 		// 처음 공지사항은 최근 것이므로 별도로 왼쪽에 내용미리보기와 함께 표시하기 위해 분리한다...
		BoardInstanceInfo board = boards[0];
 		String workId = board.getWorkId();
 		String workName = board.getWorkName();
 		int workType = board.getWorkType();
 		boolean isWorkRunning = board.isWorkRunning();
 		String workFullPathName = board.getWorkFullPathName();
//		WorkSpaceInfo workSpace = board.getWorkSpace();
		String workSpaceId = board.getWorkSpaceId();
		String workSpaceName = board.getWorkSpaceName();
		int workSpaceType = board.getWorkSpaceType();
		UserInfo owner = board.getOwner();
 	
	%>
		<!-- 나머지 4개를 리스트로 표시한다 -->
		<div class="recent_notice">
			<table>
				<%
				for(int i=0; i<boards.length; i++) {
					board = boards[i];
//					workSpace = board.getWorkSpace();
					workSpaceId = board.getWorkSpaceId();
					workSpaceName = board.getWorkSpaceName();
					workSpaceType = board.getWorkSpaceType();
				%>			
					<tr class="instance_list js_content_list" href="<%=board.getController() %>?cid=<%=board.getContextId()%>&workId=<%=SmartWork.ID_BOARD_MANAGEMENT%>&wid=<%=workSpaceId%>">
						<td class="title">
							<span><span  class="js_pop_user_info" userId="<%=board.getOwner().getId()%>" longName="<%=board.getOwner().getLongName() %>" minPicture="<%=board.getOwner().getMinPicture() %>" profile="<%=board.getOwner().getOrgPicture()%>" userDetail="<%=SmartUtil.getUserDetailInfo(board.getOwner().getUserModel().getUserInfo())%>"><img class="profile_size_s" src="<%=board.getOwner().getMinPicture()%>"></span>&nbsp;<%=board.getSubject()%></span> 
							<%if(board.getSubInstanceCount()>0){ %><font class="t_sub_count">[<b><%=board.getSubInstanceCount() %></b>]</font><%} %>
							<%if(board.isNew()){ %><span class="icon_new"></span><%} %>
							<%
							if(!SmartUtil.isBlankObject(workSpaceId) && !workSpaceId.equals(cUser.getId())){
							%>
								<span class="arr">▶</span><span class="space_name"><%=workSpaceName%></span>
							<%
							}
							%>
						</td>
						<td class="date t_date"><%=board.getLastModifiedDate().toLocalString() %></td>
					</tr>
				<%
				}
				%>
			</table>
		</div>
		<!-- 나머지 4개를 리스트로 표시한다 //-->
	<%
	// 공지사항이 있으면 화면을 그린다...
	}else{
	%>
		<span><fmt:message key="common.message.no_board"/></span>
	<%
	}
	%>
</div>
