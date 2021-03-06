<%@page import="net.smartworks.model.work.info.WorkInfo"%>
<%@page import="net.smartworks.model.community.info.InstanceSpaceInfo"%>
<%@page import="net.smartworks.model.instance.info.CommentInstanceInfo"%>
<%@page import="net.smartworks.model.instance.WorkInstance"%>
<%@page import="net.smartworks.util.SmartTest"%>
<%@page import="net.smartworks.model.instance.info.InstanceInfo"%>
<%@page import="net.smartworks.model.community.info.GroupInfo"%>
<%@page import="net.smartworks.model.community.info.DepartmentInfo"%>
<%@page import="net.smartworks.model.work.info.SmartWorkInfo"%>
<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.model.instance.info.MemoInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.ImageInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.FileInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.EventInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.BoardInstanceInfo"%>
<%@page import="net.smartworks.model.community.info.WorkSpaceInfo"%>
<%@page import="net.smartworks.model.instance.Instance"%>
<%@page import="net.smartworks.model.work.SocialWork"%>
<%@page import="net.smartworks.model.work.Work"%>
<%@page import="net.smartworks.model.work.SmartWork"%>
<%@page import="net.smartworks.model.instance.info.WorkInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.TaskInstanceInfo"%>
<%@page import="net.smartworks.model.instance.TaskInstance"%>
<%@page import="net.smartworks.model.calendar.CompanyEvent"%>
<%@page import="net.smartworks.util.SmartMessage"%>
<%@page import="net.smartworks.model.calendar.WorkHourPolicy"%>
<%@page import="net.smartworks.model.community.WorkSpace"%>
<%@page import="java.util.Calendar"%>
<%@page import="net.smartworks.model.calendar.CompanyCalendar"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();

	InstanceInfo[] subInstances = (InstanceInfo[])session.getAttribute("subInstances");
	String instanceId = (String)session.getAttribute("instanceId");
	String wsId = (String)session.getAttribute("workSpaceId");
	
