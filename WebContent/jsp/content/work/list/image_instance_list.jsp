<%@page import="net.smartworks.model.work.FileCategory"%>
<%@page import="net.smartworks.model.community.Department"%>
<%@page import="net.smartworks.model.community.Group"%>
<%@page import="net.smartworks.model.community.Community"%>
<%@page import="net.smartworks.model.community.WorkSpace"%>
<%@page import="net.smartworks.model.instance.info.ImageInstanceInfo"%>
<%@page import="net.smartworks.model.work.ImageCategory"%>
<%@page import="net.smartworks.model.work.info.ImageCategoryInfo"%>
<%@page import="net.smartworks.model.instance.info.WorkInstanceInfo"%>
<%@page import="net.smartworks.model.work.info.SmartTaskInfo"%>
<%@page import="net.smartworks.model.instance.Instance"%>
<%@page import="net.smartworks.model.instance.SortingField"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.service.impl.SmartWorks"%>
<%@page import="net.smartworks.model.instance.info.TaskInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.PWInstanceInfo"%>
<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.model.instance.info.InstanceInfo"%>
<%@page import="net.smartworks.model.work.ProcessWork"%>
<%@page import="net.smartworks.model.instance.FieldData"%>
<%@page import="net.smartworks.model.instance.info.InstanceInfoList"%>
<%@page import="net.smartworks.model.instance.info.RequestParams"%>
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
	User cUser = SmartUtil.getCurrentUser();
	String cid = (String) session.getAttribute("cid");
	String wid = (String) session.getAttribute("wid");
	Community workSpace = smartWorks.getWorkSpaceById(wid);
	int displayType = Integer.parseInt(request.getParameter("displayType"));
	String parentId = request.getParameter("parentId");
	if(!SmartUtil.isBlankObject(parentId) && parentId.equals(FileCategory.ID_ALL_FILES)) 
		parentId = "";
	String strLastDate = request.getParameter("lastDate"); 
	LocalDate lastDate = new LocalDate();
	if(!SmartUtil.isBlankObject(strLastDate))		
		lastDate = LocalDate.convertLocalStringToLocalDate(strLastDate);
	
	String workId = SmartWork.ID_FILE_MANAGEMENT;
%>

<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<ul class="js_image_instance_list_page" parentId="<%=parentId %>" displayType="<%=displayType %>" spaceId="<%=wid%>">
	<%
	if(SmartUtil.isBlankObject(parentId)){
		ImageCategoryInfo[] imageCategories = smartWorks.getImageCategoriesByType(displayType, wid);
		if(!SmartUtil.isBlankObject(imageCategories) && imageCategories.length>0 ){
	%>
			<!-- 폴더 추가 -->
			<div class="tab_buttons js_add_image_folder_btn">
				<a href="" title="<fmt:message key='common.button.add_new_folder'/>"><span class="btn_bfolder_add"></span></a>
			</div>
			<!-- 폴더추가 //-->	
	<%
			for(int i=0; i<imageCategories.length; i++){
				ImageCategoryInfo category = imageCategories[i];
				if(category.getId().equals(FileCategory.ID_ALL_FILES)) continue;
	%>
				<!--폴더 목록1 -->
				<li>
					
					<div class="picture_folder">
						<%
						if(displayType == ImageCategory.DISPLAY_BY_CATEGORY &&
							!category.getId().equals(FileCategory.ID_UNCATEGORIZED) &&
							(wid.equals(cUser.getId()) 
								|| ((workSpace.getClass().equals(Group.class) || workSpace.getClass().equals(Department.class))
									&& workSpace.amIMember()))){
						%>
							<!-- 삭제 , 수정버튼 -->
							<div class="ctgr_action">
								<span class="btn_text_category js_text_image_folder_btn" folderId="<%=category.getId() %>" title="<fmt:message key='mail.button.text_folder'/>"></span>
								<%if(category.getLength()==0){ %>
									<span class="btn_remove_category js_remove_image_folder_btn" folderId="<%=category.getId() %>" folderName="<%=category.getName() %>" title="<fmt:message key='mail.button.remove_folder'/>"></span>
								<%} %>
							</div>
							<!-- 삭제 , 수정버튼//-->
						<%
						}
						%>
						<a href="image_instance_list.sw" class="js_image_instance_item" categoryId="<%=category.getId()%>">
							<div class="thum_picture"><img style="max-width:75px;max-height:75px;" src="<%=category.getFirstImage().getImgSource()%>"></div>
						</a>						
					</div>
					<div class="title_folder"><%=category.getName() %></div>
					<div class="t_gray"><fmt:message key="space.title.image_count"><fmt:param><%=category.getLength() %></fmt:param></fmt:message></div>
				</li>
				<!--폴더 목록1 //-->
		<%	
			}
		}else{
		%>
		
	<%
		}
	}else{
		ImageInstanceInfo[] imageInstances = smartWorks.getImageInstancesByDate(displayType, wid, parentId, lastDate, 64);
		if(!SmartUtil.isBlankObject(imageInstances) && imageInstances.length>0 ){
			for(int i=0; i<imageInstances.length; i++){
				ImageInstanceInfo image = imageInstances[i];
				if(i == 64){
					String lastDateStr = (i>0) ? (new LocalDate(imageInstances[i-1].getLastModifiedDate().getTime())).toLocalDateString2() : ""; 
	%>
					<div class="t_nowork cb">
						<a href="" class="js_more_image_instance_list" lastDate="<%=lastDateStr%>"><fmt:message key="common.message.more_work_task"></fmt:message></a>
						<span class="js_progress_span"></span>
					</div>
				<%				
					break;
				}
				%>
				<!--폴더 목록1 -->
				<li class="mt10">
					<%-- <input type="checkbox" class="tl js_check_image_instance" value="<%=image.getId()%>"> --%>
					<input type="checkbox" class="tl js_check_image_instance" value="<%=image.getFileId()%>">
					<div class="picture_detail_area">
						
						<!-- 삭제버튼 -->
						<%
						image.setEditableForMe(true);
						if(image.isEditableForMe()){
						%>
							<div class="ctgr_action">
								<span class="btn_remove_category js_remove_image_instance_btn" instanceId="<%=image.getId() %>" workId="<%=workId%>" title="<fmt:message key='common.button.delete'/>"></span>
							</div>
						<%
						}
						%>
						<!-- 삭제버튼//-->
						
						<div class="detail_picture">
							<a class="js_show_picture_detail" instanceId="<%=image.getId()%>" href=""><img src="<%=image.getImgSource()%>"></a>
						</div>
					</div>
 					<div><%=image.getFileName()%>
 						<%if(((WorkInstanceInfo)image).getSubInstanceCount()>0){ %><font class="t_sub_count">[<b><%=((WorkInstanceInfo)image).getSubInstanceCount() %></b>]</font><%} %>
						<%if(image.isNew()){ %><span class="icon_new"></span><%} %>					
 					</div>
					<div class="t_date"><%=image.getOwner().getLongName()%> <%=image.getLastModifiedDate().toLocalString()%></div>
 				</li>
				<!--폴더 목록1 //--> 	 
	<%
			}
		}
	}
	%>
</ul>
