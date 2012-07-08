<%@page import="net.smartworks.model.work.SmartWork"%>
<%@page import="net.smartworks.model.instance.info.BoardInstanceInfo"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>

<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 현재사용자 정보도 가져온다 
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();

	// 호출할때 전달된 cid(Context ID, 패이지 컨택스트를 지정하는 변수) 를 가져옮..
	String cid = request.getParameter("cid");
	if (SmartUtil.isBlankObject(cid))
		cid = ISmartWorks.CONTEXT_HOME;

	String communityId = SmartUtil.getSpaceIdFromContentContext(cid);

	BoardInstanceInfo[] boards = smartWorks.getRecentBoardInstances(communityId, 5);
	String listTarget = "board_list.sw?cid=" + ISmartWorks.CONTEXT_PREFIX_BOARD_LIST + SmartWork.ID_BOARD_MANAGEMENT + "&wid=" + communityId;
	
%>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!-- 최근 공지 -->
<div class="nav_tit">
	<div class="icon_board_works fn js_content" href="<%=listTarget%>"><fmt:message key="common.title.recent_board"/></a></div>
</div>    
<ul>
 	<%
 	if(!SmartUtil.isBlankObject(boards)){
 		for(int i=0; i<boards.length; i++){
 			BoardInstanceInfo board = boards[i];
 			String target = "iwork_space.sw?cid=" + ISmartWorks.CONTEXT_PREFIX_IWORK_SPACE + board.getId() + "&wid=" + communityId + "&workId=" + SmartWork.ID_BOARD_MANAGEMENT;
 	%>
      <li>
       <a href="<%=target %>" class="js_content">
	       <img class="profile_size_s" src="<%=board.getOwner().getMinPicture()%>">
	       <div>
	       <%=board.getSubject() %>
	       <span class="t_date fr"><%=board.getLastModifiedDate().toLocalString() %></span>
	       </div>
       </a>
      </li>	
 	
 	<%
 		}
 	}
 	%>
</ul>
<!-- 최근 공지 //-->