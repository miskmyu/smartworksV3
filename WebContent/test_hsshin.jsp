<%@page import="net.smartworks.model.community.info.CommunityInfo"%>
<%@page import="java.util.Date"%>
<%@page import="net.smartworks.server.engine.sera.model.CourseDetail"%>
<%@page import="net.smartworks.model.sera.SeraBoardList"%>
<%@page import="net.smartworks.server.service.ISeraService"%>
<%@page import="net.smartworks.model.work.SmartWork"%>
<%@page import="net.smartworks.server.engine.process.task.manager.impl.TskManagerMailAdvisorImpl"%>
<%@page import="javax.mail.Authenticator"%>
<%@page import="java.io.UnsupportedEncodingException"%>
<%@page import="javax.mail.MessagingException"%>
<%@page import="javax.mail.internet.MimeMessage"%>
<%@page import="javax.mail.Message"%>
<%@page import="javax.mail.PasswordAuthentication"%>
<%@page import="javax.mail.Transport"%>
<%@page import="javax.mail.internet.InternetAddress"%>
<%@page import="javax.mail.Session"%>
<%@page import="java.util.Properties"%>
<%@page import="javax.mail.internet.AddressException"%>
<%@page import="net.smartworks.server.engine.common.util.PasswordGenerator"%>
<%@page import="java.util.Random"%>
<%@page import="net.smartworks.model.community.info.DepartmentInfo"%>
<%@page import="net.smartworks.server.engine.infowork.domain.model.SwdDomain"%>
<%@page import="net.smartworks.server.engine.infowork.domain.model.SwdDomainCond"%>
<%@page import="net.smartworks.server.engine.category.model.CtgCategory"%>
<%@page import="net.smartworks.server.engine.category.model.CtgCategoryCond"%>
<%@page import="net.smartworks.server.engine.process.process.model.PrcProcessInst"%>
<%@page import="net.smartworks.server.engine.process.process.model.PrcProcessInstCond"%>
<%@page import="net.smartworks.server.engine.pkg.model.PkgPackageCond"%>
<%@page import="net.smartworks.server.engine.pkg.model.PkgPackage"%>
<%@page import="net.smartworks.server.engine.opinion.model.Opinion"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.HashMap"%>
<%@page import="net.smartworks.util.SmartMessage"%>
<%@page import="net.smartworks.model.work.info.FileCategoryInfo"%>
<%@page import="net.smartworks.server.engine.docfile.model.FileWork"%>
<%@page import="net.smartworks.server.engine.docfile.model.FileWorkCond"%>
<%@page import="net.smartworks.server.engine.docfile.manager.IDocFileManager"%>
<%@page import="net.smartworks.model.work.FileCategory"%>
<%@page import="net.smartworks.server.engine.docfile.model.HbFileModel"%>
<%@page import="net.smartworks.model.community.Community"%>
<%@page import="org.springframework.util.StringUtils"%>
<%@page import="net.smartworks.model.instance.info.ImageInstanceInfo"%>
<%@page import="net.smartworks.server.engine.docfile.model.DocFile"%>
<%@page import="net.smartworks.server.engine.common.model.Order"%>
<%@page import="net.smartworks.server.engine.folder.model.FdrFolderCond"%>
<%@page import="net.smartworks.server.engine.folder.model.FdrFolder"%>
<%@page import="net.smartworks.server.engine.folder.model.FdrFolderFile"%>
<%@page import="net.smartworks.server.engine.docfile.model.IFileModel"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.model.work.info.ImageCategoryInfo"%>
<%@page import="net.smartworks.model.instance.info.RequestParams"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.server.engine.common.util.StringUtil"%>
<%@page import="net.smartworks.model.service.Variable"%>
<%@page import="net.smartworks.model.service.WSDLOperation"%>
<%@page import="net.smartworks.model.service.WSDLPort"%>
<%@page import="net.smartworks.model.service.WSDLDetail"%>
<%@page import="javax.xml.namespace.QName"%>
<%@page import="javax.wsdl.Part"%>
<%@page import="java.util.Set"%>
<%@page import="javax.wsdl.Output"%>
<%@page import="javax.wsdl.Input"%>
<%@page import="javax.wsdl.Operation"%>
<%@page import="javax.wsdl.PortType"%>
<%@page import="java.util.Map"%>
<%@page import="net.smartworks.server.engine.config.manager.ISwcManager"%>
<%@page import="net.smartworks.model.community.info.GroupInfo"%>
<%@page import="net.smartworks.server.service.ICommunityService"%>
<%@page import="net.smartworks.server.engine.common.model.SmartServerConstant"%>
<%@page import="net.smartworks.server.engine.common.util.id.IDCreator"%>
<%@page import="net.smartworks.server.engine.organization.model.SwoGroup"%>
<%@page import="java.util.ArrayList"%>
<%@page import="net.smartworks.server.engine.organization.model.SwoGroupMember"%>
<%@page import="net.smartworks.server.engine.organization.model.SwoGroupCond"%>
<%@page import="net.smartworks.server.service.impl.WorkServiceImpl"%>
<%@page import="net.smartworks.model.instance.info.InstanceInfoList"%>
<%@page import="net.smartworks.server.engine.common.manager.IManager"%>
<%@page import="net.smartworks.server.engine.common.model.Property"%>
<%@page import="net.smartworks.server.engine.process.task.model.TskTaskCond"%>
<%@page import="net.smartworks.model.instance.info.TaskInstanceInfo"%>
<%@page import="net.smartworks.server.service.IInstanceService"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.model.calendar.CompanyCalendar"%>
<%@page import="net.smartworks.server.service.IWorkService"%>
<%@page import="net.smartworks.model.community.Department"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.model.community.WorkSpace"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="net.smartworks.server.engine.factory.SwManagerFactory"%>
<%@page import="net.smartworks.server.engine.infowork.domain.model.SwdDataField"%>
<%@page import="net.smartworks.server.engine.infowork.domain.model.SwdRecordCond"%>
<%@page import="net.smartworks.server.engine.infowork.domain.model.SwdRecord"%>
<%@page import="net.smartworks.server.engine.infowork.domain.manager.ISwdManager"%>
<%@page import="net.smartworks.server.engine.infowork.form.model.SwfField"%>
<%@page import="net.smartworks.server.engine.infowork.form.model.SwfForm"%>
<%@page import="net.smartworks.server.engine.infowork.form.model.SwfFormCond"%>
<%@page import="net.smartworks.server.engine.infowork.form.manager.ISwfManager"%>
<%@page import="net.smartworks.server.engine.process.process.model.PrcProcessCond"%>
<%@page import="net.smartworks.server.engine.process.deploy.manager.IDepManager"%>
<%@page import="net.smartworks.server.engine.organization.model.SwoUserCond"%>
<%@page import="net.smartworks.server.engine.organization.model.SwoUser"%>
<%@page import="net.smartworks.server.engine.organization.manager.ISwoManager"%>
<%@page import="net.smartworks.server.engine.process.link.model.LnkLink"%>
<%@page import="net.smartworks.server.engine.process.link.manager.ILnkManager"%>
<%@page import="net.smartworks.server.engine.process.process.model.PrcProcess"%>
<%@page import="net.smartworks.server.engine.process.process.manager.IPrcManager"%>
<%@page import="net.smartworks.server.engine.process.task.model.TskTaskDef"%>
<%@page import="net.smartworks.server.engine.process.task.model.TskTask"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="net.smartworks.server.engine.process.task.manager.impl.TskManagerImpl"%>
<%@page import="net.smartworks.server.engine.process.task.manager.ITskManager"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
</head>
<body>
<%
	/* IDepManager mgr = (IDepManager)SmartUtil.getBean("depManager", request);

	IPrcManager prcMgr = (IPrcManager)SmartUtil.getBean("prcManager", request);

	PrcProcessCond cond = new PrcProcessCond();
	
	cond.setObjId("pkg_01584e5c9ae544bfb390a526cb22e26c|prc_8c4176cba1ed46ed95997b71028291c7");
	
	PrcProcess[] prcs = prcMgr.getProcesses("hsshin@maninsoft.co.kr", cond, null);
	
	
	String xpdl = prcs[0].getDiagram();
	
	mgr.deploy("hsshin@maninsoft.co.kr", xpdl, null); */
	
