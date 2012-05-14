<%@page import="net.smartworks.model.sera.MemberInformList"%>
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
	String teamId = request.getParameter("teamId");
	String lastId = request.getParameter("lastId");

	SeraUserInfo[] members = smartWorks.getTeamMemberInformsByType(type, teamId, lastId, MemberInformList.MAX_MEMBER_LIST);

	if(!SmartUtil.isBlankObject(members)){
		for(int i=0; i<members.length; i++){
			SeraUserInfo member = members[i];
			String userHref = (cUser.getId().equals(member.getId())) ? "myPAGE.sw" : "othersPAGE.sw?userId=" + member.getId();
			String btnClass = type == MemberInformList.TYPE_MEMBERS ? "js_more_member_btn" : "js_more_non_member_btn";
			if(i==MemberInformList.MAX_MEMBER_LIST){
	%>
				<!-- 더보기 -->
				<div class="more js_more_member_informs_btn <%=btnClass%>" requestType="<%=type %>" teamId="<%=teamId%>" lastId="<%=members[i-1].getId()%>">
					<div class="icon_more">더보기<span class="ml3 js_progress_span"></span></div>
				</div>
				<!-- 더보기 //-->
			<%
				break;
			}
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
						<li class="">
							<a href="othersPAGE.sw?userId=<%=member.getId()%>">
								<span><%=CommonUtil.toNotNull(member.getNickName()) %><br /> <span class="cb t_id"><%=CommonUtil.toNotNull(member.getName()) %></span></span>
							</a>
						</li>
						<li class="bo_l"><span><%=CommonUtil.toNotNull(member.getGoal()) %><br /> <span class="t_id"><%=member.getId() %></span>
						</span>
						</li>
						<li class="fr bo_l">
							<span> <!-- Btn -->
								<div class="btn_green_l js_destroy_membership_btn" userId="<%=member.getId()%>">
									<div class="btn_green_r"><span class="icon_green_down"></span>팀원 삭제</div>
								</div> <!-- Btn //--> 
							</span>
							<span>
								<div class="btn_green_l js_friend_request_btn" userId="<%=member.getId() %>" <%if(member.isFriend()){%>style="display:none"<%} %>>
									<div class="btn_green_r"><span class="icon_green_down"></span>친구 요청</div>
								</div> <!-- Btn //--> 
								<div class="btn_green_l js_destroy_friendship_btn" userId="<%=member.getId()%>" <%if(!member.isFriend()){%>style="display:none"<%} %>>
									<div class="btn_green_r"><span class="icon_green_down"></span>친구 끊기</div>
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
						<li class="">
							<a href="othersPAGE.sw?userId=<%=member.getId()%>">
								<span><%=CommonUtil.toNotNull(member.getNickName()) %><br /> <span class="cb t_id"><%=CommonUtil.toNotNull(member.getName()) %></span></span>
							</a>
						</li>
						<li class="bo_l"><span><%=CommonUtil.toNotNull(member.getGoal()) %><br /> <span class="t_id"><%=member.getId() %></span>
						</span>
						</li>
						<li class="fr bo_l">
							<span> <!-- Btn -->
								<div class="btn_green_l js_member_request_btn" userId="<%=member.getId()%>" teamId="<%=teamId%>">
									<div class="btn_green_r"><span class="icon_green_down"></span>팀원 요청</div>
								</div> <!-- Btn //--> 
							</span>
							<span> <!-- Btn -->
								<div class="btn_green_l js_friend_request_btn" userId="<%=member.getId() %>" <%if(member.isFriend()){%>style="display:none"<%} %>>
									<div class="btn_green_r"><span class="icon_green_down"></span>친구 요청</div>
								</div> <!-- Btn //--> 
								<div class="btn_green_l js_destroy_friendship_btn" userId="<%=member.getId()%>" <%if(!member.isFriend()){%>style="display:none"<%} %>>
									<div class="btn_green_r"><span class="icon_green_down"></span>친구 끊기</div>
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