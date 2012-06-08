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
				$('#work_instance_list_page').html(data);
				smartPop.closeProgress();
			},
			error : function(xhr, ajaxOptions, thrownError) {
				smartPop.closeProgress();
				smartPop.showInfo(smartPop.ERROR, smartMessage.get('workListError'));
			}
		});
	};
	
	saveAsSearchFilter = function(filterId){
		var pworkList = $('.js_pwork_list_page');
		var searchFilter = $('.js_search_filter_page');
		var url = "set_pwork_search_filter.sw";
		if(isEmpty(filterId)){
			url = "create_pwork_search_filter.sw";
			searchFilter.find('input[name="txtNewFilterId"]').addClass('required');
		}

		if (!SmartWorks.GridLayout.validate(searchFilter.find('form.js_validation_required'), $('.js_filter_error_message'))) return;

		var paramsJson = {};
		var workId = iworkList.attr('workId');
		var searchFilters = searchFilter.find('form[name="frmSearchFilter"]');
		paramsJson['workId'] = workId;
		if(!isEmpty(filterId))
			paramsJson['filterId'] = filterId;
		if(!isEmpty(searchFilters)){
			var searchFilterArray = new Array();
			for(var i=0; i<searchFilters.length; i++){
				var searchFilter = $(searchFilters[i]);
				if(searchFilter.is(':visible'))
					searchFilterArray.push(searchFilter.find(':visible').serializeObject());
			}
			paramsJson['frmSearchFilters'] = searchFilterArray;
		}
		var progressSpan = searchFilter.find('span.js_progress_span:first');
		smartPop.progressCont(progressSpan);
		$.ajax({
			url : url,
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				$('.js_search_filter_list_box:first').html(data);
				$('a.js_search_filter_close').click();
				smartPop.closeProgress();
				smartPop.showInfo(smartPop.INFO, smartMessage.get('setFilterSucceed'));
			},
			error : function(xhr, ajaxOptions, thrownError) {
				smartPop.closeProgress();
				if(xhr.status == httpStatus.InternalServerError){
					var message = smartMessage.get(xhr.responseText);
					if(!isEmpty(message)){
						smartPop.showInfo(smartPop.ERROR, message);
						return;
					}
				}
				smartPop.showInfo(smartPop.ERROR, smartMessage.get('setFilterError'));
			}
		});
	};
	
	saveSearchFilter = function(){
		var searchFilter = $('.js_search_filter_page');
		var filterId = searchFilter.attr('filterId');
		if(isEmpty(filterId)) searchFilter.find('input[name="txtNewFilterId"]').removeClass('required');
		saveAsSearchFilter(filterId);
	};

	selectListParam = function(progressSpan, isGray){
		var pworkList = $('.js_space_work_list_page');
		var forms = pworkList.find('form:visible');
		console.log(forms);
		var paramsJson = {};
		var workId = pworkList.attr('workId');
		paramsJson["href"] = "jsp/content/work/list/work_instance_list.jsp?workId=" + workId;
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
	session.setAttribute("cid", cid);
	session.setAttribute("wid", wid);

	User cUser = SmartUtil.getCurrentUser();
	WorkSpace workSpace = smartWorks.getWorkSpaceById(wid);
	String workSpaceName = (SmartUtil.isBlankObject(wid)) ? cUser.getCompany() : workSpace.getName(); 

%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<jsp:include page="/jsp/content/upload/select_upload_action.jsp"></jsp:include>

<!-- 컨텐츠 레이아웃-->
<div class="section_portlet js_space_work_list_page js_work_list_page">
	<div class="portlet_t"><div class="portlet_tl"></div></div>
	<div class="portlet_l" style="display: block;">
		<ul class="portlet_r" style="display: block;">

			<!-- 타이틀 -->
			<div class="body_titl">
				<div class="body_titl_iworks title">
					<div class="title myspace_h"><%=workSpaceName %>
						<span class="bul_space"><fmt:message key="space.title.works"/></span>
					</div>
				</div>
				<!-- 우측 버튼 -->
				<div class="txt_btn">
				</div>
				<div class="solid_line"></div>
			</div>
			<!-- 타이틀 -->

			<!-- 목록보기 -->
			<div class=" contents_space">
				<!-- 목록보기 타이틀-->
				<div class="list_title_space js_work_list_title mt15">
					<div class="title"><fmt:message key="common.title.instance_list" /></div>
					<div class="title_line_btns">
						<div class="icon_btn_edit">
							<a href="search_filter.sw?workId=" class="js_edit_search_filter icon_btn_tail"><fmt:message key='filter.button.edit_search_filter' /></a>
						</div>
					</div>
					
					<div class="title_line_options">
						<form name="frmSearchInstance" class="po_left">
							<span class="js_progress_span"></span>
							<div class="srch_wh srch_wsize">
								<input name="txtSearchInstance" class="nav_input" type="text" placeholder="<fmt:message key='search.search_instance' />">
									<button title="<fmt:message key='search.search_instance'/>" onclick="selectListParam($('.js_work_list_title').find('.js_progress_span:first'), false);return false;"></button>
							</div>
						</form>

						<form class="form_space po_left js_form_filter_name" name="frmPworkFilterName">
							<select name="selFilterName" class="js_select_filter" href="search_filter.sw?workId=">
								<option value="<%=SearchFilter.FILTER_ALL_INSTANCES%>" selected>
									<fmt:message key='filter.name.all_instances' />
								</option>
								<option value="<%=SearchFilter.FILTER_MY_INSTANCES%>">
									<fmt:message key='filter.name.my_instances' />
								</option>
								<option value="<%=SearchFilter.FILTER_RECENT_INSTANCES%>">
									<fmt:message key='filter.name.recent_instances' />
								</option>
								<option value="<%=SearchFilter.FILTER_MY_RECENT_INSTANCES%>">
									<fmt:message key='filter.name.my_recent_instances' />
								</option>
								<option value="<%=SearchFilter.FILTER_MY_RUNNING_INSTANCES%>">
									<fmt:message key='filter.name.my_running_instances' />
								</option>
							</select>
						</form>
					</div>
				</div>
				<!-- 목록보기 타이틀-->
				
				<!-- 상세필터 및 새업무등록하기 화면 -->
				<div id="search_filter" class="filter_section js_new_work_form"></div>
				<!-- 상세필터 -->

				<!-- 목록 테이블 -->
 				<div class="list_contents">
					<div id='work_instance_list_page'>
 						<jsp:include page="/jsp/content/work/list/work_instance_list.jsp"/>
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
