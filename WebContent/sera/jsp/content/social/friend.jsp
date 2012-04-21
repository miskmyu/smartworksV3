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

	SeraUserInfo[] friendRequests = smartWorks.getFriendRequestsForMe(null, -1);
	FriendList friendList = smartWorks.getFriendsById(cUser.getId(), FriendList.MAX_FRIEND_LIST);
	int requestCount = (SmartUtil.isBlankObject(friendRequests)) ? 0 : friendRequests.length;
	
%>
<!-- Header Title -->
<div class="header_tit">
	<div class="tit_friend">일상부터, 유용한 정보까지 함께 만들어가는자신의 친구들을 관리할 수
		있습니다.</div>
</div>
<!-- Header Title //-->

<!-- Panel Section -->
<div class="content_section js_friend_page">
	<!-- Panel1 -->
	<div>
		<div class="header mt10">
			<div>새 친구 요청 <span class="t_orange tb js_requester_count" count="<%=requestCount%>">(<%=requestCount %>)</span></div>
		</div>
		<div class="panel_area">
			<%
			if(!SmartUtil.isBlankObject(friendRequests)){
				for(int i=0; i<friendRequests.length; i++){
					SeraUserInfo requester = friendRequests[i];
			%>
					<!-- 목록1-->
					<div class="panel_rds_block mb10 js_friend_request_item" userId="<%=requester.getId()%>">
						<ul>							
							<li class="pl0pr10">
								<a href="othersPAGE.sw?userId=<%=requester.getId()%>">
									<img class="profile_size_m" src="<%=requester.getMidPicture() %>" />
								</a>
							</li>
							<li class="w90">
								<a href="othersPAGE.sw?userId=<%=requester.getId()%>">
									<span><br /><span class="cb t_id"><%=requester.getNickName() %></span></span>
								</a>
							</li>
							<li class="bo_l w310">
								<span><%=CommonUtil.toNotNull(requester.getGoal()) %><br/>
									<span class="t_id"><%=requester.getId() %></span>
								</span>
							</li>
							<li class="fr bo_l">
								<span>
									<div class="btn_mid_l js_accept_friend_btn">
										<div class="btn_mid_r pr5pl5"><span class="icon_blu_down mr3"></span>확 인</div>
									</div>
									<div class="btn_mid_l ml5 js_deny_friend_btn">
										<div class="btn_mid_r"><span class="icon_after_check mr3"></span>나중에 확인</div>
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

	<!-- Panel2 -->
	<div>
		<div class="header mt20">
			<div class="fl"><span class="t_myid"><%=cUser.getNickName() %>님</span>의 친구 (<span class="tb js_friend_count"><%=friendList.getTotalFriends() %></span>) </div>
			<div class="fr">
				<input class="fl fieldline" style="width: 150px" type="text" />
				<button type="button" class="fl ml5">검색</button>
			</div>
		</div>

		<div class="panel_area js_friend_list">
			<%
			if(friendList.getTotalFriends()>0){
				for(int i=0; i<friendList.getFriends().length; i++){
					SeraUserInfo friend = friendList.getFriends()[i];
			%>
					<!-- 목록1-->
					<div class="panel_rds_block mb10 js_friend_item" userId="<%=friend.getId()%>">
						<ul>
							<li class="pl0pr10">
								<a href="othersPAGE.sw?userId=<%=friend.getId()%>">
									<img src="<%=friend.getMinPicture() %>" />
								</a>
							</li>
							<li class="w90">
								<a href="othersPAGE.sw?userId=<%=friend.getId()%>">
									<span><%=friend.getNickName() %><br /> <span class="cb t_id"><%=friend.getName() %></span></span>
								</a>
							</li>
							<li class="bo_l w370"><span><%=CommonUtil.toNotNull(friend.getGoal()) %><br /> <span class="t_id"><%=friend.getId() %></span>
							</span>
							</li>
							<li class="fr bo_l">
								<span> <!-- Btn -->
									<div class="btn_green_l js_destroy_friendship_btn" userId="<%=friend.getId()%>">
										<div class="btn_green_r"><span class="icon_green_down mr5"></span>친구 끊기</div>
									</div> <!-- Btn //--> 
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
	if (friendList.getFriends() != null) {
		SeraUserInfo[] friends = friendList.getFriends();
		if(friendList.getTotalFriends()>friends.length){
			String lastId = friends[friends.length-1].getId(); 
	%>
			<!-- 더보기 -->
			<div class="more js_more_friend_btn" userId="<%=cUser.getId()%>" lastId="<%=lastId%>">
				<div class="icon_more">더보기</div>
				<span class="js_progress_span"></span>
			</div>
			<!-- 더보기 //-->
	<%
		}
	}	
	%>
</div>
<!-- Panel Section //-->
