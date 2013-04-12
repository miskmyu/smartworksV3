<%@page import="java.io.File"%>
<%@page import="net.smartworks.server.engine.common.util.FileUtil"%>
<%@page import="net.smartworks.server.service.util.ModelConverter"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="net.smartworks.server.engine.resource.model.IFormModel"%>
<%@page import="java.util.List"%>
<%@page import="net.smartworks.server.engine.resource.manager.IResourceDesigntimeManager"%>
<%@page import="net.smartworks.server.engine.resource.model.IProcessModel"%>
<%@page import="net.smartworks.server.engine.pkg.model.PkgPackageCond"%>
<%@page import="net.smartworks.server.engine.resource.model.hb.HbPackageModel"%>
<%@page import="net.smartworks.server.engine.common.manager.IManager"%>
<%@page import="net.smartworks.server.engine.common.model.Order"%>
<%@page import="net.smartworks.server.engine.category.model.CtgCategoryCond"%>
<%@page import="net.smartworks.server.engine.common.model.SwmProduct"%>
<%@page import="net.smartworks.server.engine.factory.SwManagerFactory"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.server.engine.category.model.CtgCategory"%>
<%@page import="net.smartworks.server.engine.resource.model.IPackageModel"%>
<%@page import="net.smartworks.server.engine.pkg.model.PkgPackages"%>
<%@page import="net.smartworks.server.engine.pkg.model.PkgPackage"%>
<%@page import="net.smartworks.server.engine.common.util.ServletUtil"%>
<%@page import="net.smartworks.server.engine.common.model.Property"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
</head>

<body>
<script type="text/javascript">
</script>
<%!

	public static Object getBean(String beanName, HttpServletRequest request) {
	
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
	
		return (Object) wac.getBean(beanName);
	}
	
	public void getAllSubPackageByCategoryId(String categoryId, List resultList) throws Exception {
		
		if (resultList == null)
			return;
		
		PkgPackageCond pkgCond = new PkgPackageCond();
		pkgCond.setCategoryId(categoryId);
		PkgPackage[] pkgs = SwManagerFactory.getInstance().getPkgManager().getPackages("", pkgCond, IManager.LEVEL_LITE);
		
		if (!CommonUtil.isEmpty(pkgs)) {
			for (int i = 0; i < pkgs.length; i++) {
				PkgPackage pkg = pkgs[i];
				System.out.println("PackageName : " + pkg.getName());
				if (!resultList.contains(pkg.getPackageId())) {
					resultList.add(pkg.getPackageId());
				}
			}
		}
		
		CtgCategoryCond ctgCond = new CtgCategoryCond();
		ctgCond.setParentId(categoryId);
		CtgCategory[] ctgs = SwManagerFactory.getInstance().getCtgManager().getCategorys("", ctgCond, IManager.LEVEL_LITE);
		if (!CommonUtil.isEmpty(ctgs)) {
			for (int i =0; i < ctgs.length; i++) {
				CtgCategory ctg = ctgs[i];
				System.out.println("CategoryName : " + ctg.getName());
				getAllSubPackageByCategoryId(ctg.getObjId(), resultList);
			}
		}
	}
	public PkgPackage[] getAllPackageByIdArray(List packageIdList) throws Exception {
		
		if (CommonUtil.isEmpty(packageIdList))
			return null;
		
		String[] packageIdIns = new String[packageIdList.size()];
		for (int i=0; i < packageIdList.size(); i++) {
			String packageId = (String)packageIdList.get(i);
			packageIdIns[i] = packageId;
		}
		
		PkgPackageCond cond = new PkgPackageCond();
		cond.setPackageIdIns(packageIdIns);
		return SwManagerFactory.getInstance().getPkgManager().getPackages("", cond, IManager.LEVEL_ALL);
	}

%>
<%
	String userId = "kmyu@maninsoft.co.kr";
	String resultFilePath = "/Users/gwangminyu/development/productFile/samsin.txt";

	IResourceDesigntimeManager rdtMgr = SwManagerFactory.getInstance().getDesigntimeManager();
	
	String categoryId = "4028b2a53d57123a013d571767f40005";
	String pkgId = null;
	List pkgIds = null;
	
	PkgPackage[] swpPkgs = null;
	
	if (!CommonUtil.isEmpty(categoryId) && CommonUtil.isEmpty(pkgId) && CommonUtil.isEmpty(pkgIds)) {
		
		List resultPkgIdArray = new ArrayList();
		getAllSubPackageByCategoryId(categoryId, resultPkgIdArray);
		swpPkgs = getAllPackageByIdArray(resultPkgIdArray);
		
	} else if (!CommonUtil.isEmpty(pkgId) && CommonUtil.isEmpty(categoryId) && CommonUtil.isEmpty(pkgIds)) {
		PkgPackageCond pkgCond = new PkgPackageCond();
		pkgCond.setPackageId(pkgId);
		PkgPackage swpPkg = SwManagerFactory.getInstance().getPkgManager().getPackage(userId, pkgCond, IManager.LEVEL_ALL);
		if (swpPkg != null) {
			swpPkgs = new PkgPackage[]{swpPkg};
		}
		
	} else if (!CommonUtil.isEmpty(pkgIds) && CommonUtil.isEmpty(categoryId) && CommonUtil.isEmpty(pkgId)) {
		swpPkgs = getAllPackageByIdArray(pkgIds);
	}
	
	if (swpPkgs == null)
		return;
	
	SwmProduct product = new SwmProduct();
	
	PkgPackages pkgs = new PkgPackages();	
	
	for (int i = 0; i < swpPkgs.length; i++) {
		PkgPackage swpPkg = swpPkgs[i];
		
		String pkgType = swpPkg.getType();
		String packageId = swpPkg.getPackageId();
		if (pkgType == null || pkgType.length() == 0) {
			throw new Exception("Package type is null.");
		} else if (pkgType.equalsIgnoreCase(PkgPackage.TYPE_PROCESS)) {
			IProcessModel prc = rdtMgr.retrieveProcessByPackage("", packageId, 1);
			if (prc != null)
				swpPkg.setContent(rdtMgr.retrieveProcessContent(userId, prc.getId()));
		} else if (pkgType.equalsIgnoreCase(PkgPackage.TYPE_SINGLE)) {
			List frmList = rdtMgr.findSingleFormByPackage(userId, packageId, 1);
			if (frmList != null && !frmList.isEmpty()) {
				IFormModel frm = (IFormModel)frmList.get(0);
				String frmCtt = rdtMgr.retrieveFormContent(userId, frm.getId());
				swpPkg.setContent(frmCtt);
			}
		}
			
		List frmList = rdtMgr.findFormByPackage(userId, packageId, 1);
		frmList.size();
		List frmCList = new ArrayList();
		for (Iterator itr = frmList.iterator(); itr.hasNext();) {
			IFormModel frm = (IFormModel)itr.next();
			String frmType = frm.getType();
			if (frmType != null && frmType.equalsIgnoreCase(IFormModel.TYPE_SINGLE))
				continue;
			String frmCtt = rdtMgr.retrieveFormContent(userId, frm.getId());
			if (frmCtt == null)
				continue;
			frmCList.add(frmCtt);
		}
		if (!frmCList.isEmpty()) {
			String[] frmCArray = (String[]) frmCList.toArray(new String[frmCList.size()]);
			swpPkg.setFormContents(frmCArray);
		}
		pkgs.addPackage(swpPkg);
	}

	product.setValue(pkgs.toString());
	
	//System.out.println(product.getValue());
	FileUtil.writeString(resultFilePath, product.toString(), true, "UTF-8");

%>
Done!
</body>
</html>
