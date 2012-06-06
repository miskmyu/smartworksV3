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
				if(member.getId().equals(cUser.getId())){
		%>
					<!-- 목록1-->
					<div class="panel_rds_block mb10 js_member_item" userId="<%=member.getId()%>" teamId="<%=teamId%>">
						<ul>
							<li class="pl0pr10">
								<a href="myPAGE.sw">
									<img class="profile_size_m" src="<%=member.getMinPicture() %>" />
								</a>
							</li>
							<li class="">
								<a href="myPAGE.sw">
									<span><%=CommonUtil.toNotNull(member.getNickName()) %><br /> <span class="cb t_id"><%=CommonUtil.toNotNull(member.getName()) %></span></span>
								</a>
							</li>
							<li class="bo_l"><span><%=CommonUtil.toNotNull(member.getGoal()) %><br /> <span class="t_id"><%=member.getId() %></span>
							</span>
							</li>
							<li class="fr bo_l end">
								<span>
								<!-- Btn -->
									<div class="btn_fred_l mr7 js_leave_team_btn">
										<div class="btn_fred_r"><span class="icon_delete_inbtn"></span>팀 탈퇴</div>
									</div>
								<!-- Btn //--> 
								</span>
							</li>
						</ul>
					</div>
					<!-- 목록1//-->
				<%
				}else{
				%>
					<!-- 목록1-->
					<div class="panel_rds_block mb10 js_member_item" userId="<%=member.getId()%>" teamId="<%=teamId%>">
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
									<div class="btn_fred_l mr7 js_destroy_membership_btn" userId="<%=member.getId()%>">
										<div class="btn_fred_r"><span class="icon_delete_inbtn"></span>팀원 삭제</div>
									</div>
								<!-- Btn //--> 

								<!-- Btn -->
									<div class="btn_fgreen_l mr7 js_friend_request_btn" userId="<%=member.getId() %>" <%if(member.isFriend()){%>style="display:none"<%} %>>
										<div class="btn_fgreen_r"><span class="icon_green_down"></span>친구 요청</div>
									</div> 
								<!-- Btn //-->

								<!-- Btn //--> 
									<div class="btn_fgray_l mr7 js_destroy_friendship_btn" userId="<%=member.getId()%>" <%if(!member.isFriend()){%>style="display:none"<%} %>>
										<div class="btn_fgray_r"><span class="icon_delete_inbtn"></span>친구 끊기</div>
									</div>
								<!-- Btn //--> 
								</span>
							</li>
						</ul>
					</div>
					<!-- 목록1//-->
			<%
				}
				break;
			case MemberInformList.TYPE_INVITED_MEMBERS:
			%>
				<!-- 목록1-->
				<div class="panel_rds_block mb10 js_invited_member_item" userId="<%=member.getId()%>">
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
						
							<!-- Btn -->
								<div class="btn_fgray_l mr7 js_destroy_friendship_btn" userId="<%=member.getId()%>" <%if(!member.isFriend()){%>style="display:none"<%} %>>
									<div class="btn_fgray_r"><span class="icon_delete_inbtn"></span>친구 끊기</div>
								</div>
							<!-- Btn //--> 
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
							<div class="btn_fblu_l mr7 js_join_team_request_btn" userId="<%=member.getId()%>" teamId="<%=teamId%>">
								<div class="btn_fblu_r"><span class="icon_bludown_inbtn"></span>팀원초대</div>
							</div>
						<!-- Btn //--> 
						
						<!-- Btn -->
							<div class="btn_fgreen_l mr7 js_friend_request_btn" userId="<%=member.getId() %>" <%if(member.isFriend()){%>style="display:none"<%} %>>
								<div class="btn_fgreen_r"><span class="icon_green_down"></span>친구 요청</div>
							</div> 
						<!-- Btn //--> 
					
						<!-- Btn -->
							<div class="btn_fgray_l mr7 js_destroy_friendship_btn" userId="<%=member.getId()%>" <%if(!member.isFriend()){%>style="display:none"<%} %>>
								<div class="btn_fgray_r"><span class="icon_delete_inbtn"></span>친구 끊기</div>
							</div>
						<!-- Btn //--> 
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
