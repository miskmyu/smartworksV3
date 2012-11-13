<%@page import="pro.ucity.model.OPSituation"%>
<%@page import="pro.ucity.model.System"%>
<%@page import="pro.ucity.model.Event"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.model.instance.WorkInstance"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.model.work.info.SmartFormInfo"%>
<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.model.work.info.SmartTaskInfo"%>
<%@page import="net.smartworks.model.work.SmartForm"%>
<%@page import="net.smartworks.model.work.SmartTask"%>
<%@page import="net.smartworks.model.work.SmartDiagram"%>
<%@page import="net.smartworks.model.work.ProcessWork"%>
<%@page import="net.smartworks.model.instance.CommentInstance"%>
<%@page import="net.smartworks.model.instance.MemoInstance"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.model.security.EditPolicy"%>
<%@page import="net.smartworks.model.security.WritePolicy"%>
<%@page import="net.smartworks.model.security.AccessPolicy"%>
<%@page import="net.smartworks.model.work.InformationWork"%>
<%@page import="net.smartworks.model.work.SmartWork"%>
<%@page import="net.smartworks.model.work.Work"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();

	String STATUS_CODE_OCCURRENCE = "OCCURRENCE";
	String STATUS_CODE_RECEIPT = "RECEIPT";
	String STATUS_CODE_FINISH = "FINISH";

	String userviceCode = request.getParameter("userviceCode");
	String serviceCode = request.getParameter("serviceCode");
	String eventCode = request.getParameter("eventCode");
	String situationStatus = request.getParameter("statusCode");
	if(SmartUtil.isBlankObject(situationStatus))
		situationStatus = OPSituation.STATUS_SITUATION_OCCURRED;
	else if(situationStatus.equals(STATUS_CODE_OCCURRENCE))
		situationStatus = OPSituation.STATUS_SITUATION_OCCURRED;
	else if(situationStatus.equals(STATUS_CODE_RECEIPT))
		situationStatus = OPSituation.STATUS_SITUATION_PROCESSING;
	else if(situationStatus.equals(STATUS_CODE_FINISH))
		situationStatus = OPSituation.STATUS_SITUATION_RELEASE;
		
	String eventId = null;
	if(SmartUtil.isBlankObject(userviceCode) || SmartUtil.isBlankObject(serviceCode) || SmartUtil.isBlankObject(eventCode))
	 	eventId = Event.ID_ENV_GALE;
	else
		eventId = Event.getEventIdByCode(userviceCode, serviceCode, eventCode);

	String workId = System.getManualProcessId(userviceCode, serviceCode, eventCode, situationStatus);
	// TEST PURPOSE
	if(SmartUtil.isBlankObject(workId)) workId = "pkg_336b0e079fc44ab19acbe49ded2e8b12";
	// TEST PURPOSE

	ProcessWork work = (ProcessWork)smartWorks.getWorkById(workId);
	SmartDiagram diagram = null;
	SmartTaskInfo[] tasks = null;
	if(!SmartUtil.isBlankObject(work)){
		diagram = work.getDiagram();
		if (diagram != null)
			tasks = diagram.getTasks();
	}
	
	String pointerPos = (situationStatus.equals(OPSituation.STATUS_SITUATION_OCCURRED)) ? "top:85px;" :  (situationStatus.equals(OPSituation.STATUS_SITUATION_PROCESSING)) ? "top:210px;" : "top:330px";
	
%>
<script type="text/javascript">

