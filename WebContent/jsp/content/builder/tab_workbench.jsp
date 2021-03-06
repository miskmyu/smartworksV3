<%@page import="net.smartworks.model.work.WorkCategory"%>
<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.model.work.Work"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
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
	boolean isEditable = (!work.isRunning() && !work.isEditing());
	String iconClass = "";
	switch(work.getType()){
	case SmartWork.TYPE_INFORMATION:
		iconClass = "body_titl_iworks";
		break;
	case SmartWork.TYPE_PROCESS:
		iconClass = "body_titl_pworks";
		break;
	case SmartWork.TYPE_SCHEDULE:
		iconClass = "body_titl_sworks";
		break;
	}
	
	String categoryId = work.getMyCategory().getId();
	String groupId = (SmartUtil.isBlankObject(work.getMyGroup())) ? "" : work.getMyGroup().getId();
	
	String categoryIconClass = (SmartUtil.isBlankObject(work.getMyGroup())) ? work.getMyCategory().getIconClass() : work.getMyGroup().getIconClass();
	
	session.setAttribute("cid", cid);
	session.removeAttribute("wid");
	session.setAttribute("smartWork", work);
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!-- 컨텐츠 레이아웃-->
<div class="section_portlet js_tab_workbench_page" workId="<%=workId%>" isEditable="<%=isEditable%>" categoryId="<%=categoryId%>" groupId="<%=groupId%>">
	<div class="portlet_t"><div class="portlet_tl"></div></div>
	<div class="portlet_l" style="display: block;">
		<ul class="portlet_r" style="display: block;">
			<!-- 타이틀 -->
			<div class="body_titl">
				<div class="<%=iconClass %> title"><%=work.getName() %></div>
				<span class="t_location <%=categoryIconClass%>" style="padding-top:5px;">
					<span><%=work.getPathName() %></span>
				</span>
				<!-- tab -->
				<div id="" class="tab_adm fr">
					<ul class="cb">
						<li class="current"><div><a><span class="btn_wo_adm"></span><fmt:message key="builder.title.work_definition"/></a></div></li>
						<li><div><a href="tab_work_settings.sw?cid=<%=cid %>" class="js_tab_smart_builder"><span class="btn_set_adm"></span><fmt:message key="builder.title.work_settings"/></a></div></li>
						<li><div class="end"><a href="tab_work_sharing.sw?cid=<%=cid %>" class="js_tab_smart_builder"><span class="btn_app_shar"></span><fmt:message key="builder.title.work_sharing"/></a></div></li>
					</ul>
				</div>
				<!-- tab//-->
				<div class="solid_line cb"></div>
			</div>
			<!-- 타이틀 -->
			<!-- 컨텐츠 -->
			<div class="contents_space">
				<!-- 설명 -->
				<div>
					<table cellspacing="0" cellpadding="0" border="0">
					<colgroup>
						<col width="150px">
						<col width="">
					</colgroup>
						<tbody>
							<tr>
								<td class="bul_dot vt"><fmt:message key="common.title.desc"/> :</td>
								<td class="content"><%=CommonUtil.toNotNull(work.getDesc()) %></td>
							</tr>
							<tr>
								<td class="bul_dot vt"><fmt:message key="common.title.owner"/> :</td>
								<%
								User creater = work.getCreater();
								User lastModifer = work.getLastModifier();
								String createrName = (SmartUtil.isBlankObject(work.getCreater())) ? "" : work.getCreater().getLongName();
								String createdDate = (SmartUtil.isBlankObject(work.getCreatedDate())) ? "" : work.getCreatedDate().toLocalString();
								%>
								<td class="content">
									<a href="<%=creater.getSpaceController()%>?cid=<%=creater.getSpaceContextId() %>&wid=<%=creater.getId()%>"><img class="profile_size_s" src="<%=creater.getMinPicture() %>"></a>
									<a class="userFieldLink t_name" href="<%=creater.getSpaceController()%>?cid=<%=creater.getSpaceContextId() %>&wid=<%=creater.getId()%>"><%=createrName%></a>
									<span class="t_date ml5"><%=createdDate %></span>
								</td>
							</tr>
							<tr>
								<td class="bul_dot vt"><fmt:message key="common.title.last_modifier"/> :</td>
								<td class="content">
									<a href="<%=lastModifer.getSpaceController()%>?cid=<%=lastModifer.getSpaceContextId() %>&wid=<%=lastModifer.getId()%>"><img class="profile_size_s" src="<%=lastModifer.getMinPicture() %>"></a>
									<a class="userFieldLink t_name" href="<%=lastModifer.getSpaceController()%>?cid=<%=lastModifer.getSpaceContextId() %>&wid=<%=lastModifer.getId()%>"><%=work.getLastModifier().getLongName() %></a>
									<span class="t_date ml5"><%=work.getLastModifiedDate().toLocalString() %></span>
								</td>
							</tr>
							<%
							if(!SmartUtil.isBlankObject(work.getEditingUser())){
								User editingUser = work.getEditingUser();
							%>
								<tr>
									<td class="bul_dot vt"><fmt:message key="builder.title.editing_user"/> :</td>
									<td class="content">
										<a href="<%=editingUser.getSpaceController()%>?cid=<%=editingUser.getSpaceContextId() %>&wid=<%=editingUser.getId()%>"><img class="profile_size_s" src="<%=editingUser.getMinPicture() %>"></a>
										<a class="userFieldLink t_name" href="<%=editingUser.getSpaceController()%>?cid=<%=editingUser.getSpaceContextId() %>&wid=<%=editingUser.getId()%>"><%=editingUser.getLongName() %></a>
										<span class="t_date ml5"><%=work.getEditingStartDate().toLocalString() %></span>
									</td>
								</tr>
							<%
							}
							%>
						</tbody>
					</table>
				</div>

				<table class="lh0">
					<%
					String serviceStatus = (work.isRunning()) ? 
							SmartMessage.getString("builder.title.service_started") : 
							SmartMessage.getString("builder.title.service_stopped");							
					String editingStatus = (isEditable) ? 
							SmartMessage.getString("builder.title.editable") : (work.canIStop() ? SmartMessage.getString("builder.title.editing") : 
							SmartMessage.getString("builder.title.not_editable"));
					String myCategoryId = (SmartUtil.isBlankObject(work.getMyCategory())) ? "" : work.getMyCategory().getId();
					String myGroupId = (SmartUtil.isBlankObject(work.getMyGroup())) ? "" : work.getMyGroup().getId();
					%>
					<tr>
						<td class="state">
							<fmt:message key="builder.title.service_status"/> : <span style="color: #40991b; font-weight: bold"><%=serviceStatus %></span> &nbsp;&nbsp;
							<fmt:message key="builder.title.editing_status"/> : <span style="color: #666666; font-weight: bold"><%=editingStatus %></span>
						</td>
						<span class="js_progress_span"></span>
						<td class="tr">
							<span class="btn_gray" <%if(work.isRunning() || work.isEditing()){%>style="visibility:hidden"<%} %>>
								<a href="" class="js_start_work_service">
									<span class="txt_btn_start"></span>
										<span class="txt_btn_center"><fmt:message key="builder.button.start_service"/></span>
									<span class="txt_btn_end"></span>
								</a>
							</span>
							<span class="btn_gray" <%if(!work.isRunning()){%>style="display:none"<%} %>>
								<a href="" class="js_stop_work_service">
									<span class="txt_btn_start"></span>
										<span class="txt_btn_center"><fmt:message key="builder.button.stop_service"/></span>
									<span class="txt_btn_end"></span>
								</a>
							</span>
							<span class="btn_gray" <%if(work.isRunning() || work.isEditing()){%>style="display:none"<%} %>>
								<a href="" class="js_start_work_editing">
									<span class="txt_btn_start"></span>
										<span class="txt_btn_center"><fmt:message key="builder.button.start_edit"/></span>
									<span class="txt_btn_end"></span>
								</a>
							</span>
							<span class="btn_gray" <%if(!work.canIStop()){%>style="display:none"<%} %>>
								<a href="" class="js_stop_work_editing">
									<span class="txt_btn_start"></span>
										<span class="txt_btn_center"><fmt:message key="builder.button.stop_edit"/></span>
									<span class="txt_btn_end"></span>
								</a>
							</span>
							<span class="btn_gray" <%if(work.isEditing()){%>style="display:none"<%} %>>
								<a href="" class="js_copy_work_definition" workId="<%=work.getId()%>" workFullName="<%=work.getFullpathName()%>" categoryId=<%=myCategoryId%> groupId="<%=myGroupId%>" workName="<%=work.getName()%>" workDesc="<%=work.getDesc()%>">
									<span class="txt_btn_start"></span>
										<span class="txt_btn_center"><fmt:message key="common.button.copy"/></span>
									<span class="txt_btn_end"></span>
								</a>
							</span>
							<span class="btn_gray" <%if(work.isRunning() || work.isEditing()){%>style="display:none"<%} %>>
								<a href="" class="js_move_work_definition" workId="<%=work.getId()%>" workFullName="<%=work.getFullpathName()%>">
									<span class="txt_btn_start"></span>
										<span class="txt_btn_center"><fmt:message key="common.button.move"/></span>
									<span class="txt_btn_end"></span>
								</a>
							</span>
						</td>
					</tr>
				</table>
				<!-- 설명//-->
				<%
				if(work.getType() == SmartWork.TYPE_SCHEDULE){
				%>
<%-- 					<!--탭-->
					<div class="tab js_space_tab_weekly_page js_space_tab_page"  startDate="">
 						<a href="" class="btn_arr_prev2 js_space_tab_index"></a> 
						<a href="" class="btn_arr_prev js_space_tab_index"></a>
					
						<ul>
 							<li class=""><span class="intab"><a class="js_space_tab_index" href="">D+1</a></span></li>
							<li class=""><span class="intab"><a class="js_space_tab_index" href="">D+1</a></span></li>
							<li class=""><span class="intab"><a class="js_space_tab_index" href="">D+1</a></span></li>
							<li class=""><span class="intab"><a class="js_space_tab_index" href="">D+1</a></span></li>
							<li class=""><span class="intab"><a class="js_space_tab_index" href="">D+1</a></span></li>
							<li class=""><span class="intab"><a class="js_space_tab_index" href="">D+1</a></span></li>
						</ul>
							<a href="" class="btn_arr_next js_space_tab_index"></a>
							<a href="" class="btn_arr_next2 js_space_tab_index"></a>
						<span class="js_progress_span" style="margin-top:10px;display:inline-block"></span>
					
						<div class="option_section js_space_date_scope">
							<span class="js_progress_span"></span>
					  		<span class="sel_date_section">2013.04.08<input type="hidden" class="js_space_datepicker" value=""><a href="space_tab_weekly.sw" class="btn_calendar js_space_datepicker_button"></a></span> 
							<select class="js_space_select_scope">
								<option value="space_tab_timeline.sw"><fmt:message key="space.title.tab_timeline"/></option>
								<option value="space_tab_dayly.sw"><fmt:message key="space.title.tab_dayly"/></option>
								<option selected><fmt:message key="space.title.tab_weekly"/></option>
								<option value="space_tab_monthly.sw"><fmt:message key="space.title.tab_monthly"/></option>
							</select>
						</div>
					
					</div>
					<!--탭//-->
 --%>				<%
				}
				%>
				<div class="up p0 js_smart_workbench_editor"></div>
			</div>
			<!-- 컨텐츠 //-->
		</ul>
	</div>
	
  <div class="portlet_b" style="display: block;"></div>
</div>
<!-- 컨텐츠 레이아웃//-->

<script type="text/javascript">
	var tabWorkbench = $('.js_tab_workbench_page');
	var workId = tabWorkbench.attr("workId");
	var target = tabWorkbench.find('.js_smart_workbench_editor');
	loadSmartBuilder(target, {
		packageId : workId
	});
</script>