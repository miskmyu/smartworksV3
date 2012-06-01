<%@page import="net.smartworks.model.instance.info.InstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.TaskInstanceInfo"%>
<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.model.instance.info.EventInstanceInfo"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ page import="net.smartworks.model.community.*"%>
<%@ page import="net.smartworks.model.notice.*"%>
<%@ page import="net.smartworks.model.instance.*"%>
<%@ page import="net.smartworks.model.work.*"%>
<%@ page import="net.smartworks.util.LocalDate"%>
<script>
	function updateAvailableChatters(messasge) {
		var userInfos = message.userInfos;
		if (userInfos != null) {
			var nickNameBase = ($('.js_chatter_list_page').attr('nickNameBase') === 'true');
			var data = "";
			var length = userInfos.length;
			for ( var i = 0; i < length; i++) {
				data = data + "<li>" +
									"<a href='' userId='" + userInfos[i].userId + "'>" +
										"<span><img src='" + userInfos[i].minPicture + "' class='mr2 profile_size_s chat_online' title='" + nickNameBase ? userInfos[i].nickName : userInfos[i].longName + "'/>" + nickNameBase ? userInfos[i].nickName : userInfos[i].longName + "</span>" +
									"</a>" +
								"</li>";
			}
			$("#available_chatter_list").html(data).parents('div.js_chatter_list').find('span.js_chatters_number').html("(" + length + ")");
		}
	}
</script>
<%
	User cUser = SmartUtil.getCurrentUser();
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	String userNaming = (String)session.getAttribute("userNaming");  
	boolean nickNameBase = SmartUtil.isBlankObject(userNaming) ? false : userNaming.equals(User.NAMING_NICKNAME_BASE) ? true : false;
	UserInfo[] chatters = smartWorks.getAvailableChatter();
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />
<!-- POP-채팅 설정 -->
	<div class="chatter_list_area admin" style="display:block">
		<!-- 오프라인으로 표시 클릭하면 <fmt:message key="chat.title.chatting"/> 앞에 아이콘도 class="chatic_titl off"처럼  클래스명"off"를 추가해줍니다  -->
		<ul>
			<li>
			<a href=""><span class="chatic_titl off pop">오프라인으로 표시</span></a>
			</li>
		</ul>
	</div>
<!-- POP-채팅 설정 //-->
<!-- 채팅Default -->	 
<div class="chat_de_section js_chatter_list js_chatter_list_page" nickNameBase="<%=nickNameBase%>">
	<!-- 상단 -->
	<div class="top_group">
		<a href="" class="js_toggle_chatter_list">
			<div class="chatic_titl"><fmt:message key="chat.title.chatting"/><span class="t_white js_chatters_number">(<%=chatters.length%>)</span></div>
		</a>
		<!-- 상단우측 아이콘-->
		<div class="txt_btn">
			<div class="ch_right btn_admin"><a><span></span></a></div>
		</div>
		<!-- 상단 우측 아이콘//-->
	</div>
	<!-- 상단 //-->

	<!--온라인 일때는 class="chat_online", 오프라인 일때는 chat_offline -->
	<div class="js_chatter_search_area" style="display: none">

		<!-- Body -->
		<div class="chat_de_list js_chatter_list" id="available_chatter_list">
			<ul>
				<%
				for (UserInfo chatter : chatters) {
					String userName = (nickNameBase) ? chatter.getNickName() : chatter.getLongName();
				%>
					<li>
						<a href="" userId="<%=chatter.getId()%>">
							<span><img src="<%=chatter.getMinPicture()%>" class="mr2 profile_size_s chat_offline" title="<%=userName%>" /><%=userName%></span>
						</a>
					</li>
				<%
				}
				%>
			</ul>
		</div>
		<!-- Body //-->

		<!-- 검색영역 -->
		<div class="chat_input_section js_chatter_names">
			<div class="chat_input_area">
				<input id="" class="input js_auto_complete" type="text" href="chatter_name.sw" placeholder="<fmt:message key='search.search_chatter'/>" title="<fmt:message key='search.search_chatter'/>">
			</div>
		</div>
	</div>
	<!-- 검색영역//-->
</div>
<!-- 채팅Default //-->

<!-- 이동 화살표-->
	<div class="chat_num_section js_chatting_group_prev" style="display:none">
	<a href="">
		<div class="top_group">
			<span class="cha_num js_group_prev_count"></span>
			<span class="cha_prev"></span>
		</div>
	</a>
</div>

<!-- 채팅창 생성 -->
<div class="js_chatting_box_list"></div>

<!-- 이동 화살표-->
	<div class="chat_num_section js_chatting_group_next" style="display:none">
	<a href="">
		<div class="top_group">
			<span class="cha_next"></span>
			<span class="cha_num js_group_next_count"></span>
		</div>
	</a>
</div>
<!-- 이동 화살표 //-->