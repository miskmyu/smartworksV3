<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.model.calendar.WorkHour"%>
<%@page import="net.smartworks.model.RecordList"%>
<%@page import="net.smartworks.model.instance.info.RequestParams"%>
<%@page import="java.util.Calendar"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.model.calendar.WorkHourPolicy"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 현재사용자 정보도 가져온다..
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	RequestParams params = (RequestParams)request.getAttribute("requestParams");
	if(SmartUtil.isBlankObject(params)){
		params = new RequestParams();
	}
	User cUser = SmartUtil.getCurrentUser();

	RecordList recordList = smartWorks.getWorkHourPolicyList(params);
	int pageSize = recordList.getPageSize();
	int totalPages = recordList.getTotalPages();
	int currentPage = recordList.getCurrentPage();
	WorkHourPolicy[] workHourPolicies = (WorkHourPolicy[])recordList.getRecords();

%>
<script type="text/javascript">

	selectListParam = function(progressSpan, isGray){
		var companyWorkHour = $('.js_company_work_hour_page');
		var forms = companyWorkHour.find('form:visible');
		var paramsJson = {};
		paramsJson["href"] = "jsp/content/settings/company_work_hour.jsp";
		for(var i=0; i<forms.length; i++){
			var form = $(forms[i]);
			paramsJson[form.attr('name')] = mergeObjects(form.serializeObject(), SmartWorks.GridLayout.serializeObject(form));
		}
		progressSpan = companyWorkHour.find('span.js_progress_span:first');
		smartPop.progressCont(progressSpan);
		console.log(JSON.stringify(paramsJson));
		var url = "set_instance_list_params.sw";
		$.ajax({
			url : url,
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				$('#content').html(data);
				smartPop.closeProgress();
			},
			error : function(xhr, ajaxOptions, thrownError) {
				smartPop.closeProgress();
				smartPop.showInfo(smartPop.ERROR, smartMessage.get('workHourListError'));
			}
		});
	};
