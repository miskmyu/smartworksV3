<!-- Name 			: pop_show_picture.jsp								 -->
<!-- Description	: 이미지 상세보기 팝업화면 							 	-->
<!-- Author			: Maninsoft, Inc.									 -->
<!-- Created Date	: 2012.08.01.										-->

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
	String instId = request.getParameter("instId");
	
	ImageInstance image = smartWorks.getImageInstanceById(instId);
	session.setAttribute("workInstance", image);

%>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!--  전체 레이아웃 -->
<div class="pop_corner_all pop_section_400 js_show_picture_page" instanceId="<%=instId %>" workType="<%=SmartWork.TYPE_INFORMATION%>">
	
	<!-- 타이틀 -->
	<div class="body_titl_pic mt5">
			<div class="noti_pic mr7">
				<img class="profile_size_m" src="<%=image.getOwner().getMidPicture()%>" />
			</div>
	
			<div class="noti_in_bodytitle case_2line ">
				<div>
					<span class="t_name"><%=image.getOwner().getLongName()%></span>
					<!-- 인스턴스 마지막수정일자 -->
					<span class="t_date"><%=image.getLastModifiedDate().toLocalString()%></span>
					<!-- 인스턴스 마지막수정일자 //-->
				</div>
				<!-- Title -->
				<div>
					<span class="title_picico">
					<%=image.getOwner().getLongName()%>
					>
					<%=image.getFileName()%>
					</span>
				</div>
				<!-- Title //-->
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
		<!-- 사진   -->
		<div class="original_picture">
			<!-- start : added by sjlee 2012.08.05 -->
			<div class="arrow_space">
			<%if(!SmartUtil.isBlankObject(image.getPrevInstId())){%>
				<div class="btn_arr_prev js_prev_picture_btn" instId="<%=image.getPrevInstId()%>"></div>
			<%}if(!SmartUtil.isBlankObject(image.getNextInstId())){%>	
				<div class="btn_arr_next js_next_picture_btn" instId="<%=image.getNextInstId()%>"></div>
			<%}%>
			</div>
			<!-- end : added by sjlee 2012.08.05 -->
			<img src="<%=image.getOriginImgSource()%>" />
		</div>
		<!-- 사진 //-->

		<!-- 컨텐츠 -->
		<div class="mb5"><%=CommonUtil.toNotNull(image.getContent()) %></div>
		<!-- 컨텐츠 //-->
		
		<div class="js_comments_box mb3" <%if(image.getSubInstanceCount()==0){%>style="display:none"<%} %>>
			<!-- 댓글 -->
		   <div class="reply_point pos_reply_point"></div>
		   <div class="reply_section" >  
		        <div class="list_reply">
		            <ul class="js_comment_list">
		            	<li class="comment_list js_comment_instance" style="display:none">
							<div class="noti_pic">
								<a class="js_pop_user_info" href="<%=cUser.getSpaceController() %>?cid=<%=cUser.getSpaceContextId()%>" userId="<%=cUser.getId()%>" profile="<%=cUser.getOrgPicture()%>" userDetail="<%=SmartUtil.getUserDetailInfo(cUser.getUserInfo())%>">
									<img src="<%=cUser.getMinPicture()%>" align="bottom" class="profile_size_c"/>
								</a>
							</div>
							<div class="noti_in">
								<span class="t_name"><%=cUser.getLongName()%></span>
								<span	class="t_date pl10"><%=(new LocalDate()).toLocalString()%></span>
								<div class="js_comment_content"></div>
							</div>
		            	</li>
		            	<%
		            	if(image.getSubInstanceCount()>WorkInstance.DEFAULT_SUB_INSTANCE_FETCH_COUNT){
		            		session.setAttribute("subComments", null);
		            	%>
			            	<li>
			            		<img class="repl_tinfo">
		            			<a href="sub_instances_in_instance.sw?instanceId=<%=image.getId()%>&fetchCount=<%=WorkInstance.FETCH_ALL_SUB_INSTANCE %>" class="js_show_all_comments">
		            				<span><strong><fmt:message key="common.title.show_all_comments"><fmt:param><%=image.getSubInstanceCount() %></fmt:param><</fmt:message></strong></span>
		            			</a>
			            	</li>
							<jsp:include page="/jsp/content/work/list/sub_instances_in_instance.jsp" >
								<jsp:param value="<%=image.getId() %>" name="instanceId"/>
								<jsp:param value="<%=WorkInstance.DEFAULT_SUB_INSTANCE_FETCH_COUNT %>" name="fetchCount"/>
							</jsp:include>
						<%
						} else {
							session.setAttribute("subComments", image.getSubInstances());
						%>
							<jsp:include page="/jsp/content/work/list/sub_instances_in_instance.jsp" >
								<jsp:param value="<%=image.getId() %>" name="instanceId"/>
								<jsp:param value="<%=WorkInstance.DEFAULT_SUB_INSTANCE_FETCH_COUNT %>" name="fetchCount"/>
							</jsp:include>
						<%
						}
						%>
					</ul>
		        </div>
		        
		        <div class="reply_input js_return_on_comment" style="display:none">
					<div class="noti_pic">
						<img src="<%=cUser.getMinPicture()%>" class="profile_size_c"/>
					</div>
					<div class="noti_in">
						<textarea class="up_textarea" name="txtaCommentContent" placeholder="<fmt:message key='work.message.leave_comment'/>"></textarea>
					</div>
		        </div>
		    
		    </div>
		    <!-- 댓글 //-->
		</div>
	    <div class="btns_action m0 js_action_btns">
	    	<a class="js_add_comment" href=""><span class="t_action"><fmt:message key="common.button.add_comment"/></span></a>
	    	<a class="js_add_like" href=""><span class="t_action"><fmt:message key="common.button.add_like"/></span></a>
	    </div>
	</div>
	<!-- 팝업 컨텐츠 //-->

</div>
<!-- 전체 레이아웃//-->

<!-- 전체 레이아웃//-->
