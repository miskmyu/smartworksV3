
<!-- Name 			: pop_new_work_definition.jsp							 -->
<!-- Description	: 새로운 업무w정의를 생성하는 팝업화면 						 -->
<!-- Author			: Maninsoft, Inc.										 -->
<!-- Created Date	: 2011.9.												 -->

<%@page import="net.smartworks.model.report.ChartReport"%>
<%@page import="net.smartworks.model.Matrix"%>
<%@page import="net.smartworks.model.report.ReportPane"%>
<%@page import="net.smartworks.model.report.Report"%>
<%@page import="net.smartworks.model.work.info.WorkInfo"%>
<%@page import="net.smartworks.model.work.WorkCategory"%>
<%@page import="net.smartworks.model.work.Work"%>
<%@page import="net.smartworks.model.work.SmartWork"%>
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

	String paneId = request.getParameter("paneId");
	String paneName = request.getParameter("paneName");
	String targetWorkId = request.getParameter("targetWorkId");
	String targetWorkName = request.getParameter("targetWorkName");
	String targetWorkIcon = request.getParameter("targetWorkIcon");
	String reportId = request.getParameter("reportId");
	String reportName = request.getParameter("reportName");
	String strReportType = request.getParameter("reportType");
	int reportType = (SmartUtil.isBlankObject(strReportType)) ? Report.TYPE_CHART : Integer.parseInt(strReportType);
	String chartType = request.getParameter("chartType");
	String isChartView = request.getParameter("isChartView");
	String isStacked = request.getParameter("isStacked");
	String showLegend = request.getParameter("showLegend");
	String stringLabelRotation = request.getParameter("stringLabelRotation");
	String strPaneColumnSpans = request.getParameter("paneColumnSpans");
	int paneColumnSpans = (SmartUtil.isBlankObject(strPaneColumnSpans)) ? 1 : Integer.parseInt(strPaneColumnSpans);
	String panePosition = request.getParameter("panePosition");
	
%>
<script type="text/javascript">

	// 개인정보프로파일 수정하기 버튼을 클릭하면, 
	// 모든정보를 JSON형식으로 Serialize해서 서버의 update_my_profile.sw 서비스를 호출하여 수정한다.
	function submitForms(e) {
		var newReportPane = $('.js_new_report_pane_page');
		if (SmartWorks.GridLayout.validate(newReportPane.find('form.js_validation_required'), $('.js_pop_error_message'))) {
			var forms = newReportPane.find('form');
			var paneId = newReportPane.attr('paneId');
			var reportId = newReportPane.attr('reportId');
			var targetWorkId = newReportPane.attr('targetWorkId');
			var paramsJson = {};
			var url = "create_new_work_report_pane.sw";
			if(!isEmpty(paneId)){
				paramsJson['paneId'] = paneId;
				url = "set_work_report_pane.sw";
			}
			paramsJson['reportId'] = reportId;
			paramsJson['targetWorkId'] = targetWorkId;
			for(var i=0; i<forms.length; i++){
				var form = $(forms[i]);
				if(form.attr('name') === 'frmSmartForm'){
					paramsJson['formId'] = form.attr('formId');
					paramsJson['formName'] = form.attr('formName');
				}
				paramsJson[form.attr('name')] = mergeObjects(form.serializeObject(), SmartWorks.GridLayout.serializeObject(form));
			}
			paramsJson['isNewRow'] = newReportPane.find('select[name="selReportPanePosition"] option:selected').attr("isNewRow");
			console.log(JSON.stringify(paramsJson));
			var progressSpan = newReportPane.find('.js_progress_span');
			smartPop.progressCont(progressSpan);
			$.ajax({
				url : url,
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					// 사용자정보 수정이 정상적으로 완료되었으면, 현재 페이지에 그대로 있는다.
					smartPop.closeProgress();
					smartPop.close();
					if(isEmpty(paneId)){
						smartPop.showInfo(smartPop.INFO, smartMessage.get('createReportPaneSucceed'));
					}else{
						$.get( "dashboard.sw", { contentType : "charset=utf-8"}, function(data){
							$('#content').html(data);
						});
					}
				},
				error : function(e) {
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.ERROR, isEmpty(paneId) ? smartMessage.get('createReportPaneError') : smartMessage.get('setReportPaneError'));
				}
			});
		}
	};
