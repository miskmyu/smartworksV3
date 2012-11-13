package pro.ucity.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.tmax.tibero.jdbc.TbSQLException;

import pro.ucity.util.UcityTest;
import pro.ucity.util.UcityUtil;
import net.smartworks.model.KeyMap;
import net.smartworks.model.instance.TaskInstance;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.service.factory.SwServiceFactory;
import net.smartworks.util.SmartUtil;

public class OPSms {

	public static final KeyMap[] OPPORTAL_SMS_FIELDS = {
		new KeyMap("메시지 아이디", UcityConstant.getQueryByKey("OPSms.SMS_ID")), new KeyMap("메시지 구분", UcityConstant.getQueryByKey("OPSms.SEND_TYPE")), new KeyMap("등록일", UcityConstant.getQueryByKey("OPSms.REGIST_DATE")), new KeyMap("발송자", UcityConstant.getQueryByKey("OPSms.SEND_USER_NAME")),
		new KeyMap("예약발송일자", UcityConstant.getQueryByKey("OPSms.RESERVATION_SEND_DATE")), new KeyMap("알림구분", UcityConstant.getQueryByKey("OPSms.NOTICE_TYPE")), new KeyMap("상태", UcityConstant.getQueryByKey("OPSms.STATUS")), new KeyMap("메시지 내용", UcityConstant.getQueryByKey("OPSms.SMS_CONTENT"))
	};
	
	private String situationId;
	
	private String smsId;
	private String sendType;
	private String registDate;
	private String sendUserName;
	private String reservationSendDate;
	private String noticeType;
	private String status;
	private String smsContent;

	
	public String getSituationId() {
		return situationId;
	}

	public void setSituationId(String situationId) {
		this.situationId = situationId;
	}

	public String getSmsId() {
		return smsId;
	}

	public void setSmsId(String smsId) {
		this.smsId = smsId;
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	public String getRegistDate() {
		return registDate;
	}

	public void setRegistDate(String registDate) {
		this.registDate = registDate;
	}

	public String getSendUserName() {
		return sendUserName;
	}

	public void setSendUserName(String sendUserName) {
		this.sendUserName = sendUserName;
	}

	public String getReservationSendDate() {
		return reservationSendDate;
	}

	public void setReservationSendDate(String reservationSendDate) {
		this.reservationSendDate = reservationSendDate;
	}

	public String getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSmsContent() {
		return smsContent;
	}

	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}
	
	public OPSms(ResultSet resultSet){
		super();
		if(SmartUtil.isBlankObject(resultSet)) return;
		this.setResult(resultSet);
	}
	
	public Map<String, Object> getDataRecord(){
		
		Map<String, Object> dataRecord = new HashMap<String, Object>();
		KeyMap[] keyMaps = OPSms.OPPORTAL_SMS_FIELDS;

		if(!this.isValid()) return null;
		
		dataRecord.put("isSms", "true");
		
		for(int i=0; i<keyMaps.length; i++){
			KeyMap keyMap = keyMaps[i];
			if(keyMap.getKey().equals(UcityConstant.getQueryByKey("OPSms.SMS_ID")))
				dataRecord.put(keyMap.getId(), this.smsId);
			else if(keyMap.getKey().equals(UcityConstant.getQueryByKey("OPSms.SEND_TYPE")))
				dataRecord.put(keyMap.getId(), this.sendType);
			else if(keyMap.getKey().equals(UcityConstant.getQueryByKey("OPSms.REGIST_DATE")))
				dataRecord.put(keyMap.getId(), this.registDate);
			else if(keyMap.getKey().equals(UcityConstant.getQueryByKey("OPSms.SEND_USER_NAME")))
				dataRecord.put(keyMap.getId(), this.sendUserName);
			else if(keyMap.getKey().equals(UcityConstant.getQueryByKey("OPSms.NOTICE_TYPE")))
				dataRecord.put(keyMap.getId(), this.noticeType);
			else if(keyMap.getKey().equals(UcityConstant.getQueryByKey("OPSms.STATUS")))
				dataRecord.put(keyMap.getId(), this.status);
			else if(keyMap.getKey().equals(UcityConstant.getQueryByKey("OPSms.SMS_CONTENT")))
				dataRecord.put(keyMap.getId(), this.smsContent);
			else if(keyMap.getKey().equals(UcityConstant.getQueryByKey("OPSms.RESERVATION_SEND_DATE")))
				dataRecord.put(keyMap.getId(), this.reservationSendDate);
		}	
		return dataRecord;
	}
	
