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
	<!-- Panel1 -->
	<div>
		<div class="header mt10">
			<div class="tit">팀원 요청 <span class="t_orange tb js_requester_count" count="<%=memberInforms.getTotalRequesters()%>">(<%=memberInforms.getTotalRequesters() %>)</span></div>
		</div>
		<div class="panel_area">
			<%
			SeraUserInfo[] memberRequests = memberInforms.getRequesters();
			if(!SmartUtil.isBlankObject(memberRequests)){
				for(int i=0; i<memberRequests.length; i++){
					SeraUserInfo requester = memberRequests[i];
			%>
					<!-- 목록1-->
					<div class="panel_rds_block mb10 js_member_request_item" userId="<%=requester.getId()%>" teamId="<%=teamId%>">
						<ul>							
							<li class="pl0pr10">
								<a href="othersPAGE.sw?userId=<%=requester.getId()%>">
									<img class="profile_size_m" src="<%=requester.getMidPicture() %>" />
								</a>
							</li>
							<li class="">
								<a href="othersPAGE.sw?userId=<%=requester.getId()%>">
									<span><%=CommonUtil.toNotNull(requester.getNickName()) %><br /> <span class="cb t_id"><%=CommonUtil.toNotNull(requester.getName()) %></span></span>
								</a>
							</li>
							<li class="bo_l">
								<span><%=CommonUtil.toNotNull(requester.getGoal()) %><br/>
									<span class="t_id"><%=requester.getId() %></span>
								</span>
							</li>
							<li class="fr bo_l end">
								<span>
								<!-- Btn -->
									<div class="btn_mid_l mr7 js_accept_member_btn">
										<div class="btn_mid_r"><span class="icon_blu_down mr3"></span>승 인</div>
									</div>
								<!-- Btn //-->
								<!-- Btn -->
									<div class="btn_mid_l mr7 js_deny_member_btn">
										<div class="btn_mid_r"><span class="icon_after_check"></span>거 절</div>
									</div>
								<!-- Btn //-->
									<div class="btn_green_l mr7 js_friend_request_btn" userId="<%=requester.getId() %>" <%if(requester.isFriend()){%>style="display:none"<%} %>>
										<div class="btn_green_r"><span class="icon_green_down"></span>친구 요청</div>
									</div>
								<!-- Btn -->
									<div class="btn_green_l js_destroy_friendship_btn" userId="<%=requester.getId()%>" <%if(!requester.isFriend()){%>style="display:none"<%} %>>
										<div class="btn_green_r"><span class="icon_green_down"></span>친구 끊기</div>
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
	<!-- Panel1 //-->

	<!-- Panel2 -->
	<div>
		<div class="header mt20">
			<div class="fl tit"><%=team.getName() %>팀<span>의 팀원들</span> <span class="t_orange tb js_member_count">(<%=memberInforms.getTotalMembers() %>)</span> </div>
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
									<div class="btn_green_l mr7 js_destroy_membership_btn" userId="<%=member.getId()%>">
										<div class="btn_green_r"><span class=icon_team_delete></span>팀원 삭제</div>
									</div>
								<!-- Btn //--> 

								<!-- Btn -->
									<div class="btn_green_l mr7 js_friend_request_btn" userId="<%=member.getId() %>" <%if(member.isFriend()){%>style="display:none"<%} %>>
										<div class="btn_green_r"><span class="icon_green_down"></span>친구 요청</div>
									</div> 
								<!-- Btn //-->

								<!-- Btn //--> 
									<div class="btn_green_l js_destroy_friendship_btn" userId="<%=member.getId()%>" <%if(!member.isFriend()){%>style="display:none"<%} %>>
										<div class="btn_green_r"><span class="icon_green_down"></span>친구 끊기</div>
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
		<div class="header mt20">
			<div class="fl tit">초청이 가능한 코스멤버들 <span class="t_orange tb js_non_member_count">(<%=memberInforms.getTotalNonMembers() %>)</span> </div>
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
									<div class="btn_green_l mr7 js_member_request_btn" userId="<%=member.getId()%>" teamId="<%=teamId%>">
										<div class="btn_green_r"><span class="icon_green_down"></span>팀원 요청</div>
									</div>
								<!-- Btn //--> 
								
								<!-- Btn -->
									<div class="btn_green_l mr7 js_friend_request_btn" userId="<%=member.getId() %>" <%if(member.isFriend()){%>style="display:none"<%} %>>
										<div class="btn_green_r"><span class="icon_green_down"></span>친구 요청</div>
									</div> 
								<!-- Btn //--> 
							
								<!-- Btn -->
									<div class="btn_green_l js_destroy_friendship_btn" userId="<%=member.getId()%>" <%if(!member.isFriend()){%>style="display:none"<%} %>>
										<div class="btn_green_r"><span class="icon_green_down"></span>친구 끊기</div>
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
