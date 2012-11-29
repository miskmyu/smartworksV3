<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.PreparedStatement"%>
<%@ page contentType="text/html; charset=euc-kr"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>

<%
	String done = "done.";
	String SQL_DRIVER="com.microsoft.sqlserver.jdbc.SQLServerDriver";
	String URL="jdbc:sqlserver://222.116.101.228:1433;SelectMethod=cursor;DatabaseName=SmartworksV3";
	String USER = "sa";
	String PASS = "admin123!";
	
	try{
		   Class.forName(SQL_DRIVER);
	} catch (ClassNotFoundException e) {
		   e.printStackTrace();
	}
	
	String query = "";
	query = query + "select id, content from swform";

	PreparedStatement pstmt = null;
	Connection con = null;
	ResultSet rs = null;
	ArrayList<String> list = new ArrayList<String>();
	ArrayList<String> listid = new ArrayList<String>();
	try {
		// 커넥션 연결	
		con = DriverManager.getConnection(URL,USER,PASS);
		// 카운트 쿼리 실행 후 COUNT_SUM에 적재
		pstmt = con.prepareStatement(query);
		rs = pstmt.executeQuery();
	
		while(rs.next()){
			listid.add(rs.getString(1));
			list.add(rs.getString(2));
		}
		String replace = "eachTime=\""+"false"+"\"";
		String replaces = "eachTime=\""+"true"+"\"";
		String rsplacess = "(한번만)";
		String rsplacesss = "(매번)";
		String par;
		String temp;
		String cre; 
		String update = "";
		for(int i=0; i<list.size();i++){	
			//Thread.sleep(100);
			temp = list.get(i);
			if(temp == null){}
			else{
				par = temp.replace(replaces,replace);
				cre = par.replace(rsplacesss , rsplacess);
				update = "";
				update = update + " update swform set content=? where id =?";
				System.out.println(i + " " + listid.get(i));
				pstmt = con.prepareStatement(update);
				pstmt.setString(1, cre);
				pstmt.setString(2, listid.get(i));
				
				pstmt.executeUpdate();
			}
			
		}
		
	}catch (SQLException e) {
		  e.printStackTrace();
		}finally{
		  try {rs.close();	} catch (SQLException e) {e.printStackTrace();}
		  try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
		  try {con.close();} catch (SQLException e) {e.printStackTrace();}
	}
	
%>
<%=done%>