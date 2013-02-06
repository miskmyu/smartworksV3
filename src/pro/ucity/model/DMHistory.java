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
import net.smartworks.server.service.factory.SwServiceFactory;
import net.smartworks.util.SmartUtil;

import org.apache.log4j.Logger;

import pro.ucity.util.UcityUtil;

import com.tmax.tibero.jdbc.TbSQLException;

public class DMHistory {

	private static final Logger logger = Logger.getLogger(DMHistory.class);
	
	public static final String DEVICE_ID_MEDIABOARD		= "CIMBMBD";
	public static final String DEVICE_ID_TRAFFIC_BIT	= "TMPTBIS";
	public static final String DEVICE_ID_TRAFFIC_VMS	= "TMEMVMS";
	public static final String DEVICE_ID_KIOSK			= "TMPTKIO";
	public static final String DEVICE_ID_ENV_VMS		= "EVEIVMS";

	public static final String FLAG_STOP_DISPLAY 	= "Y";
	
	public static final KeyMap[] DEVMID_TRACE_FIELDS = {
		new KeyMap("상황 아이디", "situation_id"), 
		new KeyMap("미디어보드", "cimbmbd"), 
		new KeyMap("환경VMS", "eveivms"), 
		new KeyMap("교통VMS", "tmemvms"), 
		new KeyMap("교통BIT", "tmembit"), 
		new KeyMap("KIOSK", "tmemkiosk"),
	};

	private String eventId;
	private String cimbMbd;
	private String tmemBit;
	private String tmemVms;
	private String tmemKiosk;
	private String eveiVms;;
	
	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getCimbMbd() {
		return cimbMbd;
	}

	public void setCimbMbd(String cimbMbd) {
		this.cimbMbd = cimbMbd;
	}

	public String getTmemBit() {
		return tmemBit;
	}

	public void setTmemBit(String tmemBit) {
		this.tmemBit = tmemBit;
	}

	public String getTmemVms() {
		return tmemVms;
	}

	public void setTmemVms(String tmemVms) {
		this.tmemVms = tmemVms;
	}

	public String getTmemKiosk() {
		return tmemKiosk;
	}

	public void setTmemKiosk(String tmemKiosk) {
		this.tmemKiosk = tmemKiosk;
	}

	public String getEveiVms() {
		return eveiVms;
	}

	public void setEveiVms(String eveiVms) {
		this.eveiVms = eveiVms;
	}

	public DMHistory(){
		super();
	}
	
	public Map<String, Object> getDataRecord(){
		
		Map<String, Object> dataRecord = new HashMap<String, Object>();
		KeyMap[] keyMaps = DMHistory.DEVMID_TRACE_FIELDS;
		
		if(!this.isValid()) return null;
		for(int i=0; i<keyMaps.length; i++){
			KeyMap keyMap = keyMaps[i];
			if(keyMap.getKey().equals("situation_id"))
				dataRecord.put(keyMap.getId(), this.eventId);
			else if(keyMap.getKey().equals("cimbmbd"))
				dataRecord.put(keyMap.getId(), this.cimbMbd);
			else if(keyMap.getKey().equals("tmembit"))
				dataRecord.put(keyMap.getId(), this.tmemBit);
			else if(keyMap.getKey().equals("tmemvms"))
				dataRecord.put(keyMap.getId(), this.tmemVms);
			else if(keyMap.getKey().equals("tmemkiosk"))
				dataRecord.put(keyMap.getId(), this.tmemKiosk);
			else if(keyMap.getKey().equals("eveivms"))
				dataRecord.put(keyMap.getId(), this.eveiVms);
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
		
		String division = null;
		String dataPath = null;
		try{
			this.eventId = result.getString(UcityConstant.getQueryByKey("DMHistory.EVENT_ID"));
		}catch (Exception ex){}
		try{
			division = result.getString(UcityConstant.getQueryByKey("DMHistory.ADAPTER_DIVISION"));
		}catch (Exception ex){}
		try{
			dataPath = result.getString(UcityConstant.getQueryByKey("DMHistory.DATA_PATH"));
		}catch (Exception ex){}
		
		if(!SmartUtil.isBlankObject(division)){
			if(SmartUtil.isBlankObject(this.cimbMbd) && division.equals(DEVICE_ID_MEDIABOARD)){
				this.cimbMbd = dataPath;
			}else if(SmartUtil.isBlankObject(this.tmemBit) && division.equals(DEVICE_ID_TRAFFIC_BIT)){
				this.tmemBit = dataPath;
			}else if(SmartUtil.isBlankObject(this.tmemVms) && division.equals(DEVICE_ID_TRAFFIC_VMS)){
				this.tmemVms =dataPath;
			}else if(SmartUtil.isBlankObject(this.tmemKiosk) && division.equals(DEVICE_ID_KIOSK)){
				this.tmemKiosk = dataPath;
			}else if(SmartUtil.isBlankObject(this.eveiVms) && division.equals(DEVICE_ID_ENV_VMS)){
				this.eveiVms = dataPath;
			}
		}
	}

	public boolean isValid(){
		if(!SmartUtil.isBlankObject(this.eventId))
			return true;
		return false;
	}

	public static Map<String,Object> readHistoryTable(String eventId, String status, String deviceId){
		
		if(SmartUtil.isBlankObject(eventId)) return null;

		Map<String, Object> dataRecord = null;

		if(SmartUtil.isBlankObject(status)) status = "N";
		Connection connection = null;
		PreparedStatement selectPstmt = null;
				
		String cmHistorySelectSql = (status.equals(FLAG_STOP_DISPLAY)) ? UcityConstant.getQueryByKey("DMHistory.QUERY_SELECT_FOR_STOP_PERFORM") : UcityConstant.getQueryByKey("DMHistory.QUERY_SELECT_FOR_DISPLAY_PERFORM");
		try {
			try{
			    Context init = new InitialContext();
			    Context envinit = (Context)init.lookup("java:comp/env");
			    DataSource ds = (DataSource) envinit.lookup("bpm/tibero");
			    connection = ds.getConnection();
			}catch (TbSQLException te){
				logger.error("DB Connection error : DMHistory.readHistoryTable");
				return null;
			}		
			try{
				selectPstmt = connection.prepareStatement(cmHistorySelectSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				selectPstmt.setString(1, eventId);
				ResultSet rs = selectPstmt.executeQuery();	
				rs.setFetchSize(10);
				rs.last(); 
				int count = rs.getRow();
				rs.first();
				DMHistory dmHistory = null;
				if(count >=1){
					dmHistory = new DMHistory();
				}

				while(count>=1) {
					dmHistory.setResult(rs);
					try{
						rs.next();
						count = rs.getRow();
					}catch (Exception we){
						logger.error("Result set error : DMHistory.readHistoryTable");
						dataRecord = null;
						count = 0;
					}
				}
				if(dmHistory!=null && dmHistory.isValid())
					return dmHistory.getDataRecord();
			}catch (Exception e){
				logger.error("select error : DMHistory.277");
			}
		} catch (Exception e) {
			logger.error("select error : DMHistory.281");
		} finally {
			try {
				if (selectPstmt != null)
					selectPstmt.close();
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				logger.error("Finally error : DMHistory.291");
			}
		}
		if(dataRecord!=null)
			return dataRecord;
		return null;
	}
}
