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
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.service.factory.SwServiceFactory;
import net.smartworks.util.SmartUtil;

public class DMHistory {

	public static final String DEVICE_ID_MEDIABOARD		= "CIMBMBD";
	public static final String DEVICE_ID_TRAFFIC_BIT	= "TMEMBIT";
	public static final String DEVICE_ID_TRAFFIC_VMS	= "TMEMVMS";
	public static final String DEVICE_ID_KIOSK			= "TMEMKIOSK";
	public static final String DEVICE_ID_ENV_VMS		= "EVEIVMS";
	
	public static final String FIELD_NAME_EVENT_ID = "a.EVENT_ID";
	public static final String FIELD_NAME_SEND_VALUE = "c.SEND_RESPONSE_VALUE";
	public static final String FIELD_NAME_SEND_SEQ = "SEND_INFO_SEQ";
	public static final String FIELD_NAME_ADAPTER_DIV = "b.ADAPTER_DIVISION";
	
	
	
	public static final String FLAG_STOP_DISPLAY 	= "Y";
	
//	public static final String QUERY_SELECT_FOR_DISPLAY_PERFORM = "select distinct a."+ FIELD_NAME_SEND_SEQ +",b.*," + FIELD_NAME_SEND_VALUE + ", " + FIELD_NAME_EVENT_ID + " from " + System.TABLE_NAME_SEND_INFO + " a, " + System.TABLE_NAME_SEND_CONTENTS + " b, "
//			+ System.TABLE_NAME_RCV_DEVICE + " c where a." + FIELD_NAME_SEND_SEQ + " = b." + FIELD_NAME_SEND_SEQ + " and c." + FIELD_NAME_SEND_SEQ +  " = a." + FIELD_NAME_SEND_SEQ + " and " + FIELD_NAME_EVENT_ID +
//			" = ? and " + FIELD_NAME_ADAPTER_DIV + " = ? order by a." + FIELD_NAME_SEND_SEQ + " desc";
//	
//	public static final String QUERY_SELECT_FOR_STOP_PERFORM = "select distinct a."+ FIELD_NAME_SEND_SEQ +",b.*," + FIELD_NAME_SEND_VALUE + ", " + FIELD_NAME_EVENT_ID + " from " + System.TABLE_NAME_SEND_INFO + " a, " + System.TABLE_NAME_SEND_CONTENTS + " b, "
//			+ System.TABLE_NAME_RCV_DEVICE + " c where a." + FIELD_NAME_SEND_SEQ + " = b." + FIELD_NAME_SEND_SEQ + " and c." + FIELD_NAME_SEND_SEQ +  " = a." + FIELD_NAME_SEND_SEQ + " and " + FIELD_NAME_EVENT_ID +
//			" = ? and " + FIELD_NAME_ADAPTER_DIV + " = ? order by a." + FIELD_NAME_SEND_SEQ + " desc";

	
	public static final KeyMap[] DEVMID_TRACE_FIELDS = {
		new KeyMap("표출내용", "DATA_PATH"), new KeyMap("표출지속시간", "PLAY_TIME"), new KeyMap("표출결과", "SEND_RESPONSE_VALUE"), new KeyMap("이벤트아이디", "EVENT_ID"), new KeyMap("디바이스아이디", "ADAPTER_DIVISION")
	};

	private String eventId;
	private String deviceId;
	