/* 	ISwfManager swfMgr = (ISwfManager)SmartUtil.getBean("swfManager", request);

	SwfForm swfForm = swfMgr.getForm("hsshin@maninsoft.co.kr", "frm_3dbf6b88c28346a181172db0828a4bd4");

	SwfField[] fields = swfForm.getFields();

	for(SwfField field : fields) {
		System.out.println(field.getName());
		System.out.println(field.getSystemType());
	}

	ISwdManager swdMgr = (ISwdManager)SmartUtil.getBean("swdManager", request);

	SwdRecordCond swdRecordCond = new SwdRecordCond();
	swdRecordCond.setFormId("frm_board_SYSTEM");
	swdRecordCond.setRecordId("402880a53169245601316a27f1530005");

	SwdRecord swdRecord = swdMgr.getRecord("hsshin@maninsoft.co.kr", swdRecordCond, null);

	System.out.println(swdRecord.toString(null, null));
	SwdDataField[] dataFields = swdRecord.getDataFields();
	for(SwdDataField dataField : dataFields) {
		dataField.getId();
		dataField.getName();
		dataField.getValue();
	}

	List<String> list = SwManagerFactory.getInstance().getDocManager().findDocIdByGroupId("fg_0019ae0b682e451b953fbf823a633db6");
	for(String str : list) {
		out.println(str);
	} */

