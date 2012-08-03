<!-- Name 			: pop_show_picture.jsp								 -->
<!-- Description	: 이미지 상세보기 팝업화면 							 	-->
<!-- Author			: Maninsoft, Inc.									 -->
<!-- Created Date	: 2012.08.01.										-->

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
<div class="pop_corner_all pop_section_400 js_show_picture_page">

	<div class="body_titl space">
		<div class="noti_pic">
			<img class="profile_size_m" src="<%=image.getOwner().getMidPicture()%>" />
		</div>
		<div class="noti_in_m">
			<a href=""> <span class="t_name"><%=image.getOwner().getLongName()%></span>
			</a>
			<!-- 인스턴스 마지막수정일자 -->
			<span class="t_date pl10"><%=image.getLastModifiedDate().toLocalString()%></span>
			<!-- 인스턴스 마지막수정일자 //-->
		</div>
		<div class="info fl">
			<div class="title"><%=image.getOwner().getLongName()%>
				>
				<%=image.getFileName()%></div>
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
	<div class="space_contents">
		<!-- 사진 -->
		<div class="original_picture">
			<div class="arrow_space">
				<div class="btn_arr_prev js_prev_picture_btn" instId="<%=image.getPrevInstId()%>"></div>
				<div class="btn_arr_next js_next_picture_btn" instId="<%=image.getNextInstId()%>"></div>
			</div>

			<img src="<%=image.getOriginImgSource()%>" />
		</div>
		<!-- 사진 //-->

		<!-- 컨텐츠 -->
		<div class="det_title">
			<div class="noti_in_m">
				<div><%=CommonUtil.toNotNull(image.getContent()) %></div>
			</div>
		</div>
		<!-- 컨텐츠 //-->
<%-- 
		<jsp:include page="/jsp/content/work/space/space_instance_list.jsp">
			<jsp:param value="<%=SmartWork.ID_FILE_MANAGEMENT %>" name="workId"/>
			<jsp:param value="<%=instId %>" name="instId"/> 
		</jsp:include>	

 --%>
	</div>
	<!-- 팝업 컨텐츠 //-->

</div>
<!-- 전체 레이아웃//-->

<!-- 전체 레이아웃//-->