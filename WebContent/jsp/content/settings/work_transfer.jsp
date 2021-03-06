<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.model.approval.ApprovalLine"%>
<%@page import="net.smartworks.model.RecordList"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.model.instance.info.RequestParams"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 현재사용자 정보도 가져온다..
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();
	
%>
<script type="text/javascript">

	// 개인정보프로파일 수정하기 버튼을 클릭하면, 
	// 모든정보를 JSON형식으로 Serialize해서 서버의 update_my_profile.sw 서비스를 호출하여 수정한다.
	function submitForms(e) {
		var workTransfer = $('.js_work_transfer_page');
		if (SmartWorks.GridLayout.validate(workTransfer.find('form.js_validation_required'), $('.js_pop_error_message'))) {
			var forms = workTransfer.find('form');
			
			var comType = workTransfer.attr('comType');
			var fromCommunity = workTransfer.find('td[fieldId="txtFromCommunity"] .js_community_item');
			var givenMember = workTransfer.find('td[fieldId="txtGivenMember"] .js_community_item');
			var givenCommunity = workTransfer.find('td[fieldId="txtGivenCommunity"] .js_community_item');
			if(comType === 'user'){
				if(fromCommunity.attr('comId') === givenMember.attr('comId')){
					smartPop.showInfo(smartPop.INFO, smartMessage.get('sameMemberError'));				
					return;
				}
			}else if(comType === 'department' || comType === 'group'){
				if(fromCommunity.attr('comId') === givenCommunity.attr('comId')){
					smartPop.showInfo(smartPop.INFO, smartMessage.get('sameCommunityError'));				
					return;
				}
			}
			var paramsJson = {};
			for(var i=0; i<forms.length; i++){
				var form = $(forms[i]);
				if(form.attr('name') === 'frmSmartForm'){
					paramsJson['formId'] = form.attr('formId');
					paramsJson['formName'] = form.attr('formName');
				}
				paramsJson[form.attr('name')] = mergeObjects(form.serializeObject(), SmartWorks.GridLayout.serializeObject(form));
			}
			paramsJson['communityType'] = comType;
			
			console.log(JSON.stringify(paramsJson));
			var progressSpan = workTransfer.find('.js_progress_span');
			smartPop.progressCont(progressSpan);
			var url = "execute_work_transfer.sw";
			$.ajax({
				url : url,
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					// 사용자정보 수정이 정상적으로 완료되었으면, 현재 페이지에 그대로 있는다.
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.INFO, smartMessage.get('executeWorkTransferSucceed'), function(){
						fromCommunity.find('a').click();
						givenMember.find('a').click();
						givenCommunity.find('a').click();
					});
				},
				error : function(e) {
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.ERROR, smartMessage.get('executeWorkTransferError'));
				}
			});
		}
	};
