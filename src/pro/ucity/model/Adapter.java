package pro.ucity.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import net.smartworks.model.KeyMap;
import net.smartworks.model.instance.TaskInstance;
import net.smartworks.model.work.ProcessWork;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.service.factory.SwServiceFactory;
import net.smartworks.util.SmartUtil;

import org.apache.log4j.Logger;

import pro.ucity.util.UcityUtil;

import com.tmax.tibero.jdbc.TbSQLException;

public class Adapter {

	private static final Logger logger = Logger.getLogger(Adapter.class);
	
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
	
	public static final String STATUS_STOP_DISPLAY = "Y";
	
	public static final KeyMap[][] ADAPTER_HISTORY_FIELDS = {		
		{new KeyMap("이벤트 ID", "event_id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("특보분류", "env_event_type"), new KeyMap("발생내용", "event_content")},
		{new KeyMap("이벤트 ID", "event_id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name"), new KeyMap("오염물질수", "pollution_number"), new KeyMap("오염물질구분", "pollution_type"), new KeyMap("오염물질측정치", "pollution_value"), new KeyMap("오염등급", "pollution_level"), new KeyMap("오염물질 예/경보구분", "pollution_example")},
		{new KeyMap("이벤트 ID", "event_id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("발생장소명", "location_name"), new KeyMap("오염물질등급", "pollution_level"), new KeyMap("오염물질수치", "pollution_figure")},
		{new KeyMap("이벤트 ID", "event_id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name"), new KeyMap("차량번호", "car_number"), new KeyMap("차량차종", "car_type"), new KeyMap("범죄유형", "crime_code"), new KeyMap("이미지경로", "file_path")},
		{new KeyMap("이벤트 ID", "event_id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name"), new KeyMap("시설물위치", "link_id"), new KeyMap("돌발상황 유형", "outbreak_type"), new KeyMap("돌발상황 코드", "outbreak_code")},
		{new KeyMap("이벤트 ID", "event_id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("탐지분류", "search_type")},
		{new KeyMap("이벤트 ID", "event_id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name")},
		{new KeyMap("이벤트 ID", "event_id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name"), new KeyMap("차량번호", "car_number"), new KeyMap("차량차종", "car_type"), new KeyMap("범죄유형", "crime_code"), new KeyMap("이미지경로", "file_path")},
		{new KeyMap("이벤트 ID", "event_id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name"), new KeyMap("임계치 값", "threshold_value")},
		{new KeyMap("이벤트 ID", "event_id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name"), new KeyMap("시설물이상구분", "facility_type")},		
		{new KeyMap("장비 ID", "device_id"), new KeyMap("배경색상", "background_color"), new KeyMap("색상", "font_color"), new KeyMap("폰트크기", " font_size"), new KeyMap("폰트타입", "font_type"), new KeyMap("폰트굵기", "font_thickness"), new KeyMap("표출유형", "display_type"), new KeyMap("데이터형식", "data_type"), new KeyMap("표출내용", "message")},		
		{new KeyMap("장비 ID", "device_id"), new KeyMap("배경색상", "background_color"), new KeyMap("색상", "font_color"), new KeyMap("폰트크기", " font_size"), new KeyMap("폰트타입", "font_type"), new KeyMap("폰트굵기", "font_thickness"), new KeyMap("표출유형", "display_type"), new KeyMap("데이터형식", "data_type"), new KeyMap("표출내용", "message")},		
		{new KeyMap("장비 ID", "device_id"), new KeyMap("배경색상", "background_color"), new KeyMap("색상", "font_color"), new KeyMap("폰트크기", " font_size"), new KeyMap("폰트타입", "font_type"), new KeyMap("폰트굵기", "font_thickness"), new KeyMap("표출유형", "display_type"), new KeyMap("데이터형식", "data_type"), new KeyMap("표출내용", "message")},		
		{new KeyMap("장비 ID", "device_id"), new KeyMap("배경색상", "background_color"), new KeyMap("색상", "font_color"), new KeyMap("폰트크기", " font_size"), new KeyMap("폰트타입", "font_type"), new KeyMap("폰트굵기", "font_thickness"), new KeyMap("표출유형", "display_type"), new KeyMap("데이터형식", "data_type"), new KeyMap("표출내용", "message")},		
		{new KeyMap("장비 ID", "device_id"), new KeyMap("배경색상", "background_color"), new KeyMap("색상", "font_color"), new KeyMap("폰트크기", " font_size"), new KeyMap("폰트타입", "font_type"), new KeyMap("폰트굵기", "font_thickness"), new KeyMap("표출유형", "display_type"), new KeyMap("데이터형식", "data_type"), new KeyMap("표출내용", "message")}		
	};
	
	private String commHeader;
	private String commBody;
	
	private static int process=-1;
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
	private String displayContent;
	private boolean stopDisplay;
	private String linkId;
	
	private String pollutionFigure;
	private String filePath;
	private String fileName;
	
	private String nextOccuredDate;
	
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
	public String getDisplayContent() {
		return displayContent;
	}
	public void setDisplayContent(String displayContent) {
		this.displayContent = displayContent;
	}
	public boolean isStopDisplay() {
		return stopDisplay;
	}
	public void setStopDisplay(boolean stopDisplay) {
		this.stopDisplay = stopDisplay;
	}
	public String getLinkId() {
		return linkId;
	}
	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}
	public String getPollutionFigure() {
		return pollutionFigure;
	}
	public void setPollutionFigure(String pollutionFigure) {
		this.pollutionFigure = pollutionFigure;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getNextOccuredDate() {
		return nextOccuredDate;
	}
	public void setNextOccuredDate(String nextOccuredDate) {
		this.nextOccuredDate = nextOccuredDate;
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
	
	// TB_UAST_CMNC_HS 테이블의 전문 중 Header를 쪼개어 각 각 해당 변수에 SET
	private void parseCommHeader(String commHeader){
		int sizeHeader = commHeader.length();
		if(SmartUtil.isBlankObject(commHeader) || commHeader.length() != LENGTH_COMM_HEADER) return;
		
		this.serviceCode = commHeader.substring(Adapter.POS_SERVICE_CODE, Adapter.POS_SERVICE_CODE+Adapter.LENGTH_SERVICE_CODE);
		this.eventCode = commHeader.substring(Adapter.POS_EVENT_CODE, Adapter.POS_EVENT_CODE+Adapter.LENGTH_EVENT_CODE);
		String eventType = commHeader.substring(Adapter.POS_EVENT_TYPE, Adapter.POS_EVENT_TYPE+Adapter.LENGTH_EVENT_TYPE);
		
		this.process = Event.getProcessByServiceId(this.serviceCode);
		if(this.process == -1)
			this.process = Event.getProcessByEventId(this.eventCode);
		
		if(eventType.equals(Event.TYPE_OCCURRENCE))
			this.eventType = EVENT_TYPE_OCCURRENCE;
		if(eventType.equals(Event.TYPE_RELEASE))
			this.eventType = EVENT_TYPE_RELEASE;
		
	}
	// TB_UAST_CMNC_HS 테이블의 전문 중 Body를 쪼개어 각 각 해당 변수에 SET
	private void parseCommBody(String commBody){
		if(SmartUtil.isBlankObject(commBody) || this.process<0 || this.process>System.MAX_PROCESS) return;
		
		int processLen = ADAPTER_HISTORY_FIELDS[process].length;
		String[] tokens = commBody.split(Adapter.FIELD_SEPERATOR,processLen);
		if(tokens != null){
			if(tokens != null && tokens.length < 3 && (this.process == System.PROCESS_ENV_VMS || this.process == System.PROCESS_MEDIABORAD || this.process == System.PROCESS_TRAFFIC_VMS || this.process == System.PROCESS_TRAFFIC_BIT || this.process == System.PROCESS_KIOSK)){
				this.stopDisplay = true;
			}else if((this.process == System.PROCESS_ENV_VMS || this.process == System.PROCESS_MEDIABORAD || this.process == System.PROCESS_TRAFFIC_VMS || this.process == System.PROCESS_TRAFFIC_BIT || this.process == System.PROCESS_KIOSK)){
				this.stopDisplay = false;
			}else if(tokens.length != processLen)
				return;
		}
		
		switch(process){
		case System.PROCESS_ENV_WEAHTER:
			this.eventId = tokens[0];
			this.occuredDate = tokens[1];
			this.envEventType = tokens[2];
			this.eventContent = tokens[3];
			if( occuredDate.length() > 14)
				occuredDate = occuredDate.substring(0, 14);
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
			if( occuredDate.length() > 14)
				occuredDate = occuredDate.substring(0, 14);
			break;
		case System.PROCESS_ENV_WATER:
			this.eventId = tokens[0];
			this.occuredDate = tokens[1];
			this.locationName = tokens[2];
			this.pollutionLevel = tokens[3];
			this.pollutionFigure = tokens[4];
			if( occuredDate.length() > 14)
				occuredDate = occuredDate.substring(0, 14);
			break;
		case System.PROCESS_TRAFFIC_ILLEGAL_PARKING:
			this.eventId = tokens[0];
			this.occuredDate = tokens[1];
			this.facilityId = tokens[2];
			this.locationName = tokens[3];
			this.carNumber = tokens[4];
			this.carType = tokens[5];
			this.crimeCode = tokens[6];
			this.filePath = tokens[7];			
			if( occuredDate.length() > 14)
				occuredDate = occuredDate.substring(0, 14);
			break;
		case System.PROCESS_TRAFFIC_INCIDENT:
			this.eventId = tokens[0];
			this.occuredDate = tokens[1];
			this.facilityId = tokens[2];
			this.locationName = tokens[3];
			this.linkId = tokens[4];
			this.outbreakType = tokens[5];
			this.outbreakCode = tokens[6];
			if( occuredDate.length() > 14)
				occuredDate = occuredDate.substring(0, 14);
			break;
		case System.PROCESS_DISASTER_FIRE:
			this.eventId = tokens[0];
			this.occuredDate = tokens[1];
			this.facilityId = tokens[2];
			this.searchType = tokens[3];
			if( occuredDate.length() > 14)
				occuredDate = occuredDate.substring(0, 14);
			break;
		case System.PROCESS_CRIME_CCTV:
			this.eventId = tokens[0];
			this.occuredDate = tokens[1];
			this.facilityId = tokens[2];
			this.locationName = tokens[3];
			if( occuredDate.length() > 14)
				occuredDate = occuredDate.substring(0, 14);
			break;
		case System.PROCESS_CRIME_VEHICLES:
			this.eventId = tokens[0];
			this.occuredDate = tokens[1];
			this.facilityId = tokens[2];
			this.locationName = tokens[3];
			this.carNumber = tokens[4];
			this.carType = tokens[5];
			this.crimeCode = tokens[6];
			this.filePath = tokens[7];
			if( occuredDate.length() > 14)
				occuredDate = occuredDate.substring(0, 14);
			break;
		case System.PROCESS_WATERWORKS_LEAKS:
			this.eventId = tokens[0];
			this.occuredDate = tokens[1];
			this.facilityId = tokens[2];
			this.locationName = tokens[3];
			this.thresholdValue = tokens[4];
			if( occuredDate.length() > 14)
				occuredDate = occuredDate.substring(0, 14);
			break;
		case System.PROCESS_FACILITY_MANAGEMENT:
			this.eventId = tokens[0];
			this.occuredDate = tokens[1];
			this.facilityId = tokens[2];
			this.locationName = tokens[3];
			this.facilityType = tokens[4];
			if( occuredDate.length() > 14)
				occuredDate = occuredDate.substring(0, 14);
			break;
		case System.PROCESS_ENV_VMS:
		case System.PROCESS_TRAFFIC_BIT:
		case System.PROCESS_TRAFFIC_VMS:
		case System.PROCESS_MEDIABORAD:
		case System.PROCESS_KIOSK:
			this.eventId = tokens[0];
			int tokenLen = 0;
			if(!this.stopDisplay){
				tokenLen = tokens.length-1;
				this.displayContent = tokens[tokenLen];
			}else{
				this.displayContent = "표출중지요청";
			}		
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

//	    eventId의 길이를 20크기로 맞쳐주는데 수정하여, like 검색으로 수정( 추 후를 위해 남겨둠 )
//		int len = 20; 
//		if(len != eventId.length()){
//			if(!this.isValid()) return null;
//		}
		dataRecord.put("serviceName", Service.getServiceNameByCode(this.getServiceCode()));
		if(this.process == System.PROCESS_ENV_WEAHTER)
			dataRecord.put("eventName", "환경경보");
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
			else if(keyMap.getKey().equals("message"))
				dataRecord.put(keyMap.getId(), this.displayContent);
			else if(keyMap.getKey().equals("link_id"))
				dataRecord.put(keyMap.getId(), this.linkId);
			else if(keyMap.getKey().equals("pollution_figure"))
				dataRecord.put(keyMap.getId(), this.pollutionFigure);
			else if(keyMap.getKey().equals("file_path"))
				dataRecord.put(keyMap.getId(), this.filePath);
			else if(keyMap.getKey().equals("file_name"))
				dataRecord.put(keyMap.getId(), this.fileName);
		}
		return dataRecord;
	}
	
	//U-Service 상황 발생 후, BPM 프로세스 시작.
	public void startProcess() throws Exception{
		if(this.process<0 || this.process>System.MAX_PROCESS || this.eventType!=EVENT_TYPE_OCCURRENCE) return;
		
		ProcessWork processWork = (ProcessWork)SwServiceFactory.getInstance().getWorkService().getWorkById(System.getProcessId(this.process));
		if(processWork==null) return;
//		리스트에 해당 이벤트아이디가 있는지 확인 메소드지만, 사용안해도 무방함( 사용 하면 성능이 나빠짐 )
//		if(UcityUtil.ucityWorklistSearch(System.getProcessId(this.process),this.eventId) == true ){
			if(this.process == System.PROCESS_FACILITY_MANAGEMENT){
				UcityUtil.startUServiceProcess(System.getProcessId(this.process), this.eventId, this.occuredDate, this.getDataRecord(), this.facilityId);
			}else{
				UcityUtil.startUServiceProcess(System.getProcessId(this.process), this.eventId, this.occuredDate, this.getDataRecord());			
			}		
//		}else{
//			logger.info("startProcess 에서 null return");
//			return;
//		}
	}
	//U-Service 상황 종료 후, BPM 프로세스 시작.
	public void endProcess() throws Exception{
		if(this.process<0 || this.process>System.MAX_PROCESS || this.eventType!=EVENT_TYPE_RELEASE) return;
		
		ProcessWork processWork = (ProcessWork)SwServiceFactory.getInstance().getWorkService().getWorkById(System.getProcessId(this.process));
		if(processWork==null) return;
		if(this.process == System.PROCESS_FACILITY_MANAGEMENT){
			UcityUtil.endUServiceProcessFacility(System.getProcessId(this.process), this.facilityId, this.getDataRecord());
		}else{
			UcityUtil.endUServiceProcess(System.getProcessId(this.process), this.eventId, this.getDataRecord());
		}
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
				this.communicationId = result.getString(UcityConstant.getQueryByKey("Adapter.FIELD_NAME_COMM_TG_ID"));
				String commContent = result.getString(UcityConstant.getQueryByKey("Adapter.FIELD_NAME_COMM_CONTENT"));
				if(SmartUtil.isBlankObject(commContent) || commContent.length()<Adapter.LENGTH_COMM_HEADER) return;
				this.commHeader = commContent.substring(0, Adapter.LENGTH_COMM_HEADER);
				this.commBody = commContent.substring(Adapter.LENGTH_COMM_HEADER);
				this.parseCommHeader(this.commHeader);
				this.parseCommBody(this.commBody);				
		}catch (Exception e){
			logger.error("전문 분석 오류 : Adapter.setResult");
		}
	}
	
	public boolean isValid(){
		if( this.process == System.PROCESS_FACILITY_MANAGEMENT)
			return true;
		if(	this.process != -1 && !SmartUtil.isBlankObject(this.commHeader) 
				&& !SmartUtil.isBlankObject(this.commBody) && this.eventType !=0 && !SmartUtil.isBlankObject(this.eventId))
			return true;
		return false;
	}
	
	public boolean isValid(String eventId, String status){
		if(	(this.process == System.PROCESS_ENV_VMS || this.process == System.PROCESS_KIOSK || this.process == System.PROCESS_TRAFFIC_VMS || this.process == System.PROCESS_TRAFFIC_BIT || this.process == System.PROCESS_MEDIABORAD) && !SmartUtil.isBlankObject(this.eventId) && this.eventId.equals(eventId)) {
			if((!SmartUtil.isBlankObject(status) && this.stopDisplay && status.equals(Adapter.STATUS_STOP_DISPLAY)) || (!this.stopDisplay))
			return true;			
		}
		return false;
	}
	
	public static void scheduler(){
		readHistoryTableToStart();
	}
	
	// 20초 마다 TB_UAST_CMNC_HS 테이블을 읽어, BPM프로세스 진행시킴.
	synchronized public static void readHistoryTableToStart(){

		Connection connection = null;
		PreparedStatement selectPstmt = null;
		PreparedStatement selectPstmt2 = null;
		PreparedStatement selectPstmt3 = null;
		PreparedStatement updatePstmt = null;
		int number = 1;
				
		String adapterSelectStartSql = UcityConstant.getQueryByKey("Adapter.QUERY_SELECT_FOR_START");
		String adapterSelectEndSql = UcityConstant.getQueryByKey("Adapter.QUERY_SELECT_FOR_END");
		String adapterUpdateSql = UcityConstant.getQueryByKey("Adapter.QUERY_UPDATE_FOR_READ_CONFIRM");
		String adapterRollbackSql = UcityConstant.getQueryByKey("Adapter.QUERY_UPDATE_FOR_READ_ROLLBACK");
		String adapterJoinTrafficSql = UcityConstant.getQueryByKey("Adapter.QUERY_SELECT_FOR_TRAFFIC");
		
		String opSituationJoinFacilitySql = UcityConstant.getQueryByKey("OPSituation.QUERY_SELECT_FOR_FACILITY");
		

		try {
			try{
			    Context init = new InitialContext();
			    Context envinit = (Context)init.lookup("java:comp/env");
			    DataSource ds = (DataSource) envinit.lookup("bpm/tibero");
			    connection = ds.getConnection();
			}catch (TbSQLException te){
				logger.error("DB Connection 오류 : Adapter.readHistoryTableToStart");
				return;
			}
//			connection.setAutoCommit(false);
			try{
				selectPstmt = connection.prepareStatement(adapterSelectStartSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = selectPstmt.executeQuery();		
				rs.setFetchSize(10);
				rs.last();
				int count = rs.getRow(); 
				rs.beforeFirst();
				if (count != 0) {
					int processedCount = 0;
					logger.info("============== ADAPTER 시작이벤트 발생 ===============");
					
					while(rs.next() && number == 1 && !Thread.currentThread().isInterrupted()) {
						try{
							String communicationId = rs.getString(UcityConstant.getQueryByKey("Adapter.FIELD_NAME_COMM_TG_ID"));
							
							updatePstmt = connection.prepareStatement(adapterUpdateSql);
							updatePstmt.setString(1, communicationId);
							boolean result = updatePstmt.execute();
							Adapter adapter = new Adapter(rs);
							if(adapter.isValid() && adapter.getEventType() == Adapter.EVENT_TYPE_OCCURRENCE){
								try{
									selectPstmt2 = connection.prepareStatement(opSituationJoinFacilitySql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
									selectPstmt2.setString(1, adapter.getEventId());
									ResultSet joinFacilityRs = selectPstmt2.executeQuery();	
									joinFacilityRs.last();
									int facilitycount = joinFacilityRs.getRow(); 
									joinFacilityRs.beforeFirst();
									if(joinFacilityRs.next() && facilitycount != 0){
										String location = joinFacilityRs.getString("LC_NM");
										adapter.setLocationName(location);
									}
									try{
										/*교통사고 상황일 경우, 발생장소에 linkId가 들어감.
										해당 상황 일 시, 해당 발생장소 맵핑테이블을 찾아, 장소를 바꿔줌. */
										if(process == System.PROCESS_TRAFFIC_INCIDENT){
											selectPstmt3 = connection.prepareStatement(adapterJoinTrafficSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
											selectPstmt3.setString(1, adapter.getLocationName());
											ResultSet joinTrafficRs = selectPstmt3.executeQuery();	
											joinTrafficRs.last();
											int trafficCount = joinTrafficRs.getRow(); 
											joinTrafficRs.beforeFirst();
											if(joinTrafficRs.next() && trafficCount != 0){
												String location = joinTrafficRs.getString("ROAD_NM");
												adapter.setLocationName(location);											
											}
										}
									}catch(Exception e){
										logger.error("========================================");
										logger.error("링크아이디로 장소를 찾아오는데 실패하였습니다.");
										logger.error("========================================");
									}	
									adapter.startProcess();	
									logger.info("[SUCCESS] 새로운 ADAPTER 발생 이벤트가 정상적으로 시작되었습니다!");
									
									
								}catch (Exception se){
									logger.error("startProcese 오류 : adapter.startProcess");
								}
							}else{
								logger.error("[ERROR] 새로운 ADAPTER 이벤트를 시작하는데 오류가 발생하였습니다!");
							}
						}catch (Exception we){
							logger.error("while문 이후 error : adapter.readHistoryTableToStart");
							return;
						}
					}
				}else{
					//시작이벤트가 아니고, 종료 이벤트 상황일 시..
					try{
						selectPstmt = connection.prepareStatement(adapterSelectEndSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
						rs = selectPstmt.executeQuery();
						rs.setFetchSize(10);
						rs.last();
						count = rs.getRow(); 
						rs.beforeFirst();					
						if (count != 0) {
							int processedCount = 0;
							logger.info("============== ADAPTER 종료이벤트 발생 ===============");
							
							while(rs.next() && number == 1 && !Thread.currentThread().isInterrupted()) {
								try{
									String communicationId = rs.getString(UcityConstant.getQueryByKey("Adapter.FIELD_NAME_COMM_TG_ID"));
									updatePstmt = connection.prepareStatement(adapterUpdateSql);
									updatePstmt.setString(1, communicationId);
									boolean result = updatePstmt.execute();
									Adapter adapter = new Adapter(rs);
									if(adapter.isValid() && adapter.getEventType() == Adapter.EVENT_TYPE_RELEASE){
										try{
											adapter.endProcess();
											logger.info("[SUCCESS] 새로운 ADAPTER 종료 이벤트가 정상적으로 처리되었습니다!");
										}catch (Exception se){
											logger.error("endProcess error : adapter.endProcess.734",se);
										}
									}else{
										logger.error("[ERROR] 새로운 ADAPTER 이벤트를 시작하는데 오류가 발생하였습니다!");
									}
								}catch (Exception e){
									logger.error("종료 이벤트 발생 error : adapter.readHistoryTableToStart.763");
									return;
								}
							}
						}							
					}catch (Exception e){
						logger.error("종료 이벤트 발생 error : adapter.readHistoryTableToStart.772");
					}
				}
			}catch (Exception e){
				logger.error("이벤트 발생 error 777 : adapter.readHistoryTableToStart.778");
			}
		} catch (Exception e) {
			logger.error("이벤트 발생 error 777");
		} finally {
			try {
				if (selectPstmt != null)
					selectPstmt.close();
				if (updatePstmt != null)
					updatePstmt.close();
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error("finally close error : adapter.796");
			}
		}
	}
	// 상황 처리 상황에서, 외부표출 및 SMS를 보냇는지 확인.
	public static Map<String,Object> readHistoryTable(String eventId, String deviceId, String status){
		if(SmartUtil.isBlankObject(eventId) || SmartUtil.isBlankObject(Service.getDeviceCodeByDeviceId(deviceId))) return null;

		for(int i=eventId.length(); i < 20; i++){
			eventId = eventId + "0";
		}
		Connection connection = null;
		PreparedStatement selectPstmt = null;
		PreparedStatement updatePstmt = null;
				
		String adapterSelectSql = UcityConstant.getQueryByKey("Adapter.QUERY_SELECT_FOR_PERFORM");
		String adapterUpdateSql = UcityConstant.getQueryByKey("Adapter.QUERY_UPDATE_FOR_READ_CONFIRM");
		try {
			try{
			    Context init = new InitialContext();
			    Context envinit = (Context)init.lookup("java:comp/env");
			    DataSource ds = (DataSource) envinit.lookup("bpm/tibero");
			    connection = ds.getConnection();
			}catch (TbSQLException te){
				logger.error("DB Connection error : adapter.readHistoryTable.825");
				return null;
			}
			
			try{
				selectPstmt = connection.prepareStatement(adapterSelectSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				selectPstmt.setString(1, Service.getDeviceCodeByDeviceId(deviceId));
				selectPstmt.setString(2, eventId);
				ResultSet rs = selectPstmt.executeQuery();	
				rs.setFetchSize(10);
				rs.last(); 
				int count = rs.getRow();
				rs.beforeFirst();
				if(count>0){
					while(rs.next() && !Thread.currentThread().isInterrupted()) {
						try{
							Adapter adapter = new Adapter(rs);
							if(adapter.isValid(eventId, status)){
								try {
									String communicationId = rs.getString(UcityConstant.getQueryByKey("Adapter.FIELD_NAME_COMM_TG_ID"));
									updatePstmt = connection.prepareStatement(adapterUpdateSql);
									updatePstmt.setString(1, communicationId);
									boolean result = updatePstmt.execute();
									if (selectPstmt != null)
										selectPstmt.close();
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									logger.error("result set error : adapter.readHistoryTable.851");
								}
								return adapter.getDataRecord();
							}
						}catch (Exception e){
							logger.error("rs.next error : adapter.858");
						}
					}
				}
			}catch (Exception e){
				logger.error("select error : adapter.864");
			}
		} catch (Exception e) {
			logger.error("select error : adapter.868");
		} finally {
			try {
				if (selectPstmt != null)
					selectPstmt.close();
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				logger.error("finally close error : adapter.877");
			}
		}
		return null;
	}
}
