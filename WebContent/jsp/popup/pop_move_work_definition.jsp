
<!-- Name 			: pop_move_work_definition.jsp							 -->
<!-- Description	: 새로운 업무w정의를 생성하는 팝업화면 						 -->
<!-- Author			: Maninsoft, Inc.										 -->
<!-- Created Date	: 2011.9.												 -->

<%@page import="net.smartworks.model.work.info.WorkInfo"%>
<%@page import="net.smartworks.model.work.WorkCategory"%>
<%@page import="net.smartworks.model.work.Work"%>
<%@page import="net.smartworks.model.work.SmartWork"%>
<%@page import="net.smartworks.model.community.Group"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.model.KeyMap"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.util.LocaleInfo"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 현재사용자 정보도 가져온다 
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();

	String type = request.getParameter("type");
	String workId = request.getParameter("workId");
	String workName = request.getParameter("workName");
	String workDesc = request.getParameter("workDesc");
	String workFullName = request.getParameter("workFullName");
	String categoryId = request.getParameter("categoryId");
	String groupId = request.getParameter("groupId");
	boolean isCopy = type.equals("copy") ? true : false;
	
%>
<script type="text/javascript">

	// 개인정보프로파일 수정하기 버튼을 클릭하면, 
	// 모든정보를 JSON형식으로 Serialize해서 서버의 update_my_profile.sw 서비스를 호출하여 수정한다.
	function submitForms(e) {
		var moveWorkDefinition = $('.js_move_work_definition_page');
		if (SmartWorks.GridLayout.validate(moveWorkDefinition.find('form.js_validation_required'), $('.js_pop_error_message'))) {
			var forms = moveWorkDefinition.find('form');
			var workId = moveWorkDefinition.attr('workId');
			var type = moveWorkDefinition.attr('type');
			var paramsJson = {};
			var url = "";
			if(type === "move")
				url = "move_work_definition.sw";
			else if(type === "copy")
				url = "copy_work_definition.sw";
			paramsJson['workId'] = workId;
		
			for(var i=0; i<forms.length; i++){
				var form = $(forms[i]);
				if(form.attr('name') === 'frmSmartForm'){
					paramsJson['formId'] = form.attr('formId');
					paramsJson['formName'] = form.attr('formName');
				}
				paramsJson[form.attr('name')] = mergeObjects(form.serializeObject(), SmartWorks.GridLayout.serializeObject(form));
			}
			console.log(JSON.stringify(paramsJson));
			var progressSpan = moveWorkDefinition.find('.js_progress_span');
			smartPop.progressCont(progressSpan);
			$.ajax({
				url : url,
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					// 사용자정보 수정이 정상적으로 완료되었으면, 현재 페이지에 그대로 있는다.
					smartPop.closeProgress();
					smartPop.close();
					var categoryId = form.find('select[name="selWorkCategoryId"]').attr('value');
					var groupId = form.find('select[name="selWorkGroupId"]').attr('value');
					openWorkCategoryTree(categoryId, groupId);
					var target = $('#content');
					$.ajax({
						url : "start_work_service.sw?workId=" + workId,
						success : function(data, status, jqXHR) {
							target.html(data);
						}			
					});
				},
				error : function(e) {
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.ERROR, (type === "move") ? smartMessage.get('moveWorkDefinitionError') : smartMessage.get('copyWorkDefinitionError'));
				}
			});
		}
	};
</script>

