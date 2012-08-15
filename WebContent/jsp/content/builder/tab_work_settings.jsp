<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.model.community.info.CommunityInfo"%>
<%@page import="net.smartworks.model.work.SmartForm"%>
<%@page import="net.smartworks.model.security.EditPolicy"%>
<%@page import="net.smartworks.model.security.WritePolicy"%>
<%@page import="net.smartworks.model.security.AccessPolicy"%>
<%@page import="net.smartworks.model.work.InformationWork"%>
<%@page import="net.smartworks.model.work.FormField"%>
<%@page import="net.smartworks.util.SmartMessage"%>
<%@page import="net.smartworks.model.work.SmartWork"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 현재사용자 정보도 가져온다..
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();
	
	String cid = request.getParameter("cid");
	String workId = SmartUtil.getSpaceIdFromContentContext(cid);
	
	SmartWork work = (SmartWork) smartWorks.getWorkById(workId);
	
	session.setAttribute("cid", cid);
	session.removeAttribute("wid");
	session.setAttribute("smartWork", work);
%>
<script type="text/javascript">

	// 근무시간정책을 수정하기 버튼을 클릭하면, 
	// 모든정보를 JSON형식으로 Serialize해서 서버의 set_work_hour_policy.sw 서비스를 호출하여 수정한다.
	function submitForms(e) {
		var tabWorkSettings = $('.js_tab_work_settings_page');
		if(tabWorkSettings.find('.js_display_field_items tr').length==2){
			smartPop.showInfo(smartPop.ERROR, smartMessage.get('noneDisplayFieldError'));			
			return;
		}
		if (SmartWorks.GridLayout.validate(tabWorkSettings.find('form.js_validation_required'), tabWorkSettings.find('.js_profile_error_message'))) {
			var forms = tabWorkSettings.find('form');
			var paramsJson = {};
			var workId = tabWorkSettings.attr('workId');
			paramsJson['workId'] = workId
			for(var i=0; i<forms.length; i++){
				var form = $(forms[i]);
				if(form.attr('name') === 'frmSmartForm'){
					paramsJson['formId'] = form.attr('formId');
					paramsJson['formName'] = form.attr('formName');
				}
				paramsJson[form.attr('name')] = mergeObjects(form.serializeObject(), SmartWorks.GridLayout.serializeObject(form));
			}
			console.log(JSON.stringify(paramsJson));
			
			var url = "set_work_settings.sw";
			var confirmMessage = smartMessage.get("saveConfirmation");
			smartPop.confirm( confirmMessage, function(){
				var progressSpan = tabWorkSettings.find('.js_progress_span');
				smartPop.progressCont(progressSpan);
				$.ajax({
					url : url,
					contentType : 'application/json',
					type : 'POST',
					data : JSON.stringify(paramsJson),
					success : function(data, status, jqXHR) {
						// 사용자정보 수정이 정상적으로 완료되었으면, 현재 페이지에 그대로 있는다.
						smartPop.showInfo(smartPop.INFO, smartMessage.get('setWorkSettingsSucceed'), function(){
							document.location.href = "tab_work_settings.sw?cid=bd.sp."+ workId;					
						});
						smartPop.closeProgress();
					},
					error : function(e) {
						smartPop.showInfo(smartPop.ERROR, smartMessage.get('setWorkSettingsError'));
						smartPop.closeProgress();
					}
				});
			});
		}
	};
