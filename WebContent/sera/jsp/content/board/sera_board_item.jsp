<%@page import="net.smartworks.model.security.AccessPolicy"%>
<%@page import="net.smartworks.model.work.SmartWork"%>
<%@page import="net.smartworks.model.sera.Constants"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.WorkSpace"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<script type="text/javascript">

	//완료버튼 클릭시 create_new_board.sw 서비스를 실행하기 위해 submit하는 스크립트..
	function submitForms() {
	
		var newSeraBoard = $('.js_sera_board_item_page');
	
		// new_board 에 있는 활성화되어 있는 모든 입력화면들을 validation하여 이상이 없으면 submit를 진행한다...
		if(!SmartWorks.GridLayout.validate(newSeraBoard.find('form.js_validation_required'), $('.js_upload_error_message'))) return
	
		// 그려진 화면에 있는 입력화면들을 JSON형식으로 Serialize한다...
		var forms = newSeraBoard.find('form');
		var paramsJson = {};
		for(var i=0; i<forms.length; i++){
			var form = $(forms[i]);
			// 폼이 스마트폼이면 formId와 formName 값을 전달한다...
			if(form.attr('name') === 'frmSmartForm'){
				paramsJson['formId'] = form.attr('formId');
				paramsJson['formName'] = form.attr('formName');
			}else if(form.attr('name') === 'frmNewBoard'){
				continue;
			}
			// 폼이름 키값으로 하여 해당 폼에 있는 모든 입력항목들을 JSON형식으로 Serialize 한다...
			paramsJson[form.attr('name')] = mergeObjects(form.serializeObject(), SmartWorks.GridLayout.serializeObject(form));
		}
		var accessSpace = {};
		var wid = newSeraBoard.attr('wid');
		accessSpace['selWorkSpace'] = wid;
		accessSpace['selWorkSpaceType'] = "<%=ISmartWorks.SPACE_TYPE_GROUP%>";
		accessSpace['selAccessLevel'] = "<%=AccessPolicy.LEVEL_PUBLIC%>";
		paramsJson['frmAccessSpace'] = accessSpace;
		console.log(JSON.stringify(paramsJson));
		// 서비스요청 프로그래스바를 나타나게 한다....
		var progressSpan = newSeraBoard.find('.js_progress_span');
		smartPop.progressCont(progressSpan);
		var url = "create_new_board.sw";
		// create_new_memo.sw서비스를 요청한다..
		$.ajax({
			url : url,
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				// 성공시에 프로그래스바를 제거하고 성공메시지를 보여준다...
				smartPop.closeProgress();
				smartPop.showInfo(smartPop.INFO, "새로운 항목이 정상적으로 생성되었습니다!", function(){
					document.location.href = (wid === "<%=Constants.SERA_WID_SERA_NEWS%>" ) ? "seraNews.sw" : "seraTrend.sw";					
				});
			},
			error : function(e) {
				// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
				smartPop.closeProgress();
				smartPop.showInfo(smartPop.ERROR, "새로운항목 생성에 문제가 발생하였습니다. 관리자에게 문의하시기 바랍니다!");
			}
		});
	}
	
	getIntanceList = function(paramsJson, progressSpan, isGray) {
		if (isEmpty(progressSpan))
			progressSpan = $('.js_work_list_title').find(
					'.js_progress_span:first');
		if (isGray)
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
				$('#board_instance_list_page').html(data);
				smartPop.closeProgress();
			},
			error : function(xhr, ajaxOptions, thrownError) {
				smartPop.closeProgress();
				smartPop.showInfo(smartPop.ERROR, smartMessage.get('workListError'));
			}
		});
	};

	selectListParam = function(progressSpan, isGray) {
		var boardList = $('.js_board_list_page');
		var forms = boardList.find('form:visible');
		var paramsJson = {};
		var workId = boardList.attr('workId');
		paramsJson["href"] = "sera/jsp/content/board/board_instance_list.jsp?workId="
				+ workId;
		var searchFilters = boardList.find('form[name="frmSearchFilter"]');
		for ( var i = 0; i < forms.length; i++) {
			var form = $(forms[i]);
			if (form.attr('name') !== "frmSearchFilter"
					&& !(!isEmpty(searchFilters) && form.attr('name') === "frmSearchInstance")) {
				paramsJson[form.attr('name')] = mergeObjects(form.serializeObject(), SmartWorks.GridLayout.serializeObject(form));
			}
		}
		if (!isEmpty(searchFilters)) {
			var searchFilterArray = new Array();
			for ( var i = 0; i < searchFilters.length; i++) {
				var searchFilter = $(searchFilters[i]);
				if (searchFilter.is(':visible'))
					searchFilterArray.push(searchFilter.find(':visible').serializeObject());
			}
			paramsJson['frmSearchFilters'] = searchFilterArray;
		}
		getIntanceList(paramsJson, progressSpan, isGray);
	};
