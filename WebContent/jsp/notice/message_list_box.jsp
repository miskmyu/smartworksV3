
<!-- Name 			: message_list_box.jsp									 -->
<!-- Description	: 화면구성중에 Header 에서 새로운 쪽지들의 목록들을 보여주는 박스 	 -->
<!-- Author			: Maninsoft, Inc.										 -->
<!-- Created Date	: 2011.9.												 -->

<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.model.instance.info.AsyncMessageInstanceInfo"%>
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
	int noticeType = Notice.TYPE_MESSAGE;

	// 서버에게 lastNoticeId를 기준으로 최근 10개의 Notice항목을 가져오는 기능.
	NoticeBox noticeBox = smartWorks.getNoticeBoxForMe10(noticeType, lastNoticeId);
%>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />
<%

	NoticeMessage[] noticeMessages = noticeBox.getNoticeMessages();
	if (noticeMessages != null) {
		String lastTaskId = null;
		int count = 0;
		for (NoticeMessage nMessage : (NoticeMessage[]) noticeBox.getNoticeMessages()) {
			count++;
			if (noticeBox != null && noticeBox.getNoticeType() == Notice.TYPE_MESSAGE) {
				AsyncMessageInstanceInfo messageInstance = (AsyncMessageInstanceInfo) nMessage.getInstance();
				if(count == 10) lastTaskId = messageInstance.getId();
				UserInfo owner = messageInstance.getSender();
%>
				<ul>
				<li>
				<div class="info_ms_section">
					<div class="info_img">
						<a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>" title="<%=owner.getLongName()%>"> <img src="<%=owner.getMinPicture()%>"  class="profile_size_s"> </a>
					</div>
					<a href="" class="js_start_chat_with_user" chatId="<%=messageInstance.getChatId() %>" userId="<%=owner.getId() %>" longName="<%=owner.getLongName() %>" minPicture="<%=owner.getMinPicture() %>">
						<div class="info_list"><%=messageInstance.getMessage()%>
							<div class="t_date"><%=messageInstance.getSendDate().toLocalString()%>
								<a href="" noticeId=<%=nMessage.getId() %> noticeType="<%=noticeType%>" lastNoticeId=<%=lastNoticeId %>>
									<div class="btn_x js_remove_notice" ></div></a>
							</div>
						</div>
					</a>
					</div>
				</li>
				</ul>
<%
			}
		}
		if(noticeBox.getRemainingLength() > 0 && !SmartUtil.isBlankObject(lastTaskId)){
%>
			<ul>
				<li class="tc pt2">
					<a class="js_more_notice_list" href="message_list_box.sw" lastTaskId="<%=lastTaskId%>"><fmt:message key="common.message.more_work_task"/></a>
					<span class="js_progress_span"></span>
				</li>
			</ul>
<%
		}
	}
%>
