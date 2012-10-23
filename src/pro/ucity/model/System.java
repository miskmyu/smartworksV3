package pro.ucity.model;

import net.smartworks.model.KeyMap;
import net.smartworks.util.SmartUtil;

public class System {

	public static final int MAX_PROCESS						= 10;
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
	
	
	public static final int ID_OPERATION_PORTAL 		= 1;
	public static final int ID_INTEGRATED_CONTROL 		= 2;
	public static final int ID_COMMUNICATION_MIDDLEWARE = 3;
	public static final int ID_DEVICE_MIDDLEWARE 		= 4;
	public static final int ID_ADAPTER 					= 5;


	//dev
	public static final String DATABASE_JDBC_DRIVE = "com.tmax.tibero.jdbc.TbDriver";
	public static final String DATABASE_CONNECTION = "jdbc:tibero:thin:@dev.smartworks.net:8629:tibero";
	public static final String DATABASE_USERNAME = "tibero";
	public static final String DATABASE_PASSWORD = "tmax";
	//개발
//	public static final String DATABASE_JDBC_DRIVE = "com.tmax.tibero.jdbc.TbDriver";
//	public static final String DATABASE_CONNECTION = "jdbc:tibero:thin:@10.2.10.147:8629:CNUCTIDV";
//	public static final String DATABASE_USERNAME = "bpmuser";
//	public static final String DATABASE_PASSWORD = "bpmadmin";
	
	public static final int TABLE_ID_OPPORTAL_SITUATION 	= 11;
	public static final int TABLE_ID_OPPORTAL_DISPLAY 		= 12;
	public static final int TABLE_ID_OPPORTAL_SMS 		= 13;
	public static final int TABLE_ID_INTCON_SITUATION 		= 21;
	public static final int TABLE_ID_COMMID_TRACE 			= 31;
	public static final int TABLE_ID_DEVMID_SEND_STATUS 	= 41;
	public static final int TABLE_ID_ADAPTER_HISTORY		= 51;	
	
	//DEV
	public static final String TABLE_NAME_OPPORTAL_SITUATION    = "TH_ST_SITUATION_HISTORY";
	public static final String TABLE_NAME_OPPORTAL_ST = "USITUATION.TM_ST_SITUATION";							// 업무포털(발생,접수,처리,종료)조인테이블
	public static final String TABLE_NAME_OPPORTAL_FACILITY = "CMDB.TM_CM_FACILITY_INFO";						// 시설물코드 테이블
	public static final String TABLE_NAME_OPPORTAL_DISPLAY 		= "TN_ST_DISPLAY";
	public static final String TABLE_NAME_OPPORTAL_SMS 		= "USITUATION.TM_SM_SMS_ITEM";   				// SMS 테이블
	public static final String TABLE_NAME_INTCON_SITUATION 		= "TN_CTL_EVENT_INFO";
	public static final String TABLE_NAME_COMMID_TRACE 			= "TB_COM_INTG_LOG";
	public static final String TABLE_NAME_DEVMID_SEND_STATUS   	= "TN_IDM_SEND_STATUS";
	public static final String TABLE_NAME_ADAPTER_HISTORY	    = "TB_UAST_CMNC_HS";
	public static final String TABLE_NAME_COMMID_JOIN           = "CMDB.TN_CM_EVENT_OUTB_INFO";
	public static final String TABLE_NAME_SEND_CONTENTS = "UIDB.TN_IDM_SEND_CONTENTS";						// 외부표출 시 컨텐츠정보 테이블 ( JOIN )
	public static final String TABLE_NAME_SEND_INFO = "UIDB.TN_IDM_SEND_INFO";								// 단말연계미들웨어 테이블
	public static final String TABLE_NAME_RCV_DEVICE = "UIDB.TN_IDM_CONTENTS_RCV_DEVICE";						// 단말연계미들웨어 RCV 테이블
	
