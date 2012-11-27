package pro.ucity.model;

import net.smartworks.model.KeyMap;
import net.smartworks.util.SmartUtil;

public class System {

	public static final int MAX_PROCESS						= 15;
	public static final int PROCESS_ENV_WEAHTER				= 0;    //기상
	public static final int PROCESS_ENV_ATMOSPHERE			= 1;    //대기
	public static final int PROCESS_ENV_WATER				= 2;        //수질
	public static final int PROCESS_TRAFFIC_ILLEGAL_PARKING	= 3;    //불법주정차
	public static final int PROCESS_TRAFFIC_INCIDENT		= 4;        //돌발
	public static final int PROCESS_DISASTER_FIRE			= 5;         //화재특보
	public static final int PROCESS_CRIME_CCTV				= 6;     //방범 cctv
	public static final int PROCESS_CRIME_VEHICLES			= 7;     //방범 용의차량
	public static final int PROCESS_WATERWORKS_LEAKS		= 8;         //상수도누수
	public static final int PROCESS_FACILITY_MANAGEMENT		= 9;      //시설물 관리
	public static final int PROCESS_ENV_VMS 			= 10;
	public static final int PROCESS_TRAFFIC_VMS 			= 11;
	public static final int PROCESS_TRAFFIC_BIT 			= 12;
	public static final int PROCESS_MEDIABORAD 			= 13;
	public static final int PROCESS_KIOSK 			= 14;
	
	
	public static final int ID_OPERATION_PORTAL 		= 1;
	public static final int ID_INTEGRATED_CONTROL 		= 2;
	public static final int ID_COMMUNICATION_MIDDLEWARE = 3;
	public static final int ID_DEVICE_MIDDLEWARE 		= 4;
	public static final int ID_ADAPTER 					= 5;	
	
	public static final int TABLE_ID_OPPORTAL_SITUATION 	= 11;
	public static final int TABLE_ID_OPPORTAL_DISPLAY 		= 12;
	public static final int TABLE_ID_OPPORTAL_SMS 		= 13;
	public static final int TABLE_ID_INTCON_SITUATION 		= 21;
	public static final int TABLE_ID_COMMID_TRACE 			= 31;
	public static final int TABLE_ID_DEVMID_SEND_STATUS 	= 41;
	public static final int TABLE_ID_ADAPTER_HISTORY		= 51;	

	public static final String DATA_FIELD_NAME_EVENT_ID			= "Event Id";
	public static final String DATA_FIELD_NAME_TABLE_ID			= "Table Id";
	public static final String DATA_FIELD_NAME_STATUS			= "Status";
	public static final String DATA_FIELD_NAME_DISPLAY_ID		= "Display Id";
	public static final String DATA_FIELD_NAME_DEVICE_ID		= "Device Id";
	public static final String DATA_FIELD_NAME_SMS_ID		= "SMS Id";
	public static final String DATA_FIELD_NAME_TIMEOUT			= "Timeout Minutes";
	
	public static final long DEFAULT_TASK_TIMEOUT				= 10 * 60 * 1000;
	public static final long DEFAULT_POLLING_INTERVAL 			= 10 * 1000;
	
	public static final String TASK_FORM_NAME_USERVICE_END 		= "상황발생 종료화면";
	public static final String TASK_NAME_USERVICE_END 			= "상황종료";
	public static final String TASK_NAME_DISPLAY_HISTORY 			= "표출이력";
	
	public static final String REPORT_OPTION_CATEGORY_BY_TIME = "option.category.byTime";
	public static final String REPORT_OPTION_CATEGORY_BY_AMPM = "option.category.byAmPm";
	public static final String REPORT_OPTION_CATEGORY_BY_DAY = "option.category.byDay";
	public static final String REPORT_OPTION_CATEGORY_BY_MONTH = "option.category.byMonth";
	public static final String REPORT_OPTION_CATEGORY_BY_SEASON = "option.category.bySeason";
	public static final String REPORT_OPTION_CATEGORY_BY_QUARTER = "option.category.byQuarter";
	public static final String REPORT_OPTION_CATEGORY_BY_HALFYEAR = "option.category.byHalfYear";
	
