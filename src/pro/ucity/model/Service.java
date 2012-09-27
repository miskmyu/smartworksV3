package pro.ucity.model;

import net.smartworks.util.SmartUtil;

public class Service {

	public static final String ID_ENVIRONMENT 		= "1101";
	public static final String ID_TRAFFIC 			= "1102";
	public static final String ID_DISASTER 			= "1103";//확인필요(이벤트와 코드가 다름)
	public static final String ID_CRIME 			= "1104";//확인필요(이벤트와 코드가 다름)
	public static final String ID_WATERWORKS 		= "1105";
	public static final String ID_FACILITY 			= "1106";
	public static final String ID_ENVIRONMENT_VMS 	= "1107";
	public static final String ID_TRAFFIC_VMS 		= "1108";
	public static final String ID_TRAFFIC_BIT 		= "1109";
	public static final String ID_TRAFFIC_KIOSK		= "1110";
	public static final String ID_MEDIABOARD 		= "1111";
	public static final String ID_PERM_CONTROL 		= "1112";
	public static final String ID_SMS_MMS 			= "1113";
	
	public static String getServiceNameByCode(String serviceCode){
		if(SmartUtil.isBlankObject(serviceCode)) return "";
		if(serviceCode.equals(ID_ENVIRONMENT)){
			return "환경";
		}else if(serviceCode.equals(ID_TRAFFIC)){
			return "교통";			
		}else if(serviceCode.equals(ID_DISASTER)){
			return "방범";			
		}else if(serviceCode.equals(ID_CRIME)){
			return "방재";			
		}else if(serviceCode.equals(ID_WATERWORKS)){
			return "상수도";			
		}else if(serviceCode.equals(ID_FACILITY)){
			return "시설물";			
		}else if(serviceCode.equals(ID_ENVIRONMENT_VMS)){
			return "환경 VMS 표출";			
		}else if(serviceCode.equals(ID_TRAFFIC_VMS)){
			return "교통 VMS 표출";			
		}else if(serviceCode.equals(ID_TRAFFIC_BIT)){
			return "교통 BIT 표출";			
		}else if(serviceCode.equals(ID_TRAFFIC_KIOSK)){
			return "교통 KIOSK 표출";			
		}else if(serviceCode.equals(ID_MEDIABOARD)){
			return "미디어보드 표출";			
		}else if(serviceCode.equals(ID_PERM_CONTROL)){
			return "상시관제";			
		}else if(serviceCode.equals(ID_SMS_MMS)){
			return "SMS/MMS 발송";			
		}
		return "";
	}
}