/* 	ISmartWorks smartworks = (ISmartWorks)SmartUtil.getBean("smartWorks", request);

	DepartmentInfo[] departmentInfos = smartworks.getMyChildDepartments();

	System.out.println(departmentInfos); */
	//InformationWork infoWork = (InformationWork)smartworks.getWorkById("Maninsoft", "hsshin@maninsoft.co.kr", "pkg_af2c5abbdc694feab78b2706c31f3bde");
	//Work work = smartworks.getWorkById("Maninsoft", "hsshin@maninsoft.co.kr", "pkg_af2c5abbdc694feab78b2706c31f3bde");

	//System.out.println("PackageName = " + work.getName() + ", PackageId = " + work.getId() + ", PackageType = " + work.getType() + ", PackageDescription = " + work.getDesc());

 	/* InformationWork infoWork = (InformationWork) smartworks.getWorkById("Semiteq", "hsshin@maninsoft.co.kr", "pkg_af2c5abbdc694feab78b2706c31f3bde");

 	System.out.println("getAccessPolicy().getLevel()>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+infoWork.getAccessPolicy().getLevel());
	System.out.println("getWritePolicy().getLevel()>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+infoWork.getWritePolicy().getLevel());
	System.out.println("getEditPolicy().getLevel()>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+infoWork.getEditPolicy().getLevel());

 	for(FormField form : infoWork.getDisplayFields()) {
		try {
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+CommonUtil.toNotNull(form.getId()) + " " + form.getName());
		} catch(NullPointerException e){
			e.printStackTrace();
		}
	}
 */
/* 	SmartWork smartWork = (SmartWork) smartworks.getWorkById("Semiteq", "hsshin@maninsoft.co.kr", "pkg_af2c5abbdc694feab78b2706c31f3bde");
	System.out.println(smartWork.getFullpathName()); */
/* 	WorkSpace workSpace = smartworks.getWorkSpaceById("PROTEC1_TEAM");
	if(workSpace.getClass().equals(User.class)) {
		User user = (User)workSpace;
		System.out.println(user.getName());
	} else if(workSpace.getClass().equals(Department.class)) {
		Department department = (Department)workSpace;
		System.out.println(department.getName());
	} else {
		System.out.println("null");
	} */

/* 	User user = smartworks.getUserById("hsshin@maninsoft.co.kr");
	System.out.println(user.getEmailAddressShown() + user.getName());
	Department department = smartworks.getDepartmentById("PROTEC1_TEAM");
	System.out.println(department.getName() + department.getParent().getName()); */