	public static final String REPORT_OPTION_THIS_YEAR = "option.period.thisYear";
	public static final String REPORT_OPTION_RECENT_A_YEAR = "option.period.recentAYear";
	public static final String REPORT_OPTION_RECENT_THREE_YEARS = "option.period.recentThreeYears";
	public static final String REPORT_OPTION_RECENT_FIVE_YEARS = "option.period.recentFiveYears";
	public static final String REPORT_OPTION_ALL_HISTORY = "option.period.all";

	public static final String REPORT_OPTION_ALL_SERVICES = "option.service.all";
	public static final String REPORT_OPTION_ALL_EVENTS = "option.event.all";
	

	public static String getProcessId(int process){
		if(process<0 || process>MAX_PROCESS) return null;
		
		//dev
//		switch(process){
//		case PROCESS_ENV_WEAHTER:
//			return "pkg_336b0e079fc44ab19acbe49ded2e8b12";
//		case PROCESS_ENV_ATMOSPHERE:
//			return "pkg_9564849550184543b06fa46e3290f296";
//		case PROCESS_ENV_WATER:
//			return "pkg_9564849550184543b06fa46e3290f296";
//		case PROCESS_TRAFFIC_ILLEGAL_PARKING:
//			return "pkg_9564849550184543b06fa46e3290f296";
//		case PROCESS_TRAFFIC_INCIDENT:
//			return "pkg_9564849550184543b06fa46e3290f296";
//		case PROCESS_DISASTER_FIRE:
//			return "pkg_9564849550184543b06fa46e3290f296";
//		case PROCESS_CRIME_CCTV:
//			return "pkg_9564849550184543b06fa46e3290f296";
//		case PROCESS_CRIME_VEHICLES:
//			return "pkg_9564849550184543b06fa46e3290f296";
//		case PROCESS_WATERWORKS_LEAKS:
//			return "pkg_9564849550184543b06fa46e3290f296";
//		case PROCESS_FACILITY_MANAGEMENT:
//			return "pkg_9564849550184543b06fa46e3290f296";
		
//		개발서버
		switch(process){
		case PROCESS_ENV_WEAHTER:
			return "pkg_6247af34746044228556b5366752141e";
		case PROCESS_ENV_ATMOSPHERE:
			return "pkg_95c0181fcb3f47429fadc3ea50f0d803";
		case PROCESS_ENV_WATER:
			return "pkg_116c47c171f74fcbaaa69381e8730f25";
		case PROCESS_TRAFFIC_ILLEGAL_PARKING:
			return "pkg_356e545e04444974bf1c07fadc073a77";
		case PROCESS_TRAFFIC_INCIDENT:
			return "pkg_f9b10b352d7b4136a3f7bdd8682a176a";
		case PROCESS_DISASTER_FIRE:
			return "pkg_696d2e135f804dd5a2e652d0b10ef8a3";
		case PROCESS_CRIME_CCTV:
			return "pkg_ac7357a828eb46b9bd2eaa1d295a42e7";
		case PROCESS_CRIME_VEHICLES:
			return "pkg_59a3f0787cb749bbaaa518d471bee01a";
		case PROCESS_WATERWORKS_LEAKS:
			return "pkg_a0d39535ff9540e7ae95fdb6bf82329f";
		case PROCESS_FACILITY_MANAGEMENT:
			return "pkg_c161ed7d5e6942a2a4e5af43fdc50af4";
		
		}
		
		return null;
	}
	
