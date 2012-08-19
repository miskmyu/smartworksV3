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
	InstanceInfoList downloadList = smartWorks.getDownloadHistoryList(instanceId, params);	
	
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
		var downloadHistories = $('.js_download_histories_page');
		var forms = downloadHistories.find('form:visible');
		var paramsJson = {};
		var instanceId = downloadHistories.parents('.js_iwork_space_page').attr('instId');
		paramsJson["href"] = "jsp/content/work/space/download_histories.jsp?instanceId=" + instanceId;
		for(var i=0; i<forms.length; i++){
			var form = $(forms[i]);
			paramsJson[form.attr('name')] = mergeObjects(form.serializeObject(), SmartWorks.GridLayout.serializeObject(form));
		}
		if(isEmpty(progressSpan)) progressSpan = downloadHistories.find('span.js_progress_span:first');
		getIntanceList(paramsJson, progressSpan, isGray);		
	};
</script>

<!-- 업무계획하기 -->
<div class="js_download_histories_page">
	<%
	int pageSize = 20, totalPages = 1, currentPage = 1;
	if (downloadList != null) {
		pageSize = downloadList.getPageSize();
		totalPages = downloadList.getTotalPages();
		currentPage = downloadList.getCurrentPage();
		int currentCount = downloadList.getTotalSize()-(currentPage-1)*pageSize;
		if(downloadList.getInstanceDatas() != null) {
			InstanceInfo[] histories = (InstanceInfo[]) downloadList.getInstanceDatas();
	%>
			<div class="up_point pos_works"></div> 
	            
			<!-- 컨텐츠 -->
		   	<div class="form_wrap up history_list">    
			    <!-- 리스트 -->       
				<ul class="p10">
					<%
					for (InstanceInfo history : histories) {
						UserInfo owner = history.getOwner();
					%>
				        <li class="sub_instance_list">
							<div class="det_title" style="line-height: 16px">
					        	<span class="number"><%=currentCount %></span>
					            <span class="task_state">
					            	<span class="icon_txt gray"><fmt:message key="content.status.completed"/></span>
					            </span>
					            <span><%=history.getSubject() %></span>
					            <a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>">
					                <img class="profile_size_c" src="<%=owner.getMinPicture()%>">
					            </a>
					            <span class="vm">
					                <div>
					                    <a href="<%=owner.getSpaceController() %>?cid=<%=owner.getSpaceContextId()%>">
					                        <span class="t_name"><%=owner.getLongName()%></span>
					                    </a>
					                    <span class="ml5 t_date"><%=history.getCreatedDate().toLocalDateTimeSimpleString()%></span>
					                </div>
					            </span>
					    	</div>
				        </li>
					<%
						currentCount--;
					}
					%>
				</ul>
				<form name="frmInstanceListPaging">
					<!-- 페이징 -->
					<div class="paginate">
						<%
						if (currentPage > 0 && totalPages > 0 && currentPage <= totalPages) {
							boolean isFirst10Pages = (currentPage <= 10) ? true : false;
							boolean isLast10Pages = (((currentPage - 1)  / 10) == ((totalPages - 1) / 10)) ? true : false;
							int startPage = ((currentPage - 1) / 10) * 10 + 1;
							int endPage = isLast10Pages ? totalPages : startPage + 9;
							if (!isFirst10Pages) {
						%>
								<a class="pre_end js_select_paging" href="" title="<fmt:message key='common.title.first_page'/>">
									<span class="spr"></span>
									<input name="hdnPrevEnd" type="hidden" value="false"> 
								</a>		
								<a class="pre js_select_paging" href="" title="<fmt:message key='common.title.prev_10_pages'/> ">
									<span class="spr"></span>
									<input name="hdnPrev10" type="hidden" value="false">
								</a>
							<%
							}
							for (int num = startPage; num <= endPage; num++) {
								if (num == currentPage) {
							%>
									<strong><%=num%></strong>
									<input name="hdnCurrentPage" type="hidden" value="<%=num%>"/>
								<%
								} else {
								%>
									<a class="num js_select_current_page" href=""><%=num%></a>
								<%
								}
							}
							if (!isLast10Pages) {
							%>
								<a class="next js_select_paging" title="<fmt:message key='common.title.next_10_pages'/> ">
									<span class="spr"></span>
									<input name="hdnNext10" type="hidden" value="false"/>
								</a>
								<a class="next_end js_select_paging" title="<fmt:message key='common.title.last_page'/> ">
									<span class="spr"><input name="hdnNextEnd" type="hidden" value="false"/></span> 
								</a>
						<%
							}
						}
						%>
						<span class="js_progress_span"></span>
					</div>
					
					<div class="num_box">
						<span class="js_progress_span"></span>
						<select class="js_select_page_size" name="selPageSize" title="<fmt:message key='common.title.count_in_page'/>">
							<option <%if (pageSize == 10) {%> selected <%}%>>10</option>
							<option <%if (pageSize == 20) {%> selected <%}%>>20</option>
							<option <%if (pageSize == 30) {%> selected <%}%>>30</option>
							<option <%if (pageSize == 50) {%> selected <%}%>>50</option>
						</select>
					</div>
					<!-- 페이징 //-->
				</form>
			</div>
	<%
		}
	}
	%>					

</div>
