<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="org.springframework.util.StringUtils"%>
<%@page import="net.smartworks.server.engine.resource.manager.IResourceDesigntimeManager"%>
<%@page import="net.smartworks.server.engine.infowork.form.model.SwfForm"%>
<%@page import="net.smartworks.server.engine.infowork.form.manager.ISwfManager"%>
<%@page import="net.smartworks.server.engine.factory.SwManagerFactory"%>
<%@page import="net.smartworks.server.engine.infowork.form.model.SwfFormCond"%>
<%@page import="net.smartworks.server.engine.common.manager.IManager"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%


	boolean toTrue = true;
	String user = "kmyu@maninsoft.co.kr";
	String level = IManager.LEVEL_ALL;
	SwfFormCond cond = new SwfFormCond();
	cond.setStatus("DEPLOYED");
	//cond.setObjId("4028800b3b3f95a4013b3fa3914c0003");
	
	ISwfManager mgr = SwManagerFactory.getInstance().getSwfManager();
	SwfForm[] forms = mgr.getForms(user, cond, level);
	IResourceDesigntimeManager dMgr = SwManagerFactory.getInstance().getDesigntimeManager();
	
	
	
	/* for (int i = 0; i < forms.length; i++) {
		SwfForm form = forms[i];
		String formId = form.getId();
		String formName = form.getName();
		String formXml = form.getObjString();
		//parsing

		if (CommonUtil.isEmpty(formXml))
			continue;
		
		if (toTrue) {
			if (formXml.indexOf("eachTime=\"false\"") == -1)
				continue;
			formXml = StringUtils.replace(formXml, "eachTime=\"false\"", "eachTime=\"true\"");
			formXml = StringUtils.replace(formXml, "(한번만)", "(매번)");
		} else {
			if (formXml.indexOf("eachTime=\"true\"") == -1)
				continue;
			formXml = StringUtils.replace(formXml, "eachTime=\"true\"", "eachTime=\"false\"");
			formXml = StringUtils.replace(formXml, "(매번)", "(한번만)");
		}
		
		//saving
		dMgr.updateFormContentWithoutStatus(formId, 1, formXml);
		
		System.out.println("[FORM NAME : " + formName + ", FORM ID : " + formId + "] EachTime=\""+ toTrue +"\" CHANGED!");
		Thread.sleep(100);
	}
	mgr.cleanFormMapCache(); */

%>
