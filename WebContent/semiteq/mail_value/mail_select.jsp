<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.SQLException"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="net.smartworks.model.semiteq.Mail_Admin" %>
<%@ page contentType="text/html; charset=utf-8"%>
<%
	String form = request.getParameter("form"); 
	String field = request.getParameter("field"); 

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
		
		String query = "select mailfieldid,mailstand,mailday,mailconditiontype,mailcondition,attachmentyn,mailcontent from mail_admin ";
		query = query + " where 1 = 1 ";
		query = query + " and mailformId = '"+ form +"'";
		query = query + " and mailfieldId = '"+ field +"'";
		   
		pstmt = con.prepareStatement(query);
		rs = pstmt.executeQuery();
		Mail_Admin mail;
		ArrayList<Mail_Admin> list = new ArrayList<Mail_Admin>();
		while(rs.next()){
			mail = new Mail_Admin();
			mail.setMailfieldid(rs.getString(1));
			mail.setMailstand(String.valueOf(rs.getBoolean(2)));
			mail.setMailday(String.valueOf(rs.getInt(3)));
			mail.setMailconditiontype(rs.getString(4));
			mail.setMailcondition(rs.getString(5));
			mail.setAttachmentyn(String.valueOf(rs.getBoolean(6)));
			mail.setMailcontent(rs.getString(7));
			list.add(mail);
			result = 1;
		}
		   
		// 넘길 데이터를 정리합니다.
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("result", result);
			jsonObject.put("list", list);
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
