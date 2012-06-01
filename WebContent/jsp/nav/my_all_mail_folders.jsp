
<!-- Name 			: my_all_mail_folders.jsp										 -->
<!-- Description	: 좌측의 Navigation Bar 의  메일에서 메일폴더들을 가져다 보여주는 화면 	 -->
<!-- Author			: Maninsoft, Inc.												 -->
<!-- Created Date	: 2011.9.														 -->

<%@page import="net.smartworks.model.mail.MailFolder"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.model.work.info.WorkInfo"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ page import="net.smartworks.model.work.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 현재사용자 정보도 가져온다 
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User currentUser = SmartUtil.getCurrentUser();

	// 메일서버에서 현재사용자의 폴더정보들을 가져온다...
	MailFolder[] folders = smartWorks.getMailFoldersById("");
%>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=currentUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<ul>
	<%
	if(folders != null){
		for (MailFolder folder : folders) {
			String iconClass = "";
			switch(folder.getType()){
			case MailFolder.TYPE_SYSTEM_INBOX :
				iconClass = "icon_mail_inbox";
				break;
			case MailFolder.TYPE_SYSTEM_SENT :
				iconClass = "icon_mail_sent";
				break;
			case MailFolder.TYPE_SYSTEM_JUNK :
				iconClass = "icon_mail_junk";
				break;
			case MailFolder.TYPE_SYSTEM_TRASH :
				iconClass = "icon_mail_trash";
				break;
			case MailFolder.TYPE_SYSTEM_DRAFTS :
				iconClass = "icon_mail_drafts";
				break;
			}
	%>
			<li>
				<!--  모든폴더에 공통으로 필요한 html -->
				<a href="mail_list.sw?cid=<%=folder.getId()%>" class="js_content" folderId="<%=folder.getId()%>"> 
					<span class="<%=iconClass%>"></span><%=folder.getName() %><%if (folder.getUnreadItemCount() > 0) {%><span>(<%=folder.getUnreadItemCount()%>)</span><%}%>						
				</a>
			</li>
	<%
		}
	}
	%>
</ul>
