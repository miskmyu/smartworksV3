<%@page import="net.smartworks.model.work.info.FileCategoryInfo"%>
<%@page import="net.smartworks.model.work.FileCategory"%>
<%@page import="net.smartworks.model.community.WorkSpace"%>
<%@page import="net.smartworks.model.work.info.ImageCategoryInfo"%>
<%@page import="net.smartworks.model.filter.info.SearchFilterInfo"%>
<%@page import="net.smartworks.model.work.ProcessWork"%>
<%@page import="net.smartworks.model.work.FormField"%>
<%@page import="net.smartworks.model.work.SmartForm"%>
<%@page import="net.smartworks.model.filter.SearchFilter"%>
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
	String cid = request.getParameter("cid");
	String wid = request.getParameter("wid");
	
	User cUser = SmartUtil.getCurrentUser();
	
	WorkSpace workSpace = smartWorks.getWorkSpaceById(wid);

	ImageCategoryInfo[] categories = smartWorks.getImageCategoriesByType(FileCategory.DISPLAY_BY_CATEGORY, wid);
		
	session.setAttribute("cid", cid);
	session.setAttribute("wid", wid);
	session.setAttribute("lastLocation", "image_list.sw");
	session.setAttribute("workSpace", workSpace);
	
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />
<!-- 타이틀 -->

<jsp:include page="/jsp/content/community/space/space_title.jsp"></jsp:include>

<jsp:include page="/jsp/content/upload/select_upload_action.jsp"></jsp:include>

<!-- 컨텐츠 레이아웃-->
<div class="section_portlet js_image_list_page js_work_list_page">
	<div class="portlet_t"><div class="portlet_tl"></div></div>
	<div class="portlet_l" style="display: block;">
		<ul class="portlet_r" style="display: block;">
			<!-- 컨텐츠 -->
			<div class="contents_space">
			
				<!-- 필터 -->
	        	<div class="txt_btn tr mb2 js_image_list_header" style="height:25px">
    				<select class="js_image_display_by fl">
	            			<option selected value="<%=FileCategory.DISPLAY_BY_CATEGORY%>"><fmt:message key="space.title.by_category"/></option>
	        				<option value="<%=FileCategory.DISPLAY_BY_YEAR%>"><fmt:message key="space.title.by_year"/></option>
	        				<option value="<%=FileCategory.DISPLAY_BY_OWNER%>"><fmt:message key="space.title.by_owner"/></option>
	          		</select>
    				<select class="js_image_category_list fl">
						<%
						if(!SmartUtil.isBlankObject(categories)){
							for(int i=0; i<categories.length; i++){
								ImageCategoryInfo category = categories[i];
						%>
								<option value=<%=category.getId() %>><%=category.getName() %></option>
						<%								
							}
						}
						%>
	          		</select>
					<span class="js_progress_span fl"></span>
	       		</div>
	     		<!-- 필터//-->
	     		<div class="solid_line"></div>
               
				<!-- 사진 목록 -->
				<div class="picture_section js_image_instance_list">
					<jsp:include page="/jsp/content/work/list/image_instance_list.jsp">
						<jsp:param value="<%=FileCategory.DISPLAY_BY_CATEGORY %>" name="displayType"/>
						<jsp:param value="" name="parentId"/>
					</jsp:include>
				</div>
      			<!-- 사진 목록// -->
			</div>
			<!-- 컨텐츠 //-->
		</ul>
	</div>
	<div class="portlet_b" style="display: block;"></div>
</div>
<!-- 컨텐츠 레이아웃//-->
