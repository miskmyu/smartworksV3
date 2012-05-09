<%@page import="net.smartworks.model.work.MailWork"%>
<%@page import="net.smartworks.model.mail.MailFolder"%>
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
				smartPop.showInfo(smartPop.ERROR, "새로운 항목 생성중에 이상이 발생하였습니다.");
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
		var input = $(targetElement(e)).parents('a.js_select_paging');
		input.find('input').attr('value', 'true');
		selectListParam();
		return false;
	});
	
	$('a.js_select_current_page').live("click", function(e){
		var input = $(targetElement(e));
		input.siblings('input[name="hdnCurrentPage"]').attr('value', input.text());
		selectListParam();
		return false;
	});
	
	$('a.js_select_field_sorting').live('click', function(e){
		var input = $(targetElement(e));
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
		var input = $(targetElement(e)).parents('a.js_search_filter_execute');
		selectListParam();
		return false;
	});	
</script>
<%
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	String cid = request.getParameter("cid");
	String wid = request.getParameter("wid");

	String folderId = SmartUtil.getSpaceIdFromContentContext(cid);
	User cUser = SmartUtil.getCurrentUser();
	MailWork work = new MailWork(folderId, MailFolder.getFolderNameById(folderId), "");
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!-- 컨텐츠 레이아웃-->
<div class="section_portlet">
	<div class="portlet_t"><div class="portlet_tl"></div></div>
	<div class="portlet_l" style="display: block;">
		<ul class="portlet_r" style="display: block;">

			<!-- 타이틀 -->
			<div class="body_titl" style="overflow: inherit">
				<div class="body_titl_area ti_mail title">
					<div class="title myspace_h">받은 메일함
						<span class="t_mail"><span class="t_s11">안읽음</span><span class="new_mail">7</span><span class="bar"> / </span>255</span><span class=" t_s11"> 통</span>
					</div>
				</div>

				<!-- 메일 검색 -->
				<div class="mail_srch">
					<div class="srch_wh srch_wsize_mail">
						<input id="" class="nav_input" type="text" href="" placeholder="메일 검색" title="메일 검색">
						<button onclick="" title="검색"></button>
					</div>
				</div>
				<!-- 메일 검색//-->


				<div class="solid_line cb"></div>
			</div>
			<!-- 타이틀 //-->

			<!-- 컨텐츠 -->
			<div class="contents_space">
				<div class="buttonSet">
					<button>전체선택<span class="icon_in_down"><a href=""></a></span></button>
					<button><span class="icon_mail_delet"></span>삭제</button>
					<button>스팸신고</button>
					<button>답장</button>
					<button>전달</button>
					<button>이동<span class="icon_in_down"><a href=""> </a></span></button>
					<button class="fr t_bold"><span class="icon_mail_write"></span>새 메일쓰기</button>
				</div>

				<!-- 메일 리스트-->
				<div class="list_contents mail_list_section">
					<div id='mail_list_page' >
						<jsp:include page="/jsp/content/work/list/mail_instance_list.jsp"></jsp:include>
					</div>
				</div>
				<!-- 메일 리스트//-->
			</div>
			<!-- 컨텐츠 //-->

		</ul>
	</div>
	<div class="portlet_b" style="display: block;"></div>
</div>
<!-- 컨텐츠 레이아웃//-->
