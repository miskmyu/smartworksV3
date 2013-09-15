<%@page import="net.smartworks.service.impl.SmartWorks"%>
<%@page import="net.smartworks.model.instance.info.RequestParams"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.model.report.ChartReport"%>
<%@page import="net.smartworks.model.report.info.ReportInfo"%>
<%@page import="net.smartworks.model.filter.info.SearchFilterInfo"%>
<%@page import="net.smartworks.model.report.Report"%>
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
				$('#iwork_instance_list_page').html(data);
				smartPop.closeProgress();
			},
			error : function(xhr, ajaxOptions, thrownError) {
				smartPop.closeProgress();
				smartPop.showInfo(smartPop.ERROR, smartMessage.get('workListError'));
			}
		});
	};
	
	saveAsSearchFilter = function(filterId){
		var iworkList = $('.js_iwork_list_page');
		var searchFilter = $('.js_search_filter_page');
		var url = "set_work_search_filter.sw";
		if(isEmpty(filterId)){
			url = "create_work_search_filter.sw";
			searchFilter.find('input[name="txtNewFilterName"]').addClass('required');
		}

		if (!SmartWorks.GridLayout.validate(searchFilter.find('form.js_validation_required'), $('.js_filter_error_message'))) return;

		var paramsJson = {};
		var workId = iworkList.attr('workId');
		var searchFilters = searchFilter.find('form[name="frmSearchFilter"]');
		paramsJson['workId'] = workId;
		paramsJson['workType'] = <%=SmartWork.TYPE_INFORMATION%>;
		if(isEmpty(filterId)) {
			filterId = "";
		}
		paramsJson['filterId'] = filterId;
		paramsJson['txtNewFilterName'] = searchFilter.find('input[name="txtNewFilterName"]').attr('value');

		if(!isEmpty(searchFilters)){
			var searchFilterArray = new Array();
			for(var i=0; i<searchFilters.length; i++){
				var searchFilter = $(searchFilters[i]);
				if(searchFilter.is(':visible'))
					searchFilterArray.push(searchFilter.serializeObject());
			}
			paramsJson['frmSearchFilters'] = searchFilterArray;
		}
		console.log(JSON.stringify(paramsJson));
		var progressSpan = searchFilter.find('span.js_progress_span:first');
		smartPop.progressCont(progressSpan);
		$.ajax({
			url : url,
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				var selectSearchFilter = iworkList.find('.js_select_search_filter');
				selectSearchFilter.find('.js_custom_filter').remove();
				selectSearchFilter.append(data);
				$('a.js_search_filter_close').click();
				smartPop.closeProgress();
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
		//filterId에 system 문자열이 들어가지 않을 시,fileterId를 전달
		if(filterId.match(".*system.*")){
			saveAsSearchFilter("");
		}else{
			saveAsSearchFilter(filterId);
		}
	};

	selectListParam = function(progressSpan, isGray){
		var iworkList = $('.js_iwork_list_page');
		var forms = iworkList.find('form:visible');
		var paramsJson = {};
		var workId = iworkList.attr('workId');
		paramsJson["href"] = "jsp/content/work/list/iwork_instance_list.jsp?workId=" + workId;
		var searchFilters = iworkList.find('form[name="frmSearchFilter"]');
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
					searchFilterArray.push(searchFilter.serializeObject());
			}
			paramsJson['frmSearchFilters'] = searchFilterArray;
		}
		if(isEmpty(progressSpan)) progressSpan = iworkList.find('.js_search_filter_page').next('span.js_progress_span:first');
		getIntanceList(paramsJson, progressSpan, isGray);		
	};
