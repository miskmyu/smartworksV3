
<!-- Name 			: pwork_space.jsp						 -->
<!-- Description	: 프로세스업무 인스턴스 공간을 표시하는 페이지    -->
<!-- Author			: Maninsoft, Inc.						 -->
<!-- Created Date	: 2011.9.								 -->

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
	if(taskHistories != null && instance.getStatus() != Instance.STATUS_REJECTED){
		if(!SmartUtil.isBlankObject(taskInstId)){
			for(TaskInstanceInfo task : taskHistories){
				if(task.isRunningForwardedForMe(cUser.getId(), taskInstId)){
					forwardedTask = task;
					break;
				}else if(task.isRunningApprovalForMe(cUser.getId(), taskInstId, null)){
					approvalTaskInstId = task.getId();
					approvalTask = task;
					taskInstId = task.getApprovalTaskId();
					break;
				}
			}
		}
		if(SmartUtil.isBlankObject(taskInstId) && SmartUtil.isBlankObject(approvalTask)){
			approvalTask = instance.getMyRunningApprovalTask();
			if(!SmartUtil.isBlankObject(approvalTask)){
				approvalTaskInstId = approvalTask.getId();
				taskInstId = approvalTask.getApprovalTaskId();
			}
		}
		if(SmartUtil.isBlankObject(taskInstId) && SmartUtil.isBlankObject(forwardedTask)){
			forwardedTask = instance.getMyRunningForwardedTask();
			if(!SmartUtil.isBlankObject(forwardedTask)){
				taskInstId = forwardedTask.getId();
			}			
		}
	}
	if(SmartUtil.isBlankObject(approvalTask)){
		approvalTaskInstId = "";
	}
	TaskInstanceInfo taskInstance = (SmartUtil.isBlankObject(taskInstId)) ? ((SmartUtil.isBlankObject(taskHistories)) ? null : taskHistories[0]) : instance.getTaskInstanceById(taskInstId);

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
<div class="section_portlet js_pwork_space_page" lastHref="<%=lastHref %>" workId="<%=workId%>" instId="<%=instId%>" taskInstId="<%=CommonUtil.toNotNull(taskInstId) %>">
    <div class="portlet_t"><div class="portlet_tl"></div></div>
    <div class="portlet_l" style="display: block;">
	    <ul class="portlet_r" style="display: block;">		            
			<!-- 타이틀 -->
			<div class="body_titl_pic">		
	                <div class="noti_pic mr7"><img src="<%=instance.getOwner().getMidPicture() %>"  class="profile_size_m"/></div>
	                <div class="noti_in_bodytitle case_2line">
	                    <div>
		                    <span class="t_name"><%=instance.getOwner().getLongName()%></span>
		                    <span class="t_date"><%=instance.getCreatedDate().toLocalString()%></span>
						</div>
						<div>
                    		<span class="icon_pworks t_date"> <%=work.getFullpathName() %>
                    			<span class="title_picico ml5"><%=instance.getSubject()%></span>
                    		</span> 
                    	</div>
	                </div>
		                    
	            <!-- 우측 버튼-->
	            <div class="fr" style="line-height: 15px">