// 완료버튼 클릭시 create_new_iwork.sw 서비스를 실행하기 위해 submit하는 스크립트..
function submitForms() {
	var pworkManual = $('.js_pwork_manual_page');
	
	var forms = pworkManual.find('form');
	var workId = pworkManual.attr('workId');
	var paramsJson = {};
	paramsJson['workId'] = workId;
	for(var i=0; i<forms.length; i++){
		var form = $(forms[i]);
		
		// 폼이 스마트폼이면 formId와 formName 값을 전달한다...
		if(form.attr('name') === 'frmSmartForm'){
			paramsJson['formId'] = form.attr('formId');
			paramsJson['formName'] = form.attr('formName');
		}
		
		// 폼이름 키값으로 하여 해당 폼에 있는 모든 입력항목들을 JSON형식으로 Serialize 한다...
		paramsJson[form.attr('name')] = mergeObjects(form.serializeObject(), SmartWorks.GridLayout.serializeObject(form));
		
	}
	console.log(JSON.stringify(paramsJson));
	var url = "set_pwork_manual.sw";
	
	// 서비스요청 프로그래스바를 나타나게 한다....
	var progressSpan = pworkManual.find('.js_progress_span');
	smartPop.progressCont(progressSpan);
	
	// set_iwork_manual.sw서비스를 요청한다..
	$.ajax({
		url : url,
		contentType : 'application/json',
		type : 'POST',
		data : JSON.stringify(paramsJson),
		success : function(data, status, jqXHR) {
			
			// 성공시에 프로그래스바를 제거하고 성공메시지를 보여준다...
			window.location.reload(true);
			smartPop.closeProgress();
		},
		error : function(e) {
			// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
			smartPop.closeProgress();
			smartPop.showInfo(smartPop.ERROR, smartMessage.get("setWorkManualError"));
		}
	});
	return;
}

