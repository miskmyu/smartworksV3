package pro.ucity.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.tmax.tibero.jdbc.TbSQLException;

import pro.ucity.util.UcityTest;
import pro.ucity.util.UcityUtil;
import net.smartworks.model.KeyMap;
import net.smartworks.model.instance.TaskInstance;
import net.smartworks.server.service.factory.SwServiceFactory;
import net.smartworks.util.SmartUtil;

public class OPSms {

	public static final String FIELD_NAME_SMS_ID = "SMS_ID";
	public static final String FIELD_NAME_SITUATION_ID = "SITUATION_ID";
	public static final String FIELD_NAME_READ_CONFIRM = "BPM_CNFM_YN";
	public static final String FIELD_NAME_REGIST_DATE = "REGIST_DATE";

//	public static final String QUERY_SELECT_FOR_CHECK = "select * from " + System.TABLE_NAME_OPPORTAL_SMS + " where " + FIELD_NAME_SITUATION_ID + " = ? and (" + FIELD_NAME_READ_CONFIRM + " != 'Y' or " + FIELD_NAME_READ_CONFIRM + " is null) order by " + FIELD_NAME_REGIST_DATE + " desc";
//	public static final String QUERY_UPDATE_FOR_READ_CONFIRM = "update " + System.TABLE_NAME_OPPORTAL_SMS + " set " + FIELD_NAME_READ_CONFIRM + " = 'Y' where " + FIELD_NAME_SITUATION_ID + " = ?";
//	public static final String QUERY_SELECT_FOR_PERFORM = "select * from " + System.TABLE_NAME_OPPORTAL_SMS + " where " + FIELD_NAME_SITUATION_ID + " = ? and " + FIELD_NAME_SMS_ID + " = ?";

	public static final KeyMap[] OPPORTAL_SMS_FIELDS = {
		new KeyMap("메시지 아이디", "SMS_ID"), new KeyMap("메시지 구분", "SEND_TYPE"), new KeyMap("등록일", "REGIST_DATE"), new KeyMap("발송자", "SEND_USER_NAME"),
		new KeyMap("예약발송일자", "RESERVATION_SEND_DATE"), new KeyMap("알림구분", "NOTICE_TYPE"), new KeyMap("상태", "STATUS"), new KeyMap("메시지 내용", "SMS_CONTENT")
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
			if(keyMap.getKey().equals("SMS_ID"))
				dataRecord.put(keyMap.getId(), this.smsId);
			else if(keyMap.getKey().equals("SEND_TYPE"))
				dataRecord.put(keyMap.getId(), this.sendType);
			else if(keyMap.getKey().equals("REGIST_DATE"))
				dataRecord.put(keyMap.getId(), this.registDate);
			else if(keyMap.getKey().equals("SEND_USER_NAME"))
				dataRecord.put(keyMap.getId(), this.sendUserName);
			else if(keyMap.getKey().equals("NOTICE_TYPE"))
				dataRecord.put(keyMap.getId(), this.noticeType);
			else if(keyMap.getKey().equals("STATUS"))
				dataRecord.put(keyMap.getId(), this.status);
			else if(keyMap.getKey().equals("SMS_CONTENT"))
				dataRecord.put(keyMap.getId(), this.smsContent);
			else if(keyMap.getKey().equals("RESERVATION_SEND_DATE"))
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
			this.situationId = result.getString("SITUATION_ID");
		}catch (Exception ex){}
		try{
			this.smsId = result.getString("SMS_ID");
		}catch (Exception ex){}
		try{
			this.sendType = result.getString("SEND_TYPE");
		}catch (Exception ex){}
		try{
			this.registDate = result.getString("REGIST_DATE");
		}catch (Exception ex){}
		try{
			this.sendUserName = result.getString("SEND_USER_NAME");
		}catch (Exception ex){}
		try{
			this.noticeType = result.getString("NOTICE_TYPE");
		}catch (Exception ex){}
		try{
			this.status = result.getString("STATUS");
		}catch (Exception ex){}
		try{
			this.smsContent = result.getString("SMS_CONTENT");
		}catch (Exception ex){}
		try{
			this.reservationSendDate = result.getString("RESERVATION_SEND_DATE");
		}catch (Exception ex){}
	}
	
	public boolean isValid(){
		if(!SmartUtil.isBlankObject(this.situationId) && !SmartUtil.isBlankObject(this.smsId)) return true;
		return false;
	}
	
	public static Map<String,Object> checkForDisplay(String eventId,  Map<String,Object> dataRecord){
		
		if(SmartUtil.isBlankObject(eventId) || SmartUtil.isBlankObject(dataRecord)) return dataRecord;
		try {
			Class.forName(System.DATABASE_JDBC_DRIVE);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Connection con = null;
		PreparedStatement selectPstmt = null;
		PreparedStatement updatePstmt = null;
				
//		String opSmsSelectSql = QUERY_SELECT_FOR_CHECK;
//		String opSmsUpdateSql = QUERY_UPDATE_FOR_READ_CONFIRM;
		
		String opSmsSelectSql = UcityConstant.getQueryByKey("OPSms.QUERY_SELECT_FOR_CHECK");
		String opSmsUpdateSql = UcityConstant.getQueryByKey("OPSms.QUERY_UPDATE_FOR_READ_CONFIRM");
		try {
			try{
				con = DriverManager.getConnection(System.DATABASE_CONNECTION, System.DATABASE_USERNAME, System.DATABASE_PASSWORD);
			}catch (TbSQLException te){
				te.printStackTrace();
				return null;
			}
			
			try{
				selectPstmt = con.prepareStatement(opSmsSelectSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				selectPstmt.setString(1, eventId);
				ResultSet rs = selectPstmt.executeQuery();				
				rs.last(); 
				int count = rs.getRow();
				rs.first();
				if(count>0) {
					try{
						String smsId = rs.getString(OPSms.FIELD_NAME_SMS_ID);
						updatePstmt = con.prepareStatement(opSmsUpdateSql);
						updatePstmt.setString(1, eventId);
						boolean result = updatePstmt.execute();
						try {
							if (selectPstmt != null)
								selectPstmt.close();
							con.close();
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
				if(con != null)
					con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dataRecord;
		
	}
	
	public static Map<String,Object> readHistoryTable(String eventId, String smsId){
		
		if(SmartUtil.isBlankObject(eventId) || SmartUtil.isBlankObject(smsId)) return null;
		try {
			Class.forName(System.DATABASE_JDBC_DRIVE);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Connection con = null;
		PreparedStatement selectPstmt = null;
				
//		String opSmsSelectSql = OPSms.QUERY_SELECT_FOR_PERFORM;
		String opSmsSelectSql = UcityConstant.getQueryByKey("OPSms.QUERY_SELECT_FOR_PERFORM");
		try {
			try{
				con = DriverManager.getConnection(System.DATABASE_CONNECTION, System.DATABASE_USERNAME, System.DATABASE_PASSWORD);
			}catch (TbSQLException te){
				te.printStackTrace();
				return null;
			}
			
			try{
				selectPstmt = con.prepareStatement(opSmsSelectSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
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
								con.close();
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
				if(con != null)
					con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}
