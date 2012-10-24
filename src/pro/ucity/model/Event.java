package pro.ucity.model;

import net.smartworks.util.SmartUtil;

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
	public static final String ID_ENV_WARNING 				= "0116";
	public static final String ID_ENV_PULLUTION 				= "0131";
	
	public static final String ID_TRAFFIC_INCIDENT 			= "0201";
	public static final String ID_TRAFFIC_ILLEGAL_PARKING 	= "0202";
	public static final String ID_TRAFFIC_ACCIDENTS 		= "0203";
	public static final String ID_TRAFFIC_HIT_AND_RUN 		= "0204";
	public static final String ID_TRAFFIC_VEHICLE_BREAKDOWN = "0205";
	public static final String ID_TRAFFIC_CONTROL_SITUATION ="0206";      //교통통제상황
	public static final String ID_TRAFFIC_CONGESTION ="0207";             //교통혼잡상황
	
	public static final String ID_DISASTER_FIRE 			= "0301";
	public static final String ID_DISASTER_TYPHOON 		= "0302";    //태풍상황
	public static final String ID_DISASTER_UNDERPASS_FLOODING		= "0303";  //지하도침수상황
	public static final String ID_DISASTER_WATER_LEVEL_ALRAM		= "0304";   //수위경보
	
	
	public static final String ID_CRIME_VEHICLE 				= "0401";
	public static final String ID_CRIME_EMERGENCY 			= "0402";
	public static final String ID_EMERGENCY_SITUATIONS 		= "0403";      //응급상황
	public static final String ID_ROBBERY_SITUATION 			= "0404";      //강도상황
	public static final String ID_STRAY_CHILD_SITUATION 		= "0405";      //미아상황

	public static final String ID_WATERWORKS_LEAKS 			= "0501";	   //수량 초과
	public static final String ID_WATERWORKS_UNDER 			= "0502";      //수량 미만
	public static final String ID_HYDRAULIC_LEAKS 			= "0503";      //유압 초과
	public static final String ID_HYDRAULIC_UNDER 			= "0504";      //유압 미만

	public static final String ID_FACILITY_TROUBLE 			= "0601";
	public static final String ID_FACILITY_EMERGENCY		= "0602";
	
	public static final String TYPE_OCCURRENCE			= "01";
	public static final String TYPE_RELEASE				= "02";
	public static final String TYPE_PROCESSING			= "03";
	
	public static final int TASK_EVENT_OCCURRENCE			= 1;
	public static final int TASK_EVENT_PROCESSING			= 2;
	public static final int TASK_EVENT_RELEASE				= 3;
		
	public static String getEventNameByCode(String eventId){
		if(SmartUtil.isBlankObject(eventId)) return "";
		
		if(eventId.equals(ID_ENV_GALE)){
			return "강풍";
		}else if(eventId.equals(ID_ENV_AIRFLOW)){
			return "풍량";
		}else if(eventId.equals(ID_ENV_STORM)){
			return "호우";
		}else if(eventId.equals(ID_ENV_HEAVY_SNOWFALL)){
			return "대설";
		}else if(eventId.equals(ID_ENV_DRYING)){
			return "건조";
		}else if(eventId.equals(ID_ENV_STORM_SURGES)){
			return "폭풍해일";
		}else if(eventId.equals(ID_ENV_TSUNAMI)){
			return "지진해일";
		}else if(eventId.equals(ID_ENV_COLD_WAVE)){
			return "한파";
		}else if(eventId.equals(ID_ENV_TYPHOON)){
			return "태풍";
		}else if(eventId.equals(ID_ENV_ASIAN_DUST)){
			return "황사";
		}else if(eventId.equals(ID_ENV_HEATWAVE)){
			return "폭염";
		}else if(eventId.equals(ID_ENV_FINE_DUST)){
			return "대기특보(미세먼지)";
		}else if(eventId.equals(ID_ENV_OZONE)){
			return "대기특보(오존)";
		}else if(eventId.equals(ID_ENV_PULLUTION)){
			return "대기오염";
		}else if(eventId.equals(ID_ENV_CANAL_WAY)){
			return "주운";
		}else if(eventId.equals(ID_ENV_WATER)){
			return "수질";
		}else if(eventId.equals(ID_ENV_WARNING)){
			return "환경경보";
		}else if(eventId.equals(ID_TRAFFIC_INCIDENT)){
			return "돌발";
		}else if(eventId.equals(ID_TRAFFIC_ILLEGAL_PARKING)){
			return "불법주정차(용의차량)";
		}else if(eventId.equals(ID_TRAFFIC_ACCIDENTS)){
			return "교통사고";
		}else if(eventId.equals(ID_TRAFFIC_HIT_AND_RUN)){
			return "뺑소니";
		}else if(eventId.equals(ID_TRAFFIC_VEHICLE_BREAKDOWN)){
			return "차량고장";
		}else if(eventId.equals(ID_TRAFFIC_CONTROL_SITUATION)){
			return "교통통제";
		}else if(eventId.equals(ID_TRAFFIC_CONGESTION)){
			return "교통혼잡";
		}else if(eventId.equals(ID_DISASTER_FIRE)){
			return "화재";
		}else if(eventId.equals(ID_DISASTER_TYPHOON)){
			return "태풍";
		}else if(eventId.equals(ID_DISASTER_UNDERPASS_FLOODING)){
			return "지하도침수";
		}else if(eventId.equals(ID_DISASTER_WATER_LEVEL_ALRAM)){
			return "수위경보";
		}else if(eventId.equals(ID_CRIME_VEHICLE)){
			return "방범(용의차량)상황";
		}else if(eventId.equals(ID_CRIME_EMERGENCY)){
			return "비상벨요청";
		}else if(eventId.equals(ID_EMERGENCY_SITUATIONS)){
			return "응급";
		}else if(eventId.equals(ID_ROBBERY_SITUATION)){
			return "강도";
		}else if(eventId.equals(ID_STRAY_CHILD_SITUATION)){
			return "미아";
		}else if(eventId.equals(ID_WATERWORKS_LEAKS)){
			return "상하수도누수";
		}else if(eventId.equals(ID_WATERWORKS_UNDER)){
			return "수량미만";
		}else if(eventId.equals(ID_HYDRAULIC_LEAKS)){
			return "유압초과";
		}else if(eventId.equals(ID_HYDRAULIC_UNDER)){
			return "유압미만";
		}else if(eventId.equals(ID_FACILITY_TROUBLE)){
			return "시설물고장";
		}else if(eventId.equals(ID_FACILITY_EMERGENCY)){
			return "긴급메세지";
		}
		return "";
	}
	public static String[] getAllEventNames(){
		return new String[]{
				"강풍",	
				"호우",
				"대설",
				"건조",
				"폭풍해일",
				"지진해일",
				"한파",
				"태풍",
				"황사",
				"폭염",
				"대기특보(미세먼지)",
				"대기특보(오존)",
				"대기오염",
				"주운",
				"수질",
				"환경경보",
				"돌발상황",
				"불법주정차(용의차량)상황",
				"교통사고상황",
				"뺑소니상황",
				"차량고장상황",
				"교통통제상황",
				"교통혼잡상황",
				"교통통제상황",
				"교통혼잡상황",
				"화재",
				"태풍상황",
				"지하도침수상황",
				"수위경보",
				"방범(용의차량)상황",
				"비상벨상황",
				"응급상황",
				"강도상황",
				"미아상황",
				"수량초과",
				"수량미만",
				"유압초과",
				"유압미만",
				"장애발생",
				"긴급메세지"
		};
	}
	
	public static String[] getAllEventNames(String serviceName){
		if(SmartUtil.isBlankObject(serviceName)) return null;
		if(serviceName.equals("환경"))
			return new String[]{
					"강풍",	
					"호우",
					"대설",
					"건조",
					"폭풍해일",
					"지진해일",
					"한파",
					"태풍",
					"황사",
					"폭염",
					"대기특보(미세먼지)",
					"대기특보(오존)",
					"대기오염",
					"주운",
					"수질",
					"환경경보"
			};
		else if(serviceName.equals("교통"))
			return new String[]{
					"돌발상황",
					"불법주정차(용의차량)상황",
					"교통사고상황",
					"뺑소니상황",
					"차량고장상황",
					"교통통제상황",
					"교통혼잡상황"
			};
		else if(serviceName.equals("방범"))
			return new String[]{
					"방범(용의차량)상황",
					"비상벨상황",
					"응급상황",
					"강도상황",
					"미아상황"
			};
		else if(serviceName.equals("방재"))
			return new String[]{
					"화재",
					"태풍상황",
					"지하도침수상황",
					"수위경보"
			};
		else if(serviceName.equals("상수도"))
			return new String[]{
					"수량초과",
					"수량미만",
					"유압초과",
					"유압미만"
			};
		else if(serviceName.equals("시설물"))
			return new String[]{
					"장애발생",
					"긴급메세지"
			};
		return null;
	}
	
	public static int getProcessByEventId(String eventId){
		
		if(SmartUtil.isBlankObject(eventId)) return -1;
		
		if(	eventId.equals(Event.ID_ENV_GALE) ||
			eventId.equals(Event.ID_ENV_AIRFLOW) ||
			eventId.equals(Event.ID_ENV_STORM) ||
			eventId.equals(Event.ID_ENV_HEAVY_SNOWFALL) ||
			eventId.equals(Event.ID_ENV_DRYING) ||
			eventId.equals(Event.ID_ENV_STORM_SURGES) ||
			eventId.equals(Event.ID_ENV_TSUNAMI) ||
			eventId.equals(Event.ID_ENV_COLD_WAVE) ||
			eventId.equals(Event.ID_ENV_TYPHOON) ||
			eventId.equals(Event.ID_ENV_ASIAN_DUST) ||
			eventId.equals(Event.ID_ENV_HEATWAVE) ||
			eventId.equals(Event.ID_ENV_WARNING)){
			return System.PROCESS_ENV_WEAHTER;
		}else if(eventId.equals(Event.ID_ENV_OZONE) || eventId.equals(Event.ID_ENV_FINE_DUST) || eventId.equals(Event.ID_ENV_PULLUTION)){
			return System.PROCESS_ENV_ATMOSPHERE;
		}else if(eventId.equals(Event.ID_ENV_CANAL_WAY) || eventId.equals(Event.ID_ENV_WATER)){
			return System.PROCESS_ENV_WATER;
		}else if(eventId.equals(Event.ID_TRAFFIC_ILLEGAL_PARKING)){
			return System.PROCESS_TRAFFIC_ILLEGAL_PARKING;
		}else if(eventId.equals(Event.ID_TRAFFIC_INCIDENT) || 
				eventId.equals(Event.ID_TRAFFIC_ACCIDENTS) ||
				eventId.equals(Event.ID_TRAFFIC_HIT_AND_RUN) ||
				eventId.equals(Event.ID_TRAFFIC_VEHICLE_BREAKDOWN) ||
				eventId.equals(Event.ID_TRAFFIC_CONTROL_SITUATION) ||
				eventId.equals(Event.ID_TRAFFIC_CONGESTION)){
			return System.PROCESS_TRAFFIC_INCIDENT;
		}else if(eventId.equals(Event.ID_DISASTER_FIRE)){
			return System.PROCESS_DISASTER_FIRE;
		}else if(eventId.equals(Event.ID_CRIME_EMERGENCY) ||
				eventId.equals(Event.ID_EMERGENCY_SITUATIONS) ||
				eventId.equals(Event.ID_ROBBERY_SITUATION) ||
				eventId.equals(Event.ID_STRAY_CHILD_SITUATION)){
			return System.PROCESS_CRIME_CCTV;
		}else if(eventId.equals(Event.ID_CRIME_VEHICLE)){
			return System.PROCESS_CRIME_VEHICLES;
		}else if(eventId.equals(Event.ID_WATERWORKS_LEAKS) ||
				eventId.equals(Event.ID_WATERWORKS_UNDER) ||
				eventId.equals(Event.ID_HYDRAULIC_LEAKS) ||
				eventId.equals(Event.ID_HYDRAULIC_UNDER) ||
				eventId.equals(Event.ID_DISASTER_WATER_LEVEL_ALRAM) ||
				eventId.equals(Event.ID_DISASTER_UNDERPASS_FLOODING)){
			return System.PROCESS_WATERWORKS_LEAKS;
		}else if(eventId.equals(Event.ID_FACILITY_TROUBLE) || eventId.equals(Event.ID_FACILITY_EMERGENCY)){
			return System.PROCESS_FACILITY_MANAGEMENT;
		}
		return -1;
	}
	
	public static String getEventIdByCode(String userviceCode, String serviceCode, String eventCode){
		if(SmartUtil.isBlankObject(userviceCode) || SmartUtil.isBlankObject(serviceCode) || SmartUtil.isBlankObject(eventCode)) return "";
		
		if(userviceCode.equals(Service.USERVICE_CODE_SECURITY) && serviceCode.equals("092") && eventCode.equals("11"))
			return ID_ENV_STORM;
		else if(userviceCode.equals(Service.USERVICE_CODE_ENVIRONMENT) && serviceCode.equals("092") && eventCode.equals("11"))
			return ID_ENV_PULLUTION;
		else if(userviceCode.equals(Service.USERVICE_CODE_SECURITY) && serviceCode.equals("092") && eventCode.equals("13"))
			return ID_ENV_TYPHOON;
		else if(userviceCode.equals(Service.USERVICE_CODE_ENVIRONMENT) && serviceCode.equals("093"))
			return ID_ENV_WATER;
		else if(userviceCode.equals(Service.USERVICE_CODE_ENVIRONMENT) && serviceCode.equals("091"))
			return ID_ENV_WARNING;
		else if(userviceCode.equals(Service.USERVICE_CODE_TRAFFIC) && serviceCode.equals("091") && eventCode.equals("11"))
			return ID_TRAFFIC_ACCIDENTS;
		else if(userviceCode.equals(Service.USERVICE_CODE_TRAFFIC) && serviceCode.equals("091") && eventCode.equals("12"))
			return ID_TRAFFIC_HIT_AND_RUN;
		else if(userviceCode.equals(Service.USERVICE_CODE_TRAFFIC) && serviceCode.equals("091") && eventCode.equals("13"))
			return ID_TRAFFIC_VEHICLE_BREAKDOWN;
		else if(userviceCode.equals(Service.USERVICE_CODE_SECURITY) && serviceCode.equals("092") && eventCode.equals("12"))
			return ID_DISASTER_FIRE;
		else if(userviceCode.equals(Service.USERVICE_CODE_SECURITY) && serviceCode.equals("091") && eventCode.equals("14"))
			return ID_CRIME_VEHICLE;
		else if(userviceCode.equals(Service.USERVICE_CODE_SECURITY) && serviceCode.equals("091") && eventCode.equals("15"))
			return ID_CRIME_EMERGENCY;
		else if(userviceCode.equals(Service.USERVICE_CODE_FACILITY) && serviceCode.equals("092") && eventCode.equals("11"))
			return ID_FACILITY_TROUBLE;
		else if(userviceCode.equals(Service.USERVICE_CODE_FACILITY) && serviceCode.equals("092") && eventCode.equals("12"))
			return ID_FACILITY_TROUBLE;//FCL 시설물 92 시설물파손(12)
		else if(userviceCode.equals(Service.USERVICE_CODE_FACILITY) && serviceCode.equals("091"))
			return ID_WATERWORKS_LEAKS;
		else if(userviceCode.equals(Service.USERVICE_CODE_SECURITY) && serviceCode.equals("091") && eventCode.equals("11"))
			return ID_ROBBERY_SITUATION;//SEC 방재 91 강도(11)
		else if(userviceCode.equals(Service.USERVICE_CODE_SECURITY) && serviceCode.equals("091") && eventCode.equals("12"))
			return ID_STRAY_CHILD_SITUATION;//SEC 방재	91 미아(12)
		else if(userviceCode.equals(Service.USERVICE_CODE_SECURITY) && serviceCode.equals("091") && eventCode.equals("13"))
			return ID_EMERGENCY_SITUATIONS;//SEC 방재	91 응급(13)
		else if(userviceCode.equals(Service.USERVICE_CODE_SECURITY) && serviceCode.equals("092") && eventCode.equals("14"))
			return ID_DISASTER_UNDERPASS_FLOODING;//SEC 방재	92 지하차도침수(14)
		else if(userviceCode.equals(Service.USERVICE_CODE_SECURITY) && serviceCode.equals("092") && eventCode.equals("15"))
			return ID_DISASTER_WATER_LEVEL_ALRAM;//SEC 방재	92 수위경보(15)
		else if(userviceCode.equals(Service.USERVICE_CODE_TRAFFIC) && serviceCode.equals("091") && eventCode.equals("15"))
			return ID_TRAFFIC_CONGESTION;//TRF 교통	91 교통혼잡(15)
		else if(userviceCode.equals(Service.USERVICE_CODE_PLATFORM) && serviceCode.equals("091") && eventCode.equals("11"))
			return ID_TRAFFIC_CONTROL_SITUATION;//TRF 교통	91 교통혼잡(15)
		return "";
		
/* TO DO
		FCL 시설물 92 시설물파손(12)
		SEC 방재 91 강도(11)
		SEC 방재	91 미아(12)
		SEC 방재	91 응급(13)
		SEC 방재	92 지하차도침수(14)
		SEC 방재	92 수위경보(15)
		TRF 교통	91 교통혼잡(15)
*/
		
	}	
}
