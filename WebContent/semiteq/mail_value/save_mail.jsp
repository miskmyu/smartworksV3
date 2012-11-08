<%@page import="java.util.StringTokenizer"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="net.smartworks.server.engine.alim.model.MailAdminCond"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="java.net.URLDecoder" %>    
<%
	String form = request.getParameter("form"); 
	String field = request.getParameter("field"); 
	String[] standa = request.getParameterValues("stand");
	String[] daya = request.getParameterValues("day"); 
	String[] conditiona = request.getParameterValues("condition"); 
	String[] condition_texta = request.getParameterValues("condition_text"); 
	String[] filea = request.getParameterValues("file"); 
	String[] contexta = request.getParameterValues("context");
	StringTokenizer stands = new StringTokenizer(standa[0], ",");
	StringTokenizer days = new StringTokenizer(daya[0], ",");
	StringTokenizer conditions = new StringTokenizer(conditiona[0], ",");
	StringTokenizer condition_texts = new StringTokenizer(condition_texta[0], "▩▦,");
	StringTokenizer files = new StringTokenizer(filea[0], ",");
	StringTokenizer contexts = new StringTokenizer(contexta[0], "▩▦,");
	String[] stand = new String[stands.countTokens()];
	String[] day = new String[stands.countTokens()];
	String[] condition = new String[stands.countTokens()];
	String[] condition_text = new String[stands.countTokens()];
	String[] file = new String[stands.countTokens()];
	String[] context = new String[stands.countTokens()];

	for(int i=0;i<=stands.countTokens();i++){
			stand[i] = stands.nextToken();
			day[i] = days.nextToken();
			condition[i] = conditions.nextToken();
			condition_text[i] = condition_texts.nextToken();
			file[i] = files.nextToken();
			context[i] = contexts.nextToken();
			condition[i] = URLDecoder.decode(condition[i], "utf-8");
			condition_text[i] = URLDecoder.decode(condition_text[i], "utf-8");
			context[i] = URLDecoder.decode(context[i], "utf-8");
	}
	
	String SQL_DRIVER="com.microsoft.sqlserver.jdbc.SQLServerDriver";
	String URL="jdbc:sqlserver://192.168.50.126:1434;SelectMethod=cursor;DatabaseName=Dev_Test_SemiteqV3;";
	String USER = "sa";
	String PASS = "dkagh1";
	
	try{
		   Class.forName(SQL_DRIVER);
	} catch (ClassNotFoundException e) {
		   e.printStackTrace();
	}
	int result = 0;
	PreparedStatement pstmt = null;
	Connection con = null;
	ResultSet rs = null;
	try{
		con = DriverManager.getConnection(URL,USER,PASS);
	for(int i=0;i<stand.length;i++){
		
		String query = "";
		query = query + "select MAILOBJID from MAIL_ADMIN where 1=1 ";
		query = query + " and MAILFORMID = '" + form + "' ";
		query = query + " and MAILFIELDID = '" + field + "' ";
		if(stand[i].equals("0")){query = query + " and MAILSTAND =0 ";}else{ query = query + " and MAILSTAND =1 "; }
		query = query + " and MAILDAY = '" + day[i] + "' ";
		if(condition[i] == null){}else{
			query = query + " and MAILCONDITIONTYPE = '" + condition[i] + "' ";
		}
		
		if(condition_text[i] == null){}else{
			query = query + " and MAILCONDITION = '" + condition_text[i] + "' ";
		}
		
		if(file[i].equals("true")){	query = query + " and ATTACHMENTYN =1 ";}else{query = query + " and ATTACHMENTYN = 0 ;";}

		pstmt = con.prepareStatement(query);
		
		rs = pstmt.executeQuery();
		
		String mode = "insert";
		int row = 0;
		if(rs.next()){
			mode = "update";
			row = rs.getInt(1);
		}
		if(mode.equals("insert")){
			String rowNum = "";
			
			rowNum = rowNum + "select MAX(mailobjid) as row from MAIL_ADMIN";
			pstmt = con.prepareStatement(rowNum);
			rs = pstmt.executeQuery();
			if(rs.next()){
				row = rs.getInt(1);
			}
			row = row + 1;
			String insert = "";	
			insert = insert + "insert into MAIL_ADMIN values (";
			insert = insert + "" + row + ",";
			insert = insert + "'" + form + "',";
			insert = insert + "'" + field + "',";
			if(stand[i].equals("0")){insert = insert + "0,";}else{insert = insert + "1,"; }
			insert = insert + "'" + day[i] + "',";
			if(condition[i]==null){insert = insert + "' ',";}else{insert = insert + "'" +  condition[i] + "',";}
			if(condition_text[i]==null){insert = insert + "' ',";}else{insert = insert + "'" +  condition_text[i] + "',";}
			if(file[i].equals("true")){insert = insert + "1,";}else{insert = insert + "0,";}
			if(context[i]==null){insert = insert + "' ')";}else{insert = insert + "'" +  context[i] + "')";}
			//System.out.println(insert);
			pstmt = con.prepareStatement(insert);
			result = pstmt.executeUpdate();
			
		}else{
			String update = "";
			update = update + "update MAIL_ADMIN set ";
			if(stand[i].equals("0")){ update = update + " MAILSTAND = 0, ";}else{ update = update + " MAILSTAND = 1, "; }
			update = update + " MAILDAY = '" + day[i] + "', ";
			update = update + " MAILCONDITIONTYPE = '" + condition[i] + "', ";
			if(condition_text[i]==null){update = update + " MAILCONDITION = ' ', ";}else{update = update + " MAILCONDITION = '" + condition_text[i] + "', ";}
			if(file[i].equals("true")){update = update + " ATTACHMENTYN = 1,";}else{update = update + " ATTACHMENTYN = 0,";}
			if(context[i]==null){update = update + " MAILCONTENT = ' ' ";}else{update = update + " MAILCONTENT = '" + context[i] + "' ";}
			update = update + "where MAILOBJID = " + row;
			//System.out.println(update);
			pstmt = con.prepareStatement(update);
			result = pstmt.executeUpdate();
		}
	}
	
	// 넘길 데이터를 정리합니다.
	JSONObject jsonObject = new JSONObject();
	try {
		jsonObject.put("result", result);
		out.print(jsonObject.toString());
	} catch (Exception e) {
		  e.printStackTrace();
	}
	
} catch (SQLException e) {
	  e.printStackTrace();
	}finally{
	  try {rs.close();} catch (SQLException e) {e.printStackTrace();}
	  try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
	  try {con.close();} catch (SQLException e) {e.printStackTrace();}
	}
	
	
%>