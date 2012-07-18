<%@page import="net.smartworks.model.work.FileCategory"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.work.info.FileCategoryInfo"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	String wid = request.getParameter("wid");
	int displayType = Integer.parseInt(request.getParameter("displayType"));
	String parentId = request.getParameter("parentId");
	
	User cUser = SmartUtil.getCurrentUser();
	FileCategoryInfo[] categories = smartWorks.getFileCategoriesByType(displayType, wid, parentId);

%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<%
if(!SmartUtil.isBlankObject(categories) && categories.length>0){
	for(int i=0; i<categories.length; i++){
		FileCategoryInfo category = categories[i];
%>
		<li>
			<span class="dep">
				<a class="js_file_category_item" href="" categoryId="<%=category.getId()%>"> 
					<span class="<%=category.getIconClass()%>"></span>
					<span><%=category.getName() %> (<%=category.getLength() %>)</span>
					<%
					if(displayType == FileCategory.DISPLAY_BY_CATEGORY 
						&& !category.getId().equals(FileCategory.ID_ALL_FILES) 
						&& !category.getId().equals(FileCategory.ID_UNCATEGORIZED) ){
					%> 
						<!-- 삭제 , 수정버튼 -->
						<span class="ctgr_action">
							<%if(category.getLength()==0){ %>
								<span class="btn_remove_category js_remove_file_folder_btn" folderId="<%=category.getId() %>" title="<fmt:message key='mail.button.text_folder'/>"></span>
							<%} %>
							<span class="btn_text_category js_text_file_folder_btn" folderId="<%=category.getId() %>" folderName="<%=category.getName() %>" title="<fmt:message key='mail.button.text_folder'/>"></span>
						</span>
						<!-- 삭제 , 수정버튼//-->
					<%
					}
					%>
				</a>
			</span>
		</li>
<%
	}
}
%>