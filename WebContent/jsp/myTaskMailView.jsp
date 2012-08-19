<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.server.engine.common.util.DateUtil"%>
<%@page import="net.smartworks.server.engine.process.process.model.PrcProcessInst"%>
<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.server.service.util.ModelConverter"%>
<%@page import="net.smartworks.server.engine.process.task.model.TskTask"%>
<%@page import="net.smartworks.server.engine.factory.SwManagerFactory"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%!
public static String getTaskTypeName(String lang, String taskType, String approvalLine, String receiptUser, String referenceUser, String userLanguage) {
	String taskTypeName = "";
	if(taskType == null) {
		if (lang.equalsIgnoreCase("KOR")) {
			taskTypeName = "할당 업무";
		} else {
			taskTypeName = "AssignedJob";
		}
	} else {
		//결재라인이 있을 경우 전자 결재
		if(!CommonUtil.isEqual(approvalLine, "")) {
			if (lang.equalsIgnoreCase("KOR")) {
				taskTypeName = "전자 결재";
			} else {
				taskTypeName = "ElectronicApproval";
			}
		} else {
			//수신자가 존재하면 업무전달로 할당된 업무
			if(!CommonUtil.isEqual(receiptUser, "")) {
				if (lang.equalsIgnoreCase("KOR")) {
					taskTypeName = "업무 전달";
				} else {
					taskTypeName = "BusinessForward";
				}
			//수신자 없이 참조로만 보낸 경우
			} else {
				if(!CommonUtil.isEqual(referenceUser, "")) {
					if(taskType.equals("COMMON")) {
						if (lang.equalsIgnoreCase("KOR")) {
							taskTypeName = "할당 업무";
						} else {
							taskTypeName = "AssignedJob";
						}
					} else if( taskType.equals("SINGLE")) {
						if (lang.equalsIgnoreCase("KOR")) {
							taskTypeName = "참조 업무";
						} else {
							taskTypeName = "BusinessReference";
						}
					}
				} else {
					if(taskType.equals("COMMON")) {
						if (lang.equalsIgnoreCase("KOR")) {
							taskTypeName = "할당 업무";
						} else {
							taskTypeName = "AssignedJob";
						}
					} else if( taskType.equals("SINGLE")) {
						if (lang.equalsIgnoreCase("KOR")) {
							taskTypeName = "전달 업무";
						} else {
							taskTypeName = "BusinessReception";
						}
					}	
				}					
			}				
		}
		
		if( taskType.equals("REFERENCE")) {
			if (lang.equalsIgnoreCase("KOR")) {
				taskTypeName = "전달 업무";
			} else {
				taskTypeName = "BusinessReference";
			}
		} else if( taskType.equals("GANTT")) {
			if (lang.equalsIgnoreCase("KOR")) {
				taskTypeName = "간트 업무";
			} else {
				taskTypeName = "GanttChart";
			}
		}
	}
	return taskTypeName;
}


