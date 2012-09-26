package pro.ucity.model;

import java.util.HashMap;
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

	public static final String FIELD_SEPERATOR = "||";
	public static final int LENGTH_COMM_HEADER = 4;
	public static final int LENGTH_COMM_TYPE = 2;
	
	public static final int COMM_TYPE_OCCURRENCE = 1;
	public static final int COMM_TYPE_RELEASE = 2;
	
	public static final KeyMap[][] ADAPTER_HISTORY_FIELDS = {
		{new KeyMap("이벤트ID", "event_Id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name")},
		{new KeyMap("이벤트ID", "event_Id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name")},
		{new KeyMap("이벤트ID", "event_Id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name")},
		{new KeyMap("이벤트ID", "event_Id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name")},
		{new KeyMap("이벤트ID", "event_Id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name")},
		{new KeyMap("이벤트ID", "event_Id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name")},
		{new KeyMap("이벤트ID", "event_Id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name")},
		{new KeyMap("이벤트ID", "event_Id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name")},
		{new KeyMap("이벤트ID", "event_Id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name")},
		{new KeyMap("이벤트ID", "event_Id"), new KeyMap("상황발생일시", "occured_date"), new KeyMap("상황발생시설물ID", "facility_id"), new KeyMap("발생장소명", "location_name")}		
	};
	
	private String commHeader;
	private String commBody;
	
	private int process=-1;
	private int commType;
	
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
	public int getCommType() {
		return commType;
	}
	public void setCommType(int commType) {
		this.commType = commType;
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
	
	public Adapter(String commHeader, String commType, String commBody){
		super();
		this.commHeader = commHeader;
		this.commBody = commBody;
		parseProcess(commHeader);
		parseCommType(commType);
		parseCommBody(commBody);
	}

	private void parseProcess(String commHeader){
		if(SmartUtil.isBlankObject(commHeader) || commHeader.length() != LENGTH_COMM_HEADER) return;
		
		if(	commHeader.equals(Event.ID_ENV_GALE) ||
			commHeader.equals(Event.ID_ENV_AIRFLOW) ||
			commHeader.equals(Event.ID_ENV_STORM) ||
			commHeader.equals(Event.ID_ENV_HEAVY_SNOWFALL) ||
			commHeader.equals(Event.ID_ENV_DRYING) ||
			commHeader.equals(Event.ID_ENV_STORM_SURGES) ||
			commHeader.equals(Event.ID_ENV_TSUNAMI) ||
			commHeader.equals(Event.ID_ENV_COLD_WAVE) ||
			commHeader.equals(Event.ID_ENV_TYPHOON) ||
			commHeader.equals(Event.ID_ENV_ASIAN_DUST) ||
			commHeader.equals(Event.ID_ENV_HEATWAVE) ||
			commHeader.equals(Event.ID_ENV_FINE_DUST)){
			this.process = System.PROCESS_ENV_WEAHTER;
		}else if(commHeader.equals(Event.ID_ENV_OZONE)){
			this.process = System.PROCESS_ENV_ATMOSPHERE;
		}else if(commHeader.equals(Event.ID_ENV_CANAL_WAY) || commHeader.equals(Event.ID_ENV_WATER)){
			this.process = System.PROCESS_ENV_WATER;
		}else if(commHeader.equals(Event.ID_TRAFFIC_ILLEGAL_PARKING)){
			this.process = System.PROCESS_TRAFFIC_ILLEGAL_PARKING;
		}else if(commHeader.equals(Event.ID_TRAFFIC_INCIDENT) || 
				commHeader.equals(Event.ID_TRAFFIC_ACCIDENTS) ||
				commHeader.equals(Event.ID_TRAFFIC_HIT_AND_RUN) ||
				commHeader.equals(Event.ID_TRAFFIC_VEHICLE_BREAKDOWN)){
			this.process = System.PROCESS_TRAFFIC_INCIDENT;
		}else if(commHeader.equals(Event.ID_DISASTER_FIRE)){
			this.process = System.PROCESS_DISASTER_FIRE;
		}else if(commHeader.equals(Event.ID_CRIME_EMERGENCY)){
			this.process = System.PROCESS_CRIME_CCTV;
		}else if(commHeader.equals(Event.ID_CRIME_VEHICLE)){
			this.process = System.PROCESS_CRIME_VEHICLES;
		}else if(commHeader.equals(Event.ID_WATERWORKS_LEAKS)){
			this.process = System.PROCESS_WATERWORKS_LEAKS;
		}else if(commHeader.equals(Event.ID_FACILITY_TROUBLE) || commHeader.equals(Event.ID_FACILITY_EMERGENCY)){
			this.process = System.PROCESS_FACILITY_MANAGEMENT;
		}else{
			return;
		}
	}
	
	private void parseCommType(String commType){
		if(SmartUtil.isBlankObject(commType) || commType.length()!=LENGTH_COMM_TYPE) return;
	
		if(commType.equals(Event.COMM_TYPE_OCCURRENCE))
			this.commType = COMM_TYPE_OCCURRENCE;
		if(commType.equals(Event.COMM_TYPE_RELEASE))
			this.commType = COMM_TYPE_RELEASE;
	
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
			if(keyMap.getId().equals("event_id"))
				dataRecord.put(keyMap.getKey(), this.eventId);
			else if(keyMap.getId().equals("occured_date"))
				dataRecord.put(keyMap.getKey(), this.occuredDate);
			else if(keyMap.getId().equals("facilityId"))
				dataRecord.put(keyMap.getKey(), this.facilityId);
			else if(keyMap.getId().equals("locationName"))
				dataRecord.put(keyMap.getKey(), this.locationName);
		}
		return dataRecord;
	}
	
	public void startProcess() throws Exception{
		if(this.process<0 || this.process>System.MAX_PROCESS || this.commType!=COMM_TYPE_OCCURRENCE) return;
		
		ProcessWork processWork = (ProcessWork)SwServiceFactory.getInstance().getWorkService().getWorkById(System.getProcessId(this.process));
		if(processWork==null) return;
		
		TaskInstance startTaskInstance = SwServiceFactory.getInstance().getInstanceService().getTaskInstanceById(processWork.getId(), processWork.getDiagram().getStartTask().getId());
		if(startTaskInstance==null) return;
		
		
		UcityUtil.startUServiceProcess(System.getProcessId(this.process), this.getDataRecord());
	}
	
	public void performTask(String taskInstId) throws Exception{
		TaskInstance taskInstance = null;
		if(SmartUtil.isBlankObject(taskInstId)){
			taskInstance = UcityUtil.getTaskInstanceByEventId(this.eventId, System.getProcessId(this.process));
		}else{
			taskInstance = SwServiceFactory.getInstance().getInstanceService().getTaskInstanceById(System.getProcessId(this.process), taskInstId);
		}
		
		if(SmartUtil.isBlankObject(taskInstance)) return;
		
		UcityUtil.performUServiceTask(taskInstance, this.getDataRecord());		
	}
}
