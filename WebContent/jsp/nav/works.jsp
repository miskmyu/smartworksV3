
<!-- Name 			: works.jsp										 				 -->
<!-- Description	: 좌측의 Navigation Bar에서 현재사용자의 업무를 찾아서 페이징하는 박스 	 -->
<!-- Author			: Maninsoft, Inc.												 -->
<!-- Created Date	: 2011.9.														 -->

<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>

<!-- 나의 업무 와 검색박스가 있는 헤더  -->
<ul class="nav_tit_mail">

	<!-- 나의 업무 라벨과 클릭시 아래의 업무선택트리화면을 접었다 폈다하는 기능 제공  -->
	<!-- *** js_collapse_parent_siblings : sw_act_nav.js 에서 이클래스의 클릭이벤트를 받아서 -->
	<!--            아래의 js_callapsible 클래스를 찾아 toggle 한다 -->
	<li> 
		<a href="" class='js_collapse_parent_siblings arr_on'><fmt:message key="nav.works.my_works" /></a>
		<span></span><!--  프로그래스아이콘이 실행되는 곳 -->
	</li>
	
	<!--  검색박스를 제공하여, 초성검색 기능을 제공 -->
	<li class="nav_srch js_srch_my_works" style="width:0">
		<a href="" onclick="$(this).hide().next().show().parents('ul:first').removeClass('nav_tit_mail').addClass('nav_tit');return false;"  title="<fmt:message key='search.search_work'/>"><div class="srch_icon_only"></div></a>
		<div class="srch srch_wsize" style="display:none">
			<!-- *** js_auto_complete : js_act_search.js에서 이 클래스의 keyup, focusout, keydown등을 받아서 처리한다. -->
			<input id="" class="nav_input js_auto_complete" type="text" title="<fmt:message key='search.search_work'/>"
				placeholder="<fmt:message key='search.search_work'/>" href="work.sw" />
			<a href="" onclick="$(this).parent().hide().prev().show().parents('ul').removeClass('nav_tit').addClass('nav_tit_mail');return false;"><div class='srch_icon js_srch_x'></div></a>
		</div>
		
		<!-- nav 검색 리스트 -->
		<div class="nav_srch_list m0" style="display: none"></div>
		<!-- nav 검색 리스트 -->
		
	</li>
	<!--  검색박스를 제공하여, 초성검색 기능을 제공 //-->

</ul>
<!-- 나의 업무 와 검색박스가 있는 헤더  //-->

<!--  최근업무, 즐겨찾는 업무, 전체업무들의 업무선택 트리 화면  -->
<div class="nav_sub_list js_collapsible  js_nav_my_works">
	<!-- 내부 메뉴 -->
	<div class="tab_buttons js_nav_tab_work">
	
 		<a href="my_all_works.sw" title="<fmt:message key='nav.works.my_all_works' />"><span class="btn_all_works"/></span></a>
 		<a href="my_recent_instances.sw" title="<fmt:message key='nav.works.my_recent_instances' />" ><span class="btn_recent"></span></a>
 		<a href="my_favorite_works.sw" title="<fmt:message key='nav.works.my_favorite_works' />"><span class="btn_favorite current"/></span></a>
		<span class="fr ml2 js_progress_span"></span><!--  프로그래스아이콘이 실행되는 곳 -->
 		
	</div>
	
	<div id='my_works'>
		<jsp:include page="my_favorite_works.jsp" />
	</div>
	<!--내부메뉴//-->
</div>
<!--  최근업무, 즐겨찾는 업무, 전체업무들의 업무선택 트리 화면  //-->