<%-- 	                <div class="txt_btn t_s11 h_auto fr cb">
	                	<a id="js_copy_address" href=""><fmt:message key="common.button.copy_url"/></a>
	                </div>
 --%>	            
					<!-- 전자결재, 업무전달 버튼들 -->
					<div class="fr cb">
						<%
						if(forwardedTask == null){
						%>
							<a href="" class="js_toggle_forward_btn" title="<fmt:message key='common.button.forward'/>"><span class="icon_forward_w"></span></a>
							<a href="" class="js_toggle_approval_btn" title="<fmt:message key='common.button.approval'/>"><span class="icon_approval_w"></span></a>
						<%
						}
						%>
						<%
						if(cUser.isUseMail()){
						%>
	                		<a href="" class="js_email_content_btn" title="<fmt:message key='common.button.email'/>"><span class="icon_mail_w"></span></a>
	                	<%
	                	}
	                	%>
	                	<a href="" class="js_print_content_btn" title="<fmt:message key='common.button.print'/>"><span class="icon_print_w"></span></a>
	            	</div>
	            	
	            <!-- 다이어그램 보기 -->
					<div class="txt_btn fr cb h_auto">
	                	<a href="" class="js_view_instance_diagram"><fmt:message key="common.button.view_instance_diagram"/>▼</a>
	                </div>
	                <div class="txt_btn fr cb h_auto" style="display:none"><a href="" class="js_close_instance_diagram"><fmt:message key="common.button.close_instance_diagram"/>▼</a></div>	            
				<!--  다이어그램 보기// -->
				</div>
				
	            <!-- 우측 버튼 -->
		                    
               	<div class="solid_line cb"></div>
			</div>
			<!-- 타이틀 -->
					 		            
			<!-- 프로세스다이어그램 -->
			<div class="define_space js_process_instance_viewer" style="display:none; height:512px;"></div>
			
			<!-- 프로세스 영역 -->
			<div class="define_space" style="padding: 0 45px; height:80px">
			
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
				        			boolean isSelectable = ((task.getStatus()==TaskInstance.STATUS_RUNNING||task.getStatus()==TaskInstance.STATUS_DELAYED_RUNNING)
				        										&& !task.getAssignee().getId().equals(cUser.getId())) ? false : true;
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
						            <li class="<%=statusClass %> js_instance_task <%if(isSelectable){%>js_select_task_instance<%} %>" formId="<%=task.getFormId() %>" taskInstId="<%=task.getId()%>" 
						            		formMode="<%=formMode %>" isApprovalWork="<%=task.isApprovalWork()%>" approvalLineId=<%=CommonUtil.toNotNull(approvalLineId) %>>
					                    <!-- task 정보 -->
					                    <div class="title"><%=count%>) <%=task.getName() %></div>
					                    <img src="<%=task.getPerformer().getMinPicture()%>" class="noti_pic profile_size_s">
					                    <div class="noti_in_s">
						                    <div class="t_date"><%=task.getPerformer().getLongName()%></div>
						                    <div class="t_date"><%=task.getLastModifiedDate().toLocalString() %></div>
					                    </div>
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
           			if(taskHistories[taskHistories.length-1].getStatus() == Instance.STATUS_COMPLETED){
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
			<div class="contents_space js_form_header">
				<div class="up_point pos_default js_form_content_pointer"></div>

				<!--  전자결재화면이 나타나는 곳 -->
				<div class="js_form_task_approval js_form_task p0 mb15" 
					<%if(approvalTask==null && (SmartUtil.isBlankObject(taskInstance) || !taskInstance.isApprovalWork())){ %>style="display:none"<%} %>></div>
				
				<div class="form_wrap up form_read js_form_content"></div>
				<div class="js_check_completion_notice" style="display:none">
					<jsp:include page="/jsp/content/upload/check_completion_notice.jsp"></jsp:include>
				</div>
			</div>

			<!-- 업무전달화면이 나타나는 곳 -->
			<div class="js_form_task_forward  js_form_task" <%if(forwardedTask==null){ %>style="display:none"<%} %>>
				<%
				if(forwardedTask!=null){
				%>
					<jsp:include page="/jsp/content/upload/append_task_forward.jsp">
						<jsp:param value="<%=taskInstId %>" name="taskInstId"/>
					</jsp:include>
				<%
				}
				%>
			</div>
				
			<!-- 버튼 영역 -->
			<div class="glo_btn_space">
			
				<!-- 수정, 삭제버튼 -->
			    <div class="fr">

			        <span class="btn_gray js_btn_do_forward" style="display:none">
			        	<a href="" class="js_forward_work_instance">
				            <span class="txt_btn_start"></span>
				            <span class="txt_btn_center"><fmt:message key="common.button.do_forward"/></span>
				            <span class="txt_btn_end"></span>
			            </a>
			   		</span>
			
			        <span class="btn_gray js_btn_reply_forward" style="display:none">
			        	<a href="" class="js_reply_forward">
				            <span class="txt_btn_start"></span>
				            <span class="txt_btn_center"><fmt:message key="common.button.reply_forward"/></span>
				            <span class="txt_btn_end"></span>
			            </a>
			   		</span>
			
			        <span class="btn_gray js_btn_do_approval" style="display:none">
			        	<a href="" class="js_approval_work_instance">
				            <span class="txt_btn_start"></span>
				            <span class="txt_btn_center"><fmt:message key="common.button.do_approval"/></span>
				            <span class="txt_btn_end"></span>
			            </a>
			   		</span>
			
			        <span class="btn_gray js_btn_approve_approval" style="display:none">
			        	<a href="" class="js_reply_approval">
				            <span class="txt_btn_start"></span>
				            <span class="txt_btn_center"><fmt:message key="common.button.approve_approval"/></span>
				            <span class="txt_btn_end"></span>
			            </a>
			   		</span>
			
			        <span class="btn_gray js_btn_submit_approval" style="display:none">
			        	<a href="" class="js_reply_approval">
				            <span class="txt_btn_start"></span>
				            <span class="txt_btn_center"><fmt:message key="common.button.submit_approval"/></span>
				            <span class="txt_btn_end"></span>
			            </a>
			   		</span>
			
			        <span class="btn_gray js_btn_return_approval" style="display:none">
			        	<a href="" class="js_reply_approval">
				            <span class="txt_btn_start"></span>
				            <span class="txt_btn_center"><fmt:message key="common.button.return_approval"/></span>
				            <span class="txt_btn_end"></span>
			            </a>
			   		</span>
			
			        <span class="btn_gray js_btn_reject_approval" style="display:none">
			        	<a href="" class="js_reply_approval">
				            <span class="txt_btn_start"></span>
				            <span class="txt_btn_center"><fmt:message key="common.button.reject_approval"/></span>
				            <span class="txt_btn_end"></span>
			            </a>
			   		</span>
			
			        <span class="btn_gray js_btn_do_email" style="display:none">
			        	<a href="" class="js_email_iwork_instance">
				            <span class="txt_btn_start"></span>
				            <span class="txt_btn_center"><fmt:message key="common.button.do_email"/></span>
				            <span class="txt_btn_end"></span>
			            </a>
			   		</span>
				
					<span class="btn_gray ml5 js_btn_complete" style="display:none">
			        	<a href="" class="js_perform_task_instance">
				            <span class="txt_btn_start"></span>
				            <span class="txt_btn_center"><fmt:message key="common.button.complete"/></span>
				            <span class="txt_btn_end"></span>
				    	</a>
			   		</span>
					<span class="btn_gray ml5 js_btn_return" style="display:none">
			        	<a href="" class="js_return_task_instance">
				            <span class="txt_btn_start"></span>
				            <span class="txt_btn_center"><fmt:message key="common.button.return"/></span>
				            <span class="txt_btn_end"></span>
				    	</a>
			   		</span>
					<span class="btn_gray ml5 js_btn_reassign" style="display:none">
			        	<a href="" class="js_reassign_task_instance">
				            <span class="txt_btn_start"></span>
				            <span class="txt_btn_center"><fmt:message key="common.button.reassign"/></span>
				            <span class="txt_btn_end"></span>
				    	</a>
			   		</span>
					<span class="btn_gray ml5 js_btn_temp_save" style="display:none">
			        	<a href="" class="js_temp_save_task_instance">
				            <span class="txt_btn_start"></span>
				            <span class="txt_btn_center"><fmt:message key="common.button.temp_save"/></span>
				            <span class="txt_btn_end"></span>
				    	</a>
			   		</span>
				</div>
				<!-- 수정, 삭제버튼 //-->    					  

				<!--  실행시 표시되는 프로그래스아이콘을 표시할 공간 -->
				<div class="fr form_space js_progress_span" ></div>

				<div class="txt_btn task_information">
				    <div class="po_left"><fmt:message key="common.title.last_modification"/> :  
				    	<%
			    		User lastModifier = instance.getLastModifier();
			    		String userDetailInfo = SmartUtil.getUserDetailInfo(lastModifier.getUserInfo());
			    		%>
				    	<a class="js_pop_user_info" href="<%=lastModifier.getSpaceController() %>?cid=<%=lastModifier.getSpaceContextId()%>" userId="<%=lastModifier.getId()%>" longName="<%=lastModifier.getLongName() %>" minPicture="<%=lastModifier.getMinPicture() %>" profile="<%=lastModifier.getOrgPicture()%>" userDetail="<%=userDetailInfo%>"><img src="<%=lastModifier.getMinPicture() %>" class="profile_size_s" /> <%=lastModifier.getLongName() %></a>
				    	<span class="t_date"> <%= instance.getLastModifiedDate().toLocalString() %> </span>
				    </div>
				    <%if(numberOfForwardHistories > 0){ %><div class="po_left pt3"><a href="" class="js_toggle_forward_histories"><fmt:message key="common.title.forward_history"/> <span class="t_up_num">[<%=numberOfForwardHistories %>]</span></a></div><%} %>
				</div>     

				<!-- 실행시 데이터 유효성 검사이상시 에러메시지를 표시할 공간 -->
				<span class="form_space sw_error_message js_space_error_message" style="text-align:right; color: red"></span>

			</div>
			<!-- 버튼 영역 //-->     				
			<div class="js_instance_histories"></div>
		</ul>
	</div>
	<div class="portlet_b" style="display: block;"></div>
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
	function clickOnTask(input){
		var pworkSpace = input.parents('.js_pwork_space_page');
		var workId = pworkSpace.attr("workId");
		var formId = input.attr("formId");
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
		
</script>

<jsp:include page="/jsp/content/work/space/space_instance_list.jsp">
	<jsp:param value="<%=work.getId() %>" name="workId"/>
	<jsp:param value="<%=instId %>" name="instId"/>
</jsp:include>	

<!-- 목록 버튼 -->
<div class="tc">
	<div class="btn_gray" >
    	<a href="<%=instance.getWork().getController(wid) %>?cid=<%=instance.getWork().getContextId() %>&wid=<%=CommonUtil.toNotNull(wid) %>" class="js_content"> 
    		<span class="txt_btn_start"></span> 
    		<span class="txt_btn_center"><fmt:message key="common.button.list"/></span> 
    		<span class="txt_btn_end"></span>
    	</a>
	</div>
</div>
<!-- 목록 버튼//-->

