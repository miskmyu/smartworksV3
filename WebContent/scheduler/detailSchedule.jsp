<%@page import='net.smartworks.server.engine.common.util.CommonUtil'%>
<%@page import='net.smartworks.server.engine.factory.SwManagerFactory'%>
<%@page import='net.smartworks.server.engine.scheduling.model.ScheduleDef'%>
<%
	String scheduleObjId = request.getParameter("scheduleObjId");
	ScheduleDef def = SwManagerFactory.getInstance().getScheduleManager().getScheduleDef("", scheduleObjId, null);
	
	String objId = "";
	String name = "";
	String packageType = "";
	String packageId = "";
	String targetFormId = "";
	String targetFieldId = "";
	String status = "";
	String groupName = "";
	String jobName = "";
	String triggerName = "";
	String targetClass = "";
	String cronExpression = "";
	String isAutoStart = "";
	
	if (def != null) {
		objId = CommonUtil.toNotNull(def.getObjId());
		name = CommonUtil.toNotNull(def.getName());
		packageType = CommonUtil.toNotNull(def.getTargetPackageType());
		packageId = CommonUtil.toNotNull(def.getTargetPackageId());
		targetFormId = CommonUtil.toNotNull(def.getTargetFormId());
		targetFieldId = CommonUtil.toNotNull(def.getTargetFieldId());
		status = CommonUtil.toNotNull(def.getStatus());
		groupName = CommonUtil.toNotNull(def.getGroupName());
		jobName = CommonUtil.toNotNull(def.getJobName());
		triggerName = CommonUtil.toNotNull(def.getTriggerName());
		targetClass = CommonUtil.toNotNull(def.getTargetClass());
		cronExpression = CommonUtil.toNotNull(def.getCronExpression());
		isAutoStart = CommonUtil.toNotNull(def.getIsAutoStart());
	}
%>
<form id='detailScheduleForm' name='detailScheduleForm'>
	<table>
		<tr>
			<td>id</td><td colspan='3'><input type='text' id='objId' name='objId' value='<%=objId %>'></td>
		</tr>
		<tr>
			<td>status</td><td colspan='3'><input type='text' id='status' name='status' value='<%=status %>'></td>
		</tr>
		<tr>
			<td>name</td><td colspan='3'><input type='text' id='name' name='name' value='<%=name %>'></td>
		</tr>
		<tr>
			<td>packageType</td><td><input type='text' id='packageType' name='packageType' value='<%=packageType %>'></td><td>packageId</td><td><input type='text' id='packageId' name='packageId' value='<%=packageId %>'></td>
		</tr>
		<tr>
			<td>targetFormId</td><td><input type='text' id='targetFormId' name='targetFormId' value='<%=targetFormId %>'></td><td>targetFieldId</td><td><input type='text' id='targetFieldId' name='targetFieldId' value='<%=targetFieldId %>'></td>
		</tr>
		<tr>
			<td>groupName</td><td><input type='text' id='groupName' name='groupName' value='<%=groupName %>'></td><td>jobName</td><td><input type='text' id='jobName' name='jobName' value='<%=jobName %>'></td>
		</tr>
		<tr>
			<td>triggerName</td><td><input type='text' id='triggerName' name='triggerName' value='<%=triggerName %>'></td><td>targetClass</td><td><input type='text' id='targetClass' name='targetClass' value='<%=targetClass %>'></td>
		</tr>
		<tr>
			<td>cronExpression</td><td><input type='text' id='cronExpression' name='cronExpression' value='<%=cronExpression %>'></td><td>isAutoStart</td><td><input type='text' id='isAutoStart' name='isAutoStart' value='<%=isAutoStart %>'></td>
		</tr>
	</table>
</form>
<div style='float:right;'><a href='' class='js_save' onclick='return false;'>save</a>|<a href='' class='js_delete' onclick='return false;'>delete</a>|<a class='js_cancel' href='' onclick='return false;'>cancel</a></div>