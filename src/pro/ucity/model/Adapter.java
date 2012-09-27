package pro.ucity.model;

import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public static final int LENGTH_EVENT_CODE = 4;
	public static final int LENGTH_EVENT_TYPE = 2;
	
	public static final int POS_EVENT_CODE = 37;
	public static final int POS_EVENT_TYPE = 39;
	
	public static final int EVENT_TYPE_OCCURRENCE = 1;
	public static final int EVENT_TYPE_RELEASE = 2;
	
	public static final KeyMap[][] ADAPTER_HISTORY_FIELDS = {
		{new KeyMap("이벤트 ID", "event_id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name")},
		{new KeyMap("이벤트 ID", "event_id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name")},
		{new KeyMap("이벤트 ID", "event_id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name")},
		{new KeyMap("이벤트 ID", "event_id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name")},
		{new KeyMap("이벤트 ID", "event_id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name")},
		{new KeyMap("이벤트 ID", "event_id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name")},
		{new KeyMap("이벤트 ID", "event_id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name")},
		{new KeyMap("이벤트 ID", "event_id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name")},
		{new KeyMap("이벤트 ID", "event_id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name")},
		{new KeyMap("이벤트 ID", "event_id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name")}		
	};
	
	private String commHeader;
	private String commBody;
	
	private int process=-1;
	private int eventType;
	
	private String eventId;
	private String occuredDate;
	private String facilityId;
	private String locationName;
	
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
		this.occuredDate = occuredDate;
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
		
		String eventCode = commHeader.substring(Adapter.POS_EVENT_CODE, Adapter.POS_EVENT_CODE+Adapter.LENGTH_EVENT_CODE);
		String eventType = commHeader.substring(Adapter.POS_EVENT_TYPE, Adapter.POS_EVENT_TYPE+Adapter.LENGTH_EVENT_TYPE);
		
		if(	eventCode.equals(Event.ID_ENV_GALE) ||
			eventCode.equals(Event.ID_ENV_AIRFLOW) ||
			eventCode.equals(Event.ID_ENV_STORM) ||
			eventCode.equals(Event.ID_ENV_HEAVY_SNOWFALL) ||
			eventCode.equals(Event.ID_ENV_DRYING) ||
			eventCode.equals(Event.ID_ENV_STORM_SURGES) ||
			eventCode.equals(Event.ID_ENV_TSUNAMI) ||
			eventCode.equals(Event.ID_ENV_COLD_WAVE) ||
			eventCode.equals(Event.ID_ENV_TYPHOON) ||
			eventCode.equals(Event.ID_ENV_ASIAN_DUST) ||
			eventCode.equals(Event.ID_ENV_HEATWAVE) ||
			eventCode.equals(Event.ID_ENV_FINE_DUST)){
			this.process = System.PROCESS_ENV_WEAHTER;
		}else if(eventCode.equals(Event.ID_ENV_OZONE)){
			this.process = System.PROCESS_ENV_ATMOSPHERE;
		}else if(eventCode.equals(Event.ID_ENV_CANAL_WAY) || commHeader.equals(Event.ID_ENV_WATER)){
			this.process = System.PROCESS_ENV_WATER;
		}else if(eventCode.equals(Event.ID_TRAFFIC_ILLEGAL_PARKING)){
			this.process = System.PROCESS_TRAFFIC_ILLEGAL_PARKING;
		}else if(eventCode.equals(Event.ID_TRAFFIC_INCIDENT) || 
				commHeader.equals(Event.ID_TRAFFIC_ACCIDENTS) ||
				commHeader.equals(Event.ID_TRAFFIC_HIT_AND_RUN) ||
				commHeader.equals(Event.ID_TRAFFIC_VEHICLE_BREAKDOWN)){
			this.process = System.PROCESS_TRAFFIC_INCIDENT;
		}else if(eventCode.equals(Event.ID_DISASTER_FIRE)){
			this.process = System.PROCESS_DISASTER_FIRE;
		}else if(eventCode.equals(Event.ID_CRIME_EMERGENCY)){
			this.process = System.PROCESS_CRIME_CCTV;
		}else if(eventCode.equals(Event.ID_CRIME_VEHICLE)){
			this.process = System.PROCESS_CRIME_VEHICLES;
		}else if(eventCode.equals(Event.ID_WATERWORKS_LEAKS)){
			this.process = System.PROCESS_WATERWORKS_LEAKS;
		}else if(eventCode.equals(Event.ID_FACILITY_TROUBLE) || commHeader.equals(Event.ID_FACILITY_EMERGENCY)){
			this.process = System.PROCESS_FACILITY_MANAGEMENT;
		}

		if(eventType.equals(Event.TYPE_OCCURRENCE))
			this.eventType = EVENT_TYPE_OCCURRENCE;
		if(eventType.equals(Event.TYPE_RELEASE))
			this.eventType = EVENT_TYPE_RELEASE;
	
	}
	private void parseCommBody(String commBody){
		if(SmartUtil.isBlankObject(commBody) || this.process<0 || this.process>System.MAX_PROCESS) return;
		
		String[] tokens = commBody.split(Adapter.FIELD_SEPERATOR);
		if(tokens.length != ADAPTER_HISTORY_FIELDS[System.PROCESS_ENV_WEAHTER].length) return;
		
		switch(process){
		case System.PROCESS_ENV_WEAHTER:
			this.eventId = tokens[0];
			this.occuredDate = tokens[1];
			this.facilityId = tokens[2];
			this.locationName = tokens[3];
			break;
		case System.PROCESS_ENV_ATMOSPHERE:
			this.eventId = tokens[0];
			this.occuredDate = tokens[1];
			this.facilityId = tokens[2];
			this.locationName = tokens[3];
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
			break;
		case System.PROCESS_TRAFFIC_INCIDENT:
			this.eventId = tokens[0];
			this.occuredDate = tokens[1];
			this.facilityId = tokens[2];
			this.locationName = tokens[3];
			break;
		case System.PROCESS_DISASTER_FIRE:
			this.eventId = tokens[0];
			this.occuredDate = tokens[1];
			this.facilityId = tokens[2];
			this.locationName = tokens[3];
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
			break;
		case System.PROCESS_WATERWORKS_LEAKS:
			this.eventId = tokens[0];
			this.occuredDate = tokens[1];
			this.facilityId = tokens[2];
			this.locationName = tokens[3];
			break;
		case System.PROCESS_FACILITY_MANAGEMENT:
			this.eventId = tokens[0];
			this.occuredDate = tokens[1];
			this.facilityId = tokens[2];
			this.locationName = tokens[3];
			break;
		}
	}
	
	public Map<String, Object> getDataRecord(){
		if(this.process<0 || this.process>System.MAX_PROCESS) return null;
		
		Map<String, Object> dataRecord = new HashMap<String, Object>();
		KeyMap[] keyMaps = Adapter.ADAPTER_HISTORY_FIELDS[this.process];
		
		for(int i=0; i<keyMaps.length; i++){
			KeyMap keyMap = keyMaps[i];
			if(keyMap.getKey().equals("event_id"))
				dataRecord.put(keyMap.getId(), this.eventId);
			else if(keyMap.getKey().equals("occured_date"))
				dataRecord.put(keyMap.getId(), this.occuredDate);
			else if(keyMap.getKey().equals("facility_id"))
				dataRecord.put(keyMap.getId(), this.facilityId);
			else if(keyMap.getKey().equals("location_name"))
				dataRecord.put(keyMap.getId(), this.locationName);
		}
		return dataRecord;
	}
	
	public void startProcess() throws Exception{
		if(this.process<0 || this.process>System.MAX_PROCESS || this.eventType!=EVENT_TYPE_OCCURRENCE) return;
		
		ProcessWork processWork = (ProcessWork)SwServiceFactory.getInstance().getWorkService().getWorkById(System.getProcessId(this.process));
		if(processWork==null) return;
		
		UcityUtil.startUServiceProcess(System.getProcessId(this.process), this.getDataRecord());
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
				String commContent = result.getString("CMNC_TG_CONT");
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
}
