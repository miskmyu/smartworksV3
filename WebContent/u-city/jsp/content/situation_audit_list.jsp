<%@page import="pro.ucity.model.Audit"%>
<%@page import="pro.ucity.model.System"%>
<%@page import="pro.ucity.util.UcityTest"%>
<%@page import="net.smartworks.model.instance.info.RequestParams"%>
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
				$('#pwork_instance_list_page').html(data);
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
		var url = "set_work_search_filter.sw";
		if(isEmpty(filterId)){
			url = "create_work_search_filter.sw";
			searchFilter.find('input[name="txtNewFilterName"]').addClass('required');
		}

		if (!SmartWorks.GridLayout.validate(searchFilter.find('form.js_validation_required'), $('.js_filter_error_message'))) return;

		var paramsJson = {};
		var workId = pworkList.attr('workId');
		var searchFilters = searchFilter.find('form[name="frmSearchFilter"]');
		paramsJson['workId'] = workId;
		paramsJson['workType'] = <%=SmartWork.TYPE_PROCESS%>;
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
		var progressSpan = searchFilter.find('span.js_progress_span:first');
		smartPop.progressCont(progressSpan);
		$.ajax({
			url : url,
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				var selectSearchFilter = pworkList.find('.js_select_search_filter');
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
		if(isEmpty(filterId) || filterId.match(".*system.*")){
			searchFilter.find('input[name="txtNewFilterName"]').removeClass('required');
			saveAsSearchFilter("");
		}else{
			saveAsSearchFilter(filterId);
		}
	};

	selectListParam = function(progressSpan, isGray){
		var pworkList = $('.js_pwork_list_page');
		var forms = pworkList.find('form:visible');
		var paramsJson = {};
		var workId = pworkList.attr('workId');
		paramsJson["href"] = "u-city/jsp/content/situation_instance_list.jsp?workId=" + workId;
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
					searchFilterArray.push(searchFilter.serializeObject());
			}
			paramsJson['frmSearchFilters'] = searchFilterArray;
		}
		if(isEmpty(progressSpan)) progressSpan = pworkList.find('.js_search_filter_page').next('span.js_progress_span:first');
		getIntanceList(paramsJson, progressSpan, isGray);		
	};
</script>
<%
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks"); 
/* 	String cid = request.getParameter("cid");
	String wid = request.getParameter("wid");

	session.setAttribute("cid", cid);
	session.setAttribute("wid", wid);
	String workId = SmartUtil.getSpaceIdFromContentContext(cid);
 */
 	session.setAttribute("lastLocation", "situation_list.sw");

 	String auditId = (String)session.getAttribute("auditId");
 	if(SmartUtil.isBlankObject(auditId)) auditId = Audit.DEFAULT_AUDIT_ID_STR;
 	
 	int[][] auditTasks = smartWorks.getUcityAuditTaskCounts(true);
 	
	String workId = System.getProcessId(System.PROCESS_ENV_WEAHTER);
	User cUser = SmartUtil.getCurrentUser();
	ProcessWork work = (ProcessWork) smartWorks.getWorkById(workId);
	String selectedFilterId = SearchFilter.FILTER_ALL_INSTANCES;
	RequestParams params = (RequestParams)request.getAttribute("requestParams");
	if (params == null){
		String savedWorkId = (String)session.getAttribute("workId");
		if(!SmartUtil.isBlankObject(workId) && workId.equals(work.getId())){
			params = (RequestParams)session.getAttribute("requestParams");
		}
	}if (params != null){
		selectedFilterId = params.getFilterId();
	}
	
	session.setAttribute("smartWork", work);
	session.removeAttribute("workInstance");
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!-- 컨텐츠 레이아웃-->
<div class="section_portlet js_pwork_list_page js_work_list_page">

			<!-- 목록보기 -->
			<div class=" contents_space">

				<div class="title_list">상황 이벤트 감시					
					<!-- 우측 영역 -->
					<div class="fr txt_btn js_work_report_list_box" style="position: relative; top: 5px;">
					</div>			
					<!-- 우측 영역 //-->
				</div>
				
				<!-- 테이블 -->
				<div class="table_border">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<th rowspan="2"  style="text-align:center">상황발생</th>
							<th rowspan="2">통신 미들웨어</th>
							<th colspan="3">운영포털</th>
							<th rowspan="2">통합관제</th>
							<th rowspan="2">단말연계 미들웨어</th>
						</tr>
						<tr>
							<th>접수</th>
							<th>처리</th>
							<th>종료</th>
						</tr>
						<tr>
							<td>3</td>
							<td>0</td>
							<td>0</td>
							<td>5</td>
							<td>0</td>
							<td>0</td>
							<td>7</td>
						</tr>
					</table>
				</div>
				<!-- 테이블 //-->
				
				<!-- 목록보기 타이틀-->
				<div class="list_title_space js_work_list_title mt20">
					<div class="title_line_options">
					</div>
				</div>
				<!-- 목록보기 타이틀-->

				<!-- 목록 테이블 -->
 				<div class="list_contents">
					<div id='pwork_instance_list_page'>
						<jsp:include page="/u-city/jsp/content/situation_instance_list.jsp">
							<jsp:param value="<%=workId%>" name="workId"/>
							<jsp:param value="<%=auditId%>" name="auditId"/>							
						</jsp:include>
					</div>
				</div>
				<!-- 목록 테이블 //-->

				<!-- 목록보기 -->
			</div>

</div>
<!-- 컨텐츠 레이아웃//-->
