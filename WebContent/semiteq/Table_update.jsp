<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.PreparedStatement"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%
String SQL_DRIVER="com.microsoft.sqlserver.jdbc.SQLServerDriver";
String URL="jdbc:sqlserver://222.116.101.228:1433;SelectMethod=cursor;DatabaseName=SmartWorks";
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
/* 도면 정보 */
ArrayList<String> d_domainId = new ArrayList<String>();	// 도메인 아이디
ArrayList<String> d_creator = new ArrayList<String>();    // 작성자 아이디
ArrayList<String> d_createdTime = new ArrayList<String>();	// 작성시간
ArrayList<String> d_modifier = new ArrayList<String>();    // 수정자 아이디
ArrayList<String> d_modifierdTime = new ArrayList<String>();	// 수정시간
ArrayList<String> c2 = new ArrayList<String>(); // Rev번호
ArrayList<String> c8 = new ArrayList<String>(); // 소분류
ArrayList<String> c5 = new ArrayList<String>(); // 기안자
ArrayList<String> c4 = new ArrayList<String>(); // 작성부서
ArrayList<String> c1 = new ArrayList<String>(); // 표준명
ArrayList<String> c3 = new ArrayList<String>(); // 대분류
ArrayList<String> c9 = new ArrayList<String>(); // PKGTYPE
ArrayList<String> c6 = new ArrayList<String>();	// 중분류
ArrayList<String> c0 = new ArrayList<String>(); // 표준번호
ArrayList<String> c7 = new ArrayList<String>();	// 표준양식
/* 도면 정보 */

/* 지침서 정보 */
ArrayList<String> g_domainId = new ArrayList<String>();	// 도메인 아이디
ArrayList<String> g_creator = new ArrayList<String>();    // 작성자 아이디
ArrayList<String> g_createdTime = new ArrayList<String>();	// 작성시간
ArrayList<String> g_modifier = new ArrayList<String>();    // 수정자 아이디
ArrayList<String> g_modifierdTime = new ArrayList<String>();	// 수정시간
ArrayList<String> g_c8  = new ArrayList<String>(); 
ArrayList<String> g_c6  = new ArrayList<String>(); 
ArrayList<String> g_c5  = new ArrayList<String>(); 
ArrayList<String> g_c1  = new ArrayList<String>(); 
ArrayList<String> g_c7  = new ArrayList<String>(); 
ArrayList<String> g_c4  = new ArrayList<String>(); 
ArrayList<String> g_c2  = new ArrayList<String>(); 
ArrayList<String> g_c3  = new ArrayList<String>(); 
ArrayList<String> g_c0  = new ArrayList<String>(); 
ArrayList<String> g_c9  = new ArrayList<String>(); 
ArrayList<String> g_c10 = new ArrayList<String>(); 
ArrayList<String> g_c11 = new ArrayList<String>(); 
ArrayList<String> g_c12 = new ArrayList<String>(); 
/* 지침서 정보 */

/* 매그나칩 본딩 정보 */
ArrayList<String> b_domainId = new ArrayList<String>();	// 도메인 아이디
ArrayList<String> b_creator = new ArrayList<String>();    // 작성자 아이디
ArrayList<String> b_createdTime = new ArrayList<String>();	// 작성시간
ArrayList<String> b_modifier = new ArrayList<String>();    // 수정자 아이디
ArrayList<String> b_modifierdTime = new ArrayList<String>();	// 수정시간
ArrayList<String> b_c7 = new ArrayList<String>(); 
ArrayList<String> b_c0 = new ArrayList<String>(); 
ArrayList<String> b_c6 = new ArrayList<String>(); 
ArrayList<String> b_c1 = new ArrayList<String>(); 
ArrayList<String> b_c3 = new ArrayList<String>(); 
ArrayList<String> b_c2 = new ArrayList<String>(); 
ArrayList<String> b_c4 = new ArrayList<String>(); 
ArrayList<String> b_c5 = new ArrayList<String>(); 
/* 매그나칩 본딩 정보 */

