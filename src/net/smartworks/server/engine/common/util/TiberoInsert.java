/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 9. 5.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.common.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.util.StringUtils;

import net.smartworks.server.engine.common.util.FileUtil;

public class TiberoInsert {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			//Class.forName("net.sourceforge.jtds.jdbc.Driver");
			Class.forName("com.tmax.tibero.jdbc.TbDriver");
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Connection con = null;
		PreparedStatement pstmt = null;
		Statement stmt = null;
		
		Map formIdMap = new HashMap();
		formIdMap.put("5e6caf381f26b928011f37cbba8b04cb", "5e6caf381f26b928011f37cbba8b04cb.txt");//기안품의
		formIdMap.put("40288afb1b280665011b2810bd840004", "40288afb1b280665011b2810bd840004.txt");//부서관리
		formIdMap.put("40288afb1b280665011b28195cac0007", "40288afb1b280665011b28195cac0007.txt");//역할관리
		formIdMap.put("40288afb1b280665011b283293f8000d", "40288afb1b280665011b283293f8000d.txt");//연락처
		formIdMap.put("40288afb1b280665011b2808064c0001", "40288afb1b280665011b2808064c0001.txt");//회사관리
		formIdMap.put("ff8080811b3037ba011b3040c8950002", "ff8080811b3037ba011b3040c8950002.txt");//사용자관리
		formIdMap.put("402880ec34e3d85d0134e3eb5768000c", "402880ec34e3d85d0134e3eb5768000c.txt");//일정
		formIdMap.put("frm_default_SYSTEM", "frm_default_SYSTEM.txt");//기본폼
		formIdMap.put("SYSTEMFORM", "SYSTEMFORM.txt");//기본폼
		
		String CONNECTION = "jdbc:tibero:thin:@66.232.147.111:8629:tibero";
		String USER = "tibero";
		String PSWD = "tmax";
		
		try {
			
			con = DriverManager.getConnection(CONNECTION, USER, PSWD);
			stmt = con.createStatement();
			String formSql = "update swform set content=? where id=?";
			
			Set keySet = formIdMap.keySet();
			Iterator itr = keySet.iterator();
			
			while (itr.hasNext()) {
				String key = (String)itr.next();
				String content = (String)FileUtil.readString("C:/tebero_insert/form/"+key+".txt", "EUC-KR");
				pstmt = con.prepareStatement(formSql);
				pstmt.setString(1, content);
				pstmt.setString(2, key);
				pstmt.execute();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				con.close();
				System.out.println("Closed Done!!!!!!!!!!!!!!!!!!!!1");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		try {
			con = DriverManager.getConnection(CONNECTION, USER, PSWD);
			stmt = con.createStatement();
			
			String prcprcSql = "update prcprc set prcdiagram=? where prcprcid=?";
			
			String content = (String)FileUtil.readString("C:/tebero_insert/prcprc/prc_28f6b70603b3465982240da1ff800ea2.txt", "EUC-KR");
			
			
			pstmt = con.prepareStatement(prcprcSql);
			pstmt.setString(1, content);
			pstmt.setString(2, "prc_28f6b70603b3465982240da1ff800ea2");
			pstmt.execute();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				con.close();
				System.out.println("Closed Done!!!!!!!!!!!!!!!!!!!!1");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			con = DriverManager.getConnection(CONNECTION, USER, PSWD);
			stmt = con.createStatement();
			
			String prcprcSql = "update swprocess set content=? where id=?";
			
			String content = (String)FileUtil.readString("C:/insert/swprocess/5e6caf381f26b928011f37cbba6c04ca.txt", "EUC-KR");
			
			
			pstmt = con.prepareStatement(prcprcSql);
			pstmt.setString(1, content);
			pstmt.setString(2, "5e6caf381f26b928011f37cbba6c04ca");
			pstmt.execute();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				con.close();
				System.out.println("Closed Done!!!!!!!!!!!!!!!!!!!!1");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}

}
