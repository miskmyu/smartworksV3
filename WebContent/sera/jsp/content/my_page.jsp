<%@page import="net.smartworks.model.instance.info.AsyncMessageInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.YTVideoInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.EventInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.BoardInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.CommentInstanceInfo"%>
<%@page import="net.smartworks.model.instance.WorkInstance"%>
<%@page import="net.smartworks.model.instance.info.WorkInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.MemoInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.ImageInstanceInfo"%>
<%@page import="net.smartworks.model.instance.Instance"%>
<%@page import="net.smartworks.model.instance.info.InstanceInfo"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@ page contentType="text/html; charset=utf-8"%>

<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 그리고 현재사용자 정보도 가져온다.	
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");

	User cUser = SmartUtil.getCurrentUser();
%>

<div class="my_comment_section">
	<div class="my_photo"><img src="<%=cUser.getMidPicture() %>" width="120px" height="120px"/></div>
	<!-- My Comment -->
	<div class="my_comment">
		<div class="header"><%=cUser.getNickName() %>님</div>
		<div class="comment_txt ">
			<textarea name="" cols="" rows="5" placeholder="전공 공부만큼 영어를 좋아했는데 입사하고 나니 영어에는 손을 뚝 끊어버리게 되네요~ "></textarea>
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
					<li class="icon_memo ml10"><a href="" title="메모"> </a></li>
					<li class="icon_video"><a href="" title="동영상"> </a></li>
					<li class="icon_photo"><a href="" title="사진"> </a></li>
					<li class="icon_link"><a href=""  title="링크"> </a></li>
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
<!-- Comment Pannel-->
<div class="panel_section js_my_instance_list_page">
	<div class="header">
		<div class="icon_mytext"><a href="" class="js_view_user_instances current" userId="<%=cUser.getId()%>">내글보기</a></div><div> | <a href="" class="js_view_all_instances">전체보기</a></div>
	</div>

	<div class="js_user_instance_list">
		<jsp:include page="/sera/jsp/content/sera_instances.jsp">
			<jsp:param value="<%=cUser.getId() %>" name="userId"/>
		</jsp:include>
	</div>
</div>
<!-- Comment Pannel-->