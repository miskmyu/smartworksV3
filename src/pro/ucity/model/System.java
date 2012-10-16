package pro.ucity.model;

import net.smartworks.model.KeyMap;
import net.smartworks.util.SmartUtil;

public class System {

	public static final int MAX_PROCESS						= 10;
	public static final int PROCESS_ENV_WEAHTER				= 0;
	public static final int PROCESS_ENV_ATMOSPHERE			= 1;
	public static final int PROCESS_ENV_WATER				= 2;
	public static final int PROCESS_TRAFFIC_ILLEGAL_PARKING	= 3;
	public static final int PROCESS_TRAFFIC_INCIDENT		= 4;
	public static final int PROCESS_DISASTER_FIRE			= 5;
	public static final int PROCESS_CRIME_CCTV				= 6;
	public static final int PROCESS_CRIME_VEHICLES			= 7;
	public static final int PROCESS_WATERWORKS_LEAKS		= 8;
	public static final int PROCESS_FACILITY_MANAGEMENT		= 9;
	
	
	public static final int ID_OPERATION_PORTAL 		= 1;
	public static final int ID_INTEGRATED_CONTROL 		= 2;
	public static final int ID_COMMUNICATION_MIDDLEWARE = 3;
	public static final int ID_DEVICE_MIDDLEWARE 		= 4;
	public static final int ID_ADAPTER 					= 5;


	//dev
//	public static final String DATABASE_JDBC_DRIVE = "com.tmax.tibero.jdbc.TbDriver";
//	public static final String DATABASE_CONNECTION = "jdbc:tibero:thin:@dev.smartworks.net:8629:tibero";
//	public static final String DATABASE_USERNAME = "tibero";
//	public static final String DATABASE_PASSWORD = "tmax";
	//개발
	public static final String DATABASE_JDBC_DRIVE = "com.tmax.tibero.jdbc.TbDriver";
	public static final String DATABASE_CONNECTION = "jdbc:tibero:thin:@10.2.10.147:8629:CNUCTIDV";
	public static final String DATABASE_USERNAME = "bpmuser";
	public static final String DATABASE_PASSWORD = "bpmadmin";
	
	public static final int TABLE_ID_OPPORTAL_SITUATION 	= 11;
	public static final int TABLE_ID_OPPORTAL_DISPLAY 		= 12;
	public static final int TABLE_ID_INTCON_SITUATION 		= 21;
	public static final int TABLE_ID_COMMID_TRACE 			= 31;
	public static final int TABLE_ID_DEVMID_SEND_STATUS 	= 41;
	public static final int TABLE_ID_ADAPTER_HISTORY		= 51;	
	
	//DEV
//	public static final String TABLE_NAME_OPPORTAL_SITUATION 	    = "TH_ST_SITUATION_HISTORY";
//	public static final String TABLE_NAME_OPPORTAL_DISPLAY 		= "TN_ST_DISPLAY";
//	public static final String TABLE_NAME_INTCON_SITUATION 		= "TN_CTL_EVENT_INFO";
//	public static final String TABLE_NAME_COMMID_TRACE 			= "TB_COM_INTG_LOG";
//	public static final String TABLE_NAME_DEVMID_SEND_STATUS   	= "TN_IDM_SEND_STATUS";
//	public static final String TABLE_NAME_DEVMID_DEVICE_STATUS 	= "TN_IDM_DEVICE_STATUS";
//	public static final String TABLE_NAME_ADAPTER_HISTORY		    = "TB_UAST_CMNC_HS";
	
	//개발서버
	public static final String TABLE_NAME_OPPORTAL_SITUATION 	= "USITUATION.TH_ST_SITUATION_HISTORY";			// 업무포털(발생,접수,처리,종료)테이블
	public static final String TABLE_NAME_OPPORTAL_DISPLAY 		= "USITUATION.TN_ST_DISPLAY";				// 외부표출테이블
	public static final String TABLE_NAME_INTCON_SITUATION 		= "US1.TN_CTL_EVENT_INFO";					// 통합관제테이블
	public static final String TABLE_NAME_COMMID_TRACE 			= "TB_COM_INTG_LOG";						// 통신미들웨어테이블
	public static final String TABLE_NAME_DEVMID_SEND_STATUS 	= "TN_IDM_SEND_STATUS";							// 안씀
	public static final String TABLE_NAME_ADAPTER_HISTORY		= "TB_UAST_CMNC_HS";							// 어댑터 테이블
	public static final String TABLE_NAME_COMMID_JOIN             = "CMDB.TN_CM_EVENT_OUTB_INFO";             // 외부표출조인테이블
	public static final String TABLE_NAME_SEND_CONTENTS = "UIDB.TN_IDM_SEND_CONTENTS";						// 외부표출 시 컨텐츠정보 테이블 ( JOIN )
	public static final String TABLE_NAME_SEND_INFO = "UIDB.TN_IDM_SEND_INFO";								// 단말연계미들웨어 테이블
	public static final String TABLE_NAME_RCV_DEVICE = "UIDB.TN_IDM_CONTENTS_RCV_DEVICE";						// 단말연계미들웨어 RCV 테이블
	
	public static final String DATA_FIELD_NAME_EVENT_ID			= "Event Id";
	public static final String DATA_FIELD_NAME_TABLE_ID			= "Table Id";
	public static final String DATA_FIELD_NAME_STATUS			= "Status";
	public static final String DATA_FIELD_NAME_DISPLAY_ID		= "Display Id";
	public static final String DATA_FIELD_NAME_DEVICE_ID		= "Device Id";
	public static final String DATA_FIELD_NAME_TIMEOUT			= "Timeout Minutes";
	
	public static final long DEFAULT_TASK_TIMEOUT				= 10 * 60 * 1000;
	public static final long DEFAULT_POLLING_INTERVAL 			= 10 * 1000;
	
	public static final String TASK_FORM_NAME_USERVICE_END 		= "기상특보 종료";
	public static String getProcessId(int process){
		if(process<0 || process>MAX_PROCESS) return null;
		
		//dev
//		switch(process){
//		case PROCESS_ENV_WEAHTER:
//			return "pkg_9564849550184543b06fa46e3290f296";
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
		
		//개발서버
		switch(process){
		case PROCESS_ENV_WEAHTER:
			return "pkg_6247af34746044228556b5366752141e";
		case PROCESS_ENV_ATMOSPHERE:
			return "pkg_6247af34746044228556b5366752141e";
		case PROCESS_ENV_WATER:
			return "pkg_6247af34746044228556b5366752141e";
		case PROCESS_TRAFFIC_ILLEGAL_PARKING:
			return "pkg_6247af34746044228556b5366752141e";
		case PROCESS_TRAFFIC_INCIDENT:
			return "pkg_6247af34746044228556b5366752141e";
		case PROCESS_DISASTER_FIRE:
			return "pkg_6247af34746044228556b5366752141e";
		case PROCESS_CRIME_CCTV:
			return "pkg_6247af34746044228556b5366752141e";
		case PROCESS_CRIME_VEHICLES:
			return "pkg_6247af34746044228556b5366752141e";
		case PROCESS_WATERWORKS_LEAKS:
			return "pkg_6247af34746044228556b5366752141e";
		case PROCESS_FACILITY_MANAGEMENT:
			return "pkg_6247af34746044228556b5366752141e";
		
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
	
}
