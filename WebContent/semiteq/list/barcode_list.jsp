<%@page import="net.sf.json.JSONObject"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="net.smartworks.model.semiteq.BarcodeList"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%
String method  = request.getParameter("method");

// 처음 jqgrid 로딩시에는 아무것도 가져오지 못하게 합니다.
if(method.equals("all")){
	return;
}

// 넘어오는 파라미터
String lot_number  = request.getParameter("lot_number");
String pages  = request.getParameter("page");
String rows  = request.getParameter("rows");
String sidx  = request.getParameter("sidx");
String sord  = request.getParameter("sord");

if(sidx.equals("")){
	sidx = "dates";
}
if(sidx.equals("date")){
	sidx = "dates";
}

String device = request.getParameter("device");
String pkg_type = URLDecoder.decode(request.getParameter("pkg_type"), "utf-8");
String spec =  request.getParameter("spec");

String process = URLDecoder.decode(request.getParameter("process"), "utf-8");
String big = URLDecoder.decode(request.getParameter("big"), "utf-8");
String middle = URLDecoder.decode(request.getParameter("middle"), "utf-8");
String small = URLDecoder.decode(request.getParameter("small"), "utf-8");
String title = URLDecoder.decode(request.getParameter("title"), "utf-8");

 String SQL_DRIVER="com.microsoft.sqlserver.jdbc.SQLServerDriver";
 String URL="jdbc:sqlserver://222.116.101.228:1433;SelectMethod=cursor;DatabaseName=SmartWorks";
 String USER = "sa";
 String PASS = "admin123!";
 
 String MES_SQL_DRIVER="oracle.jdbc.driver.OracleDriver";
 String MES_URL="jdbc:oracle:thin:@222.116.101.213:1521:SEMITEQ1";
 String MES_USER = "mighty";
 String MES_PASS = "mighty";
 
