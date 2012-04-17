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

	FriendList friendList = smartWorks.getFriendsById(cUser.getId(), FriendList.MAX_FRIEND_LIST);
	
%>
<!-- Header Title -->
<div class="header_tit">
	<div class="tit_friend">일상부터, 유용한 정보까지 함께 만들어가는자신의 친구들을 관리할 수
		있습니다.</div>
</div>
<!-- Header Title //-->

<!-- Panel Section -->
<div class="content_section">
	<!-- Panel1 -->
	<div>
		<div class="header mt10">
			<div>
				새 친구 요청 <span class="t_orange tb">(5)</span>
			</div>
		</div>
		<div class="panel_area">

			<!-- 목록1-->
			<div class="panel_rds_block mb10">
				<ul>
					<li class="pl0pr10"><img class="profile_size_m" src="../images/photo_mid48_2.jpg" />
					</li>
					<li class="w90"><span> 아이디는일곱자<br /> <span
							class="cb t_id">identity</span> </span>
					</li>
					<li class="bo_l w310"><span>
							목표내용목표내용목표내용목표내용목표내용목표내용목표내용목표내용<br /> <span class="t_id">hongildong@korea.com</span>
					</span>
					</li>
					<li class="fr bo_l"><span> <!-- Btn -->
							<div class="btn_mid_l">
								<div class="btn_mid_r pr5pl5">
									<span class="icon_blu_down mr3"></span>확 인
								</div>
							</div> <!-- Btn //--> <!-- Btn -->
							<div class="btn_mid_l ml5">
								<div class="btn_mid_r">
									<span class="icon_after_check mr3"></span>나중에 확인
								</div>
							</div> <!-- Btn //--> </span>
					</li>
				</ul>
			</div>
			<!-- 목록1//-->

			<!-- 목록1-->
			<div class="panel_rds_block mb10">
				<ul>
					<li class="pl0pr10"><img src="../images/photo_mid48_2.jpg" />
					</li>
					<li class="w90"><span> 피터팬<br /> <span class="cb t_id">identity</span>
					</span>
					</li>
					<li class="bo_l w310"><span>
							목표내용목표내용목표내용목표내용목표내용목표내용목표내용목표내용<br /> <span class="t_id">hongildong@korea.com</span>
					</span>
					</li>
					<li class="fr bo_l"><span> <!-- Btn -->
							<div class="btn_mid_l">
								<div class="btn_mid_r pr5pl5">
									<span class="icon_blu_down mr3"></span>확 인
								</div>
							</div> <!-- Btn //--> <!-- Btn -->
							<div class="btn_mid_l ml5">
								<div class="btn_mid_r">
									<span class="icon_after_check mr3"></span>나중에 확인
								</div>
							</div> <!-- Btn //--> </span>
					</li>
				</ul>
			</div>
			<!-- 목록1//-->

			<!-- 목록1-->
			<div class="panel_rds_block mb10">
				<ul>
					<li class="pl0pr10"><img src="../images/photo_mid48_2.jpg" />
					</li>
					<li class="w90"><span> 피터팬<br /> <span class="cb t_id">identity</span>
					</span>
					</li>
					<li class="bo_l w310"><span>
							목표내용목표내용목표내용목표내용목표내용목표내용목표내용목표내용<br /> <span class="t_id">hongildong@korea.com</span>
					</span>
					</li>
					<li class="fr bo_l"><span> <!-- Btn -->
							<div class="btn_mid_l">
								<div class="btn_mid_r pr5pl5">
									<span class="icon_blu_down mr3"></span>확 인
								</div>
							</div> <!-- Btn //--> <!-- Btn -->
							<div class="btn_mid_l ml5">
								<div class="btn_mid_r">
									<span class="icon_after_check mr3"></span>나중에 확인
								</div>
							</div> <!-- Btn //--> </span>
					</li>
				</ul>
			</div>
			<!-- 목록1//-->

		</div>
	</div>
	<!-- Panel1 //-->

	<!-- Panel2 -->
	<div>
		<div class="header mt20">
			<div class="fl">
				<span class="t_myid"><%=cUser.getNickName() %>님</span>의 친구
			</div>

			<div class="fr">
				<input class="fl fieldline" style="width: 150px" type="text" />
				<button type="button" class="fl ml5">검색</button>
			</div>
		</div>

		<div class="panel_area">

			<%
			if(friendList.getTotalFriends()>0){
				for(int i=0; i<friendList.getFriends().length; i++){
					UserInfo friend = friendList.getFriends()[i];
			%>
				<!-- 목록1-->
				<div class="panel_rds_block mb10">
					<ul>
						<li class="pl0pr10"><img src="<%=friend.getMinPicture() %>" />
						</li>
						<li class="w90"><span><%=friend.getNickName() %><br /> <span class="cb t_id"><%=friend.getName() %></span>
						</span>
						</li>
						<li class="bo_l w370"><span>
								목표내용목표내용목표내용목표내용목표내용목표내용목표내용목표내용<br /> <span class="t_id"><%=friend.getId() %></span>
						</span>
						</li>
						<li class="fr bo_l"><span> <!-- Btn -->
								<div class="btn_green_l">
									<div class="btn_green_r">
										<span class="icon_green_down mr5"></span>친구 끊기
									</div>
								</div> <!-- Btn //--> </span>
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
		if(friendList.getTotalFriends()>friendList.getFriends().length){
	%>
		<!-- 더보기 -->
		<div class="more">
			<div class="icon_more">더보기</div>
		</div>
		<!-- 더보기 //-->
	<%
		}
	}	
	%>
</div>
<!-- Panel Section //-->
