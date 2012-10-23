package pro.ucity.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tmax.tibero.jdbc.TbSQLException;

import pro.ucity.util.UcityUtil;
import net.smartworks.model.KeyMap;
import net.smartworks.model.instance.TaskInstance;
import net.smartworks.model.work.ProcessWork;
import net.smartworks.server.service.INoticeService;
import net.smartworks.server.service.IWorkService;
import net.smartworks.server.service.factory.SwServiceFactory;
import net.smartworks.util.SmartUtil;

public class Adapter {

	public static final String FIELD_SEPERATOR = "\\|\\|";
	public static final int LENGTH_COMM_HEADER = 50;
	public static final int LENGTH_SERVICE_CODE = 4;
	public static final int LENGTH_EVENT_CODE = 4;
	public static final int LENGTH_EVENT_TYPE = 2;
	
	public static final int POS_SERVICE_CODE = 33;
	public static final int POS_EVENT_CODE = 37;
	public static final int POS_EVENT_TYPE = 41;
	
	public static final int EVENT_TYPE_OCCURRENCE = 1;
	public static final int EVENT_TYPE_RELEASE = 2;
	//dev
	public static final String FIELD_NAME_COMM_TG_ID = "CMNC_TG_ID";
	public static final String FIELD_NAME_COMM_CONTENT = "CMNC_TG_CONT";
	public static final String FIELD_NAME_READ_CONFIRM = "BPM_CNFM_YN";
	public static final String FIELD_NAME_DVSN_TYPE = "COMM_DVSN_CD";
	//개발
//	public static final String FIELD_NAME_COMM_TG_ID = "RECV_CMNC_TG_ID";
//	public static final String FIELD_NAME_COMM_CONTENT = "CMNC_TG_CONT";
//	public static final String FIELD_NAME_READ_CONFIRM = "BPM_CNFM_YN";
//	public static final String FIELD_NAME_DVSN_TYPE = "COMM_DVSN_CD";
	
	public static final String DVSN_RECV_TYPE = "RECV";
//  public static final String DVSN_SEND_TYPE = "SEND";
 	
	public static final String QUERY_SELECT_FOR_START = "select * from " + System.TABLE_NAME_ADAPTER_HISTORY + " where (" + FIELD_NAME_READ_CONFIRM + " != 'Y' or " + FIELD_NAME_READ_CONFIRM + " is null) and " + FIELD_NAME_DVSN_TYPE + " = '" + DVSN_RECV_TYPE + "'";
	public static final String QUERY_SELECT_FOR_PERFORM = "select * from " + System.TABLE_NAME_ADAPTER_HISTORY + " where (" + FIELD_NAME_READ_CONFIRM + " != 'Y' or " + FIELD_NAME_READ_CONFIRM + " is null) and " + FIELD_NAME_DVSN_TYPE + " = '" + DVSN_RECV_TYPE + "'";
	public static final String QUERY_UPDATE_FOR_READ_CONFIRM = "update " + System.TABLE_NAME_ADAPTER_HISTORY + " set " + FIELD_NAME_READ_CONFIRM + " = 'Y' where " + FIELD_NAME_COMM_TG_ID + " = ?";

