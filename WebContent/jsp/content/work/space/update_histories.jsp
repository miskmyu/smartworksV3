
<!-- Name 			: append_task_forward.js							 -->
<!-- Description	: 새업무를 등록시 다른사용자에게 업무전달을 위한 화	      	 -->
<!-- Author			: Maninsoft, Inc.									 -->
<!-- Created Date	: 2011.9.											 -->

<%@page import="net.smartworks.model.instance.info.InstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.InstanceInfoList"%>
<%@page import="net.smartworks.model.instance.info.RequestParams"%>
<%@page import="net.smartworks.util.SmartMessage"%>
<%@page import="net.smartworks.model.instance.info.WorkInstanceInfo"%>
<%@page import="net.smartworks.model.instance.TaskInstance"%>
<%@page import="net.smartworks.model.instance.Instance"%>
<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.model.instance.info.TaskInstanceInfo"%>
<%@page import="net.smartworks.model.instance.WorkInstance"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 현재사용자 정보도 가져온다..
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();
		
	String instanceId = request.getParameter("instanceId");
	RequestParams params = (RequestParams)request.getAttribute("requestParams");

	if(SmartUtil.isBlankObject(params)){
		params = new RequestParams();
		params.setPageSize(20);
		params.setCurrentPage(1);
	}
	InstanceInfoList updateList = smartWorks.getUpdateHistoryList(instanceId, params);	
	
%>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<script type="text/javascript">
	getIntanceList = function(paramsJson, progressSpan, isGray){
		smartPop.progressCont(progressSpan);
		console.log(JSON.stringify(paramsJson));
		var url = "set_instance_list_params.sw";
		$.ajax({
			url : url,
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				$('.js_instance_histories').html(data);
				smartPop.closeProgress();
			},
			error : function(xhr, ajaxOptions, thrownError) {
				smartPop.closeProgress();
				smartPop.showInfo(smartPop.ERROR, smartMessage.get('workListError'));
			}
		});
	};
	
	selectListParam = function(progressSpan, isGray){
		var updateHistories = $('.js_update_histories_page');
		var forms = updateHistories.find('form:visible');
		var paramsJson = {};
		var instanceId = updateHistories.parents('.js_iwork_space_page').attr('instId');
		paramsJson["href"] = "jsp/content/work/space/update_histories.jsp?instanceId=" + instanceId;
		for(var i=0; i<forms.length; i++){
			var form = $(forms[i]);
			paramsJson[form.attr('name')] = mergeObjects(form.serializeObject(), SmartWorks.GridLayout.serializeObject(form));
		}
		if(isEmpty(progressSpan)) progressSpan = updateHistories.find('span.js_progress_span:first');
		getIntanceList(paramsJson, progressSpan, isGray);		
	};
</script>

<!-- 업무계획하기 -->
<div class="js_update_histories_page">
	<%
	int pageSize = 20, totalPages = 1, currentPage = 1;
	if (updateList != null) {
		pageSize = updateList.getPageSize();
		totalPages = updateList.getTotalPages();
		currentPage = updateList.getCurrentPage();
		int currentCount = updateList.getTotalSize()-(currentPage-1)*pageSize;
		if(updateList.getInstanceDatas() != null) {
			TaskInstanceInfo[] histories = (TaskInstanceInfo[]) updateList.getInstanceDatas();
	%>
			<div class="up_point pos_works"></div> 
	            
			<!-- 컨텐츠 -->
		   	<div class="form_wrap up history_list">    
			    <!-- 리스트 -->       
				<ul class="p10">
					<%
					for (TaskInstanceInfo task : histories) {
						if(SmartUtil.isBlankObject(task)) continue;
						UserInfo owner = null;
						WorkInstanceInfo workInstance = task.getWorkInstance();
						String statusImage = "";
						String statusTitle = "";
						String activity = "";
						switch (task.getStatus()) {
						// 인스턴스가 현재 진행중인 경우..
						case Instance.STATUS_RUNNING:
							statusImage = "icon_status_running";
							statusTitle = "content.status.running";
							break;
						// 인스턴스가 지연진행중인 경우....
						case Instance.STATUS_DELAYED_RUNNING:
							statusImage = "icon_status_d_running";
							statusTitle = "content.status.delayed_running";
							break;
						// 인스턴스가 반려된 경우...
						case Instance.STATUS_RETURNED:
							statusImage = "icon_status_returned";
							statusTitle = "content.status.returned";
							break;
							// 인스턴스가 반려된 경우...
						case Instance.STATUS_COMPLETED:
							statusImage = "icon_status_completed";
							statusTitle = "content.status.completed";
							break;
							// 기타 잘못되어 상태가 없는 경우..
						default:
							statusImage = "icon_status_not_yet";
							statusTitle = "content.status.not_yet";
						}
						owner = task.getOwner();
						if(currentCount==1){
							activity = SmartMessage.getString("common.title.created");								
						}else{
							switch(task.getTaskType()){
							case TaskInstance.TYPE_INFORMATION_TASK_CREATED:
								activity = SmartMessage.getString("common.title.created");
								break;
							case TaskInstance.TYPE_INFORMATION_TASK_UPDATED:
								activity = SmartMessage.getString("common.title.updated");
								break;
							case TaskInstance.TYPE_INFORMATION_TASK_ASSIGNED:
								activity = SmartMessage.getString("common.title.assigned");
								break;
							case TaskInstance.TYPE_INFORMATION_TASK_DELETED:
								activity = SmartMessage.getString("common.title.deleted");
								break;								
							}
						}
					%>
						<li class="sub_instance_list js_show_instance" taskInstId="<%=task.getId()%>" formId="<%=task.getFormId()%>">
							<div class="det_title" style="line-height: 16px">
					        	<span class="number"><%=currentCount %></span>
					            <span class="<%=statusImage %> vm" title="<fmt:message key='statusTitle'/>"></span>
					            <span class="task_state">
					            	<span class="icon_txt gray"><%=activity %></span>
					            </span>
					            <a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>">
					                <img class="profile_size_c" src="<%=owner.getMinPicture()%>">
					            </a>
					            <span class="vm">
					                <div>
					                    <a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>">
					                        <span class="t_name"><%=owner.getLongName()%></span>
					                    </a>
					                    <span class="ml5 t_date"><%=task.getLastModifiedDate().toLocalDateTimeSimpleString()%></span>
					                </div>
					                <div class="tb"><%=CommonUtil.toNotNull(task.getSubject())%><%if(task.isNew()){ %><span class="ml5 icon_new"></span><%} %></div>
					            </span>
					        </div>
						</li>					
					<%
						currentCount--;
					}
					%>
				</ul>
			</div>
	<%
		}
	}
	%>					
</div>