	public void performTask(String processId, String taskInstId) throws Exception{
		TaskInstance taskInstance = null;
		if(SmartUtil.isBlankObject(taskInstId)) return;
		
		taskInstance = SwServiceFactory.getInstance().getInstanceService().getTaskInstanceById(processId, taskInstId);
		
		if(SmartUtil.isBlankObject(taskInstance)) return;
		
		UcityUtil.performUServiceTask(taskInstance, this.getDataRecord());		
	}
	
	public void setResult(ResultSet result){
		if(SmartUtil.isBlankObject(result)) return; 				
		try{
			this.situationId = result.getString(UcityConstant.getQueryByKey("OPSms.SITUATION_ID"));
		}catch (Exception ex){}
		try{
			this.smsId = result.getString(UcityConstant.getQueryByKey("OPSms.SMS_ID"));
		}catch (Exception ex){}
		try{
			this.sendType = result.getString(UcityConstant.getQueryByKey("OPSms.SEND_TYPE"));
		}catch (Exception ex){}
		try{
			this.registDate = result.getString(UcityConstant.getQueryByKey("OPSms.REGIST_DATE"));
		}catch (Exception ex){}
		try{
			this.sendUserName = result.getString(UcityConstant.getQueryByKey("OPSms.SEND_USER_NAME"));
		}catch (Exception ex){}
		try{
			this.noticeType = result.getString(UcityConstant.getQueryByKey("OPSms.NOTICE_TYPE"));
		}catch (Exception ex){}
		try{
			this.status = result.getString(UcityConstant.getQueryByKey("OPSms.STATUS"));
		}catch (Exception ex){}
		try{
			this.smsContent = result.getString(UcityConstant.getQueryByKey("OPSms.SMS_CONTENT"));
		}catch (Exception ex){}
		try{
			this.reservationSendDate = result.getString(UcityConstant.getQueryByKey("OPSms.RESERVATION_SEND_DATE"));
		}catch (Exception ex){}
	}
	
	public boolean isValid(){
		if(!SmartUtil.isBlankObject(this.situationId) && !SmartUtil.isBlankObject(this.smsId)) return true;
		return false;
	}
	
