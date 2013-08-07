
<!-- Name 			: header.jsp									 -->
<!-- Description	: 화면구성중에 Header 에 해당 되는 부분을 표현하는 화면 	 -->
<!-- Author			: Y.S. JUNG										 -->
<!-- Created Date	: 2011.9.										 -->

<%@page import="net.smartworks.model.work.SmartWork"%>
<%@page import="net.smartworks.model.mail.MailFolder"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ page import="net.smartworks.model.community.*"%>
<%@ page import="net.smartworks.model.notice.*"%>
<%@page import="net.smartworks.model.company.CompanyGeneral"%>

<script>

// 서버에서 SmartUtil.publishMessage()로 NOTICE_COUNT를 현재사용자에게 메시지를 보내면 처리하는 함수이다.
function updateNoticeCount(message){
	var type = message.type;
	var count = message.count;
	var data = "<span></span>";
	if (count > 0)
		data = "<em class='icon_number'>" + count + "<span></span></em>";
		
	if (type == 0) {
		$('#notification_count').html(data);
	} else if (type == 1) {
		$('#message_count').html(data);
	} else if (type == 2) {
		$('#comment_count').html(data);
	} else if (type == 3) {
		$('#assigned_count').html(data);
	} else if (type == 4) {
		$('#mailbox_count').html(data);
		data = (count>0) ? " [<b>" + count + "</b>]" : "";
		$('.js_inbox_unread_count').html(data);
		$('.js_folder_unread_count').html(count);
	} else if (type == 5) {
		$('#savedbox_count').html(data);
	}
};

function logout() {
	smartPop.progressCenter();
	document.location.href = "logout.sw?userId=" + currentUser.userId;
};

</script>
</script>
<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 그리고 현재사용자 정보도 가져온다.	
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();
	String cid = request.getParameter("cid");
	if (SmartUtil.isBlankObject(cid))
		cid = ISmartWorks.CONTEXT_PREFIX_HOME + cUser.getId();
	String wid = request.getParameter("wid");
	if (SmartUtil.isBlankObject(wid))
		wid = cUser.getId();
	
	// 서버에서 현재사용자에 대한 Notice들을 가져온다.
	Notice[] notices = smartWorks.getNoticesForMe();
	//로고반영 구현을 위해 추가
	CompanyGeneral companyGeneral = smartWorks.getCompanyGeneral();
	String companyLogo = (SmartUtil.isBlankObject(companyGeneral) || SmartUtil.isBlankObject(companyGeneral.getCompanyLogo())) ? "images/logo_compay.gif" : companyGeneral.getCompanyLogo();

	String mailInboxId = smartWorks.getFolderIdByType(MailFolder.TYPE_SYSTEM_INBOX);
%>

<!-- 회사 로고 및 연결 링크 -->
<!--  style 확인 필요함 -->
<div class="company_logo">
	<span>
		<a href="home.sw?cid=<%=ISmartWorks.CONTEXT_PREFIX_HOME + cUser.getId()%>">
			<img class="js_auto_picture js_company_logo_src" src="<%=companyLogo%>" />
		</a>
	</span>
	<span class="line"> </span>
</div>
<!-- 회사 로고 및 연결 링크 //-->

