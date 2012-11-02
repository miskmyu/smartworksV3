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
	
	public static final String FLAG_STOP_DISPLAY 	= "Y";
	
	public static final KeyMap[] DEVMID_TRACE_FIELDS = {
		new KeyMap("상황 아이디", "situation_id"), new KeyMap("미디어보드", "cimbmbd"), new KeyMap("환경VMS", "eveivms"), new KeyMap("교통VMS", "tmemvms"), new KeyMap("교통BIT", "tmembit"), 
		new KeyMap("KIOSK", "tmemkiosk"), new KeyMap("미디어보드 표출중지", "cimbmbd_stop"), new KeyMap("환경VMS 표출중지", "eveivms_stop"), new KeyMap("교통VMS 표출중지", "tmemvms_stop"), new KeyMap("교통BIT 표출중지", "tmembit_stop"), 
		new KeyMap("KIOSK 표출중지", "tmemkiosk_stop")
	};

	private String eventId;
	private String cimbMbd;
	private String tmemBit;
	private String tmemVms;
	private String tmemKiosk;
	private String eveiVms;;
	private String cimbMbdStop;
	private String tmemBitStop;
	private String tmemVmsStop;
	private String tmemKioskStop;
	private String eveiVmsStop;
	
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

	public String getCimbMbdStop() {
		return cimbMbdStop;
	}

	public void setCimbMbdStop(String cimbMbdStop) {
		this.cimbMbdStop = cimbMbdStop;
	}

	public String getTmemBitStop() {
		return tmemBitStop;
	}

	public void setTmemBitStop(String tmemBitStop) {
		this.tmemBitStop = tmemBitStop;
	}

	public String getTmemVmsStop() {
		return tmemVmsStop;
	}

	public void setTmemVmsStop(String tmemVmsStop) {
		this.tmemVmsStop = tmemVmsStop;
	}

	public String getTmemKioskStop() {
		return tmemKioskStop;
	}

	public void setTmemKioskStop(String tmemKioskStop) {
		this.tmemKioskStop = tmemKioskStop;
	}

	public String getEveiVmsStop() {
		return eveiVmsStop;
	}

	public void setEveiVmsStop(String eveiVmsStop) {
		this.eveiVmsStop = eveiVmsStop;
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
			else if(keyMap.getKey().equals("cimbmbd_stop"))
				dataRecord.put(keyMap.getId(), this.cimbMbdStop);
			else if(keyMap.getKey().equals("tmembit_stop"))
				dataRecord.put(keyMap.getId(), this.tmemBitStop);
			else if(keyMap.getKey().equals("tmemvms_stop"))
				dataRecord.put(keyMap.getId(), this.tmemVmsStop);
			else if(keyMap.getKey().equals("tmemkiosk_stop"))
				dataRecord.put(keyMap.getId(), this.tmemKioskStop);
			else if(keyMap.getKey().equals("eveivms_stop"))
				dataRecord.put(keyMap.getId(), this.eveiVmsStop);
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
			if(division.equals(DEVICE_ID_MEDIABOARD)){
				this.cimbMbd = dataPath;
			}else if(division.equals(DEVICE_ID_TRAFFIC_BIT)){
				this.tmemBit = dataPath;
			}else if(division.equals(DEVICE_ID_TRAFFIC_VMS)){
				this.tmemVms =dataPath;
			}else if(division.equals(DEVICE_ID_KIOSK)){
				this.tmemKiosk = dataPath;
			}else if(division.equals(DEVICE_ID_ENV_VMS)){
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
//		try {
//			Class.forName(System.DATABASE_JDBC_DRIVE);
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}

		Map<String, Object> dataRecord = null;

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
				ResultSet rs = selectPstmt.executeQuery();				
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
						we.printStackTrace();
						dataRecord = null;
						count = 0;
					}
				}
				if(dmHistory!=null && dmHistory.isValid())
					return dmHistory.getDataRecord();
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
		if(dataRecord!=null)
			return dataRecord;
		return null;
	}
}
