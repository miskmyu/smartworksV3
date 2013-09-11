<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.model.community.Community"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.model.community.Group"%>
<%@page import="net.smartworks.model.community.Department"%>
<%@page import="net.smartworks.model.instance.info.MemoInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.ImageInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.FileInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.EventInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.BoardInstanceInfo"%>
<%@page import="net.smartworks.model.community.info.WorkSpaceInfo"%>
<%@page import="net.smartworks.model.instance.Instance"%>
<%@page import="net.smartworks.model.work.SocialWork"%>
<%@page import="net.smartworks.model.work.Work"%>
<%@page import="net.smartworks.model.work.SmartWork"%>
<%@page import="net.smartworks.model.instance.info.WorkInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.TaskInstanceInfo"%>
<%@page import="net.smartworks.model.instance.TaskInstance"%>
<%@page import="net.smartworks.model.calendar.CompanyEvent"%>
<%@page import="net.smartworks.util.SmartMessage"%>
<%@page import="net.smartworks.model.calendar.WorkHourPolicy"%>
<%@page import="net.smartworks.model.community.WorkSpace"%>
<%@page import="java.util.Calendar"%>
<%@page import="net.smartworks.model.calendar.CompanyCalendar"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();

	WorkSpace workSpace = (WorkSpace)session.getAttribute("workSpace");
	Group group = (Group)workSpace;
	WorkSpaceInfo[] users = null;
	
%>
<script type="text/javascript">

	// 개인정보프로파일 수정하기 버튼을 클릭하면, 
	// 모든정보를 JSON형식으로 Serialize해서 서버의 update_my_profile.sw 서비스를 호출하여 수정한다.
	function submitForms(e) {
		var spaceTabGroupSetting = $('.js_space_tab_group_setting_page');
		var maxMembers = spaceTabGroupSetting.find('input[name="rdoGroupMaxMembers"]:checked');
		if(maxMembers.attr('value') == "UserDefined"){
			spaceTabGroupSetting.find('input[name="txtGroupMaxMembers"]').addClass('required');
		}else{
			spaceTabGroupSetting.find('input[name="txtGroupMaxMembers"]').removeClass('required');			
		}
		if (SmartWorks.GridLayout.validate(spaceTabGroupSetting.find('form.js_validation_required'), $('.js_profile_error_message'))) {
			var forms = spaceTabGroupSetting.find('form');
			var groupId = spaceTabGroupSetting.attr('groupId');
			var paramsJson = {};
			paramsJson['groupId'] = groupId;
			for(var i=0; i<forms.length; i++){
				var form = $(forms[i]);
				if(form.attr('name') === 'frmSmartForm'){
					paramsJson['formId'] = form.attr('formId');
					paramsJson['formName'] = form.attr('formName');
				}
				paramsJson[form.attr('name')] = mergeObjects(form.serializeObject(), SmartWorks.GridLayout.serializeObject(form));
			}
			console.log(JSON.stringify(paramsJson));
			var url = "update_group_setting.sw";
			var progressSpan = spaceTabGroupSetting.find('.js_progress_span');
			smartPop.progressCont(progressSpan);
			$.ajax({
				url : url,
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					// 사용자정보 수정이 정상적으로 완료되었으면, 현재 페이지에 그대로 있는다.
					document.location.href = "group_space.sw?cid=gp.sp." + groupId + "&wid=" + groupId;
					smartPop.closeProgress();
				},
				error : function(e) {
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.ERROR, smartMessage.get('setGroupSpaceError'));
				}
			});
		}
	};