<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!--  전체 레이아웃 -->
<div class="pop_corner_all js_move_work_definition_page" workId="<%=workId%>" type="<%=type%>">

	<!-- 팝업 타이틀 -->
	<div class="form_title">
		<%
		if(isCopy){
		%>
			<div class="pop_title"><fmt:message key="builder.title.copy_work_definition"></fmt:message></div>
		<%
		}else{
		%>
			<div class="pop_title"><fmt:message key="builder.title.move_work_definition"></fmt:message></div>
		<%
		}
		%>
		<div class="txt_btn">
			<a href="" onclick="smartPop.close();return false;"><div class="btn_x"></div></a>
		</div>
		<div class="solid_line"></div>
	</div>
	<!-- 팝업 타이틀 //-->
	<!-- 컨텐츠 -->
	<form name="frmMoveWorkDefinition" class="js_validation_required">
		<div class="contents_space">
			<table>
				<tr>
					<td><fmt:message key="builder.title.source_work" /></td>
					<td>
						<div><%=workFullName%></div>		
					</td>
				</tr>
				<%
				WorkInfo[] workCategories = smartWorks.getAllWorksByCategoryId("");
				%>
				<tr>
					<%
					if(isCopy){
					%>
						<td><fmt:message key="builder.title.copy_category_name"/></td>
					<%
					}else{
					%>
						<td><fmt:message key="builder.title.move_category_name"/></td>
					<%
					}
					%>
					<td>
						<select name="selWorkCategoryId" class="js_select_work_category">
							<%
							if(!SmartUtil.isBlankObject(workCategories)){
								for(WorkInfo category : workCategories){
									if(category.getType()!= WorkCategory.TYPE_CATEGORY) continue;
							%>
									<option value="<%=category.getId()%>" <%if(category.getId().equals(categoryId)){ %>selected<%} %>><%=category.getName() %></option>
							<%
								}
							}
							%>
						</select>
					</td>
				</tr>
				<%
				WorkInfo[] workGroups = null;
				if(!SmartUtil.isBlankObject(categoryId)) workGroups = smartWorks.getAllWorksByCategoryId(categoryId);
				%>
				<tr>
					<%
					if(isCopy){
					%>
						<td><fmt:message key="builder.title.copy_group_name"/></td>
					<%
					}else{
					%>
						<td><fmt:message key="builder.title.move_group_name"/></td>
					<%
					}
					%>
					<td>
						<select name="selWorkGroupId" class="js_work_group_target">
							<option><fmt:message key="common.title.none"/></option>
							<%
							if(!SmartUtil.isBlankObject(workGroups)){
								for(WorkInfo workGroup : workGroups){
									if(workGroup.getType()!= WorkCategory.TYPE_CATEGORY) continue;
							%>
									<option value="<%=workGroup.getId()%>" <%if(workGroup.getId().equals(groupId)){ %>selected<%} %>><%=workGroup.getName() %></option>
							<%
								}
							}
							%>
							
						</select>
					</td>
				</tr>
				<%
				if(isCopy){
				%>
					<tr>
						<td><fmt:message key="builder.title.to_work_name"/></td>
						<td>
							<input name="txtToWorkName" type="text" class="fieldline required" value="<%=workName%><fmt:message key='builder.title.copied_name'/>">
						</td>
					</tr>
					<tr>
						<td><fmt:message key="common.title.desc" /></td>
						<td>
							<textarea name="txtaToWorkDesc" class="fieldline" rows="4"><%=CommonUtil.toNotNull(workDesc) %></textarea>	
						</td>
					</tr>
				<%
				}
				%>
			</table>
		</div>
	</form>
	<!-- 컨텐츠 //-->
	<!-- 버튼 영역 -->
	<div class="glo_btn_space">
	<!--  실행시 표시되는 프로그래스아이콘을 표시할 공간 -->

		<div class="fr">

			<span class="btn_gray">
				<a href="" onclick='submitForms(); return false;'>
					<span class="txt_btn_start"></span>
					<%
					if(isCopy){
					%>
						<span class="txt_btn_center"><fmt:message key="common.button.copy"/></span>
					<%
					}else{
					%>
						<span class="txt_btn_center"><fmt:message key="common.button.move"/></span>
					<%
					}
					%>
					<span class="txt_btn_end"></span>
				</a> 
			</span>
			 <span class="btn_gray ml5"> 
				 <a href="" class="js_close_move_work"> 
				 	<span class="txt_btn_start"></span>
				 	<span class="txt_btn_center"><fmt:message key="common.button.cancel"/></span>
				 	<span class="txt_btn_end"></span> 
				 </a>
			</span>
		</div>

		<!--  실행시 표시되는 프로그래스아이콘을 표시할 공간 -->
		<span class="fr js_progress_span"></span>
	
		<!-- 실행시 데이터 유효성 검사이상시 에러메시지를 표시할 공간 -->
		<div class="sw_error_message js_pop_error_message" style="color: red; line-height: 20px"></div>

	</div>
	<!-- 버튼 영역 //-->

</div>
<!-- 전체 레이아웃//-->
