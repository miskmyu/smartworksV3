package pro.ucity.model;

public class Event {

	public static final String ID_ENV_GALE 					= "0101";
	public static final String ID_ENV_AIRFLOW 				= "0102";
	public static final String ID_ENV_STORM 				= "0103";
	public static final String ID_ENV_HEAVY_SNOWFALL 		= "0104";
	public static final String ID_ENV_DRYING 				= "0105";
	public static final String ID_ENV_STORM_SURGES 			= "0106";
	public static final String ID_ENV_TSUNAMI 				= "0107";
	public static final String ID_ENV_COLD_WAVE 			= "0108";
	public static final String ID_ENV_TYPHOON 				= "0109";
	public static final String ID_ENV_ASIAN_DUST			= "0110";
	public static final String ID_ENV_HEATWAVE 				= "0111";
	public static final String ID_ENV_FINE_DUST 			= "0112";
	public static final String ID_ENV_OZONE 				= "0113";
	public static final String ID_ENV_CANAL_WAY 			= "0114";
	public static final String ID_ENV_WATER 				= "0115";
	
	public static final String ID_TRAFFIC_INCIDENT 			= "0201";
	public static final String ID_TRAFFIC_ILLEGAL_PARKING 	= "0202";
	public static final String ID_TRAFFIC_ACCIDENTS 		= "0203";
	public static final String ID_TRAFFIC_HIT_AND_RUN 		= "0204";
	public static final String ID_TRAFFIC_VEHICLE_BREAKDOWN = "0205";
	
	public static final String ID_DISASTER_FIRE 			= "0301";	
	
	public static final String ID_CRIME_VEHICLE 			= "0401";
	public static final String ID_CRIME_EMERGENCY 			= "0402";

	public static final String ID_WATERWORKS_LEAKS 			= "0501";

	public static final String ID_FACILITY_TROUBLE 			= "0601";
	public static final String ID_FACILITY_EMERGENCY		= "0602";
	
	public static final String TYPE_OCCURRENCE				= "01";
	public static final String TYPE_RELEASE					= "02";
	public static final String TYPE_PROCESSING				= "03";
	
	public static final int TASK_EVENT_OCCURRENCE			= 1;
	public static final int TASK_EVENT_PROCESSING			= 2;
	public static final int TASK_EVENT_RELEASE				= 3;
	
}
