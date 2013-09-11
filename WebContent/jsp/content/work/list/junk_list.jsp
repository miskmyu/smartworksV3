<%@page import="net.smartworks.model.work.SocialWork"%>
<%@page import="net.smartworks.server.engine.infowork.form.model.SwfFormModel"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="com.sun.xml.internal.txw2.Document"%>
<%@page import="net.smartworks.model.work.ProcessWork"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.model.KeyMap"%>
<%@page import="net.smartworks.model.filter.ConditionOperator"%>
<%@page import="net.smartworks.model.filter.Condition"%>
<%@page import="net.smartworks.model.work.FormField"%>
<%@page import="net.smartworks.model.work.SmartForm"%>
<%@page import="net.smartworks.model.filter.SearchFilter"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.model.security.EditPolicy"%>
<%@page import="net.smartworks.model.security.WritePolicy"%>
<%@page import="net.smartworks.model.security.AccessPolicy"%>
<%@page import="net.smartworks.model.work.InformationWork"%>
<%@page import="net.smartworks.model.work.SmartWork"%>
<%@page import="net.smartworks.model.work.Work"%> 
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();
	
	String[][] junkIds = smartWorks.getJunkIds(); 
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />


<!-- 상세 필터 -->
<div class="filter_group m5 js_junk_list_page">
	<table class="mb10">
		<tbody class="fl">
			<tr>
				<td class="tc" style="width:300px"><fmt:message key="mail.title.junk_mail_list"/></td>
				<td style="width:20px"></td>
				<td class="tc" style="width:300px"><fmt:message key="mail.title.junk_domain_list"/></td>
			</tr>
			<tr>
				<td style="width:300px;height:25px">
					<input class="fieldline" type="text" style="width:270px;height:16px;padding-left:2px;margin-bottom:7px">
					<!-- 추가 버튼  -->
					<span class="btn_plus_space vb">
						<span class="btn_plus js_add_junk_mail" title="<fmt:message key='mail.title.add_junk_mail'/>" /> </span>
					</span>
				</td>
				<td style="width:20px"></td>
				<td style="width:300px">
	 					<input  class="fieldline" type="text" style="width:270px;height:16px;padding-left:2px;margin-bottom:7px">
					<!-- 추가 버튼  -->
					<span class="btn_plus_space vb">
						<span class="btn_plus js_add_junk_domain" title="<fmt:message key='mail.title.add_junk_domain'/>" /> </span>
					</span>
				</td>
			</tr>
			<tr>
				<td>
					<div class="fieldline" style="height:300px;overflow-y:auto;padding:2px">
						<ul class="list_action_item">
							<%
							if(!SmartUtil.isBlankObject(junkIds) && !SmartUtil.isBlankObject(junkIds[0])){
								for(int i=0; i<junkIds[0].length; i++){
							%>
									<li junkId="<%=junkIds[0][i]%>"><span><%=junkIds[0][i]%></span><span class="list_action fr pr5"><span title="<fmt:message key='common.button.delete'/>" class="js_remove_junk_mail"> X </span></span></li>
							<%
								}
							}
							%>
						</ul>
					</div>
				</td>
				<td></td>
				<td>
					<div class="fieldline" style="height:300px;overflow-y:auto;padding:2px">
						<ul class="list_action_item">
							<%
							if(!SmartUtil.isBlankObject(junkIds) && !SmartUtil.isBlankObject(junkIds[1])){
								for(int i=0; i<junkIds[1].length; i++){
							%>
									<li junkId="<%=junkIds[1][i]%>"><span><%=junkIds[1][i]%></span><span class="list_action fr pr5"><span title="<fmt:message key='common.button.delete'/>" class="js_remove_junk_domain"> X </span></span></li>
							<%
								}
							}
							%>
						</ul>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
	<div style="min-height:20px">
		<div class="fr ml10">
			<span class="btn_gray">
				<a href="" class="js_junk_list_close"> 
					<span class="txt_btn_start"></span> 
					<span class="txt_btn_center"><fmt:message key="common.button.close"/></span> 
					<span class="txt_btn_end"></span> 
				</a> 
			</span>
		</div>

		<!--  실행시 표시되는 프로그래스아이콘을 표시할 공간 -->
		<span class="fr form_space js_progress_span" ></span>
		
		<!-- 실행시 데이터 유효성 검사이상시 에러메시지를 표시할 공간 -->
		<div class="form_space sw_error_message js_filter_error_message" style="text-align:right; color: red; line-height: 20px"></div>
		
	</div>
</div>