</script>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />
<%if(!SmartUtil.isBlankObject(work)){ %>
	<!-- 업무 설명 보기 -->
	<div class="contents_space js_pwork_manual_page js_sub_instance_list js_space_sub_instance" workId="<%=work.getId()%>" workType="<%=work.getType()%>"
									userviceCode="<%=userviceCode %>" serviceCode="<%=serviceCode %>" eventCode="<%=eventCode %>" situationStatus="<%=situationStatus %>" >
	
		<!-- 타이틀-->
		<div class="list_title_space bn">
			<div class="title guide"><%=CommonUtil.toNotNull(Event.getEventNameByCode(eventId)) %> 상황 운영 가이드</div>
		</div>
		<!-- 타이틀//-->
	
		<!-- 컨텐츠 -->
		<div class="section_guide">
	
			<!-- Left Section -->
			<div class="section_lft">
				<div class="lft_title"></div>
				<div class="lft_step">
					<div class="s1 <%if(OPSituation.STATUS_SITUATION_OCCURRED.equals(situationStatus)){ %>current<%}%>">
						<a class="js_ucity_content" href="situationManual.sw?userviceCode=<%=userviceCode%>&serviceCode=<%=serviceCode%>&eventCode=<%=eventCode%>&statusCode=<%=OPSituation.STATUS_SITUATION_OCCURRED%>"> </a>
					</div>
					<div class="arr"></div>
					<div class="s2 <%if(OPSituation.STATUS_SITUATION_PROCESSING.equals(situationStatus)){ %>current<%}%>">
						<a class="js_ucity_content" href="situationManual.sw?userviceCode=<%=userviceCode%>&serviceCode=<%=serviceCode%>&eventCode=<%=eventCode%>&statusCode=<%=OPSituation.STATUS_SITUATION_PROCESSING%>"> </a>
					</div>
					<div class="arr"></div>
					<div class="s3 <%if(OPSituation.STATUS_SITUATION_RELEASE.equals(situationStatus)){ %>current<%}%> end">
						<a class="js_ucity_content" href="situationManual.sw?userviceCode=<%=userviceCode%>&serviceCode=<%=serviceCode%>&eventCode=<%=eventCode%>&statusCode=<%=OPSituation.STATUS_SITUATION_RELEASE%>"> </a>
					</div>
				</div>
			</div>
			<!-- Left Section //-->
	
			<!-- Right Section -->
			<div class="section_rgt">
				<div class="point" style="<%=pointerPos%>"></div>
				<div class="group_rgt js_manual_tasks_holder" style="overflow:hidden">
					<div class="working_proces js_manual_tasks">
						<ul>
							<!-- 태스크 //-->
							<%
							if (tasks != null) {
								int count = 0;
								for (SmartTaskInfo task : tasks) {
									count++;
									String currentClass = (count==1) ? "current" : "";
							%>
									<li class="task <%=currentClass %> js_manual_task js_select_situation_manual" taskId="<%=task.getId()%>">
										<a class="js_select_task_manual" href=""> 
											<span class="<%=("n" + count)%>"> </span>
											<div class="task_tx"><%=task.getName()%></div>
										</a>
									</li>
									<%if(count!=tasks.length){ %>
										<li class="arr"></li>
									<%} %>
							<%
									}
								}
							%>
							<!-- 태스크 -->
						</ul>
					</div>
					<div class="section_guide_tx">
						<form class="rgt_area" name="frmPWorkManual">	
							<%if(!SmartUtil.isBlankObject(tasks) && tasks.length>0){
								for(int i=0; i<tasks.length; i++){
							%>
									<div class="js_situation_task" taskId="<%=tasks[i].getId()%>" <%if(i>0){ %>style="display:none"<%} %>>
										<div class="js_form_desc_view"><%=tasks[i].getForm().getDescription() %></div>
										<div class="js_form_desc_edit"  style="display:none">
											<span class="fr js_situation_editor_box" fieldName="txtaFormDesc<%=tasks[i].getId() %>">
												<input name="rdoEditor<%=i %>" type="radio" checked value="text"/>미리보기
												<input name="rdoEditor<%=i %>" type="radio" value="editor"/>편집기
											</span>
											<div class="js_form_desc_text" style="height: 262px"><%=CommonUtil.toNotNull(tasks[i].getForm().getDescription()) %></div>
											<input type="hidden" name="txtaFormDesc<%=tasks[i].getId() %>" value="<%=SmartUtil.smartEncode(CommonUtil.toNotNull(tasks[i].getForm().getDescription())) %>"/>
											<div class="js_form_desc_editor"></div>
										</div>
									</div>
							<%
								}
							}
							%>
						</form>
					</div>
				</div>
			</div>
			<!-- Right Section //-->
		    <!-- 라인 -->
			<div class="solid_line_s mt10 mb5"></div>		
	
			<!-- 우측 버튼 -->
			<div class="txt_btn">	
	
				<!-- 수정하기 -->
				<div class="fr ml5">
					<%
					if(work.amIBuilderUser()) {
					%>

					<span class="btn_gray js_modify_situation_manual"> 

							<a href="">
								<span class="txt_btn_start"></span>
								<span class="txt_btn_center"><fmt:message key='common.button.modify' /> </span> 
								<span class="txt_btn_end"></span>
							</a>
						</span>
						<span class="fr btn_gray js_cancel_situation_manual" style="display:none"> 
							<a href="">
								<span class="txt_btn_start"></span> 
								<span class="txt_btn_center"><fmt:message key='common.button.cancel' /> </span>
								<span class="txt_btn_end"></span>
							</a>
						</span>
						<span class="fr btn_gray js_save_situation_manual" style="display:none"> 
							<a href="">
								<span class="txt_btn_start"></span> 
								<span class="txt_btn_center"><fmt:message key='common.button.save' /> </span>
								<span class="txt_btn_end"></span>
							</a>
						</span>
				<%
					}

					%>
				</div>
				<!-- 수정하기 //-->
			</div>
	
		</div>
	 </div>
	<!-- 업무 설명 보기 -->
<%}else{ %>
	<script>
		alert("등록되지 않은 이벤트 코드 입니다. 관리자에게 문의하여 주시기 바랍니다!");
	</script>
<%} %>
<script>
	var manualFile = $('.js_pwork_manual_page').find('.js_manual_file');
	if(!isEmpty(manualFile)){
		var groupId = manualFile.attr('groupId');
		viewFiles(groupId, manualFile);
	}
</script>

