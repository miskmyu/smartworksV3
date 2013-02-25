<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@page import="net.smartworks.server.engine.common.model.Filter"%>
<%@page import="java.util.Calendar"%>
<%@page import="net.smartworks.util.SmartUtil"%>
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
	
			workSpaceIdInList.add(userId);
			DepartmentInfo[] myDepartments = getMyDepartments(userId);
			if(!CommonUtil.isEmpty(myDepartments)) {
				for(DepartmentInfo myDepartment : myDepartments) {
					String myDeptId = myDepartment.getId();
					workSpaceIdInList.add(myDeptId);
				}
			}
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
	StringBuffer buffer = new StringBuffer();
	
	try {
		if(method.equals("")) {
		
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
				buffer.append("[");
				boolean isFirst = true;
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
							if (!CommonUtil.isEmpty(startDate)) {
								startDate = startDate.substring(0, 16);
							}
						}
						if (!CommonUtil.isEmpty(endDateStr)) {
							Date eDate = DateUtil.toDate(endDateStr,"yyyy-MM-dd hh:mm:ss");
							long time = eDate.getTime() + (1000 * 60 * 60 * 9);
							eDate.setTime(time);
							endDate = DateUtil.toDateString(eDate);
							if (!CommonUtil.isEmpty(endDate)) {
								endDate = endDate.substring(0, 16);
							}
						}
						if (isFirst) {
							buffer.append("{\"name\":\"").append(name).append("\",\"startDate\":\"").append(startDate).append("\", \"endDate\":\"").append(endDate).append("\"}");
							isFirst = false;
						} else {
							buffer.append(",{\"name\":\"").append(name).append("\",\"startDate\":\"").append(startDate).append("\", \"endDate\":\"").append(endDate).append("\"}");
						}
						
					}
				}
				buffer.append("]");
				System.out.println(buffer.toString());
			}
			
		} else if(method.equals("getNotice")) {
			String workId = SmartWork.ID_BOARD_MANAGEMENT;
			SwoUser user = SwManagerFactory.getInstance().getSwoManager().getUser(userId, userId, IManager.LEVEL_LITE);

			SwdRecordCond swdRecordCond = new SwdRecordCond();
			swdRecordCond.setCompanyId(user.getCompanyId());
			String domainId = "frm_notice_SYSTEM";
			swdRecordCond.setDomainId(domainId);
			String[] workSpaceIdIns = getWorkSpaceIdIns(user.getId());
			swdRecordCond.setWorkSpaceIdIns(workSpaceIdIns);
			swdRecordCond.setLikeAccessValues(workSpaceIdIns);

			swdRecordCond.setOrders(new Order[]{new Order("createdTime", false)});
			
			Calendar cal = Calendar.getInstance();			
			String searchDate = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + (cal.get(Calendar.DATE) - 1) + " 15:00:00.000";
			swdRecordCond.setFilter(new Filter[]{new Filter(">=","duration", "dateTime", searchDate)});
			
			//END
			SwdRecord[] swdRecords = SwManagerFactory.getInstance().getSwdManager().getRecords(userId, swdRecordCond, IManager.LEVEL_LITE);

			buffer.append("[");
			boolean isFirst = true;
			if(swdRecords != null) {
				for(int i=0; i < swdRecords.length; i++) {
					SwdRecord swdRecord = swdRecords[i];
					//0 title 3 ???
					String title = swdRecord.getDataFieldValue("0");
					String content = swdRecord.getDataFieldValue("1");
					content = "";
					if (!CommonUtil.isEmpty(content)) {
						content = StringUtils.replace(content, ",", "_");
					}
					String createDateStr = swdRecord.getDataFieldValue("3");
					String createDate = "";
					if (!CommonUtil.isEmpty(createDateStr)) {
						Date sDate = DateUtil.toDate(createDateStr,"yyyy-MM-dd hh:mm:ss");
						long time = sDate.getTime() + (1000 * 60 * 60 * 9);
						sDate.setTime(time);
						createDate = DateUtil.toDateString(sDate);
						if (!CommonUtil.isEmpty(createDate)) {
							createDate = createDate.substring(0, 16);
						}
					}
					if (isFirst) {
						buffer.append("{\"name\":\"").append(title).append("\",\"createDate\":\"").append(createDate).append("\", \"content\":\""+ content +"\"}");
						isFirst = false;
					} else {
						buffer.append(",{\"name\":\"").append(title).append("\",\"createDate\":\"").append(createDate).append("\", \"content\":\""+ content +"\"}");
					}
				}
			}
			buffer.append("]");
		} else {
			
		}
		
	} catch (Throwable th) {
		th.printStackTrace();
	}
%><%= buffer.toString() %>