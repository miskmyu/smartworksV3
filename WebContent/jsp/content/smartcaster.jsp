<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.model.instance.info.TaskInstanceInfo"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();

	session.setAttribute("cid", ISmartWorks.CONTEXT_SMARTCASTER);
	session.setAttribute("lastLocation", "smartcaster.sw");
	session.removeAttribute("wid");
	
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!-- 타이틀 -->
<div class="body_titl">
	<div class="smartcaster title"><fmt:message key="header.top_menu.smartcaster"/></div>
       	<div class="txt_btn"></div>
	<div class="solid_line"></div>
</div>
<!-- 타이틀 //-->
			
<!-- 새업무, 사진, 파일, 이벤트... 을 올리는 화면 -->
<jsp:include page="/jsp/content/upload/select_upload_action.jsp" />
<!-- 새업무, 사진, 파일, 이벤트... 을 올리는 화면 //-->
<!-- 컨텐츠 레이아웃-->

<div class="section_portlet js_smartcaster_page">
	<div class="portlet_t">
		<div class="portlet_tl"></div>
	</div>
	<div class="portlet_l" style="display: block;">
		<ul class="portlet_r" style="display: block;">
		
			<!-- 검색&필터 -->
			<div class="contents_space oh">
				<div class="title_line_options fr">
					<form class="po_left" name="frmSearchInstance">
						<div class="srch_wh srch_wsize">
							<input class="nav_input" type="text" placeholder="항목 찾기" name="txtSearchInstance">
							<button onclick="selectListParam($('.js_work_list_title').find('.js_progress_span:first'), false);return false;" title="항목 찾기"></button>
						</div>
						</form>
						<form class="form_space po_left js_form_filter_name" name="frmIworkFilterName">
						<select class="js_select_search_filter" name="selFilterName">
							<option selected="" value="">이름  </option>
							<option value="">이름 </option>
							<option value="">이름 </option>
							<option value="">이름 </option>
						</select>
					</form>
					<a class="js_edit_search_filter" title="상세필터편집" href="">
						<div class="icon_btn_edit"></div>
					</a>
				</div>
				<div class="solid_line cb"></div>
			</div>
			<!-- 검색&필터 //-->
			
			<jsp:include page="/jsp/content/more_smartcast.jsp"></jsp:include>
		</ul>
	</div>
	<div class="portlet_b" style="display: block;"></div>
</div>
<!-- 컨텐츠 레이아웃//-->