%>
<%
	request.setCharacterEncoding("UTF-8");
	String path = request.getContextPath();
	StringBuffer basePathBuf = new StringBuffer(request.getScheme()).append("://").append(request.getServerName());
	basePathBuf.append(":").append(request.getServerPort()).append(path);//.append("/");
	String basePath = basePathBuf.toString();
	//String basePath = "http://sw.semiteq.co.kr";
	
	String taskId = CommonUtil.toNotNull(request.getParameter("id"));
	String userId = CommonUtil.toNotNull(request.getParameter("assigner"));
	String requesterPos = "";
	
	TskTask tskObj = SwManagerFactory.getInstance().getTskManager().getTask(userId, taskId, null);

	UserInfo assignerObj = ModelConverter.getUserInfoByUserId(tskObj.getAssigner());
	UserInfo assigneeObj = ModelConverter.getUserInfoByUserId(tskObj.getAssignee());
	
	String assigner = assignerObj.getName();
	String assignee = assigneeObj.getName();
	String procInstId = tskObj.getProcessInstId();
	PrcProcessInst prcInst = SwManagerFactory.getInstance().getPrcManager().getProcessInst(null, procInstId, null);
	String procInstStarter = "";
	String procInstCreationDate = "";
	String processName = "";
	String name = CommonUtil.toNotNull(tskObj.getName());
	if(prcInst != null) {
		procInstStarter = ModelConverter.getUserInfoByUserId(prcInst.getCreationUser()) != null ? ModelConverter.getUserInfoByUserId(prcInst.getCreationUser()).getName() : "";
		procInstCreationDate = DateUtil.toXsdDateString(new LocalDate(prcInst.getCreationDate().getTime()));
		processName = CommonUtil.toNotNull(prcInst.getName()); 
	}
	String dueDate = CommonUtil.toNotNull(DateUtil.toXsdDateString(tskObj.getDueDate()));
	String priority = CommonUtil.toNotNull(tskObj.getPriority());
	String title = CommonUtil.toNotNull(tskObj.getTitle()); 
	String position = "";
	String lang = "KOR";	
	
	if(priority.length() != 0) {
		if(priority.equals("H")) {
			priority = "높음";//"높음";
		} else if(priority.equals("M")) {
			priority = "중간";//"중간";
		} else if(priority.equals("L")) {
			priority = "낮음";//"낮음";receiptUser
		}
	}
	String taskType = getTaskTypeName(lang, CommonUtil.toNotNull(tskObj.getType()), CommonUtil.toNotNull(tskObj.getExtendedPropertyValue("approvalLine")), CommonUtil.toNotNull(tskObj.getExtendedPropertyValue("receiptUser")), CommonUtil.toNotNull(tskObj.getExtendedPropertyValue("referenceUser")), lang);
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<style type="text/css">
			BODY {
				background-color: #FFFFFF; COLOR: #000000;   FONT-SIZE: 9pt; LINE-HEIGHT: 150%; TEXT-DECORATION: none; scrollbar-face-color:  #FFFFFF; scrollbar-highlight-color: #FFFFFF; scrollbar-3dlight-color: #EBEBEB; scrollbar-shadow-color: #EBEBEB; scrollbar-darkshadow-color: #FFFFFF; scrollbar-track-color: #FFFFFF; scrollbar-arrow-color: #EBEBEB
			}
			TABLE {
				  FONT-SIZE: 9pt; LINE-HEIGHT: 150%; TEXT-DECORATION: none
			}
			TR {
				  FONT-SIZE: 9pt; LINE-HEIGHT: 150%; TEXT-DECORATION: none
			}
			TD {
				  FONT-SIZE: 9pt; LINE-HEIGHT: 150%; TEXT-DECORATION: none
			}
			
			A:link {
				COLOR: #616161;   FONT-SIZE: 9pt; LINE-HEIGHT: 150%; TEXT-DECORATION: none
			}
			A:visited {
				COLOR: #616161;   FONT-SIZE: 9pt; LINE-HEIGHT: 150%; TEXT-DECORATION: none
			}
			A:active {
				COLOR: #616161;   FONT-SIZE: 9pt; LINE-HEIGHT: 150%; TEXT-DECORATION: none
			}
			A:hover {
				COLOR: #B87B29;   FONT-SIZE: 9pt; LINE-HEIGHT: 150%; TEXT-DECORATION: none
			}
		</style>
	</head>
	<body>
		<table width="737" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="3%"><img src="<%=basePath%>/images/bullet_03.gif"></td>
				<td align="left" valign="middle">[스마트웍스닷넷] 업무 도착 알림</td>
			</tr>
			<tr	height="10"><td></td></tr>
		</table>
		<table width="600" cellpadding="0" cellspacing="1" border="0" bgcolor="#DADADA">
			<tr height="3" bgcolor="#DADADA">
				<td colspan="4"></td>
			</tr>	
			<tr>
				<td width="20%" bgcolor="#ECECEC" style="padding-left: 5px;padding-top: 7px;" valign="middle">제목</td>
				<td width="30%" colspan="3" bgcolor="FFFFFF" style="padding-left: 5px;padding-top: 7px;" valign="middle"><%= title %></td>
			</tr>
			<tr height="1" bgcolor="#DADADA">
				<td colspan="4"></td>
			</tr>	
			<tr>
				<td width="20%" bgcolor="#ECECEC" style="padding-left: 5px;padding-top: 7px;">업무명 / 태스크명</td>
				<td width="80%" colspan="3" bgcolor="FFFFFF" style="padding-left: 5px;padding-top: 7px;"><%= processName %><%= processName.equals("") ? "" : " / " %><%= name %></td>
			</tr>
			<tr height="1" bgcolor="#DADADA">
				<td colspan="4"></td>
			</tr>	
			<tr>
				<td width="20%" bgcolor="#ECECEC" style="padding-left: 5px;padding-top: 7px;">종류</td>
				<td width="30%" bgcolor="FFFFFF" style="padding-left: 5px;padding-top: 7px;"><%= taskType %></td>
				<td width="20%" bgcolor="#ECECEC" style="padding-left: 5px;padding-top: 7px;">중요도</td>
				<td width="30%" bgcolor="FFFFFF" style="padding-left: 5px;padding-top: 7px;"><%= priority %></td>
			</tr>
			<tr height="1" bgcolor="#DADADA">
				<td colspan="4"></td>
			</tr>	
			<tr>
				<td width="20%" bgcolor="#ECECEC" style="padding-left: 5px;padding-top: 7px;">담당자</td>
				<td width="30%" bgcolor="FFFFFF" style="padding-left: 5px;padding-top: 7px;"><%= assignee %></td>
				<td width="20%" bgcolor="#ECECEC" style="padding-left: 5px;padding-top: 7px;">처리기한</td>
				<td width="30%" bgcolor="FFFFFF" style="padding-left: 5px;padding-top: 7px;"><%= dueDate %></td>
			</tr>
			<tr height="1" bgcolor="#DADADA">
				<td colspan="4"></td>
			</tr>	
			<tr>
				<td width="20%" bgcolor="#ECECEC" style="padding-left: 5px;padding-top: 7px;">시작자</td>
				<td width="30%" bgcolor="FFFFFF" style="padding-left: 5px;padding-top: 7px;"><%= procInstStarter %></td>
				<td width="20%" bgcolor="#ECECEC" style="padding-left: 5px;padding-top: 7px;">시작일</td>
				<td width="30%" bgcolor="FFFFFF" style="padding-left: 5px;padding-top: 7px;"><%= procInstCreationDate %></td>
			</tr>
		</table>
		<table width="737" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td	height="10"></td>
			</tr>
			<tr>
				
<%
	if(lang.equalsIgnoreCase("ENG")) {
%>
<td style="padding-left: 5px;">A task has arrived at Publication Wizard. Click <a href="<%=basePath%>" target="_new"><font color="red">here</font></a> to confirm your task.</td>
<%
	} else if(lang.equalsIgnoreCase("KOR")) {
%>
<td style="padding-left: 5px;">
<%if(lang.equals("KOR")){ %>
	위의 업무가 <%=position%>&nbsp;<%=assignee%>님의 처리할 업무함에 도착하였습니다.<br>
	업무처리를 원하시면  <a href="<%=basePath%>" target="_new"><font color="blue" style="text-decoration: underline;">스마트웍스닷넷</font></a> 으로 접속하여 주시기 바랍니다.
<%}else{ %>
	The above task has arrived to <%=position%>&nbsp;<%=assignee%> work box as works to do<br/>
	To process the work, please access <a href="<%=basePath%>" target="_new"><font color="blue" style="text-decoration: underline;">Smartworks.net</font></a>.
<%} %>
</td>
<%
	}
%>				
			</tr>
		</table>
	</body>
</html>
