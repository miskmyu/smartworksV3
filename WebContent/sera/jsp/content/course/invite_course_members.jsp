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
		<h2>멘티초대</h2>
		<div>
			본 코스에 참여할 멘티들을 초대할 수 있습니다.
		</div>
	</div>
</div>
<!-- Header Title //-->
<!-- Course Section -->
<div class="content_section js_invite_course_members_page" courseId="<%=courseId%>">

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
	
	<div class="js_course_non_mentees_list" courseId="<%=courseId%>">
		<%
		SeraUserInfo[] nonMentees = menteeInformations.getNonMentees();
		if(!SmartUtil.isBlankObject(nonMentees)){
			for(int i=0; i<nonMentees.length; i++){
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
								<span><%=nonMentee.getNickName() %><br /> <span class="cb t_id"><%=nonMentee.getName() %></span></span>
							</a>
						</li>
						<li class="bo_l w370"><span><%=CommonUtil.toNotNull(nonMentee.getGoal()) %><br /> <span class="t_id"><%=nonMentee.getId() %></span>
						</span>
						</li>
						<li class="fr bo_l">
							<span> <!-- Btn -->
								<div class="btn_green_l js_invite_mentee_btn" userId="<%=nonMentee.getId() %>">
									<div class="btn_green_r"><span class="icon_green_down"></span>멘티 초대</div>
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
				<div class="more js_more_mentee_informs_btn" requestType="<%=MenteeInformList.TYPE_NON_MENTEES %>" courseId="<%=courseId %>" lastId="<%=nonMentees[nonMentees.length-1].getId()%>">
					<div class="icon_more">더보기</div>
					<span class="js_progress_span"></span>
				</div>
				<!-- 더보기 //-->
		<%
			}		
		}
		%>
	</div>

</div>
<!-- Comment Pannel-->