</script>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!-- 컨텐츠 레이아웃-->
<div class="section_portlet js_company_work_hour_page">
	<div class="portlet_t"><div class="portlet_tl"></div></div>
	<div class="portlet_l" style="display: block;">
		<ul class="portlet_r" style="display: block;">

			<!-- 타이틀 -->
			<div class="body_titl">
				<div class="body_titl_area ti_company title_noico"><fmt:message key="settings.title.company.work_hour_policy"/></div>
				<div class="solid_line"></div>
			</div>
			<!-- 타이틀 -->

			<!-- 컨텐츠 -->
			<div class="contents_space">

				<!-- 타이틀 영역 -->
				<div class="list_title_space">
					<div class="title"><fmt:message key="settings.title.company.work_hour_list"/></div>
					<!-- 우측버튼 -->
					<div class="title_line_btns">
						<div class="icon_btn_create">
							<a class="icon_btn_tail js_new_work_hour" href=""><fmt:message key="common.button.add_new"/></a>
						</div>
					</div>
					<!-- 우측버튼 //-->
				</div>
				<!-- 타이틀 영역// -->

				<!-- 추가하기 테이블 -->
				<div class="js_new_work_hour"></div>

				<!-- 근무정책 목록 -->
				<div class="list_contents">
					<div>
						<table>
							<tbody>
								<tr class="tit_bg">
									<th class="r_line"><fmt:message key="settings.title.work_hour.valid_from"/></th>
									<th class="r_line"><fmt:message key="settings.title.work_hour.first_day_of_week"/></th>
									<th class="r_line"><fmt:message key="settings.title.work_hour.working_days"/></th>
									<th class="r_line"><%=LocalDate.getDayLocalString(Calendar.SUNDAY) %></th>
									<th class="r_line"><%=LocalDate.getDayLocalString(Calendar.MONDAY) %></th>
									<th class="r_line"><%=LocalDate.getDayLocalString(Calendar.TUESDAY) %></th>
									<th class="r_line"><%=LocalDate.getDayLocalString(Calendar.WEDNESDAY) %></th>
									<th class="r_line"><%=LocalDate.getDayLocalString(Calendar.THURSDAY) %></th>
									<th class="r_line"><%=LocalDate.getDayLocalString(Calendar.FRIDAY) %></th>
									<th class="r_line"><%=LocalDate.getDayLocalString(Calendar.SATURDAY) %></th>
									<th width="20px"></th>
								</tr>
								<%
								if(!SmartUtil.isBlankObject(workHourPolicies)){
									for(WorkHourPolicy whp : workHourPolicies){	
										WorkHour[] workHours = whp.getWorkHours();
								%>
											<tr class="js_edit_work_hour list_action_item" policyId=<%=CommonUtil.toNotNull(whp.getId()) %>>
												<td><a href=""><%=whp.getValidFrom().toLocalDateSimpleString() %></a></td>
												<td><a href=""><%=LocalDate.getDayLocalString(whp.getFirstDayOfWeek()) %></a></td>
												<td><a href=""><%=whp.getWorkingDays() %> <fmt:message key="calendar.title.days"/></a></td>
												<%
												if(!SmartUtil.isBlankObject(workHours) && workHours.length==7){
													for(WorkHour workHour : workHours){
														if(workHour != null) {
												%>													
															<td><a href=""><%=LocalDate.convertTimeToString(workHour.getStart()) %> ~ <%=LocalDate.convertTimeToString(workHour.getEnd()) %></a></td>
														<%
														} else {
														%>
															<td><a href=""><fmt:message key="settings.title.work_hour.none"/></a></td>
												<%
														}
													}
												}else{
												%>
													<td></td><td></td><td></td><td></td><td></td><td></td><td></td>
												<%
												}
												%>
												<td><%if(!SmartUtil.isBlankObject(whp.getId())){ %><div class="list_action"><div title="<fmt:message key='common.button.delete'/>" class="js_delete_work_hour"> X </div></div><%} %></td>
											</tr>
								<%
									}
								}else{
								%>
									<tr><td><fmt:message key="common.message.no_instance"/></td></tr>
								<%
								}

								%>

							</tbody>
						</table>

						<form name="frmWorkHourListPaging">
							<!-- 페이징 -->
							<div class="paginate">
								<%
								if (currentPage > 0 && totalPages > 0 && currentPage <= totalPages) {
									boolean isFirst10Pages = (currentPage <= 10) ? true : false;
									boolean isLast10Pages = (((currentPage - 1)  / 10) == ((totalPages - 1) / 10)) ? true : false;
									int startPage = ((currentPage - 1) / 10) * 10 + 1;
									int endPage = isLast10Pages ? totalPages : startPage + 9;
									if (!isFirst10Pages) {
								%>
										<a class="pre_end js_select_paging" href="" title="<fmt:message key='common.title.first_page'/>">
											<span class="spr"></span>
											<input name="hdnPrevEnd" type="hidden" value="false"> 
										</a>		
										<a class="pre js_select_paging" href="" title="<fmt:message key='common.title.prev_10_pages'/> ">
											<span class="spr"></span>
											<input name="hdnPrev10" type="hidden" value="false">
										</a>
									<%
									}
									for (int num = startPage; num <= endPage; num++) {
										if (num == currentPage) {
									%>
											<strong><%=num%></strong>
											<input name="hdnCurrentPage" type="hidden" value="<%=num%>"/>
										<%
										} else {
										%>
											<a class="num js_select_current_page" href=""><%=num%></a>
										<%
										}
									}
									if (!isLast10Pages) {
									%>
										<a class="next js_select_paging" title="<fmt:message key='common.title.next_10_pages'/> ">
											<span class="spr"></span>
											<input name="hdnNext10" type="hidden" value="false"/>
										</a>
										<a class="next_end js_select_paging" title="<fmt:message key='common.title.last_page'/> ">
											<span class="spr"><input name="hdnNextEnd" type="hidden" value="false"/></span> 
										</a>
								<%
									}
								}
								%>
								<span class="js_progress_span"></span>
							</div>
							
							<div class="num_box">
								<span class="js_progress_span"></span>
								<select class="js_select_page_size" name="selPageSize" title="<fmt:message key='common.title.count_in_page'/>">
									<option <%if (pageSize == 10) {%> selected <%}%>>10</option>
									<option <%if (pageSize == 20) {%> selected <%}%>>20</option>
									<option <%if (pageSize == 30) {%> selected <%}%>>30</option>
									<option <%if (pageSize == 50) {%> selected <%}%>>50</option>
								</select>
							</div>
							<!-- 페이징 //-->
						</form>
					</div>

				</div>
				<!-- 근무정책 목록 //-->

			</div>
			<!-- 컨텐츠 //-->

		</ul>
	</div>
	<div class="portlet_b" style="display: block;"></div>
</div>
<!-- 컨텐츠 레이아웃//-->
