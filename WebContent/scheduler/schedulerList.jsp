<%@page import="org.quartz.Trigger"%>
<%@page import="org.quartz.impl.StdScheduler"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.server.engine.scheduling.model.ScheduleDefCond"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.server.engine.scheduling.manager.IScheduleManager"%>
<%@page import="net.smartworks.server.engine.factory.SwManagerFactory"%>
<%@page import="net.smartworks.server.engine.scheduling.model.ScheduleDef"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%!
	public static Object getBean(String beanName, HttpServletRequest request) {
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
		return (Object) wac.getBean(beanName);
	}
%>

<% 
	StdScheduler sc = (StdScheduler)getBean("schedulerFactoryBean", request);
	User user = SmartUtil.getCurrentUser();
	String userId = user.getId();

	IScheduleManager schMgr = SwManagerFactory.getInstance().getScheduleManager();
	ScheduleDefCond cond = new ScheduleDefCond();
	ScheduleDef[] schedules = schMgr.getScheduleDefs(userId, cond, null);

%>

<html>
<head>
<style type="text/css">
	table {
		border-spacing: 1;
		border-color: #c7c7c7; 
		border-style: solid; 
		border-width: 1px;
		border-collapse: collapse;
		width: 100%;
		line-height: 22px;
		font-size:12px
	}
	td, th {
		border-spacing: 1;
		border-color: #c7c7c7; 
		border-style: solid; 
		border-width: 1px;
		display: table-cell; 
		vertical-align: middle;
		align: center;
	}
	input {
		width: 70%;
	}
</style>

<script type="text/javascript" src="../js/jquery/jquery-1.6.2.min.js"></script>
<!--  
<script type="text/javascript" src="js/jquery/jquery-1.7.1.min.js"></script>
 -->
<script type="text/javascript" src="../js/jquery/jquery.ui.core.min.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.validate.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.effects.core.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.effects.explode.js"></script>
<script type="text/javascript" src="../js/jquery/jquery-ui-1.8.16.custom.min.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.ui.widget.min.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.ui.mouse.min.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.ui.draggable.min.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.ui.resizable.min.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.ui.slider.min.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.ui.datepicker.min.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.ui.datepicker-ko.js"></script>
<script type="text/javascript" src="../js/jquery/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript" src="../js/jquery/jquery-ui-timepicker-ko.js"></script>
<script type="text/javascript" src="../js/jquery/history/jquery.history.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.json-2.3.min.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.zclip.min.js"></script>
<script type="text/javascript" src="../js/jquery/jshashtable-2.1.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.numberformatter-1.2.2.min.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.formatCurrency-1.4.0.min.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.simplemodal.1.4.2.min.js"></script>
<script type="text/javascript" src="../js/jquery/jquery-ui-1.8.21.custom.min.js"></script>
<script type="text/javascript" src="../scheduler/js/schedule-actions.js"></script>

<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
</head>
<body>
<form>
<div id='titleDiv'>
<span style='font-size:30px;'>Scheduler List</span>
</div>
<div style='height:10px;'></div>
<div style="float:right;"><a href="detailSchedule.jsp" class='js_new' onclick="return false;">new</a></div>
<form id='scheduleListForm'>
	<div id='schedulerListDiv'>
		<table>
			<tr>
				<th>status</th>
				<th>Name</th>
				<th>TargetPkgType</th>
				<th>TargetPkgId</th>
				<th>TargetFieldId</th>
				<th>TriggerName</th>
				<th>JobName</th>
				<th>IsAutoStart</th>
				<th>TargetClass</th>
			</tr>
<%
	if (schedules != null && schedules.length != 0) {
		for (int i= 0; i < schedules.length; i++) {
			ScheduleDef def = schedules[i];
			String id = CommonUtil.toNotNull(def.getObjId());
			String name = CommonUtil.toNotNull(def.getName());
			String targetPkgType = CommonUtil.toNotNull(def.getTargetPackageType());
			String targetPkgId = CommonUtil.toNotNull(def.getTargetFormId());
			String targetFieldId = CommonUtil.toNotNull(def.getTargetFieldId());
			String triggerName = CommonUtil.toNotNull(def.getTriggerName());
			String jobName = CommonUtil.toNotNull(def.getJobName());
			String isAuString = CommonUtil.toNotNull(def.getIsAutoStart());
			String targetClass = CommonUtil.toNotNull(def.getTargetClass());
			String groupName = def.getGroupName();
			
			groupName = CommonUtil.isEmpty(groupName) ? StdScheduler.DEFAULT_GROUP : groupName;
			
			String isRunning = "STOP <a href='detailSchedule.jsp' onclick='return false;' class='js_run' objId='"+id+"'>run</a>";
			if (sc != null) {
				Trigger trigger = sc.getTrigger(triggerName, groupName);
				if (trigger != null)
					isRunning = "RUNNING <a href='detailSchedule.jsp' onclick='return false;' class='js_stop' objId='"+id+"'>stop</a>";
			}
%>
			<tr id="<%=id%>">
				<td align='center'><%=isRunning %></td>
				<td align='center'><a href='detailSchedule.jsp' onclick='return false;' class='js_select_list' objId='<%=id%>'><%= name%></a></td>
				<td><%= targetPkgType%></td>
				<td><%= targetPkgId%></td>
				<td><%= targetFieldId%></td>
				<td><%= triggerName%></td>
				<td><%= jobName%></td>
				<td><%= isAuString%></td>
				<td><%= targetClass%></td>
			</tr>
<%
		}
	}
%>
		</table>
	</div>
</form>
	<div style='height:20px;'></div>
	<div id='detailScheduleDiv'></div>
</form>
</body>
</html>