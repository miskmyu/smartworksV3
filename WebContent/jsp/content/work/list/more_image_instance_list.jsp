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
	int displayBy = Integer.parseInt(request.getParameter("displayBy"));
	String parentId = request.getParameter("parentId");
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<ul class="image_instance_list_page">
	<%
	if(SmartUtil.isBlankObject(parentId)){
		ImageCategoryInfo[] imageCategories = smartWorks.getImageCategoriesByType(displayBy, wid);
		if(!SmartUtil.isBlankObject(imageCategories) && imageCategories.length>0 ){
			for(int i=0; i<imageCategories.length; i++){
				ImageCategoryInfo category = imageCategories[i];
	%>
				<!--폴더 목록1 -->
				<li>
					<div class="picture_folder">
						<div class="thum_picture"><img href="image_instance_list.sw" class="" style="width:70px;height:70px;" src="<%=category.getFirstImage().getImgSource()%>"></div>
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
	%>
	<%
	}
	%>
</ul>
