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
	
	MissionInstanceInfo[] missions = smartWorks.getMissionInstanceList(courseId, null, null);

	if(!SmartUtil.isBlankObject(missions)){
		for(int i=0; i<missions.length; i++){
			MissionInstanceInfo mission = missions[i];
%>
	<!-- 미션 수행 -->
	<div class="panel_block mt10 js_mission_list_item" style="width: 688px" courseId="<%=courseId%>" missionId="<%=mission.getId()%>">
		<dl class="content_mission">
			<dt>
				<!-- title -->
				<div class="tit_mission_l">
					<div class="tit_mission_r">[미션<%=mission.getIndex()+1 %>]</div>
				</div>
				<span class="tb ml5"><%=mission.getSubject() %></span>
				<!-- 우측영역 -->
				<div class="fr ">
					<%
					if(course.isMyRunningCourse()){
					%>
						<%
						if(SmartUtil.isBlankObject(mission.getMissionClearers())){
						%>
							<div class="icon_delete_red fr ml10 js_delete_mission_btn">
								<a href="" title="미션삭제"> </a>
							</div>
						<%
						}
						%>
						<div class="btn_mid_l fr ml10 js_show_modify_mission">
							<div class="btn_mid_r">미션수정</div>
						</div>
					<%
					}
					LocalDate now = new LocalDate();
					LocalDate closeDate = mission.getCloseDate();
					String remainDays = (LocalDate.getDiffDate(closeDate, now)>0) ? LocalDate.getDiffDate(closeDate, now) + "일 지남" : LocalDate.getDiffDate(now, closeDate)+1 + "일 남음";
					%>
					<span class="t_redb"><%=remainDays%></span> <span class="t_refe ml5"><%=closeDate.toLocalDateTimeSimpleString() %></span>
				</div>
			</dt>
			<dd style="display: block" class="js_mission_content_item">
				<div class="text w100"><%=mission.getContent() %></div>
				<%
				if(!SmartUtil.isBlankObject(mission.getFileGroupId())){
				%>
					<div><%=SmartUtil.getFilesDetailInfo(mission.getFiles()) %></div>
				<%
				}
				%>
				<!-- 별점 -->
				<div class="star_score fr">
					<ul>
						<%
						for(int j=0; j<5; j++){
							String pointClass = "";
							if(mission.getStarPoint()>j)
								if(mission.getStarPoint()>=(j+1))
									pointClass = "full";
								else
									pointClass = "half";
						%>
							<li class="icon_star_score <%=pointClass%>"><a href=""> </a></li>
						<%
						}
						%>
					</ul>
				</div>
				<!-- 별점 //-->
			</dd>
			<!-- Icon Close -->
			<div class="icon_close_area">
				<div class="icon_close_red fr js_toggle_mission_btn">
					<a href=""> </a>
				</div>
			</div>
			<%
			if((course.isMyAttendingCourse() || course.isMyRunningCourse()) && !mission.isClearedByMe()){
			%>
				<div class="btn_default_l cb fr js_select_mission" style="margin:5px 10px" missionId="<%=mission.getId()%>">
					<div class="btn_default_r">미션수행</div>
				</div>
			<%
			}
			%>
		</dl>
	</div>
	<!-- 미션수행 //-->
	<%
		}
	}
	%>
