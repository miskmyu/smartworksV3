
<!-- Name 			: pop_create_group.jsp									 -->
<!-- Description	: 새로운 커뮤너티 그룹을 생성하는 팝업화면 						 -->
<!-- Author			: Maninsoft, Inc.										 -->
<!-- Created Date	: 2011.9.												 -->

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
	
	String workId = request.getParameter("workId");
	String instanceId = request.getParameter("instanceId");
	String taskInstId = request.getParameter("taskInstId");

%>
<script type="text/javascript">

	// 개인정보프로파일 수정하기 버튼을 클릭하면, 
	// 모든정보를 JSON형식으로 Serialize해서 서버의 update_my_profile.sw 서비스를 호출하여 수정한다.
	function submitForms(e) {
		var reassignPerformer = $('.js_reassign_performer_page');
		var workId = reassignPerformer.attr('workId');
		var instanceId = reassignPerformer.attr('instanceId');
		var taskInstId = reassignPerformer.attr('taskInstId');
		if (SmartWorks.GridLayout.validate(reassignPerformer.find('form.js_validation_required'), $('.js_pop_error_message'))) {
			var forms = reassignPerformer.find('form');
			var paramsJson = {};
			for(var i=0; i<forms.length; i++){
				var form = $(forms[i]);
				if(form.attr('name') === 'frmSmartForm'){
					paramsJson['formId'] = form.attr('formId');
					paramsJson['formName'] = form.attr('formName');
				}
				paramsJson[form.attr('name')] = mergeObjects(form.serializeObject(), SmartWorks.GridLayout.serializeObject(form));
			}
			var newParamsJson = {};
			newParamsJson['workId'] = workId;
			newParamsJson['instanceId'] = instanceId;
			newParamsJson['taskInstId'] = taskInstId;
			newParamsJson['newPerformer'] = paramsJson.frmReassignPerformer.txtNewPerformer.users;
			console.log(JSON.stringify(newParamsJson));
			var progressSpan = reassignPerformer.find('.js_progress_span');
			smartPop.progressCont(progressSpan);
			var url = "reassign_task_instance.sw";
			$.ajax({
				url : url,
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(newParamsJson),
				success : function(data, status, jqXHR) {					
					// 성공시에 프로그래스바를 제거하고 성공메시지를 보여준다...
					smartPop.closeProgress();
					document.location.href = "pwork_list.sw?cid=pw.li." + workId;
					smartPop.close();
					return;
				},
				error : function(e) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("reassignTaskInstanceError"), function(){
						return;
					});
					smartPop.closeProgress();					
				}
			});
		}
	};
</script>

<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!--  전체 레이아웃 -->
<div class="pop_corner_all js_reassign_performer_page" workId="<%=workId%>" instanceId="<%=instanceId%>" taskInstId="<%=taskInstId%>">

	<!-- 팝업 타이틀 -->
	<div class="form_title">
		<div class="pop_title"><fmt:message key="common.title.reassign_performer"></fmt:message></div>
		<div class="txt_btn">			
			<a href="" onclick="smartPop.close();return false;"><div class="btn_x"></div></a>
		</div>
		<div class="solid_line"></div>
	</div>
	<!-- 팝업 타이틀 //-->
	<!-- 컨텐츠 -->
	<form name="frmReassignPerformer" class="js_validation_required">
		<div class="contents_space">
			<table>
				<tr>
					<th style="width:20%"><fmt:message key="common.title.new_performer" /></th>
					<td class="js_type_userField" style="width:80%" fieldId="txtNewPerformer" multiUsers="false">
						<div class="form_value w100">
							<div>
								<div class="fieldline community_names js_community_names sw_required">
									<input class="m0 js_auto_complete" style="width:100px" href="user_name.sw" type="text">
								</div>
								<div class="js_community_list srch_list_nowid" style="display: none"></div>
							</div>
						</div>
					</td>
				</tr>
			</table>
		</div>
	</form>
	<!-- 컨텐츠 //-->
	<!-- 버튼 영역 -->
	<div class="glo_btn_space">	
		<div class="fr">
			<span class="btn_gray">
				<a href="" onclick='submitForms(); return false;'>
					<span class="txt_btn_start"></span>
					<span class="txt_btn_center"><fmt:message key="common.button.reassign"/></span>
					<span class="txt_btn_end"></span>
				</a> 
			</span>
			 <span class="btn_gray ml5"> 
				 <a href="" class="js_close_reassign_performer"> 
				 	<span class="txt_btn_start"></span>
				 	<span class="txt_btn_center"><fmt:message key="common.button.cancel"/></span>
				 	<span class="txt_btn_end"></span> 
				 </a>
			</span>
		</div>

		<!--  실행시 표시되는 프로그래스아이콘을 표시할 공간 -->
		<span class="fr form_space js_progress_span"></span>
	
		<!-- 실행시 데이터 유효성 검사이상시 에러메시지를 표시할 공간 -->
		<div class="form_space sw_error_message js_pop_error_message" style="color: red; line-height: 20px"></div>

	</div>
	<!-- 버튼 영역 //-->

</div>
<!-- 전체 레이아웃//-->