/* 마킹 정보 */
ArrayList<String> m_domainId = new ArrayList<String>();	// 도메인 아이디
ArrayList<String> m_creator = new ArrayList<String>();    // 작성자 아이디
ArrayList<String> m_createdTime = new ArrayList<String>();	// 작성시간
ArrayList<String> m_modifier = new ArrayList<String>();    // 수정자 아이디
ArrayList<String> m_modifierdTime = new ArrayList<String>();	// 수정시간
ArrayList<String> m_c2 = new ArrayList<String>();   
ArrayList<String> m_c1 = new ArrayList<String>();   
ArrayList<String> m_c5 = new ArrayList<String>();   
ArrayList<String> m_c4 = new ArrayList<String>();   
ArrayList<String> m_c3 = new ArrayList<String>();   
ArrayList<String> m_c0 = new ArrayList<String>();   
ArrayList<String> m_c7 = new ArrayList<String>();   
ArrayList<String> m_c6 = new ArrayList<String>();   
/* 마킹 정보 */

try {
	
	// 커넥션 연결	
	con = DriverManager.getConnection(URL,USER,PASS);
/*
	// 도면정보
	String query = "";
	query = query + "select * from dt_f967d987a6e34eb3b63e67856bf7b838";
	pstmt = con.prepareStatement(query);
	rs = pstmt.executeQuery();
	
	while(rs.next()){
		d_domainId.add(rs.getString(2));
		d_creator.add(rs.getString(5));
		d_createdTime.add(rs.getString(6));
		d_modifier.add(rs.getString(7));
		d_modifierdTime.add(rs.getString(8));
		c2.add(rs.getString(9));
		c8.add(rs.getString(10));
		c5.add(rs.getString(11));
		c4.add(rs.getString(12));
		c1.add(rs.getString(13));
		c3.add(rs.getString(14));
		c9.add(rs.getString(15));
		c6.add(rs.getString(16));
		c0.add(rs.getString(17));
		c7.add(rs.getString(18));
	}
	*/
	// 지침서정보
	String g_query = "";
	g_query = g_query + "select * from dt_cb25ebbdf451436c854636642b74e5db";
	pstmt = con.prepareStatement(g_query);
	rs = pstmt.executeQuery();
	
	while(rs.next()){
		g_domainId.add(rs.getString(2));
		g_creator.add(rs.getString(5));
		g_createdTime.add(rs.getString(6));
		g_modifier.add(rs.getString(7));
		g_modifierdTime.add(rs.getString(8));
		g_c8.add(rs.getString(9));
		g_c6.add(rs.getString(10));
		g_c5.add(rs.getString(11));
		g_c1.add(rs.getString(12));
		g_c7.add(rs.getString(13));
		g_c4.add(rs.getString(14));
		g_c2.add(rs.getString(15));
		g_c3.add(rs.getString(16));
		g_c0.add(rs.getString(17));
		g_c9.add(rs.getString(18));
		g_c10.add(rs.getString(19));
		g_c11.add(rs.getString(20));
		g_c12.add(rs.getString(21));
	}
	// 지침서정보
		String b_query = "";
		b_query = b_query + "select * from dt_6fe04d36cf82406cbc39fe3359346d7b where c7 ='매그나칩'";
		pstmt = con.prepareStatement(b_query);
		rs = pstmt.executeQuery();
		
		while(rs.next()){
			b_domainId.add(rs.getString(2));
			b_creator.add(rs.getString(3));
			b_createdTime.add(rs.getString(6));
			b_modifier.add(rs.getString(7));
			b_modifierdTime.add(rs.getString(8));
			b_c7.add(rs.getString(9));
			b_c0.add(rs.getString(10));
			b_c6.add(rs.getString(11));
			b_c1.add(rs.getString(12));
			b_c3.add(rs.getString(13));
			b_c2.add(rs.getString(14));
			b_c4.add(rs.getString(15));
			b_c5.add(rs.getString(16));
		}
		// 지침서정보
		String m_query = "";
		m_query = m_query + "select * from dt_72e6a44c0d6c42e1955946417bfcaa06";
		pstmt = con.prepareStatement(m_query);
		rs = pstmt.executeQuery();
		
		while(rs.next()){
			m_domainId.add(rs.getString(2));
			m_creator.add(rs.getString(3));
			m_createdTime.add(rs.getString(6));
			m_modifier.add(rs.getString(7));
			m_modifierdTime.add(rs.getString(8));
			m_c2.add(rs.getString(9));
			m_c1.add(rs.getString(10));
			m_c5.add(rs.getString(11));
			m_c4.add(rs.getString(12));
			m_c3.add(rs.getString(13));
			m_c0.add(rs.getString(14));
			m_c7.add(rs.getString(15));
			m_c6.add(rs.getString(16));
		}
		String recordId = "";
		recordId = "dr_" + CommonUtil.newId();

  		for(int i=0;i<g_domainId.size();i++){
// 		for(int i=0;i<1;i++){
			recordId = "dr_" + CommonUtil.newId();
			System.out.println(recordId);
			String insert = "";
			insert = insert + "insert into dt_1330130316125 values (";
			insert = insert + "?,";	   // id
			insert = insert + "?,";	   // domainId
			insert = insert + "NULL,";	   // workItemId
			insert = insert + "NULL,";     // masterRecordId
			insert = insert + "?,";     // creator
			insert = insert + "?,";     // createdTime
			insert = insert + "?,";     // modifier
			insert = insert + "?,";     // modifiedTime
			insert = insert + "NULL,";     // 생산기술1팀 배포공정
			insert = insert + "NULL,";     // 생산기술2팀 배포부수
			insert = insert + "NULL,";     // 적용 DEVICE
			insert = insert + "NULL,";     // 표준 중분류
			insert = insert + "NULL,";     // CS팀 배포부수
			insert = insert + "NULL,";     // 생산2팀 배포부수
			insert = insert + "?,";     // 첨부파일
			insert = insert + "?,";     // 등록일자
			insert = insert + "NULL,";     // CS팀 배포공정
			insert = insert + "?,";     // 표준번호
			insert = insert + "NULL,";     // c19
			insert = insert + "NULL,";     // 생산2팀 배포공정
			insert = insert + "?,";     // 기안자
			insert = insert + "?,";     // 개정번호
			insert = insert + "?,";     // 적용 PKG TYPE
			insert = insert + "NULL,";     // 생산기술2팀 배포공정
			insert = insert + "NULL,";     // 품질관리팀 배포공정
			insert = insert + "NULL,";     // 품질관리팀 배포부수
			insert = insert + "NULL,";     // Packing 배포요청부수
			insert = insert + "NULL,";     // TEST생산 배포요청부수
			insert = insert + "?,";     // 표준제목
			insert = insert + "NULL,";     // STOCK 배포요청부수
			insert = insert + "?,";     // 제/개정(폐기)사유
			insert = insert + "NULL,";     // 생산기술1팀 배포부수
			insert = insert + "NULL,";     // c18
			insert = insert + "NULL,";     // 생산1팀 배포부수
			insert = insert + "?,";     // 표준 소분류
			insert = insert + "?,";     // 기안부서
			insert = insert + "?,";     // 공정
			insert = insert + "NULL,";     // 생산1팀 배포공정
			insert = insert + "?,";     // 적용 고객사
			insert = insert + "NULL,";     // 유효기간
			insert = insert + "NULL,";     // ECN 배포요청부수
			insert = insert + "NULL,";     // 표준 대분류
			insert = insert + "NULL,";     // ROM CODE
			insert = insert + "NULL";      // 적용 Body Size		
			insert = insert + ")";		
			pstmt = con.prepareStatement(insert);
			pstmt.setString(1,recordId);
			pstmt.setString(2,g_domainId.get(i));
			pstmt.setString(3,g_creator.get(i));
			pstmt.setString(4,g_createdTime.get(i));
			pstmt.setString(5,g_modifier.get(i));
			pstmt.setString(6,g_modifierdTime.get(i));
			pstmt.setString(7,g_c7.get(i));
			pstmt.setString(8,g_c12.get(i));
			pstmt.setString(9,g_c0.get(i));
			pstmt.setString(10,g_c5.get(i));
			pstmt.setString(11,g_c2.get(i));
			pstmt.setString(12,g_c9.get(i));
			pstmt.setString(13,g_c1.get(i));
			pstmt.setString(14,g_c11.get(i));
			pstmt.setString(15,g_c6.get(i));
			pstmt.setString(16,g_c4.get(i));
			pstmt.setString(17,g_c8.get(i));
			pstmt.setString(18,g_c10.get(i));
			pstmt.executeUpdate();
			
			
		}
		 
		/*
		
		
		insert = insert + "insert into dt_1330130316125 values (";
		insert = insert + "'"+  +"',";	   // id
		insert = insert + "'"+ d_domainId.get(i)  +"',";	   // domainId
		insert = insert + "'',";	   // workItemId
		insert = insert + "'',";     // masterRecordId
		insert = insert + "'"+ d_creator.get(i) +"',";     // creator
		insert = insert + "'"+ d_createdTime.get(i) +"',";     // createdTime
		insert = insert + "'"+ d_modifier.get(i) +"',";     // modifier
		insert = insert + "'"+ d_modifierdTime.get(i) +"',";     // modifiedTime
		insert = insert + "'"+  +"',";     // 생산기술1팀 배포공정
		insert = insert + "'"+  +"',";     // 생산기술2팀 배포부수
		insert = insert + "'"+  +"',";     // 적용 DEVICE
		insert = insert + "'"+  +"',";     // 표준 중분류
		insert = insert + "'"+  +"',";     // CS팀 배포부수
		insert = insert + "'"+  +"',";     // 생산2팀 배포부수
		insert = insert + "'"+  +"',";     // 첨부파일
		insert = insert + "'"+  +"',";     // 등록일자
		insert = insert + "'"+  +"',";     // CS팀 배포공정
		insert = insert + "'"+  +"',";     // 표준번호
		insert = insert + "'"+  +"',";     // c19
		insert = insert + "'"+  +"',";     // 생산2팀 배포공정
		insert = insert + "'"+  +"',";     // 기안자
		insert = insert + "'"+  +"',";     // 개정번호
		insert = insert + "'"+  +"',";     // 적용 PKG TYPE
		insert = insert + "'"+  +"',";     // 생산기술2팀 배포공정
		insert = insert + "'"+  +"',";     // 품질관리팀 배포공정
		insert = insert + "'"+  +"',";     // 품질관리팀 배포부수
		insert = insert + "'"+  +"',";     // Packing 배포요청부수
		insert = insert + "'"+  +"',";     // TEST생산 배포요청부수
		insert = insert + "'"+  +"',";     // 표준제목
		insert = insert + "'"+  +"',";     // STOCK 배포요청부수
		insert = insert + "'"+  +"',";     // 제/개정(폐기)사유
		insert = insert + "'"+  +"',";     // 생산기술1팀 배포부수
		insert = insert + "'"+  +"',";     // c18
		insert = insert + "'"+  +"',";     // 생산1팀 배포부수
		insert = insert + "'"+  +"',";     // 표준 소분류
		insert = insert + "'"+  +"',";     // 기안부서
		insert = insert + "'"+  +"',";     // 공정
		insert = insert + "'"+  +"',";     // 생산1팀 배포공정
		insert = insert + "'"+  +"',";     // 적용 고객사
		insert = insert + "'"+  +"',";     // 유효기간
		insert = insert + "'"+  +"',";     // ECN 배포요청부수
		insert = insert + "'"+  +"',";     // 표준 대분류
		insert = insert + "'"+  +"',";     // ROM CODE
		insert = insert + "'"+  +"'";      // 적용 Body Size		
		insert = insert + ")";		
		
		
		
		
		insert = insert + "insert into dt_1330130316125 values (";
		insert = insert + "'"+  +"',";	   // id
		insert = insert + "'"+ d_domainId.get(i)  +"',";	   // domainId
		insert = insert + "'',";	   // workItemId
		insert = insert + "'',";     // masterRecordId
		insert = insert + "'"+ d_creator.get(i) +"',";     // creator
		insert = insert + "'"+ d_createdTime.get(i) +"',";     // createdTime
		insert = insert + "'"+ d_modifier.get(i) +"',";     // modifier
		insert = insert + "'"+ d_modifierdTime.get(i) +"',";     // modifiedTime
		insert = insert + "'"+  +"',";     // 생산기술1팀 배포공정
		insert = insert + "'"+  +"',";     // 생산기술2팀 배포부수
		insert = insert + "'"+  +"',";     // 적용 DEVICE
		insert = insert + "'"+  +"',";     // 표준 중분류
		insert = insert + "'"+  +"',";     // CS팀 배포부수
		insert = insert + "'"+  +"',";     // 생산2팀 배포부수
		insert = insert + "'"+  +"',";     // 첨부파일
		insert = insert + "'"+  +"',";     // 등록일자
		insert = insert + "'"+  +"',";     // CS팀 배포공정
		insert = insert + "'"+  +"',";     // 표준번호
		insert = insert + "'"+  +"',";     // c19
		insert = insert + "'"+  +"',";     // 생산2팀 배포공정
		insert = insert + "'"+  +"',";     // 기안자
		insert = insert + "'"+  +"',";     // 개정번호
		insert = insert + "'"+  +"',";     // 적용 PKG TYPE
		insert = insert + "'"+  +"',";     // 생산기술2팀 배포공정
		insert = insert + "'"+  +"',";     // 품질관리팀 배포공정
		insert = insert + "'"+  +"',";     // 품질관리팀 배포부수
		insert = insert + "'"+  +"',";     // Packing 배포요청부수
		insert = insert + "'"+  +"',";     // TEST생산 배포요청부수
		insert = insert + "'"+  +"',";     // 표준제목
		insert = insert + "'"+  +"',";     // STOCK 배포요청부수
		insert = insert + "'"+  +"',";     // 제/개정(폐기)사유
		insert = insert + "'"+  +"',";     // 생산기술1팀 배포부수
		insert = insert + "'"+  +"',";     // c18
		insert = insert + "'"+  +"',";     // 생산1팀 배포부수
		insert = insert + "'"+  +"',";     // 표준 소분류
		insert = insert + "'"+  +"',";     // 기안부서
		insert = insert + "'"+  +"',";     // 공정
		insert = insert + "'"+  +"',";     // 생산1팀 배포공정
		insert = insert + "'"+  +"',";     // 적용 고객사
		insert = insert + "'"+  +"',";     // 유효기간
		insert = insert + "'"+  +"',";     // ECN 배포요청부수
		insert = insert + "'"+  +"',";     // 표준 대분류
		insert = insert + "'"+  +"',";     // ROM CODE
		insert = insert + "'"+  +"'";      // 적용 Body Size		
		insert = insert + ")";		
		
		
		
		
		insert = insert + "insert into dt_1330130316125 values (";
		insert = insert + "'"+  +"',";	   // id
		insert = insert + "'"+ d_domainId.get(i)  +"',";	   // domainId
		insert = insert + "'',";	   // workItemId
		insert = insert + "'',";     // masterRecordId
		insert = insert + "'"+ d_creator.get(i) +"',";     // creator
		insert = insert + "'"+ d_createdTime.get(i) +"',";     // createdTime
		insert = insert + "'"+ d_modifier.get(i) +"',";     // modifier
		insert = insert + "'"+ d_modifierdTime.get(i) +"',";     // modifiedTime
		insert = insert + "'"+  +"',";     // 생산기술1팀 배포공정
		insert = insert + "'"+  +"',";     // 생산기술2팀 배포부수
		insert = insert + "'"+  +"',";     // 적용 DEVICE
		insert = insert + "'"+  +"',";     // 표준 중분류
		insert = insert + "'"+  +"',";     // CS팀 배포부수
		insert = insert + "'"+  +"',";     // 생산2팀 배포부수
		insert = insert + "'"+  +"',";     // 첨부파일
		insert = insert + "'"+  +"',";     // 등록일자
		insert = insert + "'"+  +"',";     // CS팀 배포공정
		insert = insert + "'"+  +"',";     // 표준번호
		insert = insert + "'"+  +"',";     // c19
		insert = insert + "'"+  +"',";     // 생산2팀 배포공정
		insert = insert + "'"+  +"',";     // 기안자
		insert = insert + "'"+  +"',";     // 개정번호
		insert = insert + "'"+  +"',";     // 적용 PKG TYPE
		insert = insert + "'"+  +"',";     // 생산기술2팀 배포공정
		insert = insert + "'"+  +"',";     // 품질관리팀 배포공정
		insert = insert + "'"+  +"',";     // 품질관리팀 배포부수
		insert = insert + "'"+  +"',";     // Packing 배포요청부수
		insert = insert + "'"+  +"',";     // TEST생산 배포요청부수
		insert = insert + "'"+  +"',";     // 표준제목
		insert = insert + "'"+  +"',";     // STOCK 배포요청부수
		insert = insert + "'"+  +"',";     // 제/개정(폐기)사유
		insert = insert + "'"+  +"',";     // 생산기술1팀 배포부수
		insert = insert + "'"+  +"',";     // c18
		insert = insert + "'"+  +"',";     // 생산1팀 배포부수
		insert = insert + "'"+  +"',";     // 표준 소분류
		insert = insert + "'"+  +"',";     // 기안부서
		insert = insert + "'"+  +"',";     // 공정
		insert = insert + "'"+  +"',";     // 생산1팀 배포공정
		insert = insert + "'"+  +"',";     // 적용 고객사
		insert = insert + "'"+  +"',";     // 유효기간
		insert = insert + "'"+  +"',";     // ECN 배포요청부수
		insert = insert + "'"+  +"',";     // 표준 대분류
		insert = insert + "'"+  +"',";     // ROM CODE
		insert = insert + "'"+  +"'";      // 적용 Body Size		
		insert = insert + ")";		
		*/
		
} catch (SQLException e) {
	  e.printStackTrace();
	}finally{
	  try { rs.close();} catch (SQLException e) {e.printStackTrace();}
	  try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
	  try {con.close();} catch (SQLException e) {e.printStackTrace();}
	}
%>