	public static String getManualProcessId(String userviceCode, String serviceCode, String eventCode, String status){
		if(SmartUtil.isBlankObject(userviceCode) || SmartUtil.isBlankObject(serviceCode) || SmartUtil.isBlankObject(eventCode) || SmartUtil.isBlankObject(status)) return null;
		
		if(userviceCode.equals(Service.USERVICE_CODE_SECURITY) && serviceCode.equals("092") && eventCode.equals("11")){
			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
				return "pkg_dc91d2ec9b094b4ebd27dc4c9ff63822";
			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
				return "pkg_c43e60dda6564140a9105110b713c536";
			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
				return "pkg_825e6fe6d48841c9b5cffd2956abc3a3";
			return null;//호우
		}else if(userviceCode.equals(Service.USERVICE_CODE_ENVIRONMENT) && serviceCode.equals("092") && eventCode.equals("11")){
			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
				return "pkg_9de67e21265d4be3897dc0f72a980917";
			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
				return "pkg_5483a6924d7d47b3951f046ea358f0d9";
			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
				return "pkg_83f1a038f3fc4427811d76ad9dee51e8";
			return null;//대기
		}else if(userviceCode.equals(Service.USERVICE_CODE_SECURITY) && serviceCode.equals("092") && eventCode.equals("13")){
			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
				return "pkg_ff9f46ac20bb4738b0bec085c6ace804";
			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
				return "pkg_8a009024238a454d9e26b0e44d4db898";
			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
				return "pkg_94f0d49949b14e59a8904d09e34e9f0f";
			return null;//태풍
		}else if(userviceCode.equals(Service.USERVICE_CODE_ENVIRONMENT) && serviceCode.equals("093")){
			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
				return "pkg_4951539ef1eb40dd815079f30185cd57";
			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
				return "pkg_4345157596784b9f83f80539671063a2";
			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
				return "pkg_5d1afb61edd7421e9f8b234730e1d4c7";
			return null;//수질
		}else if(userviceCode.equals(Service.USERVICE_CODE_ENVIRONMENT) && serviceCode.equals("091")){
			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
				return "pkg_1dac5ad2b0884866a7a34f02c6e6dbeb";
			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
				return "pkg_01dc24dc84eb4304be25e9ac72bddfc6";
			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
				return "pkg_bcd556318f274bcfaede3056d82dc4d2";
			return null;//환경경보
		}else if(userviceCode.equals(Service.USERVICE_CODE_TRAFFIC) && serviceCode.equals("091") && eventCode.equals("11")){
			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
				return "pkg_03063ef9523d45e6909325b1e84b2e73";
			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
				return "pkg_7d814eac92a24fdd9944b4725b6c9000";
			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
				return "pkg_a5549a663d0f45269f8094364e4107d7";
			return null;//교통사고
		}else if(userviceCode.equals(Service.USERVICE_CODE_TRAFFIC) && serviceCode.equals("091") && eventCode.equals("12")){
			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
				return "pkg_cab1086a9e8a439098dad6f46e3140eb";
			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
				return "pkg_1f5799d70f5249a684902f99c987735c";
			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
				return "pkg_4ab3d823a1504328ae35e00dd8761e1c";
			return null;//뺑소니
		}else if(userviceCode.equals(Service.USERVICE_CODE_TRAFFIC) && serviceCode.equals("091") && eventCode.equals("13")){
			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
				return "pkg_af1bed692e74415fab1e2b3413ab67b0";
			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
				return "pkg_43bac75d70da4cfc9a1629106a810725";
			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
				return "pkg_22b540c42a1a46c4b25d6ff61bfb4fc5";
			return null;//차량고장
		}else if(userviceCode.equals(Service.USERVICE_CODE_SECURITY) && serviceCode.equals("092") && eventCode.equals("12")){
			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
				return "pkg_9c7a8b7690cf4c4a810f2fb44a8b9511";
			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
				return "pkg_d416790a8da24ed29879d351658deeeb";
			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
				return "pkg_34fee390431140b0a2f9d976877d0497";
			return null;//화재
		}else if(userviceCode.equals(Service.USERVICE_CODE_SECURITY) && serviceCode.equals("091") && eventCode.equals("14")){
			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
				return "pkg_ee7339d2132b4dc2bf6e63ec16ec1fcb";
			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
				return "pkg_11f909a45f9c40129380152f44234237";
			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
				return "pkg_90b8f3c58fce4858abf11fb1a5a76a2f";
			return null;//방범용의차량
		}else if(userviceCode.equals(Service.USERVICE_CODE_SECURITY) && serviceCode.equals("091") && eventCode.equals("15")){
			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
				return "pkg_0473f7d619934e2da4cabde0bd47d7fd";
			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
				return "pkg_fe5279b1c79640ce813185b3c451a8ce";
			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
				return "pkg_c1e63567691743aeb8f6d7128c8f838e";
			return null;//비상벨
		}else if(userviceCode.equals(Service.USERVICE_CODE_SECURITY) && serviceCode.equals("091") && eventCode.equals("11")){
			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
				return "pkg_ccf617246c1849699b48754231282db1";
			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
				return "pkg_4d8d8c805b1747b2b66bc3fdd72c9b64";
			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
				return "pkg_0a8a8f31c278430d82add9e02b6fc7bf";
			return null;//강도
		}else if(userviceCode.equals(Service.USERVICE_CODE_SECURITY) && serviceCode.equals("091") && eventCode.equals("12")){
			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
				return "pkg_2c014ab8e596482c83237e0038d6666e";
			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
				return "pkg_3bb05b4c4d914c9382be38844fba723f";
			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
				return "pkg_330c4f0e4a5f43ed9265b39e7b47217c";
			return null;//미아
		}else if(userviceCode.equals(Service.USERVICE_CODE_SECURITY) && serviceCode.equals("091") && eventCode.equals("13")){
			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
				return "pkg_f53d36914edc409bbe4be571d9f7dd88";
			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
				return "pkg_33a6786c36534ca6b602741713288a5a";
			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
				return "pkg_e88ae1739cea4fb7b8d1c9e4f83690a4";
			return null;//응급
		}else if(userviceCode.equals(Service.USERVICE_CODE_SECURITY) && serviceCode.equals("092") && eventCode.equals("14")){
			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
				return "pkg_4d6d8f4c69fb4937a87b5917d2322694";
			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
				return "pkg_12b385bf214b4981997168d3e1ed322b";
			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
				return "pkg_3946234e88e44b70b345839f793010a9";
			return null;//지하차도침수
		}else if(userviceCode.equals(Service.USERVICE_CODE_SECURITY) && serviceCode.equals("092") && eventCode.equals("15")){
			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
				return "pkg_f50bbdd0bbdd497fae84cb9fbb72f2b9";
			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
				return "pkg_cfdca9eac7dd40c88361ae5e80e4a7d4";
			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
				return "pkg_61023d86b54845e995576bb5c63973b9";
			return null;//수위경보
		}else if(userviceCode.equals(Service.USERVICE_CODE_TRAFFIC) && serviceCode.equals("091") && eventCode.equals("15")){
			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
				return "pkg_1d7b9b3fae9841aa83ec94805312a8a2";
			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
				return "pkg_bd646089e8d14d6786331423981d2b1f";
			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
				return "pkg_39b55eb57d504bbe9d67623950bdb047";
			return null;//교통혼잡
		}else if(userviceCode.equals(Service.USERVICE_CODE_PLATFORM) && serviceCode.equals("091") && eventCode.equals("11")){
			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
				return "pkg_96ece13e42894455a98aed75e16f2fa1";
			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
				return "pkg_1fca4579951443be8d11b95d22bd6f74";
			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
				return "pkg_94756601af6a4798815f9681cc1efee7";
			return null;//도로통제
		}else if(userviceCode.equals(Service.USERVICE_CODE_FACILITY) && serviceCode.equals("091")){
			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
				return "pkg_85a803f50c204b9d97e0eddeca52fab7";
			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
				return "pkg_f3377a11073946789166451117b0dced";
			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
				return "pkg_eba9e94abb0c46a99233920c19ea65f6";
			return null;//상하수도 누수
		}else if(userviceCode.equals(Service.USERVICE_CODE_FACILITY) && serviceCode.equals("092") && eventCode.equals("11")){
			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
				return "pkg_4b54b8c95dc5492c9d0d9df23b3ce413";
			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
				return "pkg_b7dfc939aee8420f94114c3858e55a57";
			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
				return "pkg_2a79a0d1dd2b4c47b276f25e3b25be71";
			return null;//시설물고장
		}
		
		return null;
	}
	
