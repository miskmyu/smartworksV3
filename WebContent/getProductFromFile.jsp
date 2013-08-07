<%@page import="java.io.File"%>
<%@page import="net.smartworks.server.engine.common.util.FileUtil"%>
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

public static IPackageModel convertPkgModel(PkgPackage pkg) {
	IPackageModel newPkg = new HbPackageModel();
	if (pkg.getObjId() != null)
		newPkg.setId(pkg.getObjId());
	if (pkg.getPackageId() != null)
		newPkg.setPackageId(pkg.getPackageId());
	if (pkg.getVersion() > 0)
		newPkg.setVersion(pkg.getVersion());
	if (pkg.getStatus() != null)
		newPkg.setStatus(pkg.getStatus());
	if (pkg.getLatestDeployedYn() != null)
		newPkg.setLatestDeployedYn(pkg.getLatestDeployedYn());
	if (pkg.getCategoryId() != null)
		newPkg.setCategoryId(pkg.getCategoryId());
	if (pkg.getName() != null)
		newPkg.setName(pkg.getName());
	if (pkg.getType() != null)
		newPkg.setType(pkg.getType());
	if (pkg.getCreationDate() != null)
		newPkg.setCreatedTime(pkg.getCreationDate());
	if (pkg.getCreationUser() != null)
		newPkg.setCreator(pkg.getCreationUser());
	if (pkg.getModificationDate() != null)
		newPkg.setModifiedTime(pkg.getModificationDate());
	if (pkg.getModificationUser() != null)
		newPkg.setModifier(pkg.getModificationUser());
	if (pkg.getDescription() != null)
		newPkg.setDescription(pkg.getDescription());
	if (pkg.getContent() != null)
		newPkg.setContent(pkg.getContent());
	if (pkg.getFormContents() != null)
		newPkg.setFormContentList(CommonUtil.toList(pkg.getFormContents()));
	return newPkg;
}
%>
<%
	String filePath = "/Users/gwangminyu/Desktop/onegene_temp2.txt";
	String userId = "kmyu@maninsoft.co.kr";
	String targetCatId = "402880bb3fcc8e62013fccd89a930001";
	
	
	String returnStr = FileUtil.readString(new File(filePath), "UTF-8");
	if (CommonUtil.isEmpty(returnStr))
		return;
	
	if (returnStr != null && returnStr.length() != 0 && returnStr.indexOf("product") != -1) {
		SwmProduct product = (SwmProduct)SwmProduct.toObject(returnStr);
		PkgPackages packages = (PkgPackages)PkgPackages.toObject(product.getValue());
		//packages = (PkgPackages)packages.cloneNew();
	
		PkgPackage[] pkgArray = packages.getPackage();
		
		for (int i = 0 ; i < pkgArray.length ; i++ ) {
			IPackageModel pkg = convertPkgModel(pkgArray[i]);
			//TODO pkg.setCompanyId(cRompanyId);
			SwManagerFactory.getInstance().getDesigntimeManager().createPackage(userId, targetCatId, pkg);
		}
	} else {
		out.println("No Product");
	}

%>

</body>
</html>
