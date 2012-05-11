<%@page import="net.smartworks.model.work.SmartWork"%>
<%@page import="net.smartworks.model.sera.Constants"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.WorkSpace"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<script type="text/javascript">
	getIntanceList = function(paramsJson, progressSpan, isGray){
		if(isEmpty(progressSpan))
			progressSpan = $('.js_work_list_title').find('.js_progress_span:first');
		if(isGray)
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
				console.log('data = ',data);
				$('#board_instance_list_page').html(data);
				smartPop.closeProgress();
			},
			error : function(xhr, ajaxOptions, thrownError) {
				smartPop.closeProgress();
				smartPop.showInfo(smartPop.ERROR, smartMessage.get('workListError'));
			}
		});
	};

	selectListParam = function(progressSpan, isGray){
		var boardList = $('.js_board_list_page');
		var forms = boardList.find('form:visible');
		var paramsJson = {};
		var workId = boardList.attr('workId');
		paramsJson["href"] = "sera/jsp/content/board/board_instance_list.jsp?workId=" + workId;
		var searchFilters = boardList.find('form[name="frmSearchFilter"]');
		for(var i=0; i<forms.length; i++){
			var form = $(forms[i]);
			if(form.attr('name') !== "frmSearchFilter" && !(!isEmpty(searchFilters) && form.attr('name') === "frmSearchInstance")){
				paramsJson[form.attr('name')] = mergeObjects(form.serializeObject(), SmartWorks.GridLayout.serializeObject(form));
			}
		}
		if(!isEmpty(searchFilters)){
			var searchFilterArray = new Array();
			for(var i=0; i<searchFilters.length; i++){
				var searchFilter = $(searchFilters[i]);
				if(searchFilter.is(':visible'))
					searchFilterArray.push(searchFilter.find(':visible').serializeObject());
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
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />
<div id="course_list_section" class="js_work_list_page js_board_list_page" workId="<%=SmartWork.ID_BOARD_MANAGEMENT%>">
	<!-- SNB Left -->
	<div class="snb2">
		<ul class="snb_menu">
			<li><img height="112" width="149"
				src="/sera/images/function_title_admin.gif"></li>
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

			<!-- 검색 -->
			<div class="fr mt25">
				<form name="frmSearchInstance" class="po_left">
					<span class="js_progress_span"></span>
					<div class="srch_wh srch_wsize">
						<input name="txtSearchInstance" class="nav_input" type="text" placeholder="<fmt:message key='search.search_instance' />">
							<button title="<fmt:message key='search.search_instance'/>" onclick="selectListParam($('.js_work_list_title').find('.js_progress_span:first'), false);return false;"></button>
					</div>
				</form>
			</div>
			<!-- 검색//-->

		</div>
		<!-- Title //-->

		<!-- Btn -->
		<div class="buttonSet">
			<button>
				<span class="icon_txt_delet"></span>삭제
			</button>
			<button>글쓰기</button>
			<button class="fr ml5">글숨기기</button>
			<button class="fr">글보이기</button>
		</div>
		<!-- Btn //-->
		<!-- 조회 -->
		<div class="list_contents">
			<div id='board_instance_list_page'>
				<jsp:include page="/sera/jsp/content/board/board_instance_list.jsp"/>
			</div>
		</div>
	</div>
	<!-- Content //-->
</div>