try{
	   Class.forName(SQL_DRIVER);
	   Class.forName(MES_SQL_DRIVER);
} catch (ClassNotFoundException e) {
	   e.printStackTrace();
}
PreparedStatement pstmt = null;
PreparedStatement pstmt_count = null;
PreparedStatement mes_pstmt = null;
Connection con = null;
Connection mes_con = null;
ResultSet rs = null;
ResultSet rs_count = null;
ResultSet mes_rs = null;
ArrayList<BarcodeList> list = new ArrayList<BarcodeList>();
String count_sum = "0";
try {
	// 커넥션 연결	
	con = DriverManager.getConnection(URL,USER,PASS);
	mes_con = DriverManager.getConnection(MES_URL,MES_USER,MES_PASS);
	
	int isOut = 0;
	
	ArrayList<String> no = new ArrayList<String>();
	if(device != ""){
		String no_sql = "";
		no_sql = "select std_no from SQ_DVC_SPEC where part_id = '" + device + "'";
		mes_pstmt = mes_con.prepareStatement(no_sql);
		mes_rs = mes_pstmt.executeQuery();
		
		isOut = 1;
		while(mes_rs.next()){
			  no.add(mes_rs.getString(1));
			  isOut = 0;
		}
		
	}
	if(isOut == 1){
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("page", "1");
			jsonObject.put("total", "0"); 
			jsonObject.put("records", "0"); 
			jsonObject.put("rows", list); 
			out.print(jsonObject.toString());
		} catch (Exception e) {
		  e.printStackTrace();
		}
		return;
	}

	// 총 카운트에 대한 쿼리 = query_count
	String query_count =  
			" select count(*) as rows from ("+
			" select          "+
			" c4 as attachment             "+
			" from dt_1330130316125  where 1 = 1";
	
	if(!(process.equals("ALL"))){query_count = query_count + " and c7 = '"+process+"'";}
	if(!(pkg_type.equals("ALL"))){query_count = query_count + " and c66 like '"+pkg_type+"%'";}
	if(!(big.equals("ALL"))){query_count = query_count + " and c86 = '"+big+"'";}
	if(!(middle.equals("ALL"))){query_count = query_count + " and c6 = '"+middle+"'";}
	if(!(small.equals("ALL"))){query_count = query_count + " and c15 like '"+small+"%'";}
	if(!(title.equals(""))){query_count = query_count + " and c2 LIKE '%"+title+"%'";}
	
	if(spec != ""){
		query_count = query_count + " and c0 = '"+spec+"' ";
	}else{
		if(no.size()>0){
			query_count = query_count + " and c0 In (";
			for(int mes_i=0;mes_i<no.size();mes_i++){
				if(mes_i == no.size()-1){
					query_count = query_count + "'" + no.get(mes_i)  + "'";
				}else{
					query_count = query_count + "'" + no.get(mes_i)  + "',";
				}
			}
			query_count = query_count + ")";
		}
	}
	
	
	
	query_count = query_count + " union all                     "+
			" select                        "+
			" c16 as attachment            "+
			" from dt_1338344682619   where 1 = 1  ";
			

	if(!(process.equals("ALL"))){query_count = query_count + " and c11 = '"+process+"'";}
	if(!(pkg_type.equals("ALL"))){query_count = query_count + " and c13 like '"+pkg_type+"%'";}
	if(!(big.equals("ALL"))){query_count = query_count + " and c8 = '"+big+"'";}
	if(!(middle.equals("ALL"))){query_count = query_count + " and c9 = '"+middle+"'";}
	if(!(small.equals("ALL"))){query_count = query_count + " and c10 like '"+small+"%'";}
	if(!(title.equals(""))){query_count = query_count + " and c0 LIKE '%"+title+"%'";}			
	if(spec != ""){
		query_count = query_count + " and c1 = '"+spec+"' ";
	}else{
		if(no.size()>0){
			query_count = query_count + " and c1 In (";
			for(int mes_i=0;mes_i<no.size();mes_i++){
				if(mes_i == no.size()-1){
					query_count = query_count + "'" + no.get(mes_i)  + "'";
				}else{
					query_count = query_count + "'" + no.get(mes_i)  + "',";
				}
			}
			query_count = query_count + ")";
		}
	}
	
	query_count = query_count + ")testss ";
	
	// 일반 리스트 쿼리
	String query =  " WITH TEMPTABLE AS (   		"+
					" SELECT * FROM 				"+
					" (								"+
					" select                        "+
					" c4 as attachment,             "+
					" c71 as device,   				"+
					" c19 as romcode,           	"+
					" c7 as process,                "+
					" c67 as customer,              "+
					" c0 as standard_number,        "+
					" c1 as revision_number,        "+
					" c2 as standard_title,         "+
					" c5 as repoter,                "+
					" convert(char, c9)as dates,    "+
					" id as id,                   	"+
					" 'pkg_2bd9f4e8e36045269503f55b9cca1f64' as domainid,          "+
					" 'dt_1330130316125' as recodeId,          "+
					" creator as creator          "+
					" from dt_1330130316125  where 1 = 1";
	

	if(!(process.equals("ALL"))){	query = query + " and c7 = '"+process+"'";}
	if(!(pkg_type.equals("ALL"))){query = query + " and c66 = '"+pkg_type+"'";}
	if(!(big.equals("ALL"))){	query = query + " and c86 = '"+big+"'";}
	if(!(middle.equals("ALL"))){	query = query + " and c6 = '"+middle+"'";}
	if(!(small.equals("ALL"))){	query = query + " and c15 like '"+small+"%'";}
	if(!(title.equals(""))){	query = query + " and c2 LIKE '%"+title+"%'";}
	if(spec != ""){
		query = query + " and c0 = '"+spec+"' ";
	}else{
		if(no.size()>0){
			query = query + " and c0 In (";
			for(int mes_i=0;mes_i<no.size();mes_i++){
				if(mes_i == no.size()-1){
					query = query + "'" + no.get(mes_i)  + "'";
				}else{
					query = query + "'" + no.get(mes_i)  + "',";
				}
			}
			query = query + ")";
		}
	}
	query = query + " union all                     "+
					" select                        "+
					" c16 as attachment,            "+
					" c12 as device,   				"+
					" c18 as romcode,         		"+
					" c11 as process,               "+
					" c14 as customer,              "+
					" c1 as standard_number,        "+
					" c2 as revision_number,        "+
					" c0 as standard_title,         "+
					" c4 as repoter,                "+
					" convert(char, c15) as dates,  "+
					" id as id,                   	"+
					" 'pkg_67bbf2c4a8ab4c9ab671fa2dcebccd5e' as domainid,          "+	
					" 'dt_1338344682619' as recodeId,          "+	
					" creator as creator          "+
					" from dt_1338344682619   where 1 = 1  ";
					
	if(!(process.equals("ALL"))){	query = query + " and c11 = '"+process+"'"; 	}
	if(!(pkg_type.equals("ALL"))){	query = query + " and c13 = '"+pkg_type+"'";}
	if(!(big.equals("ALL")))	{	query = query + " and c8 = '"+big+"'";			}
	if(!(middle.equals("ALL"))) {	query = query + " and c9 = '"+middle+"'";		}
	if(!(small.equals("ALL")))	{	query = query + " and c10 like'"+small+"%'";		}
	if(!(title.equals("")))		{	query = query + " and c0 LIKE '%"+title+"%'";	}			
	if(spec != ""){
		query = query + " and c1 = '"+spec+"' ";
	}else{
		if(no.size()>0){
			query = query + " and c1 In (";
			for(int mes_i=0;mes_i<no.size();mes_i++){
				if(mes_i == no.size()-1){
					query = query + "'" + no.get(mes_i)  + "'";
				}else{
					query = query + "'" + no.get(mes_i)  + "',";
				}
			}
			query = query + ")";
		}
	}
	
	// 카운트 쿼리 실행 후 COUNT_SUM에 적재
	pstmt_count = con.prepareStatement(query_count);
	rs_count = pstmt_count.executeQuery();
	if(rs_count.next()){	count_sum = rs_count.getString(1);	}
	
	// 페이징 처리-----------------------------------------------------------
	int table_paging = Integer.parseInt(rows) * Integer.parseInt(pages);
	int view_first = 0;
	if(pages.equals("0")){	view_first = 0;
	}else{	view_first = (Integer.parseInt(pages) - 1) * Integer.parseInt(rows);	}
	if(Integer.parseInt(count_sum) < table_paging){	table_paging = Integer.parseInt(count_sum) - view_first; }
	query = query + " )tests )";
	query = query + " select TOP "+rows+" * from TEMPTABLE WHERE  attachment NOT IN(SELECT TOP "+view_first+" attachment FROM TEMPTABLE) ";
	// ----------------------------------------------------------------------
	// 리스트 쿼리 실행
	pstmt = con.prepareStatement(query);
	rs = pstmt.executeQuery();
	BarcodeList beans;
	int check = 0;
		while(rs.next()){
			beans = new BarcodeList();
			beans.setAttachment(rs.getString(1));	
			
			String temp = rs.getString(2);
			if(temp == null){
				beans.setDevice("");
			}else{
				String temp2 = temp.toUpperCase();
				if( temp2.indexOf("ALL") > -1  ) {
					beans.setDevice("All");	
				}else{
					beans.setDevice(rs.getString(2));	
				}
			}
			
		
		beans.setRomcode(rs.getString(3));					
		beans.setProcess(rs.getString(4));				
		String sql = "select syscode_name , syscode_desc from syscodedata where plant = 'SEMITEQ' and systable_name = 'CUSTOMER' and syscode_name = ?";
		mes_pstmt = mes_con.prepareStatement(sql);
		mes_pstmt.setString(1,rs.getString(5));
		mes_rs = mes_pstmt.executeQuery();
		
		if(mes_rs.next()){
			beans.setCustomer(mes_rs.getString(2));	
		}else{
			String mes_temp = rs.getString(5);
			
			if(mes_temp == null){
				beans.setCustomer("");
			}else{
				String mes_temp2 = mes_temp.toUpperCase();
				
				if( mes_temp2.indexOf("ALL") > -1  ) {
					beans.setCustomer("All");	
				}else{
					beans.setCustomer(rs.getString(5));
				}
			}
			
			
		}
		beans.setStandard_number(rs.getString(6));
		beans.setRevision_number(rs.getString(7));
		beans.setStandard_title(rs.getString(8));	
		beans.setRepoter(rs.getString(9));	
		String date = rs.getString(10);
		
		if(date == null){
			beans.setDate("");
		}else{
			if("-".equals(date.substring(4,5))){
				String dates = date.substring(0,4) + "년 " + date.substring(5,7) + "월 ";
				int day = Integer.parseInt(date.substring(9,11).trim());
				dates = dates + "0" + String.valueOf(day) + "일";
				beans.setDate(dates);
			}else{
				String dates = date.substring(6,10) + "년 " + date.substring(0,2) + "월 ";
				int day = Integer.parseInt(date.substring(3,5).trim());
				if(day < 10){
					dates = dates + "0" + String.valueOf(day) + "일";
				}else{
					dates = dates + String.valueOf(day)+ "일";
				}
				beans.setDate(dates);
			}				
		}
		
		beans.setId(rs.getString(11));				
		beans.setDomainId(rs.getString(12));						
		beans.setRecodeId(rs.getString(13));
		beans.setPakageId(rs.getString(14));
		list.add(beans);
		check = check + 1;
	}
	if(check == 0){
		
	}else{
		
		
		String fileQuery = "";
		String countQuery = "";
		for(int i = 0 ; i< list.size();i++){
			fileQuery = "SELECT D.groupId,F.id,F.type,F.fileName FROM SWDocGroup D, SWFile F WHERE D.docId = F.id and D.groupId ='"+list.get(i).getAttachment()+"'";
			countQuery = "SELECT COUNT(*) FROM SWDocGroup D, SWFile F WHERE D.docId = F.id and D.groupId ='"+list.get(i).getAttachment()+"'";
			
			pstmt = con.prepareStatement(countQuery);
			rs = pstmt.executeQuery();
			int count = 0;
			if(rs.next()){
				count = rs.getInt(1);
			}
			
			pstmt = con.prepareStatement(fileQuery);
			
			rs = pstmt.executeQuery();
			String fileId = "";
			String fileType = "";
			String fileName = "";
			int loop = 0;
			while(rs.next()){
				if(count-1 == loop){
					fileId = fileId + rs.getString(2);
					fileType = fileType + rs.getString(3);
					fileName = fileName + rs.getString(4);
				}else{
					fileId = fileId +  rs.getString(2) + ",";
					fileType = fileType + rs.getString(3) + "," ;
					fileName = fileName  + rs.getString(4) + ",";
				}
				BarcodeList code = (BarcodeList)list.get(i);
				code.setFileId(fileId);
				code.setFileName(fileName);
				code.setFileType(fileType);
				list.set(i, code);
				loop = loop + 1;
			}
		}
	}
} catch (SQLException e) {
	  e.printStackTrace();
	}finally{
	  try { 
		if(rs == null){}else{
			  rs.close();
		}
	  	if(mes_rs == null){}else{
	  		mes_rs.close();
	  	}
	  	if(rs_count == null){}else{
	  		rs_count.close();
		}
	  } catch (SQLException e) {e.printStackTrace();}
	  try {
		  	if(pstmt == null){}else{
				pstmt.close();
		  	}
			if(mes_pstmt == null){}else{
				mes_pstmt.close();
		  	}
			if(pstmt_count == null){}else{
				pstmt_count.close();
		  	}
		} catch (SQLException e) {e.printStackTrace();}
	  try {
		  con.close(); 
		  mes_con.close();} catch (SQLException e) {e.printStackTrace();}
	}
// 넘길 데이터를 정리합니다.
JSONObject jsonObject = new JSONObject();
try {
	jsonObject.put("page", pages);
	jsonObject.put("total", (Integer.parseInt(count_sum) / Integer.parseInt(rows))+1); 
	jsonObject.put("records", count_sum); 
	jsonObject.put("rows", list); 
	out.print(jsonObject.toString());
} catch (Exception e) {
  e.printStackTrace();
}
%>
