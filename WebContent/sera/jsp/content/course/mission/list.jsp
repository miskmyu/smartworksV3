<%@page import="net.smartworks.model.instance.Instance"%>
<%@page import="java.util.Calendar"%>
<%@page import="net.smartworks.model.sera.info.MissionInstanceInfo"%>
<%@page import="net.smartworks.model.sera.Course"%>
<%@page import="net.smartworks.model.instance.info.EventInstanceInfo"%>
<%@page import="java.util.Date"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@ page contentType="text/html; charset=utf-8"%>

<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 그리고 현재사용자 정보도 가져온다.	
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();

	String courseId = request.getParameter("courseId");
	Course course = (Course)session.getAttribute("course");
	if(SmartUtil.isBlankObject(course) || !course.getId().equals(courseId)) course = smartWorks.getCourseById(courseId);
	
	LocalDate thisDate = new LocalDate();
	String todayStr = request.getParameter("today");
	if(!SmartUtil.isBlankObject(todayStr)){
		thisDate = LocalDate.convertLocalDateStringToLocalDate(todayStr);
	}
	LocalDate firstDateOfMonth = LocalDate.convertLocalMonthWithDiffMonth(thisDate, 0);
	String prevMonthStr = LocalDate.convertLocalMonthWithDiffMonth(thisDate, -1).toLocalDateSimpleString();
	String nextMonthStr = LocalDate.convertLocalMonthWithDiffMonth(thisDate, 1).toLocalDateSimpleString();
%>
<!-- Nav SNB -->
<div id="nav_snb" class="js_mission_list_page" prevMonth="<%=prevMonthStr %>" nextMonth="<%=nextMonthStr%>" courseId="<%=courseId%>">
	<div class="this_month">
		<div class="tit_area">
			<a href="" class="b_prev fl js_prev_month_mission"></a>
				<span class="tit"><%=thisDate.toLocalMonthString() %></span>
			<a href="" class="b_next fl js_next_month_mission"></a>
		</div>
		<div class="cb">
			<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<th width="36px">날짜</th>
					<th width="146px">내용</th>
				</tr>
				<%
				LocalDate today = new LocalDate();
				EventInstanceInfo[] events = null;//smartWorks.getEventInstanceList(course.getId(), firstDateOfMonth, new LocalDate(firstDateOfMonth.getGMTDate() + LocalDate.ONE_DAY*31));
				MissionInstanceInfo[] missions = smartWorks.getMissionInstanceList(course.getId(), firstDateOfMonth, new LocalDate(firstDateOfMonth.getGMTDate() + LocalDate.ONE_DAY*31));
				for(int i=0; i<31; i++){
					LocalDate currentDate = new LocalDate(firstDateOfMonth.getGMTDate() + LocalDate.ONE_DAY*i);
					if(thisDate.getMonth() != currentDate.getMonth()) break;
					String todayClass = (today.getYear()==currentDate.getYear() && today.getMonth()==currentDate.getMonth() && today.getDateOnly()==currentDate.getDateOnly()) ? "today_cel" : "";
					String dayClass = (currentDate.getDayOfWeek()==Calendar.SATURDAY) ? "t_blue" : (currentDate.getDayOfWeek()==Calendar.SUNDAY) ? "t_red" : "";
 				%>
					<tr href="" class="<%=todayClass%> js_create_new_mission">
						<td class="<%=dayClass %>"><%=String.format("%02d", currentDate.getDateOnly()) %>(<%=currentDate.toLocalDayString() %>)</td>
						<td>
							<%
							if(!SmartUtil.isBlankObject(events)){
								for(int e=0; e<events.length; e++){
									EventInstanceInfo event = events[e];
									if(event.getStart().getMonth() != currentDate.getMonth() || event.getStart().getDateOnly() != currentDate.getDateOnly()) continue;
							%>
									<div>[이벤트] <%=event.getSubject() %></div>
							<%
								}
							}
							%>
							<%
							if(!SmartUtil.isBlankObject(missions)){
								for(int m=0; m<missions.length; m++){
									MissionInstanceInfo mission = missions[m];
									if(mission.getOpenDate().getMonth() != currentDate.getMonth() || mission.getOpenDate().getDateOnly() != currentDate.getDateOnly()) continue;
									String iconClass = (mission.getOpenDate().getDateOnly()>today.getDateOnly()) ? "icon_reserve" : (mission.isClearedByMe()) ? "icon_mission" :  "icon_mission current";
							%>
									<a href="" class="js_select_mission" missionId="<%=mission.getId()%>"><div><span class="<%=iconClass%>"></span>미션<%=mission.getIndex()+1%> <%=mission.getSubject() %></div></a>
							<%
								}
							}
							%>
						</td>
					</tr>
				<%
				}
				%>
			</table>
		</div>
	</div>
</div>
<!-- Nav SNB //-->

<!-- Navi Indication-->
<div class="icon_Indication" style="top: 215px">미션마감일을 선택한 후, 미션을 등록할수 있습니다 = 미션 마감일 선택한 위치</div>
<!-- Navi Indication//-->

<!-- Section Center -->
<div id="section_cen" class="js_mission_space_detail">
	<jsp:include page="/sera/jsp/content/course/mission/perform.jsp"></jsp:include>
</div>
<!-- Section Center //-->