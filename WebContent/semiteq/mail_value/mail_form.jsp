<%@page import="net.smartworks.server.engine.infowork.domain.model.SwdDomainCond"%>
<%@page import="net.smartworks.server.engine.infowork.domain.model.SwdDomain"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="net.smartworks.server.engine.common.model.Order"%>
<%@page import="net.smartworks.server.engine.infowork.form.model.SwfForm"%>
<%@page import="net.smartworks.server.engine.infowork.form.model.SwfFormCond"%>
<%@page import="net.smartworks.server.engine.process.process.model.PrcProcessCond"%>
<%@page import="net.smartworks.server.engine.factory.SwManagerFactory"%>
<%@page import="net.smartworks.server.engine.process.process.model.PrcProcess"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%
	String processId = request.getParameter("data"); 
	String method = request.getParameter("method");
	
	if( method.equals("1") ){	// 정보관리 업무 검색
		SwdDomainCond formCond = new SwdDomainCond();
		formCond.setOrders(new Order[]{new Order(SwdDomain.A_FORMNAME, true)});	// 가나다라순
		SwdDomain[] forms = SwManagerFactory.getInstance().getSwdManager().getDomains("", formCond, null);
		List list = new ArrayList();
		for (int i = 0; i < forms.length ; i++) {
			SwdDomain form = forms[i];
			list.add(form);
		}
		JSONObject jsonObject = new JSONObject();
		try {
	       jsonObject.put("list", list);
	       jsonObject.put("count", String.valueOf(forms.length));
		   out.print(jsonObject.toString());
		} catch (Exception e) {
		  e.printStackTrace();
		}
	}else{		// 프로세스 업무 검색
		
		PrcProcessCond prcCond = new PrcProcessCond();
		prcCond.setProcessId(processId);
		PrcProcess[] prcs = SwManagerFactory.getInstance().getPrcManager().getProcesses("", prcCond, null);
		
		//프로세스 아이디로 검색했기 때문에 프로세스는 항상 1개다
		PrcProcess prc = prcs[0];
		String packageId = prc.getDiagramId();
		SwfFormCond formCond = new SwfFormCond();
		formCond.setPackageId(packageId);
		
		SwfForm[] forms = SwManagerFactory.getInstance().getSwfManager().getForms("", formCond, null);
		List list = new ArrayList();
		for (int i = 0; i < forms.length ; i++) {
			SwfForm form = forms[i];
			list.add(form);
		}
		JSONObject jsonObject = new JSONObject();
		try {
	       jsonObject.put("list", list);
	       jsonObject.put("count", String.valueOf(forms.length));
		   out.print(jsonObject.toString());
		} catch (Exception e) {
		  e.printStackTrace();
		}
	}
%>
