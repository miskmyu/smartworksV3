
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

%>
<script type="text/javascript">

	// 개인정보프로파일 수정하기 버튼을 클릭하면, 
	// 모든정보를 JSON형식으로 Serialize해서 서버의 update_my_profile.sw 서비스를 호출하여 수정한다.
	function submitForms(e) {
		var newGroup = $('.js_new_group_page');
		var maxMembers = newGroup.find('input[name="rdoGroupMaxMembers"]:checked');
		if(maxMembers.attr('value') == "UserDefined"){
			newGroup.find('input[name="txtGroupMaxMembers"]').addClass('required');
		}else{
			newGroup.find('input[name="txtGroupMaxMembers"]').removeClass('required');			
		}
		if (SmartWorks.GridLayout.validate(newGroup.find('form.js_validation_required'), $('.js_pop_error_message'))) {
			var forms = newGroup.find('form');
			var paramsJson = {};
			for(var i=0; i<forms.length; i++){
				var form = $(forms[i]);
				if(form.attr('name') === 'frmSmartForm'){
					paramsJson['formId'] = form.attr('formId');
					paramsJson['formName'] = form.attr('formName');
				}
				paramsJson[form.attr('name')] = mergeObjects(form.serializeObject(), SmartWorks.GridLayout.serializeObject(form));
			}
			console.log(JSON.stringify(paramsJson));
			var progressSpan = newGroup.find('.js_progress_span');
			smartPop.progressCont(progressSpan);
			var url = "create_new_group.sw";
			$.ajax({
				url : url,
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					// 사용자정보 수정이 정상적으로 완료되었으면, 현재 페이지에 그대로 있는다.
					document.location.href = data.href;
					smartPop.closeProgress();
					smartPop.close();
				},
				error : function(e) {
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.ERROR, smartMessage.get('createGroupError'));
				}
			});
		}
	};
</script>

<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!--  전체 레이아웃 -->
<div class="pop_corner_all js_new_group_page">

	<!-- 팝업 타이틀 -->
	<div class="form_title">
		<div class="pop_title"><fmt:message key="group.title.new_group"></fmt:message></div>
		<div class="txt_btn">			
			<a href="" onclick="smartPop.close();return false;"><div class="btn_x"></div></a>
		</div>
		<div class="solid_line"></div>
	</div>
	<!-- 팝업 타이틀 //-->
	<!-- 컨텐츠 -->
	<form name="frmNewGroupProfile" class="js_validation_required">
		<div class="contents_space">
			<table>
				<tr>
					<th style="width:20%"><fmt:message key="group.title.picture" /></th>
					<td class="photo_section fl">			
						<!--  *** js_group_profile_field : sw_act_work.js에서 화면로딩이 완료되면 이 클래스로 찾아서,  	-->
						<!--      현재 커뮤너티 그룹의 사진을 보여주고, 다른 사진을 올리줄 있도록하는 기능을 제공한다. 			-->
						<div class="js_group_profile_field js_auto_load_group_profile"></div>
						<div class="t_s11"><fmt:message key="profile.title.size_desc"/></div>
					</td>					
				</tr>
				<tr>
					<th class="required_label"  style="width:12%"><fmt:message key="group.title.name" /></th>
					<td>
						<input name="txtGroupName" class="fieldline required" type="text">		
					</td>
				</tr>
				<tr>
					<th><fmt:message key="group.title.desc" /></th>
					<td>
						<textarea name="txtaGroupDesc" class="fieldline" rows="4"></textarea>	
					</td>
				</tr>
				<tr>
					<th><fmt:message key="group.title.type" /></th>
					<td>
						<select name="selGroupProfileType">
							<option value="<%=Group.GROUP_TYPE_OPEN %>" selected><fmt:message key="group.title.type_open" /></option>
							<option value="<%=Group.GROUP_TYPE_CLOSED %>"><fmt:message key="group.title.type_closed" /></option>
						</select>
					</td>
				</tr>
				<tr>
					<th><fmt:message key="group.title.leader" /></th>
					<td>
						<div><img src="<%=SmartUtil.getCurrentUser().getMinPicture()%>" class="profile_size_s"/><input name="txtGroupLeader" type="hidden" class=""  value="<%=SmartUtil.getCurrentUser().getId() %>"><%=SmartUtil.getCurrentUser().getLongName() %></div>
					</td>
				</tr>
				<tr>
					<th><fmt:message key="group.title.join_approval" /></th>
					<td>
						<div>
							<input name="rdoGroupAutoApproval" type="radio" value="true" checked><fmt:message key="group.label.auto_approval"/>
							<input name="rdoGroupAutoApproval" type="radio" value="false"><fmt:message key="group.label.leader_approval"/>
						</div>
					</td>
				</tr>
				<tr>
					<th><fmt:message key="group.title.max_members" /></th>
					<td>
						<div>
							<input name="rdoGroupMaxMembers" type="radio" checked value="<%=Group.MAX_MEMBERS_UNLIMITED %>"><fmt:message key="group.label.unlimited_members"/>
							<input name="rdoGroupMaxMembers" type="radio" value="UserDefined">
							<input name="txtGroupMaxMembers" type="text" style="width:40px" class="fieldline number tr">
							<fmt:message key="group.label.members"/>
						</div>
					</td>
				</tr>
				<tr>
					<th><fmt:message key="group.title.members" /></th>
					<td class="js_type_userField" fieldId="txtGroupMembers" multiUsers="true">
						<div class="form_value w100">
							<div class="fieldline community_names js_community_names">
								<div class="js_selected_communities user_sel_area"></div>
								<input class="m0 w99 js_auto_complete" href="community_name.sw" type="text">
								<div class="js_srch_x"></div>
							</div>
							<div class="js_community_list com_list" style="display: none"></div>
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
					<span class="txt_btn_center"><fmt:message key="common.button.create"/></span>
					<span class="txt_btn_end"></span>
				</a> 
			</span>
			 <span class="btn_gray ml5"> 
				 <a href="" class="js_close_new_group"> 
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
