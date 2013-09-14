<%@page import="net.smartworks.model.community.Group"%>
<%@page import="net.smartworks.model.community.Department"%>
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

	WorkSpace workSpace = (WorkSpace)session.getAttribute("workSpace");
	String contextStr = null;
	if(SmartUtil.isBlankObject(workSpace)) contextStr = "";
	else if(workSpace.getClass().equals(User.class)) contextStr = ISmartWorks.CONTEXT_USER_SPACE;
	else if(workSpace.getClass().equals(Department.class)) contextStr = ISmartWorks.CONTEXT_DEPARTMENT_SPACE;
	else if(workSpace.getClass().equals(Group.class)) contextStr = ISmartWorks.CONTEXT_GROUP_SPACE;
	
	LocalDate today =  LocalDate.convertLocalDateStringToLocalDate((new LocalDate()).toLocalDateSimpleString());

	LocalDate startDate = new LocalDate();
	String startDateStr = request.getParameter("startDate");
	if(!SmartUtil.isBlankObject(startDateStr)){
		LocalDate tempStartDate = LocalDate.convertLocalDateStringToLocalDate(startDateStr);
		startDate = new LocalDate(tempStartDate.getTime());
	}
	startDateStr = startDate.toLocalDateSimpleString();
	session.setAttribute("startDate", startDateStr);

	TaskInstanceInfo[] taskInstances = smartWorks.getTaskInstancesByTimeline(contextStr, workSpace.getId(), startDate, 10); 
	
%>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!--탭-->
<div class="tab js_space_tab_timeline_page" workSpaceId="<%=workSpace.getId() %>" startDate="<%=startDateStr%>">
	<div class="option_section js_space_date_scope">
		<span class="js_progress_span"></span>
		<select class="js_space_select_scope">
			<option selected value="space_tab_timeline.sw"><fmt:message key="space.title.tab_timeline"/></option>
			<option value="space_tab_dayly.sw"><fmt:message key="space.title.tab_dayly"/></option>
			<option value="space_tab_weekly.sw"><fmt:message key="space.title.tab_weekly"/></option>
			<option value="space_tab_monthly.sw"><fmt:message key="space.title.tab_monthly"/></option>
		</select>
	</div>
</div>
<!--탭//-->

<!-- 컨텐츠 레이아웃-->
<div class="section_portlet js_space_timeline_page" contextId="<%=contextStr %>" spaceId="<%=workSpace.getId() %>" >
	<div class="portlet_t">
		<div class="portlet_tl"></div>
	</div>
	<div class="portlet_l" style="display: block;">
		<ul class="portlet_r" style="display: block;">

			<!-- 컨텐츠 -->
			<div class="contents_space">			
				<div class="space_section js_space_timeline">
					<div class="title"><fmt:message key="common.title.work_timeline"/></div>

						<%
						if(!SmartUtil.isBlankObject(taskInstances)){
							session.setAttribute("taskHistories", taskInstances);
						%>
							<jsp:include page="/jsp/content/community/space/space_task_histories.jsp"></jsp:include>
						<%
						}else{
						%>
							<div class="t_nowork"><fmt:message key="common.message.no_work_task"/></div>
						<%
						}
						%>											
				</div>
				<!-- 근무시간//-->
			</div>
			<!-- 컨텐츠 //-->

		</ul>
	</div>
	<div class="portlet_b" style="display: block;"></div>
</div>
<!-- 컨텐츠 레이아웃//-->