</script>

<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!--  전체 레이아웃 -->
<div class="pop_corner_all js_new_report_pane_page" paneId="<%=paneId%>" reportId="<%=reportId%>" targetWorkId=<%=targetWorkId %>>

	<!-- 팝업 타이틀 -->
	<div class="form_title">
		<%
		if(SmartUtil.isBlankObject(paneId)){
		%>
			<div class="pop_title"><fmt:message key="report.title.new_report_pane"></fmt:message></div>
		<%
		}else{
		%>
			<div class="pop_title"><fmt:message key="report.title.change_report_pane"></fmt:message></div>
		<%
		}
		%>
		<div class="txt_btn">
			<a href="" onclick="smartPop.close();return false;"><div class="btn_x"></div></a>
		</div>
		<div class="solid_line"></div>
	</div>
	<!-- 팝업 타이틀 //-->
	<!-- 컨텐츠 -->
	<form name="frmNewReportPane" class="js_validation_required">
		<div class="contents_space">
			<table>
				<tr>
					<td class="required_label"><fmt:message key="report.title.pane_name" /></td>
					<td>
						<input name="txtPaneName" class="fieldline required" type="text" value="<%=CommonUtil.toNotNull(paneName)%>">		
					</td>
				</tr>
				<tr>
					<td><fmt:message key="report.title.target_work"/></td>
					<td>
						<div style="display:inline-block">
							<span class="<%=targetWorkIcon%>"></span>
							<span style="line-height:21px;margin-left:4px;"><%=CommonUtil.toNotNull(targetWorkName) %></span>
						</div>
					</td>
				</tr>
				<tr>
					<td><fmt:message key="report.title.report_name"/></td>
					<td>
						<span><%=CommonUtil.toNotNull(reportName) %></span>
					</td>
				</tr>
				<tr>
					<td><fmt:message key="report.title.pane_position"/></td>
					<td>
						<select name="selReportPanePosition">
							<%
							if(!SmartUtil.isBlankObject(panePosition)){
							%>
								<option value="<%=panePosition%>" selected>
									<%=Matrix.getPositionName(panePosition, paneColumnSpans) %>
								</option>
							<%
							}
							ReportPane[] dashboardPanes = smartWorks.getMyDashboard();
							Matrix currentPosition = SmartUtil.isBlankObject(panePosition) ? null : new Matrix(panePosition);
							Matrix[] availablePositions = ReportPane.getAvailablePositions(dashboardPanes, currentPosition);
							if(!SmartUtil.isBlankObject(availablePositions)){
								for(int i=0; i<availablePositions.length; i++){
									Matrix position = availablePositions[i];
							%>
									<option value="<%=position.toString()%>" isNewRow=<%=position.isNewRow() %>><%=position.getPositionName() %></option>
							<%
								}
							}
							%>
						</select>
					</td>
				</tr>
				<%
				if(reportType == Report.TYPE_CHART){
				%>				
					<tr>
						<td><fmt:message key="report.chart.type" /></td>
						<td>
							<select name="selReportChartType">
								<option value="<%=ChartReport.CHART_TYPE_COLUMN%>"
									<%if(ChartReport.CHART_TYPES_STRING[ChartReport.CHART_TYPE_COLUMN].equals(chartType)) {%> selected <%}%>>
									<fmt:message key="report.chart.type.column" />
								</option>
								<option value="<%=ChartReport.CHART_TYPE_BAR%>"
									<%if(ChartReport.CHART_TYPES_STRING[ChartReport.CHART_TYPE_BAR].equals(chartType)) {%> selected <%}%>>
									<fmt:message key="report.chart.type.bar" />
								</option>
								<option value="<%=ChartReport.CHART_TYPE_LINE%>"
									<%if(ChartReport.CHART_TYPES_STRING[ChartReport.CHART_TYPE_LINE].equals(chartType)) {%> selected <%}%>>
									<fmt:message key="report.chart.type.line" />
								</option>
								<option value="<%=ChartReport.CHART_TYPE_PIE%>"
									<%if(ChartReport.CHART_TYPES_STRING[ChartReport.CHART_TYPE_PIE].equals(chartType)) {%> selected <%}%>>
									<fmt:message key="report.chart.type.pie" />
								</option>
								<option value="<%=ChartReport.CHART_TYPE_AREA%>"
									<%if(ChartReport.CHART_TYPES_STRING[ChartReport.CHART_TYPE_AREA].equals(chartType)) {%> selected <%}%>>
									<fmt:message key="report.chart.type.area" />
								</option>
								<option value="<%=ChartReport.CHART_TYPE_RADAR%>"
									<%if(ChartReport.CHART_TYPES_STRING[ChartReport.CHART_TYPE_RADAR].equals(chartType)) {%> selected <%}%>>
									<fmt:message key="report.chart.type.radar" />
								</option>
								<option value="<%=ChartReport.CHART_TYPE_SCATTER%>"
									<%if(ChartReport.CHART_TYPES_STRING[ChartReport.CHART_TYPE_SCATTER].equals(chartType)) {%> selected <%}%>>
									<fmt:message key="report.chart.type.scatter" />
								</option>
							</select>
						<td>
						</td>
					</tr>
					<tr>
						<td><fmt:message key="report.button.view_chart" /></td>
						<td>
							<input type="checkbox" name="chkChartView" <%if("true".equalsIgnoreCase(isChartView)){ %>checked<%} %> class="js_change_chart_view">
						</td>
					</tr>
					<tr>
						<td><fmt:message key="report.button.stacked_chart" /></td>
						<td>
							<input type="checkbox" name="chkStackedChart" <%if("true".equalsIgnoreCase(isStacked)){ %>checked<%} %>>
						</td>
					</tr>
					<tr>
						<td><fmt:message key="report.title.show_legend" /></td>
						<td>
							<input type="checkbox" name="chkShowLegend" <%if("true".equalsIgnoreCase(showLegend)){ %>checked<%} %>>
						</td>
					</tr>
					<tr>
						<td><fmt:message key="report.title.label_rotation" /></td>
						<td>
							<select name="selStringLabelRotation">
								<option value="<%=ChartReport.STRING_LABEL_ROTATION_AUTO%>"
									<%if(ChartReport.STRING_LABEL_ROTATION_AUTO.equals(stringLabelRotation)) {%> selected <%}%>>
									<fmt:message key="report.chart.label_rotation_auto" />
								</option>
								<option value="<%=ChartReport.STRING_LABEL_ROTATION_HORIZONTAL%>"
									<%if(ChartReport.STRING_LABEL_ROTATION_HORIZONTAL.equals(stringLabelRotation)) {%> selected <%}%>>
									<fmt:message key="report.chart.label_rotation_horizontal" />
								</option>
								<option value="<%=ChartReport.STRING_LABEL_ROTATION_ROTATED%>"
									<%if(ChartReport.STRING_LABEL_ROTATION_ROTATED.equals(stringLabelRotation)) {%> selected <%}%>>
									<fmt:message key="report.chart.label_rotation_rotated" />
								</option>
							</select>
						</td>
					</tr>
				<%
				}
				%>
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
					<%
					if(SmartUtil.isBlankObject(paneId)){
					%>
						<span class="txt_btn_center"><fmt:message key="common.button.create"/></span>
					<%
					}else{
					%>
						<span class="txt_btn_center"><fmt:message key="common.button.modify"/></span>
					<%
					}
					%>
					<span class="txt_btn_end"></span>
				</a> 
			</span>
			 <span class="btn_gray ml5"> 
				 <a href="" class="js_close_new_report_pane"> 
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