<!-- Notice 아이콘들 및 연결 기능  -->
<div class="notice_icons js_notice_icons_area">
	<span>
		<ul>
			<!--  Notification 알림 영역 -->
			<%-- *** js_notice_count : sw_act_nav.sw에서 이벤트를 받아 Message List Box를 보여준다. --%>
			<li class="icon_info js_notice_count">
				<a id="notification_count" href="notice_message_box.sw?noticeType=<%=Notice.TYPE_NOTIFICATION%>" 
					title="<fmt:message key='header.notice.icon.notification'/>">
					<%
		 			if (notices.length > Notice.TYPE_NOTIFICATION && notices[Notice.TYPE_NOTIFICATION].getLength() > 0) {
		 			%> 
		 				<em class="icon_number"><%=notices[Notice.TYPE_NOTIFICATION].getLength()%><span></span></em>
					<%
					}
					%>
				</a>
			</li> 
			<!--  Notification 알림 영역 --%>

 			<%if(companyGeneral.isUseChattingService()){ %>
				<!-- 쪽지 알림 영역 -->
				<!-- *** js_notice_count : sw_act_nav.sw에서 이벤트를 받아 Message List Box를 보여준다. --> 
				<li class="icon_note js_notice_count">
					<a id="message_count" href="notice_message_box.sw?noticeType=<%=Notice.TYPE_MESSAGE%>"
						title="<fmt:message key='header.notice.icon.message'/>"> 
						<%
			 			if (notices.length > Notice.TYPE_MESSAGE && notices[Notice.TYPE_MESSAGE].getLength() > 0) {
			 			%> 
			 				<em class="icon_number"><%=notices[Notice.TYPE_MESSAGE].getLength()%><span></span></em> 
						<%
						}
						%>
					</a>
				</li>
				<!-- 쪽지 알림 영역 //-->
			<%} %>
			
			<!-- 메모 알림 영역 
			<li class="icon_memo">
				<a id="" href=""
						title=""> 
		 				<em class="icon_number"><%=notices[Notice.TYPE_MESSAGE].getLength()%><span></span></em> 
				</a>
			</li>
			<!-- 메모 알림 영역 //-->
	
			<!-- 댓글 알림 영역  -->
			<!-- *** js_notice_count : sw_act_nav.sw에서 이벤트를 받아 Message List Box를 보여준다. --> 
			<li class="icon_reply js_notice_count">
				<a id="comment_count" href="notice_message_box.sw?noticeType=<%=Notice.TYPE_COMMENT%>"
					title="<fmt:message key='header.notice.icon.comments'/>">
					<%
				 	if (notices.length > Notice.TYPE_COMMENT && notices[Notice.TYPE_COMMENT].getLength() > 0) {
				 	%> 
					 	<em class="icon_number"><%=notices[Notice.TYPE_COMMENT].getLength()%><span></span></em> 
					<%
					}
					%>
				</a>
			</li>
			<!-- 댓글 알림 영역  //-->
	
			<!-- 할당업무 알림 영역  -->
			<!-- *** js_notice_count : sw_act_nav.sw에서 이벤트를 받아 Message List Box를 보여준다. --> 
			<li class="icon_assworks js_notice_count">
				<a id="assigned_count" href="notice_message_box.sw?noticeType=<%=Notice.TYPE_ASSIGNED%>"
					title="<fmt:message key='header.notice.icon.assigned'/>">
					<%
				 	if (notices.length > Notice.TYPE_ASSIGNED && notices[Notice.TYPE_ASSIGNED].getLength() > 0) {
				 	%> 
					 	<em class="icon_number"><%=notices[Notice.TYPE_ASSIGNED].getLength()%><span></span></em>
					<%
					}
					%>
				</a>
			</li>
			<!-- 할당업무 알림 영역  //-->
			
			<!-- 업무 알림 영역 
			<li class="icon_info_works">
				<a href="">
					<em class="icon_number"><%=notices[Notice.TYPE_ASSIGNED].getLength()%><span></span></em>
				</a>
			</li>
			<!-- 업무 알림 영역  //-->
			
	
			<!-- 메일 알림 영역  -->
			<!-- *** js_notice_count : sw_act_nav.sw에서 이벤트를 받아 Message List Box를 보여준다. -->
			<%if(cUser.isUseMail()){ %> 
				<li class="icon_mail js_notice_count">
					<%--<a id="mailbox_count" href="notice_message_box.sw?noticeType=<%=Notice.TYPE_MAILBOX%> --%>
					<a id="mailbox_count" class="js_mailbox_notice" href="" mailInboxId="<%=mailInboxId%>"
						title="<fmt:message key='header.notice.icon.mailbox'/>">
						<%
					 	if (notices.length > Notice.TYPE_MAILBOX && notices[Notice.TYPE_MAILBOX].getLength() > 0) {
						%> 
							<em class="icon_number"><%=notices[Notice.TYPE_MAILBOX].getLength()%><span></span></em> 
						<%
						}
						%>
					</a>
				</li>
			<%
			} 
			%>
			<!-- 메일 알림 영역  //-->
	
			<!-- 임시저장 알림 영역  -->
			<!-- *** js_notice_count : sw_act_nav.sw에서 이벤트를 받아 Message List Box를 보여준다. --> 
<%-- --%>			<li class="icon_saved js_notice_count">
				<a id="savedbox_count" class="js_saved_notice" href=""
					title="<fmt:message key='header.notice.icon.savedbox'/>"> 
					<%
				 	if (notices.length > Notice.TYPE_SAVEDBOX && notices[Notice.TYPE_SAVEDBOX].getLength() > 0) {
				 	%> 
				 		<em class="icon_number"><%=notices[Notice.TYPE_SAVEDBOX].getLength()%><span></span></em> 
					<%
					}
					%>
				</a>
			</li>
 			<!-- 임시저장 알림 영역  -->
			<li></li>
		</ul>
	</span>
