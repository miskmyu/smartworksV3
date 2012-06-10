
<!-- Name 			: iwork_space.jsp						 -->
<!-- Description	: 정보관리업무 인스턴스 공간을 표시하는 페이지    -->
<!-- Author			: Maninsoft, Inc.						 -->
<!-- Created Date	: 2011.9.								 -->

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
	String instId = SmartUtil.getSpaceIdFromContentContext(cid);
	String workId = request.getParameter("workId");
	String taskInstId = request.getParameter("taskInstId");
	
	InformationWorkInstance instance = (InformationWorkInstance)smartWorks.getWorkInstanceById(SmartWork.TYPE_INFORMATION, workId, instId);
	User owner = instance.getOwner();
	WorkSpace workSpace = instance.getWorkSpace();
	InformationWork work = (InformationWork)instance.getWork();
	int numberOfRelatedWorks = instance.getNumberOfRelatedWorks();
	int numberOfHistories = 0;
	TaskInstanceInfo[] tasks = instance.getTasks();
	TaskInstanceInfo approvalTask = null;
	TaskInstanceInfo forwardedTask = null;
	if(tasks != null){
		for(TaskInstanceInfo task : tasks){
			if(task.getType() == TaskInstance.TYPE_INFORMATION_TASK_ASSIGNED || task.getType() == TaskInstance.TYPE_INFORMATION_TASK_CREATED
				|| task.getType() == TaskInstance.TYPE_INFORMATION_TASK_UDATED){
				numberOfHistories++;
			}
		}
		if(!SmartUtil.isBlankObject(taskInstId)){
			for(TaskInstanceInfo task : tasks){
				if(task.isRunningForwardedForMe(cUser.getId(), taskInstId)){
					forwardedTask = task;
					break;
				}else if(task.isRunningApprovalForMe(cUser.getId(), taskInstId)){
					approvalTask = task;
					break;
				}
			}
		}
	}
	
	session.setAttribute("cid", cid);
	if(SmartUtil.isBlankObject(wid))
		session.removeAttribute("wid");
	else
		session.setAttribute("wid", wid);
	session.setAttribute("workInstance", instance);
		
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />
<!-- 컨텐츠 레이아웃-->
    <div class="section_portlet js_iwork_space_page" workId="<%=workId%>" instId="<%=instId%>">
    	<div class="portlet_t">
      		<div class="portlet_tl"></div>
    	</div>
    	<div class="portlet_l" style="display: block;">
		    <ul class="portlet_r" style="display: block;">		    		            
            	<div class="body_titl_pic js_form_header">		
                    <div class="">
                        <div class="noti_pic mr7"><img class="profile_size_m" src="<%=instance.getOwner().getMidPicture() %>" /></div>
                        <div class="noti_in_bodytitle">
                        	<div>
	                            <span class="t_name"><%=instance.getOwner().getLongName()%></span>
	                            <%if(workSpace != null && !workSpace.getId().equals(owner.getId())){ %><span class="arr">▶</span><span class="icon_division_s"><%=workSpace.getName() %></span><%} %>
	                            <span class="t_date"><%=instance.getCreatedDate().toLocalString() %></span>
                            </div>
                           	<div class=""><%=instance.getSubject()%></div>
                           	<div class="">
                            	<span class="<%=work.getIconClass() %>"></span>
                            	<span><%=work.getFullpathName() %></span> 
                            </div>
                        </div>
                    </div>
		                    
		            <!-- 우측 버튼-->
		            <div class="txt_btn t_s11">
		                <div class="fr"><a class="js_copy_address" href=""><fmt:message key="common.button.copy_url"/></a></div>
		            </div>
		            
					<!-- 전자결재, 업무전달 버튼들 -->
					<div class="mb10 fr vb">
						<%
						if(approvalTask == null && forwardedTask == null){
						%>
							<a href="" class="js_toggle_forward_btn" title="<fmt:message key='common.button.forward'/>"><span class="icon_forward_w"></span></a>
							<a href="" class="js_toggle_forward_btn" title="<fmt:message key='common.button.approval'/>"><span class="icon_approval_w"></span></a>
		                	<a href="" title="<fmt:message key='common.button.email'/>"><span class="icon_mail_w"></span></a>
		                	<a href="" title="<fmt:message key='common.button.print'/>"><span class="icon_print_w"></span></a>
						<%
						}
						%>
					</div>
					<!-- 전자결재, 업무전달 버튼들 //-->
		                    
                	<div class="solid_line cb"></div>
                </div>
		            <!-- 타이틀 -->
		            
				<!--  전자결재화면이 나타나는 곳 -->
				<div class="js_form_task_approval js_form_task" <%if(approvalTask==null){ %>style="display:none"<%} %>>
				</div>
				
				<!-- 업무전달화면이 나타나는 곳 -->
				<div class="js_form_task_forward  js_form_task" <%if(forwardedTask==null){ %>style="display:none"<%} %>>
					<%
					if(forwardedTask!=null){
					%>
						<jsp:include page="/jsp/content/upload/append_task_forward.jsp">
							<jsp:param value="<%=forwardedTask.getSubject() %>" name="subject"/>
 							<jsp:param value="<%=forwardedTask.getContent() %>" name="content"/>
						</jsp:include>
					<%
					}
					%>
				</div>
				
				<!-- 상세보기 컨텐츠 -->
				<div class="contents_space">				            
			       <div class="list_contents up form_read js_form_content">      
			       </div>
				</div>
		
				<!-- 버튼 영역 -->
				<div class="glo_btn_space">
				
					<div class="txt_btn task_information">
					    <%if(numberOfRelatedWorks > 0){ %><div class="po_left"><a href=""><fmt:message key="common.title.refering_works"/> <span class="t_up_num">[<%=numberOfRelatedWorks %>]</span></a></div><%} %>
					    <%if(numberOfHistories > 0){ %><div class="po_left"><a href=""><fmt:message key="common.title.update_history"/> <span class="t_up_num">[<%=numberOfHistories %>]</span></a></div><%} %>
					    <div class="po_left"><fmt:message key="common.title.last_modification"/> :  
					    	<a href=""><img src="<%=instance.getLastModifier().getMinPicture() %>" class="profile_size_s" /> <%=instance.getLastModifier().getLongName() %></a>
					    	<span class="t_date"> <%= instance.getLastModifiedDate().toLocalString() %> </span>
					    </div>
					</div>     

					<!-- 수정, 삭제버튼 -->
				    <div class="fr">
						<!--  실행시 표시되는 프로그래스아이콘을 표시할 공간 -->
						<div class="form_space js_progress_span" ></div>
						
						<!-- 실행시 데이터 유효성 검사이상시 에러메시지를 표시할 공간 -->
						<span class="form_space sw_error_message js_space_error_message" style="text-align:right; color: red"></span>

						<%
						if(work.getEditPolicy().isEditableForMe(owner.getId())){
						%>
					        <span class="btn_gray js_btn_modify">
					        	<a href="" class="js_modify_iwork_instance">
						            <span class="txt_btn_start"></span>
						            <span class="txt_btn_center"><fmt:message key="common.button.modify"/></span>
						            <span class="txt_btn_end"></span>
					            </a>
					   		</span>
					   	<%
					   	}
					   	%>
				
				        <span class="btn_gray js_btn_do_forward" style="display:none">
				        	<a href="" class="js_forward_iwork_instance">
					            <span class="txt_btn_start"></span>
					            <span class="txt_btn_center"><fmt:message key="common.button.do_forward"/></span>
					            <span class="txt_btn_end"></span>
				            </a>
				   		</span>
				
				        <span class="btn_gray js_btn_do_approval" style="display:none">
				        	<a href="" class="js_approval_iwork_instance">
					            <span class="txt_btn_start"></span>
					            <span class="txt_btn_center"><fmt:message key="common.button.do_approval"/></span>
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
				
						<%
						if(work.getEditPolicy().isEditableForMe(owner.getId())){
						%>
							<span class="btn_gray ml5 js_btn_delete">
					        	<a href="" class="js_delete_iwork_instance">
						            <span class="txt_btn_start"></span>
						            <span class="txt_btn_center"><fmt:message key="common.button.delete"/></span>
						            <span class="txt_btn_end"></span>
						    	</a>
					   		</span>
					        <span class="btn_gray js_btn_save" style="display:none">
					        	<a href="" class="js_save_iwork_instance">
						            <span class="txt_btn_start"></span>
						            <span class="txt_btn_center"><fmt:message key="common.button.save"/></span>
						            <span class="txt_btn_end"></span>
					            </a>
					   		</span>
					   	<%
					   	}
					   	%>
				
						<span class="btn_gray ml5 js_btn_cancel" style="display:none">
				        	<a href="" class="js_cancel_iwork_instance">
					            <span class="txt_btn_start"></span>
					            <span class="txt_btn_center"><fmt:message key="common.button.cancel"/></span>
					            <span class="txt_btn_end"></span>
					    	</a>
				   		</span>
					</div>
					<!-- 수정, 삭제버튼 //-->    					  
				</div>
				<!-- 버튼 영역 //-->     				
			</ul>
		</div>
		<div class="portlet_b" style="display: block;"></div>
	</div> 
	<!-- 컨텐츠 레이아웃//-->
<script type="text/javascript">
	var iworkSpace = $('.js_iwork_space_page');
	var workId = iworkSpace.attr("workId");
	var instId = iworkSpace.attr("instId");
	var formContent = iworkSpace.find('div.js_form_content');
	new SmartWorks.GridLayout({
		target : formContent,
		mode : "view",
		workId : workId,
		recordId : instId,
		onSuccess : function(){
		}
	});
</script>

<jsp:include page="/jsp/content/work/space/space_instance_list.jsp">
	<jsp:param value="<%=work.getId() %>" name="workId"/>
	<jsp:param value="<%=instId %>" name="instId"/>
</jsp:include>	