/*	IWorkService workService = (IWorkService)SmartUtil.getBean("workServiceImpl", request);

	workService.addAFavoriteWork("pkg_ba32f6e9af594c9ea9cf921ffa2cadee");
	*/

	//CompanyCalendar[] companyCalendars = smartworks.getCompanyCalendars(new LocalDate(), 3);
	//workService.removeAFavoriteWork("pkg_ba32f6e9af594c9ea9cf921ffa2cadee");

	IInstanceService instanceService = (IInstanceService)SmartUtil.getBean("instanceServiceImpl", request);

/* 	ITskManager tskMgr = (ITskManager)SmartUtil.getBean("tskManager", request);
	TskTaskCond tskCond = new TskTaskCond();
	tskCond.setForm("frm_98aa2e3d4f334536a441ff484ac37611");
	tskCond.setExtendedProperties(new Property[] {new Property("referencedRecordId", "52fca4b2252dcd8d012534040dd00029")});
	TskTask[] tasks = tskMgr.getTasks("", tskCond, IManager.LEVEL_LITE);

	System.out.println(tasks.length); */

/* 	ISwdManager swdMgr = (ISwdManager)SmartUtil.getBean("swdManager", request);

	SwdRecordCond swdRecordCond = new SwdRecordCond();
	swdRecordCond.setFormId("frm_98aa2e3d4f334536a441ff484ac37611");
	swdRecordCond.setReferencedRecordId("52fca4b2252dcd8d012534040dd00029");

	long totalSize = swdMgr.getRecordSize("hsshin@maninsoft.co.kr", swdRecordCond);  

	System.out.println(totalSize);
	SwdRecord[] swdRecords = swdMgr.getRecords("", swdRecordCond, null); */

	//TaskInstanceInfo[] taskInstanceInfos = instanceService.getInstanceTaskHistoriesById("52fca4b22ca0dbd5012ca52c28ae0011");
	//System.out.println(taskInstanceInfos);
	//InstanceInfoList[] resultList = instanceService.getInstanceRelatedWorksById("52fca4b2265f233c012684fba2630068");
	//System.out.println(resultList.length);

/* 	ISwfManager swfMgr = (ISwfManager)SmartUtil.getBean("swfManager", request);

	int size = swfMgr.getReferenceFormSize("", "52fca4b2265f233c012684fba2630068");

	System.out.println("size ::: " + size); */
	
	
/* 	WorkServiceImpl workServiceImpl = (WorkServiceImpl)SmartUtil.getBean("workServiceImpl", request);
	
	String a = workServiceImpl.getRecentSomeDays(5);

	String b = workServiceImpl.getRecentSomeMonths(5);

	String c = workServiceImpl.getThisWeek();

	String d = workServiceImpl.getThisYear();
	
	String e = workServiceImpl.getThisQuarter();

	String f = workServiceImpl.getThisHalfYear();

	System.out.println(a);
	System.out.println(b);
	System.out.println(c);
	System.out.println(d);
	System.out.println(e);
	System.out.println(f); */

/* 	User user = SmartUtil.getCurrentUser();
	ISwoManager swoMgr = (ISwoManager)SmartUtil.getBean("swoManager", request);
 
	SwoGroup swoGroupCond = new SwoGroup();
	swoGroupCond.setId("group_586ac2d927654b7d99ba45afd5203f01");
	swoGroupCond.setCompanyId(user.getCompanyId());
	swoGroupCond.setName("��ǵ�ȣȸ15");
	swoGroupCond.setGroupLeader(user.getId());
	swoGroupCond.setGroupType("1");
	swoGroupCond.setStatus("2");
	swoGroupCond.setDescription("���μ���Ʈ �系 ��ǵ�ȣȸ15�Դϴ�.");
	List<SwoGroupMember> list = new ArrayList<SwoGroupMember>();
	SwoGroupMember swoGroupMember = new SwoGroupMember();
	swoGroupMember.setUserId("cccc@maninsoft.co.kr");
	swoGroupMember.setJoinType("1");
	swoGroupMember.setJoinStatus("2");
	swoGroupMember.setJoinDate(new LocalDate());
 	list.add(swoGroupMember);

	SwoGroupMember[] swoGroupMembers = new SwoGroupMember[list.size()];
	list.toArray(swoGroupMembers);
	swoGroupCond.setSwoGroupMembers(swoGroupMembers); */

