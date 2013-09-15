<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.server.engine.authority.model.SwaUser"%>
<%@page import="net.smartworks.server.engine.authority.model.SwaUserCond"%>
<%@page import="net.smartworks.server.engine.authority.model.SwaAuthProxyCond"%>
<%@page import="net.smartworks.server.engine.authority.model.SwaAuthProxy"%>
<%@page import="net.smartworks.server.engine.authority.model.SwaResource"%>
<%@page import="net.smartworks.server.engine.process.process.model.PrcSwProcess"%>
<%@page import="net.smartworks.server.engine.process.process.model.PrcSwProcessCond"%>
<%@page import="net.smartworks.server.engine.infowork.form.model.SwfForm"%>
<%@page import="net.smartworks.server.engine.infowork.form.model.SwfFormCond"%>
<%@page import="net.smartworks.server.engine.process.process.model.PrcProcess"%>
<%@page import="net.smartworks.server.engine.process.process.model.PrcProcessCond"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="net.smartworks.server.engine.pkg.model.PkgPackage"%>
<%@page import="net.smartworks.server.engine.common.manager.IManager"%>
<%@page import="net.smartworks.server.engine.factory.SwManagerFactory"%>
<%@page import="net.smartworks.server.engine.pkg.model.PkgPackageCond"%>
<%@page import="net.smartworks.server.engine.authority.model.SwaResourceCond"%>
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

	List singleIdList = new ArrayList();
	List processIdList = new ArrayList();

	SwfFormCond formCond = new SwfFormCond();
	SwfForm[] forms = SwManagerFactory.getInstance().getSwfManager().getForms("", formCond, IManager.LEVEL_LITE);	
	for (int i = 0; i < forms.length; i++) {
		
		SwfForm form = forms[i];
		String formId = form.getId();
		singleIdList.add(formId);		
	}
	PrcSwProcessCond prcCond = new PrcSwProcessCond();
	PrcSwProcess[] prcs = SwManagerFactory.getInstance().getPrcManager().getSwProcesses("", prcCond);
	
	for (int i = 0; i < prcs.length; i++) {
		PrcSwProcess prc = prcs[i];
		String processId = prc.getProcessId();
		processIdList.add(processId);
	}
	
	
	for (int i = 0; i < singleIdList.size(); i++) {

		SwaAuthProxyCond proxyCond = new SwaAuthProxyCond();
		proxyCond.setResourceId((String)singleIdList.get(i));
		long count = SwManagerFactory.getInstance().getSwaManager().getAuthProxySize("", proxyCond);
		if (count > 0)
			continue;
		
		SwaResourceCond resourceCond = new SwaResourceCond();
		resourceCond.setResourceId((String)singleIdList.get(i));
		resourceCond.setMode("R");
		
		SwaResource[] resources = SwManagerFactory.getInstance().getSwaManager().getResources("", resourceCond, IManager.LEVEL_LITE);
		
		if (CommonUtil.isEmpty(resources))
			continue;
		
		//1비공개, 2선택공개, 3공개 
		SwaAuthProxy proxy = new SwaAuthProxy();
		proxy.setResourceId(resources[0].getResourceId());
		
		if (resources[0].getPermission().equalsIgnoreCase("PUB_ALL")) {
			proxy.setAccessLevel("3");
		} else if (resources[0].getPermission().equalsIgnoreCase("PUB_SELECT")) {
			proxy.setAccessLevel("2");
			
			SwaUserCond userCond = new SwaUserCond();
			userCond.setResourceId(resources[0].getResourceId());
			
			SwaUser[] users = SwManagerFactory.getInstance().getSwaManager().getUsers("", userCond, IManager.LEVEL_LITE);
			boolean first = true;
			StringBuffer userBuff = new StringBuffer();
			for (int j = 0 ; j < users.length; j++) {
				SwaUser user = users[j];
				if (first) {
					userBuff.append(user.getUserId());
				} else {
					userBuff.append(";").append(user.getUserId());
				}
				first = false;
			}
			proxy.setAccessValue(userBuff.toString());
		} else if (resources[0].getPermission().equalsIgnoreCase("PUB_NO")) {
			proxy.setAccessLevel("1");
		}
		SwManagerFactory.getInstance().getSwaManager().setAuthProxy("", proxy, IManager.LEVEL_ALL);
		System.out.println(proxy.getResourceId() + " _ save complete!");		
	}
	for (int i = 0; i < processIdList.size(); i++) {
		
		SwaAuthProxyCond proxyCond = new SwaAuthProxyCond();
		proxyCond.setResourceId((String)processIdList.get(i));
		long count = SwManagerFactory.getInstance().getSwaManager().getAuthProxySize("", proxyCond);
		if (count > 0)
			continue;
		
		SwaResourceCond resourceCond = new SwaResourceCond();
		resourceCond.setResourceId((String)processIdList.get(i));
		resourceCond.setMode("R");
		
		SwaResource[] resources = SwManagerFactory.getInstance().getSwaManager().getResources("", resourceCond, IManager.LEVEL_LITE);

		if (CommonUtil.isEmpty(resources))
			continue;
		
		//1비공개, 2선택공개, 3공개 
		SwaAuthProxy proxy = new SwaAuthProxy();
		proxy.setResourceId(resources[0].getResourceId());
		
		if (resources[0].getPermission().equalsIgnoreCase("PUB_ALL")) {
			proxy.setAccessLevel("3");
		} else if (resources[0].getPermission().equalsIgnoreCase("PUB_SELECT")) {
			proxy.setAccessLevel("2");
			
			SwaUserCond userCond = new SwaUserCond();
			userCond.setResourceId(resources[0].getResourceId());
			
			SwaUser[] users = SwManagerFactory.getInstance().getSwaManager().getUsers("", userCond, IManager.LEVEL_LITE);
			boolean first = true;
			StringBuffer userBuff = new StringBuffer();
			for (int j = 0 ; j < users.length; j++) {
				SwaUser user = users[j];
				if (first) {
					userBuff.append(user.getUserId());
				} else {
					userBuff.append(";").append(user.getUserId());
				}
				first = false;
			}
			proxy.setAccessValue(userBuff.toString());
		} else if (resources[0].getPermission().equalsIgnoreCase("PUB_NO")) {
			proxy.setAccessLevel("1");
		}
		SwManagerFactory.getInstance().getSwaManager().setAuthProxy("", proxy, IManager.LEVEL_ALL);	
		System.out.println(proxy.getResourceId() + " _ save complete!");
	}

%>

</body>
</html>