	//개발서버
//	public static final String TABLE_NAME_OPPORTAL_SITUATION 	= "USITUATION.TH_ST_SITUATION_HISTORY";			// 업무포털(발생,접수,처리,종료)테이블
//	public static final String TABLE_NAME_OPPORTAL_ST = "USITUATION.TM_ST_SITUATION";							// 업무포털(발생,접수,처리,종료)조인테이블
//	public static final String TABLE_NAME_OPPORTAL_FACILITY = "CMDB.TM_CM_FACILITY_INFO";						// 시설물코드 테이블
//	public static final String TABLE_NAME_OPPORTAL_DISPLAY 		= "USITUATION.TN_ST_DISPLAY";				// 외부표출테이블
//	public static final String TABLE_NAME_OPPORTAL_SMS 		= "USITUATION.TM_SM_SMS_ITEM";   				// SMS 테이블
//	public static final String TABLE_NAME_INTCON_SITUATION 		= "US1.TN_CTL_EVENT_INFO";					// 통합관제테이블
//	public static final String TABLE_NAME_COMMID_TRACE 			= "TB_COM_INTG_LOG";						// 통신미들웨어테이블
//	public static final String TABLE_NAME_DEVMID_SEND_STATUS 	= "TN_IDM_SEND_STATUS";							// 안씀
//	public static final String TABLE_NAME_ADAPTER_HISTORY		= "TB_UAST_CMNC_HS";							// 어댑터 테이블
//	public static final String TABLE_NAME_COMMID_JOIN             = "CMDB.TN_CM_EVENT_OUTB_INFO";             // 외부표출조인테이블
//	public static final String TABLE_NAME_SEND_CONTENTS = "UIDB.TN_IDM_SEND_CONTENTS";						// 외부표출 시 컨텐츠정보 테이블 ( JOIN )
//	public static final String TABLE_NAME_SEND_INFO = "UIDB.TN_IDM_SEND_INFO";								// 단말연계미들웨어 테이블
//	public static final String TABLE_NAME_RCV_DEVICE = "UIDB.TN_IDM_CONTENTS_RCV_DEVICE";						// 단말연계미들웨어 RCV 테이블

	

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
		switch(process){
		case PROCESS_ENV_WEAHTER:
			return "pkg_9564849550184543b06fa46e3290f296";
		case PROCESS_ENV_ATMOSPHERE:
			return "pkg_9564849550184543b06fa46e3290f296";
		case PROCESS_ENV_WATER:
			return "pkg_9564849550184543b06fa46e3290f296";
		case PROCESS_TRAFFIC_ILLEGAL_PARKING:
			return "pkg_9564849550184543b06fa46e3290f296";
		case PROCESS_TRAFFIC_INCIDENT:
			return "pkg_9564849550184543b06fa46e3290f296";
		case PROCESS_DISASTER_FIRE:
			return "pkg_9564849550184543b06fa46e3290f296";
		case PROCESS_CRIME_CCTV:
			return "pkg_9564849550184543b06fa46e3290f296";
		case PROCESS_CRIME_VEHICLES:
			return "pkg_9564849550184543b06fa46e3290f296";
		case PROCESS_WATERWORKS_LEAKS:
			return "pkg_9564849550184543b06fa46e3290f296";
		case PROCESS_FACILITY_MANAGEMENT:
			return "pkg_9564849550184543b06fa46e3290f296";
		
		//개발서버
//		switch(process){
//		case PROCESS_ENV_WEAHTER:
//			return "pkg_6247af34746044228556b5366752141e";
//		case PROCESS_ENV_ATMOSPHERE:
//			return "pkg_95c0181fcb3f47429fadc3ea50f0d803";
//		case PROCESS_ENV_WATER:
//			return "pkg_116c47c171f74fcbaaa69381e8730f25";
//		case PROCESS_TRAFFIC_ILLEGAL_PARKING:
//			return "pkg_356e545e04444974bf1c07fadc073a77";
//		case PROCESS_TRAFFIC_INCIDENT:
//			return "pkg_e267443deb0b471586759d7dad83bcf4";
//		case PROCESS_DISASTER_FIRE:
//			return "pkg_492f63ad6d9b480b8535b0bdb7a2d9a9";
//		case PROCESS_CRIME_CCTV:
//			return "pkg_17bb1abf2809465fbde08db85c3103a0";
//		case PROCESS_CRIME_VEHICLES:
//			return "pkg_d6c93e05baf546c5a6901328e75a2e12";
//		case PROCESS_WATERWORKS_LEAKS:
//			return "pkg_1ef67bf9a18b4ee4bfb86d62504a1223";
//		case PROCESS_FACILITY_MANAGEMENT:
//			return "pkg_efa10156dc8445ee8a89df5e95493791";
		
		}
		
