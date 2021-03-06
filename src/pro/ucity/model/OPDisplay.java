package pro.ucity.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import net.smartworks.model.KeyMap;
import net.smartworks.model.instance.TaskInstance;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.service.factory.SwServiceFactory;
import net.smartworks.util.SmartUtil;

import org.apache.log4j.Logger;

import pro.ucity.util.UcityUtil;

import com.tmax.tibero.jdbc.TbSQLException;

public class OPDisplay {
	
	private static Logger logger = Logger.getLogger(OPDisplay.class);

	public static final KeyMap[] OPPORTAL_DISPLAY_FIELDS = {
		new KeyMap("상황표출 아이디", UcityConstant.getQueryByKey("OPDisplay.DISPLAY_ID")), 
		new KeyMap("상황 아이디", UcityConstant.getQueryByKey("OPDisplay.SITUATION_ID")), 
		new KeyMap("상황표출 내용", UcityConstant.getQueryByKey("OPDisplay.DISPLAY_CONTENT")),
//		new KeyMap("상황표출 이미지경로", UcityConstant.getQueryByKey("OPDisplay.DISPLAY_IMAGE_PATH")), 
//		new KeyMap("상황표출 동영상경로", UcityConstant.getQueryByKey("OPDisplay.DISPLAY_MOVIE_PATH")), 
//		new KeyMap("상황표출시간(초)", UcityConstant.getQueryByKey("OPDisplay.DISPLAY_SECOND")), 
		new KeyMap("미디어보드표출여부", UcityConstant.getQueryByKey("OPDisplay.MB_YN")),
		new KeyMap("환경VMS표출여부", UcityConstant.getQueryByKey("OPDisplay.ENV_VMS_YN")), 
		new KeyMap("교통VMS표출여부", UcityConstant.getQueryByKey("OPDisplay.TRA_VMS_YN")), 
		new KeyMap("BIT표출여부", UcityConstant.getQueryByKey("OPDisplay.BIT_YN")), 
		new KeyMap("KIOSK표출여부", UcityConstant.getQueryByKey("OPDisplay.KIOSK_YN")),
		new KeyMap("표출요청일시", UcityConstant.getQueryByKey("OPDisplay.REQUEST_DATE")), 
		new KeyMap("표출중지", UcityConstant.getQueryByKey("OPDisplay.STOP_REQUEST")), 
		new KeyMap("표출중지일시", UcityConstant.getQueryByKey("OPDisplay.STOP_REQUEST_DATE")), 
//		new KeyMap("표출중지요청아이디", UcityConstant.getQueryByKey("OPDisplay.STOP_REQUEST_USER_ID"))
	};
	
