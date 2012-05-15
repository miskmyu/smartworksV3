<%@page import="net.smartworks.model.sera.MemberInformList"%>
<%@page import="net.smartworks.model.sera.MenteeInformList"%>
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

	int type = Integer.parseInt(request.getParameter("type"));
	String courseId = request.getParameter("courseId");
	String teamId = request.getParameter("teamId");
	String key = request.getParameter("key");
	
	SeraUserInfo[] teamMembers = smartWorks.searchTeamMemberByType(type, courseId, teamId, key);
	if(!SmartUtil.isBlankObject(teamMembers)){
		for(int i=0; i<teamMembers.length; i++){
			SeraUserInfo member = teamMembers[i];

			switch(type){
			case MemberInformList.TYPE_MEMBERS:
	%>
				<!-- 목록1-->
				<div class="panel_rds_block mb10 js_member_item" userId="<%=member.getId()%>" teamId="<%=teamId%>">
					<ul>
						<li class="pl0pr10">
							<a href="othersPAGE.sw?userId=<%=member.getId()%>">
								<img class="profile_size_m" src="<%=member.getMinPicture() %>" />
							</a>
						</li>
						<li class="w90">
							<a href="othersPAGE.sw?userId=<%=member.getId()%>">
								<span><%=CommonUtil.toNotNull(member.getNickName()) %><br /> <span class="cb t_id"><%=CommonUtil.toNotNull(member.getName()) %></span></span>
							</a>
						</li>
						<li class="bo_l w370"><span><%=CommonUtil.toNotNull(member.getGoal()) %><br /> <span class="t_id"><%=member.getId() %></span>
						</span>
						</li>
						<li class="fr bo_l">
							<span> <!-- Btn -->
								<div class="btn_fgreen_l js_destroy_membership_btn" userId="<%=member.getId()%>">
									<div class="btn_fgreen_r"><span class="icon_green_down"></span>팀원 삭제</div>
								</div> <!-- Btn //--> 
							</span>
							<span>
								<div class="btn_fgreen_l js_friend_request_btn" userId="<%=member.getId() %>" <%if(member.isFriend()){%>style="display:none"<%} %>>
									<div class="btn_fgreen_r"><span class="icon_green_down"></span>친구 요청</div>
								</div> <!-- Btn //--> 
								<div class="btn_fgreen_l js_destroy_friendship_btn" userId="<%=member.getId()%>" <%if(!member.isFriend()){%>style="display:none"<%} %>>
									<div class="btn_fgreen_r"><span class="icon_green_down"></span>친구 끊기</div>
								</div> <!-- Btn //--> 
							</span>
						</li>
					</ul>
				</div>
				<!-- 목록1//-->
	<%
				break;
			case MemberInformList.TYPE_NON_MEMBERS:
	%>
				<!-- 목록1-->
				<div class="panel_rds_block mb10 js_non_member_item" userId="<%=member.getId()%>">
					<ul>
						<li class="pl0pr10">
							<a href="othersPAGE.sw?userId=<%=member.getId()%>">
								<img class="profile_size_m" src="<%=member.getMinPicture() %>" />
							</a>
						</li>
						<li class="w90">
							<a href="othersPAGE.sw?userId=<%=member.getId()%>">
								<span><%=CommonUtil.toNotNull(member.getNickName()) %><br /> <span class="cb t_id"><%=CommonUtil.toNotNull(member.getName()) %></span></span>
							</a>
						</li>
						<li class="bo_l w370"><span><%=CommonUtil.toNotNull(member.getGoal()) %><br /> <span class="t_id"><%=member.getId() %></span>
						</span>
						</li>
						<li class="fr bo_l">
							<span> <!-- Btn -->
								<div class="btn_fgreen_l js_member_request_btn" userId="<%=member.getId()%>" teamId="<%=teamId%>">
									<div class="btn_fgreen_r"><span class="icon_green_down"></span>팀원 요청</div>
								</div> <!-- Btn //--> 
							</span>
							<span> <!-- Btn -->
								<div class="btn_fgreen_l js_friend_request_btn" userId="<%=member.getId() %>" <%if(member.isFriend()){%>style="display:none"<%} %>>
									<div class="btn_fgreen_r"><span class="icon_green_down"></span>친구 요청</div>
								</div> <!-- Btn //--> 
								<div class="btn_fgreen_l js_destroy_friendship_btn" userId="<%=member.getId()%>" <%if(!member.isFriend()){%>style="display:none"<%} %>>
									<div class="btn_fgreen_r"><span class="icon_green_down"></span>친구 끊기</div>
								</div> <!-- Btn //--> 
							</span>
						</li>
					</ul>
				</div>
				<!-- 목록1//-->
	<%
				break;
			}
		}		
	}
	%>
