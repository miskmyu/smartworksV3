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
	String teamId = request.getParameter("teamId");
	Team team = SmartUtil.isBlankObject(teamId) ? null :  (Team)session.getAttribute("team");
	if(!SmartUtil.isBlankObject(teamId) && SmartUtil.isBlankObject(team)) team = smartWorks.getTeamById(teamId);

	MemberInformList memberInforms = smartWorks.getTeamMemberInformations(teamId, MemberInformList.MAX_MEMBER_LIST);
%>

<!-- Panel Section -->
<div class="js_team_members_page" courseId="<%=courseId %>" teamId="<%=teamId %>" >
	<!-- Panel2 -->
	<div>
		<div class="header dep2 mt20">
			<div class="fl tit"><%=team.getName() %><span>팀의 팀원</span> <span class="t_orange tb js_member_count">(<%=memberInforms.getTotalMembers() %>)</span> </div>
			<div class="fr">
				<input class="fl fieldline js_member_search_key" style="width: 150px" type="text" />
				<button type="button" class="fl ml5 js_member_search_btn">검색</button>
			</div>
		</div>

		<div class="panel_area js_member_list">
			<%
			if(memberInforms.getTotalMembers()>0 && !SmartUtil.isBlankObject(memberInforms.getMembers())){
				for(int i=0; i<memberInforms.getMembers().length; i++){
					if (i == MemberInformList.MAX_MEMBER_LIST)
						break;
					SeraUserInfo member = memberInforms.getMembers()[i];
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
				}
			}
			%>
		</div>
	</div>
	<!-- Panel2 //-->

	<%
	if (memberInforms.getTotalMembers() > 0 && !SmartUtil.isBlankObject(memberInforms.getMembers())) {
		SeraUserInfo[] members = memberInforms.getMembers();
		if(memberInforms.getTotalMembers()>members.length){
			String lastId = members[members.length-2].getId(); 
	%>
			<!-- 더보기 -->
			<div class="more js_more_member_informs_btn js_more_member_btn" requestType="<%=MemberInformList.TYPE_MEMBERS %>" teamId="<%=teamId%>" lastId="<%=lastId%>">
				<div class="icon_more">더보기<span class="ml3 js_progress_span"></span></div>
				
			</div>
			<!-- 더보기 //-->
	<%
		}
	}	
	%>
	<!-- Panel3 -->
	<div>
		<div class="header dep2 mt20">
			<div class="tit">팀원수락 대기 멘티 <span class="t_orange tb js_invited_member_count">(<%=memberInforms.getTotalInvitedMembers() %>)</span> </div>
			<div class="fr">
				<input class="fl fieldline js_invited_member_search_key" style="width: 150px" type="text" />
				<button type="button" class="fl ml5 js_invited_member_search_btn">검색</button>
			</div>
		</div>

		<div class="panel_area js_invited_member_list">
			<%
			if(memberInforms.getTotalInvitedMembers()>0 && !SmartUtil.isBlankObject(memberInforms.getInvitedMembers())){
				for(int i=0; i<memberInforms.getInvitedMembers().length; i++){
					if (i == MemberInformList.MAX_MEMBER_LIST)
						break;
					SeraUserInfo member = memberInforms.getInvitedMembers()[i];
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
				}
			}
			%>
		</div>
	</div>
	<!-- Panel2 //-->

	<%
	if (memberInforms.getTotalInvitedMembers() > 0 && !SmartUtil.isBlankObject(memberInforms.getInvitedMembers())) {
		SeraUserInfo[] members = memberInforms.getInvitedMembers();
		if(memberInforms.getTotalInvitedMembers()>members.length){
			String lastId = members[members.length-2].getId(); 
	%>
			<!-- 더보기 -->
			<div class="more js_more_member_informs_btn js_more_invited_member_btn" requestType="<%=MemberInformList.TYPE_INVITED_MEMBERS %>" teamId="<%=teamId%>" lastId="<%=lastId%>">
				<div class="icon_more">더보기<span class="ml3 js_progress_span"></span></div>
				
			</div>
			<!-- 더보기 //-->
	<%
		}
	}	
	%>
	<!-- Panel3 -->
	<div>
		<div class="header dep2 mt20">
			<div class="tit">초대 가능한 멘티 <span class="t_orange tb js_non_member_count">(<%=memberInforms.getTotalNonMembers() %>)</span> </div>
			<div class="fr">
				<input class="fl fieldline js_non_member_search_key" style="width: 150px" type="text" />
				<button type="button" class="fl ml5 js_non_member_search_btn">검색</button>
			</div>
		</div>

		<div class="panel_area js_non_member_list">
			<%
			if(memberInforms.getTotalNonMembers()>0 && !SmartUtil.isBlankObject(memberInforms.getNonMembers())){
				for(int i=0; i<memberInforms.getNonMembers().length; i++){
					if (i == MemberInformList.MAX_MEMBER_LIST)
						break;
					SeraUserInfo member = memberInforms.getNonMembers()[i];
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
				}
			}
			%>
		</div>
	</div>
	<!-- Panel2 //-->

	<%
	if (memberInforms.getTotalNonMembers() > 0 && !SmartUtil.isBlankObject(memberInforms.getNonMembers())) {
		SeraUserInfo[] members = memberInforms.getNonMembers();
		if(memberInforms.getTotalNonMembers()>members.length){
			String lastId = members[members.length-2].getId(); 
	%>
			<!-- 더보기 -->
			<div class="more js_more_member_informs_btn js_more_non_member_btn" requestType="<%=MemberInformList.TYPE_NON_MEMBERS %>" teamId="<%=teamId%>" lastId="<%=lastId%>">
				<div class="icon_more">더보기<span class="ml3 js_progress_span"></span></div>
				
			</div>
			<!-- 더보기 //-->
	<%
		}
	}	
	%>
</div>
<!-- Panel Section //-->