/* 	SwoGroupMember swoGroupMember = new SwoGroupMember();
	swoGroupMember.setUserId("trtrtrtr@maninsoft.co.kr");
	swoGroupMember.setJoinType("1");
	swoGroupMember.setJoinStatus("2");
	swoGroupMember.setJoinDate(new LocalDate());
	SwoGroup swoGroup = swoMgr.getGroup(user.getId(), "group_586ac2d927654b7d99ba45afd5203f01", IManager.LEVEL_ALL);
	swoGroup.addGroupMember(swoGroupMember); */

	//swoMgr.setGroup(user.getId(), swoGroupCond, IManager.LEVEL_ALL);

	//swoMgr.createGroup(user.getId(), swoGroupCond);

 	ICommunityService communityService = (ICommunityService)SmartUtil.getBean("communityServiceImpl", request);
	/*GroupInfo[] groupInfo = communityService.getMyGroups();
	System.out.println(groupInfo); */

/* 	LocalDate localDate = LocalDate.convertLocalTimeStringToLocalDate("14:32");
	System.out.println(localDate);
	
	String str = LocalDate.convertLocalTimeStringToLocalDate("14:32").toGMTTimeString();
	System.out.println(str); */
	
	/* String aa = LocalDate.convertGMTTimeStringToLocalDate("06:14").toLocalTimeString2();
	System.out.println(aa);
	String bb = LocalDate.convertGMTSimple2StringToLocalDate("2012.02.23").toLocalDateSimpleString();
	System.out.println(bb); */

/* 	String cc = LocalDate.convertGMTStringToLocalDate("2012-02-23 15:00:00.0").toLocalDateSimpleString();
	System.out.println(cc);

	String dd = LocalDate.convertGMTStringToLocalDate("2012-02-23 15:00:00.0").toLocalDateTimeSimpleString();
	System.out.println(dd); */