</script>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!-- 컨텐츠 레이아웃-->
<div class="section_portlet setting_section js_work_transfer_page" fromCommunityId="">
	<div class="portlet_t"><div class="portlet_tl"></div></div>
	<div class="portlet_l" style="display: block;">
		<ul class="portlet_r" style="display: block;">
			<!-- 타이틀 -->
			<div class="body_titl">
				<div class="body_titl_area ti_approval title_noico"><fmt:message key="settings.title.work_transfer"/></div>
				<div class="solid_line"></div>
			</div>
			<!-- 타이틀 -->
			<!-- 컨텐츠 -->
			<form name="frmWorkTransfer" class="js_validation_required">
				<div class="contents_space" style="width:700px">
					<table>
						<tr>
							<th class="required_label"><fmt:message key="transfer.title.from_community" /></th>
							<td class="js_type_userField" fieldId="txtFromCommunity" multiUsers="false" style="width:600px">
								<div class="form_value w100 ">
									<div class="icon_fb_space">
										<div class="fieldline community_names js_community_names sw_required">
											<div class="js_selected_communities user_sel_area"></div>
											<input class="m0 w99 js_auto_complete" href="community_name.sw" type="text">
										</div>
										<div class="js_community_list com_list" style="display: none"></div>
										<span class="js_community_popup"></span>
										<a href="" class="js_communitypicker_button">
											<span class="icon_fb_users"></span>
										</a>
									</div>
								</div>
								<div><fmt:message key="transfer.message.select_community"/></div>
							</td>
						</tr>
						<tr class="js_transfer_user" style="display:none">
							<th class="required_label"><fmt:message key="transfer.title.given_member" /></th>
							<td class="js_type_userField" fieldId="txtGivenMember" multiUsers="false">
								<div class="form_value w100">
									<div class="icon_fb_space">
										<div class="fieldline community_names js_community_names sw_required">
											<div class="js_selected_communities user_sel_area"></div>
											<input class="m0 w99 js_auto_complete" href="user_name.sw" type="text">
										</div>
										<div class="js_community_list com_list" style="display: none"></div>
										<span class="js_community_popup"></span>
										<a href="" class="js_userpicker_button">
											<span class="icon_fb_user"></span>
										</a>
								</div>
							</td>
						</tr>
						<tr class="js_transfer_depart js_transfer_group" style="display:none">
							<th class="required_label"><fmt:message key="transfer.title.given_community" /></th>
							<td class="js_type_userField" fieldId="txtGivenCommunity" multiUsers="false">
								<div class="form_value w100">
									<div class="icon_fb_space">
										<div class="fieldline community_names js_community_names sw_required">
											<div class="js_selected_communities user_sel_area"></div>
											<input class="m0 w99 js_auto_complete" href="community_name.sw" type="text">
										</div>
										<div class="js_community_list com_list" style="display: none"></div>
										<span class="js_community_popup"></span>
										<a href="" class="js_communitypicker_button">
											<span class="icon_fb_users"></span>
										</a>
								</div>
							</td>
						</tr>
						<tr class="js_transfer_user" style="display:none">
							<th><fmt:message key="transfer.title.authority" /></th>
							<td>
								<div>
									<input name="chkTransferAuthority" type="checkbox" checked><fmt:message key="transfer.label.transfer_authority"/>
								</div>
							</td>
						</tr>
						<tr class="js_transfer_user js_transfer_depart js_transfer_group" style="display:none">
							<th><fmt:message key="transfer.title.work_transfer" /></th>
							<td>
								<div>
									<input class="js_click_transfer_all" name="rdoTransferAll" type="radio" value="true" checked><fmt:message key="transfer.label.all_works"/>
									<input class="js_click_selected_transfer" name="rdoTransferAll" type="radio" value="false"><fmt:message key="transfer.label.selected_works"/>
									<span class="js_sub_progress_span"></span>
								</div>
							</td>
						</tr>
						<tr class="js_transfer_user" style="display:none">
							<th></th>
							<td><fmt:message key="transfer.message.work_transfer_user"/></td>
						</tr>
						<tr class="js_transfer_depart js_transfer_group" style="display:none">
							<th></th>
							<td><fmt:message key="transfer.message.work_transfer_community"/></td>
						</tr>
						<tr class="js_transfer_user js_transfer_depart js_transfer_group" style="display:none">
							<th></th>
							<td id="js_used_work_list">
									<jsp:include page="/jsp/content/settings/used_work_list.jsp">
									<jsp:param value="" name="comId"/>
								</jsp:include>
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
							<span class="txt_btn_center"><fmt:message key="common.button.execute"/></span>
							<span class="txt_btn_end"></span>
						</a> 
					</span>
					 <span class="btn_gray ml5"> 
						 <a href="" class="js_close_retire_member"> 
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
			<!-- 컨텐츠 //-->
		</ul>
	</div>
	<div class="portlet_b" style="display: block;"></div>
</div>
<!-- 컨텐츠 레이아웃//-->
