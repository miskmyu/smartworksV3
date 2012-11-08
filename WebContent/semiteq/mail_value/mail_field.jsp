<%@ page import="java.io.*" %> 
<%@ page import="java.util.*" %> 
<%@ page import="org.jdom.*" %> 
<%@ page import="org.jdom.input.*" %> 
<%@page import="net.smartworks.server.engine.infowork.form.model.SwfFormDef"%>
<%@page import="net.smartworks.server.engine.infowork.form.model.SwfForm"%>
<%@page import="net.smartworks.server.engine.infowork.form.model.SwfFormCond"%>
<%@page import="net.smartworks.server.engine.infowork.domain.model.SwdField"%>
<%@page import="net.smartworks.server.engine.infowork.form.model.SwfField"%>
<%@page import="net.smartworks.server.engine.infowork.domain.model.SwdFieldCond"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="net.smartworks.server.engine.factory.SwManagerFactory"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%
	String FormId = request.getParameter("data"); 
	String method = request.getParameter("method");
	if(method.equals("1")) // 정보 관리 업무에 대한 필드를 가지고 옵니다.
	{
	 	SwfFormCond cond = new SwfFormCond();
		List names = new ArrayList();
		List sytemTypes = new ArrayList();
		List ids = new ArrayList();
		try{
			String content = SwManagerFactory.getInstance().getSwfManager().getFormContent("", FormId);
		
			SAXBuilder builder = new SAXBuilder(); 
			if(content==null){
				
			}else{
			Document doc = builder.build(new StringReader(content)); 
			Element xmlRoot = doc.getRootElement(); //form 
			Attribute name;
			Attribute systemType;
			Attribute id;
			String nameValue = ""; 
			String systemTypeValue = "";
			String idValue = "";
			//content 
			List contentList = xmlRoot.getChildren("children"); 
			for(int i=0; i<1; i++){
				Element xmlContent = (Element) contentList.get(i); 
				List form = xmlContent.getChildren("formEntity"); 
				//System.out.println(form.size());
				for(int j=0;j<form.size(); j++){
					Element xmlform = (Element) form.get(j); 
					name = xmlform.getAttribute("name"); 
					systemType= xmlform.getAttribute("systemType");
					id= xmlform.getAttribute("id"); 
					systemTypeValue = systemType.getValue(); 
					nameValue = name.getValue();
					idValue = id.getValue();
					names.add(nameValue); 
					sytemTypes.add(systemTypeValue);
					ids.add(idValue);
					//System.out.println("Id => " + idValue + " name = >" + nameValue + "type => " + systemTypeValue);
				}
			} 
			
			JSONObject jsonObject = new JSONObject();
		 	try {
		        jsonObject.put("id", ids);
		        jsonObject.put("name", names);
		        jsonObject.put("type", sytemTypes);
		 	    out.print(jsonObject.toString());
		 	} catch (Exception e) {
		 	  e.printStackTrace();
		 	}
		}	
			
		} catch(Exception e) { 
			e.printStackTrace(); 
		} 
		
	}else{					// 프로세스 업무에 대한 필드를 가지고옵니다.
		SwfFormCond cond = new SwfFormCond();
		List names = new ArrayList();
		List sytemTypes = new ArrayList();
		List ids = new ArrayList();
		try{
			String content = SwManagerFactory.getInstance().getSwfManager().getFormContent("", FormId);
		
			SAXBuilder builder = new SAXBuilder(); 
			if(content==null){
				
			}else{
			Document doc = builder.build(new StringReader(content)); 
			Element xmlRoot = doc.getRootElement(); //form 
			Attribute name;
			Attribute systemType;
			Attribute id;
			String nameValue = ""; 
			String systemTypeValue = "";
			String idValue = "";
			//content 
			List contentList = xmlRoot.getChildren("children"); 
			for(int i=0; i<1; i++){
				Element xmlContent = (Element) contentList.get(i); 
				List form = xmlContent.getChildren("formEntity"); 
				//System.out.println(form.size());
				for(int j=0;j<form.size(); j++){
					Element xmlform = (Element) form.get(j); 
					name = xmlform.getAttribute("name"); 
					systemType= xmlform.getAttribute("systemType"); 
					id= xmlform.getAttribute("id"); 
					systemTypeValue = systemType.getValue(); 
					nameValue = name.getValue();
					idValue = id.getValue();
					names.add(nameValue); 
					sytemTypes.add(systemTypeValue);
					ids.add(idValue);
					//System.out.println("Id => " + idValue + " name = >" + nameValue + "type => " + systemTypeValue);
				}
			} 
			
			JSONObject jsonObject = new JSONObject();
		 	try {
		        jsonObject.put("id", ids);
		        jsonObject.put("name", names);
		        jsonObject.put("type", sytemTypes);
		 	    out.print(jsonObject.toString());
		 	} catch (Exception e) {
		 	  e.printStackTrace();
		 	}
		}	
			
		} catch(Exception e) { 
			e.printStackTrace(); 
		} 
	}

%>
