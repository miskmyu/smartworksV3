<%@page import="net.smartworks.model.work.SmartWork"%>
<%@page import="net.smartworks.model.instance.info.EventInstanceInfo"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>

<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 현재사용자 정보도 가져온다 
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();

	// 호출할때 전달된 cid(Context ID, 패이지 컨택스트를 지정하는 변수) 를 가져옮..
	String communityId = request.getParameter("wid");

	EventInstanceInfo[] events = smartWorks.getCommingEventInstances(communityId, 5);
	String listTarget = "event_list.sw?cid=" + ISmartWorks.CONTEXT_PREFIX_EVENT_LIST + SmartWork.ID_EVENT_MANAGEMENT + "&wid=" + communityId;
	
%>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!-- 최근 이벤트 -->
<div class="nav_tit">
	<div class="icon_event_works fn">
		<a class="js_content" href="<%=listTarget%>">
		<fmt:message key="common.title.comming_event"/>
		</a>
		<!-- 전체보기 -->
		<%if(!SmartUtil.isBlankObject(events)){%>
		<span class="more">
			<a href="<%=listTarget%>">	<fmt:message key="common.button.view_all"/>	</a>
		</span>
		<%} %>
		<!-- 전체보기 //-->
	</div>
	
</div>    
<ul>
 	<%
 	if(!SmartUtil.isBlankObject(events)){
 		for(int i=0; i<events.length; i++){
 			EventInstanceInfo event = events[i];
 			String target = "iwork_space.sw?cid=" + ISmartWorks.CONTEXT_PREFIX_IWORK_SPACE + event.getId() + "&wid=" + communityId + "&workId=" + SmartWork.ID_EVENT_MANAGEMENT;
 	%>
      <li>
       <a href="<%=target %>" class="js_content">
	       <span class="t_date"><%=event.getStart().toLocalString() %></span>
	       <div><%=event.getSubject() %></div>
       </a>
      </li>	
 	
 	<%
 		}
 	}
 	%>
</ul>
<!-- 최근 이벤트 //-->
 
