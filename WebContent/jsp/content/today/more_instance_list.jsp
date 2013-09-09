
<!-- Name 			: more_instance_list.jsp										 -->
<!-- Description	: 현재사용자에게 할당된 업무나, 현재사용자 진행중인 업무목록을 보여주는 화면	 -->
<!-- Author			: Maninsoft, Inc.												 -->
<!-- Created Date	: 2011.9.														 -->

<%@page import="net.smartworks.model.work.Work"%>
<%@page import="java.util.Date"%>
<%@page import="net.smartworks.model.work.info.WorkInfo"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.model.instance.info.RequestParams"%>
<%@page import="net.smartworks.model.community.info.WorkSpaceInfo"%>
<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.model.work.info.SmartWorkInfo"%>
<%@page import="net.smartworks.model.instance.info.WorkInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.TaskInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.InstanceInfo"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="net.smartworks.model.work.SmartWork"%>
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

%>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<%	

final int MAX_INSTANCE_LIST = 10; 

RequestParams params = (RequestParams)request.getAttribute("requestParams");
if (params == null) {
	String searchKey = request.getParameter("searchKey");
	if (!CommonUtil.isEmpty(searchKey)) {
		params = new RequestParams();
		params.setSearchKey(searchKey);
	}
}

// 호출시 전달된 lastDate, assignedOnly 값을 가져온다..
String strLastDate = request.getParameter("lastDate");
LocalDate lastDate = new LocalDate();
if(!SmartUtil.isBlankObject(strLastDate))		
	lastDate = LocalDate.convertLocalStringToLocalDate(request.getParameter("lastDate"));
boolean assignedOnly = Boolean.parseBoolean(request.getParameter("assignedOnly"));
boolean runningOnly = Boolean.parseBoolean(request.getParameter("runningOnly"));

