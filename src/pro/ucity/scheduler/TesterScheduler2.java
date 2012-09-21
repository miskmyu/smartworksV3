/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 9. 20.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package pro.ucity.scheduler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class TesterScheduler2 extends QuartzJobBean  {
	
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {

		System.out.println("스케쥴러 동작 시간 : " + new Date());
		
		try {
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		Connection con = null;
		PreparedStatement selectPstmt = null;
		PreparedStatement updatePstmt = null;
		try {
			String CONNECTION = "jdbc:jtds:sqlserver://localhost:1433/onegene";
			String USER = "sa";
			String PSWD = "mis";
			
			con = DriverManager.getConnection(CONNECTION, USER, PSWD);
			
			String selectSql = "select * from SampleTable where status = ?";
			String updateSql = "update SampleTable set status='Y' where id = ?";
			selectPstmt = con.prepareStatement(selectSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			selectPstmt.setString(1, "N");
			ResultSet rs = selectPstmt.executeQuery();
			
			rs.last(); 
			int count = rs.getRow(); 
			rs.beforeFirst();
			
			if (count != 0) {
				System.out.println("############ 이벤트 발생 ################");
				System.out.println("이벤트 발생 시간 : " + new Date());
				System.out.println("조회 데이터 수 : " + rs.getRow());
				System.out.println("------------ 데이터 처리 ----------------");
				while(rs.next()) {
					String id = rs.getString("id");
					String name = rs.getString("name");
					System.out.println("ID : " + id);
					
					startBpmProcess(id, name);
					
					updatePstmt = con.prepareStatement(updateSql);
					updatePstmt.setString(1, id);
					boolean result = updatePstmt.execute();
					System.out.println("ID : '" + id + "' UPDATE STATUS COMPLETE!");
				}
			System.out.println("############ 이벤트 처리 완료 ################");
			}
			
		} catch (Exception e) {
			System.out.println("UPDATE FAIL!!!!!!!!!!!!!!!!!!!!!!!!");
			e.printStackTrace();
		} finally {
			try {
				if (selectPstmt != null)
					selectPstmt.close();
				if (updatePstmt != null)
					updatePstmt.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private boolean startBpmProcess(String id, String name) throws Exception {

		System.out.println("---> BPMS PROCESS START! (ID :"+ id + ", NAME :" + name +") <----");
		
		return true;
	}
}
