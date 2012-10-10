package pro.ucity.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import pro.ucity.util.UcityTest;
import pro.ucity.util.UcityUtil;
import net.smartworks.model.KeyMap;
import net.smartworks.model.instance.TaskInstance;
import net.smartworks.server.service.factory.SwServiceFactory;
import net.smartworks.util.SmartUtil;

public class OPDisplay {

	public static final String FIELD_NAME_DISPLAY_ID = "DISPLAY_ID";
	public static final String FIELD_NAME_SITUATION_ID = "SITUATION_ID";
	public static final String FIELD_NAME_READ_CONFIRM = "BPM_CNFM_YN";
	public static final String FIELD_NAME_REQUEST_DATE = "REQUEST_DATE";

	public static final String QUERY_SELECT_FOR_CHECK = "select * from " + System.TABLE_NAME_OPPORTAL_DISPLAY + " where " + FIELD_NAME_SITUATION_ID + " = ? and (" + FIELD_NAME_READ_CONFIRM + " != 'Y' or " + FIELD_NAME_READ_CONFIRM + " is null) order by " + FIELD_NAME_REQUEST_DATE + " desc";
	public static final String QUERY_UPDATE_FOR_READ_CONFIRM = "update " + System.TABLE_NAME_OPPORTAL_DISPLAY + " set " + FIELD_NAME_READ_CONFIRM + " = 'Y' where " + FIELD_NAME_SITUATION_ID + " = ? ";
	public static final String QUERY_SELECT_FOR_PERFORM = "select * from " + System.TABLE_NAME_OPPORTAL_DISPLAY + " where " + FIELD_NAME_SITUATION_ID + " = ? and " + FIELD_NAME_DISPLAY_ID + " = ?";

	public static final KeyMap[] OPPORTAL_DISPLAY_FIELDS = {
		new KeyMap("상황표출 아이디", "DISPLAY_ID"), new KeyMap("상황 아이디", "SITUATION_ID"), new KeyMap("상황표출 내용", "DISPLAY_CONTENT"),
		new KeyMap("상황표출 이미지경로", "DISPLAY_IMAGE_PATH"), new KeyMap("상황표출 동영상경로", "DISPLAY_MOVIE_PATH"), new KeyMap("상황표출시간(초)", "DISPLAY_SECOND"), new KeyMap("미디어보드표출여부", "MB_YN"),
		new KeyMap("환경VMS표출여부", "ENV_VMS_YN"), new KeyMap("교통VMS표출여부", "TRA_VMS_YN"), new KeyMap("BIT표출여부", "BIT_YN"), new KeyMap("KIOSK표출여부", "KIOSK_YN"),
		new KeyMap("표출요청일시", "REQUEST_DATE"), new KeyMap("표출중지", "STOP_REQUEST"), new KeyMap("표출중지일시", "STOP_REQUEST_DATE"), new KeyMap("표출중지요청아이디", "STOP_REQUEST_USER_ID")
	};
	
	private String situationId;
	private String displayId;
	private String displayContent;
	private String displayImagePath;
	private String displayMoviePath;
	private String displaySecond;
	private String mbYn;
	private String envVmsYn;
	private String traVmsYn;
	private String bitYn;
	private String kioskYn;
	private String requestDate;
	private String stopRequest;
	private String stopRequestDate;
	private String stopRequestUserId;
	