</script>
<%
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	String cid = request.getParameter("cid");
	String wid = request.getParameter("wid");

	session.setAttribute("cid", cid);
	session.setAttribute("wid", wid);
	session.setAttribute("lastLocation", "iwork_list.sw");

	String workId = SmartUtil.getSpaceIdFromContentContext(cid);
	User cUser = SmartUtil.getCurrentUser();
	InformationWork work = (InformationWork) smartWorks.getWorkById(workId);
	String selectedFilterId = SearchFilter.FILTER_ALL_INSTANCES;
	RequestParams params = (RequestParams)request.getAttribute("requestParams");
	String searchKey = "";
	if (params == null){
		String savedWorkId = (String)session.getAttribute("workId");
		if(!SmartUtil.isBlankObject(savedWorkId) && savedWorkId.equals(work.getId())){
			params = (RequestParams)session.getAttribute("requestParams");
		}
	}
	if (params != null){
		selectedFilterId = params.getFilterId();
		searchKey = params.getSearchKey();
		params.setSearchFilter(null);
	}

	session.setAttribute("smartWork", work);
	session.removeAttribute("workInstance");
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!-- 컨텐츠 레이아웃-->
<div class="section_portlet js_iwork_list_page js_work_list_page" workId="<%=work.getId()%>">
	<div class="portlet_t"><div class="portlet_tl"></div></div>
	<div class="portlet_l" style="display: block;">
		<ul class="portlet_r" style="display: block;">

			<!-- 타이틀 -->
			<div class="body_titl">
				<div class="body_titl_iworks title"><%=work.getFullpathName()%></div>
				<!-- 우측 버튼 -->
				<div class="fr txt_btn icon_smartbuilder">
					<%if(work.amIBuilderUser()){ %>
						<a href="tab_workbench.sw?cid=<%=work.getContextId() %>" class="js_content">
							<fmt:message key="header.global_menu.smartbuilder"/>
						</a>
					<%} %>
				</div>
				<div class="txt_btn" style="line-height: 27px">
					<span class="js_progress_span"></span>
					<a class="js_view_work_manual" href="iwork_manual.sw"><fmt:message key="common.button.view.work_manual" /><span class="icon_in_down"></span></a>
					<a style="display: none" class="js_view_work_manual" href=""><fmt:message key="common.button.close.work_manual" /><span class="icon_in_down"></span></a>
				</div>
				<!-- 우측 버튼 //-->
				<div class="solid_line"></div>
			</div>
			<!-- 타이틀 -->

			<!-- 업무매뉴얼 보기 -->
			<div id="work_manual" style="display: none"></div>
			<!-- 업무매뉴얼 보기 //-->

			<!-- 목록영역  -->
			<div class="contents_space">
				<div>
					<jsp:include page="/jsp/content/work/report/work_report.jsp">
						<jsp:param value="<%=work.getLastReportId() %>" name="reportId"/>
					</jsp:include>
				</div>
				<!-- 목록보기 -->
				<div>
					<!-- 목록보기 타이틀-->
					<div class="list_title_space js_work_list_title mt15">
						<div class="title_line_btns">
							<%
							if(work.getWritePolicy().isWritableForMe()) {
							%>
								<div class="icon_btn_start">
									<a href="new_iwork.sw?workId=<%=workId%>" class="js_create_new_work icon_btn_tail" workId="<%=workId%>"><fmt:message key="common.button.add_new_iwork"/></a>
								</div>
								<div class="icon_btn_start">
									<a href="" class="icon_btn_tail js_import_from_excel"><fmt:message key="common.button.excel_import"/></a>
								</div>
							<%
								}
							%>
							<div class="icon_btn_start">
								<a href="" class="icon_btn_tail js_export_to_iwork_list_excel"><fmt:message key="common.button.excel_export"/></a>
							</div>
							<iframe class='js_excel_export' style='visibility: hidden;' src='' width='1' height='1'></iframe>
						</div>
					
						<div class="title_line_options">
							<form class="form_space po_left js_form_filter_name" name="frmIworkFilterName">
								<select name="selFilterName" class="js_select_search_filter">
									<option value="<%=SearchFilter.FILTER_ALL_INSTANCES%>" 
										<%if(SmartUtil.isBlankObject(selectedFilterId) || SearchFilter.FILTER_ALL_INSTANCES.equals(selectedFilterId)){%> selected <%} %>>
										<fmt:message key='filter.name.all_instances' />
									</option>
									<option value="<%=SearchFilter.FILTER_MY_INSTANCES%>"
										<%if(SearchFilter.FILTER_MY_INSTANCES.equals(selectedFilterId)){%> selected <%} %>>
										<fmt:message key='filter.name.my_instances' />
									</option>
									<option value="<%=SearchFilter.FILTER_RECENT_INSTANCES%>"
										<%if(SearchFilter.FILTER_RECENT_INSTANCES.equals(selectedFilterId)){%> selected <%} %>>
										<fmt:message key='filter.name.recent_instances' />
									</option>
									<option value="<%=SearchFilter.FILTER_MY_RECENT_INSTANCES%>"
										<%if(SearchFilter.FILTER_MY_RECENT_INSTANCES.equals(selectedFilterId)){%> selected <%} %>>
										<fmt:message key='filter.name.my_recent_instances' />
									</option>
									<option value="<%=SearchFilter.FILTER_RECENT_1YEAR_INSTANCES%>"
										<%if(SearchFilter.FILTER_RECENT_1YEAR_INSTANCES.equals(selectedFilterId)){%> selected <%} %>>
										<fmt:message key='filter.name.recent_1year_instances' />
									</option>
									<option value="<%=SearchFilter.FILTER_RECENT_3YEARS_INSTANCES%>"
										<%if(SearchFilter.FILTER_RECENT_3YEARS_INSTANCES.equals(selectedFilterId)){%> selected <%} %>>
										<fmt:message key='filter.name.recent_3years_instances' />
									</option>
									<%
									SearchFilterInfo[] filters = work.getSearchFilters();
									if (filters != null) {
										for (SearchFilterInfo filter : filters) {
											if(SmartUtil.isBlankObject(filter.getId())) continue;
									%>
											<option class="js_custom_filter" value="<%=filter.getId()%>" <%if(filter.getId().equals(selectedFilterId)){%> selected <%} %>><%=CommonUtil.toNotNull(filter.getName())%></option>
									<%
										}
									}
									%>
								</select>
							</form>
							<a href="search_filter.sw?workId=<%=workId%>" class="po_left js_edit_search_filter" title="<fmt:message key='filter.button.edit_search_filter' />">
								<div class="icon_btn_edit"></div>
							</a>
							<form name="frmSearchInstance" class="po_left ml10"> 
								<div class="srch_wh srch_wsize">
									<input name="txtSearchInstance" class="nav_input" value="<%=CommonUtil.toNotNull(searchKey) %>" type="text" placeholder="<fmt:message key='search.search_instance' />">
									<button title="<fmt:message key='search.search_instance'/>" onclick="selectListParam($('.js_work_list_title').find('.js_progress_span:first'), false);return false;"></button>
								</div>
							</form>
							<span class="js_progress_span"></span>
						</div>
					</div>
					<!-- 목록보기 타이틀-->

					<!-- 상세필터 및 새업무등록하기 화면 -->
					<div id="search_filter" class="filter_section js_new_work_form"></div>
					<!-- 상세필터 -->

					<!-- 목록 테이블 -->
					<div class="list_contents">
						<div id='iwork_instance_list_page' >
							<jsp:include page="/jsp/content/work/list/iwork_instance_list.jsp">
								<jsp:param value="<%=workId%>" name="workId"/>
							</jsp:include>
						</div>
					</div>
					<!-- 목록 테이블 //-->
				</div>
				<!-- 목록 보기 -->
			</div>
			<!-- 목록영역 // -->
		</ul>
	</div>
	<div class="portlet_b" style="display: block;"></div>
</div>
<!-- 컨텐츠 레이아웃//-->
