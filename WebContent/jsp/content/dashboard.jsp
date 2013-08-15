<%@page import="net.smartworks.model.work.SmartWork"%>
<%@page import="net.smartworks.util.SmartTest"%>
<%@page import="net.smartworks.model.report.ReportPane"%>
<%@page import="net.smartworks.model.report.ChartReport"%>
<%@page import="net.smartworks.model.report.Report"%>
<%@page import="net.smartworks.model.work.Work"%>
<%@page import="net.smartworks.model.instance.info.RequestParams"%>
<%@page import="net.smartworks.model.filter.SearchFilter"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%

	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();	
	String cid = request.getParameter("cid");
	String wid = request.getParameter("wid");
	
	session.setAttribute("cid", cid);
	session.setAttribute("wid", wid);
	session.setAttribute("lastLocation", "dashboard.sw");
	
	ReportPane[] myDashboard = smartWorks.getMyDashboard();
	
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!-- 컨텐츠 레이아웃-->
<div class="section_portlet js_dashboard_page">
	<div class="portlet_t"><div class="portlet_tl"></div></div>
	<div class="portlet_l" style="display: block;">
		<ul class="portlet_r" style="display: block;">

			<!-- 타이틀 -->
			<div class="body_titl">
				<div class="user fl"></div>
				<div class="noti_in_bodytitle" style="margin-top:25px">
					<div class="pr10 fl">
						<span class="title"><fmt:message key="header.top_menu.dashboard"/></span>
					</div>
				</div>				
				 <div class="txt_btn"  style="line-height: 27px">	
					<div class="title_line_btns">
						<div class="icon_btn_add">
							<a class="icon_btn_tail js_content" href="report_list.sw?cid=<%=ISmartWorks.CONTEXT_PREFIX_REPORT_LIST%><%=SmartWork.ID_ALL_WORKS%>"><fmt:message key="report.button.new_pane"/></a>
						</div>
					</div>
				</div>
				<div class="solid_line"></div>
			</div>
			<!-- 타이틀 -->

			<!-- 목록영역  -->
			<div class="contents_space">
				<div class="js_dashboard_window">
					<%
					if(!SmartUtil.isBlankObject(myDashboard)){
						int currentRow = -1;
						for(int i=0; i<myDashboard.length; i++){
							ReportPane pane = myDashboard[i];
							if(SmartUtil.isBlankObject(pane.getPosition())) continue;
							
							if(pane.getPosition().getRow() > currentRow){
								if(currentRow > -1){
								%>
									</div>
								<%
								}
								currentRow++;
								%>
								<div class="js_dashboard_pane_row" style="margin-top:14px;"> 					
							<%
							}							
							%>
							<span>
							<jsp:include page="/jsp/content/work/report/work_report_pane.jsp">
								<jsp:param value="<%=pane.getId()%>" name="paneId"/>
								<jsp:param value="<%=SmartUtil.escape(pane.getName())%>" name="paneName"/>
								<jsp:param value="<%=pane.getColumnSpans() %>" name="paneColumnSpans"/>
								<jsp:param value="<%=pane.getPosition().toString() %>" name="panePosition"/>
								<jsp:param value="<%=pane.getTargetWork() != null ? pane.getTargetWork().getId() : null%>" name="targetWorkId"/>
								<jsp:param value="<%=pane.getTargetWork() != null ? SmartUtil.escape(pane.getTargetWork().getFullpathName()) : null %>" name="targetWorkName"/>
								<jsp:param value="<%=pane.getTargetWork() != null ? pane.getTargetWork().getIconClass() : null %>" name="targetWorkIcon"/>
								<jsp:param value="<%=pane.getReportId() %>" name="reportId"/>
								<jsp:param value="<%=SmartUtil.escape(pane.getReportName()) %>" name="reportName"/>
								<jsp:param value="<%=pane.getReportType() %>" name="reportType"/>
								<jsp:param value="<%=ChartReport.getChartTypeInString(pane.getChartType()) %>" name="chartType"/>
								<jsp:param value="<%=pane.isChartView() %>" name="isChartView"/>
								<jsp:param value="<%=pane.isStacked() %>" name="isStacked"/>
								<jsp:param value="<%=pane.isShowLegend() %>" name="showLegend"/>
								<jsp:param value="<%=pane.getStringLabelRotation() %>" name="stringLabelRotation"/>
							</jsp:include>
							</span>
							<script type="text/javascript">
								Ext.onReady(function () {
									
//									setTimeout(function(){
										smartChart.loadPane("<%=pane.getReportType()%>", 
															"<%=pane.getReportId()%>", 
															"<%=ChartReport.getChartTypeInString(pane.getChartType())%>", 
															<%=pane.isStacked()%>,
															<%=pane.isChartView()%>,
															<%=pane.isShowLegend()%>,
															"<%=pane.getStringLabelRotation()%>",
															"chart_target_" + "<%=pane.getPosition().toString()%>",
															parseInt(<%=pane.getColumnSpans()%>));
//									},100);
								});
							</script>
						<%
						}
						if(currentRow>-1){
						%>
							</div>
						<%
						}
					}else{
					%>
						<div class="js_dashboard_window">
							<div class="js_dashboard_pane_row" style="margin-top:14px;height:412px;text-align:center">
								<span><br><br><br><br><br><fmt:message key="report.message.no_report_pane"/></span>
							</div>				
						</div>
					
					<%
					}
					%>
 				</div>
			</div>
		</ul>
	</div>
	<div class="portlet_b" style="display: block;"></div>
</div>
<!-- 컨텐츠 레이아웃//-->