</script>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<div class="section_portlet js_space_tab_group_setting_page js_space_tab_group" groupId=<%=group.getId() %>>
	<div class="portlet_t">
		<div class="portlet_tl"></div>
	</div>
	<div class="portlet_l" style="display: block;">
		<div class="portlet_r" style="display: block;">
			<!-- 화면 -->
			<div class="contents_space setting_section" style="display: table-cell">
				<div class="titl_section">
					<!-- 타이틀을 나타내는 곳 -->
					<div class="title pr10 fl">
						<span class="current"> <a href="" class="js_select_group_space_tab js_setting"><fmt:message key="common.title.space_setting"/></a> </span> | <span class=" ">
							<a href="" class="js_select_group_space_tab js_members"><fmt:message key="group.title.members"/></a> </span>
					</div>
				</div>
				<div class="solid_line mb10"></div>
				<!-- 설정 부분 -->
				<form name="frmGroupSpaceSetting" class="js_validation_required">
					<!-- Left -->
					<div class="photo_section">
						<div class="js_group_profile_field" imgSource="<%=group.getOrgPicture() %>"></div>
						<div class="t_s11"><fmt:message key="profile.title.size_desc"/></div>
					</div>
					<!-- Left//-->
					<!--Right-->
					<div class="table_normal600 fl">
						<table>
							<tbody>
								<tr>
									<th class="required_label"><fmt:message key="group.title.name"/></th>
									<td><input name="txtGroupName" type="text" class="fieldline required" value="<%=group.getName() %>"/></td>
								</tr>
								<tr>
									<th><fmt:message key="group.title.desc"/></th>
									<td><textarea name="txtaGroupDesc" class="fieldline"><%=CommonUtil.toNotNull(group.getDesc()) %></textarea></td>
								</tr>
								<tr>
									<th class="required_label"><fmt:message key="group.title.leader"/></th>
									<td>
										<div>
											<span class="form_col js_type_userField" fieldId="txtGroupLeader" multiUsers="false">
												<div class="w100 form_value">
													<div class="icon_fb_space">
														<div class="fieldline community_names js_community_names sw_required">
															<%
															if(!SmartUtil.isBlankObject(group.getLeader())){
															%>
																<span class="js_community_item user_select" comId="<%=group.getLeader().getId() %>" comName="<%=group.getLeader().getLongName() %>"><%=group.getLeader().getLongName() %><a class="js_remove_community" href="">&nbsp;x</a></span>								
															<%
															}
															%>
															<input class="m0 js_auto_complete" href="user_name.sw" type="text" style="width:100px">
														</div>
														<div class="js_community_list srch_list_nowid" style="display: none"></div>
														<span class="js_community_popup"></span>
														<a href="" class="js_userpicker_button"><span class="icon_fb_user"></span></a>
													</div>
												</div>
											</span>
										</div>
									</td>
								</tr>
								<tr>
									<th><fmt:message key="group.title.max_members"/></th>
									<td>
										<input name="rdoGroupMaxMembers" type="radio" <%if(group.getMaxMembers()==Group.MAX_MEMBERS_UNLIMITED){ %>checked<%} %> value="<%=Group.MAX_MEMBERS_UNLIMITED %>" /><label><fmt:message key="group.label.unlimited_members"/></label>
										<%
											String maxMember = group.getMaxMembers() != Group.MAX_MEMBERS_UNLIMITED ? group.getMaxMembers() + "" : "";
										%>	
										<input name="rdoGroupMaxMembers" type="radio" <%if(group.getMaxMembers()!=Group.MAX_MEMBERS_UNLIMITED){ %>checked<%} %> value="UserDefined" /><label><input name="txtGroupMaxMembers" type="text" class="fieldline tr number" style="width: 50px" value="<%=maxMember%>"/> <fmt:message key="group.label.members"/>
										</label>
									</td>
								</tr>
								<tr>
									<th><fmt:message key="group.title.join_approval"/></th>
									<td>
										<input name="rdoGroupAutoApproval" type="radio" <%if(group.isAutoApproval()){ %>checked<%} %> value="true" /><label><fmt:message key="group.label.auto_approval"/></label>
										<input name="rdoGroupAutoApproval" type="radio" <%if(!group.isAutoApproval()){ %>checked<%} %> value="false" /><label><fmt:message key="group.label.leader_approval"/></label>
									</td>
								</tr>
								<%-- <tr>
									<th><fmt:message key="group.title.invitable_members"/></th>
									<td>
										<input name="chkInvitableMembersLeader" type="checkbox" <%if(group.getInvitableMembers().isLeaderChecked()){ %>checked<%} %> /><label><fmt:message key="group.title.leader"/></label>
										<input name="chkInvitableMembersMembers" type="checkbox" <%if(group.getInvitableMembers().isMembersChecked()){ %>checked<%} %> /><label><fmt:message key="group.title.members"/></label>
										<input name="chkInvitableMembersCustom" type="checkbox" class="js_toggle_policy_custom" <%if(group.getInvitableMembers().isCustomChecked()){ %>checked<%} %> /><label><fmt:message key="common.security.custom"/></label>
										<div class="js_space_policy_custom" <%if(!group.getInvitableMembers().isCustomChecked()) {%>style="display:none"<%} %>>
											<span class="form_col js_type_userField" fieldId="txtInvitableMembersCustoms" multiUsers="true">
												<div class="w100 form_value">
													<div class="icon_fb_space">
														<div class="fieldline community_names js_community_names sw_required">
															<%
															users = group.getInvitableMembers().getCustoms();
															if(!SmartUtil.isBlankObject(users)){
																for(int i=0; i<users.length; i++){
																	UserInfo user = users[i];
															%>
																	<span class="js_community_item user_select" comId="<%=user.getId() %>"><%=user.getLongName() %><a class="js_remove_community" href="">&nbsp;x</a></span>								
															<%
																}
															}
															
															%>
															<input class="m0 js_auto_complete" href="user_name.sw" type="text" style="width:100px">
														</div>
														<div class="js_community_list srch_list_nowid" style="display: none"></div>
														<span class="js_community_popup"></span>
														<a href="" class="js_userpicker_button"><span class="icon_fb_users"></span></a>
													</div>
												</div>
											</span>
										</div>
									</td>
								</tr> --%>
								<tr>
									<th><fmt:message key="group.title.board.write_policy"/></th>
									<td>
										<input name="chkBoardWriteLeader" type="checkbox" <%if(group.getBoardWritePolicy().isLeaderChecked()){ %>checked<%} %> /><label><fmt:message key="group.title.leader"/></label> 
										<input name="chkBoardWriteMembers" type="checkbox" <%if(group.getBoardWritePolicy().isMembersChecked()){ %>checked<%} %> /><label><fmt:message key="group.title.members"/></label> 
										<input name="chkBoardWriteCustom" type="checkbox" class="js_toggle_policy_custom" <%if(group.getBoardWritePolicy().isCustomChecked()){ %>checked<%} %> /><label><fmt:message key="common.security.custom"/></label> <!-- 사용자 지정 -->
										<div class="js_space_policy_custom" <%if(!group.getBoardWritePolicy().isCustomChecked()) {%>style="display:none"<%} %>>
											<span class="form_col js_type_userField" fieldId="txtBoardWriteCustoms" multiUsers="true">
												<div class="w100 form_value">
													<div class="icon_fb_space">
														<div class="fieldline community_names js_community_names sw_required">
															<%
															users = group.getBoardWritePolicy().getCustoms();
															if(!SmartUtil.isBlankObject(users)){
																for(int i=0; i<users.length; i++){
																	WorkSpaceInfo user = users[i];
															%>
																	<span class="js_community_item user_select" comId="<%=user.getId() %>" comName="<%=user.getLongName() %>"><%=user.getLongName() %><a class="js_remove_community" href="">&nbsp;x</a></span>								
															<%
																}
															}
															
															%>
															<input class="m0 js_auto_complete" href="community_name.sw" type="text" style="width:100px">
														</div>
														<div class="js_community_list srch_list_nowid" style="display: none"></div>
														<span class="js_community_popup"></span>
														<a href="" class="js_userpicker_button"><span class="icon_fb_users"></span></a>
													</div>
												</div>
											</span>
										</div>
									</td>
								</tr>
								<tr>
									<th><fmt:message key="group.title.board.edit_policy"/></th>
									<td>
										<input name="chkBoardEditOwner" type="checkbox" <%if(group.getBoardEditPolicy().isOwnerChecked()){ %>checked<%} %> /><label><fmt:message key="common.security.owner"/></label>
										<input name="chkBoardEditAdministrator" type="checkbox" <%if(group.getBoardEditPolicy().isSystemAdministratorChecked()){ %>checked<%} %> /><label><fmt:message key="organization.user_level.administrator"/></label>
										<input name="chkBoardEditLeader" type="checkbox" <%if(group.getBoardEditPolicy().isLeaderChecked()){ %>checked<%} %> /><label><fmt:message key="group.title.leader"/></label>
										<input name="chkBoardEditCustom" type="checkbox" class="js_toggle_policy_custom" <%if(group.getBoardEditPolicy().isCustomChecked()){ %>checked<%} %> /><label><fmt:message key="common.security.custom"/></label>
										<div class="js_space_policy_custom" <%if(!group.getBoardEditPolicy().isCustomChecked()) {%>style="display:none"<%} %>>
											<span class="form_col js_type_userField" fieldId="txtBoardEditCustoms" multiUsers="true">
												<div class="w100 form_value">
													<div class="icon_fb_space">
														<div class="fieldline community_names js_community_names sw_required">
															<%
															users = group.getBoardEditPolicy().getCustoms();
															if(!SmartUtil.isBlankObject(users)){
																for(int i=0; i<users.length; i++){
																	WorkSpaceInfo user = users[i];
															%>
																	<span class="js_community_item user_select" comId="<%=user.getId() %>" comName="<%=user.getLongName() %>"><%=user.getLongName() %><a class="js_remove_community" href="">&nbsp;x</a></span>								
															<%
																}
															}
															
															%>
															<input class="m0 js_auto_complete" href="community_name.sw" type="text" style="width:100px">
														</div>
														<div class="js_community_list srch_list_nowid" style="display: none"></div>
														<span class="js_community_popup"></span>
														<a href="" class="js_userpicker_button"><span class="icon_fb_users"></span></a>
													</div>
												</div>
											</span>
										</div>
									</td>
								</tr>
								<tr>
									<th><fmt:message key="group.title.event.write_policy"/></th>
									<td>
										<input name="chkEventWriteLeader" type="checkbox" <%if(group.getEventWritePolicy().isLeaderChecked()){ %>checked<%} %> /><label><fmt:message key="group.title.leader"/></label> 
										<input name="chkEventWriteMembers" type="checkbox" <%if(group.getEventWritePolicy().isMembersChecked()){ %>checked<%} %> /><label><fmt:message key="group.title.members"/></label> 
										<input name="chkEventWriteCustom" type="checkbox" class="js_toggle_policy_custom" <%if(group.getEventWritePolicy().isCustomChecked()){ %>checked<%} %> /><label><fmt:message key="common.security.custom"/></label>
										<div class="js_space_policy_custom" <%if(!group.getEventWritePolicy().isCustomChecked()) {%>style="display:none"<%} %>>
											<span class="form_col js_type_userField" fieldId="txtEventWriteCustoms" multiUsers="true">
												<div class="w100 form_value">
													<div class="icon_fb_space">
														<div class="fieldline community_names js_community_names sw_required">
															<%
															users = group.getEventWritePolicy().getCustoms();
															if(!SmartUtil.isBlankObject(users)){
																for(int i=0; i<users.length; i++){
																	WorkSpaceInfo user = users[i];
															%>
																	<span class="js_community_item user_select" comId="<%=user.getId() %>" comName="<%=user.getLongName() %>"><%=user.getLongName() %><a class="js_remove_community" href="">&nbsp;x</a></span>								
															<%
																}
															}
															
															%>
															<input class="m0 js_auto_complete" href="community_name.sw" type="text" style="width:100px">
														</div>
														<div class="js_community_list srch_list_nowid" style="display: none"></div>
														<span class="js_community_popup"></span>
														<a href="" class="js_userpicker_button"><span class="icon_fb_users"></span></a>
													</div>
												</div>
											</span>
										</div>
									</td>
								</tr>
								<tr>
									<th><fmt:message key="group.title.event.edit_policy"/></th>
									<td>
										<input name="chkEventEditOwner" type="checkbox" <%if(group.getEventEditPolicy().isOwnerChecked()){ %>checked<%} %> /><label><fmt:message key="common.security.owner"/></label>
										<input name="chkEventEditAdministrator" type="checkbox" <%if(group.getEventEditPolicy().isSystemAdministratorChecked()){ %>checked<%} %> /><label><fmt:message key="organization.user_level.administrator"/></label> 
										<input name="chkEventEditLeader" type="checkbox" <%if(group.getEventEditPolicy().isLeaderChecked()){ %>checked<%} %> /><label><fmt:message key="group.title.leader"/></label>
										<input name="chkEventEditCustom" type="checkbox" class="js_toggle_policy_custom" <%if(group.getEventEditPolicy().isCustomChecked()){ %>checked<%} %> /><label><fmt:message key="common.security.custom"/></label>
										<div class="js_space_policy_custom" <%if(!group.getEventEditPolicy().isCustomChecked()) {%>style="display:none"<%} %>>
											<span class="form_col js_type_userField" fieldId="txtEventEditCustoms" multiUsers="true">
												<div class="w100 form_value">
													<div class="icon_fb_space">
														<div class="fieldline community_names js_community_names sw_required">
															<%
															users = group.getEventEditPolicy().getCustoms();
															if(!SmartUtil.isBlankObject(users)){
																for(int i=0; i<users.length; i++){
																	WorkSpaceInfo user = users[i];
															%>
																	<span class="js_community_item user_select" comId="<%=user.getId() %>" comName="<%=user.getLongName() %>"><%=user.getLongName() %><a class="js_remove_community" href="">&nbsp;x</a></span>								
															<%
																}
															}
															
															%>
															<input class="m0 js_auto_complete" href="community_name.sw" type="text" style="width:100px">
														</div>
														<div class="js_community_list srch_list_nowid" style="display: none"></div>
														<span class="js_community_popup"></span>
														<a href="" class="js_userpicker_button"><span class="icon_fb_users"></span></a>
													</div>
												</div>
											</span>
										</div>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
					<!-- Right //-->
				</form>
				<!-- 설정 부분 //-->
			</div>
			<!-- 화면 //-->
			<!-- 버튼 영역 -->
			<div class="glo_btn_space">
				<div class="fr ml10">
					<span class="btn_gray">
						<a onclick="submitForms(); return false;" href="">
							<span class="txt_btn_start"></span> 
							<span class="txt_btn_center"><fmt:message key="common.button.modify"/></span>
							<span class="txt_btn_end"></span> 
						</a> 
					</span> 
					<span class="btn_gray ml5">
						<a onclick="return true;" href="<%=group.getSpaceController()%>?cid=<%=group.getSpaceContextId()%>&wid=<%=group.getId()%>"> 
							<span class="txt_btn_start"></span>
							<span class="txt_btn_center"><fmt:message key="common.button.cancel"/></span> 
							<span class="txt_btn_end"></span>
						</a> 
					</span>
				</div>
				<!-- 실행시 표시되는 프로그래스아이콘을 표시할 공간 -->
				<span class="fr form_space js_progress_span"></span> 
				<!-- 실행시 데이터 유효성 검사이상시 에러메시지를 표시할 공간 -->
				<div class="form_space sw_error_message js_profile_error_message" style="text-align: right; color: red; line-height: 20px"></div>
			</div>
			<!-- 버튼 영역 //-->
		</div>
	</div>
	<div class="portlet_b" style="display: block;"></div>
</div>

<script>
loadGroupProfileField();
</script>
