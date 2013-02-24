
<!-- Name 			: three_days_event.jsp											 -->
<!-- Description	: 현재사용자에게 해당되는 오늘, 내일 그리고 모레이후의 이벤트를 표시해주는 화면 -->
<!-- Author			: Maninsoft, Inc.												 -->
<!-- Created Date	: 2011.9.														 -->

<%@page import="java.util.TimeZone"%>
<%@page import="java.util.Date"%>
<%@page import="net.smartworks.model.work.SmartWork"%>
<%@page import="net.smartworks.model.community.info.DepartmentInfo"%>
<%@page import="net.smartworks.model.community.info.GroupInfo"%>
<%@page import="net.smartworks.model.community.info.WorkSpaceInfo"%>
<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.model.instance.info.EventInstanceInfo"%>
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
	String wid = (String)session.getAttribute("wid");
	
	// 회사 달력에 있는 3일의 달력정보를 가져온다...
	CompanyCalendar[] threeDaysCC = smartWorks.getCompanyCalendars(new LocalDate(), 3);
	LocalDate today = new LocalDate(threeDaysCC[0].getDate().getGMTDate());
	LocalDate tomorrow = new LocalDate(threeDaysCC[1].getDate().getGMTDate());
	LocalDate afterTomorrow =  new LocalDate(threeDaysCC[2].getDate().getGMTDate());
	
	EventInstanceInfo[] events = null;
	if(SmartUtil.isBlankObject(wid))
	// 나에 대한 이벤트들을 오늘부터 10일간을 가져온다...
		events = smartWorks.getMyEventInstances(new LocalDate(), 10);
	else
		events = smartWorks.getCommunityEventInstances(new LocalDate(), 10, wid);
	
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

	<!-- 이벤트,공지 포틀릿 -->	
	<div class="section_portlet mb20">
        <!-- 오늘에 대한 날짜 이벤트 시간을 표시하는 곳 -->
        <div class="red_stroke">
        	<span class="red_tab_content">
        		<span class="js_now_date_string"><%=today.toLocalDateString()%></span>
        		<span class="t_light_orange">
				<%
				// 오늘에 대한 회사 이벤트들이 있으면 나타낼 준비를 한다.....
				if (!SmartUtil.isBlankObject(threeDaysCC[0].getCompanyEvents())) {
				%> 
			 		( 
			 	<%
			 	}
			 	CompanyEvent[] cesToday = threeDaysCC[0].getCompanyEvents();
			 	CompanyEvent[] cesTomorrow = threeDaysCC[1].getCompanyEvents();
			 	CompanyEvent[] cesAfterTomorrow = threeDaysCC[2].getCompanyEvents();
			 	// 오늘에 대한 이벤트를 표시한다..
			 	if(cesToday != null) {
			 	for (int i = 0; i < cesToday.length; i++) {
			 		if (i != 0) {
			 	%>
				 			,
			 		<%
		 			}
		 			%>
		 			<%=cesToday[i].getName()%> 
		 		<%
		 		}
			 	}
		 		%> 
			 	<%
			 	// 오늘에 대한 이벤트가 있었으면 마무리한다...
			 	if (!SmartUtil.isBlankObject(threeDaysCC[0].getCompanyEvents())) {
			 	%>
			 		)
			 	<%
			 	}
			 	%>
				</span> 
				<span class="js_now_time_string"><%=today.toLocalTimeString()%></span>
        		<span class="right_cap"></span>
        	</span>
        	<div class="red_stroke_right"></div>
        </div>
        <!-- 오늘에 대한 날짜 이벤트 시간을 표시하는 곳 //-->

		<!-- 오늘, 내일 그리고 모레이후 이벤트들을 나타내는 곳 -->
	    <div class="portlet_l" style="display: block;">
	   		<ul class="portlet_r" style="display: block;">
		
				<!-- 이벤트 목록 영역 -->
				<div class="event_space">
					<ul>
						<%
						// start 오늘, 내일, 그리고 모레의 회사 이벤트를 나타내는 곳
						for (int cnt = 0; cnt < threeDaysCC.length; cnt++) {
						%>
							<!-- 하루씩 증가하면서 이벤트박스 생성  -->
							<li class="fl event_cell<%=cnt%>">
								<div>
									<div class="event_cell" style="display: block;">
										<ul style="display: block;">
											<%
											if (cnt == 0) {
											%>
												<li class="line_dashed tc">
													<span class="cell_date"><span class="tb"><fmt:message key='content.threedays.today' /> </span> <%=today.toLocalDateShortString()%></span>
												</li>
											<%
											} else if (cnt == 1) {
											%>
												<li class="line_dashed tc">
													<span class="cell_date"><span class="tb"><fmt:message key='content.threedays.tomorrow' /> </span> <%=tomorrow.toLocalDateShortString()%></span>
												</li>
											<%
											} else if (cnt == 2) {
											%>
												<li class="line_dashed tc">
													<span class="cell_date"><span class="tb"><fmt:message key='content.threedays.after' /> </span></span>
												</li>
											<%
											}
											%>
											<li>
												<span class="t_red"> 
													<%
													if(cesToday != null) {
				 									for (int i = 0; (cnt == 0) && (i < cesToday.length); i++) {
				 										if (i != 0) {
				 									%>
				 											, 
				 										<%
				 										}
				 										%>
				 										<%=cesToday[i].getName()%> 
				 									<%
				 									}
													}
				 									%> 
				 									<%
				 									if(cesTomorrow != null) {
				 									for (int i = 0; (cnt == 1) && (i < cesTomorrow.length); i++) {
				 										if (i != 0) {
				 									%>
				 											, 
				 										<%
				 										}
				 										%>
				 										<%=cesTomorrow[i].getName()%> 
				 									<%
				 									}
				 									}
				 									%>
				 									<%
				 									if(cesAfterTomorrow != null) {
				 									for (int i = 0; (cnt == 2) && (i < cesAfterTomorrow.length); i++) {
				 										if (i != 0) {
				 									%>
				 											, 
				 										<%
				 										}
				 										%>
				 										<%=cesAfterTomorrow[i].getName()%> 
				 									<%
				 									}
				 									}
				 									%> 
				 								</span>
											</li>
											<%
											if (events != null && events.length>0) {
												boolean hasTodayEvent=false, hasTomorrowEvent=false, hasAfterEvent=false;
												// Start 이벤트가 있으면 있는 만큼 돌면서 리스트를 만든다...
												for (EventInstanceInfo event : events) {
													if (((cnt == 0) && today.isSameDate(event.getStart())) || ((cnt == 1) && tomorrow.isSameDate(event.getStart()))
															|| ((cnt == 2) && tomorrow.isAfterDate(event.getStart()))) {
														UserInfo owner = event.getOwner();
//														WorkSpaceInfo workSpace = event.getWorkSpace();
														String workSpaceId = event.getWorkSpaceId();
														String workSpaceName = event.getWorkSpaceName();
														int workSpaceType = event.getWorkSpaceType();
														if (cnt < 2) {
															if(cnt==0) hasTodayEvent = true;
															else hasTomorrowEvent = true;
											%>
															<li class="event_list">
																<span class="t_gbold"><%=event.getStart().toLocalTimeShortString()%></span>
														<%
														} else {
															hasAfterEvent = true;
														%>
															<li class="event_list">
																<span class="t_gbold"><%=event.getStart().toLocalString()%></span>
														<%
														}
														%>
				 										<a href="<%=event.getController() %>?cid=<%=event.getContextId()%>&workId=<%=SmartWork.ID_EVENT_MANAGEMENT%>&wid=<%=workSpaceId%>"><%=event.getSubject()%></a>														
														<%
														if (!owner.getId().equals(cUser.getId())) {
														%> 
															<span class="t_name"><a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>" title="<%=owner.getLongName()%>"><%=owner.getName()%></a></span>
														<%
				 										}
				 										%> 
				 										<%
				 										if (!workSpaceId.equals(owner.getId())) {
				 										%> 
															<span class="arr">▶</span><span class="space_name"><a href="<%=WorkSpaceInfo.getSpaceController(workSpaceType)%>?cid=<%=WorkSpaceInfo.getSpaceContextId(workSpaceType, workSpaceId)%>"><%=workSpaceName%></a></span> 
				 										<%
				 										}
				 										%>
														<%if(event.getSubInstanceCount()>0){ %><font class="t_sub_count">[<b><%=event.getSubInstanceCount() %></b>]</font><%} %>
														<%if(event.isNew()){ %><span class="icon_new"></span><%} %>
													</li>
													<%
													}
												}
												// End 이벤트가 있으면 있는 만큼 돌면서 리스트를 만든다...
											// 이벤트가 없으면 이벤트가 없습니다라고 표시한다..
												if(cnt==0 && !hasTodayEvent || cnt==1 && !hasTomorrowEvent || cnt==2 && !hasAfterEvent){
												%>
													<li><fmt:message key="common.message.no_event"/></li>
											<%
												}
											}else{
											%>
												<li><fmt:message key="common.message.no_event"/></li>
											<%
											}
											%>
										</ul>
									</div>
								</div>
							</li>
							<!-- 하루씩 증가하면서 이벤트박스 생성 //-->
						<%
		 				}
						// end 오늘, 내일, 그리고 모레의 회사 이벤트를 나타내는 곳
						%>
					</ul>				
				</div>
				<!-- 이벤트 목록 영역 //-->
				
				<!-- 최근 5개 공지사항을 보여주는 곳 -->
				<div>
					<jsp:include page="/jsp/content/today/recent_board_list.jsp" />
				</div>
				<!-- 최근 5개 공지사항을 보여주는 곳 //-->
				
			</ul>
		</div>
		<!-- 오늘, 내일 그리고 모레이후 이벤트들을 나타내는 곳 //-->

		<div class="portlet_b" style="display: block;"></div>

	</div>
	<!-- 이벤트,공지 포틀릿//-->
