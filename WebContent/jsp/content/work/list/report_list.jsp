<%@page import="net.smartworks.model.work.WorkCategory"%>
<%@page import="net.smartworks.model.work.info.WorkInfo"%>
<%@page import="net.smartworks.model.work.ReportWork"%>
<%@page import="net.smartworks.util.SmartTest"%>
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
				$('#report_instance_list_page').html(data);
				smartPop.closeProgress();
			},
			error : function(xhr, ajaxOptions, thrownError) {
				smartPop.closeProgress();
				smartPop.showInfo(smartPop.ERROR, smartMessage.get('workListError'));
			}
		});
	};
	
	saveAsSearchFilter = function(filterId){
		var reportList = $('.js_report_list_page');
		var searchFilter = $('.js_search_filter_page');
		var url = "set_work_search_filter.sw";
		if(isEmpty(filterId)){
			url = "create_work_search_filter.sw";
			searchFilter.find('input[name="txtNewFilterName"]').addClass('required');
		}

		if (!SmartWorks.GridLayout.validate(searchFilter.find('form.js_validation_required'), $('.js_filter_error_message'))) return;

		var paramsJson = {};
		var workId = reportList.attr('workId');
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
				var selectSearchFilter = reportList.find('.js_select_search_filter');
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
		var reportList = $('.js_report_list_page');
		var forms = reportList.find('form:visible');
		var paramsJson = {};
		var targetWorkId = reportList.attr('targetWorkId');
		var targetWorkType = reportList.attr('targetWorkType');
		var producedBy = reportList.attr('producedBy');
		paramsJson["href"] = "jsp/content/work/list/report_instance_list.jsp?targetWorkId=" + targetWorkId + "&targetWorkType=" + targetWorkType + "&producedBy=" + producedBy;
		var searchFilters = reportList.find('form[name="frmSearchFilter"]');
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
		if(isEmpty(progressSpan)) progressSpan = reportList.find('.js_search_filter_page').next('span.js_progress_span:first');
		getIntanceList(paramsJson, progressSpan, isGray);		
	};
</script>
<%
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	String cid = request.getParameter("cid");
	String wid = request.getParameter("wid");

	session.setAttribute("cid", cid);
	session.setAttribute("wid", wid);
	session.setAttribute("lastLocation", "report_list.sw");

	String workId = SmartWork.ID_REPORT_MANAGEMENT;
	String targetWorkId = SmartUtil.getSpaceIdFromContentContext(cid);
	String producedBy = Report.PRODUCED_BY_SMARTWORKS;
	User cUser = SmartUtil.getCurrentUser();
	ReportWork work = (ReportWork) smartWorks.getWorkById(workId);
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

	int userReportCount = smartWorks.getUserReportCount(targetWorkId);
	
	session.setAttribute("smartWork", work);
	session.removeAttribute("workInstance");
	
	WorkInfo[] workCategories = smartWorks.getAllWorksByCategoryId("");
	String categoryId = "";
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!-- 컨텐츠 레이아웃-->
<div class="section_portlet js_report_list_page js_work_list_page js_work_report_page" workId="<%=SmartWork.ID_REPORT_MANAGEMENT%>" targetWorkId="<%=targetWorkId %>" targetWorkName="<fmt:message key="report.title.company_all_works"/>" targetWorkIcon="icon_depart" targetWorkType="<%=Work.TYPE_NONE %>" producedBy="<%=producedBy%>">
	<div class="portlet_t"><div class="portlet_tl"></div></div>
	<div class="portlet_l" style="display: block;">
		<ul class="portlet_r" style="display: block;">

			<!-- 타이틀 -->
			<div class="body_titl">
				<div class="body_titl_iworks title"><%=work.getFullpathName()%></div>
				<!-- 우측 버튼 -->
				<div class="txt_btn" style="text-align:left;font-size:14px;">
					<span class="btn_gray" style="margin-left:40px"> 
						<a href="" class="js_pop_all_target_works"> 
							<span class="txt_btn_start"></span>
							<span class="txt_btn_center"><fmt:message key="report.title.target_work"/></span> 
							<span class="txt_btn_end"></span> 
						</a>
					</span>
					<div class="js_target_work_info" style="display:inline-block">
						<span class="icon_depart"></span>
						<span style="font-size:13px;line-height:21px;margin-left:4px;"><fmt:message key="report.title.company_all_works"/></span>
					</div>
				</div>
				<!-- 우측 버튼 //-->
				<div class="solid_line"></div>
				<!-- 업무트리가 나타나는 곳 -->
				<span class="js_all_target_work_popup" style="position:absolute;"></span>
			</div>
			<!-- 타이틀 -->

			<!-- 목록영역  -->
			<div class="contents_space">
				<!-- 목록보기 -->
				<div>
					<!-- 목록보기 타이틀-->
					<div class="list_title_space js_work_list_title mt15">
						<div class="title pr10 fl js_report_list_title js_report_count">
							<span class="js_view_report_list"><a href="" producedBy="smartworks"><fmt:message key="report.title.default_reports"/></a></span> | 
							<span class="disabled js_view_report_list"><a href="" producedBy='user'><fmt:message key="report.title.user_reports"/> <span class="t_red_bold js_user_report_count"> [<%=userReportCount %>]</span> </a></span> 
						</div>					
						<div class="title_line_btns">
							<div class="icon_btn_start">
								<a href="work_report_edit.sw" class="js_edit_work_report icon_btn_tail" targetWorkId="<%=targetWorkId%>"><fmt:message key="common.button.add_new_iwork"/></a>
							</div>
							<div class="icon_btn_start">
								<a href="" class="icon_btn_tail js_export_to_iwork_list_excel"><fmt:message key="common.button.excel_export"/></a>
							</div>
							<iframe class='js_excel_export' style='visibility: hidden;' src='' width='1' height='1'></iframe>
						</div>
					
						<div class="title_line_options">
							<form name="frmSearchInstance" class="po_left" <%if(Report.PRODUCED_BY_SMARTWORKS.equals(producedBy)){ %>style="display:none"<%} %>> 
								<div class="srch_wh srch_wsize">
									<input name="txtSearchInstance" class="nav_input" value="<%=CommonUtil.toNotNull(searchKey) %>" type="text" placeholder="<fmt:message key='search.search_instance' />">
									<button title="<fmt:message key='search.search_instance'/>" onclick="selectListParam($('.js_work_list_title').find('.js_progress_span:first'), false);return false;"></button>
								</div>
							</form>
							<span class="js_progress_span"></span>
						</div>
					</div>
					<!-- 목록보기 타이틀-->

					<!-- 새보고서등록하기 화면 -->
					<div class="js_work_report_edit"></div>
					<div class="js_work_report_view border_no_topline" style="display:none"></div>
					<!-- 새보고서등록하기 화면 -->

					<!-- 목록 테이블 -->
					<div class="list_contents">
						<div id='report_instance_list_page' >
 							<jsp:include page="/jsp/content/work/list/report_instance_list.jsp">
								<jsp:param value="<%=targetWorkId%>" name="targetWorkId"/>
								<jsp:param value="<%=Work.TYPE_NONE%>" name="targetWorkType"/>
								<jsp:param value="<%=producedBy%>" name="producedBy"/>
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
