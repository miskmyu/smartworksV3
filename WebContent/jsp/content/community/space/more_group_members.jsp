<%@page import="net.smartworks.model.community.Group"%>
<%@page import="net.smartworks.model.community.info.GroupMemberList"%>
<%@page import="net.smartworks.model.instance.WorkInstance"%>
<%@page import="net.smartworks.util.SmartTest"%>
<%@page import="net.smartworks.model.instance.info.InstanceInfo"%>
<%@page import="net.smartworks.model.community.info.GroupInfo"%>
<%@page import="net.smartworks.model.community.info.DepartmentInfo"%>
<%@page import="net.smartworks.model.work.info.SmartWorkInfo"%>
<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.model.instance.info.MemoInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.ImageInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.FileInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.EventInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.BoardInstanceInfo"%>
<%@page import="net.smartworks.model.community.info.WorkSpaceInfo"%>
<%@page import="net.smartworks.model.instance.Instance"%>
<%@page import="net.smartworks.model.work.SocialWork"%>
<%@page import="net.smartworks.model.work.Work"%>
<%@page import="net.smartworks.model.work.SmartWork"%>
<%@page import="net.smartworks.model.instance.info.WorkInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.TaskInstanceInfo"%>
<%@page import="net.smartworks.model.instance.TaskInstance"%>
<%@page import="net.smartworks.model.calendar.CompanyEvent"%>
<%@page import="net.smartworks.util.SmartMessage"%>
<%@page import="net.smartworks.model.calendar.WorkHourPolicy"%>
<%@page import="net.smartworks.model.community.WorkSpace"%>
<%@page import="java.util.Calendar"%>
<%@page import="net.smartworks.model.calendar.CompanyCalendar"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();
	
	String groupId = request.getParameter("groupId");
	String lastId = request.getParameter("lastId");
	int maxSize = Integer.parseInt(request.getParameter("maxSize"));

	Group group = smartWorks.getGroupById(groupId);
	UserInfo[] groupMembers = smartWorks.getGroupMembersById(groupId, lastId, maxSize);	
%>
<!--  다국어 지원을 위해, 로케일 및 다국어 resource bundle 을 설정 한다. -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

	<%
	if(!SmartUtil.isBlankObject(groupMembers) && groupMembers.length > 0){
		for(int i=0; i<groupMembers.length; i++){
			UserInfo member = groupMembers[i];
			if(i==GroupMemberList.MAX_MEMBER_LIST){
	%>
				<li class="t_nowork">
					<a href="" class="js_group_more_members" lastId="<%=groupMembers[i-1].getId()%>"><fmt:message key="common.message.more_work_task"></fmt:message></a>
					<span class="js_progress_span"></span>
				</li>
			<%
				break;
			}
			%>
            <li class="instance_list">
             	<div class="det_title">
                     <div class="profile_photo">
                     	<%if(member.getRole() == User.USER_ROLE_LEADER){ %><span class="leader m"></span><%} %>
                       	<img class="profile_size_m" src="<%=member.getMidPicture()%>">
              		</div>
                     <div>
	                    <span>
	                    <%=member.getLongName() %><%if(member.getRole()==User.USER_ROLE_LEADER){ %>(<fmt:message key="group.title.leader"/>)<%} %></br>
	                    <%=member.getDepartment().getFullpathName() %>
	                    </span>
	                    <span class="bar">|</span>
                      </div>
                     <%-- <div><span><%=member.getDepartment().getFullpathName() %></span></div> --%>
                      <div>
                        <span>
                        	<%=member.getId() %>
                        </span>
                        <span class="bar">|</span>
                       </div>
                       
                       <div style="width:100px"><span>가입일: <br>2012.03.25</span><span class="bar">|</span></div>
                       <div style="width:100px"><span>최근 방문일: <br>2012.05.12 18:50</div>
                     <div class="buttonSet">
                       	<span class="bar">|</span>
                     	<%
                     	if(member.getId().equals(cUser.getId())){
                     	%>
                         	<span><a href="" class="js_leave_group_request" isGroupLeader="<%=group.amIGroupLeader(cUser)%>"><fmt:message key="group.button.leave"/></a></span>
                     	<%
                     	}else if(group.getLeader().getId().equals(cUser.getId()) && !member.getId().equals(cUser.getId())){
                     	%>
               				<span><a href="" class="js_pushout_group_member" memberId="<%=member.getId()%>"><fmt:message key="group.button.pushout"/></a></span>
              			<%
              			}
              			%>
                     </div>
                 </div>
             </li>
	<%
		}
	}
	%>	