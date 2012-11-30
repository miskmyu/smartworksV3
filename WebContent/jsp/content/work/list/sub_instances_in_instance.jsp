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
	int fetchCount = Integer.parseInt(request.getParameter("fetchCount"));
	InstanceInfo[] subInstances = (InstanceInfo[])session.getAttribute("subComments");
	if(SmartUtil.isBlankObject(subInstances))
		subInstances = smartWorks.getRecentSubInstancesInInstance(instanceId, fetchCount);
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<%
if (subInstances != null) {
	for (InstanceInfo workInstance : subInstances) {
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
	%>
		<li class="sub_instance_list js_sub_instance_list" instanceId="<%=workInstance.getId() %>" >
			<%
			switch(workInstance.getType()){
			
			// 태스크가 게시판인 경우...									
			case Instance.TYPE_BOARD:
				board = (BoardInstanceInfo)workInstance;
			%>
			<div class="det_title">
					<div class="noti_pic">
						<a class="js_pop_user_info" href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>" userId="<%=owner.getId()%>" longName="<%=owner.getLongName() %>" minPicture="<%=owner.getMinPicture() %>" profile="<%=owner.getOrgPicture()%>" userDetail="<%=userDetailInfo%>">
							<img src="<%=owner.getMidPicture()%>" class="profile_size_c">
						</a>
					</div>
					<div class="noti_in">
						<div><%=workFullPathName%></div>
						<a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>"><span class="t_name"><%=owner.getLongName()%></span></a>
						<span class="t_date pl10"><%=workInstance.getLastModifiedDate().toLocalString()%></span>
						<a href="<%=board.getController() %>?cid=<%=board.getContextId() %>&wid=<%=workSpaceId %>&workId=<%=workId %>">
							<div>
								<span class="<%=WorkInfo.getIconClass(workId, workType, isWorkRunning)%>"></span>
								<div><%=board.getSubject() %>
									<%if(((WorkInstanceInfo)workInstance).getSubInstanceCount()>0){ %><font class="t_sub_count">[<b><%=((WorkInstanceInfo)workInstance).getSubInstanceCount() %></b>]</font><%} %>
									<%if(workInstance.isNew()){ %><span class="icon_new"></span><%} %>
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
						<a class="js_pop_user_info" href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>" userId="<%=owner.getId()%>" longName="<%=owner.getLongName() %>" minPicture="<%=owner.getMinPicture() %>" profile="<%=owner.getOrgPicture()%>" userDetail="<%=userDetailInfo%>">
							<img src="<%=owner.getMidPicture()%>" class="profile_size_c">
						</a>
					</div>
					<div class="noti_in">
						<div><%=workFullPathName%></div>
						<a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>"><span class="t_name"><%=owner.getLongName()%></span></a>
						<span class="t_date pl10"><%=workInstance.getLastModifiedDate().toLocalString()%></span>
						<div><%=event.getSubject() %>
							<%if(((WorkInstanceInfo)workInstance).getSubInstanceCount()>0){ %><font class="t_sub_count">[<b><%=((WorkInstanceInfo)workInstance).getSubInstanceCount() %></b>]</font><%} %>
							<%if(workInstance.isNew()){ %><span class="icon_new"></span><%} %>
						</div>
						<div><fmt:message key="common.upload.event.start_date"/> : <%=event.getStart().toLocalString() %> 
							<%if(!SmartUtil.isBlankObject(event.getEnd())) {%><fmt:message key="common.upload.event.end_date"/> : <%=event.getEnd().toLocalString() %> <%} %></div>
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
						<a class="js_pop_user_info" href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>" userId="<%=owner.getId()%>" longName="<%=owner.getLongName() %>" minPicture="<%=owner.getMinPicture() %>" profile="<%=owner.getOrgPicture()%>" userDetail="<%=userDetailInfo%>">
							<img src="<%=owner.getMidPicture()%>" class="profile_size_c">
						</a>
					</div>
					<div class="noti_in">
						<div><%=workFullPathName%></div>
						<a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>"><span class="t_name"><%=owner.getLongName()%></span></a>
						<span class="t_date pl10"><%=workInstance.getLastModifiedDate().toLocalString()%></span>
						<%if(!SmartUtil.isBlankObject(file.getFiles())){ %><div><%=SmartUtil.getFilesDetailInfo(file.getFiles(), workId, null, file.getId()) %>
							<%if(((WorkInstanceInfo)workInstance).getSubInstanceCount()>0){ %><font class="t_sub_count">[<b><%=((WorkInstanceInfo)workInstance).getSubInstanceCount() %></b>]</font><%} %>
							<%if(workInstance.isNew()){ %><span class="icon_new"></span><%} %>
						</div><%} %>
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
						<a class="js_pop_user_info" href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>" userId="<%=owner.getId()%>" longName="<%=owner.getLongName() %>" minPicture="<%=owner.getMinPicture() %>" profile="<%=owner.getOrgPicture()%>" userDetail="<%=userDetailInfo%>">
							<img src="<%=owner.getMidPicture()%>" class="profile_size_c">
						</a>
					</div>
					<div class="noti_in">
						<div><%=workFullPathName%></div>
						<a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>"><span class="t_name"><%=owner.getLongName()%></span></a>
						<span class="t_date pl10"><%=workInstance.getLastModifiedDate().toLocalString()%></span>
						<div><a href="" class="js_show_picture_detail" instanceId="<%=image.getId()%>" href=""><img src="<%=image.getImgSource()%>" style="min-height:20px;width:100px;"></a>
							<%if(((WorkInstanceInfo)workInstance).getSubInstanceCount()>0){ %><font class="vt t_sub_count">[<b><%=((WorkInstanceInfo)workInstance).getSubInstanceCount() %></b>]</font><%} %>
							<%if(workInstance.isNew()){ %><span class="vt icon_new"></span><%} %>
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
						<a class="js_pop_user_info" href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>" userId="<%=owner.getId()%>" longName="<%=owner.getLongName() %>" minPicture="<%=owner.getMinPicture() %>" profile="<%=owner.getOrgPicture()%>" userDetail="<%=userDetailInfo%>">
							<img src="<%=owner.getMidPicture()%>" class="profile_size_c">
						</a>
					</div>
					<div class="noti_in">
						<div><%=workFullPathName%></div>
						<a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>"><span class="t_name"><%=owner.getLongName()%></span></a>
						<span class="t_date pl10"><%=workInstance.getLastModifiedDate().toLocalString()%></span>
						<div><%=memo.getContent() %>
							<%if(((WorkInstanceInfo)workInstance).getSubInstanceCount()>0){ %><font class="t_sub_count">[<b><%=((WorkInstanceInfo)workInstance).getSubInstanceCount() %></b>]</font><%} %>
							<%if(workInstance.isNew()){ %><span class="icon_new"></span><%} %>
						</div>
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
					<a class="js_pop_user_info" href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>" userId="<%=owner.getId()%>" longName="<%=owner.getLongName() %>" minPicture="<%=owner.getMinPicture() %>" profile="<%=owner.getOrgPicture()%>" userDetail="<%=userDetailInfo%>">
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
					<div class="noti_pic">
						<a class="js_pop_user_info" href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>" userId="<%=owner.getId()%>" profile="<%=owner.getOrgPicture()%>" userDetail="<%=userDetailInfo%>">
							<img src="<%=owner.getMidPicture()%>" class="profile_size_c">
						</a>
					</div>
					<div class="noti_in">
						<div><%=workFullPathName%></div>
						<a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>"><span class="t_name"><%=owner.getLongName()%></span></a>
						<span class="t_date pl10"><%=workInstance.getLastModifiedDate().toLocalString()%></span>
						<div><%=workInstance.getSubject() %>
							<%if(((WorkInstanceInfo)workInstance).getSubInstanceCount()>0){ %><font class="t_sub_count">[<b><%=((WorkInstanceInfo)workInstance).getSubInstanceCount() %></b>]</font><%} %>
							<%if(workInstance.isNew()){ %><span class="icon_new"></span><%} %>
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