%>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

	<%
	if(!SmartUtil.isBlankObject(subInstances) && subInstances.length > 0){
		for(int i=0; i<subInstances.length; i++){
			InstanceInfo workInstance = subInstances[i];
			if(SmartUtil.isBlankObject(workInstance)){
				String lastDateStr = (i>0) ? (new LocalDate(subInstances[i-1].getLastModifiedDate().getTime())).toLocalDateString2() : ""; 
	%>
				<li class="t_nowork">
					<a href="" class="js_space_more_instance" lastDate="<%=lastDateStr%>" maxSize="<%=WorkInstance.MAX_INSTANCE_SUB_INSTANCE%>"><fmt:message key="common.message.more_work_task"></fmt:message></a>
					<span class="js_progress_span"></span>
				</li>
	<%
				break;
			}
//			SmartWorkInfo work = (SmartWorkInfo)workInstance.getWork();
			String workId = workInstance.getWorkId();
			String workName = workInstance.getWorkName();
			int workType = workInstance.getWorkType();
			boolean isWorkRunning = workInstance.isWorkRunning();
			String workFullPathName = workInstance.getWorkFullPathName();
			UserInfo owner = workInstance.getOwner();
			String userDetailInfo = SmartUtil.getUserDetailInfo(owner);
//			WorkSpaceInfo workSpace = workInstance.getWorkSpace();
			String workSpaceId = workInstance.getWorkSpaceId();
			String workSpaceName = workInstance.getWorkSpaceName();
			int workSpaceType = workInstance.getWorkSpaceType();
			if(SmartUtil.isBlankObject(workSpaceId)){
				workSpaceId = workInstance.getOwner().getId();
				workSpaceName = workInstance.getOwner().getName();
				workSpaceType = workInstance.getOwner().getSpaceType();
			}
			boolean onWorkSpace = false;
			if(workSpaceType != ISmartWorks.SPACE_TYPE_USER && !workSpaceId.equals(wsId) )
				onWorkSpace = true;
			BoardInstanceInfo board=null;
			EventInstanceInfo event=null;
			FileInstanceInfo file=null;
			ImageInstanceInfo image=null;
			MemoInstanceInfo memo=null;
			CommentInstanceInfo comment=null;
			
			boolean isUpdatedInstance = (workInstance.getCreatedDate().getTime() != workInstance.getLastModifiedDate().getTime());
			if(isUpdatedInstance && !SmartUtil.isBlankObject(workInstance.getLastModifier()))
				owner = workInstance.getLastModifier();
			
	%>
			<li class="sub_instance_list js_sub_instance_list js_space_sub_instance" instanceId="<%=workInstance.getId() %>"  workType="<%=workType%>">
				<%
				switch(workInstance.getType()){
				
				// 태스크가 게시판인 경우...									
				case Instance.TYPE_BOARD:
					board = (BoardInstanceInfo)workInstance;
				%>
					<div class="det_title">
						<div class="noti_pic"><a class="js_pop_user_info" href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>&wid=<%=owner.getId() %>" userId="<%=owner.getId()%>" longName="<%=owner.getLongName() %>" minPicture="<%=owner.getMinPicture() %>" profile="<%=owner.getOrgPicture()%>" userDetail="<%=userDetailInfo%>"><img src="<%=owner.getMidPicture()%>" class="profile_size_m"></a></div>
						<div class="noti_in_m">
							<%
							if(cUser.getId().equals(owner.getId())){
							%>
								<span class="t_name"><%=owner.getLongName()%></span>
							<%
							}else{
							%>
								<a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>&wid=<%=owner.getId()%>"><span class="t_name"><%=owner.getLongName()%></span></a>
							<%
							}
							%>
							<%if(isUpdatedInstance){%><fmt:message key="content.sentence.itask_updated">
								<fmt:param>
									<a class="js_content" href='<%=board.getController() %>?cid=<%=board.getContextId() %>&wid=<%=workSpaceId %>&workId=<%=workId %>'>
										<span class="t_woname"><%=workInstance.getWorkName()%></span>
									</a>
								</fmt:param>
							</fmt:message><%}%>
							<!-- 인스턴스 마지막수정일자 -->
							<span class="t_date vb pl10"><%=workInstance.getLastModifiedDate().toLocalString()%></span>
							<!-- 인스턴스 마지막수정일자 //-->
							<a href="<%=board.getController() %>?cid=<%=board.getContextId() %>&wid=<%=workSpaceId %>&workId=<%=workId %>">
								<div>
									<span class="<%=Work.getIconClass(workId, workType, isWorkRunning)%>"></span>
									<div><%=board.getSubject() %>
									<%if(board.isNew()){ %><span class="icon_new"></span><%} %>
									</div>
								</div>
								<div><%=board.getBriefContent()%></div>
							</a>
							<%if(!SmartUtil.isBlankObject(board.getFiles())){ %><div><%=SmartUtil.getFilesDetailInfo(board.getFiles(), workId, null, board.getId()) %></div><%} %>
						</div>
					</div>
				<%
					break;
				// 태스크가 이벤트인 경우...									
				case Instance.TYPE_EVENT:
					event = (EventInstanceInfo)workInstance;
				%>
					<div class="det_title">
						<div class="noti_pic"><a class="js_pop_user_info" href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>&wid=<%=owner.getId() %>" userId="<%=owner.getId()%>" longName="<%=owner.getLongName() %>" minPicture="<%=owner.getMinPicture() %>" profile="<%=owner.getOrgPicture()%>" userDetail="<%=userDetailInfo%>"><img src="<%=owner.getMidPicture()%>" class="profile_size_m"></a></div>
						<div class="noti_in_m">
							<%
							if(cUser.getId().equals(owner.getId())){
							%>
								<span class="t_name"><%=owner.getLongName()%></span>
							<%
							}else{
							%>
								<a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>&wid=<%=owner.getId()%>"><span class="t_name"><%=owner.getLongName()%></span></a>
							<%
							}
							%>
							<%if(isUpdatedInstance){%><fmt:message key="content.sentence.itask_updated">
								<fmt:param>
									<a class="js_content" href='<%=event.getController() %>?cid=<%=event.getContextId() %>&wid=<%=workSpaceId %>&workId=<%=workId %>'>
										<span class="t_woname"><%=workInstance.getWorkName()%></span>
									</a>
								</fmt:param>
							</fmt:message><%}%>
							<!-- 인스턴스 마지막수정일자 -->
							<span class="t_date vb pl10"><%=workInstance.getLastModifiedDate().toLocalString()%></span>
							<!-- 인스턴스 마지막수정일자 //-->
							
							<a href="<%=event.getController() %>?cid=<%=event.getContextId() %>&wid=<%=workSpaceId %>&workId=<%=workId %>">
								<div>
									<span class="<%=Work.getIconClass(workId, workType, isWorkRunning)%>"></span>
									<div><%=event.getSubject() %>
										<%if(event.isNew()){ %><span class="icon_new"></span><%} %>
									</div>
								</div>
								<div><fmt:message key="common.upload.event.start_date"/> : <%=event.getStart().toLocalString() %> 
									<%if(!SmartUtil.isBlankObject(event.getEnd())) {%><fmt:message key="common.upload.event.end_date"/> : <%=event.getEnd().toLocalString() %> <%} %></div>
							</a>
						</div>
					</div>
				<%
					break;
				// 태스크가 파일인 경우...									
				case Instance.TYPE_FILE:
					file = (FileInstanceInfo)workInstance;
				%>
					<div class="det_title">
						<div class="noti_pic"><a class="js_pop_user_info" href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>&wid=<%=owner.getId() %>" userId="<%=owner.getId()%>" longName="<%=owner.getLongName() %>" minPicture="<%=owner.getMinPicture() %>" profile="<%=owner.getOrgPicture()%>" userDetail="<%=userDetailInfo%>"><img src="<%=owner.getMidPicture()%>" class="profile_size_m"></a></div>
						<div class="noti_in_m">
							<%
							if(cUser.getId().equals(owner.getId())){
							%>
								<span class="t_name"><%=owner.getLongName()%></span>
							<%
							}else{
							%>
								<a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>&wid=<%=owner.getId()%>"><span class="t_name"><%=owner.getLongName()%></span></a>
							<%
							}
							%>
							<%if(isUpdatedInstance){%><fmt:message key="content.sentence.itask_updated">
								<fmt:param>
									<a class="js_content" href='<%=file.getController() %>?cid=<%=file.getContextId() %>&wid=<%=workSpaceId %>&workId=<%=workId %>'>
										<span class="t_woname"><%=workInstance.getWorkName()%></span>
									</a>
								</fmt:param>
							</fmt:message><%}%>
							<!-- 인스턴스 마지막수정일자 -->
							<span class="t_date vb pl10"><%=workInstance.getLastModifiedDate().toLocalString()%></span>
							<!-- 인스턴스 마지막수정일자 //-->
							
							<div>
								<a class="js_content" href='<%=file.getController() %>?cid=<%=file.getContextId() %>&wid=<%=workSpaceId %>&workId=<%=workId %>'><%=file.getSubject() %></a>
								<%if(file.isNew()){ %><span class="icon_new"></span><%} %>
							</div>
							<%if(!SmartUtil.isBlankObject(file.getFiles())){ %><div><%=SmartUtil.getFilesDetailInfo(file.getFiles(), workId, null, file.getId()) %></div><%} %>
							<%if(!SmartUtil.isBlankObject(file.getContent())){ %><div><%=file.getContent() %></div><%} %>
						</div>
					</div>
				<%
					break;
				// 태스크가 사진인 경우...									
				case Instance.TYPE_IMAGE:
					image = (ImageInstanceInfo)workInstance;
				%>
					<div class="det_title">
						<div class="noti_pic"><a class="js_pop_user_info" href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>&wid=<%=owner.getId() %>" userId="<%=owner.getId()%>" longName="<%=owner.getLongName() %>" minPicture="<%=owner.getMinPicture() %>" profile="<%=owner.getOrgPicture()%>" userDetail="<%=userDetailInfo%>"><img src="<%=owner.getMidPicture()%>" class="profile_size_m"></a></div>
						<div class="noti_in_m">
							<%
							if(cUser.getId().equals(owner.getId())){
							%>
								<span class="t_name"><%=owner.getLongName()%></span>
							<%
							}else{
							%>
								<a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>&wid=<%=owner.getId()%>"><span class="t_name"><%=owner.getLongName()%></span></a>
							<%
							}
							%>
							<%if(isUpdatedInstance){%><fmt:message key="content.sentence.itask_updated">
								<fmt:param>
									<a class="js_content" href='<%=image.getController() %>?cid=<%=image.getContextId() %>&wid=<%=workSpaceId %>&workId=<%=workId %>'>
										<span class="t_woname"><%=workInstance.getWorkName()%></span>
									</a>
								</fmt:param>
							</fmt:message><%}%>
							<!-- 인스턴스 마지막수정일자 -->
							<span class="t_date vb pl10"><%=workInstance.getLastModifiedDate().toLocalString()%></span>
							<!-- 인스턴스 마지막수정일자 //-->
							
							<div>
								<a class="js_show_picture_detail" instanceId="<%=image.getId()%>" href="">
								<img src="<%=image.getImgSource()%>" style="max-width:200px"></a>
								<%if(image.isNew()){ %><span class="icon_new vc"></span><%} %>
							</div>
							<%if(!SmartUtil.isBlankObject(image.getContent())){ %><div><%=image.getContent() %></div><%} %>
						</div>
					</div>
				<%
					break;
				// 태스크가 메모인 경우...									
				case Instance.TYPE_MEMO:
					memo = (MemoInstanceInfo)workInstance;
				%>
					<div class="det_title">
						<div class="noti_pic"><a class="js_pop_user_info" href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>&wid=<%=owner.getId() %>" userId="<%=owner.getId()%>" longName="<%=owner.getLongName() %>" minPicture="<%=owner.getMinPicture() %>" profile="<%=owner.getOrgPicture()%>" userDetail="<%=userDetailInfo%>"><img src="<%=owner.getMidPicture()%>" class="profile_size_m"></a></div>
						<div class="noti_in_m">
							<%
							if(cUser.getId().equals(owner.getId())){
							%>
								<span class="t_name"><%=owner.getLongName()%></span>
							<%
							}else{
							%>
								<a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>&wid=<%=owner.getId()%>"><span class="t_name"><%=owner.getLongName()%></span></a>
							<%
							}
							%>
							<%if(isUpdatedInstance){%><fmt:message key="content.sentence.itask_updated">
								<fmt:param>
									<a class="js_content" href='<%=memo.getController() %>?cid=<%=memo.getContextId() %>&wid=<%=workSpaceId %>&workId=<%=workId %>'>
										<span class="t_woname"><%=workInstance.getWorkName()%></span>
									</a>
								</fmt:param>
							</fmt:message><%}%>
							<!-- 인스턴스 마지막수정일자 -->
							<span class="t_date vb pl10"><%=workInstance.getLastModifiedDate().toLocalString()%></span>
							<!-- 인스턴스 마지막수정일자 //-->
							<a href="<%=memo.getController() %>?cid=<%=memo.getContextId() %>&wid=<%=workSpaceId %>&workId=<%=workId %>">
								<div>
									<span class="<%=Work.getIconClass(workId, workType, isWorkRunning)%>"></span>
									<div><%=memo.getContent() %>
										<%if(memo.isNew()){ %><span class="icon_new"></span><%} %>
									</div>
								</div>
							</a>
						</div>
					</div>
				<%
				break;
				// 태스크가 댓글인 경우...									
				case Instance.TYPE_COMMENT:
					comment = (CommentInstanceInfo)workInstance;
			%>
				<div class="det_title">
					<div class="noti_pic">
						<a class="js_pop_user_info" href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>&wid=<%=owner.getId() %>" userId="<%=owner.getId()%>" longName="<%=owner.getLongName() %>" minPicture="<%=owner.getMinPicture() %>" profile="<%=owner.getOrgPicture()%>" userDetail="<%=userDetailInfo%>">
							<img src="<%=owner.getMidPicture()%>" class="profile_size_m">
						</a>
					</div>
					<div class="noti_in_m">
						<%
						if(comment.getOwner().getId().equals(cUser.getId())){
						%>	
							<div class="delet_action">
							<a href="" class=" js_delete_comment_btn" title="<fmt:message key='common.title.deleted'/>">
								<div class="btn_x fr"></div>
							</a>
							</div>
						<%
						}
						%>
						<a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>&wid=<%=owner.getId()%>">
							<span class="t_name"><%=owner.getLongName()%></span>
						</a>
						<!-- 인스턴스 마지막 수정일자 -->
						<span class="t_date pl10"><%=workInstance.getLastModifiedDate().toLocalString()%></span>
						<!-- 인스턴스 마지막 수정일자 // -->
						<div>
						<%=comment.getComment() %>
							<%if(workInstance.isNew()){ %><span class="icon_new"></span><%} %>
						</div>
					</div>
				</div>
			<%
				break;
				default:
			%>
					<div class="det_title">
						<div class="noti_pic"><a class="js_pop_user_info" href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>&wid=<%=owner.getId() %>" userId="<%=owner.getId()%>" longName="<%=owner.getLongName() %>" minPicture="<%=owner.getMinPicture() %>" profile="<%=owner.getOrgPicture()%>" userDetail="<%=userDetailInfo%>"><img src="<%=owner.getMidPicture()%>" class="profile_size_m"></a></div>
						<div class="noti_in_m">
							<%
							if(cUser.getId().equals(owner.getId())){
							%>
								<span class="t_name"><%=owner.getLongName()%></span>
							<%
							}else{
							%>
								<a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>&wid=<%=owner.getId()%>"><span class="t_name"><%=owner.getLongName()%></span></a>
							<%
							}
							%>
							<%if(isUpdatedInstance){%><fmt:message key="content.sentence.itask_updated">
								<fmt:param>
									<a class="js_content" href='<%=((WorkInstanceInfo)workInstance).getController() %>?cid=<%=((WorkInstanceInfo)workInstance).getContextId() %>&wid=<%=workSpaceId %>&workId=<%=workId %>'>
										<span class="t_woname"><%=workInstance.getWorkName()%></span>
									</a>
								</fmt:param>
							</fmt:message><%}%>
							<!-- 인스턴스 마지막 수정일자 -->
							<span class="t_date pl10"><%=workInstance.getLastModifiedDate().toLocalString()%></span>
							<!-- 인스턴스 마지막 수정일자 // -->
							<div>
								<a class="js_content" href='<%=((WorkInstanceInfo)workInstance).getController()%>?cid=<%=((WorkInstanceInfo)workInstance).getContextId()%>&wid=<%=workSpaceId%>&workId=<%=workId%>'>
									<span class="<%=Work.getIconClass(workId, workType, isWorkRunning)%>"></span>
									<span class="t_date"><%=workFullPathName%></span>
								</a>
								<a href="<%=((WorkInstanceInfo)workInstance).getController()%>?cid=<%=((WorkInstanceInfo)workInstance).getContextId()%>&wid=<%=workSpaceId%>&workId=<%=workId%>">
									<!-- 전자결재 아이콘: <span class="icon_txt blue">전자결재</span> -->
									<span class="tb"><%=workInstance.getSubject()%>
									<%if(workInstance.isNew()){ %><span class="icon_new"></span><%} %>
									</span> 
								</a>
							</div>
						</div>
					</div>
				<%
				}
	        	if(workInstance.getType() != Instance.TYPE_COMMENT) {
		        	WorkInstanceInfo instance = (WorkInstanceInfo)workInstance;
				%>
				<div class="js_comments_box" <%if(instance.getSubInstanceCount()==0){%>style="display:none"<%} %>>
					<!-- 댓글 -->
				   <div class="reply_point pos_reply_point"></div>
				   <div class="reply_section pos_reply">  
				        <div class="list_reply">
				            <ul class="js_comment_list">
				            	<div>
					            	<li class="comment_list js_comment_instance" style="display:none">
										<div class="noti_pic">
											<a class="js_pop_user_info" href="<%=cUser.getSpaceController() %>?cid=<%=cUser.getSpaceContextId()%>&wid=<%=cUser.getId() %>" userId="<%=cUser.getId()%>" longName="<%=owner.getLongName() %>" minPicture="<%=owner.getMinPicture() %>" profile="<%=cUser.getOrgPicture()%>" userDetail="<%=SmartUtil.getUserDetailInfo(cUser.getUserInfo())%>">
												<img src="<%=cUser.getMinPicture()%>" align="bottom" class="profile_size_c"/>
											</a>
										</div>
										<div class="noti_in">
											<span class="t_name"><%=cUser.getLongName()%></span>
											<span	class="t_date pl10"><%=(new LocalDate()).toLocalString()%></span>
											<div class="js_comment_content"></div>
										</div>
					            	</li>
				            	</div>
				            	<%
								if(instance.getSubInstanceCount() > 0) {
									session.setAttribute("subComments", instance.getSubInstances());
								%>
	 								<jsp:include page="/jsp/content/work/list/sub_instances_in_instance.jsp" >
										<jsp:param value="<%=instance.getId() %>" name="instanceId"/>
										<jsp:param value="<%=instance.getSubInstanceCount() %>" name="instanceCount"/>
										<jsp:param value="<%=WorkInstance.DEFAULT_SUB_INSTANCE_FETCH_COUNT %>" name="fetchCount"/>
									</jsp:include>
	 							<%
								}
								%>
							</ul>
				        </div>				        
				        <div class="reply_input js_return_on_comment" style="display:none">
							<div class="noti_pic">
								<img src="<%=cUser.getMinPicture()%>" class="profile_size_c"/>
							</div>
							<div class="noti_in">
								<textarea style="width:560px" class="up_textarea" name="txtaCommentContent" placeholder="<fmt:message key='work.message.leave_comment'/>"></textarea>
							</div>
				        </div>				    
				    </div>
				    <!-- 댓글 //-->
				</div>
				    <div class="btns_action js_action_btns">
				    	<a class="js_add_comment" href=""><span class="t_action"><fmt:message key="common.button.add_comment"/></span></a> 
				    	<a class="js_add_like" href=""><span class="t_action"><fmt:message key="common.button.add_like"/></span></a>
				    </div>
				<%
				}
				%>
			</li>
	<%		
		}
	}
	%>
			