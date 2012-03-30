<%@page import="net.smartworks.model.sera.info.CourseInfo"%>
<%@page import="net.smartworks.model.sera.CourseList"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@ page contentType="text/html; charset=utf-8"%>

<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 그리고 현재사용자 정보도 가져온다.	
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();

	String otherUserId = request.getParameter("userId");
	User otherUser = smartWorks.getUserById(otherUserId);
	CourseList courseList = smartWorks.getCoursesById(otherUserId, CourseList.MAX_COURSE_LIST);
%>
<div class="my_comment_section">
	<!-- Photo Section -->
	<div class="photo_section">
		<div class="my_photo">
			<img src="<%=otherUser.getMidPicture() %>" />친구사진
		</div>
		<!-- Btn -->
		<div class="btn_green_l cb" style="margin: 8px 0 0 10px">
			<div class="btn_green_r">
				<span class="icon_green_down mr5"></span>친구요청
			</div>
		</div>
		<!-- Btn //-->
	</div>
	<!-- Photo Section //-->
	<!-- My Comment -->
	<div class="my_comment">
		<div class="header"><%=otherUser.getNickName() %></div>
		<div class="comment_txt ">
			<textarea name="" cols="" rows="5" placeholder="전공 공부만큼 영어를 좋아했는데 입사하고 나니 영어에는 손을 뚝 끊어버리게 되네요~"></textarea>
		</div>
		<div class="cb mt6">
			<!-- 좌측 영역 -->
			<div class="option">
				<!-- 버튼 -->
				<div class="btn_wstyle_l">
					<div class="btn_wstyle_r">텍스트</div>
				</div>
				<!-- 버튼 //-->
				<!-- 전체공개 -->
				<div class="txt ml10">
					<a href=""> 전체공개<span class="icon_bul_select ml5"></span> </a> |
				</div>
				<!-- 전체공개 //-->
				<!-- 태그넣기 -->
				<div class="txt">
					<a href=""> 태그넣기<span class="icon_bul_select ml5"></span> </a>
				</div>
				<!-- 태그넣기//-->
			</div>
			<!-- 좌측 영역//-->
			<!-- 우측 버튼 영역 -->
			<div class="attach_file">
				<ul>
					<li class="t_s11"><span class="t_red">0</span> /1000kbyte</li>
					<li class="icon_memo ml10"><a href=""> </a></li>
					<li class="icon_video"><a href=""> </a></li>
					<li class="icon_photo"><a href=""> </a></li>
					<li class="icon_link"><a href=""> </a></li>
					<!-- Btn 등록-->
					<li class="btn_default_l ml10">
						<div class="btn_default_r">등록</div>
					</li>
					<!-- Btn 등록//-->
				</ul>
			</div>
			<!-- 우측 버튼 영역 //-->
		</div>
	</div>
	<!-- My Comment //-->
</div>

<!-- Panel Section -->
<div class="content_section">

	<!-- Content Section -->
	<div class="content_section">
		<div class="header mt10">
			<div><%=otherUser.getNickName() %>님의 운영코스</div>
		</div>
		<!-- 리스트1 -->
		<div class="category_list">
			<%
			if(courseList.getRunnings()>0){
				for(int i=0; i<courseList.getRunningCourses().length; i++){
					CourseInfo course = courseList.getRunningCourses()[i];
					String endClass = ((i+1) % 3 == 0) ? "end" : "";
			%>
					<ul href="courseHome.sw?courseId=<%=course.getId() %>" class="category_box <%=endClass%> js_sera_content">
						<li class="photo">
							<a href=""> <img width="218" height="148" src="<%=course.getMidPicture()%>"></a>
						</li>
						<li class="subject"><a href=""><%=course.getName() %></a></li>
						<li class="maker"><a href=""><%=course.getOwner().getName() %></a></li>
						<li class="info">
							<dl>
								<dd class="menteeNo"><%=course.getNumberOfGroupMember() %>명</dd>
								<dd class="makeDate"><%=course.getOpenDate().toLocalString() %></dd>
								<dd class="category"></dd>
							</dl></li>
						<li class="detail"><a href="/Course/Detail/212"><%=course.getBriefDesc() %></a></li>
						<!-- Gauge -->
						<li class="gauge_box w_auto mt5">
							<dl>
								<dd class="gauge_outline w170">
									<label class="gauge" style="width: <%=course.getAchievedPoint()*100/course.getTargetPoint()%>%;"></label>
								</dd>
								<dd class="process">(<%=course.getAchievedPoint() %>/<%=course.getTargetPoint() %>)</dd>
							</dl></li>
						<!-- Gauge //-->
					</ul>
			<%
				}
			}
			if(courseList.getRunnings()>CourseList.MAX_COURSE_LIST){
			%>
				<!-- 더보기 -->
				<div class="more cb">
					<div class="icon_more">더보기</div>
				</div>
				<!-- 더보기 //-->
			<%
			}
			%>
		</div>
		<!-- 리스트1 //-->

	</div>
	<!-- Content Section //-->

	<!-- Content Section2 -->
	<div class="content_section">
		<div class="header mt10">
			<div><%=otherUser.getNickName() %>님의 참여코스</div>
		</div>
		<!-- 리스트1 -->
		<div class="category_list">
		<%
		if(courseList.getAttendings()>0){
			for(int i=0; i<courseList.getAttendingCourses().length; i++){
				CourseInfo course = courseList.getAttendingCourses()[i];
				String endClass = ((i+1) % 3 == 0) ? "end" : "";
		%>
				<ul href="courseHome.sw?courseId=<%=course.getId() %>" class="category_box <%=endClass%> js_sera_content">
					<li class="photo">
						<a href=""> <img width="218" height="148" src="<%=course.getMidPicture()%>"></a>
					</li>
					<li class="subject"><a href=""><%=course.getName() %></a></li>
					<li class="maker"><a href=""><%=course.getOwner().getName() %></a></li>
					<li class="info">
						<dl>
							<dd class="menteeNo"><%=course.getNumberOfGroupMember() %>명</dd>
							<dd class="makeDate"><%=course.getOpenDate().toLocalString() %></dd>
							<dd class="category"></dd>
						</dl></li>
					<li class="detail"><a href="/Course/Detail/212"><%=course.getBriefDesc() %></a></li>
					<!-- Gauge -->
					<li class="gauge_box w_auto mt5">
						<dl>
							<dd class="gauge_outline w170">
								<label class="gauge" style="width: <%=course.getAchievedPoint()*100/course.getTargetPoint()%>%;"></label>
							</dd>
							<dd class="process">(<%=course.getAchievedPoint() %>/<%=course.getTargetPoint() %>)</dd>
						</dl></li>
					<!-- Gauge //-->
				</ul>
		<%
			}
		}
		if(courseList.getAttendings()>CourseList.MAX_COURSE_LIST){
		%>
			<!-- 더보기 -->
			<div class="more cb">
				<div class="icon_more">더보기</div>
			</div>
			<!-- 더보기 //-->
		<%
		}
		%>
		</div>
		<!-- 리스트1 //-->
	</div>
	<!-- Content Section2 //-->

</div>
<!-- Panel Section //-->
