<%@page import="net.smartworks.model.instance.WorkInstance"%>
<%@page import="net.smartworks.model.work.info.WorkInfo"%>
<%@page import="net.smartworks.model.instance.info.MemoInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.ImageInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.FileInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.EventInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.BoardInstanceInfo"%>
<%@page import="net.smartworks.model.community.info.GroupInfo"%>
<%@page import="net.smartworks.model.community.info.DepartmentInfo"%>
<%@page import="net.smartworks.model.community.info.WorkSpaceInfo"%>
<%@page import="net.smartworks.model.instance.info.WorkInstanceInfo"%>
<%@page import="net.smartworks.model.work.info.SmartWorkInfo"%>
<%@page import="net.smartworks.model.instance.info.CommentInstanceInfo"%>
<%@page import="net.smartworks.model.instance.Instance"%>
<%@page import="net.smartworks.model.instance.info.InstanceInfo"%>
<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.model.work.SmartWork"%>
<%@page import="net.smartworks.model.work.Work"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();

	String instanceId = request.getParameter("instanceId");
	String instanceCountStr = request.getParameter("instanceCount");
	int instanceCount = (instanceCountStr==null) ? 0 : Integer.parseInt(instanceCountStr);
	int fetchCount = Integer.parseInt(request.getParameter("fetchCount"));
	String toDate = request.getParameter("toDate");
	LocalDate to = SmartUtil.isBlankObject(toDate) ? null : LocalDate.convertLocalStringToLocalDate(toDate);
	InstanceInfo[] subInstances = (InstanceInfo[])session.getAttribute("subComments");
	if(SmartUtil.isBlankObject(subInstances)){
		subInstances = smartWorks.getSubInstancesInInstance(instanceId, fetchCount, to);
		instanceCount = smartWorks.getSubInstancesInInstanceCount(instanceId);
	}
	
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<%
if (subInstances != null) {
	String lastToDate = "";
	if(SmartUtil.isBlankObject(subInstances[subInstances.length-1])){
		lastToDate = subInstances[0].getLastModifiedDate().toLocalDateString2();
%>
		<li>
			<img class="repl_tinfo">
			<a href="sub_instances_in_instance.sw?instanceId=<%=instanceId %>&fetchCount=<%=WorkInstance.FETCH_MORE_SUB_INSTANCE %>&toDate=<%=lastToDate %>" class="js_show_more_comments">
				<span><strong><fmt:message key="common.title.show_more_comments"><fmt:param><%=instanceCount %></fmt:param><</fmt:message></strong></span>
			</a>
		</li>
<%
	}
}
%>
<%
if(subInstances != null) {
	for (InstanceInfo workInstance : subInstances) {
		if(SmartUtil.isBlankObject(workInstance)) break;
//		SmartWorkInfo work = (SmartWorkInfo)workInstance.getWork();
		String workId = workInstance.getWorkId();
		String workName = workInstance.getWorkName();
		int workType = workInstance.getWorkType();
		boolean isWorkRunning = workInstance.isWorkRunning();
		String workFullPathName = workInstance.getWorkFullPathName();
		UserInfo owner = workInstance.getOwner();
		String userDetailInfo = SmartUtil.getUserDetailInfo(owner);
//		WorkSpaceInfo workSpace = workInstance.getWorkSpace();
		String workSpaceId = workInstance.getWorkSpaceId();
		String workSpaceName = workInstance.getWorkSpaceName();
		int workSpaceType = workInstance.getWorkSpaceType();
		if(SmartUtil.isBlankObject(workSpaceId)){
			workSpaceId = workInstance.getOwner().getId();
			workSpaceName = workInstance.getOwner().getName();
			workSpaceType = workInstance.getOwner().getSpaceType();
		}
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
		<li class="sub_instance_list js_sub_instance_list" instanceId="<%=workInstance.getId() %>">
			<%
			switch(workInstance.getType()){
			
			// 태스크가 게시판인 경우...									
			case Instance.TYPE_BOARD:
				board = (BoardInstanceInfo)workInstance;
			%>
				<div class="det_title">
					<div class="noti_pic">
						<a class="js_pop_user_info" href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>&wid=<%=owner.getId() %>" userId="<%=owner.getId()%>" longName="<%=owner.getLongName() %>" minPicture="<%=owner.getMinPicture() %>" profile="<%=owner.getOrgPicture()%>" userDetail="<%=userDetailInfo%>">
							<img src="<%=owner.getMidPicture()%>" class="profile_size_c">
						</a>
					</div>
					<div class="noti_in">
						<%
						if(cUser.getId().equals(owner.getId())){
						%>
							<span class="t_name"><%=owner.getLongName()%></span>
						<%
						}else{
						%>
							<a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>"><span class="t_name"><%=owner.getLongName()%></span></a>
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
									<%if(board.getSubInstanceCount()>0){ %><font class="t_sub_count">[<b><%=board.getSubInstanceCount() %></b>]</font><%} %>
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
					<div class="noti_pic">
						<a class="js_pop_user_info" href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>&wid=<%=owner.getId() %>" userId="<%=owner.getId()%>" longName="<%=owner.getLongName() %>" minPicture="<%=owner.getMinPicture() %>" profile="<%=owner.getOrgPicture()%>" userDetail="<%=userDetailInfo%>">
							<img src="<%=owner.getMidPicture()%>" class="profile_size_c">
						</a>
					</div>
					<div class="noti_in">
						<%
						if(cUser.getId().equals(owner.getId())){
						%>
							<span class="t_name"><%=owner.getLongName()%></span>
						<%
						}else{
						%>
							<a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>"><span class="t_name"><%=owner.getLongName()%></span></a>
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
									<%if(event.getSubInstanceCount()>0){ %><font class="t_sub_count">[<b><%=event.getSubInstanceCount() %></b>]</font><%} %>
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
					<div class="noti_pic">
						<a class="js_pop_user_info" href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>&wid=<%=owner.getId() %>" userId="<%=owner.getId()%>" longName="<%=owner.getLongName() %>" minPicture="<%=owner.getMinPicture() %>" profile="<%=owner.getOrgPicture()%>" userDetail="<%=userDetailInfo%>">
							<img src="<%=owner.getMidPicture()%>" class="profile_size_c">
						</a>
					</div>
					<div class="noti_in">
						<%
						if(cUser.getId().equals(owner.getId())){
						%>
							<span class="t_name"><%=owner.getLongName()%></span>
						<%
						}else{
						%>
							<a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>"><span class="t_name"><%=owner.getLongName()%></span></a>
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
							<%if(file.getSubInstanceCount()>0){ %><font class="t_sub_count">[<b><%=file.getSubInstanceCount() %></b>]</font><%} %>
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
					<div class="noti_pic">
						<a class="js_pop_user_info" href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>&wid=<%=owner.getId() %>" userId="<%=owner.getId()%>" longName="<%=owner.getLongName() %>" minPicture="<%=owner.getMinPicture() %>" profile="<%=owner.getOrgPicture()%>" userDetail="<%=userDetailInfo%>">
							<img src="<%=owner.getMidPicture()%>" class="profile_size_c">
						</a>
					</div>
					<div class="noti_in">
						<%
						if(cUser.getId().equals(owner.getId())){
						%>
							<span class="t_name"><%=owner.getLongName()%></span>
						<%
						}else{
						%>
							<a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>"><span class="t_name"><%=owner.getLongName()%></span></a>
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
							<%if(image.getSubInstanceCount()>0){ %><font class="vc t_sub_count">[<b><%=image.getSubInstanceCount() %></b>]</font><%} %>
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
					<div class="noti_pic">
						<a class="js_pop_user_info" href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>&wid=<%=owner.getId() %>" userId="<%=owner.getId()%>" longName="<%=owner.getLongName() %>" minPicture="<%=owner.getMinPicture() %>" profile="<%=owner.getOrgPicture()%>" userDetail="<%=userDetailInfo%>">
							<img src="<%=owner.getMidPicture()%>" class="profile_size_c">
						</a>
					</div>
					<div class="noti_in">
						<%
						if(cUser.getId().equals(owner.getId())){
						%>
							<span class="t_name"><%=owner.getLongName()%></span>
						<%
						}else{
						%>
							<a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>"><span class="t_name"><%=owner.getLongName()%></span></a>
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
									<%if(memo.getSubInstanceCount()>0){ %><font class="t_sub_count">[<b><%=memo.getSubInstanceCount() %></b>]</font><%} %>
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
						<img src="<%=owner.getMidPicture()%>" class="profile_size_c">
					</a>
				</div>
				<div class="noti_in">
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
					<a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>"><span class="t_name"><%=owner.getLongName()%></span></a>
					<span class="t_date pl10"><%=workInstance.getLastModifiedDate().toLocalString()%></span>
					<div><%=comment.getComment() %>
						<%if(workInstance.isNew()){ %><span class="icon_new"></span><%} %>
					</div>
				</div>
			</div>	
			<%
				break;
			default:
			%>
				<div class="det_title">
					<div class="noti_pic"><a class="js_pop_user_info" href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>&wid=<%=owner.getId() %>" userId="<%=owner.getId()%>" longName="<%=owner.getLongName() %>" minPicture="<%=owner.getMinPicture() %>" profile="<%=owner.getOrgPicture()%>" userDetail="<%=userDetailInfo%>"><img src="<%=owner.getMidPicture()%>" class="profile_size_c"></a></div>
					<div class="noti_in">
						<%
						if(cUser.getId().equals(owner.getId())){
						%>
							<span class="t_name"><%=owner.getLongName()%></span>
						<%
						}else{
						%>
							<a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>"><span class="t_name"><%=owner.getLongName()%></span></a>
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
						<span class="t_date pl10"><%=workInstance.getLastModifiedDate().toLocalString()%></span>
						<div>
							<a class="js_content" href="<%=((WorkInstanceInfo)workInstance).getController()%>?cid=<%=((WorkInstanceInfo)workInstance).getContextId()%>&wid=<%=workSpaceId%>&workId=<%=workId%>">
								<span class="<%=Work.getIconClass(workId, workType, isWorkRunning)%>"></span>
								<span class="t_date"><%=workFullPathName%></span>
							</a>
							<a class="js_content" href="<%=((WorkInstanceInfo)workInstance).getController()%>?cid=<%=((WorkInstanceInfo)workInstance).getContextId()%>&wid=<%=workSpaceId%>&workId=<%=workId%>">
									<span class="tb"><%=workInstance.getSubject()%>
									<%if(((WorkInstanceInfo)workInstance).getSubInstanceCount()>0){ %><font class="t_sub_count">[<b><%=((WorkInstanceInfo)workInstance).getSubInstanceCount() %></b>]</font><%} %>
									<%if(workInstance.isNew()){ %><span class="icon_new"></span><%} %>
								</span> 
							</a>
						</div>
					</div>
			</div>	
			<%
			}
			%>		
		</li>
<%
	}
}
%>
