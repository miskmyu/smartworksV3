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

	public static final String USERVICE_CODE_ENVIRONMENT 	= "ENV";
	public static final String USERVICE_CODE_FACILITY 		= "FCL";
	public static final String USERVICE_CODE_PLATFORM 		= "PFM";
	public static final String USERVICE_CODE_SECURITY 		= "SEC";
	public static final String USERVICE_CODE_TRAFFIC 		= "TRF";	
	
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
	
	public static String[] getAllServiceNames(){
		return new String[] {
				"환경",
				"교통",			
				"방범",			
				"방재",			
				"상수도",			
				"시설물"				
		};
	}

	public static String getServiceCodeByUCode(String userviceCode){
		if(SmartUtil.isBlankObject(userviceCode)) return "";
		
		if(userviceCode.equals(USERVICE_CODE_ENVIRONMENT)) return ID_ENVIRONMENT;
		else if(userviceCode.equals(USERVICE_CODE_FACILITY)) return ID_FACILITY;
//		else if(userviceCode.equals(USERVICE_CODE_PLATFORM)) return ID_FACILITY;
		else if(userviceCode.equals(USERVICE_CODE_SECURITY)) return ID_DISASTER;
		else if(userviceCode.equals(USERVICE_CODE_TRAFFIC)) return ID_TRAFFIC;
		return "";

/* TO DO
		PFM 복합상황 91 도로통제
		
		SEC 방재 91 강도(11)
			91 미아(12)
			91 응급(13)
			91 용의차량추적(14)
			91 비상벨요청(15)
			92 호우(11)
			92 화재(12)
			92 태풍(13)
			92 지하차도침수(14)
			92 수위경보(15)
		
		TRF 교통 91 교통사고(11)
			91 뺑소니(12)
			91 차량고장(13)
			91 교통혼잡(15)
*/

	}

}
