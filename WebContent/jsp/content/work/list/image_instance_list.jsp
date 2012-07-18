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
	int displayType = Integer.parseInt(request.getParameter("displayType"));
	String parentId = request.getParameter("parentId");
	String strLastDate = request.getParameter("lastDate");
	LocalDate lastDate = new LocalDate();
	if(!SmartUtil.isBlankObject(strLastDate))		
		lastDate = LocalDate.convertLocalStringToLocalDate(request.getParameter("lastDate"));
%>
<script type="text/javascript">

function imgResize(img){ 
	  img1= new Image(); 
	  img1.src=(img); 
	  imgControll(img); 
	}
function imgControll(img){ 
	  if((img1.width!=0)&&(img1.height!=0))
	    viewImage(img); 		  
	  else{ 
	    controller="imgControll('"+img+"')"; 
	    intervalID=setTimeout(controller,20); 
	  } 
	}
function viewImage(img){ 
	 W=img1.width+20; 
	 H=img1.height;
	 L=(screen.width-W)/2;
	 O="width="+W+",height="+H+",left="+L+",top=200"; 
	 imgWin=window.open("","",O);
	 imgWin.document.write("<body topmargin=0 leftmargin=0>");
	 imgWin.document.write("<img src="+img+" onclick='self.close()' style='cursor:hand;'>");
	 imgWin.document.close();
	} 

</script>

<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<ul class="js_image_instance_list_page" parentId="<%=parentId %>" displayType="<%=displayType %>" spaceId="<%=wid%>">
	<%
	if(SmartUtil.isBlankObject(parentId)){
		ImageCategoryInfo[] imageCategories = smartWorks.getImageCategoriesByType(displayType, wid);
		if(!SmartUtil.isBlankObject(imageCategories) && imageCategories.length>0 ){
			for(int i=0; i<imageCategories.length; i++){
				ImageCategoryInfo category = imageCategories[i];
	%>
				<!--폴더 목록1 -->
				<li>
					
					<div class="picture_folder">
						<!-- 삭제 , 수정버튼 -->
						<div class="ctgr_action">
							<span class="btn_text_category" categorydesc="null" title="폴더 이름수정"></span>
							<span class="btn_remove_category" title="폴더삭제"></span>
						</div>
						<!-- 삭제 , 수정버튼//-->
						<a href="image_instance_list.sw" class="js_image_instance_item" categoryId="<%=category.getId()%>">
							<div class="thum_picture"><img style="width:70px;height:70px;" src="<%=category.getFirstImage().getImgSource()%>"></div>
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
	%>
				<!--폴더 목록1 -->
				<li>
					<input type="checkbox" class="tl">
					<div class="picture_detail_area">
						
						<!-- 삭제버튼 -->
						<div class="ctgr_action">
							<span class="btn_remove_category" title="삭제"></span>
						</div>
						<!-- 삭제버튼//-->
												
						<a href="javascript:imgResize('<%=image.getOriginImgSource()%>')">
						<div class="detail_picture"><img style="width:155px;height:125px;" src="<%=image.getImgSource()%>"></div>
						</a>												
 					</div>
 					<div><%=image.getFileName()%></div>
					<div class="t_date"><%=image.getOwner().getLongName()%> <%=image.getLastModifiedDate().toLocalString()%></div>
 				</li>
				<!--폴더 목록1 //--> 	 
	<%
			}
		}
	}
	%>
</ul>
<!-- 목록 버튼:사진공간  상세목록 페이지에서만 나옴 -->
<div class="tc cb">
	<div class="btn_gray">
		<a class="js_content" href="file_list.sw?cid=fl.li.pkg_309666dd2bb5493c9d7e618b3a0aad96&wid=">
			<span class="txt_btn_start"></span>
			<span class="txt_btn_center">목록보기</span>
			<span class="txt_btn_end"></span>
		</a>
	</div>
</div>
<!-- 목록 버튼: 사진공간  상세목록 페이지에서만 나옴//-->