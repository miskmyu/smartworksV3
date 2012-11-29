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
	
	selectListParam = function(progressSpan, isGray){
		var pworkList = $('.js_pwork_list_page');
		var forms = pworkList.find('form:visible');
		var paramsJson = {};
		var auditId = pworkList.attr('auditId');
		var workId = pworkList.attr('workId');
		paramsJson["href"] = "u-city/jsp/content/situation_instance_list.jsp?workId=" + workId + "&auditId=" + auditId;
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
 	session.setAttribute("lastLocation", "situationAudit.sw");

 	String auditId = (String)request.getParameter("auditId");
 	if(SmartUtil.isBlankObject(auditId)) auditId = (String)session.getAttribute("auditId");
 	if(SmartUtil.isBlankObject(auditId)) auditId = Audit.DEFAULT_AUDIT_ID_STR;
 	int auditNumber = Integer.parseInt(auditId);
 	
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
			if(!SmartUtil.isBlankObject(params))
				params.setSearchFilter(null);
		}
	}if (params != null){
		selectedFilterId = params.getFilterId();
		params.setSearchFilter(null);
	}
	
	session.setAttribute("auditId", auditId);
	session.setAttribute("smartWork", work);
	session.removeAttribute("workInstance");
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!-- 컨텐츠 레이아웃-->
<div class="section_portlet js_pwork_list_page js_work_list_page" auditId="<%=auditId%>">

			<!-- 목록보기 -->
			<div class=" contents_space">
				
				<div class="list_title_space bn">
					<div class="title audit mb2">상황 이벤트 감시</div>
					
					<!-- 우측 영역 -->
					<div class="fr txt_btn js_work_report_list_box" style="position: relative; top: 5px;">
					</div>			
					<!-- 우측 영역 //-->
					
				</div>
				
				<!-- 테이블 -->
				<div class="table_border">

					<table width="930" border="0" cellspacing="0" cellpadding="0">
					<colgroup>
						<col width="<%=930/7 %>px">
						<col width="<%=930/7 %>px">
						<col width="<%=930/7 %>px">
						<col width="<%=930/7 %>px">
						<col width="<%=930/7 %>px">
						<col width="<%=930/7 %>px">
						<col width="<%=930/7 %>px">
					</colgroup>
					<tbody>
						<tr>
							<th rowspan="2" <%if(auditNumber==Audit.ID_SITUATION_OCCURRED) {%>class="current"<%} %>>
								<a href="situationAudit.sw?auditId=<%=Audit.ID_SITUATION_OCCURRED %>" auditId="<%=Audit.ID_SITUATION_OCCURRED%>"><%=Audit.getAuditNameById(Audit.ID_SITUATION_OCCURRED) %></a>
							</th>
							<th rowspan="2" <%if(auditNumber==Audit.ID_COMMUNICATION_MW) {%>class="current"<%} %>>
								<a href="situationAudit.sw?auditId=<%=Audit.ID_COMMUNICATION_MW %>" auditId="<%=Audit.ID_COMMUNICATION_MW%>"><%=Audit.getAuditNameById(Audit.ID_COMMUNICATION_MW) %></a>
							</th>
							<th colspan="3">운영포털</th>
							<th rowspan="2" <%if(auditNumber==Audit.ID_DEVICE_MW) {%>class="current"<%} %>>
								<a href="situationAudit.sw?auditId=<%=Audit.ID_DEVICE_MW %>" auditId="<%=Audit.ID_DEVICE_MW%>"><%=Audit.getAuditNameById(Audit.ID_DEVICE_MW) %></a>
							</th>
							<th rowspan="2" <%if(auditNumber==Audit.ID_SITUATION_DISPLAY) {%>class="current"<%} %>>
								<a href="situationAudit.sw?auditId=<%=Audit.ID_SITUATION_DISPLAY %>" auditId="<%=Audit.ID_SITUATION_DISPLAY%>"><%=Audit.getAuditNameById(Audit.ID_SITUATION_DISPLAY) %></a>
							</th>
						</tr>
						<tr>
							<th <%if(auditNumber==Audit.ID_PORTAL_ACCEPTED) {%>class="current"<%} %>>
								<a href="situationAudit.sw?auditId=<%=Audit.ID_PORTAL_ACCEPTED %>" auditId="<%=Audit.ID_PORTAL_ACCEPTED%>">접수</a>
							</th>
							<th <%if(auditNumber==Audit.ID_PORTAL_PROCESSING) {%>class="current"<%} %>>
								<a href="situationAudit.sw?auditId=<%=Audit.ID_PORTAL_PROCESSING %>" auditId="<%=Audit.ID_PORTAL_PROCESSING%>">처리</a>
							</th>
							<th <%if(auditNumber==Audit.ID_PORTAL_RELEASE) {%>class="current"<%} %>>
								<a href="situationAudit.sw?auditId=<%=Audit.ID_PORTAL_RELEASE %>" auditId="<%=Audit.ID_PORTAL_RELEASE%>">종료</a>
							</th>
						</tr>
						<tr>
							<td <%if(auditNumber==Audit.ID_SITUATION_OCCURRED) {%>class="current"<%} %>>
								<a href="situationAudit.sw?auditId=<%=Audit.ID_SITUATION_OCCURRED %>" auditId="<%=Audit.ID_SITUATION_OCCURRED%>"><%if(auditTasks!=null){ %><%=auditTasks[0][Audit.ID_SITUATION_OCCURRED] %><%}else{ %>0<%} %></a>
							</td>
							<td <%if(auditNumber==Audit.ID_COMMUNICATION_MW) {%>class="current"<%} %>>
								<a href="situationAudit.sw?auditId=<%=Audit.ID_COMMUNICATION_MW %>" auditId="<%=Audit.ID_COMMUNICATION_MW%>"><%if(auditTasks!=null){ %><%=auditTasks[0][Audit.ID_COMMUNICATION_MW] %><%}else{ %>0<%} %></a>
							</td>
							<td  <%if(auditNumber==Audit.ID_PORTAL_ACCEPTED) {%>class="current"<%} %>>
								<a href="situationAudit.sw?auditId=<%=Audit.ID_PORTAL_ACCEPTED %>" auditId="<%=Audit.ID_PORTAL_ACCEPTED%>"><%if(auditTasks!=null){ %><%=auditTasks[0][Audit.ID_PORTAL_ACCEPTED] %><%}else{ %>0<%} %></a>
							</td>
							<td <%if(auditNumber==Audit.ID_PORTAL_PROCESSING) {%>class="current"<%} %>>
								<a href="situationAudit.sw?auditId=<%=Audit.ID_PORTAL_PROCESSING %>" auditId="<%=Audit.ID_PORTAL_PROCESSING%>"><%if(auditTasks!=null){ %><%=auditTasks[0][Audit.ID_PORTAL_PROCESSING] %><%}else{ %>0<%} %></a>
							</td>
							<td <%if(auditNumber==Audit.ID_PORTAL_RELEASE) {%>class="current"<%} %>>
								<a href="situationAudit.sw?auditId=<%=Audit.ID_PORTAL_RELEASE %>" auditId="<%=Audit.ID_PORTAL_RELEASE%>"><%if(auditTasks!=null){ %><%=auditTasks[0][Audit.ID_PORTAL_RELEASE] %><%}else{ %>0<%} %></a>
							</td>
							<td <%if(auditNumber==Audit.ID_DEVICE_MW) {%>class="current"<%} %>>
								<a href="situationAudit.sw?auditId=<%=Audit.ID_DEVICE_MW %>" auditId="<%=Audit.ID_DEVICE_MW%>"><%if(auditTasks!=null){ %><%=auditTasks[0][Audit.ID_DEVICE_MW] %><%}else{ %>0<%} %></a>
							</td>
							<td <%if(auditNumber==Audit.ID_SITUATION_DISPLAY) {%>class="current"<%} %>>
								<a href="situationAudit.sw?auditId=<%=Audit.ID_SITUATION_DISPLAY %>" auditId="<%=Audit.ID_SITUATION_DISPLAY%>"><%if(auditTasks!=null){ %><%=auditTasks[0][Audit.ID_SITUATION_DISPLAY] %><%}else{ %>0<%} %></a>
							</td>
						</tr>
						</tbody>
					</table>
				</div>
				<!-- 테이블 //-->
				
				<!-- 목록보기 타이틀-->
  				<div class="sub_title01"><%=Audit.getAuditNameById(auditNumber) %><span></span></div>
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
