<%@page import="net.smartworks.model.sera.SeraUserList"%>
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
	SeraUserList joinRequesterList = new SeraUserList();//smartWorks.getCourseJoinRequesters(courseId, new LocalDate(), 10);
	SeraUserList menteeList = new SeraUserList();//smartWorks.getCourseMentees(courseId, new LocalDate(), 10);

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
<div class="content_section">
	<!-- 목록1-->
	<div class="t_redb mb10">* 가입을 신청한 멘티가 <%=joinRequesterList.getTotalUsers() %>명 있습니다</div>
	<%
	SeraUserInfo[] requesters = joinRequesterList.getUsers();
	if(!SmartUtil.isBlankObject(requesters)){
		for(int i=0; i<requesters.length; i++){
			SeraUserInfo requester = requesters[i];
	%>
			<div class="panel_rds_block mb20">
				<ul>
					<li class="pl0pr10"><img src="<%=requester.getMidPicture() %>" /></li>
					<li class="w470"><span> <%=requester.getNickName() %><br /> <%=requester.getName() %><br /><%=requester.getId()%></span></li>
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
	}
	%>
	<div class="t_redb mb10 fl">* 등록한 멘티가 <%=menteeList.getTotalUsers() %>명 있습니다</div>
	<!-- 검색 -->
	<div class="fr">
		<span class="fl tb mr5">멘티검색</span> <input class="fl fieldline" style="width: 150px" type="text" />
		<button type="button" class="fl ml5">검색</button>
	</div>
	<!-- 검색 //-->
	<%
	SeraUserInfo[] mentees = menteeList.getUsers();
	if(!SmartUtil.isBlankObject(mentees)){
		for(int i=0; i<mentees.length; i++){
			SeraUserInfo mentee = mentees[i];
	%>
			<!-- 목록1-->
			<div class="panel_rds_block mb10 cb">
				<ul>
					<li class="pl0pr10"><img src="<%=mentee.getMidPicture() %>" /></li>
					<li class="w90"><span><%=mentee.getNickName() %><br /> <span class="cb t_id"><%=mentee.getId() %></span> </span></li>
					<li class="bo_l" style="width: 15%"><span> 미션수행 20 </span></li>
					<li class="bo_l" style="width: 15%"><span> 받은 게시물 15<br />댓글쓰기 20<br /> 공감 + 19 </span></li>
					<li class="bo_l" style="width: 15%"><span> 참여 90일째 </span></li>
					<li class="fr bo_l">
						<span>
							<div class="btn_green_l">
								<div class="btn_green_r"><span class="icon_green_down mr5"></span>멘티 강퇴</div>
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
<!-- Comment Pannel-->
