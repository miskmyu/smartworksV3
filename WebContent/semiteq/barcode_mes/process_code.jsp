<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.PreparedStatement"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%
 	//String lot_code = request.getParameter("lotcode");
	//System.out.println(lot_code);
 //ORACLE
 String SQL_DRIVER="oracle.jdbc.driver.OracleDriver";
 String URL="jdbc:oracle:thin:@222.116.101.213:1521:SEMITEQ1";
 String USER = "rptmit";
 String PASS = "rptmit";
 List name = new ArrayList();
 try{
	   Class.forName(SQL_DRIVER);
  } catch (ClassNotFoundException e) {
	   e.printStackTrace();
  }
 
 String sql = "SELECT SHORT_DESC FROM LOTSTS A, OPERATION B WHERE A.PLANT = 'SEMITEQ' AND A.PLANT = B.PLANT";
 PreparedStatement pstmt = null;
 
 Connection con = null;
 ResultSet rs = null;
 try {
 con = DriverManager.getConnection(URL,USER,PASS);
 pstmt = con.prepareStatement(sql);
  rs = pstmt.executeQuery();
  
  while(rs.next()){
      name.add(rs.getString(1));
      System.out.println(rs.getString(1));
  }
  
} catch (SQLException e) {
  e.printStackTrace();
}finally{
  try {rs.close();} catch (SQLException e) {e.printStackTrace();}
  try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
  try {con.close();} catch (SQLException e) {e.printStackTrace();}
}

//  JSONObject jsonObject = new JSONObject();
//  System.out.println(name);
// 	try {
//      jsonObject.put("device", name);
// 	    out.print(jsonObject.toString());
// 	} catch (Exception e) {
// 	  e.printStackTrace();
// 	}
%>