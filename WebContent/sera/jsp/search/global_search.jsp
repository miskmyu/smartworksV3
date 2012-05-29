<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.model.sera.GlobalSearchList"%>
<%@page import="net.smartworks.model.sera.info.SeraUserInfo"%>
<%@page import="net.smartworks.model.community.Community"%>
<%@page import="net.smartworks.model.sera.Course"%>
<%@page import="net.smartworks.util.LocalDate"%>
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

	String key = request.getParameter("key");
	if (SmartUtil.isBlankObject(key))
		return;

	GlobalSearchList searchList = smartWorks.searchGlobal(key, GlobalSearchList.MAX_COURSE_LIST, GlobalSearchList.MAX_SERA_USER_LIST);

%>
	<!-- Content -->
	<div id="sera_content" class="js_global_search_page">
		<!-- 코스 검색 결과 -->

		<!-- Header Title -->
		<div class="header_tit">
			<div class="tit_dep2 course m0">
				<h2>
					코스 중 <span class="t_blue">"<%=key %>"</span> 검색결과 <span class="t_red">(<%=searchList.getTotalCourses() %>)</span>건
				</h2>
			</div>
		</div>
		<!-- Header Title //-->

		<div class="course_listbox1">
			<ul class="course_list js_search_course_list">
				<%
				CourseInfo[] courses = searchList.getCourses();
				if(!SmartUtil.isBlankObject(courses)){
					for(int i=0; i<courses.length; i++){
						CourseInfo course = courses[i];
						if(i==GlobalSearchList.MAX_COURSE_LIST){
				%>
							<div class="js_more_search_course more cb" key="<%=key %>" lastId="<%=courses[i-1].getId()%>">
								<div class="icon_more">더보기<span class="ml3 js_progress_span"></span></div>
							</div>
						<%
							break;
						}
						%>
						<li>
							<dl>
								<dd class="mb10">
									<a href=""> <img width="120" height="100" src="<%=course.getOrgPicture()%>"> </a>
								</dd>
								<dd class="mb3"><%=LocalDate.getDiffDate(course.getOpenDate(), course.getCloseDate())%> Days</dd>
								<dd class="mb3 title"><a href="courseHome.sw?courseId=<%=course.getId() %>"> <%=course.getName() %></a></dd>
							</dl>
						</li>
				<%
					}
				}
				%>
			</ul>
		</div>
		<!-- 코스 검색 결과-->

		<!-- 친구 검색 결과 -->
		<!-- Header Title -->
		<div class="header_tit cb">
			<div class="tit_dep2 friend m0">
				<h2>
					친구 중 <span class="t_blue">"<%=key %>"</span> 검색결과 <span class="t_red">(<%=searchList.getTotalSeraUsers() %>)</span>건
				</h2>
			</div>
		</div>
		<!-- Header Title //-->

		<%
		SeraUserInfo[] seraUsers = searchList.getSeraUsers();
		if(!SmartUtil.isBlankObject(seraUsers)){
			for(int i=0; i<seraUsers.length; i++){
				if(i == GlobalSearchList.MAX_SERA_USER_LIST)
					break;
				SeraUserInfo seraUser = seraUsers[i];
				String userHref = (cUser.getId().equals(seraUser.getId())) ? "myPAGE.sw" : "othersPAGE.sw?userId=" + seraUser.getId();
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
						<%
								if(!cUser.isAnonymusUser()){
								%>
						<li class="fr bo_l">
							<span> <!-- Btn -->
									<div class="btn_fgreen_l js_friend_request_btn" userId="<%=seraUser.getId() %>" <%if(seraUser.isFriend()){%>style="display:none"<%} %>>
										<div class="btn_fgreen_r"><span class="icon_green_down"></span>친구 요청</div>
									</div> <!-- Btn //--> 
									<div class="btn_fgreen_l js_destroy_friendship_btn" userId="<%=seraUser.getId()%>" <%if(!seraUser.isFriend()){%>style="display:none"<%} %>>
										<div class="btn_fgreen_r"><span class="icon_green_down"></span>친구 끊기</div>
									</div> <!-- Btn //--> 
							</span>
						</li>
						<%
								}
								%>
					</ul>
				</div>
				<!-- 목록1//-->
		<%
			}
		}
		%>

		<%
		if (searchList.getTotalSeraUsers() > 0 && !SmartUtil.isBlankObject(searchList.getSeraUsers())) {
			SeraUserInfo[] users = searchList.getSeraUsers();
			if(searchList.getTotalSeraUsers()>users.length){
				String lastId = users[users.length-2].getId(); 
		%>
				<!-- 더보기 -->
				<div class="more js_more_search_user_btn" key="<%=key %>" lastId="<%=lastId%>">
					<div class="icon_more">더보기<span class="ml3 js_progress_span"></span></div>
					
				</div>
				<!-- 더보기 //-->
		<%
			}
		}	
		%>
	</div>
	<!-- Content //-->
