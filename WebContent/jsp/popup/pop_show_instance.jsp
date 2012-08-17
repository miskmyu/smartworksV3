<!-- Name 			: pop_show_instance.jsp								 -->
<!-- Description	: 인스턴스정보 상세보기 팝업화면 							 	-->
<!-- Author			: Maninsoft, Inc.									 -->
<!-- Created Date	: 2012.08.01.										-->

<%@page import="net.smartworks.model.community.WorkSpace"%>
<%@page import="net.smartworks.model.instance.info.TaskInstanceInfo"%>
<%@page import="net.smartworks.model.instance.TaskInstance"%>
<%@page import="net.smartworks.model.instance.ProcessWorkInstance"%>
<%@page import="net.smartworks.model.instance.Instance"%>
<%@page import="net.smartworks.model.work.Work"%>
<%@page import="net.smartworks.model.instance.WorkInstance"%>
<%@page import="net.smartworks.model.instance.ImageInstance"%>
<%@page import="net.smartworks.model.work.SmartWork"%>
<%@page import="net.smartworks.model.instance.InformationWorkInstance"%>
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
	//스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 현재사용자 정보도 가져온다.
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();
	String workId = request.getParameter("workId");
	String instId = request.getParameter("instId");
	String taskInstId = request.getParameter("taskInstId");
	String formId = request.getParameter("formId");
	if(SmartUtil.isBlankObject(workId) && !SmartUtil.isBlankObject(formId)) workId = smartWorks.getWorkIdByFormId(formId);
	
	int workType = WorkInstance.TYPE_INFORMATION;
	Instance instance = null;
	InformationWorkInstance iworkInstance = null;
	ProcessWorkInstance pworkInstance = null;
	TaskInstance taskInstance = null;
	TaskInstanceInfo approvalTask = null;
	TaskInstanceInfo forwardedTask = null;
	
	if(!SmartUtil.isBlankObject(formId) && !SmartUtil.isBlankObject(instId)){
		iworkInstance = (InformationWorkInstance)smartWorks.getWorkInstanceById(SmartWork.TYPE_INFORMATION, workId, instId);
		instance = (Instance)iworkInstance;
	}else{
		instance = smartWorks.getInstanceById(instId);
	}

	User owner = instance.getOwner();
	WorkSpace workSpace = instance.getWorkSpace();
	SmartWork work = (SmartWork)instance.getWork();
	
	switch(instance.getType()){
	case WorkInstance.TYPE_INFORMATION:
		workType = SmartWork.TYPE_INFORMATION;
		iworkInstance = (InformationWorkInstance)instance;
		break;
	case WorkInstance.TYPE_PROCESS:
		workType = SmartWork.TYPE_PROCESS;
		pworkInstance = (ProcessWorkInstance)instance;
		break;
	case Instance.TYPE_TASK:
		workType = SmartWork.TYPE_INFORMATION;
		taskInstance = (TaskInstance)instance;

		break;
	}

%>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!--  전체 레이아웃 -->
<div class="pop_corner_all js_show_instance_page" instId="<%=instId %>" workType="<%=workType%>" workId="<%=workId%>">
	
	<!-- 타이틀 -->
	<div class="body_titl_pic mt5">
		<div class="noti_pic mr7"><img class="profile_size_m" src="<%=instance.getOwner().getMidPicture() %>" /></div>
		<div class="noti_in_bodytitle case_2line">
			<div>
		     <span class="t_name"><%=instance.getOwner().getLongName()%></span>
		     <%if(workSpace != null && !workSpace.getId().equals(owner.getId())){ %><span class="arr">▶</span><span class="icon_division_s"><%=workSpace.getName() %></span><%} %>
		     <span class="t_date"><%=instance.getCreatedDate().toLocalString() %></span>
		    </div>
		    <div>
		    	<span class="<%=work.getIconClass() %> t_date"> <%=work.getFullpathName() %>
		    		<span class="title_picico ml5"><%=instance.getSubject()%></span>
		    	</span>
		    	
		   	</div>
		</div>
		<div class="txt_btn">
			<a onclick="smartPop.close();return false;" href="">
				<div class="btn_x"></div>
			</a>
		</div>

		<div class="solid_line"></div>
	</div>
	<!-- 타이틀//-->

	<!-- 팝업 컨텐츠 -->
	<div class="js_sub_instance_list pop_picture_section">
	
		<!-- 업무전달화면이 나타나는 곳 -->
		<div class="js_form_task_forward  js_form_task" <%if(forwardedTask==null){ %>style="display:none"<%} %>>
			<%
			if(forwardedTask!=null){
			%>
				<jsp:include page="/jsp/content/upload/append_task_forward.jsp">
					<jsp:param value="<%=instId %>" name="taskInstId"/>
				</jsp:include>
			<%
			}
			%>
		</div>
		
		<!--  전자결재화면이 나타나는 곳 -->
		<div class="js_form_task_approval js_form_task" <%if(approvalTask==null && iworkInstance!=null && !iworkInstance.isApprovalWork()){ %>style="display:none"<%} %>>
			<%
			if(approvalTask!=null || iworkInstance.isApprovalWork()){
			%>
				<jsp:include page="/jsp/content/upload/append_task_approval.jsp">
					<jsp:param value="<%=instId %>" name="taskInstId"/>
				</jsp:include>
			<%
			}
			%>
		</div>
		
		<!-- 상세보기 컨텐츠 -->
		<div class="contents_space">				            
	       <div class="up form_read js_form_content">      
	       </div>
		</div>
		
	</div>
	<!-- 팝업 컨텐츠 //-->

	<script type="text/javascript">
	
		var mode = "view";
		<%
		if(instance.getStatus() == Instance.STATUS_RETURNED){
		%>
			mode = "edit";
		<%
		}
		%>
		var showInstance = $('.js_show_instance_page');
		var workId = showInstance.attr("workId");
		var instId = showInstance.attr("instId");
		var formContent = showInstance.find('div.js_form_content');
		new SmartWorks.GridLayout({
			target : formContent,
			mode : mode,
			workId : workId,
			recordId : instId,
			onSuccess : function(){
			}
		});
		
		<%
		if(SmartUtil.isBlankObject(approvalTask) && SmartUtil.isBlankObject(forwardedTask) && iworkInstance!=null && iworkInstance.isApprovalWork()){
		%>
			showInstance.find('.js_btn_approve_approval').hide().siblings().hide();			
		<%
		}
		%>
	</script>
	
<%-- 	<jsp:include page="/jsp/content/work/space/space_instance_list.jsp">
		<jsp:param value="<%=workId %>" name="workId"/>
		<jsp:param value="<%=instId %>" name="instId"/> 
	</jsp:include>
 --%>
</div>
<!-- 전체 레이아웃//-->
