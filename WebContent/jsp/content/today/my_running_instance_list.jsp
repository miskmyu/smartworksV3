
<!-- Name 			: my_running_instance_list.jsp										 -->
<!-- Description	: 현재사용자에게 할당된 업무나, 현재사용자가 시작한 진행중인 업무들을 보여주는 화면 -->
<!-- Author			: Maninsoft, Inc.													 -->
<!-- Created Date	: 2011.9.															 -->

<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.model.filter.info.SearchFilterInfo"%>
<%@page import="net.smartworks.model.filter.SearchFilter"%>
<%@page import="net.smartworks.model.instance.RunningCounts"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>

<script type="text/javascript">
	getIntanceList = function(paramsJson, progressSpan, isGray){
		if(isEmpty(progressSpan))
			progressSpan = $('.js_running_instance_title').find('.js_progress_span:first');
		if(isGray)
			smartPop.progressContGray(progressSpan);
		else
			smartPop.progressCont(progressSpan);
		var url = "set_instance_list_params.sw";
		$.ajax({
			url : url,
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				$('.js_instance_list_table').html(data);
				smartPop.closeProgress();
			},
			error : function(xhr, ajaxOptions, thrownError) {
				smartPop.closeProgress();
				smartPop.showInfo(smartPop.ERROR, smartMessage.get('workListError'));
			}
		});
	};
	
	selectListParam = function(progressSpan, isGray){
		var runningInstanceList = $('.js_my_running_instance_list_page');
		var forms = runningInstanceList.find('form:visible');
		var paramsJson = {};
		for(var i=0; i<forms.length; i++){
			var form = $(forms[i]);
			if(form.attr('name') !== "frmSearchFilter" && form.attr('name') === "frmSearchInstance"){
				paramsJson[form.attr('name')] = mergeObjects(form.serializeObject(), SmartWorks.GridLayout.serializeObject(form));
			}
		}
		var searchType = $('.js_instance_counts').find('.current').attr('searchType');//class에 current 포함된놈이 누구냐
		var isAssignedOnly;
		if (searchType === 'my_running_instances') {
			isAssignedOnly = false;
		} else if (searchType === 'assigned_instances') {
			isAssignedOnly = true;
		}
		paramsJson["href"] = "/jsp/content/today/more_instance_list.jsp?assignedOnly=" + isAssignedOnly ;
		getIntanceList(paramsJson, progressSpan, isGray);		
	};
</script>

<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 현재사용자 정보도 가져온다 
 	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();
 
	// 현재사용자의 진행중인 업무들의 갯수(전체업무, 할당업무)를 가져온다.
	RunningCounts runningCounts = smartWorks.getMyRunningInstancesCounts();
	if(SmartUtil.isBlankObject(runningCounts)) runningCounts = new RunningCounts();
	
	String selectedFilterId = "";
%>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!-- 나의 진행중인 업무 -->
<div class="section_portlet js_my_running_instance_list_page">
	<div class="portlet_t"><div class="portlet_tl"></div></div>
	<div class="portlet_l" style="display: block;">
		<ul class="portlet_r" style="display: block;">
			<div id="work_ing">		
				<div class="titl_section">
				
					<!-- 타이틀을 나타내는 곳 -->
					<div class="title pr10 fl js_running_instance_title js_instance_count">
						<span class="js_view_my_instances"><a href="" viewType="smartcaster_instances"><fmt:message key="header.top_menu.smartcaster"/></a></span> | 
						<span class="current js_view_my_instances"><a href="" viewType='assigned_instances'><fmt:message key="content.my_running_assigned_count"/> <span class="t_red_bold js_assigned_count"> [<%=runningCounts.getAssignedOnly() %>]</span> </a></span> | 
						<span class="js_view_my_instances"><a href="" viewType='running_instances'><fmt:message key="content.my_running_instance_count"/> <span class="t_red_bold js_running_count"> [<%=runningCounts.getRunningOnly() %>]</span> </a></span>
					</div>					
					<!-- 타이틀을 나타내는 곳 // -->
					
<%-- 					<form name="frmSearchInstance" class="fr js_search_running_instance">
						<div class="srch_wh srch_wsize">
							<input name="txtSearchInstance" class="nav_input" onkeydown="if(event.keyCode == 13){ $(this).next().click();return false;}" type="text" title="<fmt:message key="search.search_running_instance"/>"
								placeholder="<fmt:message key="search.search_running_instance"/>">
							<button title="<fmt:message key='search.search'/>" onclick="selectListParam($('.js_running_instance_title').find('.js_progress_span:first'), false);return false;"></button>
						</div>
					</form> --%>
<%-- 					<form class="form_space po_left js_form_filter_name" name="frmHomeFilterName">
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
							SearchFilterInfo[] filters = runningCounts.getCasterFilters();
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
					<a href="search_filter.sw?workId=smartcaster" class="js_edit_search_filter" title="<fmt:message key='filter.button.edit_search_filter' />">
						<div class="icon_btn_edit"></div>
					</a>					
 --%>					<span class="js_progress_span"></span>
					
				</div>
				
				<!-- 진행중인 업무목록 및 더보기 버튼 -->
				<div class="solid_line"></div>
				<div class="js_instance_list_table">
					<jsp:include page="/jsp/content/today/more_instance_list.jsp">
						<jsp:param value="true" name="assignedOnly"/>
					</jsp:include>
				</div>
			</div>
		</ul>
	</div>
	<div class="portlet_b" style="display: block;"></div>
</div>
<!-- 나의 진행중인 업무 //-->