	public static final KeyMap[][] ADAPTER_HISTORY_FIELDS = {
		{new KeyMap("이벤트 ID", "event_id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("특보분류", "env_event_type"), new KeyMap("발생내용", "event_content")},
		{new KeyMap("이벤트 ID", "event_id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name"), new KeyMap("오염물질수", "pollution_number"), new KeyMap("오염물질구분", "pollution_type"), new KeyMap("오염물질측정치", "pollution_value"), new KeyMap("오염등급", "pollution_level"), new KeyMap("오염물질 예/경보구분", "pollution_example")},
		{new KeyMap("이벤트 ID", "event_id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name")},
		{new KeyMap("이벤트 ID", "event_id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name"), new KeyMap("차량번호", "car_number"), new KeyMap("차량차종", "car_type"), new KeyMap("범죄유형", "crime_code")},
		{new KeyMap("이벤트 ID", "event_id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("노트링크 시작 ID", "link_start_id"), new KeyMap("노트링크 종료 ID", "link_end_id"), new KeyMap("돌발상황 유형", "outbreak_type"), new KeyMap("돌발상황 코드", "outbreak_code")},
		{new KeyMap("이벤트 ID", "event_id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("탐지분류", "search_type")},
		{new KeyMap("이벤트 ID", "event_id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name")},
		{new KeyMap("이벤트 ID", "event_id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name"), new KeyMap("차량번호", "car_number"), new KeyMap("차량차종", "car_type"), new KeyMap("범죄유형", "crime_code")},
		{new KeyMap("이벤트 ID", "event_id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name"), new KeyMap("임계치 값", "threshold_value")},
		{new KeyMap("이벤트 ID", "event_id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name"), new KeyMap("시설물이상구분", "facility_type")}		
	};
	
	private String commHeader;
	private String commBody;
	
	private int process=-1;
	private int eventType;	
	private String serviceCode;
	private String eventCode;
	private String communicationId;
	
	private String eventId;
	private String occuredDate;
	private String envEventType;
	private String eventContent;
	private String facilityId;
	private String locationName;
	private String pollutionNumber;
	private String pollutionType;
	private String pollutionValue;
	private String pollutionLevel;
	private String pollutionExample;
	private String carNumber;
	private String carType;
	private String crimeCode;
	private String linkStartId;
	private String linkEndId;
	private String outbreakType;
	private String outbreakCode;
	private String searchType;
	private String thresholdValue;
	private String facilityType;
	
	public String getCommHeader() {
		return commHeader;
	}
	public void setCommHeader(String commHeader) {
		this.commHeader = commHeader;
	}
	public String getCommBody() {
		return commBody;
	}
	public void setCommBody(String commBody) {
		this.commBody = commBody;
	}
	public int getProcess() {
		return process;
	}
	public void setProcess(int process) {
		this.process = process;
	}
	public int getEventType() {
		return eventType;
	}
	public void setEventType(int eventType) {
		this.eventType = eventType;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getEventCode() {
		return eventCode;
	}
	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}
	public String getCommunicationId() {
		return communicationId;
	}
	public void setCommunicationId(String communicationId) {
		this.communicationId = communicationId;
	}
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public String getOccuredDate() {
		return occuredDate;
	}
	public void setOccuredDate(String occuredDate) {
		if(!SmartUtil.isBlankObject(occuredDate) && occuredDate.length()==12)
			this.occuredDate = occuredDate.substring(0,4) + "-" + occuredDate.substring(4,2) + "-" + occuredDate.substring(6,2) + " " + occuredDate.substring(8,2) + ":" + occuredDate.substring(10,2);
		else
			this.occuredDate = occuredDate;
	}
	public String getEnvEventType() {
		return envEventType;
	}
	public void setEnvEventType(String envEventType) {
		this.envEventType = envEventType;
	}
	public String getEventContent() {
		return eventContent;
	}
	public void setEventContent(String eventContent) {
		this.eventContent = eventContent;
	}
	public String getFacilityId() {
		return facilityId;
	}
	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}	
	public String getPollutionNumber() {
		return pollutionNumber;
	}
	public void setPollutionNumber(String pollutionNumber) {
		this.pollutionNumber = pollutionNumber;
	}
	public String getPollutionType() {
		return pollutionType;
	}
	public void setPollutionType(String pollutionType) {
		this.pollutionType = pollutionType;
	}
	public String getPollutionValue() {
		return pollutionValue;
	}
	public void setPollutionValue(String pollutionValue) {
		this.pollutionValue = pollutionValue;
	}
	public String getPollutionLevel() {
		return pollutionLevel;
	}
	public void setPollutionLevel(String pollutionLevel) {
		this.pollutionLevel = pollutionLevel;
	}
	public String getPollutionExample() {
		return pollutionExample;
	}
	public void setPollutionExample(String pollutionExample) {
		this.pollutionExample = pollutionExample;
	}
	public String getCarNumber() {
		return carNumber;
	}
	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}
	public String getCarType() {
		return carType;
	}
	public void setCarType(String carType) {
		this.carType = carType;
	}
	public String getCrimeCode() {
		return crimeCode;
	}
	public void setCrimeCode(String crimeCode) {
		this.crimeCode = crimeCode;
	}
	public String getLinkStartId() {
		return linkStartId;
	}
	public void setLinkStartId(String linkStartId) {
		this.linkStartId = linkStartId;
	}
	public String getLinkEndId() {
		return linkEndId;
	}
	public void setLinkEndId(String linkEndId) {
		this.linkEndId = linkEndId;
	}
	public String getOutbreakType() {
		return outbreakType;
	}
	public void setOutbreakType(String outbreakType) {
		this.outbreakType = outbreakType;
	}
	public String getOutbreakCode() {
		return outbreakCode;
	}
	public void setOutbreakCode(String outbreakCode) {
		this.outbreakCode = outbreakCode;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public String getThresholdValue() {
		return thresholdValue;
	}
	public void setThresholdValue(String thresholdValue) {
		this.thresholdValue = thresholdValue;
	}
	public String getFacilityType() {
		return facilityType;
	}
	public void setFacilityType(String facilityType) {
		this.facilityType = facilityType;
	}
	public Adapter(String commHeader, String commBody){
		super();
		this.commHeader = commHeader;
		this.commBody = commBody;
		parseCommHeader(commHeader);
		parseCommBody(commBody);
	}

	public Adapter(ResultSet resultSet){
		super();
		if(SmartUtil.isBlankObject(resultSet)) return;
		this.setResult(resultSet);
	}
	private void parseCommHeader(String commHeader){
		int sizeHeader = commHeader.length();
		if(SmartUtil.isBlankObject(commHeader) || commHeader.length() != LENGTH_COMM_HEADER) return;
		
		this.serviceCode = commHeader.substring(Adapter.POS_SERVICE_CODE, Adapter.POS_SERVICE_CODE+Adapter.LENGTH_SERVICE_CODE);
		this.eventCode = commHeader.substring(Adapter.POS_EVENT_CODE, Adapter.POS_EVENT_CODE+Adapter.LENGTH_EVENT_CODE);
		String eventType = commHeader.substring(Adapter.POS_EVENT_TYPE, Adapter.POS_EVENT_TYPE+Adapter.LENGTH_EVENT_TYPE);
		
		this.process = Event.getProcessByEventId(this.eventCode);
		
		if(eventType.equals(Event.TYPE_OCCURRENCE))
			this.eventType = EVENT_TYPE_OCCURRENCE;
		if(eventType.equals(Event.TYPE_RELEASE))
			this.eventType = EVENT_TYPE_RELEASE;
	
	}
	private void parseCommBody(String commBody){
		if(SmartUtil.isBlankObject(commBody) || this.process<0 || this.process>System.MAX_PROCESS) return;
		
		String[] tokens = commBody.split(Adapter.FIELD_SEPERATOR);
		if(tokens.length != ADAPTER_HISTORY_FIELDS[process].length) return;
		
		switch(process){
		case System.PROCESS_ENV_WEAHTER:
			this.eventId = tokens[0];
			this.occuredDate = tokens[1];
			this.envEventType = tokens[2];
			this.eventContent = tokens[3];
			break;
		case System.PROCESS_ENV_ATMOSPHERE:
			this.eventId = tokens[0];
			this.occuredDate = tokens[1];
			this.facilityId = tokens[2];
			this.locationName = tokens[3];
			this.pollutionNumber = tokens[4];
			this.pollutionType = tokens[5];
			this.pollutionValue = tokens[6];
			this.pollutionLevel = tokens[7];
			this.pollutionExample = tokens[8];
			break;
		case System.PROCESS_ENV_WATER:
			this.eventId = tokens[0];
			this.occuredDate = tokens[1];
			this.facilityId = tokens[2];
			this.locationName = tokens[3];
			break;
		case System.PROCESS_TRAFFIC_ILLEGAL_PARKING:
			this.eventId = tokens[0];
			this.occuredDate = tokens[1];
			this.facilityId = tokens[2];
			this.locationName = tokens[3];
			this.carNumber = tokens[4];
			this.carType = tokens[5];
			this.crimeCode = tokens[6];
			break;
		case System.PROCESS_TRAFFIC_INCIDENT:
			this.eventId = tokens[0];
			this.occuredDate = tokens[1];
			this.linkStartId = tokens[2];
			this.linkEndId = tokens[3];
			this.outbreakType = tokens[4];
			this.outbreakCode = tokens[5];
			break;
		case System.PROCESS_DISASTER_FIRE:
			this.eventId = tokens[0];
			this.occuredDate = tokens[1];
			this.facilityId = tokens[2];
			this.searchType = tokens[3];
			break;
		case System.PROCESS_CRIME_CCTV:
			this.eventId = tokens[0];
			this.occuredDate = tokens[1];
			this.facilityId = tokens[2];
			this.locationName = tokens[3];
			break;
		case System.PROCESS_CRIME_VEHICLES:
			this.eventId = tokens[0];
			this.occuredDate = tokens[1];
			this.facilityId = tokens[2];
			this.locationName = tokens[3];
			this.carNumber = tokens[4];
			this.carType = tokens[5];
			this.crimeCode = tokens[6];
			break;
		case System.PROCESS_WATERWORKS_LEAKS:
			this.eventId = tokens[0];
			this.occuredDate = tokens[1];
			this.facilityId = tokens[2];
			this.locationName = tokens[3];
			this.thresholdValue = tokens[4];
			break;
		case System.PROCESS_FACILITY_MANAGEMENT:
			this.eventId = tokens[0];
			this.occuredDate = tokens[1];
			this.facilityId = tokens[2];
			this.locationName = tokens[3];
			this.facilityType = tokens[4];
			break;
		}
	}
	
	public String getServiceName(){
		return Service.getServiceNameByCode(this.serviceCode);
	}
	
	public String getEventName(){
		return Event.getEventNameByCode(this.eventCode);
	}
	
	public Map<String, Object> getDataRecord(){
		if(this.process<0 || this.process>System.MAX_PROCESS) return null;
		
		Map<String, Object> dataRecord = new HashMap<String, Object>();
		KeyMap[] keyMaps = Adapter.ADAPTER_HISTORY_FIELDS[this.process];
		
		if(!this.isValid()) return null;
		
		dataRecord.put("serviceName", Service.getServiceNameByCode(this.getServiceCode()));
		if(this.process == System.PROCESS_ENV_WEAHTER)
			dataRecord.put("eventName", this.envEventType);
		else
			dataRecord.put("eventName", Event.getEventNameByCode(this.getEventCode()));
		dataRecord.put("eventPlace", this.locationName);
		
		for(int i=0; i<keyMaps.length; i++){
			KeyMap keyMap = keyMaps[i];
			if(keyMap.getKey().equals("event_id"))
				dataRecord.put(keyMap.getId(), this.eventId);
			else if(keyMap.getKey().equals("occured_date"))
				dataRecord.put(keyMap.getId(), this.occuredDate);
			else if(keyMap.getKey().equals("env_event_type"))
				dataRecord.put(keyMap.getId(), this.envEventType);
			else if(keyMap.getKey().equals("event_content"))
				dataRecord.put(keyMap.getId(), this.eventContent);
			else if(keyMap.getKey().equals("facility_id"))
				dataRecord.put(keyMap.getId(), this.facilityId);
			else if(keyMap.getKey().equals("facility_type"))
				dataRecord.put(keyMap.getId(), this.facilityType);
			else if(keyMap.getKey().equals("location_name"))
				dataRecord.put(keyMap.getId(), this.locationName);
			else if(keyMap.getKey().equals("pollution_number"))
				dataRecord.put(keyMap.getId(), this.pollutionNumber);
			else if(keyMap.getKey().equals("pollution_type"))
				dataRecord.put(keyMap.getId(), this.pollutionType);
			else if(keyMap.getKey().equals("pollution_value"))
				dataRecord.put(keyMap.getId(), this.pollutionValue);
			else if(keyMap.getKey().equals("pollution_level"))
				dataRecord.put(keyMap.getId(), this.pollutionLevel);
			else if(keyMap.getKey().equals("pollution_example"))
				dataRecord.put(keyMap.getId(), this.pollutionExample);
			else if(keyMap.getKey().equals("car_number"))
				dataRecord.put(keyMap.getId(), this.carNumber);
			else if(keyMap.getKey().equals("car_type"))
				dataRecord.put(keyMap.getId(), this.carType);
			else if(keyMap.getKey().equals("crime_code"))
				dataRecord.put(keyMap.getId(), this.crimeCode);
			else if(keyMap.getKey().equals("link_start_id"))
				dataRecord.put(keyMap.getId(), this.linkStartId);
			else if(keyMap.getKey().equals("link_end_id"))
				dataRecord.put(keyMap.getId(), this.linkEndId);
			else if(keyMap.getKey().equals("outbreak_type"))
				dataRecord.put(keyMap.getId(), this.outbreakType);
			else if(keyMap.getKey().equals("outbreak_code"))
				dataRecord.put(keyMap.getId(), this.outbreakCode);
			else if(keyMap.getKey().equals("search_type"))
				dataRecord.put(keyMap.getId(), this.searchType);
			else if(keyMap.getKey().equals("threshold_value"))
				dataRecord.put(keyMap.getId(), this.thresholdValue);
		}
		return dataRecord;
	}
	
	public void startProcess() throws Exception{
		if(this.process<0 || this.process>System.MAX_PROCESS || this.eventType!=EVENT_TYPE_OCCURRENCE) return;
		
		ProcessWork processWork = (ProcessWork)SwServiceFactory.getInstance().getWorkService().getWorkById(System.getProcessId(this.process));
		if(processWork==null) return;
		
		UcityUtil.startUServiceProcess(System.getProcessId(this.process), this.eventId, this.occuredDate, this.getDataRecord());
	}
	
	public void endProcess() throws Exception{
		if(this.process<0 || this.process>System.MAX_PROCESS || this.eventType!=EVENT_TYPE_RELEASE) return;
		
		ProcessWork processWork = (ProcessWork)SwServiceFactory.getInstance().getWorkService().getWorkById(System.getProcessId(this.process));
		if(processWork==null) return;
		
		UcityUtil.endUServiceProcess(System.getProcessId(this.process), this.eventId, this.getDataRecord());
	}
	
	public void performTask(String taskInstId) throws Exception{
		TaskInstance taskInstance = null;
		if(SmartUtil.isBlankObject(taskInstId)) return;
		
		taskInstance = SwServiceFactory.getInstance().getInstanceService().getTaskInstanceById(System.getProcessId(this.process), taskInstId);
		
		if(SmartUtil.isBlankObject(taskInstance)) return;
		
		UcityUtil.performUServiceTask(taskInstance, this.getDataRecord());		
	}
	
	public void setResult(ResultSet result){
		try{
			if(result.getRow()>0){ 
				this.communicationId = result.getString(FIELD_NAME_COMM_TG_ID);
				String commContent = result.getString(FIELD_NAME_COMM_CONTENT);
				if(SmartUtil.isBlankObject(commContent) || commContent.length()<Adapter.LENGTH_COMM_HEADER) return;
				this.commHeader = commContent.substring(0, Adapter.LENGTH_COMM_HEADER);
				this.commBody = commContent.substring(Adapter.LENGTH_COMM_HEADER);
				this.parseCommHeader(this.commHeader);
				this.parseCommBody(this.commBody);				
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public boolean isValid(){
		if(	this.process != -1 && !SmartUtil.isBlankObject(this.commHeader) 
				&& !SmartUtil.isBlankObject(this.commBody) && this.eventType !=0 && !SmartUtil.isBlankObject(this.eventId))
			return true;
		return false;
	}
	
	synchronized public static void readHistoryTableToStart(){
		java.lang.System.out.println("############ START checking ADAPTER History To Start  ################");
		try {
			Class.forName(System.DATABASE_JDBC_DRIVE);
		} catch (ClassNotFoundException e) {
			java.lang.System.out.println("[ERROR] ADAPTER 이벤트 데이터베이스 오류 종료");
			e.printStackTrace();
			return;
		}

		Connection con = null;
		PreparedStatement selectPstmt = null;
		PreparedStatement updatePstmt = null;
				
		String adapterSelectSql = Adapter.QUERY_SELECT_FOR_START;
		String adapterUpdateSql = Adapter.QUERY_UPDATE_FOR_READ_CONFIRM;
		try {
			try{
				con = DriverManager.getConnection(System.DATABASE_CONNECTION, System.DATABASE_USERNAME, System.DATABASE_PASSWORD);
			}catch (TbSQLException te){
				java.lang.System.out.println("[ERROR] ADAPTER 이벤트 데이터베이스 오류 종료");
				te.printStackTrace();
				java.lang.System.out.println("############ END checking ADAPTER History To Start  ################");
				return;
			}
//			con.setAutoCommit(false);
			
			try{
				selectPstmt = con.prepareStatement(adapterSelectSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = selectPstmt.executeQuery();				
				rs.last();
				int count = rs.getRow(); 
				rs.beforeFirst();
				if (count != 0) {
					java.lang.System.out.println("============== ADAPTER 이벤트 발생 ===============");
					java.lang.System.out.println("이벤트 발생 시간 : " + new Date());
					java.lang.System.out.println("이벤트 발생 갯수 : " + count);
					while(rs.next()) {
						try{
							String communicationId = rs.getString(Adapter.FIELD_NAME_COMM_TG_ID);
							updatePstmt = con.prepareStatement(adapterUpdateSql);
							updatePstmt.setString(1, communicationId);
							boolean result = updatePstmt.execute();
							Adapter adapter = new Adapter(rs);
							if(adapter.isValid() && adapter.getEventType() == Adapter.EVENT_TYPE_OCCURRENCE){
								try{
									adapter.startProcess();	
//									con.commit();
									java.lang.System.out.println("[SUCCESS] 새로운 ADAPTER 발생 이벤트(아이디 : '" + communicationId + ")가 정상적으로 시작되었습니다!");
								}catch (Exception se){
									java.lang.System.out.println("[ERROR] 새로운 ADAPTER 발생 이벤트를 시작하는데 오류가 발생하였습니다!");
									se.printStackTrace();
//									if(con != null)
//										con.rollback();
								}
							}else if(adapter.isValid() && adapter.getEventType() == Adapter.EVENT_TYPE_RELEASE){
								try{
									adapter.endProcess();
//									con.commit();
									java.lang.System.out.println("[SUCCESS] 새로운 ADAPTER 종료 이벤트(아이디 : '" + communicationId + ")가 정상적으로 처리되었습니다!");
								}catch (Exception se){
									java.lang.System.out.println("[ERROR] 새로운 ADAPTER 종료 이벤트를 처리하는데 오류가 발생하였습니다!");
									se.printStackTrace();
//									if(con != null)
//										con.rollback();
								}
							}else{
//								con.rollback();
								java.lang.System.out.println("[ERROR] 새로운 ADAPTER 이벤트를 시작하는데 오류가 발생하였습니다!");
							}
						}catch (Exception we){
							java.lang.System.out.println("[ERROR] ADAPTER 이벤트 데이터베이스 오류 종료");
							we.printStackTrace();
							java.lang.System.out.println("############ END checking ADAPTER History To Start  ################");
							return;
						}
					}
				}
			}catch (Exception e1){
				java.lang.System.out.println("[ERROR] ADAPTER 이벤트 데이터베이스 오류 종료");
				e1.printStackTrace();
			}
		} catch (Exception e) {
			java.lang.System.out.println("[ERROR] ADAPTER 이벤트 데이터베이스 오류 종료");
			e.printStackTrace();
		} finally {
			try {
				if (selectPstmt != null)
					selectPstmt.close();
				if (updatePstmt != null)
					updatePstmt.close();
				if(con != null)
					con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			java.lang.System.out.println("############ END checking ADAPTER History To Start  ################");
		}
	}
	
//	public static Map<String,Object> readHistoryTable(String eventId){
//		java.lang.System.out.println("############ START checking ADAPTER History To Process(Event Id:" + eventId +  ")  ################");
//		try {
//			Class.forName(System.DATABASE_JDBC_DRIVE);
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//			java.lang.System.out.println("[ERROR ] ADAPTER 처리 이벤트 데이터베이스 오류 종료!");
//			java.lang.System.out.println("############ END checking ADAPTER History To Process(Event Id:" + eventId +  ")  ################");
//		}
//
//		Connection con = null;
//		PreparedStatement selectPstmt = null;
//		PreparedStatement updatePstmt = null;
//				
//		String adapterSelectSql = Adapter.QUERY_SELECT_FOR_PERFORM;
//		String adapterUpdateSql = Adapter.QUERY_UPDATE_FOR_READ_CONFIRM;
//		try {
//			
//			con = DriverManager.getConnection(System.DATABASE_CONNECTION, System.DATABASE_USERNAME, System.DATABASE_PASSWORD);
//			con.setAutoCommit(false);
//			
//			try{
//				selectPstmt = con.prepareStatement(adapterSelectSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//				ResultSet rs = selectPstmt.executeQuery();				
//				rs.last(); 
//				int count = rs.getRow(); 
//				rs.first();
//				if (count == 1) {
//					try{
//						java.lang.System.out.println("============== ADAPTER 처리 이벤트 발견 ===============");
//						String communicationId = rs.getString(Adapter.FIELD_NAME_COMM_TG_ID);
//						updatePstmt = con.prepareStatement(adapterUpdateSql);
//						updatePstmt.setString(1, communicationId);
//						boolean result = updatePstmt.execute();
//						Adapter adapter = new Adapter(rs);
//						if(adapter.isValid() && adapter.getEventType() == Adapter.EVENT_TYPE_RELEASE && adapter.getEventId().equals(eventId)){
//							con.commit();
//							try {
//								if (selectPstmt != null)
//									selectPstmt.close();
//								if (updatePstmt != null)
//									updatePstmt.close();
//								con.close();
//							} catch (SQLException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//							return adapter.getDataRecord();
//						}else{
//							con.rollback();
//						}
//					}catch (Exception we){
//						we.printStackTrace();
//						java.lang.System.out.println("[ERROR ] ADAPTER 처리 이벤트 데이터베이스 오류 종료!");
//					}
//				}
//			}catch (Exception e1){
//				e1.printStackTrace();
//				java.lang.System.out.println("[ERROR ] ADAPTER 처리 이벤트 데이터베이스 오류 종료!");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			java.lang.System.out.println("[ERROR ] ADAPTER 처리 이벤트 데이터베이스 오류 종료!");
//		} finally {
//			try {
//				if (selectPstmt != null)
//					selectPstmt.close();
//				if (updatePstmt != null)
//					updatePstmt.close();
//				con.close();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		java.lang.System.out.println("############ END checking ADAPTER History To Process(Event Id:" + eventId +  ")  ################");
//		return null;
//	}
}
