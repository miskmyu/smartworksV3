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
	String fromDateStr = request.getParameter("fromDate");
	Course course = (Course)session.getAttribute("course");
	if(SmartUtil.isBlankObject(course) || !course.getId().equals(courseId)) course = smartWorks.getCourseById(courseId);
	LocalDate fromDate = (SmartUtil.isBlankObject(fromDateStr)) ? new LocalDate() : LocalDate.convertLocalStringToLocalDate(fromDateStr);
	ReviewInstanceInfo[] reviews = smartWorks.getReviewInstancesByCourse(courseId, fromDate, 10);
	
	if(!SmartUtil.isBlankObject(reviews) && reviews.length>0){
		for(int i=0; i<reviews.length; i++){
			ReviewInstanceInfo review = reviews[i];
			if(reviews.length>10){
	%>
				<!-- 더보기 -->
				<div class="more cb js_more_course_reviews_btn" courseId="<%=courseId %>" fromDate="<%=reviews[i-1].getLastModifiedDate().toLocalDateString2()%>">
					<div class="icon_more">더보기<span class="ml3 js_progress_span"></span></div>
				</div>
				<!-- 더보기 //-->
			<%
				break;
			}
			%>
	
				<!-- Reply-->
				<div class="reply_section <%if(i+1==reviews.length){%>end<%}%>">
					<div class="photo">
						<img class="profile_size_m" src="<%=review.getOwner().getMinPicture() %>" />
					</div>
					<div class="reply_text w375 fl">
						<span class="name"><%=review.getOwner().getNickName() %> : </span><%=CommonUtil.toNotNull(review.getContent()) %>
						<div class="icon_date"><%=review.getLastModifiedDate().toLocalString() %></div>
					</div>
					<div class="fr">
						<div class="name fl mr5">별점</div>
						<div class="star_score fr">
							<ul>
								<%
								for(int j=0; j<5; j++){
									String pointClass = "";
									if(review.getStarPoint()>j)
										if(review.getStarPoint()>=(j+1))
											pointClass = "full";
										else
											pointClass = "half";
								%>
									<li class="icon_star_score <%=pointClass%>"><a href=""> </a></li>
								<%
								}
								%>
							</ul>
						</div>
					</div>
				</div>
				<!-- Reply//-->
	<%
		}		
	}
	%>	