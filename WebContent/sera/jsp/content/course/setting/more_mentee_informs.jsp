<%@page import="net.smartworks.util.SeraTest"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.model.sera.MenteeInformList"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.model.sera.info.SeraUserInfo"%>
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
	String lastId = request.getParameter("lastId");
	
	SeraUserInfo[] users = smartWorks.getCourseMenteeInformsByType(type, courseId, lastId, MenteeInformList.MAX_USER_LIST);
	
	if(!SmartUtil.isBlankObject(users)){
		for(int i=0; i<users.length; i++){
			SeraUserInfo user = users[i];
			String btnClass = type == MenteeInformList.TYPE_MENTEES ? "js_more_mentee_btn" : "js_more_non_mentee_btn";
			if(i==MenteeInformList.MAX_USER_LIST){
	%>
				<!-- 더보기 -->
				<div class="more js_more_mentee_informs_btn <%=btnClass%>" requestType="<%=type %>" courseId="<%=courseId %>" lastId="<%=users[i-1].getId()%>">
					<div class="icon_more">더보기<span class="ml3 js_progress_span"></span></div>
				</div>
				<!-- 더보기 //-->
			<%
				break;
			}
			switch(type){
			case MenteeInformList.TYPE_JOIN_REQUESTERS:
			%>
				<div class="panel_rds_block mb20 js_join_requester_item" courseId="<%=courseId%>" userId="<%=user.getId()%>">
					<ul>
						<li class="pl0pr10">
							<a href="othersPAGE.sw?userId=<%=user.getId()%>">
								<img class="profile_size_m" src="<%=user.getMidPicture() %>" />
							</a>
						</li>
						<li class="w470">
							<a href="othersPAGE.sw?userId=<%=user.getId()%>">
								<span> <%=CommonUtil.toNotNull(user.getNickName()) %><br /> <%=CommonUtil.toNotNull(user.getName()) %><br /><%=user.getId()%></span>
							</a>
						</li>
						<li class="fr bo_l">
							<span>
								<div class="btn_default_l mr5 js_approve_join_btn"><div class="btn_default_r">승 인</div></div>
								<div class="btn_default_l js_reject_join_btn"><div class="btn_default_r">거 절</div></div>
							</span>
						</li>
					</ul>		
				</div>
				<!-- 목록1//-->
			<%
				break;
			case MenteeInformList.TYPE_MENTEES:
			%>
				<!-- 목록1-->
				<div class="panel_rds_block mb10 cb js_mentee_item" courseId="<%=courseId%>" userId="<%=user.getId()%>">
					<ul>
						<li class="pl0pr10">
							<a href="othersPAGE.sw?userId=<%=user.getId()%>">
								<img class="profile_size_m" src="<%=user.getMidPicture() %>" />
							</a>
						</li>
						<li class="w90">
							<a href="othersPAGE.sw?userId=<%=user.getId()%>">
								<span><%=CommonUtil.toNotNull(user.getNickName()) %><br /> <span class="cb t_id"><%=CommonUtil.toNotNull(user.getName()) %></span> </span>
							</a>
						</li>
						<li class="bo_l" style="width: 15%"><span> 미션수행 0 </span></li>
						<li class="bo_l" style="width: 15%"><span> 받은 게시물 0<br />댓글쓰기 0<br /> 공감 + 0 </span></li>
						<li class="bo_l" style="width: 15%"><span> 참여 0일째 </span></li>>
						<li class="fr bo_l">
							<span>
								<div class="btn_fgreen_l js_pushout_mentee_btn">
									<div class="btn_fgreen_r"><span class="icon_green_down"></span>멘티 강퇴</div>
								</div>
							</span>
						</li>
					</ul>
				</div>
				<!-- 목록1//-->
			<%
				break;
			case MenteeInformList.TYPE_NON_MENTEES:
			%>
				<!-- 목록1-->
				<div class="panel_rds_block mb10 cb js_non_mentee_item" courseId="<%=courseId%>" userId="<%=user.getId()%>">
					<ul>
						<li class="pl0pr10">
							<a href="othersPAGE.sw?userId=<%=user.getId()%>">
								<img class="profile_size_m" src="<%=user.getMidPicture() %>" />
							</a>
						</li>
						<li class="w90">
							<a href="othersPAGE.sw?userId=<%=user.getId()%>">
								<span><%=CommonUtil.toNotNull(user.getNickName()) %><br /> <span class="cb t_id"><%=CommonUtil.toNotNull(user.getName()) %></span></span>
							</a>
						</li>
						<li class="bo_l w370"><span><%=CommonUtil.toNotNull(user.getGoal()) %><br /> <span class="t_id"><%=user.getId() %></span>
						</span>
						</li>
						<li class="fr bo_l">
							<span> <!-- Btn -->
								<div class="btn_fgreen_l js_invite_mentee_btn" userId="<%=user.getId() %>">
									<div class="btn_fgreen_r"><span class="icon_green_down"></span>멘티 초대</div>
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
