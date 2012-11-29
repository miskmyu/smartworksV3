<%@page import="java.util.ArrayList"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.PreparedStatement"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%
 //	System.out.println(lot_code);
 //ORACLE
 String SQL_DRIVER="oracle.jdbc.driver.OracleDriver";
 String URL="jdbc:oracle:thin:@222.116.101.213:1521:SEMITEQ1";
 String USER = "rptmit";
 String PASS = "rptmit";
 
 try{
	   Class.forName(SQL_DRIVER);
  } catch (ClassNotFoundException e) {
	   e.printStackTrace();
  }
 String sql = "select a.syscode_name from syscodedata a, partspec b where a.plant = b.plant and a.plant = 'SEMITEQ' and a.syscode_name = b.pkg_type  and a.systable_name like 'PKG_TYPE%' group by a.syscode_name order by a.syscode_name";
 PreparedStatement pstmt = null;
 ArrayList<String> list = new ArrayList<String>();
 Connection con = null;
 ResultSet rs = null;
 try {
 con = DriverManager.getConnection(URL,USER,PASS);
 pstmt = con.prepareStatement(sql);
 rs = pstmt.executeQuery();
  while(rs.next()){
     	list.add(rs.getString(1));
  }     
} catch (SQLException e) {
  e.printStackTrace();
}finally{
  try {rs.close();} catch (SQLException e) {e.printStackTrace();}
  try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
  try {con.close();} catch (SQLException e) {e.printStackTrace();}
}
 JSONObject jsonObject = new JSONObject();

	try {
     jsonObject.put("list", list);
     
	 out.print(jsonObject.toString());
	} catch (Exception e) {
	  e.printStackTrace();
	}
%>