	public String getSituationId() {
		return situationId;
	}
	public void setSituationId(String situationId) {
		this.situationId = situationId;
	}
	public String getDisplayId() {
		return displayId;
	}
	public void setDisplayId(String displayId) {
		this.displayId = displayId;
	}
	public String getDisplayContent() {
		return displayContent;
	}
	public void setDisplayContent(String displayContent) {
		this.displayContent = displayContent;
	}
	public String getDisplayImagePath() {
		return displayImagePath;
	}
	public void setDisplayImagePath(String displayImagePath) {
		this.displayImagePath = displayImagePath;
	}
	public String getDisplayMoviePath() {
		return displayMoviePath;
	}
	public void setDisplayMoviePath(String displayMoviePath) {
		this.displayMoviePath = displayMoviePath;
	}
	public String getDisplaySecond() {
		return displaySecond;
	}
	public void setDisplaySecond(String displaySecond) {
		this.displaySecond = displaySecond;
	}
	public String getMbYn() {
		return mbYn;
	}
	public void setMbYn(String mbYn) {
		this.mbYn = mbYn;
	}
	public String getEnvVmsYn() {
		return envVmsYn;
	}
	public void setEnvVmsYn(String envVmsYn) {
		this.envVmsYn = envVmsYn;
	}
	public String getTraVmsYn() {
		return traVmsYn;
	}
	public void setTraVmsYn(String traVmsYn) {
		this.traVmsYn = traVmsYn;
	}
	public String getKioskYn() {
		return kioskYn;
	}
	public void setKioskYn(String kioskYn) {
		this.kioskYn = kioskYn;
	}
	public String getBitYn() {
		return bitYn;
	}
	public void setBitYn(String bitYn) {
		this.bitYn = bitYn;
	}
	public String getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}
	public String getStopRequest() {
		return stopRequest;
	}
	public void setStopRequest(String stopRequest) {
		this.stopRequest = stopRequest;
	}
	public String getStopRequestDate() {
		return stopRequestDate;
	}
	public void setStopRequestDate(String stopRequestDate) {
		this.stopRequestDate = stopRequestDate;
	}
	public String getStopRequestUserId() {
		return stopRequestUserId;
	}
	public void setStopRequestUserId(String stopRequstUserId) {
		this.stopRequestUserId = stopRequestUserId;
	}
	public OPDisplay(ResultSet resultSet){
		super();
		if(SmartUtil.isBlankObject(resultSet)) return;
		this.setResult(resultSet);
	}
	
	public Map<String, Object> getDataRecord(){
		
		Map<String, Object> dataRecord = new HashMap<String, Object>();
		KeyMap[] keyMaps = OPDisplay.OPPORTAL_DISPLAY_FIELDS;

		if(!this.isValid()) return null;
		
		String externalDisplay = "";
		String isSms = "";
		for(int i=0; i<keyMaps.length; i++){
			KeyMap keyMap = keyMaps[i];
			if(keyMap.getKey().equals("SITUATION_ID"))
				dataRecord.put(keyMap.getId(), this.situationId);
			else if(keyMap.getKey().equals("DISPLAY_ID"))
				dataRecord.put(keyMap.getId(), this.displayId);
			else if(keyMap.getKey().equals("DISPLAY_CONTENT"))
				dataRecord.put(keyMap.getId(), this.displayContent);
			else if(keyMap.getKey().equals("DISPLAY_IMAGE_PATH"))
				dataRecord.put(keyMap.getId(), this.displayImagePath);
			else if(keyMap.getKey().equals("DISPLAY_MOVIE_PATH"))
				dataRecord.put(keyMap.getId(), this.displayMoviePath);
			else if(keyMap.getKey().equals("DISPLAY_SECOND"))
				dataRecord.put(keyMap.getId(), this.displaySecond);
			else if(keyMap.getKey().equals("MB_YN")){
				dataRecord.put(keyMap.getId(), "Y".equalsIgnoreCase(this.mbYn) ? "on" : "");
				if("Y".equalsIgnoreCase(this.mbYn))
					externalDisplay = externalDisplay + (SmartUtil.isBlankObject(externalDisplay) ? "" : ", ") +  "미디어보드";
			}else if(keyMap.getKey().equals("ENV_VMS_YN")){
				dataRecord.put(keyMap.getId(), "Y".equalsIgnoreCase(this.envVmsYn) ? "on" : "");
				if("Y".equalsIgnoreCase(this.envVmsYn))
					externalDisplay = externalDisplay + (SmartUtil.isBlankObject(externalDisplay) ? "" : ", ") +  "환경VMS";
			}else if(keyMap.getKey().equals("BIT_YN")){
				dataRecord.put(keyMap.getId(), "Y".equalsIgnoreCase(this.bitYn) ? "on" : "");
				if("Y".equalsIgnoreCase(this.bitYn))
					externalDisplay = externalDisplay + (SmartUtil.isBlankObject(externalDisplay) ? "" : ", ") +  "BIT";
			}else if(keyMap.getKey().equals("TRA_VMS_YN")){
				dataRecord.put(keyMap.getId(), "Y".equalsIgnoreCase(this.traVmsYn) ? "on" : "");
				if("Y".equalsIgnoreCase(this.traVmsYn))
					externalDisplay = externalDisplay + (SmartUtil.isBlankObject(externalDisplay) ? "" : ", ") +  "교통VMS";	
			}else if(keyMap.getKey().equals("KIOSK_YN")){
				dataRecord.put(keyMap.getId(), "Y".equalsIgnoreCase(this.kioskYn) ? "on" : "");
				if("Y".equalsIgnoreCase(this.kioskYn))
					externalDisplay = externalDisplay + (SmartUtil.isBlankObject(externalDisplay) ? "" : ", ") +  "KIOSK";		
			}else if(keyMap.getKey().equals("REQUEST_DATE"))
				dataRecord.put(keyMap.getId(), this.requestDate);
		     else if(keyMap.getKey().equals("STOP_REQUEST"))
				dataRecord.put(keyMap.getId(), "Y".equalsIgnoreCase(this.stopRequest) ? "on" : "");
		     else if(keyMap.getKey().equals("STOP_REQUEST_DATE"))
				dataRecord.put(keyMap.getId(), this.stopRequestDate);
		     else if(keyMap.getKey().equals("STOP_REQUEST_USER_ID"))
				dataRecord.put(keyMap.getId(), this.stopRequestUserId);
		}
		
		if(!SmartUtil.isBlankObject(externalDisplay))
			dataRecord.put("externalDisplay", externalDisplay);
		if(!SmartUtil.isBlankObject(isSms))
			dataRecord.put("isSms", isSms);
		
		return dataRecord;
//		return UcityTest.getOPDisplayDataRecord();
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
			this.displayId = result.getString("DISPLAY_ID");
		}catch (Exception ex){}
		try{
			this.displayContent = result.getString("DISPLAY_CONTENT");
		}catch (Exception ex){}
		try{
			this.displayImagePath = result.getString("DISPLAY_IMAGE_PATH");
		}catch (Exception ex){}
		try{
			this.displayMoviePath = result.getString("DISPLAY_MOVIE_PATH");
		}catch (Exception ex){}
		try{
			this.displaySecond = result.getString("DISPLAY_SECOND");
		}catch (Exception ex){}
		try{
			this.mbYn = result.getString("MB_YN");
		}catch (Exception ex){}
		try{
			this.envVmsYn = result.getString("ENV_VMS_YN");
		}catch (Exception ex){}
		try{
			this.bitYn = result.getString("BIT_YN");
		}catch (Exception ex){}
		try{
			this.traVmsYn = result.getString("TRA_VMS_YN");
		}catch (Exception ex){}
		try{
			this.kioskYn = result.getString("KIOSK_YN");
		}catch (Exception ex){}
		try{
			this.requestDate = result.getString("REQUEST_DATE");
		}catch (Exception ex){}
		try{
			this.requestDate = result.getString("STOP_REQUEST");
		}catch (Exception ex){}
		try{
			this.requestDate = result.getString("STOP_REQUEST_DATE");
		}catch (Exception ex){}
		try{
			this.requestDate = result.getString("STOP_REQUEST_USER_ID");
		}catch (Exception ex){}
	}
	
	public boolean isValid(){
		if(!SmartUtil.isBlankObject(this.situationId) && !SmartUtil.isBlankObject(this.displayId)) return true;
		return false;
	}
	
	public static Map<String,Object> checkForDisplay(String eventId, Map<String,Object> dataRecord){
		
		if(SmartUtil.isBlankObject(eventId) || SmartUtil.isBlankObject(dataRecord)) return dataRecord;
		try {
			Class.forName(System.DATABASE_JDBC_DRIVE);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Connection con = null;
		PreparedStatement selectPstmt = null;
		PreparedStatement updatePstmt = null;
				
		String opDisplaySelectSql = OPDisplay.QUERY_SELECT_FOR_CHECK;
		String opDisplayUpdateSql = OPDisplay.QUERY_UPDATE_FOR_READ_CONFIRM;
		try {
			
			con = DriverManager.getConnection(System.DATABASE_CONNECTION, System.DATABASE_USERNAME, System.DATABASE_PASSWORD);
			
			try{
				selectPstmt = con.prepareStatement(opDisplaySelectSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				selectPstmt.setString(1, eventId);
				ResultSet rs = selectPstmt.executeQuery();				
				rs.last(); 
				int count = rs.getRow();
				rs.first();
				if(count>0) {
					try{
						String displayId = rs.getString(OPDisplay.FIELD_NAME_DISPLAY_ID);
						updatePstmt = con.prepareStatement(opDisplayUpdateSql);
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
						dataRecord.put("Display Id", displayId);
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
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dataRecord;
		
	}
	
	public static Map<String,Object> readHistoryTable(String eventId, String displayId){
		
		if(SmartUtil.isBlankObject(eventId) || SmartUtil.isBlankObject(displayId)) return null;
		try {
			Class.forName(System.DATABASE_JDBC_DRIVE);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Connection con = null;
		PreparedStatement selectPstmt = null;
				
		String opDisplaySelectSql = OPDisplay.QUERY_SELECT_FOR_PERFORM;
		try {
			
			con = DriverManager.getConnection(System.DATABASE_CONNECTION, System.DATABASE_USERNAME, System.DATABASE_PASSWORD);
			
			try{
				selectPstmt = con.prepareStatement(opDisplaySelectSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				selectPstmt.setString(1, eventId);
				selectPstmt.setString(2, displayId);
				ResultSet rs = selectPstmt.executeQuery();				
				rs.last(); 
				int count = rs.getRow();
				rs.first();
				if (count == 1) {
					try{
						OPDisplay opDisplay = new OPDisplay(rs);
						if(opDisplay.isValid()){
							try {
								if (selectPstmt != null)
									selectPstmt.close();
								con.close();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							return opDisplay.getDataRecord();
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
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}
