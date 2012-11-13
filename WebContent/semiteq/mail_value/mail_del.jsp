<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="java.net.URLDecoder"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%
	// 정보관리 업무, 필드가져오기
	String form = request.getParameter("form"); 
	String field = request.getParameter("field");
	// 기준가져오기, 전송일 가져오기, 조건 가져오기, 조건값 가져오기, 첨부파일 확인 가져오기, 메일편집 가져오기
	String stand = request.getParameter("stand"); 
	String day = request.getParameter("day");
	String condition = URLDecoder.decode(request.getParameter("condition"), "utf-8");
	String condition_text = URLDecoder.decode(request.getParameter("condition_text"), "utf-8");
	String file = request.getParameter("file");
	String context = URLDecoder.decode(request.getParameter("context"), "utf-8");
	// DB 연결하기
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
	try{
		con = DriverManager.getConnection(URL,USER,PASS);
		// 삭제하기
		String delete = "delete from MAIL_ADMIN WHERE 1=1";
		delete = delete + " and MAILFORMID = '"+ form +"' ";
		delete = delete + " and MAILFIELDID = '"+ field +"' ";
		if(stand.equals("0")){		delete = delete + " and MAILSTAND =0 ";}else{ delete = delete + " and MAILSTAND =1 "; }
		delete = delete + " and MAILDAY = '"+ day +"' ";
		delete = delete + " and MAILCONDITIONTYPE = '"+ condition +"' ";
		delete = delete + " and MAILCONDITION = '"+ condition_text +"' ";
		if(file.equals("true")){	delete = delete + " and ATTACHMENTYN =1 ";}else{delete = delete + " and ATTACHMENTYN = 0 ;";}
		// delete = delete + " and MAILCONTENT = '"+ context +"' ";
		//System.out.println(delete);
		pstmt = con.prepareStatement(delete);
		result = pstmt.executeUpdate();
		// JSON 전달
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
		  try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
		  try {con.close();} catch (SQLException e) {e.printStackTrace();}
	}
%>