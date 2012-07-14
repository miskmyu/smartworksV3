<%@page import="net.smartworks.model.instance.info.RequestParams"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.server.engine.process.xpdl.xpdl2.Category"%>
<%@page import="net.smartworks.model.work.info.FileCategoryInfo"%>
<%@page import="net.smartworks.model.work.FileCategory"%>
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
		var url = "set_file_list_params.sw";
		$.ajax({
			url : url,
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				$('#file_instance_list_page').html(data);
				smartPop.closeProgress();
			},
			error : function(xhr, ajaxOptions, thrownError) {
				smartPop.closeProgress();
				smartPop.showInfo(smartPop.ERROR, smartMessage.get('workListError'));
			}
		});
	};
	
	selectListParam = function(progressSpan, isGray){
		var fileList = $('.js_file_list_page');
		var forms = fileList.find('form:visible');
		var displayType = fileList.attr("displayType");
		var categoryId = fileList.attr("categoryId");
		var paramsJson = {};
		paramsJson["displayType"] = displayType;
		paramsJson["categoryId"] = categoryId;
		paramsJson["href"] = "jsp/content/work/list/file_instance_list.jsp?displayType=" + displayType;
		var searchFilters = fileList.find('form[name="frmSearchFilter"]');
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
		if(isEmpty(progressSpan)) progressSpan = fileList.find('.js_file_list_header span.js_progress_span');
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
	int displayType = (SmartUtil.isBlankObject(wid)) ? FileCategory.DISPLAY_ALL : FileCategory.DISPLAY_BY_CATEGORY;

	FileCategoryInfo[] categories = smartWorks.getFileCategoriesByType(displayType, wid, "");
	
	session.setAttribute("cid", cid);
	session.setAttribute("wid", wid);
	session.setAttribute("lastLocation", "file_list.sw");
	session.setAttribute("workSpace", workSpace);
	
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

		
<jsp:include page="/jsp/content/community/space/space_title.jsp"></jsp:include>

<jsp:include page="/jsp/content/upload/select_upload_action.jsp"></jsp:include>

<!-- 컨텐츠 레이아웃-->
<div class="section_portlet js_file_list_page" workId="<%=SmartWork.ID_FILE_MANAGEMENT %>" displayType="<%=displayType%>" workSpaceId="<%=wid%>" categoryId="<%=FileCategory.ID_ALL_FILES%>">
	<div class="portlet_t"><div class="portlet_tl"></div></div>
	<div class="portlet_l" style="display: block;">
		<ul class="portlet_r" style="display: block;">
		
			<!-- 왼쪽 폴더영역이  접혔을때 class:lft_fd를 추가 , 펼쳤을때는 class:lft_fd를 지움 --> 
			<div class="contents_space oh lft_fd"> 
				<!-- 컨텐츠 상단 영역 -->
				<div class="js_file_list_header" style="height:25px">
					<!-- 폴더구분  -->
					<select class="js_file_display_by fl">
						<option value=<%=FileCategory.DISPLAY_BY_CATEGORY %>><fmt:message key="space.title.by_category"/></option>
						<option value=<%=FileCategory.DISPLAY_BY_WORK %>><fmt:message key="space.title.by_work"/></option>
						<option value=<%=FileCategory.DISPLAY_BY_YEAR %>><fmt:message key="space.title.by_year"/></option>
						<option value=<%=FileCategory.DISPLAY_BY_OWNER %>><fmt:message key="space.title.by_owner"/></option>
						<option value=<%=FileCategory.DISPLAY_BY_FILE_TYPE %>><fmt:message key="space.title.by_filetype"/></option>
					</select>
					<select class="js_file_category_list fl">
						<%
						if(!SmartUtil.isBlankObject(categories)){
							for(int i=0; i<categories.length; i++){
								FileCategoryInfo category = categories[i];
						%>
								<option value=<%=category.getId() %>><%=category.getName() %></option>
						<%								
							}
						}
						%>
					</select>
					<span class="js_progress_span fl"></span>
					<!-- 폴더 구분//-->
					<!-- 우측 검색영역 -->
					<div class="list_title_space js_work_list_title">
						<div class="title_line_options fr">
							<form name="frmSearchInstance" class="po_left">
								<div class="srch_wh srch_wsize m0">
									<input name="txtSearchInstance" class="nav_input" type="text" placeholder="<fmt:message key='search.search_instance' />">
									<button title="<fmt:message key='search.search_instance'/>" onclick="selectListParam($('.js_work_list_title').find('.js_progress_span:first'), false);return false;"></button>
								</div>
							</form>					
						</div>
					</div>
					<!-- 우측 검색영역//-->
				</div>
				<!-- 컨텐츠 상단 영역 //-->
				<!-- 접기/열기 버튼 -->
				<div class="btn_fold"><a href="" class="js_file_category_tree"></a></div>
				<!-- 접기/열기 버튼 -->		
				<%
				if(displayType!=FileCategory.DISPLAY_ALL){
				%>          
					<!-- Left -->
					<div class="left_fold_area">

						<!-- 카테고리 -->
						<div class="pop_list_area file_fd">
							<!-- 우측 버튼영역 -->
							<div class="tab_buttons js_add_file_folder_btn"><a href=""><span class="btn_bfolder_add"></span></a></div>
							<!-- 우측 버튼영역 -->

							<ul class="js_file_categories">
								<jsp:include page="/jsp/content/work/list/categories_by_type.jsp">
									<jsp:param value="<%=displayType%>" name="displayType"/>
									<jsp:param value="<%=wid%>" name="wid"/>
									<jsp:param value="" name="parentId"/>
								</jsp:include>
							</ul>
						</div>
						<!-- 카테고리 //-->
					</div>
					<!-- Left//-->
					<!-- Right -->	
					<div class="right_content_area">
					<!-- 목록보기 -->
						<!-- 목록 테이블 -->
						<div class="list_contents">
							<div id="file_instance_list_page">
								<jsp:include page="/jsp/content/work/list/file_instance_list.jsp">
									<jsp:param value="<%=FileCategory.DISPLAY_BY_CATEGORY %>" name="displayType"/>
									<jsp:param value="<%=wid %>" name="workSpaceId"/>
								</jsp:include>
							</div>
						</div>
						<!-- 목록 테이블 //-->
					</div>
					<!-- 목록보기 -->
				<%
				}else{
					InformationWork work = (InformationWork)smartWorks.getWorkById(SmartWork.ID_FILE_MANAGEMENT);
					session.setAttribute("smartWork", work);
					session.removeAttribute("workInstance");
				%>
					<div>
						<jsp:include page="/jsp/content/work/report/work_report.jsp">
							<jsp:param value="<%=work.getLastReportId() %>" name="reportId"/>
						</jsp:include>
					</div>

					<!-- Right -->	
					<div>
					<!-- 목록보기 -->

						<!-- 상세필터 및 새업무등록하기 화면 -->
						<div id="search_filter" class="filter_section js_new_work_form"></div>
						<!-- 상세필터 -->
							
						<!-- 목록 테이블 -->
						<div class="list_contents">
							<div id="file_instance_list_page">
								<jsp:include page="/jsp/content/work/list/file_instance_list.jsp">
									<jsp:param value="<%=FileCategory.DISPLAY_ALL %>" name="displayType"/>
								</jsp:include>
							</div>
						</div>
						<!-- 목록 테이블 //-->
					</div>
					<!-- 목록보기 -->
				<%
				}
				%>
			</div>
		</ul>
	</div>
	<div class="portlet_b" style="display: block;"></div>
</div>
<!-- 컨텐츠 레이아웃//-->