</script>
<%
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();
	String wid = (String)session.getAttribute("wid");
	String workId = request.getParameter("workId");
	String instId = request.getParameter("instId");
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />
<div id="course_list_section" class="js_sera_board_item_page" workId="<%=workId%>" instId="<%=instId%>" wid="<%=wid%>">
	<!-- SNB Left -->
	<div class="snb2">
		<ul class="snb_menu">
			<li><img height="112" width="149"
				src="sera/images/function_title_admin.gif"></li>
			
			<li class="<%if(Constants.SERA_WID_SERA_NEWS.equals(wid)){%>selected<%}%>"><a href="seraNews.sw">세라소식</a></li>
			<li class="<%if(Constants.SERA_WID_SERA_TREND.equals(wid)){%>selected<%}%>"><a href="seraTrend.sw">트렌드 세라</a></li>
		</ul>
	</div>
	<!-- SNB Left//-->
	<!-- Content -->
	<div id="content_list_section">
		<!-- Title -->
		<div class="tit_header">
			<h2><%if(Constants.SERA_WID_SERA_NEWS.equals(wid)){%>세라 소식
				<%}else if(Constants.SERA_WID_SERA_TREND.equals(wid)){%>트렌드 세라<%}%></h2>
		</div>
		<!-- Title //-->

		<!-- Input Form -->
		<div class="form_layout js_form_content">
		</div>
		<div class="sw_error_message tl" style="color: red"></div>

		<!-- Btn -->
		<div class="btn_space">
			<div style="clear: both; display: inline-block">
				<%if(cUser.getUserLevel()>=User.USER_LEVEL_AMINISTRATOR){ %>
					<div class="btn_blu_l mr10 js_create_sera_board_btn" href="" <%if(!SmartUtil.isBlankObject(instId)) { %>style="display:none"<%}%>>
						<div class="btn_blu_r">등 록</div>
					</div>
					<div class="btn_blu_l mr10 js_save_sera_board_btn" href="" style="display:none">
						<div class="btn_blu_r">저 장</div>
					</div>
					<div class="btn_red_l mr10 js_modify_sera_board_btn" href="" <%if(SmartUtil.isBlankObject(instId)) { %>style="display:none"<%}%>>
						<div class="btn_red_r">수 정</div>
					</div>
					<div class="btn_red_l mr10 js_delete_sera_board_btn" href="" <%if(SmartUtil.isBlankObject(instId)) { %>style="display:none"<%}%>>
						<div class="btn_red_r">삭 제</div>
					</div>
					<div class="btn_blu_l mr10 js_cancel_sera_board_btn" href="" style="display:none">
						<div class="btn_blu_r">취 소</div>
					</div>
				<%
				}
				%>
				<div class="btn_blu_l js_list_sera_board_btn">
					<a href="<%if(Constants.SERA_WID_SERA_NEWS.equals(wid)){%>seraNews.sw<%}else{%>seraTrend.sw<%}%>">
						<div class="btn_blu_r">목 록</div>
					</a>
				</div>
			</div>
		</div>
		<!-- Btn //-->

	</div>
	<!-- Content //-->
</div>
<script type="text/javascript">
	var boardItem = $('.js_sera_board_item_page');
	var workId = boardItem.attr("workId");
	var instId = boardItem.attr("instId");
	var mode = isEmpty(instId) ? "edit" : "view";
	var formContent = boardItem.find('div.js_form_content');
	new SmartWorks.GridLayout({
		target : formContent,
		mode : mode,
		workId : workId,
		recordId : instId,
		onSuccess : function(){
		}
	});
</script>