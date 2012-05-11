<%@page import="net.smartworks.model.work.SmartWork"%>
<%@page import="net.smartworks.model.sera.Constants"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.WorkSpace"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<script type="text/javascript">
	getIntanceList = function(paramsJson, progressSpan, isGray) {
		if (isEmpty(progressSpan))
			progressSpan = $('.js_work_list_title').find(
					'.js_progress_span:first');
		if (isGray)
			smartPop.progressContGray(progressSpan);
		else
			smartPop.progressCont(progressSpan);
		console.log(JSON.stringify(paramsJson));
		var url = "set_instance_list_params.sw";
		$.ajax({
			url : url,
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				console.log('data = ', data);
				$('#board_instance_list_page').html(data);
				smartPop.closeProgress();
			},
			error : function(xhr, ajaxOptions, thrownError) {
				smartPop.closeProgress();
				smartPop.showInfo(smartPop.ERROR, smartMessage
						.get('workListError'));
			}
		});
	};

	selectListParam = function(progressSpan, isGray) {
		var boardList = $('.js_board_list_page');
		var forms = boardList.find('form:visible');
		var paramsJson = {};
		var workId = boardList.attr('workId');
		paramsJson["href"] = "sera/jsp/content/board/board_instance_list.jsp?workId="
				+ workId;
		var searchFilters = boardList.find('form[name="frmSearchFilter"]');
		for ( var i = 0; i < forms.length; i++) {
			var form = $(forms[i]);
			if (form.attr('name') !== "frmSearchFilter"
					&& !(!isEmpty(searchFilters) && form.attr('name') === "frmSearchInstance")) {
				paramsJson[form.attr('name')] = mergeObjects(form
						.serializeObject(), SmartWorks.GridLayout
						.serializeObject(form));
			}
		}
		if (!isEmpty(searchFilters)) {
			var searchFilterArray = new Array();
			for ( var i = 0; i < searchFilters.length; i++) {
				var searchFilter = $(searchFilters[i]);
				if (searchFilter.is(':visible'))
					searchFilterArray.push(searchFilter.find(':visible')
							.serializeObject());
			}
			paramsJson['frmSearchFilters'] = searchFilterArray;
		}
		getIntanceList(paramsJson, progressSpan, isGray);
	};
</script>
<%
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();
	session.setAttribute("wid", Constants.SERA_WID_SERA_NEWS);
	String workId = request.getParameter("workId");
	String instId = request.getParameter("instId");
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />
<div id="course_list_section" class="js_sera_news_item_page" workId="<%=workId%>" instId="<%=instId%>">
	<!-- SNB Left -->
	<div class="snb2">
		<ul class="snb_menu">
			<li><img height="112" width="149"
				src="../images/function_title_admin.gif"></li>
			<li class="selected"><a href="">세라소식</a></li>
			<li class=""><a href="">트렌드 세라</a></li>
		</ul>
	</div>
	<!-- SNB Left//-->
	<!-- Content -->
	<div id="content_list_section">
		<!-- Title -->
		<div class="tit_header">
			<h2>세라 소식</h2>
		</div>
		<!-- Title //-->

		<!-- Input Form -->
		<div class="form_layout js_form_content">
		</div>
		<div class="sw_error_message tl" style="color: red"></div>

		<!-- Btn -->
		<div class="btn_space">
			<div style="clear: both; display: inline-block">
				<div class="btn_blu_l mr10 js_create_news_btn" href="" <%if(!SmartUtil.isBlankObject(instId)) { %>style="display:none"<%}%>>
					<div class="btn_blu_r">등 록</div>
				</div>
				<div class="btn_blu_l mr10 js_save_news_btn" href="" style="display:none">
					<div class="btn_blu_r">저 장</div>
				</div>
				<div class="btn_red_l js_modify_news_btn" href="" <%if(SmartUtil.isBlankObject(instId)) { %>style="display:none"<%}%>>
					<div class="btn_red_r">수 정</div>
				</div>
				<div class="btn_red_l js_delete_news_btn" href="" <%if(SmartUtil.isBlankObject(instId)) { %>style="display:none"<%}%>>
					<div class="btn_red_r">삭 제</div>
				</div>
				<div class="btn_red_l js_cancel_news_btn" href="">
					<div class="btn_red_r">취 소</div>
				</div>
			</div>
		</div>
		<!-- Btn //-->

	</div>
	<!-- Content //-->
</div>
<script type="text/javascript">
	var newsItem = $('.js_sera_news_item_page');
	var workId = newsItem.attr("workId");
	var instId = newsItem.attr("instId");
	var formContent = newsItem.find('div.js_form_content');
	new SmartWorks.GridLayout({
		target : formContent,
		mode : "view",
		workId : workId,
		recordId : instId,
		onSuccess : function(){
		}
	});
</script>