	public static String getTableName(String tableId){
		if(SmartUtil.isBlankObject(tableId)) return null;
		int iTableId = Integer.parseInt(tableId);
		switch(iTableId){
		case System.TABLE_ID_OPPORTAL_SITUATION:
			return UcityConstant.getQueryByKey("System.TABLE_NAME_OPPORTAL_SITUATION");
		case System.TABLE_ID_OPPORTAL_DISPLAY:
			return UcityConstant.getQueryByKey("System.TABLE_NAME_OPPORTAL_DISPLAY");
		case System.TABLE_ID_OPPORTAL_SMS:
			return UcityConstant.getQueryByKey("System.TABLE_NAME_OPPORTAL_SMS");
		case System.TABLE_ID_INTCON_SITUATION:
			return UcityConstant.getQueryByKey("System.TABLE_NAME_INTCON_SITUATION");
		case System.TABLE_ID_COMMID_TRACE:
			return UcityConstant.getQueryByKey("System.TABLE_NAME_COMMID_TRACE");
		case System.TABLE_ID_DEVMID_SEND_STATUS:
			return UcityConstant.getQueryByKey("System.TABLE_NAME_DEVMID_SEND_STATUS");
		case System.TABLE_ID_ADAPTER_HISTORY:	
			return UcityConstant.getQueryByKey("System.TABLE_NAME_ADAPTER_HISTORY");		
		}
		return null;
	}
	