</div>
<!-- Notice 아이콘들 및 연결 기능  //-->

<!-- Notice icon들을 클릭했을때 보여주는 메시지 리스트 박스 -->
<div class="pop_i_info" id="notice_message_box" style="display: none">
</div>

<!-- 헤더에 있는 메뉴들 및 연결 기능 -->
<div class="top_menu">
	<ul class="fl" style="width:470px">
		<!--  홈메뉴  -->
		<li class="idx1">
				<a href="home.sw?cid=<%=ISmartWorks.CONTEXT_PREFIX_HOME + cUser.getId()%>"><fmt:message key="header.top_menu.home" /></a>
		</li>
		<!--  홈메뉴  //-->
 
		<!--  대시보드 메뉴  -->
		<li class="idx2">
 				<a href="dashboard.sw?cid=<%=ISmartWorks.CONTEXT_PREFIX_DASHBOARD + cUser.getId()%>"><fmt:message key="header.top_menu.dashboard" /></a> 
		</li>
		<!--  대시보드 메뉴  //-->

		<!--  스마트케스트 메뉴  -->
		<li class="idx3">
 				<a href="smartcaster.sw?cid=<%=ISmartWorks.CONTEXT_PREFIX_SMARTCASTER + cUser.getId()%>"><fmt:message key="header.top_menu.smartcaster" /></a> 
		</li>
		<!--  스마트케스트 메뉴 // -->

		<!--  커뮤너티 메뉴  -->
		<li class="idx4">
 				<a href="communities.sw?cid=<%=ISmartWorks.CONTEXT_PREFIX_COMMUNITIES + cUser.getId()%>"><fmt:message key="header.top_menu.communities" /></a> 
		</li>
		<!--  스마트케스트 메뉴 // -->

 	</ul>
	
	<!-- 통합 검색 기능  -->
	<div class="global_srch" style="display:none">
		<div class="srch srch_wsize">
			<input id="" class="nav_input" type="text" title="<fmt:message key='search.global_search'/>" placeholder="<fmt:message key='search.global_search'/>">
			<button title="<fmt:message key='search.search'/>" onclick=""></button>
		</div>
	</div>
	<!-- 통합 검색 기능  //-->

</div>
<!-- 헤더에 있는 메뉴들 및 연결 기능 // -->

<!--  전체 메뉴  -->
<div class="global_menu">

	<!-- 도움말 연결  -->
	<div>
		<a title="<fmt:message key='header.global_menu.help'/>" target="_blank" href="http://manual.smartWorks.net/"><fmt:message key="header.global_menu.help" /> </a>
	</div>
	<!-- 도움말 연결  //-->

	<%
	if(cUser.getUserLevel() == User.USER_LEVEL_AMINISTRATOR || cUser.getUserLevel() == User.USER_LEVEL_SYSMANAGER){
	%>
		<!-- 관리자 권한이 있는 사용자에게 제공되는 시스템설정, 스마트빌더, 앱스토어 버튼들  -->
		<div class="pop_admin">
			<a href="settings_home.sw"><span class="btn_setting" title="<fmt:message key='header.global_menu.settings'/>" ></span></a> 
			<a href="builder_home.sw"><span class="btn_builder" title="<fmt:message key='header.global_menu.smartbuilder'/>"></span></a>
			<a href="http://appstore.smartworks.net" target="_blank"><span class="btn_appstore" title="<fmt:message key='header.global_menu.appstore'/>"></span></a>
		</div>
	<%
	}
	%>
	<!-- 개인정보수정 및 로그아웃 영역  -->
	<div>
		<a href="" onclick="$(this).parent().next('div').toggle(); return false;"><%=cUser.getLongName()%>▼ </a>
	</div>

	<!-- 위의 사용자정보 클릭시 나타나는 개인정보수정 및 로그아웃 버튼들  -->
	<div class="pop js_header_user_settings" style="display: none">
		<ul>
			<li><a
				href="my_profile.sw?cid=<%=ISmartWorks.CONTEXT_PREFIX_MYPROFILE + cUser.getId()%>"><fmt:message
						key="header.global_menu.edit_my_profile" /> </a>
			</li>
			<li><a href="javascript:logout();"><fmt:message key="header.global_menu.logout" />
			</a>
			</li>
		</ul>
	</div>
	<!-- // -->
	<!-- 개인정보수정 및 로그아웃 영역  //-->

</div>
<!--  전체 메뉴  //-->
