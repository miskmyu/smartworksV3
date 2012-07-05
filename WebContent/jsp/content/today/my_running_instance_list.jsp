
<!-- Name 			: my_running_instance_list.jsp										 -->
<!-- Description	: 현재사용자에게 할당된 업무나, 현재사용자가 시작한 진행중인 업무들을 보여주는 화면 -->
<!-- Author			: Maninsoft, Inc.													 -->
<!-- Created Date	: 2011.9.															 -->

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
				<div class="titl_section js_running_instance_title">
				
					<!-- 타이틀을 나타내는 곳 -->
					<%-- <div class="tit js_running_instance_title pr10"><fmt:message key="content.my_running_instance_list" /></div> --%>
					<div class="title js_running_instance_title pr10 fl">
						<span class="current js_view_smartcaster_instances"><a href="">스마트캐스터</a></span> | 
						<span class="js_view_assigned_instances"><a href="" searchType='assigned_instances'>처리할 업무 <span class="t_red_bold js_assigned_count">[<%=runningCounts.getAssignedOnly() %>]</span> </a></span> | 
						<span class="js_view_running_instances"><a href="" searchType='running_instances'>내가 시작한 업무 <span class="t_red_bold js_running_count">[<%=runningCounts.getAssignedOnly() %>]</span> </a></span>
					</div>					
					<!-- 타이틀을 나타내는 곳 // -->
					
					<form name="frmSearchInstance" class="fr js_search_running_instance">
						<div class="srch_wh srch_wsize">
							<input name="txtSearchInstance" class="nav_input" type="text" title="<fmt:message key="search.search_running_instance"/>"
								placeholder="<fmt:message key="search.search_running_instance"/>">
							<button title="<fmt:message key='search.search'/>" onclick="selectListParam($('.js_running_instance_title').find('.js_progress_span:first'), false);return false;"></button>
						</div>
					</form>
					<span class="js_progress_span"></span>
					
					<!-- 전체/할당업무만의 갯수와 선택버튼들 -->
					<%-- <div class="txt_btn fr js_instance_counts">
						<a href="" searchType='assigned_instances' class="current js_view_assigned_instances" instanceCount="<%=runningCounts.getAssignedOnly()%>"><fmt:message key="content.my_running_assigned_count"/> 
							<span class="t_red_bold js_assigned_count">[<%=runningCounts.getAssignedOnly() %>]</span>
						</a>
						 | 
						<a href="" searchType='my_running_instances' class="js_view_my_running_instances" instanceCount="<%=runningCounts.getTotal()%>"><fmt:message key="content.my_running_instance_count"/> 
							<span class="t_red_bold js_all_running_count">[<%=runningCounts.getTotal() %>]</span>
						</a>
					</div> --%>
					<!-- 전체/할당업무만의 갯수와 선택버튼들 //-->
					
				</div>
				
				<!-- 진행중인 업무목록 및 더보기 버튼 -->
				<div class="solid_line mb5"></div>
				<table class="js_instance_list_table">
					<jsp:include page="/jsp/content/today/more_instance_list.jsp">
						<jsp:param value="true" name="assignedOnly"/>
					</jsp:include>
				</table>
				<!-- 진행중인 업무목록 및 더보기 버튼 //-->
				<!-- 더보기 버튼 -->
				<%if(runningCounts.getTotal() > 20){ %>
					<div class="js_more_list">
						<a href="more_instance_list.sw"><fmt:message key="content.more_running_instance"/></a>
						<span class="js_progress_span"></span>
					</div>
				<%} %>
				<!-- 더보기 버튼 !!-->
	
			</div>
		</ul>
	</div>
	<div class="portlet_b" style="display: block;"></div>
</div>
<!-- 나의 진행중인 업무 //-->
