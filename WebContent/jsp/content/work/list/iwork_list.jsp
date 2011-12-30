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
	getIntanceList = function(paramsJson){
		console.log(JSON.stringify(paramsJson));
		var url = "iwork_instance_list.sw";
		$.ajax({
			url : url,
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				var target = $('#iwork_list_page');
				target.html(data);
			},
			error : function(e) {
				popShowInfo(swInfoType.ERROR, "새로운 항목 생성중에 이상이 발생하였습니다.");
			}
		});
	};
	
	selectListParam = function(){
		var forms = $('form:visible');
		var paramsJson = {};
		var searchFilters = $('form[name="frmSearchFilter"]');
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
				searchFilterArray.push(searchFilter.find(':visible').serializeObject());				
			}
			paramsJson['frmSearchFilters'] = searchFilterArray;
		}		
		console.log(JSON.stringify(paramsJson));		
	};
	
	$('a.js_select_paging').live("click", function(e){
		var input = $(e.target).parents('a.js_select_paging');
		input.find('input').attr('value', 'true');
		selectListParam();
		return false;
	});
	
	$('a.js_select_current_page').live("click", function(e){
		var input = $(e.target);
		input.siblings('input[name="hdnCurrentPage"]').attr('value', input.text());
		selectListParam();
		return false;
	});
	
	$('a.js_select_field_sorting').live('click', function(e){
		var input = $(e.target);
		var sortingField = $('form[name="frmSortingField"]').find('input[name="hdnSortingFieldId"]');
		var sortingIsAscending = $('form[name="frmSortingField"]').find('input[name="hdnSortingIsAscending"]');
		console.log("input...=", input, sortingField, sortingIsAscending);
		if(sortingField.attr('value') === input.attr('fieldId')){
			var isAscending = sortingIsAscending.attr('value');
			sortingIsAscending.attr('value', (isAscending === "true") ? "false" : "true");
		}else{
			sortingField.attr('value', input.attr('fieldId'));
			sortingIsAscending.attr('value', 'false');
		}
		selectListParam();
		return false;
	});

	$('a.js_search_filter_execute').live("click", function(e){
		var input = $(e.target).parents('a.js_search_filter_execute');
		selectListParam();
		return false;
	});	
