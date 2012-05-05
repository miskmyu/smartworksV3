<%@page import="net.smartworks.model.instance.info.InstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.TaskInstanceInfo"%>
<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.model.instance.info.EventInstanceInfo"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ page import="net.smartworks.model.community.*"%>
<%@ page import="net.smartworks.model.notice.*"%>
<%@ page import="net.smartworks.model.instance.*"%>
<%@ page import="net.smartworks.model.work.*"%>
<%@ page import="net.smartworks.util.LocalDate"%>
<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다.
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();

	// 호출될때 전달되는 lastNoticeId를 가져온다.
	String lastNoticeId = request.getParameter("lastNoticeId");
	int noticeType = Notice.TYPE_NOTIFICATION;
	
	// 서버에게 lastNoticeId를 기준으로 최근 10개의 Notice항목을 가져오는 기능.
	NoticeBox noticeBox = smartWorks.getNoticeBoxForMe10(noticeType, lastNoticeId);

	NoticeMessage[] noticeMessages = noticeBox.getNoticeMessages();
	if (noticeMessages != null) {
%>
		<div class="sera_tpop_section" style="display:block; z-index: 1000; right:314px; top:36px">
	        <div href="" class="icon_alarm current js_shown_notice_btn"></div>
	        <div class="sera_tpop" style="margin:38px 2px 0 0">
	        	<div class="layer_pop_tit">알 림</div>
				<ul>
					<%
					for (NoticeMessage nMessage : (NoticeMessage[]) noticeBox.getNoticeMessages()) {
						if (noticeBox != null && noticeBox.getNoticeType() == Notice.TYPE_NOTIFICATION) {
							UserInfo owner = null;
			
							// Notice message type 이 시스템 알림인 경우, 
							if (nMessage.getType() == NoticeMessage.TYPE_SYSTEM_NOTICE) {
								UserInfo issuer = nMessage.getIssuer();
					%>
					            <li>
					      			<div class="info_ms_section">
					                	<div class="info_img"> 
					                		<a title="<%=issuer.getNickName() %>" href="othersHome.sw?userId=<%=issuer.getId()%>"> 
					                			<img class="profile_size_s" src="<%=issuer.getMinPicture()%>"> 
					                		</a> 
					                	</div>
					                	<div class="info_list"><%=nMessage.getMessage() %>
					                		<div class="t_date"><%=nMessage.getIssuedDate().toLocalDateString() %>
					                			<a  href=""><div class="icon_delete fr"></div></a>
					                		</div>
					                	</div>
					              	</div>
					            </li>
							<%
							// Notice Message type 이 이벤트 알림인 경우 
							} else if (nMessage.getType() == NoticeMessage.TYPE_EVENT_ALARM) {
								EventInstanceInfo event = (EventInstanceInfo) nMessage.getEvent();
								owner = event.getOwner();
							%>
					            <li>
					      			<div class="info_ms_section">
					                	<div class="info_img"> 
					                		<a title="<%=owner.getNickName() %>" href="othersHome.sw?userId=<%=owner.getId()%>"> 
					                			<img class="profile_size_s" src="<%=owner.getMinPicture()%>"> 
					                		</a> 
					                	</div>
					                	<div class="info_list"><%=nMessage.getMessage() %>
											<b><%=event.getStart().toLocalString()%> </b><a href=""><%=event.getSubject()%></a>
					                		<div class="t_date"><%=nMessage.getIssuedDate().toLocalDateString() %>
					                			<a  href=""><div class="icon_delete fr"></div></a>
					                		</div>
					                	</div>
					              	</div>
					            </li>
							<%
							// Notice Message type 이 지연 업무 알림인 경우
							} else if (nMessage.getType() == NoticeMessage.TYPE_JOIN_REQUEST) {
								owner = nMessage.getIssuer();
							%>
					            <li>
					      			<div class="info_ms_section">
					                	<div class="info_img"> 
					                		<a title="<%=owner.getNickName() %>" href="othersHome.sw?userId=<%=owner.getId()%>"> 
					                			<img class="profile_size_s" src="<%=owner.getMinPicture()%>"> 
					                		</a> 
					                	</div>
					                	<div class="info_list"><%=nMessage.getMessage() %>
											<a href=""><%=nMessage.getGroup().getName()%></a>
											<fmt:message key="notice.message.join.request" />
					                		<div class="t_date"><%=nMessage.getIssuedDate().toLocalDateString() %>
					                			<a  href=""><div class="icon_delete fr"></div></a>
					                		</div>
					                	</div>
					              	</div>
					            </li>
							<%
							// Notice Message type 이 새로운 업무 생성 알림인 경우 
							} else if (nMessage.getType() == NoticeMessage.TYPE_INSTANCE_CREATED) {
								InstanceInfo instance = (InstanceInfo) nMessage.getInstance();
								owner = instance.getOwner();
							%>
					            <li>
					      			<div class="info_ms_section">
					                	<div class="info_img"> 
					                		<a title="<%=owner.getNickName() %>" href="othersHome.sw?userId=<%=owner.getId()%>"> 
					                			<img class="profile_size_s" src="<%=owner.getMinPicture()%>"> 
					                		</a> 
					                	</div>
					                	<div class="info_list"><%=nMessage.getMessage() %>
											<a href=""><%=instance.getSubject()%></a>
											<fmt:message key="notice.message.instance.created" />
					                		<div class="t_date"><%=nMessage.getIssuedDate().toLocalDateString() %>
					                			<a  href=""><div class="icon_delete fr"></div></a>
					                		</div>
					                	</div>
					              	</div>
					            </li>
					<%
							}
						}
					}
					%>
				</ul>
          		<div class="more"> <a href="">더보기</a> </div>
			</div>
		</div>
	<%
	}
	%>
