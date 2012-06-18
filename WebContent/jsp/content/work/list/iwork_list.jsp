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
				//if(searchFilter.is(':visible'))
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
		saveAsSearchFilter(filterId);
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
					searchFilterArray.push(searchFilter.find(':visible').serializeObject());
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

	String selectedFilterId = SearchFilter.FILTER_ALL_INSTANCES;
	RequestParams params = (RequestParams)request.getAttribute("requestParams");
	if (params == null)
		params = (RequestParams)session.getAttribute("requestParams");
	if (params != null)
		selectedFilterId = params.getFilterId();
	
	String workId = SmartUtil.getSpaceIdFromContentContext(cid);
	User cUser = SmartUtil.getCurrentUser();
	InformationWork work = (InformationWork) smartWorks.getWorkById(workId);
	session.setAttribute("smartWork", work);
	session.removeAttribute("workInstance");
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!-- 컨텐츠 레이아웃-->
<div class="section_portlet js_iwork_list_page js_work_list_page" workId=<%=work.getId()%>>
	<div class="portlet_t"><div class="portlet_tl"></div></div>
	<div class="portlet_l" style="display: block;">
		<ul class="portlet_r" style="display: block;">

			<!-- 타이틀 -->
			<div class="body_titl">
				<div class="body_titl_iworks title"><%=work.getName()%></div>
				<!-- 우측 버튼 -->
				<div class="txt_btn">
					<a class="js_view_work_manual" href="iwork_manual.sw"><fmt:message key="common.button.view.work_manual" /><span class="icon_in_down"></span></a>
					<a style="display: none" class="js_view_work_manual" href=""><fmt:message key="common.button.close.work_manual" /><span class="icon_in_down"></span></a>
				</div>
				<span class="js_progress_span"></span>
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
						<div class="title"><fmt:message key="common.title.instance_list" /></div>
						<div class="title_line_btns">
							<span class="icon_filter_edit ">
								<a href="search_filter.sw?workId=<%=workId%>" class="js_edit_search_filter" title="상세필터 편집"><%-- <fmt:message key='filter.button.edit_search_filter' /> --%></a>
							</span>
							<%
								if(work.getWritePolicy().isWritableForMe()) {
							%>
							<span class="icon_work_create">
								<a href="new_iwork.sw?workId=<%=workId%>" class="js_create_new_work" workId="<%=workId%>" title="새항목 등록하기"><%-- <fmt:message key="common.button.add_new_iwork"/> --%></a>
							</span>
							<span class="icon_excel">
								<a href="" title="엑셀로 등록하기"></a>
							</span>
							<%
								}
							%>
						</div>
					
						<div class="title_line_options">
							<form name="frmSearchInstance" class="po_left">
								<span class="js_progress_span"></span>
								<div class="srch_wh srch_wsize">
									<input name="txtSearchInstance" class="nav_input" type="text" placeholder="<fmt:message key='search.search_instance' />">
										<button title="<fmt:message key='search.search_instance'/>" onclick="selectListParam($('.js_work_list_title').find('.js_progress_span:first'), false);return false;"></button>
								</div>
							</form>
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
									<%
									SearchFilterInfo[] filters = work.getSearchFilters();
									if (filters != null) {
										for (SearchFilterInfo filter : filters) {
											if(SmartUtil.isBlankObject(filter.getId())) continue;
									%>
											<option value="<%=filter.getId()%>" <%if(filter.getId().equals(selectedFilterId)){%> selected <%} %>><%=CommonUtil.toNotNull(filter.getName())%></option>
									<%
										}
									}
									%>
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
