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
	String key = request.getParameter("key");
	
	SeraUserInfo[] courseMembers = smartWorks.searchCourseMemberByType(type, courseId, key);
	if(!SmartUtil.isBlankObject(courseMembers)){
		for(int i=0; i<courseMembers.length; i++){
			SeraUserInfo courseMember = courseMembers[i];

			switch(type){
			case MenteeInformList.TYPE_MENTEES:
	%>
				<!-- 목록1-->
				<div class="panel_rds_block mb10 cb js_mentee_item" courseId="<%=courseId%>" userId="<%=courseMember.getId()%>">
					<ul>
						<li class="pl0pr10">
							<a href="othersPAGE.sw?userId=<%=courseMember.getId()%>">
								<img class="profile_size_m" src="<%=courseMember.getMidPicture() %>" />
							</a>
						<li class="">
							<a href="othersPAGE.sw?userId=<%=courseMember.getId()%>">
								<span><%=courseMember.getNickName() %><br /> <span class="cb t_id"><%=courseMember.getName() %></span></span>
							</a>
						<li class="bo_l" style="width: 15%"><span> 미션수행 0 </span></li>
						<li class="bo_l" style="width: 15%"><span> 받은 게시물 0<br />댓글쓰기 0<br /> 공감 + 0 </span></li>
						<li class="bo_l" style="width: 10%"><span> 참여 0일째 </span></li>
						<li class="fr bo_l">
							<span>
								<div class="btn_green_l js_pushout_mentee_btn" userId="<%=courseMember.getId()%>">
									<div class="btn_green_r"><span class="icon_green_down mr5"></span>멘티 강퇴</div>
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
				<div class="panel_rds_block mb10 cb js_non_mentee_item" courseId="<%=courseId%>" userId="<%=courseMember.getId()%>">
					<ul>
						<li class="pl0pr10">
							<a href="othersPAGE.sw?userId=<%=courseMember.getId()%>">
								<img class="profile_size_m" src="<%=courseMember.getMidPicture() %>" />
							</a>
						</li>
						<li class="w90">
							<a href="othersPAGE.sw?userId=<%=courseMember.getId()%>">
								<span><%=courseMember.getNickName() %><br /> <span class="cb t_id"><%=courseMember.getName() %></span></span>
							</a>
						</li>
						<li class="bo_l w370"><span><%=CommonUtil.toNotNull(courseMember.getGoal()) %><br /> <span class="t_id"><%=courseMember.getId() %></span>
						</span>
						</li>
						<li class="fr bo_l">
							<span> <!-- Btn -->
								<div class="btn_green_l js_invite_mentee_btn" userId="<%=courseMember.getId() %>">
									<div class="btn_green_r"><span class="icon_green_down mr5"></span>멘티 초대</div>
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
