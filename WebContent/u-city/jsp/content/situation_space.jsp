
<!-- Name 			: pwork_space.jsp						 -->
<!-- Description	: 프로세스업무 인스턴스 공간을 표시하는 페이지    -->
<!-- Author			: Maninsoft, Inc.						 -->
<!-- Created Date	: 2011.9.								 -->

<%@page import="net.smartworks.server.engine.factory.SwManagerFactory"%>
<%@page import="pro.ucity.manager.ucityWorkList.model.UcityWorkList"%>
<%@page import="pro.ucity.manager.ucityWorkList.model.UcityWorkListCond"%>
<%@page import="net.smartworks.model.approval.ApprovalLine"%>
<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.model.instance.Instance"%>
<%@page import="net.smartworks.model.instance.WorkInstance"%>
<%@page import="net.smartworks.util.SmartTest"%>
<%@page import="net.smartworks.model.work.ProcessWork"%>
<%@page import="net.smartworks.model.instance.ProcessWorkInstance"%>
<%@page import="net.smartworks.model.instance.info.TaskInstanceInfo"%>
<%@page import="net.smartworks.model.instance.TaskInstance"%>
<%@page import="net.smartworks.model.work.InformationWork"%>
<%@page import="net.smartworks.model.community.WorkSpace"%>
<%@page import="net.smartworks.model.instance.InformationWorkInstance"%>
<%@page import="net.smartworks.model.work.Work"%>
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
	String wid = request.getParameter("wid");
	String lastHref = SmartUtil.getLastHref(request);

	String instId = SmartUtil.getSpaceIdFromContentContext(cid);
	String workId = request.getParameter("workId");
	String taskInstId = request.getParameter("taskInstId");
	
	ProcessWorkInstance instance = null;
	WorkInstance workInstance = (WorkInstance)session.getAttribute("workInstance");
	if(SmartUtil.isBlankObject(workInstance) || !workInstance.getId().equals(instId)) 
		instance = (ProcessWorkInstance)smartWorks.getWorkInstanceById(SmartWork.TYPE_PROCESS, workId, instId);
	else
		instance = (ProcessWorkInstance)workInstance;

	int numberOfForwardHistories = instance.getNumberOfForwardHistories();
	
	User owner = instance.getOwner();
	WorkSpace workSpace = instance.getWorkSpace();
	ProcessWork work = (ProcessWork)instance.getWork();
	workId = work.getId();
	
	TaskInstanceInfo[] taskHistories = instance.getTasks();
	
	String approvalTaskInstId = "";
	TaskInstanceInfo approvalTask = null;
	TaskInstanceInfo forwardedTask = null;

	TaskInstanceInfo taskInstance = (SmartUtil.isBlankObject(taskInstId)) ? ((SmartUtil.isBlankObject(taskHistories)) ? null : taskHistories[0]) : instance.getTaskInstanceById(taskInstId);

	UcityWorkListCond cond = new UcityWorkListCond();
	cond.setPrcInstId(instance.getId());
	UcityWorkList workList = SwManagerFactory.getInstance().getUcityWorkListManager().getUcityWorkList("", cond, null);
	
 	session.setAttribute("cid", cid);
	if(SmartUtil.isBlankObject(wid))
		session.removeAttribute("wid");
	else
		session.setAttribute("wid", wid);

 	session.setAttribute("workInstance", instance);
	session.setAttribute("workSpaceId", instance.getId());
	
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />
<!-- 컨텐츠 레이아웃-->
<div class="contents_space js_pwork_space_page" lastHref="<%=lastHref %>" workId="<%=workId%>" instId="<%=instId%>" taskInstId="<%=CommonUtil.toNotNull(taskInstId) %>">	            
					 		            
 			<!-- 타이틀 -->
			<div class="title">
				<%=workList.getServiceName() %> / <%=workList.getEventName() %><%if(!SmartUtil.isBlankObject(workList.getEventPlace())) {%>(장소 : <%=workList.getEventPlace() %>)<%} %> 상세화면 

				<!-- 다이어그램 보기 -->
				<div class="txt_btn fr h_auto pt5">
					<a class ="js_situation_space_reload" href="">새로고침</a>
                	<a href="">목록보기</a>
                </div>
				<!--  다이어그램 보기// -->				
			</div>
			<!-- 타이틀 -->
			
			
			<!-- 목록보기
			<div class="button_set">
				<div><a href="">목록보기</a></div>
			</div>
			<!-- 목록보기 //-->

			<!-- 프로세스다이어그램 -->
			<div class="define_space js_process_instance_viewer" style="height:512px;"></div>
			
			<!-- 프로세스 영역 -->
			<div class="define_space" style="display:none;height:68px">
			
				<!-- 방향 Prev -->
        		<a href="" class="js_instance_tasks_left"><div class="proc_btn_prev" style="display:block"></div></a>
	        	<!-- 방향 Prev //-->
	        	
				<div class="process_section js_instance_tasks_holder">
			        <div class="proc_start_compl fl js_task_start js_instance_task"><fmt:message key="process.task.start"/></div>
			        <div class="proc_arr_next fl js_instance_task_arrow"></div>
			        
			        <!-- 태스크 시작-->
			        <div class="process_space js_instance_tasks">			        
						<ul>
				        	<%
				        	if(!SmartUtil.isBlankObject(taskHistories)){
				        		int count = 0;
				        		for(int i=0; i<taskHistories.length; i++){
				        			TaskInstanceInfo task = taskHistories[i];
				        			if(!SmartUtil.isBlankObject(task.getForwardId()) || !SmartUtil.isBlankObject(task.getApprovalId())) continue;
				        			count++;
				        			String statusClass = "proc_task not_yet";
				        			String formMode = (task.getAssignee().getId().equals(cUser.getId()) 
				        								&& ( 	task.getStatus()==TaskInstance.STATUS_RUNNING
				        									 || task.getStatus()==TaskInstance.STATUS_DELAYED_RUNNING) 
				        									 || (task.getStatus()==Instance.STATUS_APPROVAL_RUNNING
				        									 		&& instance.getStatus()==Instance.STATUS_RETURNED
				        									 		&& !SmartUtil.isBlankObject(approvalTask) 
				        									 		&& task.getId().equals(approvalTask.getApprovalTaskId()))) ? "edit" : "view";
				        			boolean isSelectable = ((task.getStatus()==TaskInstance.STATUS_RUNNING||task.getStatus()==TaskInstance.STATUS_DELAYED_RUNNING)) ? false : true;
				        			String approvalLineId = "";
				        			if(task.getStatus() == TaskInstance.STATUS_RETURNED){
				        				statusClass = "proc_task returned";
				        			}else if(task.getStatus() == TaskInstance.STATUS_RUNNING){
				        				approvalLineId = task.getApprovalLineId();
				        				statusClass = "proc_task running";
				        			}else if(task.getStatus() == TaskInstance.STATUS_DELAYED_RUNNING){
				        				approvalLineId = task.getApprovalLineId();
				        				statusClass = "proc_task delayed";
				        			}else if(task.getStatus() == TaskInstance.STATUS_APPROVAL_RUNNING){
				        				statusClass = "proc_task running";
				        			}else if(task.getStatus() == TaskInstance.STATUS_COMPLETED){
				        				statusClass = "proc_task completed";
				        			}else{
				        				statusClass = "proc_task not_yet";
				        			}
				        	%>
			            			<!-- 태스크 --> 
						            <li class="<%=statusClass %> js_instance_task <%if(isSelectable){%>js_select_task_instance<%} %>" formId="<%=task.getFormId() %>" taskName="<%=task.getName() %>" taskInstId="<%=task.getId()%>" 
						            		formMode="<%=formMode %>" isApprovalWork="<%=task.isApprovalWork()%>" approvalLineId=<%=CommonUtil.toNotNull(approvalLineId) %>>
					                    <!-- task 정보 -->
					                    <%if(isSelectable){%><a class="js_select_task_instance" href=""><%} %>
						                    <div class="title"><%=count%>) <%=task.getName() %></div>
						                    <div class="noti_in_s">
							                    <div class="t_date"><%=task.getLastModifiedDate().toLocalString() %></div>
						                    </div>
						                <%if(isSelectable){%></a><%} %>
					                    <!-- task 정보 //-->
						            </li>
				            		<!-- 태스크 //--> 
						            <!--화살표-->
						            <li class="proc_arr_next fl js_instance_task_arrow"></li>
						            <!--화살표-->
			            	<%
				        		}
				        	}
			            	%>
		            	</ul>
			        </div>
			        <!-- 태스크 시작//-->
           			<%
           			if(instance.getStatus() == Instance.STATUS_COMPLETED){
           			%>
				        <div class="proc_start_compl fl js_task_stop js_instance_task"><fmt:message key="process.task.stop"/></div>
			        <%
			        }
			        %>			        
				</div>
				
				<!-- 방향 Next -->
	    		<a href="" class="js_instance_tasks_right"><div class="proc_btn_next"  style="display:block"></div></a>
	    		<!-- 방향 Next //-->
			</div>
			<!--프로세스 영역//-->
				
			<!-- 상세보기 컨텐츠 -->
			<div class="js_form_header">
				<div class="form_title js_selected_task_title"><%=taskInstance.getName() %></div>
				
				<!--  전자결재화면이 나타나는 곳 -->
				<div class="js_form_task_approval js_form_task p0 mb15" 
					<%if(approvalTask==null && (SmartUtil.isBlankObject(taskInstance) || !taskInstance.isApprovalWork())){ %>style="display:none"<%} %>></div>
				
				<div class="form_wrap up form_read js_form_content"> </div>
				<div class="js_check_completion_notice" style="display:none">
					<jsp:include page="/jsp/content/upload/check_completion_notice.jsp"></jsp:include>
				</div>
			</div>
			<!-- 목록보기 -->
			<div class="mt10 fr">
				<span class="btn_gray">
					<a class="js_situation_space_reload" href="">
					<span class="txt_btn_start"></span>
					<span class="txt_btn_center">새로고침</span>
					<span class="txt_btn_end"></span>
					</a>
				</span>
				<span class="btn_gray">
					<a class="" href="">
					<span class="txt_btn_start"></span>
					<span class="txt_btn_center">목록보기</span>
					<span class="txt_btn_end"></span>
					</a>
				</span>
			</div>
			<!-- 목록보기// -->
			
			<!-- 업무전달화면이 나타나는 곳 -->
			<div class="js_form_task_forward  js_form_task" <%if(forwardedTask==null){ %>style="display:none"<%} %>>
				<%
				if(forwardedTask!=null){
				%>
					<jsp:include page="/jsp/content/upload/append_task_forward.jsp">
						<jsp:param value="<%=taskInstId %>" name="taskInstId"/>
						<jsp:param value="<%=forwardedTask.getForwardId() %>" name="forwardId"/>
					</jsp:include>
				<%
				}
				%>
			</div>
				 				
			<div class="js_instance_histories"></div>
