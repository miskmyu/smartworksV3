<%@page import="net.smartworks.util.SmartTest"%>
<%@page import="net.smartworks.model.community.info.GroupMemberList"%>
<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.model.community.Community"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
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
	Group group = (Group)workSpace;
	
	GroupMemberList groupMemberList = smartWorks.getGroupMemberInformList(group.getId());
	
%>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<div class="section_portlet js_space_tab_group_members_page js_space_tab_group" groupId=<%=group.getId() %>>
	<div class="portlet_t">
		<div class="portlet_tl"></div>
	</div>
	<div class="portlet_l" style="display: block;">
		<ul class="portlet_r" style="display: block;">
			<!-- 화면 -->
			<div class="contents_space setting_section">
				<div class="titl_section">
					<!-- 타이틀을 나타내는 곳 -->
					<div class="title pr10 fl">
						<span><a href="" class="js_select_group_space_tab js_setting"><fmt:message key="common.title.space_setting"/></a> </span> | 
						<span class="current"><a href="" class="js_select_group_space_tab js_members js_group_members_tab"><fmt:message key="group.title.members"/></a></span>
					</div>
				</div>
				<div class="solid_line mb10"></div>
				<!-- 설정 부분 -->
				<div class="list_contents">
	            <!-- 가입승인 대기 -->
	                <div>
	                <!-- 타이틀 -->
	                <div class="list_title_space solid_line_sb pb3">
	                    <div class="title"><fmt:message key="group.title.join_requesters"/> 
	                    	<span class="t_red_bold">[<%=groupMemberList.getTotalRequesters() %>]</span>
	                    </div>
	                </div>
	                <!-- 타이틀 //-->
	                <div class="space_section">
	                    <ul>
	                    	<%
	                    	if(!SmartUtil.isBlankObject(groupMemberList.getRequesters())){
	                    		UserInfo[] requesters = groupMemberList.getRequesters();
	                    		for(int i=0; i<requesters.length; i++){
	                    			UserInfo requester = requesters[i];
	                    			requester.setDepartment(SmartTest.getDepartmentInfo1());
	                    	%>
		                        <li class="instance_list">
		                        	<div class="det_title">
		                                <div class="profile_photo"><img class="profile_size_m" src="<%=requester.getMidPicture()%>"></div>
		                                <div>
			                                <span>
			                                <%=requester.getLongName() %> </br>
			                                <%=requester.getDepartment().getFullpathName() %>
			                                </span>
		                                	<span class="bar">|</span>
		                                </div>
		                                <%-- <div><span><%=requester.getDepartment().getFullpathName() %></span><span class="bar">|</span></div> --%>
		                                <div><span><%=requester.getId() %></span></div>
		                                <div class="buttonSet">
		                                	<span class="bar">|</span>
			                                <span>
			                                    <a href="" class="js_accept_join_group button mr3" userId="<%=requester.getId()%>"><fmt:message key="group.button.accept_join"/></a>
			                                    <a href="" class="js_reject_join_group button" userId="<%=requester.getId()%>"><fmt:message key="group.button.reject_join"/></a>
			                                </span>
		                                </div>
		                            </div>
		                        </li>
	                    	<%
	                    		}
	                    	}
	                    	%>
						</ul>
                   </div>
	                <!-- 가입승인 대기 //-->
	                
					<!-- 구성원 -->
	              	<div class="mt20">
	                    <!-- 타이틀 -->
	                    <div class="list_title_space solid_line_sb pb3">
	                        <div class="title"><fmt:message key="group.title.members"/> <span class="t_red_bold">[<%=groupMemberList.getTotalMembers() %>]</span> </div>
	                    </div>
	                    <!-- 타이틀 //-->
	                  	<div class="space_section member">
	                    	<ul>
	                    		<%
	                    		if(!SmartUtil.isBlankObject(groupMemberList.getMembers())){
	                    			UserInfo[] members = groupMemberList.getMembers();
	                    			for(int i=0; i<members.length; i++){
	                    				UserInfo member = members[i];
	                    				member.setDepartment(SmartTest.getDepartmentInfo1());
	                    		%>
				                        <li class="instance_list">
				                        	<div class="det_title">
				                                <div class="profile_photo">
				                                	<%if(member.getRole() == User.USER_ROLE_LEADER){ %><span class="leader m"></span><%} %>
				                                  	<img class="profile_size_m" src="<%=member.getMidPicture()%>">
				                         		</div>
				                                <div>
					                                <span>
					                                <%=member.getLongName() %><%if(member.getRole()==User.USER_ROLE_LEADER){ %>(<fmt:message key="group.title.leader"/>)<%} %></br>
					                                <%=member.getDepartment().getFullpathName() %>
					                                </span>
					                                <span class="bar">|</span>
				                                </div>
				                                <div>
					                                <span>
					                                	<%=member.getId() %>
					                                </span>
					                                <!-- <span class="bar">|</span> -->
				                                </div>
				                                <%-- <div><span><%=member.getId() %></span><span class="bar">|</span></div> --%>
				                                <!-- <div style="width:100px"><span>가입일: <br>2012.03.25</span><span class="bar">|</span></div>
				                                <div style="width:100px"><span>최근 방문일: <br>2012.05.12 18:50</div> -->
				                                <div class="buttonSet">
				                                	<span class="bar">|</span>
				                                	<%
				                                	if(member.getId().equals(cUser.getId())){
				                                	%>
				                                    	<span><a href="" class="js_leave_group_request button" isGroupLeader="<%=group.amIGroupLeader(cUser)%>"><fmt:message key="group.button.leave"/></a></span>
				                                	<%
				                                	}else if(group.getLeader().getId().equals(cUser.getId()) && !member.getId().equals(cUser.getId())){
				                                	%>
	                                    				<span><a href="" class="js_pushout_group_member button" memberId="<%=member.getId()%>"><fmt:message key="group.button.pushout"/></a></span>
	                                    			<%
	                                    			}
	                                    			%>
				                                </div>
				                            </div>
				                        </li>
	                    		
	                    		<%
	                    			}
	                    		}
	                    		%>
		                      	<%
		                      	if(!SmartUtil.isBlankObject(groupMemberList.getMembers()) && groupMemberList.getMembers().length<groupMemberList.getTotalMembers()){
		                      	%>
			                      	<!-- 더보기 -->
			                      	<li class="t_nowork">
			                        	<a class="js_group_more_members" lastId="<%=groupMemberList.getMembers()[groupMemberList.getMembers().length-1].getId()%>"><fmt:message key="common.message.more_work_task"/></a>
										<span class="js_progress_span"></span>
			                      	</li>
			                      	<!-- 더보기 -->
		                      	<%
		                      	}
		                      	%>
	                    	</ul>
	                  	</div>
	              </div>
	            <!-- 구성원 //-->                
	            </div>
	            <!-- 설정 부분 //-->
			</div>
			<!-- 화면 //-->
		</div>
		<!-- 버튼 영역 -->
			<div class="glo_btn_space">
				<div class="fr ml10">
					<span class="btn_gray ml5">
						<a onclick="return true;" href="<%=group.getSpaceController()%>?cid=<%=group.getSpaceContextId()%>&wid=<%=group.getId()%>"> 
							<span class="txt_btn_start"></span>
							<span class="txt_btn_center"><fmt:message key="common.button.cancel"/></span> 
							<span class="txt_btn_end"></span>
						</a> 
					</span>
				</div>
				<!-- 실행시 표시되는 프로그래스아이콘을 표시할 공간 -->
				<span class="fr form_space js_progress_span"></span> 
				<!-- 실행시 데이터 유효성 검사이상시 에러메시지를 표시할 공간 -->
				<div class="form_space sw_error_message js_profile_error_message" style="text-align: right; color: red; line-height: 20px"></div>
			</div>
		<!-- 버튼 영역 //-->
	</ul>
	</div>
	<div class="portlet_b" style="display: block;"></div>
</div>