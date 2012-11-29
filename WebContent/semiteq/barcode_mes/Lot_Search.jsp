<%@page import="java.util.ArrayList"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.PreparedStatement"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%
 String lot_code = request.getParameter("lotcode");
 //	System.out.println(lot_code);
 //ORACLE
 String SQL_DRIVER="oracle.jdbc.driver.OracleDriver";
 String URL="jdbc:oracle:thin:@222.116.101.213:1521:SEMITEQ1";
 String USER = "rptmit";
 String PASS = "rptmit";
 
 String device = "";
 String pkg_type = "";
 String customer = "";
 String body_size = "";
 String part_id = "";
 String operation = "";
 try{
	   Class.forName(SQL_DRIVER);
  } catch (ClassNotFoundException e) {
	   e.printStackTrace();
  }
 
 String sql = "SELECT A.PKG_TYPE, A.CUSTOMER, A.BODY_SIZE,A.PART_ID AS DEVICE, B.OPERATION FROM PARTSPEC A, LOTSTS B WHERE A.PLANT = 'SEMITEQ' AND A.PLANT = B.PLANT AND B.PART = A.PART_ID AND B.LOT_NUMBER = ?";
 PreparedStatement pstmt = null;
 
 Connection con = null;
 ResultSet rs = null;
 try {
 con = DriverManager.getConnection(URL,USER,PASS);
 pstmt = con.prepareStatement(sql);
 pstmt.setString(1, lot_code);
  rs = pstmt.executeQuery();
  if(rs.next()){
   pkg_type = rs.getString(1);
   customer = rs.getString(2);
   body_size = rs.getString(3);
   device = rs.getString(4);
   operation = rs.getString(5);
  }     
  
  
} catch (SQLException e) {
  e.printStackTrace();
}finally{
  try {rs.close();} catch (SQLException e) {e.printStackTrace();}
  try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
  try {con.close();} catch (SQLException e) {e.printStackTrace();}
}
 if(device.equals("")){
	 device = "error";
 }
 JSONObject jsonObject = new JSONObject();

 try {
     jsonObject.put("device", device);
     jsonObject.put("pkg_type", pkg_type);
     jsonObject.put("customer", customer);
     jsonObject.put("body_size", body_size);
     jsonObject.put("operation", operation);
     
	 out.print(jsonObject.toString());
	} catch (Exception e) {
	  e.printStackTrace();
	}
%>