	private String dataPath;
	private String playTime;
	private String sendResponseValue;

	
	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDataPath() {
		return dataPath;
	}

	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
	}

	public String getPlayTime() {
		return playTime;
	}

	public void setPlayTime(String playTime) {
		this.playTime = playTime;
	}

	public String getSendResponseValue() {
		return sendResponseValue;
	}

	public void setSendResponseValue(String sendResponseValue) {
		this.sendResponseValue = sendResponseValue;
	}

	public DMHistory(ResultSet resultSet){
		super();
		if(SmartUtil.isBlankObject(resultSet)) return;
		this.setResult(resultSet);
	}
	
	public Map<String, Object> getDataRecord(){
		
		Map<String, Object> dataRecord = new HashMap<String, Object>();
		KeyMap[] keyMaps = DMHistory.DEVMID_TRACE_FIELDS;
		
		if(!this.isValid()) return null;
		
		for(int i=0; i<keyMaps.length; i++){
			KeyMap keyMap = keyMaps[i];
			if(keyMap.getKey().equals("DATA_PATH"))
				dataRecord.put(keyMap.getId(), this.dataPath);
			else if(keyMap.getKey().equals("PLAY_TIME"))
				dataRecord.put(keyMap.getId(), this.playTime);
			else if(keyMap.getKey().equals("SEND_RESPONSE_VALUE"))
				dataRecord.put(keyMap.getId(), this.sendResponseValue);	
			else if(keyMap.getKey().equals("EVENT_ID"))
				dataRecord.put(keyMap.getId(), this.eventId);	
			else if(keyMap.getKey().equals("ADAPTER_DIVISION"))
				dataRecord.put(keyMap.getId(), this.deviceId);	
		}
		return dataRecord;
//		return UcityTest.getCMHistoryDataRecord();		
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
			this.dataPath = result.getString("DATA_PATH");
		}catch (Exception ex){}
		try{
			this.playTime = result.getString("PLAY_TIME");
		}catch (Exception ex){}
		try{
			this.sendResponseValue = result.getString("SEND_RESPONSE_VALUE");
		}catch (Exception ex){}
		try{
			this.eventId = result.getString("EVENT_ID");
		}catch (Exception ex){}
		try{
			this.deviceId = result.getString("ADAPTER_DIVISION");
		}catch (Exception ex){}
	}

	public boolean isValid(){
		if(!SmartUtil.isBlankObject(this.eventId) && !SmartUtil.isBlankObject(this.deviceId))
			return true;
		return false;
	}

	public static Map<String,Object> readHistoryTable(String eventId, String status, String deviceId){
		
		if(SmartUtil.isBlankObject(eventId)) return null;
//		try {
//			Class.forName(System.DATABASE_JDBC_DRIVE);
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}

		if(SmartUtil.isBlankObject(status)) status = "N";
		Connection con = null;
		PreparedStatement selectPstmt = null;
				
//		String cmHistorySelectSql = (status.equals(FLAG_STOP_DISPLAY)) ? DMHistory.QUERY_SELECT_FOR_STOP_PERFORM : DMHistory.QUERY_SELECT_FOR_DISPLAY_PERFORM;
		String cmHistorySelectSql = (status.equals(FLAG_STOP_DISPLAY)) ? UcityConstant.getQueryByKey("DMHistory.QUERY_SELECT_FOR_STOP_PERFORM") : UcityConstant.getQueryByKey("DMHistory.QUERY_SELECT_FOR_DISPLAY_PERFORM");
		try {
			try{
				//con = DriverManager.getConnection(System.DATABASE_CONNECTION, System.DATABASE_USERNAME, System.DATABASE_PASSWORD);
				con = SwManagerFactory.getInstance().getUcityContantsManager().getDataSource().getConnection();
			}catch (TbSQLException te){
				te.printStackTrace();
				return null;
			}
			
			try{
				selectPstmt = con.prepareStatement(cmHistorySelectSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				selectPstmt.setString(1, eventId);
				selectPstmt.setString(2, deviceId);
				ResultSet rs = selectPstmt.executeQuery();				
				rs.last(); 
				int count = rs.getRow();
				rs.first();
				if (count >= 1) {
					try{
						DMHistory dmHistory = new DMHistory(rs);
						if(dmHistory.isValid()){
							try {
								if (selectPstmt != null)
									selectPstmt.close();
								con.close();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							return dmHistory.getDataRecord();
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