	public static int getTableId(String tableName){
		if(SmartUtil.isBlankObject(tableName)) return -1;

		if(tableName.equals(UcityConstant.getQueryByKey("System.TABLE_NAME_OPPORTAL_SITUATION"))){
			return System.TABLE_ID_OPPORTAL_SITUATION;
		}else if(tableName.equals(UcityConstant.getQueryByKey("System.TABLE_NAME_OPPORTAL_DISPLAY"))){
			return System.TABLE_ID_OPPORTAL_DISPLAY;
		}else if(tableName.equals(UcityConstant.getQueryByKey("System.TABLE_NAME_OPPORTAL_SMS"))){
			return System.TABLE_ID_OPPORTAL_SMS;
		}else if(tableName.equals(UcityConstant.getQueryByKey("System.TABLE_NAME_INTCON_SITUATION"))){
			return System.TABLE_ID_INTCON_SITUATION;
		}else if(tableName.equals(UcityConstant.getQueryByKey("System.TABLE_NAME_COMMID_TRACE"))){
			return System.TABLE_ID_COMMID_TRACE;
		}else if(tableName.equals(UcityConstant.getQueryByKey("System.TABLE_NAME_DEVMID_SEND_STATUS"))){
			return System.TABLE_ID_DEVMID_SEND_STATUS;
		}else if(tableName.equals(UcityConstant.getQueryByKey("System.TABLE_NAME_ADAPTER_HISTORY"))){	
			return System.TABLE_ID_ADAPTER_HISTORY;
		}
		return -1;
	}
	
	public static String[] getReleaseTaskNames(){
		return new String[]{ "기상특보 종료",
							"대기특보 종료",
							"수질특보 종료",
							"불법주정차 종료",
							"돌발상황 종료",
							"방범 CCTV 종료",
							"용의차량추적상황 종료",
							"상수도누수 종료",
							"시설물관리 종료",
							"화재특보 종료",
							"상황종료",
							"통합관제 종료표출",
							"종료 SMS발송",
							"미디어보드 중단",
							"교통 BIT 중단",
							"교통 VMS 중단",
							"KIOSK 중단",
							"환경 VMS 중단",
							"통신 미들웨어(종료)"
		};
	}

}