		return null;
	}

//	public static String getProcessIdByProcessStatus(int process, String status){
//		if(process<0 || process>MAX_PROCESS) return null;
//		
//		//개발서버
//		switch(process){
//		case PROCESS_ENV_WEAHTER:
//			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
//				return "pkg_48adacc27fc9443d82d5545028262ca8";
//			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
//				return "pkg_5483a6924d7d47b3951f046ea358f0d9";
//			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
//				return "pkg_83f1a038f3fc4427811d76ad9dee51e8";
//			return null;
//		case PROCESS_ENV_ATMOSPHERE:
//			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
//				return "pkg_336b0e079fc44ab19acbe49ded2e8b12";
//			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
//				return "pkg_17a57e3cea6e4127b4bf1f5bc2da3c9a";
//			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
//				return "pkg_c54deaaf347e4b3bbd437d5e9b4aef1c";
//			return null;
//		case PROCESS_ENV_WATER:
//			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
//				return "pkg_336b0e079fc44ab19acbe49ded2e8b12";
//			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
//				return "pkg_17a57e3cea6e4127b4bf1f5bc2da3c9a";
//			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
//				return "pkg_c54deaaf347e4b3bbd437d5e9b4aef1c";
//			return null;
//		case PROCESS_TRAFFIC_ILLEGAL_PARKING:
//			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
//				return "pkg_336b0e079fc44ab19acbe49ded2e8b12";
//			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
//				return "pkg_17a57e3cea6e4127b4bf1f5bc2da3c9a";
//			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
//				return "pkg_c54deaaf347e4b3bbd437d5e9b4aef1c";
//			return null;
//		case PROCESS_TRAFFIC_INCIDENT:
//			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
//				return "pkg_336b0e079fc44ab19acbe49ded2e8b12";
//			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
//				return "pkg_17a57e3cea6e4127b4bf1f5bc2da3c9a";
//			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
//				return "pkg_c54deaaf347e4b3bbd437d5e9b4aef1c";
//			return null;
//		case PROCESS_DISASTER_FIRE:
//			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
//				return "pkg_336b0e079fc44ab19acbe49ded2e8b12";
//			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
//				return "pkg_17a57e3cea6e4127b4bf1f5bc2da3c9a";
//			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
//				return "pkg_c54deaaf347e4b3bbd437d5e9b4aef1c";
//			return null;
//		case PROCESS_CRIME_CCTV:
//			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
//				return "pkg_336b0e079fc44ab19acbe49ded2e8b12";
//			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
//				return "pkg_17a57e3cea6e4127b4bf1f5bc2da3c9a";
//			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
//				return "pkg_c54deaaf347e4b3bbd437d5e9b4aef1c";
//			return null;
//		case PROCESS_CRIME_VEHICLES:
//			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
//				return "pkg_244eb0f1593f4ee8bb6b9c2ef6a028f7";
//			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
//				return "pkg_ec5ddaa7b06c41e5a5b64fda54b23c64";
//			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
//				return "pkg_04a736259d534583b319d2c546d407d6";
//			return null;
//		case PROCESS_WATERWORKS_LEAKS:
//			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
//				return "pkg_336b0e079fc44ab19acbe49ded2e8b12";
//			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
//				return "pkg_17a57e3cea6e4127b4bf1f5bc2da3c9a";
//			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
//				return "pkg_c54deaaf347e4b3bbd437d5e9b4aef1c";
//			return null;
//		case PROCESS_FACILITY_MANAGEMENT:
//			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
//				return "pkg_336b0e079fc44ab19acbe49ded2e8b12";
//			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
//				return "pkg_17a57e3cea6e4127b4bf1f5bc2da3c9a";
//			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
//				return "pkg_c54deaaf347e4b3bbd437d5e9b4aef1c";
//			return null;		
//		}
//		
//		return null;
//	}
	
	public static String getManualProcessId(String userviceCode, String serviceCode, String eventCode, String status){
		if(SmartUtil.isBlankObject(userviceCode) || SmartUtil.isBlankObject(serviceCode) || SmartUtil.isBlankObject(eventCode) || SmartUtil.isBlankObject(status)) return null;
		
		if(userviceCode.equals(Service.USERVICE_CODE_ENVIRONMENT) && serviceCode.equals("091") && eventCode.equals("11")){
			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
				return "pkg_22b8cf03a873499ea6bb7716684c5664";
			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
				return "pkg_d416790a8da24ed29879d351658deeeb";
			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
				return "pkg_7a5217318a8c4aeeb264c0e2858a55cb";
			return null;//호우
		}else if(userviceCode.equals(Service.USERVICE_CODE_SECURITY) && serviceCode.equals("092") && eventCode.equals("13")){
			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
				return "pkg_ccb7b36f208a43b6bc6df38fd60eb4e1";
			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
				return "pkg_94ec50d57eec499493c17a04279a1bd9";
			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
				return "pkg_94ec50d57eec499493c17a04279a1bd9";
			return null;//태풍
		}else if(userviceCode.equals(Service.USERVICE_CODE_ENVIRONMENT) && serviceCode.equals("093")){
			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
				return "pkg_13622961d532401b8608593f8c0dc882";
			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
				return "pkg_51b060bc68c142df85597f5161462d00";
			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
				return "pkg_390ea965ae7e4036902189fbac7d6f9b";
			return null;//수질
		}else if(userviceCode.equals(Service.USERVICE_CODE_ENVIRONMENT) && serviceCode.equals("091")){
			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
				return "pkg_9e4b17a8d89648e3b054b1613de5357a";
			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
				return "pkg_0c1b1c31366d4a3ca6535ffc3508f3f7";
			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
				return "pkg_7bb19b5353b4408999657463a0c323f5";
			return null;//환경경보
		}else if(userviceCode.equals(Service.USERVICE_CODE_TRAFFIC) && serviceCode.equals("091") && eventCode.equals("11")){
			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
				return "pkg_bc7c0588d72948f998d44fc0b79f7ba9";
			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
				return "pkg_fb3812e865504c15ae63d123f45596e8";
			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
				return "pkg_a8ee1defafd643978affd123463a1cf9";
			return null;//교통사고
		}else if(userviceCode.equals(Service.USERVICE_CODE_TRAFFIC) && serviceCode.equals("091") && eventCode.equals("12")){
			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
				return "pkg_b533cdf57a6b42baa75caf8860234c80";
			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
				return "pkg_de8a8d6052274c10804cec33b2eab780";
			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
				return "pkg_59ff491e925849978f04045e39e1d4ab";
			return null;//뺑소니
		}else if(userviceCode.equals(Service.USERVICE_CODE_TRAFFIC) && serviceCode.equals("091") && eventCode.equals("13")){
			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
				return "pkg_dd7673c38efe4d929b147b238773a370";
			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
				return "pkg_aced5223874c44ebbf293b283a6161bc";
			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
				return "pkg_cffbace680fd43ce9c67cc3a14c0e86d";
			return null;//차량고장
		}else if(userviceCode.equals(Service.USERVICE_CODE_SECURITY) && serviceCode.equals("092") && eventCode.equals("12")){
			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
				return "pkg_48adacc27fc9443d82d5545028262ca8";
			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
				return "pkg_5483a6924d7d47b3951f046ea358f0d9";
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
				return "pkg_fb0f805e648c4069af427b0f157e7dcd";
			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
				return "pkg_273309006c0d463db6204a4871a364c0";
			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
				return "pkg_acd422acfaf847358b7850e34eac2ebb";
			return null;//미아
		}else if(userviceCode.equals(Service.USERVICE_CODE_SECURITY) && serviceCode.equals("091") && eventCode.equals("13")){
			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
				return "pkg_f58780073e324fe885ea6bfaf604abb0";
			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
				return "pkg_d981dea2a995430693b8e6d724dc6b48";
			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
				return "pkg_7d4676c505024a21ad7d0d632db8a657";
			return null;//응급
		}else if(userviceCode.equals(Service.USERVICE_CODE_SECURITY) && serviceCode.equals("092") && eventCode.equals("14")){
			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
				return "pkg_a0346cf5278848e88c2abb3579d96679";
			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
				return "pkg_e3801416525045928a9660acd41de1ce";
			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
				return "pkg_d52bf881f96f44a0b8368ab0f14144ad";
			return null;//지하차도침수
		}else if(userviceCode.equals(Service.USERVICE_CODE_SECURITY) && serviceCode.equals("092") && eventCode.equals("15")){
			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
				return "pkg_3840ea7a7ede4094b4292c5cc112b90d";
			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
				return "pkg_1827a67e601b4e948c19ff4faf85c04d";
			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
				return "pkg_410a0ce658c2487dbc0d24bb23fb840f";
			return null;//수위경보
		}else if(userviceCode.equals(Service.USERVICE_CODE_TRAFFIC) && serviceCode.equals("091") && eventCode.equals("15")){
			if(OPSituation.STATUS_SITUATION_OCCURRED.equals(status))
				return "pkg_863c9a442c454fde98af4f3ffb43cc26";
			else if(OPSituation.STATUS_SITUATION_PROCESSING.equals(status))
				return "pkg_6f816c0caaa844a3bb02e512d02e2951";
			else if(OPSituation.STATUS_SITUATION_RELEASE.equals(status))
				return "pkg_a7ce4192f8174a42be5e8b351a3054ad";
			return null;//교통혼잡
		}
		
		return null;
	}
	
	public static String getTableName(String tableId){
		if(SmartUtil.isBlankObject(tableId)) return null;
		int iTableId = Integer.parseInt(tableId);
		switch(iTableId){
		case System.TABLE_ID_OPPORTAL_SITUATION:
			return TABLE_NAME_OPPORTAL_SITUATION;
		case System.TABLE_ID_OPPORTAL_DISPLAY:
			return TABLE_NAME_OPPORTAL_DISPLAY;
		case System.TABLE_ID_INTCON_SITUATION:
			return TABLE_NAME_INTCON_SITUATION;
		case System.TABLE_ID_COMMID_TRACE:
			return TABLE_NAME_COMMID_TRACE;
		case System.TABLE_ID_DEVMID_SEND_STATUS:
			return TABLE_NAME_DEVMID_SEND_STATUS;
		case System.TABLE_ID_ADAPTER_HISTORY:	
			return TABLE_NAME_ADAPTER_HISTORY;		
		}
		return null;
	}
	
	public static int getTableId(String tableName){
		if(SmartUtil.isBlankObject(tableName)) return -1;

		if(tableName.equals(TABLE_NAME_OPPORTAL_SITUATION)){
			return System.TABLE_ID_OPPORTAL_SITUATION;
		}else if(tableName.equals(TABLE_NAME_OPPORTAL_DISPLAY)){
			return System.TABLE_ID_OPPORTAL_DISPLAY;
		}else if(tableName.equals(TABLE_NAME_INTCON_SITUATION)){
			return System.TABLE_ID_INTCON_SITUATION;
		}else if(tableName.equals(TABLE_NAME_COMMID_TRACE)){
			return System.TABLE_ID_COMMID_TRACE;
		}else if(tableName.equals(TABLE_NAME_DEVMID_SEND_STATUS)){
			return System.TABLE_ID_DEVMID_SEND_STATUS;
		}else if(tableName.equals(TABLE_NAME_ADAPTER_HISTORY)){	
			return System.TABLE_ID_ADAPTER_HISTORY;
		}
		return -1;
	}
	
	public static String[] getReleaseTaskNames(){
		return new String[]{ "기상특보 종료",
							"대기특보 종료",
							"수질특보 종료",
							"상수도누수 종료",
							"시설물관리 종료",
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
