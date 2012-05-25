<%@page import="net.smartworks.model.security.AccessPolicy"%>
<%@page import="net.smartworks.model.sera.MemberInformList"%>
<%@page import="net.smartworks.model.sera.Team"%>
<%@page import="net.smartworks.model.sera.FriendInformList"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.model.sera.info.SeraUserInfo"%>
<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.model.sera.FriendList"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@ page contentType="text/html; charset=utf-8"%>

<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 그리고 현재사용자 정보도 가져온다.	
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();

	String courseId = request.getParameter("courseId");

	Team[] requestTeams = smartWorks.getJoinRequestTeamsByCourseId(courseId);
	int totalRequests = SmartUtil.isBlankObject(requestTeams) ? 0 : requestTeams.length;
%>

<!-- Panel Section -->
<div class="js_join_requests_page" courseId="<%=courseId %>">
	<!-- Panel1 -->
	<div>
		<div class="header mt10">
			<div class="tit">팀 가입요청 목록<span class="t_orange tb js_join_request_count" count="<%=totalRequests%>">(<%=totalRequests %>)</span></div>
		</div>
		<div class="panel_area">
			<%
			if(!SmartUtil.isBlankObject(requestTeams)){
				for(int i=0; i<requestTeams.length; i++){
					Team requestTeam = requestTeams[i];
					int memberCount = (SmartUtil.isBlankObject(requestTeam.getMembers())) ? 0 : requestTeam.getMembers().length;
			%>
					<!-- 목록1-->
					<div class="panel_rds_block mb10 js_join_request_item" teamId="<%=requestTeam.getId()%>">
						<ul>							
							<li class="">
								<span><%=CommonUtil.toNotNull(requestTeam.getName()) %><br/>
									(<%if(requestTeam.getAccessPolicy()==AccessPolicy.LEVEL_PRIVATE){ %>비공개<%}else{ %>공개<%} %>)
								</span>
							</li>
							<li class="">
								<span><%=CommonUtil.toNotNull(requestTeam.getDesc()) %></span>
							</li>
							<li class="">
								<span>기간 : <%=requestTeam.getStart().toLocalDateSimpleString() %> ~ <%=requestTeam.getEnd().toLocalDateSimpleString() %><br/>
									팀구성원 : <%=memberCount %>/<%=requestTeam.getMaxMembers() %><br/>
								</span>
							</li>
							<li class="fr bo_l end">
								<span>
								<!-- Btn -->
									<div class="btn_mid_l mr7 js_accept_join_team_btn">
										<div class="btn_mid_r"><span class="icon_blu_down mr3"></span>승 인</div>
									</div>
								<!-- Btn //-->
								<!-- Btn -->
									<div class="btn_mid_l mr7 js_deny_join_team_btn">
										<div class="btn_mid_r"><span class="icon_after_check"></span>거 절</div>
									</div>
								</span>
							</li>
						</ul>
					</div>
					<!-- 목록1//-->
			<%
				}
			}
			%>
		</div>
	</div>
	<!-- Panel1 //-->

	<%
	if(totalRequests>0){
		for(int i=0; i<requestTeams.length; i++){
			Team requestTeam = requestTeams[i];
			int memberCount = (SmartUtil.isBlankObject(requestTeam.getMembers())) ? 0 : requestTeam.getMembers().length;
	%>
			<!-- Panel2 -->
			<div>
				<div class="header mt20">
					<div class="fl tit"><%=requestTeam.getName() %><span>팀의 팀원들</span> <span class="t_orange tb js_member_count">(<%=memberCount %>)</span> </div>
					<div class="fr">
						<input class="fl fieldline js_member_search_key" style="width: 150px" type="text" />
						<button type="button" class="fl ml5 js_member_search_btn">검색</button>
					</div>
				</div>
		
				<div class="panel_area js_member_list">
					<%
					if(memberCount>0 && !SmartUtil.isBlankObject(requestTeam.getMembers())){
						for(int j=0; j<requestTeam.getMembers().length; j++){
							SeraUserInfo member = requestTeam.getMembers()[j];
					%>
							<!-- 목록1-->
							<div class="panel_rds_block mb10 js_member_item" userId="<%=member.getId()%>" teamId="<%=requestTeam.getId()%>">
								<ul>
									<li class="pl0pr10">
										<a href="othersPAGE.sw?userId=<%=member.getId()%>">
											<img class="profile_size_m" src="<%=member.getMinPicture() %>" />
										</a>
									</li>
									<li class="">
										<a href="othersPAGE.sw?userId=<%=member.getId()%>">
											<span><%=CommonUtil.toNotNull(member.getNickName()) %><br /> <span class="cb t_id"><%=CommonUtil.toNotNull(member.getName()) %></span></span>
										</a>
									</li>
									<li class="bo_l"><span><%=CommonUtil.toNotNull(member.getGoal()) %><br /> <span class="t_id"><%=member.getId() %></span>
									</span>
									</li>
									<li class="fr bo_l end">
										<span>
										<!-- Btn -->
											<div class="btn_fgreen_l mr7 js_friend_request_btn" userId="<%=member.getId() %>" <%if(member.isFriend()){%>style="display:none"<%} %>>
												<div class="btn_fgreen_r"><span class="icon_green_down"></span>친구 요청</div>
											</div> 
										<!-- Btn //-->
		
										<!-- Btn //--> 
											<div class="btn_fgreen_l js_destroy_friendship_btn" userId="<%=member.getId()%>" <%if(!member.isFriend()){%>style="display:none"<%} %>>
												<div class="btn_fgreen_r"><span class="icon_delete_inbtn"></span>친구 끊기</div>
											</div>
										<!-- Btn //--> 
										</span>
									</li>
								</ul>
							</div>
							<!-- 목록1//-->
					<%
						}
					}
					%>
				</div>
			</div>
			<!-- Panel2 //-->
	<%	
		}
	}
	%>
</div>
<!-- Panel Section //-->