/* 	User user = SmartUtil.getCurrentUser();
	String userId = user.getId();
	Opinion opinion = new Opinion();
	String workInstanceId = "dr_402880ed3614eeda0136150157930004";
	String comment = "";

	TskTaskCond tskCond = new TskTaskCond();
	tskCond.setExtendedProperties(new Property[] {new Property("recordId", workInstanceId)});
	tskCond.setModificationUser(userId);
	tskCond.setOrders(new Order[]{new Order(TskTaskCond.A_CREATIONDATE, false)});
	TskTask[] tskTasks = SwManagerFactory.getInstance().getTskManager().getTasks(userId, tskCond, IManager.LEVEL_LITE);
	TskTask tskTask = tskTasks[0];
	String taskInstId = tskTask.getObjId();
	String def = tskTask.getDef();
	String formId = tskTask.getForm();
	
	String singleWorkInfos = def;
	String domainId = null;
	if (!CommonUtil.isEmpty(singleWorkInfos)) {
		String[] singleWorkInfo = StringUtils.tokenizeToStringArray(singleWorkInfos, "|");	
		domainId = singleWorkInfo[0];
	} */

 	User user = SmartUtil.getCurrentUser();
	String userId = user.getId();
	/* PkgPackageCond pkgPackageCond = new PkgPackageCond();
	pkgPackageCond.setStatus("DEPLOYED");
	pkgPackageCond.setOrders(new Order[]{new Order("categoryId", true)});
	PkgPackage[] pkgPackages = SwManagerFactory.getInstance().getPkgManager().getPackages(userId, pkgPackageCond, IManager.LEVEL_LITE);

	Map<String, FileCategoryInfo> informationMap = new LinkedHashMap<String, FileCategoryInfo>();
	Map<String, FileCategoryInfo> prcInstMap = new LinkedHashMap<String, FileCategoryInfo>();

	FileCategoryInfo[] fileCategoryInfos = null;
	List<FileCategoryInfo> fileCategoryInfoList = new ArrayList<FileCategoryInfo>();
	List<FileCategoryInfo> fileCategoryInfoList2 = new ArrayList<FileCategoryInfo>();
	if(!CommonUtil.isEmpty(pkgPackages)) {
		for(PkgPackage pkgPackage : pkgPackages) {
			if(pkgPackage.getType().equalsIgnoreCase("SINGLE")) {
				SwfFormCond formCond = new SwfFormCond();
				formCond.setPackageId(pkgPackage.getPackageId());
				SwfForm[] swfForms = SwManagerFactory.getInstance().getSwfManager().getForms(userId, formCond, IManager.LEVEL_LITE);
				if(!CommonUtil.isEmpty(swfForms)) {
					for(SwfForm swfForm : swfForms) {
						String formId = swfForm.getId();
						SwdDomainCond domainCond = new SwdDomainCond();
						domainCond.setFormId(formId);
						SwdDomain[] swdDomains = SwManagerFactory.getInstance().getSwdManager().getDomains(userId, domainCond, IManager.LEVEL_LITE);
						String id = swfForm.getPackageId();
						String name = swfForm.getName();
						int length = 0; 
						if(!CommonUtil.isEmpty(swdDomains)) {
							SwdDomain swdDomain = swdDomains[0];
							FileCategoryInfo fileCategoryInfo = new FileCategoryInfo();
							String packageId = id;
							String packageName = name;
							String tableName = swdDomain.getTableName();
							length = SwManagerFactory.getInstance().getSwdManager().getTableRowCount(tableName);
							fileCategoryInfo.setId(packageId);
							fileCategoryInfo.setName(packageName);
							fileCategoryInfo.setLength(length);
							informationMap.put(packageId, fileCategoryInfo);
						}
					}
				}
				
			} else if(pkgPackage.getType().equalsIgnoreCase("PROCESS")) {
				PrcProcessInstCond prcProcessInstCond = new PrcProcessInstCond();
				prcProcessInstCond.setDiagramId(pkgPackage.getPackageId());
				PrcProcessInst[] prcProcessInsts = SwManagerFactory.getInstance().getPrcManager().getProcessInsts(userId, prcProcessInstCond, IManager.LEVEL_LITE);
				if(!CommonUtil.isEmpty(prcProcessInsts)) {
					for(PrcProcessInst prcProcessInst : prcProcessInsts) {
						String diagramId = prcProcessInst.getDiagramId();
						if(!CommonUtil.isEmpty(diagramId)) {
							FileCategoryInfo fileCategoryInfo = new FileCategoryInfo();
							String prcName = prcProcessInst.getName();
							int length = 0;
							fileCategoryInfo.setId(diagramId);
							fileCategoryInfo.setName(prcName);
							if(prcInstMap.size() > 0) {
								for(Map.Entry<String, FileCategoryInfo> entry : prcInstMap.entrySet()) {
									if(entry.getKey().equals(fileCategoryInfo.getId()))
										length = entry.getValue().getLength() + 1;
								}
							}
							fileCategoryInfo.setLength(length);
							prcInstMap.put(diagramId, fileCategoryInfo);
						}
					}
				}
			}
		}
	}
	if(!CommonUtil.isEmpty(informationMap)) {
		for(Map.Entry<String, FileCategoryInfo> entry : informationMap.entrySet()) {
			FileCategoryInfo fileCategoryInfo = entry.getValue();
			fileCategoryInfoList.add(fileCategoryInfo);
		}
	}
	if(fileCategoryInfoList.size() > 0) {
		fileCategoryInfos = new FileCategoryInfo[fileCategoryInfoList.size()];
		fileCategoryInfoList.toArray(fileCategoryInfos);
	}
	if(!CommonUtil.isEmpty(fileCategoryInfos)) {
		for(FileCategoryInfo fileCategoryInfo : fileCategoryInfos) {
			PkgPackageCond packageCond = new PkgPackageCond();
			packageCond.setPackageId(fileCategoryInfo.getId());
			PkgPackage pkgPackage = SwManagerFactory.getInstance().getPkgManager().getPackage(userId, packageCond, IManager.LEVEL_LITE);
			if(pkgPackage != null) {
				String categoryId = pkgPackage.getCategoryId();
				CtgCategoryCond categoryCond = new CtgCategoryCond();
				categoryCond.setObjId(categoryId);
				CtgCategory ctgCategory = SwManagerFactory.getInstance().getCtgManager().getCategory(userId, categoryCond, IManager.LEVEL_LITE);
				if(ctgCategory != null) {
					String ctgName = ctgCategory.getName();
					String parentId = ctgCategory.getParentId();
					categoryCond = new CtgCategoryCond();
					categoryCond.setObjId(parentId);
					ctgCategory = SwManagerFactory.getInstance().getCtgManager().getCategory(userId, categoryCond, IManager.LEVEL_LITE);
					String parentName = "";
					if(ctgCategory != null) {
						if(!ctgCategory.getObjId().equals("_PKG_ROOT_"))
							parentName = ctgCategory.getName() + " > ";
					}
					System.out.println("�������������� : " + parentName + ctgName + " > " + fileCategoryInfo.getName() + ", ��ϰǼ� : " + fileCategoryInfo.getLength());
				}
			}
		}
	}

	if(!CommonUtil.isEmpty(prcInstMap)) {
		for(Map.Entry<String, FileCategoryInfo> entry : prcInstMap.entrySet()) {
			FileCategoryInfo fileCategoryInfo = entry.getValue();
			fileCategoryInfoList2.add(fileCategoryInfo);
		}
	}
	if(fileCategoryInfoList2.size() > 0) {
		fileCategoryInfos = new FileCategoryInfo[fileCategoryInfoList2.size()];
		fileCategoryInfoList2.toArray(fileCategoryInfos);
	}
	if(!CommonUtil.isEmpty(fileCategoryInfos)) {
		for(FileCategoryInfo fileCategoryInfo : fileCategoryInfos) {
			PkgPackageCond packageCond = new PkgPackageCond();
			packageCond.setPackageId(fileCategoryInfo.getId());
			PkgPackage pkgPackage = SwManagerFactory.getInstance().getPkgManager().getPackage(userId, packageCond, IManager.LEVEL_LITE);
			if(pkgPackage != null) {
				String categoryId = pkgPackage.getCategoryId();
				CtgCategoryCond categoryCond = new CtgCategoryCond();
				categoryCond.setObjId(categoryId);
				CtgCategory ctgCategory = SwManagerFactory.getInstance().getCtgManager().getCategory(userId, categoryCond, IManager.LEVEL_LITE);
				if(ctgCategory != null) {
					String ctgName = ctgCategory.getName();
					String parentId = ctgCategory.getParentId();
					categoryCond = new CtgCategoryCond();
					categoryCond.setObjId(parentId);
					ctgCategory = SwManagerFactory.getInstance().getCtgManager().getCategory(userId, categoryCond, IManager.LEVEL_LITE);
					String parentName = "";
					if(ctgCategory != null) {
						if(!ctgCategory.getObjId().equals("_PKG_ROOT_"))
							parentName = ctgCategory.getName() + " > ";
					}
					System.out.println("���μ��������� : " + parentName + ctgName + " > " + fileCategoryInfo.getName() + ", ��ϰǼ� : " + fileCategoryInfo.getLength());
				}
			}
		}
	} */