// lastDate와 assignedOnly값을 가지고 현재 진행중인 모든 인스턴스리스트를 가져온다...
InstanceInfo[] instances = null;
try {
	instances = smartWorks.getMyRunningInstances(lastDate, MAX_INSTANCE_LIST, assignedOnly, runningOnly, params);
} catch (Exception e) {
	e.printStackTrace();
}
if(!SmartUtil.isBlankObject(instances)) {
%>
<div class="space_section">
<ul>
		<%
		// 인스턴스 갯수 만큼 리스트를 그린다...
		for (int i = 0; i < instances.length; i++) {
			if (i == MAX_INSTANCE_LIST)
				break;
			InstanceInfo instance = instances[i];
			String statusImage;
			String statusTitle;
			WorkInstanceInfo workInstance = null;
			TaskInstanceInfo taskInstance = null;
			TaskInstanceInfo[] assignedTasks = null;
			TaskInstanceInfo[] forwardedTasks = null;
			boolean isAssignedTask = false;
			String trTarget = "";
			String lastTaskInstId = null;
			// 인스턴스 타입이 Work인경우.....
			if (instance.getType() == Instance.TYPE_WORK) {
				workInstance = (WorkInstanceInfo) instance;
				TaskInstanceInfo lastTask = workInstance.getLastTask();
				if (!CommonUtil.isEmpty(lastTask))
					lastTaskInstId = lastTask.getId();
				int lastTaskCount = workInstance.getLastTaskCount();
				List<TaskInstanceInfo> assignedList = new ArrayList<TaskInstanceInfo>();
				List<TaskInstanceInfo> forwardedList = new ArrayList<TaskInstanceInfo>();
				if ((lastTask.getTaskType() % 10) == 1)
							assignedList.add(lastTask);
				else if ((lastTask.getTaskType() % 10) == 2)
							forwardedList.add(lastTask);
				assignedTasks = (TaskInstanceInfo[]) assignedList.toArray(new TaskInstanceInfo[0]);
				forwardedTasks = (TaskInstanceInfo[]) forwardedList.toArray(new TaskInstanceInfo[0]);
				trTarget = workInstance.getController() + "?cid=" + workInstance.getContextId() + "&workId=" + workInstance.getWorkId() + "&taskInstId=" + lastTaskInstId;
			// 인스턴스타입이 태스크인 경우.....
			} else if (instance.getType() == Instance.TYPE_TASK) {
				isAssignedTask = true;
				workInstance = (WorkInstanceInfo)((TaskInstanceInfo) instance).getWorkInstance();
				
				taskInstance = (TaskInstanceInfo) instance;
				trTarget = taskInstance.getController() + "?cid=" + taskInstance.getContextId() + "&workId=" + workInstance.getWorkId() + "&taskInstId=" + taskInstance.getId();
			}
			UserInfo owner = workInstance.getOwner();
			String userDetailInfo = SmartUtil.getUserDetailInfo(owner);
//			SmartWorkInfo work = (SmartWorkInfo) workInstance.getWork();
			String workId = workInstance.getWorkId();
			String workName = workInstance.getWorkName();
			int workType = workInstance.getWorkType();
			boolean isWorkRunning = workInstance.isWorkRunning();
			String workFullPathName = workInstance.getWorkFullPathName();
	
			int status;
			if(isAssignedTask){
				switch(workInstance.getStatus()){
				case Instance.STATUS_RETURNED:
					status = Instance.STATUS_RETURNED;
					break;
				case Instance.STATUS_REJECTED:
					status = Instance.STATUS_REJECTED;
					break;
				default:
					status = taskInstance.getStatus();
				}
			}else{
				status = instance.getStatus();
			}
			switch (status) {
			// 인스턴스가 현재 진행중인 경우..
			case Instance.STATUS_RUNNING:
				statusImage = "icon_status_running";
				statusTitle = "content.status.running";
				break;
			// 인스턴스가 지연진행중인 경우....
			case Instance.STATUS_DELAYED_RUNNING:
				statusImage = "icon_status_d_running";
				statusTitle = "content.status.delayed_running";
				break;
			// 인스턴스가 반려된 경우...
			case Instance.STATUS_RETURNED:
				statusImage = "icon_status_returned";
				statusTitle = "content.status.returned";
				break;
			// 인스턴스가 기각된 경우...
			case Instance.STATUS_REJECTED:
				statusImage = "icon_status_rejected";
				statusTitle = "content.status.rejected";
				break;
			// 인스턴스가 반려된 경우...
			case Instance.STATUS_COMPLETED:
				statusImage = "icon_status_completed";
				statusTitle = "content.status.completed";
				break;
			// 기타 잘못되어 상태가 없는 경우..
			default:
				statusImage = "icon_status_not_yet";
				statusTitle = "content.status.not_yet";
			}
		%>
			<!-- 진행중인 업무 아이템 -->
			<li class="instance_list js_more_instance_item js_content_list" href="<%=trTarget%>" dateValue="<%=workInstance.getLastModifiedDate().toLocalDateString2()%>">
				<div class="det_title">
				<!-- 인스턴스 상태 및 시작자 사진표시 -->
				<div class="noti_pic">
					<span class="<%=statusImage%> tc vm" title="<fmt:message key='<%=statusTitle%>'/>" ></span>
					<a class="js_pop_user_info" href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>&wid=<%=owner.getId() %>" userId="<%=owner.getId()%>" longName="<%=owner.getLongName() %>" minPicture="<%=owner.getMinPicture() %>" profile="<%=owner.getOrgPicture()%>" userDetail="<%=userDetailInfo%>"><img class="profile_size_m" src="<%=owner.getMidPicture()%>"/></a>
				</div>
				<!-- 인스턴스 상태 및 시작자 사진표시 -->
				
				<!-- 인스턴스 상세내용 표시 -->
				<div class="noti_in_m case_2line">
					<!--  시작자 이름 -->
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
					<%
					// 공간이 사람이 아닌 공간(그룹, 부서) 경우에는 공간 표시를 해준다...
					if (!SmartUtil.isBlankObject(workInstance.getWorkSpaceId()) && !workInstance.getWorkSpaceId().equals(owner.getId())) {
//						WorkSpaceInfo workSpace = workInstance.getWorkSpace();
						String workSpaceId = workInstance.getWorkSpaceId();
						String workSpaceName = workInstance.getWorkSpaceName();
						int workSpaceType = workInstance.getWorkSpaceType();
						WorkInfo workSpaceInstanceWork = workInstance.getWorkSpaceInstanceWork();
					%>
						<span class="arr">▶</span>
						<a href="<%=WorkSpaceInfo.getSpaceController(workSpaceType, workSpaceInstanceWork)%>?cid=<%=WorkSpaceInfo.getSpaceContextId(workSpaceType, workSpaceId, workSpaceInstanceWork)%>"><span class="<%=WorkSpaceInfo.getIconClass(workSpaceType, workSpaceInstanceWork)%> fix_pos"><%=workSpaceName%></span> </a>
					<%
					}
					// 인스턴스 타입에 할당태스크인 경우들 중에서....
					if (isAssignedTask) {
						UserInfo assignee = taskInstance.getAssignee();
						switch (taskInstance.getTaskType()) {
						
						// 정보관리업무 할당 태스크인 경우..
						case TaskInstance.TYPE_INFORMATION_TASK_ASSIGNED:
					%>
							<fmt:message key="content.sentence.itask_assigned">
								<fmt:param>
									<a class="js_content" href='<%=((TaskInstanceInfo)taskInstance).getController()%>?cid=<%=((TaskInstanceInfo)taskInstance).getContextId()%>&workId=<%=workId%>&taskInstId=<%=taskInstance.getId()%>'>
										<span class='t_woname'><%=((TaskInstanceInfo)taskInstance).getName()%></span> 
									</a>
								</fmt:param>
								<fmt:param>
									<%
									if(cUser.getId().equals(assignee.getId())){
									%>
										<span class="t_name"><%=assignee.getLongName()%></span>
									<%
									}else{
									%>
										<a href="<%=assignee.getSpaceController() %>?cid=<%=assignee.getSpaceContextId()%>&wid=<%=assignee.getId()%>"><span class="t_name"><%=assignee.getLongName()%></span></a>
									<%
									}
									%>
								</fmt:param>
							</fmt:message>
						<%
							break;
						// 정보관리업무 업무전달인 경우....
						case TaskInstance.TYPE_INFORMATION_TASK_FORWARDED:
						%>
							<fmt:message key="content.sentence.itask_forwarded">
								<fmt:param>
									<a class="js_content" href="<%=((TaskInstanceInfo)taskInstance).getController()%>?cid=<%=((TaskInstanceInfo)taskInstance).getContextId()%>&workId=<%=workId%>&taskInstId=<%=taskInstance.getId()%>">
										<span class="t_woname"><%=((TaskInstanceInfo)taskInstance).getSubject()%></span>
									</a>
								</fmt:param>
								<fmt:param>
									<%
									if(cUser.getId().equals(assignee.getId())){
									%>
										<span class="t_name"><%=assignee.getLongName()%></span>
									<%
									}else{
									%>
										<a href="<%=assignee.getSpaceController() %>?cid=<%=assignee.getSpaceContextId()%>&wid=<%=assignee.getId()%>"><span class="t_name"><%=assignee.getLongName()%></span></a>
									<%
									}
									%>
								</fmt:param>
							</fmt:message>
						<%
							break;
						// 프로세스업무 할당테스크인 경우...
						case TaskInstance.TYPE_PROCESS_TASK_ASSIGNED:
							String taskInstParam = (SmartUtil.isBlankObject(taskInstance)) ? "" : "&taskInstId=" + taskInstance.getId();
							if (CommonUtil.isEmpty(taskInstParam)) {
								if (!CommonUtil.isEmpty(lastTaskInstId)) {
									taskInstParam = "&taskInstId=" + lastTaskInstId;
								}
							}
							if(workInstance.getStatus() == Instance.STATUS_RETURNED){
						%>
								<fmt:message key="content.sentence.ptask_returned">
									<fmt:param>
										<a class="js_content" href="<%=((TaskInstanceInfo)taskInstance).getController()%>?cid=<%=((TaskInstanceInfo)taskInstance).getContextId()%>&workId=<%=workId%><%=taskInstParam%>">
											<span class="t_woname"><%=((TaskInstanceInfo)taskInstance).getName()%></span> 
										</a>
									</fmt:param>
									<fmt:param>
										<%
										if(cUser.getId().equals(assignee.getId())){
										%>
											<span class="t_name"><%=assignee.getLongName()%></span>
										<%
										}else{
										%>
											<a href="<%=assignee.getSpaceController() %>?cid=<%=assignee.getSpaceContextId()%>&wid=<%=assignee.getId()%>"><span class="t_name"><%=assignee.getLongName()%></span></a>
										<%
										}
										%>
									</fmt:param>
								</fmt:message>
							<%
							}else{
							%>
								<fmt:message key="content.sentence.ptask_assigned">
									<fmt:param>
										<a class="js_content" href="<%=((TaskInstanceInfo)taskInstance).getController()%>?cid=<%=((TaskInstanceInfo)taskInstance).getContextId()%>&workId=<%=workId%><%=taskInstParam%>">
											<span class="t_woname"><%=((TaskInstanceInfo)taskInstance).getName()%></span> 
										</a>
									</fmt:param>
									<fmt:param>
										<%
										if(cUser.getId().equals(assignee.getId())){
										%>
											<span class="t_name"><%=assignee.getLongName()%></span>
										<%
										}else{
										%>
											<a href="<%=assignee.getSpaceController() %>?cid=<%=assignee.getSpaceContextId()%>&wid=<%=assignee.getId()%>"><span class="t_name"><%=assignee.getLongName()%></span></a>
										<%
										}
										%>
									</fmt:param>
								</fmt:message>
						<%
							}
							break;
						// 프로세스업무 업무전달인 경우...
						case TaskInstance.TYPE_PROCESS_TASK_FORWARDED:
						%>
							<fmt:message key="content.sentence.ptask_forwarded">
								<fmt:param>
									<a class="js_content" href="<%=((TaskInstanceInfo)taskInstance).getController()%>?cid=<%=((TaskInstanceInfo)taskInstance).getContextId()%>&workId=<%=workId%>&taskInstId=<%=taskInstance.getId()%>">
										<span class="t_woname"><%=((TaskInstanceInfo)taskInstance).getSubject()%></span> 
									</a>
								</fmt:param>
								<fmt:param>
									<%
									if(cUser.getId().equals(assignee.getId())){
									%>
										<span class="t_name"><%=assignee.getLongName()%></span>
									<%
									}else{
									%>
										<a href="<%=assignee.getSpaceController() %>?cid=<%=assignee.getSpaceContextId()%>&wid=<%=assignee.getId()%>"><span class="t_name"><%=assignee.getLongName()%></span></a>
									<%
									}
									%>
								</fmt:param>
							</fmt:message>
						<%
							break;
						// 일정계획업무 할당태스크인 경우...
						case TaskInstance.TYPE_SCHEDULE_TASK_ASSIGNED:
						%>
							<fmt:message key="content.sentence.stask_assigned">
								<fmt:param>
									<a class="js_content" href="<%=((TaskInstanceInfo)taskInstance).getController()%>?cid=<%=((TaskInstanceInfo)taskInstance).getContextId()%>&workId=<%=workId%>&taskInstId=<%=taskInstance.getId()%>">
										<span class="t_woname"><%=((TaskInstanceInfo)taskInstance).getName()%></span> 
									</a>
								</fmt:param>
								<fmt:param>
									<%
									if(cUser.getId().equals(assignee.getId())){
									%>
										<span class="t_name"><%=assignee.getLongName()%></span>
									<%
									}else{
									%>
										<a href="<%=assignee.getSpaceController() %>?cid=<%=assignee.getSpaceContextId()%>&wid=<%=assignee.getId()%>"><span class="t_name"><%=assignee.getLongName()%></span></a>
									<%
									}
									%>
								</fmt:param>
							</fmt:message>
						<%
							break;
						// 일정계획업무 업무전달인 경우...
						case TaskInstance.TYPE_SCHEDULE_TASK_FORWARDED:
						%>
							<fmt:message key="content.sentence.stask_forwarded">
								<fmt:param>
									<a class="js_content" href="<%=((TaskInstanceInfo)taskInstance).getController()%>?cid=<%=((TaskInstanceInfo)taskInstance).getContextId()%>&workId=<%=workId%>&taskInstId=<%=taskInstance.getId()%>">
										<span class="t_woname"><%=((TaskInstanceInfo)taskInstance).getSubject()%></span> 
									</a>
								</fmt:param>
								<fmt:param>
									<%
									if(cUser.getId().equals(assignee.getId())){
									%>
										<span class="t_name"><%=assignee.getLongName()%></span>
									<%
									}else{
									%>
										<a href="<%=assignee.getSpaceController() %>?cid=<%=assignee.getSpaceContextId()%>&wid=<%=assignee.getId()%>"><span class="t_name"><%=assignee.getLongName()%></span></a>
									<%
									}
									%>
								</fmt:param>
							</fmt:message>
						<%
							break;
						// 전자결재업무 할당태스크인 경우...
						case TaskInstance.TYPE_APPROVAL_TASK_ASSIGNED:
							String approvalStatus = (workInstance.getStatus() == Instance.STATUS_RETURNED) ? "returned" : (workInstance.getStatus() == Instance.STATUS_REJECTED) ? "rejected" : "assigned";
						%>
							<%if(workInstance.getStatus() == Instance.STATUS_RETURNED){ %>
								<fmt:message key="content.sentence.atask_returned">
									<fmt:param>
										<a class="js_content" href="<%=((TaskInstanceInfo)taskInstance).getController()%>?cid=<%=((TaskInstanceInfo)taskInstance).getContextId()%>&workId=<%=workId%>&taskInstId=<%=taskInstance.getId()%>">
											<span class="t_woname"><%=((TaskInstanceInfo)taskInstance).getName()%></span> 
										</a>
									</fmt:param>
									<fmt:param>
										<%
										if(cUser.getId().equals(assignee.getId())){
										%>
											<span class="t_name"><%=assignee.getLongName()%></span>
										<%
										}else{
										%>
											<a href="<%=assignee.getSpaceController() %>?cid=<%=assignee.getSpaceContextId()%>&wid=<%=assignee.getId()%>"><span class="t_name"><%=assignee.getLongName()%></span></a>
										<%
										}
										%>
									</fmt:param>
								</fmt:message>
							<% }else if(workInstance.getStatus() == Instance.STATUS_REJECTED){ %>
								<fmt:message key="content.sentence.atask_rejected">
									<fmt:param>
										<a class="js_content" href="<%=((TaskInstanceInfo)taskInstance).getController()%>?cid=<%=((TaskInstanceInfo)taskInstance).getContextId()%>&workId=<%=workId%>&taskInstId=<%=taskInstance.getId()%>">
											<span class="t_woname"><%=((TaskInstanceInfo)taskInstance).getName()%></span> 
										</a>
									</fmt:param>
									<fmt:param>
										<%
										if(cUser.getId().equals(assignee.getId())){
										%>
											<span class="t_name"><%=assignee.getLongName()%></span>
										<%
										}else{
										%>
											<a href="<%=assignee.getSpaceController() %>?cid=<%=assignee.getSpaceContextId()%>&wid=<%=assignee.getId()%>"><span class="t_name"><%=assignee.getLongName()%></span></a>
										<%
										}
										%>
									</fmt:param>
								</fmt:message>
							<% }else{ %>
								<fmt:message key="content.sentence.atask_assigned">
									<fmt:param>
										<a class="js_content" href="<%=((TaskInstanceInfo)taskInstance).getController()%>?cid=<%=((TaskInstanceInfo)taskInstance).getContextId()%>&workId=<%=workId%>&taskInstId=<%=taskInstance.getId()%>">
											<span class="t_woname"><%=((TaskInstanceInfo)taskInstance).getName()%></span> 
										</a>
									</fmt:param>
									<fmt:param>
										<%
										if(cUser.getId().equals(assignee.getId())){
										%>
											<span class="t_name"><%=assignee.getLongName()%></span>
										<%
										}else{
										%>
											<a href="<%=assignee.getSpaceController() %>?cid=<%=assignee.getSpaceContextId()%>&wid=<%=assignee.getId()%>"><span class="t_name"><%=assignee.getLongName()%></span></a>
										<%
										}
										%>
									</fmt:param>
								</fmt:message>
							<% } %>
						<%
							break;
						// 전자결재업무 업무전달인 경우....
						case TaskInstance.TYPE_APPROVAL_TASK_FORWARDED:
						%>
							<fmt:message key="content.sentence.atask_forwarded">
								<fmt:param>
									<a class="js_content" href="<%=((TaskInstanceInfo)taskInstance).getController()%>?cid=<%=((TaskInstanceInfo)taskInstance).getContextId()%>&workId=<%=workId%>&taskInstId=<%=taskInstance.getId()%>">
										<span class="t_woname"><%=((TaskInstanceInfo)taskInstance).getSubject()%></span> 
									</a>
								</fmt:param>
								<fmt:param>
									<%
									if(cUser.getId().equals(assignee.getId())){
									%>
										<span class="t_name"><%=assignee.getLongName()%></span>
									<%
									}else{
									%>
										<a href="<%=assignee.getSpaceController() %>?cid=<%=assignee.getSpaceContextId()%>&wid=<%=assignee.getId()%>"><span class="t_name"><%=assignee.getLongName()%></span></a>
									<%
									}
									%>
								</fmt:param>
							</fmt:message>
					<%
							break;
						// 기타 잘못된 경우....
						default:
							break;
						}
					// 할당태스크가 아닌경우....
					} else {
						
						// 할당된 태스크들이 있으면... 현재상태를 설명해줄수 있는 표현문장을 만든다....
						if (assignedTasks != null) {
					%>
							<fmt:message key="content.sentence.users_work_is" />
					<%
							boolean firstRun = true;
							for (TaskInstanceInfo assignedTask : assignedTasks) {
								UserInfo assignee = assignedTask.getAssignee();
								String runningTaskName = assignedTask.getName();
								if (firstRun) {
									firstRun = false;
								} else {
					%>
									,
								<%
								}
								%>
								<fmt:message key="content.sentence.task_by_assignee">
									<fmt:param>
										<a href="<%=assignee.getSpaceController() %>?cid=<%=assignee.getSpaceContextId()%>&wid=<%=assignee.getId()%>" title="<%=assignee.getLongName()%>">
											<span class="t_name"><%=assignee.getName()%></span>
										</a>
									</fmt:param>
									<fmt:param>
										<a href="<%=assignedTask.getController()%>?cid=<%=assignedTask.getContextId()%>&wid=<%=assignee.getId() %>&workId=<%=workInstance.getWorkSpaceId()%>">
											<span class="t_woname"><%=runningTaskName%></span> 
										</a>
									</fmt:param>
									<fmt:param>
										<a <%if(!cUser.getId().equals(assignee.getId())){ %>href="<%=assignee.getSpaceController() %>?cid=<%=assignee.getSpaceContextId()%>&wid=<%=assignee.getId()%>" <%} %>><span class="t_name"><%=assignee.getLongName()%></span> </a>
									</fmt:param>
								</fmt:message>
							<%
							}
							%>
							<fmt:message key="content.sentence.task_is_running" />
						<%
						}
						if (!SmartUtil.isBlankObject(forwardedTasks)) {
							UserInfo forwardee = forwardedTasks[0].getAssignee();
							String userContextId = ISmartWorks.CONTEXT_PREFIX_USER_SPACE + forwardee.getId();
							String forwardedContextId = ISmartWorks.CONTEXT_PREFIX_USER_SPACE + forwardee.getId();
							String runningTaskName = forwardedTasks[0].getName();
							if (assignedTasks != null) {
						%>
								<fmt:message key="content.sentence.and_also" />
							<%
							} else {
							%>
								<fmt:message key="content.sentence.forwarded_work_is" />
							<%
							}
							%>
							<fmt:message key="content.sentence.and_user">
								<fmt:param>
									<a href="<%=forwardee.getSpaceController() %>?cid=<%=forwardee.getSpaceContextId()%>&wid=<%=forwardee.getId()%>" title="<%=forwardee.getLongName()%>">
										<span class="t_name"><%=forwardee.getName()%></span>
									</a>
								</fmt:param>
							</fmt:message>
							<%
							if (forwardedTasks.length > 1) {
							%>
								<fmt:message key="content.sentence.with_other_users">
									<fmt:param><%=forwardedTasks.length - 1%></fmt:param>
								</fmt:message>
							<%
							}
							%>
							<fmt:message key="content.sentence.on_forwarded_task" />
					<%
						}
					}
					%>
					<!-- 인스턴스 마지막수정일자 -->
					<span class="t_date vb pl10"><%=workInstance.getLastModifiedDate().toLocalString()%></span>
					<!-- 인스턴스 마지막수정일자 //-->
					<br/>
					<a href="<%=WorkInfo.getController(workId, workType)%>?cid=<%=WorkInfo.getContextId(workId, workType)%>" class="js_content">
						<span class="<%=Work.getIconClass(workId, workType, isWorkRunning)%>"></span>
						<span class="t_date"><%=workFullPathName%></span>
					</a>
					<%
					String taskInstParam = (SmartUtil.isBlankObject(taskInstance)) ? "" : "&taskInstId=" + taskInstance.getId();
					if (CommonUtil.isEmpty(taskInstParam)) {
						if (!CommonUtil.isEmpty(lastTaskInstId)) {
							taskInstParam = "&taskInstId=" + lastTaskInstId;
						}
					}
					%>
					<a href="<%=workInstance.getController()%>?cid=<%=workInstance.getContextId() %>&workId=<%=workId %><%=taskInstParam%>" class="js_content">
						<span class="tb"><%=workInstance.getSubject()%></span> 
					</a>
					<%if(workInstance.getSubInstanceCount()>0){ %><font class="t_sub_count tb">[<%=workInstance.getSubInstanceCount() %>]</font><%} %>
					<%if(workInstance.isNew()){ %><span class="icon_new"></span><%} %>
					
				</div>
				<!-- 인스턴스 상세내용 표시 //-->			
				</div>
			</li>
			<!-- 진행중인 업무 아이템 //-->
		<%
		}
		%>
		<!-- 더보기 버튼 -->
		<%if(instances.length > MAX_INSTANCE_LIST){ %>
			<div class="t_nowork js_more_list">
				<a href="more_instance_list.sw"><fmt:message key="common.message.more_work_task"/></a>
				<span class="js_progress_span"></span>
			</div>
		<%} %>
		<!-- 더보기 버튼 !!-->
	</ul>
	</div>
<%
}else{
%>
	<div class="tc"><fmt:message key="common.message.no_instance"/></div>
<%
}
%>	
