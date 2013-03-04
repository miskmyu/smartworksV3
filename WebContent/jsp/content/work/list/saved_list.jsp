<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.model.instance.info.RequestParams"%>
<%@page import="net.smartworks.model.community.WorkSpace"%>
<%@page import="net.smartworks.model.filter.info.SearchFilterInfo"%>
<%@page import="net.smartworks.model.work.ProcessWork"%>
<%@page import="net.smartworks.model.work.FormField"%>
<%@page import="net.smartworks.model.work.SmartForm"%>
<%@page import="net.smartworks.model.filter.SearchFilter"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.model.security.EditPolicy"%>
<%@page import="net.smartworks.model.security.WritePolicy"%>
<%@page import="net.smartworks.model.security.AccessPolicy"%>
<%@page import="net.smartworks.model.work.InformationWork"%>
<%@page import="net.smartworks.model.work.SmartWork"%>
<%@page import="net.smartworks.model.work.Work"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
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
				$('#saved_instance_list_page').html(data);
				smartPop.closeProgress();
			},
			error : function(xhr, ajaxOptions, thrownError) {
				smartPop.closeProgress();
				smartPop.showInfo(smartPop.ERROR, smartMessage.get('workListError'));
			}
		});
	};
	
	selectListParam = function(progressSpan, isGray){
		var pworkList = $('.js_space_work_list_page');
		var forms = pworkList.find('form:visible');
		var paramsJson = {};
		var workId = pworkList.attr('workId');
		paramsJson["href"] = "jsp/content/work/list/saved_instance_list.jsp?workId=" + workId;
		var searchFilters = pworkList.find('form[name="frmSearchFilter"]');
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
		if(isEmpty(progressSpan)) progressSpan = pworkList.find('.js_search_filter').next('span.js_progress_span:first');
		getIntanceList(paramsJson, progressSpan, isGray);		
	};
</script>
<%
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	String cid = request.getParameter("cid");
	String wid = request.getParameter("wid");

	User cUser = SmartUtil.getCurrentUser();
	WorkSpace workSpace = smartWorks.getWorkSpaceById(wid);
	String workSpaceName = (SmartUtil.isBlankObject(wid)) ? cUser.getCompany() : workSpace.getName(); 

	RequestParams params = (RequestParams)request.getAttribute("requestParams");
	String searchKey = "";
	if (params == null){
		String savedWorkId = (String)session.getAttribute("workId");
		if(!SmartUtil.isBlankObject(savedWorkId) && savedWorkId.equals(SmartWork.ID_SAVED_WORKS)){
			params = (RequestParams)session.getAttribute("requestParams");
		}
	}if (params != null){
		searchKey = params.getSearchKey();
		params.setSearchFilter(null);
	}
	
	session.setAttribute("cid", cid);
	session.setAttribute("wid", wid);
	session.setAttribute("lastLocation", "saved_list.sw");
	session.setAttribute("workSpace", workSpace);
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<div class="body_titl space">
	<div class="saved fl"></div>
		<div class="info">
			<div class="title"><fmt:message key='common.title.saved_list'/></div>
			<div></div>
		</div>
	<div class="solid_line"></div>
</div>
			
<!-- 컨텐츠 레이아웃-->
<div class="section_portlet js_space_work_list_page js_saved_list_page">
	<div class="portlet_t"><div class="portlet_tl"></div></div>
	<div class="portlet_l" style="display: block;">
		<ul class="portlet_r" style="display: block;">

			<!-- 목록보기 -->
			<div class=" contents_space">
				<!-- 목록보기 타이틀-->
				<div class="list_title_space js_work_list_title">
					<div class="title_line_btns">
					</div>
					
					<div class="title_line_options fr">
						<form name="frmSearchInstance" class="po_left">
							<span class="js_progress_span"></span>
							<div class="srch_wh srch_wsize m0">
								<input name="txtSearchInstance" class="nav_input" value="<%=CommonUtil.toNotNull(searchKey) %>" type="text" placeholder="<fmt:message key='search.search_instance' />">
								<button title="<fmt:message key='search.search_instance'/>" onclick="selectListParam($('.js_work_list_title').find('.js_progress_span:first'), false);return false;"></button>
							</div>
						</form>

						
					</div>
				</div>
				<!-- 목록보기 타이틀-->
				
				<!-- 상세필터 및 새업무등록하기 화면 -->
				<div id="search_filter" class="filter_section js_new_work_form"></div>
				<!-- 상세필터 -->

				<!-- 목록 테이블 -->
 				<div class="list_contents">
					<div id='saved_instance_list_page'>
 						<jsp:include page="/jsp/content/work/list/saved_instance_list.jsp"/>
					</div>
				</div>
				<!-- 목록 테이블 //-->

				<!-- 목록보기 -->
			</div>
		</ul>
	</div>
	<div class="portlet_b" style="display: block;"></div>
</div>
<!-- 컨텐츠 레이아웃//-->