/* 	TskManagerMailAdvisorImpl tskManagerMailAdvisorImpl = (TskManagerMailAdvisorImpl)SmartUtil.getBean("tskManagerMailAdvisorImpl", request);

	PasswordGenerator passwordGenerator = new PasswordGenerator(8);
	SwoUser[] swoUsers = SwManagerFactory.getInstance().getSwoManager().getUsers(userId, null, null);
	if(!CommonUtil.isEmpty(swoUsers)) {
		for(SwoUser swoUser : swoUsers) {
			String id = swoUser.getId();
			String password = swoUser.getPassword();
			String name = swoUser.getName();
			String[] strings = id.split("@");
			if(strings.length == 2 && name != null) {
				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append("<b>" + name + "</b> �� �ȳ��ϼ���. <br>");
				stringBuffer.append("<br>");
				stringBuffer.append("SERA Campus�� �湮�� �ּż� �����մϴ�. <br>");
				stringBuffer.append("���� ����ķ�۽��� ����� ��ȭ�Ͽ�, ������ �Ǿ����ϴ�. <br>");
				stringBuffer.append("�Ʒ��� ��ũ�� Ŭ���Ͽ� SERA Campus �α����� �Ͻþ�, ������ ���� ��ȣ�� ���Ͽ� ������ �缳���Ͽ� �ֽʽÿ�. <br>");
				stringBuffer.append("<br>");
				stringBuffer.append("<b><a href='http://www.seracampus.com'>http://www.seracampus.com</a></b> <br>");
				stringBuffer.append("<br>");
				stringBuffer.append("�α��� �Ͻ� �� �޴� ��ܿ� ��ġ�� ������ ���̵� ���� v ǥ�ø� Ŭ���Ͻø� [��������]�� �����Ͻʴϴ�. <br>");
				stringBuffer.append("<br>");
				stringBuffer.append("������ ���̵� : <b>" + id + "</b> <br>");
				stringBuffer.append("�ӽ� ��й�ȣ : <b>" + password + "</b> <br>");
				stringBuffer.append("<br>");
				stringBuffer.append("* ���� ȸ���Բ��� ���� �߼������簳�߿�(��ȭ ��ȣ : 02-701-0564)�� �����Ͻø�, �ż��� ó���� �帮���� �ϰڽ��ϴ�. <br>");
				stringBuffer.append("<br>");
				stringBuffer.append("���� �̿� ��Ź�帮�� ���ϲ� ����帳�ϴ�. <br>");
				stringBuffer.append("<br>");
				stringBuffer.append("�� �޽����� SERA�� ���õ� ���� �̸����Դϴ�. <br>");
				stringBuffer.append("�Ϲ����� ���Ǵ� SERAȨ������ ������ �Ǵ� sera@seracampus.com Ȥ�� admin@seracampus.com ���� �����Ͻñ� �ٶ��ϴ�. <br>");
	
				String content = stringBuffer.toString();
				tskManagerMailAdvisorImpl.sendMailByUserInfo(userId, "admin@seracampus.com", id, "SERA Campus ���� ���� ������ ���� �ȳ������Դϴ�.", content);
			}
		}
	} */
	//InstanceInfoList instanceInfoList = instanceService.getInstanceInfoListByWorkId("hsshin@maninsoft.co.kr", null, SmartWork.ID_BOARD_MANAGEMENT);

	//System.out.println(instanceInfoList);
	/* SwoGroup[] swoGroups = SwManagerFactory.getInstance().getSwoManager().getGroups("", null, null);
	CourseDetail[] courseDetails = SwManagerFactory.getInstance().getSeraManager().getCourseDetails("", null);

	for(SwoGroup swoGroup : swoGroups) {
		String groupId = swoGroup.getId();
		String status = swoGroup.getStatus();
		String creator = swoGroup.getCreationUser();
		//swoGroup.setModificationUser(creator);
		//SwManagerFactory.getInstance().getSwoManager().setGroup("", swoGroup, IManager.LEVEL_LITE);
		for(CourseDetail courseDetail : courseDetails) {
			String courseId = courseDetail.getCourseId();
			Date creationDate = courseDetail.getCreateDate();
			if(groupId.equals(courseId)) {
				swoGroup.setCreationDate(creationDate);
				swoGroup.setModificationDate(creationDate);
				SwManagerFactory.getInstance().getSwoManager().setGroup("", swoGroup, IManager.LEVEL_LITE);
			}
		}
	} */
	CommunityInfo[] communityInfos = communityService.getAllComsByGroupId(null);
	System.out.println(communityInfos);
%>
<textarea style="width:800px;height:400px;">
</textarea>
</body>
</html>