</script>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!-- 컨텐츠 레이아웃-->
<div class="section_portlet js_tab_work_settings_page" workId="<%=workId%>">
	<div class="portlet_t"><div class="portlet_tl"></div></div>
	<div class="portlet_l" style="display: block;">
		<ul class="portlet_r" style="display: block;">
			<!-- 타이틀 -->
			<div class="body_titl">
				<div class="body_titl_iworks title"><%=work.getName() %></div>
				<span class="t_location"><%=work.getPathName() %></span>
				<!-- tab -->
				<div id="" class="tab_adm fr">
					<ul class="cb">
						<li><div><a href="tab_workbench.sw?cid=<%=cid %>" class="js_tab_smart_builder"><span class="btn_wo_adm"></span><fmt:message key="builder.title.work_definition"/></a></div></li>
						<li class="current"><div><a><span class="btn_set_adm"></span><fmt:message key="builder.title.work_settings"/></a></div></li>
						<li><div class="end"><a href="tab_work_sharing.sw?cid=<%=cid %>" class="js_tab_smart_builder"><span class="btn_app_shar"></span><fmt:message key="builder.title.work_sharing"/></a></div></li>
					</ul>
				</div>
				<!-- tab//-->
				<div class="solid_line cb"></div>
			</div>
			<!-- 타이틀 -->
			<!-- 컨텐츠 -->
			<div class="contents_space">
		
				<!-- 타이틀 영역 -->
				<%
				String title = "";
				switch(work.getType()){
				case SmartWork.TYPE_INFORMATION : 
					title = SmartMessage.getString("builder.title.iwork_settings");
					break;
				case SmartWork.TYPE_PROCESS :
					title = SmartMessage.getString("builder.title.pwork_settings");
					break;
				case SmartWork.TYPE_SCHEDULE :
					title = SmartMessage.getString("builder.title.swork_settings");
					break;
				}
				%>
				<div class="default_title_space"><div class="title"><%=title %></div></div>
				<!-- 타이틀 영역// -->
				
				<!-- 목록 -->
				<form name="frmWorkSettings" class="list_contents js_validation_required">

					<%
					if(work.getType() == SmartWork.TYPE_INFORMATION){
						InformationWork informationWork = (InformationWork)work;
						SmartForm smartForm = informationWork.getForm();
						FormField[] formFields = null;
						if(smartForm != null)
							formFields = smartForm.getFields();
						FormField[] displayFields = informationWork.getDisplayFields();
					%>
						<!-- 보이는 항목,안보이는 항목 설정-->
						<div class="oh">
							<div class="left40 gray_style">
								<table class="js_display_field_items">
									<tr>
										<th width="20px"><fmt:message key="builder.title.key_field"/></th>
										<th><fmt:message key="builder.title.display_fields"/></th>
										<th width="50px"><fmt:message key="builder.title.move_item"/></th>
									</tr>
									<tr class="list_action_item" style="display:none">
										<td class="rdo_key_field" >
											<div class="key_option">
												<a href=""><div class="icon_key js_key_field"></div></a>
											</div>										
										</td>
										<td class="js_input_display_field"></td>
										<td class="btn_move_field" >
											<span class="move_actions">
												<span class="js_up_field_item" title="<fmt:message key="builder.button.move_up_item"/>"><a href="" class="icon_up"></a></span>
												<span style="display:none" class="js_down_field_item" title="<fmt:message key="builder.button.move_down_item"/>"><a href="" class="icon_down"></a></span>
												<span class="js_hide_field_item" title="<fmt:message key="builder.button.hide_item"/>"><a href="" class="icon_hide"></a></span>
											</span>
										</td>
									</tr>
									<%
									if(!SmartUtil.isBlankObject(displayFields) && displayFields.length>0){
										String keyId = (SmartUtil.isBlankObject(informationWork.getKeyField())) ? displayFields[0].getId() : informationWork.getKeyField().getId();
									%>
										<input name="rdoKeyField" type="hidden" value="<%=keyId%>"/>
									<%
										int count = 0;;
										for(FormField formField : displayFields){
											String checkedClass = (formField.getId().equals(keyId)) ? "checked" : "";
									%>
											<tr class="list_action_item">
												<td class="rdo_key_field" >
													
													<!-- key Option -->
													<div class="key_option <%=checkedClass %>" >
														<a href=""><div class="icon_key js_key_field <%=checkedClass %>" fieldId="<%=formField.getId() %>"></div></a>
													</div>
										
												</td>
												
												<td><input name="hdnDisplayFields" type="hidden" value="<%=formField.getId()%>" fieldName="<%=formField.getName() %>"/><%=formField.getName() %></td>
												<td class="btn_move_field" >
													<span class="move_actions">
														<span <%if(count==0){ %>style="display:none"<%} %> class="js_up_field_item" title="<fmt:message key="builder.button.move_up_item"/>"><a href="" class="icon_up"></a></span>
														<span <%if(count==displayFields.length-1){ %>style="display:none"<%} %> class="js_down_field_item icon_down" title="<fmt:message key="builder.button.move_down_item"/>" ><a href="" class="icon_down"></a></span>
														<span class="js_hide_field_item" title="<fmt:message key="builder.button.hide_item"/>"><a href="" class="icon_hide"></a></span>
													</span>
												</td>
											</tr>
										<%
											count++;
										}
										%>
									<%
									}
									%>
								</table>
							</div>
	
							<div class="right40 gray_style">
								<table class="js_hidden_field_items">
									<tr>
										<th width="20px"><fmt:message key="builder.title.move_item"/></th>
										<th><fmt:message key="builder.title.hidden_fields"/></th>
									</tr>
									<tr class="list_action_item" style="display:none"> 
										<td class="btn_move_field">
											<span class="move_actions" title="<fmt:message key="builder.button.show_item"/>"><span class="js_show_field_item"><a href="" class="icon_show"></a></span></span>
										</td>
										<td></td>
									</tr>
									<%
									if(!SmartUtil.isBlankObject(formFields) && formFields.length>0){
										for(FormField formField : formFields){
											boolean isDisplayField = false;
											for(FormField disField : displayFields){
												if(formField.getId().equals(disField.getId())){
													isDisplayField = true;
													break;
												}
											}
											if(isDisplayField) continue;
									%>
											<tr class="list_action_item" fieldId="<%=formField.getId()%>">
												<td class="btn_move_field">
													<span class="move_actions" title="<fmt:message key="builder.button.show_item"/>"><span class="js_show_field_item"><a href="" class="icon_show"></a></span></span>
												</td>
												<td><%=formField.getName()%></td>
											</tr>
									<%
										}
									}
									%>
								</table>
							</div>
						</div>
						<!-- 보이는 항목, 안보이는 항목 //-->
	
						<div class="pt20">
							<table>
								<tbody class="js_display_field_list">
									<tr class="tit_bg">
										<%
										if(!SmartUtil.isBlankObject(informationWork.getDisplayFields()) && informationWork.getDisplayFields().length>0){
											for(FormField formField : displayFields){									
										%>					
											<th class="r_line"><%=formField.getName() %></th>
										<%
											}
										}else if(!SmartUtil.isBlankObject(formFields)){
											FormField formField = formFields[0];
										%>
											<th class="r_line"><%=formField.getName() %></th>
										<%
										}
										%>
										<th class="r_line"><fmt:message key='common.title.last_modifier' /> / <fmt:message key='common.title.last_modified_date' /></th>
										<th style="display:none" class="r_line"></th>
									</tr>
								</tbody>
							</table>
						</div>
						<div class="mt10 gray_style">
							<table>
								<tr>
									<th width="30%"><fmt:message key="builder.title.key_duplication"/></th>
									<td width="70%">
										<input type="radio" name="radKeyDuplicable" value="true" <%if(informationWork.isKeyDuplicatable()){ %>checked<%} %>>
										<span <%if(informationWork.isKeyDuplicatable()){ %>class="tb"<%} %>><fmt:message key="builder.title.key_dup.true"/></span> 
										<input type="radio" name="radKeyDuplicable" value="false" <%if(!informationWork.isKeyDuplicatable()){ %>checked<%} %>>
										<span <%if(!informationWork.isKeyDuplicatable()){ %>class="tb"<%} %>><fmt:message key="builder.title.key_dup.false"/></span> 
									</td>
								</tr>
							</table>
						</div>
					<%
					}
					
					%>
					
					<!-- 권한 -->
					<%
					int accessLevel = (SmartUtil.isBlankObject(work.getAccessPolicy())) ? AccessPolicy.LEVEL_DEFAULT : work.getAccessPolicy().getLevel();
					int writeLevel = (SmartUtil.isBlankObject(work.getWritePolicy())) ? WritePolicy.LEVEL_DEFAULT : work.getWritePolicy().getLevel();
					int editLevel = (SmartUtil.isBlankObject(work.getEditPolicy())) ? EditPolicy.LEVEL_DEFAULT : work.getEditPolicy().getLevel();
					CommunityInfo[] communities = null;
					
					%>
					<div class="mt10 gray_style">
						<table>
							<tr>
								<th width="30%"><fmt:message key="common.security.title.access"/></th>
								<td width="70%" class="js_select_access_level">
									<input name="rdoAccessLevel" type="radio" value="<%=AccessPolicy.LEVEL_PUBLIC %>" <%if(accessLevel==AccessPolicy.LEVEL_PUBLIC){ %>checked<%} %>/>
									<span <%if(accessLevel==AccessPolicy.LEVEL_PUBLIC){ %>class="tb"<%} %>><fmt:message key="common.security.access.public"/> <fmt:message key="common.security.default"/></span> 
									<input name="rdoAccessLevel" type="radio" value="<%=AccessPolicy.LEVEL_PRIVATE %>"  <%if(accessLevel==AccessPolicy.LEVEL_PRIVATE){ %>checked<%} %>/>
									<span <%if(accessLevel==AccessPolicy.LEVEL_PRIVATE){ %>class="tb"<%} %>><fmt:message key="common.security.access.private"/></span> 
									<input name="rdoAccessLevel" class="js_security_level_custom" type="radio" value="<%=AccessPolicy.LEVEL_CUSTOM %>"  <%if(accessLevel==AccessPolicy.LEVEL_CUSTOM){ %>checked<%} %>/>
									<span <%if(accessLevel==AccessPolicy.LEVEL_CUSTOM){ %>class="tb"<%} %>><fmt:message key="common.security.access.custom"/></span>
									<div class="js_access_level_custom" <%if(accessLevel!=AccessPolicy.LEVEL_CUSTOM) {%>style="display:none"<%} %>>
										<span class="form_col js_type_userField" fieldId="txtAccessableUsers" multiUsers="true">
											<div class="w100 form_value">
												<div class="icon_fb_space">
													<div class="fieldline community_names js_community_names sw_required">
														<%
														communities = work.getAccessPolicy().getCommunitiesToOpen();
														if(!SmartUtil.isBlankObject(communities)){
															for(int i=0; i<communities.length; i++){
																CommunityInfo community = communities[i];
																String comName = (community.getClass().equals(UserInfo.class)) ? ((UserInfo)community).getLongName() : community.getName();
														%>
																<span class="js_community_item user_select" comId="<%=community.getId() %>"><%=comName %><a class="js_remove_community" href="">&nbsp;x</a></span>		
														
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
						</table>
					</div>
					<!-- 권한 //-->

					<!-- 권한 -->
					<div class="mt10 gray_style">
						<table>
							<tr>
								<th width="30%"><fmt:message key="common.security.title.write"/></th>
								<td width="70%" class="js_select_write_level">
									<input name="rdoWriteLevel" type="radio" value="<%=WritePolicy.LEVEL_PUBLIC %>" <%if(writeLevel==WritePolicy.LEVEL_PUBLIC){ %>checked<%} %>/>
									<span <%if(writeLevel==WritePolicy.LEVEL_PUBLIC){ %>class="tb"<%} %>><fmt:message key="common.security.write.public"/> <fmt:message key="common.security.default"/></span> 
									<input name="rdoWriteLevel" class="js_security_level_custom" type="radio" value="<%=WritePolicy.LEVEL_CUSTOM %>" <%if(writeLevel==WritePolicy.LEVEL_CUSTOM){ %>checked<%} %>/>
									<span <%if(writeLevel==WritePolicy.LEVEL_CUSTOM){ %>class="tb"<%} %>><fmt:message key="common.security.write.custom"/></span>
									<div class="js_write_level_custom" <%if(writeLevel!=WritePolicy.LEVEL_CUSTOM) {%>style="display:none"<%} %>>
										<span class="form_col js_type_userField" fieldId="txtWritableUsers" multiUsers="true">
											<div class="w100 form_value">
												<div class="icon_fb_space">
													<div class="fieldline community_names js_community_names sw_required">
														<%
														communities = work.getWritePolicy().getCommunitiesToWrite();
														if(!SmartUtil.isBlankObject(communities)){
															for(int i=0; i<communities.length; i++){
																CommunityInfo community = communities[i];
																String comName = (community.getClass().equals(UserInfo.class)) ? ((UserInfo)community).getLongName() : community.getName();
														%>
																<span class="js_community_item user_select" comId="<%=community.getId() %>"><%=comName %><a class="js_remove_community" href="">&nbsp;x</a></span>		
														
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
						</table>
					</div>
					<!-- 권한 //-->

					<!-- 권한 -->
					<div class="mt10 gray_style">
						<table>
							<tr>
								<th width="30%"><fmt:message key="common.security.title.edit"/></th>
								<td width="70%" class="js_select_edit_level">
									<input name="rdoEditLevel" type="radio" value="<%=EditPolicy.LEVEL_PUBLIC %>" <%if(editLevel==EditPolicy.LEVEL_PUBLIC){ %>checked<%} %>/>
									<span <%if(editLevel==EditPolicy.LEVEL_PUBLIC){ %>class="tb"<%} %>><fmt:message key="common.security.edit.public"/></span> 
									<input name="rdoEditLevel" type="radio" value="<%=EditPolicy.LEVEL_PRIVATE %>"  <%if(editLevel==EditPolicy.LEVEL_PRIVATE){ %>checked<%} %>/>
									<span <%if(editLevel==EditPolicy.LEVEL_PRIVATE){ %>class="tb"<%} %>><fmt:message key="common.security.edit.private"/> <fmt:message key="common.security.default"/></span> 
									<input name="rdoEditLevel" class="js_security_level_custom" type="radio" value="<%=EditPolicy.LEVEL_CUSTOM %>"  <%if(editLevel==EditPolicy.LEVEL_CUSTOM){ %>checked<%} %>/>
									<span <%if(editLevel==EditPolicy.LEVEL_CUSTOM){ %>class="tb"<%} %>><fmt:message key="common.security.edit.custom"/></span>
									<div class="js_edit_level_custom" <%if(editLevel!=EditPolicy.LEVEL_CUSTOM) {%>style="display:none"<%} %>>
										<span class="form_col js_type_userField" fieldId="txtEditableUsers" multiUsers="true">
											<div class="w100 form_value">
												<div class="icon_fb_space">
													<div class="fieldline community_names js_community_names sw_required">
														<%
														communities = work.getEditPolicy().getCommunitiesToEdit();
														if(!SmartUtil.isBlankObject(communities)){
															for(int i=0; i<communities.length; i++){
																CommunityInfo community = communities[i];
																String comName = (community.getClass().equals(UserInfo.class)) ? ((UserInfo)community).getLongName() : community.getName();
														%>
																<span class="js_community_item user_select" comId="<%=community.getId() %>"><%=comName %><a class="js_remove_community" href="">&nbsp;x</a></span>		
														
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
						</table>
					</div>
					<!-- 권한 //-->					

					<!-- 권한 -->
					<div class="mt10 gray_style">
						<table>
							<tr>
								<th width="30%"><fmt:message key="common.security.title.builder"/></th>
								<td width="70%" class="js_select_edit_builder">
									<input name="chkBuilderPolicyAdministrator" disabled type="checkbox" <%if(work.getBuilderPolicy().isSystemAdministratorChecked()){ %>checked<%} %> /><label><fmt:message key="organization.user_level.administrator"/></label> 
									<input name="chkBuilderPolicyCustom" type="checkbox" class="js_toggle_builder_policy_custom" <%if(work.getBuilderPolicy().isCustomChecked()){ %>checked<%} %> /><label><fmt:message key="common.security.custom"/></label>
									<div class="js_builder_policy_custom" <%if(!work.getBuilderPolicy().isCustomChecked()) {%>style="display:none"<%} %>>
										<span class="form_col js_type_userField" fieldId="txtBuilderPolicyCustoms" multiUsers="true">
											<div class="w100 form_value">
												<div class="icon_fb_space">
													<div class="fieldline community_names js_community_names sw_required">
														<%
														UserInfo[] users = work.getBuilderPolicy().getCustoms();
														if(!SmartUtil.isBlankObject(users)){
															for(int i=0; i<users.length; i++){
																UserInfo user = users[i];
														%>
																<span class="js_community_item user_select" comId="<%=user.getId() %>"><%=user.getLongName() %><a class="js_remove_community" href="">&nbsp;x</a></span>								
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
						</table>
					</div>
					<!-- 권한 //-->	
					</form>				
				<!-- 목록 //-->
				</div>
				<!-- 컨텐츠 //-->
				
				<!-- 버튼영역 -->
				<div class="glo_btn_space">
					<!-- 실행시 데이터 유효성 검사이상시 에러메시지를 표시할 공간 -->
					<span class="form_space sw_error_message js_profile_error_message" style="text-align:right; color: red"></span>
					<!--  실행시 표시되는 프로그래스아이콘을 표시할 공간 -->
					<span class="js_progress_span"></span>
					
					<div class="fr">
						<span class="btn_gray"> 
							<a href="" onclick='submitForms(); return false;'>
								<span class="txt_btn_start"></span>
								<span class="txt_btn_center"><fmt:message key="common.button.modify"/></span>
								<span class="txt_btn_end"></span>
							</a>
						</span> 
					</div>
				</div>
				<!-- 버튼영역 //-->
		</ul>		
	</div>	
	<div class="portlet_b" style="display: block;"></div>
</div>
<!-- 컨텐츠 레이아웃//-->
