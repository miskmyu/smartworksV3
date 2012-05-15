<%@page import="net.smartworks.model.sera.Course"%>
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

	String courseId = request.getParameter("courseId");
	
	MenteeInformList menteeInformations = smartWorks.getCoursesMenteeInformations(courseId, MenteeInformList.MAX_USER_LIST);
%>
<!-- Header Title -->
<div class="header_tit">
	<div class="tit_dep2 m0">
		<h2>멘티관리</h2>
		<div>
			본 코스에 참여하고 있는 모든 멘티들에 대한 정보를 한눈에 보고 관리할 수 있습니다.<br /> 또한, 멘토는 코스 참여에
			부적격한 멘티의 경우에는 강제 퇴장시킬 수 있습니다.
		</div>
	</div>
</div>
<!-- Header Title //-->
<!-- Course Section -->
<div class="content_section js_course_setting_mentee_page" courseId="<%=courseId%>">
	<!-- 목록1-->
	<div class="t_redb mb10">* 가입을 신청한 멘티가 <%=menteeInformations.getTotalJoinRequesters()%>명 있습니다</div>
	<div class="js_join_requesters_list">
		<%
		SeraUserInfo[] requesters = menteeInformations.getJoinRequesters();
		if(!SmartUtil.isBlankObject(requesters)){
			for(int i=0; i<requesters.length; i++){
				SeraUserInfo requester = requesters[i];
		%>
				<div class="panel_rds_block mb20 js_join_requester_item" courseId="<%=courseId%>" userId="<%=requester.getId()%>">
					<ul>
						<li class="pl0pr10">
							<a href="othersPAGE.sw?userId=<%=requester.getId()%>">						
								<img class="profile_size_m" src="<%=requester.getMidPicture() %>" />
							</a>
						</li>
						<li class="">
							<a href="othersPAGE.sw?userId=<%=requester.getId()%>">												
								<span> <%=CommonUtil.toNotNull(requester.getNickName()) %><br /> <%=CommonUtil.toNotNull(requester.getName()) %><br /><span class="t_id"><%=requester.getId()%></span></span>
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
			}
			if(menteeInformations.getTotalJoinRequesters()>requesters.length){
			%>
				<!-- 더보기 -->
				<div class="more js_more_mentee_informs_btn" requestType="<%=MenteeInformList.TYPE_JOIN_REQUESTERS %>" courseId="<%=courseId %>" lastId="<%=requesters[requesters.length-1].getId()%>">
					<div class="icon_more">더보기<span class="ml3 js_progress_span"></span></div>
				</div>
				<!-- 더보기 //-->
		<%
			}		
		}
		%>
	</div>
	
	<div class="title_srch">
	<span class="t_redb">* 등록한 멘티가 <span class="js_mentee_count"><%=menteeInformations.getTotalMentees() %></span>명 있습니다</span>
	<!-- 검색 -->
	<div class="fr">
		<span class="fl tb mr5">멘티검색</span> 
		<input class="fl fieldline js_mentee_search_key" style="width: 150px" type="text" />
		<button type="button" class="fl ml5 js_mentee_search_btn">검색</button>
	</div>
	<!-- 검색 //-->
	</div>

	<div class="js_course_mentees_list">
		<%
		SeraUserInfo[] mentees = menteeInformations.getMentees();
		if(!SmartUtil.isBlankObject(mentees)){
			for(int i=0; i<mentees.length; i++){
				if (i == MenteeInformList.MAX_USER_LIST)
					break;
				SeraUserInfo mentee = mentees[i];
		%>
				<!-- 목록1-->
				<div class="panel_rds_block mb10 cb js_mentee_item" courseId="<%=courseId%>" userId="<%=mentee.getId()%>">
					<ul>
						<li class="pl0pr10">
							<a href="othersPAGE.sw?userId=<%=mentee.getId()%>">
								<img class="profile_size_m" src="<%=mentee.getMidPicture() %>" />
							</a>
						</li>
						<li class="">
							<a href="othersPAGE.sw?userId=<%=mentee.getId()%>">
								<span><%=CommonUtil.toNotNull(mentee.getNickName()) %><br /> <span class="cb t_id"><%=CommonUtil.toNotNull(mentee.getId()) %></span> </span>
							</a>
						</li>
						<li class="fr bo_l">
							<span>
								<div class="btn_fgray_l js_pushout_mentee_btn">
									<div class="btn_fgray_r"><span class="icon_delete_inbtn"></span>멘티 강퇴</div>
								</div>
							</span>
						</li>
						<li class="bo_l fr" style="width: 65px"><span> 참여 0일째 </span></li>
						<li class="bo_l fr" style="width: 80px"><span> 받은 게시물 0<br />댓글쓰기 0<br /> 공감 + 0 </span></li>
						<li class="bo_l fr" style="width: 70px"><span> 미션수행 0 </span></li>
					</ul>
				</div>
				<!-- 목록1//-->
			<%
			}
			if(menteeInformations.getTotalMentees()>mentees.length){
			%>
				<!-- 더보기 -->
				<div class="more js_more_mentee_informs_btn js_more_mentee_btn" requestType="<%=MenteeInformList.TYPE_MENTEES %>" courseId="<%=courseId %>" lastId="<%=mentees[mentees.length-2].getId()%>">
					<div class="icon_more">더보기<span class="ml3 js_progress_span"></span></div>
				</div>
				<!-- 더보기 //-->
		<%
			}		
		}
		%>
	</div>

	<div class="title_srch">
	<div class="t_redb fl">* 등록가능한 멤버가 <span class="js_non_mentee_count"><%=menteeInformations.getTotalNonMentees() %></span>명 있습니다</div>
	<!-- 검색 -->
	<div class="fr">
		<span class="fl tb mr5">멤버검색</span> 
		<input class="fl fieldline js_non_mentee_search_key" style="width: 150px" type="text" />
		<button type="button" class="fl ml5 js_non_mentee_search_btn">검색</button>
	</div>
	<!-- 검색 //-->
	</div>
	
	<div class="js_course_non_mentees_list">
		<%
		SeraUserInfo[] nonMentees = menteeInformations.getNonMentees();
		if(!SmartUtil.isBlankObject(nonMentees)){
			for(int i=0; i<nonMentees.length; i++){
				if (i == MenteeInformList.MAX_USER_LIST)
					break;
				SeraUserInfo nonMentee = nonMentees[i];
		%>
				<!-- 목록1-->
				<div class="panel_rds_block mb10 cb js_non_mentee_item" courseId="<%=courseId%>" userId="<%=nonMentee.getId()%>">
					<ul>
						<li class="pl0pr10">
							<a href="othersPAGE.sw?userId=<%=nonMentee.getId()%>">
								<img class="profile_size_m" src="<%=nonMentee.getMidPicture() %>" />
							</a>
						</li>
						<li class="w90">
							<a href="othersPAGE.sw?userId=<%=nonMentee.getId()%>">
								<span><%=CommonUtil.toNotNull(nonMentee.getNickName()) %><br /> <span class="cb t_id"><%=CommonUtil.toNotNull(nonMentee.getName()) %></span></span>
							</a>
						</li>
						<li class="bo_l w370"><span><%=CommonUtil.toNotNull(nonMentee.getGoal()) %><br /> <span class="t_id"><%=nonMentee.getId() %></span>
						</span>
						</li>
						<li class="fr bo_l">
							<span> <!-- Btn -->
								<div class="btn_fgreen_l js_invite_mentee_btn" userId="<%=nonMentee.getId() %>">
									<div class="btn_fgreen_r"><span class="icon_green_down"></span>멘티 초대</div>
								</div> <!-- Btn //--> 
							</span>
						</li>
					</ul>
				</div>
				<!-- 목록1//-->
			<%
			}
			if(menteeInformations.getTotalNonMentees()>nonMentees.length){
			%>
				<!-- 더보기 -->
				<div class="more js_more_mentee_informs_btn js_more_non_mentee_btn" requestType="<%=MenteeInformList.TYPE_NON_MENTEES %>" courseId="<%=courseId %>" lastId="<%=nonMentees[nonMentees.length-2].getId()%>">
					<div class="icon_more">더보기<span class="ml3 js_progress_span"></span></div>
				</div>
				<!-- 더보기 //-->
		<%
			}		
		}
		%>
	</div>

</div>
<!-- Comment Pannel-->
