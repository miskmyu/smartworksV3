<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.model.sera.info.ReviewInstanceInfo"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.model.sera.Mentor"%>
<%@page import="net.smartworks.model.sera.Course"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@ page contentType="text/html; charset=utf-8"%>

<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 그리고 현재사용자 정보도 가져온다. 
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();

	String courseId = request.getParameter("courseId");
	Course course = (Course)session.getAttribute("course");
	if(SmartUtil.isBlankObject(course) || !course.getId().equals(courseId)) course = smartWorks.getCourseById(courseId);
	UserInfo[] members = course.getMembers();
	boolean isMyCourse = false;
	if(course.getOwner().getId().equals(cUser.getId())){
		isMyCourse = true;
	}else if(!SmartUtil.isBlankObject(members)){
		for(int i=0; i<members.length; i++){
			if(members[i].getId().equals(cUser.getId())){
				isMyCourse = true;
				break;
			}
		}
	}
	
%>
<div class="js_course_general_page" courseId="<%=courseId%>">
	<div>
		<div class="header mt10">
			<div>코스개요</div>
		</div>
		<ul class="panel_area">
			<!-- photo-->
			<li class="">
				<div class="photo_bg">
					<img class="profile_size_72" src="<%=course.getOwner().getMinPicture()%>" />
					<div class="rgt_name"><%=course.getOwner().getNickName()%></div>
				</div>
				<div class="grade">
					<!-- 왕관-->
					<div class="icon_mentor"></div>
					<!-- 별 -->
					<div class="icon_star"></div>
					<!-- 하트 -->
					<div class="icon_heart current">
						<div class="grade_heart">34</div>
					</div>
				</div>
			</li>
			<!-- photo//-->
			<!-- 개요내용 -->
			<li class="fr">
				<div class="panel_block fr">
					<!-- 코스 목적 -->
					<dl class="content bor">
						<dt class="name">코스목적</dt>
						<dd>
							<div class="text"><%=CommonUtil.toNotNull(course.getObject())%></div>
							<!-- Thum Image-->
							<div class="thum_image"><img src="<%=course.getOrgPicture() %>" /></div>
							<!-- Thum Image//-->
						</dd>
					</dl>
					<!-- 코스 설명 -->
					<dl class="content bor">
						<dt class="name">코스설명</dt>
						<dd>
							<div class="text w100"><%=CommonUtil.toNotNull(course.getDesc())%></div>
						</dd>
					</dl>
					<!-- 코스 일정 -->
					<dl class="content bor_no">
						<dt class="name">코스일정</dt>
						<dd class="gauge_box fl mt10">
							<div class="gauge_outline">
								<label class="gauge"
									style="width: <%=course.getAchievedRatio()%>%;"></label>
							</div>
						</dd>
						<%
						if(!isMyCourse){
						%>
							<dd class="fr">
								<div class="btn_large_l js_join_course_request" autoApproval="<%=course.isAutoApproval()%>">
									<div class="btn_large_r"><span class="icon_blu_down2 mr5"></span>코스 가입하기</div>
								</div>
							</dd>
						<%
						}
						%>
						<div class="mission_info"><%=course.getTargetPoint()%>일 미션기간 중 <%=course.getAchievedPoint()%>일째가 진행중입니다</div>
						<div class="process">(<%=course.getAchievedPoint()%>/<%=course.getTargetPoint()%>)
						</div>
					</dl>
				</div>
			</li>
			<!-- 개요내용 //-->
		</ul>
	</div>
	<!-- 코스 개요 //-->
	<!-- 멘토 소개 -->
	<div>
		<div class="header mt10">
			<div>멘토 소개</div>
		</div>
		<ul class="panel_area">
			<!-- 개요내용 -->
			<li class="fr">
				<div class="panel_block fr">
					<!-- 코스 목적 -->
					<div class="content">
						<%
							if (!SmartUtil.isBlankObject(course.getLeader())) {
								Mentor mentor = smartWorks.getMentorById(course.getLeader().getId());
						%>
						<div class="photo_line">
							<img class="profile_size_b" src="<%=mentor.getMidPicture()%>" width="118" height="118" />
						</div>
						<div class="text m0" style="width:auto">
							<ul class="cb">
								<li class="tb"><%=mentor.getName()%></li>
								<li><span class="bullet_dot">출생 :</span><%=CommonUtil.toNotNull(mentor.getBorn())%>
									(<%=CommonUtil.toNotNull(mentor.getHomeTown())%>)</li>
								<li><span class="bullet_dot">가족 :</span><%=CommonUtil.toNotNull(mentor.getFamily())%></li>
								<li><span class="bullet_dot">주소 :</span><%=CommonUtil.toNotNull(mentor.getLiving())%></li>
								<li><span class="bullet_dot">학력 :</span><%=CommonUtil.toNotNull(mentor.getEducations())%></li>
								<li><span class="bullet_dot">경력 :</span><%=CommonUtil.toNotNull(mentor.getWorks())%></li>
								<li><span class="bullet_dot">강의 :</span><%=CommonUtil.toNotNull(mentor.getLectures())%></li>
								<li><span class="bullet_dot">수상 :</span><%=CommonUtil.toNotNull(mentor.getAwards())%></li>
								<li><span class="bullet_dot">기타 :</span><%=CommonUtil.toNotNull(mentor.getEtc())%></li>
							</ul>
						</div>
						<%
							}
						%>
					</div>
				</div>
			</li>
			<!-- 개요내용 //-->
		</ul>
	</div>
	<!-- 멘토 소개//-->
	<!-- 코스 리뷰 -->
	<div>
		<div class="header mt10">
			<div>코스리뷰</div>
		</div>
		<ul class="panel_area">
			<li class="fl">
				<div class="btn_large_l">
					<div class="btn_large_r js_view_new_course_review" >
						<span class="icon_blu_down2 mr5"></span>리뷰 남기기
					</div>
				</div>
			</li>
			<!-- 리뷰 -->
			<li class="fr">
				<div class="panel_block fr js_course_review_list">
					<!-- Reply-->
					<div class="reply_section js_return_on_course_review" style="display:none">
						<div class="photo">
							<img class="profile_size_m" src="<%=cUser.getMinPicture() %>" />
						</div>
						<div class="reply_text w375 fl">
 							<span class="name" style="display:none"><%=cUser.getNickName() %> : </span>
							<span class="js_review_content" style="display:none"></span>
							<div class="icon_date" style="display:none"><%=(new LocalDate()).toLocalString() %></div>
							<textarea style="width:95%" class="up_textarea" name="txtaReviewContent" placeholder="리뷰 남겨주세요!"></textarea>
						</div>
						<div class="fr">
							<div class="name fl mr5">별점</div>
							<div class="star_score fr">
								<ul class="js_star_point_list js_star_point_btn">
									<li class="icon_star_score"><a href=""></a></li>
									<li class="icon_star_score"><a href=""></a></li>
									<li class="icon_star_score"><a href=""></a></li>
									<li class="icon_star_score"><a href=""></a></li>
									<li class="icon_star_score"><a href=""></a></li>
								</ul>
							</div>
						</div>
					</div>
					<!-- Reply//-->
					<jsp:include page="/sera/jsp/content/course/detail/more_course_reviews.jsp">
						<jsp:param value="<%=courseId %>" name="courseId"/>
					</jsp:include>
				</div>			
			</li>
			<!-- 리뷰//-->
		</ul>
	</div>
	<!-- 코스 리뷰 //-->
</div>