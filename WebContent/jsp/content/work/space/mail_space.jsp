<%@page import="net.smartworks.model.mail.MailFolder"%>
<%@page import="net.smartworks.model.instance.MailInstance"%>
<%@page import="net.smartworks.model.work.MailWork"%>
<%@page import="net.smartworks.model.instance.info.TaskInstanceInfo"%>
<%@page import="net.smartworks.model.instance.TaskInstance"%>
<%@page import="net.smartworks.model.work.InformationWork"%>
<%@page import="net.smartworks.model.community.WorkSpace"%>
<%@page import="net.smartworks.model.instance.InformationWorkInstance"%>
<%@page import="net.smartworks.model.work.Work"%>
<%@page import="net.smartworks.model.work.SmartWork"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	String folderId = request.getParameter("folderId");
	String msgId = request.getParameter("msgId");
	User cUser = SmartUtil.getCurrentUser();

	MailInstance instance = smartWorks.getMailInstanceById(folderId, msgId);
	MailFolder mailFolder = smartWorks.getMailFolderById(folderId);
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />
<!-- 컨텐츠 레이아웃-->
<div class="section_portlet">
	<div class="portlet_t"><div class="portlet_tl"></div></div>
	<div class="portlet_l" style="display: block;">
		<ul class="portlet_r" style="display: block;">
	
			<!-- 타이틀 -->
			<div class="body_titl">
				<div class="body_titl_area ti_mail title">
					<div class="title myspace_h"><%=mailFolder.getName() %>
						<span class="t_mail"><span class="t_s11"><fmt:message key="mail.title.count.unread"/></span><span class="new_mail"><%=mailFolder.getUnreadItemCount() %></span><span class="bar"> / </span><%=mailFolder.getTotalItemCount() %></span><span class=" t_s11"><fmt:message key="mail.title.count"/></span>
					</div>
				</div>

				<!-- 메일 검색 -->
				<div class="mail_srch">
					<div class="srch_wh srch_wsize_mail">
						<input id="" class="nav_input" type="text" href="" placeholder="<fmt:message key="search.search_mail"/>" title="<fmt:message key="search.search_mail"/>">
						<button onclick="" title="<fmt:message key="search.search"/>"></button>
					</div>
				</div>
				<!-- 메일 검색//-->
			    			    
			    <div class="solid_line cb"></div>
			</div>
			<!-- 타이틀 //-->

			<!-- 컨텐츠 -->
			<div class="contents_space">
				<div class="buttonSet">
					<button><fmt:message key="common.button.mail.select_all"/><span class="icon_in_down"><a href=""></a></span></button>
					<button><span class="icon_mail_delet"></span><fmt:message key="common.button.delete"/></button>
					<button><fmt:message key="common.button.mail.register_spam"/></button>
					<button><fmt:message key="common.button.mail.reply"/></button>
					<button><fmt:message key="common.button.mail.reply_all"/></button>
					<button><fmt:message key="common.button.mail.forward"/></button>
					<button><fmt:message key="common.button.move"/><span class="icon_in_down"><a href=""> </a></span></button>
					<button class="fr t_bold"><span class="icon_mail_write"></span><fmt:message key="common.button.mail.new"/></button>
				</div>
				<div class="table_line"> </div>

				<!-- 메일 리스트-->
				<div class="list_contents mail_list_section">
					<div class="view_title">
						<h1><%=instance.getSubject() %></h1>
					    <span class="fr t_date mt10"><%=instance.getSendDate().toLocalDateLongString() %></span>
					    <dl>
					    	<dt><fmt:message key="common.title.sender"/> :</dt>
					        <dd>&lt;<%=instance.getSender().getEmailAddressShown() %>&gt;</dd>
					        <dt><fmt:message key="common.title.receivers"/> :</dt>
					        <dd>&lt;<%=instance.getReceiversShown() %>&gt;</dd>
							<%
							if(!SmartUtil.isBlankObject(instance.getCcReceivers())){
							%>
						        <dt><fmt:message key="common.title.cc_receivers"/> :</dt>
						        <dd>&lt;<%=instance.getCcReceiversShown() %>&gt;</dd>
							<%
							}
							if(!SmartUtil.isBlankObject(instance.getBccReceivers())){
							%>
						        <dt><fmt:message key="common.title.bcc_receivers"/> :</dt>
						        <dd>&lt;<%=instance.getBccReceiversShown() %>&gt;</dd>
							<%
							}
							%>
					    </dl>
					</div>
					<!-- 업무 내용 -->
					<div class="read_frame list_contents js_form_content">
						<div id="msgText">
							<iframe id="msgTextIframe" align="center" frameborder="0" height="100%" width="100%"
								style="font-size: 11px; font: arial, sans-serif;" scrolling="no"
								src="webmail/dumpPart.service?partid=<%=instance.getPartId()%>" border="0" />
						</div>
					</div>
					<!-- 업무 내용 //-->
				</div>
		        <!-- 메일 리스트//-->
			</div>
		</ul>
	</div>
	<div class="portlet_b" style="display: block;"></div>
</div>
<!-- 컨텐츠 레이아웃//-->
