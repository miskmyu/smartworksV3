<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.model.sera.info.SeraUserInfo"%>
<%@page import="net.smartworks.model.sera.SeraUser"%>
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
	String otherUserId = request.getParameter("userId");
	SeraUser otherUser = smartWorks.getSeraUserById(otherUserId);
	FriendList friendList = smartWorks.getFriendsById(otherUserId, FriendList.MAX_FRIEND_LIST);

%>
<div class="my_comment_section">
	<!-- Photo Section -->
	<div class="photo_section">
		<div class="my_photo">
			<img src="<%=otherUser.getMidPicture() %>" />
		</div>
		<!-- Btn -->
		<%
		if(!otherUser.isFriend()){
		%>
			<div class="btn_green_l cb js_fried_request_btn" userId="<%=otherUser.getId() %>" style="margin: 8px 0 0 10px">
				<div class="btn_green_r"><span class="icon_green_down mr5"></span>친구요청</div>
			</div>
		<%
		}
		%>
		<!-- Btn //-->
	</div>
	<!-- Photo Section //-->
	<!-- My Comment -->
	<div class="my_comment">
		<div class="header"><%=otherUser.getNickName() %>님</div>
		<jsp:include page="/sera/jsp/content/sera_note.jsp">
			<jsp:param value="<%=ISmartWorks.SPACE_TYPE_USER %>" name="spaceType"/>
			<jsp:param value="<%=otherUser.getId() %>" name="spaceId"/>
		</jsp:include>
	</div>
	<!-- My Comment //-->
</div>

<!-- Panel Section -->
<div class="content_section js_others_friend_page">

	<!-- Panel2 -->
	<div>
		<div class="header mt20">
			<div class="fl"><span class="t_myid"><%=otherUser.getNickName() %>님</span>의 친구 (<span class="tb js_friend_count"><%=friendList.getTotalFriends() %></span>)</div>

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
				<div class="panel_rds_block mb10 js_friend_item">
					<ul>
						<li class="pl0pr10">
							<a href="othersPAGE.sw?userId=<%=friend.getId()%>">
								<img src="<%=friend.getMidPicture() %>" />
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
								<%
								if(!friend.isFriend() && !friend.getId().equals(cUser.getId())){
								%>
									<div class="btn_green_l js_friend_request_btn" userId="<%=friend.getId() %>">
										<div class="btn_green_r"><span class="icon_green_down mr5"></span>친구 요청</div>
									</div> <!-- Btn //--> 
								<%
								}
								%>
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