	public static Map<String,Object> checkForDisplay(Connection connection, String eventId,  Map<String,Object> dataRecord){
		
		if(SmartUtil.isBlankObject(connection) || SmartUtil.isBlankObject(eventId) || SmartUtil.isBlankObject(dataRecord)) return dataRecord;

//		Connection con = null;
		PreparedStatement selectPstmt = null;
		PreparedStatement updatePstmt = null;
				
		String opSmsSelectSql = UcityConstant.getQueryByKey("OPSms.QUERY_SELECT_FOR_CHECK");
		String opSmsUpdateSql = UcityConstant.getQueryByKey("OPSms.QUERY_UPDATE_FOR_READ_CONFIRM");
		try {
//			try{
////			    Context init = new InitialContext();
////			    Context envinit = (Context)init.lookup("java:comp/env");
////			    DataSource ds = (DataSource) envinit.lookup("bpm/tibero");
////			    con = ds.getConnection();
//				con = SwManagerFactory.getInstance().getUcityContantsManager().getDataSource().getConnection();
//			}catch (TbSQLException te){
//				te.printStackTrace();
//				return null;
//			}
			
			try{
				selectPstmt = connection.prepareStatement(opSmsSelectSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				selectPstmt.setString(1, eventId);
				ResultSet rs = selectPstmt.executeQuery();				
				rs.last(); 
				int count = rs.getRow();
				rs.first();
				if(count>0) {
					try{
						String smsId = rs.getString(UcityConstant.getQueryByKey("OPSms.FIELD_NAME_SMS_ID"));
						updatePstmt = connection.prepareStatement(opSmsUpdateSql);
						updatePstmt.setString(1, eventId);
						boolean result = updatePstmt.execute();
						try {
							if (selectPstmt != null)
								selectPstmt.close();
//							con.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						dataRecord.put("SMS Id", smsId);
						return dataRecord;
					}catch (Exception we){
						we.printStackTrace();
					}
				}
			}catch (Exception e1){
				e1.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (selectPstmt != null)
					selectPstmt.close();
//				if(con != null)
//					con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dataRecord;
		
	}
	
	public static boolean checkIfDisplay(Connection connection, String eventId){
		
		if(SmartUtil.isBlankObject(connection) || SmartUtil.isBlankObject(eventId)) return false;

//		Connection con = null;
		PreparedStatement selectPstmt = null;
		PreparedStatement updatePstmt = null;
						
		String opSmsSelectSql = UcityConstant.getQueryByKey("OPSms.QUERY_SELECT_FOR_CHECK");
		String opSmsUpdateSql = UcityConstant.getQueryByKey("OPSms.QUERY_UPDATE_FOR_READ_CONFIRM");
		try {
//			try{
////			    Context init = new InitialContext();
////			    Context envinit = (Context)init.lookup("java:comp/env");
////			    DataSource ds = (DataSource) envinit.lookup("bpm/tibero");
////			    con = ds.getConnection();
//				con = SwManagerFactory.getInstance().getUcityContantsManager().getDataSource().getConnection();
//			}catch (TbSQLException te){
//				te.printStackTrace();
//				return false;
//			}
			
			try{
				selectPstmt = connection.prepareStatement(opSmsSelectSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				selectPstmt.setString(1, eventId);
				ResultSet rs = selectPstmt.executeQuery();				
				rs.last(); 
				int count = rs.getRow();
				rs.first();
				if(count>0) {
					try {
						if (selectPstmt != null)
							selectPstmt.close();
//						con.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return true;
				}
			}catch (Exception e1){
				e1.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (selectPstmt != null)
					selectPstmt.close();
//				if(con != null)
//					con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
		
	}
	
	public static Map<String,Object> readHistoryTable(Connection connection, String eventId, String smsId){
		
		if(SmartUtil.isBlankObject(connection) || SmartUtil.isBlankObject(eventId) || SmartUtil.isBlankObject(smsId)) return null;

//		Connection con = null;
		PreparedStatement selectPstmt = null;
				
		String opSmsSelectSql = UcityConstant.getQueryByKey("OPSms.QUERY_SELECT_FOR_PERFORM");
		try {
//			try{
////			    Context init = new InitialContext();
////			    Context envinit = (Context)init.lookup("java:comp/env");
////			    DataSource ds = (DataSource) envinit.lookup("bpm/tibero");
////			    con = ds.getConnection();
//				con = SwManagerFactory.getInstance().getUcityContantsManager().getDataSource().getConnection();
//			}catch (TbSQLException te){
//				te.printStackTrace();
//				return null;
//			}
			
			try{
				selectPstmt = connection.prepareStatement(opSmsSelectSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				selectPstmt.setString(1, eventId);
				selectPstmt.setString(2, smsId);
				ResultSet rs = selectPstmt.executeQuery();				
				rs.last(); 
				int count = rs.getRow();
				rs.first();
				if (count == 1) {
					try{
						OPSms opSms = new OPSms(rs);
						if(opSms.isValid()){
							try {
								if (selectPstmt != null)
									selectPstmt.close();
//								con.close();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							return opSms.getDataRecord();
						}
					}catch (Exception we){
						we.printStackTrace();
					}
				}
			}catch (Exception e1){
				e1.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (selectPstmt != null)
					selectPstmt.close();
//				if(con != null)
//					con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}
