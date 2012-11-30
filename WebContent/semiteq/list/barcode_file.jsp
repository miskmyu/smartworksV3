<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.PreparedStatement"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%
	String files = request.getParameter("files");

	String query = "";

	 String SQL_DRIVER="com.microsoft.sqlserver.jdbc.SQLServerDriver";
	 String URL="jdbc:sqlserver://222.116.101.228:1433;SelectMethod=cursor;DatabaseName=SmartWorks_V3";
	 String USER = "sa";
	 String PASS = "admin123!";


	try{
		   Class.forName(SQL_DRIVER);
	} catch (ClassNotFoundException e) {
		   e.printStackTrace();
	}

	PreparedStatement pstmt = null;
	Connection con = null;

	ResultSet rs = null;
	
	try {
		// 커넥션 연결	
		con = DriverManager.getConnection(URL,USER,PASS);

		// 카운트 쿼리 실행 후 COUNT_SUM에 적재
		pstmt = con.prepareStatement(query);
		rs = pstmt.executeQuery();
		if(rs.next()){	
			//count_sum = rs.getString(1);	
			
		}
	} catch (SQLException e) {
		  e.printStackTrace();
		}finally{
		  try {rs.close();} catch (SQLException e) {e.printStackTrace();}
		  try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
		  try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
%>
