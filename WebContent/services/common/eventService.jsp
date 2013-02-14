<%@page import="net.smartworks.server.engine.organization.model.SwoGroup"%>
<%@page import="net.smartworks.server.engine.organization.model.SwoGroupMember"%>
<%@page import="net.smartworks.server.engine.organization.model.SwoGroupCond"%>
<%@page import="org.springframework.util.StringUtils"%>
<%@page import="net.smartworks.server.engine.organization.model.SwoUserExtend"%>
<%@page import="net.smartworks.server.engine.common.util.DateUtil"%>
<%@page import="java.util.Date"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.server.engine.infowork.domain.model.SwdDataField"%>
<%@page import="net.smartworks.server.engine.infowork.domain.model.SwdRecord"%>
<%@page import="net.smartworks.model.community.info.GroupInfo"%>
<%@page import="net.smartworks.server.service.factory.SwServiceFactory"%>
<%@page import="net.smartworks.model.community.info.DepartmentInfo"%>
<%@page import="net.smartworks.server.engine.common.util.SmartUtil"%>
<%@page import="net.smartworks.server.service.util.ModelConverter"%>
<%@page import="net.smartworks.server.engine.infowork.domain.model.SwdRecordCond"%>
<%@page import="net.smartworks.server.engine.infowork.domain.model.SwdDomain"%>
<%@page import="net.smartworks.server.engine.infowork.form.model.SwfForm"%>
<%@page import="net.smartworks.server.engine.infowork.form.model.SwfFormCond"%>
<%@page import="net.smartworks.server.engine.infowork.domain.model.SwdDomainCond"%>
<%@page import="net.smartworks.model.work.SmartWork"%>
<%@page import="net.smartworks.server.engine.resource.model.hb.HbOrgUserModel"%>
<%@page import="net.smartworks.server.engine.resource.model.IOrgUserModel"%>
<%@page import="net.smartworks.server.engine.organization.model.SwoUser"%>
<%@page import="net.smartworks.server.engine.organization.model.SwoUserCond"%>
<%@page import="net.smartworks.server.engine.resource.model.hb.HbOrgDeptModel"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.server.engine.resource.model.IOrgDeptModel"%>
<%@page import="net.smartworks.server.engine.common.manager.IManager"%>
<%@page import="net.smartworks.server.engine.factory.SwManagerFactory"%>
<%@page import="net.smartworks.server.engine.organization.model.SwoDepartment"%>
<%@page import="net.smartworks.server.engine.common.model.Order"%>
<%@page import="net.smartworks.server.engine.organization.model.SwoDepartmentCond"%>
<%@page import="net.smartworks.server.engine.resource.util.lang.ExceptionUtil"%>
<%@page import="net.smartworks.server.engine.resource.util.lang.StringUtil"%>
<%@ page contentType="text/xml; charset=UTF-8" %>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%!
	public GroupInfo[] getMyGroups(String userId) throws Exception {
		try{
			List<GroupInfo> groupInfoList = new ArrayList<GroupInfo>();
			SwoGroupCond swoGroupCond = new SwoGroupCond();
	
			SwoGroupMember swoGroupMember = new SwoGroupMember();       
			swoGroupMember.setUserId(userId);		
			SwoGroupMember[] swoGroupMembers = new SwoGroupMember[1];
			swoGroupMembers[0] = swoGroupMember;
			swoGroupCond.setSwoGroupMembers(swoGroupMembers);
			swoGroupCond.setOrders(new Order[]{new Order("creationDate", false)});
			SwoGroup[] swoGroups = SwManagerFactory.getInstance().getSwoManager().getGroups(userId, swoGroupCond, IManager.LEVEL_ALL);
			if(swoGroups != null) {
				for(SwoGroup swoGroup : swoGroups) {
	//				GroupInfo groupInfo = ModelConverter.getGroupInfoByGroupId(swoGroup.getId());
					GroupInfo groupInfo = ModelConverter.getGroupInfoBySwoGroup(null, swoGroup);
					groupInfoList.add(groupInfo);
				}
				GroupInfo[] groupInfos = new GroupInfo[groupInfoList.size()];
				groupInfoList.toArray(groupInfos);
				return groupInfos;
			}
			return null;
		}catch (Exception e){
			e.printStackTrace();
			return null;			
		}
	}
	private void getDeptTreeByDeptId(List<SwoDepartment> deptList, String deptId) throws Exception {

		try{
			SwoDepartment dept = SwManagerFactory.getInstance().getSwoManager().getDepartment("", deptId, IManager.LEVEL_LITE);
			if (dept == null)
				return;
			if (!deptList.contains(dept))
				deptList.add(dept);
			if (!dept.getParentId().equalsIgnoreCase("root")) {
				getDeptTreeByDeptId(deptList, dept.getParentId());
			}
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			// Exception Handling Required			
		}
	}
	public DepartmentInfo[] getMyDepartments(String userId) throws Exception {
	
		try{
	
			SwoUserExtend userExtend = SwManagerFactory.getInstance().getSwoManager().getUserExtend(userId, userId, true);
			String myDeptId = userExtend.getDepartmentId();
			List<SwoDepartment> deptList = new ArrayList<SwoDepartment>();
			//겸직적용
			String adjunctDeptids = userExtend.getAdjunctDeptIds();
			if (!CommonUtil.isEmpty(adjunctDeptids)) {
				String[] ajtDeptInfo = StringUtils.tokenizeToStringArray(adjunctDeptids, ";");
				for (int i = 0; i < ajtDeptInfo.length; i++) {
					String[] ajtDeptIdInfo = StringUtils.tokenizeToStringArray(ajtDeptInfo[i], "|");
					String deptId = ajtDeptIdInfo[0];
					String position = ajtDeptIdInfo[1];
					getDeptTreeByDeptId(deptList, deptId);
				}
			}
			getDeptTreeByDeptId(deptList, myDeptId);
			DepartmentInfo[] deptInfos = new DepartmentInfo[deptList.size()];
			int index = deptList.size() - 1;
			for (int i = 0; i < deptList.size(); i++) {
				DepartmentInfo deptInfo = new DepartmentInfo();
				SwoDepartment swDept = deptList.get(i);
				deptInfo.setId(swDept.getId());
				deptInfo.setName(swDept.getName());
				deptInfo.setDesc(swDept.getDescription());
				deptInfos[index--] = deptInfo;
			}
			return deptInfos;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}		
	}

	public String[] getWorkSpaceIdIns(String userId) throws Exception {
		try {
	
			List<String> workSpaceIdInList = new ArrayList<String>();
			String[] workSpaceIdIns = null;
	
			// 나의 공간
			workSpaceIdInList.add(userId);
			// 나의 부서
			DepartmentInfo[] myDepartments = getMyDepartments(userId);
			if(!CommonUtil.isEmpty(myDepartments)) {
				for(DepartmentInfo myDepartment : myDepartments) {
					String myDeptId = myDepartment.getId();
					workSpaceIdInList.add(myDeptId);
				}
			}
			// 나의 그룹
			GroupInfo[] myGroups = getMyGroups(userId);
			if(!CommonUtil.isEmpty(myGroups)) {
				for(GroupInfo myGroup : myGroups) {
					String myGroupId = myGroup.getId();
					workSpaceIdInList.add(myGroupId);
				}
			}
			if(workSpaceIdInList.size() > 0) {
				workSpaceIdIns = new String[workSpaceIdInList.size()];
				workSpaceIdInList.toArray(workSpaceIdIns);
			}
	
			return workSpaceIdIns;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
%>
<%
	response.setHeader("Pragma", "no-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Expires", "0");
	
	String userId = request.getParameter("userId");
	String method = StringUtil.toNotNull(request.getParameter("method"));
//	StringBuffer buffer = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
	StringBuffer buffer = new StringBuffer();
	
	try {
		if(method.equals("")) {
			buffer.append("<Result status=\"Failed\"><message>Invalid method! Not found method parameter</message><trace/></Result>");
		
		// 사용자 생성
		} else if(method.equals("getEvent")) {			
			
			String workId = SmartWork.ID_EVENT_MANAGEMENT;
			SwoUser user = SwManagerFactory.getInstance().getSwoManager().getUser(userId, userId, IManager.LEVEL_LITE);

			SwdDomainCond swdDomainCond = new SwdDomainCond();
			swdDomainCond.setCompanyId(user.getCompanyId());

			SwfFormCond swfFormCond = new SwfFormCond();
			swfFormCond.setCompanyId(user.getCompanyId());
			swfFormCond.setPackageId(workId);

			SwfForm[] swfForms = SwManagerFactory.getInstance().getSwfManager().getForms(user.getId(), swfFormCond, IManager.LEVEL_LITE);

			if(swfForms == null) {
				buffer.append("<Result status=\"Failed\"><message>Not Exist Form!</message><trace/></Result>");
			} else {
				String formId = swfForms[0].getId();
				swdDomainCond.setFormId(formId);

				SwdDomain swdDomain = SwManagerFactory.getInstance().getSwdManager().getDomain(user.getId(), swdDomainCond, IManager.LEVEL_LITE);

				SwdRecordCond swdRecordCond = new SwdRecordCond();
				swdRecordCond.setCompanyId(user.getCompanyId());
				swdRecordCond.setFormId(swdDomain.getFormId());
				swdRecordCond.setDomainId(swdDomain.getObjId());

				String formFieldId = "1";
				String tableColName = SwManagerFactory.getInstance().getSwdManager().getTableColName(swdDomain.getObjId(), formFieldId);
				swdRecordCond.setOrders(new Order[]{new Order(tableColName, false)});
				
				String[] workSpaceIdIns = getWorkSpaceIdIns(userId);
				swdRecordCond.setWorkSpaceIdIns(workSpaceIdIns);
				//swdRecordCond.setLikeAccessValues(workSpaceIdIns);
				
				SwdRecord[] swdRecords = SwManagerFactory.getInstance().getSwdManager().getRecords(user.getId(), swdRecordCond, IManager.LEVEL_LITE);
				buffer.append("<Result status=\"OK\">");
				if(swdRecords != null) {
					for(int i=0; i < swdRecords.length; i++) {
						SwdRecord swdRecord = swdRecords[i];
							
						String name = swdRecord.getDataFieldValue("0");
						String content = CommonUtil.toNotNull(swdRecord.getDataFieldValue("6"));
						String startDateStr = swdRecord.getDataFieldValue("1");
						String endDateStr = swdRecord.getDataFieldValue("2");
						
						String startDate = "";
						String endDate = "";
						if (!CommonUtil.isEmpty(startDateStr)) {
							
							Date sDate = DateUtil.toDate(startDateStr,"yyyy-MM-dd hh:mm:ss");
							long time = sDate.getTime() + (1000 * 60 * 60 * 9);
							sDate.setTime(time);
							startDate = DateUtil.toDateString(sDate);
							
						}
						if (!CommonUtil.isEmpty(endDateStr)) {
							Date eDate = DateUtil.toDate(endDateStr,"yyyy-MM-dd hh:mm:ss");
							long time = eDate.getTime() + (1000 * 60 * 60 * 9);
							eDate.setTime(time);
							endDate = DateUtil.toDateString(eDate);
						}
						
						buffer.append("<item name=\""+ name + "\" startDate=\"" + startDate + "\" endDate=\"" +endDate+ "\"><![CDATA["+content+"]]></item>");
					}
				}
				buffer.append("</Result>");
				System.out.println(buffer.toString());
			}
			
		} else if(method.equals("setEvent")) {
			
		// 루트 부서 조회
		} else {
			buffer.append("<Result status=\"Failed\"><message>Invalid method! Not found method parameter</message><trace/></Result>");
		}
		
	} catch (Throwable th) {
		buffer.append("<Result status=\"Failed\">");
		buffer.append("<message>");
		buffer.append("Failed to execute method (" + method + ") - " + th.getMessage());
		buffer.append("</message>");
		buffer.append("<trace><![CDATA[");
		buffer.append(ExceptionUtil.getTraceMessage("", th));
		buffer.append("]]></trace>");
		buffer.append("</Result>");
		LogFactory.getLog("organizationService").error(buffer.toString());
	}
%><%= buffer.toString() %>