
<!-- Name 			: pop_create_group.jsp									 -->
<!-- Description	: 새로운 커뮤너티 그룹을 생성하는 팝업화면 						 -->
<!-- Author			: Maninsoft, Inc.										 -->
<!-- Created Date	: 2011.9.												 -->

<%@page import="net.smartworks.model.work.info.UsedWorkInfo"%>
<%@page import="org.claros.intouch.webmail.services.GetUnreadCountService"%>
<%@page import="net.smartworks.model.community.info.UserInfo"%>
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
	
	String retiredUserId = request.getParameter("userId");
	UserInfo givenUser = smartWorks.getHeadByUserId(retiredUserId);

%>
<script type="text/javascript">

	// 개인정보프로파일 수정하기 버튼을 클릭하면, 
	// 모든정보를 JSON형식으로 Serialize해서 서버의 update_my_profile.sw 서비스를 호출하여 수정한다.
	function submitForms(e) {
		var retireMember = $('.js_pop_retire_member_page');
		if (SmartWorks.GridLayout.validate(retireMember.find('form.js_validation_required'), $('.js_pop_error_message'))) {
			var forms = retireMember.find('form');
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
			var progressSpan = retireMember.find('.js_progress_span');
			smartPop.progressCont(progressSpan);
			var url = "execute_retire_member.sw";
			$.ajax({
				url : url,
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					// 사용자정보 수정이 정상적으로 완료되었으면, 현재 페이지에 그대로 있는다.
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.INFO, smartMessage.get('executeRetireMemberSucceed'), function(){
						smartPop.close();
					});
				},
				error : function(e) {
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.ERROR, smartMessage.get('executeRetireMemberError'));
				}
			});
		}
	};
</script>

<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!--  전체 레이아웃 -->
<div class="pop_corner_all js_pop_retire_member_page js_work_transfer_page" fromMemberId="<%=retiredUserId%>">

	<!-- 팝업 타이틀 -->
	<div class="form_title">
		<div class="pop_title"><fmt:message key="common.button.retire_member"></fmt:message></div>
		<div class="txt_btn">			
			<a href="" onclick="smartPop.close();return false;"><div class="btn_x"></div></a>
		</div>
		<div class="solid_line"></div>
	</div>
	<!-- 팝업 타이틀 //-->
	<!-- 컨텐츠 -->
	<form name="frmRetireMember" class="js_validation_required">
		<div class="contents_space">
			<table>
				<tr>
					<th><fmt:message key="transfer.title.given_member" /></th>
					<td class="js_type_userField" fieldId="txtGivenMember" multiUsers="false">
						<div class="form_value w100">
							<div class="icon_fb_space">
								<div class="fieldline community_names js_community_names">
									<div class="js_selected_communities user_sel_area"></div>
									<input class="m0 w99 js_auto_complete" href="community_name.sw" type="text">
								</div>
								<div class="js_community_list com_list" style="display: none"></div>
						</div>
					</td>
				</tr>
				<tr>
					<th></th>
					<td><fmt:message key="transfer.message.given_member"><fmt:param><span class="t_name""><%=givenUser.getLongName() %></span></fmt:param></fmt:message></td>
				</tr>
				<tr height="10px">
					<th></th>
					<td></td>
				</tr>
				<tr>
					<th><fmt:message key="transfer.title.work_transfer" /></th>
					<td>
						<div >
							<input class="js_click_transfer_all" name="rdoTransferAll" type="radio" value="true" checked><fmt:message key="transfer.label.all_works"/>
							<input class="js_click_selected_transfer" name="rdoTransferAll" type="radio" value="false"><fmt:message key="transfer.label.selected_works"/>
							<span class="js_sub_progress_span"></span>
						</div>
					</td>
				</tr>
				<tr>
					<th></th>
					<td id="js_used_work_list">
						<jsp:include page="/jsp/content/settings/used_work_list.jsp">
							<jsp:param value="" name="userId"/>
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

</div>
<!-- 전체 레이아웃//-->