</script>
<%
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	String cid = request.getParameter("cid");
	String wid = request.getParameter("wid");

	String workId = SmartUtil.getSpaceIdFromContentContext(cid);
	User cUser = SmartUtil.getCurrentUser();
	InformationWork work = (InformationWork) smartWorks.getWorkById(workId);
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!-- 컨텐츠 레이아웃-->
<div class="section_portlet">
	<div class="portlet_t">
		<div class="portlet_tl"></div>
	</div>
	<div class="portlet_l" style="display: block;">
		<ul class="portlet_r" style="display: block;">

			<!-- 타이틀 -->
			<div class="body_titl">
				<div class="body_titl_iworks title"><%=work.getName()%></div>

				<!-- 우측 버튼 -->
				<div class="txt_btn">

					<!-- 수정하기 -->
					<div class="float_right space_l5">
					<%
					if (cUser.getUserLevel() == User.USER_LEVEL_AMINISTRATOR) {
					%>
						<span class="btn_gray"> 
							<span class="Btn01Start"></span> 
							<span class="Btn01Center"><fmt:message key='common.button.modify' /> </span>
							<span class="Btn01End"></span>
						</span>
					<%
					}
					%>
					</div>
					<!-- 수정하기 //-->

					<!-- 최종수정자 -->
					<div class="float_right">
						<img class="pho_user" title="<fmt:message key="common.title.last_modifier" />"
							src="<%=work.getLastModifier().getMinPicture()%>"> 
						<span class="t_name"><%=work.getLastModifier().getLongName()%></span>
						<span class="t_date"><%=work.getLastModifiedDate().toLocalString()%></span>
					</div>
					<!-- 최종수정자 //-->

				</div>
				<!-- 우측 버튼 //-->

				<div class="solid_line"></div>
			</div>
			<!-- 타이틀 -->

			<!-- 컨텐츠 -->
			<div class="contents_space js_content_div">

				<!-- 업무 정의 영역 -->
				<div class="">
					<%if(!SmartUtil.isBlankObject(work.getDesc())) {%><%=work.getDesc()%>
					<%}else{ %><fmt:message key="common.message.no_work_desc" />
					<%} %>
				</div>
				<!-- 업무 정의 영역 //-->

				<!-- 버튼 영역-->
				<div class="txt_btn solid_line_sb margin_t15">
					<span class="po_left bu_work_explan"> 
						<a class="js_view_work_manual" href=""><fmt:message key="common.button.view.work_manual" />▼</a>
						<a style="display: none" class="js_view_work_manual" href=""><fmt:message key="common.button.close.work_manual" />▼</a>
					</span> 
					<span class="po_left">
						<%if (work.getManualFileName() != null) {%>
							<a href="" class="bu_video space_r2" title="<fmt:message key='work.title.manual_file'/>"></a> 
						<%
						}
 						if (work.getHelpUrl() != null) {
 						%> 
 							<a href="<%=work.getHelpUrl()%>" class="bu_webex" title="<fmt:message key='work.title.help_url'/>" target="_blank"></a>
 						<%
 						}
 						%>
 					</span>

					<!-- 우측 권한 아이콘-->
					<span> 
						<%
 						switch (work.getAccessPolicy().getLevel()) {
 						case AccessPolicy.LEVEL_PUBLIC:
 						%>
							<div class="ch_right"><fmt:message key="common.security.access.public" /></div>
						<%
 							break;
 						case AccessPolicy.LEVEL_PRIVATE:
 						%>
							<div class="ch_right"><fmt:message key="common.security.access.private" /></div> 
						<%
 							break;
 						case AccessPolicy.LEVEL_CUSTOM:
 						%>
							<div class="ch_right"><fmt:message key="common.security.access.custom" /></div> 
						<%
 							break;
 						}
 						%>
						<div class="po_right"><span class="bu_read" title="<fmt:message key='common.security.title.access'/>"></span></div>
						<%
 						switch (work.getWritePolicy().getLevel()) {
 						case WritePolicy.LEVEL_PUBLIC:
 						%>
							<div class="ch_right"><fmt:message key="common.security.write.public" /></div> 
						<%
 							break;
 						case WritePolicy.LEVEL_CUSTOM:
 						%>
							<div class="ch_right"><fmt:message key="common.security.write.custom" /></div> 
						<%
 							break;
 						}
 						%>
						<div class="po_right"><span class="bu_regit"  title="<fmt:message key='common.security.title.write'/>"></span></div> 
						<%
 						switch (work.getEditPolicy().getLevel()) {
 						case EditPolicy.LEVEL_WIKI:
						 %>
							<div class="ch_right"><fmt:message key="common.security.edit.wiki" /></div> 
						<%
 							break;
 						case EditPolicy.LEVEL_BLOG:
 						%>
							<div class="ch_right"><fmt:message key="common.security.edit.blog" /></div> 
						<%
						 	break;
						 }
						 %>
						<div class="po_right"><span class="bu_modfy"  title="<fmt:message key='common.security.title.edit'/>"></span></div> </span>
					<!-- 우측 권한 아이콘-->

				</div>
				<!-- 버튼 영역 //-->

			</div>
			<div id="work_manual" style="display: none">
				<jsp:include page="/jsp/content/work/list/iwork_manual.jsp"></jsp:include>
			</div>

			<!-- 목록영역  -->
			<div class="contents_space">

				<!--통계메뉴 영역-->
				<div class="txt_btn margin_b5 margin_t10 js_work_report">

					<div class="po_right">
						<a href="work_report.sw?workId=<%=work.getId()%>"
							class="js_new_work_report"><fmt:message key="report.button.new_work_report"/></a>
					</div>
					<div class="po_right bu_stat">
						<select name="selMyReportList" class="js_select_work_report"
							href="work_report_view.sw?workId=<%=workId%>&workType=<%=work.getType()%>">
							<%
							Report[] infoReports = ChartReport.DEFAULT_CHARTS_INFORMATION;
							if (infoReports != null) {
								for (Report report : infoReports) {
									String chartType = null;
									if(report.getType() == Report.TYPE_CHART) chartType = ((ChartReport)report).getChartTypeInString();
							%>
							<option value="<%=report.getId()%>" reportType="<%=report.getType()%>" <%if(chartType!=null){ %>chartType="<%=chartType%>"<%}%>>
								<fmt:message key="<%=report.getName()%>" />
							</option>
							<%
								}
							}
							ReportInfo[] reports = work.getReports();
							if (reports != null) {
								for (ReportInfo report : reports) {
									String chartType = report.getChartTypeInString();
							%>
							<option value="<%=report.getId()%>" reportType="<%=report.getType()%>" <%if(chartType!=null){ %>chartType="<%=chartType%>"<%}%>><%=report.getName()%></option>
							<%
								}
							}
							%>
						</select>
					</div>
				</div>
				<!--통계메뉴 영역//-->
				<div class="js_work_report_form margin_b5"></div>

				<!-- 목록보기 -->
				<div>

					<!-- 목록보기 타이틀-->
					<div class="list_title_space js_work_list_title">
						<div class="txt_btn posi_ab">
							<div class="title"><fmt:message key="common.title.instance_list" /></div>
							<form name="frmSearchInstance" class="po_left">
								<div class="srch_wh srch_wsize">
									<input name="txtSearchInstance" class="nav_input" type="text" placeholder="<fmt:message key='search.search_instance' />">
 									<button title="<fmt:message key='search.search_instance'/>" onclick="selectListParam();return false;"></button>
								</div>
							</form>

							<div class="po_left">
								<form class="form_space" name="frmIworkFilterName">
									<select name="selFilterName" onchange="$('#search_filter').slideUp(500).html('');$('a.js_search_filter').show();selectListParam();
									return false;">
										<option value="<%=SearchFilter.FILTER_ALL_INSTANCES%>" selected><fmt:message key='filter.name.all_instances' /></option>
										<option value="<%=SearchFilter.FILTER_MY_INSTANCES%>"><fmt:message key='filter.name.my_instances' /></option>
										<option value="<%=SearchFilter.FILTER_RECENT_INSTANCES%>"><fmt:message key='filter.name.recent_instances' /></option>
										<option value="<%=SearchFilter.FILTER_MY_RECENT_INSTANCES%>"><fmt:message key='filter.name.my_recent_instances' /></option>
										<%
										SearchFilterInfo[] filters = work.getSearchFilters();
										if (filters != null) {
											for (SearchFilterInfo filter : filters) {
										%>
											<option value="<%=filter.getId()%>"><%=filter.getName()%></option>
										<%
											}
										}
										%>
									</select>
								</form>
							</div>
							<a href="search_filter.sw?workId=<%=workId%>" class="js_search_filter">
								<div class="po_left"><fmt:message key='filter.button.search_filter' /></div>
							</a>
						</div>

						<div class="txt_btn">
							<div class="po_right"><a href="">엑셀로 등록하기</a></div>
							<div class="po_right">
								<a href="new_iwork.sw?workId=<%=workId%>" class="js_create_new_work" workId="<%=workId%>">새항목 등록하기</a>
							</div>
						</div>
					</div>
					<!-- 목록보기 타이틀-->

					<!-- 상세필터 -->
					<div id="search_filter" class="filter_section js_new_work_form"></div>
					<!-- 상세필터 -->

					<!-- 목록 테이블 -->
					<div class="list_contents">
						<div id='iwork_list_page' >
							<jsp:include page="/jsp/content/work/list/iwork_instance_list.jsp"></jsp:include>
						</div>

						<!-- 목록 테이블 //-->

					</div>
					<!-- 목록영역 // -->
		</ul>
	</div>
	<div class="portlet_b" style="display: block;"></div>
</div>
<!-- 컨텐츠 레이아웃//-->
