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
String rom_code = request.getParameter("rom_code");
String process = URLDecoder.decode(request.getParameter("process"), "utf-8");
String big = URLDecoder.decode(request.getParameter("big"), "utf-8");
String middle = URLDecoder.decode(request.getParameter("middle"), "utf-8");
String small = URLDecoder.decode(request.getParameter("small"), "utf-8");
String title = URLDecoder.decode(request.getParameter("title"), "utf-8");

// SQL 정보입니다. LOCAL
// String SQL_DRIVER="com.microsoft.sqlserver.jdbc.SQLServerDriver";
// String URL="jdbc:sqlserver://192.168.50.126:1434;SelectMethod=cursor;DatabaseName=Dev_Test_SemiteqV3;";
// String USER = "sa";
// String PASS = "dkagh1";

// String SQL_DRIVER="com.microsoft.sqlserver.jdbc.SQLServerDriver";
// String URL="jdbc:sqlserver://192.168.50.126:1434;SelectMethod=cursor;DatabaseName=smartworks;";
// String USER = "admin123!";
// String PASS = "admin123!";

 String SQL_DRIVER="com.microsoft.sqlserver.jdbc.SQLServerDriver";
 String URL="jdbc:sqlserver://222.116.101.228:1433;SelectMethod=cursor;DatabaseName=SmartWorks";
 String USER = "sa";
 String PASS = "admin123!";

// 총 카운트에 대한 쿼리 = query_count
String query_count =  
		" select count(*) as rows from ("+
		" select          "+
		" c4 as attachment             "+
		" from dt_1330130316125  where 1 = 1";

if(!(lot_number.equals(""))){}
if(!(device.equals(""))){query_count = query_count + " and c71 = '"+device+"'";}
// if(!(rom_code.equals(""))){query_count = query_count + " and c91 = '"+rom_code+"'";}
if(!(process.equals("ALL"))){query_count = query_count + " and c7 = '"+process+"'";}
if(!(big.equals("ALL"))){query_count = query_count + " and c86 = '"+big+"'";}
if(!(middle.equals("ALL"))){query_count = query_count + " and c6 = '"+middle+"'";}
if(!(small.equals("ALL"))){query_count = query_count + " and c15 like '"+small+"%'";}
if(!(title.equals(""))){query_count = query_count + " and c2 LIKE '%"+title+"%'";}

query_count = query_count + " union all                     "+
		" select                        "+
		" c16 as attachment            "+
		" from dt_1338344682619   where 1 = 1  ";
		
if(!(lot_number.equals(""))){}
if(!(device.equals(""))){query_count = query_count + " and c12 = '"+device+"'";}
// if(!(rom_code.equals(""))){query_count = query_count + " and c18 = '"+rom_code+"'";}
if(!(process.equals("ALL"))){query_count = query_count + " and c11 = '"+process+"'";}
if(!(big.equals("ALL"))){query_count = query_count + " and c8 = '"+big+"'";}
if(!(middle.equals("ALL"))){query_count = query_count + " and c9 = '"+middle+"'";}
if(!(small.equals("ALL"))){query_count = query_count + " and c10 like '"+small+"%'";}
if(!(title.equals(""))){query_count = query_count + " and c0 LIKE '%"+title+"%'";}			

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
				" 'dt_1330130316125' as recodeId          "+
				" from dt_1330130316125  where 1 = 1";

if(!(lot_number.equals(""))){	}
if(!(device.equals(""))){query = query + " and c71 = '"+device+"'";}
 if(!(rom_code.equals(""))){query = query + " and c91 = '"+rom_code+"'";}
if(!(process.equals("ALL"))){	query = query + " and c7 = '"+process+"'";}
if(!(big.equals("ALL"))){	query = query + " and c86 = '"+big+"'";}
if(!(middle.equals("ALL"))){	query = query + " and c6 = '"+middle+"'";}
if(!(small.equals("ALL"))){	query = query + " and c15 like '"+small+"%'";}
if(!(title.equals(""))){	query = query + " and c2 LIKE '%"+title+"%'";}
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
				" 'dt_1338344682619' as recodeId          "+	
				" from dt_1338344682619   where 1 = 1  ";
				
if(!(lot_number.equals(""))){													}
if(!(device.equals("")))	{	query = query + " and c12 = '"+device+"'";												}
 if(!(rom_code.equals("")))	{	query = query + " and c18 = '"+rom_code+"'";												}