</div> 
<!-- 컨텐츠 레이아웃//-->
<script type="text/javascript">

	var isReturned = false;
	var isApprovalForMe = false;
	<%
	if(instance.getStatus()==Instance.STATUS_RETURNED && !SmartUtil.isBlankObject(approvalTask)){
	%>
		isReturned = true;
	<%
	}
	if(!SmartUtil.isBlankObject(approvalTask)){
	%>
		isApprovalForMe = true;
	<%
	}
	%>
	function clickOnTaskInDiagram(formId){
		var taskInstances = $('.js_pwork_space_page .js_instance_task');
		if(!isEmpty(taskInstances)){
			var selectedTask = null;
			for(var i=0; i<taskInstances.length; i++){
				var taskInstance = $(taskInstances[i]);
				if(taskInstance.attr('formId') === formId && taskInstance.hasClass("completed"))
					selectedTask = taskInstance;
			}
			if(!isEmpty(selectedTask) && selectedTask.hasClass("completed"))
				clickOnTask(selectedTask);			
		}
	}
	function clickOnTask(input){
		var pworkSpace = input.parents('.js_pwork_space_page');
		var workId = pworkSpace.attr("workId");
		var formId = input.attr("formId");
		var taskName = input.attr("taskName");
		var formMode = input.attr("formMode");
		var instId = input.attr("taskInstId");
		var isApprovalWork = input.attr("isApprovalWork");
		var approvalLineId = input.attr("approvalLineId"); 
		var approvalContent = pworkSpace.find('div.js_form_task_approval').html('').hide();
		var formContent = pworkSpace.find('div.js_form_content').html('');
		var formContentPointer = pworkSpace.find('div.js_form_content_pointer');
 		if(isApprovalWork == 'true' && !isEmpty(approvalContent)){
			$.ajax({
				url : 'append_task_approval.sw',
				data : { 
					processTaskInstId : instId
				},
				success : function(data, status, jqXHR) {
					approvalContent.html(data).show();
				},
				error : function(xhr, ajaxOptions, thrownError){					
				}
			});
		}else if(!isEmpty(approvalLineId) && formMode === "edit"){
			$.ajax({
				url : 'append_task_approval.sw',
				data : { 
					approvalLineId : approvalLineId
				},
				success : function(data, status, jqXHR) {
					approvalContent.html(data).show();
				},
				error : function(xhr, ajaxOptions, thrownError){					
				}
			});			
		}
		var selectedTask = input;
		pworkSpace.find('.js_instance_task').removeClass('selected');
		selectedTask.addClass('selected');
		formContentPointer.css({"left": selectedTask.position().left + selectedTask.outerWidth()/2 + "px"});
		pworkSpace.find('.js_selected_task_title').html(taskName);
		new SmartWorks.GridLayout({
			target : formContent,
			mode : formMode,
			first : (formMode=='edit'),
			workId : workId,
			formId : formId,
			taskInstId : instId,
			onSuccess : function(){
				formContent.attr('taskInstId', instId);
				smartPop.closeProgress();																
			},
			onError : function(){
				smartPop.closeProgress();
				
			}
		});
		pworkSpace.attr("taskInstId", instId);
		pworkSpace.attr("formMode", formMode);
		if(!isEmpty(pworkSpace.find('.js_form_task_forward:visible'))) 
			return;
		if(formMode==="edit"){
			if(isReturned || !isEmpty(approvalLineId)){
				pworkSpace.find('.js_toggle_approval_btn').hide();
				if(!isEmpty(approvalLineId)){
					pworkSpace.find('.js_btn_do_approval').show().siblings().hide();
					pworkSpace.find('.js_btn_return').show();																
				}else if(isReturned){
					pworkSpace.find('.js_btn_submit_approval').show().siblings().hide();
					pworkSpace.find('.js_btn_return').show();											
				}
			}else{
				pworkSpace.find('.js_btn_complete').show().siblings().hide();
				pworkSpace.find('.js_btn_return').show();
				pworkSpace.find('.js_btn_reassign').show();
				pworkSpace.find('.js_btn_temp_save').show();
				pworkSpace.find('.js_toggle_approval_btn').show();				
			}
			pworkSpace.find('.js_check_completion_notice').show();
		}else{
			if(isApprovalWork == 'true' && isApprovalForMe){
				pworkSpace.find('.js_toggle_approval_btn').hide();
				pworkSpace.find('.js_btn_approve_approval').show().siblings().hide();
				pworkSpace.find('.js_btn_return_approval').show();
				pworkSpace.find('.js_btn_reject_approval').show();
			} else {
				pworkSpace.find('.js_btn_complete').hide().siblings().hide();
			}
			pworkSpace.find('.js_toggle_approval_btn').hide();
			pworkSpace.find('.js_check_completion_notice').hide();
		}
	}
	
	var getTasksWidth = function(tasks, arrows){
		var width = 0;
		if(isEmpty(tasks) || isEmpty(arrows)) return width;
		
		for(var i=0; i<tasks.length; i++){
			width = width + $(tasks[i]).outerWidth();
		}
		for(var j=0; j<arrows.length; j++)
			width = width + $(arrows[j]).outerWidth() + 10;
		return width;
	};
	
	var pworkSpace = $('.js_pwork_space_page');
	var taskInstId = pworkSpace.attr('taskInstId');
	var instanceTasksHolder = pworkSpace.find(".js_instance_tasks_holder");
	var instanceTasks = instanceTasksHolder.find(".js_instance_tasks");
	var instanceLeft = pworkSpace.find('.js_instance_tasks_left');	
	var instanceRight = pworkSpace.find('.js_instance_tasks_right');

	var taskStart = instanceTasksHolder.find('.js_task_start');
	var taskStop = instanceTasksHolder.find('.js_task_stop');
	var tasks = instanceTasks.find(".js_instance_task");
	var arrows = instanceTasksHolder.find('.js_instance_task_arrow');
	var selectedTask = $(tasks[0]);
	var viewWidth = instanceTasksHolder.width();
	var tasksWidth = getTasksWidth(tasks, arrows);
	if(isEmpty(taskInstId)){
 		instanceLeft.hide();
		if(tasksWidth>viewWidth)
			instanceRight.show();
		else
			instanceRight.hide();
 	}else{
 		for(var i=0; i<tasks.length; i++){
			var task = $(tasks[i]);
			if(task.attr('taskInstId') === taskInstId)
				break;
		}
		var selectedIndex = i;
		if(selectedIndex<tasks.length)
			selectedTask = $(tasks[selectedIndex]);
		if(selectedIndex<tasks.length && selectedTask.position().top>=selectedTask.height()){
			var width = 0;
			for(var i=selectedIndex; i>=0; i--){
				width = width + $(tasks[i]).outerWidth() + $(arrows[i]).outerWidth() + 10;
				if(width>viewWidth) break;
			}
			if(i>-1){
				for(var j=0; j<i+1; j++){
					$(tasks[j]).hide();
					$(arrows[j]).hide();
				}
				taskStart.hide();
			}
		}
		if(selectedIndex == tasks.length-1)
			instanceRight.hide();
		else
			instanceRight.show();
			
 	}
	
	if(!isEmpty(selectedTask)) clickOnTask(selectedTask);

 	var target = pworkSpace.find('.js_process_instance_viewer');
	var instanceId = pworkSpace.attr('instId');
	loadInstanceViewer(target, {instanceId : instanceId });
	
</script>
