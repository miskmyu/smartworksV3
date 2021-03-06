
<!-- Name 			: comment_list_box.jsp									 -->
<!-- Description	: 화면구성중에 Header 에서 새로운 댓글 목록들을 보여주는 박스 	 -->
<!-- Author			: Maninsoft, Inc.										 -->
<!-- Created Date	: 2011.9.												 -->

<%@page import="net.smartworks.model.work.info.SmartWorkInfo"%>
<%@page import="net.smartworks.model.instance.info.WorkInstanceInfo"%>
<%@page import="net.smartworks.model.community.info.GroupInfo"%>
<%@page import="net.smartworks.model.community.info.DepartmentInfo"%>
<%@page import="net.smartworks.model.community.info.WorkSpaceInfo"%>
<%@page import="net.smartworks.model.instance.info.TaskInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.InstanceInfo"%>
<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.model.work.info.WorkInfo"%>
<%@page import="net.smartworks.model.instance.info.CommentInstanceInfo"%>
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
	int noticeType = Notice.TYPE_COMMENT;
	NoticeBox noticeBox = smartWorks.getNoticeBoxForMe10(noticeType, lastNoticeId);
%>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />
<%

	// 서버에게 lastNoticeId를 기준으로 최근 10개의 Notice항목을 가져오는 기능.
	NoticeMessage[] noticeMessages = (noticeBox==null) ? null : noticeBox.getNoticeMessages();
	if (noticeMessages != null) {
		String lastTaskId = null;
		int count = 0;
		for (NoticeMessage nMessage : (NoticeMessage[]) noticeBox.getNoticeMessages()) {
			count++;
			if (noticeBox != null && noticeBox.getNoticeType() == Notice.TYPE_COMMENT) {
				CommentInstanceInfo commentInstance = (CommentInstanceInfo) nMessage.getInstance();
				if(count == 10) lastTaskId = commentInstance.getId();
				UserInfo owner = commentInstance.getOwner();
//				WorkInfo work = null;

				// 업무 매뉴얼에 댓글로 의견을 남긴 경우  
				if (commentInstance.getCommentType() == CommentInstance.COMMENT_TYPE_ON_WORK_MANUAL) {
//					work = commentInstance.getWork();
					String workId = commentInstance.getWorkId();
					String workName = commentInstance.getWorkName();
					int workType = commentInstance.getWorkType();
					boolean isWorkRunning = commentInstance.isWorkRunning();
					String workFullPathName = commentInstance.getWorkFullPathName();
					owner = commentInstance.getOwner();
%>
					<ul>
					<li>
						<div class="info_ms_section">
							<a href="<%=WorkInfo.getController(workId, workType)%>?cid=<%=WorkInfo.getContextId(workId, workType)%>&workId=<%=workId %>">
								<span class="<%=Work.getIconClass(workId, workType, isWorkRunning)%>"></span>
								<span><%=workName%></span>
							</a>
							<span class="t_date vb pl10 fr"><%=commentInstance.getLastModifiedDate().toLocalString() %></span>
							<div class="reply">
								<div class="info_img">
									<a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>&workId=<%=workId %>" title="<%=owner.getLongName()%>">
										<img src="<%=owner.getMinPicture()%>" class="profile_size_s"> </a>
								</div>
								<div class="info_list">
									<div><%=commentInstance.getComment()%>
										<a href="" noticeId=<%=nMessage.getId() %> noticeType="<%=noticeType%>" lastNoticeId=<%=lastNoticeId %>>
											<div class="btn_x js_remove_notice" ></div></a>
									</div>
								</div>
							</div>
						</div>
					</li>
				<%
				// 업무 인스턴스 공간에 댓글을 남긴 경우   
				} else if (commentInstance.getCommentType() == CommentInstance.COMMENT_TYPE_ON_WORK_INSTANCE) {
//					work = commentInstance.getWorkInstance().getWork();
					String workId = commentInstance.getWorkInstance().getWorkId();
					String workName = commentInstance.getWorkInstance().getWorkName();
					int workType = commentInstance.getWorkInstance().getWorkType();
					boolean isWorkRunning = commentInstance.getWorkInstance().isWorkRunning();
					String workFullPathName = commentInstance.getWorkInstance().getWorkFullPathName();
					WorkInstanceInfo workInstance = (WorkInstanceInfo)commentInstance.getWorkInstance();
				%>
					<li>
					<div class="info_ms_section">
							<span class="<%=Work.getIconClass(workId, workType, isWorkRunning)%>"></span>
							<%
							if(workInstance.getWorkId().equals(SmartWork.ID_BOARD_MANAGEMENT)){
							}else if(workInstance.getWorkId().equals(SmartWork.ID_EVENT_MANAGEMENT)){
							}else if(workInstance.getWorkId().equals(SmartWork.ID_FILE_MANAGEMENT)){
							}else if(workInstance.getWorkId().equals(SmartWork.ID_MEMO_MANAGEMENT)){
							}else{									
							%>
								<span><%=workInstance.getWorkName() %></span>
							<%
							}
							%>
						<a href="<%=workInstance.getController()%>?cid=<%=workInstance.getContextId() %>&workId=<%=workId %>">
							<span class="tb"><%=workInstance.getSubject()%></span> 
						</a>
						<span class="t_date vb pl10 fr"><%=commentInstance.getLastModifiedDate().toLocalString() %></span>
						<div class="reply">
							<div class="info_img">
								<a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>&workId=<%=workId %>"title="<%=owner.getLongName()%>">
									<img src="<%=owner.getMinPicture()%>"  class="profile_size_s"> </a>
							</div>
							<div class="info_list">
								<div><%=commentInstance.getComment()%>
									<a href="" noticeId=<%=nMessage.getId() %> noticeType="<%=noticeType%>" lastNoticeId=<%=lastNoticeId %>>
										<div class="btn_x js_remove_notice" ></div></a>
								</div>
							</div>
						</div>
					</div>
					</li>
				<%
				// Community 및 업무 공간에 댓글을 남긴 경우   
				} else if (commentInstance.getCommentType() == CommentInstance.COMMENT_TYPE_ON_WORK_SPACE) {
					String workId = commentInstance.getWorkId();
					String workName = commentInstance.getWorkName();
					int workType = commentInstance.getWorkType();
					boolean isWorkRunning = commentInstance.isWorkRunning();
					String workFullPathName = commentInstance.getWorkFullPathName();
//					WorkSpaceInfo workSpace = commentInstance.getWorkSpace();
					String workSpaceId = commentInstance.getWorkSpaceId();
					String workSpaceName = commentInstance.getWorkSpaceName();
					int workSpaceType = commentInstance.getWorkSpaceType();
					String workSpaceMinPicture = commentInstance.getWorkSpaceMinPicture();
					WorkInfo workSpaceInstanceWork = commentInstance.getWorkSpaceInstanceWork();
				%>
					<li>
					<div class="info_ms_section">
						<a href="<%=WorkSpaceInfo.getSpaceController(workSpaceType, workSpaceInstanceWork)%>?cid=<%=WorkSpaceInfo.getSpaceContextId(workSpaceType, workSpaceId, workSpaceInstanceWork)%>&workId=<%=workId %>">
							<span class="profile_size_m"><%=workSpaceMinPicture%></span>
							<span><%=workSpaceName%></span>
						</a>	
						<span class="t_date vb pl10 fr"><%=commentInstance.getLastModifiedDate().toLocalString() %></span>
						<div class="reply">				
							<div class="info_img">
								<a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>&workId=<%=workId %>" title="<%=owner.getLongName()%>">
									<img src="<%=owner.getMinPicture()%>"  class="profile_size_s"> </a>
							</div>
							<div class="info_list">
								<div><%=commentInstance.getComment()%>
									<a href="" noticeId=<%=nMessage.getId() %> noticeType="<%=noticeType%>" lastNoticeId=<%=lastNoticeId %>>
										<div class="btn_x js_remove_notice" ></div></a>
								</div>
							</div>
						</div>
					</div>
					</li>
					</ul>
	<%
				}
			}
		}
		if(noticeBox.getRemainingLength() > 0 && !SmartUtil.isBlankObject(lastTaskId)){
%>
			<ul>
				<li class="tc pt2">
					<a class="js_more_notice_list" href="comment_list_box.sw" lastTaskId="<%=lastTaskId%>"><fmt:message key="common.message.more_work_task"/></a>
					<span class="js_progress_span"></span>
				</li>
			</ul>
<%
		}
	}
	%>