if(!(process.equals("ALL"))){	query = query + " and c11 = '"+process+"'"; 	}
if(!(big.equals("ALL")))	{	query = query + " and c8 = '"+big+"'";			}
if(!(middle.equals("ALL"))) {	query = query + " and c9 = '"+middle+"'";		}
if(!(small.equals("ALL")))	{	query = query + " and c10 like'"+small+"%'";		}
if(!(title.equals("")))		{	query = query + " and c0 LIKE '%"+title+"%'";	}			


try{
	   Class.forName(SQL_DRIVER);
} catch (ClassNotFoundException e) {
	   e.printStackTrace();
}

PreparedStatement pstmt = null;
PreparedStatement pstmt_count = null;

Connection con = null;

ResultSet rs = null;
ResultSet rs_count = null;

ArrayList<BarcodeList> list = new ArrayList<BarcodeList>();
String count_sum = "0";
try {
// 커넥션 연결	
con = DriverManager.getConnection(URL,USER,PASS);

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
		beans.setDevice(rs.getString(2));			
		beans.setRomcode(rs.getString(3));					
		beans.setProcess(rs.getString(4));				
		beans.setCustomer(rs.getString(5));				
		beans.setStandard_number(rs.getString(6));
		beans.setRevision_number(rs.getString(7));
		beans.setStandard_title(rs.getString(8));	
		beans.setRepoter(rs.getString(9));	
		String date = rs.getString(10);
		if("-".equals(date.substring(4,5))){
			String dates = date.substring(0,4) + "년" + date.substring(5,7) + "월";
			int day = Integer.parseInt(date.substring(9,11).trim());
			dates = dates + "0" + String.valueOf(day) + "일";
			beans.setDate(dates);
		}else{
			String dates = date.substring(6,10) + "년" + date.substring(0,2) + "월";
			int day = Integer.parseInt(date.substring(3,5).trim());
			if(day < 10){
				dates = dates + "0" + String.valueOf(day) + "일";
			}else{
				dates = dates + String.valueOf(day)+ "일";
			}
			beans.setDate(dates);
		}				
		beans.setId(rs.getString(11));				
		beans.setDomainId(rs.getString(12));						
		beans.setRecodeId(rs.getString(13));
		list.add(beans);
		check = check + 1;
	}
	if(check == 0){
		
	}else{
		// 파일 쿼리를 만듭니다.
		String fileQuery = "SELECT D.groupId,F.id,F.type,F.fileName FROM SWDocGroup D, SWFile F WHERE D.docId = F.id and D.groupId IN (";
		for(int i=0;i<list.size();i++){
			
			if(i==list.size()-1){
				fileQuery = fileQuery + "'" + list.get(i).getAttachment() + "')";
			}else{
				fileQuery = fileQuery + "'" + list.get(i).getAttachment() + "',";
			}
		}
		pstmt = con.prepareStatement(fileQuery);
		rs = pstmt.executeQuery();
		String fileTemp = "";
		int i = 0;
		String fileId = "";
		String fileType = "";
		String fileName = "";
		
		while(rs.next()){
			
			if(!(fileTemp.equals(rs.getString(1)))){	// 새로운 그룹이면
				fileId = fileId + rs.getString(2);
				fileType = fileType + rs.getString(3);
				fileName = fileName + rs.getString(4);
			}else{										//  기존 그룹이면
				fileId = fileId +  rs.getString(2) + ",";
				fileType = fileType + rs.getString(3) + "," ;
				fileName = fileName  + rs.getString(4) + ",";
			}
			
			if(!(fileTemp.equals(rs.getString(1)))){
				BarcodeList code = (BarcodeList)list.get(i);
				code.setFileId(fileId);
				code.setFileName(fileName);
				code.setFileType(fileType);
				list.set(i, code);
				i++;
				fileId = "";
				fileType = "";
				fileName = "";
			}
			fileTemp = rs.getString(1);
		}
	}
} catch (SQLException e) {
	  e.printStackTrace();
	}finally{
	  try {rs.close();	rs_count.close();} catch (SQLException e) {e.printStackTrace();}
	  try {pstmt.close(); pstmt_count.close();} catch (SQLException e) {e.printStackTrace();}
	  try {con.close();} catch (SQLException e) {e.printStackTrace();}
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