	private String situationId;
	private String displayId;
	private String displayContent;
	private String mbYn;
	private String envVmsYn;
	private String traVmsYn;
	private String bitYn;
	private String kioskYn;
	private String requestDate;
	private String stopRequest;
	private String stopRequestDate;
	
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
		String isSms = "false";
		for(int i=0; i<keyMaps.length; i++){
			KeyMap keyMap = keyMaps[i];
			if(keyMap.getKey().equals(UcityConstant.getQueryByKey("OPDisplay.SITUATION_ID")))
				dataRecord.put(keyMap.getId(), this.situationId);
			else if(keyMap.getKey().equals(UcityConstant.getQueryByKey("OPDisplay.DISPLAY_ID")))
				dataRecord.put(keyMap.getId(), this.displayId);
			else if(keyMap.getKey().equals(UcityConstant.getQueryByKey("OPDisplay.DISPLAY_CONTENT")))
				dataRecord.put(keyMap.getId(), this.displayContent);
			else if(keyMap.getKey().equals(UcityConstant.getQueryByKey("OPDisplay.MB_YN"))){
				dataRecord.put(keyMap.getId(), "Y".equalsIgnoreCase(this.mbYn) ? "on" : "");
				if("Y".equalsIgnoreCase(this.mbYn))
					externalDisplay = externalDisplay + (SmartUtil.isBlankObject(externalDisplay) ? "" : ", ") +  "미디어보드";
			}else if(keyMap.getKey().equals(UcityConstant.getQueryByKey("OPDisplay.ENV_VMS_YN"))){
				dataRecord.put(keyMap.getId(), "Y".equalsIgnoreCase(this.envVmsYn) ? "on" : "");
				if("Y".equalsIgnoreCase(this.envVmsYn))
					externalDisplay = externalDisplay + (SmartUtil.isBlankObject(externalDisplay) ? "" : ", ") +  "환경VMS";
			}else if(keyMap.getKey().equals(UcityConstant.getQueryByKey("OPDisplay.BIT_YN"))){
				dataRecord.put(keyMap.getId(), "Y".equalsIgnoreCase(this.bitYn) ? "on" : "");
				if("Y".equalsIgnoreCase(this.bitYn))
					externalDisplay = externalDisplay + (SmartUtil.isBlankObject(externalDisplay) ? "" : ", ") +  "BIT";
			}else if(keyMap.getKey().equals(UcityConstant.getQueryByKey("OPDisplay.TRA_VMS_YN"))){
				dataRecord.put(keyMap.getId(), "Y".equalsIgnoreCase(this.traVmsYn) ? "on" : "");
				if("Y".equalsIgnoreCase(this.traVmsYn))
					externalDisplay = externalDisplay + (SmartUtil.isBlankObject(externalDisplay) ? "" : ", ") +  "교통VMS";	
			}else if(keyMap.getKey().equals(UcityConstant.getQueryByKey("OPDisplay.KIOSK_YN"))){
				dataRecord.put(keyMap.getId(), "Y".equalsIgnoreCase(this.kioskYn) ? "on" : "");
				if("Y".equalsIgnoreCase(this.kioskYn))
					externalDisplay = externalDisplay + (SmartUtil.isBlankObject(externalDisplay) ? "" : ", ") +  "KIOSK";		
			}else if(keyMap.getKey().equals(UcityConstant.getQueryByKey("OPDisplay.REQUEST_DATE")))
				dataRecord.put(keyMap.getId(), this.requestDate);
		     else if(keyMap.getKey().equals(UcityConstant.getQueryByKey("OPDisplay.STOP_REQUEST")))
				dataRecord.put(keyMap.getId(), "Y".equalsIgnoreCase(this.stopRequest) ? "on" : "");
		     else if(keyMap.getKey().equals(UcityConstant.getQueryByKey("OPDisplay.STOP_REQUEST_DATE")))
				dataRecord.put(keyMap.getId(), this.stopRequestDate);
		}
		
