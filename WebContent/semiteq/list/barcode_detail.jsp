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

// 넘어오는 파라미터
String table = request.getParameter("table"); // 테이블 
String id = request.getParameter("id");  // 레코드

String SQL_DRIVER="com.microsoft.sqlserver.jdbc.SQLServerDriver";
String URL="jdbc:sqlserver://222.116.101.228:1433;SelectMethod=cursor;DatabaseName=SmartWorks_V3";
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
Connection con = null;
ResultSet rs = null;

PreparedStatement mes_pstmt = null;
Connection mes_con = null;
ResultSet mes_rs = null;

try {
// 커넥션 연결	
con = DriverManager.getConnection(URL,USER,PASS);
mes_con = DriverManager.getConnection(MES_URL,MES_USER,MES_PASS);
String query = "select ";

if(table.equals("dt_1330130316125")){		// 표준문서
	query = query + "c31,c86,convert(char, c78) as c78,c2 ,c37,c33,c71,c67,c7 ,c39,c8 ,c4 , ";
	query = query + "c36,c40,c1 ,c64,c21,c30,c0 ,c66,c62,c15,c22,c6 , ";
	query = query + "c34,c28,c27,c25,c24,c5 ,convert(char, c9) as c9 ,c83 ";
	query = query + "from dt_1330130316125 where id = '" + id + "'";
	pstmt = con.prepareStatement(query);
	rs = pstmt.executeQuery();
	if(rs.next()){
		 String r1 = rs.getString(1);  // 생산기술2팀 배포부수 
		 String r2 = rs.getString(2);  // 표준 대분류          
		 String r3 = rs.getString(3);  // 유효기간             
		 String r4 = rs.getString(4);  // 표준제목             
		 String r5 = rs.getString(5);  // 품질보증팀 배포부수  
		 String r6 = rs.getString(6);  // 품질관리팀 배포공정  
		 String r7 = rs.getString(7);  // 적용 제품     
		 String sql = "select syscode_name , syscode_desc from syscodedata where plant = 'SEMITEQ' and systable_name = 'CUSTOMER' and syscode_name = ?";
		 mes_pstmt = mes_con.prepareStatement(sql);
		 mes_pstmt.setString(1,rs.getString(8));		
		 mes_rs = mes_pstmt.executeQuery();
		 String r8;
		 if(mes_rs.next()){
	 		 r8 = mes_rs.getString(2);	
		 }else{
	 		 r8 = rs.getString(8);  // 적용 고객사  
		 }
		 String r9 = rs.getString(9);  // 공정                 
		 String r10 = rs.getString(10);  // Packing 배포요청부수
		 String r11 = rs.getString(11);  // 기안부서            
		 String r12 = rs.getString(12);  // 첨부파일            
		 String r13 = rs.getString(13);  // 품질보증팀 배포공정 
		 String r14 = rs.getString(14);  // STOCK 배포요청부수  
		 String r15 = rs.getString(15);  // 개정번호            
		 String r16 = rs.getString(16);  // 제/개정(폐기)사유   
		 String r17 = rs.getString(17);  // 생산1부 배포공정    
		 String r18 = rs.getString(18);  // 생산기술2팀 배포공정
		 String r19 = rs.getString(19);  // 표준번호            
		 String r20 = rs.getString(20);  // 적용 PKG TYPE       
		 String r21 = rs.getString(21);  // TEST생산 배포요청부?
		 String r22 = rs.getString(22);  // 표준 소분류         
		 String r23 = rs.getString(23);  // 생산1부 배포부수    
		 String r24 = rs.getString(24);  // 표준 중분류         
		 String r25 = rs.getString(25);  // 품질관리팀 배포부수 
		 String r26 = rs.getString(26);  // 생산2부 배포부수    
		 String r27 = rs.getString(27);  // 생산2부 배포공정    
		 String r28 = rs.getString(28);  // 생산기술1팀 배포부수
		 String r29 = rs.getString(29);  // 생산기술1팀 배포공정
		 String r30 = rs.getString(30);  // 기안자              
		 String r31 = rs.getString(31);  // 등록일자            
		 int 	r32 = rs.getInt(32);  // ECN 배포요청부수    
		 
		 if(r3 == null){
			 
		 }else{
			 if("-".equals(r3.substring(4,5))){
					String dates = r3.substring(0,4) + "년 " + r3.substring(5,7) + "월 ";
					int day = Integer.parseInt(r3.substring(9,11).trim());
					dates = dates + "0" + String.valueOf(day) + "일";
					r3 = dates;
				}else{
					String dates = r3.substring(6,10) + "년 " + r3.substring(0,2) + "월 ";
					int day = Integer.parseInt(r3.substring(3,5).trim());
					if(day < 10){
						dates = dates + "0" + String.valueOf(day) + "일";
					}else{
						dates = dates + String.valueOf(day)+ "일";
					}
					r3 = dates;
			  }	
		 }
		if(r31 == null){
			 
		 }else{
			 
			 if("-".equals(r31.substring(4,5))){
					String dates = r31.substring(0,4) + "년 " + r31.substring(5,7) + "월 ";
					int day = Integer.parseInt(r31.substring(9,11).trim());
					dates = dates + "0" + String.valueOf(day) + "일";
					r3 = dates;
				}else{
					String dates = r31.substring(6,10) + "년 " + r31.substring(0,2) + "월 ";
					int day = Integer.parseInt(r31.substring(3,5).trim());
					if(day < 10){
						dates = dates + "0" + String.valueOf(day) + "일";
					}else{
						dates = dates + String.valueOf(day)+ "일";
					}
					r31 = dates;
			  }	
		 }
		 JSONObject jsonObject = new JSONObject();
			try {
				 jsonObject.put("r1" ,  r1 );  
				 jsonObject.put("r2" ,  r2 );  
				 jsonObject.put("r3" ,  r3 );  
				 jsonObject.put("r4" ,  r4 );  
				 jsonObject.put("r5" ,  r5 );  
				 jsonObject.put("r6" ,  r6 );  
				 jsonObject.put("r7" ,  r7 );  
				 jsonObject.put("r8" ,  r8 );  
				 jsonObject.put("r9" ,  r9 );  
				 jsonObject.put("r10",  r10);  
				 jsonObject.put("r11",  r11);  
				 jsonObject.put("r12",  r12);  
				 jsonObject.put("r13",  r13);  
				 jsonObject.put("r14",  r14);  
				 jsonObject.put("r15",  r15);  
				 jsonObject.put("r16",  r16);  
				 jsonObject.put("r17",  r17);  
				 jsonObject.put("r18",  r18);  
				 jsonObject.put("r19",  r19);  
				 jsonObject.put("r20",  r20);  
				 jsonObject.put("r21",  r21);  
				 jsonObject.put("r22",  r22);  
				 jsonObject.put("r23",  r23);  
				 jsonObject.put("r24",  r24);  
				 jsonObject.put("r25",  r25);  
				 jsonObject.put("r26",  r26);  
				 jsonObject.put("r27",  r27);  
				 jsonObject.put("r28",  r28);  
				 jsonObject.put("r29",  r29);  
				 jsonObject.put("r30",  r30);  
				 jsonObject.put("r31",  r31);  
				 jsonObject.put("r32",  r32);  
				 out.print(jsonObject.toString());
			} catch (Exception e) {
			  e.printStackTrace();
			}
	}
	
}else{		// 임시 스펙
	query = query + "c7 ,c14,c15,c17,c2 ,c6 ,c1 ,c9 ,c16,c12, ";
	query = query + "c5 ,c0 ,c13,c3 ,c11,c10,c8 ,c4  ";
	query = query + "from dt_1338344682619 where id = '"+ id + "'";
	pstmt = con.prepareStatement(query);
	rs = pstmt.executeQuery();
	if(rs.next()){
		String r1	 = rs.getString(1);		//ECN 배포부수    
		String sql = "select syscode_name , syscode_desc from syscodedata where plant = 'SEMITEQ' and systable_name = 'CUSTOMER' and syscode_name = ?";
		 mes_pstmt = mes_con.prepareStatement(sql);
		 mes_pstmt.setString(1,rs.getString(2));		
		 mes_rs = mes_pstmt.executeQuery();
		 String r2;
		 if(mes_rs.next()){
	 		 r2 = mes_rs.getString(2);	
		 }else{
	 		 r2 = rs.getString(2);  // 적용 고객사  
		 }
		String r3	 = rs.getString(3);		//등록일자                
		String r4	 = rs.getString(4);		//TEST사업부 ECN 배포부수 
		String r5	 = rs.getString(5);		//개정번호                
		String r6	 = rs.getString(6);		//변경사유                
		String r7	 = rs.getString(7);		//표준번호                
		String r8	 = rs.getString(8);		//표준 중분류             
		String r9	 = rs.getString(9);		//첨부파일                
		String r10 = rs.getString(10);		//적용 제품               
		String r11 = rs.getString(11);		//유효기간                
		String r12 = rs.getString(12);		//표준제목                
		String r13 = rs.getString(13);		//적용 PKG TYPE           
		String r14 = rs.getString(14);		//기안부서          
		
		// 넘길 데이터를 정리합니다.
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("r1",	r1	);
			jsonObject.put("r2",	r2	);
			jsonObject.put("r3",	r3	);
			jsonObject.put("r4",	r4	);
			jsonObject.put("r5",	r5	);
			jsonObject.put("r6",	r6	);
			jsonObject.put("r7",	r7	);
			jsonObject.put("r8",	r8	);
			jsonObject.put("r9",	r9	);
			jsonObject.put("r10",	r10	);
			jsonObject.put("r11",	r11	);
			jsonObject.put("r12",	r12	);
			jsonObject.put("r13",	r13	);
			jsonObject.put("r14",	r14	);
			out.print(jsonObject.toString());
		} catch (Exception e) {
		  e.printStackTrace();
		}
	}
	
}


} catch (SQLException e) {
	  e.printStackTrace();
	}finally{
	  try {rs.close();mes_rs.close();} catch (SQLException e) {e.printStackTrace();}
	  try {pstmt.close();mes_pstmt.close();} catch (SQLException e) {e.printStackTrace();}
	  try {con.close();mes_con.close();} catch (SQLException e) {e.printStackTrace();}
	}

%>
