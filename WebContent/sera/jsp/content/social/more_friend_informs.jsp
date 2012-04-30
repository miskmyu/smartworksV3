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
	String userId = request.getParameter("userId");
	String lastId = request.getParameter("lastId");
	
	SeraUserInfo[] friends = smartWorks.getFriendInformsByType(type, userId, lastId, FriendInformList.MAX_FRIEND_LIST);
	
	if(!SmartUtil.isBlankObject(friends)){
		for(int i=0; i<friends.length; i++){
			SeraUserInfo friend = friends[i];
			String userHref = (friend.getId().equals(cUser.getId())) ? "myPAGE.sw" : "othersPAGE.sw?userId=" + friend.getId();
			if(i==FriendInformList.MAX_FRIEND_LIST){
	%>
				<!-- 더보기 -->
				<div class="more js_more_friend_informs_btn" requestType="<%=type %>" userId="<%=userId%>" lastId="<%=friends[i-1].getId()%>">
					<div class="icon_more">더보기<span class="ml3 js_progress_span"></span></div>
				</div>
				<!-- 더보기 //-->
			<%
				break;
			}
			switch(type){
			case FriendInformList.TYPE_FRIENDS:
			%>
				<!-- 목록1-->
				<div class="panel_rds_block mb10 js_friend_item" userId="<%=friend.getId()%>">
					<ul>
						<li class="pl0pr10">
							<a href="<%=userHref%>">
								<img class="profile_size_m" src="<%=friend.getMinPicture() %>" />
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
								<div class="btn_green_l js_friend_request_btn" userId="<%=friend.getId() %>" <%if(friend.isFriend()){%>style="display:none"<%} %>>
									<div class="btn_green_r"><span class="icon_green_down"></span>친구 요청</div>
								</div> <!-- Btn //--> 
								<div class="btn_green_l js_destroy_friendship_btn" userId="<%=friend.getId()%>" <%if(!friend.isFriend()){%>style="display:none"<%} %>>
									<div class="btn_green_r"><span class="icon_green_down"></span>친구 끊기</div>
								</div> <!-- Btn //--> 
							</span>
						</li>
					</ul>
				</div>
				<!-- 목록1//-->
			<%
				break;
			case FriendInformList.TYPE_NON_FRIENDS:
			%>
				<!-- 목록1-->
				<div class="panel_rds_block mb10 js_non_friend_item" userId="<%=friend.getId()%>">
					<ul>
						<li class="pl0pr10">
							<a href="<%=userHref%>">
								<img class="profile_size_m" src="<%=friend.getMinPicture() %>" />
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
								<div class="btn_green_l js_friend_request_btn" userId="<%=friend.getId() %>" <%if(friend.isFriend()){%>style="display:none"<%} %>>
									<div class="btn_green_r"><span class="icon_green_down"></span>친구 요청</div>
								</div> <!-- Btn //--> 
								<div class="btn_green_l js_destroy_friendship_btn" userId="<%=friend.getId()%>" <%if(!friend.isFriend()){%>style="display:none"<%} %>>
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