		if(!SmartUtil.isBlankObject(externalDisplay))
			dataRecord.put("externalDisplay", externalDisplay);
		if(!SmartUtil.isBlankObject(isSms))
			dataRecord.put("isSms", isSms);
		
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
			this.situationId = result.getString(UcityConstant.getQueryByKey("OPDisplay.SITUATION_ID"));
		}catch (Exception ex){}
		try{
			this.displayId = result.getString(UcityConstant.getQueryByKey("OPDisplay.DISPLAY_ID"));
		}catch (Exception ex){}
		try{
			this.displayContent = result.getString(UcityConstant.getQueryByKey("OPDisplay.DISPLAY_CONTENT"));
		}catch (Exception ex){}
		try{
			this.mbYn = result.getString(UcityConstant.getQueryByKey("OPDisplay.MB_YN"));
		}catch (Exception ex){}
		try{
			this.envVmsYn = result.getString(UcityConstant.getQueryByKey("OPDisplay.ENV_VMS_YN"));
		}catch (Exception ex){}
		try{
			this.bitYn = result.getString(UcityConstant.getQueryByKey("OPDisplay.BIT_YN"));
		}catch (Exception ex){}
		try{
			this.traVmsYn = result.getString(UcityConstant.getQueryByKey("OPDisplay.TRA_VMS_YN"));
		}catch (Exception ex){}
		try{
			this.kioskYn = result.getString(UcityConstant.getQueryByKey("OPDisplay.KIOSK_YN"));
		}catch (Exception ex){}
		try{
			this.requestDate = result.getString(UcityConstant.getQueryByKey("OPDisplay.REQUEST_DATE"));
		}catch (Exception ex){}
		try{
			this.stopRequest = result.getString(UcityConstant.getQueryByKey("OPDisplay.STOP_REQUEST"));
		}catch (Exception ex){}
		try{
			this.stopRequestDate = result.getString(UcityConstant.getQueryByKey("OPDisplay.STOP_REQUEST_DATE"));
		}catch (Exception ex){}
	}
	
	public boolean isValid(){
		if(!SmartUtil.isBlankObject(this.situationId) && !SmartUtil.isBlankObject(this.displayId)) return true;
		return false;
	}
	
	public static Map<String,Object> checkForDisplay(String eventId, boolean isStopRequest, Map<String,Object> dataRecord){
		
		if(SmartUtil.isBlankObject(eventId) || SmartUtil.isBlankObject(dataRecord)) return dataRecord;

		Connection connection = null;
		PreparedStatement selectPstmt = null;
		PreparedStatement updatePstmt = null;
				
		String opDisplaySelectSql = (isStopRequest) ? UcityConstant.getQueryByKey("OPDisplay.QUERY_SELECT_FOR_STOP_CHECK") : UcityConstant.getQueryByKey("OPDisplay.QUERY_SELECT_FOR_CHECK");
		String opDisplayUpdateSql = (isStopRequest) ? UcityConstant.getQueryByKey("OPDisplay.QUERY_UPDATE_FOR_STOP_READ_CONFIRM") : UcityConstant.getQueryByKey("OPDisplay.QUERY_UPDATE_FOR_READ_CONFIRM");
		try {
			try{
			    Context init = new InitialContext();
			    Context envinit = (Context)init.lookup("java:comp/env");
			    DataSource ds = (DataSource) envinit.lookup("bpm/tibero");
			    connection = ds.getConnection();
//				connection = SwManagerFactory.getInstance().getUcityContantsManager().getDataSource().getConnection();
			}catch (TbSQLException te){
				logger.error("DB Connection errer : OPDisplay");
				return null;
			}
			
			try{
				selectPstmt = connection.prepareStatement(opDisplaySelectSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				selectPstmt.setString(1, eventId);
				ResultSet rs = selectPstmt.executeQuery();		
				rs.setFetchSize(10);
				rs.last(); 
				int count = rs.getRow();
				rs.first();
				if(count>0) {
					try{
						String displayId = rs.getString(UcityConstant.getQueryByKey("OPDisplay.FIELD_NAME_DISPLAY_ID"));
						updatePstmt = connection.prepareStatement(opDisplayUpdateSql);
						updatePstmt.setString(1, eventId);
						boolean result = updatePstmt.execute();
						try {
							if (selectPstmt != null)
								selectPstmt.close();
//							con.close();
						} catch (SQLException e) {
							logger.error("select error : OPDisplay.315");
							// TODO Auto-generated catch block
//							e.printStackTrace();
						}
						dataRecord.put("Display Id", displayId);
						return dataRecord;
					}catch (Exception we){
						logger.error("update error : OPDisplay.322");
//						we.printStackTrace();
					}
				}
			}catch (Exception e1){
				logger.error("select error : OPDisplay.327");
//				e1.printStackTrace();
			}
		} catch (Exception e) {
			logger.error("select error : OPDisplay.331");
//			e.printStackTrace();
		} finally {
			try {
				if (selectPstmt != null)
					selectPstmt.close();
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error("Finally close error : OPDisplay.341");
//				e.printStackTrace();
			}
		}
		return dataRecord;
		
	}
	
	public static boolean checkIfDisplay(String eventId, boolean isStopRequest){

		if( SmartUtil.isBlankObject(eventId)) return false;

		Connection connection = null;
		PreparedStatement selectPstmt = null;
		PreparedStatement updatePstmt = null;
				
		String opDisplaySelectSql = (isStopRequest) ? UcityConstant.getQueryByKey("OPDisplay.QUERY_SELECT_FOR_STOP_CHECK") : UcityConstant.getQueryByKey("OPDisplay.QUERY_SELECT_FOR_CHECK");
		String opDisplayUpdateSql = (isStopRequest) ? UcityConstant.getQueryByKey("OPDisplay.QUERY_UPDATE_FOR_STOP_READ_CONFIRM") : UcityConstant.getQueryByKey("OPDisplay.QUERY_UPDATE_FOR_READ_CONFIRM");
		try {
			try{
			    Context init = new InitialContext();
			    Context envinit = (Context)init.lookup("java:comp/env");
			    DataSource ds = (DataSource) envinit.lookup("bpm/tibero");
			    connection = ds.getConnection();
//				connection = SwManagerFactory.getInstance().getUcityContantsManager().getDataSource().getConnection();
			}catch (TbSQLException te){
				logger.error("DB Connection error : OPDisplay.checkIfDisplay");
//				te.printStackTrace();
				return false;
			}
			
			try{
				selectPstmt = connection.prepareStatement(opDisplaySelectSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				selectPstmt.setString(1, eventId);
				ResultSet rs = selectPstmt.executeQuery();		
				rs.setFetchSize(10);
				rs.last(); 
				int count = rs.getRow();
				rs.first();
				if(count>0) {
					try {
						if (selectPstmt != null)
							selectPstmt.close();
//						con.close();
					} catch (SQLException e) {
						logger.error("Select error : OPDisplay.385");
						// TODO Auto-generated catch block	
//						e.printStackTrace();
					}
					return true;
				}
			}catch (Exception e){
				logger.error("Select error : OPDisplay.392");
//				e.printStackTrace();
			}
		} catch (Exception e) {
			logger.error("Select error : OPDisplay.checkIfDisplay.396");
//			e.printStackTrace();
		} finally {
			try {
				if (selectPstmt != null)
					selectPstmt.close();
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				logger.error("Finally close error : OPDisplay.405");
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
		}
		return false;
		
	}
	
	public static Map<String,Object> readHistoryTable(String eventId, String displayId){
		
		if(SmartUtil.isBlankObject(eventId) || SmartUtil.isBlankObject(displayId)) return null;

		Connection connection = null;
		PreparedStatement selectPstmt = null;
				
		String opDisplaySelectSql = UcityConstant.getQueryByKey("OPDisplay.QUERY_SELECT_FOR_PERFORM");
		try {
			try{
			    Context init = new InitialContext();
			    Context envinit = (Context)init.lookup("java:comp/env");
			    DataSource ds = (DataSource) envinit.lookup("bpm/tibero");
			    connection = ds.getConnection();
//				connection = SwManagerFactory.getInstance().getUcityContantsManager().getDataSource().getConnection();
			}catch (TbSQLException te){
				logger.error("DB Connection error");
//				te.printStackTrace();
				return null;
			}
			
			try{
				selectPstmt = connection.prepareStatement(opDisplaySelectSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				selectPstmt.setString(1, eventId);
				selectPstmt.setString(2, displayId);
				ResultSet rs = selectPstmt.executeQuery();	
				rs.setFetchSize(10);
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
//								con.close();
							} catch (SQLException e) {
								logger.error("OPDisplay.readHistoryTable.452");
								// TODO Auto-generated catch block
//								e.printStackTrace();
							}
							return opDisplay.getDataRecord();
						}
					}catch (Exception we){
						logger.error("OPDisplay.readHistoryTable.459");
//						we.printStackTrace();
					}
				}
			}catch (Exception e){
				logger.error("OPDisplay.readHistoryTable.464");
//				e1.printStackTrace();
			}
		} catch (Exception e) {
			logger.error("OPDisplay.readHistoryTable.468");
//			e.printStackTrace();
		} finally {
			try {
				if (selectPstmt != null)
					selectPstmt.close();
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				logger.error("OPDisplay.readHistoryTable.477");
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
		}
		return null;
	}
}
