
<!-- Name 			: savedbox_list_box.jsp													 -->
<!-- Description	: 화면구성중에 Header 에서 현재사용자가 임시저장해놓은 항목의 목록들을 보여주는 박스 	 -->
<!-- Author			: Maninsoft, Inc.														 -->
<!-- Created Date	: 2011.9.																 -->

<%@page import="net.smartworks.model.work.info.WorkInfo"%>
<%@page import="net.smartworks.model.instance.info.WorkInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.TaskInstanceInfo"%>
<%@page import="net.smartworks.model.mail.MailFolder"%>
<%@page import="net.smartworks.model.community.info.WorkSpaceInfo"%>
<%@page import="net.smartworks.model.work.info.SmartWorkInfo"%>
<%@page import="net.smartworks.model.instance.info.InstanceInfo"%>
<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.instance.info.MailInstanceInfo"%>
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
	int noticeType = Notice.TYPE_SAVEDBOX;

	// 서버에게 lastNoticeId를 기준으로 최근 10개의 Notice항목을 가져오는 기능.
	NoticeBox noticeBox = smartWorks.getNoticeBoxForMe10(noticeType, lastNoticeId);
%>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />
<%

	String draftBoxId = smartWorks.getFolderIdByType(MailFolder.TYPE_SYSTEM_DRAFTS);
	NoticeMessage[] noticeMessages = noticeBox.getNoticeMessages();
	if (noticeMessages != null) {
		String lastTaskId = null;
		int count = 0;
		for (NoticeMessage nMessage : (NoticeMessage[]) noticeBox.getNoticeMessages()) {
			count++;
			if (noticeBox != null && noticeBox.getNoticeType() == Notice.TYPE_SAVEDBOX) {
				InstanceInfo instance = nMessage.getInstance();
				if(count == 10) lastTaskId = instance.getId();
//				SmartWorkInfo work = (SmartWorkInfo)instance.getWork();
				String workId = instance.getWorkId();
				String workName = instance.getWorkName();
				int workType = instance.getWorkType();
				boolean isWorkRunning = instance.isWorkRunning();
				String workFullPathName = instance.getWorkFullPathName();
				if(instance.getType() == Instance.TYPE_TASK){
//					work = (SmartWorkInfo)((TaskInstanceInfo)instance).getWorkInstance().getWork();
					workId = ((TaskInstanceInfo)instance).getWorkInstance().getWorkId();
					workName = ((TaskInstanceInfo)instance).getWorkInstance().getWorkName();
					workType = ((TaskInstanceInfo)instance).getWorkInstance().getWorkType();
					isWorkRunning = ((TaskInstanceInfo)instance).getWorkInstance().isWorkRunning();
					workFullPathName = ((TaskInstanceInfo)instance).getWorkInstance().getWorkFullPathName();
				}
//				WorkSpaceInfo workSpace = instance.getWorkSpace();
				String workSpaceId = instance.getWorkSpaceId();
				String workSpaceName = instance.getWorkSpaceName();
				int workSpaceType = instance.getWorkSpaceType();
				UserInfo owner = instance.getOwner();
				String targetContent = "", iconType="";
				switch(instance.getType()){
				
				// 업무인스턴스인 경우에는 정보관리업무만 임시저장 기능을 제공한다.
				case Instance.TYPE_WORK:
					WorkInstanceInfo workInstance = (WorkInstanceInfo)instance;
					targetContent = workInstance.getController() + "?cid=" + workInstance.getContextId();
					break;
				
				// 태스크인스턴스인 경우에는 프로세스업무나 일정계획업무가 포함된다.
				case Instance.TYPE_TASK:
					TaskInstanceInfo taskInstance = (TaskInstanceInfo)instance;
					targetContent = taskInstance.getController() + "?cid=" + taskInstance.getContextId();
					break;
				
				// 이메일인 경우에는 이메일 임시저장함에 저장 관리 된다.
				case Instance.TYPE_MAIL:
					targetContent = "mail_space.sw?folderId=" + draftBoxId + "msgId=" + instance.getId();
					break;
				}
%>
				<ul>
				<li>
				<div class="info_ms_section">
					<div class="info_img">
						<div class="<%=Work.getIconClass(workId, workType, isWorkRunning)%>"><%=workName%></div>
					</div>
					<div class="info_list ml15">
						<a href="<%=targetContent %>" ><%=instance.getSubject()%></a>		
						<div class="t_date"><%=instance.getCreatedDate().toLocalString()%></div>
					</div>
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
					<a class="js_more_notice_list" href="saved_list_box.sw" lastTaskId="<%=lastTaskId%>"><fmt:message key="common.message.more_work_task"/></a>
					<span class="js_progress_span"></span>
				</li>
			</ul>
<%
		}
	}
%>
