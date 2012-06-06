<%@page import="net.smartworks.model.sera.GlobalSearchList"%>
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

	String key = request.getParameter("key");
	String lastId = request.getParameter("lastId");

	SeraUserInfo[] seraUsers = smartWorks.searchSeraUsers(key, lastId, GlobalSearchList.MAX_SERA_USER_LIST);

	if(!SmartUtil.isBlankObject(seraUsers)){
		for(int i=0; i<seraUsers.length; i++){
			SeraUserInfo seraUser = seraUsers[i];
			String userHref = (cUser.getId().equals(seraUser.getId())) ? "myPAGE.sw" : "othersPAGE.sw?userId=" + seraUser.getId();
			if(i==GlobalSearchList.MAX_SERA_USER_LIST){
	%>
				<!-- 더보기 -->
				<div class="more js_more_search_user_btn" key="<%=key %>" lastId="<%=seraUsers[i-1].getId()%>">
					<div class="icon_more">더보기<span class="ml3 js_progress_span"></span></div>
				</div>
				<!-- 더보기 //-->
			<%
				break;
			}
			%>
			<!-- 목록1-->
			<div class="panel_rds_block mb10 js_sera_user_item" userId="<%=seraUser.getId()%>">
				<ul>
					<li class="pl0pr10">
						<a href="<%=userHref%>">
							<img class="profile_size_m" src="<%=seraUser.getMinPicture() %>" />
						</a>
					</li>
					<li class="w90">
						<a href="othersPAGE.sw?userId=<%=seraUser.getId()%>">
							<span><%=CommonUtil.toNotNull(seraUser.getNickName()) %><br /> <span class="cb t_id"><%=CommonUtil.toNotNull(seraUser.getName())%></span></span>
						</a>
					</li>
					<li class="bo_l w370"><span><%=CommonUtil.toNotNull(seraUser.getGoal()) %><br /> <span class="t_id"><%=seraUser.getId() %></span>
					</span>
					</li>
					<li class="fr bo_l">
						<span> <!-- Btn -->
							<%
							if(!cUser.isAnonymusUser()){
							%>
								<div class="btn_fgreen_l js_friend_request_btn" userId="<%=seraUser.getId() %>" <%if(seraUser.isFriend()){%>style="display:none"<%} %>>
									<div class="btn_fgreen_r"><span class="icon_green_down"></span>친구 요청</div>
								</div> <!-- Btn //--> 
								<div class="btn_fgreen_l js_destroy_friendship_btn" userId="<%=seraUser.getId()%>" <%if(!seraUser.isFriend()){%>style="display:none"<%} %>>
									<div class="btn_fgreen_r"><span class="icon_green_down"></span>친구 끊기</div>
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