
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
	
	String groupId = request.getParameter("groupId");

%>
<script type="text/javascript">

	// 개인정보프로파일 수정하기 버튼을 클릭하면, 
	// 모든정보를 JSON형식으로 Serialize해서 서버의 update_my_profile.sw 서비스를 호출하여 수정한다.
	function submitForms(e) {
		var inviteGroupMembers = $('.js_invite_group_members_page');
		var groupId = inviteGroupMembers.attr('groupId');
		if (SmartWorks.GridLayout.validate(inviteGroupMembers.find('form.js_validation_required'), $('.js_pop_error_message'))) {
			var forms = inviteGroupMembers.find('form');
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
			newParamsJson['groupId'] = groupId;
			newParamsJson['users'] = paramsJson.frmInviteGroupMembers.txtGroupMembers.users;
			console.log(JSON.stringify(newParamsJson));
			var progressSpan = inviteGroupMembers.find('.js_progress_span');
			smartPop.progressCont(progressSpan);
			var url = "invite_group_members.sw";
			$.ajax({
				url : url,
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(newParamsJson),
				success : function(data, status, jqXHR) {
					window.location.reload(true);
					smartPop.closeProgress();
				},
				error : function(e) {
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.ERROR, smartMessage.get('inviteGroupMembersError'));
				}
			});
		}
	};
</script>

<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!--  전체 레이아웃 -->
<div class="pop_corner_all js_invite_group_members_page" groupId="<%=groupId%>">

	<!-- 팝업 타이틀 -->
	<div class="form_title">
		<div class="pop_title"><fmt:message key="group.title.invite_group_members"></fmt:message></div>
		<div class="txt_btn">			
			<a href="" onclick="smartPop.close();return false;"><div class="btn_x"></div></a>
		</div>
		<div class="solid_line"></div>
	</div>
	<!-- 팝업 타이틀 //-->
	<!-- 컨텐츠 -->
	<form name="frmInviteGroupMembers" class="js_validation_required">
		<div class="contents_space">
			<table>
				<tr>
					<th style="width:20%"><fmt:message key="group.title.members" /></th>
					<td class="js_type_userField" style="width:80%" fieldId="txtGroupMembers" multiUsers="true">
						<div class="form_value w100">
							<div>
								<div class="fieldline community_names js_community_names sw_required">
									<input class="m0 js_auto_complete" style="width:100px" href="community_non_member.sw?communityId=<%=groupId %>" type="text">
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
					<span class="txt_btn_center"><fmt:message key="group.button.invite_member"/></span>
					<span class="txt_btn_end"></span>
				</a> 
			</span>
			 <span class="btn_gray ml5"> 
				 <a href="" class="js_close_invite_member"> 
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
